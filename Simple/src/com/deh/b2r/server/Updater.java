package com.deh.b2r.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;
import java.util.jar.Manifest;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class Updater {
	
	private static String bucketName     = "drilling-info-bucket";
	private static String updatedJarName = "";
	private final AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
	
	/**
	 * TODO make a better initializer
	 */
	public Updater() {
		
	}
	
	/**
	 * This pulls a jar from Amazon S3 and places a copy in the local directory.
	 * Make sure that the jar file has a manifest in it. 
	 * 
	 * @param jar The name of the jar to get. Pass without ".jar" at the end. 
	 */
	public void get(String jar) {
		// TODO Get the updated jar
		updatedJarName = jar;
		
		try {
			//Creates the local jar file
		  	File file = new File(updatedJarName + "_temp.jar");
		  	file.createNewFile();
            
            
		  	//Downloads the file from S3 and puts it's contents into a output stream
		  	//TODO What if file is not found?
		  	System.out.println("Downloading an object\n");
            S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, updatedJarName + ".jar"));
            InputStream reader = new BufferedInputStream(s3object.getObjectContent());
            OutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
            
            //Pumps the file into the local file
            int read = -1;
            while ((read = reader.read()) != -1) {
            	writer.write(read);
            }
            
            writer.flush();
            writer.close();
            reader.close();
            
            checkVersions(file);
            
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
	
	/**
	 * This compares the file based on the manifest file.
	 * 
	 * @param file The original file to check against the new. 
	 * @throws IOException Caught by the caller. 
	 */
	private void checkVersions(File file) throws IOException {
		File originalFile = new File(updatedJarName + ".jar");
		JarFile originalJarFile = new JarFile(originalFile);
        Manifest originalManifest = originalJarFile.getManifest();
        
		JarFile jarFile = new JarFile(file);
        Manifest manifest = jarFile.getManifest();
        
        String originalVersionNumber = "";
        Attributes originalAttributes = originalManifest.getMainAttributes();
        if (originalAttributes!=null){
            Iterator it = originalAttributes.keySet().iterator();
            while (it.hasNext()){
                Name key = (Name) it.next();
                String keyword = key.toString();
                //Different types of manifest files may require a change here.
                if (keyword.equals("Implementation-Version") || keyword.equals("Bundle-Version") || keyword.equals("Manifest-Version")){
                    originalVersionNumber = (String) originalAttributes.get(key);
                    break;
                }
            }
        }
        
        String versionNumber = "";
        Attributes attributes = manifest.getMainAttributes();
        if (attributes!=null){
            Iterator it = attributes.keySet().iterator();
            while (it.hasNext()){
                Name key = (Name) it.next();
                String keyword = key.toString();
                //Different types of manifest files may require a change here.
                if (keyword.equals("Implementation-Version") || keyword.equals("Bundle-Version") || keyword.equals("Manifest-Version")){
                    versionNumber = (String) attributes.get(key);
                    break;
                }
            }
        }
        
        originalJarFile.close();
        jarFile.close();
        System.out.println("Original Version: " + originalVersionNumber);
        System.out.println("New Version: " + versionNumber);
	}

}
