package edu.virginia.gfx.gensat.iregistration;

import java.io.InputStream;
import java.io.OutputStream;

import com.thoughtworks.xstream.XStream;

import edu.virginia.gfx.gensat.iregistration.util.Matrix;

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
	 * Given an image, I, parameterized over [0, 1]x[0, 1] the
	 * resulting image, J, after warping is as follows:
	 * 
	 * u = warpX[y * height * width + x * width] / width
	 * v = warpY[y * height * width + x * width] / height
	 * J(x, y) = I(u, v)
	 * 
	 */
	public final short[] warpX;
	public final short[] warpY;
	
	/**
	 * @param x Between 0 and width - 1
	 * @param y Between 0 and height - 1
	 * @return The corresponding 
	 */
	public int getWarpImgIndex(int x, int y) {
		return y * width + x;
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
	}
}
