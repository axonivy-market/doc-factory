/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.listener;

import ch.ivyteam.ivy.addons.filemanager.FileTag;

/**
 * @author ec
 *
 */
public class FileTagActionEvent extends java.util.EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1543419252974667651L;

	private FileTag ft;
	
	public FileTagActionEvent(FileTag ft) {
		super(ft);
		this.ft =ft;
		
	}

	public FileTag getFileTag() {
		return this.ft;
	}
}
