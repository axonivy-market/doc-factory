/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.exception;

/**
 * This Exception represents an exception that is thrown because of a FileManagement Problem
 * @author ec
 *
 */
public class FileManagementException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5697561864970453840L;
	
	public FileManagementException() {
		super(); 
	}
	
	public FileManagementException(String message) {
		super(message); 
	}
	
	public FileManagementException(String message, Throwable cause) {
		super(message, cause); 
	}
	
	public FileManagementException(Throwable cause) {
		super(cause); 
	}

}
