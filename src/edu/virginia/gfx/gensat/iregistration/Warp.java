package edu.virginia.gfx.gensat.iregistration;

import java.io.InputStream;
import java.io.OutputStream;

import com.thoughtworks.xstream.XStream;

import edu.virginia.gfx.gensat.iregistration.util.Matrix;

public class Warp {
	/**
	 * Rotation in degrees
	 */
	public float rotation;
	public float translationX;
	public float translationY;
	public float scaleX;
	public float scaleY;
	public final float[] srcVertices;
	public final float[] dstVertices;
	public final int[] triangles;

	private final float[] affine = new float[16];

	/**
	 * @return The affine component of the warp. Note that this matrix should
	 *         NOT be modified.
	 */
	public float[] getAffine() {
		Matrix.setIdentityM(affine, 0);
		Matrix.scaleM(affine, 0, scaleX, scaleY, 1);
		
		
		Matrix.translateM(affine, 0, 0.5f, 0.5f, 0);
		Matrix.rotateM(affine, 0, rotation, 0, 0, 1);
		Matrix.translateM(affine, 0, -0.5f, -0.5f, 0);
		
		affine[12] = translationX;
		affine[13] = translationY;
		affine[14] = 0;
		affine[15] = 1;
		
		return affine;
	}

	public int getNumTriangles() {
		return triangles.length / 3;
	}

	/**
	 * Constructor
	 * 
	 * @param affine
	 *            A column-major 4x4 affine transformation matrix
	 * @param vertices
	 *            A buffer of point data with (x, y) pairs packed together
	 * @param triangles
	 *            An "index buffer" into vertices suitable for use with
	 *            glDrawElements(GL_TRIANGLES)
	 */
	public Warp(float[] vertices, int[] triangles) {
		this.translationX = 0;
		this.translationY = 0;
		this.scaleX = 1;
		this.scaleY = 1;
		this.rotation = 0;
		this.srcVertices = vertices;
		this.dstVertices = srcVertices.clone();
		this.triangles = triangles;
	}

	public void writeWarp(OutputStream os) {
		XStream xstream = new XStream();
		xstream.alias("Warp", Warp.class);
		xstream.toXML(this, os);
	}

	public static Warp readWarp(InputStream is) {
		XStream xstream = new XStream();
		xstream.alias("Warp", Warp.class);
		return (Warp) xstream.fromXML(is);
	}
}
