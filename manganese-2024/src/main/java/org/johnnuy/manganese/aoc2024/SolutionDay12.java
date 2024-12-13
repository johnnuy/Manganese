package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.GridUtils;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Timer;

public class SolutionDay12 {

	private static Timer<Integer> timer = new Timer<>();

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> process(new ClassPathReader("day12/sample_1.txt")))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> process(new ClassPathReader("day12/input_1.txt")))));

		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> process2(new ClassPathReader("day12/sample_2.txt")))));
		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> process2(new ClassPathReader("day12/input_2.txt")))));
	}

	public static Integer process(Reader reader) {
		List<String> rows = new ArrayList<>();

		new LineHandler((line, index) -> {
			rows.add(line);
			return true;
		}).handle(reader);

		List<List<Pair<Character, Boolean>>> grid = GridUtils.gridifyList(rows, (c) -> Pair.of(c, Boolean.FALSE));

		return cost(grid);
	}

	private static int cost(List<List<Pair<Character, Boolean>>> grid) {
		int totalCost = 0;

		for (int y = 0; y < grid.size(); y++) {
			List<Pair<Character, Boolean>> row = grid.get(y);
			for (int x = 0; x < row.size(); x++) {
				Pair<Character, Boolean> curr = row.get(x);				
				/* check if we've visited this area yet */
				if (curr.getRight() == false) {
					Pair<Integer, Integer> cost = mapGarden(grid, x, y);
					totalCost += cost.getLeft() * cost.getRight();
				}				
			}
		}

		return totalCost;
	}

	private static Pair<Integer, Integer> mapGarden(List<List<Pair<Character, Boolean>>> grid, int x, int y) {
		Pair<Character, Boolean> curr = extract(grid, x, y);
		grid.get(y).set(x, curr = Pair.of(curr.getLeft(), Boolean.TRUE));
		/* cost = {area, perimeter} */
		Pair<Integer, Integer> cost = Pair.of(1, 0);
		/* check up for a matching character that we haven't traversed yet */
		Pair<Character, Boolean> up = extract(grid, x, y - 1);
		if (up.getLeft() == curr.getLeft()) {
			if (up.getRight().equals(Boolean.FALSE)) {
				Pair<Integer, Integer> upCost = mapGarden(grid, x, y - 1);
				cost = Pair.of(cost.getLeft() + upCost.getLeft(), cost.getRight() + upCost.getRight());
			}
		} else {
			cost = Pair.of(cost.getLeft(), cost.getRight() + 1);
		}

		Pair<Character, Boolean> down = extract(grid, x, y + 1);
		if (down.getLeft() == curr.getLeft()) {
			if (down.getRight().equals(Boolean.FALSE)) {
				Pair<Integer, Integer> downCost = mapGarden(grid, x, y + 1);
				cost = Pair.of(cost.getLeft() + downCost.getLeft(), cost.getRight() + downCost.getRight());
			}
		} else {
			cost = Pair.of(cost.getLeft(), cost.getRight() + 1);
		}

		Pair<Character, Boolean> left = extract(grid, x - 1, y);
		if (left.getLeft() == curr.getLeft()) {
			if (left.getRight().equals(Boolean.FALSE)) {
				Pair<Integer, Integer> leftCost = mapGarden(grid, x - 1, y);
				cost = Pair.of(cost.getLeft() + leftCost.getLeft(), cost.getRight() + leftCost.getRight());
			}
		} else {
			cost = Pair.of(cost.getLeft(), cost.getRight() + 1);
		}

		Pair<Character, Boolean> right = extract(grid, x + 1, y);
		if (right.getLeft() == curr.getLeft()) {
			if (right.getRight().equals(Boolean.FALSE)) {
				Pair<Integer, Integer> rightCost = mapGarden(grid, x + 1, y);
				cost = Pair.of(cost.getLeft() + rightCost.getLeft(), cost.getRight() + rightCost.getRight());
			}
		} else {
			cost = Pair.of(cost.getLeft(), cost.getRight() + 1);
		}

		return cost;
	}
	
	public static Integer process2(Reader reader) {
		List<String> rows = new ArrayList<>();

		new LineHandler((line, index) -> {
			rows.add(line);
			return true;
		}).handle(reader);

		List<List<Pair<Character, Boolean>>> grid = GridUtils.gridifyList(rows, (c) -> Pair.of(c, Boolean.FALSE));

		return cost2(grid);
	}
	
	private static int cost2(List<List<Pair<Character, Boolean>>> grid) {
		int totalCost = 0;

		for (int y = 0; y < grid.size(); y++) {
			List<Pair<Character, Boolean>> row = grid.get(y);
			for (int x = 0; x < row.size(); x++) {
				Pair<Character, Boolean> curr = row.get(x);				
				/* check if we've visited this area yet */
				if (curr.getRight() == false) {
					Pair<Integer, Integer> cost = mapGarden2(grid, x, y);
					totalCost += cost.getLeft() * cost.getRight();
				}				
			}
		}

		return totalCost;
	}

	private static Pair<Integer, Integer> mapGarden2(List<List<Pair<Character, Boolean>>> grid, int x, int y) {
		Pair<Character, Boolean> curr = extract(grid, x, y);
		grid.get(y).set(x, curr = Pair.of(curr.getLeft(), Boolean.TRUE));
		/* cost = {area, perimeter} */
		Pair<Integer, Integer> cost = Pair.of(1, 0);
		/* check up for a matching character that we haven't traversed yet */
		Pair<Character, Boolean> up = extract(grid, x, y - 1);
		if (up.getLeft() == curr.getLeft()) {
			if (up.getRight().equals(Boolean.FALSE)) {
				Pair<Integer, Integer> upCost = mapGarden2(grid, x, y - 1);
				cost = Pair.of(cost.getLeft() + upCost.getLeft(), cost.getRight() + upCost.getRight());
			}
		}

		Pair<Character, Boolean> down = extract(grid, x, y + 1);
		if (down.getLeft() == curr.getLeft()) {
			if (down.getRight().equals(Boolean.FALSE)) {
				Pair<Integer, Integer> downCost = mapGarden2(grid, x, y + 1);
				cost = Pair.of(cost.getLeft() + downCost.getLeft(), cost.getRight() + downCost.getRight());
			}
		}

		Pair<Character, Boolean> left = extract(grid, x - 1, y);
		if (left.getLeft() == curr.getLeft()) {
			if (left.getRight().equals(Boolean.FALSE)) {
				Pair<Integer, Integer> leftCost = mapGarden2(grid, x - 1, y);
				cost = Pair.of(cost.getLeft() + leftCost.getLeft(), cost.getRight() + leftCost.getRight());
			}
		}

		Pair<Character, Boolean> right = extract(grid, x + 1, y);
		if (right.getLeft() == curr.getLeft()) {
			if (right.getRight().equals(Boolean.FALSE)) {
				Pair<Integer, Integer> rightCost = mapGarden2(grid, x + 1, y);
				cost = Pair.of(cost.getLeft() + rightCost.getLeft(), cost.getRight() + rightCost.getRight());
			}
		}
		
		/* check if we are in a corner */
		if (up.getLeft() != curr.getLeft() && left.getLeft() != curr.getLeft()) {
			cost = Pair.of(cost.getLeft(), cost.getRight() + 2);
		}
		if (down.getLeft() != curr.getLeft() && right.getLeft() != curr.getLeft()) {
			cost = Pair.of(cost.getLeft(), cost.getRight() + 2);
		}		
		Pair<Character, Boolean> upRight = extract(grid, x + 1, y - 1);
		if (up.getLeft() == curr.getLeft() && right.getLeft() == curr.getLeft() && upRight.getLeft() != curr.getLeft()) {
			cost = Pair.of(cost.getLeft(), cost.getRight() + 2);
		}		
		Pair<Character, Boolean> downLeft = extract(grid, x - 1, y + 1);
		if (down.getLeft() == curr.getLeft() && left.getLeft() == curr.getLeft() && downLeft.getLeft() != curr.getLeft()) {
			cost = Pair.of(cost.getLeft(), cost.getRight() + 2);
		}

		return cost;
	}

	private static Pair<Character, Boolean> extract(List<List<Pair<Character, Boolean>>> grid, int x, int y) {
		try {
			return grid.get(y).get(x);
		} catch (IndexOutOfBoundsException e) {
			return Pair.of('\0', true);
		}
	}
}