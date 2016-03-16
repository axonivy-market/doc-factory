package ch.ivyteam.ivy.addons.filemanager.database.filelink;

import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.listener.FileVersionActionListener;
import ch.ivyteam.ivy.scripting.objects.File;

public abstract class AbstractFileLinkController implements FileVersionActionListener{
	
	/**
	 * returns the FileLink by the given id.
	 * @param fileLinkId the file link id to search for. Must be greater than 0.
	 * @return the found FileLink or null.
	 * @throws Exception
	 */
	public abstract FileLink getFileLink(long fileLinkId) throws Exception;
	
	/**
	 * Returns the fileLink by its path.
	 * @param fileLinkPath the path of the fileLink to search for. Cannot be blank.
	 * @return the found FileLink at the given path or null if not found.
	 * @throws Exception
	 */
	public abstract FileLink getFileLink(String fileLinkPath) throws Exception;
	
	/**
	 * returns the FileLink by the given id with the fileLink content deserialize as File. The FileLink javaFile property is set with this File.
	 * @param fileLinkId the file link id to search for. Must be greater than 0.
	 * @return the found FileLink with the content deserialiszed as java file or null.
	 * @throws Exception
	 */
	public abstract FileLink getFileLinkWithJavaFile(long fileLinkId) throws Exception;
	
	/**
	 * returns a temp ivy File corresponding to the given FileLink.
	 * @param fl the FileLink which content should be deserialized in a temp Ivy File
	 * @return a seesion temp Ivy File
	 * @throws Exception
	 */
	public abstract File getFileLinkContentAsIvyTempFile(FileLink fl) throws Exception;
	
	/**
	 * get all the FileLinks located in the directory denoted by the given id.
	 * @param directoryId the dierectoryId, must be greater than 0.
	 * @param recursive if true, all FIleLinks located in the children directories will be returned.
	 * @return a java.util.List of FileLinks found. Empty List if no FileLinks are present in the directory.
	 * @throws Exception
	 */
	public abstract List<FileLink> getFileLinksInDirectory(long directoryId, boolean recursive) throws Exception;
	
	/**
	 * Same as {@link #getFileLinksInDirectory(long, boolean)} but with the directory denoted by the given path.
	 * @param path the path of the directory. Cannot be blank.
	 * @param recursive if true, all FIleLinks located in the children directories will be returned.
	 * @return a java.util.List of FileLinks found. Empty List if no FileLinks are present in the directory.
	 * @throws Exception
	 */
	public abstract List<FileLink> getFileLinksUnderPath(String path, boolean recursive) throws Exception;
	
	/**
	 * Get all the fileLinks that link to the File denoted by the given id.
	 * @param fileId the file ID. Must be greater than zero.
	 * @return a java.util.List of FileLinks that link to the file. Empty List if no FileLinks are present in the directory.
	 * @throws Exception
	 */
	public abstract List<FileLink> getFileLinksForFile(long fileId) throws Exception;
	
	/**
	 * returns all the FileLinks linked to the given file id and to the given version number.
	 * @param fileId the file ID. Must be greater than zero.
	 * @param versionNumber the version number. Must be gretaer than zero.
	 * @return a java.util.List of FileLinks that link to the file's given version. Empty List if no FileLinks are present in the directory.
	 * @throws Exception
	 */
	public abstract List<FileLink> getFileLinksByFileIdAndVersionNumber(long fileId, int versionNumber) throws Exception;
	
	/**
	 * Used for updating the fileVersionId of the FileLink to the file version corresponding to the given fileid with versionNumber.<br />
	 * It is called by the API whenever a file version is rolledback as active version or is pushed in the versioning sstem as version. 
	 * If the actual version is pushed to the file versioning system, then the existing FileLinks with the actual active version should link to the file version.<br />
	 * If a version is rolledback as active version, then the FileLinks should link to the active document (the fileLink versionId will be zero).<br />
	 * After this operation, calling the FileLink isReferenceVersion() method should return true if the FileLink links to a version in the versioning system, else false.;
	 * @param fileId
	 * @param versionNumber
	 * @return the number of FileLinks modified.
	 * @throws Exception
	 */
	public abstract int updateFileLinksVersionId(long fileId, int versionNumber) throws Exception;
	
	/**
	 * returns a list of the file links, linked to a particular file version. 
	 * @param fileVersionId the file version id. Must be greater than 0.
	 * @return list of FileLink
	 * @throws Exception
	 */
	public abstract List<FileLink> getFileLinksForFileVersion(long fileVersionId) throws Exception;
	
	public abstract FileLink createFileLink(FileLink fileLink) throws Exception;
	
	/**
	 * creates a new FileLink for the given DocumentOnServer under the given directory path.<br />
	 * If the directory path is null or empty, then the FileLink will be created under the same directory as the document.<br />
	 * If the directoryPath is given, then the corresponding directory MUST already exist.<br />
	 * If the fileLinkName is null or empty, a generic name based on the document name will be used.<br />
	 * @param doc The DocumentOnServer which should be linked. Cannot be null. Its Id, path and name properties must be set.
	 * @param fileLinkName the fileLink name. If null or empty, a generic name based on the document name will be used.
	 * @param directoryPath the directory path. 
	 * If the directory path is null or empty, then the FileLink will be created under the same directory as the document.<br />
	 * If the directoryPath is given, then the corresponding directory MUST already exist.
	 * @return the created FileLink.
	 * @throws Exception
	 */
	public abstract FileLink createFileLinkForDocumentOnServer(DocumentOnServer doc, String fileLinkName, String directoryPath) throws Exception;
	
	/**
	 * Paste the given copied file link to the destination path.
	 * @param copiedFileLink
	 * @param destinationDirectoryPath
	 * @return the new pasted FileLink
	 * @throws Exception
	 */
	public abstract FileLink pasteCopiedFileLinkToDestination(FileLink copiedFileLink, String destinationDirectoryPath) throws Exception;
	
	/**
	 * Updates the given FileLink
	 * @param fileLink FileLink to update. Its Id, FileId, and directoryId properties must be set. 
	 * @return updated FileLink
	 * @throws Exception
	 */
	public abstract FileLink updateFileLink(FileLink fileLink) throws Exception;
	
	/**
	 * move a FileLink to the given destination.
	 * @param fileLink the FIleLink to move.
	 * @param newDestinationDirectoryPath
	 * @return the moved FileLink.
	 * @throws Exception
	 */
	public abstract FileLink moveFileLink(FileLink fileLink, String newDestinationDirectoryPath) throws Exception;
	
	/**
	 * delete the given FileLink
	 * @param fileLink the FileLink to delete. 
	 * @return true if the fileLink to deleted has been deleted, else false.
	 * @throws Exception
	 */
	public abstract boolean deleteFileLink(FileLink fileLink) throws Exception;
	
	/**
	 * deletes all the fileLinks in the given directory.
	 * @param directoryId the directory id where the fielLink has to be deleted
	 * @return the number of FileLinks deleted.
	 * @throws Exception
	 */
	public abstract int deleteFileLinksInDirectory(long directoryId) throws Exception;
	
	/**
	 * Deletes all the FileLinks attached to the file denoted by the given id.
	 * @param fileId the file id which links should be deleted.
	 * @return the number of FileLinks deleted. Zero if the file does not have any FileLinks.
	 * @throws Exception
	 */
	public abstract int deleteFileLinksForFile(long fileId) throws Exception;
	
	/**
	 * Deletes all the FileLinks attached to the file version denoted by the given id.
	 * @param fileVersionId the file version id which links should be deleted.
	 * @return  the number of FileLinks deleted. Zero if FileLinks are linked to the given file version.
	 * @throws Exception
	 */
	public abstract int deleteFileLinksForFileVersion(long fileVersionId) throws Exception;

}
