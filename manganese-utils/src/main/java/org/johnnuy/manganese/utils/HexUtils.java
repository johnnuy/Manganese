package org.johnnuy.manganese.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class HexUtils {

	/* string of positional characters for a hex string */
	private static String HEXCHARS = "0123456789abcdef";
		
	/**
	 * reads hex characters from the reader, and writes the bytes
	 * to the output stream provided
	 * @param reader
	 * @param out
	 */
	public static void readHex(Reader reader, OutputStream out) throws IOException {
		char[] buffer = new char[2];
		int count = 0;
		int total = 1;
		int[] tmp = new int[2];
		int writes = 0;
		while ((count = reader.read(buffer, 0, 2)) != -1) {
			if (count == 1) {
				throw new IllegalArgumentException("Incomplete hex code at position " + total + ".");
			}
			if (buffer[0] >= '0' && buffer[0] <= '9') {
				tmp[0] = (buffer[0] - '0');
			}
			else if (buffer[0] >= 'a' && buffer[0] <= 'f') {
				tmp[0] = (buffer[0] - 'a' + 10);
			}
			else {
				throw new IllegalArgumentException("Invalid hex value at position " + total + ".");
			}
			if (buffer[1] >= '0' && buffer[1] <= '9') {
				tmp[1] = (buffer[1] - '0');
			}
			else if (buffer[1] >= 'a' && buffer[1] <= 'f') {
				tmp[1] = (buffer[1] - 'a' + 10);
			}
			else {
				throw new IllegalArgumentException("Invalid hex value at position " + (total + 1) + ".");
			}
			out.write(tmp[0] << 4 | tmp[1]);
			writes++;
			if (writes % 1024 == 0) {
				writes = 0;
				out.flush();
			}
		}
	}
	
	/**
	 * reads data from the input stream, and writes the hex values to the writer
	 * @param in
	 * @param writer 
	 * @throws IOException
	 */
	public static void writeHex(InputStream in, Writer writer) throws IOException {
		int count = 0;
		byte[] buffer = new byte[1024];
		while ((count = in.read(buffer, 0, 1024)) != -1) {
			for (int i=0; i<count; i++) {
				byte b = buffer[i];
				int i1 = ((int)b >> 4) & 0x0000000F;
				int i2 = ((int)b & 0x0000000F);
				writer.append(HEXCHARS.charAt(i1));
				writer.append(HEXCHARS.charAt(i2));				
			}
			writer.flush();
		}
	}

	/**
	 * returns the byte array for the given hex string
	 * @param input
	 * @return
	 */
	public static byte[] readHex(String input) {
		if (input == null || input.length() == 0) {
			return new byte[0];
		}
		if (input.length() % 2 != 0) {
			throw new IllegalArgumentException("Incomplete hex code at position " + input.length() + "."); 
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int[] tmp = new int[2];
		char c;
		for (int i=0; i<input.length();) {
			c = input.charAt(i++);
			if (c >= '0' && c <= '9') {
				tmp[0] = (c - '0');
			}
			else if (c >= 'a' && c <= 'f') {
				tmp[0] = (c - 'a' + 10);
			}
			else if (c >= 'A' && c <= 'F') {
				tmp[0] = (c - 'A' + 10);
			}
			else {
				throw new IllegalArgumentException(String.format("Invalid hex value %s at position %d in input %s.", c, i, input));
			}
			c = input.charAt(i++);
			if (c >= '0' && c <= '9') {
				tmp[1] = (c - '0');
			}
			else if (c >= 'a' && c <= 'f') {
				tmp[1] = (c - 'a' + 10);
			}
			else if (c >= 'A' && c <= 'F') {
				tmp[1] = (c - 'A' + 10);
			}
			else {
				throw new IllegalArgumentException("Invalid hex value at position " + i + ".");
			}
			baos.write((tmp[0] << 4 | tmp[1]));
		}	
		return baos.toByteArray();
	}
	
	/**
	 * Returns the hex String for the given byte[] input
	 * @param input
	 * @return
	 */
	public static String writeHex(byte[] input) {
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<input.length; i++) {	
			byte b = input[i];
			int i1 = ((int)b >> 4) & 0x0000000F;
			int i2 = ((int)b & 0x0000000F);
			builder.append(HEXCHARS.charAt(i1));
			builder.append(HEXCHARS.charAt(i2));
		}
		return builder.toString();
	}
}
