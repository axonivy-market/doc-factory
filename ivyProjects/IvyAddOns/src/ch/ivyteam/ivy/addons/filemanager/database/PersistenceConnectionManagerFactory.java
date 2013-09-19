/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database;


import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.filetag.FileTagSQLPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.filetype.FileTypeSQLPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileTagPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IItemTranslationPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IDocumentOnServerPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileTypePersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileTypeTranslationPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFolderOnServerPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.ILanguagePersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.TranslatedFileManagerItemsEnum;

/**
 * This class declares static methods allowing to get IItemPersistence instances for the different kind of persistent filemanager objects.
 * @author ec
 *
 */
public class PersistenceConnectionManagerFactory {
	
	private PersistenceConnectionManagerFactory() { }
	
	/**
	 * return an IPersistenceConnectionManager that can communicate with the underlying persistence resource (SQL Ivy external Database...).
	 * For the moment only an {@link IvyExternalDatabaseConnectionManager} to communicate with an Ivy external database can be returned.
	 * @param configuration BasicConfigurationController object
	 * @return IPersistenceConnectionManager<?> For the moment only an IvyExternalDatabaseConnectionManager can be returned. If the BasicConfigurationController 
	 * configuration object is set to use the IvySystemDB (isUseIvySystemDB() returns true), this method returns null.
	 */
	public static IPersistenceConnectionManager<?> getPersistenceConnectionManagerInstance(BasicConfigurationController configuration) {
		if(configuration.isUseIvySystemDB()) {
			return null;
		}
		return new IvyExternalDatabaseConnectionManager(configuration.getIvyDBConnectionName());
	}
	
	/**
	 * Returns an {@link IDocumentOnServerPersistence} instance.<br>
	 * For the moment only a {@link DocumentOnServerSQLPersistence} can be returned.
	 * @param configuration a {@link BasicConfigurationController} object containing all the necessary information 
	 * @return
	 */
	public static IDocumentOnServerPersistence getIDocumentOnServerPersistenceInstance(BasicConfigurationController configuration) {
		return new DocumentOnServerSQLPersistence(configuration);
	}
	
	/**
	 * Returns an {@link IFolderOnServerPersistence} instance.<br>
	 * For the moment only a {@link FolderOnServerSQLPersistence} can be returned.
	 * @param configuration a {@link BasicConfigurationController} object containing all the necessary information 
	 * @return
	 */
	public static IFolderOnServerPersistence getIFolderOnServerPersistenceInstance(BasicConfigurationController configuration) {
		return new FolderOnServerSQLPersistence(configuration);
	}
	
	/**
	 * Returns the LanguagePersistenceHandler. This class is responsible for CRUD operation on supported languages in the filemanager for translating
	 * directories and other items.
	 * @param config a {@link BasicConfigurationController} object containing all the necessary information 
	 * @return
	 * @throws Exception
	 */
	public static ILanguagePersistence getILanguagePersistenceInstance(BasicConfigurationController config) throws Exception {
		return new LanguageSQLPersistence(config);
	}
	
	/**
	 * Returns an {@link IFileTypePersistence} object responsible for managing the persistence of the file types
	 * @param config a {@link BasicConfigurationController} object containing all the necessary information 
	 * @return an {@link IFileTypePersistence} object responsible for managing the persistence of the file types
	 * @throws Exception
	 */
	public static IFileTypePersistence getIFileTypePersistenceInstance(BasicConfigurationController config) throws Exception {
		return new FileTypeSQLPersistence(config);
	}
	
	/**
	 * Returns an {@link IFileTypeTranslationPersistence} object responsible for managing the persistence of the file types translation
	 * @param config a {@link BasicConfigurationController} object containing all the necessary information 
	 * @param TranslatedFileManagerItemsEnum Enum to be able to choose the right Item as declared see {@link TranslatedFileManagerItemsEnum}
	 * @return an {@link IFileTypeTranslationPersistence} object responsible for managing the persistence of the file types translation
	 * @throws Exception
	 */
	public static IItemTranslationPersistence getIItemTranslationPersistenceInstance(BasicConfigurationController config, TranslatedFileManagerItemsEnum itemType) throws Exception {
		return new ItemTranslationSQLPersistence(config, itemType);
	}
	
	public static IFileTagPersistence getIFileTagPersistenceInstance(BasicConfigurationController config){
		return new FileTagSQLPersistence(config);
	}

}
