/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.security;

import java.util.Iterator;
import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.security.IUser;

/**
 * This interface must be implemented by securityHandlers hold in a SecurityHandlerChain.
 * @author ec
 * 
 */
public interface SecurityHandler {

	

	/**
	 * Checks if a user has the right to perform an action on a
	 * FolderOnServer. The SecurityHandler object should be a member of a
	 * SecurityHandlerChain object. If The SecurityHandler cannot compute the asked right, it
	 * just has to call the next SecurityHandler through the passed Iterator<SecurityHandler>. <br>
	 * Example:<br>
	 * The SecurityHandlerChain contains these SecurityHandler in that order :<br>
	 * [DirectoryFilterSecurityHandler, MyAppSecurityHandler, DefaultSecurityHandler]<br>
	 * <br>
	 * 
	 * When the user selects a folder, the FileManager calls
	 * <code><pre>
	 * theSecurityHandlerChain.hasRight(null, righttype, selectedFolder, user, roles)
	 * </pre></code>
	 * The SecurityHandlerChain itself implements this interface, so you can call its hasRight(...) implemented method.<br>
	 * The iterator can be null here because the securityHandlerChain retrieves its Iterator in its hasRight(...) method if it is null.<br>
	 * 
	 * Here the first Handler is the DirectoryFilterSecurityHandler and computes something like
	 * this in this hasRight method :
	 * <code><pre>
	 * if(righttype==openDirectory &&  folder.getName().contains("confidential") {
	 *  return SecurityResponse with flag to false and a message
	 * } else {
	 *   // Here the MyAppSecurityHandler object will be the next called SecurityHandler in the list.
	 *   return theChain.getNext(this).hasRight(securityHandlerIterator, righttype, folder, user, roles) 
	 * }</pre></code>
	 * If the folder is not black listed, then the next SecurityHandler will be responsible to compute the right.<br>
	 * And so on until the last default securityHandler is reached.
	 * 
	 * @param securityHandlerIterator the SecurityHandler Iterator object used to walked through the SecurityHandler List.
	 * @param rightType int the right type that has to be checked. @see SecurityRightsEnum.
	 * @param folderOnServer the FolderOnServer Object representing the directory on or in which the action has to be performed
	 * @param user the user (IUser) that is performing the action
	 * @param roles the roles names owned by the user.
	 * @return a SecurityResponse containing a flag reflecting the computed right (false or true)
	 */
	public SecurityResponse hasRight(Iterator<SecurityHandler> securityHandlerIterator, SecurityRightsEnum rightType,
			FolderOnServer folderOnServer, IUser user, List<String> roles);


}
