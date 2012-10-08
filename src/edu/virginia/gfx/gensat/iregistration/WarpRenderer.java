package edu.virginia.gfx.gensat.iregistration;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;

public class WarpRenderer implements Renderable {
	private final TextureData warpImg;
	private final Warp warp;

	private Texture warpTex;
	private Mesh2DShader shader;

	public WarpRenderer(TextureData warpImg, Warp warp) {
		this.warpImg = warpImg;
		this.warp = warp;
	}

	@Override
	public void render(GL2 gl, float[] parent) {
		System.out.println("Drawing warp");
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glDisable(GL2.GL_DEPTH_TEST);
		
		float[] total = new float[16];
		Matrix.multiplyMM(total, 0, parent, 0, warp.affine, 0);
		System.out.println("parent = " + Arrays.toString(parent));
		System.out.println("affine = " + Arrays.toString(warp.affine));
		System.out.println("total = " + Arrays.toString(total));
		shader.use(gl, warp.dstVertices, warp.srcVertices, warp.triangles,
				total, warpTex, 0xffffffff);
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
