package org.johnnuy.manganese.utils;

public class Robot {
	
	private Position position; 
	private Direction velo;
	
	public Robot(Position position, Direction velo) {
		this.position = position;
		this.velo = velo;
	}
	
	public Position position() {
		return position;
	}
	
	public Direction velo() {
		return velo;
	}
	
	
	public Robot move(int steps, int gridX, int gridY) {
		this.position = position.move(velo.scale(steps)).constrain(gridX, gridY);
		return this;
	}
	
	public Robot moveTo(Position p) {
		this.position = p;
		return this;
	}
}