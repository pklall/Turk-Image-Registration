package edu.virginia.gfx.gensat.iregistration;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JPanel;
import javax.swing.BoxLayout;

public class Main extends JApplet {

	/**
	 * Create the applet.
	 */
	public Main() {
	}

	AnnotationEditor targetEditor;

	protected void initAnnotatedImgComponents() {
		BufferedImage img = null;
		try {
			img = ImageIO
					.read(new URL(
							"https://www.okpsc.org/sites/default/files/test%20pattern.gif?1345117273"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AnnotatedImage aimg = new AnnotatedImage("Test", img);
		aimg.getAnnotations().add(new Arrow(100, 30, 10, 10));
		targetEditor = new AnnotationEditor(aimg);
		targetEditor.setSelected(-1);
		targetEditor.setAddMode();
		targetEditor.setShowAll(true);
		getContentPane().add(targetEditor);
	}

	protected void initAnnotationPanel() throws IOException {
		AnnotatedImage template = new AnnotatedImage("template",
				ImageIO.read(new File(
						"1810041L15Rik_adult_S_DAB_10X_08_cryo.med.jpg")));
		new AnnotatedImage("", null, null);
		AnnotationPanel p = new AnnotationPanel(template, template);
	}

	public void init() {
		System.out.println("initializing... ");
		getContentPane().setLayout(new BorderLayout(0, 0));
		// initAnnotatedImgComponents();
		try {
			initAnnotationPanel();
		} catch (Exception e) {
			System.err.println("");
		}
	}

	public void start() {
		System.out.println("starting... ");
	}

	@Override
	public void update(Graphics g) {
		super.update(g);
	}

	public void stop() {
		System.out.println("stopping... ");
	}

	public void destroy() {
		System.out.println("destroying...");
	}
}
