package ch.ivyteam.ivy.addons.filemanager.database.persistence;

import ch.ivyteam.ivy.addons.filemanager.ItemTranslation;

/**
 * This Interface have to be implemented by classes responsible for the filemanager objects (FolderOnServer, Filetypes...) translation persistence.<br>
 * This class extends the {@link IItemPersistence} class.<br>
 * It does not declare any other method than the {@link IItemPersistence} class ones, and just overwrites the <T> generic parameter of this interface with {@link ItemTranslation}.<br>
 * To get an instance of this interface please use the appropriate static method of the {@link PersistenceConnectionManagerFactory}. 
 * @author ec (ecomba@soreco.ch)
 * @since 07/08/2013
 */
public interface IItemTranslationPersistence extends IItemPersistence<ItemTranslation> {
	public final static int FOLDER_ON_SERVER = 1;
	public final static int FILETYPE = 2;
}
