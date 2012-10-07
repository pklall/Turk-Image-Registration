package edu.virginia.gfx.gensat.iregistration;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;

import org.ejml.alg.dense.mult.MatrixVectorMult;
import org.ejml.data.Matrix64F;
import org.ejml.ops.MatrixFeatures;
import org.ejml.simple.SimpleMatrix;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;

public class WarpRenderer implements Renderable {
	private final TextureData warpImg;
	private final TextureData targetImg;
	private final Warp warp;

	private Texture warpTex;
	private Texture targetTex;
	private WarpShader shader;

	public WarpRenderer(TextureData warpImg, TextureData targetImg, Warp warp) {
		this.warpImg = warpImg;
		this.targetImg = targetImg;
		this.warp = warp;

	}

	@Override
	public void render(GL2 gl) {
		System.out.println("Drawing warp");
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glDisable(GL2.GL_DEPTH_TEST);
		shader.use(gl, warp, warpTex);
	}

	@Override
	public void destroy(GL2 gl) {
		warpTex.destroy(gl);
		targetTex.destroy(gl);
		shader.destroy(gl);
	}

	@Override
	public void init(GL2 gl) {
		warpTex = new Texture(gl, warpImg);
		targetTex = new Texture(gl, targetImg);
		shader = new WarpShader(gl);
	}
}

class WarpShader extends Shader {
	private static final String[] VERTEX = new String[] { "", //
			"attribute vec2 aSrc;",//
			"attribute vec2 aDst;",//
			"varying vec2 vTexCoord;",//
			"void main() {",//
			"	gl_Position = vec4(aDst, 1, 1);",//
			"	vTexCoord = aSrc;",//
			"}" };
	private static final String[] FRAGMENT = new String[] { "",//
			"uniform sampler2D uWarpTex;",//
			"varying vec2 vTexCoord;",//
			"void main() {",//
			"   gl_FragColor = texture2D(uWarpTex, vTexCoord);",//
			// "	gl_FragColor = vec4(0, 1, 0, 1);",//
			"}" };

	public WarpShader(GL2 gl) {
		super(gl, VERTEX, FRAGMENT);
	}

	private int aSrcHandle;
	private int aDstHandle;
	private int uWarpTexHandle;

	@Override
	protected void initShaderParams(GL2 gl) {
		aSrcHandle = gl.glGetAttribLocation(program, "aSrc");
		aDstHandle = gl.glGetAttribLocation(program, "aDst");
		uWarpTexHandle = gl.glGetAttribLocation(program, "uWarpTex");
	}

	public void use(GL2 gl, Warp warp, Texture warpTexture) {
		gl.glUseProgram(program);
		gl.glVertexAttribPointer(aSrcHandle, 2, GL2.GL_FLOAT, false, 0,
				(Buffer) FloatBuffer.wrap(warp.srcVertices));
		gl.glEnableVertexAttribArray(aSrcHandle);
		gl.glVertexAttribPointer(aDstHandle, 2, GL2.GL_FLOAT, false, 0,
				(Buffer) FloatBuffer.wrap(warp.dstVertices));
		gl.glEnableVertexAttribArray(aDstHandle);
		warpTexture.enable(gl);
		warpTexture.bind(gl);
		gl.glUniform1i(uWarpTexHandle, warpTexture.getTarget());
		gl.glDrawElements(GL2.GL_TRIANGLES, warp.triangles.length,
				GL2.GL_UNSIGNED_INT, (Buffer) IntBuffer.wrap(warp.triangles));
	}
}
