package org.johnnuy.manganese.aoc2024;

import java.awt.Point;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.tuple.Pair;
import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.Direction;
import org.johnnuy.manganese.utils.GridUtils;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Timer;

public class SolutionDay6 {

	private static Timer<Integer> timer = new Timer<>();

	private static int[][] ROTATION_MATRIX = new int[][] {
			{ 0, 1 },
			{ -1, 0 }
	};

	private static int UP_MASK = 0x01;
	private static int DOWN_MASK = 0x02;
	private static int LEFT_MASK = 0x04;
	private static int RIGHT_MASK = 0x08;

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> patrol(new ClassPathReader("day6/sample_1.txt")))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> patrol(new ClassPathReader("day6/input_1.txt")))));

		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> patrolWithLoopCounter(new ClassPathReader("day6/sample_2.txt")))));
		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> patrolWithLoopCounter(new ClassPathReader("day6/input_2.txt")))));
	}
	
	public static int patrol(Reader reader) {
		char[][] grid = loadGrid(reader);
		
		Pair<Point, Direction> guard = locateGuard(grid);
		System.out.println("Found Guard Located {%d,%d} Facing Direction {%d,%d}".formatted(guard.getLeft().x, guard.getLeft().y, guard.getRight().x(), guard.getRight().y()));
		
		return tracePath(grid, guard).size();
	}
	
	public static int patrolWithLoopCounter(Reader reader) {
		char[][] grid = loadGrid(reader);
		
		Pair<Point, Direction> guard = locateGuard(grid);
		System.out.println("Found Guard Located {%d,%d} Facing Direction {%d,%d}".formatted(guard.getLeft().x, guard.getLeft().y, guard.getRight().x(), guard.getRight().y()));
		
		Set<Point> path = tracePath(grid, Pair.of(guard.getLeft(), guard.getRight()));
		System.out.println("Traced Path of %d elements".formatted(path.size()));
		AtomicInteger loops = new AtomicInteger();
		path.forEach((point) -> {
			try {
				/* add an obstruction at this point in the graph and see if we get an infinite loop */
				char[][] obstructedGrid = cloneGrid(grid);
				obstructedGrid[point.x][point.y] = '#';
				tracePath(obstructedGrid, Pair.of(guard.getLeft(), guard.getRight()));
			}
			catch(IllegalArgumentException e) {
				loops.incrementAndGet();
			}
		});
		
		return loops.get();
	}
	
	private static char[][] cloneGrid(char[][] grid) {
		char[][] clone = new char[grid.length][];
		for (int i=0; i<grid.length; i++) {
			clone[i] = Arrays.copyOf(grid[i], grid[i].length);
		}
		return clone;
	}
	
	private static Set<Point> tracePath(char[][] grid, Pair<Point, Direction> guard) {
		/* 
		 * create a direction buffer and fill it with 0's, our dbuffer is used to track which directions
		 * we have crossed a given element at, this is required for cycle detection
		 */
		int[][] dBuffer = new int[grid.length][grid[0].length];
		for (int i = 0; i < dBuffer.length; i++) {
			Arrays.fill(dBuffer[i], 0);
		}

		/* first we want to traverse the map and capture all of the locations we've visited */
		Set<Point> visited = new HashSet<>();
		
		char c;
		Point nextStep;
		walking: while (true) {
			nextStep = move(guard.getLeft(), guard.getRight());
			c = extract(grid, nextStep.x, nextStep.y);						
			switch (c) {
			case '.':
				/* set the current value to an 'X' indicating we've visited this one already */
				grid[guard.getLeft().x][guard.getLeft().y] = 'X';
				visited.add(new Point(guard.getLeft().x, guard.getLeft().y));
				/* update our dbuffer... this is new so we know we haven't been here yet */
				dBuffer[guard.getLeft().x][guard.getLeft().y] = applyMask(dBuffer[guard.getLeft().x][guard.getLeft().y], guard.getRight());
				guard = Pair.of(nextStep, guard.getRight());
				break;
			case 'X':				
				/* move the guard forward to the next step but keep direction the same */
				visited.add(new Point(guard.getLeft().x, guard.getLeft().y));		
				/* calculate our new dbuffer mask, and compare it to the existing one, if it didn't change then we are in a cycle */
				int mask = applyMask(dBuffer[guard.getLeft().x][guard.getLeft().y], guard.getRight());
				if (dBuffer[guard.getLeft().x][guard.getLeft().y] == mask) {
					throw new IllegalArgumentException("Detected Grid Cycle");
				}
				/* update our dbuffer since we know we aren't in a cycle now */
				dBuffer[guard.getLeft().x][guard.getLeft().y] = mask;
				guard = Pair.of(nextStep, guard.getRight());
				break;
			case '#':
				dBuffer[guard.getLeft().x][guard.getLeft().y] = applyMask(dBuffer[guard.getLeft().x][guard.getLeft().y], guard.getRight());
				/* rotate the guard to the right, but keep the location the same */
				guard = Pair.of(guard.getLeft(), rotate(guard.getRight()));
				break;
			case '\0':
				visited.add(new Point(guard.getLeft().x, guard.getLeft().y));
				guard = Pair.of(nextStep, guard.getRight());				
				break walking;
			}
		}
				
		return visited;
	}
	
	
	private static char[][] loadGrid(Reader reader) {
		List<String> tmp = new ArrayList<>();

		new LineHandler((line, index) -> {
			tmp.add(line);
			return true;
		}).handle(reader);

		return GridUtils.gridify(tmp);
	}

	/**
	 * returns the location of the guard as a Pair.
	 * 
	 * First element represents the location, Second element represents his
	 * direction
	 * 
	 * @param grid
	 * @return
	 */
	private static Pair<Point, Direction> locateGuard(char[][] grid) {
		for (int i = 0; i < grid.length; i++) {
			char[] row = grid[i];
			for (int j = 0; j < row.length; j++) {
				char curr = row[j];
				switch (curr) {
				case '^':
					grid[i][j] = 'X';
					return Pair.of(new Point(i, j), new Direction(0, -1));
				case 'v':
					grid[i][j] = 'X';
					return Pair.of(new Point(i, j), new Direction(0, 1));
				case '>':
					grid[i][j] = 'X';
					return Pair.of(new Point(i, j), new Direction(1, 0));
				case '<':
					grid[i][j] = 'X';
					return Pair.of(new Point(i, j), new Direction(-1, 0));
				}
			}
		}
		throw new IllegalArgumentException("No Starting Position Found");
	}

	/**
	 * rotates our vector by 90 degrees using a 2d rotation matrix
	 * @param v
	 * @return
	 */
	private static Direction rotate(Direction v) {
		return new Direction(
				ROTATION_MATRIX[0][0] * v.x() + ROTATION_MATRIX[1][0] * v.y(),
				ROTATION_MATRIX[0][1] * v.x() + ROTATION_MATRIX[1][1] * v.y());
	}

	/**
	 * move the guard forward one increment
	 * 
	 * @param pos
	 * @param v
	 * @return
	 */
	private static Point move(Point pos, Direction v) {
		return new Point(pos.x + v.y(), pos.y + v.x());
	}

	/**
	 * get the status of the gird
	 * 
	 * @param grid
	 * @param x
	 * @param y
	 * @return
	 */
	private static char extract(char[][] grid, int x, int y) {
		try {
			return grid[x][y];
		} catch (IndexOutOfBoundsException e) {
			return '\0';
		}
	}

	/**
	 * Applies a direction mask to the dvalue buffer
	 * 
	 * @param value
	 * @param v
	 * @return
	 */
	private static int applyMask(int value, Direction v) {
		/* Up '^' */
		if (v.x() == 0 && v.y() == -1) {
			return value | UP_MASK;
		}
		/* Down 'v' */
		else if (v.x() == 0 && v.y() == 1) {
			return value | DOWN_MASK;
		}
		/* left '<' */
		else if (v.x() == -1 && v.y() == 0) {
			return value | LEFT_MASK;
		} else {
			return value | RIGHT_MASK;
		}
	}
}
