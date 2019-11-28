package ch.ivyteam.ivy.addons.filemanager.database.persistence;

import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.database.search.DocumentCreationDateSearch;

/**
 * This interface is sub-interface of IItemPersistence for the particular DocumentOnServer data type.<br>
 * This Interface have to be implemented by classes responsible for the DocumentOnServer persistence.<br>
 * This class extends the {@link IItemPersistence} class.<br>
 * To get an instance of this interface please use the appropriate static method of the {@link ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory}. 
 * @author ec (ecomba@soreco.ch)
 * @since 01/07/2013
 * @author ec
 *
 */
public interface IDocumentOnServerPersistence extends IItemPersistence<DocumentOnServer> {
	
	/**
	 * Gets a List of DocumentOnServer objects retrieved from the persistence system with the given path
	 * @param searchpath the search path
	 * @param recursive boolean flag. If true the search will be recursive and will look from the given path and in all the children paths. 
	 * Else the search is strictly limited to the given search path.
	 * @return a java.util.List<DocumentOnServer> objects or an empty List if no item was found.
	 * @throws Exception in case of error
	 */
	public List<DocumentOnServer> getList(String searchpath, boolean recursive) throws Exception;
	
	/**
	 * Returns a DocumentOnServer with a java.io.File that really exists in its javaFile field.
	 * @param id the long id of the DocumentOnServer
	 * @return DocumentOnServer with a java.io.File that really exists in its javaFile field.
	 * @throws Exception
	 */
	public DocumentOnServer getDocumentOnServerWithJavaFile(long id) throws Exception;
	
	/**
	 * Returns a DocumentOnServer with a java.io.File that really exists in its javaFile field.
	 * @param path
	 * @return DocumentOnServer with a java.io.File that really exists in its javaFile field.
	 * @throws Exception
	 */
	public DocumentOnServer getDocumentOnServerWithJavaFile(String path) throws Exception;
	
	/**
	 * Sets the given DocumentOnServer.javaFile field with a java.io.File that really exists and that holds its content.
	 * @param doc the given documentOnServer which java.io.File "javaFile" field has been filled with a java.io.File that really exists and that holds its content.
	 * @throws Exception
	 */
	public void setGivenDocumentOnServerJavaFile(DocumentOnServer doc) throws Exception;
	
	/**
	 * Returns a List of DocumentOnServer meta informations that verify the given List of conditions<br>
	 * The format of these conditions is specific to the underlying persistence system.
	 * @param _conditions: List<String> representing the conditions to perform the search in the DB
	 * @return an ArrayList of {@link DocumentOnServer} Objects.<br>
	 * Each DocumentOnServer object represents a File with several informations (name, path, size, CreationDate, creationUser...)
	 * @throws Exception
	 */
	public java.util.List<DocumentOnServer> getDocuments(java.util.List<String> _conditions)throws Exception;
	
	/**
	 * this Method should be used to unlock all the files edited by a given user under a given path.
	 * If the boolean argument "recursive" is true, then all the files in the children directories
	 * are going to be unLocked. Else just the files directly under the given path are going to be unLocked.
	 * This method can be used when you close an application for example.
	 * @param path the path where to look for the Locked files
	 * @param user the ivy user name 
	 * @param recursive true or false. If is recursive, look in all the sub directories under the path
	 * @throws Exception
	 */
	public void unlockDocumentsUnderPathEditedByUserWithOptionalRecursivity(String path, String user, boolean recursive) throws Exception;
	
	/**
	 * Return the list of documentOnServer matching the given conditions.<br />
	 * Only the filepath condition is mandatory.
	 * @param filepathCondition you can specify a precise filepath like "myapp/documents/employee/45/test.pdf" for getting one document, 
	 * or use some wildcards like "myapp/documents/employee/45/%" for searching all the documents under a specified path. Cannot be blank.
	 * @param filetypeNameCondition if specified, only the documents matching this filetype name will be returned. 
	 * This can be a precise name like "payslip" or one with some wildcard like "pay%". Can be blank.
	 * @param tagNameCondition If specified this must be a precise tag name as the documents can have more then one tag. Using wildcard search like "myTag%" won't work. Can be blank.
	 * @param creationDateCondition this object allows setting condition search for the creation date. Can be null.
	 * @return a list of matching documentOnServer
	 * @throws Exception
	 */
	public java.util.List<DocumentOnServer> getDocumentsFilteredby(String filepathCondition, String filetypeNameCondition, String tagNameCondition, DocumentCreationDateSearch creationDateCondition) throws Exception;
}
