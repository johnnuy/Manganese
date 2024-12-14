package org.johnnuy.manganese.utils;

import java.awt.Point;

public record Position(int x, int y) {

	public Position() {
		this(0, 0);
	}
	
	public Position(Position d) {
		this(d.x, d.y);
	}
	
	public Position(Point from, Point to) {
		this(to.x - from.x, to.y - from.y);
	}
	
	public Position move(Direction d) {
		return new Position(x + d.x(), y + d.y());
	}
	
	public Position constrain(int maxX, int maxY) {
		int currX = x % maxX;
		int currY = y % maxY;
		
		if (currX < 0) {
			currX = maxX + currX;
		}
		
		if (currY < 0) {
			currY = maxY + currY;
		}
		return new Position(currX, currY);
	}
}
