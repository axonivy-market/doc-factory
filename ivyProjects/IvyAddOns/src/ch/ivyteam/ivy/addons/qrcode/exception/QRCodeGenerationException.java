package ch.ivyteam.ivy.addons.qrcode.exception;

public class QRCodeGenerationException extends RuntimeException {

	private static final long serialVersionUID = -4259385194881782205L;
	
	private String message;
	
	public QRCodeGenerationException(String message, Exception exception) {
		this.message = message;
		if(exception != null) {
			initCause(exception);
		}
	}

	@Override
	public String getMessage() {
		return message;
	}
	
	

}
