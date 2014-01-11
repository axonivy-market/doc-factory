/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.listener;

/**
 * @author ec
 *
 */
public interface FileTagActionListener {

	public void fileTagCreated(FileTagActionEvent eventObject);
	
	public void fileTagDeleted(FileTagActionEvent eventObject);
	
	public void fileTagChanged(FileTagActionEvent eventObject);
	
}
