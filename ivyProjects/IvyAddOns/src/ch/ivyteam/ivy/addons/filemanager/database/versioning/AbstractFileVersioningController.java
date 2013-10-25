/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.versioning;

import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileVersion;

/**
 * @author ec
 *
 */
public abstract class AbstractFileVersioningController {
	
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
	 *             in case of a Persistence or SQLException
	 */
	public abstract List<FileVersion> getFileVersions(long fileId) throws Exception;
	
	/**
	 * Creates a new version based on the actual file stored in the
	 * xivy_addons_fileManager_fileMetaDataTableName table
	 * 
	 * @param fileId
	 *            the file ID that has to be versioned
	 * @return the new FileVersion object with its id. The Object does not
	 *         contain any content. null if the given fileId <=0
	 * @throws Exception
	 *             in case of a Persistence or SQLException
	 */
	public abstract FileVersion createNewVersion(long fileId) throws Exception;
	
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
	public abstract FileVersion getFileVersionWithParentFileIdAndVersionNumber(
			long fileId, int versionNumber) throws Exception;

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
	 *             in case of a Persistence or SQLException, or if the
	 *             parameters are not valid (Id <=0 or path is null or empty
	 *             String).
	 */
	public abstract List<FileVersion> extractVersionsToPath(long parentFileId,
			String _path) throws Exception;
	
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
	 *             in case of a Persistence or SQLException
	 */
	public abstract FileVersion getFileVersionWithJavaFile(long fileVersionId)
			throws Exception;
	/**
	 * This method deletes all the versions from a given file denoted by its id.<br />
	 * Do nothing if the fileID <=0 or if no version are found for this id.<br />
	 * It does not delete the file from the main table (last version).
	 * 
	 * @param fileId the file id from the files main table
	 * @throws Exception in case of a Persistence or SQLException
	 */
	public abstract void deleteAllVersionsFromFile(long fileId) throws Exception;
	
	/**
	 * This method is used to get a last version to the active document
	 * 
	 * @param fileId
	 * @return true if success else false
	 * @throws Exception
	 */
	public abstract boolean rollbackLastVersionAsActiveDocument(long fileId)throws Exception;
	
	/**
	 * Returns the versions of a given file from the file management system.<br />
	 * The returned FileVersion Objects do not contain any File Content.<br />
	 * This method is designed to display such a list to a user for example.<br />
	 * The List is returned with the last created File version at first, and the
	 * very first version at the end.<br />
	 * This method does not release the given java.sql.Connection Object. The calling method has to do that.
	 * @param fileId fileId
	 *            the id of the file as stored in the table whose name is
	 *            defined in the ivy var:
	 *            "xivy_addons_fileManager_fileMetaDataTableName"
	 * @param con java.sql.Connection Object.
	 * @return an empty list if the given fileID < 0 or if no version exists for
	 *         the file.
	 * @throws Exception
	 *             in case of a Persistence or SQLException
	 * @throws Exception
	 */
	public abstract List<FileVersion> getFileVersions(long fileId, java.sql.Connection con) throws Exception;
	
	/**
	 * This method deletes all the versions from a given file denoted by its id.<br />
	 * Do nothing if the fileID <=0 or if no version are found for this id.<br />
	 * It does not delete the file from the main table (last version).<br />
	 * This method does not release the given java.sql.Connection Object. The calling method has to do that.
	 * @param fileId the file id from the files main table
	 * @param con java.sql.Connection Object to communicate with the DB.<br />
	 * @throws Exception in case of a Persistence or SQLException
	 */
	public abstract void deleteAllVersionsFromFile(long fileId, java.sql.Connection con) throws Exception;
	
	/**
	 * returns a DocumentOnServer Object with its Id. The DocumentOnServer
	 * Object Type is the parent of the FileVersion. It represents a File on the
	 * Server.
	 * 
	 * @param fileId
	 * @return the documentOnServer object corresponding to the given fileid
	 * @throws Exception
	 */
	public abstract DocumentOnServer getParentFileWithoutContent(long fileId)
			throws Exception;
}