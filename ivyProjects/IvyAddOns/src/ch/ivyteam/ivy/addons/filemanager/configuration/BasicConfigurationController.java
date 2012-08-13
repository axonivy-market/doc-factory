/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.configuration;


import ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler;
import ch.ivyteam.ivy.addons.filemanager.database.fileaction.FileActionConfiguration;
import ch.ivyteam.ivy.environment.Ivy;

/**
 * @author Emmanuel Comba<br>
 * @since 29.05.2012<br>
 * This class is used in all the File Management rich dialogs to be able to ease the start configuration of these RDCs.<br>
 * It can be extended to give more control possibilities for some Rich Dialogs.<br>
 * For example, the FileManager RDC uses the FileManagerConfigurationController that extends this BasicConfigurationController.
 *
 */
public class BasicConfigurationController{

	/**
	 * The auto generated serial Version ID
	 */
	private static final long serialVersionUID = 6999167937669289180L;

	/**
	 * If true the File Manager RDC will use the Ivy System DB to store the file's meta data information.<br />
	 * Per default, the value is set to false.<br/>
	 * If this is true, then the storeFilesInDB, activateSecurity and activateFileVersioning are false. 
	 */
	private boolean useIvySystemDB = false;

	/**
	 * If true, the files content is stored as BLOB into the Table which name is set in filesContentTableName.<br />
	 * If true, then useIvySystemDB is false,<br />
	 * If false, then activateSecurity and activateFileVersioning are false.
	 */
	private boolean storeFilesInDB = false;
	/**
	 * If true the security management on the directories will be activated. storeFilesInDB must be true.
	 */
	private boolean activateSecurity = false;
	/**
	 * If true the File version feature will be activated. storeFilesInDB must be true.
	 */
	private boolean activateFileVersioning = false;
	/**
	 * If true, the file types feature will be activated.
	 */
	private boolean activateFileType = false;
	/**
	 * If true, the file tags feature will be activated.
	 */
	private boolean activateFileTags = false;

	/**
	 * The name of the ivy role that is allowed to manage the file manager security.<br>
	 * Default is the value of the xivy_addons_fileManager_admin_roleName global variable.
	 */
	private String adminRole=Ivy.var().get("xivy_addons_fileManager_admin_roleName").trim();
	/**
	 * The name of the Ivy database Configuration name used to connect to the database.<br>
	 * Default is the value of the xivy_addons_fileManager_ivyDatabaseConnectionName global variable.
	 */
	private String IvyDBConnectionName=Ivy.var().get("xivy_addons_fileManager_ivyDatabaseConnectionName").trim();

	/**
	 * The name of the database schema that may be used if the tables are stored in a schema<br>
	 * Default is the value of the xivy_addons_fileManager_databaseSchemaName global variable.
	 */
	private String databaseSchemaName=Ivy.var().get("xivy_addons_fileManager_databaseSchemaName").trim();

	/**
	 * The name of the Table that stores the information about the files.<br>
	 * Default is the value of the xivy_addons_fileManager_fileMetaDataTableName global variable.
	 */
	private String filesTableName=Ivy.var().get("xivy_addons_fileManager_fileMetaDataTableName").trim();
	/**
	 * The name of the DB Table that stores the directory structure.<br>
	 * Default is the value of the xivy_addons_fileManager_directoriesTableName global variable.
	 */
	private String directoriesTableName=Ivy.var().get("xivy_addons_fileManager_directoriesTableName").trim();
	/**
	 * The name of the DB Table that stores the file content.<br>
	 * Default is the value of the xivy_addons_fileManager_fileContentTableName global variable.
	 */
	private String filesContentTableName=Ivy.var().get("xivy_addons_fileManager_fileContentTableName").trim();
	/**
	 * The name of the DB Table that stores the file versions information.<br>
	 * Default is the value of the xivy_addons_fileManager_fileVersioningMetaDataTableName global variable.
	 */
	private String filesVersionTableName=Ivy.var().get("xivy_addons_fileManager_fileVersioningMetaDataTableName").trim();
	/**
	 * The name of the DB Table that stores the file versions content.<br>
	 * Default is the value of the xivy_addons_fileManager_fileVersioningContentTableName global variable.
	 */
	private String filesVersionContentTableName=Ivy.var().get("xivy_addons_fileManager_fileVersioningContentTableName").trim();

	/**
	 * Name of the DB Table that stores the file types
	 * Default is the value of the xivy_addons_fileManager_fileTypesTableName global variable.
	 */
	private String fileTypeTableName =Ivy.var().get("xivy_addons_fileManager_fileTypesTableName").trim();

	/**
	 * Name of the DB Table that stores the file tags
	 * Default is the value of the xivy_addons_fileManager_fileTagsTableName global variable.
	 */
	private String fileTagsTableName =Ivy.var().get("xivy_addons_fileManager_fileTagsTableName").trim();
	/**
	 * The rootPath is the directory entry path.
	 */
	private String rootPath="";
	/**
	 * The max file size the user is allowed to upload in Kb. 0 means no limit.<br>
	 * This size can be set to limit the files upload.
	 */
	private int maxFileUploadSize = 0;
	
	/**
	 * FileActionConfiguration object that controls the file history tracking feature
	 */
	private FileActionConfiguration fileActionHistoryConfiguration = new FileActionConfiguration();

	/**
	 * 
	 */
	public BasicConfigurationController() {
		super();
		this.fileActionHistoryConfiguration = new FileActionConfiguration();
	}

	/**
	 * Set if the File Manager will use the Ivy System DB to store the file's meta data information.<br />
	 * If this is true, then the storeFilesInDB will be set to false and the storeFilesOnFileset will be set to true.
	 * @param useIvySystemDB the useIvySystemDB to set
	 */
	public void setUseIvySystemDB(boolean useIvySystemDB) {
		this.useIvySystemDB = useIvySystemDB;
		if(this.useIvySystemDB)
		{
			this.storeFilesInDB=false;
			this.fileActionHistoryConfiguration.setActivateFileActionHistory(false);
		}
	}

	/**
	 * Returns if the File Manager will use the Ivy System DB to store the file's meta data information.<br />
	 * If this is true, then the storeFilesInDB, activateSecurity and activateFileVersioning are false.
	 * @return the useIvySystemDB
	 */
	public boolean isUseIvySystemDB() {
		return useIvySystemDB;
	}

	/**
	 * @return the storeFilesInDB
	 */
	public boolean isStoreFilesInDB() {
		return storeFilesInDB;
	}
	/**
	 * Set if the files content is stored as BLOB into the Table which name is set in filesContentTableName.<br />
	 * If true, then useIvySystemDB is false,<br />
	 * If false, then isActivateSecurity() and isActivateFileVersioning() return false.
	 * @param storeFilesInDB the storeFilesInDB to set
	 */
	public void setStoreFilesInDB(boolean storeFilesInDB) {
		this.storeFilesInDB = storeFilesInDB;
		if(this.storeFilesInDB)
		{
			this.useIvySystemDB=false;
		}
		if(!this.storeFilesInDB)
		{
			this.fileActionHistoryConfiguration.setActivateFileActionHistory(false);
		}
	}

	/**
	 * Tells if the security management on the directories is activated.
	 * As prerequisite the storeFilesInDB attribute must be true, so if isStoreFilesInDB() returns false, 
	 * then this method will still returns false 
	 * even if the activateSecurity attribute stored value is true.
	 * @return the activateSecurity, true if the security management feature can be activated: storeFilesInDB is true && activateSecurity is true.
	 */
	public boolean isActivateSecurity() {
		//Ivy.log().info("Hey isActivateSecurity() was called and returned: "+(this.storeFilesInDB && activateSecurity)+ " the real value of activateSecurity is "+activateSecurity);
		return (this.storeFilesInDB && activateSecurity);
	}

	/**
	 * Set if the security management on the directories is activated or not.
	 * As prerequisite the storeFilesInDB attribute must be true, so if you set the security to true,<br>
	 * and isStoreFilesInDB() returns false, then the isActivateSecurity() method will still returns false 
	 * even if the activateSecurity attribute stored value is true. 
	 * @param activateSecurity the activateSecurity to set
	 */
	public void setActivateSecurity(boolean activateSecurity) {
		this.activateSecurity = activateSecurity;
		//Ivy.log().info("Hey setActivateSecurity was called and set to : "+this.activateSecurity);
	}

	/**
	 * Tells if the file version feature is activated.
	 * As prerequisite the storeFilesInDB attribute must be true, so if you set the file version feature to true,<br>
	 * and isStoreFilesInDB() returns false, then this method will still returns false 
	 * even if the activateFileVersioning attribute stored value is true.
	 * @return the activateFileVersioning, true if the file version feature can be activated: storeFilesInDB is true && activateFileVersioning is true.
	 */
	public boolean isActivateFileVersioning() {
		return (this.storeFilesInDB && activateFileVersioning);
	}

	/**
	 * Set if the file version feature is activated or not.
	 * As prerequisite the storeFilesInDB attribute must be true, so if you set the file version feature to true,<br>
	 * and isStoreFilesInDB() returns false, then the isActivateFileVersioning() method will still returns false 
	 * even if the activateFileVersioning attribute stored value is true.
	 * @param activateFileVersioning the activateFileVersioning to set
	 */
	public void setActivateFileVersioning(boolean activateFileVersioning) {
		this.activateFileVersioning = activateFileVersioning;
	}

	/**
	 * Set if the file type feature is activated or not.
	 * As prerequisite the storeFilesInDB attribute must be true, so if you set the file type feature to true,<br>
	 * and isStoreFilesInDB() returns false, then the setActivateFileType() method will still returns false 
	 * even if the activateFileType attribute stored value is true.
	 * @param activateFileType the activateFileType to set
	 */
	public void setActivateFileType(boolean activateFileType) {
		this.activateFileType = activateFileType;
	}

	/**
	 * Tells if the file type feature is activated.
	 * As prerequisite the storeFilesInDB attribute must be true, so if you set the file type feature to true,<br>
	 * and isStoreFilesInDB() returns false, then this method will still returns false 
	 * even if the activateFileType attribute stored value is true. 
	 * @return the activateFileType, true if the file type feature can be activated: storeFilesInDB is true && activateFileType is true.
	 */
	public boolean isActivateFileType() {
		return (this.storeFilesInDB && activateFileType);
	}

	/**
	 * Set if the file tag feature is activated or not.
	 * As prerequisite the storeFilesInDB attribute must be true, so if you set the file tag feature to true,<br>
	 * and isStoreFilesInDB() returns false, then the setActivateFileType() method will still returns false 
	 * even if the activateFileType attribute stored value is true.
	 * @param activateFileTags the activateFileTags to set
	 */
	public void setActivateFileTags(boolean activateFileTags) {
		this.activateFileTags = activateFileTags;
	}

	/**
	 * Tells if the file tag feature is activated.
	 * As prerequisite the storeFilesInDB attribute must be true, so if you set the file tag feature to true,<br>
	 * and isStoreFilesInDB() returns false, then this method will still returns false 
	 * even if the activateFileType attribute stored value is true. 
	 * @return the activateFileTags
	 */
	public boolean isActivateFileTags() {
		return (this.storeFilesInDB && activateFileTags);
	}

	/**
	 * Returns the Ivy role name that grants its users the management of the file manager security.
	 * @return the adminRole
	 */
	public String getAdminRole() {
		return adminRole;
	}

	/**
	 * Set the Ivy role name that grants its users the management of the file manager security.
	 * @param _adminRole the adminRole to set
	 */
	public void setAdminRole(String _adminRole) {
		this.adminRole = _adminRole;
	}

	/**
	 * 
	 * @return the ivyDBConnectionName
	 */
	public String getIvyDBConnectionName() {
		return IvyDBConnectionName;
	}

	/**
	 * @param _ivyDBConnectionName the ivyDBConnectionName to set
	 */
	public void setIvyDBConnectionName(String _ivyDBConnectionName) {
		if(_ivyDBConnectionName== null || _ivyDBConnectionName.trim().length()==0)
		{
			this.IvyDBConnectionName = Ivy.var().get("xivy_addons_fileManager_ivyDatabaseConnectionName").trim();
		}else{
			this.IvyDBConnectionName = _ivyDBConnectionName.trim();
		}
		this.fileActionHistoryConfiguration.setIvyDBConnectionName(this.IvyDBConnectionName);
	}

	/**
	 * @return the filesTableName
	 */
	public String getFilesTableName() {
		return filesTableName;
	}

	/**
	 * Set the name of the table that stores the meta information about the files(Creation date, user, description etc...).<br>
	 * If the given parameter is null or an empty String then the global variable xivy_addons_fileManager_fileMetaDataTableName content will be used.
	 * @param _filesTableName the filesTableName to set
	 */
	public void setFilesTableName(String _filesTableName) {
		if(_filesTableName==null || _filesTableName.trim().length()==0)
		{
			this.filesTableName = Ivy.var().get("xivy_addons_fileManager_fileMetaDataTableName").trim();
		}else{
			this.filesTableName = _filesTableName.trim();
		}
	}

	/**
	 * @return the directoriesTableName
	 */
	public String getDirectoriesTableName() {
		return directoriesTableName;
	}

	/**
	 * Set the name of the table that stores the meta information about the directories(Creation date, user, security information etc...).<br>
	 * This table is only used if the attribute storeFilesInDB is true.<br>
	 * If the given parameter is null or an empty String then the global variable xivy_addons_fileManager_directoriesTableName content will be used. 
	 * @param _directoriesTableName the directoriesTableName to set
	 */
	public void setDirectoriesTableName(String _directoriesTableName) {
		if(_directoriesTableName==null || _directoriesTableName.trim().length()==0)
		{
			this.directoriesTableName = Ivy.var().get("xivy_addons_fileManager_directoriesTableName").trim();
		}else{
			this.directoriesTableName = _directoriesTableName;
		}
	}

	/**
	 * @return the filesContentTableName
	 */
	public String getFilesContentTableName() {
		return filesContentTableName;
	}

	/**
	 * Set the name of the table that stores the content of the files as BLOB.<br>
	 * This table is only used if the attribute storeFilesInDB is true.<br>
	 * If the given parameter is null or an empty String then the global variable xivy_addons_fileManager_fileContentTableName content will be used.  
	 * @param _filesContentTableName the filesContentTableName to set
	 */
	public void setFilesContentTableName(String _filesContentTableName) {
		if(_filesContentTableName== null || _filesContentTableName.trim().length()==0)
		{
			this.filesContentTableName = Ivy.var().get("xivy_addons_fileManager_fileContentTableName").trim();
		}else{
			this.filesContentTableName = _filesContentTableName;
		}
	}

	/**
	 * @return the filesVersionTableName
	 */
	public String getFilesVersionTableName() {
		return filesVersionTableName;
	}

	/**
	 * Set the name of the table that stores the information about the files versions.<br>
	 * This table is only used if the attributes storeFilesInDB and activateFileVersioning are true.<br>
	 * If the given parameter is null or an empty String then the global variable xivy_addons_fileManager_fileVersioningMetaDataTableName content will be used.  
	 * @param _filesVersionTableName the filesVersionTableName to set
	 */
	public void setFilesVersionTableName(String _filesVersionTableName) {
		if(_filesVersionTableName==null || _filesVersionTableName.trim().length()==0)
		{
			this.filesVersionTableName = Ivy.var().get("xivy_addons_fileManager_fileVersioningMetaDataTableName").trim();
		}
		this.filesVersionTableName = _filesVersionTableName;
	}

	/**
	 * @return the filesVersionContentTableName
	 */
	public String getFilesVersionContentTableName() {
		return filesVersionContentTableName;
	}

	/**
	 * Set the name of the table that stores the content of the files versions as BLOB.<br>
	 * This table is only used if the attributes storeFilesInDB and activateFileVersioning are true.<br>
	 * If the given parameter is null or an empty String then the global variable xivy_addons_fileManager_fileVersioningMetaDataTableName content will be used. 
	 * @param _filesVersionContentTableName the filesVersionContentTableName to set
	 */
	public void setFilesVersionContentTableName(String _filesVersionContentTableName) {
		if(_filesVersionContentTableName==null || _filesVersionContentTableName.trim().length()==0)
		{
			this.filesVersionContentTableName = Ivy.var().get("xivy_addons_fileManager_fileVersioningContentTableName").trim();
		}else{
			this.filesVersionContentTableName = _filesVersionContentTableName;
		}
	}

	/**
	 * set the file types table name. If the parameter is null or an empty string,<br>
	 * then the Default value "filetypes" remains the value of this field.
	 * @param fileTypeTableName the fileTypeTableName to set
	 */
	public void setFileTypeTableName(String fileTypeTableName) {
		this.fileTypeTableName = fileTypeTableName;
	}

	/**
	 * @return the fileTypeTableName
	 */
	public String getFileTypeTableName() {
		return fileTypeTableName;
	}

	/**
	 * @param fileTagsTableName the fileTagsTableName to set
	 */
	public void setFileTagsTableName(String fileTagsTableName) {
		this.fileTagsTableName = fileTagsTableName;
	}

	/**
	 * @return the fileTagsTableName
	 */
	public String getFileTagsTableName() {
		return fileTagsTableName;
	}

	/**
	 * Set the rootPath attribute. The root path is considered to be the root directory of the file manager.<br>
	 * This path will be automatically formatted so it contains only "/" as separator and ends always with "/"
	 * @param _rootPath the rootPath to set
	 */
	public void setRootPath(String _rootPath) {
		if(_rootPath==null)
		{
			this.rootPath ="";
		}else{
			this.rootPath = AbstractFileManagementHandler.formatPathForDirectory(_rootPath);
		}
	}

	/**
	 * @return the rootPath
	 */
	public String getRootPath() {
		return rootPath;
	}

	/**
	 * @return the databaseSchemaName
	 */
	public String getDatabaseSchemaName() {
		return databaseSchemaName;
	}

	/**
	 * Set the database schema name that is used in your database. Leave it empty if you use the default one (ex: "public" in postGRE, "dbo" in MS SQL etc...)
	 * @param _databaseSchemaName the databaseSchemaName to set
	 */
	public void setDatabaseSchemaName(String _databaseSchemaName) {
		if(_databaseSchemaName==null)
		{
			this.databaseSchemaName="";
		}
		this.databaseSchemaName = _databaseSchemaName.trim();
		this.fileActionHistoryConfiguration.setSchemaName(this.databaseSchemaName);
	}

	/**
	 * The max file size the user is allowed to upload in Kb. 0 means no limit.<br>
	 * This size can be set to limit the files upload.
	 * @param maxFileSize the maxFileSize to set
	 */
	public void setMaxFileUploadSize(int maxFileSize) {
		this.maxFileUploadSize = maxFileSize;
	}

	/**
	 * The max file size the user is allowed to upload in Kb. 0 means no limit.<br>
	 * This size can be set to limit the files upload.
	 * @return the maxFileSize
	 */
	public int getMaxFileUploadSize() {
		return maxFileUploadSize;
	}

	/**
	 * @param _fileActionHistoryConfiguration the fileActionHistoryConfiguration to set
	 */
	public void setFileActionHistoryConfiguration(
			FileActionConfiguration _fileActionHistoryConfiguration) {
		if(_fileActionHistoryConfiguration!=null)
			this.fileActionHistoryConfiguration = _fileActionHistoryConfiguration;
	}

	/**
	 * @return the fileActionHistoryConfiguration
	 */
	public FileActionConfiguration getFileActionHistoryConfiguration() {
		return fileActionHistoryConfiguration;
	}

	/**
	 * Check if the configuration is correct to be able to start the file manager.<br>
	 * For example: if the files are stored in the DB as BLOB AND the file content table name is unknown, then the configuration is not correct.<br>
	 * This doesn't check if the rootPath was provided.
	 * @return true if the configuration is correct else false.
	 */
	public boolean isConfigurationCorrect()
	{
		return (this.isUseIvySystemDB() || 
				(
						this.IvyDBConnectionName.length()>0 &&
						this.filesTableName.length()>0 &&
						(!this.isStoreFilesInDB() || (this.filesContentTableName.length()>0 && this.directoriesTableName.length()>0)) &&
						(!this.isActivateFileVersioning() || (this.filesVersionTableName.length()>0 && this.filesVersionContentTableName.length()>0))
				)
		);
	}


}
