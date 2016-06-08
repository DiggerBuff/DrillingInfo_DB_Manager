package amazonS3Uploader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;

public class UpdateRunner {

	private static String bucketName = "drilling-info-bucket";
	private static String folderName = "DrillingInfo_Updates";
	private static AmazonS3 s3client;
	private static File file;

	public static void main(String[] args) {
		UpdateRunner updater = new UpdateRunner();
		Scanner kb = new Scanner(System.in);
		String fileName = "";

		System.out.println("Type \"EXIT\" to quit.");
		while(true) {
			System.out.print("What Jar or Directory of Jars do you want to upload: ");
			fileName = kb.nextLine();

			if (fileName.equals("EXIT")) break;

			file = new File(fileName);
			if (file.isDirectory()){
				updater.uploadDirectory();
			}
			else if (file.exists()){
				updater.upload();
			}
			else System.out.println("File is not found. Please try again.");
		}

		kb.close();
	}
	
	/**
	 * Upload a directory of jars. The directory should only contain .jar files.
	 * Searches for the directory in the current directory. 
	 */
	private void uploadDirectory() {
		System.out.println("Uploading new objects to S3 from a directory\n");
		TransferManager tm = new TransferManager(s3client);
		MultipleFileUpload upload = tm.uploadDirectory(bucketName, folderName, file, true, new JarMetadataProvider());

		try {
			upload.waitForCompletion();
			if (upload.isDone()) {
				System.out.println("The upload was complete.");
			}
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which " +
					"means your request made it " +
					"to Amazon S3, but was rejected with an error response" +
					" for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which " +
					"means the client encountered " +
					"an internal error while trying to " +
					"communicate with S3, " +
					"such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Here!
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Upload the single file name provided. Must be a .jar file.
	 * Searches in the current directory. 
	 */
	private void upload() {
		try {
			System.out.println("Uploading a new object to S3 from a file\n");

			if (!file.exists()) throw new IOException("No such file. Did you put in the right path?");

			JarMetadataProvider provider = new JarMetadataProvider();
			ObjectMetadata metadata = new ObjectMetadata();
			provider.provideObjectMetadata(file, metadata);

			if (metadata.getUserMetaDataOf("version") == null) throw new IOException("Could not find the Bundle-Version or Bundle-SymbolicName in the manifest file. Upload failed.");
			//System.out.println(metadata.getUserMetaDataOf("version"));

			InputStream content = new BufferedInputStream(new FileInputStream(file));

			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + "/" + file.getName(), content, metadata);

			s3client.putObject(putObjectRequest);
			
			System.out.println("The upload was complete.");

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which " +
					"means your request made it " +
					"to Amazon S3, but was rejected with an error response" +
					" for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which " +
					"means the client encountered " +
					"an internal error while trying to " +
					"communicate with S3, " +
					"such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Initializer that sets up the s3client.
	 */
	public UpdateRunner() {
		s3client = new AmazonS3Client(new ProfileCredentialsProvider());
	}
}
