package ch.ivyteam.ivy.addons.filemanager.thumbnailer.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

public class IOUtil {
	/**
	 * Close, ignoring IOExceptions
	 * @param stream	Stream to be closed. May be null (in this case, nothing is done).
	 * @see Apache I/O Utils
	 */
	public static void quietlyClose(Closeable stream)
	{
		try{
			if (stream != null)
				stream.close();
		}
		catch (IOException e){
			// Ignore
		}
	}

	/**
	 * Close the given zip file ignoring IOExceptions
	 * @param zipFile zip file to be closed. May be null (in this case, nothing is done).
	 */
	public static void quietlyClose(ZipFile zipFile) {
		try{
			if (zipFile != null)
				zipFile.close();
		}
		catch (IOException e){
			// Ignore
		}
	}
	
	public static void deleteQuietlyForce(File file)
	{
		if (file != null){
			if(!file.delete()){
				if (file.exists())
					file.deleteOnExit();
			}
		}
	}
	
	// More difficult than I thought. 
	//See http://www.java2s.com/Code/Java/File-Input-Output/Getrelativepath.htm and 
	//http://stackoverflow.com/questions/204784/how-to-construct-a-relative-path-in-java-from-two-absolute-paths-or-urls
	/**
	 * Simplistic version: return the substring after the base
	 */
	public static String getRelativeFilename(File base, File target) {
		return getRelativeFilename(base.getAbsolutePath(), target.getAbsolutePath());
	}

	public static String getRelativeFilename(String sBase, String sTarget) {
		if (sTarget.startsWith(sBase))
		{
			if (sBase.endsWith("/") || sBase.endsWith("\\") || sTarget.length() == sBase.length())
				return sTarget.substring(sBase.length());
			else
				return sTarget.substring(sBase.length() + 1);
		}
		else
			return sTarget; // Leave absolute
	}
}
