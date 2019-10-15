/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.versioning;

import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileVersion;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileVersionPersistence;
import ch.ivyteam.ivy.addons.filemanager.document.FilemanagerItemMetaData;
import ch.ivyteam.ivy.addons.filemanager.listener.FileVersionActionEvent;
import ch.ivyteam.ivy.addons.filemanager.listener.FileVersionActionListener;

/**
 * @author ec
 * 
 */
public class FileVersioningController extends AbstractFileVersioningController {

	public static final String DELETE_VERSION_NOT_PERMITTED = "Deleting files' versions is not permitted because the file archive protection is enabled.";
	private BasicConfigurationController config;
	private IFileVersionPersistence fileVersioningPersistence;
	private boolean activateArchiveProtection;
	
	
	/**
	 * creates a new FileVersioningController object.<br />
	 * All the parameters for the Database connections are going to retrieved
	 * from the ivy global variables.
	 * 
	 */
	public FileVersioningController() {
		this.config = new BasicConfigurationController();
		try {
			this.initializePersistence();
		} catch (Exception e) {

		}
	}

	/**
	 * creates a new FileVersioningController object.<br />
	 * You can fine tunes the settings with the input parameters.<br />
	 * If any parameter is null or is an empty String, it will be set with its
	 * ivy global variable.
	 * 
	 * @param _ivyDBConnectionName
	 * @param _fileTableName
	 * @param _fileContentTableName
	 * @param _fileVersionTableName
	 * @param _fileVersionContentTableName
	 * @param _schemaName
	 */
	public FileVersioningController(String _ivyDBConnectionName,
			String _fileTableName, String _fileContentTableName,
			String _fileVersionTableName, String _fileVersionContentTableName,
			String _schemaName) {
		this.config = new BasicConfigurationController();
		this.config.setIvyDBConnectionName(_ivyDBConnectionName);
		this.config.setFilesTableName(_fileTableName);
		this.config.setFilesContentTableName(_fileContentTableName);
		this.config.setFilesVersionTableName(_fileVersionTableName);
		this.config.setFilesVersionContentTableName(_fileVersionContentTableName);

		if (_schemaName != null && !_schemaName.trim().isEmpty()) {
			this.config.setDatabaseSchemaName(_schemaName);
		}
		this.setActivateArchiveProtection(this.config.isFileArchiveProtectionEnabled());
		try {
			this.initializePersistence();
		} catch (Exception e) {

		}
	}

	public FileVersioningController(BasicConfigurationController config) {
		if(config==null) {
			this.config = new BasicConfigurationController();
		} else {
			this.config = config;
		}
		this.setActivateArchiveProtection(this.config.isFileArchiveProtectionEnabled());
		try {
			this.initializePersistence();
		} catch (Exception e) {

		}
	}
	
	/**
	 * For unit tests only
	 */
	protected FileVersioningController(IFileVersionPersistence fileVersioningPersistence) {
		this.fileVersioningPersistence = fileVersioningPersistence;
	}

	private void initializePersistence() throws Exception {
		this.fileVersioningPersistence = PersistenceConnectionManagerFactory.getIFileVersionPersistenceInstance(this.config);
	}

	@Override
	public List<FileVersion> getFileVersions(long fileId) throws Exception {
		return this.fileVersioningPersistence.getFileVersions(fileId);
	}

	@Override
	@Deprecated
	public List<FileVersion> getFileVersions(long fileId, java.sql.Connection con) throws Exception {
		return this.fileVersioningPersistence.getFileVersions(fileId);
	}

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
	 *             in case of a Persistence or SQLException
	 */
	public int getNextVersionNumberForFile(long fileId) throws Exception {
		return this.fileVersioningPersistence.getNextVersionNumberForFile(fileId);
	}

	@Override
	public FileVersion createNewVersion(long fileId) throws Exception {
		FileVersion fileVersion = this.fileVersioningPersistence.createNewVersion(fileId, null);
		this.fireFileVersionCreatedEvent(fileVersion);
		return fileVersion;
	}

	@Override
	public FileVersion createNewVersion(long fileId,
			FilemanagerItemMetaData filemanagerItemMetaData) throws Exception {
		FileVersion fileVersion = this.fileVersioningPersistence.createNewVersion(fileId, filemanagerItemMetaData);
		this.fireFileVersionCreatedEvent(fileVersion);
		return fileVersion;
	}

	@Override
	public DocumentOnServer getParentFileWithoutContent(long fileId)
			throws Exception {
		return this.fileVersioningPersistence.getParentFileWithoutContent(fileId);
	}

	@Override
	public FileVersion getFileVersionWithParentFileIdAndVersionNumber(
			long fileId, int versionNumber) throws Exception {
		return this.fileVersioningPersistence.getFileVersionWithParentFileIdAndVersionNumber(fileId, versionNumber);
	}

	@Override
	public List<FileVersion> extractVersionsToPath(long parentFileId,
			String _path) throws Exception {
		return this.fileVersioningPersistence.extractVersionsToPath(parentFileId, _path);
	}

	@Override
	public FileVersion getFileVersionWithJavaFile(long fileVersionId)
			throws Exception {
		return this.fileVersioningPersistence.getFileVersionWithJavaFile(fileVersionId);
	}

	/**
	 * delete all the given file versions
	 * @throws FileVersionProtectedException if the archive protection is enabled.
	 * @throws Exception In case of SQL or persistence exception
	 */
	@Override
	public void deleteAllVersionsFromFile(long fileId) throws FileVersionProtectedException, Exception {
		if(this.activateArchiveProtection) {
			throw new FileVersionProtectedException(DELETE_VERSION_NOT_PERMITTED);
		}
		this.fileVersioningPersistence.deleteAllVersionsFromFile(fileId);
	}

	@Override
	@Deprecated
	public void deleteAllVersionsFromFile(long fileId, java.sql.Connection con) throws FileVersionProtectedException, Exception {
		if(this.activateArchiveProtection) {
			throw new FileVersionProtectedException(DELETE_VERSION_NOT_PERMITTED);
		}
		this.fileVersioningPersistence.deleteAllVersionsFromFile(fileId);
	}

	/**
	 * This method is used to get a last version to the active document
	 * 
	 * @param fileId
	 * @return true if success else false
	 * @throws FileVersionProtectedException if the archive protection is enabled and the actual document version that should be implicitly deleted
	 * has been an archive in the past.
	 * @throws Exception In case of SQL or persistence exception
	 */
	@Override
	public boolean rollbackLastVersionAsActiveDocument(long fileId) throws FileVersionProtectedException, Exception {
		if(this.activateArchiveProtection) {
			if(this.fileVersioningPersistence.wasFileVersionArchived(fileId,this.getParentFileWithoutContent(fileId).getVersionnumber().intValue())) {
				throw new FileVersionProtectedException(DELETE_VERSION_NOT_PERMITTED);
			}
		}
		FileVersion rolledBackFileVersion = this.fileVersioningPersistence.rollbackLastVersionAsActiveDocument(fileId);
		this.fireFileVersionRolledBackEvent(rolledBackFileVersion);
		return rolledBackFileVersion != null;
	}
	
	@Override
	public boolean wasFileVersionArchived(long fileid, int versionnumber) throws Exception {
		if(this.activateArchiveProtection) {
			return this.fileVersioningPersistence.wasFileVersionArchived(fileid,versionnumber);
		}
		return false;
	}

	/**
	 * Returns the Ivy database connection name used to get a connection to the
	 * database,<br />
	 * that contains the file's tables.
	 * 
	 * @return
	 */
	public String getIvyDBConnectionName() {
		return this.config.getIvyDBConnectionName();
	}

	/**
	 * Set the Ivy database connection name used to get a connection to the
	 * database,<br />
	 * that contains the file's tables.
	 * 
	 * @param ivyDBConnectionName
	 *            the Ivy database connection name used to get a connection to
	 *            the database.
	 */
	public void setIvyDBConnectionName(String ivyDBConnectionName) {
		this.config.setIvyDBConnectionName(ivyDBConnectionName);
		try {
			this.initializePersistence();
		} catch (Exception e) {

		}
	}

	/**
	 * Returns the name of the table that stores the informations about the
	 * files.
	 * 
	 * @return
	 */
	public String getFileTableName() {
		return this.config.getFilesTableName();
	}

	/**
	 * Sets the name of the table that stores the informations about the files.
	 * 
	 * @param fileTableName
	 */
	public void setFileTableName(String fileTableName) {
		this.config.setFilesTableName(fileTableName);
		try {
			this.initializePersistence();
		} catch (Exception e) {

		}
	}

	/**
	 * Returns the name of the table that stores the files content.<br />
	 * This table contains the most actual version of the files. The one you get
	 * directly in the File Manager.
	 * 
	 * @return
	 */
	public String getFileContentTableName() {
		return this.config.getFilesContentTableName();
	}

	/**
	 * Sets the name of the table that stores the files content.<br />
	 * This table contains the most actual version of the files. The one you get
	 * directly in the File Manager.
	 * 
	 * @param fileContentTableName
	 */
	public void setFileContentTableName(String fileContentTableName) {
		this.config.setFilesContentTableName(fileContentTableName);
		try {
			this.initializePersistence();
		} catch (Exception e) {

		}
	}

	/**
	 * Returns the name of the table that contains the informations about the
	 * old files versions.
	 * 
	 * @return
	 */
	public String getFileVersionTableName() {
		return this.config.getFilesVersionTableName();
	}

	/**
	 * Sets the name of the table that contains the informations about the old
	 * files versions.
	 * 
	 * @param fileVersionTableName
	 */
	public void setFileVersionTableName(String fileVersionTableName) {
		this.config.setFilesVersionTableName(fileVersionTableName);

		try {
			this.initializePersistence();
		} catch (Exception e) {

		}
	}

	/**
	 * Returns the name of the table that contains all the files versions
	 * contents.
	 * 
	 * @return
	 */
	public String getFileVersionContentTableName() {
		return this.config.getFilesVersionContentTableName();
	}

	/**
	 * Sets the name of the table that contains all the files versions contents.
	 * 
	 * @param fileVersionContentTableName
	 */
	public void setFileVersionContentTableName(String fileVersionContentTableName) {
		this.config.setFilesVersionContentTableName(fileVersionContentTableName);
		try {
			this.initializePersistence();
		} catch (Exception e) {

		}
	}

	/**
	 * @return the databaseSchemaName
	 */
	public String getDatabaseSchemaName() {
		return this.config.getDatabaseSchemaName();
	}

	/**
	 * @param databaseSchemaName
	 *            the databaseSchemaName to set
	 */
	public void setDatabaseSchemaName(String databaseSchemaName) {
		this.config.setDatabaseSchemaName(databaseSchemaName);
		try {
			this.initializePersistence();
		} catch (Exception e) {

		}
	}
	
	protected boolean isActivateArchiveProtection() {
		return activateArchiveProtection;
	}

	protected void setActivateArchiveProtection(boolean activateArchiveProtection) {
		this.activateArchiveProtection = activateArchiveProtection;
	}
	
	private synchronized void fireFileVersionCreatedEvent(FileVersion fileVersion) {
		FileVersionActionEvent fileVersionActionEvent = new FileVersionActionEvent(fileVersion);
		for(FileVersionActionListener listener: fileVersionActionListeners) {
			listener.fileVersionCreated(fileVersionActionEvent);
		}
	}
	
	private synchronized void fireFileVersionRolledBackEvent(FileVersion fileVersion) {
		FileVersionActionEvent fileVersionActionEvent = new FileVersionActionEvent(fileVersion);
		for(FileVersionActionListener listener: fileVersionActionListeners) {
			listener.fileVersionRolledback(fileVersionActionEvent);
		}
	}
	
}
