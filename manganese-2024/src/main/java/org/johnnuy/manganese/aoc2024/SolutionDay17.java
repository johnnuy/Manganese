package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.StringUtils;
import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Timer;

/**
 * https://www.reddit.com/r/adventofcode/comments/1hgo81r/2024_day_17_genuinely_enjoyed_this/
 * 
 * -- grab the lower 3 bits of A --
 * 2,4 -> bst(4) -> b = regA % 8 -> b = lower 3 bits of A
 * 
 * 
 * -- flip the middle bit
 * 1,2 -> bxl(2) -> b = b xor 2
 * 
 * 0 -> 2
 * 1 -> 3
 * 2 -> 0
 * 3 -> 1
 * 4 -> 6
 * 5 -> 7
 * 6 -> 4
 * 7 -> 5
 *  
 * 
 * 7,5 -> cdv(5) -> c = a / 2 ^ b
 * 1,3 -> bxl(3) -> b = b xor 3
 * 4,3 -> bxc(3) -> b = b xor c
 * 
 * 
 * -- print the lower 3 bits of B
 * 5,5 -> out(5) -> sysout b % 8 -> sysout lower 3 bits of B
 * 
 * -- shift A to the right and go again until A is 0
 * 0,3 -> adv(3) -> a = a / 2^3 -> a = a / 8 -> shift A 3 bits to the right
 * 3,0 -> jnz(0) -> restart
 * 
 * 
 * 
 */
public class SolutionDay17 {
	
	private static Map<Integer, Op> operations = Map.of(
			0, SolutionDay17::adv,
			1, SolutionDay17::bxl,
			2, SolutionDay17::bst,
			3, SolutionDay17::jnz,
			4, SolutionDay17::bxc,
			5, SolutionDay17::out,
			6, SolutionDay17::bdv,
			7, SolutionDay17::cdv);

	private static Timer<String> timer = new Timer<>();
	private static Timer<Integer> timer2 = new Timer<>();

	public static void main(String[] args) throws IOException {
//		System.out.println("Sample 1: %s\n".formatted(timer.time("Sample 1", () -> run(new ClassPathReader("day17/sample_1.txt")))));
//		System.out.println("Problem 1: %s\n".formatted(timer.time("Problem 1", () -> run(new ClassPathReader("day17/input_1.txt")))));
		
		
//		System.out.println("Sample 2: %d\n".formatted(timer2.time("Sample 2", () -> run2(new ClassPathReader("day17/sample_2.txt")))));		
		System.out.println("Problem 2: %d\n".formatted(timer2.time("Problem 2", () -> run2(new ClassPathReader("day17/input_2.txt")))));
	}
		
	public static String run(Reader reader) {
		AtomicReference<String> code = new AtomicReference<>();
		AtomicLong a = new AtomicLong();
		AtomicLong b = new AtomicLong();
		AtomicLong c = new AtomicLong();
		AtomicLong pc = new AtomicLong(0);
		List<Integer> sysout = new ArrayList<>();
		
		
		new LineHandler((line, i) -> {
			if (StringUtils.isBlank(line)) {
				return true;
			}
			else if (line.startsWith("Register A:")) {
				a.set(Integer.parseInt(line.substring(12)));
				return true;
			}
			else if (line.startsWith("Register B:")) {
				b.set(Integer.parseInt(line.substring(12)));
				return true;
			}			
			else if (line.startsWith("Register C:")) {
				c.set(Integer.parseInt(line.substring(12)));
				return true;
			}
			else {			
				code.set(line.substring(9));
			}
			return true;
		}).handle(reader);
		
		List<Integer> opCodes = Arrays.stream(code.get().split(",")).map(Integer::valueOf).toList();
		do {
			Op op = operations.get(opCodes.get((int) pc.get()));
			int operand = opCodes.get((int) pc.get() + 1);
			op.exec(sysout, pc, a, b, c, operand);
		} while (pc.get() < opCodes.size());
		
		
		return StringUtils.join(sysout, ',');
	}
	
	public static Integer run2(Reader reader) {
		AtomicReference<String> code = new AtomicReference<>();
		AtomicLong a = new AtomicLong();
		AtomicLong b = new AtomicLong();
		AtomicLong c = new AtomicLong();
		AtomicLong pc = new AtomicLong(0);
		List<Integer> sysout = new ArrayList<>();
		
		
		new LineHandler((line, i) -> {
			if (StringUtils.isBlank(line)) {
				return true;
			}
			else if (line.startsWith("Register A:")) {
				a.set(Integer.parseInt(line.substring(12)));
				return true;
			}
			else if (line.startsWith("Register B:")) {
				b.set(Integer.parseInt(line.substring(12)));
				return true;
			}			
			else if (line.startsWith("Register C:")) {
				c.set(Integer.parseInt(line.substring(12)));
				return true;
			}
			else {			
				code.set(line.substring(9));
			}
			return true;
		}).handle(reader);
				
		List<Integer> opCodes = Arrays.stream(code.get().split(",")).map(Integer::valueOf).toList();
		
		restart: for (int i=0; i<Integer.MAX_VALUE; i++) {
			if (i%10000000 == 0) {
				System.out.println("Running with seed %d".formatted(i));
			}			
			/* initialize our state with the value of i */
			a.set(i);
			b.set(0);
			c.set(0);
			pc.set(0);
			sysout.clear();
			try {
				do {
					Op op = operations.get(opCodes.get((int) pc.get()));			
					int operand = opCodes.get((int) pc.get() + 1);
					int out = sysout.size();
					op.exec(sysout, pc, a, b, c, operand);
					if (sysout.size() > out) {						
						/* we wrote something to output.. ensure that the output matches our input */
						if (!compare(sysout, opCodes)) {
							continue restart;
						}
						if (sysout.size() == 2) {
							System.out.println("%d -> %s :: %s".formatted(i, StringUtils.join(sysout, ','), Long.toBinaryString(i)));
						}
					}
				} while (pc.get() < opCodes.size());
				if (sysout.size() == opCodes.size() && compare(sysout, opCodes)) {
					return i;
				}
			}
			catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("Invalid input for seed %d".formatted(i));
			}
		}
		
		return -1;
	}
	
	private static boolean compare(List<Integer> sysout, List<Integer> opCodes) {
		if (sysout.size() > opCodes.size()) {
			return false;
		}
		for (int i=0; i<sysout.size(); i++) {
			if (sysout.get(i) != opCodes.get(i)) {
				return false;
			}
		}
		return true;
	}
	
	private static void adv(List<Integer> sysout, AtomicLong pc, AtomicLong a, AtomicLong b, AtomicLong c, int operand) {
		int val = combo(operand, a, b, c);
		int d = (int) Math.pow(2, val);
		a.set(a.get() / d);		
		pc.addAndGet(2);
	}
	
	private static void bxl(List<Integer> sysout, AtomicLong pc, AtomicLong a, AtomicLong b, AtomicLong c, int operand) {
		b.set(b.get() ^ operand);
		pc.addAndGet(2);
	}
	
	private static void bst(List<Integer> sysout, AtomicLong pc, AtomicLong a, AtomicLong b, AtomicLong c, int operand) {
		int val = combo(operand, a, b, c) % 8;
		b.set(val);
		pc.addAndGet(2);
	}
	
	private static void jnz(List<Integer> sysout, AtomicLong pc, AtomicLong a, AtomicLong b, AtomicLong c, int operand) {
		if (a.get() == 0) {
			pc.addAndGet(2);
			return;
		}
		pc.set(operand);
	}
	
	private static void bxc(List<Integer> sysout, AtomicLong pc, AtomicLong a, AtomicLong b, AtomicLong c, int operand) {
		b.set(b.get() ^ c.get());
		pc.addAndGet(2);
	}
	
	private static void out(List<Integer> sysout, AtomicLong pc, AtomicLong a, AtomicLong b, AtomicLong c, int operand) {
		int val = combo(operand, a, b, c) %8;
		sysout.add(val);
		pc.addAndGet(2);
	}
	
	private static void bdv(List<Integer> sysout, AtomicLong pc, AtomicLong a, AtomicLong b, AtomicLong c, int operand) {
		int val = combo(operand, a, b, c);
		int d = (int) Math.pow(2, val);
		b.set(a.get() / d);
		pc.addAndGet(2);
	}
	
	private static void cdv(List<Integer> sysout, AtomicLong pc, AtomicLong a, AtomicLong b, AtomicLong c, int operand) {
		int val = combo(operand, a, b, c);
		int d = (int) Math.pow(2, val);
		c.set(a.get() / d);
		pc.addAndGet(2);
	}

	private static int combo(int operand, AtomicLong a, AtomicLong b, AtomicLong c) {
		switch (operand) {
			case 0:
			case 1:
			case 2:
			case 3:
				return operand;
			case 4:
				return (int) a.get();
			case 5:
				return (int) b.get();
			case 6:
				return (int) c.get();
			default:
				throw new IllegalArgumentException("Invalid combo opcode");
		}
	}
	
	@FunctionalInterface
	private interface Op {
		void exec(List<Integer> sysout, AtomicLong pc, AtomicLong a, AtomicLong b, AtomicLong c, int operand);
	}
}