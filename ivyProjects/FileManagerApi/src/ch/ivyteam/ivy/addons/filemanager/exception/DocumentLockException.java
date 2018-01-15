package ch.ivyteam.ivy.addons.filemanager.exception;

/**
 * Exception that may be thrown if an operation is not permitted because a file is locked.
 * @author ec
 *
 */
public class DocumentLockException extends Exception {

	public DocumentLockException(String message) {
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3052323905508719434L;

}
