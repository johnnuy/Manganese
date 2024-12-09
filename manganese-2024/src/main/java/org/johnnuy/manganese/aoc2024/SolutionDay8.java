package org.johnnuy.manganese.aoc2024;

import java.awt.Point;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.Direction;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Timer;

public class SolutionDay8 {

	private static Timer<Integer> timer = new Timer<>();
	

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> scan(new ClassPathReader("day8/sample_1.txt"), false))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> scan(new ClassPathReader("day8/input_1.txt"), false))));
		
		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> scan(new ClassPathReader("day8/sample_2.txt"), true))));
		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> scan(new ClassPathReader("day8/input_2.txt"), true))));
	}
	
	public static Integer scan(Reader reader, boolean includeResonance) {
		Set<Point> antinodes = new HashSet<>();
		Map<Character, List<Point>> antennas = new HashMap<>();
		AtomicReference<Point> limit = new AtomicReference<>(new Point(0, 0));
		
		new LineHandler((line, i) -> {			
			for (int j = 0; j<line.length(); j++) {
				char curr = line.charAt(j);
				if (curr != '.') {
					antennas.computeIfAbsent(curr, (c) -> new ArrayList<>()).add(new Point(j + 1, i));
				}
			}
			if (limit.get().x < line.length()) {
				limit.set(new Point(line.length(), limit.get().y));
			}
			if (limit.get().y < i) {
				limit.set(new Point(limit.get().x, i));
			}
			return true;
		}).handle(reader);
						
		antennas.entrySet().forEach((e) -> {
			if (includeResonance) {
				antinodes.addAll(antinodesWithResonance(e.getValue(), limit.get()));
			}
			else {
				antinodes.addAll(antinodes(e.getValue(), limit.get()));
			}
		});
		
		return antinodes.size();
	}
	
	private static List<Point> antinodes(List<Point> antennas, Point limit) {
		List<Point> antinodes = new ArrayList<>();
		
		for(int i=0; i<antennas.size(); i++) {
			Point a1 = antennas.get(i);
			for (int j=i + 1; j<antennas.size(); j++) {				
				Point a2 = antennas.get(j);								
				Direction d = new Direction(a1, a2);
				/* extend before 'a1' to get antinode 1 */
				Point an = new Point(a1.x - d.x(), a1.y - d.y());
				if (an.x >= 1 && an.y >= 1 && an.x <= limit.x && an.y <= limit.y) {
					antinodes.add(an);
				}
				
				/* extend past 'a2' to get antinode 2 */
				an = new Point(a2.x + d.x(), a2.y + d.y());
				if (an.x >= 1 && an.y >= 1 && an.x <= limit.x && an.y <= limit.y) {
					antinodes.add(an);
				}
			}
		}
		
		return antinodes;
	}
	
	private static List<Point> antinodesWithResonance(List<Point> antennas, Point limit) {
		List<Point> antinodes = new ArrayList<>();
		
		for(int i=0; i<antennas.size(); i++) {
			Point a1 = antennas.get(i);
			for (int j=i + 1; j<antennas.size(); j++) {				
				Point a2 = antennas.get(j);
				Direction d = new Direction(a1, a2);
				Point an = null;
				/* extend before 'a1' to get antinodes in negative direction */
				int c = 0;
				while(true) {
					an = new Point(a1.x - (c * d.x()), a1.y - (c * d.y()));				
					if (an.x >= 1 && an.y >= 1 && an.x <= limit.x && an.y <= limit.y) {
						antinodes.add(an);
					}
					else {
						break;
					}
					c++;
				}
				
				
				/* extend past 'a2' to get antinodes in positive direction */
				c = 0;
				while(true) {
					an = new Point(a2.x + (c * d.x()), a2.y + (c * d.y()));
					if (an.x >= 1 && an.y >= 1 && an.x <= limit.x && an.y <= limit.y) {
						antinodes.add(an);
					}
					else {
						break;
					}
					c++;
				}
			}
		}
		
		return antinodes;
	}
}