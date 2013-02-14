/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.filetype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import ch.ivyteam.db.jdbc.DatabaseUtil;
import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileType;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.db.IExternalDatabase;
import ch.ivyteam.ivy.db.IExternalDatabaseApplicationContext;
import ch.ivyteam.ivy.db.IExternalDatabaseRuntimeConnection;
import ch.ivyteam.ivy.environment.EnvironmentNotAvailableException;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.persistence.PersistencyException;
import ch.ivyteam.ivy.scripting.objects.List;

/**
 * @author Emmanuel Comba<br>
 * This class allows managing the file types of document on server objects.<br>
 * It provides also convenient public methods to manage the file type on document on server objects.<br>
 * The fileType Objects are DataClass ch.ivyteam.ivy.addons.filemanager.FileType
 * 
 */
public class FileTypesController {

	private String ivyDBConnectionName = null; // the user friendly connection name to Database in Ivy
	private IExternalDatabase database=null;
	private String tableName = null; // the table that stores file types info
	private String tableNameSpace = null; // equals to tableName if schemaName == null, else schemaName.tableName
	private String schemaName=null;
	private String filesTableNameSpace = null; // this name space contains the files table name


	/**
	 * Instantiates a new FileTypesController with the given BasicConfigurationController Object.<br />
	 * The BasicConfigurationController Object contains all the necessary informations to create a new fileManagerController,<br />
	 * and to connect to the database that stores the file types table.
	 * @param _config BasicConfigurationController Object
	 * @see ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController
	 * @throws Exception
	 */
	public FileTypesController(BasicConfigurationController _config) throws Exception
	{
		if(_config!=null)
		{

			this.setTableName(_config.getFileTypeTableName());
			this.setSchemaName(_config.getDatabaseSchemaName());
			this.ivyDBConnectionName = _config.getIvyDBConnectionName();
			if(_config.getDatabaseSchemaName()!=null && _config.getDatabaseSchemaName().length()>0)
			{
				filesTableNameSpace = this.schemaName+"."+_config.getFilesTableName();
			}else
			{
				filesTableNameSpace = _config.getFilesTableName();
			}
		}
	}

	/**
	 * Creates a new FileType.
	 * @param name: String the name of the file type
	 * @param optionalApplicationName: String the application name for which the file type is created. can be empty or null
	 * @return the newly created FileType object. Its id will be greater than zero.
	 * @throws Exception if any SQL Exceptions occur or the name argument is null or an empty String.<br> 
	 * An SQLException will be also thrown if a FileType with the same name and same applicationName already exists.
	 */
	public FileType createFileType(String name, String optionalApplicationName) throws Exception
	{
		if(name==null || name.trim().length()==0)
		{
			throw new IllegalArgumentException("The name argument in the method createFileType(String name, String optionalApplicationName) is invalid.");
		}
		optionalApplicationName = optionalApplicationName==null?"":optionalApplicationName.trim();
		FileType ft = new FileType();
		String query = "INSERT INTO "+this.tableNameSpace+" (name, appname) VALUES (?,?)";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, name.trim());
				stmt.setString(2, optionalApplicationName);

				stmt.executeUpdate();
				
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		ft = this.getFileTypeWithName(name, optionalApplicationName);
		return ft;
	}

	/**
	 * Modify an existing FileType.<br>
	 * If the given FileType does not exists (its id = 0) then tries to create a new one
	 * @param ft The FilteType to modify. Its filetypeName and applicationName will be taken to modify the FileType with its id.<br>
	 * If the id is 0 then it will try to create a new one.
	 * @return the modified or created FileType object.
	 * @throws Exception if any SQL Exceptions occur or the FileType name is null or an empty String.<br> 
	 * An SQLException will be also thrown if a FileType with the same name and same applicationName already exists.
	 */
	public FileType modifyFileType(FileType ft) throws Exception
	{
		if(ft==null || ft.getFileTypeName()==null || ft.getFileTypeName().trim().length()==0)
		{
			throw new IllegalArgumentException("The FileType argument in the method modifyFileType(FileType ft) is invalid.");

		}
		if(ft.getId()==0)
		{
			return this.createFileType(ft.getFileTypeName(), ft.getApplicationName());
		}
		String query = "UPDATE "+this.tableNameSpace+" SET name =?, appname=? WHERE id = ?";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, ft.getFileTypeName().trim());
				if(ft.getApplicationName()== null)
				{
					stmt.setString(2, "");
				}else{
					stmt.setString(2, ft.getApplicationName().trim());
				}
				stmt.setLong(3, ft.getId());
				stmt.executeUpdate();

			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return ft;
	}
	
	/**
	 * Delete the FileType corresponding to the given id.<br>
	 * <b>You must be sure about what you are doing:</b> <br>
	 * this method deletes the file type <b>BUT ALSO</b> set the fileType id on the associated documents to zero.
	 * @param fileTypeId : the id of the file type to delete
	 * @throws Exception
	 */
	public void deleteFileType(long fileTypeId) throws Exception
	{
		if(fileTypeId ==0)
		{
			return;
		}
		
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				String query = "DELETE FROM "+this.tableNameSpace+" WHERE id = ?";
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileTypeId);
				stmt.executeUpdate();
				
				query ="UPDATE "+this.filesTableNameSpace+" SET filetypeid = 0 WHERE filetypeid = ?";
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileTypeId);
				stmt.executeUpdate();
				
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
	 * Returns the FileType corresponding to the given id.<br>
	 * @param fileTypeId the FileType id
	 * @return
	 * @throws Exception 
	 * @throws SQLException 
	 * @throws PersistencyException 
	 */
	public FileType getFileTypeWithId(long fileTypeId) throws PersistencyException, SQLException, Exception
	{
		FileType ft = new FileType();
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			ft= this.getFileTypeWithId(fileTypeId, jdbcConnection);
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return ft;
	}
	
	/**
	 * Returns the FileType corresponding to the given id.<br>
	 * @param fileTypeId the FileType id
	 * @param con the java.sql.Connection object used to communicate with the database.<br>
	 * <b>IMPORTANT: </b>This method does NOT release this Connection. The method that calls this method has to do it.
	 * @return
	 * @throws Exception 
	 * @throws SQLException 
	 * @throws PersistencyException 
	 */
	public FileType getFileTypeWithId(long fileTypeId, java.sql.Connection con) throws PersistencyException, SQLException, Exception
	{
		if(con==null || con.isClosed())
		{
			throw new IllegalArgumentException("The java.sql.Connection Object is null or closed. Method getFileTypeWithId in FileTypeController.");
		}
		FileType ft = new FileType();
		if(fileTypeId>0)
		{
			String query="SELECT * FROM "+this.tableNameSpace+" WHERE id = ?";
			PreparedStatement stmt = null;
			try{
				stmt = con.prepareStatement(query);
				stmt.setLong(1, fileTypeId);
				ResultSet rst = stmt.executeQuery();
				if(rst.next())
				{
					ft.setId(fileTypeId);
					ft.setFileTypeName(rst.getString("name"));
					ft.setApplicationName(rst.getString("appname"));
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		}
		return ft;
	}


	/**
	 * Search a file type with the given name and optionally the given application name.<br>
	 * If no application name is given, this will return the file type associated with the given name and an empty application name.
	 * File types can be created for several applications and stored in the same table.<br> 
	 * A given file type name must be unique for a given application.<br> File types with the same name can be created for different applications.
	 * @param typeName the name of the fileType to search
	 * @param optionalApplicationName the name of the application (not mandatory)
	 * @return a FileType corresponding to the name and application that you provided as parameters.<br> 
	 * If no file type has the given name and application name, then the returned FileType has empty field values.
	 * @throws PersistencyException
	 * @throws SQLException
	 * @throws Exception
	 */
	public FileType getFileTypeWithName(String typeName, String optionalApplicationName) throws PersistencyException, SQLException, Exception
	{
		FileType ft =new FileType();

		if(typeName != null && typeName.trim().length()!=0)
		{
			String query="";
			IExternalDatabaseRuntimeConnection connection = null;
			optionalApplicationName = optionalApplicationName==null?"":optionalApplicationName.trim();
			try {
				connection = getDatabase().getAndLockConnection();
				Connection jdbcConnection=connection.getDatabaseConnection();

				query="SELECT * FROM "+this.tableNameSpace+" WHERE name LIKE ? AND appname LIKE ?";

				PreparedStatement stmt = null;
				try{
					stmt = jdbcConnection.prepareStatement(query);
					stmt.setString(1, typeName);
					stmt.setString(2, optionalApplicationName);

					ResultSet rst = stmt.executeQuery();
					if(rst.next())
					{
						ft.setId(rst.getLong("id"));
						ft.setFileTypeName(rst.getString("name"));
						ft.setApplicationName(rst.getString("appname"));

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
		return ft;
	}

	/**
	 * returns all the file types that where created for one application.<br>
	 * If the provided applicationName is null or an empty String, it will return all the file types without application name information.
	 * @param applicationName the application name (ex: xcrm, xrec, xjob, epd, ...) or null if you want to get all the file types without application name.
	 * @return java.util.List<FileType> the list of all the fileTypes declared for one application or without any application link if the input parameter was set to null.
	 * @throws PersistencyException
	 * @throws SQLException
	 * @throws Exception
	 */
	public java.util.List<FileType> getFileTypesWithAppName(String applicationName) throws PersistencyException, SQLException, Exception
	{
		List<FileType> ftl = List.create(FileType.class);

		String query="";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			applicationName = applicationName==null?"":applicationName.trim();
			PreparedStatement stmt = null;
			try{
				query="SELECT * FROM "+this.tableNameSpace+" WHERE appname LIKE ?";
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, applicationName);

				ResultSet rst = stmt.executeQuery();
				while(rst.next())
				{
					FileType ft = new FileType();
					ft.setId(rst.getLong("id"));
					ft.setFileTypeName(rst.getString("name"));
					ft.setApplicationName(rst.getString("appname"));
					ftl.add(ft);
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}

		return ftl;
	}

	/**
	 * returns all the file types
	 * @return the list of file types
	 * @throws PersistencyException
	 * @throws SQLException
	 * @throws Exception
	 */
	public java.util.List<FileType> getAllFileTypes() throws PersistencyException, SQLException, Exception
	{
		List<FileType> ftl = List.create(FileType.class);

		String query="";
		IExternalDatabaseRuntimeConnection connection = null;

		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			query="SELECT * FROM "+this.tableNameSpace;

			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);

				ResultSet rst = stmt.executeQuery();
				while(rst.next())
				{
					FileType ft = new FileType();
					ft.setId(rst.getLong("id"));
					ft.setFileTypeName(rst.getString("name"));
					ft.setApplicationName(rst.getString("appname")==null?"":rst.getString("appname"));
					ftl.add(ft);
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}

		return ftl;
	}

	/**
	 * returns all the documentOnServer objects corresponding to the given file type name and application name.<br>
	 * If you do not provide an application name this will return all the documents associated with the given file type and a null value as application name.
	 * @param typeName the type name
	 * @param optionalApplicationName the application name, null is accepted
	 * @return a java.util.List<DocumentOnServer> List of documents that have the corresponding file Type.<br>
	 * Empty if the file type cannot be found or no documents are associated with this type.
	 * @throws Exception
	 */
	public java.util.List<DocumentOnServer> getDocumentsWithFileTypeName(String typeName, String optionalApplicationName) throws Exception
	{
		return this.getDocumentsWithFileTypeId(this.getFileTypeWithName(typeName, optionalApplicationName).getId());
	}

	/**
	 * returns all the documentOnServer objects which FileType has the given applicationName.<br>
	 * If the given applicationName is null or an empty String, the method 
	 * @param applicationName
	 * @return
	 * @throws Exception
	 */
	public java.util.List<DocumentOnServer> getDocumentsWithApplicationName(String applicationName) throws Exception
	{
		List<DocumentOnServer> docs = List.create(DocumentOnServer.class);
		java.util.List<FileType> ftl = this.getFileTypesWithAppName(applicationName);
		for(FileType ft: ftl)
		{
			docs.addAll(this.getDocumentsWithFileTypeId(ft.getId()));
		}
		return docs;
	}

	/**
	 * returns all the DocumentOnServer objects having the given FIleTypeId
	 * @param typeId
	 * @return
	 * @throws Exception
	 */
	public java.util.List<DocumentOnServer> getDocumentsWithFileTypeId(long typeId) throws Exception
	{

		List<DocumentOnServer> docs = List.create(DocumentOnServer.class);
		if(typeId <=0)
		{
			return docs;
		}
		String query="";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			FileType ft = this.getFileTypeWithId(typeId);
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			query="SELECT * FROM "+this.filesTableNameSpace+" WHERE filetypeid = ?";

			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, typeId);
				ResultSet rst = stmt.executeQuery();
				while(rst.next())
				{
					DocumentOnServer doc = new DocumentOnServer();
					doc.setFileID(String.valueOf(rst.getLong("FileId")));
					doc.setFilename(rst.getString("FileName"));
					doc.setPath(rst.getString("FilePath"));
					doc.setFileSize(rst.getString("FileSize"));
					doc.setUserID(rst.getString("CreationUserId"));
					doc.setCreationDate(rst.getString("CreationDate"));
					doc.setCreationTime(rst.getString("CreationTime"));
					doc.setModificationUserID(rst.getString("ModificationUserId"));
					doc.setModificationDate(rst.getString("ModificationDate"));
					doc.setModificationTime(rst.getString("ModificationTime"));
					doc.setLocked(rst.getString("Locked"));
					doc.setLockingUserID(rst.getString("LockingUserId"));
					doc.setDescription(rst.getString("Description"));
					doc.setFileType(ft);
					docs.add(doc);
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return docs;
	}

	/**
	 * 
	 * @param _docs
	 * @return
	 */
	public java.util.List<DocumentOnServer> completeDocumentsWithFileTypes(java.util.List<DocumentOnServer> _docs)
	{
		if(_docs==null)
		{
			return null;
		}
		for(DocumentOnServer doc : _docs)
		{
			if(doc.getFileType().getId()>0)
			{
				try{
					doc.setFileType(this.getFileTypeWithId(doc.getFileType().getId()));
				}catch(Exception ex)
				{
					//do nothing
				}
			}
		}
		return _docs;
	}

	/**
	 * Set the fileType on given documentOnServer Object. The only needed information in the DocumentOnServer is its FileId attribute.
	 * If the given fileTypeId is 0, then the DocumentOnServer object will not be associated with any FileType.<br>
	 * If no FileType corresponds to the given fileTypeId, then the DocumentOnServer object will not be associated with any FileType.
	 * @param doc : the DocumentOnServer object
	 * @param fileTypeId: the fileTypeId
	 * @return the DocumentOnServer whose FileType attribute has been updated
	 * @throws Exception
	 */
	public DocumentOnServer setDocumentFileType(DocumentOnServer doc, long fileTypeId) throws Exception
	{
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			this.setDocumentFileType(doc, fileTypeId, jdbcConnection);
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return doc;
	}
	
	/**
	 * Set the fileType on given documentOnServer Object. The only needed information in the DocumentOnServer is its FileId attribute.
	 * If the given fileTypeId is 0, then the DocumentOnServer object will not be associated with any FileType.<br>
	 * If no FileType corresponds to the given fileTypeId, then the DocumentOnServer object will not be associated with any FileType.
	 * @param doc : the DocumentOnServer object
	 * @param fileTypeId: the fileTypeId
	 * @param con the java.sql.Connection object used to communicate with the database.<br>
	 * <b>IMPORTANT: </b>This method does NOT release this Connection. The method that calls this method has to do it.
	 * @return the DocumentOnServer whose FileType attribute has been updated
	 * @throws Exception
	 */
	public DocumentOnServer setDocumentFileType(DocumentOnServer doc, long fileTypeId, java.sql.Connection con) throws Exception
	{
		if(con==null || con.isClosed())
		{
			throw new IllegalArgumentException("The java.sql.Connection Object is null or closed. Method setDocumentFileType in FileTypeController.");
		}
		if(doc==null || Long.parseLong(doc.getFileID())==0)
		{
			throw new IllegalArgumentException("The documentOnServer argument is invalid in setDocumentFileType(DocumentOnServer doc, long fileTypeId) method.");
		}
		String query="";

		FileType ft = fileTypeId>0?this.getFileTypeWithId(fileTypeId,con):null;
		query="UPDATE "+this.filesTableNameSpace+" SET filetypeid = ? WHERE FileId = ?";

		PreparedStatement stmt = null;
		try{
			stmt = con.prepareStatement(query);
			stmt.setLong(1, fileTypeId);
			stmt.setLong(2, Long.parseLong(doc.getFileID()));
			stmt.executeUpdate();
			doc.setFileType(ft);
		}finally{
			DatabaseUtil.close(stmt);
		}
		return doc;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
		if(this.schemaName==null || this.schemaName.length()==0)
		{
			this.tableNameSpace=this.tableName;
		}else{
			this.tableNameSpace=this.schemaName+"."+this.tableName;
		}
	}

	/**
	 * @return the schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * @param schemaName the schemaName to set
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
		if(this.schemaName==null || this.schemaName.length()==0)
		{
			this.tableNameSpace=this.tableName;
		}else{
			this.tableNameSpace=this.schemaName+"."+this.tableName;
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
		if(this.database==null){
			final String _nameOfTheDatabaseConnection = this.ivyDBConnectionName;
			this.database = Ivy.session().getSecurityContext().executeAsSystemUser(new Callable<IExternalDatabase>(){
				public IExternalDatabase call() throws Exception {
					IExternalDatabaseApplicationContext context = (IExternalDatabaseApplicationContext)Ivy.wf().getApplication().getAdapter(IExternalDatabaseApplicationContext.class);
					return context.getExternalDatabase(_nameOfTheDatabaseConnection);
				}
			});
		}
		return this.database;	
	}
}
