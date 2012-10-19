package edu.virginia.gfx.gensat.iregistration.util;

import java.io.IOException;

import javax.media.opengl.GL2;
import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.texture.TextureData;

public abstract class RenderablePointSelector extends PointSelector implements
		Renderable {
	// Renderers for each type of point state
	protected final PointRenderer pointRenderer;
	protected final PointRenderer selectedPointRenderer;
	protected final PointRenderer hoverPointRenderer;

	// hold the currently hovered/selected point
	private final float[] curPointBuffer = new float[2];

	public RenderablePointSelector(float[] points, TextureData unselectedTexture,
			TextureData hoverTexture, TextureData selectedTexture,
			GLProfile profile) throws IOException {
		super(points);

		// initialize renderers
		selectedPointRenderer = new PointRenderer(selectedTexture,
				curPointBuffer);
		hoverPointRenderer = new PointRenderer(hoverTexture, curPointBuffer);
		pointRenderer = new PointRenderer(unselectedTexture, points);
	}

	@Override
	public final void render(GL2 gl, float[] parent) {
		pointRenderer.render(gl, parent);
		curPointBuffer[0] = pointBuf[getSelectedPoint() * 2 + 0];
		curPointBuffer[1] = pointBuf[getSelectedPoint() * 2 + 1];
		if (isDragging()) {
			selectedPointRenderer.render(gl, parent);
		} else {
			hoverPointRenderer.render(gl, parent);
		}
	}

	@Override
	public final void destroy(GL2 gl) {
		pointRenderer.destroy(gl);
		selectedPointRenderer.destroy(gl);
		hoverPointRenderer.destroy(gl);
	}

	@Override
	public final void init(GL2 gl) {
		pointRenderer.init(gl);
		selectedPointRenderer.init(gl);
		hoverPointRenderer.init(gl);
	}
}
