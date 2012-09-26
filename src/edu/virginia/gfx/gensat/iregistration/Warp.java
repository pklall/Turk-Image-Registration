package edu.virginia.gfx.gensat.iregistration;

public class Warp {
	public final float[] srcVertices;
	public final float[] dstVertices;
	public final int[] triangles;

	public Warp(float[] vertices, int[] triangles) {
		this.srcVertices = vertices;
		this.dstVertices = srcVertices.clone();
		this.triangles = triangles;
	}
}
