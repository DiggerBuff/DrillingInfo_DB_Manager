package server.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
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

import org.apache.commons.io.output.ByteArrayOutputStream;

import client.LocalConnector;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import database.DBConnector;

import security.SecurityChecksum;
import server.Fred;


public class VersionedJarService {

	private DBConnector dbConnector = new DBConnector();
	private Map<String, String> jarsOldToNew = new HashMap<String, String>();

	public String detectAll() {
		Map<String, ObjectMetadata> dbJarMap = dbConnector.getAllJars();
		Map<String, ArrayList<String>> localJars = getLocalJars();

		Iterator<Entry<String, ObjectMetadata>> it = dbJarMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ObjectMetadata> pair = (Map.Entry<String, ObjectMetadata>)it.next();

			String s3jar = pair.getKey().toString();
			String symName = dbJarMap.get(s3jar).getUserMetaDataOf("bundle-symbolicname");

			System.out.println("SymName expecting to match : " + symName);
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
			return "Detected updates for " + jarsOldToNew.size() + " jars.";
		}
		else {
			return null;
		}
	}

	private Map<String, ArrayList<String>> getLocalJars() {
		return new LocalConnector().getLocalJars();
	}

	public String replace() {
		boolean sumsMatched = false;

		try {
			Iterator<Entry<String, String>> it = jarsOldToNew.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
				
				System.out.println(pair.getKey());
				S3Object s3object = dbConnector.downloadFile(pair.getValue());
				ObjectMetadata id = s3object.getObjectMetadata();
				String s3sum = id.getETag();
				String localNew = pair.getKey().substring(0, pair.getKey().lastIndexOf('/'));
				
				//Create the streams
				InputStream reader = new BufferedInputStream(s3object.getObjectContent());
				OutputStream writer = new BufferedOutputStream(new FileOutputStream(localNew));
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
				}
				writer.flush();
				writer.close();
				bais.close();
				reader.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (sumsMatched + "");
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
