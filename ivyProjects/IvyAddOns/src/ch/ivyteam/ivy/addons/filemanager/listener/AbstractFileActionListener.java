/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.listener;

/**
 * 
 * This class is an abstract helper class that implements the FileActionListener, FileTypeActionListener interfaces.<br>
 * It allows declaring only the needed FileActionListener or FileTypeActionListener methods in your filemanager listeners.<br>
 * The file management configuration controller object has a list of AbstractFileActionListener objects to be able to fire the needed events.
 * @author ec
 */
public abstract class AbstractFileActionListener implements FileActionListener, FileTypeActionListener, FileTagActionListener{

	/* FROM FileActionListener */
	@Override
	public void fileCreated(FileActionEvent e) {
	}

	@Override
	public void fileChanged(FileActionEvent e) {
	}

	@Override
	public void fileDeleted(FileActionEvent e) {
	}

	@Override
	public void fileVersionRollbacked(FileActionEvent e) {
		
	}

	/* FROM FileTypeActionListener */
	@Override
	public void fileTypeCreated(FileTypeActionEvent eventObject) {
		
	}

	@Override
	public void fileTypeDeleted(FileTypeActionEvent eventObject) {
		
	}

	@Override
	public void fileTypeChanged(FileTypeActionEvent eventObject) {
		
	}

	/* FROM FileTagActionListener */
	@Override
	public void fileTagCreated(FileTagActionEvent eventObject) {
		
	}

	@Override
	public void fileTagDeleted(FileTagActionEvent eventObject) {
		
	}

	@Override
	public void fileTagChanged(FileTagActionEvent eventObject) {
		
	}
	
	
	

}
