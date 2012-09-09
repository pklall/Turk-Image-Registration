package edu.virginia.gfx.gensat.iregistration;

import java.awt.Point;

public class Arrow {
	private Vector head;
	private Vector tail;
	
	public Vector getHead() {
		return head;
	}
	public void setHead(Vector head) {
		this.head = head;
	}
	public Vector getTail() {
		return tail;
	}
	public void setTail(Vector tail) {
		this.tail = tail;
	}
	
	@Override
	public String toString() {
		return "Arrow [head=" + head.toString() + ", tail=" + tail.toString() + "]";
	}
	
	public Arrow(int hx, int hy, int tx, int ty) {
		this(new Vector(hx, hy), new Vector(tx,ty));
	}
	
	public Arrow(Vector head, Vector tail) {
		super();
		this.head = head;
		this.tail = tail;
	}
}