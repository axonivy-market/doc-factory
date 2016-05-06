package ch.ivyteam.ivy.addons.filemanager.document;

import java.util.Collection;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler;
import ch.ivyteam.ivy.addons.filemanager.util.MethodArgumentsChecker;
import ch.ivyteam.ivy.addons.restricted.util.IvySessionsRetriever;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.ISession;
import ch.ivyteam.ivy.security.IUser;

public final class DocumentLockManager {

	private DocumentLockManager() {
	}

	public static void unlockDocumentsLockedByInactiveUsers(
			Collection<DocumentOnServer> documents,
			AbstractFileManagementHandler fileManagementHandler)
			throws Exception {
		MethodArgumentsChecker
				.throwIllegalArgumentExceptionIfOneOfTheArgumentIsNull(
						"The documents collection to check and the FileManagementHandler cannot be null",
						documents, fileManagementHandler);
		Ivy.log().debug("Getting {0} documents to check", documents.size());
		Collection<ISession> sessions = null;
		for (DocumentOnServer doc : documents) {
			if (doc.getLocked().equals("1")) {
				Ivy.log().debug("Found {0} locked", doc.getFilename());
				if (sessions == null) {
					sessions = IvySessionsRetriever.getActiveSessions();
				}
				String username = doc.getLockingUserID();
				IUser user = Ivy.wf().getSecurityContext().findUser(username);
				boolean found = false;
				for (ISession session : sessions) {
					if(session.getSessionUser() != null && session.getSessionUser().equals(user)) {
						Ivy.log().debug("Found active user {0}", user.getFullName());
						found = true;
						break;
					}
				}
				if(!found) {
					if(fileManagementHandler.unlockDocument(doc)) {
						Ivy.log().debug("Document {0} unlocked because User {1} inactive.", doc.getFilename(), username);
					}
				}
			}
		}
	}
	
	public static void unlockDocumentsLockedUnderPathByInactiveUsers(
			String path,
			AbstractFileManagementHandler fileManagementHandler)
			throws Exception {
		MethodArgumentsChecker
				.throwIllegalArgumentExceptionIfOneOfTheArgumentIsNull(
						"The path and the FileManagementHandler cannot be null",
						path, fileManagementHandler);
		unlockDocumentsLockedByInactiveUsers(fileManagementHandler.getDocumentOnServersInDirectory(path, true), fileManagementHandler);
	}

}
