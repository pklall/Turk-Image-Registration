package edu.virginia.gfx.gensat.iregistration;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JPanel;
import javax.swing.BoxLayout;

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

	private GLProfile profile;
	private Warp warp;
	private WarpRenderer warpRenderer;
	private SquareRenderer sqRenderer;

	protected void initWarpRenderer() {
		float[] affine = new float[16];
		Matrix.setIdentityM(affine, 0);
		float[] points = new float[] { 0, 0, 0, 1, 1, 1, 1, 0 };
		int[] triangles = new int[] { 0, 1, 2, 0, 2, 3 };
		warp = new Warp(affine, points, triangles);

		BufferedImage img = null;
		try {
			img = ImageIO
					.read(new URL(
							"http://www.vnvlvokc.com/ow_userfiles/plugins/shoppro/images/product_1.jpg"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TextureData warpImg = AWTTextureIO.newTextureData(profile, img, false);
		TextureData targetImg = warpImg;
		warpRenderer = new WarpRenderer(warpImg, warp);

		// FIXME
		sqRenderer = new SquareRenderer(targetImg);
	}

	public void init() {
		System.out.println("initializing... ");
		getContentPane().setLayout(new BorderLayout(0, 0));

		// setup OpenGL Version 2
		profile = GLProfile.get(GLProfile.GL2);

		// The canvas is the widget that's drawn in the JFrame
		GLCanvas glCanvas = new GLCanvas(new GLCapabilities(profile));

		initWarpRenderer();

		glCanvas.addGLEventListener(new GLEventListener() {
			@Override
			public void display(GLAutoDrawable d) {
				GL2 gl = d.getGL().getGL2();
				gl.glEnable(GL2.GL_BLEND);

				float[] mat = new float[16];
				Matrix.setIdentityM(mat, 0);
				Matrix.scaleM(mat, 0, 2, 2, 1);
				Matrix.translateM(mat, 0, -0.5f, -0.5f, 0);

				// sqRenderer.render(gl, mat);
				warpRenderer.render(gl, mat);
			}

			@Override
			public void dispose(GLAutoDrawable d) {
				GL2 gl = d.getGL().getGL2();
				sqRenderer.destroy(gl);
				warpRenderer.destroy(gl);
			}

			@Override
			public void init(GLAutoDrawable d) {
				GL2 gl = d.getGL().getGL2();
				warpRenderer.init(gl);
				sqRenderer.init(gl);
			}

			@Override
			public void reshape(GLAutoDrawable d, int x, int y, int width,
					int height) {
				d.getGL().glViewport(x, y, width, height);
			}
		});
		glCanvas.setSize(300, 300);

		getContentPane().add(glCanvas);
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