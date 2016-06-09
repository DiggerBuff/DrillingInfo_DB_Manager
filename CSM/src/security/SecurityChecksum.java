package security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

/**
 * This class creates a checksum using md5.
 * 
 * TODO add a comparison method to improve code readability.
 *
 */
public class SecurityChecksum {
	/**
	 * Generates a checksum based on the given file path
	 * 
	 * @param path the path to the file to generate the checksums
	 * @return the MD5 checksum
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 */
	public static String hashPassword(String path) throws NoSuchAlgorithmException, FileNotFoundException{
		FileInputStream fis = new FileInputStream(path);
		MessageDigest md = MessageDigest.getInstance("MD5");
		String b = getDigest(fis, md);
		return b;
	}

	/**
	 * Generates a checksum from an input stream and a message digest.
	 * 
	 * @param is Input Stream to create a checksum for.
	 * @param md The type of checksum to create. 
	 * @return the checksum.
	 * @throws NoSuchAlgorithmException
	 */
	public static String getDigest(InputStream is, MessageDigest md)throws NoSuchAlgorithmException {
		md.reset();
		byte[] bytes = new byte[md.getDigestLength()];
		int numBytes;
		try {
			while ((numBytes = is.read(bytes)) != -1) {
				md.update(bytes, 0, numBytes);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] digest = md.digest();
		String result = new String(Hex.encodeHex(digest));
		return result;
	}
}
