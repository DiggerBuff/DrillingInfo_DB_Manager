package com.deh.b2r.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;

//These allow for conversion of File object to byte[]
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;


public class SecurityChecksum {
	
	public static String hashPassword(String path) throws NoSuchAlgorithmException, FileNotFoundException{
		
		FileInputStream fis = new FileInputStream(path);
		
		
//		byte[] data = Files.readAllBytes(jarFile);
		
		MessageDigest md = MessageDigest.getInstance("MD5");
//		md.update(jarFile.getBytes());
				
		byte[] b = md.digest();
		StringBuffer sb = new StringBuffer();
		for(byte b1 : b){
			sb.append(Integer.toHexString(b1 & 0xff).toString());
		}
		
	//	System.out.println("This password is printed from SecurityChecksum method " + sb.toString());
		return sb.toString();
		
	}

}
