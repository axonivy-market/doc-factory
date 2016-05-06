/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.security;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;

/**
 * @author ec
 * <b>NOT PUBLIC API FOR THE MOMENT</b>
 */
public interface DocumentFilter {
	
	/**
	 * <b>NOT PUBLIC API FOR THE MOMENT</b><br>
	 * This method can be used to filter the action the filemanager can perform on a given DocumentOnServer
	 * @param doc
	 * @param action
	 * @return
	 */
	public DocumentFilterAnswer allowAction(DocumentOnServer doc, DocumentFilterActionEnum action);

}
