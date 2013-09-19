/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.listener;

/**
 * 
 * This class is an helper class allowing to declare only the wanted FileActionListener method.<br>
 * You can extends this class and overrides only the used FileActionEvent method.
 * @author ec
 */
public abstract class AbstractFileActionListener implements FileActionListener{

	@Override
	public void fileCreated(FileActionEvent e) {
	}

	@Override
	public void fileChanged(FileActionEvent e) {
	}

	@Override
	public void fileDeleted(FileActionEvent e) {
	}

}
