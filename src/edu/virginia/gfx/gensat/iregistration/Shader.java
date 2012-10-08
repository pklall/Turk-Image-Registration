package edu.virginia.gfx.gensat.iregistration;

import java.nio.IntBuffer;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.glsl.ShaderUtil;
import com.jogamp.opengl.util.GLBuffers;

/**
 * Handles the life-cycle of an OpenGL ES 2.0 Program (both the vertex and
 * fragment shaders).
 */
public abstract class Shader {
	private String[] vertex;
	private String[] fragment;

	private int vertexShader;
	private int fragmentShader;
	protected int program;

	public Shader(GL2 gl, String[] vertex, String[] fragment) {
		this.vertex = vertex;
		this.fragment = fragment;
		init(gl);
	}

	protected abstract void initShaderParams(GL2 gl);

	private void init(GL2 gl) {
		IntBuffer retInt = GLBuffers.newDirectIntBuffer(1);
		retInt.rewind();
		ShaderUtil.createAndCompileShader(gl.getGL(), retInt,
				GL2.GL_VERTEX_SHADER, new CharSequence[][] { vertex },
				System.out);
		vertexShader = retInt.get(0);
		retInt.rewind();
		ShaderUtil.createAndCompileShader(gl.getGL(), retInt,
				GL2.GL_FRAGMENT_SHADER, new CharSequence[][] { fragment },
				System.out);
		fragmentShader = retInt.get(0);
		
		program = gl.glCreateProgram();
		gl.glAttachShader(program, vertexShader);
		gl.glAttachShader(program, fragmentShader);
		gl.glLinkProgram(program); // Check the link status
		retInt.rewind();
		gl.glGetProgramiv(program, gl.GL_LINK_STATUS, retInt);
		if (retInt.get(0) == 0) {
			gl.glDeleteProgram(program);
		}
		initShaderParams(gl);
	}
	
	public final void destroy(GL2 gl) {
		gl.glDeleteShader(vertexShader);
		gl.glDeleteShader(fragmentShader);
		gl.glDeleteProgram(program);
	}
}
