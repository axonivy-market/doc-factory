package ch.ivyteam.ivy.addons.filemanager.document;

import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileManagementHandlersFactory;
import ch.ivyteam.ivy.addons.filemanager.ReturnedMessage;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler;
import ch.ivyteam.ivy.addons.filemanager.database.filelink.AbstractFileLinkController;
import ch.ivyteam.ivy.addons.filemanager.database.filelink.FileLink;
import ch.ivyteam.ivy.addons.filemanager.database.versioning.AbstractFileVersioningController;
import ch.ivyteam.ivy.addons.filemanager.util.MethodArgumentsChecker;
import ch.ivyteam.ivy.environment.Ivy;

public final class DocumentDeleteMessageBuilder {

	private static String ALL_FILES_EDITED = "/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/information/noDocumentCanBeDeletedCauseEdition";
	private static String ALL_FILES_EDITED_OR_ARCHIVED = "/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/information/noDocumentCanBeDeletedCauseEditionOrArchived";
	private static String DELETE_FILES_CONFIRMATION = "/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/question/confirmDeleteFiles";
	private static String DELETE_FILES_CONFIRMATION_LIST_UNREMOVABLE = "/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/question/confirmDeleteFilesAndListUnremovableOnes";
	private static String DELETE_FILES_AND_VERSIONS_CONFIRMATION = "/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/question/confirmDeleteFileAndVersion";
	private static String DELETE_FILES_AND_VERSIONS_CONFIRMATION_LIST_UNREMOVABLE = "/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/question/confirmDeleteFileAndVersionListUnremovable";
	private static String DELETE_FILES_LAST_VERSION_CONFIRMATION = "/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/question/confirmDeleteLastVersion";
	private static String DELETE_FILES_LAST_VERSION_CONFIRMATION_LIST_UNREMOVABLE_ARCHIVE_PROTECTED = "/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/question/confirmDeleteLastVersionProtectionActivatedListUnremovable";
	private static String DELETE_FILES_LAST_VERSION_CONFIRMATION_LIST_UNREMOVABLE = "/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/question/confirmDeleteLastVersionListUnremovable";
	private static String DELETE_FILES_AND_VERSIONS_CONFIRMATION_LIST_UNREMOVABLE_ARCHIVE_PROTECTED = "/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/question/confirmDeleteFileAndVersionProtectionActivatedListUnremovable";
	private static String DELETE_FILES_LINKS_CONFIRMATION = "/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/fileLinks/confirmDeleteFileLink";
	private static String DELETE_FILES_LINKS_FROM_FILES_TO_DELETE = "/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/fileLinks/fileLinksWillBeDeleted";
	private static String DELETE_FILES_LINKS_FROM_FILES_VERSION_TO_DELETE = "/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/fileLinks/fileLinksToVersionWillBeDeleted";


	private DocumentDeleteMessageBuilder() { }

	protected static List<List<DocumentOnServer>> splitDocumentListInRemovableDocumentsAndNotRemovableDocuments(List<DocumentOnServer> docsToRemove, 
			BasicConfigurationController config, boolean onlyLastVersion) throws Exception {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheArgumentIsNull("The List<DocumentOnServer> " +
				"or the BasicConfigurationController arguments cannot be null in " +
				"DocumentRemoverUtil.splitDocumentListInRemovableDocumentsAndNotRemovableDocuments.", docsToRemove, config);

		List<DocumentOnServer> removableDocs = new ArrayList<>();
		List<DocumentOnServer> notRemovableDocs = new ArrayList<>();
		List<List<DocumentOnServer>> splittedLists = new ArrayList<>();
		AbstractFileVersioningController fvc = null;
		if(config.isFileArchiveProtectionEnabled()) {
			fvc = FileManagementHandlersFactory.getFileVersioningControllerInstance(config);
		}
		AbstractFileManagementHandler fmh = FileManagementHandlersFactory.getFileManagementHandlerInstance(config);
		for(DocumentOnServer doc: docsToRemove) {
			if(fmh.isDocumentOnServerLocked(doc)) {
				notRemovableDocs.add(doc);
			} else {
				if(fvc!=null && config.isFileArchiveProtectionEnabled()) {
					if(onlyLastVersion) {
						if(fvc.wasFileVersionArchived(Long.parseLong(doc.getFileID()), doc.getVersionnumber().intValue())) {
							notRemovableDocs.add(doc);
							continue;
						}
					} else if(doc.getVersionnumber().intValue()>1 || fvc.wasFileVersionArchived(Long.parseLong(doc.getFileID()), 1)) {
						notRemovableDocs.add(doc);
						continue;
					}
				}
				removableDocs.add(doc);
			}
		}
		splittedLists.add(0, removableDocs);
		splittedLists.add(1, notRemovableDocs);
		return splittedLists;
	}

	/**
	 * @param docsToRemove
	 * @param config
	 * @param removeOnlyLastDocumentVersion
	 * @return
	 * @throws Exception
	 */
	public static ReturnedMessage buildMessageBeforeDocumentDelete(List<DocumentOnServer> docsToRemove, 
			BasicConfigurationController config, boolean removeOnlyLastDocumentVersion) throws Exception {

		ReturnedMessage rm = new ReturnedMessage();
		ch.ivyteam.ivy.scripting.objects.List <DocumentOnServer> removableDocs = ch.ivyteam.ivy.scripting.objects.List.create(DocumentOnServer.class);
		
		if(config.isActivateFileLink() && areAllTheDocumentsFileLink(docsToRemove)) {
			removableDocs.addAll(docsToRemove);
			rm.setDocumentOnServers(removableDocs);
			rm.setText(Ivy.cms().co(DELETE_FILES_LINKS_CONFIRMATION));
			return rm;
		}
		
		List<List<DocumentOnServer>> split = splitDocumentListInRemovableDocumentsAndNotRemovableDocuments(docsToRemove, config, removeOnlyLastDocumentVersion);
		
		removableDocs.addAll(split.get(0));
		List<DocumentOnServer> notRemovableDocs = split.get(1);
		rm.setDocumentOnServers(removableDocs);
		// no files can be deleted
		if(removableDocs.isEmpty()) {
			return makeMessageNoFileCanBeRemoved(config, rm);
		}
		
		String messageForLinks = makeMessageIfSomeVersionLinksWillBeDeleted(config, removableDocs, removeOnlyLastDocumentVersion);
		
		// all the files can be deleted
		if(notRemovableDocs.isEmpty()) {
			if(config.isActivateFileVersioning()) {
				rm.setText(String.format((removeOnlyLastDocumentVersion? Ivy.cms().co(DELETE_FILES_LAST_VERSION_CONFIRMATION) : 
					Ivy.cms().co(DELETE_FILES_AND_VERSIONS_CONFIRMATION)), messageForLinks));
			} else {
				rm.setText(String.format(Ivy.cms().co(DELETE_FILES_CONFIRMATION), messageForLinks));
			}
			return rm;
		}
		
		String message = "";
		
		if(config.isActivateFileVersioning()) {
			if(config.isFileArchiveProtectionEnabled()) {
				message = removeOnlyLastDocumentVersion? 
						Ivy.cms().co(DELETE_FILES_LAST_VERSION_CONFIRMATION_LIST_UNREMOVABLE_ARCHIVE_PROTECTED) : 
							Ivy.cms().co(DELETE_FILES_AND_VERSIONS_CONFIRMATION_LIST_UNREMOVABLE_ARCHIVE_PROTECTED);
			} else {
				message = removeOnlyLastDocumentVersion? 
						Ivy.cms().co(DELETE_FILES_LAST_VERSION_CONFIRMATION_LIST_UNREMOVABLE) : 
							Ivy.cms().co(DELETE_FILES_AND_VERSIONS_CONFIRMATION_LIST_UNREMOVABLE);
			}
		} else {
			message = Ivy.cms().co(DELETE_FILES_CONFIRMATION_LIST_UNREMOVABLE);
		}
		message = String.format(message, messageForLinks);
		
		String s = "";
		for(DocumentOnServer doc: notRemovableDocs) {
			s+=doc.getFilename()+"<br />";
			Ivy.log().debug("Not removable doc: {0}", doc.getFilename());
		}
		message = message.replace("EDITEDFILES", s);
		rm.setText(message);
		return rm;
	}

	private static String makeMessageIfSomeVersionLinksWillBeDeleted(
			BasicConfigurationController config,
			ch.ivyteam.ivy.scripting.objects.List<DocumentOnServer> removableDocs, boolean removeOnlyLastDocumentVersion) throws Exception {
		if(!config.isActivateFileLink()) {
			return "";
		}
		AbstractFileLinkController flc = FileManagementHandlersFactory.getFileLinkControllerInstance(config);
		boolean deleteFileLinks = false;
		for(DocumentOnServer doc: removableDocs) {
			List<FileLink> fileLinks;
			if(removeOnlyLastDocumentVersion) {
				fileLinks = flc.getFileLinksByFileIdAndVersionNumber(Long.parseLong(doc.getFileID()), doc.getVersionnumber().intValue());
				Ivy.log().info("Check last version links: {0} {1} {2}",doc.getFileID(), doc.getVersionnumber(), fileLinks.isEmpty());
			} else {
				fileLinks = flc.getFileLinksForFile(Long.parseLong(doc.getFileID()));
				Ivy.log().info("Check files links: {0} {1}",doc.getFileID(), fileLinks.isEmpty());
			}
			if(!fileLinks.isEmpty()) {
				deleteFileLinks = true;
				break;
			}
		}
		String message = "";
		if(deleteFileLinks) {
			message = removeOnlyLastDocumentVersion? Ivy.cms().co(DELETE_FILES_LINKS_FROM_FILES_VERSION_TO_DELETE) :
				Ivy.cms().co(DELETE_FILES_LINKS_FROM_FILES_TO_DELETE);
			message += "<BR>";
		}
		Ivy.log().debug(message);
		return message;
	}

	private static ReturnedMessage makeMessageNoFileCanBeRemoved(
			BasicConfigurationController config, ReturnedMessage rm) {
		if(config.isFileArchiveProtectionEnabled()) {
			rm.setText(Ivy.cms().co(ALL_FILES_EDITED_OR_ARCHIVED));
		} else {
			rm.setText(Ivy.cms().co(ALL_FILES_EDITED));
		}
		return rm;
	}
	
	private static boolean areAllTheDocumentsFileLink(List<DocumentOnServer> docs) {
		if(docs.isEmpty()) {
			return false;
		}
		for(DocumentOnServer doc: docs) {
			if(!(doc instanceof FileLink)) {
				return false;
			}
		}
		return true;
	}


}
