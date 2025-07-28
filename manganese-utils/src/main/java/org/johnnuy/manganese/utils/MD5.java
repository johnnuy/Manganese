package org.johnnuy.manganese.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	/**
	 * returns the md5 digest of the given data encoded with the charset
	 * @param data
	 * @param charset
	 * @return
	 */
	public static String digest(String data, Charset charset) {
		return digest(data.getBytes(charset));
	}
	
	/**
	 * return the md5 digest of the given bytes
	 * @param bytes
	 * @return
	 */
	public static String digest(byte[] bytes) {
		try {
			MessageDigest msg = MessageDigest.getInstance("MD5");
			byte[] hash = msg.digest(bytes);			
			return HexUtils.writeHex(hash);
		}
		catch(NoSuchAlgorithmException e) {
			throw new RuntimeException("Unable to calculate md5", e);
		}
	}
	
	/**
	 * returns the modulus of the digest of the given bytes, used to uniformly shard the data 
	 * @param bytes
	 * @param modulus
	 * @return
	 */
	public static int digestAndModulus(byte[] bytes, int modulus) {
		try {
			MessageDigest msg = MessageDigest.getInstance("MD5");
			byte[] hash = msg.digest(bytes);
			int res = 0;
			for (int i=0; i<hash.length; i++) {
				res += ((int) hash[i] & 0x0000FF);
			}
			return res % modulus;
		}
		catch(NoSuchAlgorithmException e) {
			throw new RuntimeException("Unable to calculate md5", e);
		}
	}
}