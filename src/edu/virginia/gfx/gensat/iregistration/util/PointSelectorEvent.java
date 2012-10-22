package edu.virginia.gfx.gensat.iregistration.util;

public class PointSelectorEvent {
	public enum PointState {
		NONE, HOVER, SELECT
	}

	public final int point;

	/**
	 * Previous mouse (x, y) coordinates
	 */
	public final float pmx, pmy;

	/**
	 * Current mouse (x, y) coordinates
	 */
	public final float mx, my;

	public final PointState oldState;
	public final PointState newState;

	public PointSelectorEvent(int point, float pmx, float pmy, float mx,
			float my, PointState oldState, PointState newState) {
		super();
		this.point = point;
		this.pmx = pmx;
		this.pmy = pmy;
		this.mx = mx;
		this.my = my;
		this.oldState = oldState;
		this.newState = newState;
	}

	@Override
	public String toString() {
		return "PointSelectorEvent [point=" + point + ", mx=" + mx + ", my="
				+ my + ", oldState=" + oldState + ", newState=" + newState
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + point;
		result = prime * result + Float.floatToIntBits(mx);
		result = prime * result + Float.floatToIntBits(my);
		result = prime * result
				+ ((newState == null) ? 0 : newState.hashCode());
		result = prime * result
				+ ((oldState == null) ? 0 : oldState.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PointSelectorEvent other = (PointSelectorEvent) obj;
		if (point != other.point)
			return false;
		if (Float.floatToIntBits(mx) != Float.floatToIntBits(other.mx))
			return false;
		if (Float.floatToIntBits(my) != Float.floatToIntBits(other.my))
			return false;
		if (newState != other.newState)
			return false;
		if (oldState != other.oldState)
			return false;
		return true;
	}
}