package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.Direction;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Position;
import org.johnnuy.manganese.utils.Timer;

public class SolutionDay14 {
	
	private static Pattern INPUT = Pattern.compile("p=([0-9]+),([0-9]+)\\sv=([0-9\\-]+),([0-9\\\\-]+)");

	private static Timer<Long> timer = new Timer<>();

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> step(new ClassPathReader("day14/sample_1.txt"), 100, 11, 7))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> step(new ClassPathReader("day14/input_1.txt"), 100, 101, 103))));

		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> evaluate(new ClassPathReader("day14/input_2.txt"), 101, 103))));
	}

	public static Long step(Reader reader, int stepCount, int gridX, int gridY) {
		List<Robot> robots = new ArrayList<>();
		
		new LineHandler((line, index) -> {			
			Matcher m = INPUT.matcher(line);
			if (m.matches()) {
				Robot robot = new Robot();
				robot.position = new Position(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
				robot.velo = new Direction(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)));
				robots.add(robot);
			}
			else {
				throw new IllegalArgumentException(line);
			}
			
			return true;
		}).handle(reader);
		
		System.out.println("Found %d robots".formatted(robots.size()));
		int xBoundary = (gridX - 1) / 2;
		int yBoundary = (gridY - 1) / 2;
		
		long topLeft = 0;
		long topRight = 0;
		long bottomLeft = 0;
		long bottomRight = 0;
		
		for (Robot robot : robots) {
			robot.move(stepCount, gridX, gridY);
			if (robot.position.x() < xBoundary) {
				if (robot.position.y() < yBoundary) {
					topLeft++;
				}
				else if (robot.position.y() > yBoundary){
					bottomLeft++;
				}
			}
			else if (robot.position.x() > xBoundary){
				if (robot.position.y() < yBoundary) {
					topRight++;
				}
				else if (robot.position.y() > yBoundary){
					bottomRight++;
				}
			}
		}
		
		return topLeft * topRight * bottomLeft * bottomRight;
	}
	
	/**
	 * Evaluate the robots positions
	 * @param reader
	 * @param gridX
	 * @param gridY
	 * @return
	 */
	public static Long evaluate(Reader reader, int gridX, int gridY) {
		List<Robot> robots = new ArrayList<>();
		
		new LineHandler((line, index) -> {			
			Matcher m = INPUT.matcher(line);
			if (m.matches()) {
				Robot robot = new Robot();
				robot.position = new Position(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
				robot.velo = new Direction(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)));
				robots.add(robot);
			}
			else {
				throw new IllegalArgumentException(line);
			}
			
			return true;
		}).handle(reader);
		
		System.out.println("Tracking %d robots".formatted(robots.size()));
		
		int maxX = 0;
		long bestX = -1;
		
		boolean[][] buffer = new boolean[gridY][gridX];		
		
		for (int i=1; i<=(gridX * gridY); i++) {
			/* move the robots */
			for (Robot robot : robots) {				
				robot.move(1, gridX, gridY);
			}
		
			/* calculate maximum number of robots in a horizontal and vertical line */
			int dx = maxX(robots, buffer);
			if (dx > maxX) {
				maxX = dx;
				if (dx > 10) {
					printRobots(i, robots, buffer);
				}
				bestX = i;				
			}			
		}		
		
		return bestX;
	}
	
	
	
	/**
	 * determine the maximum number of consecutive robots in any given row
	 *  
	 * @param robots
	 * @param buffer
	 * @return
	 */
	private static int maxX(List<Robot> robots, boolean[][] buffer) {
		for (int i=0; i<buffer.length; i++) {
			Arrays.fill(buffer[i], false);
		}
		for (Robot robot : robots) {
			buffer[robot.position.y()][robot.position.x()] = true;
		}
		
		int maxX = 0;
		for (int i=0; i<buffer.length; i++) {
			boolean[] row = buffer[i];
			int currX = 0;
			for (int j=0; j<row.length; j++) {
				if (row[j]) {
					currX++;
				}
				else {
					currX = 0;
				}
				if (currX > maxX) {
					maxX = currX;
				}
			}			
		}
		return maxX;
	}	
	
	/**
	 * print the robots buffer for visual inspection
	 * @param iteration
	 * @param robots
	 * @param buffer
	 */
	private static void printRobots(int iteration, List<Robot> robots, boolean[][] buffer) {
		System.out.println("Robots Position at iteration %d".formatted(iteration));
		for (int i=0; i<buffer.length; i++) {
			boolean[] row = buffer[i];
			for (int j=0; j<row.length; j++) {
				System.out.print(row[j] ? "*" : " ");
			}
			System.out.println();
		}
	}
	
	private static class Robot {
		public Position position;
		public Direction velo;
		
		public void move(int steps, int gridX, int gridY) {
			position = position.move(velo.scale(steps)).constrain(gridX, gridY);
		}
	}
} 