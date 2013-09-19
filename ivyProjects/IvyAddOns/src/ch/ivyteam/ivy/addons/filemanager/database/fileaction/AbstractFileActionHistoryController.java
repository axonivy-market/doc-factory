package ch.ivyteam.ivy.addons.filemanager.database.fileaction;

import java.sql.Connection;
import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.FileAction;
import ch.ivyteam.ivy.addons.filemanager.FileActionType;

public abstract class AbstractFileActionHistoryController {
	
	/**
	 * creates a new file action history record.
	 * @param fileid: the file id on which the action was done
	 * @param actionType: the action type
	 * @param username: the user's name who has performed the action (eg. ec, sk ect..)
	 * @param actionInfos: if any, a description of the action.
	 * @throws Exception
	 */
	public abstract void createNewActionHistory(long fileid, short actionType, String username, String actionInfos) throws Exception;
	
	/**
	 * Creates a new file action history record.<br>
	 * It takes a java.sql.connection as parameter to be able to be called from within other methods <br>
	 * without locking another database connection during its processing.<br>
	 * See Issue #28265, database deadlock in some of the FileManagement methods used concurrently in a lot of different processes.<br>
	 * <b>Important: </b>this method does not release the given java.sql.Connection. It is up to the calling method to do that.
	 * @param fileid: the file id on which the action was done
	 * @param actionType: a short number representing the action type. The action types are stored in the "fileactiontype" table.
	 * @param username: the user's name who has performed the action (eg. ec, sk ect..)
	 * @param actionInfos: if any, a description of the action.
	 * @param con a java.sql.connection to the database.
	 * @throws Exception if the file id is not a valid id, if the file action type does not exist, or in case of SQLException.
	 */
	public abstract void createNewActionHistory(long fileid, short actionType, String username, String actionInfos, Connection con) throws Exception;
	
	/**
	 * Check if the given action Type exists in the table.
	 * @param actionType a short number representing the action type. The action types are stored in the "fileactiontype" table.
	 * @return true if the action type was found, else false.
	 * @throws Exception
	 */
	public abstract boolean actionTypeExists(short actionType) throws Exception;
	
	/**
	 * Check if the given action Type exists in the table.
	 * It takes a java.sql.connection as parameter to be able to be called from within other methods <br>
	 * without locking another database connection during its processing.<br>
	 * See Issue #28265, database deadlock in some of the FileManagement methods used concurrently in a lot of different processes.<br>
	 * <b>Important: </b>this method does not release the given java.sql.Connection. It is up to the calling method to do that.
	 * @param actionType a short number representing the action type. The action types are stored in the "fileactiontype" table.
	 * @param con a java.sql.connection to the database.
	 * @return
	 * @throws Exception
	 */
	public abstract boolean actionTypeExists(short actionType, Connection con) throws Exception;
	
	/**
	 * returns all the action types with the action description in the right language.<br>
	 * The language is stored in a column which name is the ISO language code in lowercase: "en", "fr", "de"...<br>
	 * If the language column does not exist the English translation will be inserted.
	 * @param lang: the desired language for the description. "en", "fr", "de"....
	 * @return the java.util.List of ch.ivyteam.ivy.addons.filemanager.FileActionType objects
	 * @throws Exception in case of sql exceptions etc...
	 */
	public abstract List<FileActionType> getAllFileActionTypes(String lang) throws Exception;
	
	/**
	 * Gets the registered actions on a given file. The action desc are given in the given language if this translation exists.<br>
	 * The language is stored in a column which name is the ISO language code in lowercase: "en", "fr", "de"...<br>
	 * @param fileid : the file id
	 * @return : the desired language for the action description. This description is stored in a language column in the fileactiontype table.<br>
	 * If the language column cannot be found, the english translation will be taken instead.
	 * @throws Exception
	 */
	public abstract List<FileAction> getFileActions(long fileid, String lang) throws Exception;
	
	/**
	 * Checks if the given language code is used as translation in the fileActionTypes table.<br>
	 * Calling fileActionTypeTranslationExist("it") will return true if a column named "it" exists in the fileActionTypes table for the Italian translation.<br>
	 * Per convention the translation column names are spelled in lowercase and represent the ISO Code of the language (en, fr, de ...)
	 * @param lang
	 * @return true if a column representing the given language translation exists
	 * @throws Exception
	 */
	public abstract boolean fileActionTypeTranslationExist(String lang) throws Exception;
	
	/**
	 * 
	 * @return
	 */
	public abstract FileActionConfiguration getConfig();

}
