package org.johnnuy.manganese.aoc2015;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.atomic.AtomicInteger;

import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;

public class SolutionDay1 {

	public static void main(String[] args) throws IOException {
		System.out.println("Current Floor: %d".formatted(findFloor(new ClassPathReader("day1/input_1.txt"))));
	}
	
	public static int findFloor(Reader reader) throws IOException {
		AtomicInteger currentFloor = new AtomicInteger(0);
		
		new LineHandler((line, index) -> {
			// start at 1, and increment over each character in our instructions
			for (int c=1; c<=line.length(); c+=1) {
				char currentInstruction = line.charAt(c-1);
				if (currentInstruction == '(') {
					currentFloor.incrementAndGet();
				}
				else if (currentInstruction == ')') {
					if (currentFloor.decrementAndGet() == -1) {
						System.out.println("Entered Basement on step %d".formatted(c));
					}
				}
			}			
			
			return true;
		}).handle(reader);
		
		return currentFloor.get();
	}
}