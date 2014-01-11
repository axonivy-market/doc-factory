/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.filetype;

import java.sql.SQLException;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileType;
import ch.ivyteam.ivy.persistence.PersistencyException;

/**
 * @author ec
 *
 */
public abstract class AbstractFileTypesController {
	
	
	
	
	/**
	 * Creates a new FileType.
	 * @param name: String the name of the file type
	 * @param optionalApplicationName: String the application name for which the file type is created. can be empty or null
	 * @return the newly created FileType object. Its id will be greater than zero.
	 * @throws Exception if any SQL Exceptions occur or the name argument is null or an empty String.<br> 
	 * An SQLException will be also thrown if a FileType with the same name and same applicationName already exists.
	 */
	public abstract FileType createFileType(String name, String optionalApplicationName) throws Exception;
	
	/**
	 * Modify an existing FileType.<br>
	 * If the given FileType does not exists (its id = 0) then tries to create a new one
	 * @param ft The FilteType to modify. Its filetypeName and applicationName will be taken to modify the FileType with its id.<br>
	 * If the id is 0 then it will try to create a new one.
	 * @return the modified or created FileType object.
	 * @throws Exception if any SQL Exceptions occur or the FileType name is null or an empty String.<br> 
	 * An SQLException will be also thrown if a FileType with the same name and same applicationName already exists.
	 */
	public abstract FileType modifyFileType(FileType ft) throws Exception;
	
	/**
	 * Delete the FileType corresponding to the given id.<br>
	 * <b>You must be sure about what you are doing:</b> <br>
	 * this method deletes the file type <b>BUT ALSO</b> set the fileType id on the associated documents to zero.
	 * @param fileTypeId : the id of the file type to delete
	 * @throws Exception
	 */
	public abstract void deleteFileType(long fileTypeId) throws Exception;
	
	/**
	 * Returns the FileType corresponding to the given id.<br>
	 * @param fileTypeId the FileType id
	 * @return
	 * @throws Exception 
	 * @throws SQLException 
	 * @throws PersistencyException 
	 */
	public abstract FileType getFileTypeWithId(long fileTypeId) throws Exception;
	
	/**
	 * Search a file type with the given name and optionally the given application name.<br>
	 * If no application name is given, this will return the file type associated with the given name and an empty application name.
	 * File types can be created for several applications and stored in the same table.<br> 
	 * A given file type name must be unique for a given application.<br> File types with the same name can be created for different applications.
	 * @param typeName the name of the fileType to search
	 * @param optionalApplicationName the name of the application (not mandatory)
	 * @return a FileType corresponding to the name and application that you provided as parameters.<br> 
	 * If no file type has the given name and application name, then the returned FileType has empty field values.
	 * @throws PersistencyException
	 * @throws SQLException
	 * @throws Exception
	 */
	public abstract FileType getFileTypeWithName(String typeName, String optionalApplicationName) throws Exception;
	
	/**
	 * returns all the file types that where created for one application.<br>
	 * If the provided applicationName is null or an empty String, it will return all the file types without application name information.
	 * @param applicationName the application name (ex: xcrm, xrec, xjob, epd, ...) or null if you want to get all the file types without application name.
	 * @return java.util.List<FileType> the list of all the fileTypes declared for one application or without any application link if the input parameter was set to null.
	 * @throws PersistencyException
	 * @throws SQLException
	 * @throws Exception
	 */
	public abstract java.util.List<FileType> getFileTypesWithAppName(String applicationName) throws Exception;
	
	/**
	 * returns all the file types
	 * @return the list of file types
	 * @throws PersistencyException
	 * @throws SQLException
	 * @throws Exception
	 */
	public abstract java.util.List<FileType> getAllFileTypes() throws Exception;
	
	/**
	 * returns all the documentOnServer objects corresponding to the given file type name and application name.<br>
	 * If you do not provide an application name this will return all the documents associated with the given file type and a null value as application name.
	 * @param typeName the type name
	 * @param optionalApplicationName the application name, null is accepted
	 * @return a java.util.List<DocumentOnServer> List of documents that have the corresponding file Type.<br>
	 * Empty if the file type cannot be found or no documents are associated with this type.
	 * @throws Exception
	 */
	public abstract java.util.List<DocumentOnServer> getDocumentsWithFileTypeName(String typeName, String optionalApplicationName) throws Exception;
	
	/**
	 * returns all the documentOnServer objects which FileType has the given applicationName.<br>
	 * If the given applicationName is null or an empty String, the method 
	 * @param applicationName
	 * @return
	 * @throws Exception
	 */
	public abstract java.util.List<DocumentOnServer> getDocumentsWithApplicationName(String applicationName) throws Exception;
	
	/**
	 * returns all the DocumentOnServer objects having the given FIleTypeId
	 * @param typeId
	 * @return
	 * @throws Exception
	 */
	public abstract java.util.List<DocumentOnServer> getDocumentsWithFileTypeId(long typeId) throws Exception;
	
	/**
	 * Fills each DocumentOnServer object from the given list with its corresponding FileType object if any.
	 * @param _docs a List of DocumentOnServers
	 * @return the list of DocumentOnServers, each DocumentOnServer object as a Filetype if any has been found for it. 
	 */
	public abstract java.util.List<DocumentOnServer> completeDocumentsWithFileTypes(java.util.List<DocumentOnServer> _docs);
	
	/**
	 * Set the fileType on given documentOnServer Object. The only needed information in the DocumentOnServer is its FileId attribute.
	 * If the given fileTypeId is 0, then the DocumentOnServer object will not be associated with any FileType.<br>
	 * If no FileType corresponds to the given fileTypeId, then the DocumentOnServer object will not be associated with any FileType.
	 * @param doc : the DocumentOnServer object
	 * @param fileTypeId: the fileTypeId
	 * @return the DocumentOnServer whose FileType attribute has been updated
	 * @throws Exception
	 */
	public abstract DocumentOnServer setDocumentFileType(DocumentOnServer doc, long fileTypeId) throws Exception;
	
	/**
	 * Set the fileType on given documentOnServer Object. The only needed information in the DocumentOnServer is its FileId attribute.
	 * If the given fileTypeId is 0, then the DocumentOnServer object will not be associated with any FileType.<br>
	 * If no FileType corresponds to the given fileTypeId, then the DocumentOnServer object will not be associated with any FileType.
	 * @param doc : the DocumentOnServer object
	 * @param fileTypeId: the fileTypeId
	 * @param con the java.sql.Connection object used to communicate with the database.<br>
	 * <b>IMPORTANT: </b>This method does NOT release this Connection. The method that calls this method has to do it.
	 * @return the DocumentOnServer whose FileType attribute has been updated
	 * @throws Exception
	 */
	@Deprecated
	public abstract DocumentOnServer setDocumentFileType(DocumentOnServer doc, long fileTypeId, java.sql.Connection con) throws Exception;
}
