/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.filetag;

import java.util.ArrayList;
import java.util.Iterator;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileTag;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileTagPersistence;
import ch.ivyteam.ivy.addons.filemanager.listener.AbstractFileActionListener;
import ch.ivyteam.ivy.addons.filemanager.listener.FileActionEvent;
import ch.ivyteam.ivy.addons.filemanager.listener.FileTagActionEvent;

/**
 * @author ec
 * 
 */
public class FileTagsController extends AbstractFileTagsController {

	private IFileTagPersistence ftPersistence;
	private BasicConfigurationController config;
	private ArrayList<AbstractFileActionListener> listeners = new ArrayList<AbstractFileActionListener>();

	/**
	 * 
	 * @param _config
	 */
	public FileTagsController(BasicConfigurationController _config) {
		this.config = _config == null ? new BasicConfigurationController()
				: _config;
		this.ftPersistence = PersistenceConnectionManagerFactory
				.getIFileTagPersistenceInstance(_config);
		if (_config.getFileActionListeners() != null) {
			listeners.addAll(_config.getFileActionListeners());
		}
	}

	@Override
	public FileTag createTag(long fileId, String tag) throws Exception {
		FileTag ft = new FileTag();
		ft.setFileId(fileId);
		ft.setTag(tag);
		ft = this.ftPersistence.create(ft);
		this.fireTagCreatedEvent(ft);
		return ft;
	}

	@Override
	public FileTag modifyTag(long tagId, String tag) throws Exception {
		FileTag ft = this.getTagById(tagId);
		ft.setTag(tag);
		ft = this.ftPersistence.update(ft);
		this.fireTagChangedEvent(ft);
		return ft;
	}

	@Override
	public void deleteTag(long tagId) throws Exception {
		FileTag tag = this.getTagById(tagId);
		if(tag!=null){
			if(this.ftPersistence.delete(tag)) {
				this.fireTagDeletedEvent(tag);
			}
		}
		
	}

	@Override
	public FileTag getTag(long fileId, String tag) throws Exception {
		return this.ftPersistence.getFileTag(fileId, tag);
	}

	@Override
	public FileTag getTagById(long tagId) throws Exception {
		return this.ftPersistence.get(tagId);
	}

	@Override
	public java.util.List<FileTag> getTagsWithPattern(long fileId,
			String tagPattern) throws Exception {
		return this.ftPersistence.getTagsWithPatternAndOptionalFileId(fileId,
				tagPattern);
	}

	@Override
	public java.util.List<String> searchAvailableTags(String searchFor)
			throws Exception {
		return this.ftPersistence.searchAvailableTags(searchFor);
	}

	@Override
	public java.util.List<FileTag> getFileTags(long fileId) throws Exception {
		return this.ftPersistence.getFileTags(fileId);
	}

	private synchronized void fireTagCreatedEvent(FileTag tag) {
		FileTagActionEvent event = new FileTagActionEvent(tag);
		FileActionEvent fae = makeFileActionEventWithFileId(tag);
		Iterator<AbstractFileActionListener> i = listeners.iterator();
		while (i.hasNext()) {
			AbstractFileActionListener listen = i.next();
			listen.fileTagCreated(event);
			listen.fileChanged(fae);
		}
	}
	private synchronized void fireTagChangedEvent(FileTag tag) {
		FileTagActionEvent event = new FileTagActionEvent(tag);
		FileActionEvent fae = makeFileActionEventWithFileId(tag);
		Iterator<AbstractFileActionListener> i = listeners.iterator();
		while (i.hasNext()) {
			AbstractFileActionListener listen = i.next();
			listen.fileTagChanged(event);
			listen.fileChanged(fae);
		}
	}
	
	private synchronized void fireTagDeletedEvent(FileTag tag) {
		FileTagActionEvent event = new FileTagActionEvent(tag);
		FileActionEvent fae = makeFileActionEventWithFileId(tag);
		Iterator<AbstractFileActionListener> i = listeners.iterator();
		while (i.hasNext()) {
			AbstractFileActionListener listen = i.next();
			listen.fileTagDeleted(event);
			listen.fileChanged(fae);
		}
	}
	
	private FileActionEvent makeFileActionEventWithFileId(FileTag tag) {
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID(String.valueOf(tag.getFileId()));
		FileActionEvent fae = new FileActionEvent(doc);
		return fae;
	}

	/**
	 * @return the ivyDBConnectionName
	 */
	@Deprecated
	public String getIvyDBConnectionName() {
		return this.config != null ? this.config.getIvyDBConnectionName() : "";
	}

	/**
	 * @param ivyDBConnectionName
	 *            the ivyDBConnectionName to set
	 */
	@Deprecated
	public void setIvyDBConnectionName(String ivyDBConnectionName) {
		this.config.setIvyDBConnectionName(ivyDBConnectionName);
		this.ftPersistence = PersistenceConnectionManagerFactory
				.getIFileTagPersistenceInstance(this.config);

	}

	/**
	 * @return the tableName
	 */
	@Deprecated
	public String getTableName() {
		return this.config != null ? this.config.getFileTagsTableName() : "";
	}

	/**
	 * @param tableName
	 *            the tableName to set
	 */
	@Deprecated
	public void setTableName(String tableName) {
		this.config.setFilesTableName(tableName);
		this.ftPersistence = PersistenceConnectionManagerFactory
				.getIFileTagPersistenceInstance(this.config);
	}

	/**
	 * @return the schemaName
	 */
	@Deprecated
	public String getSchemaName() {
		return this.config.getDatabaseSchemaName();
	}

	/**
	 * @return the filesTableNameSpace
	 */
	@Deprecated
	public String getFilesTableNameSpace() {
		if (this.config.getDatabaseSchemaName().trim().isEmpty()) {
			return this.config.getFileTagsTableName();
		} else {
			return this.config.getDatabaseSchemaName().trim() + "."
					+ this.config.getFileTagsTableName();
		}
	}

	/**
	 * @param filesTableNameSpace
	 *            the filesTableNameSpace to set
	 */
	@Deprecated
	public void setFilesTableNameSpace(String filesTableNameSpace) {
		if (filesTableNameSpace != null) {
			if (filesTableNameSpace.contains(".")) {
				String[] s = filesTableNameSpace.split("\\.");
				if (s.length == 2) {
					this.config.setDatabaseSchemaName(s[0]);
					this.config.setFileTagsTableName(s[1]);
				}
			} else {
				this.config.setDatabaseSchemaName("");
				this.config.setFileTagsTableName(filesTableNameSpace);
			}
			this.ftPersistence = PersistenceConnectionManagerFactory
					.getIFileTagPersistenceInstance(this.config);
		}
	}

	/**
	 * @param schemaName
	 *            the schemaName to set
	 */
	@Deprecated
	public void setSchemaName(String schemaName) {
		this.config.setDatabaseSchemaName(schemaName);
		this.ftPersistence = PersistenceConnectionManagerFactory
				.getIFileTagPersistenceInstance(this.config);
	}

}
