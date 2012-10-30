package edu.virginia.gfx.gensat.iregistration;

import java.io.IOException;
import java.io.InputStream;

import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import edu.virginia.gfx.gensat.iregistration.util.InteractiveRenderable;
import edu.virginia.gfx.gensat.iregistration.util.Matrix;
import edu.virginia.gfx.gensat.iregistration.util.PointRenderer;
import edu.virginia.gfx.gensat.iregistration.util.PointSelector;
import edu.virginia.gfx.gensat.iregistration.util.PointSelector.PointSelectorEventListener;
import edu.virginia.gfx.gensat.iregistration.util.PointSelectorEvent;
import edu.virginia.gfx.gensat.iregistration.util.PointSelectorEvent.PointState;

public class MeshTool implements InteractiveRenderable {
	private final Warp warp;

	private final PointRenderer pointRenderer;

	private float radius = 0.05f;
	private float radiusDelta = 0.01f;
	private float minRadius = 0.01f;
	private float maxRadius = 0.10f;

	private final float[] mouse = new float[2];

	public MeshTool(Warp warp, GLProfile profile) throws IOException {
		this.warp = warp;

		InputStream hollowStream = MeshTool.class
				.getResourceAsStream("/circle_hollow.png");
		TextureData hollow = AWTTextureIO.newTextureData(profile, hollowStream,
				true, "png");

		InputStream solidStream = MeshTool.class
				.getResourceAsStream("/circle_full.png");
		TextureData solid = AWTTextureIO.newTextureData(profile, solidStream,
				true, "png");

		this.pointRenderer = new PointRenderer(new TextureData[] { hollow,
				solid }, mouse, 0, 0x0000ffff);
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
		System.out.println("mouse down");
	}

	@Override
	public void mouseUp(float mx, float my, int buttons, float[] mat) {
		dragging = false;
	}

	@Override
	public void mouseMove(float mx, float my, float[] mat) {
		mouseToMeshSpace(mx, my, mat, curMouse);
		if (dragging) {
			int x = (int) (warp.width * curMouse[0]);
			int y = (int) (warp.width * curMouse[1]);
			float dx = curMouse[0] - prevMouse[0];
			float dy = curMouse[1] - prevMouse[1];
			warp.warpX[warp.getWarpImgIndex(x, y)] -= 65535 * dx / 2;
			warp.warpY[warp.getWarpImgIndex(x, y)] -= 65535 * dy / 2;
		}
		float[] tmp = prevMouse;
		prevMouse = curMouse;
		curMouse = tmp;
	}

	@Override
	public void render(GL2 gl, float[] parent) {
		// do nothing
	}

	@Override
	public void destroy(GL2 gl) {
		pointRenderer.destroy(gl);
	}

	@Override
	public void init(GL2 gl) {
		pointRenderer.init(gl);
	}
}
