/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.sql;

/**
 * @author ec
 *
 */
public class FileTypesSQLQueries {
	
	/**
	 * 
	 */
	public static String TABLENAMESPACE_PLACEHOLDER ="TABLENAMESPACE";
	
	/**
	 * SELECT * FROM --TABLENAMESPACE_PLACEHOLDER-- WHERE id = ?
	 */
	public static String SELECT_FILETYPE_BY_ID = "SELECT * FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE id = ?";
	/**
	 * SELECT * FROM --TABLENAMESPACE_PLACEHOLDER-- WHERE name = ? AND appname = ?
	 */
	public static String SELECT_FILETYPE_BY_NAME_AND_BY_APPNAME = "SELECT * FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE name = ? AND appname = ?";
	/**
	 * Special query for ORACLE if appname is null<br>
	 * SELECT * FROM --TABLENAMESPACE_PLACEHOLDER-- WHERE name = ? AND appname is null
	 */
	public static String SELECT_FILETYPE_BY_NAME_AND_BY_APPNAME_IS_NULL = "SELECT * FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE name = ? AND appname is null";
	/**
	 * SELECT * FROM --TABLENAMESPACE_PLACEHOLDER-- WHERE appname = ?
	 */
	public static String SELECT_FILETYPES_BY_APPNAME = "SELECT * FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE appname = ?";
	/**
	 * Special query for ORACLE if appname is null<br>
	 * SELECT * FROM --TABLENAMESPACE_PLACEHOLDER-- WHERE appname is null
	 */
	public static String SELECT_FILETYPES_BY_APPNAME_IS_NULL = "SELECT * FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE appname is null";
	/**
	 * SELECT * FROM --TABLENAMESPACE_PLACEHOLDER--
	 */
	public static String SELECT_ALL_FILETYPES = "SELECT * FROM "+TABLENAMESPACE_PLACEHOLDER;
	/**
	 * TABLENAMESPACE_PLACEHOLDER for files table name<br>
	 * SELECT * FROM --TABLENAMESPACE_PLACEHOLDER-- WHERE filetypeid = ?
	 */
	public static String SELECT_DOCUMENTS_WITH_FILETYPEID = "SELECT * FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE filetypeid = ?";
	/**
	 * UPDATE --TABLENAMESPACE_PLACEHOLDER-- SET name =?, appname=? WHERE id = ?
	 */
	public static String UPDATE_FILETYPE = "UPDATE "+TABLENAMESPACE_PLACEHOLDER+" SET name =?, appname=? WHERE id = ?";
	/**
	 * INSERT INTO --TABLENAMESPACE_PLACEHOLDER-- (name, appname) VALUES (?,?)
	 */
	public static String INSERT_FILETYPE = "INSERT INTO "+TABLENAMESPACE_PLACEHOLDER+" (name, appname) VALUES (?,?)";
	/**
	 * TABLENAMESPACE_PLACEHOLDER for files table name<br>
	 * UPDATE --TABLENAMESPACE_PLACEHOLDER-- SET filetypeid = ? WHERE FileId = ?
	 */
	public static String UPDATE_DOCUMENT_FILETYPE = "UPDATE "+TABLENAMESPACE_PLACEHOLDER+" SET filetypeid = ? WHERE FileId = ?";
	/**
	 * DELETE FROM --TABLENAMESPACE_PLACEHOLDER-- WHERE id = ?
	 */
	public static String DELETE_FILETYPE_BY_ID = "DELETE FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE id = ?";
	/**
	 * TABLENAMESPACE_PLACEHOLDER for files table name<br>
	 * UPDATE --TABLENAMESPACE_PLACEHOLDER-- SET filetypeid = 0 WHERE filetypeid = ?
	 */
	public static String REMOVE_FILETYPE_FROM_DOCUMENTS = "UPDATE "+TABLENAMESPACE_PLACEHOLDER+" SET filetypeid = 0 WHERE filetypeid = ?";
	
}
