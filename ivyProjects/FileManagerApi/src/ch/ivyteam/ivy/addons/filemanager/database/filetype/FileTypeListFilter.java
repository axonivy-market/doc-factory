package ch.ivyteam.ivy.addons.filemanager.database.filetype;

import ch.ivyteam.ivy.addons.filemanager.FileType;

/**
 * Used by the FileTypesController to filter the FileType Objects in the methods returning List of FileType.<br><br>
 * If you need the FileType filtering feature, you just have to set the BasicConfigurationController FileTypeListFilter field with the following method:<br>
 * {@link ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController#setFileTypeListFilter(FileTypeListFilter)}
 * @author ec
 *
 */
public interface FileTypeListFilter {
	
	/**
	 * Used to include or exclude the FileType objects found by persistence system.
	 * @param fileType the FileType to be included or excluded
	 * @return true if the FileType is accepted and included in the list, else false.
	 */
	boolean accept(FileType fileType);

}
