package edu.virginia.gfx.gensat.iregistration;

import java.util.Arrays;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;

public class PointRenderer implements Renderable {
	private final float[] sqVert = new float[] { -0.5f, -0.5f, 0.5f, -0.5f,
			0.5f, 0.5f, -0.5f, 0.5f };
	private final float[] sqTex = new float[] { 0, 0, 1, 0, 1, 1, 0, 1 };
	private final int[] triangles = new int[] { 0, 1, 2, 0, 2, 3 };

	private final TextureData img;
	private Texture tex;
	private Mesh2DShader shader;
	public final float[] xy;
	
	public int color;
	public float radius = 0.025f;

	public PointRenderer(TextureData img, float[] xy) {
		this.img = img;
		this.xy = xy;
		this.color = 0xffffffff;
	}

	@Override
	public void render(GL2 gl, float[] parent) {
		gl.glBlendEquation(GL2.GL_ADD);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		float[] mat = new float[16];
		for (int i = 0; i < xy.length; i += 2) {
			Matrix.translateM(mat, 0, parent, 0, xy[i], xy[i + 1], 0);
			Matrix.scaleM(mat, 0, radius, radius, 1);
			shader.use(gl, sqVert, sqTex, triangles, mat, tex, color);
		}
	}

	@Override
	public void destroy(GL2 gl) {
		tex.destroy(gl);
		shader.destroy(gl);
	}

	@Override
	public void init(GL2 gl) {
		tex = new Texture(gl, img);
		shader = new Mesh2DShader(gl);
	}
}