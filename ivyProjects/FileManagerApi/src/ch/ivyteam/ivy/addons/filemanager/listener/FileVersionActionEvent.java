package ch.ivyteam.ivy.addons.filemanager.listener;

import ch.ivyteam.ivy.addons.filemanager.FileVersion;

public class FileVersionActionEvent extends java.util.EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2949691990787137208L;
	
	private FileVersion fileVersion;

	public FileVersionActionEvent(FileVersion fileVersion) {
		super(fileVersion);
		this.fileVersion = fileVersion;
	}

	public FileVersion getFileVersion() {
		return fileVersion;
	}

	public void setFileVersion(FileVersion fileVersion) {
		this.fileVersion = fileVersion;
	}
	
}
