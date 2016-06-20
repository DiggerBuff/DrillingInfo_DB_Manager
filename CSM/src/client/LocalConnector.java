package client;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;

import exception.LocalFileError;

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
	 */
	public Map<String, ArrayList<String>> getLocalJars() {

		String baseDir = System.getProperty("user.home");
		//String baseDir = "C:\\Users\\Harry\\Documents\\DrillingInfo_DB_Manager";
		System.out.println(baseDir);

		System.out.println("Dir search start.");
		long start = System.currentTimeMillis();
		String pluginDir = getPath(baseDir);
		System.out.println(pluginDir);
		long stop = System.currentTimeMillis();
		System.out.println("Dir search end. Elapsed time : " + (stop - start) + " ms" );

		if(pluginDir == null) {
			throw new LocalFileError("Unable to find local Transform application.");
		}

		File dir = new File(pluginDir);
		GenericExtFilter filter = new GenericExtFilter(".jar");
		String[] relativeFilePaths = dir.list(filter);
		if (relativeFilePaths.length == 0) {
			throw new LocalFileError("Unable to find local Transform files.");
		}

		for (String relativeFilePath : relativeFilePaths) {
			System.out.println("\nFound Local Jar : " + relativeFilePath);

			File file = new File(pluginDir, relativeFilePath);

			if (file.exists()) {
				addToMap(file);
			}
			else {
				throw new LocalFileError("Unable to create a link to the local file: " + relativeFilePath);
			}
		}
		return localJars;
	}
	
	/**
	 * This class is just a filter to make sure we only pull in jars and not some other files. 
	 *
	 */
	public class GenericExtFilter implements FilenameFilter {

		private String ext;

		public GenericExtFilter(String ext) {
			this.ext = ext;
		}
		
		@Override
		public boolean accept(File dir, String name) {
			return (name.endsWith(ext));
		}
	}
	
	/**
	 * This is the recursive method that finds the local jars on the users computer. 
	 * Given a base path, it searches recursively through the sub-directories. 
	 * It ignores the following: non-folders, files that start with ".", files that cannot be read, if the file is hidden, and if the file is named "Application Data". 
	 * That last one is for Windows specifically. 
	 * If there is an error that causes the program to not work it is probably here or in SystemService.java in the server.service package. 
	 * 
	 * @param path the base path to start searching from. 
	 * @return the directory path to the Transform/plugins folder as a String.
	 */
	private String getPath(String path) {
		Path dir = FileSystems.getDefault().getPath( path );
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(dir);

			for (Path local : stream) {
				File newFile = new File(path + File.separatorChar + local.getFileName().toString());
				
				//This the check to if the method should search down a specific folder. 
				if(newFile.isDirectory() && !(newFile.getName().charAt(0) == '.') && newFile.canRead() && !newFile.getName().equals("Application Data") && !newFile.isHidden()){
					
					//This is to see if the folder is the Transform/plugins folder. 
					if(newFile.getAbsolutePath().contains("Transform" + File.separatorChar + "plugins")) {
						System.out.println("Found Transform/plugins directory");
						return newFile.getAbsolutePath();
					}
					
					//This prepares the path for the recursive call. 
					String nextPath = getPath(path + File.separatorChar + local.getFileName());
					if(!(nextPath == null)) {
						return nextPath;
					}
				}
			}
			stream.close();

		} catch (IOException e) {
			throw new LocalFileError("Unable to find local directory path: " + path);
		}
		return null;
	}

	/**
	 * This gets the appropriate data from each jar's manifest and adds a new pair to the map.
	 * 
	 * @param file
	 * @throws IOException
	 */
	private void addToMap(File file) {
		try {
			JarFile jarFile = new JarFile(file);

			Manifest manifest = jarFile.getManifest();

			jarFile.close();

			//System.out.println(manifest.getAttributes("Implementation-Version"));

			Attributes attributes = manifest.getMainAttributes();

			String version = getVersion(attributes);
			String name = getSymbolicName(attributes);

			ArrayList<String> list = new ArrayList<String>();
			list.add(version);
			list.add(file.getAbsolutePath());

			localJars.put(name,  list);

		} catch (IOException e) {
			throw new LocalFileError("Unable to create a link to the file: " + file.getName() + "\n" + e.getMessage());
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
				if (keyword.equals("Bundle-SymbolicName") || keyword.equals("Implementation-Title")){
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
				if (keyword.equals("Bundle-Version") || keyword.equals("Implementation-Version")){
					return (String) attributes.get(key);
				}
			}
		}
		throw new IOException("Could not find the Bundle-Version in the manifest file.");
	}
}
