package edu.virginia.gfx.gensat.iregistration;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import edu.virginia.gfx.gensat.iregistration.Warp.Operator;
import edu.virginia.gfx.gensat.iregistration.Warp.RawOperator;
import edu.virginia.gfx.gensat.iregistration.util.InteractiveRenderable;
import edu.virginia.gfx.gensat.iregistration.util.Matrix;
import edu.virginia.gfx.gensat.iregistration.util.PointRenderer;

public class MeshTool implements InteractiveRenderable {
	private final Warp warp;

	private final PointRenderer pointRenderer;

	public enum ToolMode {
		STRETCH, ERASE, SMOOTH
	}

	private ToolMode toolMode = ToolMode.STRETCH;

	public void setToolMode(ToolMode mode) {
		this.toolMode = mode;
	}

	private float strength = 1.0f;
	private static final float STRENGTH_MIN = 0.0f;
	private static final float STRENGTH_MAX = 1.0f;

	private float radius = 0.05f;
	private static final float RADIUS_DELTA = 0.01f;
	private static final float RADIUS_MIN = 0.01f;
	private static final float RADIUS_MAX = 0.20f;

	private static final int POINTER_COLOR = 0x0000ff00;

	private final float[] mouse;

	public MeshTool(Warp warp, GLProfile profile) {
		this.warp = warp;

		InputStream hollowStream = MeshTool.class
				.getResourceAsStream("/circle_hollow.png");
		try {
			TextureData hollow = AWTTextureIO.newTextureData(profile,
					hollowStream, true, "png");
		} catch (Exception e) {

		}

		InputStream solidStream = MeshTool.class
				.getResourceAsStream("/circle_full.png");
		TextureData solid;
		try {
			solid = AWTTextureIO.newTextureData(profile, solidStream, true,
					"png");
		} catch (Exception e) {
			solid = AWTTextureIO.newTextureData(profile, new BufferedImage(0,
					0, BufferedImage.TYPE_BYTE_GRAY), false);
		}

		mouse = new float[2];
		pointRenderer = new PointRenderer(new TextureData[] { solid }, mouse,
				0, POINTER_COLOR);
	}

	private final float[] tmpMat = new float[16];

	private void mouseToMeshSpace(float mx, float my, float[] parentMat,
			float[] result) {
		synchronized (tmpMat) {
			Matrix.invertM(tmpMat, 0, parentMat, 0);
			result[0] = mx;
			result[1] = my;
			result[2] = 0;
			result[3] = 1;
			Matrix.multiplyMV(result, 0, tmpMat, 0, result, 0);
		}
	}

	private boolean dragging = false;
	private float[] prevMouse = new float[4];
	private float[] curMouse = new float[4];

	@Override
	public void mouseDown(float mx, float my, int buttons, float[] mat) {
		dragging = true;
		switch (buttons) {
		case 1:
			setToolMode(ToolMode.STRETCH);
			break;
		case 2:
			setToolMode(ToolMode.ERASE);
			break;
		case 3:
			setToolMode(ToolMode.SMOOTH);
			break;
		}
		System.out.println("mouse down: " + buttons);
	}

	@Override
	public void mouseUp(float mx, float my, int buttons, float[] mat) {
		dragging = false;
	}

	@Override
	public void mouseMove(float mx, float my, float[] mat) {
		mouseToMeshSpace(mx, my, mat, curMouse);
		mouse[0] = curMouse[0];
		mouse[1] = curMouse[1];
		if (dragging) {
			final float dx = curMouse[0] - prevMouse[0];
			final float dy = curMouse[1] - prevMouse[1];
			switch (toolMode) {
			case STRETCH:
				warp.operateGauss(curMouse[0], curMouse[1], radius,
						new Operator() {
							public float operate(float value, float weight) {
								weight *= strength;
								return value - weight * dx;
							}
						}, new Operator() {
							public float operate(float value, float weight) {
								weight *= strength;
								return value - weight * dy;
							}
						});
				break;
			case ERASE:
				warp.operateGauss(curMouse[0], curMouse[1], radius,
						new Operator() {
							public float operate(float value, float weight) {
								weight *= strength;
								return (1.0f - weight) * value;
							}
						}, new Operator() {
							public float operate(float value, float weight) {
								weight *= strength;
								return (1.0f - weight) * value;
							}
						});
				break;
			case SMOOTH:
				RawOperator avgOperator = new RawOperator() {
					public void operate(float weight, int x, int y, short[] data) {
						weight *= strength;
						// convolution with gaussian kernel evaluated at (x, y)
						// ugly hack to modify value from closure
						final float[] totalSum = new float[] { 0.0f };
						final float[] totalPossible = new float[] { 0.0f };
						warp.operateGaussRaw((float) x / warp.width, (float) y
								/ warp.height, radius, new RawOperator() {
							@Override
							public void operate(float weight, int x, int y,
									short[] data) {
								totalSum[0] += weight
										* Warp.dequantize(data[warp
												.getWarpImgIndex(x, y)]);
								totalPossible[0] += weight;
							}
						}, data);
						float convTot = totalSum[0] / totalPossible[0];
						int index = warp.getWarpImgIndex(x, y);
						float original = Warp.dequantize(data[index]);
						float fin = convTot * weight + (1.0f - weight)
								* original;
						data[index] = Warp.quantize(fin);
					}
				};
				warp.operateGaussRaw(curMouse[0], curMouse[1], radius,
						avgOperator, avgOperator);
				break;
			}
		}
		float[] tmp = prevMouse;
		prevMouse = curMouse;
		curMouse = tmp;

	}

	@Override
	public void render(GL2 gl, float[] parent) {
		pointRenderer.radius = radius * 3;
		pointRenderer.color[0] = POINTER_COLOR
				+ (int) (strength * 255.0f / 2.0f + 255.0f / 16.0f);
		pointRenderer.render(gl, parent);
	}

	@Override
	public void destroy(GL2 gl) {
		pointRenderer.destroy(gl);
	}

	@Override
	public void init(GL2 gl) {
		pointRenderer.init(gl);
	}

	public void incRadius(int amount) {
		radius += RADIUS_DELTA * amount;
		radius = Math.min(RADIUS_MAX, radius);
		radius = Math.max(RADIUS_MIN, radius);
	}

	/**
	 * @param s
	 *            Float in range [0, 1]
	 */
	public void setStrength(float s) {
		strength = s;
		strength = Math.min(STRENGTH_MAX, strength);
		strength = Math.max(STRENGTH_MIN, strength);
	}

	/**
	 * @param r
	 *            Float in range [0, 1]
	 */
	public void setRadius(float r) {
		radius = (RADIUS_MAX - RADIUS_MIN) * r + RADIUS_MIN;
		radius = Math.min(RADIUS_MAX, radius);
		radius = Math.max(RADIUS_MIN, radius);
	}
}
