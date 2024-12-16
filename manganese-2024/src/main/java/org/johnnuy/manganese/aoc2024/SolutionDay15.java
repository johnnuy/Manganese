package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.StringUtils;
import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.Direction;
import org.johnnuy.manganese.utils.GridUtils;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Position;
import org.johnnuy.manganese.utils.Timer;

public class SolutionDay15 {

	private static Timer<Long> timer = new Timer<>();

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1a: %d\n".formatted(timer.time("Sample 1a", () -> eval(new ClassPathReader("day15/sample_1a.txt")))));
		System.out.println("Sample 1b: %d\n".formatted(timer.time("Sample 1b", () -> eval(new ClassPathReader("day15/sample_1b.txt")))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> eval(new ClassPathReader("day15/input_1.txt")))));

		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> eval2(new ClassPathReader("day15/sample_2.txt")))));
		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> eval2(new ClassPathReader("day15/input_2.txt")))));
	}

	public static Long eval(Reader reader) {
		List<String> grid = new ArrayList<>();
		StringBuilder commands = new StringBuilder();
		AtomicReference<Position> robot = new AtomicReference<>();

		new LineHandler((line, i) -> {
			if (line.startsWith("#")) {
				int index = line.indexOf('@');
				if (index >= 0) {
					line = line.replace('@', '.');
					robot.set(new Position(index, grid.size()));
				}
				grid.add(line);
			} else if (StringUtils.isNotEmpty(line)) {
				commands.append(line);
			}
			return true;
		}).handle(reader);

		char[][] cGrid = GridUtils.gridify(grid);
		String commandString = commands.toString();
		for (int i = 0; i < commandString.length(); i++) {
			moveRobot(robot, cGrid, commandString.charAt(i));
		}
		return getScore(cGrid, 'O');
	}

	private static void moveRobot(AtomicReference<Position> robot, char[][] grid, char dir) {
		int free = nextFree(grid, robot.get().x(), robot.get().y(), dir);
		if (free <= 0) {
			return;
		}
		switch (dir) {
		case '^':
			/* push boxes */
			if (free > 1) {
				grid[robot.get().y() - free][robot.get().x()] = 'O';
				grid[robot.get().y() - 1][robot.get().x()] = '.';
			}
			/* move robot */
			robot.set(robot.get().move(new Direction(0, -1)));
			break;
		case '>':
			/* push boxes */
			if (free > 1) {
				grid[robot.get().y()][robot.get().x() + free] = 'O';
				grid[robot.get().y()][robot.get().x() + 1] = '.';
			}
			/* move robot */
			robot.set(robot.get().move(new Direction(1, 0)));
			break;
		case 'v':
			/* push boxes */
			if (free > 1) {
				grid[robot.get().y() + free][robot.get().x()] = 'O';
				grid[robot.get().y() + 1][robot.get().x()] = '.';
			}
			/* move robot */
			robot.set(robot.get().move(new Direction(0, 1)));
			break;
		case '<':
			/* push boxes */
			if (free > 1) {
				grid[robot.get().y()][robot.get().x() - free] = 'O';
				grid[robot.get().y()][robot.get().x() - 1] = '.';
			}
			/* move robot */
			robot.set(robot.get().move(new Direction(-1, 0)));
			break;
		default:
			throw new IllegalArgumentException(Character.toString(dir));
		}
	}

	private static int nextFree(char[][] grid, int x, int y, char dir) {
		switch (dir) {
		case '^':
			/* moving up is y - 1 */
			for (int dy = y - 1; dy >= 0; dy--) {
				char c = GridUtils.extract(grid, x, dy);
				if (c == '.') {
					return y - dy;
				} else if (c == '#') {
					return -1;
				}
			}
			return -1;
		case '>':
			/* moving right is x + 1 */
			for (int dx = x + 1; dx < grid[y].length; dx++) {
				char c = GridUtils.extract(grid, dx, y);
				if (c == '.') {
					return dx - x;
				} else if (c == '#') {
					return -1;
				}
			}
			return -1;
		case 'v':
			/* moving down is y + 1 */
			for (int dy = y + 1; dy < grid.length; dy++) {
				char c = GridUtils.extract(grid, x, dy);
				if (c == '.') {
					return dy - y;
				} else if (c == '#') {
					return -1;
				}
			}
			return -1;
		case '<':
			/* moving left is x - 1 */
			for (int dx = x - 1; dx >= 0; dx--) {
				char c = GridUtils.extract(grid, dx, y);
				if (c == '.') {
					return x - dx;
				} else if (c == '#') {
					return -1;
				}
			}
			return -1;
		default:
			throw new IllegalArgumentException(Character.toString(dir));
		}
	}

	public static Long eval2(Reader reader) {
		List<String> grid = new ArrayList<>();
		StringBuilder commands = new StringBuilder();
		AtomicReference<Position> robot = new AtomicReference<>();

		new LineHandler((line, i) -> {
			if (line.startsWith("#")) {
				line = line.replace("#", "##");
				line = line.replace(".", "..");
				line = line.replace("O", "[]");
				int index = line.indexOf('@');
				if (index >= 0) {
					line = line.replace("@", "..");
					robot.set(new Position(index, grid.size()));
				}
				grid.add(line);
			} else if (StringUtils.isNotEmpty(line)) {
				commands.append(line);
			}
			return true;
		}).handle(reader);

		char[][] cGrid = GridUtils.gridify(grid);
		String commandString = commands.toString();		
		
		for (int i = 0; i < commandString.length(); i++) {
			moveRobot2(robot, cGrid, commandString.charAt(i));
		}		

		return getScore(cGrid, '[');
	}
	
	private static long getScore(char[][] grid, char c) {
		long total = 0;
		for (int y = 0; y < grid.length; y++) {
			char[] row = grid[y];
			for (int x = 0; x < row.length; x++) {
				char val = row[x];
				if (val == c) {
					total += ((100 * y) + x);
				}
			}
		}
		return total;
	}
	
	private static boolean canMoveUp(char[][] grid, Position p, boolean doMove) {
		/* check if a box above us */
		char c = GridUtils.extract(grid, p.x(), p.y() - 1);
		if (c == '.') {
			return true;
		}
		else if (c == ']') {
			if (canMoveUp(grid, p.move(new Direction(-1, -1)), doMove) && canMoveUp(grid, p.move(new Direction(0, -1)), doMove)) {
				if (doMove) {
					grid[p.y() - 2][p.x() -1] = '[';
					grid[p.y() - 2][p.x()] = ']';
					grid[p.y() - 1][p.x() -1] = '.';
					grid[p.y() - 1][p.x()] = '.';
				}
				return true;
			}
			return false;
		}
		else if (c == '[') {
			if (canMoveUp(grid, p.move(new Direction(0, -1)), doMove) && canMoveUp(grid, p.move(new Direction(1, -1)), doMove)) {
				if (doMove) {
					grid[p.y() - 2][p.x()] = '[';
					grid[p.y() - 2][p.x() + 1] = ']';
					grid[p.y() - 1][p.x()] = '.';
					grid[p.y() - 1][p.x() + 1] = '.';
				}
				return true;
			}
			return false;
		}
		else {
			return false;
		}
	}
	
	private static boolean canMoveDown(char[][] grid, Position p, boolean doMove) {
		/* check if a box above us */
		char c = GridUtils.extract(grid, p.x(), p.y() + 1);
		if (c == '.') {
			return true;
		}
		else if (c == ']') {
			if (canMoveDown(grid, p.move(new Direction(-1, 1)), doMove) && canMoveDown(grid, p.move(new Direction(0, 1)), doMove)) {
				if (doMove) {
					grid[p.y() + 2][p.x() -1] = '[';
					grid[p.y() + 2][p.x()] = ']';
					grid[p.y() + 1][p.x() -1] = '.';
					grid[p.y() + 1][p.x()] = '.';
				}
				return true;
			}
			return false;
		}
		else if (c == '[') {
			if (canMoveDown(grid, p.move(new Direction(0, 1)), doMove) && canMoveDown(grid, p.move(new Direction(1, 1)), doMove)) {
				if (doMove) {
					grid[p.y() + 2][p.x()] = '[';
					grid[p.y() + 2][p.x() + 1] = ']';
					grid[p.y() + 1][p.x()] = '.';
					grid[p.y() + 1][p.x() + 1] = '.';
				}
				return true;
			}
			return false;
		}
		else {
			return false;
		}
	}
	
	private static boolean canMoveLeft(char[][] grid, Position p) {
		/* check if a box above us */
		char c = GridUtils.extract(grid, p.x() - 1, p.y());
		if (c == '.') {
			return true;
		}
		else if (c == ']') {
			if (canMoveLeft(grid, p.move(new Direction(-2, 0)))) {
				grid[p.y()][p.x() - 3] = '[';
				grid[p.y()][p.x() - 2] = ']';
				grid[p.y()][p.x() - 1] = '.';
				return true;
			}
			return false;
		}
		else {
			return false;
		}
	}
	
	private static boolean canMoveRight(char[][] grid, Position p) {
		/* check if a box above us */
		char c = GridUtils.extract(grid, p.x() + 1, p.y());
		if (c == '.') {
			return true;
		}
		else if (c == '[') {
			if (canMoveRight(grid, p.move(new Direction(2, 0)))) {
				grid[p.y()][p.x() + 3] = ']';
				grid[p.y()][p.x() + 2] = '[';
				grid[p.y()][p.x() + 1] = '.';
				return true;
			}
			return false;
		}
		else {
			return false;
		}
	}

	private static void moveRobot2(AtomicReference<Position> robot, char[][] grid, char dir) {
		int x = robot.get().x();
		int y = robot.get().y();
		switch (dir) {
		case '^':
			/* check if a box above us */
			if (canMoveUp(grid, robot.get(), false)) {
				canMoveUp(grid, robot.get(), true);
				robot.set(new Position(x, y - 1));
			}			
			break;
		case '>':
			if (canMoveRight(grid, robot.get())) {
				robot.set(new Position(x + 1, y));
			}
			break;
		case 'v':
			if (canMoveDown(grid, robot.get(), false)) {
				canMoveDown(grid, robot.get(), true);
				robot.set(new Position(x, y + 1));
			}
			break;
		case '<':			
			if (canMoveLeft(grid, robot.get())) {
				robot.set(new Position(x - 1, y));
			}
			break;
		default:
			throw new IllegalArgumentException(Character.toString(dir));
		}
	}
}