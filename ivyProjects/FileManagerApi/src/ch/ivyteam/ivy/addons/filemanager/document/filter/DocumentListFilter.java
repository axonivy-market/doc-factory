package ch.ivyteam.ivy.addons.filemanager.document.filter;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;

/**
 * A DocumentListFilter is a filter that works independently from the security.<br>
 * You can use such a Filter to filter the documents that are going to be returned by the file manager API in methods that return
 * List of DocumentOnServer objects.<br`>
 * If a document is not accepted, it won't be displayed to the user in the Filemanager.<br><br>
 * Important consequence: as the user does not see some files that really exist, 
 * he cannot anticipate some potential collisions by file uploading. 
 * So the files that won't be accepted by the DocumentListFilter won't be uploaded.
 * @author ec
 *
 */
public interface DocumentListFilter {
	
	/**
	 * If this method returns true then the given DocumentOnServer will be integrated in the DocumentOnServers 
	 * List returned by the FileManager API. Else it won't be included.
	 * @param doc
	 * @return
	 */
	boolean accept(DocumentOnServer doc);
	

}
