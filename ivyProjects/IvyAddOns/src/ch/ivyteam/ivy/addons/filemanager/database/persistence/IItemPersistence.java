/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.persistence;

/**
 * This class is the super interface of all the file management persistence managers
 * @author ec
 *
 */
public interface IItemPersistence<T> {
	
	/**
	 * creates the item of generic type T
	 * @param itemToCreate the item of generic type T to create
	 * @return the created item of generic type T
	 * @throws Exception in case of error
	 */
	public T create(T itemToCreate) throws Exception;
	
	/**
	 * Updates the item of generic type T
	 * @param itemToSave the item of generic type T to be updated
	 * @return the updated item of generic type T
	 * @throws Exception in case of error
	 */
	public T update(T itemToSave) throws Exception;
	
	/**
	 * Gets the item of generic type T object defined by the given unique descriptor.<br>
	 * In the file management this descriptor in in general a path (file or directory)
	 * @param uniqueDescriptor a String representing a unique property of the item of generic type T to get.
	 * @return the found item of generic type T or null
	 * @throws Exception in case of error
	 */
	public T get(String uniqueDescriptor) throws Exception;
	
	/**
	 * Gets the item of generic type T object defined by the given unique id.<br>
	 * @param id the id of the object in the persistence system as long.
	 * @return the found item of generic type T or null
	 * @throws Exception in case of error
	 */
	public T get(long id) throws Exception;
	
	/**
	 * Deletes the given item of generic type T
	 * @param itemToDelete the item of generic type T to delete
	 * @return true if the deletion was successful, else false.
	 * @throws Exception in case of error
	 */
	public boolean delete(T itemToDelete) throws Exception;

}
