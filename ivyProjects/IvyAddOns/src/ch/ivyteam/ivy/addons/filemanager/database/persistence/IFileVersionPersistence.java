package ch.ivyteam.ivy.addons.filemanager.database.persistence;

import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileVersion;
import ch.ivyteam.ivy.addons.filemanager.document.FilemanagerItemMetaData;

public interface IFileVersionPersistence extends IItemPersistence<FileVersion> {

	/**
	 * Returns the versions of a given file from the file management system.<br />
	 * The returned FileVersion Objects do not contain any File Content.<br />
	 * This method is designed to display such a list to a user for example.<br />
	 * The List is returned with the last created File version at first, and the
	 * very first version at the end.
	 * 
	 * @param fileId
	 *            the id of the file as stored in the table whose name is
	 *            defined in the ivy var:
	 *            "xivy_addons_fileManager_fileMetaDataTableName"
	 * @return an empty list if the given fileID < 0 or if no version exists for
	 *         the file.
	 * @throws Exception
	 *             in case of a Persistence Exception
	 */
	public List<FileVersion> getFileVersions(long fileId) throws Exception;
	
	/**
	 * Returns the next version number for a given file denoted by its id.<br />
	 * if there are no versions for the file, the returned number will be 1.
	 * 
	 * @param fileId
	 *            fileId the id of the file as stored in the table whose name is
	 *            defined in the ivy var:
	 *            "xivy_addons_fileManager_fileMetaDataTableName"
	 * @return the next file version number or -1 if the fileId <=0
	 * @throws Exception
	 *             in case of a Persistence Exception
	 */
	public int getNextVersionNumberForFile(long fileId) throws Exception;
	
	/**
	 * Creates a new version based on the actual file stored in the
	 * xivy_addons_fileManager_fileMetaDataTableName table
	 * 
	 * @param fileId
	 *            the file ID that has to be versioned
	 * @return the new FileVersion object with its id. The Object does not
	 *         contain any content. null if the given fileId <=0
	 * @throws Exception
	 *             in case of a Persistence Exception
	 */
	public FileVersion createNewVersion(long fileId, FilemanagerItemMetaData filemanagerItemMetaData) throws Exception;
	
	/**
	 * returns a DocumentOnServer Object with its Id. The DocumentOnServer
	 * Object Type is the parent of the FileVersion. It represents a File on the
	 * Server.
	 * 
	 * @param fileId
	 * @return the documentOnServer object corresponding to the given fileid
	 * @throws Exception
	 */
	public DocumentOnServer getParentFileWithoutContent(long fileId) throws Exception;
	
	/**
	 * Returns the FileVersion object corresponding to the parent fileID and to
	 * the versionNumber.<br />
	 * This object does not contain any content or java.io.File reference.<br />
	 * 
	 * @see the getFileVersionWithJavaFile method.
	 * @param fileId
	 *            the parent file ID contained in the table denoted by the Ivy
	 *            var: xivy_addons_fileManager_fileMetaDataTableName.<br />
	 * @param versionNumber
	 *            the version number to be retrieved
	 * @return the FileVersion object or null if problem occurred
	 * @throws Exception
	 *             in case of a Persistence or SQLException
	 */
	public FileVersion getFileVersionWithParentFileIdAndVersionNumber(long fileId, int versionNumber) throws Exception;
	
	/**
	 * Allows extracting all the versions related to a specified FileID.<br />
	 * The extracted files are going to be stored to the given path.<br />
	 * The versions files names ends with "#"+version number.<br />
	 * For example, the versions of example.doc are going to be extracted as
	 * example#1.doc, example#2.doc etc...<br />
	 * This method returns a List of extracted FileVersion objects. Each
	 * FileVersion contains a reference to its extracted java.io.File in its
	 * javaFile field.
	 * 
	 * @param parentFileId
	 *            : the fileID which versions have to be extracted.
	 * @param _path
	 *            : the path where to extract the files on the server.
	 * @return List of extracted FileVersion objects.
	 * @throws Exception
	 *             in case of a Persistence Exception, or if the
	 *             parameters are not valid (Id <=0 or path is null or empty
	 *             String).
	 */
	
	public List<FileVersion> extractVersionsToPath(long parentFileId, String _path) throws Exception;
	/**
	 * Returns the file version corresponding to the given fileVersionId. The
	 * fileVersion object contains the reference<br />
	 * to the IvyFile and the corresponding java.io.File.<br />
	 * The IvyFile / java.io.File is created dynamically by copying all the
	 * bytes contained in the BLOB version content table field.<br />
	 * The IvyFile is going to be created in the Ivy Session Server directory
	 * and will be temporary: it will be deleted<br />
	 * after the Ivy Session has been closed.<br />
	 * 
	 * @param fileVersionId
	 *            the file version id
	 * @return the FileVersion object or null if a problem occurred.
	 * @throws Exception
	 *             in case of a Persistence Exception
	 */
	public FileVersion getFileVersionWithJavaFile(long fileVersionId) throws Exception;
	
	/**
	 * This method deletes all the versions from a given file denoted by its id.<br />
	 * Do nothing if the fileID <=0 or if no version are found for this id.<br />
	 * It does not delete the file from the main table (last version).
	 * 
	 * @param fileId the file id from the files main table
	 * @throws Exception in case of a Persistence or SQLException
	 */
	public void deleteAllVersionsFromFile(long fileId) throws Exception;
	
	/**
	 * This method is used to get a last version to the active document
	 * 
	 * @param fileId
	 * @return the fileVersion that was rolledBack
	 * @throws Exception
	 */
	public FileVersion rollbackLastVersionAsActiveDocument(long fileId) throws Exception;
	
	/**
	 * This method returns true if a file version was already archived.<br>
	 * For example: <br><br>
	 * 1. A particular version was rolled back as actual active version in the files table. It was already archived in the version system.
	 * In that case the method returns true.<br>
	 * 2. A Version has been created right now and is the active document: this version has never been archived in the files version system. This method returns false then.
	 * If the file version system does not persist this information, this method returns always false.<br>
	 * 3. A version is in the version system right now. It is archived, the method returns true.
	 * @param fileid
	 * @param number
	 * @return
	 * @throws Exception
	 */
	boolean wasFileVersionArchived (long fileid, int number) throws Exception;

}
