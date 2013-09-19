/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database;


import java.sql.Connection;

import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;


/**
 * This interface represents a particular IPersistencySystemConnectionManager for getting java.sql.Connection object<br>
 * and closing the resources needed for getting it.
 * @author ec
 *
 */
public interface IDatabaseConnectionManager extends
		IPersistenceConnectionManager<Connection> {
	
	/**
	 * An IDatabaseConnectionManager is a particular IPersistenceSystemConnectionRetriever that gives a java.sql.Connection object back.
	 */
	@Override
	public Connection getConnection() throws Exception;
	
}
