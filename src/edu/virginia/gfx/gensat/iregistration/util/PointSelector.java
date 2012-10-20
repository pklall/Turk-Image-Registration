package edu.virginia.gfx.gensat.iregistration.util;

import edu.virginia.gfx.gensat.iregistration.util.PointSelectorEvent.PointState;

public abstract class PointSelector implements GLMouseEventListener {
	// pointer to buffer holding points
	protected final float[] pointBuf;

	private boolean moveOnSelect = false;

	public static interface PointSelectorEventListener {
		public void onPointSelectorEvent(PointSelectorEvent e);
	}

	private PointSelectorEventListener listener = null;

	public void setEventListener(PointSelectorEventListener listener) {
		this.listener = listener;
	}

	/**
	 * Indicates whether to generate a move event when a point is selected
	 * 
	 * @param b
	 */
	public void setMoveOnSelect(boolean b) {
		moveOnSelect = b;
	}

	/**
	 * The index of the currently-selected/hovered point.
	 */
	private int selectedPoint = 0;

	/**
	 * @return The index of the selected point
	 */
	public int getSelectedPoint() {
		return selectedPoint;
	}

	/**
	 * True if we are currently moving the selected point
	 */
	private boolean moving = false;

	/**
	 * @return True if the user is currently dragging the selected point (the
	 *         mouse is down)
	 */
	public boolean isDragging() {
		return moving;
	}

	public PointSelector(float[] pointBuf) {
		this.pointBuf = pointBuf;
	}

	@Override
	public void mouseDown(float mx, float my, int buttons, float[] mat) {
		moving = true;

		// transform mouse coordinates from screen space to source-warp space
		// total matrix
		float[] xyzw = new float[4];
		xyzw[0] = mx;
		xyzw[1] = my;
		xyzw[2] = 1;
		xyzw[3] = 1;
		float[] invMat = new float[16];
		Matrix.invertM(invMat, 0, mat, 0);
		Matrix.multiplyMV(xyzw, 0, invMat, 0, xyzw, 0);
		mx = xyzw[0];
		my = xyzw[1];

		listener.onPointSelectorEvent(new PointSelectorEvent(selectedPoint, mx,
				my, PointState.HOVER, PointState.SELECT));

		if (moveOnSelect) {
			listener.onPointSelectorEvent(new PointSelectorEvent(selectedPoint,
					mx, my, PointState.SELECT, PointState.SELECT));
		}
	}

	@Override
	public void mouseUp(float mx, float my, int buttons, float[] mat) {
		moving = false;
		listener.onPointSelectorEvent(new PointSelectorEvent(selectedPoint, mx,
				my, PointState.SELECT, PointState.HOVER));
	}

	@Override
	public void mouseMove(float mx, float my, float[] mat) {
		// transform mouse coordinates from screen space to source-warp space
		// total matrix
		float[] xyzw = new float[4];
		xyzw[0] = mx;
		xyzw[1] = my;
		xyzw[2] = 1;
		xyzw[3] = 1;
		float[] invMat = new float[16];
		Matrix.invertM(invMat, 0, mat, 0);
		Matrix.multiplyMV(xyzw, 0, invMat, 0, xyzw, 0);
		mx = xyzw[0];
		my = xyzw[1];

		if (!moving) {
			// find the closest point to the mouse cursor
			float closestDistance = Float.MAX_VALUE;
			int closestPointIndex = 0;
			for (int i = 0; i < pointBuf.length / 2; i++) {
				float x = pointBuf[i * 2];
				float y = pointBuf[i * 2 + 1];
				// compute distance in screen space
				float dx = mx - x;
				float dy = my - y;
				float dist = dx * dx + dy * dy;
				if (dist < closestDistance) {
					closestDistance = dist;
					closestPointIndex = i;
				}
			}
			if (selectedPoint != closestPointIndex) {
				listener.onPointSelectorEvent(new PointSelectorEvent(
						selectedPoint, mx, my, PointState.HOVER,
						PointState.NONE));
				selectedPoint = closestPointIndex;
				listener.onPointSelectorEvent(new PointSelectorEvent(
						selectedPoint, mx, my, PointState.NONE,
						PointState.HOVER));
			}
		} else {
			listener.onPointSelectorEvent(new PointSelectorEvent(selectedPoint,
					mx, my, PointState.SELECT, PointState.SELECT));
		}
	}
}
