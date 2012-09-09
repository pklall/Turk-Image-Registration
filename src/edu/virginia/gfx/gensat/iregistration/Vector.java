package edu.virginia.gfx.gensat.iregistration;

import java.awt.Point;

public class Vector {
	public float x;
	public float y;
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public float norm() {
		return (float) Math.sqrt(x*x + y*y);
	}
	
	public void normalize() {
		float n = norm();
		x /= n;
		y /= n;
	}

	public Vector(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public void add(Vector v) {
		add(v.x, v.y);
	}
	
	public void add(float vx, float vy) {
		x += vx;
		y += vy;
	}
	
	public Point toPoint() {
		return new Point((int) x, (int) y);
	}
}
