package ch.ivyteam.ivy.addons.filemanager.database.sql;

public final class FileLinkSQLQueries {
	
	private FileLinkSQLQueries() {}
	
	public static String TABLENAMESPACE_PLACEHOLDER ="TABLENAMESPACE";
	
	/**
	 * INSERT INTO TABLENAMESPACE_PLACEHOLDER (name, directory_id, file_id, content_id, version_number, version_id) VALUES (?,?,?,?,?,?)
	 */
	public static final String INSERT_FILELINK = "INSERT INTO " + TABLENAMESPACE_PLACEHOLDER
			+ " (name, directory_id, file_id, content_id, version_number, version_id) VALUES (?,?,?,?,?,?)";
	
	/**
	 * UPDATE TABLENAMESPACE_PLACEHOLDER SET name = ? , directory_id = ? , content_id = ? , version_id = ? , reference_version = ? WHERE id = ?"
	 */
	public static final String UPDATE_FILELINK = "UPDATE " + TABLENAMESPACE_PLACEHOLDER
			+ " SET name = ? , directory_id = ? , content_id = ? , version_id = ? WHERE id = ?";
	
	/**
	 * DELETE FROM  TABLENAMESPACE_PLACEHOLDER WHERE id = ?
	 */
	public static final String DELETE_FILELINK = "DELETE FROM " + TABLENAMESPACE_PLACEHOLDER + " WHERE id = ?";
	
	/**
	 * DELETE FROM  TABLENAMESPACE_PLACEHOLDER WHERE file_id = ?
	 */
	public static final String DELETE_FILELINKS_BY_FILEID = "DELETE FROM " + TABLENAMESPACE_PLACEHOLDER + " WHERE file_id = ?";
	
	/**
	 * DELETE FROM  TABLENAMESPACE_PLACEHOLDER WHERE version_id = ?
	 */
	public static final String DELETE_FILELINKS_BY_FILEVERSION_ID = "DELETE FROM " + TABLENAMESPACE_PLACEHOLDER + " WHERE version_id = ?";
	
	/**
	 * DELETE FROM  TABLENAMESPACE_PLACEHOLDER WHERE directory_id = ?
	 */
	public static final String DELETE_FILELINKS_BY_DIRECTORY = "DELETE FROM " + TABLENAMESPACE_PLACEHOLDER + " WHERE directory_id = ?";
	
	/**
	 * SELECT * FROM TABLENAMESPACE_PLACEHOLDER WHERE id = ?
	 */
	public static final String SELECT_FILELINK_BY_ID = "SELECT * FROM " + TABLENAMESPACE_PLACEHOLDER + " WHERE id = ?";
	
	/**
	 * SELECT * FROM TABLENAMESPACE_PLACEHOLDER WHERE  WHERE directory_id = ? AND name = ?
	 */
	public static final String SELECT_FILELINK_BY_DIRECTORY_ID_AND_NAME = "SELECT * FROM " + TABLENAMESPACE_PLACEHOLDER + " WHERE directory_id = ? AND name = ?";
	
	/**
	 * Used to get the content id from the file content table.<br>
	 * SELECT id FROM --file content table-- WHERE file_id = ?
	 */
	public static String SELECT_FILE_CONTENT_ID = "SELECT id FROM " + TABLENAMESPACE_PLACEHOLDER + " WHERE file_id = ?";
	
	/**
	 * Used to get the version content id from the file version content table.<br>
	 * SELECT fvcid FROM TABLENAMESPACE_PLACEHOLDER WHERE version_id = ?
	 */
	public static String SELECT_FILE_VERSION_CONTENT_ID = "SELECT fvcid FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE version_id = ?";
	
	/**
	 * Used to get the version content from the file version content table.<br>
	 * SELECT content FROM TABLENAMESPACE_PLACEHOLDER WHERE version_id = ?
	 */
	public static String SELECT_FILE_VERSION_CONTENT = "SELECT content FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE version_id = ?";
	
	/**
	 * SELECT * FROM TABLENAMESPACE_PLACEHOLDER WHERE directory_id = ?
	 */
	public static final String SELECT_FILELINKS_BY_DIRECTORY = "SELECT * FROM " + TABLENAMESPACE_PLACEHOLDER + " WHERE directory_id = ?";
	
	/**
	 * SELECT * FROM TABLENAMESPACE_PLACEHOLDER WHERE file_id = ?
	 */
	public static final String SELECT_FILELINKS_BY_FILE_ID = "SELECT * FROM " + TABLENAMESPACE_PLACEHOLDER + " WHERE file_id = ?";
	
	/**
	 * SELECT * FROM TABLENAMESPACE_PLACEHOLDER WHERE version_id = ?
	 */
	public static final String SELECT_FILELINKS_BY_FILE_VERSION_ID = "SELECT * FROM " + TABLENAMESPACE_PLACEHOLDER + " WHERE version_id = ?";
	
	/**
	 * used for getting a file id from the uploaded files with the given file path
	 * SELECT fileid FROM TABLENAMESPACE_PLACEHOLDER WHERE filepath = ?
	 */
	public static final String GET_FILE_ID_WITH_PATH = "SELECT fileid FROM " + TABLENAMESPACE_PLACEHOLDER + " WHERE filepath = ?";
}
