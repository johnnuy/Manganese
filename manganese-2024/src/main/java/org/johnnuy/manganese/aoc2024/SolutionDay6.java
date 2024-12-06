package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.tuple.Pair;
import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Timer;

public class SolutionDay6 {

	private static Timer<Integer> timer = new Timer<>();

	private static int[][] ROTATION_MATRIX = new int[][] {
			{ 0, 1 },
			{ -1, 0 }
	};

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> patrol(new ClassPathReader("day6/sample_1.txt")))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> patrol(new ClassPathReader("day6/input_1.txt")))));

//		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> calculateReordered(new ClassPathReader("day6/sample_2.txt")))));
//		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> calculateReordered(new ClassPathReader("day6/input_2.txt")))));
	}

	public static int patrol(Reader reader) {
		AtomicInteger locationCount = new AtomicInteger(0);
		List<String> tmp = new ArrayList<>();

		new LineHandler((line, index) -> {
			tmp.add(line);
			return true;
		}).handle(reader);

		char[][] grid = gridify(tmp);

		Pair<int[], int[]> guard = locateGuard(grid);
		System.out.println("Found Guard Located {%d,%d} Facing Direction {%d,%d}".formatted(guard.getLeft()[0], guard.getLeft()[1], guard.getRight()[0], guard.getRight()[1]));
		
		char c;
		int[] nextStep;
		walking: while (true) {
			nextStep = move(guard.getLeft(), guard.getRight());
			System.out.println("Next Step: {%d, %d}".formatted(guard.getLeft()[0], guard.getLeft()[1]));
			c = extract(grid, nextStep[0], nextStep[1]);
			System.out.println("Found %s".formatted((c)));
			switch(c) {
			case '.':
				locationCount.incrementAndGet();
			case 'X':
				/* move the guard forward to the next step but keep direction the same */
				grid[guard.getLeft()[0]][guard.getLeft()[1]] = 'X';
				guard = Pair.of(nextStep, guard.getRight());				
				break;
			case '#':
				/* rotate the guard to the right, but keep the location the same */
				guard = Pair.of(guard.getLeft(), rotate(guard.getRight()));
				break;
			case '\0':
				locationCount.incrementAndGet();
				break walking;
			}
		}

		return locationCount.get();
	}

	/**
	 * Generate a grid of chars
	 * 
	 * @param data
	 * @return
	 */
	private static char[][] gridify(List<String> data) {
		char[][] grid = new char[data.size()][];
		for (int i = 0; i < data.size(); i++) {
			String row = data.get(i);
			grid[i] = new char[row.length()];
			for (int j = 0; j < row.length(); j++) {
				grid[i][j] = row.charAt(j);
			}
		}
		return grid;
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
	private static Pair<int[], int[]> locateGuard(char[][] grid) {
		for (int i = 0; i < grid.length; i++) {
			char[] row = grid[i];
			for (int j = 0; j < row.length; j++) {
				char curr = row[j];
				switch (curr) {
				case '^':
					grid[i][j] = 'X';
					return Pair.of(new int[] { i, j }, new int[] { 0, -1 });
				case 'v':
					grid[i][j] = 'X';
					return Pair.of(new int[] { i, j }, new int[] { 0, 1 });
				case '>':
					grid[i][j] = 'X';
					return Pair.of(new int[] { i, j }, new int[] { 1, 0 });
				case '<':
					grid[i][j] = 'X';
					return Pair.of(new int[] { i, j }, new int[] { -1, 0 });
				}
			}
		}
		throw new IllegalArgumentException("No Starting Position Found");
	}

	private static int[] rotate(int[] v) {
		return new int[] {
				ROTATION_MATRIX[0][0] * v[0] + ROTATION_MATRIX[1][0] * v[1],
				ROTATION_MATRIX[0][1] * v[0] + ROTATION_MATRIX[1][1] * v[1]
		};
	}
	
	/**
	 * move the guard forward one increment
	 * @param pos
	 * @param v
	 * @return
	 */
	private static int[] move(int[] pos, int[] v) {
		return new int[] { pos[0] + v[1], pos[1] + v[0] };
	}
	
	/**
	 * get the status of the gird 
	 * @param grid
	 * @param x
	 * @param y
	 * @return
	 */
	private static char extract(char[][] grid, int x, int y) {
		try {
			return grid[x][y];
		}
		catch(IndexOutOfBoundsException e) {
			return '\0';
		}
	}
}
