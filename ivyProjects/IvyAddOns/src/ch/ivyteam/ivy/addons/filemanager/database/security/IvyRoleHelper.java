/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.security;

import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Tree;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.IUser;

/**
 * @author ec
 *
 */
public class IvyRoleHelper {

	
	/**
	 * Build an Ivy Role ch.ivyteam.ivy.scripting.objects.Tree from the Everybody Role.<br>
	 * Usefull to display the roles in an RDTree
	 * @return Ivy Role ch.ivyteam.ivy.scripting.objects.Tree
	 * @throws Exception
	 */
	public static Tree buildIvyRolesTree() throws Exception{
		Tree iroleTree = new Tree();
		IRole r = Ivy.session().getWorkflowContext().getSecurityContext().findRole("Everybody");

		iroleTree.setValue(r);
		iroleTree.setInfo(r.getName());
		fillIvyRolesTree(iroleTree, r);
		return iroleTree;
	}

	/**
	 * Recursive private method to build the roles Tree
	 * @param _rTree
	 * @param _r
	 * @throws Exception
	 */
	private static void fillIvyRolesTree(Tree _rTree, IRole _r) throws Exception{
		List<IRole> roles = _r.getChildRoles();

		if(roles.isEmpty())
		{
			return;
		}

		for(IRole r: roles)
		{
			_rTree.createChild(r, r.getName());
			fillIvyRolesTree(_rTree.getLastChild(),r);
		}
	}
	
	/**
	 * search if the given Ivy user has at least one of the roles listed in the given List<String> roles names.<br>
	 * @param roles List<String> roles names.
	 * @param ivyUserName the Ivy user name. If it is null or empty, this method takes the actual session user name.
	 * @return true if the user has one the roles, else false.
	 */
	public static boolean isOneUserRoleInList(List<String> roles, String ivyUserName) {
		if(roles ==null || roles.isEmpty()) {
			return false;
		}
		List <IRole> userRoles = new ArrayList<IRole>();
		try {
			if(ivyUserName== null || ivyUserName.trim().length()==0) {
				ivyUserName = Ivy.session().getSessionUserName();
			}
			userRoles.addAll(Ivy.wf().getSecurityContext().findUser(ivyUserName).getAllRoles());
		}catch(Throwable t) {
			//If a problem occurs with the username (no existing IvyUser, null etc...) the returned fos will have false in all the fields.
		}
		boolean found = false;
		for(IRole r: userRoles) {
			if(roles.contains(r.getName())) {
				found = true;
				break;
			}
		}
		return found;
	}
	
	/**
	 * returns the roles names owned by the given user.
	 * @param user the IUser. If null, returns null.
	 * @return
	 */
	public static java.util.List<String> getUserRolesAsListStrings(IUser user) {
		if(user==null) {
			return null;
		}
		java.util.List<String> roles = new ArrayList<String>();
		java.util.List<IRole> userRoles = user.getAllRoles();
		for(IRole r: userRoles) {
			roles.add(r.getName());
		}
		return roles;
	}
}
