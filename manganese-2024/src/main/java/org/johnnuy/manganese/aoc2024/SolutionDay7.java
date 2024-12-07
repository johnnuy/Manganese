package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Timer;

public class SolutionDay7 {

	private static Timer<BigInteger> timer = new Timer<>();
	private static final List<BiFunction<Long, Long, Long>> PART1_OPERATORS = List.of(
			(x, y) -> x + y,
			(x, y) -> x * y);
	
	private static final List<BiFunction<Long, Long, Long>> PART2_OPERATORS = List.of(
			(x, y) -> x + y,
			(x, y) -> x * y,
			(x, y) -> Long.parseLong("%d%d".formatted(x, y)));

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> correct(new ClassPathReader("day7/sample_1.txt"), PART1_OPERATORS))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> correct(new ClassPathReader("day7/input_1.txt"), PART1_OPERATORS))));
		
		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> correct(new ClassPathReader("day7/sample_2.txt"), PART2_OPERATORS))));
		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> correct(new ClassPathReader("day7/input_2.txt"), PART2_OPERATORS))));
	}
	
	public static BigInteger correct(Reader reader, List<BiFunction<Long, Long, Long>> operations) {
		AtomicReference<BigInteger> counter = new AtomicReference<>(BigInteger.ZERO);
		
		new LineHandler((line, index) -> {			
			counter.set(counter.get().add(BigInteger.valueOf(evaluate(line, operations))));
			return true;
		}).handle(reader);
		
		return counter.get();
	}
	
	private static long evaluate(String line, List<BiFunction<Long, Long, Long>> operations) {
		int index = line.indexOf(':');
		long value = Long.parseLong(line.substring(0, index));		
		List<Long> operands = Arrays
				.stream(line.substring(index + 2).split("\\s+"))
				.map(Long::parseLong)
				.toList();
		if (evaluate(operands.get(0), value, operands.subList(1, operands.size()), operations)) {
			return value;
		}
		else {
			return 0;
		}
	}
	
	private static boolean evaluate(long value, long target, List<Long> operands, List<BiFunction<Long, Long, Long>> operations) {
		if (operands.size() == 0) {
			return value == target;
		}
		if (value > target) {
			return false;
		}
		
		/* subtract the last value and search recursively with the prefixed sublist */		
		long firstOperand = operands.get(0);
		for(BiFunction<Long, Long, Long> operation : operations) {
			if (evaluate(operation.apply(value, firstOperand), target, operands.subList(1, operands.size()), operations)) {
				return true;
			}
		};		
		return false;
	}
}
