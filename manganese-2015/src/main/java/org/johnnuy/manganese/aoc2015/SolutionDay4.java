package org.johnnuy.manganese.aoc2015;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.johnnuy.manganese.utils.ClassPathReader;
import org.johnnuy.manganese.utils.LineHandler;
import org.johnnuy.manganese.utils.MD5;

public class SolutionDay4 {

	public static void main(String[] args) throws IOException {
		System.out.println("Minimum Nonce for 00000: %d"
				.formatted(calculateMinimumNonce(new ClassPathReader("day4/input.txt"), "00000")));
		System.out.println("Minimum Nonce for 000000: %d"
				.formatted(calculateMinimumNonce(new ClassPathReader("day4/input.txt"), "000000")));
	}

	/**
	 * returns the minimum nonce required to achieve the given prefix
	 * @param reader
	 * @param prefix
	 * @return
	 * @throws IOException
	 */
	public static int calculateMinimumNonce(Reader reader, String prefix) throws IOException {
		AtomicInteger nonce = new AtomicInteger();

		new LineHandler((line, index) -> {

			String md5 = "";
			do {
				md5 = MD5.digest(line + nonce.incrementAndGet(), StandardCharsets.US_ASCII);
			} while (!StringUtils.startsWith(md5, prefix) && nonce.get() < 1_000_000_000);

			return true;
		}).handle(reader);

		return nonce.get();
	}
}