package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.GridUtils;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Timer;

public class SolutionDay4 {

	private static Timer<Integer> timer = new Timer<>();
	
	private static int[][] LINEAR_SEARCH_VECTORS = new int[][] {
			{ -1, 0 },
			{ -1, 1 },
			{ 0, 1 },
			{ 1, 1 },
			{ 1, -1 },
			{ -1, -1 },
			{ 1, 0 },
			{ 0, -1 }
	};

	private static int[][] CROSS_SEARCH_VECTORS = new int[][] {
			{ 1, 1 },
			{ -1, -1 }
	};

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> searchXMAS(new ClassPathReader("day4/sample_1.txt")))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> searchXMAS(new ClassPathReader("day4/input_1.txt")))));

		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> searchX_MAS(new ClassPathReader("day4/sample_2.txt")))));
		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> searchX_MAS(new ClassPathReader("day4/input_2.txt")))));
	}

	public static int searchXMAS(Reader reader) {
		AtomicInteger count = new AtomicInteger();
		List<String> tmp = new ArrayList<>();

		new LineHandler((line, index) -> {
			tmp.add(line);
			return true;
		}).handle(reader);

		char[][] grid = GridUtils.gridify(tmp);
		int rows = grid.length;
		int cols = grid[0].length;

		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				/* only continue if we start on an 'X' */
				if (extract(grid, x, y) != 'X') {
					continue;
				}
				/* search all our vectors */
				for (int v = 0; v < LINEAR_SEARCH_VECTORS.length; v++) {
					if (searchLine(grid, x, y, LINEAR_SEARCH_VECTORS[v][0], LINEAR_SEARCH_VECTORS[v][1])) {
						count.incrementAndGet();
					}
				}
			}
		}

		return count.get();
	}

	public static int searchX_MAS(Reader reader) {
		AtomicInteger count = new AtomicInteger();
		List<String> tmp = new ArrayList<>();

		new LineHandler((line, index) -> {
			tmp.add(line);
			return true;
		}).handle(reader);

		char[][] grid = GridUtils.gridify(tmp);
		int rows = grid.length;
		int cols = grid[0].length;

		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				/* only continue if we start on an 'A' */
				if (extract(grid, x, y) != 'A') {
					continue;
				}
				/* search all our vectors */
				vectors: for (int v = 0; v < CROSS_SEARCH_VECTORS.length; v++) {
					if (searchX(grid, x, y, CROSS_SEARCH_VECTORS[v][0], CROSS_SEARCH_VECTORS[v][1])) {
						count.incrementAndGet();
						break vectors;
					}
				}
			}
		}

		return count.get();
	}

	/**
	 * Extract the values starting at our search vector, then opposite and across
	 * 
	 * @param grid
	 * @param x
	 * @param y
	 * @param dx
	 * @param dy
	 * @return
	 */
	private static boolean searchX(char[][] grid, int x, int y, int dx, int dy) {
		return extract(grid, x + dx, y + dy) == 'M' && extract(grid, x - dx, y - dy) == 'S' &&
				((extract(grid, x + dx, y - dy) == 'M' && extract(grid, x - dx, y + dy) == 'S') ||
						(extract(grid, x + dx, y - dy) == 'S' && extract(grid, x - dx, y + dy) == 'M'));
	}

	/**
	 * Extract 3 characters along our search vector and ensure we get the correct
	 * value
	 * 
	 * @param grid
	 * @param x
	 * @param y
	 * @param dx
	 * @param dy
	 * @return
	 */
	private static boolean searchLine(char[][] grid, int x, int y, int dx, int dy) {
		return extract(grid, x + (dx * 1), y + (dy * 1)) == 'M' &&
				extract(grid, x + (dx * 2), y + (dy * 2)) == 'A' &&
				extract(grid, x + (dx * 3), y + (dy * 3)) == 'S';
	}

	/**
	 * Extract the character from the grid, return null character if outside the
	 * grid
	 * 
	 * @param grid
	 * @param x
	 * @param y
	 * @return
	 */
	private static char extract(char[][] grid, int x, int y) {
		try {
			char v = grid[x][y];
			return v;
		} catch (IndexOutOfBoundsException e) {
			return '\0';
		}
	}
}
