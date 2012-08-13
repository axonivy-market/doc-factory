/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.fileaction;

import java.io.Serializable;

import ch.ivyteam.ivy.environment.Ivy;

/**
 * @author ec
 *
 */
public class FileActionConfiguration implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5213271934516667737L;

	/**
	 * used to activate or deactivate the file action history tracking feature in the file management system.<br>
	 * Per Default it is false.
	 */
	private boolean activateFileActionHistory = false;
	
	/**
	 * Series of flags to fine tune which kind of file actions are going to be tracked. Per Default they are all set to true.
	 */
	private boolean editFileTracked=true, changeFileDescriptionTracked=true, fileCreationTracked=true,
	deleteFileTracked=true, copyFileTracked=true, renameFileTracked=true, 
	printFileTracked=true, readFileTracked=true, downloadFileTracked =true, moveFileTracked=true;
	
	/**
	 * The name of the Ivy database Configuration name used to connect to the database.<br>
	 * Default is the value of the xivy_addons_fileManager_ivyDatabaseConnectionName global variable.
	 */
	private String ivyDBConnectionName=Ivy.var().get("xivy_addons_fileManager_ivyDatabaseConnectionName").trim(); // the user friendly connection name to Database in Ivy
	
	/**
	 * Name of the DB table that stores the file history actions
	 * Default is the value of the xivy_addons_fileManager_fileActionHistoryTableName global variable.
	 */
	private String fileActionHistoryTableName = Ivy.var().get("xivy_addons_fileManager_fileActionHistoryTableName").trim();
	
	/**
	 * Name of the DB table that stores the file action types.
	 */
	private String fileActionTypeTableName= Ivy.var().get("xivy_addons_fileManager_fileActionTypeTableName").trim();

	private String fileActionHistoryTableNameSpace=null;
	
	private String fileActionTypeNameSpace = null;
	
	/**
	 * The name of the database schema that may be used if the tables are stored in a schema<br>
	 * Default is the value of the xivy_addons_fileManager_databaseSchemaName global variable.
	 */
	private String schemaName= Ivy.var().get("xivy_addons_fileManager_databaseSchemaName").trim();
	
	/**
	 * 
	 */
	public FileActionConfiguration()
	{
		this.fileActionHistoryTableNameSpace=(this.schemaName== null || this.schemaName.trim().length()==0)?
				this.fileActionHistoryTableName:this.schemaName+"."+this.fileActionHistoryTableName;
	    this.fileActionTypeNameSpace=(this.schemaName== null || this.schemaName.trim().length()==0)?
				this.fileActionTypeTableName:this.schemaName+"."+this.fileActionTypeTableName;
	}
	
	/**
	 * @return the activateFileActionHistory
	 */
	public boolean isActivateFileActionHistory() {
		return activateFileActionHistory;
	}

	/**
	 * @param activateFileActionHistory the activateFileActionHistory to set
	 */
	public void setActivateFileActionHistory(boolean activateFileActionHistory) {
		this.activateFileActionHistory = activateFileActionHistory;
	}

	/**
	 * @return the editFileTracked
	 */
	public boolean isEditFileTracked() {
		return this.activateFileActionHistory && editFileTracked;
	}

	/**
	 * @param editFileTracked the editFileTracked to set
	 */
	public void setEditFileTracked(boolean editFileTracked) {
		this.editFileTracked = editFileTracked;
	}

	/**
	 * @return the changeFileDescriptionTracked
	 */
	public boolean isChangeFileDescriptionTracked() {
		return this.activateFileActionHistory && changeFileDescriptionTracked;
	}

	/**
	 * @param changeFileDescriptionTracked the changeFileDescriptionTracked to set
	 */
	public void setChangeFileDescriptionTracked(
			boolean changeFileDescriptionTracked) {
		this.changeFileDescriptionTracked = changeFileDescriptionTracked;
	}

	/**
	 * @return the fileCreationTracked
	 */
	public boolean isFileCreationTracked() {
		return this.activateFileActionHistory && fileCreationTracked;
	}

	/**
	 * @param fileCreationTracked the fileCreationTracked to set
	 */
	public void setFileCreationTracked(boolean fileCreationTracked) {
		this.fileCreationTracked = fileCreationTracked;
	}

	/**
	 * @return the deleteFileTracked
	 */
	public boolean isDeleteFileTracked() {
		return this.activateFileActionHistory && deleteFileTracked;
	}

	/**
	 * @param deleteFileTracked the deleteFileTracked to set
	 */
	public void setDeleteFileTracked(boolean deleteFileTracked) {
		this.deleteFileTracked = deleteFileTracked;
	}

	/**
	 * @return the printFileTracked
	 */
	public boolean isPrintFileTracked() {
		return this.activateFileActionHistory && printFileTracked;
	}

	/**
	 * @param printFileTracked the printFileTracked to set
	 */
	public void setPrintFileTracked(boolean printFileTracked) {
		this.printFileTracked = printFileTracked;
	}

	/**
	 * @return the readFileTracked
	 */
	public boolean isReadFileTracked() {
		return this.activateFileActionHistory && readFileTracked;
	}

	/**
	 * @param readFileTracked the readFileTracked to set
	 */
	public void setReadFileTracked(boolean readFileTracked) {
		this.readFileTracked = readFileTracked;
	}

	/**
	 * @return the downloadFileTracked
	 */
	public boolean isDownloadFileTracked() {
		return this.activateFileActionHistory && downloadFileTracked;
	}

	/**
	 * @param downloadFileTracked the downloadFileTracked to set
	 */
	public void setDownloadFileTracked(boolean downloadFileTracked) {
		this.downloadFileTracked = downloadFileTracked;
	}

	/**
	 * @param copyFileTracked the copyFileTracked to set
	 */
	public void setCopyFileTracked(boolean copyFileTracked) {
		this.copyFileTracked = copyFileTracked;
	}

	/**
	 * @return the copyFileTracked
	 */
	public boolean isCopyFileTracked() {
		return this.activateFileActionHistory && copyFileTracked;
	}
	
	/**
	 * @return the renameFileTracked
	 */
	public boolean isRenameFileTracked() {
		return this.activateFileActionHistory && renameFileTracked;
	}

	/**
	 * @param renameFileTracked the renameFileTracked to set
	 */
	public void setRenameFileTracked(boolean renameFileTracked) {
		this.renameFileTracked = renameFileTracked;
	}

	/**
	 * @return the moveFileTracked
	 */
	public boolean isMoveFileTracked() {
		return this.activateFileActionHistory && moveFileTracked;
	}

	/**
	 * @param moveFileTracked the moveFileTracked to set
	 */
	public void setMoveFileTracked(boolean moveFileTracked) {
		this.moveFileTracked = moveFileTracked;
	}

	/**
	 * @return the fileActionHistoryTableName
	 */
	public String getFileActionHistoryTableName() {
		return fileActionHistoryTableName;
	}

	/**
	 * @param _fileActionHistoryTableName the fileActionHistoryTableName to set
	 */
	public void setFileActionHistoryTableName(String _fileActionHistoryTableName) {
		if(_fileActionHistoryTableName==null || _fileActionHistoryTableName.trim().length()==0)
		{
			return;
		}
		this.fileActionHistoryTableName = _fileActionHistoryTableName;
		this.fileActionHistoryTableNameSpace=(this.schemaName== null || this.schemaName.trim().length()==0)?
				this.fileActionHistoryTableName:this.schemaName+"."+this.fileActionHistoryTableName;
	}

	/**
	 * @param _fileActionTypeTableName the fileActionTypeTableName to set
	 */
	public void setFileActionTypeTableName(String _fileActionTypeTableName) {
		if(_fileActionTypeTableName==null || _fileActionTypeTableName.trim().length()==0)
		{
			return;
		}
		this.fileActionTypeTableName = _fileActionTypeTableName;
		
	    this.fileActionTypeNameSpace=(this.schemaName== null || this.schemaName.trim().length()==0)?
				this.fileActionTypeTableName:this.schemaName+"."+this.fileActionTypeTableName;
	}

	/**
	 * @return the fileActionTypeTableName
	 */
	public String getFileActionTypeTableName() {
		return fileActionTypeTableName;
	}

	/**
	 * @return the fileActionHistoryTableNameSpace
	 */
	public String getFileActionHistoryTableNameSpace() {
		return fileActionHistoryTableNameSpace;
	}

	/**
	 * @return the fileActionTypeNameSpace
	 */
	public String getFileActionTypeNameSpace() {
		return fileActionTypeNameSpace;
	}

	/**
	 * @return the schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * @param _schemaName the schemaName to set
	 */
	public void setSchemaName(String _schemaName) {
		this.schemaName = _schemaName;
		
		this.fileActionHistoryTableNameSpace=(this.schemaName== null || this.schemaName.trim().length()==0)?
				this.fileActionHistoryTableName:this.schemaName+"."+this.fileActionHistoryTableName;
	    this.fileActionTypeNameSpace=(this.schemaName== null || this.schemaName.trim().length()==0)?
				this.fileActionTypeTableName:this.schemaName+"."+this.fileActionTypeTableName;
	}

	/**
	 * @param ivyDBConnectionName the ivyDBConnectionName to set
	 */
	public void setIvyDBConnectionName(String ivyDBConnectionName) {
		this.ivyDBConnectionName = ivyDBConnectionName;
	}

	/**
	 * @return the ivyDBConnectionName
	 */
	public String getIvyDBConnectionName() {
		return ivyDBConnectionName;
	}


}
