/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Callable;

import ch.ivyteam.db.jdbc.DatabaseUtil;
import ch.ivyteam.ivy.addons.filemanager.DirectoryHelper;
import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.KeyValuePair;
import ch.ivyteam.ivy.addons.filemanager.LockedFolder;
import ch.ivyteam.ivy.addons.filemanager.ReturnedMessage;
import ch.ivyteam.ivy.addons.filemanager.ZipHandler;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.fileaction.FileActionHistoryController;
import ch.ivyteam.ivy.addons.filemanager.database.filetype.FileTypesController;
import ch.ivyteam.ivy.addons.filemanager.database.security.AbstractDirectorySecurityController;
import ch.ivyteam.ivy.addons.filemanager.database.security.DirectorySecurityController;
import ch.ivyteam.ivy.addons.filemanager.database.security.FileManagementStaticController;
import ch.ivyteam.ivy.addons.filemanager.database.versioning.FileVersioningController;
import ch.ivyteam.ivy.db.IExternalDatabase;
import ch.ivyteam.ivy.db.IExternalDatabaseApplicationContext;
import ch.ivyteam.ivy.db.IExternalDatabaseRuntimeConnection;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.File;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.scripting.objects.Record;
import ch.ivyteam.ivy.scripting.objects.Time;
import ch.ivyteam.ivy.scripting.objects.Tree;

/**
 * @author ec
 *
 */
public class FileStoreDBHandler extends AbstractFileManagementHandler {


	private String ivyDBConnectionName = null; // the user friendly connection name to Database in Ivy
	private String tableName = null; // the table name that stores the files information
	private String fileContentTableName = null; // the table name that stores the files content
	private String schemaName = null;// the DB Schema name if needed (eg. by PostGreSQL)
	private String tableNameSpace = null; // equals to tableName if schemaName == null, else schemaName.tableName
	private String fileContentTableNameSpace = null; // equals to fileContentTable if schemaName == null, else schemaName.tableName
	IExternalDatabase database=null;

	private String dirTableName = null; // the table that stores directories infos
	private String dirTableNameSpace = null; // equals to dirTableName if schemaName == null, else schemaName.dirTableName
	private boolean securityActivated = false;
	private AbstractDirectorySecurityController securityController = null; // the file security controller if the security is activated.

	private boolean activateFileType=false;
	private FileTypesController ftController = null;

	private boolean activateFileVersioning=false;
	private FileVersioningController fvc=null;
	
	private boolean activateHistory=false;

	private BasicConfigurationController config=null;
	/**
	 * @throws Exception 
	 * 
	 */
	public FileStoreDBHandler() throws Exception {
		this(null,null);
	}

	/**
	 * Creates a new FileStoreHandler with possibility to overrides the ivy global variables settings for the two given parameters.<br />
	 * The other variables (security activated, database schema and directories table name) will be set with the corresponding global variables.
	 * @param _ivyDBConnectionName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _tableName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @throws Exception 
	 */
	public FileStoreDBHandler(String _ivyDBConnectionName, String _tableName) throws Exception {
		super();
		initializeVariables(_ivyDBConnectionName,_tableName,null,null,null,null);
	}


	/**
	 * Creates a new FileStoreHandler with possibility to overrides the ivy global variables settings for the five given parameters.<br />
	 * The other variable (security activated) will be set with the corresponding global variables.
	 * @param _ivyDBConnectionName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _fileTableName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _fileContentTableName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _dirTableName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _schemaName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @throws Exception
	 */
	public FileStoreDBHandler(String _ivyDBConnectionName, String _fileTableName, String _fileContentTableName, String _dirTableName, 
			String _schemaName) throws Exception {
		super();
		initializeVariables(_ivyDBConnectionName,_fileTableName,_fileContentTableName,_dirTableName,_schemaName,null);
	}

	/**
	 * Creates a new FileStoreHandler with possibility to overrides the ivy global variables settings for the six given parameters.
	 * @param _ivyDBConnectionName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _fileTableName:String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _fileContentTableName:String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _dirTableName:String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _schemaName:String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _securityActivated: Boolean, set the security activation.
	 * @throws Exception
	 */
	public FileStoreDBHandler(String _ivyDBConnectionName, String _fileTableName, String _fileContentTableName, String _dirTableName, 
			String _schemaName, boolean _securityActivated) throws Exception {
		super();
		initializeVariables(_ivyDBConnectionName,_fileTableName,_fileContentTableName,_dirTableName,_schemaName,_securityActivated);
	}

	/**
	 * Creates a new FileStoreHandler with the given BasicConfigurationController
	 * @param _conf BasicConfigurationController
	 * @throws Exception NullpointerException if the BasicConfigurationController is null
	 */
	public FileStoreDBHandler(BasicConfigurationController _conf) throws Exception
	{
		super();
		initializeVariables(_conf.getIvyDBConnectionName(),
				_conf.getFilesTableName(),
				_conf.getFilesContentTableName(),
				_conf.getDirectoriesTableName(),
				_conf.getDatabaseSchemaName(),
				_conf.isActivateSecurity());
		this.config = _conf;
		this.activateFileType=_conf.isActivateFileType();
		if(this.activateFileType)
		{
			this.ftController = new FileTypesController(_conf);
		}
		this.activateFileVersioning=this.config.isActivateFileVersioning();
		if(this.activateFileVersioning)
		{
			this.fvc = new FileVersioningController(
					this.ivyDBConnectionName, 
					this.tableName, 
					this.fileContentTableName, 
					this.config.getFilesVersionTableName(),
					this.config.getFilesVersionContentTableName(), 
					this.config.getDatabaseSchemaName());
		}
		if(this.config.getFileActionHistoryConfiguration()!=null 
				&& this.config.getFileActionHistoryConfiguration().isActivateFileActionHistory())
		{
			super.setFileActionHistoryController(new FileActionHistoryController( this.config.getFileActionHistoryConfiguration()));
			this.activateHistory=true;
		}
	}

	public FileTypesController getFileTypesController() {
		return ftController;
	}

	public FileVersioningController getFileVersioningController() {
		return fvc;
	}

	/**
	 * 
	 * @param _ivyDBConnectionName
	 * @param _tableName
	 * @param _fileContentTableName
	 * @param _dirTableName
	 * @param _schemaName
	 * @param _securityActivated
	 * @throws Exception 
	 */
	private void initializeVariables(String _ivyDBConnectionName, String _tableName, String _fileContentTableName, String _dirTableName, 
			String _schemaName, Boolean _securityActivated) throws Exception
	{
		if(_ivyDBConnectionName==null || _ivyDBConnectionName.trim().length()==0)
		{//if ivy user friendly name of database configuration not settled used default
			this.ivyDBConnectionName = Ivy.var().get("xivy_addons_fileManager_ivyDatabaseConnectionName").trim();
		}else{
			this.ivyDBConnectionName = _ivyDBConnectionName.trim();
		}

		if(_tableName==null || _tableName.trim().length()==0)
		{//if ivy table name not settled used default
			this.tableName=Ivy.var().get("xivy_addons_fileManager_fileMetaDataTableName").trim();
		}else{
			this.tableName=_tableName.trim();
		}

		if(_fileContentTableName==null || _fileContentTableName.trim().length()==0)
		{//if ivy file content table name not settled used default
			this.fileContentTableName=Ivy.var().get("xivy_addons_fileManager_fileContentTableName").trim();
		}else{
			this.fileContentTableName=_fileContentTableName.trim();
		}

		if(_dirTableName==null || _dirTableName.trim().length()==0)
		{//if ivy directories table name not settled used default
			this.dirTableName=Ivy.var().get("xivy_addons_fileManager_directoriesTableName").trim();
		}else{
			this.dirTableName=_dirTableName.trim();
		}

		if(_schemaName==null)
		{//if DB schema name not settled used default
			if(Ivy.var().get("xivy_addons_fileManager_databaseSchemaName")!=null)
			{
				this.schemaName=Ivy.var().get("xivy_addons_fileManager_databaseSchemaName").trim();
			}
		}else{
			this.schemaName=_schemaName.trim();
		}

		if(this.schemaName!=null && this.schemaName.trim().length()>0)
		{
			this.tableNameSpace="\""+this.schemaName+"\""+"."+"\""+this.tableName+"\"";
			this.dirTableNameSpace="\""+this.schemaName+"\""+"."+"\""+this.dirTableName+"\"";
			this.fileContentTableNameSpace="\""+this.schemaName+"\""+"."+"\""+this.fileContentTableName+"\"";
		}else{
			this.tableNameSpace=this.tableName;
			this.dirTableNameSpace=this.dirTableName;
			this.fileContentTableNameSpace=this.fileContentTableName;
		}

		if(_securityActivated==null)
		{
			if(Ivy.var().get("xivy_addons_fileManager_activateSecurity")!=null || 
					Ivy.var().get("xivy_addons_fileManager_activateSecurity").trim().length()>0)
			{
				this.securityActivated=Ivy.var().get("xivy_addons_fileManager_activateSecurity").trim().compareTo("1")==0?true:false;
			}else{
				this.securityActivated=false;
			}
		}else{
			this.securityActivated=_securityActivated;
		}
		this.makeSecurityController();
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = this.getDatabase().getAndLockConnection();
			if(connection.getDatabaseConnection().getMetaData().getDatabaseProductName().toLowerCase().contains("mysql")){
				setEscapeChar("\\\\");
				this.securityController.setEscapeChar("\\\\");
			}
		} catch (Exception e) {
			Ivy.log().error("Error while getting the database product name");
		}finally{
			if(connection!=null ){
				this.getDatabase().giveBackAndUnlockConnection(connection);
			}
		}
	}

	/**
	 * @throws Exception 
	 * 
	 */
	private void makeSecurityController() throws Exception{
		this.securityController=new DirectorySecurityController(this.ivyDBConnectionName, this.dirTableName, this.schemaName);
		this.securityController.setDatabase(this.getDatabase());
	}

	/**
	 * used to get Ivy IExternalDatabase object with given user friendly name of Ivy Database configuration
	 * @param _nameOfTheDatabaseConnection: the user friendly name of Ivy Database configuration
	 * @return the IExternalDatabase object
	 * @throws Exception 
	 * @throws EnvironmentNotAvailableException 
	 */
	private IExternalDatabase getDatabase() throws Exception{
		if(database==null){
			final String _nameOfTheDatabaseConnection = this.ivyDBConnectionName;
			database = Ivy.session().getSecurityContext().executeAsSystemUser(new Callable<IExternalDatabase>(){
				public IExternalDatabase call() throws Exception {
					IExternalDatabaseApplicationContext context = (IExternalDatabaseApplicationContext)Ivy.wf().getApplication().getAdapter(IExternalDatabaseApplicationContext.class);
					return context.getExternalDatabase(_nameOfTheDatabaseConnection);
				}
			});
		}
		return database;	
	}
	
	private static List<Record> executeStmt(PreparedStatement _stmt) throws Exception{

		if(_stmt == null){
			throw(new SQLException("Invalid PreparedStatement","PreparedStatement Null"));
		}

		ResultSet rst = null;
		rst=_stmt.executeQuery();
		List<Record> recordList= (List<Record>) List.create(Record.class);
		try{
			ResultSetMetaData rsmd = rst.getMetaData();
			int numCols = rsmd.getColumnCount();
			List<String> colNames= List.create(String.class);
			for(int i=1; i<=numCols; i++){
				colNames.add(rsmd.getColumnName(i));
				//Ivy.log().debug(rsmd.getColumnName(i));
			}
			while(rst.next()){
				List<Object> values = List.create(numCols);
				for(int i=1; i<=numCols; i++){

					if(rst.getString(i)==null)
						values.add(" ");
					else values.add(rst.getString(i));
				}
				Record rec = new Record(colNames,values);
				recordList.add(rec);
			}
		}catch(Exception ex){
			Ivy.log().error(ex.getMessage(), ex);
		}finally
		{
			DatabaseUtil.close(rst);
		}
		return recordList;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#changeModificationInformations(java.io.File, java.lang.String)
	 */
	@Override
	public void changeModificationInformations(java.io.File _file, String _userID) throws Exception {
		if(_file == null || !_file.exists() || _userID == null)
		{
			throw new Exception("File null or doesn't exist, or userID null in changeModificationInformations method.");
		}
		IExternalDatabaseRuntimeConnection connection = null;
		try {

			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			String query= "UPDATE "+this.tableNameSpace+" SET FileSize = ?, ModificationDate = ?, ModificationTime = ?, ModificationUserId = ? WHERE FilePath = ?";
			PreparedStatement stmt = null;
			try{			
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, FileHandler.getFileSize(_file));
				stmt.setString(2, new Date().format("dd.MM.yyyy"));
				stmt.setString(3, new Time().format("HH:mm.ss"));
				stmt.setString(4, _userID);
				stmt.setString(5, escapeBackSlash(_file.getPath()));
				stmt.executeUpdate();
			}
			finally{
				DatabaseUtil.close(stmt);
			}
		} 
		finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#createDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public ReturnedMessage createDirectory(String _destinationPath,
			String _newDirectoryName) throws Exception {
		if(_destinationPath==null || _newDirectoryName==null || _newDirectoryName.trim().equals(""))
		{
			throw new IllegalArgumentException("One of the parameters in "+this.getClass().getName()+", method createDirectory(String destinationPath, String newDirectoryName) is not set.");
		}
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		if(!_destinationPath.trim().equals("")){
			_destinationPath=formatPathForDirectory(_destinationPath);
		}
		return this.createDirectory(_destinationPath+_newDirectoryName.trim());
	}

	@Override
	public ReturnedMessage createDirectory(String _newDirectoryPath) throws Exception{
		if(_newDirectoryPath==null ||  _newDirectoryPath.trim().equals("")){
			throw new IllegalArgumentException("One of the parameters in "+this.getClass().getName()+", method createDirectory(String _newDirectoryPath) is not set.");
		}
		if(_newDirectoryPath.contains("%")){
			throw new IllegalArgumentException("The directories name cannot contain a percent sign (%).");
		}
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		if(this.directoryExists(_newDirectoryPath.trim()))
		{//already exists
			message.setType(FileHandler.INFORMATION_MESSAGE);
			message.setText("The directory to create already exists.");
			return message;
		}
		if(this.securityActivated)
		{
			//this.securityController.createIndestructibleDirectory(_newDirectoryPath.trim(), null);
			this.securityController.createDirectoryWithParentSecurity(_newDirectoryPath.trim());
			message.setType(FileHandler.SUCCESS_MESSAGE);
			message.setText("The directory was successfuly created.");
			return message;
		}

		String base = "INSERT INTO "+this.dirTableNameSpace+
		" (dir_name, dir_path, creation_user_id, creation_date, creation_time, modification_user_id, modification_date," +
		"modification_time,is_protected, cmdr, cod, cud, cdd, cwf, cdf) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		java.util.Date d = new java.util.Date();

		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(base);
				stmt.setString(1,DirectoryHelper.getDirectoryNameFromPath(_newDirectoryPath));
				stmt.setString(2, escapeBackSlash(_newDirectoryPath.trim()));
				stmt.setString(3, Ivy.session().getSessionUserName());
				stmt.setDate(4, new java.sql.Date(d.getTime()));
				stmt.setTime(5, new java.sql.Time(d.getTime()));
				stmt.setString(6, Ivy.session().getSessionUserName());
				stmt.setDate(7, new java.sql.Date(d.getTime()));
				stmt.setTime(8, new java.sql.Time(d.getTime()));
				stmt.setInt(9, 0);
				stmt.setString(10, "");
				stmt.setString(11, "");
				stmt.setString(12, "");
				stmt.setString(13, "");
				stmt.setString(14, "");
				stmt.setString(15, "");
				int i =  stmt.executeUpdate();

				if(i>0)
				{//Creation successful
					message.setType(FileHandler.SUCCESS_MESSAGE);
					message.setText("The directory was successfuly created.");
				}else
				{
					message.setType(FileHandler.ERROR_MESSAGE);
					message.setText("The directory could not be created.");
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return message;
	}

	/**
	 * 
	 * @param _path
	 * @return
	 * @throws Exception
	 */
	public boolean directoryExists(String _path) throws Exception
	{
		return this.securityController.directoryExists(_path);	
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#deleteDirectory(java.lang.String)
	 */
	@Override
	public ReturnedMessage deleteDirectory(String _directoryPath)
	throws Exception {
		if(_directoryPath==null || _directoryPath.trim().equals(""))
		{
			throw new IllegalArgumentException("The 'directoryPath' parameter in "+this.getClass().getName()+", method deleteDirectory(String directoryPath) is not set.");
		}
		_directoryPath=formatPathForDirectoryWithoutLastSeparator(_directoryPath.trim());
		
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		//Check if the directory exists, if not return
		if(!this.directoryExists(_directoryPath)){
			message.setType(FileHandler.INFORMATION_MESSAGE);
			message.setText("The directory to delete does not exist.");
			return message;
		}

		if(this.securityActivated)
		{//Check if user allowed to delete a directory
			if(!this.securityController.canUserDeleteDirectory(_directoryPath, Ivy.session().getSessionUserName()))
			{
				message.setType(FileHandler.INFORMATION_MESSAGE);
				message.setText("The user '"+Ivy.session().getSessionUserName()+"' doesn't have the right to delete the directory.");
				return message;
			}
		}
		
		//delete all the files under the directory structure
		message = this.deleteAllFilesUnderDirectory(_directoryPath);
		_directoryPath=escapeUnderscoreInPath(_directoryPath);

		//Query to delete the directory and all its sub directories
		String base ="DELETE FROM "+this.dirTableNameSpace+" WHERE dir_path LIKE ? ESCAPE '"+escapeChar+"'";
		Ivy.log().info("DELETE FROM "+this.dirTableNameSpace+" WHERE dir_path LIKE "+_directoryPath+" ESCAPE '"+escapeChar+"'");
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(base);
				//delete all the children directories
				//Ivy.log().info("Delete Directories under the path " +_directoryPath);
				stmt.setString(1, _directoryPath+"/%");
				stmt.executeUpdate();

				//delete the directory himself
				stmt.setString(1, _directoryPath);
				stmt.executeUpdate();

				message.setType(FileHandler.SUCCESS_MESSAGE);
				message.setText("The directory and all its files were successfully deleted.");
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return message;
	}

	@Override
	public ReturnedMessage deleteDirectoryAsAdministrator(String _directoryPath)
	throws Exception {
		if(_directoryPath==null || _directoryPath.trim().equals(""))
		{
			throw new IllegalArgumentException("The 'directoryPath' parameter in "+this.getClass().getName()+", method deleteDirectory(String directoryPath) is not set.");
		}
		_directoryPath=formatPathForDirectoryWithoutLastSeparator(_directoryPath.trim());
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		//Check if the directory exists, if not return
		if(!this.directoryExists(_directoryPath)){
			message.setType(FileHandler.INFORMATION_MESSAGE);
			message.setText("The directory to delete does not exist.");
			return message;
		}
		//delete all the files under the directory structure
		message = this.deleteAllFilesUnderDirectory(_directoryPath);
		_directoryPath=escapeUnderscoreInPath(_directoryPath);
		//Query to delete the directory and all its sub directories
		String base ="DELETE FROM "+this.dirTableNameSpace+" WHERE dir_path LIKE ? ESCAPE '"+escapeChar+"'";

		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(base);
				stmt.setString(1, _directoryPath);
				stmt.executeUpdate();

				//delete the directory himself
				stmt.setString(1, _directoryPath);
				stmt.executeUpdate();

				message.setType(FileHandler.SUCCESS_MESSAGE);
				message.setText("The directory and all its files were successfully deleted.");
			}finally{
				
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return message;
	}

	/**
	 * delete all the files from the db that are in the file Structure under a directory.
	 * @param _directoryPath
	 * @return
	 * @throws Exception
	 */
	public ReturnedMessage deleteAllFilesUnderDirectory(String _directoryPath) throws Exception{
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		if(_directoryPath==null || _directoryPath.trim().equals(""))
		{
			message.setType(FileHandler.INFORMATION_MESSAGE);
			message.setText("The directory to delete does not exist.");
			return message;
		}

		//Query to delete the files under a path
		String base ="DELETE FROM "+this.tableNameSpace+" WHERE FilePath LIKE ? ESCAPE '"+escapeChar+"'";
		String query="DELETE FROM "+this.fileContentTableNameSpace+" WHERE file_id = ?";
		//Ivy.log().info("We get the file ids...");
		int[] ids = this.getFileIdsUnderPath(_directoryPath+"/%");
		_directoryPath=escapeUnderscoreInPath(_directoryPath);
		//Ivy.log().info("File ids under the path to delete" +_directoryPath + " "+ids.length);
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			boolean deleteHistory=this.activateHistory && super.getFileActionHistoryController().getConfig().isDeleteFileTracked();
			try{
				if(ids!=null)
				{
					stmt = jdbcConnection.prepareStatement(query);
					for(int i=0; i<ids.length; i++)
					{
						stmt.setInt(1, ids[i]);
						stmt.executeUpdate();
						if(deleteHistory)
						{
							super.getFileActionHistoryController().createNewActionHistory(ids[i], (short) 5, 
									Ivy.session().getSessionUserName(), "Delete all files under "+_directoryPath,jdbcConnection);
						}
					}
					if(this.activateFileVersioning)
					{
						for(int i=0; i<ids.length; i++)
						{
							this.fvc.deleteAllVersionsFromFile(ids[i],jdbcConnection);
						}
					}

				}
				stmt = jdbcConnection.prepareStatement(base);
				stmt.setString(1, _directoryPath.replaceAll("_", "[_]")+"/%");
				stmt.executeUpdate();
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return message;
	}


	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#deleteDocumentOnServer(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer)
	 */
	@Override
	public ReturnedMessage deleteDocumentOnServer(DocumentOnServer document)throws Exception {
		if(document==null || document.getPath() ==null || document.getPath().trim().equals(""))
		{
			throw new IllegalArgumentException("The 'document' parameter in "+this.getClass().getName()+", method deleteDocumentOnServer(DocumentOnServer document) is not set.");
		}
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		message.setType(FileHandler.SUCCESS_MESSAGE);
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			List<DocumentOnServer>l = List.create(DocumentOnServer.class);
			l.add(document);
			this.deleteDocumentsInDBOnly(l, jdbcConnection);
		}catch(Exception ex){
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText(ex.getMessage());
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		
		return message;
	}

	/**
	 * Deletes the actual content of the document and makes the last version as the active document.<br><br>
	 * <b>Warnings: </b>
	 * <ul><li>Only available if the file versioning feature is activated.<br>
	 * If the file versioning feature is not activated, the ReturnedMessage object
	 * has the FileHandler.ERROR_MESSAGE type and nothing is done. Its text field contains an appropriate error message.<br>
	 * <li>If the given document has no version (its version number is 1), this method will have no effect.<br>
	 * In that case the ReturnedMessage object has the FileHandler.INFORMATION_MESSAGE type. Its text field contains an appropriate information message.
	 * <li>If the given document is locked, it mights be edited by somebody. In that case nothing will be done and an appropriate ERROR_MESSAGE will be returned.
	 * </ul>
	 * @param doc the DocumentOnServer object. At least it must have a valid fileId. If not an IllegalArgumentException will be thrown. 
	 * @return ReturnedMessage reflecting the status of the operation. Its type is FileHandler.SUCCESS_MESSAGE if the operation was successful.
	 * @throws Exception if any SQL Exceptions are thrown.
	 * @throws NumberFormatException  if the given DocumentOnServer is null or does not have a valid fileId.
	 */
	public ReturnedMessage rollBackToPreviousVersion(DocumentOnServer doc) throws NumberFormatException, Exception
	{
		if(doc==null || doc.getFileID().trim().equals(""))
		{
			throw new IllegalArgumentException("The 'document' parameter in "+this.getClass().getName()+", method rollBackToPreviousVersion(DocumentOnServer document) is not valid.");
		}
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		if(!this.activateFileVersioning)
		{
			message.setText("The File Versioning Feature is not activated. This method can not be used.");
			message.setType(FileHandler.ERROR_MESSAGE); 
			return message;
		}
		long id = Long.parseLong(doc.getFileID());
		if(doc.getVersionnumber().intValue()<=0)
		{//the doc just contains the file id

			doc= this.getDocumentOnServerById(id, false);
			if(doc==null || doc.getFileID().trim().length()==0)
			{
				message.setText("The Document with fileId "+id+" does not exist.");
				message.setType(FileHandler.ERROR_MESSAGE); 
				return message;
			}
		}
		if(!doc.getLocked().equals("0"))
		{
			message.setText("The Document is actually locked by "+doc.getLockingUserID()+". This operation can not be performed.");
			message.setType(FileHandler.ERROR_MESSAGE); 
			return message;
		}
		if(doc.getVersionnumber().intValue()==1)
		{
			message.setType(FileHandler.INFORMATION_MESSAGE);
			message.setText("The document contains no previous version to rollback.");
			return message;

		}
		if(this.fvc.rollbackLastVersionAsActiveDocument(id))
		{
			if(super.getFileActionHistoryController()!=null && super.getFileActionHistoryController().getConfig().isDeleteFileTracked())
			{
				super.getFileActionHistoryController().createNewActionHistory(Long.parseLong(doc.getFileID()), (short) 12, Ivy.session().getSessionUserName(), "Version "+doc.getVersionnumber().intValue());
			}
			message.setType(FileHandler.SUCCESS_MESSAGE);
			message.setText("The document was successfuly rolledback to its previous version.");
			return message;
		}else{
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText("The document could not be rolledback to its previous version. Please check this document manually.");
			return message;
		}

	}


	@Override
	public ReturnedMessage deleteDocumentOnServers(List<DocumentOnServer> documents) throws Exception {
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		message.setType(FileHandler.SUCCESS_MESSAGE);
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			this.deleteDocumentsInDBOnly(documents, jdbcConnection);
		}catch(Exception ex){
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText(ex.getMessage());
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		
		return message;
	}

	/**
	 * delete documents from the DB
	 * @param _documents the list of the DocumentOnServer to delete
	 * @return the number of items deleted
	 * @throws Exception
	 */
	public int deleteDocuments(List<DocumentOnServer> _documents)throws Exception{
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			return this.deleteDocumentsInDBOnly(_documents, jdbcConnection);
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
	}

	/**
	 * delete files from the DB
	 * @param _files the list of the java.io.File to delete
	 * @return the number of items deleted
	 * @throws Exception
	 */
	public int deleteFiles(List<java.io.File> _files)throws Exception{
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			return this.deleteFilesInDBOnly(_files, null, jdbcConnection);
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}

	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getDirectoryTree(java.lang.String)
	 */
	@Override
	public Tree getDirectoryTree(String rootPath) throws Exception {
		Tree RDTree = new Tree();
		if(rootPath==null || rootPath.trim().equals(""))
		{
			throw new Exception("");
		}
		rootPath = formatPathForDirectoryWithoutLastSeparator(rootPath);
		if(!directoryExists(rootPath))
		{//create the directory

			String parentPath="";
			if(rootPath.lastIndexOf("/")>1)
			{
				parentPath = rootPath.substring(0,rootPath.lastIndexOf("/"));
				String dirname = rootPath.substring(rootPath.lastIndexOf("/")+1, rootPath.length());
				this.createDirectory(parentPath, dirname);
			}else{
				this.createDirectory("", rootPath);
			}

		}
		ArrayList<FolderOnServer> l = this.getListDirectoriesUnderPath(rootPath);
		if(!l.isEmpty()){
			RDTree.setValue(l.get(0));
			RDTree.setInfo(l.get(0).getName());
			l.remove(0);
			fillRDTree(RDTree, l);
		}
		return RDTree;
	}

	/**
	 * For internal use. Recursive method to fill a Directory Tree.<br>
	 * Used in public Tree getDirectoryTree(String rootPath)
	 * @param tree The tree to fill. If the Tree represents a locked Folder, It will not display its children nodes.
	 * @param dirs The list of Children FolderOnServer nodes
	 */
	private void fillRDTree(Tree tree, ArrayList<FolderOnServer> dirs){
		if(dirs==null || dirs.isEmpty() || tree ==null)
		{
			return;
		}
		if(!(tree.getValue() instanceof FolderOnServer)){
			return;
		}

		FolderOnServer fos = (FolderOnServer) tree.getValue();
		ArrayList<FolderOnServer> children = getDirectChildFoldersInList(fos, dirs);
		for(FolderOnServer f:children){

			tree.createChild(f, f.getName());
			if(!(f instanceof LockedFolder)){
				fillRDTree(tree.getLastChild(),dirs);
			}
		}
	}

	/**
	 * Finds out all the direct children from a parent directory in a list of given directories.<br>
	 * @param parent the parent directory (FolderOnServer object)
	 * @param dirs the potential FolderOnServer children in an ArrayList<FolderOnServer>
	 * @return the ArrayList<FolderOnServer> filled with all the direct children of the parent
	 */
	public ArrayList<FolderOnServer> getDirectChildFoldersInList(FolderOnServer parent, ArrayList<FolderOnServer> dirs){
		ArrayList<FolderOnServer> l = new ArrayList<FolderOnServer>();
		if(parent==null || parent.getPath()==null || parent.getPath().trim().length()==0 || dirs==null || dirs.isEmpty()){
			return l;
		}
		String path = parent.getPath();
		for(FolderOnServer f:dirs){
			if(f.getPath().startsWith(path) && f.getPath().lastIndexOf("/")==path.length())
			{
				l.add(f);
			}
		}
		return l;
	}

	/**
	 * Returns all the directories present under the given path.<br> 
	 * The directory represented by the given path is also included in this list.
	 * @param rootPath
	 * @return
	 */
	public ArrayList<FolderOnServer> getListDirectoriesUnderPath(String rootPath) throws Exception
	{

		if(this.securityActivated){
			return this.securityController.getListDirectoriesUnderPath(rootPath, null);
		}

		ArrayList<FolderOnServer> l = new ArrayList<FolderOnServer>();
		if(rootPath==null || rootPath.trim().equals("")){
			return l;
		}
		rootPath = formatPathForDirectoryWithoutLastSeparator(rootPath);
		
		String base= "SELECT * FROM "+this.dirTableNameSpace+" WHERE dir_path = ? ORDER BY dir_path ASC"; 
		IExternalDatabaseRuntimeConnection connection=null;

		//Recordset rset = null;
		List<Record> recordList= (List<Record>) List.create(Record.class);
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(base);
				//Select the root
				stmt.setString(1, rootPath);
				//rset=executeStatement(stmt);
				recordList=executeStmt(stmt);
				if(recordList.size()==1)
				{// there is one dir with the denoted rootpath
					FolderOnServer fos = new FolderOnServer();
					Record rec = recordList.get(0);
					fos.setId(Integer.parseInt(rec.getField("id").toString()));
					fos.setName(rec.getField("dir_name").toString());
					fos.setPath(formatPathForDirectoryWithoutLastSeparator(rec.getField("dir_path").toString()));
					fos.setIs_protected(rec.getField("is_protected").toString().equals("1"));
					fos.setCmrd(getListFromString(rec.getField("cmdr").toString(),","));
					fos.setCod(getListFromString(rec.getField("cod").toString(),","));
					fos.setCud(getListFromString(rec.getField("cud").toString(),","));
					fos.setCdd(getListFromString(rec.getField("cdd").toString(),","));
					fos.setCwf(getListFromString(rec.getField("cwf").toString(),","));
					fos.setCdf(getListFromString(rec.getField("cdf").toString(),","));
					fos.setCanUserDeleteDir(false);
					fos.setCanUserDeleteFiles(true);
					fos.setCanUserManageRights(false);
					fos.setCanUserOpenDir(true);
					fos.setCanUserUpdateDir(true);
					fos.setCanUserWriteFiles(true);
					fos.setIsRoot(true);
					l.add(fos);
					//Select all the children
					base= "SELECT * FROM "+this.dirTableNameSpace+" WHERE dir_path LIKE ? ESCAPE '"+escapeChar+"' ORDER BY dir_path ASC"; 
					stmt = jdbcConnection.prepareStatement(base);
					rootPath=escapeUnderscoreInPath(rootPath);
					stmt.setString(1, rootPath+"/%");
					//rset=executeStatement(stmt);
					recordList=executeStmt(stmt);
					for(Record rec1: recordList){
						FolderOnServer fos1 = new FolderOnServer();
						fos1.setId(Integer.parseInt(rec1.getField("id").toString()));
						fos1.setName(rec1.getField("dir_name").toString());
						fos1.setPath(formatPathForDirectoryWithoutLastSeparator(rec1.getField("dir_path").toString()));
						fos1.setIs_protected(rec1.getField("is_protected").toString().equals("1"));
						fos1.setCmrd(getListFromString(rec1.getField("cmdr").toString(),","));
						fos1.setCod(getListFromString(rec1.getField("cod").toString(),","));
						fos1.setCud(getListFromString(rec1.getField("cud").toString(),","));
						fos1.setCdd(getListFromString(rec1.getField("cdd").toString(),","));
						fos1.setCwf(getListFromString(rec1.getField("cwf").toString(),","));
						fos1.setCdf(getListFromString(rec1.getField("cdf").toString(),","));
						fos1.setCanUserDeleteDir(true);
						fos1.setCanUserDeleteFiles(true);
						fos1.setCanUserManageRights(false);
						fos1.setCanUserOpenDir(true);
						fos1.setCanUserUpdateDir(true);
						fos1.setCanUserWriteFiles(true);
						l.add(fos1);
					}
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return l;
	}
	
	/**
	 * 
	 */
	public ArrayList<FolderOnServer> getListDirectDirectoriesUnderPath(String rootPath) throws Exception
	{

		if(this.securityActivated){
			return this.securityController.getListDirectoriesUnderPath(rootPath, null);
		}

		ArrayList<FolderOnServer> l = new ArrayList<FolderOnServer>();
		if(rootPath==null || rootPath.trim().equals("")){
			return l;
		}
		rootPath = formatPathForDirectory(rootPath);
		rootPath=escapeUnderscoreInPath(rootPath);
		String base= "SELECT * FROM "+this.dirTableNameSpace+" WHERE dir_path LIKE ? ESCAPE '"+escapeChar+"' AND dir_path NOT LIKE ? ESCAPE '"+escapeChar+"' ORDER BY dir_path ASC"; 
		IExternalDatabaseRuntimeConnection connection=null;

		//Recordset rset = null;
		List<Record> recordList= (List<Record>) List.create(Record.class);
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(base);
				//Select the root
				stmt.setString(1, rootPath+"%");
				stmt.setString(2, rootPath+"%/%");
				//rset=executeStatement(stmt);
				recordList=executeStmt(stmt);
				for(Record rec:recordList)
				{
					FolderOnServer fos = new FolderOnServer();
					fos.setId(Integer.parseInt(rec.getField("id").toString()));
					fos.setName(rec.getField("dir_name").toString());
					fos.setPath(formatPathForDirectoryWithoutLastSeparator(rec.getField("dir_path").toString()));
					fos.setIs_protected(rec.getField("is_protected").toString().equals("1"));
					fos.setCmrd(getListFromString(rec.getField("cmdr").toString(),","));
					fos.setCod(getListFromString(rec.getField("cod").toString(),","));
					fos.setCud(getListFromString(rec.getField("cud").toString(),","));
					fos.setCdd(getListFromString(rec.getField("cdd").toString(),","));
					fos.setCwf(getListFromString(rec.getField("cwf").toString(),","));
					fos.setCdf(getListFromString(rec.getField("cdf").toString(),","));
					fos.setIsRoot(false);
					l.add(fos);
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return l;
	}
	

	/**
	 * transforms a String that represents a list of token separated with a delimiter into a List<String>
	 * @param s: the String 
	 * @param list_sep: the delimiter
	 * @return the List<String>
	 */
	public List<String> getListFromString(String s, String list_sep){
		List<String> l = List.create(String.class);
		if(s==null || s.trim().equals("") || list_sep==null || list_sep.trim().equals(""))
		{
			return l;
		}
		Scanner sc = new Scanner(s);
		sc.useDelimiter(list_sep);
		while(sc.hasNext()){
			String t = sc.next().trim();

			if(t.length()>0)
			{
				l.add(t);
			}
		}
		sc.close();
		return l;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getDocumentOnServer(java.lang.String)
	 */
	@Override
	public DocumentOnServer getDocumentOnServer(String filePath)
	throws Exception {
		DocumentOnServer doc = new DocumentOnServer();
		if(filePath==null || filePath.trim().equals("")){
			return doc;
		}
		FolderOnServer fos=null;
		if(this.securityActivated)
		{
			fos=this.securityController.getDirectoryWithPath(filePath.substring(0, filePath.lastIndexOf("/")-1));
		}
		//Recordset rset = null;
		List<Record> recordList= (List<Record>) List.create(Record.class);

		String query="";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();

			query="SELECT * FROM "+this.tableNameSpace+" WHERE FilePath = ?";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, filePath);
				//rset=executeStatement(stmt);
				recordList=executeStmt(stmt);
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		if(!recordList.isEmpty())
		{
			doc = this.makeDocsWithRecordList(recordList, fos).get(0);
		}
		return doc;
	}

	@Override
	public boolean fileExists(String filePath) throws Exception {
		if(filePath == null || filePath.trim().equals(""))
		{
			return false;
		}
		boolean flag = false;
		filePath = formatPath(filePath);
		String query="";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();

			query="SELECT * FROM "+this.tableNameSpace+" WHERE FilePath = ?";
			PreparedStatement stmt = null;
			ResultSet rset=null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, filePath);
				rset = stmt.executeQuery();
				if(rset.next())
				{
					flag=true;
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return flag;
	}

	/**
	 * return the id of the DocumentOnServer denoted by the given path
	 * @param _path: the path of the document
	 * @return the id of the document or -1 if not found
	 */
	public int getDocIdWithPath(String _path) throws Exception
	{
		int i =-1;
		if(_path==null || _path.trim().equals(""))
		{
			return i;
		}
		_path=formatPath(_path);
		String query="";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			ResultSet rset = null;
			query="SELECT FileId FROM "+this.tableNameSpace+" WHERE FilePath = ?";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, _path);
				rset=stmt.executeQuery();
				if(rset.next()){
					i = rset.getInt("FileId");
				}

			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return i;
	}

	/**
	 * For private use only
	 * @param _path the path that should be resolved as file Id.
	 * @param con the java.sql.Connection to the database.<br> 
	 * THIS METHOD DOES NOT RELEASE THIS CONNECTION. THIS SHOULD BE DONE IN THE CALLING METHOD.
	 * @return the id of the found file at the given path. If no File could be found, the returned id is -1.
	 * @throws Exception
	 */
	private int getDocIdWithPath(String _path, Connection con) throws Exception
	{
		if(con==null || con.isClosed())
		{
			throw new IllegalArgumentException("The java.sql.Connection Object is null or closed. Method getDocIdWithPath in FileStoreDBHanler.");
		}
		int i =-1;
		if(_path==null || _path.trim().equals(""))
		{
			return i;
		}
		_path=formatPath(_path);
		_path=escapeUnderscoreInPath(_path);
		String query="";


		ResultSet rset = null;
		query="SELECT FileId FROM "+this.tableNameSpace+" WHERE FilePath LIKE ? ESCAPE '"+escapeChar+"'";
		PreparedStatement stmt = null;
		try{
			stmt = con.prepareStatement(query);
			stmt.setString(1, _path);
			rset=stmt.executeQuery();
			if(rset.next()){
				i = rset.getInt("FileId");
			}

		}finally{
			DatabaseUtil.close(stmt);
		}

		return i;
	}

	/**
	 * returns an array of all the file ids contained under a specifically path
	 * @param _path
	 * @return
	 * @throws Exception
	 */
	public int[] getFileIdsUnderPath(String _path) throws Exception
	{
		int[] i =null;
		if(_path==null || _path.trim().equals(""))
		{
			return i;
		}
		_path=formatPath(_path);
		_path=escapeUnderscoreInPath(_path);
		String query="";

		IExternalDatabaseRuntimeConnection connection = null;
		//Recordset rset = null;
		List<Record> recordList= (List<Record>) List.create(Record.class);
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();

			query="SELECT FileId FROM "+this.tableNameSpace+" WHERE FilePath LIKE ? ESCAPE '"+escapeChar+"'";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, _path);
				//rset = executeStatement(stmt);
				recordList = executeStmt(stmt);
				int j=0;
				i= new int[recordList.size()];
				for(Record r : recordList)
				{
					try{
						i[j]=Integer.parseInt(r.getField("FileId").toString());
						j++;
					}catch(Exception ex)
					{

					}
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return i;
	}

	@Override
	public boolean documentOnServerExists(DocumentOnServer document, String path)
	throws Exception {
		if(document== null || document.getFilename()==null || document.getFilename().trim().equals("") || path==null || path.trim().equals(""))
		{
			return false;
		}
		path=formatPathForDirectory(path);
		DocumentOnServer doc = this.getDocumentOnServer(path+document.getFilename());
		int i=0;
		try{
			i= Integer.parseInt(doc.getFileID());
		}catch(Exception ex)
		{//NullPointerException or NumberFormatException...
			i=0;
		}
		if(i<=0){
			return false;
		}else{
			return true;
		}

	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getDocumentOnServerWithJavaFile(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer)
	 */
	@Override
	public DocumentOnServer getDocumentOnServerWithJavaFile(DocumentOnServer docu)
	throws Exception {
		if(docu==null || docu.getPath()==null || docu.getPath().trim().equals(""))
		{
			return docu;
		}

		String query="";
		IExternalDatabaseRuntimeConnection connection = null;
		String path = formatPath(docu.getPath().trim());
		DocumentOnServer doc = new DocumentOnServer();
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();

			query="SELECT * FROM "+this.tableNameSpace+
			" INNER JOIN "+this.fileContentTableNameSpace+" ON "+this.tableNameSpace+".FileId = "+this.fileContentTableNameSpace+".file_id" +
			" WHERE "+this.tableNameSpace+".FilePath = ? ";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, path);
				ResultSet rst = stmt.executeQuery();
				if(this.securityActivated)
				{
					FolderOnServer fos = this.securityController.getDirectoryWithPath(docu.getPath().substring(0, docu.getPath().lastIndexOf("/")-1));
					doc = this.makeDocumentFromResultSet(rst,true, fos);
				}else{
					doc = this.makeDocumentFromResultSet(rst,true, null);
				}

				if(doc==null || doc.getFilename().trim().length()==0)
				{//May be we had the files on the file Set before.... we try to find it on the file set
					doc=docu;

					java.io.File f = new java.io.File(path);
					if(f.isFile() && docu.getFileID().trim().length()>0)
					{
						FileInputStream is = null;
						try{
							String query2 ="INSERT INTO "+this.fileContentTableNameSpace+" (file_id, file_content) VALUES (?,?)";
							stmt = jdbcConnection.prepareStatement(query2);
							is = new FileInputStream ( f );   
							stmt.setInt(1, Integer.parseInt(docu.getFileID().trim()));
							stmt.setBinaryStream (2, is, (int) f.length() ); 
							stmt.executeUpdate();
						}finally{
							if(is!=null)
							{
								is.close();
							}
						}
						doc.setJavaFile(f);
					}
				}
			}catch(Exception ex){
				Ivy.log().error("Exception " +ex.getMessage(),ex);
			}
			finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}


		return doc;
	}



	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getDocumentOnServerWithJavaFile(java.lang.String)
	 */
	@Override
	public DocumentOnServer getDocumentOnServerWithJavaFile(String filePath)
	throws Exception {
		if(filePath==null || filePath.trim().equals(""))
		{
			return null;
		}
		DocumentOnServer doc = new DocumentOnServer();
		FolderOnServer folder=null;
		if(this.securityActivated)
		{
			folder=this.securityController.getDirectoryWithPath(filePath.substring(0, filePath.lastIndexOf("/")-1));
		}
		String query="";
		IExternalDatabaseRuntimeConnection connection = null;
		String path = formatPath(filePath.trim());
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();

			query="SELECT * FROM "+this.tableNameSpace+
			" INNER JOIN "+this.fileContentTableNameSpace+" ON "+this.tableNameSpace+".FileId = "+this.fileContentTableNameSpace+".file_id" +
			" WHERE "+this.tableNameSpace+".FilePath = ? ";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, path);
				ResultSet rst = stmt.executeQuery();
				doc=this.makeDocumentFromResultSet(rst, true, folder);
			}catch(Exception ex){
				Ivy.log().error("Exception " +ex.getMessage(),ex);
			}
			finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return doc;
	}

	@Override
	public DocumentOnServer getDocumentOnServerWithJavaFile(long fileid)
	throws Exception {
		if(fileid<=0)
		{
			return null;
		}
		String.valueOf(fileid);
		DocumentOnServer doc = new DocumentOnServer();

		String query="";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();

			query="SELECT * FROM "+this.tableNameSpace+
			" INNER JOIN "+this.fileContentTableNameSpace+" ON "+this.tableNameSpace+".FileId = "+this.fileContentTableNameSpace+".file_id" +
			" WHERE "+this.tableNameSpace+".FileId = ? ";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1,fileid);
				ResultSet rst = stmt.executeQuery();
				doc = this.makeDocumentFromResultSet(rst, true, null);
				if (this.securityActivated) {
					FolderOnServer folder = this.securityController.getDirectoryWithPath(doc.getPath().substring(0,doc.getPath().lastIndexOf("/") - 1));
					doc.setCanUserDelete(folder.getCanUserDeleteFiles());
					doc.setCanUserRead(folder.getCanUserOpenDir());
					doc.setCanUserWrite(folder.getCanUserWriteFiles());
				} else {
					doc.setCanUserDelete(true);
					doc.setCanUserRead(true);
					doc.setCanUserWrite(true);
				}
			}catch(Exception ex){
				Ivy.log().error("Exception " +ex.getMessage(),ex);
			}
			finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return doc;
	}

	@Override
	public DocumentOnServer getDocumentOnServerById(long fileid, boolean getJavaFile) throws Exception {

		if(fileid<=0)
		{
			return null;
		}
		String.valueOf(fileid);
		DocumentOnServer doc = new DocumentOnServer();

		String query="";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			if(getJavaFile)
			{
				query="SELECT * FROM "+this.tableNameSpace+
				" INNER JOIN "+this.fileContentTableNameSpace+" ON "+this.tableNameSpace+".FileId = "+this.fileContentTableNameSpace+".file_id" +
				" WHERE "+this.tableNameSpace+".FileId = ? ";
			}else{
				query="SELECT * FROM "+this.tableNameSpace+" WHERE "+this.tableNameSpace+".FileId = ? ";
			}
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileid);
				ResultSet rst = stmt.executeQuery();
				doc = this.makeDocumentFromResultSet(rst, getJavaFile, null);
				if (this.securityActivated) {
					FolderOnServer folder = this.securityController.getDirectoryWithPath(doc.getPath().substring(0,doc.getPath().lastIndexOf("/") - 1));
					doc.setCanUserDelete(folder.getCanUserDeleteFiles());
					doc.setCanUserRead(folder.getCanUserOpenDir());
					doc.setCanUserWrite(folder.getCanUserWriteFiles());
				} else {
					doc.setCanUserDelete(true);
					doc.setCanUserRead(true);
					doc.setCanUserWrite(true);
				}
			}catch(Exception ex){
				Ivy.log().error("Exception " +ex.getMessage(),ex);
			}
			finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return doc;
	}


	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getDocumentOnServersInDirectory(java.lang.String, boolean)
	 */
	@Override
	public List<DocumentOnServer> getDocumentOnServersInDirectory(
			String _path, boolean _isrecursive)
			throws Exception {

		if(this.securityActivated && _isrecursive)
		{
			return this.getDocumentOnServersInPathCheckSecurity(_path);
		}
		if(_path==null || _path.trim().length()==0)
		{
			throw new Exception("Invalid path in getDocumentsInPath method");
		}

		List<DocumentOnServer>  al = List.create(DocumentOnServer.class);

		String folderPath = escapeBackSlash(FileHandler.formatPathWithEndSeparator(_path, false));
		folderPath=escapeUnderscoreInPath(folderPath);
		//Recordset rset = null;
		List<Record> recordList= (List<Record>) List.create(Record.class);

		String query="";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			if(_isrecursive)
			{
				query="SELECT * FROM "+this.tableNameSpace+" WHERE FilePath LIKE ? ESCAPE '"+escapeChar+"'";
			}
			else
			{
				query="SELECT * FROM "+this.tableNameSpace+" WHERE FilePath LIKE ? ESCAPE '"+escapeChar+"' AND FilePath NOT LIKE ? ESCAPE '"+escapeChar+"'";
			}
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				if(_isrecursive)
				{
					stmt.setString(1, folderPath+"%");
				}else
				{
					stmt.setString(1, folderPath+"%");
					stmt.setString(2, folderPath+"%/%");
				}
				//rset=executeStatement(stmt);
				recordList=executeStmt(stmt);
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		if(recordList!=null){
			al.addAll(this.makeDocsWithRecordList(recordList, null));
		}
		al.trimToSize();
		return al;
	}

	/**
	 * for internal private use. Get all the files recursively under a directory, only in directories the user can open.
	 * @param _path
	 * @return
	 * @throws Exception
	 */
	private List<DocumentOnServer> getDocumentOnServersInPathCheckSecurity(String _path) throws Exception
	{
		if(_path==null || _path.trim().length()==0)
		{
			throw new Exception("Invalid path in getDocumentsInPath method");
		}
		List<DocumentOnServer>  al = List.create(DocumentOnServer.class);
		String folderPath = escapeBackSlash(FileHandler.formatPathWithEndSeparator(_path, false));	
		Tree dirs = this.getDirectoryTree(folderPath);
		List<FolderOnServer> folders=getDirsUserCanOpenUnderTree(dirs, null);
		if(folders.size()>0)
		{
			//Recordset rset = null;	
			IExternalDatabaseRuntimeConnection connection = null;
			try {
				connection = getDatabase().getAndLockConnection();
				Connection jdbcConnection=connection.getDatabaseConnection();
				String query="SELECT * FROM "+this.tableNameSpace+" WHERE FilePath LIKE ? ESCAPE '"+escapeChar+"' AND FilePath NOT LIKE ? ESCAPE '"+escapeChar+"'";

				PreparedStatement stmt = null;
				try{
					stmt = jdbcConnection.prepareStatement(query);
					for(FolderOnServer fos:folders){
						String s1 = escapeBackSlash(FileHandler.formatPathWithEndSeparator(fos.getPath(), false));
						s1=escapeUnderscoreInPath(s1);
						stmt.setString(1, s1+"%");
						stmt.setString(2, s1+"%/%");
						//rset=executeStatement(stmt);
						al.addAll(this.makeDocsWithRecordList(executeStmt(stmt),fos));
					}

				}finally{
					DatabaseUtil.close(stmt);
				}
			} finally{
				if(connection!=null ){
					database.giveBackAndUnlockConnection(connection);
				}
			}

		}
		return al;
	}

	/**
	 * for internal private use only. build Documents with a RecordList.
	 * @param recordList
	 * @return
	 */
	private List<DocumentOnServer> makeDocsWithRecordList(List<Record> recordList, FolderOnServer fos)
	{
		List<DocumentOnServer>  al = List.create(DocumentOnServer.class);
		if(recordList==null || recordList.isEmpty())
		{
			return al;
		}

		for(Record rec: recordList){

			DocumentOnServer doc = new DocumentOnServer();
			doc.setFileID(rec.getField("FileId").toString());
			doc.setFilename(rec.getField("FileName").toString());
			doc.setPath(rec.getField("FilePath").toString());
			doc.setFileSize(rec.getField("FileSize").toString());
			doc.setUserID(rec.getField("CreationUserId").toString());
			doc.setCreationDate(rec.getField("CreationDate").toString());
			doc.setCreationTime(rec.getField("CreationTime").toString());
			doc.setModificationUserID(rec.getField("ModificationUserId").toString());
			doc.setModificationDate(rec.getField("ModificationDate").toString());
			doc.setModificationTime(rec.getField("ModificationTime").toString());
			doc.setLocked(rec.getField("Locked").toString());
			doc.setIsLocked(doc.getLocked().compareTo("1")==0);
			doc.setLockingUserID(rec.getField("LockingUserId").toString());
			doc.setDescription(rec.getField("Description").toString());
			if(this.securityActivated && fos!=null)
			{
				doc.setCanUserDelete(fos.getCanUserDeleteFiles());
				doc.setCanUserRead(fos.getCanUserOpenDir());
				doc.setCanUserWrite(fos.getCanUserWriteFiles());
			}else{
				doc.setCanUserDelete(true);
				doc.setCanUserRead(true);
				doc.setCanUserWrite(true);
			}
			if(this.activateFileType)
			{
				try{
					long id = Long.parseLong(rec.getField("filetypeid").toString());
					if(id>0)
					{
						doc.setFileType(this.ftController.getFileTypeWithId(id));
					}
				}catch(Exception ex)
				{
					//do nothing the file type is not mandatory
					//Ivy.log().error("Error while getting the file type on "+doc.getFilename()+ " "+ex.getMessage(), ex);
				}
			}
			try{
				doc.setVersionnumber(Integer.parseInt(rec.getField("versionnumber").toString()));
			}catch(Exception ex)
			{
				doc.setVersionnumber(1);
			}
			try{
				doc.setExtension(doc.getFilename().substring(doc.getFilename().lastIndexOf(".")+1));
			}catch(Exception ex){
				//Ignore the Exception here
			}
			al.add(doc);
		}
		al.trimToSize();
		return al;
	}

	/**
	 * 
	 * @param rst
	 * @param includeJavaFile
	 * @param folder
	 * @return
	 * @throws Exception
	 */
	private DocumentOnServer makeDocumentFromResultSet(ResultSet rst, boolean includeJavaFile, FolderOnServer folder) throws Exception
	{
		DocumentOnServer doc = new DocumentOnServer();
		if(rst.next())
		{
			doc.setFileID(String.valueOf(rst.getInt("FileId")));
			doc.setFilename(rst.getString("FileName"));
			doc.setPath(rst.getString("FilePath"));
			doc.setFileSize(rst.getString("FileSize"));
			doc.setUserID(rst.getString("CreationUserId"));
			doc.setCreationDate(rst.getString("CreationDate"));
			doc.setCreationTime(rst.getString("CreationTime"));
			doc.setModificationUserID(rst.getString("ModificationUserId"));
			doc.setModificationDate(rst.getString("ModificationDate"));
			doc.setModificationTime(rst.getString("ModificationTime"));
			doc.setLocked(String.valueOf(rst.getInt("Locked")));
			doc.setIsLocked(doc.getLocked().compareTo("1")==0);
			doc.setLockingUserID(rst.getString("LockingUserId"));
			doc.setDescription(rst.getString("Description"));
			if(this.securityActivated && folder!=null)
			{
				doc.setCanUserDelete(folder.getCanUserDeleteFiles());
				doc.setCanUserWrite(folder.getCanUserWriteFiles());
				doc.setCanUserRead(folder.getCanUserOpenDir());
			}else{
				doc.setCanUserDelete(true);
				doc.setCanUserWrite(true);
				doc.setCanUserRead(true);
			}
			try{
				doc.setExtension(doc.getFilename().substring(doc.getFilename().lastIndexOf(".")+1));
			}catch(Exception ex){
				//Ignore the Exception here
			}
			if(this.activateFileType)
			{
				try{
					long id = rst.getLong("filetypeid");
					if(id>0)
					{
						doc.setFileType(this.ftController.getFileTypeWithId(id));
					}
				}catch(Exception ex)
				{
					//do nothing the file type is not mandatory
				}
			}
			try{
				doc.setVersionnumber(rst.getInt("versionnumber"));
			}catch(Exception ex)
			{
				doc.setVersionnumber(1);
			}
			if(includeJavaFile)
			{
				Blob bl = null;
				byte[] byt = null;
				try{
					bl = rst.getBlob("file_content");
				}catch(Throwable t){
					try{
						byt = rst.getBytes("file_content");
					}catch(Throwable t2){

					}
				}

				//we create a temp file on the server 
				String tmpPath="tmp/"+System.nanoTime()+"/"+doc.getFilename();
				File ivyFile = new File(tmpPath,true);
				ivyFile.createNewFile();
				byte[] allBytesInBlob = bl!=null?bl.getBytes(1, (int) bl.length()):byt;

				FileOutputStream fos=null;
				try{
					java.io.File javaFile = ivyFile.getJavaFile();
					fos = new FileOutputStream(javaFile.getPath());
					fos.write(allBytesInBlob);
					doc.setJavaFile(javaFile);
				}finally{
					if(fos!=null){
						fos.close();
					}
				}
			}
		}
		return doc;
	}

	public List<FolderOnServer> getDirsUserCanOpenUnderTree(Tree dirs, List<FolderOnServer> folders)
	{
		if(folders==null)
		{
			folders = List.create(FolderOnServer.class);
		}
		FolderOnServer fos = (FolderOnServer) dirs.getValue();
		if(fos.getCanUserOpenDir())
		{
			folders.add(fos);
			if(dirs.getChildCount()>0)
			{
				List<Tree> d = dirs.getChildren();
				for(Tree t:d)
				{
					getDirsUserCanOpenUnderTree(t,folders);
				}
			}
		}
		return folders;
	}

	/**
	 * 
	 * @param dirs
	 * @param paths
	 * @return
	 */
	public List<String> getDirPathsUserCanOpenInTree(Tree dirs, List<String> paths)
	{
		if(paths==null)
		{
			paths = List.create(String.class);
		}
		FolderOnServer fos = (FolderOnServer) dirs.getValue();
		if(fos.getCanUserOpenDir())
		{
			paths.add(fos.getPath());
			if(dirs.getChildCount()>0)
			{
				List<Tree> d = dirs.getChildren();
				for(Tree t:d)
				{
					getDirPathsUserCanOpenInTree(t,paths);
				}
			}
		}
		return paths;

	}

	/**
	 * Returns all the DocumentOnServer stores in the FileManager table of the Ivy System DB.<br>
	 * The SQL query is built with the given list of 'AND' conditions.<br>
	 * If the list is null or empty, it will retrieve all the DocumentOnServer from the DB.<br>
	 * Each condition should be written like : "FileId > 1000", "FileName NOT LIKE 'test.doc'", "FileName LIKE 'test.doc'"...<br>
	 * @param _conditions: List<String> representing the conditions to perform the search in the DB
	 * @return an ArrayList of {@link DocumentOnServer} Objects.<br>
	 * Each DocumentOnServer object represents a File with several informations (name, path, size, CreationDate, creationUser...)
	 * @throws Exception
	 */
	public ArrayList<DocumentOnServer> getDocuments(List<String> _conditions)throws Exception{
		ArrayList<DocumentOnServer>  al = new ArrayList<DocumentOnServer>();
		//Recordset rset = null;
		List<Record> recordList=null;
		StringBuilder query=new StringBuilder("");

		query.append("SELECT * FROM "+this.tableNameSpace+" WHERE ");
		if(_conditions==null || _conditions.isEmpty())
		{
			return al;
		}
		if(_conditions.size()==1)
		{
			query.append(_conditions.get(0));
		}else{
			int numConditions= _conditions.size()-1;
			for(int i=0; i<numConditions;i++){
				query.append(_conditions.get(i)+" AND ");
			}
			query.append(_conditions.get(numConditions));
		}

		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query.toString());
				//rset = executeStatement(stmt);
				recordList=executeStmt(stmt);
				if(recordList!=null){
					al.addAll(this.makeDocsWithRecordList(recordList, null));
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return al;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getDocumentsInPath(java.lang.String, boolean)
	 */
	@Override
	public ArrayList<DocumentOnServer> getDocumentsInPath(String _path, boolean _isRecursive) throws Exception {
		return FileManagementStaticController.getDocumentsInPath(this.getDatabase(), tableNameSpace, _path, _isRecursive, this.getEscapeChar());
	}

	/**
	 * return all the Locked documents under a given path
	 * @param _path: the path where to look for the Locked files
	 * @param _isRecursive: if is recursive, look in all the sub directories under the path
	 * @return the list of DocumentOnServer as ArrayList<DocumentOnServer> that are Locked
	 * @throws Exception
	 */
	public ArrayList<DocumentOnServer> getDocumentsLocked(String _path, boolean _isrecursive) throws Exception{
		if(_path==null || _path.trim().length()==0)
		{
			throw new Exception("Invalid path in getDocumentsInPath method");
		}
		ArrayList<DocumentOnServer>  al = new ArrayList<DocumentOnServer>();
		String folderPath = escapeBackSlash(FileHandler.formatPathWithEndSeparator(_path, false));
		folderPath=escapeUnderscoreInPath(folderPath);
		//Recordset rset = null;
		List<Record> recordList=null;
		String query="";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			if(_isrecursive)
			{
				query="SELECT * FROM "+this.tableNameSpace+" WHERE Locked=1 AND FilePath LIKE ? ESCAPE '"+escapeChar+"'";
			}else
			{
				//query="SELECT * FROM "+this.tableNamespace+" WHERE Locked=1 AND FilePath LIKE '"+folderPath+"%' AND FilePath NOT LIKE '"+folderPath+"%["+java.io.File.separator+"]%'";
				query="SELECT * FROM "+this.tableNameSpace+" WHERE Locked=1 AND FilePath LIKE ? ESCAPE '"+escapeChar+"' AND FilePath NOT LIKE ? ESCAPE '"+escapeChar+"'";
			}
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				if(_isrecursive)
				{
					stmt.setString(1, folderPath+"%");
				}else
				{
					stmt.setString(1, folderPath+"%");
					stmt.setString(2, folderPath+"%/%");
				}
				//rset=executeStatement(stmt);
				recordList=executeStmt(stmt);
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		if(recordList!=null){
			al.addAll(this.makeDocsWithRecordList(recordList, null));
		}
		return al;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getDocumentsWithJavaFileInPath(java.lang.String, boolean)
	 */
	@Override
	public ArrayList<DocumentOnServer> getDocumentsWithJavaFileInPath(
			String _path, boolean _isRecursive) throws Exception {
		if(_path==null || _path.trim().length()==0)
		{
			throw new Exception("Invalid path in getDocumentsInPath method");
		}


		ArrayList<DocumentOnServer>  al = new ArrayList<DocumentOnServer>();
		if(this.securityActivated && _isRecursive)
		{
			al.addAll(this.getDocumentOnServersInPathWithJavaFileCheckSecurity(_path));
			return al;
		}
		String folderPath = escapeBackSlash(FileHandler.formatPathWithEndSeparator(_path, false));
		folderPath=escapeUnderscoreInPath(folderPath);
		String query="";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			if(_isRecursive)
			{
				//query="SELECT * FROM "+this.tableNameSpace+" WHERE FilePath LIKE ?";
				query="SELECT * FROM "+this.tableNameSpace+
				" INNER JOIN "+this.fileContentTableNameSpace+" ON "+this.tableNameSpace+".FileId = "+this.fileContentTableNameSpace+".file_id" +
				" WHERE "+this.tableNameSpace+".FilePath LIKE ? ESCAPE '"+escapeChar+"'";
			}
			else
			{
				//query="SELECT * FROM "+this.tableNameSpace+" WHERE FilePath LIKE ? AND FilePath NOT LIKE ?";
				query="SELECT * FROM "+this.tableNameSpace+
				" INNER JOIN "+this.fileContentTableNameSpace+" ON "+this.tableNameSpace+".FileId = "+this.fileContentTableNameSpace+".file_id" +
				" WHERE "+this.tableNameSpace+".FilePath LIKE ? ESCAPE '"+escapeChar+"' AND "+this.tableNameSpace+".FilePath NOT LIKE ? ESCAPE '"+escapeChar+"'";
			}
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				if(_isRecursive)
				{
					stmt.setString(1, folderPath+"%");
				}else
				{
					stmt.setString(1, folderPath+"%");
					stmt.setString(2, folderPath+"%/%");
				}
				ResultSet rst = stmt.executeQuery();
				al.addAll(this.makeDocsWithJavaFileFromResultSet(rst, null));
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}

		al.trimToSize();
		return al;
	}

	/**
	 * For internal use only. Utility method to get a list of documents each containing a reference to its java File.
	 * Get the documents recursively from the top given _path and only in the folder the user can open.
	 * @param _path
	 * @return
	 * @throws Exception
	 */
	private List<DocumentOnServer> getDocumentOnServersInPathWithJavaFileCheckSecurity(String _path) throws Exception
	{
		if(_path==null || _path.trim().length()==0)
		{
			throw new Exception("Invalid path in getDocumentsInPath method");
		}
		List<DocumentOnServer>  al = List.create(DocumentOnServer.class);
		String folderPath = escapeBackSlash(FileHandler.formatPathWithEndSeparator(_path, false));	
		Tree dirs = this.getDirectoryTree(folderPath);
		List<FolderOnServer> folders=getDirsUserCanOpenUnderTree(dirs, null);
		if(folders.size()>0)
		{
			IExternalDatabaseRuntimeConnection connection = null;
			try {
				connection = getDatabase().getAndLockConnection();
				Connection jdbcConnection=connection.getDatabaseConnection();
				String query="SELECT * FROM "+this.tableNameSpace+
				" INNER JOIN "+this.fileContentTableNameSpace+" ON "+this.tableNameSpace+".FileId = "+this.fileContentTableNameSpace+".file_id" +
				" WHERE "+this.tableNameSpace+".FilePath LIKE ? ESCAPE '"+escapeChar+"' AND "+this.tableNameSpace+".FilePath NOT LIKE ? ESCAPE '"+escapeChar+"'";

				PreparedStatement stmt = null;
				try{
					stmt = jdbcConnection.prepareStatement(query);
					for(FolderOnServer fos:folders){
						String s1 = escapeBackSlash(FileHandler.formatPathWithEndSeparator(fos.getPath(), false));
						s1=escapeUnderscoreInPath(s1);
						stmt.setString(1, s1+"%");
						stmt.setString(2, s1+"%/%");
						ResultSet rst = stmt.executeQuery();
						al.addAll(this.makeDocsWithJavaFileFromResultSet(rst,fos));
					}

				}finally{
					DatabaseUtil.close(stmt);
				}
			} finally{
				if(connection!=null ){
					database.giveBackAndUnlockConnection(connection);
				}
			}

		}
		return al;
	}

	/**
	 * for internal use only. Utility method to build documentOnServer Objects with the resultSet from query
	 * @param rst: ResulSet
	 * @param folder
	 * @return
	 * @throws Exception
	 */
	private List<DocumentOnServer> makeDocsWithJavaFileFromResultSet(ResultSet rst, FolderOnServer folder) throws Exception
	{
		List<DocumentOnServer>  al = List.create(DocumentOnServer.class);
		if(rst==null)
		{
			return al;
		}
		while(rst.next())
		{
			DocumentOnServer doc = new DocumentOnServer();
			doc.setFileID(String.valueOf(rst.getInt("FileId")));
			doc.setFilename(rst.getString("FileName"));
			doc.setPath(rst.getString("FilePath"));
			doc.setFileSize(rst.getString("FileSize"));
			doc.setUserID(rst.getString("CreationUserId"));
			doc.setCreationDate(rst.getString("CreationDate"));
			doc.setCreationTime(rst.getString("CreationTime"));
			doc.setModificationUserID(rst.getString("ModificationUserId"));
			doc.setModificationDate(rst.getString("ModificationDate"));
			doc.setModificationTime(rst.getString("ModificationTime"));
			doc.setLocked(String.valueOf(rst.getInt("Locked")));
			doc.setIsLocked(doc.getLocked().compareTo("1")==0);
			doc.setLockingUserID(rst.getString("LockingUserId"));
			doc.setDescription(rst.getString("Description"));
			if(this.securityActivated && folder!=null)
			{
				doc.setCanUserDelete(folder.getCanUserDeleteFiles());
				doc.setCanUserRead(folder.getCanUserOpenDir());
				doc.setCanUserWrite(folder.getCanUserWriteFiles());
			}else{
				doc.setCanUserDelete(true);
				doc.setCanUserRead(true);
				doc.setCanUserWrite(true);
			}
			try{
				doc.setExtension(doc.getFilename().substring(doc.getFilename().lastIndexOf(".")+1));
			}catch(Exception ex){
				//Ignore the Exception here
			}
			if(this.activateFileType)
			{
				try{
					long id = rst.getLong("filetypeid");
					if(id>0)
					{
						doc.setFileType(this.ftController.getFileTypeWithId(id));
					}
				}catch(Exception ex)
				{
					//do nothing the file type is not mandatory
				}
			}
			try{
				doc.setVersionnumber(rst.getInt("versionnumber"));
			}catch(Exception ex)
			{
				doc.setVersionnumber(1);
			}
			Blob bl = null;
			byte[] byt = null;
			try{
				bl = rst.getBlob("file_content");
			}catch(Throwable t){
				try{
					byt = rst.getBytes("file_content");
				}catch(Throwable t2){

				}
			}

			//we create a temp file on the server 
			String tmpPath="tmp/"+System.nanoTime()+"/"+doc.getFilename();
			File ivyFile = new File(tmpPath,true);
			ivyFile.createNewFile();
			byte[] allBytesInBlob = bl!=null?bl.getBytes(1, (int) bl.length()):byt;
			FileOutputStream fos=null;
			//DataOutputStream dos =null;
			try{
				java.io.File javaFile = ivyFile.getJavaFile();
				fos = new FileOutputStream(javaFile.getPath());
				//dos = new DataOutputStream(fos);
				//dos.writeBytes(rec.getField("FileContent").toString());
				fos.write(allBytesInBlob);
				doc.setJavaFile(javaFile);

			}finally{
				if(fos!=null){
					fos.close();
				}
			}
			al.add(doc);
		}

		al.trimToSize();
		return al;
	}

	/**
	 * get the Class Object of the current AbstractFileManagementHandler implementation Class
	 * @return the class Object of the current AbstractFileManagementHandler implementation Class
	 */
	public Class<? extends AbstractFileManagementHandler> getFileManagementHandlerClass() {
		return this.getClass();
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getFileStorageType()
	 */
	@Override
	public int getFileStorageType() {
		return AbstractFileManagementHandler.FILE_STORAGE_DATABASE;
	}

	/**
	 * Insert a List of DocumentOnServer Objects into DB<br>
	 * It checks if the documents already exits, if so, it delete them first.
	 * To be able to store the content of the Files in the DB, this method works as following:<br>
	 * - if the given DocumentOnServer object contains a valid Ivy File Object, then its content will be stored in the DB,<br>
	 * - else if the given DocumentOnServer object contains a valid java.io.File Object, then its content will be stored in the DB,<br>
	 * - else it will use the path of the DocumentOnServer object, to retrieve the java.io.File<br>
	 * - if no content can be found, it will ignore the documentOnServer Object and will treat the following one.
	 * @param _documents: the List<DocumentOnServer> to insert in the DB
	 * @return the number of inserted documents
	 * @throws Exception
	 */
	public int insertDocuments(List<DocumentOnServer> _documents)
	throws Exception {
		int insertedIDs = -1;
		if(_documents==null || _documents.size()==0)
			return insertedIDs;

		
		String base="INSERT INTO "+this.tableNameSpace+
		" (FileName, FilePath, CreationUserId, CreationDate, CreationTime, FileSize, Locked, LockingUserId, ModificationUserId, ModificationDate, ModificationTime, Description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		String query ="INSERT INTO "+this.fileContentTableNameSpace+" (file_id, file_content) VALUES (?,?)";

		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");

		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			// delete the documents that are already in the DB
			this.deleteDocumentsInDBOnly(_documents,jdbcConnection);
			PreparedStatement stmt = null;
			PreparedStatement stmt2 = null;
			try{
				boolean flag = true;
				try{
					stmt = jdbcConnection.prepareStatement(base, PreparedStatement.RETURN_GENERATED_KEYS);
				}catch(SQLFeatureNotSupportedException fex)
				{//The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS
					stmt = jdbcConnection.prepareStatement(base);
					flag=false;
				}
				stmt2 = jdbcConnection.prepareStatement(query);
				for(DocumentOnServer doc: _documents){

					java.io.File f = null;
					if(doc.getIvyFile() !=null && doc.getIvyFile().exists())
					{
						f=doc.getIvyFile().getJavaFile();
					}else if(doc.getJavaFile() !=null && doc.getJavaFile().exists())
					{
						f = doc.getJavaFile();
					}else if(doc.getPath()!=null && !doc.getPath().trim().equals(""))
					{
						f=new java.io.File(doc.getPath().trim());
					}
					if(f!=null && f.exists())
					{
						//Insert first the file in UPLOADEDFILES
						stmt.setString(1, doc.getFilename());
						stmt.setString(2, escapeBackSlash(doc.getPath()));
						stmt.setString(3, doc.getUserID());
						stmt.setString(4, date);
						stmt.setString(5, time);
						stmt.setString(6, doc.getFileSize());
						stmt.setInt(7, 0);
						stmt.setString(8, "");
						stmt.setString(9, doc.getModificationUserID());
						stmt.setString(10, date);
						stmt.setString(11, time);
						String s = doc.getDescription();
						if(s==null)
						{
							s="";
						}
						stmt.setString(12, s);
						stmt.executeUpdate();
						ResultSet rs = null;
						int id=-1;
						try{
							rs = stmt.getGeneratedKeys();
							if ( rs!=null && rs.next() ) {
								// Retrieve the auto generated key(s).
								id = rs.getInt(1);
							}
						}catch(Exception ex)
						{
							//ignore the exception: happens if system is ORACLE and so on....
						}
						if(!flag || id<=0)
						{// In case of Oracle, the Id could not be retrieved automatically
							try{
								id=this.getDocIdWithPath(escapeBackSlash(doc.getPath()),jdbcConnection);
							}catch(Exception ex){
								Ivy.log().error(ex.getMessage(),ex);
							}
						}
						if(id>0)
						{//INSERT THE FILE CONTENT IN THE CONTENT TABLE
							FileInputStream is=null;
							try{
								is = new FileInputStream ( f );  
								stmt2.setInt(1, id);
								stmt2.setBinaryStream (2, is, (int) f.length()); 
								stmt2.executeUpdate();
							}finally{
								if(is!=null){
									is.close();
								}
							}
							if(this.activateFileType && doc.getFileType() !=null && doc.getFileType().getId()!=null && doc.getFileType().getId()>0)
							{
								ftController.setDocumentFileType(doc, doc.getFileType().getId(),jdbcConnection);
							}
							if(super.getFileActionHistoryController()!=null && super.getFileActionHistoryController().getConfig().isFileCreationTracked())
							{
								super.getFileActionHistoryController().createNewActionHistory(id, (short) 1, doc.getUserID(), "",jdbcConnection);
							}
						}

						insertedIDs++;
					}
				}
			}finally{
				DatabaseUtil.close(stmt2);
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return insertedIDs;
	}


	

	/*
	 * This method is now concurrent processes compatible.
	 * See Issue #28265, database deadlock in some of the FileManagement methods used concurrently in a lot of different processes.<br>
	 * (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#insertFiles(ch.ivyteam.ivy.scripting.objects.List, java.lang.String, java.lang.String)
	 */
	@Deprecated
	@Override
	public int insertFile(java.io.File _file, String _destinationPath)throws Exception {
		int insertedId = -1;
		if(_file== null || _destinationPath == null) return insertedId;

		if(!_file.exists())
		{
			throw new FileNotFoundException("The file that should be insterted does not exist.");
		}

		String base = "INSERT INTO "+this.tableNameSpace+
		" (FileName, FilePath, CreationUserId, CreationDate, CreationTime, FileSize, Locked, LockingUserId, ModificationUserId, ModificationDate, ModificationTime, Description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		String query ="INSERT INTO "+this.fileContentTableNameSpace+" (file_id, file_content) VALUES (?,?)";

		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");
		String user= Ivy.session().getSessionUserName();
		_destinationPath=formatPathForDirectory(_destinationPath);
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			this.deleteFile(_destinationPath+_file.getName(), jdbcConnection);
			PreparedStatement stmt = null;
			FileInputStream is=null;
			try{
				boolean flag = true;
				try{
					stmt = jdbcConnection.prepareStatement(base, PreparedStatement.RETURN_GENERATED_KEYS);
				}catch(SQLFeatureNotSupportedException fex)
				{//The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS
					stmt = jdbcConnection.prepareStatement(base);
					flag=false;
				}
				stmt.setString(1, _file.getName());
				stmt.setString(2, _destinationPath+_file.getName());
				stmt.setString(3, user);
				stmt.setString(4, date);
				stmt.setString(5, time);
				stmt.setString(6, FileHandler.getFileSize(_file));
				stmt.setInt(7, 0);
				stmt.setString(8, "");
				stmt.setString(9, user);
				stmt.setString(10, date);
				stmt.setString(11, time);
				stmt.setString(12, "");
				stmt.executeUpdate();

				ResultSet rs = null;
				try{
					rs = stmt.getGeneratedKeys();
					if ( rs!=null && rs.next() ) {
						// Retrieve the auto generated key(s).
						insertedId = rs.getInt(1);
					}
				}catch(Exception ex)
				{
					//ignore the exception: happens if system is ORACLE and so on....
				}
				if(!flag || insertedId<=0)
				{// In case of Oracle, the Id could not be retrieved automatically
					try{
						insertedId=this.getDocIdWithPath(escapeBackSlash(_destinationPath+_file.getName()),jdbcConnection);
					}catch(Exception ex){
						Ivy.log().error(ex.getMessage(),ex);
					}
				}
				stmt.close();
				if(insertedId>0)
				{//INSERT THE FILE CONTENT IN THE CONTENT TABLE
					stmt = jdbcConnection.prepareStatement(query);
					is = new FileInputStream ( _file );   
					stmt.setInt(1, insertedId);
					stmt.setBinaryStream (2, is, (int) _file.length() ); 
					stmt.executeUpdate();
					if(super.getFileActionHistoryController()!=null && super.getFileActionHistoryController().getConfig().isFileCreationTracked())
					{
						super.getFileActionHistoryController().createNewActionHistory(insertedId, (short) 1, user, "",jdbcConnection);
					}
				}
			}finally{
				if(is!=null)
				{
					is.close();
				}
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return insertedId;
	}


	/*
	 * This method is now concurrent processes compatible.
	 * See Issue #28265, database deadlock in some of the FileManagement methods used concurrently in a lot of different processes.<br>
	 * (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#insertFiles(ch.ivyteam.ivy.scripting.objects.List, java.lang.String, java.lang.String)
	 */
	@Override
	public int insertFile(java.io.File _file, String _destinationPath, String _user)throws Exception {
		int insertedId = -1;
		if(_file== null || _destinationPath == null) return insertedId;

		if(!_file.exists())
		{
			throw new FileNotFoundException("The file that should be inserted does not exist.");
		}

		String base = "INSERT INTO "+this.tableNameSpace+
		" (FileName, FilePath, CreationUserId, CreationDate, CreationTime, FileSize, Locked, LockingUserId, ModificationUserId, ModificationDate, ModificationTime, Description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		String query ="INSERT INTO "+this.fileContentTableNameSpace+" (file_id, file_content) VALUES (?,?)";
		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");
		if(_user==null || _user.trim().equals("")){
			_user= Ivy.session().getSessionUserName();
		}
		_destinationPath=formatPathForDirectory(_destinationPath);
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			this.deleteFile(_destinationPath+_file.getName(), jdbcConnection);
			PreparedStatement stmt = null;
			FileInputStream is=null;
			try{
				boolean flag = true;
				//Insert first the file in UPLOADEDFILES
				try{
					stmt = jdbcConnection.prepareStatement(base, PreparedStatement.RETURN_GENERATED_KEYS);
				}catch(SQLFeatureNotSupportedException fex)
				{//The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS
					stmt = jdbcConnection.prepareStatement(base);
					flag=false;
				}
				stmt.setString(1, _file.getName());
				stmt.setString(2, _destinationPath+_file.getName());
				stmt.setString(3, _user);
				stmt.setString(4, date);
				stmt.setString(5, time);
				stmt.setString(6, FileHandler.getFileSize(_file));
				stmt.setInt(7, 0);
				stmt.setString(8, "");
				stmt.setString(9, _user);
				stmt.setString(10, date);
				stmt.setString(11, time);
				stmt.setString(12, "");
				stmt.executeUpdate();
				ResultSet rs = null;
				try{
					rs = stmt.getGeneratedKeys();
					if ( rs!=null && rs.next() ) {
						// Retrieve the auto generated key(s).
						insertedId = rs.getInt(1);
					}
				}catch(Exception ex)
				{
					//ignore the exception: happens if system is ORACLE and so on....
				}

				if(!flag || insertedId<=0)
				{// In case of Oracle, the Id could not be retrieved automatically
					try{
						insertedId=this.getDocIdWithPath(escapeBackSlash(_destinationPath+_file.getName()),jdbcConnection);
					}catch(Exception ex){
						Ivy.log().error(ex.getMessage(),ex);
					}
				}
				stmt.close();
				if(insertedId>0)
				{//INSERT THE FILE CONTENT IN THE CONTENT TABLE
					stmt = jdbcConnection.prepareStatement(query);
					is = new FileInputStream ( _file );   
					stmt.setInt(1, insertedId);
					stmt.setBinaryStream (2, is, (int) _file.length() ); 
					stmt.executeUpdate();
					if(this.getFileActionHistoryController()!=null && this.getFileActionHistoryController().getConfig().isFileCreationTracked())
					{
						this.getFileActionHistoryController().createNewActionHistory(insertedId, (short) 1, _user, "",jdbcConnection);
					}
				}

			}finally{
				if(is!=null)
				{
					is.close();
				}
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return insertedId;
	}

	/*
	 * This method is now concurrent processes compatible.
	 * See Issue #28265, database deadlock in some of the FileManagement methods used concurrently in a lot of different processes.<br>
	 * (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#insertFiles(ch.ivyteam.ivy.scripting.objects.List, java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	public int insertFiles(List<java.io.File> _files, String _user) throws Exception{
		int insertedIDs = -1;
		if(_files==null || _files.size()==0)
			return insertedIDs;

		String base="INSERT INTO "+this.tableNameSpace+
		" (FileName, FilePath, CreationUserId, CreationDate, CreationTime, FileSize, Locked, LockingUserId, ModificationUserId, ModificationDate, ModificationTime, Description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		String query ="INSERT INTO "+this.fileContentTableNameSpace+" (file_id, file_content) VALUES (?,?)";
		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");
		if(_user==null || _user.trim().equals("")){
			_user= Ivy.session().getSessionUserName();
		}

		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			// delete the documents that are already in the DB
			this.deleteFilesInDBOnly(_files,"",jdbcConnection);
			PreparedStatement stmt = null;
			PreparedStatement stmt2 = null;
			try{
				boolean flag = true;
				try{
					stmt = jdbcConnection.prepareStatement(base, PreparedStatement.RETURN_GENERATED_KEYS);
				}catch(SQLFeatureNotSupportedException fex)
				{//The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS
					stmt = jdbcConnection.prepareStatement(base);
					flag=false;
				}
				stmt2 = jdbcConnection.prepareStatement(query);
				for(java.io.File file: _files){
					if(file.exists())
					{
						stmt.setString(1, file.getName());
						stmt.setString(2, file.getPath());
						stmt.setString(3, _user);
						stmt.setString(4, date);
						stmt.setString(5, time);
						stmt.setString(6, FileHandler.getFileSize(file));
						stmt.setInt(7, 0);
						stmt.setString(8, "");
						stmt.setString(9, _user);
						stmt.setString(10, date);
						stmt.setString(11, time);
						stmt.setString(12, "");
						stmt.executeUpdate();
						int id=-1;
						ResultSet rs = null;
						try{
							rs = stmt.getGeneratedKeys();
							if ( rs!=null && rs.next() ) {
								// Retrieve the auto generated key(s).
								id = rs.getInt(1);
							}
						}catch(Exception ex)
						{
							//ignore the exception: happens if system is ORACLE and so on....
						}
						if(!flag || id<=0)
						{
							try{
								id=this.getDocIdWithPath(escapeBackSlash(file.getPath()),jdbcConnection);
							}catch(Exception ex){
								Ivy.log().error(ex.getMessage(),ex);
							}
						}
						if(id>0)
						{//INSERT THE FILE CONTENT IN THE CONTENT TABLE
							
							FileInputStream is=null;
							try{
								is = new FileInputStream ( file );  
								stmt2.setInt(1, id);
								stmt2.setBinaryStream (2, is, (int) file.length() ); 
								stmt2.executeUpdate();
								if(super.getFileActionHistoryController()!=null && super.getFileActionHistoryController().getConfig().isFileCreationTracked())
								{
									super.getFileActionHistoryController().createNewActionHistory(id, (short) 1, _user, "", jdbcConnection);
								}
							}finally{
								if(is!=null){
									is.close();
								}
							}
						}
						insertedIDs++;
					}
				}
			}finally{
				DatabaseUtil.close(stmt);
				DatabaseUtil.close(stmt2);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}

		return insertedIDs;
	}
	
	/*
	 * This method is now concurrent processes compatible.
	 * See Issue #28265, database deadlock in some of the FileManagement methods used concurrently in a lot of different processes.<br>
	 * (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#insertFiles(ch.ivyteam.ivy.scripting.objects.List, java.lang.String, java.lang.String)
	 */
	@Override
	public int insertFiles(List<java.io.File> _files, String _destinationPath, String _user) throws Exception{
		int insertedIDs = -1;
		if(_files==null || _files.size()==0)
			return insertedIDs;

		
		String base="INSERT INTO "+this.tableNameSpace+
		" (FileName, FilePath, CreationUserId, CreationDate, CreationTime, FileSize, Locked, LockingUserId, ModificationUserId, ModificationDate, ModificationTime, Description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		String query ="INSERT INTO "+this.fileContentTableNameSpace+" (file_id, file_content) VALUES (?,?)";
		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");
		if( _user==null ||  _user.trim().equals("")){
			_user= Ivy.session().getSessionUserName();
		}
		if(_destinationPath==null || _destinationPath.trim().length()==0)
		{
			_destinationPath=FileHandler.getFileDirectoryPath(_files.get(0));
		}
		_destinationPath=formatPathForDirectory(_destinationPath);
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			// delete the documents that are already in the DB
			this.deleteFilesInDBOnly(_files,_destinationPath,jdbcConnection);
			boolean historyDelete=super.getFileActionHistoryController()!=null && super.getFileActionHistoryController().getConfig().isFileCreationTracked();
			Ivy.log().info(historyDelete);
			PreparedStatement stmt = null;
			PreparedStatement stmt2 = null;
			try{
				boolean flag = true;
				try{
					stmt = jdbcConnection.prepareStatement(base, PreparedStatement.RETURN_GENERATED_KEYS);
				}catch(SQLFeatureNotSupportedException fex)
				{//The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS
					stmt = jdbcConnection.prepareStatement(base);
					flag=false;
				}
				stmt2 = jdbcConnection.prepareStatement(query);
				for(java.io.File file: _files){
					if(file.exists())
					{
						stmt.setString(1, file.getName());
						stmt.setString(2, _destinationPath+file.getName());
						stmt.setString(3, _user);
						stmt.setString(4, date);
						stmt.setString(5, time);
						stmt.setString(6, FileHandler.getFileSize(file));
						stmt.setInt(7, 0);
						stmt.setString(8, "");
						stmt.setString(9, _user);
						stmt.setString(10, date);
						stmt.setString(11, time);
						stmt.setString(12, "");
						stmt.executeUpdate();
						int id=-1;
						ResultSet rs = null;
						try{
							rs = stmt.getGeneratedKeys();
							if ( rs!=null && rs.next() ) {
								// Retrieve the auto generated key(s).
								id = rs.getInt(1);
							}
						}catch(Exception ex)
						{
							//ignore the exception: happens if system is ORACLE and so on....
						}
						
						if(!flag || id<=0)
						{
							try{
								id=this.getDocIdWithPath(escapeBackSlash(file.getPath()),jdbcConnection);
							}catch(Exception ex){
								Ivy.log().error(ex.getMessage(),ex);
							}
						}
						if(id>0)
						{//INSERT THE FILE CONTENT IN THE CONTENT TABLE
							FileInputStream is=null;
							try{
								is = new FileInputStream ( file );  
								stmt2.setInt(1, id);
								stmt2.setBinaryStream (2, is, (int) file.length() ); 
								stmt2.executeUpdate();
								if(historyDelete)
								{
									super.getFileActionHistoryController().createNewActionHistory(id, (short) 1, _user, "", jdbcConnection);
								}
							}finally{
								if(is!=null){
									is.close();
								}
							}
						}
						insertedIDs++;
					}
				}
			}finally{
				DatabaseUtil.close(stmt);
				DatabaseUtil.close(stmt2);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return insertedIDs;
	}

	/**
	 * Insert a  DocumentOnServer Object into the DB storing System.<br>
	 * To be able to store the content of the File in the DB, this method works as following:<br>
	 * - if the given DocumentOnServer object contains a valid Ivy File Object, then its content will be stored in the DB,<br>
	 * - else if the given DocumentOnServer object contains a valid java.io.File Object, then its content will be stored in the DB,<br>
	 * - else it will use the path of the DocumentOnServer object, to retrieve the java.io.File<br>
	 * - if no content can be found, it will throw an Exception.
	 * @param _document: DocumentOnServer that has to be inserted into the File storing system
	 * @return 1 if successful
	 * @throws Exception 
	 */
	public int insertOneDocument(DocumentOnServer _document)throws Exception {
		int insertedId = -1;
		if(_document== null) return insertedId;

		java.io.File f = null;
		if(_document.getIvyFile() !=null && _document.getIvyFile().exists())
		{
			f=_document.getIvyFile().getJavaFile();
		}else if(_document.getJavaFile() !=null && _document.getJavaFile().exists())
		{
			f = _document.getJavaFile();
			//Ivy.log().info("Insert One Document: java file exists "+f.length());
		}else if(_document.getPath()!=null && !_document.getPath().trim().equals(""))
		{
			f=new java.io.File(_document.getPath().trim());
		}
		if(f==null || !f.exists()){
			throw new FileNotFoundException("No File could be found to be able to store its content in the Database.");
		}

		String base="";
		base = "INSERT INTO "+this.tableNameSpace+
		" (FileName, FilePath, CreationUserId, CreationDate, CreationTime, FileSize, Locked, LockingUserId, ModificationUserId, ModificationDate, ModificationTime, Description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		String query ="INSERT INTO "+this.fileContentTableNameSpace+" (file_id, file_content) VALUES (?,?)";
		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");

		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			//deletes the document if exists
			List<DocumentOnServer> l =List.create(DocumentOnServer.class);
			l.add(_document);
			this.deleteDocumentsInDBOnly(l, jdbcConnection);
			
			PreparedStatement stmt = null;
			try{
				boolean flag = true;
				//Insert first the file in UPLOADEDFILES
				try{
					stmt = jdbcConnection.prepareStatement(base, PreparedStatement.RETURN_GENERATED_KEYS);
				}catch(SQLFeatureNotSupportedException fex)
				{//The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS
					stmt = jdbcConnection.prepareStatement(base);
					flag=false;
				}
				stmt.setString(1, _document.getFilename());
				stmt.setString(2, escapeBackSlash(_document.getPath()));
				stmt.setString(3, _document.getUserID());
				stmt.setString(4, date);
				stmt.setString(5, time);
				stmt.setString(6, _document.getFileSize());
				stmt.setInt(7, 0);
				stmt.setString(8, "");
				stmt.setString(9, _document.getUserID());
				stmt.setString(10, date);
				stmt.setString(11, time);
				String s = _document.getDescription();
				if(s==null)
				{
					s="";
				}
				stmt.setString(12, s);

				stmt.executeUpdate();
				ResultSet rs = null;
				try{
					rs = stmt.getGeneratedKeys();
					if ( rs!=null && rs.next() ) {
						// Retrieve the auto generated key(s).
						insertedId = rs.getInt(1);
					}
				}catch(Exception ex)
				{
					//ignore the exception: happens if system is ORACLE and so on....
				}

				if(!flag || insertedId<=0)
				{
					try{
						insertedId=this.getDocIdWithPath(escapeBackSlash(_document.getPath()),jdbcConnection);
					}catch(Exception ex){
						Ivy.log().error(ex.getMessage(),ex);
					}
				}
				if(insertedId>0)
				{//INSERT THE FILE CONTENT IN THE CONTENT TABLE
					//Ivy.log().info("Inserted Id in file table : "+ insertedId);
					stmt = jdbcConnection.prepareStatement(query);
					FileInputStream is=null;
					try{
						is = new FileInputStream ( f );   
						stmt.setInt(1, insertedId);
						stmt.setBinaryStream (2, is, (int) f.length() ); 
						stmt.executeUpdate();

					}finally{
						if(is!=null){
							is.close();
						}
					}
					_document.setFileID(String.valueOf(insertedId));
					if(this.activateFileType && _document.getFileType() !=null && _document.getFileType().getId() != null && _document.getFileType().getId()>0)
					{
						ftController.setDocumentFileType(_document, _document.getFileType().getId(),jdbcConnection);
					}
					if(super.getFileActionHistoryController()!=null && super.getFileActionHistoryController().getConfig().isFileCreationTracked())
					{
						super.getFileActionHistoryController().createNewActionHistory(insertedId, (short) 1, _document.getUserID(), "",jdbcConnection);
					}
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return insertedId;
	}

	/**
	 * Private use only.
	 * Insert a  DocumentOnServer Object into the DB storing System.<br>
	 * To be able to store the content of the File in the DB, this method works as following:<br>
	 * - if the given DocumentOnServer object contains a valid Ivy File Object, then its content will be stored in the DB,<br>
	 * - else if the given DocumentOnServer object contains a valid java.io.File Object, then its content will be stored in the DB,<br>
	 * - else it will use the path of the DocumentOnServer object, to retrieve the java.io.File<br>
	 * - if no content can be found, it will throw an Exception.
	 * @param _document: DocumentOnServer that has to be inserted into the File storing system
	 * @return 1 if successful
	 * @throws Exception 
	 */
	private int insertOneDocumentWithoutHistory(DocumentOnServer _document)throws Exception {
		int insertedId = -1;
		if(_document== null) return insertedId;

		java.io.File f = null;
		if(_document.getIvyFile() !=null && _document.getIvyFile().exists())
		{
			f=_document.getIvyFile().getJavaFile();
		}else if(_document.getJavaFile() !=null && _document.getJavaFile().exists())
		{
			f = _document.getJavaFile();
			//Ivy.log().info("Insert One Document: java file exists "+f.length());
		}else if(_document.getPath()!=null && !_document.getPath().trim().equals(""))
		{
			f=new java.io.File(_document.getPath().trim());
		}
		if(f==null || !f.exists()){
			throw new FileNotFoundException("No File could be found to be able to store its content in the Database.");
		}

		String base="";
		base = "INSERT INTO "+this.tableNameSpace+
		" (FileName, FilePath, CreationUserId, CreationDate, CreationTime, FileSize, Locked, LockingUserId, ModificationUserId, ModificationDate, ModificationTime, Description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		String query ="INSERT INTO "+this.fileContentTableNameSpace+" (file_id, file_content) VALUES (?,?)";
		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");

		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				boolean flag = true;
				//Insert first the file in UPLOADEDFILES
				try{
					stmt = jdbcConnection.prepareStatement(base, PreparedStatement.RETURN_GENERATED_KEYS);
				}catch(SQLFeatureNotSupportedException fex)
				{//The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS
					stmt = jdbcConnection.prepareStatement(base);
					flag=false;
				}
				stmt.setString(1, _document.getFilename());
				stmt.setString(2, escapeBackSlash(_document.getPath()));
				stmt.setString(3, _document.getUserID());
				stmt.setString(4, date);
				stmt.setString(5, time);
				stmt.setString(6, _document.getFileSize());
				stmt.setInt(7, 0);
				stmt.setString(8, "");
				stmt.setString(9, _document.getUserID());
				stmt.setString(10, date);
				stmt.setString(11, time);
				String s = _document.getDescription();
				if(s==null)
				{
					s="";
				}
				stmt.setString(12, s);

				stmt.executeUpdate();
				ResultSet rs = null;
				try{
					rs = stmt.getGeneratedKeys();
					if ( rs!=null && rs.next() ) {
						// Retrieve the auto generated key(s).
						insertedId = rs.getInt(1);
					}
				}catch(Exception ex)
				{
					//ignore the exception: happens if system is ORACLE and so on....
				}

				if(!flag || insertedId<=0)
				{
					try{
						insertedId=this.getDocIdWithPath(escapeBackSlash(_document.getPath()));
					}catch(Exception ex){
						Ivy.log().error(ex.getMessage(),ex);
					}
				}
				if(insertedId>0)
				{//INSERT THE FILE CONTENT IN THE CONTENT TABLE
					//Ivy.log().info("Inserted Id in file table : "+ insertedId);
					stmt = jdbcConnection.prepareStatement(query);
					FileInputStream is=null;
					try{
						is = new FileInputStream ( f );   
						stmt.setInt(1, insertedId);
						stmt.setBinaryStream (2, is, (int) f.length() ); 
						stmt.executeUpdate();

					}finally{
						if(is!=null){
							is.close();
						}
					}
					_document.setFileID(String.valueOf(insertedId));
					if(this.activateFileType && _document.getFileType() !=null && _document.getFileType().getId() != null && _document.getFileType().getId()>0)
					{
						ftController.setDocumentFileType(_document, _document.getFileType().getId(),jdbcConnection);
					}
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return insertedId;
	}

	/**
	 * Looks if a DocumentOnServer is actually Locked by a user<br>
	 * If you don't have a locking System for File edition, just override this method with no action in it and return always false.
	 * @param _doc: the DocumentOnServer to check
	 * @return true if the DocumentOnServer is Locked, else false
	 * @throws Exception
	 */
	public boolean isDocumentOnServerLocked(DocumentOnServer _doc)throws Exception{
		boolean retour = false;
		String sql ="SELECT * FROM "+this.tableNameSpace+" WHERE Locked=1 AND FilePath = ?";
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(sql);
				stmt.setString(1,escapeBackSlash(_doc.getPath()));
				ResultSet rst = stmt.executeQuery();
				if(rst.next())
				{
					retour=true;
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return retour;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#isDocumentOnServerLocked(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer, java.lang.String)
	 */
	@Override
	public boolean isDocumentOnServerLocked(DocumentOnServer _doc, String _user)
	throws Exception {
		boolean retour = false;
		String sql ="SELECT * FROM "+this.tableNameSpace+" WHERE Locked=1 AND FilePath = ? AND LockingUserId <> ?";
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(sql);
				stmt.setString(1,escapeBackSlash(_doc.getPath()));
				stmt.setString(2,_user.trim());
				ResultSet rst = stmt.executeQuery();
				if(rst.next())
				{
					retour=true;
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return retour;
	}

	/**
	 * get the Ivy DB connection name
	 * @return the Ivy DB connection name as String
	 */
	public String getIvyDBConnectionName() {
		return ivyDBConnectionName;
	}

	/**
	 * set the Ivy DB connection name
	 * @param _ivyDBConnectionName as String
	 */
	public void setIvyDBConnectionName(String _ivyDBConnectionName) {
		if(_ivyDBConnectionName==null || _ivyDBConnectionName.trim().length()==0)
		{
			return;
		}
		this.ivyDBConnectionName = _ivyDBConnectionName.trim();
		//reset the database, it will be reinitialized in the next query execution.
		this.database=null;
	}

	/**
	 * get the Database Table name used to store the properties of the files
	 * @return the Database table name
	 */
	public String getTableName() {
		return this.tableName;
	}

	/**
	 * set the Database table name used to store the properties of the files
	 * @param _tableName as String
	 */
	public void setTableName(String _tableName) {
		if(_tableName== null || _tableName.trim().length()==0)
		{
			return;
		}
		this.tableName = _tableName.trim();
		if(this.schemaName!=null)
		{
			this.tableNameSpace=this.schemaName+"."+this.tableName;
		}
		else
		{
			this.tableNameSpace=this.tableName;
		}
	}

	/**
	 * returns the tableNameSpace in the form of schema.table if the schemaName is setted, else just tableName.
	 * @return
	 */
	public String getTableNameSpace() {
		return tableNameSpace;
	}

	/**
	 * Look if a File is actually Locked by a user<br>
	 * If you don't have a locking System for File edition, just override this method with no action in it and return always false.
	 * @param _file: the java.io.File to check
	 * @return true if the file is Locked, else false
	 * @throws Exception
	 */
	public boolean isFileLocked(java.io.File _file)throws Exception{
		boolean retour = false;
		String sql ="SELECT * FROM "+this.tableNameSpace+" WHERE Locked=1 AND FilePath = ?";
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(sql);
				stmt.setString(1,escapeBackSlash(_file.getPath()));
				ResultSet rst = stmt.executeQuery();
				if(rst.next())
				{
					retour=true;
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return retour;
	}

	/**
	 * Look if a File is actually Locked by a user that is not the given user<br>
	 * If you don't have a locking System for File edition, just override this method with no action in it and return always false. 
	 * @param _file the java.io.File to check
	 * @param _user the user who has not to lock the file
	 * @return true if the file is Locked by another user as the given one.
	 * @throws Exception
	 */
	public boolean isFileLockedByAnotherUser(java.io.File _file, String _user)throws Exception{
		if(_file==null || _user ==null || _user.trim().equals(""))
		{
			return false;
		}
		boolean retour = false;
		String sql="SELECT * FROM "+this.tableNameSpace+" WHERE Locked=1 AND FilePath = ? AND LockingUserId <> ?";

		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(sql);
				stmt.setString(1,escapeBackSlash(_file.getPath()));
				stmt.setString(2,_user.trim());
				ResultSet rst = stmt.executeQuery();
				if(rst.next())
				{
					retour=true;
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return retour;
	}

	/**
	 * Lock a document in the DB if not already Locked by another user
	 * @param _doc: the DocumentOnServer Object that has to be Locked
	 * @param _userIn the user who locks this document
	 * @return true if the document was Locked, else false
	 * @throws Exception
	 */
	public boolean lockDocument(DocumentOnServer _doc, String _userIn) throws Exception{
		if(_doc == null || _userIn == null || _userIn.trim().length()==0)
		{
			throw new Exception("Invalid DocumentOnServer Object or invalid username in lockDocument method.");
		}
		boolean flag = false;	
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			String query = "UPDATE "+this.tableNameSpace+" SET Locked=1, LockingUserId= ? WHERE FilePath = ? AND (LockingUserId = ? OR Locked <> 1)";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, _userIn.trim());
				stmt.setString(2, escapeBackSlash(_doc.getPath()));
				stmt.setString(3, _userIn.trim());
				if(stmt.executeUpdate()>0){
					flag = true;
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return flag;
	}

	/**
	 * Lock a File in the DB if not already Locked by another user.<br>
	 * The File here just holds the informations stored in the DB (path, etc...). The File does not exit on the FileSystem.
	 * @param _file: the java.io.File to lock
	 * @param _userIn the user who locks this document
	 * @return true if the document was Locked, else false
	 * @throws Exception
	 */
	public boolean lockFile(java.io.File _file, String _userIn) throws Exception{
		if(_file == null || _userIn == null || _userIn.trim().length()==0)
		{
			throw new Exception("Invalid DocumentOnServer Object or invalid username in lockDocument method.");
		}
		boolean flag = false;	
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			String query = "UPDATE "+this.tableNameSpace+" SET Locked=1, LockingUserId= ? WHERE FilePath = ? AND (LockingUserId = ? OR Locked <> 1)";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, _userIn.trim());
				stmt.setString(2, escapeBackSlash(_file.getPath()));
				stmt.setString(3, _userIn.trim());
				if(stmt.executeUpdate()>0){
					flag = true;
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#pasteCopiedDocumentOnServers(ch.ivyteam.ivy.scripting.objects.List, java.lang.String)
	 */
	@Override
	public ReturnedMessage pasteCopiedDocumentOnServers(
			List<DocumentOnServer> documents, String fileDestinationPath)
	throws Exception {
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		message.setDocumentOnServers(List.create(DocumentOnServer.class));
		if(documents==null || fileDestinationPath==null || fileDestinationPath.trim().equals("")){
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText("Unable to paste the given DocumentOnServer objects. One of the parameter is invalid in pasteCopiedDocumentOnServers(List<DocumentOnServer> documents, String fileDestinationPath) in class "+this.getClass().getName());
			return message;
		}
		List<DocumentOnServer> pasteDocs = List.create(DocumentOnServer.class);
		String dest = formatPathForDirectory(fileDestinationPath);
		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");
		String user =Ivy.session().getSessionUserName();
		try{
			user = Ivy.session().getSessionUserName();
		}catch(Exception ex)
		{
			//do nothing
		}
		for(DocumentOnServer doc: documents){
			//Ivy.log().info("Try to copy doc: "+doc.getFilename());
			int i = getNextCopiedFileNumber(doc.getFilename(),dest);
			//Ivy.log().info("Next Copy: "+i);
			if(i<0)
			{
				continue;
			}
			DocumentOnServer docJ = new DocumentOnServer();
			docJ.setJavaFile(this.getDocumentOnServerWithJavaFile(doc).getJavaFile());
			//Ivy.log().info("Doc with Java File retrieved ");
			if(docJ.getJavaFile()==null || !docJ.getJavaFile().isFile())
			{
				continue;
			}
			String newFile=dest;
			String fname="";
			String ext= FileHandler.getFileExtension(doc.getFilename());
			if(i==0)
			{
				newFile+=doc.getFilename();
				fname=doc.getFilename();
			}else
			{
				fname=FileHandler.getFileNameWithoutExt(doc.getFilename())+"_Copy"+i+"."+ext;
				newFile+=fname;

			}
			docJ.setDescription("");
			docJ.setCreationDate(date);
			docJ.setCreationTime(time);
			docJ.setFilename(fname);
			docJ.setFileSize(doc.getFileSize());
			docJ.setLocked("0");
			docJ.setLockingUserID("");
			docJ.setModificationDate(date);
			docJ.setModificationTime(time);
			docJ.setModificationUserID("");
			docJ.setPath(newFile);
			docJ.setJavaFile(docJ.getJavaFile());
			docJ.setFileType(doc.getFileType());
			if(this.securityActivated)
			{
				FolderOnServer fos = this.securityController.getDirectoryWithPath(fileDestinationPath);
				docJ.setCanUserDelete(fos.getCanUserDeleteFiles());
				docJ.setCanUserRead(fos.getCanUserOpenDir());
				docJ.setCanUserWrite(fos.getCanUserWriteFiles());
			}else{
				docJ.setCanUserDelete(true);
				docJ.setCanUserRead(true);
				docJ.setCanUserWrite(true);
			}
			try{
				docJ.setExtension(docJ.getFilename().substring(docJ.getFilename().lastIndexOf(".")+1));
			}catch(Exception ex){
				//Ignore the Exception here
			}
			docJ.setUserID(user);
			int j = this.insertOneDocumentWithoutHistory(docJ);
			if(j>0){
				pasteDocs.add(docJ);
				if(super.getFileActionHistoryController()!=null && super.getFileActionHistoryController().getConfig().isCopyFileTracked())
				{
					super.getFileActionHistoryController().createNewActionHistory(j, (short) 1, Ivy.session().getSessionUserName(), "Copy of / Kopie von / copie de: "+doc.getPath());
					super.getFileActionHistoryController().createNewActionHistory(Long.parseLong(doc.getFileID()), (short) 9, Ivy.session().getSessionUserName(), docJ.getPath());
				}
				//Ivy.cms().findContentObjectValue("", Locale.ENGLISH).getContentAsString();
			}
		}
		message.getDocumentOnServers().addAll(pasteDocs);
		return message;
	}


	/**
	 * 
	 * @param _fileName
	 * @param _dest
	 * @return
	 * @throws Exception
	 */
	public int getNextCopiedFileNumber(String _fileName, String _dest) throws Exception{
		if(_dest==null|| _dest.trim().equals("") || _fileName == null || _fileName.trim().equals(""))
		{
			return -1;
		}
		_dest = formatPathForDirectory(_dest);
		String search = FileHandler.getFileNameWithoutExt(_fileName)+"_Copy";
		int i =0;

		String query ="SELECT FileName FROM "+this.tableNameSpace+" WHERE FilePath = ? ORDER BY FileName";
		IExternalDatabaseRuntimeConnection connection = null;
		//Recordset rset = null;
		List<Record> recordList= (List<Record>) List.create(Record.class);
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, _dest+_fileName);
				//rset=executeStatement(stmt);
				recordList=executeStmt(stmt);
				if(recordList.isEmpty()){
					i=0;
				}else
				{
					query ="SELECT FileName FROM "+this.tableNameSpace+" WHERE FilePath LIKE ? ESCAPE '"+escapeChar+"' ORDER BY FileName";
					stmt = jdbcConnection.prepareStatement(query);
					String s1= escapeUnderscoreInPath(_dest+search);
					stmt.setString(1, s1+"%");
					//Ivy.log().info("Search for copy for "+_dest+search+"%");
					//rset=executeStatement(stmt);
					recordList=executeStmt(stmt);
					if(recordList.isEmpty()){
						i=1;
					}else{
						int tmpi=i;
						for(Record rec: recordList)
						{
							String n = rec.getField("FileName").toString();
							n=FileHandler.getFileNameWithoutExt(n);
							//Ivy.log().info("Search for copy found "+n);
							try{
								n = n.substring(n.lastIndexOf("_Copy")+5);
								//Ivy.log().info("Search for copy number found "+n);
								tmpi=Integer.parseInt(n)+1;
								if(tmpi>i)
								{
									i=tmpi;
								}
							}catch (Exception ex){
								//do nothing
							}
						}
					}
				}

			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return i;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#renameDirectory(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ReturnedMessage renameDirectory(String path, 
			String newName) throws Exception {
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		if(path==null || newName==null || newName.trim().equals("")){
			message.setText("One of the parameter was invalid for the method renameDirectory in "+this.getClass().getName());
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}
		newName= formatPathForDirectoryWithoutLastSeparator(newName);
		if(newName.contains("/") || newName.contains("\"") || newName.contains("'"))
		{
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/invalidCharacterInDirName"));
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}
		//format the path

		String newPath="";
		path= formatPathForDirectoryWithoutLastSeparator(path);
		//Ivy.log().info("Formatted path: "+path);
		if(path.equals(""))
		{//no valid path was entered ("////" for example)
			message.setText("One of the parameter was invalid for the method renameDirectory in "+this.getClass().getName());
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}
		if(!path.contains("/"))
		{//path is composed just by the directory old name

			newPath=newName;
		}else
		{//We get the old directory name

			newPath= path.substring(0,path.lastIndexOf("/"))+"/"+newName;
		}
		//Ivy.log().info("The new path: "+newPath);
		//look if directory exists
		if(!this.directoryExists(path))
		{
			message.setText("The directory to rename does not exist.");
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}
		//Check if new path exists
		if(this.directoryExists(newPath))
		{
			message.setText("The directory "+newPath+" already exists. You cannot create a duplicate directory.");
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}
		//Select all the files in the dir structure. If one file is edited, cannot rename the directory.
		List<DocumentOnServer> docs = this.getDocumentOnServersInDirectory(path, true);
		boolean fileEdited=false;
		for (DocumentOnServer doc: docs){
			if(doc.getLocked().equalsIgnoreCase("1"))
			{
				fileEdited = true;
				break;
			}
		}
		if(fileEdited)
		{
			message.setText("At least on file contained in the directory or one of its subdirectories is edited. You cannot rename the directory.");
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}

		IExternalDatabaseRuntimeConnection connection = null;
		//we update all the contained files
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			String query ="UPDATE "+this.tableNameSpace+" SET FilePath = ? WHERE FileId = ?";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				for (DocumentOnServer doc: docs)
				{
					try{
						String p = path+"/";
						String s = doc.getPath().replaceFirst(p, newPath+"/");
						stmt.setString(1, s);
						stmt.setInt(2, Integer.parseInt(doc.getFileID()));
						stmt.executeUpdate();
					}catch(Exception ex){
						Ivy.log().error("Error in renameDirectory method while renaming the path of a file.", ex);
					}
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		// we replace the folder path on the child directories
		ArrayList<FolderOnServer> dirs = this.getListDirectoriesUnderPath(path+"/");
		if(dirs.size()>0 && dirs.get(0).getPath().equals(path))
		{//We remove the directory to rename from the list
			dirs.remove(0);
		}
		//Ivy.log().info("Sub dir to rename: "+dirs.size());
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			String query ="UPDATE "+this.dirTableNameSpace+" SET dir_path = ? WHERE id = ?";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				for (FolderOnServer dir: dirs)
				{
					try{
						String p = path+"/";
						String s = dir.getPath().replaceFirst(p, newPath+"/");
						//Ivy.log().info("New dir path: "+s);
						stmt.setString(1, s);
						stmt.setInt(2, dir.getId());
						stmt.executeUpdate();
					}catch(Exception ex){
						Ivy.log().error("Error in renameDirectory method while renaming the path of a child directory.", ex);
					}
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		// we update the directory to rename
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			String query ="UPDATE "+this.dirTableNameSpace+" SET dir_path = ?, dir_name = ? WHERE dir_path = ?";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, newPath);
				stmt.setString(2, newName);
				stmt.setString(3, path);
				stmt.executeUpdate();
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return message;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#renameDocument(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean renameDocument(DocumentOnServer _doc, String _newName,String _userID) throws Exception {
		if(_doc==null || _doc.getPath().trim().length()==0 || _doc.getFilename().trim().length()==0 || _newName==null || _newName.trim().length()==0)
		{
			return false;
		}
		if(_userID==null)
		{
			_userID=Ivy.session().getSessionUserName();
		}
		int i=0;
		IExternalDatabaseRuntimeConnection connection = null;
		try {

			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			String query= "UPDATE "+this.tableNameSpace+" SET FileName = ?, FilePath = ?, ModificationDate = ?, ModificationTime = ?, ModificationUserId = ? WHERE FilePath = ?";

			PreparedStatement stmt = null;
			try{
				String ext = "."+FileHandler.getFileExtension(_doc.getPath());
				String newPath = _doc.getPath().substring(0,escapeBackSlash(_doc.getPath()).lastIndexOf("/"))+"/"+_newName.trim()+ext;
				if(this.getDocIdWithPath(newPath, jdbcConnection)>0)
				{
					return false;
				}else{
					stmt = jdbcConnection.prepareStatement(query);

					stmt.setString(1, _newName.trim());
					stmt.setString(2, newPath);
					stmt.setString(3, new Date().format("dd.MM.yyyy"));
					stmt.setString(4, new Time().format("HH:mm.ss"));
					stmt.setString(5, _userID);
					stmt.setString(6, escapeBackSlash(_doc.getPath()));
					i=stmt.executeUpdate();
					if(super.getFileActionHistoryController()!=null  && super.getFileActionHistoryController().getConfig().isRenameFileTracked())
					{
						long l = 0;
						if(_doc.getFileID()==null || _doc.getFileID().trim().length()==0)
						{
							l = this.getDocIdWithPath(newPath);
						}else{
							l = Long.parseLong(_doc.getFileID());
						}
						super.getFileActionHistoryController().createNewActionHistory(l, (short) 4, _userID, _doc.getFilename() +" -> "+_newName+ext);
					}
				}
			}
			finally{
				DatabaseUtil.close(stmt);
			}
		} 
		finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		if(i>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#renameDocumentOnServer(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer, java.lang.String)
	 */
	@Override
	public ReturnedMessage renameDocumentOnServer(DocumentOnServer _doc,
			String newName) throws Exception {
		ReturnedMessage message = new ReturnedMessage();
		message.setType(FileHandler.SUCCESS_MESSAGE);
		message.setFiles(List.create(java.io.File.class));
		if(_doc==null || _doc.getPath().trim().equals("") || _doc.getFilename().trim().equals("") || newName==null || newName.trim().equals("")){
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText("Unable to rename the given DocumentOnServer. One of the parameter is invalid in renameDocumentOnServer(DocumentOnServer document, String newName) in class "+this.getClass().getName());
			return message;
		}
		IExternalDatabaseRuntimeConnection connection=null;
		newName=FileHandler.getFileNameWithoutExt(newName);
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			String query = "UPDATE "+this.tableNameSpace+" SET FileName = ?, FilePath = ?, ModificationDate = ?, ModificationTime = ?, ModificationUserId = ? WHERE FilePath = ?";
			try{
				String ext = "."+FileHandler.getFileExtension(_doc.getPath());
				stmt = jdbcConnection.prepareStatement(query);
				String newPath =escapeBackSlash( _doc.getPath());
				newPath= newPath.substring(0,newPath.lastIndexOf("/"))+"/"+newName+ext;
				Ivy.log().info("Checking if the newPath already exists: "+newPath);
				if(this.getDocIdWithPath(newPath, jdbcConnection)>0)
				{
					message.setType(FileHandler.ERROR_MESSAGE);
					message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/RenameFileAlreadyExists"));
				}else{
					stmt.setString(1, newName+ext);
					stmt.setString(2,escapeBackSlash( newPath));
					stmt.setString(3, new Date().format("dd.MM.yyyy"));
					stmt.setString(4, new Time().format("HH:mm.ss"));
					stmt.setString(5, Ivy.session().getSessionUserName());
					stmt.setString(6,escapeBackSlash( _doc.getPath()));

					//Ivy.log().debug(query+" "+newName+ext+" "+newPath+" "+document.getPath());
					stmt.executeUpdate();
					if(super.getFileActionHistoryController()!=null && super.getFileActionHistoryController().getConfig().isRenameFileTracked())
					{
						long l = 0;
						if(_doc.getFileID()==null || _doc.getFileID().trim().length()==0)
						{
							l = this.getDocIdWithPath(newPath);
						}else{
							l = Long.parseLong(_doc.getFileID());
						}
						super.getFileActionHistoryController().createNewActionHistory(l, (short) 4, Ivy.session().getSessionUserName(), _doc.getFilename() +" -> "+newName+ext);
					}
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return message;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#saveDocumentOnServer(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer, java.lang.String)
	 */
	@Override
	public ReturnedMessage saveDocumentOnServer(DocumentOnServer document,
			String fileDestinationPath) throws Exception {
		ReturnedMessage message = new ReturnedMessage();
		message.setType(FileHandler.SUCCESS_MESSAGE);
		message.setFiles(List.create(java.io.File.class));
		if(document == null || document.getPath()==null || document.getPath().trim().equals("") || document.getFilename()==null
				|| document.getFilename().trim().equals("")){
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText("Unable to save the given DocumentOnServer. One of the parameter is invalid in saveDocumentOnServer(DocumentOnServer document, String fileDestinationPath) in class "+this.getClass().getName());
			return message;
		}

		if(fileDestinationPath==null || fileDestinationPath.trim().equals(""))
		{//no destination path => we suppose the file goes in the same path as document (=Update the document)
			fileDestinationPath=formatPath(document.getPath().trim());
		}

		int id=0;
		try{
			id=Integer.parseInt(document.getFileID().trim());

		}catch(Exception ex){

		}
		if(id<=0)
		{//then it may be a new document we check if this document already exists
			DocumentOnServer doc = this.getDocumentOnServer(document.getPath().trim());

			if(doc!=null && doc.getFileID()!=null){
				try{
					id=Integer.parseInt(doc.getFileID().trim());

				}catch(Exception ex){

				}
			}
			if(id<=0)
			{//new
				int j =this.insertOneDocument(document);
				if(j>0)
				{//Success
					document.setFileID(String.valueOf(j));
					message.setDocumentOnServer(document);
					return message;
				}else
				{//error
					message.setType(FileHandler.ERROR_MESSAGE);
					message.setText("Failed to insert the new documentOnServer object in saveDocumentOnServer(DocumentOnServer document, String fileDestinationPath) in class "+this.getClass().getName());
					return message;
				}
			}else{
				// it will overwrite an existing document
				document.setFileID(doc.getFileID().trim());
			}
		}
		// id is set, we update the existing DocumentOnServer
		java.io.File f = null;
		if(document.getIvyFile() !=null && document.getIvyFile().isFile())
		{
			f=document.getIvyFile().getJavaFile();
		}else if(document.getJavaFile() !=null && document.getJavaFile().isFile())
		{
			f = document.getJavaFile();
		}else 
		{
			f=this.getDocumentOnServerWithJavaFile(document).getJavaFile();
		}

		String query="";
		String query2="";
		String filesize="";
		if(f==null || !f.isFile()){//if non file found to get content, we ignore the content field
			query = "UPDATE "+this.tableNameSpace+
			" SET FileName= ?,  FilePath= ?," +
			" FileSize= ?, Locked= ?, LockingUserId= ?, ModificationUserId= ?," +
			" ModificationDate= ?, ModificationTime= ?, Description= ? WHERE FileId = ?";
			filesize = document.getFileSize();
		}else{
			query = "UPDATE "+this.tableNameSpace+
			" SET FileName= ?,  FilePath= ?," +
			" FileSize= ?, Locked= ?, LockingUserId= ?, ModificationUserId= ?," +
			" ModificationDate= ?, ModificationTime= ?, Description= ? WHERE FileId = ?";
			query2="UPDATE "+this.fileContentTableNameSpace+" SET file_content= ? WHERE File_id = ?";
			filesize= FileHandler.getFileSize(f);
		}

		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");
		String user = Ivy.session().getSessionUserName();
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, document.getFilename());
				stmt.setString(2, escapeBackSlash(document.getPath()));
				stmt.setString(3, filesize);
				int lock =0;
				try{
					lock = Integer.parseInt(document.getLocked().trim());
					if(lock<0){
						lock=0;
					}
					if(lock>1){
						lock=1;
					}
				}catch(Exception ex){

				}
				stmt.setInt(4, lock);
				stmt.setString(5, (document.getLockingUserID()==null || document.getLockingUserID().trim().equals("")?user: document.getLockingUserID().trim()));
				stmt.setString(6, user);
				stmt.setString(7, date);
				stmt.setString(8, time);
				String s = document.getDescription();
				if(s==null)
				{
					s="";
				}
				stmt.setString(9, s);
				stmt.setInt(10, id);
				stmt.executeUpdate();
				if(f!=null && f.exists())
				{// the content exists
					// set the content
					stmt = jdbcConnection.prepareStatement(query2);
					FileInputStream is=null;
					try{
						is = new FileInputStream ( f );  
						stmt.setBinaryStream (1, is, (int) f.length() ); 
						//stmt.setBlob(10, is, (int) f.length());
						//set the id (WHERE CLAUSE)
						stmt.setInt(2, id);
						stmt.executeUpdate();
					}finally
					{
						if(is!=null)
						{
							is.close();
						}
					}
				}
				message.setDocumentOnServer(document);
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		if(this.activateFileType)
		{
			long ftId =0;
			if(document.getFileType() != null && document.getFileType().getId()!=null)
			{
				ftId = document.getFileType().getId();
			}
			this.ftController.setDocumentFileType(document, ftId);
		}
		return message;
	}

	@Override
	public ReturnedMessage setFileDescription(String path, String description)
	throws Exception {
		ReturnedMessage message = new ReturnedMessage();
		message.setType(FileHandler.SUCCESS_MESSAGE);
		message.setFiles(List.create(java.io.File.class));
		if(path==null || path.trim().equals(""))
		{
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText("Unable to set the description. One of the parameter is invalid in setFileDescription(String path, String description) in class "+this.getClass().getName());
			return message;
		}

		if(description == null)
		{
			description="";
		}

		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			String query = "UPDATE "+this.tableNameSpace+" SET Description = ? WHERE FilePath = ?";
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1,description);
				stmt.setString(2,escapeBackSlash(path));
				int i = stmt.executeUpdate();
				if(i<=0){
					message.setType(FileHandler.ERROR_MESSAGE);
					message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/fileNotfound")+" "+path);
				}
				if(super.getFileActionHistoryController()!=null && super.getFileActionHistoryController().getConfig().isChangeFileDescriptionTracked())
				{
					long l  = this.getDocIdWithPath(path);

					super.getFileActionHistoryController().createNewActionHistory(l, (short) 3, Ivy.session().getSessionUserName(), "");
				}
			}finally{
				DatabaseUtil.close(stmt);
			}

		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}

		return message;

	}

	@Override
	public ReturnedMessage setFileDescription(DocumentOnServer document,
			String description) throws Exception {
		ReturnedMessage message = new ReturnedMessage();
		message.setType(FileHandler.SUCCESS_MESSAGE);
		message.setFiles(List.create(java.io.File.class));
		if(document==null)
		{
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText("Unable to set the description. One of the parameter is invalid in setFileDescription(DocumentOnServer document, String description) in class "+this.getClass().getName());
			return message;
		}
		int id=0;
		if(document.getFileID()!=null)
		{
			try{
				id=Integer.parseInt(document.getFileID());
			}catch(Exception ex){

			}
		}
		if(id<=0 && document.getPath()!=null){
			DocumentOnServer doc=this.getDocumentOnServer(document.getPath());
			if(doc.getFileID()!=null){
				try{
					id=Integer.parseInt(doc.getFileID());
				}catch(Exception ex){

				}
			}
		}
		if(id<=0){
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText("Unable to set the description. The DocumentOnServer parameter is invalid in setFileDescription(DocumentOnServer document, String description) in class "+this.getClass().getName());
			return message;
		}
		if(description == null)
		{
			description="";
		}

		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			String query = "UPDATE "+this.tableNameSpace+" SET Description = ? WHERE FileId = ?";
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1,description);
				stmt.setInt(2,id);
				int i = stmt.executeUpdate();
				if(i<=0){
					message.setType(FileHandler.ERROR_MESSAGE);
					message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/fileNotfound"));
				}
				if(super.getFileActionHistoryController()!=null && super.getFileActionHistoryController().getConfig().isChangeFileDescriptionTracked())
				{
					super.getFileActionHistoryController().createNewActionHistory(id, (short) 3, Ivy.session().getSessionUserName(), "");
				}
			}finally{
				DatabaseUtil.close(stmt);
			}

		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}

		return message;
	}

	@Override
	public ReturnedMessage moveDocumentOnServer(DocumentOnServer document, String destination) throws Exception
	{
		ReturnedMessage message = new ReturnedMessage();
		message.setType(FileHandler.SUCCESS_MESSAGE);
		message.setFiles(List.create(java.io.File.class));
		if(document == null || document.getPath()==null || document.getPath().trim().equals("") || document.getFilename()==null
				|| document.getFilename().trim().equals("") || destination==null || destination.trim().equals("")){
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText("Unable to save the given DocumentOnServer. One of the parameter is invalid in saveDocumentOnServer(DocumentOnServer document, String fileDestinationPath) in class "+this.getClass().getName());
			return message;
		}
		destination = formatPathForDirectory(destination);
		String query="UPDATE "+this.tableNameSpace+" SET  FilePath= ? WHERE FileId = ?";
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, destination+document.getFilename());
				stmt.setInt(2, Integer.parseInt(document.getFileID()));

				stmt.executeUpdate();
				if(super.getFileActionHistoryController()!=null&& super.getFileActionHistoryController().getConfig().isMoveFileTracked())
				{
					super.getFileActionHistoryController().createNewActionHistory(Long.parseLong(document.getFileID()), (short) 10, Ivy.session().getSessionUserName(), document.getPath() +" -> "+destination+document.getFilename());
				}
				if(this.securityActivated)
				{
					FolderOnServer fos = this.securityController.getDirectoryWithPath(destination);
					document.setCanUserDelete(fos.getCanUserDeleteFiles());
					document.setCanUserRead(fos.getCanUserOpenDir());
					document.setCanUserWrite(fos.getCanUserWriteFiles());
				}else{
					document.setCanUserDelete(true);
					document.setCanUserRead(true);
					document.setCanUserWrite(true);
				}
				message.setDocumentOnServer(document);
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return message;
	}

	/**
	 * unLock a document in the DB 
	 * @param _doc: the DocumentOnServer Object that has to be Locked
	 * @return true if the document was unLocked, else false
	 * @throws Exception
	 */
	public boolean unlockDocument(DocumentOnServer _doc) throws Exception{
		if(_doc == null )
		{
			throw new Exception("Invalid DocumentOnServer Object in unlockDocument method.");
		}
		boolean flag = false;
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			String query = "UPDATE "+this.tableNameSpace+" SET Locked=0, LockingUserId= ? WHERE FilePath = ?";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, "");
				stmt.setString(2,escapeBackSlash( _doc.getPath()));
				if(stmt.executeUpdate()>0){
					flag = true;
				}
			}finally{
				DatabaseUtil.close(stmt);
			}

		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return flag;
	}

	/**
	 * unLock a document in the DB with check if given user is the same who has Locked the document.<br>
	 * If it is not the same, the File won't be Locked.
	 * @param _doc: the DocumentOnServer Object that has to be Locked
	 * @param _user the user who locks this document
	 * @return true if the document was unLocked, else false
	 * @throws Exception
	 */
	public boolean unlockDocumentWithUSerCheck(DocumentOnServer _doc, String _user)throws Exception{
		if(_doc == null )
		{
			throw new Exception("Invalid DocumentOnServer Object in unlockDocument method.");
		}
		boolean flag = false;
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			String query = "UPDATE "+this.tableNameSpace+" SET Locked=0, LockingUserId = ? WHERE FilePath = ? AND LockingUserId = ?";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, "");
				stmt.setString(2, escapeBackSlash(_doc.getPath()));
				stmt.setString(3, _user.trim());
				if(stmt.executeUpdate()>0){
					flag = true;
				}
			}finally{
				DatabaseUtil.close(stmt);
			}

		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return flag;
	}

	/**
	 * unLock a File in the DB<br>
	 * The File here just holds the informations stored in the DB (path, etc...). The File does not exit on the FileSystem.
	 * @param _file: the java.io.File to lock
	 * @return true if the document was unLocked, else false
	 * @throws Exception
	 */
	public boolean unlockFile(java.io.File _file) throws Exception{
		if(_file == null )
		{
			throw new Exception("Invalid DocumentOnServer Object in unlockDocument method.");
		}
		boolean flag = false;
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			String query = "UPDATE "+this.tableNameSpace+" SET Locked=0, LockingUserId= ? WHERE FilePath = ?";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, "");
				stmt.setString(2, escapeBackSlash(_file.getPath()));
				if(stmt.executeUpdate()>0){
					flag = true;
				}
			}finally{
				DatabaseUtil.close(stmt);
			}

		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return flag;
	}

	/**
	 * unLock a File in the DB with check if given user is the same who has Locked the document.<br>
	 * If it is not the same, the File won't be Locked.<br>
	 * The File here just holds the informations stored in the DB (path, etc...). The File does not exit on the FileSystem.
	 * @param _file: the java.io.File to lock
	 * @param _user the user who locks this document
	 * @return true if the document was unLocked, else false
	 * @throws Exception
	 */
	public boolean unlockFileWithUSerCheck(java.io.File _file, String _user) throws Exception{
		if(_file == null )
		{
			throw new Exception("Invalid DocumentOnServer Object in unlockDocument method.");
		}
		boolean flag = false;
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			String query = "UPDATE "+this.tableNameSpace+" SET Locked=0, LockingUserId= ? WHERE FilePath = ? AND LockingUserId = ?";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, "");
				stmt.setString(2, escapeBackSlash(_file.getPath()));
				stmt.setString(3, _user.trim());
				if(stmt.executeUpdate()>0){
					flag = true;
				}
			}finally{
				DatabaseUtil.close(stmt);
			}

		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return flag;
	}

	/**
	 * this Method should be used to unlock all the files edited by a given user under a given path.
	 * If the boolean argument "recursive" is true, then all the files in the children directories
	 * are going to be unLocked. Else just the files directly under the given path are going to be unLocked.
	 * This method can be used when you close an application for example.
	 * @param _path the path where to look for the Locked files
	 * @param _user the ivy user name 
	 * @param _recursive true or false. If is recursive, look in all the sub directories under the path
	 * @throws Exception
	 */
	public void unlockFilesEdited(String _path, String _user, boolean _recursive)throws Exception {
		String folderPath = escapeBackSlash(FileHandler.formatPathWithEndSeparator(_path, false));
		folderPath=escapeUnderscoreInPath(folderPath);
		String query="";
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			if(_recursive)
			{
				query="UPDATE "+ this.tableNameSpace + " SET Locked = 0, LockingUserId= ? WHERE LockingUserId = ? AND FilePath LIKE ? ESCAPE '"+escapeChar+"'";
			}else
			{
				//query="UPDATE "+ tableName + " SET Locked = 0 WHERE LockingUserId LIKE '"+user+"' AND FilePath LIKE '"+folderPath+"%' AND FilePath NOT LIKE '"+folderPath+"%["+java.io.File.separator+"]%'";
				query="UPDATE "+ this.tableNameSpace + " SET Locked = 0, LockingUserId= ? WHERE LockingUserId = ? AND FilePath LIKE ? ESCAPE '"+escapeChar+"' AND FilePath NOT LIKE ? ESCAPE '"+escapeChar+"'";
			}
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				if(_recursive)
				{
					stmt.setString(1, "");
					stmt.setString(2, _user.trim());
					stmt.setString(3, folderPath+"%");
					stmt.executeUpdate();
				}
				else
				{
					stmt.setString(1, "");
					stmt.setString(2, _user.trim());
					stmt.setString(3, folderPath+"%");
					stmt.setString(4, folderPath+"%/%");
					stmt.executeUpdate();
				}

			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
	}

	/**
	 * update the documents with one complete SQL Query as argument
	 * @param query: the SQL Query as String
	 * @throws Exception
	 */
	public void updateDocuments(String _query) throws Exception{
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(_query);
				stmt.execute();
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
	}

	/**
	 * Allows executing an update on documents with the given Key/Value pairs and conditions for filtering
	 * @param KVP: List<KeyValuePair> that represents the new values for the given properties (Keys)
	 * @param conditions: List<String> the list of the conditions to filter the update
	 * @return a RecordSet of the updated documents
	 * @throws Exception
	 */
	public int updateDocuments(List<KeyValuePair> _KVP, List<String> _conditions) throws Exception{
		int rows=0;
		if(_KVP.isEmpty())
		{//do nothing if the list of KeyValuePairs is empty
			return 0;
		}
		//build the SQL Query with the keyValue pairs and the list of conditions
		StringBuilder sql=new StringBuilder("UPDATE "+this.tableNameSpace+" SET");

		for(KeyValuePair kvp: _KVP){
			//sql.append(" "+kvp.getKey().toString()+"='"+escapeForMSSQL(kvp.getValue().toString())+"', ");
			sql.append(" "+kvp.getKey().toString()+"= ? ,");
		}
		sql=sql.deleteCharAt(sql.lastIndexOf(","));

		sql.append(" WHERE ");
		int numConditions= _conditions.size()-1;
		for(int i=0; i<numConditions;i++){
			sql.append(_conditions.get(i).trim()+" AND ");
		}
		sql.append(_conditions.get(numConditions));
		sql.trimToSize();
		String _query = sql.toString();
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt=null;
			try{
				stmt = jdbcConnection.prepareStatement(_query);
				int i = 1;
				for(KeyValuePair kvp: _KVP)
				{
					if(kvp.getValue() instanceof String){
						stmt.setString(i, kvp.getValue().toString());
					}else{
						int n=0;
						try{
							n = Integer.parseInt(kvp.getValue().toString());
						}catch(Exception ex){

						}
						stmt.setInt(i, n);
					}
					i++;
				}

				rows=stmt.executeUpdate();
			}finally{
				DatabaseUtil.close(stmt);
			}			
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return rows;
	}

	/**
	 * delete documents from the DB
	 * @param _documents the list of the DocumentOnServer to delete
	 * @return the number of items deleted
	 * @throws Exception
	 */
	public int deleteDocumentsInDBOnly(List<DocumentOnServer> _documents)throws Exception{
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			return this.deleteDocumentsInDBOnly(_documents, jdbcConnection);
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
	}
	
	/**
	 * Similar to the deleteFilesInDBOnly(List<java.io.File> _files,String parentDirectoryPath, Connection con) method.<br>
	 * For private use only.<br>
	 * deletes the given documents by using the given java.sql.Connection object.<br>
	 * @param _documents the list of the DocumentOnServer to delet
	 * @param con the java.sql.Connection to the database.<br> 
	 * THIS METHOD DOES NOT RELEASE THIS CONNECTION. THIS SHOULD BE DONE IN THE CALLING METHOD.
	 * @return the number of items deleted
	 * @throws Exception
	 */
	private int deleteDocumentsInDBOnly(List<DocumentOnServer> _documents, Connection con) throws Exception {
		if(con==null || con.isClosed())
		{
			throw new IllegalArgumentException("The java.sql.Connection Object is null or closed. Method deleteDocumentsInDBOnly in FileStoreDBHandler.");
		}
		
		if(_documents==null || _documents.size()==0)
		{
			return 0;
		}
		int deletedFiles=0;
		String base= "DELETE FROM "+this.tableNameSpace+" WHERE FileId = ?"; 
		String query="DELETE FROM "+this.fileContentTableNameSpace+" WHERE file_id = ?";

		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		boolean deleteHistory=super.getFileActionHistoryController()!=null && super.getFileActionHistoryController().getConfig().isDeleteFileTracked();
		try{
			stmt = con.prepareStatement(base);
			stmt2 = con.prepareStatement(query);
			for(DocumentOnServer doc: _documents){
				int id=0;
				if(doc.getFileID()!=null)
				{
					try{ id=Integer.parseInt(doc.getFileID()); }catch(Exception ex){/*do nothing*/}
				}
				if(id<=0){
					id=this.getDocIdWithPath(escapeBackSlash(doc.getPath()), con);
				}
				if(id>0){
					stmt.setInt(1, id);
					stmt.executeUpdate();
					stmt2.setInt(1, id);
					deletedFiles+=stmt2.executeUpdate();
					if(deleteHistory)
					{
						super.getFileActionHistoryController().createNewActionHistory(id, (short) 5, Ivy.session().getSessionUserName(), doc.getPath(),con);
					}
					if(this.activateFileVersioning)
					{
						this.fvc.deleteAllVersionsFromFile(id,con);
					}
				}
			}
		}finally{
			DatabaseUtil.close(stmt2);
			DatabaseUtil.close(stmt);
		}
		return deletedFiles;
	}

	/**
	 * For private use only.
	 * Deletes the given java File from the database.
	 * @param _files List of files that should be deleted
	 * @param parentDirectoryPath the path of the directory in the database where the files are stored.
	 * @param con the java.sql.Connection to the database.<br> 
	 * THIS METHOD DOES NOT RELEASE THIS CONNECTION. THIS SHOULD BE DONE IN THE CALLING METHOD.
	 * @return the number of deleted files.
	 * @throws Exception
	 */
	private int deleteFilesInDBOnly(List<java.io.File> _files,String parentDirectoryPath, Connection con)throws Exception{
		int deletedFiles=0;
		if(_files==null || _files.size()==0)
		{
			return 0;
		}
		if(parentDirectoryPath!=null && parentDirectoryPath.trim().length()==0)
		{
			parentDirectoryPath=null;
		}else{
			parentDirectoryPath=parentDirectoryPath.trim();
		}
		String base= "DELETE FROM "+this.tableNameSpace+" WHERE FileId = ?"; 
		String query="DELETE FROM "+this.fileContentTableNameSpace+" WHERE file_id = ?";

		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try{
			stmt = con.prepareStatement(query);
			stmt2 = con.prepareStatement(base);
			boolean deleteHistory=super.getFileActionHistoryController()!=null && super.getFileActionHistoryController().getConfig().isDeleteFileTracked();
			for(java.io.File file: _files){
				int id=0;
				try{
					id=this.getDocIdWithPath((parentDirectoryPath==null?file.getPath():parentDirectoryPath+file.getName()), con);
				}catch(Exception ex)
				{

				}
				if(id>0){
					stmt.setInt(1, id);
					stmt.executeUpdate();
					stmt2.setInt(1, id);
					deletedFiles+=stmt2.executeUpdate();
					if(deleteHistory)
					{
						super.getFileActionHistoryController().
							createNewActionHistory(id, (short) 5, Ivy.session().getSessionUserName(), (parentDirectoryPath==null?
								file.getPath():parentDirectoryPath+file.getName()),con);
					}
					if(this.activateFileVersioning)
					{
						this.fvc.deleteAllVersionsFromFile(id,con);
					}
				}
			}
		}finally{
			DatabaseUtil.close(stmt);
			DatabaseUtil.close(stmt2);
		}
		return deletedFiles;
	}

	/**
	 * @return the dirTableName
	 */
	public String getDirTableName() {
		return dirTableName;
	}

	/**
	 * @param dirTableName the dirTableName to set
	 */
	public void setDirTableName(String dirTableName) {
		this.dirTableName = dirTableName;
	}

	/**
	 * @return the dirTableNameSpace
	 */
	public String getDirTableNameSpace() {
		return dirTableNameSpace;
	}

	/**
	 * @param dirTableNameSpace the dirTableNameSpace to set
	 */
	public void setDirTableNameSpace(String dirTableNameSpace) {
		this.dirTableNameSpace = dirTableNameSpace;
	}

	@Override
	public boolean isDirectoryEmpty(String directoryPath) throws Exception {
		if(directoryPath==null || directoryPath.trim().equals(""))
		{
			throw new IllegalArgumentException("Illegal directory Path in method isDirectoryEmpty(String directoryPath) in "+this.getClass().toString());
		}
		if(directoryExists(directoryPath))
		{
			if(getDocumentsInPath(directoryPath, true).size()>0)
			{
				return false;
			}
			if(getListDirectoriesUnderPath(directoryPath).size()>1)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * @param securityOn the securityOn to set
	 */
	public void setSecurityOn(boolean securityOn) {
		this.securityActivated = securityOn;
		if(this.securityActivated && this.securityController==null)
		{
			try {
				this.makeSecurityController();
			} catch (Exception e) {
				
			}
		}
	}

	/**
	 * @return the securityOn
	 */
	public boolean isSecurityOn() {
		return securityActivated;
	}

	@Override
	public ReturnedMessage zipDocumentOnServers(
			List<DocumentOnServer> documents, String dirPath, String zipName,
			boolean checkIfExists) throws Exception {
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		message.setDocumentOnServers(List.create(DocumentOnServer.class));

		if(dirPath == null || dirPath.trim().length()<=0 || documents==null || documents.size()==0)
		{
			throw new IllegalArgumentException("One of the parameter is not set in zipDocumentOnServers(List<DocumentOnServer> documents, String dirPath, String zipName,boolean checkIfExists) in "+ this.getClass());
		}

		dirPath=formatPathForDirectory(dirPath);
		zipName = zipName.endsWith(".zip")?zipName:zipName+".zip";
		boolean exists = this.fileExists(dirPath+zipName);

		if(checkIfExists && exists){
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/zipfileAlreadyExistsCannotCreateIt"));
			return message;
		}else if(exists)
		{
			this.deleteFile(dirPath+zipName);
		}

		ArrayList<java.io.File> zipFiles = new ArrayList<java.io.File>();
		for(DocumentOnServer doc : documents){
			if(doc.getJavaFile()!=null && doc.getJavaFile().isFile())
			{
				zipFiles.add(doc.getJavaFile());
			}else{
				java.io.File f = this.getDocumentOnServerWithJavaFile(doc).getJavaFile();
				if(f!=null && f.isFile()){
					zipFiles.add(f);
				}
			}
		}

		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");
		String user ="IVYSYSTEM";
		//we create a temp file on the server 
		String tmpPath="tmp/"+System.nanoTime();
		File ivyFile = new File(tmpPath+"/"+zipName,true);
		ivyFile.createNewFile();

		java.io.File zip = ZipHandler.makeZip(tmpPath, zipName, zipFiles);
		try{
			user = Ivy.session().getSessionUserName();
		}catch(Exception ex)
		{
			user ="IVYSYSTEM";
		}
		DocumentOnServer doc = new DocumentOnServer();
		doc.setCreationDate(date);
		doc.setCreationTime(time);
		doc.setUserID(user);
		doc.setDescription("");
		doc.setFilename(zipName);
		doc.setJavaFile(zip);
		doc.setFileSize(FileHandler.getFileSize(zip));
		doc.setLocked("0");
		doc.setLockingUserID("");
		doc.setModificationDate(date);
		doc.setModificationTime(time);
		doc.setModificationUserID(user);
		doc.setPath(dirPath+zipName);
		return this.saveDocumentOnServer(doc, "");

	}

	@Override
	public boolean deleteFile(String _filepath) throws Exception{
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			return this.deleteFile(_filepath, jdbcConnection);
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
	}
	
	/**
	 * For private use only.<br>
	 * It deletes the given File if it exists in the database.<br>
	 * It takes a java.sql.connection as parameter to be able to be called from within other methods <br>
	 * without locking another database connection during its processing.<br>
	 * See Issue #28265, database deadlock in some of the FileManagement methods used concurrently in a lot of different processes.<br>
	 * <b>Important: </b>this method does not release the given java.sql.Connection. It is up to the calling method to do that.
	 * @param _filepath the path of the file in the database that has to be deleted.
	 * @param con the java.sql.Connection Object
	 * @return true if the file was found and deleted, else false.
	 * @throws Exception 
	 */
	private boolean deleteFile(String _filepath, Connection con) throws Exception{
		if(con==null || con.isClosed())
		{
			throw new IllegalArgumentException("The java.sql.Connection Object is null or closed. Method deleteFile in FileStoreDBHanler.");
		}
		if(_filepath==null || con == null || con.isClosed())
		{
			return false;
		}
		boolean flag = false;
		String path= formatPath(_filepath);
		String base= "DELETE FROM "+this.tableNameSpace+" WHERE FilePath = ?"; 
		String query="DELETE FROM "+this.fileContentTableNameSpace+" WHERE file_id = ?";
		PreparedStatement stmt = null;
		try{
			int id = this.getDocIdWithPath(path, con);
			if(id>0)
			{
				stmt = con.prepareStatement(query);
				stmt.setInt(1, id);
				stmt.executeUpdate();
				if(super.getFileActionHistoryController()!=null && super.getFileActionHistoryController().getConfig().isDeleteFileTracked())
				{
					super.getFileActionHistoryController().createNewActionHistory(id, (short) 5, Ivy.session().getSessionUserName(), path, con);
				}
				if(this.activateFileVersioning)
				{
					this.fvc.deleteAllVersionsFromFile(id,con);
				}
				stmt = con.prepareStatement(base);
				stmt.setString(1, escapeBackSlash(path));
				flag = stmt.executeUpdate()>0;
			}
		}finally{
			DatabaseUtil.close(stmt);
		}
		return flag;
	}

	@Override
	public AbstractDirectorySecurityController getSecurityController()
	throws Exception {
		return this.securityController;
	}


	@Override
	public int getFile_content_storage_type() {
		return AbstractFileManagementHandler.FILE_STORAGE_DATABASE;
	}

}
