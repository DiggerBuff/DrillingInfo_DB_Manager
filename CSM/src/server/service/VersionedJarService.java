package server.service;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import client.LocalConnector;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import database.DBConnector;
import exception.LocalFileError;
import exception.SecurityError;

import security.SecurityChecksum;

public class VersionedJarService {

	private DBConnector dbConnector = new DBConnector();
	private Map<String, String> jarsOldToNew = new HashMap<String, String>();

	/**
	 * This will find all the local files that can be updated with files on S3. 
	 * Populates a map with the location on the local machine to the name of the file on S3.
	 * 
	 * @return The String for the response body.
	 */
	public String detectAll() {
		Map<String, ArrayList<String>> localJars = getLocalJars();
		Map<String, ObjectMetadata> dbJarMap = dbConnector.getAllJars();

		Iterator<Entry<String, ObjectMetadata>> it = dbJarMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ObjectMetadata> pair = (Map.Entry<String, ObjectMetadata>)it.next();

			String s3jar = pair.getKey().toString();
			String symName = dbJarMap.get(s3jar).getUserMetaDataOf("bundle-symbolicname");

			if(localJars.containsKey(symName)){
				if (compareVersionNumbers(localJars.get(symName).get(0), dbJarMap.get(s3jar).getUserMetaDataOf("version")) <= 0) {
					localJars.remove(symName);
					it.remove();
				}
				else {
					System.out.println("Matched S3Jar : " + s3jar + " To Local Jar : " + localJars.get(symName).get(1));
					String localPath = localJars.get(symName).get(1);
					jarsOldToNew.put(localPath, s3jar);
				}
			}
			else {
				System.out.println("Local Jars didn't have " + symName);
				//TODO What if there is a file in S3 but not in the users files. 
			}
		}

		if (jarsOldToNew.size() > 0) {
			System.out.println("Detected updates for " + jarsOldToNew.size() + " jars.");
			return "Detected updates for " + jarsOldToNew.size() + " jars.";
		}
		else {
			return null;
		}
	}

	/**
	 * This will call a method to check the local file system for the files to replace. 
	 *  
	 * @return The map of the Symbolic name to an array list of the version and the local location of the file.
	 */
	private Map<String, ArrayList<String>> getLocalJars() {
		return new LocalConnector().getLocalJars();
	}

	/**
	 * This replaces the files found by the detecting for updates. 
	 * This checks the MD5 checksum on the download as well. 
	 * Pushes the file to the location of the old file and deletes the old file.
	 * 
	 * @return If the replace worked or not.
	 */
	public boolean replace() {
		boolean fileCreated = false;

		Iterator<Entry<String, String>> it = jarsOldToNew.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();

			String localNew = pair.getKey().substring(0, pair.getKey().lastIndexOf(File.separatorChar) + 1);
			localNew = localNew + pair.getValue();
			//System.out.println(localNew);
			//System.out.println(pair.getKey());

			S3Object s3object = dbConnector.downloadFile(pair.getValue());
			fileCreated = writeJar(localNew, s3object);

			//Remove if we don't want to delete the old file
			if(fileCreated) deleteFile(pair.getKey());
			else {
				deleteFile(localNew);
				throw new SecurityError("MD5 Checksums did not match.");
			}
		}
		return fileCreated;
	}
	
	/**
	 * This will create the file from the S3Object and return if it worked or not.
	 * Will also perform the checksum checking.
	 * 
	 * @param localNew The location to make the new file.
	 * @param s3object The S3Object to create the new file from.
	 * @return
	 */
	private boolean writeJar(String localNew, S3Object s3object) {
		boolean fileCreated = false;
		try {
			String s3sum = s3object.getObjectMetadata().getETag();

			//Create the streams
			InputStream reader = new BufferedInputStream(s3object.getObjectContent());
			OutputStream writer = new BufferedOutputStream(new FileOutputStream(localNew));

			//To test if it will pull corrupt files, un-comment below. 
			//reader.read();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(reader, baos);
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
				fileCreated = true;
			}
			writer.flush();
			writer.close();
			bais.close();
			reader.close();
		} catch (IOException e) {
			throw new LocalFileError("Unable to make the new file " + localNew + ".");
		} catch (NoSuchAlgorithmException e) {
			throw new SecurityError("Unable to create the security checksum. Unable to create the new file.");
		}

		return fileCreated;
	}

	/**
	 * This will delete the old file when it is no longer needed.
	 * 
	 * @param path The path to the file to delete.
	 */
	private void deleteFile(String path) {
		File deleteFile = new File(path);
		deleteFile.delete();
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
}
