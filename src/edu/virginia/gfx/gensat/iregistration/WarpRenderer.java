package edu.virginia.gfx.gensat.iregistration;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;

public class WarpRenderer implements Renderable {
	private final TextureData warpImg;
	private final Warp warp;

	private Texture warpTex;
	private Mesh2DShader shader;
	
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
		float[] total = new float[16];
		Matrix.multiplyMM(total, 0, parent, 0, warp.affine, 0);
		shader.use(gl, warp.dstVertices, warp.srcVertices, warp.triangles,
				total, warpTex, color);
	}

	@Override
	public void destroy(GL2 gl) {
		warpTex.destroy(gl);
		shader.destroy(gl);
	}

	@Override
	public void init(GL2 gl) {
		warpTex = new Texture(gl, warpImg);
		shader = new Mesh2DShader(gl);
	}
}
