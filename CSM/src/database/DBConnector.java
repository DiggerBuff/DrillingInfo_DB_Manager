package database;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Currently handles pulling of the jars and their meta-information from S3.
 *
 */
public class DBConnector {
	
	//This is where you would would change the S3 bucket name.  
	private static String bucketName = "drilling-info-bucket";
	//This is where you would change the name of the folder in S3 that holds all the jars. 
	private static String prefix = "DrillingInfo_Updates/";
	
	private final AmazonS3 s3client;

	/**
	 * Initializer sets up the s3client.
	 * 
	 * TODO set up a logger to get rid of the warning. 
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
}
