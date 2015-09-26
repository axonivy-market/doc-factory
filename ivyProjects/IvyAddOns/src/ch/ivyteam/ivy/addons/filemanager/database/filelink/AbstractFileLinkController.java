package ch.ivyteam.ivy.addons.filemanager.database.filelink;

import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.listener.FileVersionActionListener;
import ch.ivyteam.ivy.scripting.objects.File;

public abstract class AbstractFileLinkController implements FileVersionActionListener{
	
	public abstract FileLink getFileLink(long fileLinkId) throws Exception;
	
	public abstract FileLink getFileLink(String fileLinkPath) throws Exception;
	
	public abstract FileLink getFileLinkWithJavaFile(long fileLinkId) throws Exception;
	
	public abstract File getFileLinkContentAsIvyTempFile(FileLink fl) throws Exception;
	
	public abstract List<FileLink> getFileLinksInDirectory(long directoryId, boolean recursive) throws Exception;
	
	public abstract List<FileLink> getFileLinksUnderPath(String path, boolean recursive) throws Exception;
	
	public abstract List<FileLink> getFileLinksForFile(long fileId) throws Exception;
	
	/**
	 * returns all the FileLinks linked to the given file id and to the given version number.
	 * @param fileId
	 * @param versionNumber
	 * @return
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
	
	public abstract FileLink updateFileLink(FileLink fileLink) throws Exception;
	
	public abstract FileLink moveFileLink(FileLink fileLink, String newDestinationDirectoryPath) throws Exception;
	
	public abstract boolean deleteFileLink(FileLink fileLink) throws Exception;
	
	public abstract int deleteFileLinksInDirectory(long directoryId) throws Exception;
	
	public abstract int deleteFileLinksForFile(long fileId) throws Exception;
	
	public abstract int deleteFileLinksForFileVersion(long fileVersionId) throws Exception;

}
