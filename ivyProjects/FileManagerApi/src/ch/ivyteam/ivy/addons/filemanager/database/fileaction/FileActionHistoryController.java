/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.fileaction;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.FileAction;
import ch.ivyteam.ivy.addons.filemanager.FileActionType;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileActionHistoryPersistence;
import ch.ivyteam.ivy.environment.Ivy;

/**
 * @author ec
 *
 */
public class FileActionHistoryController extends AbstractFileActionHistoryController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8399180559766999962L;
	/**USE {@link FileActionType#FILE_CREATED_ACTION} instead **/
	@Deprecated 
	public static short FILE_CREATED_ACTION = FileActionType.FILE_CREATED_ACTION;
	/**USE {@link FileActionType#FILE_CONTENT_CHANGED_ACTION} instead **/
	@Deprecated
	public static short FILE_CONTENT_CHANGED_ACTION = FileActionType.FILE_CONTENT_CHANGED_ACTION;
	/**USE {@link FileActionType#FILE_DESCRIPTION_CHANGED_ACTION} instead **/
	@Deprecated
	public static short FILE_DESCRIPTION_CHANGED_ACTION = FileActionType.FILE_DESCRIPTION_CHANGED_ACTION;
	/**USE {@link FileActionType#FILE_RENAMED_ACTION} instead **/
	@Deprecated
	public static short FILE_RENAMED_ACTION = FileActionType.FILE_RENAMED_ACTION;
	/**USE {@link FileActionType#FILE_DELETED_ACTION} instead **/
	@Deprecated
	public static short FILE_DELETED_ACTION = FileActionType.FILE_DELETED_ACTION;
	/**USE {@link FileActionType#FILE_DOWNLOADED_ACTION} instead **/
	@Deprecated
	public static short FILE_DOWNLOADED_ACTION = FileActionType.FILE_DOWNLOADED_ACTION;
	/**USE {@link FileActionType#FILE_PRINTED_ACTION} instead **/
	@Deprecated
	public static short FILE_PRINTED_ACTION = FileActionType.FILE_PRINTED_ACTION;
	/**USE {@link FileActionType#FILE_OPENED_ACTION} instead **/
	@Deprecated
	public static short FILE_OPENED_ACTION = FileActionType.FILE_OPENED_ACTION;
	/**USE {@link FileActionType#FILE_COPY_PASTE_ACTION} instead **/
	@Deprecated
	public static short FILE_COPY_PASTE_ACTION = FileActionType.FILE_COPY_PASTE_ACTION;
	/**USE {@link FileActionType#FILE_MOVED_ACTION} instead **/
	@Deprecated
	public static short FILE_MOVED_ACTION = FileActionType.FILE_MOVED_ACTION;
	/**USE {@link FileActionType#FILE_NEW_VERSION_ACTION} instead **/
	@Deprecated
	public static short FILE_NEW_VERSION_ACTION = FileActionType.FILE_NEW_VERSION_ACTION;
	/**USE {@link FileActionType#FILE_VERSION_ROLLBACK_ACTION} instead **/
	@Deprecated
	public static short FILE_VERSION_ROLLBACK_ACTION = FileActionType.FILE_VERSION_ROLLBACK_ACTION;

	
	private FileActionConfiguration config = null;
	IFileActionHistoryPersistence persistence = null;

	public FileActionHistoryController() {
		this(new FileActionConfiguration());

	}

	public FileActionHistoryController(FileActionConfiguration fileActionConfiguration) {
		this.setConfig(fileActionConfiguration);
	}

	@Override
	public void createNewActionHistory(long fileid, short actionType, String username, String actionInfos) throws Exception {
		FileAction fileAction = new FileAction();
		fileAction.setFileid(fileid);
		FileActionType fat = new FileActionType();
		fat.setType(actionType);
		fileAction.setFileActionType(fat);
		fileAction.setUsername(username);
		fileAction.setFullUsername(Ivy.session().getSessionUser().getFullName());
		fileAction.setInfo(actionInfos);
		
		this.persistence.create(fileAction);
	}
	
	@Override
	@Deprecated
	public void createNewActionHistory(long fileid, short actionType, String username, String actionInfos, Connection con) throws Exception {
		this.createNewActionHistory(fileid, actionType, username, actionInfos);
	}

	@Override
	public boolean actionTypeExists(short actionType) throws Exception {
		return this.persistence.actionTypeExists(actionType);
	}
	
	@Override
	@Deprecated
	public boolean actionTypeExists(short actionType, Connection con) throws Exception {
		return this.persistence.actionTypeExists(actionType);
	}

	@Override
	public List<FileActionType> getAllFileActionTypes(String lang) throws Exception {
		return this.persistence.getAllFileActionTypesInSpecifiedLangOrDefaultLang(lang);
	}

	@Override
	public List<FileAction> getFileActions(long fileid, String lang) throws Exception {
		return this.persistence.getFileActionsForFileInSpecifiedLangOrDefaultLang(fileid, lang);
	}

	@Override
	public boolean fileActionTypeTranslationExist(String lang) throws Exception {
		return this.persistence.actionTypesExistInGivenLanguage(lang);
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(FileActionConfiguration fileActionConfiguration) {
		if(fileActionConfiguration == null) {
			this.config = new FileActionConfiguration();
		} else {
			this.config = fileActionConfiguration;
		}
		BasicConfigurationController conf = new BasicConfigurationController();
		conf.setFileActionHistoryConfiguration(fileActionConfiguration);
		conf.setIvyDBConnectionName(fileActionConfiguration.getIvyDBConnectionName());
		conf.setStoreFilesInDB(true);
		try {
			persistence = PersistenceConnectionManagerFactory.getIFileActionPersistenceInstance(conf);
		} catch (Exception e) {
			Ivy.log().error("An error occurred while getting the IFileActionPersistenceInstance.", e);
		}
	}

	/**
	 * @return the config
	 */
	public FileActionConfiguration getConfig() {
		return config;
	}
}
