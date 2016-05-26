package com.deh.b2r.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
//import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

/**
 * Used to store the data types.
 *
 */

public final class AddressBook
{
	  private List<SharedRep.Address> streets = new ArrayList<>();
	  
	  private static String bucketName     = "drilling-info-bucket";
	  private static String keyName        = "AddressBook.txt";
	  private static String uploadFileName = "AddressBook.txt";
	  private final AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
	  
	  /**
	   * Initializer pulls the file from AmazonS3 and creates a local copy.
	   * This local copy is removed when the program ends.
	   * 
	   */
	  
	  public AddressBook()
	  {		  
		  try {
			  	File file = new File(uploadFileName);
			  	file.createNewFile();
			  	file.deleteOnExit();
			  	FileWriter writer = new FileWriter(file);            	
            	BufferedWriter bufferedWriter = new BufferedWriter(writer);
	            
			  	System.out.println("Downloading an object\n");
	            S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));
	            BufferedReader reader = new BufferedReader(new InputStreamReader(s3object.getObjectContent()));
	            while (true) {
	                String line = reader.readLine();
	                if (line == null) break;
	                SharedRep.Address add = new SharedRep.Address(line);
	                streets.add(add);
	                bufferedWriter.write(add.toString());
	                bufferedWriter.newLine();
	            }
	           
	            bufferedWriter.close();
	            reader.close();
	            
	            /*System.out.println("Uploading a new object to S3 from a file\n");
	            s3client.putObject(new PutObjectRequest(bucketName, keyName, file));*/
	 
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (AmazonServiceException ase) {
	        	if (ase.getStatusCode() == 404)
	        	{
	        		System.out.println("No Address Book found. Creating...");
	        		
	        		try{
	        			File file = new File(uploadFileName);
	        			System.out.println("Updating the object in S3 from a file\n");
	        			s3client.putObject(new PutObjectRequest(bucketName, keyName, file));
	        		} catch (AmazonServiceException ase2) {
	    	            System.out.println("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");
	    	            System.out.println("Error Message:    " + ase2.getMessage());
	    	            System.out.println("HTTP Status Code: " + ase2.getStatusCode());
	    	            System.out.println("AWS Error Code:   " + ase2.getErrorCode());
	    	            System.out.println("Error Type:       " + ase2.getErrorType());
	    	            System.out.println("Request ID:       " + ase2.getRequestId());
	    	        } catch (AmazonClientException ace2) {
	    	            System.out.println("Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");
	    	            System.out.println("Error Message: " + ace2.getMessage());
	    	        }
	        	}
	        	else {
		            System.out.println("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");
		            System.out.println("Error Message:    " + ase.getMessage());
		            System.out.println("HTTP Status Code: " + ase.getStatusCode());
		            System.out.println("AWS Error Code:   " + ase.getErrorCode());
		            System.out.println("Error Type:       " + ase.getErrorType());
		            System.out.println("Request ID:       " + ase.getRequestId());
	        	}
	        } catch (AmazonClientException ace) {
	            System.out.println("Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");
	            System.out.println("Error Message: " + ace.getMessage());
	        }
	  }
	  
	  /**
	   * Adds a street to the list, local file, and AmazonS3.
	   * Called for POST
	   * 
	   * @param street	The <code>SharedRep.Address</code> to add.
	   */
	  
	  public void addStreet(SharedRep.Address street)
	  {
		  try {
			  	File file = new File(uploadFileName);
            	FileWriter writer = new FileWriter(file, true);
            	BufferedWriter bufferedWriter = new BufferedWriter(writer);
                
            	if (!streets.contains(street)){
            		System.out.println("Add Street: " + street);
            		
	            	streets.add(street);
	                
	            	bufferedWriter.write(street.toString());
	            	bufferedWriter.newLine();
	            	bufferedWriter.close();
		            
	            	System.out.println("Updating the object in S3 from a file\n");
		            s3client.putObject(new PutObjectRequest(bucketName, keyName, file));
	            }
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
	   * Passes a copy of the list of streets.
	   * 
	   * @return	List{@literal<SharedRep.Address>} of streets.
	   */
	  
	  public List<SharedRep.Address> getStreets()
	  {
		  List<SharedRep.Address> temp = new ArrayList<>(streets);
		  return temp;
	  }
}