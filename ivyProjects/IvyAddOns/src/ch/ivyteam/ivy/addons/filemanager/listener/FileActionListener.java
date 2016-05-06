/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.listener;

/**
 * @author ec
 * This Interface represents ActionListeners that listen to File actions. 
 */
public interface FileActionListener {

	/**
	 * This method should be called whenever the file manager event source creates a new File (after an upload or after a server side creation).
	 * @param e a FileActionEvent object that holds the DocumentOnServer that was created. 
	 */
	public void fileCreated(FileActionEvent e);

	/**
	 * This method should be called whenever the file manager event source changes a File (after an upload or after a server side creation).
	 * @param e a FileActionEvent object that holds the DocumentOnServer that was changed. 
	 */
	public void fileChanged(FileActionEvent e);

	/**
	 * This method should be called whenever the file manager event source deletes a File.
	 * @param e a FileActionEvent object that holds the DocumentOnServer that was deleted. 
	 */
	public void fileDeleted(FileActionEvent e);
	
	/**
	 * This method should be called whenever the file manager event source rolls a file version back.
	 * @param e a FileActionEvent object that holds the DocumentOnServer that is now the current active version. 
	 */
	public void fileVersionRollbacked(FileActionEvent e);

}
