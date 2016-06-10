package client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;

import org.apache.tools.ant.DirectoryScanner;

/**
 * The name is a bit of a misnomer since it doesn't actually connect to something.
 *
 */
public class LocalConnector {

	private Map<String, ArrayList<String>> localJars = new HashMap<String, ArrayList<String>>();

	/**
	 * This populates a map with the local Transform/plugins jars
	 * 
	 * @return A map of the local jars 
	 * 			{key = symbolic name,
	 * 			 value = List[version number, absolute local path]}
	 * @throws TODO
	 */
	public Map<String, ArrayList<String>> getLocalJars() {

		String pwd = System.getProperty("user.dir");
		System.out.println(pwd);

		/*TODO This is not the base directory but the scan takes forever 
		 * if you aren't close to the correct directory.
		 */
		String baseDir = pwd.substring(0, pwd.lastIndexOf("CSM"));

		DirectoryScanner scanner = setUpScanner(baseDir);

		System.out.println("scan starting");
		scanner.scan();
		System.out.println("scan ended");

		String[] relativeFilePaths = scanner.getIncludedFiles();

		for (String relativeFilePath : relativeFilePaths) {

			System.out.println("Matched file : " + relativeFilePath);

			String absoluteDirPath = baseDir + relativeFilePath.substring(0, relativeFilePath.lastIndexOf(File.separatorChar));
			String fileName = relativeFilePath.substring(relativeFilePath.lastIndexOf(File.separatorChar) + 1);
			File file = new File(absoluteDirPath, fileName);

			if (file.exists()) {
				addToMap(file);
			}
			else {
				System.out.println("File creation broke");
			}
		}
		return localJars;
	}

	/**
	 * This sets up the DirectoryScanner to find the plugins.
	 * 
	 * @param baseDir the base directory from where to start scanning.
	 * @return The DirectoryScanner object. 
	 */
	private DirectoryScanner setUpScanner(String baseDir) {
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setIncludes(new String[]{"**/Transform/plugins/**/*.jar"});
		scanner.setBasedir(baseDir);
		scanner.setCaseSensitive(false);
		return scanner;
	}

	/**
	 * This gets the appropriate data from each jar's manifest
	 * and adds a new pair to the map.
	 * 
	 * @param file
	 * @throws IOException TODO
	 */
	private void addToMap(File file) {
		try {
			JarFile jarFile = new JarFile(file);

			Manifest manifest = jarFile.getManifest();

			jarFile.close();

			Attributes attributes = manifest.getMainAttributes();

			String version = getVersion(attributes);
			String name = getSymbolicName(attributes);

			ArrayList<String> list = new ArrayList<String>();
			list.add(version);
			list.add(file.getAbsolutePath());
			System.out.println("Key : " + name + " List Contents : " + list);

			localJars.put(name,  list);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
		}
	}

	/**
	 * This gets the symbolic name of the jar based on the Bundle-SymbolicName in the manifest.
	 * 
	 * @param attributes the manifest attributes.
	 * @return The symbolic name as a String. 
	 * @throws IOException
	 */
	private String getSymbolicName(Attributes attributes) throws IOException {
		if (attributes!=null){
			Iterator<Object> it = attributes.keySet().iterator();
			while (it.hasNext()){
				Name key = (Name) it.next();
				String keyword = key.toString();
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

	/**
	 * This gets the version of the jar based on the Bundle-Version in the manifest.
	 * 
	 * @param attributes the manifest attributes.
	 * @return The version number as a String. 
	 * @throws IOException
	 */
	private String getVersion(Attributes attributes) throws IOException {
		if (attributes!=null){
			Iterator<Object> it = attributes.keySet().iterator();
			while (it.hasNext()){
				Name key = (Name) it.next();
				String keyword = key.toString();
				if (keyword.equals("Bundle-Version")){
					return (String) attributes.get(key);
				}
			}
		}
		throw new IOException("Could not find the Bundle-Version in the manifest file.");
	}
}
