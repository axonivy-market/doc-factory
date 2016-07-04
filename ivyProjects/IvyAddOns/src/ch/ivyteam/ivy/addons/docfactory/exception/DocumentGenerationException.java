package ch.ivyteam.ivy.addons.docfactory.exception;

import ch.ivyteam.ivy.addons.docfactory.FileOperationMessage;

public class DocumentGenerationException extends RuntimeException {

	private static final long serialVersionUID = 1109647012615300909L;
	
	private FileOperationMessage fileOperationMessage;
	
	public DocumentGenerationException(FileOperationMessage fileOperationMessage) {
		this.fileOperationMessage = fileOperationMessage;
	}
	
	@Override
	public String getMessage() {
		if(this.fileOperationMessage == null) {
			return "";
		}
		return this.fileOperationMessage.getMessage();
	}
	
	public FileOperationMessage getFileOperationMessage() {
		return this.fileOperationMessage;
	}

}
