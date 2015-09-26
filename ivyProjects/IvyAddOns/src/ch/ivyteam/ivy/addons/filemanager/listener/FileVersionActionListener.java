package ch.ivyteam.ivy.addons.filemanager.listener;

public interface FileVersionActionListener {

	void fileVersionCreated(FileVersionActionEvent eventObject);
	
	void fileVersionRolledback(FileVersionActionEvent eventObject);
	
	void fileVersiondeleted(FileVersionActionEvent eventObject);
}
