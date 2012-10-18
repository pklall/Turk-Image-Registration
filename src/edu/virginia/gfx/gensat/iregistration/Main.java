package edu.virginia.gfx.gensat.iregistration;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class Main extends JApplet {
	private static final long serialVersionUID = 1L;
	private Editor editor;

	protected void initEditor() {
		float[] affine = new float[16];
		Matrix.setIdentityM(affine, 0);
		// float[] points = new float[] { 0, 0, 0, 1, 1, 1, 1, 0 };
		// int[] triangles = new int[] { 0, 1, 2, 0, 2, 3 };

		// create a grid
		int grid = 5; // 3x3
		float[] points = new float[(grid + 1) * (grid + 1) * 2];
		for (int x = 0; x <= grid; x++) {
			for (int y = 0; y <= grid; y++) {
				points[(y * (grid + 1) + x) * 2 + 0] = (float) x * 1.0f
						/ (float) grid;
				points[(y * (grid + 1) + x) * 2 + 1] = (float) y * 1.0f
						/ (float) grid;
			}
		}
		System.out.println("Points: " + Arrays.toString(points));
		int[] triangles = new int[grid * grid * 3 * 2];
		// top-left triangles
		for (int y = 0; y < grid; y++) {
			for (int x = 0; x < grid; x++) {
				triangles[(y * grid + x) * 3 + 0] = y * (grid + 1) + x;
				triangles[(y * grid + x) * 3 + 1] = y * (grid + 1) + (x + 1);
				triangles[(y * grid + x) * 3 + 2] = (y + 1) * (grid + 1) + x;
			}
		}
		// top-left triangles
		int offset = grid * grid * 3;
		for (int y = 0; y < grid; y++) {
			for (int x = 0; x < grid; x++) {
				triangles[(y * grid + x) * 3 + 0 + offset] = y * (grid + 1)
						+ (x + 1);
				triangles[(y * grid + x) * 3 + 1 + offset] = (y + 1)
						* (grid + 1) + (x + 1);
				triangles[(y * grid + x) * 3 + 2 + offset] = (y + 1)
						* (grid + 1) + x;
			}
		}

		Warp warp = new Warp(affine, points, triangles);

		BufferedImage imgWarp = null;
		BufferedImage imgTarget = null;
		try {
			// test image
			// imgWarp = ImageIO.read(new URL(
			// "http://colorvisiontesting.com/plate%20with%205.jpg"));
			// bean
			// imgWarp = ImageIO .read(new URL(
			// "http://cdn6.fotosearch.com/bthumb/FDS/FDS106/redkid4.jpg"));
			imgWarp = ImageIO
					.read(getClass().getResourceAsStream("/brain.jpg"));
			// imgTarget = ImageIO .read(new URL(
			// "http://www.vnvlvokc.com/ow_userfiles/plugins/shoppro/images/product_1.jpg"));
			imgTarget = ImageIO.read(new URL( "http://www.gensat.org/atlas/ADULT_ATLAS_07.jpg"));
			// imgTarget = ImageIO .read(getClass().getResourceAsStream("/brain2.jpg"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			editor = new Editor(warp, imgWarp, imgTarget);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() {
		System.out.println("initializing... ");
		getContentPane().setLayout(new BorderLayout(0, 0));

		initEditor();

		final JSlider alphaSlider = new JSlider();
		alphaSlider.setMaximum(255);
		alphaSlider.setMinimum(0);
		alphaSlider.setValue(128);
		alphaSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				editor.setAlpha(alphaSlider.getValue());
			}
		});
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		getContentPane().add(editor);
		getContentPane().add(alphaSlider);
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