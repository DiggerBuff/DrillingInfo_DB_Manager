package com.deh.b2r.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

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
	private static Logger logger;
	private final String logLocation = "log.txt";
	
	/**
	 * TODO make a better initializer
	 */
	public Updater() {
		logger = Logger.getLogger(Updater.class);
		String log4jConfigFile = System.getProperty("user.dir") + File.separator + "resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);
	}
	
	/**
	 * This pulls a jar from Amazon S3 and places a copy in the local directory.
	 * Make sure that the jar file has a manifest in it. 
	 * 
	 * @param jar The name of the jar to get. Pass without ".jar" at the end. 
	 * @throws NoSuchAlgorithmException 
	 */
	public void get(String jar) throws NoSuchAlgorithmException {
		updatedJarName = jar;
		
		try {
			//Creates the local jar file
			File file;
		  	File originalFile = new File(updatedJarName + ".jar");
		  	if (originalFile.exists()){
		  		file = new File(updatedJarName + "_temp.jar");
		  		
		  		boolean goodFile = downloadFile(file);
	            if (goodFile) checkJarVersions(originalFile, file);
	            
		  	}
		  	else {
		  		file = new File(updatedJarName + ".jar");
		  		
		  		downloadFile(file);
		  	}
        } catch (IOException e) {
            e.printStackTrace();
        }catch (AmazonServiceException ase) {
            logger.error("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");
            logger.error("Error Message:    " + ase.getMessage());
            logger.error("HTTP Status Code: " + ase.getStatusCode());
            logger.error("AWS Error Code:   " + ase.getErrorCode());
            logger.error("Error Type:       " + ase.getErrorType());
            logger.error("Request ID:       " + ase.getRequestId());
            
            File file = new File(updatedJarName + ".jar");
            file.delete();
        } catch (AmazonClientException ace) {
        	logger.error("Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");
        	logger.error("Error Message: " + ace.getMessage());
        }
	}
	
	/**
	 * Pulls the file from S3. Also checks the MD5 for security.
	 * Note that there may be a problem if the ETag from Amazon is not in MD5 format. This may be the case for massive files. 
	 * 
	 * @param file the File to download
	 * @return true if the MD5 checksums matched, false otherwise
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	private boolean downloadFile(File file) throws IOException, NoSuchAlgorithmException {
		boolean sumsMatched = false;
		file.createNewFile();
        
	  	//Downloads the file from S3 and puts it's contents into a output stream
	  	System.out.println("Downloading an object\n");
        S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, updatedJarName + ".jar"));
        if (s3object == null) return sumsMatched;
        String s3sum = s3object.getObjectMetadata().getETag();
        
        //Create the streams
        InputStream reader = new BufferedInputStream(s3object.getObjectContent());
        OutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(reader, baos);
        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        
        //Checksum here
        bais.reset();
        MessageDigest md = MessageDigest.getInstance("MD5");
        String generatedSum = SecurityChecksum.getDigest(bais, md);
        
        if (generatedSum.equals(s3sum)) {
	        //Pumps the file into the local file
	        bais.reset();
	        int read = -1;
	        while ((read = bais.read()) != -1) {
	        	writer.write(read);
	        }
	        sumsMatched = true;
        }
        else {
        	logger.warn("MD5 Checksums did not match. S3:\"" + s3sum + "\" Local:\"" + generatedSum + "\"");
        	file.delete();
        }
        writer.flush();
        writer.close();
        bais.close();
        reader.close();
        
        return sumsMatched;
	}

	/**
	 * This compares the file based on the manifest file. 
	 * Makes the newest version file the standard name.  
	 * 
	 * @param file The original file to check against the new. 
	 * @throws IOException Caught by the caller. 
	 */
	private void checkJarVersions(File originalFile, File file) throws IOException {
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
        System.out.println("Local Version: " + originalVersionNumber);
        System.out.println("Downloaded Version: " + versionNumber);
        int verDiff = compareVersionNumbers(originalVersionNumber, versionNumber);
        
        //The downloaded version is newer
        if (verDiff > 0) {
        	file.renameTo(originalFile);
        }
        //The downloaded version is older
        else if (verDiff < 0){
        	file.delete();
        }
        //The versions are the same
        else {
        	file.delete();
        }
	}
	
	/**
	 * Compares two version numbers passed as strings.
	 *  
	 * @param originalVer The version of the original file.
	 * @param newVer The version of the new file.
	 * @return Returns positive if the new version is newer. 
	 * 				   negative if it is an older version.
	 * 				   zero if it is the same version. 
	 */
	private int compareVersionNumbers(String originalVer, String newVer) {
		String[] vals1 = originalVer.split("\\.");
	    String[] vals2 = newVer.split("\\.");
	    int i = 0;
	    // set index to first non-equal ordinal or length of shortest version string
	    while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
	      i++;
	    }
	    // compare first non-equal ordinal number
	    if (i < vals1.length && i < vals2.length) {
	        int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
	        return Integer.signum(diff) * -1;
	    }
	    // the strings are equal or one string is a substring of the other
	    // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
	    return Integer.signum(vals1.length - vals2.length) * -1;		
	}

	public static String getUpdatedJarName() {
		return updatedJarName + ".jar";
	}
	

}
