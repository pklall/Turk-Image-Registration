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

public class Main extends JApplet {

	/**
	 * Create the applet.
	 */
	public Main() {
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();

		// setup OpenGL Version 2
		GLProfile profile = GLProfile.get(GLProfile.GL2);

		// The canvas is the widget that's drawn in the JFrame
		GLCanvas glCanvas = new GLCanvas(new GLCapabilities(profile));
		glCanvas.addGLEventListener(new Renderer());
		glCanvas.setSize(300, 300);
		frame.getContentPane().add(glCanvas);
		frame.setSize(600, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	protected void initAnnotatedImgComponents() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new URL(
					"http://www.treatpeople.com/image.php?type=P&id=16242"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init() {
		System.out.println("initializing... ");
		getContentPane().setLayout(new BorderLayout(0, 0));

		// setup OpenGL Version 2
		GLProfile profile = GLProfile.get(GLProfile.GL2);

		// The canvas is the widget that's drawn in the JFrame
		GLCanvas glCanvas = new GLCanvas(new GLCapabilities(profile));
		glCanvas.addGLEventListener(new Renderer());
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

class Renderer implements GLEventListener {
	private GLU glu = new GLU();

	public void display(GLAutoDrawable gLDrawable) {
		final GL2 gl = gLDrawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslatef(-1.5f, 0.0f, -6.0f);
		gl.glBegin(GL2.GL_TRIANGLES);
		gl.glVertex3f(0.0f, 1.0f, 0.0f);
		gl.glVertex3f(-1.0f, -1.0f, 0.0f);
		gl.glVertex3f(1.0f, -1.0f, 0.0f);
		gl.glEnd();
		gl.glTranslatef(3.0f, 0.0f, 0.0f);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex3f(-1.0f, 1.0f, 0.0f);
		gl.glVertex3f(1.0f, 1.0f, 0.0f);
		gl.glVertex3f(1.0f, -1.0f, 0.0f);
		gl.glVertex3f(-1.0f, -1.0f, 0.0f);
		gl.glEnd();
		gl.glFlush();
	}

	public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged,
			boolean deviceChanged) {
		System.out.println("displayChanged called");
	}

	public void init(GLAutoDrawable gLDrawable) {
		System.out.println("init() called");
		GL2 gl = gLDrawable.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glShadeModel(GL2.GL_FLAT);
	}

	public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width,
			int height) {
		System.out.println("reshape() called: x = " + x + ", y = " + y
				+ ", width = " + width + ", height = " + height);
		final GL2 gl = gLDrawable.getGL().getGL2();

		if (height <= 0) // avoid a divide by zero error!
		{
			height = 1;
		}

		final float h = (float) width / (float) height;

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0f, h, 1.0, 20.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void dispose(GLAutoDrawable arg0) {
		System.out.println("dispose() called");
	}
}