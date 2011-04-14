package com.nilhcem.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

/**
 * Hash input to MD5.
 * 
 * @author Nilhcem
 * @since 1.0
 */
@Service("Md5Hasher")
public class MD5Hasher {
	/**
	 * Hash a String and return its md5 signature.
	 * @param from the string we want to hash
	 * @return the md5 signature of the input string
	 * @throws Exception
	 */
	public String toMd5(String from) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.reset();
		md.update(from.getBytes(Charset.forName("UTF8")));
		final byte[] resultByte = md.digest();
		return new String(Hex.encodeHex(resultByte));
	}
}
