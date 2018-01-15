/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.listener;

/**
 * @author ec
 *
 */
public interface FileTypeActionListener {

	public void fileTypeCreated(FileTypeActionEvent eventObject);
	
	public void fileTypeDeleted(FileTypeActionEvent eventObject);
	
	public void fileTypeChanged(FileTypeActionEvent eventObject);
	
}
