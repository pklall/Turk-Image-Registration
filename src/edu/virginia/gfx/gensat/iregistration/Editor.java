package edu.virginia.gfx.gensat.iregistration;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import edu.virginia.gfx.gensat.iregistration.util.InteractiveRenderable;
import edu.virginia.gfx.gensat.iregistration.util.Matrix;
import edu.virginia.gfx.gensat.iregistration.util.SquareRenderer;

public class Editor extends GLJPanel implements GLEventListener, MouseListener,
		MouseMotionListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;

	private final Warp warp;

	private final TextureData warpImg;
	private final TextureData targetImg;

	public void submit() {
		// TODO implement this!
	}

	public void setAlpha(int t) {
		warpRenderer.setAlpha(t);
		repaint();
	}

	private static final float PADDING = 0.15f; // 15% padding on all sides

	// Matrix to transform [0, 1]x[0,1] (origin at bottom left) into
	// OpenGL viewport coordinates. All rendering will use this as the base
	// of the matrix stack.
	private final float[] mat = new float[16];

	private WarpRenderer warpRenderer;
	private SquareRenderer targetRenderer;

	private MeshTool meshTool;

	private InteractiveRenderable activeTool;
	
	public MeshTool getMeshTool() {
		return meshTool;
	}

	public Editor(Warp warp, BufferedImage warpImg, BufferedImage targetImg)
			throws IOException {
		super(new GLCapabilities(GLProfile.get(GLProfile.GL2)));

		addGLEventListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);

		this.warp = warp;
		this.warpImg = AWTTextureIO.newTextureData(getGLProfile(), warpImg,
				true);
		this.targetImg = AWTTextureIO.newTextureData(getGLProfile(), targetImg,
				true);
		meshTool = new MeshTool(warp, getGLProfile());
		warpRenderer = new WarpRenderer(this.warpImg, warp);
		setAlpha(128);
		targetRenderer = new SquareRenderer(this.targetImg);

		activeTool = meshTool;
	}

	public void setEditorModeAffine() {
		repaint();
	}

	public void setEditorModeWarp() {
		activeTool = meshTool;
		repaint();
	}

	@Override
	public void display(GLAutoDrawable d) {
		GL2 gl = d.getGL().getGL2();
		gl.glEnable(GL2.GL_BLEND);

		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glDisable(GL2.GL_DEPTH_TEST);
		gl.glDisable(GL2.GL_CULL_FACE);

		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendEquation(GL2.GL_FUNC_ADD);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

		targetRenderer.render(gl, mat);
		warpRenderer.render(gl, mat);
		activeTool.render(gl, mat);
	}

	@Override
	public void dispose(GLAutoDrawable d) {
		GL2 gl = d.getGL().getGL2();
		targetRenderer.destroy(gl);
		warpRenderer.destroy(gl);
		meshTool.destroy(gl);
	}

	@Override
	public void init(GLAutoDrawable d) {
		GL2 gl = d.getGL().getGL2();
		targetRenderer.init(gl);
		warpRenderer.init(gl);
		meshTool.init(gl);
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);

	}

	@Override
	public void reshape(GLAutoDrawable d, int x, int y, int width, int height) {
		System.out.println(String.format("reshape(%d, %d, %d, %d)", x, y,
				width, height));

		d.getGL().glViewport(x, y, width, height);

		Matrix.setIdentityM(mat, 0);
		Matrix.scaleM(mat, 0, 2 - PADDING, 2 - PADDING, 1);
		Matrix.translateM(mat, 0, -0.5f, -0.5f, 0);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Convert mouse coordinates to screen space
		float x = e.getX();
		float y = e.getY();
		x *= 2.0f / getWidth();
		y *= 2.0f / getHeight();
		x = -1.0f + x;
		y = 1.0f - y;
		activeTool.mouseDown(x, y, e.getButton(), mat);
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		float x = e.getX();
		float y = e.getY();
		x *= 2.0f / getWidth();
		y *= 2.0f / getHeight();
		x = -1.0f + x;
		y = 1.0f - y;
		activeTool.mouseUp(x, y, e.getButton(), mat);
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		float x = e.getX();
		float y = e.getY();
		x *= 2.0f / getWidth();
		y *= 2.0f / getHeight();
		x = -1.0f + x;
		y = 1.0f - y;
		activeTool.mouseMove(x, y, mat);
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		float x = e.getX();
		float y = e.getY();
		x *= 2.0f / getWidth();
		y *= 2.0f / getHeight();
		x = -1.0f + x;
		y = 1.0f - y;
		activeTool.mouseMove(x, y, mat);
		repaint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
			meshTool.incRadius(-e.getUnitsToScroll());
			repaint();
		}
	}
}
