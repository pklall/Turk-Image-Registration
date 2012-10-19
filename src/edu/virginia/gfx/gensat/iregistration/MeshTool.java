package edu.virginia.gfx.gensat.iregistration;

import java.io.IOException;
import java.io.InputStream;

import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import edu.virginia.gfx.gensat.iregistration.util.InteractiveRenderable;
import edu.virginia.gfx.gensat.iregistration.util.PointRenderer;
import edu.virginia.gfx.gensat.iregistration.util.PointSelector;
import edu.virginia.gfx.gensat.iregistration.util.Renderable;

public class MeshTool extends PointSelector implements Renderable, InteractiveRenderable {
	private final Warp warp;

	// for rendering unselected points
	private PointRenderer unselectedPointRenderer;

	// for rendering selected points
	private PointRenderer selectedPointRenderer;
	private final float[] selectedPointBuffer = new float[2];

	public MeshTool(Warp warp, GLProfile profile) throws IOException {
		super(warp.dstVertices);
		setMoveOnSelect(true);

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

	@Override
	public void render(GL2 gl, float[] parent) {
		unselectedPointRenderer.render(gl, parent);
		selectedPointBuffer[0] = warp.dstVertices[getSelectedPoint() * 2 + 0];
		selectedPointBuffer[1] = warp.dstVertices[getSelectedPoint() * 2 + 1];
		if (isDragging()) {
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

	@Override
	protected void pointSelected(int p, float x, float y) {
		selectedPointRenderer.color = 0x00ff00ff; // orange
	}

	@Override
	protected void pointDragged(int p, float x, float y) {
		warp.dstVertices[p * 2 + 0] = x;
		warp.dstVertices[p * 2 + 1] = y;
	}

	@Override
	protected void pointHovered(int p, float x, float y) {
		selectedPointRenderer.color = 0xff2400ff; // orange
	}
}
