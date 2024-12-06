package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Timer;
import org.springframework.util.CollectionUtils;

public class SolutionDay5 {

	private static Timer<Integer> timer = new Timer<>();
	
	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> calculate(new ClassPathReader("day5/sample_1.txt")))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> calculate(new ClassPathReader("day5/input_1.txt")))));

		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> calculateReordered(new ClassPathReader("day5/sample_2.txt")))));
		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> calculateReordered(new ClassPathReader("day5/input_2.txt")))));
	}

	
	public static int calculate(Reader reader) {
		AtomicInteger count = new AtomicInteger(0);
		Map<Integer, List<Integer>> rules = new HashMap<>(); // ruleset consists of an integer as a key, and the value is all values that cannot appear before it
		List<List<Integer>> sequences = new ArrayList<>();

		AtomicInteger section = new AtomicInteger(1);
		new LineHandler((line, index) -> {
			/* blank line separates part */
			if (StringUtils.isBlank(line)) {
				section.incrementAndGet();
			}
			/* part 1 is our rules */
			else if (section.get() == 1) {
				String[] components = line.split("\\|");
				rules.computeIfAbsent(Integer.parseInt(components[0]), k -> new ArrayList<>())
				     .add(Integer.parseInt(components[1]));
			}
			else if (section.get() == 2) {
				sequences.add(Stream.of(line.split(",")).map(Integer::valueOf).toList());
			}
			else {
				System.err.println("Invalid section %d".formatted(section.get()));
			}
			return true;
		}).handle(reader);
				
		sequences.forEach((sequence) -> {
			count.addAndGet(evaluateSequence(sequence, rules));
		});
		return count.get();
	}
	
	public static int calculateReordered(Reader reader) {
		AtomicInteger count = new AtomicInteger(0);
		Map<Integer, List<Integer>> rules = new HashMap<>(); // ruleset consists of an integer as a key, and the value is all values that cannot appear before it
		List<List<Integer>> sequences = new ArrayList<>();

		AtomicInteger section = new AtomicInteger(1);
		new LineHandler((line, index) -> {
			/* blank line separates part */
			if (StringUtils.isBlank(line)) {
				section.incrementAndGet();
			}
			/* part 1 is our rules */
			else if (section.get() == 1) {
				String[] components = line.split("\\|");
				rules.computeIfAbsent(Integer.parseInt(components[0]), k -> new ArrayList<>())
				     .add(Integer.parseInt(components[1]));
			}
			else if (section.get() == 2) {
				sequences.add(new ArrayList<>(Stream.of(line.split(",")).map(Integer::valueOf).toList()));
			}
			else {
				System.err.println("Invalid section %d".formatted(section.get()));
			}
			return true;
		}).handle(reader);
				
		sequences.forEach((sequence) -> {
			/* check if we need to re-order this rule set */
			if (evaluateSequence(sequence, rules) == 0) {
				count.addAndGet(reorderSequence(sequence, rules));
			}
		});
		return count.get();
	}
	
	/**
	 * evaluates the list against the rule set, returns the middle number if valid, else 0
	 * @param sequence
	 * @param ruleset
	 * @return
	 */
	private static int evaluateSequence(List<Integer> sequence, Map<Integer, List<Integer>> ruleset) {
		List<Integer> seen = new ArrayList<>();
		for (Integer value : sequence) {			
			/* check if we have a ruleset for this value */
			if (ruleset.containsKey(value)) {
				List<Integer> rules = ruleset.get(value);
				/* if our rules contain any of the integers we have previously seen, then we fail */
				if (CollectionUtils.containsAny(rules, seen)) {
					return 0;
				}
			}						
			seen.add(value);
		};
		/* everything is good, return the middle value */
		return sequence.get((sequence.size() - 1) / 2);
	}

	/**
	 * reorders the sequence according to the rule set, and then returns the middle element
	 * @param sequence
	 * @param ruleset
	 * @return
	 */
	private static int reorderSequence(List<Integer> sequence, Map<Integer, List<Integer>> ruleset) {
		List<Integer> seen = new ArrayList<>();
		for (int i = 0; i<sequence.size(); i++) {
			Integer value = sequence.get(i);
			
			/* check if we have a ruleset for this value */
			if (ruleset.containsKey(value)) {
				List<Integer> rules = ruleset.get(value);
				/* if our rules contain any of the integers we have previously seen, then we need to shift this value back  */
				Integer firstMatch = CollectionUtils.findFirstMatch(rules, seen);
				if (firstMatch != null) {
					int index = seen.indexOf(firstMatch);
					/* reorder by moving the element at 'i' back to the 'index' we want */
					reorder(sequence, i, index);
					/* reset some state */
					i = index;
					seen = seen.subList(0, i);
				}
			}						
			seen.add(value);
		};
		
		/* everything is good now, return the middle value */
		return sequence.get((sequence.size() - 1) / 2);
	}
	
	
	public static void reorder(List<Integer> sequence, int currentIndex, int targetIndex) {
        Integer value = sequence.remove(currentIndex);
        sequence.add(targetIndex, value);
    }
}
