package org.johnnuy.manganese.aoc2023;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;

public class SolutionDay1 {

	public static void main(String[] args) throws IOException {
		System.out.println("Sample: %d".formatted(calibrate_1(new ClassPathReader("day1/sample_1.txt"))));		
		System.out.println("Problem: %d".formatted(calibrate_1(new ClassPathReader("day1/input_1.txt"))));
		
		System.out.println("Sample 2: %d".formatted(calibrate_2(new ClassPathReader("day1/sample_2.txt"))));
		System.out.println("Problem 2: %d".formatted(calibrate_2(new ClassPathReader("day1/input_2.txt"))));
	}
	
	
	/**
	 * Part 1 Calibration
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static int calibrate_1(Reader reader) throws IOException {
		AtomicInteger calibration = new AtomicInteger(0);
		
		new LineHandler((line) -> {
			int first = 0;
			int last = 0;
			/* find the first numeric */
			for (int i=0; i<line.length(); i++) {
				if (Character.isDigit(line.charAt(i))) {
					first = Integer.parseInt("" + line.charAt(i));
					break;
				}
				
			}
			
			/* find the last numeric */
			for (int i=line.length() - 1; i>= 0; i--) {
				if (Character.isDigit(line.charAt(i))) {
					last = Integer.parseInt("" + line.charAt(i));
					break;
				}
			}
			
			calibration.addAndGet((first * 10) + last);
			
			return true;
		}).handle(reader);
		
		return calibration.get();
	}
	
	/**
	 * Part 2 Calibration
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static int calibrate_2(Reader reader) throws IOException {
		AtomicInteger calibration = new AtomicInteger(0);
		
		new LineHandler((line) -> {
			line = StringUtils.replace(line, "one", "o1e");
			line = StringUtils.replace(line, "two", "t2o");
			line = StringUtils.replace(line, "three", "th3ee");
			line = StringUtils.replace(line, "four", "f4ur");
			line = StringUtils.replace(line, "five", "f5ve");
			line = StringUtils.replace(line, "six", "s6x");
			line = StringUtils.replace(line, "seven", "se7en");
			line = StringUtils.replace(line, "eight", "ei8ht");
			line = StringUtils.replace(line, "nine", "n9ne");
		
			int first = 0;
			int last = 0;
			/* find the first numeric */
			for (int i=0; i<line.length(); i++) {
				if (Character.isDigit(line.charAt(i))) {
					first = Integer.parseInt("" + line.charAt(i));
					break;
				}
			}
			
			/* find the last numeric */
			for (int i=line.length() - 1; i>= 0; i--) {
				if (Character.isDigit(line.charAt(i))) {
					last = Integer.parseInt("" + line.charAt(i));
					break;
				}
			}
			
			calibration.addAndGet((first * 10) + last);
			
			return true;
		}).handle(reader);
		
		return calibration.get();
	}
}