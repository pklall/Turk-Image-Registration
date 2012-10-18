package edu.virginia.gfx.gensat.iregistration;

public interface GLMouseEventListener {
	/**
	 * Indicates that a mouse button has been pushed.
	 * 
	 * @param x
	 *            The x coordinate of the mouse (in screen space)
	 * @param y
	 *            The y coordinate of the mouse (in screen space)
	 * @param button
	 *            The mouse button which has been pushed
	 * @param mat
	 *            The matrix transforming coordinates to screen space
	 */
	public void mouseDown(float x, float y, int button, float[] mat);

	/**
	 * Indicates that a mouse button has been released.
	 * 
	 * @param x
	 *            The x coordinate of the mouse (in screen space)
	 * @param y
	 *            The y coordinate of the mouse (in screen space)
	 * @param button
	 *            The mouse button which has been released
	 * @param mat
	 *            The matrix transforming coordinates to screen space
	 */
	public void mouseUp(float x, float y, int button, float[] mat);

	/**
	 * Indicates that the mouse has moved
	 * 
	 * @param x
	 *            The x coordinate of the mouse (in screen space)
	 * @param y
	 *            The y coordinate of the mouse (in screen space)
	 * @param mat
	 *            The matrix transforming coordinates to screen space
	 */
	public void mouseMove(float x, float y, float[] mat);
}
