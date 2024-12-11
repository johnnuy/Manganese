package org.johnnuy.manganese.aoc2024;

import java.awt.Point;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.GridUtils;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Timer;

public class SolutionDay10 {

	private static Timer<Integer> timer = new Timer<>();

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> process(new ClassPathReader("day10/sample_1.txt")))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> process(new ClassPathReader("day10/input_1.txt")))));

		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> process2(new ClassPathReader("day10/sample_2.txt")))));
		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> process2(new ClassPathReader("day10/input_2.txt")))));
	}

	public static Integer process(Reader reader) {
		AtomicInteger value = new AtomicInteger();
		List<String> lines = new ArrayList<>();

		new LineHandler((line, index) -> {
			lines.add(line);
			return true;
		}).handle(reader);

		int[][] topo = GridUtils.gridifyInt(lines);
		List<Point> heads = locateHeads(topo);
		System.out.println(StringUtils.join(heads));

		for(Point head : heads) {			
			Set<Point> tails = new HashSet<>();
			countTrailHeads(topo, head, 0, tails);
			value.addAndGet(tails.size());
		}
		
		return value.get();
	}
	
	public static Integer process2(Reader reader) {
		AtomicInteger value = new AtomicInteger();
		List<String> lines = new ArrayList<>();

		new LineHandler((line, index) -> {
			lines.add(line);
			return true;
		}).handle(reader);

		int[][] topo = GridUtils.gridifyInt(lines);
		List<Point> heads = locateHeads(topo);
		System.out.println(StringUtils.join(heads));

		for(Point head : heads) {
			int v = countTrailHeads(topo, head, 0);
			value.addAndGet(v);
		}
		
		return value.get();
	}

	private static List<Point> locateHeads(int[][] topo) {
		List<Point> heads = new ArrayList<>();
		for (int i = 0; i < topo.length; i++) {
			int[] row = topo[i];
			for (int j = 0; j < row.length; j++) {
				if (row[j] == 0) {
					heads.add(new Point(j, i));
				}
			}
		}
		return heads;
	}

	private static void countTrailHeads(int[][] topo, Point p, int v, Set<Point> tails) {
		/* base case */
		if (v == 9) {
			tails.add(p);
			return;
		}

		/* check to the left */
		if (extractValue(topo, new Point(p.x - 1, p.y)) == (v + 1)) {
			countTrailHeads(topo, new Point(p.x - 1, p.y), v + 1, tails);
		}
		/* check to the right */
		if (extractValue(topo, new Point(p.x + 1, p.y)) == (v + 1)) {
			countTrailHeads(topo, new Point(p.x + 1, p.y), v + 1, tails);
		}
		/* check up */
		if (extractValue(topo, new Point(p.x, p.y - 1)) == (v + 1)) {
			countTrailHeads(topo, new Point(p.x, p.y - 1), v + 1, tails);
		}
		/* check down */
		if (extractValue(topo, new Point(p.x, p.y + 1)) == (v + 1)) {
			countTrailHeads(topo, new Point(p.x, p.y + 1), v + 1, tails);
		}
	}
	
	private static int countTrailHeads(int[][] topo, Point p, int v) {
		/* base case */
		if (v == 9) {
			return 1;
		}

		int count = 0;
		/* check to the left */
		if (extractValue(topo, new Point(p.x - 1, p.y)) == (v + 1)) {
			count += countTrailHeads(topo, new Point(p.x - 1, p.y), v + 1);
		}
		/* check to the right */
		if (extractValue(topo, new Point(p.x + 1, p.y)) == (v + 1)) {
			count += countTrailHeads(topo, new Point(p.x + 1, p.y), v + 1);
		}
		/* check up */
		if (extractValue(topo, new Point(p.x, p.y - 1)) == (v + 1)) {
			count += countTrailHeads(topo, new Point(p.x, p.y - 1), v + 1);
		}
		/* check down */
		if (extractValue(topo, new Point(p.x, p.y + 1)) == (v + 1)) {
			count += countTrailHeads(topo, new Point(p.x, p.y + 1), v + 1);
		}
		return count;
	}

	private static int extractValue(int[][] topo, Point p) {
		try {
			return topo[p.y][p.x];
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
	}
}