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

public class LocalConnector {
	
	private Map<String, ArrayList<String>> localJars = new HashMap<String, ArrayList<String>>();
	
	public Map<String, ArrayList<String>> getLocalJars() {

		String pwd = System.getProperty("user.dir");

		//TODO this baseDir needs to be as close to *Transform/plugins as possible
		String baseDir = pwd.substring(0, pwd.lastIndexOf("CSM"));
		
		DirectoryScanner scanner = setUpScanner(baseDir);
		
		System.out.println("scan starting");
		scanner.scan();
		System.out.println("scan ended");
		
		String[] relativeFilePaths = scanner.getIncludedFiles();

		for (String relativeFilePath : relativeFilePaths) {
			
			System.out.println("Matched file : " + relativeFilePath);
			
			String absoluteDirPath = baseDir + relativeFilePath.substring(0, relativeFilePath.lastIndexOf('/'));
			String fileName = relativeFilePath.substring(relativeFilePath.lastIndexOf('/') + 1);
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
	
	private DirectoryScanner setUpScanner(String baseDir) {
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setIncludes(new String[]{"**/Transform/plugins/**/*.jar"});
		scanner.setBasedir(baseDir);
		scanner.setCaseSensitive(false);
		return scanner;
	}

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
}
