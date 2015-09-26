package ch.ivyteam.ivy.addons.filemanager.database.sql;

public class FileVersioningSQLQueries {
	
	public static String FILEVERSION_TABLENAMESPACE_PLACEHOLDER ="TABLENAMESPACE";
	public static String FILEVERSION_CONTENT_TABLENAMESPACE_PLACEHOLDER ="TABLENAMESPACE2";
	public static String FILECONTENT_TABLENAMESPACE_PLACEHOLDER ="TABLENAMESPACE3";
	public static String FILE_TABLENAMESPACE_PLACEHOLDER ="TABLENAMESPACE4";

	
	/**
	 * SELECT * FROM FILEVERSION_TABLENAMESPACE_PLACEHOLDER WHERE id=?
	 */
	public static String GET_FILEVERSION_BY_ID = "SELECT * FROM " + FILEVERSION_TABLENAMESPACE_PLACEHOLDER + " WHERE versionid=?";
	
	/**
	 * SELECT * FROM FILEVERSION_TABLENAMESPACE_PLACEHOLDER WHERE file_id = ? AND version_number=?
	 */
	public static String GET_FILEVERSION_BY_FILEID_AND_VERSION_NUMBER = "SELECT * FROM " + FILEVERSION_TABLENAMESPACE_PLACEHOLDER + " WHERE file_id=? AND version_number=?";
	
	/**
	 * SELECT * FROM FILEVERSION_TABLENAMESPACE_PLACEHOLDER WHERE file_id = ? ORDER BY version_number DESC
	 */
	public static String GET_FILEVERSIONS_BY_FILEID = "SELECT * FROM " + FILEVERSION_TABLENAMESPACE_PLACEHOLDER + " WHERE file_id=? ORDER BY version_number DESC";
	
	/**
	 * SELECT * FROM FILEVERSION_TABLENAMESPACE_PLACEHOLDER WHERE file_id = ? ORDER BY version_number DESC
	 */
	public static String GET_NEXT_FILEVERSIONNUMBER_BY_FILEID = "SELECT version_number FROM "+ FILEVERSION_TABLENAMESPACE_PLACEHOLDER + " WHERE file_id=?";
	
	/**
	 * INSERT INTO FILEVERSION_TABLENAMESPACE_PLACEHOLDER (file_id,version_number,cdate,ctime,cuser,file_name) VALUES (?,?,?,?,?,?)
	 */
	public static String INSERT_FILEVERSION = "INSERT INTO " + FILEVERSION_TABLENAMESPACE_PLACEHOLDER + " (file_id,version_number,cdate,ctime,cuser,file_name) VALUES (?,?,?,?,?,?)";
	
	/**
	 * Query to insert the new Version Content<br>
	 * INSERT INTO FILEVERSION_CONTENT_TABLENAMESPACE_PLACEHOLDER (content, version_id) VALUES ((SELECT file_content FROM FILECONTENT_TABLENAMESPACE_PLACEHOLDER WHERE file_id = ?) , ?)
	 */
	public static String INSERT_FILEVERSION_CONTENT = "INSERT INTO " + FILEVERSION_CONTENT_TABLENAMESPACE_PLACEHOLDER + " (content, version_id) VALUES " +
			"((SELECT file_content FROM " + FILECONTENT_TABLENAMESPACE_PLACEHOLDER + " WHERE file_id = ?) , ?)";
	
	/**
	 * Query to insert the new Version Content for MS SQL 2005 and older versions<br>
	 * INSERT INTO FILEVERSION_CONTENT_TABLENAMESPACE_PLACEHOLDER (content, version_id) SELECT file_content, ? FROM FILECONTENT_TABLENAMESPACE_PLACEHOLDER WHERE file_id = ?
	 */
	public static String INSERT_FILEVERSION_CONTENT_MSSQL2005 = "INSERT INTO " + FILEVERSION_CONTENT_TABLENAMESPACE_PLACEHOLDER + " (content, version_id) " +
			"SELECT file_content, ? FROM " + FILECONTENT_TABLENAMESPACE_PLACEHOLDER + " WHERE file_id = ? ";
	
	/**
	 * UPDATE FILEVERSION_TABLENAMESPACE_PLACEHOLDER SET fvc_id = ? WHERE versionid = ?
	 */
	public static String UPDATE_FILEVERSIONCONTENT_ID_BY_FILECONTENTID ="UPDATE " + FILEVERSION_TABLENAMESPACE_PLACEHOLDER + " SET fvc_id=? WHERE versionid=?";
	
	/**
	 * UPDATE FILE_TABLENAMESPACE_PLACEHOLDER SET versionnumber = ?, creationdate = ?, creationtime = ?, creationuserid = ?, modificationdate = ?, modificationtime = ?, modificationuserid = ?  WHERE fileid=?
	 */
	public static String UPDATE_FILE_INFORMATION = "UPDATE " + FILE_TABLENAMESPACE_PLACEHOLDER + " SET versionnumber=?, creationdate = ?, creationtime = ?, "
			+ "creationuserid = ?, modificationdate = ?, modificationtime = ?, modificationuserid = ?  WHERE fileid=?";
	
	/**
	 * DELETE FROM FILEVERSION_TABLENAMESPACE_PLACEHOLDER WHERE versionid = ?
	 */
	public static String DELETE_FILEVERSION_BY_ID = "DELETE FROM " + FILEVERSION_TABLENAMESPACE_PLACEHOLDER + " WHERE versionid = ?";
	/**
	 * DELETE FROM FILEVERSION_CONTENT_TABLENAMESPACE_PLACEHOLDER WHERE fvcid = ?
	 */
	public static String DELETE_FILE_VERSION_CONTENT_BY_FILECONTENT_ID= "DELETE FROM " + FILEVERSION_CONTENT_TABLENAMESPACE_PLACEHOLDER + " WHERE fvcid = ?";
	
	/**
	 * UPDATE FILEVERSION_TABLENAMESPACE_PLACEHOLDER SET version_number = ?, file_name = ?, cuser = ?, cdate = ?, ctime = ? WHERE versionid = ?
	 */
	public static String UPDATE_FILEVERSION ="UPDATE "+ FILEVERSION_TABLENAMESPACE_PLACEHOLDER +" SET version_number = ?, file_name = ?, cuser = ?, cdate = ?, ctime = ? WHERE versionid = ?";
}
