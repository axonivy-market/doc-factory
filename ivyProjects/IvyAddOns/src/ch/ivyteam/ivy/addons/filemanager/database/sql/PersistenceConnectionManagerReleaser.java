package ch.ivyteam.ivy.addons.filemanager.database.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.environment.Ivy;

public final class PersistenceConnectionManagerReleaser {

	public static final String RESULTSET_CLOSE_EXCEPTION_MESSAGE = "The ResultSet cannot be closed in %s method, %s class.";
	public static final String PREPAREDSTATEMENT_CLOSE_EXCEPTION_MESSAGE = "The PreparedStatement cannot be closed in %s method, %s class.";
	
	private PersistenceConnectionManagerReleaser() {}
	
	/**
	 * Used in several SQL Persistence classes for releasing the SQL DB Connection resources after some DB actions.
	 * @param connectionManager the connection manager that should be released. Cannot be null (NPE if null).
	 * @param stmt a preparedStatement that has to be closed. Can be null.
	 * @param rs a ResultSet that has to be closed. Can be null.
	 * @param args Objects used to fill the possible exception messages. Typically this should be in that order: the name of the method calling this releaser and its class.
	 * @throws Exception
	 */
	public static void release(IPersistenceConnectionManager<?> connectionManager, PreparedStatement stmt, ResultSet rs, Object ... args) throws Exception {
		if(rs != null) {
			try {
				rs.close();
			} catch( SQLException ex) {
				Ivy.log().error(String.format(RESULTSET_CLOSE_EXCEPTION_MESSAGE, args), ex);
			}
		}
		if(stmt!=null) {
			try {
				stmt.close();
			} catch( SQLException ex) {
				Ivy.log().error(String.format(PREPAREDSTATEMENT_CLOSE_EXCEPTION_MESSAGE, args), ex);
			}
		}
		connectionManager.closeConnection();
	}
}
