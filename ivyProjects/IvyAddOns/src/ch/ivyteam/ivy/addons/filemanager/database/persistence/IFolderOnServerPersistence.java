package ch.ivyteam.ivy.addons.filemanager.database.persistence;

import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;

/**
 * This interface is sub-interface of IItemPersistence for the particular FolderOnServer data type.<br>
 * This Interface have to be implemented by classes responsible for the FolderOnServer persistence.<br>
 * This class extends the {@link IItemPersistence} class.<br>
 * To get an instance of this interface please use the appropriate static method of the {@link PersistenceConnectionManagerFactory}. 
 * @author ec (ecomba@soreco.ch)
 * @since 01/07/2013
 * @author ec
 */
public interface IFolderOnServerPersistence extends IItemPersistence<FolderOnServer> {
	
	/**
	 * Gets a List of FolderOnServer objects retrieved from the persistence system with the given path
	 * @param searchpath the search path
	 * @param recursive boolean flag. If true the search will be recursive and will look from the given path and in all the children paths. 
	 * Else the search is strictly limited to the given search path.
	 * @return a java.util.List<FolderOnServer> objects or an empty List if no item was found.
	 * @throws Exception in case of error
	 */
	public List<FolderOnServer> getList(String searchpath, boolean recursive) throws Exception;

}
