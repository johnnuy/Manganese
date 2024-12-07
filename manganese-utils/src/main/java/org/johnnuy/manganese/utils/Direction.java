package org.johnnuy.manganese.utils;

public record Direction(int x, int y) {

	public Direction() {
		this(0, 0);
	}
	
	public Direction(Direction d) {
		this(d.x, d.y);
	}
	
//	public Direction(int x, int y) {
//		this.x = x;
//		this.y = y;
//	}
}
