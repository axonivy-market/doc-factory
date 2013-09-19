/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.sql;

/**
 * This clas contains static String that declares the needed SQL queries for the translated items in the filemanagement.
 * @author ec
 *
 */
public class TranslateItemSQLQueries {
	
	public static final String TRANSLATION_TABLENAMESPACE_PLACEHOLDER = "TRANSLATION_TABLENAMESPACE";
	
	/**
	 * used to select all the translations of an item given by its id.<br>
	 * SELECT * FROM --TRANSLATION_TABLENAMESPACE_PLACEHOLDER-- WHERE translateditemid = ?
	 */
	public static String SELECT_TRANSLATIONS_BY_TRANSLATED_ITEMID ="SELECT * FROM "+TRANSLATION_TABLENAMESPACE_PLACEHOLDER+" WHERE translateditemid = ?";
	
	/**
	 * used to create an item translation.<br>
	 * INSERT INTO --TRANSLATION_TABLENAMESPACE_PLACEHOLDER-- (translateditemid, lang, tr) VALUES (?,?,?)
	 */
	public static String CREATE_ITEM_TRANSLATION = "INSERT INTO "+TRANSLATION_TABLENAMESPACE_PLACEHOLDER+" (translateditemid, lang, tr) VALUES (?,?,?)";
	
	/**
	 * used to update an item translation.<br>
	 * UPDATE --TRANSLATION_TABLENAMESPACE_PLACEHOLDER-- SET tr = ? WHERE translateditemid = ? AND lang = ?
	 */
	public static String UPDATE_ITEM_TRANSLATION = "UPDATE "+TRANSLATION_TABLENAMESPACE_PLACEHOLDER+" SET tr = ? WHERE translateditemid = ? AND lang = ?";
	
	/**
	 * Used to delete the translation of an item given by its unique id.<br>
	 * DELETE FROM --TRANSLATION_TABLENAMESPACE_PLACEHOLDER-- WHERE translateditemid = ?
	 */
	public static String DELETE_ITEM_TRANSLATION_BY_ID = "DELETE FROM "+TRANSLATION_TABLENAMESPACE_PLACEHOLDER+" WHERE translateditemid = ?";

}
