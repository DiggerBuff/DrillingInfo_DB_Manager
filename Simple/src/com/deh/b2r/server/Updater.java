package com.deh.b2r.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
//import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class Updater {
	
	private static String bucketName     = "drilling-info-bucket";
	private static String updatedJarName = "";
	private final AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
	
	public Updater() {
		
	}

	public void get(String jar) {
		// TODO Get the updated jar
		updatedJarName = jar;
		
		try {
		  	File file = new File(updatedJarName);
		  	file.createNewFile();
            
		  	System.out.println("Downloading an object\n");
            S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, updatedJarName));
            InputStream reader = new BufferedInputStream(s3object.getObjectContent());
            
            OutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
            
            System.out.println(s3object.getObjectContent().getClass());
            
            int read = -1;
            while (( read = reader.read() ) != -1) {
            	writer.write(read);
            }
           
            writer.flush();
            writer.close();
            reader.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
	}

}
