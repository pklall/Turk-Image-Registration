package edu.virginia.gfx.gensat.iregistration;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;

/**
 * A shader for rendering textured 2D triangle meshes
 */
public class Mesh2DShader extends Shader {
	private static final String[] VERTEX = new String[] { "", //
			"uniform mat4 uVertMatrix;",//
			"attribute vec2 aVert;",//
			"attribute vec2 aTexCoord;",//
			"varying vec2 vTexCoord;",//
			"void main() {",//
			"	gl_Position = uVertMatrix * vec4(aVert, 1, 1);",//
			"	vTexCoord = aTexCoord;",//
			"}" };
	private static final String[] FRAGMENT = new String[] { "",//
			"uniform sampler2D uTex;",//
			"uniform mat3 uTexMatrix;",//
			"varying vec2 vTexCoord;",//
			"void main() {",//
			"   gl_FragColor = texture2D(uTex, (uTexMatrix * vec3(vTexCoord, 1)).xy);",//
			"}" };

	public Mesh2DShader(GL2 gl) {
		super(gl, VERTEX, FRAGMENT);
	}

	private int aVertHandle;
	private int aTexCoordHandle;
	private int uTexHandle;
	private int uVertMatrixHandle;
	private int uTexMatrixHandle;

	private final float[] tmpMat = new float[16];

	@Override
	protected void initShaderParams(GL2 gl) {
		aVertHandle = gl.glGetAttribLocation(program, "aVert");
		aTexCoordHandle = gl.glGetAttribLocation(program, "aTexCoord");
		uTexHandle = gl.glGetUniformLocation(program, "uTex");
		uVertMatrixHandle = gl.glGetUniformLocation(program, "uVertMatrix");
		uTexMatrixHandle = gl.glGetUniformLocation(program, "uTexMatrix");
	}

	public void use(GL2 gl, float[] vertex, float[] texture, int[] index,
			float[] vertexMatrix, Texture warpTexture) {
		gl.glUseProgram(program);

		gl.glVertexAttribPointer(aVertHandle, 2, GL2.GL_FLOAT, false, 0,
				(Buffer) FloatBuffer.wrap(vertex));
		gl.glEnableVertexAttribArray(aVertHandle);

		gl.glVertexAttribPointer(aTexCoordHandle, 2, GL2.GL_FLOAT, false, 0,
				(Buffer) FloatBuffer.wrap(texture));
		gl.glEnableVertexAttribArray(aTexCoordHandle);

		warpTexture.enable(gl);
		warpTexture.bind(gl);
		gl.glUniform1i(uTexHandle, warpTexture.getTarget());

		// Create a matrix to adjust for scaled texture coordinates which may
		// result if the system doesn't support non-power-of-two textures
		// or if JOGL decides to randomly flip our texture (which happens
		// regularly)
		TextureCoords t = warpTexture.getImageTexCoords();
		float[] texCoordMatrix = new float[]{
			t.right() - t.left(), 0, t.left(),
			0, t.top() - t.bottom(), t.bottom(),
			0, 0, 1
		};
		
		gl.glUniformMatrix3fv(uTexMatrixHandle, 1, true, texCoordMatrix, 0);

		gl.glUniformMatrix4fv(uVertMatrixHandle, 1, false, vertexMatrix, 0);

		gl.glDrawElements(GL2.GL_TRIANGLES, index.length, GL2.GL_UNSIGNED_INT,
				(Buffer) IntBuffer.wrap(index));
	}
}