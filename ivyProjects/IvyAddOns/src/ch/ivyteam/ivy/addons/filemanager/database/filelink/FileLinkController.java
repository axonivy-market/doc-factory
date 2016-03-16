package ch.ivyteam.ivy.addons.filemanager.database.filelink;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.FileVersion;
import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileLinkPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFolderOnServerPersistence;
import ch.ivyteam.ivy.addons.filemanager.exception.FileManagementException;
import ch.ivyteam.ivy.addons.filemanager.listener.FileVersionActionEvent;
import ch.ivyteam.ivy.addons.filemanager.util.MethodArgumentsChecker;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;

public class FileLinkController extends AbstractFileLinkController {

	private IFileLinkPersistence fileLinkPersistence;
	private IFolderOnServerPersistence folderPersistence;
	private boolean linkToVersion = false;

	private static String FILELINK_SUFFIX = "link_";

	public FileLinkController(BasicConfigurationController configuration) throws Exception {
		this.fileLinkPersistence = PersistenceConnectionManagerFactory.getIFileLinkPersistenceInstance(configuration);
		if(this.fileLinkPersistence == null){
			throw new IllegalStateException("An Exception occurred while creating the fileLinkPersistence for the given configuration.");
		}
		this.folderPersistence = PersistenceConnectionManagerFactory.getIFolderOnServerPersistenceInstance(configuration);
		this.linkToVersion = configuration.isLinkToVersion();
	}
	
	/**
	 * For unit tests only
	 */
	protected FileLinkController() {
	}
	
	/**
	 * For unit tests only
	 * @param fileLinkPersistence
	 * @param folderPersistence
	 */
	protected FileLinkController(IFileLinkPersistence fileLinkPersistence, IFolderOnServerPersistence folderPersistence) {
		this.fileLinkPersistence = fileLinkPersistence;
		this.folderPersistence = folderPersistence;
	}

	@Override
	public FileLink getFileLink(long fileLinkId) throws Exception {
		checkNumericValueGreaterThanZero(fileLinkId, "getFileLink", "fileLinkId");
		return this.fileLinkPersistence.get(fileLinkId);
	}

	@Override
	public FileLink getFileLink(String fileLinkPath) throws Exception {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheStringArgumentIsNullOrEmpty("The path cannot be null or empty.", fileLinkPath);
		
		return this.fileLinkPersistence.get(fileLinkPath);
	}

	@Override
	public FileLink getFileLinkWithJavaFile(long fileLinkId) throws Exception {
		checkNumericValueGreaterThanZero(fileLinkId, "getFileLinkWithJavaFile", "fileLinkId");
		
		return this.fileLinkPersistence.getFileLinkWithJavaFile(fileLinkId);
	}

	@Override
	public File getFileLinkContentAsIvyTempFile(FileLink fl) throws Exception {
		return this.fileLinkPersistence.getContentAsTempFile(fl);
	}

	@Override
	public List<FileLink> getFileLinksInDirectory(long directoryId, boolean recursive) throws Exception {
		checkNumericValueGreaterThanZero(directoryId, "getFileLinksInDirectory", "directoryId");
		
		return this.fileLinkPersistence.getListInDirectory(directoryId, recursive);
	}

	@Override
	public List<FileLink> getFileLinksForFile(long fileId) throws Exception {
		checkNumericValueGreaterThanZero(fileId, "getFileLinksForFile", "fileId");
		
		return this.fileLinkPersistence.getFileLinksForFile(fileId);
	}

	@Override
	public List<FileLink> getFileLinksByFileIdAndVersionNumber(long fileId,
			int versionNumber) throws Exception {
		checkNumericValueGreaterThanZero(fileId, "getFileLinksByFileIdAndVersionNumber", "fileId");
		checkNumericValueGreaterThanZero(versionNumber, "getFileLinksByFileIdAndVersionNumber", "versionNumber");
		
		List<FileLink> fileLinks = new ArrayList<>();
		for(FileLink fl: getFileLinksForFile(fileId)) {
			if(fl.getLinkedVersionNumber() == versionNumber) {
				fileLinks.add(fl);
			}
		}
		return fileLinks;
	}

	@Override
	public int updateFileLinksVersionId(long fileId, int versionNumber)
			throws Exception {
		checkNumericValueGreaterThanZero(fileId, "getFileLinksForFile", "fileId");
		checkNumericValueGreaterThanZero(versionNumber, "getFileLinksForFile", "versionNumber");
		
		return this.fileLinkPersistence.updateFileLinksVersionId(fileId, versionNumber);
	}

	@Override
	public List<FileLink> getFileLinksForFileVersion(long fileVersionId) throws Exception {
		return this.fileLinkPersistence.getFileLinksForFileVersion(fileVersionId);
	}

	@Override
	public List<FileLink> getFileLinksUnderPath(String path, boolean recursive) throws Exception {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheStringArgumentIsNullOrEmpty("The path cannot be null or empty", path);
		FolderOnServer fos = this.folderPersistence.get(path);
		if(fos == null){
			return new ArrayList<FileLink>();
		}
		return this.fileLinkPersistence.getListInDirectory(fos.getId(), recursive);
	}

	@Override
	public FileLink createFileLink(FileLink fileLink) throws Exception {
		return this.fileLinkPersistence.create(fileLink);
	}

	@Override
	public FileLink createFileLinkForDocumentOnServer(DocumentOnServer doc,
			String fileLinkName, String directoryPath) throws Exception {
		
		if(doc == null || StringUtils.isBlank(doc.getFilename()) || StringUtils.isBlank(doc.getPath())) {
			throw new IllegalArgumentException("The DocumentOnServer cannot be null and its filename and path properties must be set.");
		}
		checkDocumentOnServerId(doc.getFileID(), "createFileLinkForDocumentOnServer");
		
		if(StringUtils.isBlank(directoryPath)){
			directoryPath = PathUtil.getParentDirectoryPath(doc.getPath());
		}
		
		FolderOnServer fos = this.folderPersistence.get(directoryPath);
		if(fos == null) {
			throw new IllegalStateException(String.format("The directory with path %s for creating the file link cannot be found.", directoryPath));
		}
		
		fileLinkName = makeFileLinkName(doc, fileLinkName, directoryPath);
		
		FileLink fl = new FileLink();
		BeanUtils.copyProperties(fl, doc);
		fl.setFileLinkName(fileLinkName);
		fl.setDirectoryId(fos.getId());

		return createFileLink(fl);
	}

	private String makeFileLinkName(DocumentOnServer doc, String fileLinkName, String directoryPath)
			throws Exception {
		String fileExtension = "." + FileHandler.getFileExtension(doc.getFilename());
		if(StringUtils.isBlank(fileLinkName)) {
			fileLinkName = FILELINK_SUFFIX + FileHandler.getFileNameWithoutExt(doc.getFilename()) + fileExtension;
			int i = 0;
			while(this.fileLinkPersistence.fileExist(directoryPath, fileLinkName)){
				if(i > 0) {
					fileLinkName = FILELINK_SUFFIX + 
							FileHandler.getFileNameWithoutExt(doc.getFilename()) + 
							String.valueOf(i) + 
							fileExtension;
				}
				i++;
			}
		} else {
			if(!fileLinkName.toLowerCase().endsWith(fileExtension)) {
				fileLinkName = fileLinkName + fileExtension;
			}
			if(this.fileLinkPersistence.fileExist(directoryPath, fileLinkName)) {
				throw new FileManagementException(String.format("A file with name %s already exists under %s. Cannot create a FileLink with this name.", fileLinkName, directoryPath));
			}
		}
		
		return fileLinkName;
	}

	@Override
	public FileLink pasteCopiedFileLinkToDestination(FileLink copiedFileLink,
			String destinationDirectoryPath) throws Exception {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheStringArgumentIsNullOrEmpty("The destinationDirectoryPath cannot be null or empty.", destinationDirectoryPath);
		FileLinkValidator.throwIllegalArgumentExceptionIfInvalid(copiedFileLink, true);

		FolderOnServer fos = this.folderPersistence.get(destinationDirectoryPath);
		if(fos == null) {
			throw new IllegalStateException(String.format("The destination directory with past %s cannot be found.", destinationDirectoryPath));
		}

		FileLink fileLinkToPaste = new FileLink();
		BeanUtils.copyProperties(fileLinkToPaste, copiedFileLink);

		fileLinkToPaste.setDirectoryId(fos.getId());

		return this.createFileLink(fileLinkToPaste);
	}

	@Override
	public FileLink updateFileLink(FileLink fileLink) throws Exception {
		FileLinkValidator.throwIllegalArgumentExceptionIfInvalid(fileLink, true);
		return this.fileLinkPersistence.update(fileLink);
	}


	@Override
	public FileLink moveFileLink(FileLink fileLink,
			String newDestinationDirectoryPath) throws Exception {
		FileLinkValidator.throwIllegalArgumentExceptionIfInvalid(fileLink, true);
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheStringArgumentIsNullOrEmpty("The newDestinationDirectoryPath cannot be null or empty.", newDestinationDirectoryPath);
		FolderOnServer fos = this.folderPersistence.get(newDestinationDirectoryPath);
		if(fos == null) {
			throw new IllegalStateException(String.format("The destination directory with path %s cannot be found.", newDestinationDirectoryPath));
		}

		return this.fileLinkPersistence.moveFileLink(fileLink, fos.getId());
	}

	@Override
	public boolean deleteFileLink(FileLink fileLink) throws Exception {
		return this.fileLinkPersistence.delete(fileLink);
	}

	@Override
	public int deleteFileLinksInDirectory(long directoryId) throws Exception {
		return this.fileLinkPersistence.deleteFileLinksInDirectory(directoryId);
	}

	@Override
	public int deleteFileLinksForFile(long fileId) throws Exception {
		return this.fileLinkPersistence.deleteFileLinksForFileId(fileId);
	}

	@Override
	public int deleteFileLinksForFileVersion(long fileVersionId)
			throws Exception {
		return this.fileLinkPersistence.deleteFileLinksForFileVersionId(fileVersionId);
	}

	@Override
	public void fileVersionCreated(FileVersionActionEvent eventObject) {
		FileVersion createdFileVersion = eventObject.getFileVersion();
		try {
			if(this.linkToVersion) {
				int formerVersionNumber =  createdFileVersion.getVersionNumber()-1;

				Ivy.log().debug("Updating all the FileLinks attached to the fileid = {0}, version number = {1}",
						createdFileVersion.getFileid(), formerVersionNumber);
				this.fileLinkPersistence.updateFileLinksVersionId(createdFileVersion.getFileid(), formerVersionNumber);

			} else {
				Ivy.log().debug("Updating all the FileLinks attached to the fileid = {0}, with new version number = {1}",
						createdFileVersion.getFileid(), createdFileVersion.getVersionNumber());
				updateVersionNumberOnFileLinkFoundByFileId(
						createdFileVersion.getVersionNumber(), createdFileVersion.getFileid());
			}
		} catch (Exception e) {
			Ivy.log().error("An error occurred in fileVersionRolledback fileid = {0}, version number = {1}", 
					createdFileVersion.getFileid(), createdFileVersion.getVersionNumber());
		}
	}

	@Override
	public void fileVersionRolledback(FileVersionActionEvent eventObject) {
		FileVersion fileVersion = eventObject.getFileVersion();
		int rolledBackVersionNumber = fileVersion.getVersionNumber();
		long fileId = fileVersion.getFileid();
		try {
			if(this.linkToVersion) {
				Ivy.log().debug("Updating all the FileLinks attached to the fileid = {0}, version number = {1}",
						fileId, rolledBackVersionNumber);
				this.fileLinkPersistence.updateFileLinksVersionId(fileId, rolledBackVersionNumber);
				int deletedVersionNumber = rolledBackVersionNumber +1;
				for(FileLink fl : this.getFileLinksForFile(fileId)) {
					if(fl.getLinkedVersionNumber() == deletedVersionNumber) {
						this.deleteFileLink(fl);
					}
				}
			} else {
				updateVersionNumberOnFileLinkFoundByFileId(
						rolledBackVersionNumber, fileId);
			}
		} catch (Exception e) {
			Ivy.log().error("An error occurred in fileVersionRolledback fileid = {0}, version number = {1}", 
					fileId, rolledBackVersionNumber);
		}
	}

	private void updateVersionNumberOnFileLinkFoundByFileId(
			int newVersionNumber, long fileId) throws Exception {
		for(FileLink fl : this.fileLinkPersistence.getFileLinksForFile(fileId)) {
			fl.setVersionnumber(newVersionNumber);
			this.fileLinkPersistence.update(fl);
		}
	}

	@Override
	public void fileVersiondeleted(FileVersionActionEvent eventObject) {
		if(this.linkToVersion) {
			FileVersion fileVersion = eventObject.getFileVersion();
			try {
				Ivy.log().debug("Deleting all the FileLinks attached to the version id {0}", fileVersion.getId());
				this.fileLinkPersistence.deleteFileLinksForFileVersionId(fileVersion.getId());
			} catch (Exception e) {
				Ivy.log().error("An error occurred in fileVersionRolledback fileid = {0}, version number = {1}", 
						fileVersion.getFileid(), fileVersion.getVersionNumber());
			}
		}

	}

	private void checkNumericValueGreaterThanZero(long id, String methodName, String inputName) {
		if(StringUtils.isBlank(inputName)) {
			inputName = "id";
		}
		if(id <= 0) {
			throw new IllegalArgumentException(String.format("The given %s must be greater than zero. Method %s in class %s", 
					inputName, methodName, this.getClass()));
		}
	}
	
	private void checkDocumentOnServerId(String id, String methodName) {
		boolean valid = true;
		if(StringUtils.isBlank(id)) {
			valid = false;
		}
		try {
			Long.parseLong(id);
		} catch (NumberFormatException ex) {
			valid = false;
		}
		if(!valid) {
			throw new IllegalArgumentException(String.format("The DocumentOnServer has an invalid id %s. It must represent a number greater than zero. Method %s in class %s", 
					methodName, this.getClass()));
		}
	}
	
}
