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
import ch.ivyteam.ivy.addons.filemanager.listener.FileVersionActionEvent;
import ch.ivyteam.ivy.addons.filemanager.util.MethodArgumentsChecker;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;

public class FileLinkController extends AbstractFileLinkController {
	
	private IFileLinkPersistence fileLinkPersistence;
	private IFolderOnServerPersistence folderPersistence;
	private boolean linkToVersion = false;
	
	private static String FILELINK_SUFFIX = "_link";
	
	public FileLinkController(BasicConfigurationController configuration) throws Exception {
		this.fileLinkPersistence = PersistenceConnectionManagerFactory.getIFileLinkPersistenceInstance(configuration);
		if(this.fileLinkPersistence == null){
			throw new IllegalStateException("An Exception occurred while creating the fileLinkPersistence for the given configuration.");
		}
		this.folderPersistence = PersistenceConnectionManagerFactory.getIFolderOnServerPersistenceInstance(configuration);
		this.linkToVersion = configuration.isLinkToVersion();
	}

	@Override
	public FileLink getFileLink(long fileLinkId) throws Exception {
		return this.fileLinkPersistence.get(fileLinkId);
	}

	@Override
	public FileLink getFileLink(String fileLinkPath) throws Exception {
		return this.fileLinkPersistence.get(fileLinkPath);
	}

	@Override
	public FileLink getFileLinkWithJavaFile(long fileLinkId) throws Exception {
		return this.fileLinkPersistence.getFileLinkWithJavaFile(fileLinkId);
	}

	@Override
	public File getFileLinkContentAsIvyTempFile(FileLink fl) throws Exception {
		return this.fileLinkPersistence.getContentAsTempFile(fl);
	}

	@Override
	public List<FileLink> getFileLinksInDirectory(long directoryId, boolean recursive) throws Exception {
		return this.fileLinkPersistence.getListInDirectory(directoryId, recursive);
	}

	@Override
	public List<FileLink> getFileLinksForFile(long fileId) throws Exception {
		return this.fileLinkPersistence.getFileLinksForFile(fileId);
	}
	
	@Override
	public List<FileLink> getFileLinksByFileIdAndVersionNumber(long fileId,
			int versionNumber) throws Exception {
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
		if(doc == null || StringUtils.isBlank(doc.getFileID()) || StringUtils.isBlank(doc.getFilename()) || StringUtils.isBlank(doc.getPath())) {
			throw new IllegalArgumentException("The DocumentOnServer cannot be null and its fileID, filem=name and path properties must be set.");
		}
		if(StringUtils.isBlank(directoryPath)){
			directoryPath = PathUtil.getParentDirectoryPath(doc.getPath());
		}
		FolderOnServer fos = this.folderPersistence.get(directoryPath);
		if(fos == null) {
			throw new IllegalStateException(String.format("The directory with path %s for creating the file link cannot be found.", directoryPath));
		}
		if(StringUtils.isBlank(fileLinkName)) {
			
			fileLinkName = FileHandler.getFileNameWithoutExt(doc.getFilename()).concat(FILELINK_SUFFIX).concat(".")
					.concat(FileHandler.getFileExtension(doc.getFilename()));
			int i = 0;
			while(this.fileLinkPersistence.fileExist(directoryPath, fileLinkName)){
				if(i > 0) {
					fileLinkName = FileHandler.getFileNameWithoutExt(doc.getFilename()).concat(FILELINK_SUFFIX).concat(String.valueOf(i)).concat(".")
							.concat(FileHandler.getFileExtension(doc.getFilename()));
				}
				i++;
			}
		} else {
			if(this.fileLinkPersistence.fileExist(directoryPath, fileLinkName)) {
				throw new IllegalStateException(String.format("A file with name %s under %s already exists. Cannot create a FileLink with this name.", fileLinkName, directoryPath));
			}
		}
		FileLink fl = new FileLink();
		BeanUtils.copyProperties(fl, doc);
		fl.setFileLinkName(fileLinkName);
		fl.setDirectoryId(fos.getId());
		
		return createFileLink(fl);
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
		return this.fileLinkPersistence.update(fileLink);
	}
	

	@Override
	public FileLink moveFileLink(FileLink fileLink,
			String newDestinationDirectoryPath) throws Exception {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheStringArgumentIsNullOrEmpty("The newDestinationDirectoryPath cannot be null or empty.", newDestinationDirectoryPath);
		FolderOnServer fos = this.folderPersistence.get(newDestinationDirectoryPath);
		if(fos == null) {
			throw new IllegalStateException(String.format("The destination directory with path %s cannot be found.", newDestinationDirectoryPath));
		}
		fileLink.setDirectoryId(fos.getId());
		
		return this.fileLinkPersistence.update(fileLink);
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
		if(this.linkToVersion) {
			FileVersion fileVersion = eventObject.getFileVersion();
			int versionNumber =  fileVersion.getVersionNumber()-1;
			try {
				Ivy.log().info("Updating all the FileLinks attached to the fileid = {0}, version number = {1}",
						fileVersion.getFileid(), versionNumber);
				this.fileLinkPersistence.updateFileLinksVersionId(fileVersion.getFileid(), versionNumber);
			} catch (Exception e) {
				Ivy.log().error("An error occurred in fileVersionRolledback fileid = {0}, version number = {1}", 
						fileVersion.getFileid(), fileVersion.getVersionNumber());
			}
		}
	}

	@Override
	public void fileVersionRolledback(FileVersionActionEvent eventObject) {
		if(this.linkToVersion) {
			FileVersion fileVersion = eventObject.getFileVersion();
			try {
				Ivy.log().info("Updating all the FileLinks attached to the fileid = {0}, version number = {1}",
						fileVersion.getFileid(), fileVersion.getVersionNumber());
				this.fileLinkPersistence.updateFileLinksVersionId(fileVersion.getFileid(), fileVersion.getVersionNumber());
				int deletedVersionNumber = fileVersion.getVersionNumber() +1;
				for(FileLink fl : this.getFileLinksForFile(fileVersion.getFileid())) {
					if(fl.getLinkedVersionNumber() == deletedVersionNumber) {
						this.deleteFileLink(fl);
					}
				}
			} catch (Exception e) {
				Ivy.log().error("An error occurred in fileVersionRolledback fileid = {0}, version number = {1}", 
						fileVersion.getFileid(), fileVersion.getVersionNumber());
			}
		}
	}

	@Override
	public void fileVersiondeleted(FileVersionActionEvent eventObject) {
		if(this.linkToVersion) {
			FileVersion fileVersion = eventObject.getFileVersion();
			try {
				Ivy.log().info("Deleting all the FileLinks attached to the version id {0}", fileVersion.getId());
				this.fileLinkPersistence.deleteFileLinksForFileVersionId(fileVersion.getId());
			} catch (Exception e) {
				Ivy.log().error("An error occurred in fileVersionRolledback fileid = {0}, version number = {1}", 
						fileVersion.getFileid(), fileVersion.getVersionNumber());
			}
		}
		
	}
	
	

}
