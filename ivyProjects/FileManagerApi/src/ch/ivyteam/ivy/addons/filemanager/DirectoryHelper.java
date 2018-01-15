/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager;

import java.util.Iterator;

import org.apache.commons.io.FileUtils;

import ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;
import ch.ivyteam.ivy.environment.Ivy;

/**
 * @author ec
 * @since 02.02.2012
 * This class is a helper class to perform some actions on directories level.<br />
 * It takes in general an AbstractFileManagementHandler as parameter to be able to perform <br />
 * its actions.
 * 
 */
public class DirectoryHelper {
	
	/**
	 * copy a directory and its entire structure (sub directories, children from children etc, and contained files)<br />
	 * into a new one.
	 * @param _path : the path of the directory to be copied
	 * @param _whereToCopy : the path of the new directory
	 * @param fmHandler : the filemanagementHandler that is used in the filemanagementSystem that calls this method.<br />
	 * @throws Exception
	 */
	public static void copyDirectory(String _path, String _whereToCopy, AbstractFileManagementHandler fmHandler) throws Exception
	{
		if(fmHandler == null || _path==null || _whereToCopy==null || _path.trim().length()==0 || _whereToCopy.trim().length()==0)
		{
			throw new java.lang.IllegalArgumentException("Illegal arguments in copyDirectory method.");
		}
		if(!fmHandler.directoryExists(_path))
		{
			throw new java.lang.IllegalArgumentException("Illegal argument: the directory to copy doesn't exist.");
		}
			
		if(fmHandler.getFile_content_storage_type() == AbstractFileManagementHandler.FILE_STORAGE_FILESYSTEM)
		{//files stored on the server file system
			copyDirectoryFS(_path, _whereToCopy);
			Iterator<java.io.File> iter = FileUtils.iterateFiles(new java.io.File(_whereToCopy), null, true);
			
			while(iter.hasNext())
			{
				java.io.File f = iter.next();
				try{
					fmHandler.insertFile(f, f.getParent(),Ivy.session().getSessionUserName());
				}catch(Exception ex)
				{
					//ignore if the file path already exists
				}
			}
		}else
		{//we have to support the file management with fileContent in DB
			
		}
	}
	
	
	private static void copyDirectoryFS(String _path, String _whereToCopy) throws Exception
	{
		FileUtils.copyDirectory(new java.io.File(_path), new java.io.File(_whereToCopy));
	}
	
	/**
	 * returns the name of the directory from the given path.
	 * @param _path
	 * @return
	 * @throws java.lang.IllegalArgumentException
	 */
	public static String getDirectoryNameFromPath(String _path) throws java.lang.IllegalArgumentException
	{
		if(_path==null || _path.trim().length()==0)
		{
			throw new java.lang.IllegalArgumentException("Illegal argument: the directory path is null or empty.");
		}
		_path = PathUtil.formatPathForDirectoryWithoutLastSeparator(_path);
		if(_path.contains("/"))
		{
			return _path.substring(_path.lastIndexOf("/")+1);
		}else{
			return _path;
		}
	}
	
	

}
