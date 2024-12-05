package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;

public class SolutionDay3 {

	private static Pattern MUL_PATTERN = Pattern.compile("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)");
	private static Pattern MUL_WITH_INSTRUCTION_PATTERN = Pattern.compile("((mul\\(([0-9]{1,3}),([0-9]{1,3})\\))|(do\\(\\))|(don't\\(\\)))");

	public static void main(String[] args) throws IOException {
		System.out.println("Sample: %d".formatted(scan(new ClassPathReader("day3/sample_1.txt"))));
		System.out.println("Problem: %d".formatted(scan(new ClassPathReader("day3/input_1.txt"))));

		System.out.println("Sample 2: %d".formatted(scanWithInstructions(new ClassPathReader("day3/sample_2.txt"))));
		System.out.println("Problem 2: %d".formatted(scanWithInstructions(new ClassPathReader("day3/input_2.txt"))));
	}

	public static int scan(Reader reader) throws IOException {
		AtomicInteger sum = new AtomicInteger();

		new LineHandler((line, index) -> {
			Matcher m = MUL_PATTERN.matcher(line);
			int pos = 0;
			while (m.find(pos)) {
				sum.addAndGet(Integer.valueOf(m.group(1)) * Integer.valueOf(m.group(2)));
				pos = m.end();
			}
			return true;
		}).handle(reader);

		return sum.get();
	}

	public static int scanWithInstructions(Reader reader) throws IOException {
		AtomicInteger sum = new AtomicInteger();
		AtomicBoolean enabled = new AtomicBoolean(true);

		new LineHandler((line, index) -> {
			Matcher m = MUL_WITH_INSTRUCTION_PATTERN.matcher(line);
			int pos = 0;
			while (m.find(pos)) {
				if (m.group(5) != null) {
					enabled.set(true);
				} else if (m.group(6) != null) {
					enabled.set(false);
				} else if (enabled.get()) {
					sum.addAndGet(Integer.valueOf(m.group(3)) * Integer.valueOf(m.group(4)));
				}
				pos = m.end();
			}
			return true;
		}).handle(reader);

		return sum.get();
	}
}
