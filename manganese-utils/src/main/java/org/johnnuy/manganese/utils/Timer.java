package org.johnnuy.manganese.utils;

import java.util.function.Supplier;

public class Timer<T> {

	public T time(String label, Supplier<T> supplier) {
		long t1 = System.nanoTime();
		try {
			return supplier.get();
		}
		finally {
			System.out.println("%s took %dms".formatted(label, (System.nanoTime() - t1) / 1_000_000));
		}
	}
}
