/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.persistence;



/**
 * This Interface declares the methods that a IPersistenceSystemConnectionManager object has to implement.<br>
 * The IPersistenceSystemConnectionManager's responsibility is to give a connection object for the underlying persistence system,
 * and to be able to release it.
 * @author ec
 *
 * @param <T> generic type corresponding to the object connection's type, like a java.sql.Connection
 */
public interface IPersistenceConnectionManager<T> {
	
	/**
	 * Method that returns a connection object for the underlying persistence system.
	 * All the necessary parameters for getting the connection Object should have been given within a constructor.
	 * @return a connection Object of generic type T
	 * @throws Exception
	 */
	public T getConnection() throws Exception;
	
	/**
	 * Method responsible for releasing the connection object retrieved by using the getConnection method
	 * @throws Exception
	 */
	public void closeConnection() throws Exception;

}
