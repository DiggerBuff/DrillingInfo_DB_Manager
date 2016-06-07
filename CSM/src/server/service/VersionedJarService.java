package server.service;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;

import com.amazonaws.services.s3.model.ObjectMetadata;

import server.DBConnector;
import server.model.VersionedJar;


public class VersionedJarService {
	
	private DBConnector dbConnector = new DBConnector();
	private Map<String, List<String>> jarsOldToNew = new HashMap<String, List<String>>();

	public VersionedJar getVersionedJar(String vjName) {
		try {
			return dbConnector.getVersionedJar(vjName);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public Map<String, ArrayList<String>> getAllJars() {
		return dbConnector.getAllJars();
	}
	
	public String detect(String vjName) {
		List<String> updates = dbConnector.getAssociatedVersions(vjName);
		if (updates == null) {
			return "Database could not find a jar that matches the jar given : " + vjName;
		}
		else if (updates.size() == 0) {
			return vjName + " is up to date.";
		}
		else {
			jarsOldToNew.put(vjName, updates);
			return "Detected " + updates.size() + " updates for " + vjName;
		}
	}
	
	public String detectAll() {
		List<String> dbJars = dbConnector.getAllJars();
		List<String> localJars = getLocalJars();
		
		
	}
	
	/*
	public Person addPerson(Person newPerson) {
		newPerson.setId(people.size() + 1);
		people.put(newPerson.getId(), newPerson);
		return newPerson;
	}
	
	public Person updatePerson(Person person) {
		if(person.getId() <= 0) {
			return null;
		}
		people.put(person.getId(),  person);
		return person;
	}
	
	public Person deletePerson(long id) {
		return people.remove(id);
	}
	*/
	

	/**
	 * This compares the file based on the manifest file. 
	 * Makes the newest version file the standard name.  
	 * 
	 * @param file The original file to check against the new. 
	 * @throws IOException Caught by the caller. 
	 * @return Returns positive if the new version is newer. 
	 * 				   negative if it is an older version.
	 * 				   zero if it is the same version. 
	 */
	private int checkJarVersions(File file) throws IOException {
        
        ObjectMetadata id = s3client.getObjectMetadata(bucketName, updatedJarName + ".jar");
        //System.out.println(id.getUserMetadata().get("version"));
		//JarFile jarFile = new JarFile(file);
        //Manifest manifest = jarFile.getManifest();
        
        String originalVersionNumber = getVersionNumber(file);
        
        String versionNumber = id.getUserMetadata().get("version");
        
        
        //jarFile.close();
        System.out.println("\nLocal Version: " + originalVersionNumber);
        System.out.println("Downloaded Version: " + versionNumber);
        return compareVersionNumbers(originalVersionNumber, versionNumber);
	}
	
	/**
	 * Iterates through the main attributes of a jar until it finds specific version keywords
	 * to get the version number of the jar.
	 * 
	 * @param manifest
	 * @return version number corresponding to the keyword
	 * @throws IOException 
	 */
	private String getVersionNumber(File file) throws IOException {
		JarFile jarFile = new JarFile(file);
        Manifest manifest = jarFile.getManifest();
        jarFile.close();
		Attributes attributes = manifest.getMainAttributes();
		if (attributes!=null){
			Iterator<Object> it = attributes.keySet().iterator();
			while (it.hasNext()){
				Name key = (Name) it.next();
				String keyword = key.toString();
				//Different types of manifest files may require a change here.
				if (keyword.equals("Implementation-Version") || keyword.equals("Bundle-Version") || keyword.equals("Manifest-Version")){
					return (String) attributes.get(key);
				}
			}
		}
		return null;
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
