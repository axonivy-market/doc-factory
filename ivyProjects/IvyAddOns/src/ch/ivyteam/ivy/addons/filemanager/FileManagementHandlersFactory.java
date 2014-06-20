/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager;

import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler;
import ch.ivyteam.ivy.addons.filemanager.database.AbstractFileSecurityHandler;
import ch.ivyteam.ivy.addons.filemanager.database.FileManagementDBHandlerUniversal;
import ch.ivyteam.ivy.addons.filemanager.database.FileManagementIvySystemDBHandler;
import ch.ivyteam.ivy.addons.filemanager.database.FileStoreDBHandler;
import ch.ivyteam.ivy.addons.filemanager.database.fileaction.AbstractFileActionHistoryController;
import ch.ivyteam.ivy.addons.filemanager.database.fileaction.FileActionHistoryController;
import ch.ivyteam.ivy.addons.filemanager.database.filetag.AbstractFileTagsController;
import ch.ivyteam.ivy.addons.filemanager.database.filetag.FileTagsController;
import ch.ivyteam.ivy.addons.filemanager.database.filetype.AbstractFileTypesController;
import ch.ivyteam.ivy.addons.filemanager.database.filetype.FileTypesController;
import ch.ivyteam.ivy.addons.filemanager.database.security.AbstractDirectorySecurityController;
import ch.ivyteam.ivy.addons.filemanager.database.security.DirectorySecurityController;
import ch.ivyteam.ivy.addons.filemanager.database.versioning.AbstractFileVersioningController;
import ch.ivyteam.ivy.addons.filemanager.database.versioning.FileVersioningController;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.persistence.AbstractThumbnailHandler;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.persistence.ThumbnailHandler;

/**
 * This class is an helper class that contains static methods.<br>
 * These methods help getting instances of the different Handlers that can be found in the File Management.<br>
 * These getInstance methods take all a Configuration object ({@link ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController}). <br>
 * The given BasicConfigurationController Object contains all the necessary
 * information to choose among the different possible implementations of
 * the handlers or controllers Objects.
 * If the configuration is not set for instantiating the wanted handler, the getInstance method corresponding to this handler returns null.<br>
 * 
 * @author ec
 *
 */
public class FileManagementHandlersFactory {
	
	private FileManagementHandlersFactory() {}
	
	/**
	 * Factory for getting a FileActionHistoryController corresponding to the BasicConfigurationController.<br>
	 * If the config.getFileActionHistoryConfiguration().isActivateFileActionHistory() returns false, then this method returns null
	 * @param config a BasicConfigurationController containing the needed configuration for instantiating a FileActionHistoryController.
	 * @return
	 * @throws Exception 
	 */
	public static AbstractFileActionHistoryController getFileActionHistoryControllerInstance(BasicConfigurationController config) 
			throws Exception {
		checkConfiguration(config);
		if(config.getFileActionHistoryConfiguration().isActivateFileActionHistory()) {
			return new FileActionHistoryController(config.getFileActionHistoryConfiguration());
		}else{
			return null;
		}
	}

	/**
	 * Factory for getting a FileTagsController corresponding to the BasicConfigurationController.<br>
	 * If the config.isActivateFileTags() returns false, then this method returns null.
	 * @param config a BasicConfigurationController containing the needed configuration for instantiating a FileTagsController.
	 * @return
	 * @throws Exception
	 */
	public static AbstractFileTagsController getFileTagsControllerInstance (BasicConfigurationController config) 
			throws Exception {
		checkConfiguration(config);
		if(config.isActivateFileTags()) {
			return new FileTagsController(config);
		}else{
			return null;
		}
	}
	
	/**
	 * Factory for getting a FileSecurityHandler corresponding to the BasicConfigurationController.<br>
	 * If the config.isActivateSecurity() returns false, then this method returns null.
	 * @param config a BasicConfigurationController containing the needed configuration for instantiating a FileSecurityHandler.
	 * @return
	 * @throws Exception
	 */
	public static AbstractFileSecurityHandler getFileSecurityHandlerInstance (BasicConfigurationController config) 
			throws Exception {
		checkConfiguration(config);
		if(config.isActivateSecurity()) {
			return new FileStoreDBHandler(config);
		}else {
			return null;
		}
	}
	
	/**
	 * Factory for getting a DirectorySecurityController corresponding to the BasicConfigurationController.<br>
	 * If the config.isActivateSecurity() returns false, then this method returns null.
	 * @param config a BasicConfigurationController containing the needed configuration for instantiating a DirectorySecurityController.
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static AbstractDirectorySecurityController getDirectorySecurityControllerInstance(BasicConfigurationController config) 
			throws Exception {
		checkConfiguration(config);
		if(config.isActivateSecurity()) {
			return new DirectorySecurityController(config.getIvyDBConnectionName(), config.getDirectoriesTableName(), config.getDatabaseSchemaName(), config.getAdminRole());
		}else{
			return null;
		}
	}
	
	/**
	 * Factory for getting a FileTypesController corresponding to the BasicConfigurationController.<br>
	 * If the config.isActivateFileType() returns false, then this method returns null.
	 * @param config a BasicConfigurationController containing the needed configuration for instantiating a FileTypesController.
	 * @return
	 * @throws Exception
	 */
	public static AbstractFileTypesController getFileTypesControllerInstance (BasicConfigurationController config) 
			throws Exception {
		checkConfiguration(config);
		if(config.isActivateFileType()) {
			return new FileTypesController(config);
		}else {
			return null;
		}
	}
	
	/**
	 * Factory for getting a FileVersioningController corresponding to the BasicConfigurationController.<br>
	 * If the config.isActivateFileVersioning() returns false, then this method returns null.
	 * @param config a BasicConfigurationController containing the needed configuration for instantiating a FileVersioningController.
	 * @return
	 * @throws Exception
	 */
	public static AbstractFileVersioningController getFileVersioningControllerInstance (BasicConfigurationController config) 
			throws Exception {
		checkConfiguration(config);
		if(config.isActivateFileVersioning()) {
			return new FileVersioningController(config.getIvyDBConnectionName(), config.getFilesTableName(), 
					config.getFilesContentTableName(), config.getFilesVersionTableName(), 
					config.getFilesVersionContentTableName(), config.getDatabaseSchemaName());
		} else {
			return null;
		}
	}
	
	/**
	 * Best and recommended way to get an Instance of a FileManagementHandler
	 * Object.<br>
	 * The given BasicConfigurationController Object contains all the necessary
	 * information to choose between the different possible implementations of
	 * the FileManagementHandler Objects.
	 * 
	 * @param conf BasicConfigurationController
	 * @return the right fileManagementHandler corresponding to the provided
	 *         BasicConfigurationController Object
	 * @throws Exception
	 */
	public static AbstractFileManagementHandler getFileManagementHandlerInstance (BasicConfigurationController config) 
			throws Exception {
		checkConfiguration(config);
		if (config.isUseIvySystemDB()) {
			return new FileManagementIvySystemDBHandler();
		} else if (config.isStoreFilesInDB()) {
			return new FileStoreDBHandler(config);
		} else {
			return new FileManagementDBHandlerUniversal(config);
		}
	}
	
	/**
	 * <b>This is not a Public API method</b><br>This method may change in the future, use at own risk.<br>
	 * 
	 * If the activateSecurity flag from the configuration parameter is true, this method returns an appropriate instance of an AbstractFileSecurityHandler object.
	 * The AbstractFileSecurityHandler is responsible for the filemanagement security CRUD functions.
	 * @param config
	 * @param directorySecurityController
	 * @return
	 * @throws Exception
	 */
	public static AbstractFileSecurityHandler getFileSecurityHandlerInstance (BasicConfigurationController config, AbstractDirectorySecurityController directorySecurityController) throws Exception {
		checkConfiguration(config);
		if(config.isActivateSecurity()) {
			return new FileStoreDBHandler(config, directorySecurityController);
		}else {
			return null;
		}
	}
	
	/**
	 * Returns a new AbstractThumbnailHandler instance (responsible for managing the thumbnails persistence in the database).
	 * @param config a BasicConfigurationController containing all the necessary infos for instantiating the new AbstractThumbnailHandler.
	 * @return
	 * @throws Exception 
	 */
	public static AbstractThumbnailHandler getThumbnailHandler(BasicConfigurationController config) throws Exception {
		return new ThumbnailHandler(config);
	}
	
	private static void checkConfiguration(BasicConfigurationController config) 
			throws Exception {
		if (config == null) {
			throw new Exception(
					"InvalidConfigurationException: the configuration provided to get the handler or controller instance is null.");
		} else if (!config.isConfigurationCorrect()) {
			throw new Exception(
					"InvalidConfigurationException: the configuration provided to get the handler or controller instance is not consistant.");
		}
	}
	
}
