package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Timer;

public class SolutionDay7 {

	private static Timer<BigInteger> timer = new Timer<>();

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> correct(new ClassPathReader("day7/sample_1.txt")))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> correct(new ClassPathReader("day7/input_1.txt")))));

//		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> patrolWithLoopCounter(new ClassPathReader("day7/sample_2.txt")))));
//		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> patrolWithLoopCounter(new ClassPathReader("day7/input_2.txt")))));
	}
	
	public static BigInteger correct(Reader reader) {
		AtomicReference<BigInteger> counter = new AtomicReference<>(BigInteger.ZERO);
		
		new LineHandler((line, index) -> {			
			counter.set(counter.get().add(BigInteger.valueOf(evaluate(line))));
			return true;
		}).handle(reader);
		
		return counter.get();
	}
	
	private static long evaluate(String line) {
		int index = line.indexOf(':');
		long value = Long.parseLong(line.substring(0, index));		
		List<Long> operands = Arrays
				.stream(line.substring(index + 2).split("\\s+"))
				.map(Long::parseLong)
				.toList();
		if (evaluate(value, operands)) {
			return value;
		}
		else {
			return 0;
		}
	}
	
	private static boolean evaluate(long value, List<Long> operands) {
		if (operands.size() == 1) {			
			return operands.get(0).equals(value);
		}		
		
		/* subtract the last value and search recursively with the prefixed sublist */
		long finalOperand = operands.get(operands.size() - 1);
		if (evaluate(value - finalOperand, operands.subList(0, operands.size() - 1))) {
			return true;
		}
		/* if we have a modulus 0, this could work */		
		if ((value % finalOperand) == 0 && evaluate(value / finalOperand, operands.subList(0, operands.size() - 1))) {
			return true;
		}
				
		return false;
		
	}
}
