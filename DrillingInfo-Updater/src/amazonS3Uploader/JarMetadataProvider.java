package amazonS3Uploader;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;

import org.apache.commons.io.IOUtils;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.ObjectMetadataProvider;

public class JarMetadataProvider implements ObjectMetadataProvider {

	@Override
	public void provideObjectMetadata(File file, ObjectMetadata metadata) {
		JarFile jarFile;
		try {
			jarFile = new JarFile(file);
			Manifest manifest = jarFile.getManifest();
			jarFile.close();

			Attributes attributes = manifest.getMainAttributes();

			String version = getVersion(attributes);
			String name = getSymbolicName(attributes);

			InputStream content = new BufferedInputStream(new FileInputStream(file));

			if(version == null || name == null){
				content.close();
				return;
			}

			//metadata.setContentMD5(result);
			metadata.addUserMetadata("version", version);
			metadata.addUserMetadata("bundle-symbolicname", name);
			metadata.setContentLength(IOUtils.toByteArray(content).length);
			metadata.setContentType("application/x-java-archive");

			//System.out.println(metadata.getUserMetaDataOf("version"));
			content.close();
		} catch (IOException e) {
			System.err.println("\n" + e.getMessage() + file.getName());
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
		throw new IOException("Could not find the Bundle-SymbolicName in the manifest file. Uploaded metadata incorrect for file: ");
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
				//System.out.println("\"" + keyword + "\"");
				//Different types of manifest files may require a change here.
				if (keyword.equals("Bundle-Version")){
					return (String) attributes.get(key);
				}
			}
		}

		throw new IOException("Could not find the Bundle-Version in the manifest file. Uploaded metadata incorrect for file: ");
	}

}
