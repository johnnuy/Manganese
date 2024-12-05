package org.johnnuy.manganese.utils;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Timer<T> {

	
	
	public T time(String label, Supplier<T> supplier) {
		long t1 = System.nanoTime();
		try {
			return supplier.get();
		}
		finally {
			System.out.println("%s took %dms".formatted(label, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - t1)));
		}
	}
}
