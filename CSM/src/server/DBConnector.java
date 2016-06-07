package server;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.io.output.ByteArrayOutputStream;

import server.model.VersionedJar;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * TODO need to separate logger stuff into its own class
 * TODO likely need to change the setup of this to make it restful.
 * 
 * Currently handles pulling jars from S3, verification, and version control.
 *
 */
public class DBConnector {
	
	private static String bucketName     = "drilling-info-bucket";
	private static String updatedJarName = "";
	private static String prefix = "DrillingInfo_Updates/";
	private static String version = "6.0.1/plugins/";
	private final AmazonS3 s3client;
	
	/**
	 * Initializer sets up the logger for errors.
	 */
	public DBConnector() {
        s3client = new AmazonS3Client(new ProfileCredentialsProvider());
	}
	
	/**
	 * Get the jars that are up on S3. 
	 * Use this to determine which jars have updates available.
	 *  
	 * @return The list of the possible jars
	 */
	public  List<String> getAllJars() {
		List<String> jars = new ArrayList<String>();
		
		try {
            System.out.println("Listing objects");
            final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName);
            ListObjectsV2Result result;
            do {               
               result = s3client.listObjectsV2(req);
               
               for (S3ObjectSummary objectSummary : S3Objects.withPrefix(s3client, bucketName, prefix + version)) {
                   System.out.println(" - " + objectSummary.getKey());
                   jars.add(objectSummary.getKey());
               }
               req.setContinuationToken(result.getNextContinuationToken());
            } while(result.isTruncated() == true ); 
            
         } catch (AmazonServiceException ase) {
             Fred.logger.error("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");
             Fred.logger.error("Error Message:    " + ase.getMessage());
             Fred.logger.error("HTTP Status Code: " + ase.getStatusCode());
             Fred.logger.error("AWS Error Code:   " + ase.getErrorCode());
             Fred.logger.error("Error Type:       " + ase.getErrorType());
             Fred.logger.error("Request ID:       " + ase.getRequestId());
             
             File file = new File(updatedJarName + ".jar");
             file.delete();
         } catch (AmazonClientException ace) {
         	Fred.logger.error("Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");
         	Fred.logger.error("Error Message: " + ace.getMessage());
         }
		
		return jars;
	}
	
	/**
	 * This pulls a jar from Amazon S3 and places a copy in the local directory.
	 * Make sure that the jar file has a manifest in it. 
	 * 
	 * @param jar The name of the jar to get. Pass without ".jar" at the end. 
	 * @return 
	 * @throws NoSuchAlgorithmException 
	 */
	public VersionedJar getVersionedJar(String jar) throws NoSuchAlgorithmException {
		updatedJarName = jar;

		try {
			//Creates the local jar file
		  	File file = new File(updatedJarName + ".jar");
		  	if (file.exists()){		  		
		  		if (checkJarVersions(file) > 0) {
		  			downloadFile(file);
		        }
		  	}
		  	else {
		  		downloadFile(file);
		  	}
		  	
		  	return new VersionedJar(getVersionNumber(file), file);
		  	
        } catch (IOException e) {
            e.printStackTrace();
        }catch (AmazonServiceException ase) {
            Fred.logger.error("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");
            Fred.logger.error("Error Message:    " + ase.getMessage());
            Fred.logger.error("HTTP Status Code: " + ase.getStatusCode());
            Fred.logger.error("AWS Error Code:   " + ase.getErrorCode());
            Fred.logger.error("Error Type:       " + ase.getErrorType());
            Fred.logger.error("Request ID:       " + ase.getRequestId());
            
            File file = new File(updatedJarName + ".jar");
            file.delete();
        } catch (AmazonClientException ace) {
        	Fred.logger.error("Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");
        	Fred.logger.error("Error Message: " + ace.getMessage());
        }
		return null;
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
        ObjectMetadata id = s3client.getObjectMetadata(bucketName, updatedJarName + ".jar");
        String s3sum = id.getETag();
        
        //Create the streams
        InputStream reader = new BufferedInputStream(s3object.getObjectContent());
        OutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
        //To test if it will pull corrupt files, un-comment below. 
        //reader.read();
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
        	Fred.logger.warn("MD5 Checksums did not match. S3:\"" + s3sum + "\" Local:\"" + generatedSum + "\"");
        	file.delete();
        }
        writer.flush();
        writer.close();
        bais.close();
        reader.close();
        
        return sumsMatched;
	}

	
	/**
	 * Get the name of the jar that is wanted
	 * 
	 * @return the name of the jar with the .jar at the end. 
	 */
	public static String getUpdatedJarName() {
		return updatedJarName + ".jar";
	}
}
