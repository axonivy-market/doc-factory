/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.listener;

import ch.ivyteam.ivy.addons.filemanager.FileType;

/**
 * @author ec
 *
 */
public class FileTypeActionEvent extends java.util.EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7926838690999032703L;
	private FileType ft;
	
	public FileTypeActionEvent(FileType ft) {
		super(ft);
		this.ft =ft;
		
	}

	public FileType getFileType() {
		return this.ft;
	}
}
