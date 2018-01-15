/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.sql;

/**
 * @author ec
 *
 */
public class LanguagesSQLQueries {
	public static String TABLENAMESPACE_PLACEHOLDER ="TABLENAMESPACE";
	
	public static String SELECT_LANGUAGE_BY_NAME = "SELECT * FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE isoname LIKE ?";
	
	public static String SELECT_LANGUAGE_BY_ID = "SELECT * FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE id = ?";
	
	public static String SELECT_ALL_LANGUAGES = "SELECT * FROM "+TABLENAMESPACE_PLACEHOLDER;
	
	public static String CREATE_LANGUAGE = "INSERT INTO "+TABLENAMESPACE_PLACEHOLDER+" (isoname) VALUES (?)";
	
	public static String UPDATE_LANGUAGE_BY_ID = "UPDATE "+TABLENAMESPACE_PLACEHOLDER+" SET isoname = ? WHERE id = ?";
	
	public static String DELETE_LANGUAGE = "DELETE FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE isoname LIKE ?";
}
