package edu.virginia.gfx.gensat.iregistration;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import org.ejml.alg.dense.mult.MatrixVectorMult;
import org.ejml.data.Matrix64F;
import org.ejml.ops.MatrixFeatures;
import org.ejml.simple.SimpleMatrix;

public class MeshTool implements Tool {
	private final BufferedImage warpImg;
	private final BufferedImage targetImg;
	private final Warp warp;

	// used to color unselected warp points
	private static final int WARP_POINT_COLOR = Color.BLUE.getRGB();
	// used to color the point which is currently selected
	private static final int SELECTED_POINT_COLOR = Color.GREEN.getRGB();
	// used to color points which the user is attempting to pull out of their
	// valid range (locations which would force a change in the triangulation)
	private static final int ERROR_POINT_COLOR = Color.RED.getRGB();

	private static final int MESH_COLOR = Color.LIGHT_GRAY.getRGB();

	public MeshTool(BufferedImage warpImg, BufferedImage targetImg, Warp warp) {
		this.warpImg = warpImg;
		this.targetImg = targetImg;
		this.warp = warp;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GL2 gl) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(GL2 gl) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GL2 gl) {
		// TODO Auto-generated method stub

	}
}
