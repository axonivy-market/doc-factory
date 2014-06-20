/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database;

import java.sql.Connection;
import java.util.concurrent.Callable;

import ch.ivyteam.ivy.db.IExternalDatabase;
import ch.ivyteam.ivy.db.IExternalDatabaseApplicationContext;
import ch.ivyteam.ivy.db.IExternalDatabaseRuntimeConnection;
import ch.ivyteam.ivy.environment.Ivy;

/**
 * An IvyExternalDatabaseConnectionManager is a particular IDatabaseConnectionManager that uses the underlying ivy database connection system,
 * to retrieves a java.sql.Connection object and to be able to close all the resources opened to get this Connection object.<br>
 * The only way to set the ivyDatabaseConnectionName used to get the the Connection object is to set it through the constructor.<br>
 * 
 * Whenever you get a Connection with such an object, do not forget to call its close() method 
 * as soon as you do not need the connection object anymore.
 * 
 * @author ec
 * 
 */
public class IvyExternalDatabaseConnectionManager implements
		IDatabaseConnectionManager {

	private IExternalDatabase database = null;
	private IExternalDatabaseRuntimeConnection ivyConnection =null;
	private String ivyDbConnectionName = null;
	
	/**
	 * The Constructor is the only way to set the ivyDatabaseConnectionName
	 * @param ivyDatabaseConnectionName
	 * @throws IllegalArgumentException if the ivyDatabaseConnectionName is null or an empty String
	 */
	public IvyExternalDatabaseConnectionManager(String ivyDatabaseConnectionName) {
		
		assert (ivyDatabaseConnectionName!=null && ivyDatabaseConnectionName.trim().length()>0) :
		"The database connection name is null or empty in the IvyExternalDatabaseConnectionManager constructor.";
		
		this.ivyDbConnectionName = ivyDatabaseConnectionName.trim();
	}

	@Override
	public Connection getConnection() throws Exception {
		this.ivyConnection = this.getDatabaseRuntimeConnection();
		return this.ivyConnection.getDatabaseConnection();
	}

	@Override
	public void closeConnection() throws Exception {		
		if(this.ivyConnection!=null ){
			this.database.giveBackAndUnlockConnection(this.ivyConnection);
			this.ivyConnection=null;
			Ivy.log().debug("Ivy connection closed");
		}
	}
	
	/**
	 * private method for getting the IExternalDatabaseRuntimeConnection object
	 * @return
	 * @throws Exception
	 */
	private IExternalDatabaseRuntimeConnection getDatabaseRuntimeConnection() throws Exception {
		this.database=this.getDatabase();
		if(this.ivyConnection==null) {
			this.ivyConnection = this.database.getAndLockConnection();
		}
		return this.ivyConnection;
	}

	/**
	 * private use for getting the IExternalDatabase object corresponding to the ivyDbConnectionName.
	 * @param ivyDbConnectionName
	 * @return
	 * @throws Exception
	 */
	private IExternalDatabase getDatabase()throws Exception {
		if (this.database == null) {
			this.database = Ivy.session().getSecurityContext().executeAsSystemUser(new Callable<IExternalDatabase>() {
						public IExternalDatabase call() throws Exception {
							IExternalDatabaseApplicationContext context = 
									(IExternalDatabaseApplicationContext) Ivy.wf().getApplication().
									getAdapter(IExternalDatabaseApplicationContext.class);
							return context.getExternalDatabase(ivyDbConnectionName);
						}
					});
		}
		return this.database;
	}

}
