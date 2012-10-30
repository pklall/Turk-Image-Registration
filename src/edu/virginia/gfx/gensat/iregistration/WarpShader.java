package edu.virginia.gfx.gensat.iregistration;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;

import edu.virginia.gfx.gensat.iregistration.util.Shader;

/**
 * A shader for rendering textured 2D triangle meshes
 */
public class WarpShader extends Shader {
	private final float[] sqVert = new float[] { 0, 0, 1, 0, 1, 1, 0, 1 };
	private final float[] sqTex = new float[] { 0, 0, 1, 0, 1, 1, 0, 1 };
	private final int[] triangles = new int[] { 0, 1, 2, 0, 2, 3 };

	private static final String[] VERTEX = new String[] { "", //
			"uniform mat4 uVertMatrix;",//
			"attribute vec2 aVert;",//
			"attribute vec2 aTexCoord;",//
			"varying vec3 vTexCoord;",//
			"void main() {",//
			"	gl_Position = uVertMatrix * vec4(aVert, 1.0, 1.0);",//
			"	vTexCoord = vec3(aTexCoord, 1.0);",//
			"}" };

	private static final String[] FRAGMENT = new String[] {
			"uniform sampler2D uWarpX; ",//
			"uniform sampler2D uWarpY; ",//
			"uniform sampler2D uTex; ",//
			"varying vec3 vTexCoord; ",//
			"uniform mat3 uTexMatrix; ",//
			"uniform vec4 uColor; ",//
			"void main() { ",//
			"   vec3 delta; ",//
			"   delta.x = (texture2D(uWarpX, vTexCoord).r - 0.5) * 2.0; ",//
			"   delta.y = (texture2D(uWarpY, vTexCoord).r - 0.5) * 2.0; ",//
			"   delta.z = 0.0; ",//
			// "   gl_FragColor = vec4(uTexMatrix * delta, 1.0) * uColor; ",//
			"   gl_FragColor = texture2D(uTex, uTexMatrix * (vTexCoord + delta)) * uColor; ",//
			// "   gl_FragColor.a = 1.0; ",//
			// "   gl_FragColor *= uColor; ",//
			"}" };

	public WarpShader(GL2 gl) {
		super(gl, VERTEX, FRAGMENT);
	}

	private int aVertHandle;
	private int aTexCoordHandle;
	private int uWarpXHandle;
	private int uWarpYHandle;
	private int uTexHandle;
	private int uVertMatrixHandle;
	private int uTexMatrixHandle;
	private int uColorHandle;

	@Override
	protected void initShaderParams(GL2 gl) {
		aVertHandle = gl.glGetAttribLocation(program, "aVert");
		aTexCoordHandle = gl.glGetAttribLocation(program, "aTexCoord");
		uWarpXHandle = gl.glGetUniformLocation(program, "uWarpX");
		uWarpYHandle = gl.glGetUniformLocation(program, "uWarpY");
		uTexHandle = gl.glGetUniformLocation(program, "uTex");
		uVertMatrixHandle = gl.glGetUniformLocation(program, "uVertMatrix");
		uTexMatrixHandle = gl.glGetUniformLocation(program, "uTexMatrix");
		uColorHandle = gl.glGetUniformLocation(program, "uColor");
	}

	public void use(GL2 gl, float[] vertexMatrix, WarpGL warp,
			Texture warpTexture, int color) {
		if (gl.glGetError() != GL2.GL_NO_ERROR) {
			System.err.println("Error at beginning of WarpShader");
		}
		gl.glUseProgram(program);

		gl.glVertexAttribPointer(aVertHandle, 2, GL2.GL_FLOAT, false, 0,
				(Buffer) FloatBuffer.wrap(sqVert));
		gl.glEnableVertexAttribArray(aVertHandle);

		gl.glVertexAttribPointer(aTexCoordHandle, 2, GL2.GL_FLOAT, false, 0,
				(Buffer) FloatBuffer.wrap(sqTex));
		gl.glEnableVertexAttribArray(aTexCoordHandle);

		if (gl.glGetError() != GL2.GL_NO_ERROR) {
			System.err.println("Error at middle of WarpShader");
		}

		gl.glActiveTexture(GL2.GL_TEXTURE0);
		warp.bindX(gl);
		gl.glUniform1i(uWarpXHandle, 0);

		gl.glActiveTexture(GL2.GL_TEXTURE1);
		warp.bindY(gl);
		gl.glUniform1i(uWarpYHandle, 1);

		gl.glActiveTexture(GL2.GL_TEXTURE2);
		warpTexture.bind(gl);
		gl.glUniform1i(uTexHandle, 2);

		// Create a matrix to adjust for scaled texture coordinates which may
		// result if the system doesn't support non-power-of-two textures
		// or if JOGL decides to randomly flip our texture (which happens
		// regularly)
		TextureCoords t = warpTexture.getImageTexCoords();
		float[] texCoordMatrix = new float[] { t.right() - t.left(), 0,
				t.left(), 0, t.top() - t.bottom(), t.bottom(), 0, 0, 1 };

		gl.glUniformMatrix3fv(uTexMatrixHandle, 1, true, texCoordMatrix, 0);

		gl.glUniformMatrix4fv(uVertMatrixHandle, 1, false, vertexMatrix, 0);

		gl.glUniform4f(uColorHandle, //
				((color >> 24) & 0xff) / 255.0f, // red
				((color >> 16) & 0xff) / 255.0f, // green
				((color >> 8) & 0xff) / 255.0f, // blue
				((color >> 0) & 0xff) / 255.0f); // alpha

		gl.glDrawElements(GL2.GL_TRIANGLES, triangles.length,
				GL2.GL_UNSIGNED_INT, (Buffer) IntBuffer.wrap(triangles));
		if (gl.glGetError() != GL2.GL_NO_ERROR) {
			System.err.println("Error at end of WarpShader");
		}
	}
}