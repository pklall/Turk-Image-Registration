package edu.virginia.gfx.gensat.iregistration;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;

import edu.virginia.gfx.gensat.iregistration.util.Matrix;
import edu.virginia.gfx.gensat.iregistration.util.Mesh2DShader;
import edu.virginia.gfx.gensat.iregistration.util.Renderable;

public class WarpRenderer implements Renderable {
	private TextureData warpImg;
	private Warp warp;
	private WarpGL warpGL;

	private Texture warpTex;
	private WarpShader shader;

	private int color = 0xffffffff;

	public void setAlpha(int alpha) {
		color = 0xffffff00 + (alpha & 0x000000ff);
	}

	public WarpRenderer(TextureData warpImg, Warp warp) {
		this.warpImg = warpImg;
		this.warp = warp;
	}

	@Override
	public void render(GL2 gl, float[] parent) {
		shader.use(gl, parent, warpGL, warpTex, color);
	}

	@Override
	public void destroy(GL2 gl) {
		warpTex.destroy(gl);
		shader.destroy(gl);
	}

	@Override
	public void init(GL2 gl) {
		warpTex = new Texture(gl, warpImg);
		shader = new WarpShader(gl);
		warpGL = new WarpGL(warp, gl);
	}
}
