package edu.virginia.gfx.gensat.iregistration;

import java.io.IOException;
import java.io.InputStream;

import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class MeshTool implements WarpEditorTool {
	private final Warp warp;

	// for rendering unselected points
	private PointRenderer unselectedPointRenderer;

	// for rendering selected points
	private PointRenderer selectedPointRenderer;
	private final float[] selectedPointBuffer = new float[2];

	/**
	 * The index of the currently-selected/hovered point.
	 */
	private int selectedPoint = 0;

	/**
	 * True if we are currently moving the selected point
	 */
	private boolean moving = false;

	public MeshTool(Warp warp, GLProfile profile) throws IOException {
		this.warp = warp;
		// load texture data
		InputStream solidStream = getClass().getResourceAsStream(
				"/circle_full.png");
		TextureData solid = AWTTextureIO.newTextureData(profile, solidStream,
				false, "png");

		InputStream hollowStream = getClass().getResourceAsStream(
				"/circle_hollow.png");
		TextureData hollow = AWTTextureIO.newTextureData(profile, hollowStream,
				false, "png");

		// initialize renderers
		selectedPointRenderer = new PointRenderer(solid, selectedPointBuffer);
		unselectedPointRenderer = new PointRenderer(hollow, warp.dstVertices);
		unselectedPointRenderer.color = 0x0000ffff;
	}

	protected void selectPoint(int index) {
		selectedPoint = index;
	}

	private void screenSpaceToWarpSpace(float[] xyzw, float[] mat) {
		float[] totMat = new float[16];
		float[] invMat = new float[16];
		Matrix.multiplyMM(totMat, 0, warp.affine, 0, mat, 0);
		Matrix.invertM(invMat, 0, totMat, 0);
		Matrix.multiplyMV(xyzw, 0, invMat, 0, xyzw, 0);
	}

	@Override
	public void mouseDown(float mx, float my, int buttons, float[] mat) {
		moving = true;
		mouseMove(mx, my, mat);
	}

	@Override
	public void mouseUp(float mx, float my, int buttons, float[] mat) {
		moving = false;
	}

	public void mouseMove(float mx, float my, float[] mat) {
		// transform mouse coordinates from screen space to source-warp space
		// total matrix
		float[] xyzw = new float[4];
		xyzw[0] = mx;
		xyzw[1] = my;
		xyzw[2] = 1;
		xyzw[3] = 1;
		screenSpaceToWarpSpace(xyzw, mat);
		mx = xyzw[0];
		my = xyzw[1];

		if (moving) {
			warp.dstVertices[selectedPoint * 2 + 0] = mx;
			warp.dstVertices[selectedPoint * 2 + 1] = my;
		}

		// find the closest point to the mouse cursor
		float closestDistance = Float.MAX_VALUE;
		int closestPointIndex = 0;
		for (int i = 0; i < warp.dstVertices.length / 2; i++) {
			float x = warp.dstVertices[i * 2];
			float y = warp.dstVertices[i * 2 + 1];
			// compute distance in screen space
			float dx = mx - x;
			float dy = my - y;
			float dist = dx * dx + dy * dy;
			if (dist < closestDistance) {
				closestDistance = dist;
				closestPointIndex = i;
			}
		}
		selectedPoint = closestPointIndex;
	}

	@Override
	public void render(GL2 gl, float[] parent) {
		unselectedPointRenderer.render(gl, parent);
		selectedPointBuffer[0] = warp.dstVertices[selectedPoint * 2 + 0];
		selectedPointBuffer[1] = warp.dstVertices[selectedPoint * 2 + 1];
		if (moving) {
			selectedPointRenderer.color = 0x00ff00ff;
		} else {
			selectedPointRenderer.color = 0xCC3232ff;
		}
		selectedPointRenderer.render(gl, parent);
	}

	@Override
	public void destroy(GL2 gl) {
		unselectedPointRenderer.destroy(gl);
		selectedPointRenderer.destroy(gl);
	}

	@Override
	public void init(GL2 gl) {
		unselectedPointRenderer.init(gl);
		selectedPointRenderer.init(gl);
	}
}
