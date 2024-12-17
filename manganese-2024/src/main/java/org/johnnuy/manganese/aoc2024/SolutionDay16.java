package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.johnnuy.manganese.utils.CardinalDirection;
import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.Direction;
import org.johnnuy.manganese.utils.GridUtils;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Position;
import org.johnnuy.manganese.utils.Robot;
import org.johnnuy.manganese.utils.Timer;

public class SolutionDay16 {

	private static Timer<Integer> timer = new Timer<>();

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1a: %d\n".formatted(timer.time("Sample 1a", () -> eval(new ClassPathReader("day16/sample_1a.txt")))));
		System.out.println("Sample 1b: %d\n".formatted(timer.time("Sample 1b", () -> eval(new ClassPathReader("day16/sample_1b.txt")))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> eval(new ClassPathReader("day16/input_1.txt")))));
	}

	public static Integer eval(Reader reader) {
		List<String> grid = new ArrayList<>();
		
		Robot reindeer = new Robot(new Position(0, 0), new Direction(1, 0));
		AtomicReference<Position> start = new AtomicReference<>();
		
		new LineHandler((line, i) -> {
			if (line.startsWith("#")) {
				int index = line.indexOf('S');
				if (index >= 0) {
					line = line.replace('S', '.');
					start.set(new Position(index, grid.size()));
					reindeer.moveTo(start.get());
				}				
				grid.add(line);
			} 
			return true;
		}).handle(reader);
		
		char[][] cGrid = GridUtils.gridify(grid);
		AtomicInteger shortest = new AtomicInteger(Integer.MAX_VALUE);
		cGrid[reindeer.position().y()][reindeer.position().x()] = 'S';
		GridUtils.printGrid(cGrid);
		cGrid[reindeer.position().y()][reindeer.position().x()] = '.';		
		Set<Position> bestPositions = new HashSet<>();
		move(cGrid, start.get(), CardinalDirection.EAST, 0, shortest, false, new HashMap<>(), new Stack<>(), bestPositions, 0);
		System.out.println("Found %d best positions".formatted(bestPositions.size() + 1));
		
		System.out.println(bestPositions);		
		for(Position p : bestPositions) {
			cGrid[p.y()][p.x()] = 'O';
		}
		GridUtils.printGrid(cGrid);

		return shortest.get();
	}
	
	private static void move(char[][] cGrid, Position p, CardinalDirection dir, int path, AtomicInteger shortest, boolean turned, Map<Position, Integer> shortestMap, List<Position> positions, Set<Position> bestPositions, int turns) {
		try {
			/* push our current position onto the stack */
			if (!turned) {
				positions.add(p);
			}
			
			char c = GridUtils.extract(cGrid, p.x() + dir.getDx(), p.y() + dir.getDy());
			
			/* we can move forwards */
			if (c == '.') {		
				try {
					/* mark this position as taken to avoid cycles */
					cGrid[p.y()][p.x()] = 'X';
					Position next = p.move(new Direction(dir.getDx(), dir.getDy()));
					
					/* check if we've been to this position already */
					if (!shortestMap.containsKey(next)) {
						shortestMap.put(next, turns);			
					}
					else {			
						Integer value = shortestMap.get(next);
						/* don't continue if we already have a shorter path to this spot */
						if (turns <= value) {
							/* update our shortest map */
							shortestMap.put(next, turns);
						}
						else if (turns > value + 1) {
							return;
						}
					}
					
					move(cGrid, next, dir, path + 1, shortest, false, shortestMap, positions, bestPositions, turns);
				}
				finally {				
					/* reset our grid, this isn't a spot we want */
					cGrid[p.y()][p.x()] = '.';
				}
			}
			else if (c == 'E') {
				if (shortest.get() >= (path + 1)) {
					System.out.println("Found shortest path yet as %d with tiles %d".formatted(path + 1, positions.size()));
					if (shortest.get() > (path + 1)) {
						bestPositions.clear();
					}
					bestPositions.addAll(positions);
					shortest.set(path + 1);
				}
				return;
			} 
			
			if (!turned) {
				/* try turning clockwise */
				move(cGrid, p, CardinalDirection.clockwise(dir), path + 1000, shortest, true, shortestMap, positions, bestPositions, turns + 1);
				
				/* try turning counterclockwise */
				move(cGrid, p, CardinalDirection.counterClockwise(dir), path + 1000, shortest, true, shortestMap, positions, bestPositions, turns + 1);
							
				/* try turning backwards (could happen at the start) */
				move(cGrid, p, CardinalDirection.clockwise(CardinalDirection.clockwise(dir)), path + 2000, shortest, true, shortestMap, positions, bestPositions, turns + 1);			
			}
		}
		finally {
			if (!turned) {
				positions.remove(positions.size() - 1);
			}
		}
	}
}