/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.security;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;

/**
 * @author ec
 *
 */
public interface DocumentFilter {
	
	/**
	 * This method can be used to filter the action the filemanager can perform on a given DocumentOnServer
	 * @param doc
	 * @param action
	 * @return
	 */
	public DocumentFilterAnswer allowAction(DocumentOnServer doc, DocumentFilterActionEnum action);

}
