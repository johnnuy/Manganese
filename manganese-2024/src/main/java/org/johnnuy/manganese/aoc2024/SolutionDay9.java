package org.johnnuy.manganese.aoc2024;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.Timer;
import org.springframework.util.Assert;

public class SolutionDay9 {

	private static Timer<Long> timer = new Timer<>();

	public static void main(String[] args) throws IOException {
		System.out.println("Sample 1: %d\n".formatted(timer.time("Sample 1", () -> process(new ClassPathReader("day9/sample_1.txt"), SolutionDay9::compactIndividual))));
		System.out.println("Problem 1: %d\n".formatted(timer.time("Problem 1", () -> process(new ClassPathReader("day9/input_1.txt"), SolutionDay9::compactIndividual))));

		System.out.println("Sample 2: %d\n".formatted(timer.time("Sample 2", () -> process(new ClassPathReader("day9/sample_2.txt"), SolutionDay9::compactComplete))));
		System.out.println("Problem 2: %d\n".formatted(timer.time("Problem 2", () -> process(new ClassPathReader("day9/input_2.txt"), SolutionDay9::compactComplete))));
	}

	public static Long process(Reader reader, Consumer<List<Integer>> compaction) {

		AtomicLong value = new AtomicLong();

		new LineHandler((line, index) -> {

			List<Integer> expansion = expand(line);

			compaction.accept(expansion);

			value.set(calculate(expansion));

			return true;
		}).handle(reader);

		return value.get();
	}

	private static List<Integer> expand(String line) {
		List<Integer> expansion = new ArrayList<>();
		int fileId = -1;
		for (int i = 0; i < line.length(); i++) {
			if (i % 2 == 0) {
				fileId++;
				for (int j = 0; j < Integer.valueOf("%s".formatted(line.charAt(i))); j++) {
					expansion.add(fileId);
				}
			} else {
				for (int j = 0; j < Integer.valueOf("%s".formatted(line.charAt(i))); j++) {
					expansion.add(-1);
				}
			}
		}
		return expansion;
	}

	private static void compactIndividual(List<Integer> list) {
		int tail = list.size() - 1;
		for (int head = 0; head < tail; head++) {
			if (list.get(head) == -1) {
				while (list.get(tail) == -1) {
					tail--;
				}
				if (tail < head) {
					break;
				}
				list.set(head, list.get(tail));
				list.set(tail, -1);
				tail--;
			}
		}
	}

	private static void compactComplete(List<Integer> list) {
		/* initialize tail to the end of the list */
		int tail = list.size() - 1;
		int lastFileId = list.get(tail);

		/* attempt to move files from the end forward */
		for (int f = lastFileId; f >= 0; f--) {
			int fileSize = 0;
			while (tail >= 0 && list.get(tail) == f) {
				fileSize++;
				tail--;
			}

			/* start from our head, and search for a continuous open section */
			int count = 0;
			for (int i = 0; i <= tail; i++) {
				if (list.get(i) == -1) {
					count++;
					/* found a contiguous block, so move the data */
					if (count == fileSize) {
						for (int j = 0; j < fileSize; j++) {
							Assert.isTrue(list.get(i + 1 - fileSize + j) == -1, "element at %d was not -1".formatted(i + 1 - fileSize + j));
							list.set(i + 1 - fileSize + j, list.get(tail + 1 + j));
							list.set(tail + 1 + j, -1);
						}
						break;
					}
				} else {
					count = 0;
				}
			}

			while (tail >= 0 && (list.get(tail) != (f - 1))) {
				tail--;
			}
		}
	}

	private static long calculate(List<Integer> list) {
		long value = 0;
		for (int pos = 0; pos < list.size(); pos++) {
			if (list.get(pos) != -1) {
				value += (pos * list.get(pos));
			}
		}
		return value;
	}
}