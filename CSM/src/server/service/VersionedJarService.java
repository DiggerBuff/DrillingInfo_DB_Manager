package server.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.tools.ant.DirectoryScanner;

import java.util.Map.Entry;


import com.amazonaws.services.s3.model.ObjectMetadata;

import server.DBConnector;
import sun.tools.jar.resources.jar;


public class VersionedJarService {
	
	private DBConnector dbConnector = new DBConnector();
	private Map<String, String> jarsOldToNew = new HashMap<String, String>();

	/*
	public String detect(String vjName) {
		List<String> updates = dbConnector.getAssociatedVersions(vjName);
		if (updates == null) {
			return null;
		}
		else if (updates.size() == 0) {
			return vjName + " is up to date.";
		}
		else {
			jarsOldToNew.put(vjName, updates);
			return "Detected " + updates.size() + " updates for " + vjName;
		}
	}
	*/
	
	public String detectAll() {

		Map<String, ObjectMetadata> dbJarMap = dbConnector.getAllJars();
		Map<String, ArrayList<String>> localJars = getLocalJars();
		
		Iterator<Entry<String, ObjectMetadata>> it = dbJarMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, ObjectMetadata> pair = (Map.Entry<String, ObjectMetadata>)it.next();
	        
	        String s3jar = pair.getKey().toString();
			String symName = dbJarMap.get(s3jar).getUserMetaDataOf("bundle-symbolicname");

			if(localJars.containsKey(symName)){
				if (compareVersionNumbers(localJars.get(symName), dbJarMap.get(s3jar).getUserMetaDataOf("version")) <= 0) {
					localJars.remove(symName);
					it.remove();
				}
			}
			else {
				
				//TODO What if there is a file in S3 but not in the users files. 
			}
		}
		
		if (jarsOldToNew.size() > 0) {
			return "Detected updates for " + jarsOldToNew.size() + " jars";
		}
		else {
			return null;
		}
	}
	
	/*public Map<String, ObjectMetadata> getAllJars() {
		return dbConnector.getAllJars();
	}*/
	
	
	private Map<String, ArrayList<String>> getLocalJars() {
		String pwd = System.getProperty("user.dir");
		
		// TO-DO this baseDir needs to be as close to *Transform/plugins as possible
		String baseDir = pwd.substring(0, pwd.lastIndexOf("DrillingInfo_DB_Manager/"));

		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setIncludes(new String[]{"**/Transform/plugins/**/*.jar"});
		scanner.setBasedir(baseDir);
		scanner.setCaseSensitive(false);
		String[] relativeFilePaths = scanner.getIncludedFiles();
		
		Map<String, ArrayList<String>> localJars = new HashMap<String, ArrayList<String>>();

		for (String relativeFilePath : relativeFilePaths) {
			
			System.out.println("Matched file : " + relativeFilePath);
			
			String absoluteDirPath = baseDir + relativeFilePath.substring(0, relativeFilePath.lastIndexOf('/'));
			System.out.println("AbsoluteDirPath : " + absoluteDirPath);
			
			String fileName = relativeFilePath.substring(relativeFilePath.lastIndexOf('/') + 1);
			System.out.println("FileName : " + fileName);
			
			File file = new File(absoluteDirPath, fileName);
			
			if (file.exists()) {
				try {
					JarFile jarFile;

					jarFile = new JarFile(file);

					System.out.println("Jar name : " + jarFile.getName());

					Manifest manifest = jarFile.getManifest();

					Attributes attributes = manifest.getMainAttributes();

					String version = getVersion(attributes);
					String name = getSymbolicName(attributes);

					ArrayList<String> list = new ArrayList<String>();
					list.add(version);
					list.add(file.getAbsolutePath());
					System.out.println("List Contents : " + list);

					localJars.put(name,  list);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				System.out.println("File creation broke");
			}

		}
		return localJars;
	}
	
	private String getSymbolicName(Attributes attributes) throws IOException {
		if (attributes!=null){
			Iterator<Object> it = attributes.keySet().iterator();
			while (it.hasNext()){
				Name key = (Name) it.next();
				String keyword = key.toString();
				//Different types of manifest files may require a change here.
				if (keyword.equals("Bundle-SymbolicName")){
					String result = (String)attributes.get(key);
					if(result.lastIndexOf(';') != -1){
						result = result.substring(0, result.lastIndexOf(';'));
					}
					return result;
				}
			}
		}
		throw new IOException("Could not find the Bundle-SymbolicName in the manifest file.");
	}

	private String getVersion(Attributes attributes) throws IOException {
		if (attributes!=null){
			Iterator<Object> it = attributes.keySet().iterator();
			while (it.hasNext()){
				Name key = (Name) it.next();
				String keyword = key.toString();
				//System.out.println("\"" + keyword + "\"");
				//Different types of manifest files may require a change here.
				if (keyword.equals("Bundle-Version")){
					return (String) attributes.get(key);
				}
			}
		}
		throw new IOException("Could not find the Bundle-Version in the manifest file.");
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
