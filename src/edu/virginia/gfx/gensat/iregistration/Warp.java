package edu.virginia.gfx.gensat.iregistration;

import java.io.InputStream;
import java.io.OutputStream;

import com.thoughtworks.xstream.XStream;

public class Warp {
	public final float[] affine;
	public final float[] srcVertices;
	public final float[] dstVertices;
	public final int[] triangles;

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
	public Warp(float[] affine, float[] vertices, int[] triangles) {
		this.affine = affine;
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
		xstream.fromXML(is);
		return null;
	}
}
