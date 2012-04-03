/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.versioning;

import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import ch.ivyteam.db.jdbc.DatabaseUtil;
import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.FileVersion;
import ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler;

import ch.ivyteam.ivy.db.IExternalDatabase;
import ch.ivyteam.ivy.db.IExternalDatabaseApplicationContext;
import ch.ivyteam.ivy.db.IExternalDatabaseRuntimeConnection;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;
import ch.ivyteam.ivy.scripting.objects.Record;
import ch.ivyteam.ivy.scripting.objects.Recordset;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.Time;

/**
 * @author ec
 *
 */
public class FileVersioningController {

	private String ivyDBConnectionName = null;
	private String fileTableName=null;
	private String fileContentTableName= null;
	private String fileVersionTableName=null;
	private String fileVersionContentTableName=null;
	private String databaseSchemaName=null;
	private IExternalDatabase database;

	/**
	 * creates a new FileVersioningController object.<br />
	 * All the parameters for the Database connections are going to retrieved from the ivy global variables.
	 * 
	 */
	public FileVersioningController()
	{
		this.databaseSchemaName = Ivy.var().get("xivy_addons_fileManager_databaseSchemaName");
		this.ivyDBConnectionName=Ivy.var().get("xivy_addons_fileManager_ivyDatabaseConnectionName");
		if(databaseSchemaName!=null && databaseSchemaName.trim().length()>0)
		{
			this.fileTableName=this.databaseSchemaName+"."+Ivy.var().get("xivy_addons_fileManager_fileMetaDataTableName");
			this.fileContentTableName=this.databaseSchemaName+"."+Ivy.var().get("xivy_addons_fileManager_fileContentTableName");
			this.fileVersionTableName=this.databaseSchemaName+"."+Ivy.var().get("xivy_addons_fileManager_fileVersioningMetaDataTableName");
			this.fileVersionContentTableName=this.databaseSchemaName+"."+Ivy.var().get("xivy_addons_fileManager_fileVersioningContentTableName");
		}else
		{
			this.fileTableName=Ivy.var().get("xivy_addons_fileManager_fileMetaDataTableName");
			this.fileContentTableName=Ivy.var().get("xivy_addons_fileManager_fileContentTableName");
			this.fileVersionTableName=Ivy.var().get("xivy_addons_fileManager_fileVersioningMetaDataTableName");
			this.fileVersionContentTableName=Ivy.var().get("xivy_addons_fileManager_fileVersioningContentTableName");
		}
	}

	/**
	 * creates a new FileVersioningController object.<br />
	 * You can fine tunes the settings with the input parameters.<br />
	 * If any parameter is null or is an empty String, it will be set with its ivy global variable.
	 * @param _ivyDBConnectionName
	 * @param _fileTableName
	 * @param _fileContentTableName
	 * @param _fileVersionTableName
	 * @param _fileVersionContentTableName
	 * @param _schemaName
	 */
	public FileVersioningController(String _ivyDBConnectionName, String _fileTableName, 
			String _fileContentTableName, String _fileVersionTableName, String _fileVersionContentTableName,
			String _schemaName)
	{
		if(_ivyDBConnectionName == null || _ivyDBConnectionName.trim().length()==0){
			this.ivyDBConnectionName=Ivy.var().get("xivy_addons_fileManager_ivyDatabaseConnectionName");
		}else{
			this.ivyDBConnectionName= _ivyDBConnectionName.trim();
		}

		if(_schemaName==null || _schemaName.trim().length()==0)
		{
			this.databaseSchemaName = Ivy.var().get("xivy_addons_fileManager_databaseSchemaName");
		}else{
			this.databaseSchemaName =_schemaName.trim();
		}

		if(_fileTableName==null || _fileTableName.trim().length()==0)
		{
			this.fileTableName=Ivy.var().get("xivy_addons_fileManager_fileMetaDataTableName");
		}else{
			this.fileTableName=_fileTableName.trim();
		}

		if(_fileContentTableName==null || _fileContentTableName.trim().length()==0)
		{
			this.fileContentTableName=Ivy.var().get("xivy_addons_fileManager_fileContentTableName");
		}else{
			this.fileContentTableName=_fileContentTableName.trim();
		}

		if(_fileVersionTableName==null || _fileVersionTableName.trim().length()==0){
			this.fileVersionTableName=Ivy.var().get("xivy_addons_fileManager_fileVersioningMetaDataTableName");
		}else{
			this.fileVersionTableName=_fileVersionTableName.trim();
		}

		if(_fileVersionContentTableName==null || _fileVersionContentTableName.trim().length()==0){
			this.fileVersionContentTableName=Ivy.var().get("xivy_addons_fileManager_fileVersioningContentTableName");
		}else{
			this.fileVersionContentTableName=_fileVersionContentTableName.trim();
		}

		if(databaseSchemaName!=null && databaseSchemaName.trim().length()>0)
		{
			this.fileTableName=this.databaseSchemaName+"."+this.fileTableName;
			this.fileContentTableName=this.databaseSchemaName+"."+this.fileContentTableName;
			this.fileVersionTableName=this.databaseSchemaName+"."+this.fileVersionTableName;
			this.fileVersionContentTableName=this.databaseSchemaName+"."+this.fileVersionContentTableName;
		}
	}

	/**
	 * Returns the versions of a given file from the file management system.<br />
	 * The returned FileVersion Objects do not contain any File Content.<br />
	 * This method is designed to display such a list to a user for example.<br />
	 * The List is returned with the last created File version at first, and the very first version at the end. 
	 * @param fileId the id of the file as stored in the table whose name is defined in the ivy var: "xivy_addons_fileManager_fileMetaDataTableName"
	 * @return an empty list if the given fileID < 0 or if no version exists for the file.
	 * @throws Exception in case of a Persistence or SQLException
	 */
	public List<FileVersion> getFileVersions(long fileId) throws Exception
	{
		List<FileVersion> l = new ArrayList<FileVersion>();
		if(fileId<0)
		{
			return l;
		}

		String query = "SELECT * FROM "+this.fileVersionTableName+" WHERE file_id=? ORDER BY version_number DESC";
		IExternalDatabaseRuntimeConnection connection = null;
		Recordset rset = null;
		List<Record> recordList= new ArrayList<Record>();
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{			
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileId);
				rset=executeStatement(stmt);
				recordList=rset.toList();
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
		if(recordList!=null){
			for(Record rec: recordList){
				try{
					FileVersion fv = new FileVersion();
					fv.setId(Long.parseLong(rec.getField("versionid").toString()));
					fv.setFileid(fileId);
					fv.setVersionNumber(Integer.parseInt(rec.getField("version_number").toString()));
					fv.setVersionContentId(Long.parseLong(rec.getField("fvc_id").toString()));
					fv.setDate(new Date(rec.getField("cdate").toString()));
					try{
						if(rec.getField("ctime").toString().length()>8)
						{
							fv.setTime(new Time(rec.getField("ctime").toString().substring(0,8)));
						}
						else{
							fv.setTime(new Time(rec.getField("ctime").toString()));
						}
					}catch(Exception ex)
					{
						fv.setTime(new Time("00:00:00"));
					}

					fv.setUser(rec.getField("cuser").toString());
					fv.setFilename(rec.getField("file_name").toString());
					l.add(fv);
				}catch(Exception ex)
				{
					Ivy.log().error("An error occurred while parsing the values of a file version from the table. Correspondinmg file id = "+fileId, ex);
				}
			}
		}
		return l;
	}

	/**
	 * Returns the next version number for a given file denoted by its id.<br />
	 * if there are no versions for the file, the returned number will be 1.
	 * @param fileId fileId the id of the file as stored in the table whose name is defined in the ivy var: "xivy_addons_fileManager_fileMetaDataTableName"
	 * @return the next file version number or -1 if the fileId <=0
	 * @throws Exception in case of a Persistence or SQLException
	 */
	public int getNextVersionNumberForFile(long fileId) throws Exception
	{
		if(fileId <=0)
		{
			return -1;
		}
		int nvn = 1;
		String query = "SELECT version_number FROM "+this.fileVersionTableName+" WHERE file_id=?";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{			
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileId);
				nvn += executeStatement(stmt).size();
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
		return nvn;
	}

	/**
	 * Creates a new version based on the actual file stored in the xivy_addons_fileManager_fileMetaDataTableName table
	 * @param fileId the file ID that has to be versioned
	 * @return the new FileVersion object with its id. The Object does not contain any content. null if the given fileId <=0
	 * @throws Exception in case of a Persistence or SQLException
	 */
	public FileVersion createNewVersion(long fileId) throws Exception
	{
		//Ivy.persistence().get("").createEntityManager();
		if(fileId<=0)
		{
			return null;
		}
		FileVersion fv = null;
		//get the next version number	

		//the new id of the version content
		long vcid=0;
		//the new id of the version 
		long vid = 0;
		//get the DocumentOnServer
		DocumentOnServer doc = this.getParentFileWithoutContent(fileId);
		int vn = doc.getVersionnumber().intValue();
		//Query to insert the new version
		String q1= "INSERT INTO "+this.fileVersionTableName+" (file_id,version_number,cdate,ctime,cuser,file_name) VALUES (?,?,?,?,?,?)";
		//Query to insert the new Version Content
		String q2= "INSERT INTO "+this.fileVersionContentTableName+" (content, version_id) SELECT file_content, ? FROM "+this.fileContentTableName+" WHERE file_id = ?";
		//Query to update the new  file version  "file version content Id" field
		String q3= "UPDATE "+this.fileVersionTableName+" SET fvc_id=? WHERE versionid=?";
		//Query to update the number version in the main file table
		String q4="UPDATE "+this.fileTableName+" SET versionnumber=?, creationdate = ?, creationtime = ?, " +
		"creationuserid = ?, modificationdate = ?, modificationtime = ?, modificationuserid = ?  WHERE fileid=?";
		IExternalDatabaseRuntimeConnection connection = null;

		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{			
				boolean flag = true;
				//Insert first the new version
				try{
					stmt = jdbcConnection.prepareStatement(q1, PreparedStatement.RETURN_GENERATED_KEYS);
				}catch(SQLFeatureNotSupportedException fex)
				{//The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS
					stmt = jdbcConnection.prepareStatement(q1);
					flag=false;
				}
				Date da = new Date(doc.getCreationDate());

				DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
				stmt.setLong(1, fileId);
				stmt.setInt(2, vn);
				stmt.setDate(3, new java.sql.Date(da.toJavaDate().getTime()));
				stmt.setTime(4, new java.sql.Time(sdf.parse(doc.getCreationTime()).getTime()));
				stmt.setString(5, doc.getUserID());
				stmt.setString(6, doc.getFilename());
				stmt.executeUpdate();
				ResultSet rs=null;
				try{
					rs = stmt.getGeneratedKeys();
				}catch(SQLFeatureNotSupportedException fex)
				{//The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS
					//ignore
				}

				if ( rs!=null && rs.next() ) {
					// Retrieve the auto generated key.
					vid= rs.getInt(1);
				}
				if(!flag || vid<=0)
				{//The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS
					//we have to get the inserted Id manually....
					vid= this.getFileVersionWithParentFileIdAndVersionNumber(fileId, vn).getId();
				}

				// insert the new version content
				flag = true;
				try{
					stmt = jdbcConnection.prepareStatement(q2, PreparedStatement.RETURN_GENERATED_KEYS);
				}catch(SQLFeatureNotSupportedException fex)
				{//The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS
					stmt = jdbcConnection.prepareStatement(q2);
					flag=false;
				}

				stmt.setLong(1, vid);
				stmt.setLong(2, fileId);
				stmt.executeUpdate();
				rs=null;
				try{
					rs = stmt.getGeneratedKeys();
				}catch(SQLFeatureNotSupportedException fex)
				{//The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS
					//ignore
				}
				if ( rs!=null && rs.next() ) {
					// Retrieve the auto generated key.
					vcid= rs.getInt(1);
				}
				if(!flag || vcid<=0)
				{//The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS
					//we have to get the inserted Id manually....
					vcid= this.getLastInsertedFileVersionContent(vid);
				}

				//update the new  file version  "file version content Id" field
				stmt = jdbcConnection.prepareStatement(q3);
				stmt.setLong(1, vcid);
				stmt.setLong(2, vid);
				stmt.executeUpdate();

				// update the version number in the main file table
				stmt=jdbcConnection.prepareStatement(q4);
				vn+=1;//increment the new version number of the main file
				Date d = new Date();
				Time t = new Time();
				stmt.setInt(1, vn);
				stmt.setString(2, d.format("dd.MM.yyyy"));
				stmt.setString(3, t.format("HH:mm:ss"));
				stmt.setString(4, Ivy.session().getSessionUserName());
				stmt.setString(5, d.format("dd.MM.yyyy"));
				stmt.setString(6, t.format("HH:mm:ss"));
				stmt.setString(7, Ivy.session().getSessionUserName());
				stmt.setLong(8, fileId);
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
		return fv;
	}

	/**
	 * returns a DocumentOnServer Object with its Id. 
	 * The DocumentOnServer Object Type is the parent of the FileVersion. It represents a File on the Server.
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public DocumentOnServer getParentFileWithoutContent(long fileId) throws Exception{

		if(fileId<=0){
			return null;
		}
		DocumentOnServer doc = new DocumentOnServer();
		Recordset rset = null;
		List<Record> recordList= new ArrayList<Record>();
		String query="";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();

			query="SELECT * FROM "+this.fileTableName+" WHERE fileid = ?";
			PreparedStatement stmt = null;
			try{
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileId);
				rset=executeStatement(stmt);
				recordList=rset.toList();
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
			doc.setLockingUserID(rec.getField("LockingUserId").toString());
			doc.setDescription(rec.getField("Description").toString());
			int vn = 1;
			try{
				vn = Integer.parseInt(rec.getField("versionnumber").toString());
			}catch(Exception ex)
			{
				vn = 1;
			}
			doc.setVersionnumber(vn);
			try{
				doc.setExtension(doc.getFilename().substring(doc.getFilename().lastIndexOf(".")+1));
			}catch(Exception ex){
				//Ignore the Exception here
			}


		}
		return doc;
	}

	/**
	 * Returns the FileVersion object corresponding to the parent fileID and to the versionNumber.<br />
	 * This object does not contain any content or java.io.File reference.<br />
	 * @see the getFileVersionWithJavaFile method.
	 * @param fileId the parent file ID contained in the table denoted by the Ivy var: xivy_addons_fileManager_fileMetaDataTableName.<br />
	 * @param versionNumber the version number to be retrieved
	 * @return the FileVersion object or null if problem occurred
	 * @throws Exception in case of a Persistence or SQLException
	 */
	public FileVersion getFileVersionWithParentFileIdAndVersionNumber(long fileId, int versionNumber) throws Exception
	{
		FileVersion fv = null;
		if(fileId<=0 || versionNumber<=0)
		{
			return fv;
		}
		String query = "SELECT * FROM "+this.fileVersionTableName+" WHERE file_id=? AND version_number=?";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			Recordset rset = null;
			List<Record> recordList= new ArrayList<Record>();
			try{			
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileId);
				stmt.setInt(2, versionNumber);
				rset = executeStatement(stmt);
				recordList = rset.toList();
				if(recordList.size()>0)
				{
					Record rec = recordList.get(0);
					fv=new FileVersion();
					fv.setId(Long.parseLong(rec.getField("id").toString()));
					fv.setFileid(fileId);
					fv.setVersionNumber(versionNumber);
					fv.setVersionContentId(Long.parseLong(rec.getField("fvc_id").toString()));
					fv.setDate(new Date(rec.getField("cdate").toString()));
					fv.setTime(new Time(rec.getField("ctime").toString()));
					fv.setUser(rec.getField("cuser").toString());
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
		return fv;
	}

	/**
	 * Allows extracting all the versions related to a specified FileID.<br />
	 * The extracted files are going to be stored to the given path.<br />
	 * The versions files names ends with "#"+version number.<br />
	 * For example, the versions of example.doc are going to be extracted as example#1.doc, example#2.doc etc...<br />
	 * This method returns a List of extracted FileVersion objects. Each FileVersion contains a reference to its extracted java.io.File in its javaFile field.
	 * @param parentFileId: the fileID which versions have to be extracted.
	 * @param _path: the path where to extract the files on the server.
	 * @return List of extracted FileVersion objects.
	 * @throws Exception in case of a Persistence or SQLException, or if the parameters are not valid (Id <=0 or path is null or empty String).
	 */
	public List<FileVersion> extractVersionsToPath(long parentFileId, String _path) throws Exception
	{
		if(parentFileId<=0 || _path==null || _path.trim().length()==0)
		{
			throw new Exception("Invalid paramaters in the method extractVersionsToPath in "+this.getClass().getName());
		}
		java.io.File dir = new java.io.File(_path);
		if(!dir.exists())
		{
			if(!dir.mkdirs())
			{
				throw new Exception("Exception method extractVersionsToPath in "+this.getClass().getName()+". Unpossible to create directory for following path: "+_path);
			}
		}
		if(!dir.isDirectory())
		{
			throw new Exception("Exception method extractVersionsToPath in "+this.getClass().getName()+". The following path: "+_path+" is not a valid diectory path.");
		}
		
		ArrayList<FileVersion> versions = new ArrayList<FileVersion>();
		
		//get the versions in the versions tables
		String q1="SELECT * FROM "+this.fileVersionTableName+
		" INNER JOIN "+this.fileVersionContentTableName+" ON "+this.fileVersionTableName+".fvc_id = "+this.fileVersionContentTableName+".fvcid" +
		" WHERE "+this.fileVersionTableName+".file_id = ? ";

		// get the actual version from the 
		String q2="SELECT * FROM "+this.fileTableName+
		" INNER JOIN "+this.fileContentTableName+" ON "+this.fileTableName+".fileid = "+this.fileContentTableName+".file_id" +
		" WHERE "+this.fileTableName+".fileid = ? ";

		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			String path = AbstractFileManagementHandler.formatPathForDirectory(dir.getPath());
			try{			
				stmt = jdbcConnection.prepareStatement(q1);
				stmt.setLong(1, parentFileId);
				ResultSet rst = stmt.executeQuery();
				while(rst.next())
				{
					FileVersion fv=new FileVersion();
					fv.setId(rst.getLong("versionid"));
					fv.setFileid(rst.getLong("file_id"));
					fv.setVersionNumber(rst.getInt("version_number"));
					fv.setVersionContentId(rst.getLong("fvc_id"));
					fv.setDate(new Date(rst.getDate("cdate")));
					try{
						if(rst.getTime("ctime").toString().length()>8)
						{
							fv.setTime(new Time(rst.getTime("ctime").toString().substring(0,8)));
						}
						else{
							fv.setTime(new Time(rst.getTime("ctime").toString()));
						}
					}catch(Exception ex)
					{
						fv.setTime(new Time("00:00:00"));
					}

					fv.setUser(rst.getString("cuser"));
					fv.setFilename(rst.getString("file_name"));
					String vname = FileHandler.getFileNameWithoutExt(rst.getString("file_name"))+
						"#"+rst.getInt("version_number")+"."+FileHandler.getFileExtension(rst.getString("file_name"));					
					Blob bl = null;
					byte[] byt = null;
					try{
						bl = rst.getBlob("content");
					}catch(Throwable t){
						try{
							byt = rst.getBytes("content");
						}catch(Throwable t2){
							
						}
					}

					byte[] allBytesInBlob = bl!=null?bl.getBytes(1, (int) bl.length()):byt;

					FileOutputStream fos=null;
					try{
						java.io.File javaFile = new java.io.File(path+vname);
						fos = new FileOutputStream(javaFile.getPath());
						fos.write(allBytesInBlob);
						fv.setJavaFile(javaFile);
					}finally{
						if(fos!=null){
							fos.close();
						}
					}
					versions.add(fv);
				}
				
				stmt = jdbcConnection.prepareStatement(q2);
				stmt.setLong(1, parentFileId);
				rst = stmt.executeQuery();
				if(rst.first())
				{//should be only one file with the given id
					FileVersion fv=new FileVersion();
					fv.setId(0);
					fv.setFileid(rst.getLong("fileid"));
					fv.setVersionNumber(rst.getInt("versionnumber"));
					fv.setDate(new Date(rst.getString("creationdate")));
					try{
						fv.setTime(new Time(rst.getString("creationtime")));
					}catch(Exception ex)
					{
						fv.setTime(new Time("00:00:00"));
					}

					fv.setUser(rst.getString("creationuserid"));
					fv.setFilename(rst.getString("filename"));
					String vname = FileHandler.getFileNameWithoutExt(rst.getString("filename"))+
						"#"+rst.getInt("versionnumber")+"."+FileHandler.getFileExtension(rst.getString("filename"));					
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

					byte[] allBytesInBlob = bl!=null?bl.getBytes(1, (int) bl.length()):byt;

					FileOutputStream fos=null;
					try{
						java.io.File javaFile = new java.io.File(path+vname);
						fos = new FileOutputStream(javaFile.getPath());
						fos.write(allBytesInBlob);
						fv.setJavaFile(javaFile);
					}finally{
						if(fos!=null){
							fos.close();
						}
					}
					versions.add(fv);
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
		return versions;
	}

	/**
	 * Returns the file version corresponding to the given fileVersionId. The fileVersion object contains the reference<br />
	 * to the IvyFile and the corresponding java.io.File.<br />
	 * The IvyFile / java.io.File is created dynamically by copying all the bytes contained in the BLOB version content table field.<br />
	 * The IvyFile is going to be created in the Ivy Session Server directory and will be temporary: it will be deleted<br />
	 * after the Ivy Session has been closed.<br />
	 * @param fileVersionId the file version id 
	 * @return the FileVersion object or null if a problem occurred.
	 * @throws Exception in case of a Persistence or SQLException
	 */
	public FileVersion getFileVersionWithJavaFile(long fileVersionId) throws Exception
	{
		FileVersion fv = null;
		if(fileVersionId<=0)
		{
			return fv;
		}
		String query="SELECT * FROM "+this.fileVersionTableName+
		" INNER JOIN "+this.fileVersionContentTableName+" ON "+this.fileVersionTableName+".fvc_id = "+this.fileVersionContentTableName+".fvcid" +
		" WHERE "+this.fileVersionTableName+".versionid = ? ";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try{			
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileVersionId);
				ResultSet rst = stmt.executeQuery();
				if(rst.next())
				{

					fv=new FileVersion();
					fv.setId(fileVersionId);
					fv.setFileid(rst.getLong("file_id"));
					fv.setVersionNumber(rst.getInt("version_number"));
					fv.setVersionContentId(rst.getLong("fvc_id"));
					fv.setDate(new Date(rst.getDate("cdate")));
					try{
						if(rst.getTime("ctime").toString().length()>8)
						{
							fv.setTime(new Time(rst.getTime("ctime").toString().substring(0,8)));
						}
						else{
							fv.setTime(new Time(rst.getTime("ctime").toString()));
						}
					}catch(Exception ex)
					{
						fv.setTime(new Time("00:00:00"));
					}

					fv.setUser(rst.getString("cuser"));
					fv.setFilename(rst.getString("file_name"));

					Blob bl = null;
					byte[] byt = null;
					try{
						bl = rst.getBlob("content");
					}catch(Throwable t){
						try{
							byt = rst.getBytes("content");
						}catch(Throwable t2){
							
						}
					}

					String tmpPath="tmp/"+System.nanoTime()+"/"+rst.getString("file_name");
					File ivyFile = new File(tmpPath,true);
					ivyFile.createNewFile();
					byte[] allBytesInBlob = bl!=null?bl.getBytes(1, (int) bl.length()):byt;

					FileOutputStream fos=null;
					try{
						java.io.File javaFile = ivyFile.getJavaFile();

						fos = new FileOutputStream(javaFile.getPath());
						fos.write(allBytesInBlob);
						fv.setJavaFile(javaFile);
						fv.setIvyFile(ivyFile);

					}finally{
						if(fos!=null){
							fos.close();
						}
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
		return fv;
	}
	
	/**
	 * This method deletes all the versions from a given file denoted by its id.<br />
	 * Do nothing if the fileID <=0 or if no version are found for this id.<br />
	 * it does not delete the file from the main table (last version).
	 * @param fileId the file id from the files main table
	 * @throws Exception in case of a Persistence or SQLException
	 */
	public void deleteAllVersionsFromFile(long fileId) throws Exception{
		if(fileId<=0)
		{
			return;
		}
		List<FileVersion> fvs = this.getFileVersions(fileId);
		String q1 = "DELETE FROM "+this.fileVersionTableName+" WHERE versionid = ?";
		String q2 = "DELETE FROM "+this.fileVersionContentTableName+" WHERE version_id = ?";
		IExternalDatabaseRuntimeConnection connection = null;
		try{
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;		
			try{	
				for(FileVersion fv : fvs)
				{
					stmt = jdbcConnection.prepareStatement(q1);
					stmt.setLong(1, fv.getId());
					stmt.executeUpdate();
					
					stmt = jdbcConnection.prepareStatement(q2);
					stmt.setLong(1, fv.getId());
					stmt.executeUpdate();
				}
			}finally{
				try{
					DatabaseUtil.close(stmt);
				}catch (Exception ex){}
			}
			
		}finally{
			if(connection!=null ){
				database.giveBackAndUnlockConnection(connection);
			}
		}
	}

	/**
	 * 
	 * @param versionId
	 * @return
	 * @throws Exception in case of a Persistence or SQLException
	 */
	private long getLastInsertedFileVersionContent(long versionId) throws Exception
	{
		long id=0;
		if(versionId<=0)
		{
			return 0;
		}
		String query= "SELECT MAX(fvcid) FROM "+this.fileVersionContentTableName+" WHERE version_id=?";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			Recordset rset = null;
			List<Record> recordList= new ArrayList<Record>();
			try{			
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, versionId);
				rset = executeStatement(stmt);
				recordList = rset.toList();
				if(recordList.size()>0)
				{
					Record rec = recordList.get(0);

					id=Long.parseLong(rec.getField("id").toString());

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
		return id;
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
	 * allows executing a prepareStatement and returns the resulting recordset.<br>
	 * If the preparedStatement execution returns an empty Resultset then the RecordSet will be empty.
	 * The calling method is responsible to give back the preparedStatement with DatabaseUtil.close(stmt);
	 * @param _stmt
	 * @return
	 * @throws Exception
	 */
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
			List<String> colNames= new ArrayList<String>();
			for(int i=1; i<=numCols; i++){
				colNames.add(rsmd.getColumnName(i));
				//Ivy.log().debug(rsmd.getColumnName(i));
			}
			while(rst.next()){
				List<Object> values = new ArrayList<Object>();
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
	}

	/**
	 * Returns the Ivy database connection name used to get a connection to the database,<br />
	 * that contains the file's tables.
	 * @return 
	 */
	public String getIvyDBConnectionName() {
		return ivyDBConnectionName;
	}

	/**
	 * Set the Ivy database connection name used to get a connection to the database,<br />
	 * that contains the file's tables.
	 * @param ivyDBConnectionName the Ivy database connection name used to get a connection to the database.
	 */
	public void setIvyDBConnectionName(String ivyDBConnectionName) {
		this.ivyDBConnectionName = ivyDBConnectionName;
	}

	/**
	 * Returns the name of the table that stores the informations about the files.
	 * @return
	 */
	public String getFileTableName() {
		return fileTableName;
	}

	/**
	 * Sets the name of the table that stores the informations about the files.
	 * @param fileTableName
	 */
	public void setFileTableName(String fileTableName) {
		this.fileTableName = fileTableName;
	}

	/**
	 * Returns the name of the table that stores the files content.<br />
	 * This table contains the most actual version of the files. 
	 * The one you get directly in the File Manager.
	 * @return
	 */
	public String getFileContentTableName() {
		return fileContentTableName;
	}

	/**
	 * Sets the name of the table that stores the files content.<br />
	 * This table contains the most actual version of the files. 
	 * The one you get directly in the File Manager.
	 * @param fileContentTableName
	 */
	public void setFileContentTableName(String fileContentTableName) {
		this.fileContentTableName = fileContentTableName;
	}

	/**
	 * Returns the name of the table that contains the informations about the old files versions.
	 * @return
	 */
	public String getFileVersionTableName() {
		return fileVersionTableName;
	}

	/**
	 * Sets the name of the table that contains the informations about the old files versions.
	 * @param fileVersionTableName
	 */
	public void setFileVersionTableName(String fileVersionTableName) {
		this.fileVersionTableName = fileVersionTableName;
	}

	/**
	 * Returns the name of the table that contains all the files versions contents.
	 * @return
	 */
	public String getFileVersionContentTableName() {
		return fileVersionContentTableName;
	}

	/**
	 * Sets the name of the table that contains all the files versions contents.
	 * @param fileVersionContentTableName
	 */
	public void setFileVersionContentTableName(String fileVersionContentTableName) {
		this.fileVersionContentTableName = fileVersionContentTableName;
	}

	/**
	 * @return the databaseSchemaName
	 */
	public String getDatabaseSchemaName() {
		return databaseSchemaName;
	}

	/**
	 * @param databaseSchemaName the databaseSchemaName to set
	 */
	public void setDatabaseSchemaName(String databaseSchemaName) {

		this.databaseSchemaName = databaseSchemaName;
		if(databaseSchemaName!=null && databaseSchemaName.trim().length()>0)
		{
			this.fileTableName=this.databaseSchemaName+"."+Ivy.var().get("xivy_addons_fileManager_fileMetaDataTableName");
			this.fileContentTableName=this.databaseSchemaName+"."+Ivy.var().get("xivy_addons_fileManager_fileContentTableName");
			this.fileVersionTableName=this.databaseSchemaName+"."+Ivy.var().get("xivy_addons_fileManager_fileVersioningMetaDataTableName");
			this.fileVersionContentTableName=this.databaseSchemaName+"."+Ivy.var().get("xivy_addons_fileManager_fileVersioningContentTableName");
		}else
		{
			this.fileTableName=Ivy.var().get("xivy_addons_fileManager_fileMetaDataTableName");
			this.fileContentTableName=Ivy.var().get("xivy_addons_fileManager_fileContentTableName");
			this.fileVersionTableName=Ivy.var().get("xivy_addons_fileManager_fileVersioningMetaDataTableName");
			this.fileVersionContentTableName=Ivy.var().get("xivy_addons_fileManager_fileVersioningContentTableName");
		}
	}

}
