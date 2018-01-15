/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.sql;

/**
 * @author ec
 *
 */
public class FolderOnServerSQLQueries {

	public static String TABLENAMESPACE_PLACEHOLDER ="TABLENAMESPACE";
	public static String ESCAPECHAR_PLACEHOLDER ="ESCAPECHAR";
	public static String TRANSLATION_TABLENAMESPACE_PLACEHOLDER ="TRANSLATION_TABLENAMESPACE";

	/**
	 * Used to select a particular directory at the given path.<br>
	 * This can also be used to return a list of directories with a "regexp".<br>
	 * SELECT * FROM --TABLENAMESPACE_PLACEHOLDER-- WHERE dir_path = ? ORDER BY dir_path ASC
	 */
	public static String SELECT_DIRECTORY_BY_PATH = "SELECT * FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE dir_path = ? ORDER BY dir_path ASC";

	/**
	 * Used to select a particular directory with its id.<br>
	 * SELECT * FROM --TABLENAMESPACE_PLACEHOLDER-- WHERE id = ? ORDER BY dir_path ASC
	 */
	public static String SELECT_DIRECTORY_BY_ID = "SELECT * FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE id = ? ORDER BY dir_path ASC";
	
	/**
	 * Used to select the directories that are direct children of a given parent directory.<br>
	 * SELECT * FROM --TABLENAMESPACE_PLACEHOLDER-- WHERE dir_path LIKE ? ESCAPE --ESCAPECHAR_PLACEHOLDER-- AND dir_path NOT LIKE ? ESCAPE
	 *	--ESCAPECHAR_PLACEHOLDER-- ORDER BY dir_path ASC
	 */
	public static String SELECT_FOLDER_UNDER_PATH_NOT_RECURSIVE_Q="SELECT * FROM "
			+TABLENAMESPACE_PLACEHOLDER+" WHERE dir_path LIKE ? ESCAPE '"+ESCAPECHAR_PLACEHOLDER+"' AND dir_path NOT LIKE ? ESCAPE '"
			+ESCAPECHAR_PLACEHOLDER+"' ORDER BY dir_path ASC";

	/**
	 * Used to select the directories that are direct and indirect children of a given parent directory.<br>
	 * SELECT * FROM --TABLENAMESPACE_PLACEHOLDER-- WHERE dir_path LIKE ? ESCAPE --ESCAPECHAR_PLACEHOLDER-- ORDER BY dir_path ASC
	 */
	public static String SELECT_FOLDER_UNDER_PATH_RECURSIVE_Q="SELECT * FROM "
			+TABLENAMESPACE_PLACEHOLDER+" WHERE dir_path LIKE ? ESCAPE '"+ESCAPECHAR_PLACEHOLDER+"' ORDER BY dir_path ASC";
	
	/**
	 * Used to create a new directory. This query corresponds to the first version of the directory security.<br>
	 * Here we do not have the following rights: ccd, crd, ctd, cuf, ccf.<br>
	 * INSERT INTO --TABLENAMESPACE_PLACEHOLDER-- (dir_name, dir_path, creation_user_id, creation_date, creation_time, 
	 * modification_user_id, modification_date,
	 * modification_time,is_protected, cmdr, cod, cud, cdd, cwf, cdf) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
	 */
	public static String CREATE_FOLDER_Q1 = "INSERT INTO "+TABLENAMESPACE_PLACEHOLDER+
			" (dir_name, dir_path, creation_user_id, creation_date, creation_time, modification_user_id, modification_date," +
			"modification_time,is_protected, cmdr, cod, cud, cdd, cwf, cdf) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Used to create a new directory. This query corresponds to the second version of the directory security.<br>
	 * Here we have the following new rights: ccd, crd, ctd, cuf, ccf.<br>
	 * INSERT INTO --TABLENAMESPACE_PLACEHOLDER-- (dir_name, dir_path, creation_user_id, creation_date, creation_time, 
	 * modification_user_id, modification_date,
	 * modification_time,is_protected, cmdr, cod, cud, cdd, cwf, cdf, ccd, crd, ctd, cuf, ccf) 
	 * VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
	 */
	public static String CREATE_FOLDER_Q2 = "INSERT INTO "+TABLENAMESPACE_PLACEHOLDER+
			" (dir_name, dir_path, creation_user_id, creation_date, creation_time, modification_user_id, modification_date," +
			"modification_time,is_protected, cmdr, cod, cud, cdd, cwf, cdf, ccd, crd, ctd, ccf, cuf) " +
			"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	
	/**
	 * Used to update a directory. This query corresponds to the first version of the directory security.<br>
	 * Here we do not have the following rights: ccd, crd, ctd, cuf, ccf.<br>
	 * The WHERE condition is set by the folder id.<br>
	 * UPDATE --TABLENAMESPACE_PLACEHOLDER-- SET dir_name = ? ,dir_path = ? , creation_user_id = ?, creation_date= ?, 
	 * creation_time= ? , modification_user_id = ? , modification_date = ? , modification_time = ? ,is_protected = ? , 
	 * cmdr = ? , cod= ?, cud= ?, cdd= ?, cwf= ?, cdf= ? WHERE id = ?
	 */
	public static String UPDATE_FOLDER_BY_ID_Q1 = "UPDATE "+TABLENAMESPACE_PLACEHOLDER+
			" SET dir_name = ? ,dir_path = ? , creation_user_id = ?, creation_date= ?, " +
			"creation_time= ? , modification_user_id = ? , modification_date = ? ," +
			"modification_time = ? ,is_protected = ? , cmdr = ? , cod= ?, cud= ?, cdd= ?, cwf= ?,cdf= ? WHERE id = ?";
	
	/**
	 * Used to update a directory. This query corresponds to the second version of the directory security.<br>
	 * Here we have the following rights: ccd, crd, ctd, cuf, ccf.<br>
	 * The WHERE condition is set by the folder id.<br>
	 * UPDATE --TABLENAMESPACE_PLACEHOLDER-- SET dir_name = ? ,dir_path = ? , creation_user_id = ?, creation_date= ?, 
	 * creation_time= ? , modification_user_id = ? , modification_date = ? , modification_time = ? ,is_protected = ? , 
	 * cmdr = ? , cod= ?, cud= ?, cdd= ?, cwf= ?, cdf= ?, ccd= ?, crd= ?, ctd= ?, ccf= ?, cuf= ? WHERE id = ?
	 */
	public static String UPDATE_FOLDER_BY_ID_Q2 = "UPDATE "+TABLENAMESPACE_PLACEHOLDER+
			" SET dir_name = ? ,dir_path = ? , creation_user_id = ?, creation_date= ?, " +
			"creation_time= ? , modification_user_id = ? , modification_date = ? ," +
			"modification_time = ? ,is_protected = ? , cmdr = ? , cod= ?, cud= ?, cdd= ?, cwf= ?,cdf= ?, " +
			"ccd= ?, crd= ?, ctd= ?, ccf= ?, cuf= ? WHERE id = ?";
	
	/**
	 * Used to update a directory. This query corresponds to the first version of the directory security.<br>
	 * Here we do not have the following rights: ccd, crd, ctd, cuf, ccf.<br>
	 * The WHERE condition is set by the folder path.<br>
	 * UPDATE --TABLENAMESPACE_PLACEHOLDER-- SET dir_name = ? , creation_user_id = ?, creation_date= ?, 
	 * creation_time= ? , modification_user_id = ? , modification_date = ? , modification_time = ? ,is_protected = ? , 
	 * cmdr = ? , cod= ?, cud= ?, cdd= ?, cwf= ?, cdf= ? WHERE dir_path = ?
	 */
	public static String UPDATE_FOLDER_BY_PATH_Q1 = "UPDATE "+TABLENAMESPACE_PLACEHOLDER+
			" SET dir_name = ? , creation_user_id = ?, creation_date= ?, " +
			"creation_time= ? , modification_user_id = ? , modification_date = ? ," +
			"modification_time = ? ,is_protected = ? , cmdr = ? , cod= ?, cud= ?, cdd= ?, cwf= ?,cdf= ? WHERE dir_path = ?";
	
	/**
	 * Used to update a directory. This query corresponds to the second version of the directory security.<br>
	 * Here we have the following rights: ccd, crd, ctd, cuf, ccf.<br>
	 * The WHERE condition is set by the folder path.<br>
	 * UPDATE --TABLENAMESPACE_PLACEHOLDER-- SET dir_name = ? , creation_user_id = ?, creation_date= ?, 
	 * creation_time= ? , modification_user_id = ? , modification_date = ? , modification_time = ? ,is_protected = ? , 
	 * cmdr = ? , cod= ?, cud= ?, cdd= ?, cwf= ?, cdf= ?, ccd= ?, crd= ?, ctd= ?, ccf= ?, cuf= ? WHERE dir_path = ?
	 */
	public static String UPDATE_FOLDER_BY_PATH_Q2 = "UPDATE "+TABLENAMESPACE_PLACEHOLDER+
			" SET dir_name = ? , creation_user_id = ?, creation_date= ?, " +
			"creation_time= ? , modification_user_id = ? , modification_date = ? ," +
			"modification_time = ? ,is_protected = ? , cmdr = ? , cod= ?, cud= ?, cdd= ?, cwf= ?,cdf= ?, " +
			"ccd= ?, crd= ?, ctd= ?, ccf= ?, cuf= ? WHERE dir_path = ?";
	
	/**
	 * Used to delete a particular directory at the given path.<br>
	 * DELETE FROM --TABLENAMESPACE_PLACEHOLDER-- WHERE dir_path = ?
	 */
	public static String DELETE_DIRECTORY_BY_PATH = "DELETE FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE dir_path = ?";
	
	/**
	 * used to select all the translations of a directory given by its path.<br>
	 * SELECT * FROM --TRANSLATION_TABLENAMESPACE_PLACEHOLDER-- WHERE dirid = (SELECT id FROM --TABLENAMESPACE_PLACEHOLDER-- WHERE dir_path = ?)
	 */
	public static String SELECT_TRANSLATIONS_BY_DIRPATH ="SELECT * FROM "+TRANSLATION_TABLENAMESPACE_PLACEHOLDER+" WHERE dirid = (SELECT id FROM "+TABLENAMESPACE_PLACEHOLDER+" WHERE dir_path = ?)";
	
	
	
}
