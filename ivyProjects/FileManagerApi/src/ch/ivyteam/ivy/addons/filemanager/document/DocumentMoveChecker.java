package ch.ivyteam.ivy.addons.filemanager.document;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.ReturnedMessage;
import ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler;
import ch.ivyteam.ivy.addons.filemanager.database.filelink.FileLink;
import ch.ivyteam.ivy.addons.filemanager.util.MethodArgumentsChecker;
import ch.ivyteam.ivy.environment.Ivy;

public final class DocumentMoveChecker {
	
	private DocumentMoveChecker() {}
	
	public static List<DocumentOnServer> getFilesThatCannotBeMoved(List<DocumentOnServer> filesThatShouldBeMoved, 
			String destinationPath, AbstractFileManagementHandler fmHandler) throws Exception {
		if(fmHandler == null || StringUtils.isBlank(destinationPath) || filesThatShouldBeMoved == null) {
			throw new IllegalArgumentException("The files List to move, the FileManagementHandler or the destinationPath is not properly set");
		}
		List<DocumentOnServer> docs = fmHandler.getDocumentOnServersInDirectory(destinationPath, false);
		List<DocumentOnServer> filesThatCannotBeMoved = new ArrayList<>();
		check:
		for(DocumentOnServer doc: filesThatShouldBeMoved){
			if(doc instanceof FileLink) {
				continue;
			}
			if(doc.getLocked().equals("1")) {
				filesThatCannotBeMoved.add(doc);
				continue;
			} else {
				//I look at all the documents in the destination Folder
				for(DocumentOnServer doc1: docs) {
					//if document locked in destination Folder, and should be overwriten,
					// we don't move
					if(doc1.getLocked().equals("1")) {
						if(doc.getFilename().equals(doc1.getFilename())) {
							filesThatCannotBeMoved.add(doc);
							continue check;
						}
					}		
				}
			}
			
		}
		return filesThatCannotBeMoved;
	}
	
	public static ReturnedMessage checkIfFileToMoveWillOverwriteExistingFile(DocumentOnServer fileToMove, 
			String destinationPath, AbstractFileManagementHandler fmHandler) throws Exception {
		if(fmHandler == null || StringUtils.isBlank(destinationPath) || fileToMove == null) {
			throw new IllegalArgumentException("The file to move, the FileManagementHandler or the destinationPath is not properly set");
		}
		ReturnedMessage result = new ReturnedMessage("", ReturnedMessage.SUCCESS_MESSAGE);
		if(fmHandler.documentOnServerExists(fileToMove, destinationPath)) {
			result.setType(ReturnedMessage.INFORMATION_MESSAGE);
			result.setDocumentOnServer(fileToMove);
		}
		return result;
	}	
}
