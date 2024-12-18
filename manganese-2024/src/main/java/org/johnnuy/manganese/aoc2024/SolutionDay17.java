package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.StringUtils;
import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Timer;

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

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1a: %s\n".formatted(timer.time("Sample 1a", () -> run(new ClassPathReader("day17/sample_1.txt")))));
		System.out.println("Problem 1: %s\n".formatted(timer.time("Problem 1", () -> run(new ClassPathReader("day17/input_1.txt")))));
		
		
//		System.out.println("Sample 1b: %d\n".formatted(timer.time("Sample 1b", () -> eval(new ClassPathReader("day17/sample_1b.txt")))));		
//		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> eval(new ClassPathReader("day17/input_1.txt")))));
	}
		
	public static String run(Reader reader) {
		AtomicReference<String> code = new AtomicReference<>();
		AtomicInteger a = new AtomicInteger();
		AtomicInteger b = new AtomicInteger();
		AtomicInteger c = new AtomicInteger();
		AtomicInteger pc = new AtomicInteger(0);
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
			Op op = operations.get(opCodes.get(pc.get()));
			int operand = opCodes.get(pc.get() + 1);
			op.exec(sysout, pc, a, b, c, operand);
		} while (pc.get() < opCodes.size());
		
		
		return StringUtils.join(sysout, ',');
	}
	
	private static void adv(List<Integer> sysout, AtomicInteger pc, AtomicInteger a, AtomicInteger b, AtomicInteger c, int operand) {
		int val = combo(operand, a, b, c);
		int d = (int) Math.pow(2, val);
		a.set(a.get() / d);		
		pc.addAndGet(2);
	}
	
	private static void bxl(List<Integer> sysout, AtomicInteger pc, AtomicInteger a, AtomicInteger b, AtomicInteger c, int operand) {
		b.set(b.get() ^ operand);
		pc.addAndGet(2);
	}
	
	private static void bst(List<Integer> sysout, AtomicInteger pc, AtomicInteger a, AtomicInteger b, AtomicInteger c, int operand) {
		int val = combo(operand, a, b, c) % 8;
		b.set(val);
		pc.addAndGet(2);
	}
	
	private static void jnz(List<Integer> sysout, AtomicInteger pc, AtomicInteger a, AtomicInteger b, AtomicInteger c, int operand) {
		if (a.get() == 0) {
			pc.addAndGet(2);
			return;
		}
		pc.set(operand);
	}
	
	private static void bxc(List<Integer> sysout, AtomicInteger pc, AtomicInteger a, AtomicInteger b, AtomicInteger c, int operand) {
		b.set(b.get() ^ c.get());
		pc.addAndGet(2);
	}
	
	private static void out(List<Integer> sysout, AtomicInteger pc, AtomicInteger a, AtomicInteger b, AtomicInteger c, int operand) {
		int val = combo(operand, a, b, c) %8;
		sysout.add(val);
		pc.addAndGet(2);
	}
	
	private static void bdv(List<Integer> sysout, AtomicInteger pc, AtomicInteger a, AtomicInteger b, AtomicInteger c, int operand) {
		int val = combo(operand, a, b, c);
		int d = (int) Math.pow(2, val);
		b.set(a.get() / d);
		pc.addAndGet(2);
	}
	
	private static void cdv(List<Integer> sysout, AtomicInteger pc, AtomicInteger a, AtomicInteger b, AtomicInteger c, int operand) {
		int val = combo(operand, a, b, c);
		int d = (int) Math.pow(2, val);
		c.set(a.get() / d);
		pc.addAndGet(2);
	}

	private static int combo(int operand, AtomicInteger a, AtomicInteger b, AtomicInteger c) {
		switch (operand) {
			case 0:
			case 1:
			case 2:
			case 3:
				return operand;
			case 4:
				return a.get();
			case 5:
				return b.get();
			case 6:
				return c.get();
			default:
				throw new IllegalArgumentException("Invalid combo opcode");
		}
	}
	
	@FunctionalInterface
	private interface Op {
		void exec(List<Integer> sysout, AtomicInteger pc, AtomicInteger a, AtomicInteger b, AtomicInteger c, int operand);
	}
}