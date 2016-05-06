package ch.ivyteam.ivy.addons.restricted.util;

import java.util.Collection;
import java.util.concurrent.Callable;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.ISession;

public final class IvySessionsRetriever {
	
	private IvySessionsRetriever() {};
	
	public static Collection<ISession> getActiveSessions() throws Exception {
		return Ivy.session().getSecurityContext().executeAsSystemUser(new Callable<Collection<ISession>> () {
			
			@Override
			public Collection<ISession> call() throws Exception {
				return Ivy.session().getWorkflowContext().getSecurityContext().getSessions();
			}
			
		});
	}

}
