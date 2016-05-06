/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.security;

import java.io.Serializable;

/**
 * This class represents a Document Filter Answer.
 * It contains a boolean telling if the action is allowed (ture) or not (false),<br>
 * and a message that can be used to display an information to the user.
 * @author ec
 *
 */
public class DocumentFilterAnswer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3748116088023942351L;
	
	private String message;
	private boolean allow;
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the allow
	 */
	public boolean isAllow() {
		return allow;
	}
	/**
	 * @param allow the allow to set
	 */
	public void setAllow(boolean allow) {
		this.allow = allow;
	}
	
	

}
