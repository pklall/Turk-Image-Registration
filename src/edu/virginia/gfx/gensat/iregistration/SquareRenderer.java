package edu.virginia.gfx.gensat.iregistration;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;

public class SquareRenderer implements Renderable {
	private final float[] sqVert = new float[]{
		0, 0, 1, 0, 1, 1, 0, 1
	};
	private final float[] sqTex = new float[]{
		0, 0, 1, 0, 1, 1, 0, 1
	};
	private final int[] triangles = new int[]{
		0, 1, 2, 0, 2, 3
	};
	
	private final TextureData img;
	private Mesh2DShader shader;
	private Texture tex;

	public SquareRenderer(TextureData img) {
		this.img = img;
	}

	@Override
	public void render(GL2 gl, float[] parent) {
		shader.use(gl, sqVert, sqTex, triangles, parent, tex, 0xffffffff);
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