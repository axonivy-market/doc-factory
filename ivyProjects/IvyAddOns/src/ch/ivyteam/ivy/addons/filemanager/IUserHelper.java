/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager;

import java.util.concurrent.Callable;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.security.IUser;

/**
 * @author ec
 *
 */
public class IUserHelper {

	/**
	 * Tells if a give user denotes by its Ivy user name has an active session (is logged).
	 * @param userName: the username
	 * @return true if the user is currently logged, else false.
	 * @throws Throwable 
	 */
	public static boolean isUserlogged(String userName) throws Throwable
	{
		if(userName==null || userName.trim().length()==0)
		{
			return false;
		}

		final String usern = userName.trim();
		boolean ret =  Ivy.session().getSecurityContext().executeAsSystemUser(new Callable<Boolean>(){
			public Boolean call() throws Exception 
			{
				IUser user = Ivy.session().getWorkflowContext().getSecurityContext().findUser(usern);
				if(user==null)
				{
					return false;
				}
				java.util.List<ISession> sessions = Ivy.session().getWorkflowContext().getSecurityContext().getSessions();
				boolean found = false;
				for(ISession session:sessions){
					if (session.getSessionUser().equals(user))
					{
						found=true;
						break;
					}
				}
				return found;
			}
		});

		return ret;
	}

}
