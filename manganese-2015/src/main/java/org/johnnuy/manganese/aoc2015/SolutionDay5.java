package org.johnnuy.manganese.aoc2015;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.atomic.AtomicInteger;

import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;

public class SolutionDay5 {

	public static void main(String[] args) throws IOException {
		System.out.println("Minimum Nonce for 00000: %d"
				.formatted(doWork(new ClassPathReader("day5/input.txt"))));
	}

	/**
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static int doWork(Reader reader) throws IOException {
		AtomicInteger nonce = new AtomicInteger();

		new LineHandler((line, index) -> {

			return true;
		}).handle(reader);

		return nonce.get();
	}
}