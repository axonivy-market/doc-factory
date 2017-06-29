package ch.ivyteam.ivy.addons.filemanager.database.sql;

/**
 * @author ec
 *
 */
public class DocumentOnServerSQLQueries {
	
	public static String TABLENAMESPACE_PLACEHOLDER ="TABLENAMESPACE";
	public static String ESCAPECHAR_PLACEHOLDER ="ESCAPECHAR";
	public static String PARAMETER_SIGNS = "PARAMETERSIGNS";
	
	/**
	 * Select the documentOnServer meta data from the files table with the file path<br>
	 * Can be used to search recursively if the path is like "path%".<br>
	 * SELECT * FROM --file table-- WHERE FilePath LIKE ? ESCAPE 'ESCAPECHAR'
	 */
	public static String SELECT_WITH_PATH_Q = "SELECT * FROM "+TABLENAMESPACE_PLACEHOLDER+
			" WHERE FilePath LIKE ? ESCAPE '"+ESCAPECHAR_PLACEHOLDER+"' ORDER BY FileName";
	
	/**
	 * Select the documentOnServer meta data from the files table with the file id<br>
	 * SELECT * FROM --file table-- WHERE FileId = ?
	 */
	public static String SELECT_WITH_ID_Q = "SELECT * FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE FileId = ?";
	
	/**
	 * Used to search for documentOnServer objects under a strict path, the search is not recursive.<br>
	 * SELECT * FROM --file table-- WHERE FilePath LIKE ? ESCAPE 'ESCAPECHAR' AND FilePath NOT LIKE ? ESCAPE 'ESCAPECHAR'
	 */
	public static String SELECT_NOT_RECURSIVE_WITH_PATH_Q ="SELECT * FROM "+TABLENAMESPACE_PLACEHOLDER+
			" WHERE FilePath LIKE ? ESCAPE '"+ESCAPECHAR_PLACEHOLDER+"' AND FilePath NOT LIKE ? ESCAPE '"+ESCAPECHAR_PLACEHOLDER+"'";
	
	/**
	 * Used to extract the blob file content from the file content table.<br>
	 * SELECT file_content FROM --file content table-- WHERE file_id = ?
	 */
	public static String SELECT_FILE_CONTENT_Q ="SELECT file_content FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE file_id = ?";
	
	
	
	public static String SELECT_ALL_DOCUMENTS_BY_FILTERING_Q = "SELECT * FROM " + TABLENAMESPACE_PLACEHOLDER + " "
			+ "INNER JOIN filetype ON " + TABLENAMESPACE_PLACEHOLDER + ".filetypeid = filetype.id AND filetype.name LIKE ? "
			+ "INNER JOIN tags ON " + TABLENAMESPACE_PLACEHOLDER + ".fileid = tags.fileid AND tags.tag LIKE ? "
			+ "WHERE " + TABLENAMESPACE_PLACEHOLDER + ".filepath LIKE ? -CREATIONDATE CONDITION- "
			+ "ORDER BY uploadedfiles.fileid DESC;";
	
	
	
	/**
	 * Insert a new Document in the files table<br>
	 * INSERT INTO --file table-- (FileName, FilePath, CreationUserId, CreationDate, CreationTime, 
	 *	FileSize, Locked, LockingUserId, ModificationUserId, ModificationDate,
	 *	ModificationTime, Description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
	 */
	public static String INSERT_Q = "INSERT INTO "+TABLENAMESPACE_PLACEHOLDER+
			" (FileName, FilePath, CreationUserId, CreationDate, CreationTime, " +
			"FileSize, Locked, LockingUserId, ModificationUserId, ModificationDate, " +
			"ModificationTime, Description) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Insert the file content into the file content table<br>
	 * INSERT INTO --file table content-- (file_id, file_content) VALUES (?,?)
	 */
	public static String INSERT_CONTENT_Q="INSERT INTO "+TABLENAMESPACE_PLACEHOLDER+" (file_id, file_content) VALUES (?,?)";
	
	/**
	 * update a given document by its id in the file table<br>
	 * UPDATE --file table-- SET FileName= ?,  FilePath= ?, FileSize= ?, Locked= ?, LockingUserId= ?, ModificationUserId= ?,
	 * ModificationDate= ?, ModificationTime= ?, Description= ?, versionnumber = ?, filetypeid = ? WHERE FileId = ?
	 */
	public static String UPDATE_BY_ID_Q = "UPDATE "+TABLENAMESPACE_PLACEHOLDER+
			" SET FileName= ?,  FilePath= ?," +
			" FileSize= ?, Locked= ?, LockingUserId= ?, ModificationUserId= ?," +
			" ModificationDate= ?, ModificationTime= ?, Description= ?, versionnumber = ?, filetypeid = ? WHERE FileId = ?";
	
	/**
	 * update a given document by its id in the file table<br>
	 * UPDATE --file table-- SET FileName= ?,  FilePath= ?, FileSize= ?, Locked= ?, LockingUserId= ?, ModificationUserId= ?,
	 * ModificationDate= ?, ModificationTime= ?, Description= ?, versionnumber = ? WHERE FileId = ?
	 */
	public static String UPDATE_BY_ID_NO_FILETYPE_Q = "UPDATE "+TABLENAMESPACE_PLACEHOLDER+
			" SET FileName= ?,  FilePath= ?," +
			" FileSize= ?, Locked= ?, LockingUserId= ?, ModificationUserId= ?," +
			" ModificationDate= ?, ModificationTime= ?, Description= ?, versionnumber = ? WHERE FileId = ?";
	
	/**
	 * update the content of a given document by its id in the file content table<br>
	 * UPDATE --file table content-- SET file_content= ? WHERE File_id = ?"
	 */
	public static String UPDATE_CONTENT_Q = "UPDATE "+TABLENAMESPACE_PLACEHOLDER+" SET file_content= ? WHERE File_id = ?";
	
	/**
	 * Unlocks the documents locked by the user recursively under the given path<br>
	 * UPDATE --file table-- SET Locked = 0, LockingUserId= ? WHERE LockingUserId = ? AND FilePath LIKE ? ESCAPE 'ESCAPECHAR'
	 */
	public static String UNLOCK_DOCUMENTS_UNDERPATH_RECURSIVELY_BY_USER_Q="UPDATE "+TABLENAMESPACE_PLACEHOLDER+ 
			" SET Locked = 0, LockingUserId= ? WHERE LockingUserId = ? AND FilePath LIKE ? ESCAPE '"+ESCAPECHAR_PLACEHOLDER+"'";
	
	/**
	 * Unlocks the documents locked by the user not recursively under the given path<br>
	 * UPDATE --file table--_PLACEHOLDER SET Locked = 0, LockingUserId= ? WHERE LockingUserId = ? AND FilePath LIKE ? ESCAPE 'ESCAPECHAR'
	 * AND FilePath NOT LIKE ? ESCAPE 'ESCAPECHAR'
	 */
	public static String UNLOCK_DOCUMENTS_UNDERPATH_NOT_RECURSIVE_BY_USER_Q="UPDATE "+ TABLENAMESPACE_PLACEHOLDER + 
			" SET Locked = 0, LockingUserId= ? WHERE LockingUserId = ? AND FilePath LIKE ? ESCAPE '"+ESCAPECHAR_PLACEHOLDER+"'" +
			" AND FilePath NOT LIKE ? ESCAPE 'ESCAPECHAR'";
	
	/**
	 * Deletes the files meta data in the file table (not the tags, types etc...)
	 * DELETE FROM --file table-- WHERE FileId = ?
	 */
	public static String DELETE_FILE_Q = "DELETE FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE FileId = ?";
	
	/**
	 * Deletes the file content
	 * DELETE FROM --file table content-- WHERE file_id = ?
	 */
	public static String DELETE_CONTENT_FILE_Q ="DELETE FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE file_id = ?";

}
