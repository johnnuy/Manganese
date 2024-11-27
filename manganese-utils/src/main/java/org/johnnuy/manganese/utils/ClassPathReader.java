package org.johnnuy.manganese.utils;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Loads from class path
 */
public class ClassPathReader extends InputStreamReader {
		
	public ClassPathReader(String resource) {
		super(Thread.currentThread().getContextClassLoader().getResourceAsStream(resource), StandardCharsets.UTF_8);
	}
}
