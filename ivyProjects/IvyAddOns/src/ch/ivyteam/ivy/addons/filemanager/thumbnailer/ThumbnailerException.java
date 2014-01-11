package ch.ivyteam.ivy.addons.filemanager.thumbnailer;

/**
 * Thrown if Thumbnailing process failed.
 *
 */
class ThumbnailerException extends Exception {

	private static final long serialVersionUID = -7988812285439060247L;

	public ThumbnailerException() {
		super();
	}

	public ThumbnailerException(String message) {
		super(message);
	}

	public ThumbnailerException(Throwable cause) {
		super(cause);
	}

	public ThumbnailerException(String message, Throwable cause) {
		super(message, cause);
	}

}

