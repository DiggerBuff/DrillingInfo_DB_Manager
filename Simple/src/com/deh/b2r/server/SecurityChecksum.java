package com.deh.b2r.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityChecksum {
	public static String hashPassword(String password) throws NoSuchAlgorithmException{
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] b = md.digest();
		StringBuffer sb = new StringBuffer();
		for(byte b1 : b){
			sb.append(Integer.toHexString(b1 & 0xff).toString());
		}
		
	//	System.out.println("This password is printed from SecurityChecksum method " + sb.toString());
		return sb.toString();
		
	}

}
