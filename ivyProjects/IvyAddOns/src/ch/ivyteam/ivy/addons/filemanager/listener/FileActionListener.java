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
	 * @param e a FileActionEvent object that holds the file (java.io.File) that was created. The file path is the path of the file in the database.
	 */
	public void fileCreated(FileActionEvent e);

	/**
	 * This method should be called whenever the file manager event source changes a File (after an upload or after a server side creation).
	 * @param e a FileActionEvent object that holds the file (java.io.File) that was changed. The file path is the path of the file in the database.
	 */
	public void fileChanged(FileActionEvent e);

	/**
	 * This method should be called whenever the file manager event source deletes a File.
	 * @param e a FileActionEvent object that holds the file (java.io.File) that was deleted. The file path is the path of the file in the database.
	 */
	public void fileDeleted(FileActionEvent e);

}
