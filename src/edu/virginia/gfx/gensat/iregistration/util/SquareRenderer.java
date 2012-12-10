package edu.virginia.gfx.gensat.iregistration.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;

public class SquareRenderer implements Renderable {
	private static final float[] sqVert = new float[] { 0, 0, 1, 0, 1, 1, 0, 1 };
	private static final float[] sqTex = new float[] { 0, 0, 1, 0, 1, 1, 0, 1 };
	private static final int[] triangles = new int[] { 0, 1, 2, 0, 2, 3 };

	private static FloatBuffer sqVertBuf;
	private static FloatBuffer sqTexBuf;
	private static IntBuffer trianglesBuf;

	public static FloatBuffer getSquareVert() {
		return sqVertBuf;
	}

	public static FloatBuffer getSquareTex() {
		return sqTexBuf;
	}

	public static IntBuffer getSquareTriangles() {
		return trianglesBuf;
	}

	{
		if (sqVertBuf == null) {
			ByteBuffer buf = ByteBuffer.allocateDirect(sqVert.length * 4);
			buf.order(ByteOrder.nativeOrder());
			sqVertBuf = buf.asFloatBuffer();
			sqVertBuf.put(sqVert);
			sqVertBuf.rewind();
		}
		if (sqTexBuf == null) {
			ByteBuffer buf = ByteBuffer.allocateDirect(sqTex.length * 4);
			buf.order(ByteOrder.nativeOrder());
			sqTexBuf = buf.asFloatBuffer();
			sqTexBuf.put(sqTex);
			sqTexBuf.rewind();
		}
		if (trianglesBuf == null) {
			ByteBuffer buf = ByteBuffer.allocateDirect(triangles.length * 4);
			buf.order(ByteOrder.nativeOrder());
			trianglesBuf = buf.asIntBuffer();
			trianglesBuf.put(triangles);
			trianglesBuf.rewind();
		}
	}

	private final TextureData img;
	private Mesh2DShader shader;
	private Texture tex;

	public SquareRenderer(TextureData img) {
		this.img = img;
	}

	@Override
	public void render(GL2 gl, float[] parent) {
		shader.use(gl, sqVertBuf, sqTexBuf, trianglesBuf, parent, tex,
				0xffffffff);
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