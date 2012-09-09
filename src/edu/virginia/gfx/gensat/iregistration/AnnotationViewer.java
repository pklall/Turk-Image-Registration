package edu.virginia.gfx.gensat.iregistration;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class AnnotationViewer extends JComponent {
	private static final long serialVersionUID = 5715361033622062355L;

	protected AnnotatedImage aimg;

	protected int selected = -1;

	/*
	 * Arrow rendering parameters
	 */
	protected Color arrowColor = Color.blue;
	protected Color selectedArrowColor = Color.red;
	protected float arrowHeadSize = 5;

	/**
	 * True if the viewer should display all annotations, false if only the
	 * selected annotation should be displayed.
	 */
	private boolean showAll = true;
	
	public void setShowAll(boolean b) {
		showAll = b;
	}

	public AnnotationViewer(AnnotatedImage aimg) {
		super();
		this.aimg = aimg;
	}

	/**
	 * Modifies the given point so it's relative to image space instead of
	 * scaled ui space.
	 * 
	 * @param p
	 *            The point (in UI space) to convert.
	 */
	protected Vector uiToImgSpace(Vector v) {
		float nx = v.x * aimg.getImg().getWidth() / getWidth();
		float ny = v.y * aimg.getImg().getHeight() / getHeight();
		return new Vector(nx, ny);
	}

	protected Vector imgToUISpace(Vector v) {
		float nx = v.x * getWidth() / aimg.getImg().getWidth();
		float ny = v.y * getHeight() / aimg.getImg().getHeight();
		return new Vector(nx, ny);
	}

	protected void drawArrow(Graphics2D g, Arrow a) {
		Vector head = imgToUISpace(a.getHead());
		Vector tail = imgToUISpace(a.getTail());

		g.drawLine((int) head.x, (int) head.y, (int) tail.x, (int) tail.y);

		// create a basis to frame the arrow head
		float deltaX = head.x - tail.x;
		float deltaY = head.y - tail.y;
		float norm2 = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		deltaX /= norm2;
		deltaY /= norm2;

		float perpX = deltaY;
		float perpY = -deltaX;

		deltaX *= arrowHeadSize;
		deltaY *= arrowHeadSize;
		perpX *= arrowHeadSize;
		perpY *= arrowHeadSize;

		// draw the arrowhead
		g.drawLine((int) head.x, (int) head.y, (int) (head.x + perpX - deltaX),
				(int) (head.y + perpY - deltaY));

		g.drawLine((int) head.x, (int) head.y, (int) (head.x - perpX - deltaX),
				(int) (head.y - perpY - deltaY));
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics.create();

		// draw the image
		BufferedImage i = aimg.getImg();
		g.drawImage(i, 0, 0, getWidth(), getHeight(), 0, 0, i.getWidth(),
				i.getHeight(), null);

		// draw each arrow
		int arrowIndex = 0;
		for (Arrow a : aimg.getAnnotations()) {
			// change the color depending on whether or not it is selected
			if (arrowIndex == selected) {
				g.setColor(selectedArrowColor);
				drawArrow(g, a);
			} else if (showAll) {
				g.setColor(arrowColor);
				drawArrow(g, a);
			}
			arrowIndex++;
		}
	}

	/**
	 * @return true if an arrow is selected, false otherwise
	 */
	public boolean isSelected() {
		return selected >= 0 && selected < aimg.getAnnotations().size();
	}

	/**
	 * Sets the selected arrow index.
	 * 
	 * @Return true if the index is valid, false otherwise
	 */
	public boolean setSelected(int index) {
		selected = index;
		return isSelected();
	}

	/**
	 * @return the currently selected arrow, null if no arrow is selected
	 */
	public Arrow getSelected() {
		if (isSelected()) {
			return aimg.getAnnotations().get(selected);
		} else {
			return null;
		}
	}

	/**
	 * Increments the index of the selected arrow.
	 * 
	 * @Return true if the index wrapped around, false otherwise.
	 */
	public boolean incSelected() {
		selected++;
		if (selected >= aimg.getAnnotations().size()) {
			selected = 0;
			return true;
		}
		return false;
	}
}
