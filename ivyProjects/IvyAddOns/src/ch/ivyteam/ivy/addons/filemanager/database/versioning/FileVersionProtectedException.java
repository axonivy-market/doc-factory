package ch.ivyteam.ivy.addons.filemanager.database.versioning;

public class FileVersionProtectedException extends Exception {

	private static final long serialVersionUID = 4227342339571755329L;
	
	String message;
	
	public FileVersionProtectedException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}

}
