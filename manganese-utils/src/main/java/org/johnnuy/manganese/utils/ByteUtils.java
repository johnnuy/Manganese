package org.johnnuy.manganese.utils;

import java.math.BigInteger;

public class ByteUtils {

	/**
	 * converts bytes to an 8 byte long value with little endian byte order
	 * @param byteArray
	 * @param offset
	 * @return
	 */
	public static long byteArrayToLongLittleEndian(byte[] byteArray) {
		return (((long) byteArray[0] & 0xFF) |
				(((long) byteArray[1] & 0xFF) << 8) |
				(((long) byteArray[2] & 0xFF) << 16) |
				(((long) byteArray[3] & 0xFF) << 24) |
				(((long) byteArray[4] & 0xFF) << 32) |
				(((long) byteArray[5] & 0xFF) << 40) |
				(((long) byteArray[6] & 0xFF) << 48) |
				(((long) byteArray[7] & 0xFF) << 56));
	}

	/**
	 * converts the long value to an array of bytes with little endian byte order
	 * @param longValue
	 * @return
	 */
	public static byte[] longToByteArrayLittleEndian(long longValue) {
		return new byte[] {
				(byte) (longValue >> 0),
				(byte) (longValue >> 8),
				(byte) (longValue >> 16),
				(byte) (longValue >> 24),
				(byte) (longValue >> 32),
				(byte) (longValue >> 40),
				(byte) (longValue >> 48),
				(byte) (longValue >> 56)
		};
	}
	
	/**
	 * returns the big endian bytes for the big integer
	 * @param value
	 * @return
	 */
	public static byte[] bigIntegerToBytesBigEndian(BigInteger value) {
		return value.toByteArray();
	}
	
	/**
	 * converts the long value to an array of bytes with big endian byte order
	 * @param longValue
	 * @return
	 */
	public static byte[] longToBytesBigEndian(long longValue) {
		return new byte[] {
				(byte) (longValue >> 56),
				(byte) (longValue >> 48),
				(byte) (longValue >> 40),
				(byte) (longValue >> 32),
				(byte) (longValue >> 24),
				(byte) (longValue >> 16),
				(byte) (longValue >> 8),
				(byte) (longValue >> 0)
		};
	}	
	
	public static BigInteger bytesToBigIntegerBigEndian(byte[] bytes) {
		return new BigInteger(bytes);
	}
	
	/**
	 * converts a set of bytes to long big endian
	 * @param bytes
	 * @return
	 */
	public static Long bytesToLongBigEndian(byte[] bytes) {
		switch (bytes.length) {
		case 0:
			return 0L;
		case 1:
			return (long) bytes[0] & 0xFF;
		case 2:
			return (((long) bytes[0] & 0xFF) << 8) |
					(((long) bytes[1] & 0xFF));
		case 3:
			return (((long) bytes[0] & 0xFF) << 16) |
					(((long) bytes[1] & 0xFF) << 8) |
					(((long) bytes[2] & 0xFF));
		case 4:
			return (((long) bytes[0] & 0xFF) << 24) |
					(((long) bytes[1] & 0xFF) << 16) |
					(((long) bytes[2] & 0xFF) << 8) |
					(((long) bytes[3] & 0xFF));
		case 5:
			return (((long) bytes[0] & 0xFF) << 32) |
					(((long) bytes[1] & 0xFF) << 24) |
					(((long) bytes[2] & 0xFF) << 16) |
					(((long) bytes[3] & 0xFF) << 8) |
					(((long) bytes[4] & 0xFF));
		case 6:
			return (((long) bytes[0] & 0xFF) << 40) |
					(((long) bytes[1] & 0xFF) << 32) |
					(((long) bytes[2] & 0xFF) << 24) |
					(((long) bytes[3] & 0xFF) << 16) |
					(((long) bytes[4] & 0xFF) << 8) |
					(((long) bytes[5] & 0xFF));
		case 7:
			return (((long) bytes[0] & 0xFF) << 48) |
					(((long) bytes[1] & 0xFF) << 40) |
					(((long) bytes[2] & 0xFF) << 32) |
					(((long) bytes[3] & 0xFF) << 24) |
					(((long) bytes[4] & 0xFF) << 16) |
					(((long) bytes[5] & 0xFF) << 8) |
					(((long) bytes[6] & 0xFF));
		case 8:
			return (((long) bytes[0] & 0xFF) << 56) |
					(((long) bytes[1] & 0xFF) << 48) |
					(((long) bytes[2] & 0xFF) << 40) |
					(((long) bytes[3] & 0xFF) << 32) |
					(((long) bytes[4] & 0xFF) << 24) |
					(((long) bytes[5] & 0xFF) << 16) |
					(((long) bytes[6] & 0xFF) << 8) |
					(((long) bytes[7] & 0xFF));
		}
		throw new IllegalArgumentException("Too many bytes to convert to long");
	}
	
	/**
	 * converts the int value to an array of bytes with big endian byte order
	 * @param longValue
	 * @return
	 */
	public static byte[] IntegerToBytesBigEndian(int intValue) {
		return new byte[] {
				(byte) (intValue >> 24),
				(byte) (intValue >> 16),
				(byte) (intValue >> 8),
				(byte) (intValue >> 0)
		};
	}
	
	/**
	 * converts a set of bytes to long big endian
	 * @param bytes
	 * @return
	 */
	public static Integer bytesToIntegerBigEndian(byte[] bytes) {
		switch (bytes.length) {
		case 0:
			return 0;
		case 1:
			return (int) bytes[0] & 0xFF;
		case 2:
			return ((int) bytes[0] & 0xFF << 8) |
					((int) bytes[1] & 0xFF);
		case 3:
			return ((int) bytes[0] & 0xFF << 16) |
					((int) bytes[1] & 0xFF << 8) |
					((int) bytes[2] & 0xFF);
		case 4:
			return ((int) bytes[0] & 0xFF << 24) |
					((int) bytes[1] & 0xFF << 16) |
					((int) bytes[2] & 0xFF << 8) |
					((int) bytes[3] & 0xFF);
		}
		throw new IllegalArgumentException("Too many bytes to convert to int");
	}
}
