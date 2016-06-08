import java.io.File;
import java.io.FileFilter;

import org.apache.tools.ant.DirectoryScanner;


public class Test {
	public static void main(String[] args) {
	       System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
	       String pwd = System.getProperty("user.dir");
	       
	       String baseDir = pwd.substring(0, pwd.lastIndexOf("DrillingInfo_DB_Manager/"));

	       DirectoryScanner scanner = new DirectoryScanner();
	       scanner.setIncludes(new String[]{"**/Transform/plugins/"});
	       scanner.setBasedir(baseDir);
	       scanner.setCaseSensitive(false);
	       scanner.scan();
	       String[] files = scanner.getIncludedDirectories();
	       
	       String path = baseDir + files[0];
	       System.out.println("Path given : " + path);
	       File pluginDir = new File(baseDir + files[0]);
	       if (pluginDir.exists()) {
	    	   if (pluginDir.isDirectory()) System.out.println(pluginDir.getAbsolutePath());
	       }
	       System.out.println("It didn't exist");
	       
	       if (new File("/u/eu/dk/hhiggins/DrillingInfo_DB_Manager/production/Transform/", "plugins").exists()) {
	    	   System.out.println("Hardcoded version exists");
	       }
	       
	       for (String file : files) {
	    	   System.out.println("Matched file : " + file);
	       }

	}
}
