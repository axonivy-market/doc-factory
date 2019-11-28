/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.ReturnedMessage;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;
import ch.ivyteam.ivy.db.IExternalDatabase;
import ch.ivyteam.ivy.db.IExternalDatabaseRuntimeConnection;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.scripting.objects.Record;

/**
 * @author ec
 *
 */
public abstract class FileManagementStaticController {


	private static List<Record> executeStmt(PreparedStatement _stmt) throws Exception{
		if(_stmt == null){
			throw(new SQLException("Invalid PreparedStatement","PreparedStatement Null"));
		}

		List<Record> recordList= (List<Record>) List.create(Record.class);
		try (ResultSet rst =_stmt.executeQuery())
		{
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
		}
		return recordList;
	}

	/**
	 * 
	 * @param _nameOfTheDatabaseConnection
	 * @param tableNameSpace
	 * @param _path
	 * @param _isRecursive
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static ArrayList<DocumentOnServer> getDocumentsInPath(IExternalDatabase database, String tableNameSpace, String _path,
			boolean _isRecursive, String escapeChar) throws Exception {
		if(_path==null || _path.trim().length()==0)
		{
			throw new Exception("Invalid path in getDocumentsInPath method");
		}
		if(escapeChar==null || escapeChar.trim().length()==0)
		{
			escapeChar="\\";
		}
		ArrayList<DocumentOnServer>  al = new ArrayList<DocumentOnServer>();

		String folderPath = PathUtil.formatPathForDirectoryWithoutLastSeparator(_path)+"/";
		folderPath=PathUtil.escapeUnderscoreInPath(folderPath);
		List<Record> recordList= (List<Record>) List.create(Record.class);

		String query="";

		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = database.getAndLockConnection();
			Connection jdbcConnection=connection.getDatabaseConnection();
			if(_isRecursive)
			{
				query="SELECT * FROM "+tableNameSpace+" WHERE FilePath LIKE ? ESCAPE '"+escapeChar+"'";
			}
			else
			{
				query="SELECT * FROM "+tableNameSpace+" WHERE FilePath LIKE ? ESCAPE '"+escapeChar+"' AND FilePath NOT LIKE ? ESCAPE '"+escapeChar+"'";
			}
			try (PreparedStatement stmt = jdbcConnection.prepareStatement(query))
			{
				if(_isRecursive)
				{
					stmt.setString(1, folderPath+"%");
				}else
				{
					stmt.setString(1, folderPath+"%");
					stmt.setString(2, folderPath+"%/%");
				}
				recordList=executeStmt(stmt);
			}
		} finally{
			if(database != null && connection!=null ){
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
		}
		al.trimToSize();
		return al;
	}

	/**
	 * delete all the files from the db that are in the file Structure under a directory.
	 * @param _directoryPath
	 * @return
	 * @throws Exception
	 */
	public static ReturnedMessage deleteAllFilesUnderDirectory(IExternalDatabase database, String tableNameSpace, String fileContentTableNameSpace, String _directoryPath, String escapeChar) throws Exception
			{
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		if(_directoryPath==null || _directoryPath.trim().equals(""))
		{
			message.setType(FileHandler.INFORMATION_MESSAGE);
			message.setText("The directory to delete does not exist.");
			return message;
		}
		if(escapeChar==null || escapeChar.trim().length()==0)
		{
			escapeChar="\\";
		}
		//Query to delete the files under a path
		String base ="DELETE FROM "+tableNameSpace+" WHERE FilePath LIKE ? ESCAPE '"+escapeChar+"'";
		String query="DELETE FROM "+fileContentTableNameSpace+" WHERE file_id = ?";
		Ivy.log().info("We get the file ids...");
		int[] ids = getFileIdsUnderPath(database, tableNameSpace, _directoryPath+"/%", escapeChar);
		Ivy.log().info("File ids under the path to delete" +_directoryPath + " "+ids.length);
		Connection connection=null;
		try {
			connection = database.getConnection();
				if(ids!=null)
				{
					try (PreparedStatement stmt = connection.prepareStatement(query))
					{
						for(int i=0; i<ids.length; i++)
						{
							stmt.setInt(1, ids[i]);
							stmt.executeUpdate();
						}
					}
				}
				_directoryPath=PathUtil.escapeUnderscoreInPath(_directoryPath);
				try (PreparedStatement stmt = connection.prepareStatement(base))
				{
					stmt.setString(1, _directoryPath+"/%");
					stmt.executeUpdate();
				}
		}finally{
			if(connection!=null ){
				connection.close();
			}
		}
		return message;
			}
	
	/**
	 * returns an array of all the file ids contained under a specifically path
	 * @param _path
	 * @return
	 * @throws Exception
	 */
	public static int[] getFileIdsUnderPath(IExternalDatabase database, String tableNameSpace,String _path, String escapeChar) throws Exception
	{
		int[] i =null;
		if(_path==null || _path.trim().equals(""))
		{
			return i;
		}
		if(escapeChar==null || escapeChar.trim().length()==0)
		{
			escapeChar="\\";
		}
		_path=PathUtil.escapeUnderscoreInPath(_path);
		String query="";

		Connection connection = null;
		List<Record> recordList= (List<Record>) List.create(Record.class);
		try {
			connection = database.getConnection();

			query="SELECT FileId FROM "+tableNameSpace+" WHERE FilePath LIKE ? ESCAPE '"+escapeChar+"'";
			try (PreparedStatement stmt = connection.prepareStatement(query))
			{
				stmt.setString(1, _path);
				recordList =executeStmt(stmt);
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
			}
		} finally{
			if (connection!=null ){
				connection.close();
			}
		}
		return i;
	}
	
	/**
	 * Returns the database product name of the database connected through the given ivy database connection.
	 * @param ivyDatabaseConnectionName String the name of the ivy database connection
	 * @return product name of the database connected through the given ivy database connection.
	 */
	public static String getDatabaseProductName(IExternalDatabase database) {
		String result = null;
		try {
			result = database.getConnection().getMetaData().getDatabaseProductName();

		} catch (Exception ex) {
			Ivy.log().error("An Error occurred while trying to get the databaseProductName in "
									+ "FileManagementStaticController.getDatabaseProductName Method. "
									+ ex.getMessage());
		}
		return result;
	}

}
