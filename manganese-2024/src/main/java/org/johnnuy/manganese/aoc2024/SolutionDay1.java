package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;

public class SolutionDay1 {

	public static void main(String[] args) throws IOException {
		System.out.println("Sample: %d".formatted(difference(new ClassPathReader("day1/sample_1.txt"))));
		System.out.println("Problem: %d".formatted(difference(new ClassPathReader("day1/input_1.txt"))));

		System.out.println("Sample 2: %d".formatted(similarity(new ClassPathReader("day1/sample_2.txt"))));
		System.out.println("Problem 2: %d".formatted(similarity(new ClassPathReader("day1/input_2.txt"))));
	}

	/**
	 * Part 1 Difference
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static int difference(Reader reader) throws IOException {
		List<Integer> col1 = new ArrayList<>();
		List<Integer> col2 = new ArrayList<>();

		new LineHandler((line, index) -> {
			String[] comps = line.replaceAll("\\s+", " ").split(" ");
			col1.add(Integer.parseInt(comps[0]));
			col2.add(Integer.parseInt(comps[1]));
			return true;
		}).handle(reader);

		Collections.sort(col1);
		Collections.sort(col2);

		int diff = 0;
		for (int i = 0; i < col1.size(); i++) {
			diff += Math.abs(col1.get(i) - col2.get(i));
		}

		return diff;
	}

	/**
	 * Part 2 Similarity
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static int similarity(Reader reader) throws IOException {
		Map<Integer, Integer> col1 = new HashMap<>();
		Map<Integer, Integer> col2 = new HashMap<>();

		new LineHandler((line, index) -> {
			String[] comps = line.replaceAll("\\s+", " ").split(" ");
			col1.compute(Integer.parseInt(comps[0]), (k, v) -> v == null ? 1 : v + 1);
			col2.compute(Integer.parseInt(comps[1]), (k, v) -> v == null ? 1 : v + 1);
			return true;
		}).handle(reader);

		int sim = 0;
		for (Map.Entry<Integer, Integer> entry : col1.entrySet()) {
			sim += entry.getKey() * entry.getValue() * col2.getOrDefault(entry.getKey(), 0);
		}

		return sim;
	}
}