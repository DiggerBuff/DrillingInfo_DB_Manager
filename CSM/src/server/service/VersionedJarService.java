package server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import server.DBConnector;


public class VersionedJarService {
	
	private DBConnector dbConnector = new DBConnector();
	private Map<String, ArrayList<String>> jarsOldToNew = new HashMap<String, ArrayList<String>>();

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
		Map<String, ArrayList<String>> dbJarMap = dbConnector.getAllJars();
		List<String> localJars = getLocalJars();
		
		for (String localJar : localJars) {
			String key = localJar.substring(0, localJar.lastIndexOf('_'));
			String version = localJar.substring(localJar.lastIndexOf('_') + 1);
			
			ArrayList<String> dbJars = dbJarMap.get(key);
			
			for (String dbJar : dbJars) {
				if (!(compareVersionNumbers(version, dbJar) > 0)) {
					dbJars.remove(dbJar);
				}
				else {
					dbJar = key + "_" + dbJar;
				}
			}
			
			jarsOldToNew.put(localJar, dbJars);
		}
		
		if (jarsOldToNew.size() > 0) {
			return "Detected updates for " + jarsOldToNew.size() + " jars";
		}
		else {
			return null;
		}
	}
	
	public Map<String, ArrayList<String>> getAllJars() {
		return dbConnector.getAllJars();
	}
	
	private List<String> getLocalJars() {
		// TODO Auto-generated method stub
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