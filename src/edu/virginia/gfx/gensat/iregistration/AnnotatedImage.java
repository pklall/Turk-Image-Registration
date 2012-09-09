package edu.virginia.gfx.gensat.iregistration;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class AnnotatedImage {
	private String id;
	private BufferedImage img;
	private List<Arrow> annotations;
	
	public String getId() {
		return id;
	}
	
	public BufferedImage getImg() {
		return img;
	}
	
	public void setImg(BufferedImage img) {
		this.img = img;
	}
	
	public List<Arrow> getAnnotations() {
		return annotations;
	}
	
	public AnnotatedImage(String id, BufferedImage img, List<Arrow> annotations) {
		super();
		this.img = img;
		this.annotations = annotations;
	}
	
	public AnnotatedImage(String id, BufferedImage img) {
		super();
		this.img = img;
		this.annotations = new ArrayList<Arrow>();
	}
}
