package edu.virginia.gfx.gensat.iregistration;

import java.nio.ShortBuffer;

import javax.media.opengl.GL2;

public class WarpGL {
	private final Warp warp;
	private int x;
	private int y;

	private int createTextureLum16(GL2 gl, int width, int height) {
		int[] intBuf = new int[1];
		gl.glGenTextures(1, intBuf, 0);
		int tex = intBuf[0];
		gl.glBindTexture(GL2.GL_TEXTURE_2D, tex);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_LINEAR);

		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_CLAMP);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_CLAMP);

		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_R16, width, height, 0,
				GL2.GL_RED, GL2.GL_SHORT, null);

		return tex;
	}

	private void initTextures(GL2 gl) {
		x = createTextureLum16(gl, warp.width, warp.height);
		y = createTextureLum16(gl, warp.width, warp.height);
	}

	public void bindX(GL2 gl) {
		gl.glBindTexture(GL2.GL_TEXTURE_2D, x);
		gl.glTexSubImage2D(GL2.GL_TEXTURE_2D, 0, 0, 0, warp.width, warp.height,
				GL2.GL_RED, GL2.GL_SHORT, ShortBuffer.wrap(warp.warpX));
	}

	public void bindY(GL2 gl) {
		gl.glBindTexture(GL2.GL_TEXTURE_2D, y);
		gl.glTexSubImage2D(GL2.GL_TEXTURE_2D, 0, 0, 0, warp.width, warp.height,
				GL2.GL_RED, GL2.GL_SHORT, ShortBuffer.wrap(warp.warpY));
	}

	public WarpGL(Warp warp, GL2 gl) {
		this.warp = warp;
		initTextures(gl);
	}
}
