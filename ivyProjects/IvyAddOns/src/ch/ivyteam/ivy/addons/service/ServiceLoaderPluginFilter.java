/**
 * 
 */
package ch.ivyteam.ivy.addons.service;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

import ch.ivyteam.ivy.addons.filemanager.FileHandler;

/**
 * This FileFilter is used to filter plugins to load.<br>
 * It can also be used for other File Filtering purposes.
 * @see FileFilter
 */
public class ServiceLoaderPluginFilter implements FileFilter {

	private String shouldContain="";
	private Set<String> acceptedExtentions = new HashSet<String>();
	
	/**
	 * empty constructor
	 */
	public ServiceLoaderPluginFilter() {}
	
	/**
	 * Constructor with optional Parameters to be able to filter the files with some convention.
	 * @param optionalAcceptedExtensions set of extensions (without dot ".") that will be accepted. 
	 * Null or empty set means that no filtering will be done with the extension.
	 * @param optionalPathConvention String that should be contained in the file path. 
	 * Null or empty String means that no filtering will be done with the file path.
	 */
	public ServiceLoaderPluginFilter(Set<String> optionalAcceptedExtensions, String optionalPathConvention ) {
		if(optionalAcceptedExtensions!=null)
			this.acceptedExtentions = optionalAcceptedExtensions;
		if(optionalPathConvention!=null)
			this.shouldContain = optionalPathConvention.replaceAll("\\\\", "/");
	}
	
	/**
	 * This method returns true only if the file is a jar and contains 'plugin' in its name.
	 */
	@Override
	public boolean accept(File file) {
		if(!checkFilePath(file)) return false;
		
		if(!this.acceptedExtentions.isEmpty()){
			String ext = FileHandler.getFileExtension(file.getName());
			if(!this.acceptedExtentions.contains(ext)) {
				return false;
			}
		}
		if(this.shouldContain.length()>0) {
			return file.getPath().replaceAll("\\\\", "/").contains(this.shouldContain);
		}
		
		return true;
		
	}

	/**
	 * @param file
	 */
	private boolean checkFilePath(File file) {
		return (file!=null && file.getPath() != null && !file.getPath().trim().isEmpty());	
	}
	
	

}
