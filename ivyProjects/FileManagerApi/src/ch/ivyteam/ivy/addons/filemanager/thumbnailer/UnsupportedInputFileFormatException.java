package ch.ivyteam.ivy.addons.filemanager.thumbnailer;

import java.io.IOException;

class UnsupportedInputFileFormatException extends IOException {

	private static final long serialVersionUID = -8728813367662852880L;

	public UnsupportedInputFileFormatException() {
	}

	public UnsupportedInputFileFormatException(String arg0) {
		super(arg0);
	}

	public UnsupportedInputFileFormatException(Throwable arg0) {
		super(arg0);
	}

	public UnsupportedInputFileFormatException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
