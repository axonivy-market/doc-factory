package ch.ivyteam.ivy.addons.filemanager.ulcextensionhandler;

import ch.ivyteam.ivy.addons.filemanager.DialogParam;
import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.database.filelink.FileLink;
import ch.ivyteam.ivy.addons.filemanager.util.MethodArgumentsChecker;
import ch.ivyteam.ivy.environment.Ivy;

public final class DocumentMoveChecker {
	
	private DocumentMoveChecker() {}
	
	public static DialogParam makeDialogForDocumentOverwrite(DocumentOnServer documentToOverwrite) {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheArgumentIsNull("The document To Overwrite cannot be null.", documentToOverwrite);
		DialogParam dialogParam = new DialogParam();
		dialogParam.setWindowTitle(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/windowTitles/overwriteFiles"));
		String documentName;
		if(documentToOverwrite instanceof FileLink) {
			documentName = ( (FileLink) documentToOverwrite).getFileLinkName();
		} else {
			documentName = documentToOverwrite.getFilename();
		}
		dialogParam.setMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/question/overwriteExistingFileByMove")
				.replaceFirst("\\[NAME\\]", documentName + "<br>"));
		if(!dialogParam.getMessage().startsWith("<html>")){
			dialogParam.setMessage("<html>"+dialogParam.getMessage());
		}
		dialogParam.setDefaultButton(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/overwrite"));
		return dialogParam;
	}
}
