package org.johnnuy.manganese.utils;

public enum CardinalDirection {

	NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);
	
	private int dx;
	private int dy;
	
	private CardinalDirection(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public int getDx() {
		return dx;
	}
	
	public int getDy() {
		return dy;
	}

	public static CardinalDirection clockwise(CardinalDirection d) {
		switch(d) {
		case NORTH:
			return EAST;
		case EAST:
			return SOUTH;
		case SOUTH:
			return WEST;
		case WEST:
			return NORTH;
				default:
					return null;
		}
	}
	
	public static CardinalDirection counterClockwise(CardinalDirection d) {
		switch(d) {
		case NORTH:
			return WEST;
		case EAST:
			return NORTH;
		case SOUTH:
			return EAST;
		case WEST:
			return SOUTH;
				default:
					return null;
		}
	}
}
