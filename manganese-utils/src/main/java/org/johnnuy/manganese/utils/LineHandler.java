package org.johnnuy.manganese.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;

public class LineHandler {

	private Function<String, Boolean> lineProcessor;
	
	public LineHandler(Function<String, Boolean> lineProcessor) {
		this.lineProcessor = lineProcessor;
	}
	
	/**
	 * Handles the contents of the reader using the line processor provided
	 * @param reader
	 * @throws IOException
	 */
	public void handle(Reader reader) throws IOException {
		try (BufferedReader br = new BufferedReader(reader)) {
			String nextLine = null;
			while ((nextLine = br.readLine()) != null && lineProcessor.apply(nextLine));
		}
	}
}
