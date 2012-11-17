package edu.virginia.gfx.gensat.iregistration;

import java.util.Arrays;

/**
 * A warp is the composition of an affine transformation with a deformation
 * field specified by interpolation (via radial basis functions) between a set
 * of discrete, editable, deformation vectors on a uniform grid of a uniform
 * grid
 */
public class Warp {
	/**
	 * The horizontal resolution of the deformation field. Note that the total
	 * number of horizontal deformation field samples will be (width + 1).
	 */
	public final int width;

	/**
	 * The vertical resolution of the deformation field. Note that the total
	 * number of vertical deformation field samples will be (height + 1).
	 */
	public final int height;

	/**
	 * height x width array of warp (x, y) pairs
	 * 
	 * Warp offsets are quantized to the range [0, Short.MAX_VALUE] and stored
	 * as signed floats with 0 indicating an offset of -1 (in texture space),
	 * and Short.MAX_VALUE indicating an offset of +1 (in texture space).
	 * 
	 * Given an image, I, parameterized over [0, 1]x[0, 1] the resulting image,
	 * J, after warping is as follows:
	 * 
	 * u = warpX[y * height * width + x * width] / width
	 * 
	 * v = warpY[y * height * width + x * width] / height
	 * 
	 * J(x, y) = I(u, v)
	 * 
	 */
	public final short[] warpX;
	public final short[] warpY;

	/**
	 * @param x
	 *            Between 0 and width - 1
	 * @param y
	 *            Between 0 and height - 1
	 * @return The corresponding
	 */
	public int getWarpImgIndex(int x, int y) {
		return y * width + x;
	}

	/**
	 * Dequantizes a short in range [0, Short.MAX_VALUE] to a float in range
	 * [-1, 1]
	 */
	public static float dequantize(short value) {
		return (value / ((float) Short.MAX_VALUE) - 0.5f) * 2.0f;
	}

	/**
	 * Quantizes a float in range [-1, 1] to a short in range [0,
	 * Short.MAX_VALUE]
	 */
	public static short quantize(float value) {
		// clamp
		value = Math.max(-1.0f, value);
		value = Math.min(1.0f, value);
		return (short) (((value / 2.0f) + 0.5f) * Short.MAX_VALUE);
	}
	
	/**
	 * Clamps the value at the given (x, y) coordinates in data[] to a value
	 * which ensures mesh invertibility given it's local neighbors.
	 * 
	 * Specifically this ensures that the function f(x, y) = (x, y) + w(x, y)
	 * where w(x, y) is the R^2->R^2 function defined by this warp is monotonically
	 * increasing in both x and y.
	 * 
	 * Note that this does NOT guarantee invertibility since f(x, y) across diagonals may not be invertible
	 * 
	 * @param x
	 * @param y
	 * @param data
	 */
	public void clampToInvertible(int x, int y) {
		// smallest delta between subsequent warp samples
		float epsilon = 3 * 2.0f / Short.MAX_VALUE;
		
		float valX = dequantize(warpX[getWarpImgIndex(x, y)]);
		float strideX = 1.0f / width;
		float valXLeft = dequantize(warpX[getWarpImgIndex(Math.max(0, x - 1), y)]);
		float minX = valXLeft - strideX + epsilon;
		float valXRight = dequantize(warpX[getWarpImgIndex(Math.min(width - 1, x + 1), y)]);
		float maxX = valXRight + strideX - epsilon;
		valX = (float) Math.max(minX, valX);
		valX = (float) Math.min(maxX, valX);
		warpX[getWarpImgIndex(x, y)] = quantize(valX);
		
		float valY = dequantize(warpY[getWarpImgIndex(x, y)]);
		float strideY = (float) (1.0f / height);
		float valYTop = dequantize(warpY[getWarpImgIndex(x, Math.max(0, y - 1))]);
		float minY = valYTop - strideY + epsilon;
		float valYBottom = dequantize(warpY[getWarpImgIndex(x, Math.min(height - 1, y + 1))]);
		float maxY = valYBottom + strideY - epsilon;
		valY = (float) Math.max(minY, valY);
		valY = (float) Math.min(maxY, valY);
		warpY[getWarpImgIndex(x, y)] = quantize(valY);
	}

	private float gauss(float t, float sigma) {
		return (float) Math.exp(-(t * t) / (2 * sigma * sigma));
	}

	public static interface Operator {
		/**
		 * Operate on a float value in range [-1, 1] with weight in range [0,
		 * 1].
		 */
		public float operate(float value, float weight);
	}

	private void operateGauss(float cx, float cy, float sigma, Operator o,
			short[] data) {
		// we'll consider the relevant domain to be [sx, sx + wx]x[sy, sy + wy]
		float radx = sigma * 3 * width;
		float rady = sigma * 3 * height;
		float minx = cx * width - radx;
		float miny = cy * height - rady;
		float maxx = cx * width + radx;
		float maxy = cy * height + rady;
		minx = Math.max(minx, 0.0f);
		miny = Math.max(miny, 0.0f);
		maxx = Math.min(maxx, width);
		maxy = Math.min(maxy, height);

		for (float x = minx; x < maxx; x += 1.0f) {
			for (float y = miny; y < maxy; y += 1.0f) {
				// the (x, y) indices into the data matrix
				int ix = (int) (x);
				int iy = (int) (y);
				// the location of the indices relative to the center of the
				// gaussian
				float ixw = (float) (ix - cx * width) / (float) width;
				float iyw = (float) (iy - cy * height) / (float) height;
				// evaluate the gaussian function here
				float gval = gauss((float) Math.sqrt(ixw * ixw + iyw * iyw),
						sigma);
				int index = getWarpImgIndex(ix, iy);
				data[index] = (short) quantize(o.operate(
						dequantize(data[index]), gval));
				clampToInvertible(ix, iy);
			}
		}
	}

	/**
	 * Performs a weighted operation on both the x and y components of the warp,
	 * independently, weightec according to a radial gaussian function centered
	 * at (cx, cy) with standard deviation, sigma.
	 */
	public void operateGauss(float cx, float cy, float sigma, Operator ox,
			Operator oy) {
		operateGauss(cx, cy, sigma, ox, warpX);
		operateGauss(cx, cy, sigma, oy, warpY);
	}

	public interface RawOperator {
		public void operate(float weight, int x, int y, short[] data);
	}

	public void operateGaussRaw(float cx, float cy, float sigma, RawOperator o,
			short[] data) {
		// we'll consider the relevant domain to be [sx, sx + wx]x[sy, sy + wy]
		float radx = sigma * 3 * width;
		float rady = sigma * 3 * height;
		float minx = cx * width - radx;
		float miny = cy * height - rady;
		float maxx = cx * width + radx;
		float maxy = cy * height + rady;
		minx = Math.max(minx, 0.0f);
		miny = Math.max(miny, 0.0f);
		maxx = Math.min(maxx, width);
		maxy = Math.min(maxy, height);

		for (float x = minx; x < maxx; x += 1.0f) {
			for (float y = miny; y < maxy; y += 1.0f) {
				// the (x, y) indices into the data matrix
				int ix = (int) (x);
				int iy = (int) (y);
				// the location of the indices relative to the center of the
				// gaussian
				float ixw = (float) (ix - cx * width) / (float) width;
				float iyw = (float) (iy - cy * height) / (float) height;
				// evaluate the gaussian function here
				float gval = gauss((float) Math.sqrt(ixw * ixw + iyw * iyw),
						sigma);
				o.operate(gval, ix, iy, data);
			}
		}
	}

	public void operateGaussRaw(float cx, float cy, float sigma,
			RawOperator rox, RawOperator roy) {
		operateGaussRaw(cx, cy, sigma, rox, warpX);
		operateGaussRaw(cx, cy, sigma, roy, warpY);
	}

	/**
	 * Constructor
	 * 
	 * @param vertices
	 *            A buffer of point data with (x, y) pairs packed together
	 * @param triangles
	 *            An "index buffer" into vertices suitable for use with
	 *            glDrawElements(GL_TRIANGLES)
	 */
	public Warp(int width, int height) {
		this.width = width;
		this.height = height;
		warpX = new short[height * width];
		warpY = new short[height * width];
		Arrays.fill(warpX, (short) (Short.MAX_VALUE / 2));
		Arrays.fill(warpY, (short) (Short.MAX_VALUE / 2));
	}
}
