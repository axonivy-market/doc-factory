/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.filetag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.Callable;

import ch.ivyteam.db.jdbc.DatabaseUtil;
import ch.ivyteam.ivy.addons.filemanager.FileTag;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.db.IExternalDatabase;
import ch.ivyteam.ivy.db.IExternalDatabaseApplicationContext;
import ch.ivyteam.ivy.db.IExternalDatabaseRuntimeConnection;
import ch.ivyteam.ivy.environment.EnvironmentNotAvailableException;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.List;

/**
 * @author ec
 *
 */
public class FileTagsController {
	
	private String ivyDBConnectionName = null; // the user friendly connection name to Database in Ivy
	private IExternalDatabase database=null;
	private String tableName = null; // the table that stores file tags info
	private String tableNameSpace = null; // equals to tableName if schemaName == null, else schemaName.tableName
	private String schemaName=null;
	private String filesTableNameSpace = null; // this name space contains the files table name
	
	/**
	 * 
	 * @param _config
	 */
	public FileTagsController (BasicConfigurationController _config)
	{
		if(_config!=null)
		{

			this.setTableName(_config.getFileTagsTableName());
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
	 * creates a new Tag for the document corresponding to the given id (fileId).<br>
	 * The fileId - tag value association must be unique. So you cannot create two tags for a file with the same value.
	 * @param fileId: the id of the file that will be associated with the tag
	 * @param tag: the tag as String
	 * @return the new Tag. 
	 * @throws Exception If the fileId is not a positive number above zero or the tag value is empty. Also Exception if any SQL Exception is thrown.
	 */
	public FileTag createTag(long fileId, String tag) throws Exception
	{
		if(fileId <=0 || tag==null || tag.trim().length()==0)
		{
			throw new IllegalArgumentException("Input parameter not valid in createTag method.");
		}
		FileTag ft = new FileTag();
		String query ="INSERT INTO "+this.tableNameSpace+" (fileid,tag) VALUES (?,?)";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileId);
				stmt.setString(2, tag.trim());

				stmt.executeUpdate();
				ft = this.getTag(fileId,tag.trim());
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
	 * modify an existing tag.<br>
	 * @param tagId the id of the tag to be modified
	 * @param tag the new String value of the tag
	 * @return the modified FileTag object
	 * @throws Exception If the tagId is not a positive number above zero or the tag value is empty. Also Exception if any SQL Exception is thrown.
	 */
	public FileTag modifyTag(long tagId, String tag) throws Exception 
	{
		if(tagId <=0 || tag==null || tag.trim().length()==0)
		{
			throw new IllegalArgumentException("Input parameter not valid in modifyTag method.");
		}
		FileTag ft = new FileTag();
		String query = "UPDATE "+this.tableNameSpace+" SET tag =? WHERE id = ?";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setString(1, tag.trim());
				stmt.setLong(2, tagId);
				stmt.executeUpdate();
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		ft = this.getTagById(tagId);
		return ft;
	}
	
	/**
	 * deletes the tag corresponding to the given id
	 * @param tagId
	 * @throws Exception
	 */
	public void deleteTag(long tagId) throws Exception
	{
		if(tagId <=0)
		{
			throw new IllegalArgumentException("Input parameter not valid in deleteTag method.");
		}
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				String query = "DELETE FROM "+this.tableNameSpace+" WHERE id = ?";
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, tagId);
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
	 * Returns a precise Tag for a document. Here the given tag must match exactly the file tag stored value.
	 * @param fileId: the file id corresponding to the document.
	 * @param tag: the tag value.
	 * @return the found ch.ivyteam.ivy.addons.filemanager.FileTag object. This object is empty if no corresponding tag was found.
	 * @throws Exception If the fileId is not a positive number above zero or the tag value is empty. Also Exception if any SQL Exception is thrown.
	 */
	public FileTag getTag(long fileId, String tag) throws Exception
	{
		if(fileId ==0 || tag==null || tag.trim().length()==0)
		{
			throw new IllegalArgumentException("Input parameter not valid in getTag method.");
		}
		FileTag ft = new FileTag();
		String query ="SELECT * FROM "+this.tableNameSpace+" WHERE fileid = ? AND tag = ?";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileId);
				stmt.setString(2, tag);

				ResultSet rst = stmt.executeQuery();
				if(rst.next())
				{
					ft.setId(rst.getLong("id"));
					ft.setFileId(rst.getLong("fileid"));
					ft.setTag(rst.getString("tag").trim());
				}
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
	 * Returns the Tag corresponding to the given tag id.
	 * @param tagId the tag id to be returned
	 * @return the FileTag object found. this object is empty if no 
	 * @throws Exception
	 */
	public FileTag getTagById(long tagId) throws Exception
	{
		if(tagId ==0)
		{
			throw new IllegalArgumentException("Input parameter not valid in getTag method.");
		}
		FileTag ft = new FileTag();
		String query ="SELECT * FROM "+this.tableNameSpace+" WHERE id = ? ";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, tagId);

				ResultSet rst = stmt.executeQuery();
				if(rst.next())
				{
					ft.setId(rst.getLong("id"));
					ft.setFileId(rst.getLong("fileid"));
					ft.setTag(rst.getString("tag").trim());
				}
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
	 * Returns a list of FileTag objects that match the given tag pattern.<br>
	 * If the fileId parameter is greater than zero, then only the FileTag objects from the corresponding document will be returned.<br>
	 * If the fileId parameter is zero, then all the tags matching the pattern will be returned, regardless which document they belong to.
	 * @param fileId: optional, the id of the document.
	 * @param tagPattern: the tag pattern to be searched
	 * @return a java.util.list of FileTag objects that match the given tag pattern
	 * @throws Exception If the pattern tag parameter value is empty. Also Exception if any SQL Exception is thrown.
	 */
	public java.util.List<FileTag> getTagsWithPattern(long fileId, String tagPattern) throws Exception
	{
		List<FileTag> tags = List.create(FileTag.class);
		if(tagPattern == null || tagPattern.trim().length()==0)
		{
			throw new IllegalArgumentException("Input parameter not valid in getTagsWithPattern method.");
		}
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				if(fileId<=0)
				{
					String query ="SELECT * FROM "+this.tableNameSpace+" WHERE tag LIKE ?";
					stmt = jdbcConnection.prepareStatement(query);
					stmt.setString(1, "%"+tagPattern+"%");
				}else{
					String query ="SELECT * FROM "+this.tableNameSpace+" WHERE fileid=? AND tag LIKE ?";
					stmt = jdbcConnection.prepareStatement(query);
					stmt.setLong(1, fileId);
					stmt.setString(2, "%"+tagPattern+"%");
				}
				

				ResultSet rst = stmt.executeQuery();
				while(rst.next())
				{
					FileTag ft = new FileTag();
					ft.setId(rst.getLong("id"));
					ft.setFileId(rst.getLong("fileid"));
					ft.setTag(rst.getString("tag").trim());
					tags.add(ft);
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return tags;
	}
	
	/**
	 * Returns all the tags concerning a file
	 * @param fileId the id of the file
	 * @return a java.util.list of FileTag objects that belong to the file
	 * @throws Exception Exception If the fileId parameter <= 0. Also Exception if any SQL Exception is thrown.
	 */
	public java.util.List<FileTag> getFileTags(long fileId) throws Exception
	{
		List<FileTag> tags = List.create(FileTag.class);
		if(fileId<=0)
		{
			throw new IllegalArgumentException("Input parameter not valid in getFileTags method.");
		}
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{
				String query ="SELECT * FROM "+this.tableNameSpace+" WHERE fileid= ?";
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileId);
				ResultSet rst = stmt.executeQuery();
				while(rst.next())
				{
					FileTag ft = new FileTag();
					ft.setId(rst.getLong("id"));
					ft.setFileId(rst.getLong("fileid"));
					ft.setTag(rst.getString("tag").trim());
					tags.add(ft);
				}
			}finally{
				DatabaseUtil.close(stmt);
			}
		} finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return tags;
	}
	
	/**
	 * @return the ivyDBConnectionName
	 */
	public String getIvyDBConnectionName() {
		return ivyDBConnectionName;
	}
	/**
	 * @param ivyDBConnectionName the ivyDBConnectionName to set
	 */
	public void setIvyDBConnectionName(String ivyDBConnectionName) {
		this.ivyDBConnectionName = ivyDBConnectionName;
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
	 * @return the filesTableNameSpace
	 */
	public String getFilesTableNameSpace() {
		return filesTableNameSpace;
	}
	/**
	 * @param filesTableNameSpace the filesTableNameSpace to set
	 */
	public void setFilesTableNameSpace(String filesTableNameSpace) {
		this.filesTableNameSpace = filesTableNameSpace;
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
