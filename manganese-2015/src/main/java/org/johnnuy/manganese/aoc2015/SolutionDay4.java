package org.johnnuy.manganese.aoc2015;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;

public class SolutionDay4 {

	public static void main(String[] args) throws IOException {
		System.out.println("Minimum Nonce: %d".formatted(calculateMinimumNonce(new ClassPathReader("day4/input.txt"))));
	}

	public static int calculateMinimumNonce(Reader reader) throws IOException {
		AtomicInteger nonce = new AtomicInteger();

		new LineHandler((line, index) -> {

			String md5 = "";
			do {

			} while (!StringUtils.startsWith(md5, "00000"));

			return true;
		}).handle(reader);

		return nonce.get();
	}

}