package com.deh.b2r.server;

//These allow for conversion of File object to byte[]
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.Path;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;


public class SecurityChecksum {
	
	public static String hashPassword(String path) throws NoSuchAlgorithmException, FileNotFoundException{
		
		FileInputStream fis = new FileInputStream(path);
		
		
		//byte[] data = Files.readAllBytes(jarFile);
		
		MessageDigest md = MessageDigest.getInstance("MD5");
//		md.update(jarFile.getBytes());
				
		String b = getDigest(fis, md);
	//	System.out.println("This password is printed from SecurityChecksum method " + sb.toString());
		return b;
		
	}
	
	/**
	 * Generates a checksum from an input stream and a message digest.
	 * 
	 * @param is Input Stream to create a checksum for.
	 * @param md The type of checksum to create. 
	 * @return the checksum.
	 * @throws NoSuchAlgorithmException
	 */
	public static String getDigest(InputStream is, MessageDigest md)throws NoSuchAlgorithmException {
		md.reset();
		byte[] bytes = new byte[md.getDigestLength()];
		int numBytes;
		try {
			while ((numBytes = is.read(bytes)) != -1) {
				md.update(bytes, 0, numBytes);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] digest = md.digest();
		String result = new String(Hex.encodeHex(digest));
		//System.out.println(result);
		return result;
	}

}
