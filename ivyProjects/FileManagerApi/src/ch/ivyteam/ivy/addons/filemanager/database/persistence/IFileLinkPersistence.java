package ch.ivyteam.ivy.addons.filemanager.database.persistence;

import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.database.filelink.FileLink;
import ch.ivyteam.ivy.scripting.objects.File;

public interface IFileLinkPersistence extends IItemPersistence<FileLink>{

	/**
	 * Returns the FileLink given by its id with the java file deserialized as temporary java and ivy File on the Server.
	 * @param id the id. Must be greater than zero.
	 * @return the FileLink wich fields javaFile and IvyFile are set with the ivy temporary File.
	 * @throws Exception
	 */
	FileLink getFileLinkWithJavaFile(long id) throws Exception;
	
	/**
	 * Returns a temp Ivy File built with the content referenced by the given FileLink.
	 * If the FileLink links to a File, the FileCOntent will be used. If it links to a File version, the file version content will be used.
	 * @param fl
	 * @return
	 * @throws Exception
	 */
	File getContentAsTempFile(FileLink fl) throws Exception;

	/**
	 * gets the list of all the FileLink in a given directory. If Recursive is true, it looks also in the directory Tree contained by the directory.
	 * @param directoryId the directory id. Must be greater than zero.
	 * @param recursive it true looks also in the directory Tree contained by the directory.
	 * @return the java.util.List of all the FileLinks.
	 * @throws Exception 
	 */
	List<FileLink> getListInDirectory(long directoryId, boolean recursive) throws Exception;

	/**
	 * Returns all the file links that drive to the given fileId
	 * @param fileId the file id. Must be greater than zero.
	 * @return all the fileLink objects attached to the file. Empty List if the file does not has fileLinks.
	 * @throws Exception
	 */
	List<FileLink> getFileLinksForFile(long fileId) throws Exception;

	/**
	 * Returns all the file links that drive to the given file version.<br />
	 * {@literal Note that it returns an empty List if the file versioning feature is not activated}
	 * @param fileVersionId the file version id. Must be greater than zero.
	 * @return all the fileLink objects attached to the given file version. Empty List if the file version does not has fileLinks.
	 * @throws Exception
	 */
	List<FileLink> getFileLinksForFileVersion(long fileVersionId) throws Exception;

	/**
	 * returns the FileLink with the given name in the given directory id.
	 * @param directoryId the directory id that contains the filelink. Must be greater than zero.
	 * @param fileLinkName the fileLink name. Cannot be null or empty
	 * @return the found FileLink or null if not exit.
	 * @throws Exception
	 */
	FileLink getFileLinkByDirectoryAndName(long directoryId, String fileLinkName) throws Exception;

	/**
	 * deletes all the fileLink attached to this file id.
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	int deleteFileLinksForFileId(long fileId) throws Exception;

	/**
	 * delete all the FileLink that drive to the given file version.
	 * @param fileVersionId
	 * @return
	 * @throws Exception
	 */
	int deleteFileLinksForFileVersionId(long fileVersionId) throws Exception;

	/**
	 * delete all the fileLinks in a given directory. Not recursive.
	 * @param directoryId
	 * @return
	 * @throws Exception
	 */
	int deleteFileLinksInDirectory(long directoryId) throws Exception;
	
	/**
	 * checks whether a File (document or FileLink) already exists under the given directory path and with the given name.
	 * @param path the directory path. Cannot be null or empty. The directory must exist;
	 * @param name
	 * @return
	 * @throws Exception
	 */
	boolean fileExist(String path, String name) throws Exception;
	
	/**
	 * Used for updating the fileVersionId of the FileLink to the file version corresponding to the given fileid with versionNumber.<br />
	 * It is called by the API whenever a file version is rolledback as active version or is pushed in the versioning sstem as version. 
	 * If the actual version is pushed to the file versioning system, then the existing FileLinks with the actual active version should link to the file version.<br />
	 * If a version is rolledback as active version, then the FileLinks should link to the active document (the fileLink versionId will be zero).<br />
	 * After this operation, calling the FileLink isReferenceVersion() method should return true if the FileLink links to a version in the versioning system, else false.;
	 * @param fileId
	 * @param versionNumber
	 * @return the number of FileLinks modified.
	 */
	int updateFileLinksVersionId(long fileId, int versionNumber) throws Exception;
	
	/**
	 * moves the given fileLink to the directory denoted by the given directory id
	 * @param fileLink the fileLink to move. Cannot be null.
	 * @param newDirectoryId the new directory id as long. Must be greater than zero.
	 * @return the moved FileLink
	 * @throws Exception if an error occurs. Example: the destination directory does not exists etc...
	 */
	FileLink moveFileLink(FileLink fileLink, long newDirectoryId) throws Exception;
		
}
