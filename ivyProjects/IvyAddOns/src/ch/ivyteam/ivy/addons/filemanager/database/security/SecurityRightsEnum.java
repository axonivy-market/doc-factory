/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.security;

/**
 * @author ec
 * This enum declares the file manager security rights that are checked when the security is activated
 */
public enum SecurityRightsEnum {
	
	/**
	 * the administrator security right, the administrator has all the rights
	 */
	MANAGE_SECURITY_RIGHT,
	/**
	 * this right gives the possibility to open the directory,<br>
	 * to read, print and download its files, <br>
	 * to read the files' metadatas (description, tags, file type), to copy the
	 * files to be able to paste them in another location with
	 * CREATE_FILES_RIGHT
	 */
	OPEN_DIRECTORY_RIGHT,
	/**
	 * Deprecated it is split in 2 rights: CREATE_DIRECTORY_RIGHT and
	 * RENAME_DIRECTORY_RIGHT
	 */
	UPDATE_DIRECTORY_RIGHT,
	/**
	 * this right gives the possibility to create sub-directories in the
	 * directory.<br>
	 * It implies that the user is also granted the OPEN_DIRECTORY_RIGHT
	 */
	CREATE_DIRECTORY_RIGHT,
	/**
	 * this right gives the possibility to rename the directory.<br>
	 * It implies that the user is also granted the OPEN_DIRECTORY_RIGHT
	 */
	RENAME_DIRECTORY_RIGHT,
	/**
	 * this right gives the possibility to translate the directory.<br>
	 * It implies that the user is also granted the OPEN and
	 * RENAME_DIRECTORY_RIGHT
	 */
	TRANSLATE_DIRECTORY_RIGHT,
	/**
	 * this right gives the possibility to delete the directory if it is empty.<br>
	 * It implies that the user is also granted the OPEN-, CREATE- and
	 * RENAME_DIRECTORY_RIGHT
	 */
	DELETE_DIRECTORY_RIGHT,
	/**
	 * Deprecated it is split in 2 rights: CREATE_FILES_RIGHT and
	 * UPDATE_FILES_RIGHT
	 */
	WRITE_FILES_RIGHT,
	/**
	 * this right gives the possibility to creates files (upload, dnd, paste).<br>
	 * It implies that the user is also granted the OPEN_DIRECTORY_RIGHT.
	 */
	CREATE_FILES_RIGHT,
	/**
	 * this right gives the possibility to edit the files: rename the files,
	 * edit the files' content, edit the files' metadata, overwrite files.<br>
	 * It implies that the user is also granted the OPEN_DIRECTORY_RIGHT.
	 */
	UPDATE_FILES_RIGHT,
	/**
	 * this right gives the possibility to edit the files: rename the files,
	 * edit the files' content, edit the files' metadata, overwrite files.<br>
	 * It implies that the user is also granted the OPEN_DIRECTORY_RIGHT.
	 */
	DELETE_FILES_RIGHT;
	
	

}
