/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.security;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.security.IUser;



/**
 * This Class holds an Array of securityHandlers.<br>
 * A securityHandler is responsible for managing the security on file's actions.<br>
 * It must implement the SecurityHandler Interface.
 * 
 * @author ec 
 */
public class SecurityHandlerChain implements SecurityHandler {

	private List<SecurityHandler> handlers;

	/**
	 * empty constructor.
	 * The chain will contain a single DirectorySecurityController object.
	 * @see DirectorySecurityController
	 */
	public SecurityHandlerChain() {
		this(null,null,null,null);
	}

	/**
	 * Initializes the SecurityHandlerChain with the given array of securityHandlers.<br>
	 * A securityHandler is responsible for managing the security on file's actions. 
	 * It must implement the SecurityHandler Interface.<br>
	 * The chain is guaranteed to contain a default SecurityHandler (@see DirectorySecurityController) as last member.<br>
	 * This last member is built with the ivy database connection name, the directories table name and the schema name, as set in the corresponding Ivy global variables.
	 * @param securityHandlers an array of SecurityHandler objects. If null or empty the chain will only contain a DirectorySecurityController automatically created.
	 */
	public SecurityHandlerChain(SecurityHandler[] securityHandlers) {
		this(securityHandlers,null,null,null);
	}
	
	/**
	 * Initializes the SecurityHandlerChain with the given array of securityHandlers.<br>
	 * A securityHandler is responsible for managing the security on file's actions. 
	 * It must implement the SecurityHandler Interface.<br>
	 * The chain is guaranteed to contain a default SecurityHandler (@see DirectorySecurityController) as last member.<br>
	 * This last member is built with the given ivy database connection name, the directories table name and the schema name.
	 * @param securityHandlers an array of SecurityHandler objects. If null or empty the chain will only contain a DirectorySecurityController automatically created.
	 * @param ivyDBConnectionName the ivy database connection name used to connect to the database containing the directories table.<br> 
	 * This parameter is used in the default SecurityHandler initialization (DirectorySecurityController). If null or empty, the corresponding Ivy global variable will be used.
	 * @param dirTableName  the directories table name containing all the directories information. <br> 
	 * This parameter is used in the default SecurityHandler initialization (DirectorySecurityController). If null or empty, the corresponding Ivy global variable will be used.
	 * @param schemaName the database schema name containing the directories table. <br> 
	 * This parameter is used in the default SecurityHandler initialization (DirectorySecurityController). If null or empty, the corresponding Ivy global variable will be used.
	 */
	public SecurityHandlerChain(SecurityHandler[] securityHandlers,String ivyDBConnectionName,
			String dirTableName, String schemaName) {
		this.handlers = new ArrayList<SecurityHandler>();
		SecurityHandler sec  = new DirectorySecurityController(ivyDBConnectionName,dirTableName,schemaName);
		if (securityHandlers != null && securityHandlers.length > 0) {
			this.handlers.addAll(Arrays.asList(securityHandlers));
		}
		this.handlers.add(sec);
	}

	/**
	 * returns the first securityHandler in the array of the chain.<br>
	 * 
	 * @return
	 */
	public SecurityHandler getFirst() {
		return this.handlers.get(0);
	}
	
	/**
	 * Checks if the given SecurityHandler is a member of this chain.
	 * @param caller the SecurityHandler for that it should be check if it is a member of this chain.
	 * @return true if the given SecurityHandler is a member of this chain. Else false.
	 */
	public boolean contains(SecurityHandler caller) {
		return this.handlers.contains(caller);	
	}

	/**
	 * returns a new Iterator that has the capacity to walk through the SecurityHandler chain.<br>
	 * To get the first handler of the chain you just have to call the Iterator.next() method.
	 * @return the Iterator<SecurityHandler> allowing walking through the SecurityHandler objects contained in the chain.
	 */
	public Iterator<SecurityHandler> getSecurityHandlerIterator(){
		return this.handlers.iterator();
	}
	
	/**
	 * Return the array of all the security Handlers contained in the chain.
	 * @return the SecurityHandler[] contained in this chain.
	 */
	public List<SecurityHandler> getSecurityHandlers() {
		return this.handlers;
	}

	@Override
	public SecurityResponse hasRight(Iterator<SecurityHandler> securityHandlerIterator,
			SecurityRightsEnum rightType, FolderOnServer folderOnServer, IUser user,
			List<String> roles) {
		if(securityHandlerIterator==null) {
			securityHandlerIterator= this.handlers.iterator();
		}
		
		if(securityHandlerIterator.hasNext()){
			SecurityResponse resp = securityHandlerIterator.next().hasRight(securityHandlerIterator, rightType, folderOnServer, user, roles);
			return resp;
		}else{
			SecurityResponse resp = new SecurityResponse();
			resp.setAllow(false);
			resp.setMessage("");
			return resp;
		}
	}

}
