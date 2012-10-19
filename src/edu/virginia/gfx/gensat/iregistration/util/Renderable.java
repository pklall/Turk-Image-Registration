package edu.virginia.gfx.gensat.iregistration.util;

import javax.media.opengl.GL2;

public interface Renderable {
	public void render(GL2 gl, float[] parentTransform);

	public void destroy(GL2 gl);

	public void init(GL2 gl);
}
