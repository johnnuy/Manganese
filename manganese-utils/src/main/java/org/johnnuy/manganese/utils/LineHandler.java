package org.johnnuy.manganese.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.function.BiFunction;

/**
 * Handler used to process the lines from a reader
 * 
 */
public class LineHandler {

	private final BiFunction<String, Integer, Boolean> lineProcessor;
	
	/**
	 * Creates a new Line Handler with the provided line processor
	 * @param lineProcessor
	 */
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
