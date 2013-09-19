/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.persistence;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileType;

/**
 * This Interface declares the methods that have to be implemented by classes responsible for the fileType persistence.<br>
 * This class extends the {@link IItemPersistence} class.<br>
 * To get an instance of this interface please use the appropriate static method of the {@link ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory}.
 * @author ec (ecomba@soreco.ch)
 * @since 09/08/2013
 * 
 */
public interface IFileTypePersistence extends IItemPersistence<FileType> {
	
	/**
	 * Can be used to get the filetype with its name and application name.<br>
	 * The uniqueDescriptor parameter must be built with the following convention:<br>
	 * name*SEPARATE*applicationname
	 */
	@Override
	public FileType get(String uniqueDescriptor) throws Exception;
	
	/**
	 * Returns all the filetypes corresponding to the given application name
	 * @param applicationName
	 * @return java.util.List<FileType>
	 * @throws Exception
	 */
	public java.util.List<FileType> getFileTypesWithAppName(String applicationName) throws Exception;
	
	/**
	 * returns all the existing filetypes.
	 * @return java.util.List<FileType>
	 * @throws Exception
	 */
	public java.util.List<FileType> getAllFileTypes() throws Exception;
	
	/**
	 * Returns all the documents that have the given filetype id.
	 * @param typeId
	 * @return java.util.List<DocumentOnServer>
	 * @throws Exception
	 */
	public java.util.List<DocumentOnServer> getDocumentsWithFileTypeId(long typeId) throws Exception;
	
	/**
	 * Set the given documentOnServer fileType with the fileType corresponding to the given filetype id.
	 * @param doc
	 * @param fileTypeId
	 * @return DocumentOnServer which filetype is set. 
	 * @throws Exception
	 */
	public DocumentOnServer setDocumentFileType(DocumentOnServer doc, long fileTypeId) throws Exception;

}
