/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.persistence;

import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;

/**
 * This interface is sub-interface of IItemPersistence for the particular DocumentOnServer data type.<br>
 * This Interface have to be implemented by classes responsible for the DocumentOnServer persistence.<br>
 * This class extends the {@link IItemPersistence} class.<br>
 * To get an instance of this interface please use the appropriate static method of the {@link PersistenceConnectionManagerFactory}. 
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
	public void unlockDocumentsUnderPathEditedByUserWithOptionalRecursivity(String path, String user, boolean recursive)throws Exception;
	

}
