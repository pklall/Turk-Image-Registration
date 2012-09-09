package edu.virginia.gfx.gensat.iregistration;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class AnnotationEditor extends AnnotationViewer {

	private static final long serialVersionUID = -5297593889013621268L;

	protected Color dragBoxColor = Color.red;
	protected Color hoverBoxColor = Color.orange;

	protected int selectRadius = 10;

	private EditorMode currentMode = null;
	private final EditMode editMode = new EditMode();
	private final AddMode addMode = new AddMode();

	private void setMode(EditorMode em) {
		removeMouseListener(currentMode);
		removeMouseMotionListener(currentMode);
		currentMode = em;
		addMouseListener(em);
		addMouseMotionListener(em);
	}

	public void setEditMode() {
		setMode(editMode);
	}

	public void setAddMode() {
		setMode(addMode);
	}

	/**
	 * The current vector of interest (the point immediately under the mouse
	 * cursor). This may point to the head or tail of the selected arrow, or may
	 * be null if neither is under the mouse.
	 */
	private Vector selectedVector = null;

	/**
	 * True if the selectedVector is being dragged, false otherwise.
	 */
	protected boolean dragState = false;

	protected boolean vectorInMouseRange(Vector v, Point mouse) {
		v = imgToUISpace(v);
		return (v.x - selectRadius < mouse.x) && (v.x + selectRadius > mouse.x)
				&& (v.y - selectRadius < mouse.y)
				&& (v.y + selectRadius > mouse.y);
	}

	protected void updateSelectedPoint(Point mouse) {
		Arrow s = getSelected();
		selectedVector = null;
		if (s != null) {
			if (vectorInMouseRange(s.getTail(), mouse)) {
				selectedVector = s.getTail();
			}
			if (vectorInMouseRange(s.getHead(), mouse)) {
				selectedVector = s.getHead();
			}
		}
		repaint();
	}

	/**
	 * Constructor
	 * 
	 * @param aimg
	 */
	public AnnotationEditor(AnnotatedImage aimg) {
		super(aimg);
		setEditMode();
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics.create();
		if (selectedVector != null) {
			if (dragState) {
				g.setColor(dragBoxColor);
			} else {
				g.setColor(hoverBoxColor);
			}
			Vector v = imgToUISpace(selectedVector);
			/*
			 * g.drawRect((int) v.x - selectRadius, (int) v.y - selectRadius,
			 * selectRadius * 2, selectRadius * 2);
			 */
			g.drawArc((int) v.x - selectRadius, (int) v.y - selectRadius,
					selectRadius * 2, selectRadius * 2, 0, 360);
		}
	}

	interface EditorMode extends MouseListener, MouseMotionListener {
	}

	class EditMode implements EditorMode {
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent me) {
			if (me.getComponent() != AnnotationEditor.this) {
				return;
			}
			Point m = me.getPoint();
			updateSelectedPoint(m);
			if (selectedVector != null) {
				dragState = true;
			}
		}

		@Override
		public void mouseReleased(MouseEvent me) {
			dragState = false;
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent me) {
			if (me.getComponent() != AnnotationEditor.this
					|| selectedVector == null || dragState == false) {
				return;
			}
			// update the selected vector to the new mouse location
			Point mouse = me.getPoint();
			// clip mouse coordinates to the component's rectangle
			if (mouse.x < 0)
				mouse.x = 0;
			if (mouse.y < 0)
				mouse.y = 0;
			if (mouse.x > getWidth())
				mouse.x = getWidth();
			if (mouse.y > getHeight())
				mouse.y = getHeight();
			// image space location of the mouse
			Vector m = uiToImgSpace(new Vector(mouse.x, mouse.y));
			selectedVector.x = m.x;
			selectedVector.y = m.y;
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent me) {
			updateSelectedPoint(me.getPoint());
		}
	}

	class AddMode implements EditorMode {
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent me) {
			if (me.getComponent() != AnnotationEditor.this) {
				return;
			}
			Point m = me.getPoint();
			Vector mvector = new Vector(m.x, m.y);
			Arrow newArrow = new Arrow(uiToImgSpace(mvector),
					uiToImgSpace(mvector));
			aimg.getAnnotations().add(newArrow);
			setSelected(aimg.getAnnotations().size() - 1);
			selectedVector = newArrow.getHead();
			dragState = true;
			setEditMode();
		}

		@Override
		public void mouseReleased(MouseEvent me) {
		}

		@Override
		public void mouseDragged(MouseEvent me) {
		}

		@Override
		public void mouseMoved(MouseEvent me) {
		}
	}
}
