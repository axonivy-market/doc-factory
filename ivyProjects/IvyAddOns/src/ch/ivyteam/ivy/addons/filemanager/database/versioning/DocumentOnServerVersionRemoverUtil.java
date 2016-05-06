package ch.ivyteam.ivy.addons.filemanager.database.versioning;

import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileManagementHandlersFactory;
import ch.ivyteam.ivy.addons.filemanager.ReturnedMessage;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler;
import ch.ivyteam.ivy.addons.filemanager.document.DocumentDeleteMessageBuilder;
import ch.ivyteam.ivy.addons.filemanager.util.MethodArgumentsChecker;

public class DocumentOnServerVersionRemoverUtil {
	
	private DocumentOnServerVersionRemoverUtil() { }

	public static List<List<DocumentOnServer>> splitDocumentListInRemovableLastDocumentVersionsAndNotRemovableLastDocumentVersions(java.util.List<DocumentOnServer> docs, 
			BasicConfigurationController config) throws Exception {
		
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheArgumentIsNull("The List<DocumentOnServer> " +
				"or the BasicConfigurationController arguments cannot be null in " +
				"DocumentOnServerVersionRemoverUtil.splitDocumentListInRemovableLastDocumentVersionsAndNotRemovableLastDocumentVersions.", docs, config);
		
		AbstractFileVersioningController fvc = FileManagementHandlersFactory.getFileVersioningControllerInstance(config);
		AbstractFileManagementHandler fmh = FileManagementHandlersFactory.getFileManagementHandlerInstance(config);
		
		java.util.List<DocumentOnServer> notRemovableLastDocumentVersions = new ArrayList<>();
		java.util.List<DocumentOnServer> removableLastDocumentVersions = new ArrayList<>();
		List<List<DocumentOnServer>> splittedLists = new ArrayList<>();

		for(DocumentOnServer doc : docs) {
			if(fmh.isDocumentOnServerLocked(doc)) {
				notRemovableLastDocumentVersions.add(doc);
			} else if(fvc.wasFileVersionArchived(Long.parseLong(doc.getFileID()), doc.getVersionnumber().intValue())) {
				notRemovableLastDocumentVersions.add(doc);
			} else {
				removableLastDocumentVersions.add(doc);
			}
		}
		
		splittedLists.add(0, removableLastDocumentVersions);
		splittedLists.add(1, notRemovableLastDocumentVersions);
		
		return splittedLists;
	}
	
	public static ReturnedMessage getMessageBeforeDocumentDeleteAction(java.util.List<DocumentOnServer> docs, 
			BasicConfigurationController config, boolean deleteLastVersionOnly) throws Exception {
		return DocumentDeleteMessageBuilder.buildMessageBeforeDocumentDelete(docs, config, deleteLastVersionOnly);
	}

}
