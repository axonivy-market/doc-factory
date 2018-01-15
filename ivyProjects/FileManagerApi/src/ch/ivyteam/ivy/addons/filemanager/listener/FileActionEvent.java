/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.listener;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;

/**
 * @author ec
 * This class represents a file action event. The event will be fired with a java.io.File as event source
 */
public class FileActionEvent extends java.util.EventObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5032382191917289174L;
	
	private DocumentOnServer doc;
	
	private boolean isContentChanged;
	
	private boolean isFileACopy;
	
	private DocumentOnServer copiedDocumentOnServer;
	
	/**
	 * FileActionEvent constructor
	 * @param file
	 */
	public FileActionEvent(DocumentOnServer document) {
		super(document);	
		this.doc=document;
	}
	
	/**
	 * gets the File that is touched by the action event.
	 * <b>Important :</b> If the files are stored in the db as BLOB, the file may not exist on the server fileset.<br>
	 * In such a case you have to use the FileMangementHandler Object to get a "real" file with the method getDocumentOnServerWithJavaFile(String path),<br>
	 * where the path is the file path in the database. The returned DocumentOnServer object contains a real File that you can get with its getJavaFile() method.
	 * @return the File that is touched by the action event.
	 */
	public DocumentOnServer getDocument() {
		return this.doc;
	}

	/**
	 * returns true if the file content as been changed in a fileChanged event. <br>
	 * If false it means that only the meta data of the files has been changed.
	 * @return the isContentChanged
	 */
	public boolean isContentChanged() {
		return isContentChanged;
	}

	/**
	 * Sets if the file content as been changed in a fileChanged event.<br>
	 * If false it means that only the meta data of the files has been changed.
	 * @param isContentChanged the isContentChanged to set
	 */
	public void setContentChanged(boolean isContentChanged) {
		this.isContentChanged = isContentChanged;
	}

	/**
	 * This flag is useful in create events to know if the created file is a copy from an existing file, or if it is a brand new file.<br>
	 * If this flag is true, then the getCopiedDocumentOnServer() method should returns the copied Document. If not this object is null.
	 * @return the isFileACopy
	 */
	public boolean isFileACopy() {
		return isFileACopy;
	}

	/**
	 * Sets if the created file is a copy from an existing file, or if it is a brand new file.
	 * @param isFileACopy the isFileACopy to set
	 */
	public void setFileACopy(boolean isFileACopy) {
		this.isFileACopy = isFileACopy;
	}

	/**
	 * In the case of a create event if the created document is a copy from another document, then this method returns the copied Document.<br>
	 * Else this method returns null.
	 * @return the copiedDocumentOnServer
	 */
	public DocumentOnServer getCopiedDocumentOnServer() {
		return copiedDocumentOnServer;
	}

	/**
	 * In the case of a create event if the created document is a copy from another document, then this method is used to set the copied Document.<br>
	 * @param copiedDocumentOnServer the copiedDocumentOnServer to set
	 */
	public void setCopiedDocumentOnServer(DocumentOnServer copiedDocumentOnServer) {
		this.copiedDocumentOnServer = copiedDocumentOnServer;
	}

}
