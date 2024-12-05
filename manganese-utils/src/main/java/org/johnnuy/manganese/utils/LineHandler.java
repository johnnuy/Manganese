package org.johnnuy.manganese.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.function.BiFunction;

public class LineHandler {

	private BiFunction<String, Integer, Boolean> lineProcessor;
	
	public LineHandler(BiFunction<String, Integer, Boolean> lineProcessor) {
		this.lineProcessor = lineProcessor;
	}
	
	/**
	 * Handles the contents of the reader using the line processor provided
	 * @param reader
	 * @throws IOException
	 */
	public void handle(Reader reader) {
		try {
			try (BufferedReader br = new BufferedReader(reader)) {
				String nextLine = null;
				int count = 0;
				while ((nextLine = br.readLine()) != null && lineProcessor.apply(nextLine, ++count));
			}
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
