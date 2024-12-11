package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.DataUtils;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Timer;

public class SolutionDay11 {

	private static Timer<Long> timer = new Timer<>();

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> process(new ClassPathReader("day11/sample_1.txt"), 25))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> process(new ClassPathReader("day11/input_1.txt"), 25))));

		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> process(new ClassPathReader("day11/sample_2.txt"), 75))));
		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> process(new ClassPathReader("day11/input_2.txt"), 75))));
	}

	public static Long process(Reader reader, int blinks) {
		LinkedList<Long> stones = new LinkedList<>();

		new LineHandler((line, index) -> {
			stones.addAll(Arrays.stream(line.split("\\s+")).map(Long::valueOf).toList());
			return true;
		}).handle(reader);		
		
		/* use a cache to prevent repeated calculations */
		Map<Pair<Long, Integer>, Long> cache = new HashMap<>();		
		return stones.stream().map((stone) -> blink(stone, blinks, cache)).reduce((x, y) -> x + y).get();
	}
	
	private static long blink(Long stone, int blinks, Map<Pair<Long, Integer>, Long> cache) {		
		if (blinks == 0) {
			return 1L;
		}
		
		if (cache.containsKey(Pair.of(stone, blinks))) {
			return cache.get(Pair.of(stone, blinks));
		}

		if (stone == 0L) {
			long count = blink(1L, blinks -1, cache);
			cache.put(Pair.of(stone, blinks), count);
			return count;
		}
		
		int digits = DataUtils.digits(stone);
		if (digits %2 == 0) {
			Pair<Long, Long> split = split(stone, digits / 2);
			long count = blink(split.getLeft(), blinks - 1, cache) + blink(split.getRight(), blinks - 1, cache);
			cache.put(Pair.of(stone, blinks), count);
			return count;
		}
		
		long count = blink(stone * 2024, blinks -1, cache);
		cache.put(Pair.of(stone, blinks), count);
		return count;
		
	}
	
	
	
	/**
	 * splits the number into 2 at the digits boundary
	 * @param number
	 * @param digits
	 * @return
	 */
	private static Pair<Long, Long> split(long number, int digits) {        
		long divisor = (long) Math.pow(10, digits);        
        return Pair.of(number / divisor, number % divisor);
	}
}