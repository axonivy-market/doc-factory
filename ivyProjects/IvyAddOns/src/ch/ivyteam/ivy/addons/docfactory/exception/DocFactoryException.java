package ch.ivyteam.ivy.addons.docfactory.exception;

import ch.ivyteam.api.API;


public class DocFactoryException extends Exception {

	private static final long serialVersionUID = 1109647012615300909L;
	
	private String message;
	
	public DocFactoryException(Exception ex) {
		super(ex);
		API.checkNotNull(ex, "Exception");
		this.message = ex.getMessage();
	}
	
	public DocFactoryException(String message, Exception ex) {
		super(ex);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
	
}
