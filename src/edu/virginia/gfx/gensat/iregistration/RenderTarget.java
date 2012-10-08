package edu.virginia.gfx.gensat.iregistration;

import javax.media.opengl.GL2;

/**
 * Manages GLES framebuffer life-cycle.
 */
public class RenderTarget {
	public final int width;
	public final int height;
	private int framebuffer;
	private int texture;

	public int getTexture() {
		return texture;
	}

	public int getFramebuffer() {
		return framebuffer;
	}

	public RenderTarget(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void destroy(GL2 gl) {
		gl.glDeleteTextures(1, new int[] { texture }, 0);
		gl.glDeleteFramebuffers(1, new int[] { framebuffer }, 0);
	}

	public void init(GL2 gl) {
		int[] handleBuffer = new int[1];

		gl.glGenFramebuffers(1, handleBuffer, 0);
		framebuffer = handleBuffer[0];
		gl.glBindFramebuffer(gl.GL_FRAMEBUFFER, framebuffer);

		gl.glGenTextures(1, handleBuffer, 0);
		texture = handleBuffer[0];
		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);

		System.out.println(String.format(
				"Allocating a render buffer of size %d x %d", width, height));
		gl.glTexImage2D(gl.GL_TEXTURE_2D, 0, GL2.GL_RGB, width, height, 0,
				GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, null);

		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_NEAREST);
		// Use a min filter so we can use these for anti-aliasing
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_LINEAR);

		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_CLAMP_TO_EDGE);

		gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT0,
				GL2.GL_TEXTURE_2D, texture, 0);

		int status = gl.glCheckFramebufferStatus(GL2.GL_FRAMEBUFFER);
		switch (status) {
		case GL2.GL_FRAMEBUFFER_COMPLETE:
			break;
		case GL2.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
			System.err.println("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
			break;
		case GL2.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
			System.err.println("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
			break;
		case GL2.GL_FRAMEBUFFER_UNSUPPORTED:
			System.err.println("GL_FRAMEBUFFER_UNSUPPORTED");
			break;
		}
	}

	// We'll need this for 
	/*
	 * public Buffer getPixels(GL2 gl) { gl.glBindTexture(GL2.GL_TEXTURE0,
	 * texture); gl.glEnable(GL2.GL_TEXTURE0); // gl.glGetTextureImageEXT(arg0,
	 * arg1, arg2, arg3, arg4, arg5) }
	 */
}
