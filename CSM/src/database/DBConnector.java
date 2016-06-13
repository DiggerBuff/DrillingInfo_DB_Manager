package database;

import java.util.HashMap;
import java.util.Map;

import server.Fred;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Currently handles pulling jars from S3, verification, and version control.
 *
 */
public class DBConnector {

	private static String bucketName     = "drilling-info-bucket";
	private static String updatedJarName = "";
	private static String prefix = "DrillingInfo_Updates/";
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
	 * metadata contains the user metadata "bundle-symbolicname" and "version"
	 *  
	 * @return The map of all jars on the S3 server to their metadata
	 */
	public  Map<String,ObjectMetadata> getAllJars() {

		Map<String,ObjectMetadata> jars = new HashMap<String,ObjectMetadata>();
		System.out.println("\nDB search start");
		long start = System.currentTimeMillis();
		for (S3ObjectSummary objectSummary : S3Objects.withPrefix(s3client, bucketName, prefix)) {
			if(!objectSummary.getKey().replaceFirst(prefix, "").equals("")) {
				//System.out.println(" - " + objectSummary.getKey().replaceFirst(prefix, ""));
				jars.put(objectSummary.getKey().replaceFirst(prefix, ""), s3client.getObjectMetadata(bucketName, objectSummary.getKey()));				}
		}
		long stop = System.currentTimeMillis();
		System.out.println("DB search end. Elapsed time : " + (stop - start) + " ms\n" );
		return jars;
	}

	/**
	 * Downloads the s3object from s3 of the given name. 
	 * 
	 * @param fileName The name of the file to download.
	 * @return The s3object that was downloaded. 
	 */
	public S3Object downloadFile(String fileName)
	{
		System.out.println("Downloading an object\n");
		S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, prefix + fileName));
		//if (s3object == null) return null;

		return s3object;
	}

	/**
	 * Get the name of the jar that is wanted
	 * 
	 * @return the name of the jar with the .jar at the end. 
	 */
	public static String getUpdatedJarName() {
		return updatedJarName + ".jar";
	}

	/**
	 * Tries to access a file to see if it exists. 
	 * 
	 * @param path The name of the file to see if it exists. 
	 * @return True if the file exists. False otherwise. 
	 * @throws AmazonClientException
	 * @throws AmazonServiceException
	 */
	/*public boolean isValidFile(String path){
		boolean isValidFile = true;
		try {
			s3client.getObjectMetadata(bucketName, path);
		} catch (AmazonS3Exception s3e) {
			if (s3e.getStatusCode() == 404) {
				// i.e. 404: NoSuchKey - The specified key does not exist
				isValidFile = false;
			}
			else {
				throw s3e;    // rethrow all S3 exceptions other than 404   
			}
		}

		return isValidFile;
	}*/
}
