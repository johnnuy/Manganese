package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;

import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Timer;

public class SolutionDay9 {

	private static Timer<Integer> timer = new Timer<>();
	

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> scan(new ClassPathReader("day9/sample_1.txt")))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> scan(new ClassPathReader("day9/input_1.txt")))));
		
		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> scan(new ClassPathReader("day9/sample_2.txt")))));
		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> scan(new ClassPathReader("day9/input_2.txt")))));
	}
	
	public static Integer scan(Reader reader) {
		
		new LineHandler((line, i) -> {			
			
			return true;
		}).handle(reader);
			
		return 0;
	}
	
}