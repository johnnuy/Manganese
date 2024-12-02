package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;

public class SolutionDay2 {

	public static void main(String[] args) throws IOException {
		System.out.println("Sample: %d".formatted(safe(new ClassPathReader("day2/sample_1.txt"))));
		System.out.println("Problem: %d".formatted(safe(new ClassPathReader("day2/input_1.txt"))));

		System.out.println("Sample 2: %d".formatted(safeWithError(new ClassPathReader("day2/sample_2.txt"))));
		System.out.println("Problem 2: %d".formatted(safeWithError(new ClassPathReader("day2/input_2.txt"))));
	}

	/**
	 * Checks how many strings are safe
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static int safe(Reader reader) throws IOException {
		AtomicInteger safeCount = new AtomicInteger();
		AtomicInteger lineCount = new AtomicInteger();
		
		new LineHandler((line, index) -> {
			lineCount.incrementAndGet();
			String[] levels = line.split("\\s+");
			if (isSafe(levels)) {
				safeCount.incrementAndGet();
			}
			return true;
		}).handle(reader);

		return safeCount.get();
	}
	
	/**
	 * Checks how many strings are safe, with a single error tollerance
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static int safeWithError(Reader reader) throws IOException {
		AtomicInteger safeCount = new AtomicInteger();
		AtomicInteger lineCount = new AtomicInteger();
		
		new LineHandler((line, index) -> {
			lineCount.incrementAndGet();
			String[] levels = line.split("\\s+");
			if (isSafe(levels)) {
				safeCount.incrementAndGet();
				return true;
			}
			
			/* remove each element one at a time until we find a safe one */
			for (int i=0; i<levels.length;i++) {
				List<String> list = new ArrayList<>(List.of(levels));
				list.remove(i);
				if (isSafe(list.toArray(new String[0]))) {
					safeCount.incrementAndGet();
					return true;
				}
			}
			
			return true;
		}).handle(reader);

		return safeCount.get();
	}
	
	/**
	 * Checks if a given string is safe
	 * 
	 * @param levels
	 * @return
	 */
	private static boolean isSafe(String[] levels) {
		if (levels.length == 1) {
			return true;
		}
		
		int currLevel = Integer.parseInt(levels[0]);			
		int nextLevel = Integer.parseInt(levels[1]);			
		boolean increasing = currLevel < nextLevel;
		if (Math.abs(currLevel - nextLevel) > 3 || Math.abs(currLevel - nextLevel) < 1) {
			return false;
		}
		currLevel = nextLevel;			
		/* check ascending */
		for (int i = 2; i < levels.length; i++) {
			nextLevel = Integer.parseInt(levels[i]);
			if (increasing) {
				if (nextLevel - currLevel > 3 || nextLevel - currLevel < 1) {
					return false;
				}
			}
			else {
				if (currLevel - nextLevel > 3 || currLevel - nextLevel < 1) {
					return false;
				}
			}
			currLevel = nextLevel;
		}
		return true;
	}	
}
