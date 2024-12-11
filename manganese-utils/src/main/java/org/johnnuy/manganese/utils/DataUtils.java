package org.johnnuy.manganese.utils;

public class DataUtils {
	
	/**
	 * counts the digits in a number
	 * @param x
	 * @return
	 */
	public static int digits(long x) {
		if (x == 0) {
			return 1;
		}
		long temp = x;
        int numDigits = 0;
        while (temp > 0) {
            numDigits++;
            temp /= 10;
        }
        return numDigits;
	}
}
