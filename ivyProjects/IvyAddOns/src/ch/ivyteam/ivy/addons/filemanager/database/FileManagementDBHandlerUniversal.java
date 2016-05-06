/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

import ch.ivyteam.db.jdbc.DatabaseUtil;
import ch.ivyteam.ivy.db.IExternalDatabase;
import ch.ivyteam.ivy.db.IExternalDatabaseApplicationContext;
import ch.ivyteam.ivy.db.IExternalDatabaseRuntimeConnection;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.scripting.objects.Record;
import ch.ivyteam.ivy.scripting.objects.Time;
import ch.ivyteam.ivy.scripting.objects.Tree;
import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.KeyValuePair;
import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.ReturnedMessage;
import ch.ivyteam.ivy.addons.filemanager.ZipHandler;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.security.AbstractDirectorySecurityController;
import ch.ivyteam.ivy.addons.filemanager.database.security.FileManagementStaticController;
import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityHandler;
import ch.ivyteam.ivy.addons.filemanager.folderonserver.FolderAction;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;


/**
 * @author ec
 * @since 01.10.2010
 * This class is the interface between the FileManager RDC and the Ivy DB Connection<br>
 * that stores the files informations.<br>
 * The FileManager uses and calls this Object whenever he needs to change or get the files informations.
 */
public class FileManagementDBHandlerUniversal extends AbstractFileManagementHandler{
	private String ivyDBConnectionName = null; // the user friendly connection name to Database in Ivy
	private String tableName = null; // the table name to use in queries
	private String schemaName = "";// the DB Schema name if needed (eg. by PostGreSQL)
	private String tableNameSpace = null; // equals to tableName if schemaName == null, else schemaName.tableName
	IExternalDatabase database=null;
	
	/**re
	 * Creates a new FileManagementDBHandlerUniversal.<br />
	 * The parameters for the Ivy Database Connection name, the files table name and the eventual database schema name<br />
	 * are going to be set with the corresponding ivy global variables values.
	 * @throws Exception 
	 */
	public FileManagementDBHandlerUniversal() throws Exception{
		this(null,null);
	}


	/**
	 * Creates a new FileManagementDBHandlerUniversal with possibility to overrides the ivy global variables settings for the two given parameters.<br />
	 * The schema name will be set with the corresponding global variable.
	 * @param _ivyDBConnectionName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _tableName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @throws Exception 
	 */
	public FileManagementDBHandlerUniversal(String _ivyDBConnectionName, String _tableName) throws Exception {
		super();
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
		
		if(Ivy.var().get("xivy_addons_fileManager_databaseSchemaName")!=null)
		{
			this.schemaName=Ivy.var().get("xivy_addons_fileManager_databaseSchemaName").trim();
		}
		if(this.schemaName!=null && this.schemaName.length()>0)
		{
			this.tableNameSpace="\""+this.schemaName+"\""+"."+"\""+this.tableName+"\"";
		}else{
			this.tableNameSpace = this.tableName;
		}
		this.initialize();
	}

	/**
	 * Creates a new FileManagementDBHandlerUniversal with possibility to overrides the ivy global variables settings for the 3 given parameters.
	 * @param _ivyDBConnectionName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _tableName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _schemaName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @throws Exception
	 */
	public FileManagementDBHandlerUniversal(String _ivyDBConnectionName, String _tableName, String _schemaName) throws Exception{
		super();
		if(_ivyDBConnectionName==null || _ivyDBConnectionName.trim().length()==0)
		{//if ivy user friendly name of database configuration not set used default
			this.ivyDBConnectionName = Ivy.var().get("xivy_addons_fileManager_ivyDatabaseConnectionName").trim();
		}else{
			this.ivyDBConnectionName = _ivyDBConnectionName.trim();
		}
		if(_tableName==null || _tableName.trim().length()==0)
		{//if ivy table name not set used default
			this.tableName=Ivy.var().get("xivy_addons_fileManager_fileMetaDataTableName").trim();
		}else{
			this.tableName=_tableName.trim();
			this.tableNameSpace = this.tableName;
		}
		if(_schemaName!=null && _schemaName.trim().length()>0)
		{//set the schema name variable

			this.schemaName = _schemaName.trim();
			//since the schema name is for now only use in PostGreSQL, 
			//we escape the schema and table name to be able to support non lower case schemas
			this.tableNameSpace="\""+this.schemaName+"\""+"."+"\""+this.tableName+"\""; 
		}
		this.initialize();
	}
	
	/**
	 * Creates a new FileManagementDBHandlerUniversal with the given BasicConfigurationController
	 * @param _conf BasicConfigurationController
	 * @throws Exception NullpointerException if the BasicConfigurationController is null
	 */
	public FileManagementDBHandlerUniversal(BasicConfigurationController _conf) throws Exception
	{
		super();
		this.ivyDBConnectionName = _conf.getIvyDBConnectionName();
		this.tableName = _conf.getFilesTableName();
		this.schemaName = _conf.getDatabaseSchemaName();
		this.tableNameSpace = this.tableName;
		if(this.schemaName!=null && this.schemaName.length()>0)
		{//set the schema name variable

			//since the schema name is for now only use in PostGreSQL, 
			//we escape the schema and table name to be able to support non lower case schemas
			this.tableNameSpace="\""+this.schemaName+"\""+"."+"\""+this.tableName+"\""; 
		}
		this.initialize();
		
	}
	
	private void initialize()
	{
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = this.getDatabase().getAndLockConnection();
			DatabaseMetaData dbmd = connection.getDatabaseConnection().getMetaData();
			String prod = dbmd.getDatabaseProductName().toLowerCase();
			if(prod.contains("mysql") || (prod.contains("postgre") && 
					Double.valueOf(dbmd.getDatabaseMajorVersion()+"."+dbmd.getDatabaseMinorVersion())<9.1 )){
				setEscapeChar("\\\\");
			}
		} catch (Exception e) {
			Ivy.log().error("Error while getting the database product name");
		}finally{
			if(connection!=null ){
				this.database.giveBackAndUnlockConnection(connection);
			}
		}
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


	/**
	 * get all the Files that are listed under a specified path in the database xlin400Additional, table UploadedFiles
	 * @param _path: String representing the path of the directory that contains the files
	 * @param _isrecursive: boolean, if true, all the files in the tree structure under the directory are going to be listed.<br>
	 * If false, only the files directly under the directory are going to be listed
	 * @return an ArrayList of {@link DocumentOnServer} Objects. Each DocumentOnServer object represents a File with several informations (name, path, size, creationdate, creationUser...)
	 * @throws Exception 
	 */
	@SuppressWarnings("deprecation")
	public ArrayList<DocumentOnServer> getDocumentsInPath(String _path, boolean _isrecursive) throws Exception{
		return FileManagementStaticController.getDocumentsInPath(this.getDatabase(), tableNameSpace, _path, _isrecursive, this.getEscapeChar());
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
		List<Record> recordList= (List<Record>) List.create(Record.class);
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
				recordList=executeStmt(stmt);
				if(recordList!=null){
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
						try{
							doc.setExtension(doc.getFilename().substring(doc.getFilename().lastIndexOf(".")+1));
						}catch(Exception ex){
							//Ignore the Exception here
						}
						al.add(doc);
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

		al.trimToSize();

		return al;
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
		List<Record> recordList= (List<Record>) List.create(Record.class);
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
				try{
					doc.setExtension(doc.getFilename().substring(doc.getFilename().lastIndexOf(".")+1));
				}catch(Exception ex){
					//Ignore the Exception here
				}
				al.add(doc);
			}
		}
		al.trimToSize();
		return al;
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
	 * @return a Recordset of the updated documents
	 * @throws Exception
	 */
	public int updateDocuments(List<KeyValuePair> _KVP, List<String> _conditions) throws Exception{
		int rows=0;
		if(_KVP.isEmpty())
		{//do nothing if the list of KeyValuePairs is empty
			return 0;
		}
		//build dthe SQL Query with the keyValue pairs and the list of conditions
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

	@Override
	public boolean renameDocument(DocumentOnServer _doc, String _newName,String _userID) throws Exception {
		if(_doc==null || _doc.getPath().trim().length()==0 || _doc.getFilename().trim().length()==0 || _newName==null || _newName.trim().length()==0)
		{
			return false;
		}
		if(_userID==null)
		{
			_userID="";
		}
		int i=0;
		IExternalDatabaseRuntimeConnection connection = null;
		try {

			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			String query= "UPDATE "+this.tableNameSpace+" SET FileName = ?, FilePath = ?, ModificationDate = ?, ModificationTime = ?, ModificationUserId = ? WHERE FilePath = ?";

			PreparedStatement stmt = null;
			try{
				String newPath = _doc.getPath().substring(0,escapeBackSlash(_doc.getPath()).lastIndexOf("/"))+"/"+_newName.trim();
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, _newName.trim());
				stmt.setString(2, newPath);
				stmt.setString(3, new Date().format("dd.MM.yyyy"));
				stmt.setString(4, new Time().format("HH:mm.ss"));
				stmt.setString(5, _userID);
				stmt.setString(6, escapeBackSlash(_doc.getPath()));
				i=stmt.executeUpdate();
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

	@Override
	public void changeModificationInformations(File _file, String _userID) throws Exception {
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
	 * Lock a File in the DB if not already Locked by another user
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
	 * unLock a File in the DB
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
	 * unLock a File in the DB with check if given user is the same who has Locked the document.<br>
	 * If it is not the same, the File won't be Locked.
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
	 * Insert a  java.io.File Object into the File indexation storing System.
	 * @param _file: java.io.File that has to be inserted into the File indexation storing System.
	 * @param _user the user name who inserts the file
	 * @return the number of inserted files
	 * @throws Exception
	 */
	public int insertFile(java.io.File _file, String _user)throws Exception {
		int insertedId = -1;
		if(_file== null || _user == null) return insertedId;
		String base = "INSERT INTO "+this.tableNameSpace+
		" (FileName, FilePath, CreationUserId, CreationDate, CreationTime, FileSize, Locked, LockingUserId, ModificationUserId, ModificationDate, ModificationTime, Description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");

		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(base);
				stmt.setString(1, _file.getName());
				stmt.setString(2, escapeBackSlash(_file.getPath()));
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
				insertedId= stmt.executeUpdate();
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

	@Override
	public int insertFile(java.io.File _file,String _destinationPath, String _user)throws Exception {
		int insertedId = -1;
		if(_file== null || _user == null) return insertedId;
		String base = "INSERT INTO "+this.tableNameSpace+
		" (FileName, FilePath, CreationUserId, CreationDate, CreationTime, FileSize, Locked, LockingUserId, ModificationUserId, ModificationDate, ModificationTime, Description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");
		if(_user==null || _user.trim().equals("")){
			_user= Ivy.session().getSessionUserName();
		}
		_destinationPath = PathUtil.formatPathForDirectory(_destinationPath);
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(base);
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
				insertedId= stmt.executeUpdate();
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
	 * Insert a  DocumentOnServer Object into the File indexation storing System
	 * @param _document: DocumentOnServer that has to be inserted into the File properties storing system
	 * @return 1 if successful
	 * @throws Exception 
	 */
	public int insertOneDocument(DocumentOnServer _document)throws Exception {
		int insertedId = -1;
		if(_document== null) return insertedId;
		String base="";
		base = "INSERT INTO "+this.tableNameSpace+
		" (FileName, FilePath, CreationUserId, CreationDate, CreationTime, FileSize, Locked, LockingUserId, ModificationUserId, ModificationDate, ModificationTime, Description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");

		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(base);
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
				insertedId= stmt.executeUpdate();
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
	 * Insert a List of DocumentOnServer Objects into DB<br>
	 * It checks if the document already exits, if so, it delete them first.
	 * @param _documents: the List<DocumentOnServer> to insert in the DB
	 * @return the number of inserted documents
	 * @throws Exception
	 */
	public int insertDocuments(List<DocumentOnServer> _documents)throws Exception{
		int insertedIDs = -1;
		if(_documents==null || _documents.size()==0)
			return insertedIDs;

		// delete the documents that are already in the DB
		this.deleteDocumentsInDBOnly(_documents);
		String base="INSERT INTO "+this.tableNameSpace+
		" (FileName, FilePath, CreationUserId, CreationDate, CreationTime, FileSize, Locked, LockingUserId, ModificationUserId, ModificationDate, ModificationTime, Description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");

		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(base);
				for(DocumentOnServer doc: _documents){

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
					//Ivy.log().info(stmt.toString());
					insertedIDs+=stmt.executeUpdate();
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}

		return insertedIDs;
	}

	/**
	 * Insert a List of java.io.File Objects into DB
	 * @param _files: List<java.io.File> the files to add into the db
	 * @param _userIn: String representation of the user who is performing this operation
	 * @return the list of the last inserted Id's as a List of String
	 * @throws Exception
	 */
	public int insertFiles(List<java.io.File> _files, String _userIn) throws Exception{
		int insertedIDs = -1;
		if(_files==null || _files.size()==0)
			return insertedIDs;

		// delete the documents that are already in the DB
		this.deleteFilesInDBOnly(_files);
		String base="INSERT INTO "+this.tableNameSpace+
		" (FileName, FilePath, CreationUserId, CreationDate, CreationTime, FileSize, Locked, LockingUserId, ModificationUserId, ModificationDate, ModificationTime, Description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");

		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(base);
				for(java.io.File file: _files){
					stmt.setString(1, file.getName());
					stmt.setString(2, escapeBackSlash(file.getPath()));
					stmt.setString(3, _userIn);
					stmt.setString(4, date);
					stmt.setString(5, time);
					stmt.setString(6, FileHandler.getFileSize(file));
					stmt.setInt(7, 0);
					stmt.setString(8, "");
					stmt.setString(9, _userIn);
					stmt.setString(10, date);
					stmt.setString(11, time);
					stmt.setString(12, "");
					//Ivy.log().info(stmt.toString());
					insertedIDs+=stmt.executeUpdate();
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}

		return insertedIDs;
	}

	@Override
	public int insertFiles(List<java.io.File> _files, String _destinationPath, String _user) throws Exception{
		int insertedIDs = -1;
		if(_files==null || _files.size()==0)
			return insertedIDs;

		// delete the documents that are already in the DB
		this.deleteFilesInDBOnly(_files);
		String base="INSERT INTO "+this.tableNameSpace+
		" (FileName, FilePath, CreationUserId, CreationDate, CreationTime, FileSize, Locked, LockingUserId, ModificationUserId, ModificationDate, ModificationTime, Description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");
		if(_user==null || _user.trim().equals("")){
			_user= Ivy.session().getSessionUserName();
		}
		if(_destinationPath==null || _destinationPath.trim().length()==0)
		{
			_destinationPath=FileHandler.getFileDirectoryPath(_files.get(0));
		}
		_destinationPath = PathUtil.formatPathForDirectory(_destinationPath);
		IExternalDatabaseRuntimeConnection connection=null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(base);
				for(java.io.File file: _files){
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
					//Ivy.log().info(stmt.toString());
					insertedIDs+=stmt.executeUpdate();
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}

		return insertedIDs;
	}

	/**
	 * delete files from the DB AND File System
	 * @param _files the list of the java.io.File to delete
	 * @return the number of items deleted
	 * @throws Exception
	 */
	public int deleteFiles(List<java.io.File> _files)throws Exception{
		int deletedFiles=0;
		if(_files==null || _files.size()==0)
		{
			return 0;
		}
		IExternalDatabaseRuntimeConnection connection=null;
		String base ="";
		base= "DELETE FROM "+this.tableNameSpace+" WHERE FilePath = ?"; 
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(base);
				for(java.io.File file: _files){
					stmt.setString(1, escapeBackSlash(file.getPath()));
					deletedFiles+=stmt.executeUpdate();
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		FileHandler.deleteFiles(_files);
		return deletedFiles;
	}


	/**
	 * delete files from the DB AND NOT ON File System
	 * @param _files the list of the java.io.File to delete
	 * @return the number of items deleted
	 * @throws Exception
	 */
	public int deleteFilesInDBOnly(List<java.io.File> _files)throws Exception{
		int deletedFiles=0;
		if(_files==null || _files.size()==0)
		{
			return 0;
		}
		IExternalDatabaseRuntimeConnection connection=null;
		String base ="";
		base= "DELETE FROM "+this.tableNameSpace+" WHERE FilePath = ?"; 
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(base);
				for(java.io.File file: _files){
					stmt.setString(1, escapeBackSlash(file.getPath()));
					deletedFiles+=stmt.executeUpdate();
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		//FileHandler.deleteFiles(_files);
		return deletedFiles;
	}

	/**
	 * delete documents from the DB AND File System
	 * @param _documents the list of the DocumentOnServer to delete
	 * @return the number of items deleted
	 * @throws Exception
	 */
	public int deleteDocuments(List<DocumentOnServer> _documents)throws Exception{
		int deletedFiles=0;
		if(_documents==null || _documents.size()==0)
		{
			return 0;
		}
		IExternalDatabaseRuntimeConnection connection=null;
		String base ="";
		base= "DELETE FROM "+this.tableNameSpace+" WHERE FilePath = ?"; 
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(base);
				for(DocumentOnServer doc: _documents){
					stmt.setString(1, escapeBackSlash(doc.getPath()));
					deletedFiles+=stmt.executeUpdate();
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		List<java.io.File> _files = List.create(java.io.File.class);
		for(DocumentOnServer doc:_documents){
			_files.add(new java.io.File(doc.getPath()));
		}
		FileHandler.deleteFiles(_files);
		return deletedFiles;
	}

	/**
	 * delete documents from the DB AND NOT ON THE File System
	 * @param _documents the list of the DocumentOnServer to delete
	 * @return the number of items deleted
	 * @throws Exception
	 */
	public int deleteDocumentsInDBOnly(List<DocumentOnServer> _documents)throws Exception{
		int deletedFiles=0;
		if(_documents==null || _documents.size()==0)
		{
			return 0;
		}
		IExternalDatabaseRuntimeConnection connection=null;
		String base ="";
		base= "DELETE FROM "+this.tableNameSpace+" WHERE FilePath = ?"; 
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(base);
				for(DocumentOnServer doc: _documents){
					stmt.setString(1, escapeBackSlash(doc.getPath()));
					deletedFiles+=stmt.executeUpdate();
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}

		return deletedFiles;
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
	 * get the Class Object of the current AbstractFileManagementHandler implementation Class
	 * @return the class Object of the current AbstractFileManagementHandler implementation Class
	 */
	public Class<? extends AbstractFileManagementHandler> getFileManagementHandlerClass() {
		return this.getClass();
	}

	/**
	 * allows executing a prepareStatement and returns the resulting recordset.<br>
	 * If the preparedStatement execution returns an empty Resultset then the RecordSet will be empty.
	 * The calling method is responsible to give back the preparedStatement with DatabaseUtil.close(stmt);
	 * @param _stmt
	 * @return
	 * @throws Exception
	 
	private static Recordset executeStatement(PreparedStatement _stmt) throws Exception{

		if(_stmt == null){
			throw(new SQLException("Invalid PreparedStatement","PreparedStatement Null"));
		}

		ResultSet rst = null;
		Recordset r= new Recordset();

		rst=_stmt.executeQuery();
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
				r.add(rec);
			}
		}finally
		{
			DatabaseUtil.close(rst);
		}

		return r;
	}*/
	
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

	@Override
	public ReturnedMessage createDirectory(String destinationPath,
			String newDirectoryName) throws Exception {
		if(destinationPath==null || newDirectoryName==null || newDirectoryName.trim().equals(""))
		{
			throw new IllegalArgumentException("One of the parameters in "+this.getClass().getName()+", method createDirectory(String destinationPath, String newDirectoryName) is not set.");
		}

		return this.createDirectory(PathUtil.formatPathForDirectory(destinationPath)+newDirectoryName.trim());

	}
	
	/**
	 * This method throws an UnsupportedOperationException because it only works if the files are stored in a database.
	 */
	@Override
	public FolderOnServer moveDirectory(FolderOnServer folderToMove,
			String destinationParentPath) {
		throw new UnsupportedOperationException("The moveDirectory function is not supported in a context where the files are stored on the server filesystem.");
	}


	@Override
	public ReturnedMessage createDirectory(String _newDirectoryPath) throws Exception
	{
		if(_newDirectoryPath==null ||  _newDirectoryPath.trim().equals(""))
		{
			throw new IllegalArgumentException("One of the parameters in "+this.getClass().getName()+", method createDirectory(String _newDirectoryPath) is not set.");
		}
		if(_newDirectoryPath.contains("%")){
			throw new IllegalArgumentException("The directories name cannot contain a percent sign (%).");
		}
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));

		java.io.File dir = new java.io.File(PathUtil.formatPathForDirectory(_newDirectoryPath));
		if(dir.isDirectory())
		{//already exists
			message.setType(FileHandler.INFORMATION_MESSAGE);
			message.setText("The directory to create already exists.");
		}else
		{
			if(dir.mkdirs())
			{//Creation successful
				message.setType(FileHandler.SUCCESS_MESSAGE);
				message.setText("The directory was successfuly created.");
				message.setFile(dir);
				message.getFiles().add(dir);
			}else
			{
				message.setType(FileHandler.ERROR_MESSAGE);
				message.setText("The directory could not be created.");
			}
		}
		return message;

	}

	@Override
	public ReturnedMessage deleteDirectory(String directoryPath)
	throws Exception {
		if(directoryPath==null || directoryPath.trim().equals(""))
		{
			throw new IllegalArgumentException("The 'directoryPath' parameter in "+this.getClass().getName()+", method deleteDirectory(String directoryPath) is not set.");
		}
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		java.io.File dir = new java.io.File(PathUtil.formatPathForDirectory(directoryPath));
		if(!dir.isDirectory())
		{//Not a directory
			message.setType(FileHandler.INFORMATION_MESSAGE);
			message.setText("The directory to delete does not exist.");
		}else{
			if(deleteDirStructure(dir))
			{
				message.setType(FileHandler.SUCCESS_MESSAGE);
				message.setText("The directory and all its files were successfully deleted.");
			}
		}
		return message;
	}

	@Override
	public ReturnedMessage deleteDirectoryAsAdministrator(String directoryPath)
	throws Exception {
		return this.deleteDirectory(directoryPath);
	}

	/**
	 * Private recursive method to delete all the files and directories contained in a given directory.
	 * @param dir: the java.io.File object rep'resenting the directory to delete
	 * @return true if success else false
	 * @throws Exception
	 */
	private boolean deleteDirStructure(java.io.File dir) throws Exception{
		boolean result = false;
		if(dir!=null && dir.isDirectory())
		{
			//we list all the files and directories contained into the directory to delete
			java.io.File[] files = dir.listFiles();
			if(files!=null)
			{
				for(int i =0; i<files.length; i++)
				{//for each file or directory found
					if(files[i].isDirectory())
					{//in case of a child directory we delete the child directory
						deleteDirStructure(files[i]);
					}else
					{// in case of a file we directly delete the file
						files[i].delete();
					}
				}
				// here the directory should be empty
				result=dir.delete();
			}
		}
		return result;
	}

	@Override
	public ReturnedMessage deleteDocumentOnServer(DocumentOnServer document)
	throws Exception {
		if(document==null)
		{
			throw new IllegalArgumentException("The 'document' parameter in "+this.getClass().getName()+", method deleteDocumentOnServer(DocumentOnServer document) is not set.");
		}
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));

		List<DocumentOnServer> l = List.create(DocumentOnServer.class);
		l.add(document);
		// we delete the document from DB
		this.deleteDocuments(l);
		//We delete the document from fileSystem
		java.io.File file = new java.io.File(document.getPath());
		if(file.delete())
		{
			message.setType(FileHandler.SUCCESS_MESSAGE);
		}else
		{
			message.setType(FileHandler.ERROR_MESSAGE);
		}
		return message;
	}

	@Override
	public ReturnedMessage deleteDocumentOnServers(
			List<DocumentOnServer> documents) throws Exception {

		if(documents==null)
		{
			throw new IllegalArgumentException("The 'documents' parameter in "+this.getClass().getName()+", method deleteDocumentOnServers(List<DocumentOnServer> documents) is not set.");
		}
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		//we delete the documents from DB
		this.deleteDocuments(documents);
		message.setType(FileHandler.SUCCESS_MESSAGE);
		//we delete the documents from file system
		for(DocumentOnServer doc: documents)
		{
			try{
				java.io.File file = new java.io.File(doc.getPath());
				file.delete();
			}catch(Exception _ex){
				Ivy.log().error(_ex.getMessage()+ " In method deleteDocumentOnServers(List<DocumentOnServer> documents) from "+this.getClass().getName());
			}
		}

		return message;
	}

	@Override
	public Tree getDirectoryTree(String rootPath) throws Exception {
		Tree RDTree = new Tree();
		String entryPath = PathUtil.formatPath(rootPath);
		File dir = new File(entryPath);
		if(!dir.exists())
			dir.mkdirs();
		FolderOnServer o = new FolderOnServer();
		o.setName(dir.getName());
		o.setPath(PathUtil.formatPath(dir.getPath()));
		o.setIsRoot(true);
		RDTree.setValue(o);
		RDTree.setInfo(o.getName());
		fillRDTree(entryPath, RDTree);
		return RDTree;
	}

	@Override
	public boolean directoryExists(String _dirPath) throws Exception
	{
		if(_dirPath==null || _dirPath.trim().equals(""))
		{
			return false;
		}
		_dirPath = PathUtil.formatPathForDirectoryWithoutLastSeparator(_dirPath);
		if(new java.io.File(_dirPath).isDirectory()){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Recursive method used by the makeRDTree method
	 * @param entryPoint
	 * @param myTree
	 */
	private static void fillRDTree(String entryPoint, Tree myTree)
	{
		String entryPath = PathUtil.formatPath(entryPoint);
		File dir = new File(entryPath);
		if(!dir.exists() || !dir.isDirectory())
		{
			dir.mkdirs();
			return;
		}
		int j = 0;
		File files[] = dir.listFiles();
		for(int i = 0; i < files.length; i++)
			if(files[i].isDirectory())
				j++;

		if(j == 0)
			return;
		for(int i = 0; i < files.length; i++)
			if(files[i].isDirectory())
			{
				FolderOnServer o = new FolderOnServer();
				o.setName(files[i].getName());
				o.setPath(PathUtil.formatPath(files[i].getPath()));
				myTree.createChild(o, files[i].getName());
				fillRDTree(files[i].getPath(), myTree.getLastChild());
			}

	}

	@Override
	public DocumentOnServer getDocumentOnServer(String filePath)
	throws Exception {
		DocumentOnServer doc = new DocumentOnServer();
		if(filePath==null || filePath.trim().equals("")){
			return doc;
		}
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
			//we take the first one, normally just one
			Record rec = recordList.get(0);

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
			try{
				doc.setExtension(doc.getFilename().substring(doc.getFilename().lastIndexOf(".")+1));
			}catch(Exception ex){
				//Ignore the Exception here
			}

		}
		return doc;
	}
	
	/**
	 * returns the document on Server corresponding to the file id
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public DocumentOnServer getDocumentOnServer(long fileId)
	throws Exception {
		DocumentOnServer doc = new DocumentOnServer();
		if(fileId<=0){
			return doc;
		}
		List<Record> recordList= (List<Record>) List.create(Record.class);
		String query="";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();

			query="SELECT * FROM "+this.tableNameSpace+" WHERE FileId = ?";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileId);
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
			//we take the first one, normally just one
			Record rec = recordList.get(0);

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
			try{
				doc.setExtension(doc.getFilename().substring(doc.getFilename().lastIndexOf(".")+1));
			}catch(Exception ex){
				//Ignore the Exception here
			}
		}
		return doc;
	}

	@Override
	public List<DocumentOnServer> getDocumentOnServersInDirectory(
			String directoryPath, boolean listChildrenDirectories)
			throws Exception {
		List<DocumentOnServer> liste = List.create(DocumentOnServer.class);
		//List<DocumentOnServer> listeOnFS = List.create(DocumentOnServer.class);

		List<DocumentOnServer> docsToDelete = List.create(DocumentOnServer.class);
		if(directoryPath==null || directoryPath.trim().equals("")){
			return liste;
		}

		//we get all the files from the DB - recursive
		ArrayList<DocumentOnServer> listeInDB=this.getDocumentsInPath(directoryPath.trim(), true);

		//we get all the files from the fileSystem
		ArrayList<DocumentOnServer> listeOnFS = FileHandler.getDocumentsInPathAll(directoryPath.trim());

		//we get the files in the directory
		ArrayList<DocumentOnServer> docsInSelectedDir=FileHandler.getDocumentsInDir(new java.io.File(directoryPath));
		if(docsInSelectedDir.size()>0){

			List<DocumentOnServer> docsToAdd = List.create(DocumentOnServer.class);
			//find the files on File System that are not registered in the DB
			docsToAdd.addAll(FileHandler.getDocumentsListDiff(listeInDB,docsInSelectedDir));

			if(docsToAdd.size()>0) {
				this.insertDocuments(docsToAdd);
			}
		}

		//find the files registered in the DB but that are not on the File System
		docsToDelete.addAll(FileHandler.getDocumentsListDiff(listeOnFS,listeInDB));
		//delete theses files
		if(docsToDelete.size()>0) {
			Ivy.log().warn("The following files will be deleted: {0}",docsToDelete.size());
			this.deleteDocuments(docsToDelete);
		}

		liste.addAll(this.getDocumentsInPath(directoryPath.trim(), listChildrenDirectories));
		return liste;
	}



	@Override
	public int getFileStorageType() {
		return AbstractFileManagementHandler.FILE_STORAGE_FILESYSTEM;
	}

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
		newName= PathUtil.formatPathForDirectoryWithoutLastAndFirstSeparator(newName);
		if(newName.contains("/") || newName.contains("\"")){
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/invalidCharacterInDirName"));
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}
		//format the path
		String newPath="";
		path= PathUtil.formatPathForDirectoryWithoutLastSeparator(path);
		//Ivy.log().info("Formatted path: "+path);
		if(path.equals(""))
		{//no valid path was entered ("////" for example)
			message.setText("One of the parameter was invalid for the method renameDirectory in "+this.getClass().getName());
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}
		if(!path.contains("/")){//path is composed just by the directory old name
			newPath=newName;
		}else{//We get the old directory name
			newPath= path.substring(0,path.lastIndexOf("/"))+"/"+newName;
		}
		//look if directory exists
		if(!this.directoryExists(path)){
			message.setText("The directory to rename does not exist.");
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}
		//Select all the files in the dir structure and put the edited one in hashmap
		List<DocumentOnServer> docs = this.getDocumentOnServersInDirectory(path, true);
		message.setHashMapFiles(new HashMap<java.io.File,java.io.File>());
		for (DocumentOnServer doc: docs){
			if(doc.getLocked().equalsIgnoreCase("1")){
				message.getHashMapFiles().put(new java.io.File(doc.getPath()), new java.io.File(doc.getPath().replaceFirst("\\Q"+path+"/"+"\\E", newPath+"/")));
			}
		}
		
		//Check if new path exists
		java.io.File tempDir =null;
		if(this.directoryExists(newPath)){
			//Support for case change in the name
			java.io.File dir = new java.io.File(path);
			boolean exist = false;
			ArrayList<FolderOnServer> lfos = this.getListDirectDirectoriesUnderPath(dir.getParent());
			for(FolderOnServer fos: lfos){
				if(fos.getName().equals(newName)) {
					//the new directory exists really....
					exist=true;
					break;
				}
			}
			boolean b =false;
			if(!exist) {
				tempDir= new java.io.File(path.substring(0,path.lastIndexOf("/"))+"/"+System.nanoTime());
				b = dir.renameTo(tempDir);
			}
			if(!b || exist){
				message.setText("The directory "+newPath+" already exists. You cannot create a duplicate directory.");
				message.setType(FileHandler.ERROR_MESSAGE);
				return message;
			}
		}
		//Now rename the directory
		java.io.File dir = tempDir==null?new java.io.File(path):tempDir;
		if(!dir.renameTo(new java.io.File(newPath))) {
			message.setText("The directory "+path+" cannot be renamed. it is may be already in use by another process.");
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}
		message.getFiles().add(0, new java.io.File(path));
		message.getFiles().add(1, new java.io.File(newPath));
		
		IExternalDatabaseRuntimeConnection connection = null;
		//we update all the contained files
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			String query ="UPDATE "+this.tableNameSpace+" SET FilePath = ? WHERE FileId = ?";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				for (DocumentOnServer doc: docs){
					try{
						String p = path+"/";
						String s = doc.getPath().replaceFirst("\\Q"+ p+"\\E", newPath+"/");
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
		message.setActionType(FolderAction.RENAME);
		return message;
	}

	@Override
	public ReturnedMessage renameDocumentOnServer(DocumentOnServer document,
			String newName) throws Exception {
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		if(document==null || document.getPath().trim().equals("") || document.getFilename().trim().equals("") || newName==null || newName.trim().equals("")){
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText("Unable to rename the given DocumentOnServer. One of the parameter is invalid in renameDocumentOnServer(DocumentOnServer document, String newName) in class "+this.getClass().getName());
			return message;
		}
		java.io.File file = new java.io.File(document.getPath());
		String ext = "."+FileHandler.getFileExtension(document.getPath());
		String newDest ="";
		if(document.getPath().contains("/"))
		{
			newDest= document.getPath().substring(0,document.getPath().lastIndexOf("/")+1)+newName+ext;
		}
		else
		{
			newDest= document.getPath().substring(0,document.getPath().lastIndexOf("\\")+1)+newName+ext;
		}
		java.io.File dest=new java.io.File(newDest);
		if(file.renameTo(dest))
		{
			message.setType(FileHandler.SUCCESS_MESSAGE);
			this.renameDocument(document, newName+ext, Ivy.session().getSessionUserName());
			document.setPath(PathUtil.formatPath(dest.getPath()));
			document.setFilename(newDest);
			message.setDocumentOnServer(document);		
		}else
		{
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/RenameFileAlreadyExists"));
		}

		return message;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#saveDocumentOnServer(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer, java.lang.String)
	 */
	@Override
	public ReturnedMessage saveDocumentOnServer(DocumentOnServer document,
			String fileDestinationPath) throws Exception {
		return this.saveDocumentOnServer(document, fileDestinationPath, true);
	}
	
	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#saveDocumentOnServer(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer, java.lang.String, boolean)
	 */
	@Override
	public ReturnedMessage saveDocumentOnServer(DocumentOnServer document,
			String fileDestinationPath, boolean updateModificationMetaInfos)
			throws Exception {
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));

		message.setType(FileHandler.SUCCESS_MESSAGE);

		if(document==null || document.getPath()==null || document.getPath().trim().equals("")){
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText("Invalid DocumentOnServer object in saveDocumentOnServer method");
			return message;
		}

		if(fileDestinationPath==null || fileDestinationPath.trim().equals("")){
			fileDestinationPath=FileHandler.getFileDirectoryPath(new java.io.File(document.getPath()));
		}

		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");
		String user = Ivy.session().getSessionUserName();
		if(document.getUserID()==null || document.getUserID().trim().equals("")){
			document.setUserID(user);
		}
		if(document.getCreationDate()==null || document.getCreationDate().trim().equals("")){
			document.setCreationDate(date);
		}
		if(document.getCreationTime()==null || document.getCreationTime().trim().equals("")){
			document.setCreationTime(time);
		}
		if(document.getDescription()==null || document.getDescription().trim().equals("")){
			document.setDescription("");
		}
		if(updateModificationMetaInfos || document.getModificationUserID()==null || document.getModificationUserID().trim().equals("")){
			document.setModificationUserID(user);
		}
		if(updateModificationMetaInfos || document.getModificationDate()==null || document.getModificationDate().trim().equals("")){
			document.setModificationDate(date);
		}
		if(updateModificationMetaInfos || document.getModificationTime()==null || document.getModificationTime().trim().equals("")){
			document.setModificationTime(time);
		}

		fileDestinationPath=PathUtil.formatPath(fileDestinationPath);
		String docPath = PathUtil.formatPathForDirectory(FileHandler.getFileDirectoryPath(new java.io.File(document.getPath())))+document.getFilename();
		//Ivy.log().info(fileDestinationPath +" vs "+docPath );
		if(!docPath.equalsIgnoreCase(fileDestinationPath)){//here we move the file
			document.setPath(fileDestinationPath+document.getFilename());
			message = FileHandler.moveFile(new java.io.File(docPath), fileDestinationPath, false);
			//Ivy.log().info("Message after moving : "+message.getText());
		}
		if(message.getType()==FileHandler.SUCCESS_MESSAGE){
			int fileId = 0;
			try{
				fileId = Integer.parseInt(document.getFileID());
			}catch(Exception ex){
				fileId = 0;
			}
			if(fileId==0){
				this.insertOneDocument(document);
			}else{
				List<KeyValuePair> _KVP = List.create(KeyValuePair.class);
				KeyValuePair kvp = new KeyValuePair();

				kvp = new KeyValuePair();
				kvp.setKey("FileName");
				kvp.setValue(document.getFilename());
				_KVP.add(kvp);

				kvp = new KeyValuePair();
				kvp.setKey("FilePath");
				kvp.setValue(document.getPath());
				_KVP.add(kvp);

				kvp = new KeyValuePair();
				kvp.setKey("CreationUserId");
				kvp.setValue(document.getUserID());
				_KVP.add(kvp);

				kvp = new KeyValuePair();
				kvp.setKey("CreationDate");
				kvp.setValue(document.getCreationDate());
				_KVP.add(kvp);

				kvp = new KeyValuePair();
				kvp.setKey("CreationTime");
				kvp.setValue(document.getCreationTime());
				_KVP.add(kvp);

				kvp = new KeyValuePair();
				kvp.setKey("FileSize");
				kvp.setValue(document.getFileSize());
				_KVP.add(kvp);

				kvp = new KeyValuePair();
				kvp.setKey("Locked");
				if(document.getLocked()==null || document.getLocked().equals("0"))
				{
					kvp.setValue(0);
				}
				else{
					kvp.setValue(1);
				}
				_KVP.add(kvp);

				kvp = new KeyValuePair();
				kvp.setKey("LockingUserId");
				kvp.setValue(document.getLockingUserID());
				_KVP.add(kvp);

				kvp = new KeyValuePair();
				kvp.setKey("ModificationUserId");
				kvp.setValue(user);
				_KVP.add(kvp);

				kvp = new KeyValuePair();
				kvp.setKey("ModificationDate");
				kvp.setValue(date);
				_KVP.add(kvp);

				kvp = new KeyValuePair();
				kvp.setKey("ModificationTime");
				kvp.setValue(time);
				_KVP.add(kvp);

				kvp = new KeyValuePair();
				kvp.setKey("Description");
				kvp.setValue(document.getDescription());
				_KVP.add(kvp);

				List<String> _conditions = List.create(String.class);
				String con = "FileId = "+fileId;
				_conditions.add(con);
				this.updateDocuments(_KVP, _conditions);
				message.setDocumentOnServer(document);
				message.setDocumentOnServers(List.create(DocumentOnServer.class));
				message.getDocumentOnServers().add(document);
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
	public ReturnedMessage moveDocumentOnServer(DocumentOnServer doc, String destination) throws Exception
	{
		return this.saveDocumentOnServer(doc, destination);
	}


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
		String dest = PathUtil.formatPathForDirectory(fileDestinationPath);
		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");
		String user="IVYSYSTEM";

		try{
			user = Ivy.session().getSessionUserName();
		}catch(Exception ex)
		{
			//do nothing
		}

		for(DocumentOnServer doc: documents)
		{
			int i = getNextCopiedFileNumber(doc.getFilename(),dest);
			if(i<0)
			{
				continue;
			}
			java.io.File copiedFile = new java.io.File(PathUtil.formatPath(doc.getPath()));
			if(copiedFile.isFile())
			{
				String newFile=dest;
				String ext= FileHandler.getFileExtension(doc.getFilename());
				if(i==0)
				{
					newFile+=doc.getFilename();
				}else
				{
					newFile+=FileHandler.getFileNameWithoutExt(doc.getFilename())+"_Copy"+i+"."+ext;
				}
				FileInputStream fis=null;
				FileOutputStream fos=null;
				try{
					java.io.File pasteFile=new java.io.File(newFile);

					fis = new FileInputStream(copiedFile);
					fos = new FileOutputStream(pasteFile);
					byte b[] = new byte[1024];
					int c=0;
					while((c= fis.read(b)) != -1){
						fos.write(b,0,c);
					}
					message.getFiles().add(pasteFile);
					DocumentOnServer nDoc = new DocumentOnServer();
					nDoc.setCreationDate(date);
					nDoc.setCreationTime(time);
					nDoc.setDescription("Copy of "+doc.getPath());
					nDoc.setExtension(ext);
					nDoc.setFilename(pasteFile.getName());
					nDoc.setFileSize(doc.getFileSize());
					nDoc.setLocked("0");
					nDoc.setLockingUserID("");
					nDoc.setModificationDate(date);
					nDoc.setModificationTime(time);
					nDoc.setModificationUserID(user);
					nDoc.setPath(PathUtil.formatPath(pasteFile.getPath()));
					nDoc.setUserID(user);
					try{
						nDoc.setExtension(nDoc.getFilename().substring(nDoc.getFilename().lastIndexOf(".")+1));
					}catch(Exception ex){
						//Ignore the Exception here
					}
					pasteDocs.add(nDoc);
				}finally
				{
					if(fis!=null)
					{
						fis.close();
					}
					if(fos!=null)
					{
						fos.close();
					}
				}
			}
		}
		insertDocuments(pasteDocs);
		message.getDocumentOnServers().addAll(pasteDocs);
		return message;
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

		dirPath=PathUtil.formatPathForDirectory(dirPath);
		zipName = zipName.endsWith(".zip")?zipName:zipName+".zip";
		java.io.File zip = new java.io.File(dirPath+zipName);
		boolean exists = zip.isFile();

		if(checkIfExists && exists){
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/zipfileAlreadyExistsCannotCreateIt"));
			return message;
		}else if(exists)
		{
			List<java.io.File> _files = List.create(java.io.File.class);
			_files.add(zip);
			this.deleteFiles(_files);
		}

		ArrayList<java.io.File> zipFiles = new ArrayList<java.io.File>();
		for(DocumentOnServer doc: documents)
		{
			zipFiles.add(new java.io.File(doc.getPath()));
		}
		zip=ZipHandler.makeZip(dirPath, zipName, zipFiles);
		if(zip!=null && zip.isFile())
		{
			this.insertFile(zip, Ivy.session().getSessionUserName());
			message.setType(FileHandler.SUCCESS_MESSAGE);
			message.setFile(zip);
		}else{
			this.insertFile(zip, Ivy.session().getSessionUserName());
			message.setType(FileHandler.ERROR_MESSAGE);

		}
		return message;
	}

	@Override
	public boolean deleteFile(String _filepath) throws Exception{

		if(_filepath==null || _filepath.trim().equals(""))
		{
			return false;
		}
		_filepath = PathUtil.formatPath(_filepath);
		java.io.File f = new java.io.File(_filepath);
		if(!f.isFile())
		{
			return false;
		}
		List<java.io.File> files = List.create(java.io.File.class);
		
		return this.deleteFiles(files)==1;
	}

	/**
	 * 
	 * @param fileName
	 * @param dest
	 * @return
	 */
	private int getNextCopiedFileNumber(String fileName, String dest){

		if(fileName==null || fileName.trim().equals("") || dest==null || dest.trim().equals(""))
		{
			return -1;
		}

		if(!new File(dest+fileName).isFile())
		{//the new file doesn't already exist
			return 0;
		}

		String ext= FileHandler.getFileExtension(fileName);
		String n = FileHandler.getFileNameWithoutExt(fileName)+"_Copy";
		boolean exists=true;
		int i=1;
		while(exists)
		{
			if(new File(dest+ n+i+"."+ext).isFile())
			{
				i++;
			}
			else{
				exists=false;
			}
		}

		return i;
	}

	@Override
	public DocumentOnServer getDocumentOnServerWithJavaFile(DocumentOnServer doc)
	throws Exception {
		if(doc==null || doc.getPath()==null || doc.getPath().trim().equals(""))
		{
			return doc;
		}
		String path = PathUtil.formatPath(doc.getPath().trim());
		java.io.File f = new java.io.File(path);
		if(f.isFile())
		{
			doc.setJavaFile(f);
		}else{
			throw new IOException("The file "+doc.getFilename()+" cannot be found on the filesystem in "+doc.getPath());
		}
		return doc;
	}

	@Override
	public DocumentOnServer getDocumentOnServerWithJavaFile(String filePath)
	throws Exception {
		if(filePath==null || filePath.trim().equals(""))
		{
			return null;
		}
		String path = PathUtil.formatPath(filePath.trim());

		DocumentOnServer doc = this.getDocumentOnServer(path);
		return getDocumentOnServerWithJavaFile(doc);

	}
	
	@Override
	public DocumentOnServer getDocumentOnServerWithJavaFile(long fileid)
			throws Exception {
		if(fileid<=0)
		{
			return null;
		}
		DocumentOnServer doc = this.getDocumentOnServer(fileid);
		return getDocumentOnServerWithJavaFile(doc);
	}
	
	@Override
	public DocumentOnServer getDocumentOnServerById(long fileid,
			boolean getJavaFile) throws Exception {
		if(fileid<=0)
		{
			return null;
		}
		DocumentOnServer doc = this.getDocumentOnServer(fileid);
		if(getJavaFile)
		{
			return getDocumentOnServerWithJavaFile(doc);
		}else{
			return doc;
		}
	}

	@Override
	public ArrayList<DocumentOnServer> getDocumentsWithJavaFileInPath(
			String path, boolean isRecursive) throws Exception {
		ArrayList<DocumentOnServer> docs = this.getDocumentsInPath(path, isRecursive);
		ArrayList<DocumentOnServer> documents = new ArrayList<DocumentOnServer>();
		for(DocumentOnServer d : docs){
			java.io.File f = new java.io.File(d.getPath());
			if(f.isFile()){
				d.setJavaFile(f);
				documents.add(d);
			}
		}
		return documents;
	}


	@Override
	public boolean documentOnServerExists(DocumentOnServer document, String path)
	throws Exception {
		if(document== null || document.getFilename()==null || document.getFilename().trim().equals("") || path==null || path.trim().equals(""))
		{
			return false;
		}
		path=PathUtil.formatPathForDirectory(path);
		java.io.File f = new java.io.File(path+document.getFilename().trim());

		return f.isFile();
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
	public boolean isDirectoryEmpty(String directoryPath) throws Exception {
		if(directoryPath==null || directoryPath.trim().equals(""))
		{
			throw new IllegalArgumentException("Illegal directory Path in method isDirectoryEmpty(String directoryPath) in "+this.getClass().toString());
		}
		java.io.File dir = new java.io.File(directoryPath);
		if(dir.isDirectory()){
			return dir.list().length==0;
		}
		return true;
	}

	@Override
	public boolean fileExists(String filePath) throws Exception {
		if(filePath == null || filePath.trim().equals(""))
		{
			return false;
		}
		filePath = PathUtil.formatPath(filePath);		
		return new java.io.File(filePath).isFile();
	}


	@Override
	@Deprecated
	public AbstractDirectorySecurityController getSecurityController()
	throws Exception {

		return null;
	}
	
	@Override
	public SecurityHandler getSecurityHandler() {
		return null;
	}

	@Override
	public int getFile_content_storage_type() {
		return AbstractFileManagementHandler.FILE_STORAGE_FILESYSTEM;
	}


	@Override
	public ArrayList<FolderOnServer> getListDirectDirectoriesUnderPath(String path)
			throws Exception {
		if (path == null || path.trim().length() == 0) {
			throw new IllegalArgumentException(
					"The path argument is null or is an empty String.");
		}
		path = PathUtil.formatPathForDirectory(path);
		java.io.File dir = new java.io.File(path);
		ArrayList<FolderOnServer> fos = new ArrayList<FolderOnServer>();
		if (dir.isDirectory()) {
			String[] files = dir.list();
			for (int i = 0; i < files.length; i++) {
				java.io.File d = new java.io.File(path + files[i]);
				if (d.isDirectory()) {
					FolderOnServer fo = new FolderOnServer();
					fo.setName(files[i]);
					fo.setPath(path + files[i]);
					fos.add(fo);
				}
			}
		}
		return fos;
	}

	public FolderOnServer getDirectoryWithPath(String _directoryPath)
			throws Exception {
		_directoryPath = PathUtil.formatPathForDirectory(_directoryPath);
		java.io.File dir = new java.io.File(_directoryPath);
		FolderOnServer fos = new FolderOnServer();
		if(dir.isDirectory()) {
			fos.setName(dir.getName());
			fos.setPath(PathUtil.formatPathForDirectoryWithoutLastSeparator(_directoryPath));
		}
		return fos;
	}

}
