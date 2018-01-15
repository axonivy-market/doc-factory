package ch.ivyteam.ivy.addons.filemanager.database.security;


/**
 * This Class represents the response computed by a SecurityHandler object in its hasRight method.
 * Please refer to the following method documentation:
 * {@link SecurityHandler#hasRight(Iterator<SecurityHandler> securityHandlerIterator, SecurityRightsEnum rightType,
			FolderOnServer folderOnServer, IUser user, List<String> roles)}
 * @author ec
 *
 */
public class SecurityResponse {
	
	private boolean allow = false;
	private String message;
	
	/**
	 * returns the message of the response.
	 * It should be i18nzed since it may be displayed to the user.
	 * @return the message as String
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the message to be displayed
	 * @param message the message as String
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * returns true if the security response is positive else false.<br>
	 * Example: if a user has the rights to open a given file, this flag should be set to true.
	 * @return true if the security response is positive else false.
	 */
	public boolean isAllow() {
		return allow;
	}
	
	/**
	 * Sets the security response.<br>
	 * Example: if a user has the rights to open a given file, this flag should be set to true.
	 * @param allow the boolean reflecting whether the security allows an action (true) or not (false).
	 */
	public void setAllow(boolean allow) {
		this.allow = allow;
	}
	
	

}
