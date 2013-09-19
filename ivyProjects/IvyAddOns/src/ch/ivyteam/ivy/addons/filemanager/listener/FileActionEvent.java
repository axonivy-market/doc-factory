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

}
