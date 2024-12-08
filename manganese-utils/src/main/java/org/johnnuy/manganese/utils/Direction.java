package org.johnnuy.manganese.utils;

import java.awt.Point;

public record Direction(int x, int y) {

	public Direction() {
		this(0, 0);
	}
	
	public Direction(Direction d) {
		this(d.x, d.y);
	}
	
	public Direction(Point from, Point to) {
		this(to.x - from.x, to.y - from.y);
	}
}
