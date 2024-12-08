package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;
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
	
	private static final List<Pair<BiFunction<Long, Long, Boolean>, BiFunction<Long, Long, Long>>> PART1_OPERATIONS_INVERSE = List.of(
			Pair.of((x, y) -> x > y, (x, y) -> x - y),
			Pair.of((x, y) -> x % y == 0, (x, y) -> x / y)
			);
	
	private static final List<Pair<BiFunction<Long, Long, Boolean>, BiFunction<Long, Long, Long>>> PART2_OPERATIONS_INVERSE = List.of(
			Pair.of((x, y) -> x > y, (x, y) -> x - y),
			Pair.of((x, y) -> x % y == 0, (x, y) -> x / y),
			Pair.of(SolutionDay7::endsWith, SolutionDay7::unconcatenate)
			);
			

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> evaluate(new ClassPathReader("day7/sample_1.txt"), PART1_OPERATORS))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> evaluate(new ClassPathReader("day7/input_1.txt"), PART1_OPERATORS))));
		
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> evaluateReverse(new ClassPathReader("day7/sample_1.txt"), PART1_OPERATIONS_INVERSE))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> evaluateReverse(new ClassPathReader("day7/input_1.txt"), PART1_OPERATIONS_INVERSE))));
		
		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> evaluate(new ClassPathReader("day7/sample_2.txt"), PART2_OPERATORS))));
		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> evaluate(new ClassPathReader("day7/input_2.txt"), PART2_OPERATORS))));
		
		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> evaluateReverse(new ClassPathReader("day7/sample_2.txt"), PART2_OPERATIONS_INVERSE))));
		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> evaluateReverse(new ClassPathReader("day7/input_2.txt"), PART2_OPERATIONS_INVERSE))));
	}
	
	public static BigInteger evaluate(Reader reader, List<BiFunction<Long, Long, Long>> operations) {
		AtomicReference<BigInteger> counter = new AtomicReference<>(BigInteger.ZERO);
		
		new LineHandler((line, i) -> {			
			int index = line.indexOf(':');
			long value = Long.parseLong(line.substring(0, index));		
			List<Long> operands = Arrays
					.stream(line.substring(index + 2).split("\\s+"))
					.map(Long::parseLong)
					.toList();
			if (evaluate(operands.get(0), value, operands.subList(1, operands.size()), operations)) {
				counter.set(counter.get().add(BigInteger.valueOf(value)));
			}			
			return true;
		}).handle(reader);
		
		return counter.get();
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
	
	
	public static BigInteger evaluateReverse(Reader reader, List<Pair<BiFunction<Long, Long, Boolean>, BiFunction<Long, Long, Long>>> operations) {
		AtomicReference<BigInteger> counter = new AtomicReference<>(BigInteger.ZERO);
		
		new LineHandler((line, i) -> {			
			int index = line.indexOf(':');
			long value = Long.parseLong(line.substring(0, index));		
			List<Long> operands = Arrays
					.stream(line.substring(index + 2).split("\\s+"))
					.map(Long::parseLong)
					.toList();
			if (evaluateReverse(value, operands, operations)) {
				counter.set(counter.get().add(BigInteger.valueOf(value)));
			}
			
			return true;
		}).handle(reader);
		
		return counter.get();
	}	
	
	private static boolean evaluateReverse(long value, List<Long> operands, List<Pair<BiFunction<Long, Long, Boolean>, BiFunction<Long, Long, Long>>> operations) {
		if (operands.size() == 1) {			
			return operands.get(0).equals(value);
		}		
		
		long finalOperand = operands.get(operands.size() - 1);
		for(Pair<BiFunction<Long, Long, Boolean>, BiFunction<Long, Long, Long>> operation: operations) {
			if (operation.getLeft().apply(value, finalOperand) && evaluateReverse(operation.getRight().apply(value, finalOperand), operands.subList(0, operands.size() - 1), operations)) {
				return true;
			}
		};		
				
		return false;
	}
	
	/**
	 * checks if a given number ends with the provided suffix
	 * @param number
	 * @param suffix
	 * @return
	 */
	private static boolean endsWith(long number, long suffix) {
		int numDigits = digits(suffix);   

        long divisor = (long) Math.pow(10, numDigits);
        
        /* run the modulus with our divisor to compare to suffix */
        return number % divisor == suffix;
    }
	
	/**
	 * removes the given suffix from the number (assumes it contains the number)
	 * @param number
	 * @param suffix
	 * @return
	 */
	private static long unconcatenate(long number, long suffix) {
		int numDigits = digits(suffix);        
        
        long divisor = (long) Math.pow(10, numDigits);
        
        return number / divisor;
	}
	
	/**
	 * counts the digits in a number
	 * @param x
	 * @return
	 */
	private static int digits(long x) {
		if (x == 0) {
			return 1;
		}
		long temp = x;
        int numDigits = 0;
        while (temp > 0) {
            numDigits++;
            temp /= 10;
        }
        return numDigits;
	}
}