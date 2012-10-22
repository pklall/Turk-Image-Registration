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

	private final float[] previousMouse = new float[4];
	private final float[] currentMouse = new float[4];

	@Override
	public void mouseDown(float mx, float my, int buttons, float[] mat) {
		moving = true;

		// transform mouse coordinates from screen space to source-warp space
		// total matrix
		currentMouse[0] = mx;
		currentMouse[1] = my;
		currentMouse[2] = 0;
		currentMouse[3] = 1;

		float[] invMat = new float[16];
		Matrix.invertM(invMat, 0, mat, 0);
		Matrix.multiplyMV(currentMouse, 0, invMat, 0, currentMouse, 0);
		Matrix.multiplyMV(previousMouse, 0, invMat, 0, previousMouse, 0);
		float cx = currentMouse[0];
		float cy = currentMouse[1];
		float px = previousMouse[0];
		float py = previousMouse[1];

		listener.onPointSelectorEvent(new PointSelectorEvent(selectedPoint, px,
				py, cx, cy, PointState.HOVER, PointState.SELECT));

		if (moveOnSelect) {
			listener.onPointSelectorEvent(new PointSelectorEvent(selectedPoint,
					px, py, cx, cy, PointState.SELECT, PointState.SELECT));
		}

		previousMouse[0] = mx;
		previousMouse[1] = my;
		previousMouse[2] = 0;
		previousMouse[3] = 1;
	}

	@Override
	public void mouseUp(float mx, float my, int buttons, float[] mat) {
		moving = false;

		// transform mouse coordinates from screen space to source-warp space
		// total matrix
		currentMouse[0] = mx;
		currentMouse[1] = my;
		currentMouse[2] = 0;
		currentMouse[3] = 1;

		float[] invMat = new float[16];
		Matrix.invertM(invMat, 0, mat, 0);
		Matrix.multiplyMV(currentMouse, 0, invMat, 0, currentMouse, 0);
		Matrix.multiplyMV(previousMouse, 0, invMat, 0, previousMouse, 0);
		float cx = currentMouse[0];
		float cy = currentMouse[1];
		float px = previousMouse[0];
		float py = previousMouse[1];

		listener.onPointSelectorEvent(new PointSelectorEvent(selectedPoint, px,
				py, cx, cy, PointState.SELECT, PointState.HOVER));

		previousMouse[0] = mx;
		previousMouse[1] = my;
		previousMouse[2] = 0;
		previousMouse[3] = 1;
	}

	@Override
	public void mouseMove(float mx, float my, float[] mat) {
		// transform mouse coordinates from screen space to source-warp space
		// total matrix
		currentMouse[0] = mx;
		currentMouse[1] = my;
		currentMouse[2] = 0;
		currentMouse[3] = 1;

		float[] invMat = new float[16];
		Matrix.invertM(invMat, 0, mat, 0);
		Matrix.multiplyMV(currentMouse, 0, invMat, 0, currentMouse, 0);
		Matrix.multiplyMV(previousMouse, 0, invMat, 0, previousMouse, 0);
		float cx = currentMouse[0];
		float cy = currentMouse[1];
		float px = previousMouse[0];
		float py = previousMouse[1];

		if (!moving) {
			// find the closest point to the mouse cursor
			float closestDistance = Float.MAX_VALUE;
			int closestPointIndex = 0;
			for (int i = 0; i < pointBuf.length / 2; i++) {
				float x = pointBuf[i * 2];
				float y = pointBuf[i * 2 + 1];
				// compute distance in screen space
				float dx = cx - x;
				float dy = cy - y;
				float dist = dx * dx + dy * dy;
				if (dist < closestDistance) {
					closestDistance = dist;
					closestPointIndex = i;
				}
			}
			if (selectedPoint != closestPointIndex) {
				listener.onPointSelectorEvent(new PointSelectorEvent(
						selectedPoint, px, py, cx, cy, PointState.HOVER,
						PointState.NONE));
				selectedPoint = closestPointIndex;
				listener.onPointSelectorEvent(new PointSelectorEvent(
						selectedPoint, px, py, cx, cy, PointState.NONE,
						PointState.HOVER));
			}
		} else {
			listener.onPointSelectorEvent(new PointSelectorEvent(selectedPoint,
					px, py, cx, cy, PointState.SELECT, PointState.SELECT));
		}

		previousMouse[0] = mx;
		previousMouse[1] = my;
		previousMouse[2] = 0;
		previousMouse[3] = 1;
	}
}
