package ch.ivyteam.ivy.addons.filemanager.database.persistence;

import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.FileAction;
import ch.ivyteam.ivy.addons.filemanager.FileActionType;


public interface IFileActionHistoryPersistence extends IItemPersistence<FileAction> {
	
	/**
	 * Returns all the file actions that have been registered on the specified file in the specified language for the description.<br />
	 * If the lang parameter is not set or no description exists for the specified language, than the English will be used as default language.
	 * @param fileid the file id, must be greater than zero.
	 * @param lang the language (ISO2 code, like "de", "fr", "en").
	 * @return the list of FileAction objects related to the fileid. Empty if no actions could be found.
	 * @throws Exception
	 */
	public List<FileAction> getFileActionsForFileInSpecifiedLangOrDefaultLang(long fileid, String lang) throws Exception;
	
	/**
	 * Returns all the file action types that can be registered on a file, in the given language for the type description.<br />
	 * If the lang parameter is not set or no description exists for the specified language, than the English will be used as default language.
	 * @param lang the language (ISO2 code, like "de", "fr", "en").
	 * @return the list of all file action types.
	 * @throws Exception
	 */
	public List<FileActionType> getAllFileActionTypesInSpecifiedLangOrDefaultLang(String lang) throws Exception;
	
	/**
	 * checks if a given action TYPE exists.
	 * See the different action defined in the FileActionType class {@link FileActionType#FILE_CREATED_ACTION}, {@link FileActionType#FILE_DELETED_ACTION} ...
	 * @param type
	 * @return true if the type exists, else false.
	 * @throws Exception
	 */
	public boolean actionTypeExists(Number type) throws Exception;
	
	/**
	 * checks if the action types are described in the given language.
	 * @param lang the language (ISO2 code, like "de", "fr", "en"). Cannot be null or empty.
	 * @return true if the action types are described in the given language, else false.
	 * @throws Exception
	 */
	public boolean actionTypesExistInGivenLanguage(String lang) throws Exception;

}
