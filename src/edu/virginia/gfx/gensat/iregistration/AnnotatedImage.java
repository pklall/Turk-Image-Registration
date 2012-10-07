package edu.virginia.gfx.gensat.iregistration;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class AnnotatedImage {
	private String id;
	private BufferedImage img;
	
	public String getId() {
		return id;
	}
	
	public BufferedImage getImg() {
		return img;
	}
	
	public void setImg(BufferedImage img) {
		this.img = img;
	}
	
	public AnnotatedImage(String id, BufferedImage img) {
		super();
		this.img = img;
	}
}
