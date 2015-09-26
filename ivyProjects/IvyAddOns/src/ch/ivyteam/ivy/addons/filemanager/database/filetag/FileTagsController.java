/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.filetag;



import ch.ivyteam.ivy.addons.filemanager.FileTag;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileTagPersistence;

/**
 * @author ec
 *
 */
public class FileTagsController extends AbstractFileTagsController {

	private IFileTagPersistence ftPersistence;
	BasicConfigurationController config;
	
	/**
	 * 
	 * @param _config
	 */
	public FileTagsController (BasicConfigurationController _config)
	{
		this.config = _config==null?new BasicConfigurationController(): _config;
		this.ftPersistence = PersistenceConnectionManagerFactory.getIFileTagPersistenceInstance(_config);
		
	}
	
	@Override
	public FileTag createTag(long fileId, String tag) throws Exception
	{
		FileTag ft = new FileTag();
		ft.setFileId(fileId);
		ft.setTag(tag);
		return this.ftPersistence.create(ft);
	}
	
	
	@Override
	public FileTag modifyTag(long tagId, String tag) throws Exception 
	{
		FileTag ft = this.getTagById(tagId);
		ft.setTag(tag);
		return this.ftPersistence.update(ft);
	}
	
	@Override
	public void deleteTag(long tagId) throws Exception
	{
		this.ftPersistence.delete(this.getTagById(tagId));
	}
	
	@Override
	public FileTag getTag(long fileId, String tag) throws Exception
	{
		return this.ftPersistence.getFileTag(fileId, tag);
	}
	
	@Override
	public FileTag getTagById(long tagId) throws Exception
	{
		return this.ftPersistence.get(tagId);
	}
	
	@Override
	public java.util.List<FileTag> getTagsWithPattern(long fileId, String tagPattern) throws Exception
	{
		return this.ftPersistence.getTagsWithPatternAndOptionalFileId(fileId, tagPattern);
	}
	
	@Override
	public java.util.List<String> searchAvailableTags(String searchFor)
			throws Exception {
		return this.ftPersistence.searchAvailableTags(searchFor);
	}
	
	@Override
	public java.util.List<FileTag> getFileTags(long fileId) throws Exception
	{
		return this.ftPersistence.getFileTags(fileId);
	}
	
	
	
	@Override
	public void deleteFileTags(long fileId) throws Exception {
		java.util.List<FileTag> tags =  getFileTags(fileId);
		for(FileTag ft: tags) {
			this.deleteTag(ft.getId());
		}
		
	}

	/**
	 * @return the ivyDBConnectionName
	 */
	@Deprecated
	public String getIvyDBConnectionName() {
		return this.config!=null?this.config.getIvyDBConnectionName():"";
	}
	
	/**
	 * @param ivyDBConnectionName the ivyDBConnectionName to set
	 */
	@Deprecated
	public void setIvyDBConnectionName(String ivyDBConnectionName) {
		this.config.setIvyDBConnectionName(ivyDBConnectionName);
		this.ftPersistence = PersistenceConnectionManagerFactory.getIFileTagPersistenceInstance(this.config);
		
	}
	
	/**
	 * @return the tableName
	 */
	@Deprecated
	public String getTableName() {
		return this.config!=null?this.config.getFileTagsTableName():"";
	}
	/**
	 * @param tableName the tableName to set
	 */
	@Deprecated
	public void setTableName(String tableName) {
		this.config.setFilesTableName(tableName);
		this.ftPersistence = PersistenceConnectionManagerFactory.getIFileTagPersistenceInstance(this.config);
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
		if(this.config.getDatabaseSchemaName().trim().isEmpty()) {
			return this.config.getFileTagsTableName();
		} else {
			return this.config.getDatabaseSchemaName().trim()+"."+this.config.getFileTagsTableName();
		}
	}
	/**
	 * @param filesTableNameSpace the filesTableNameSpace to set
	 */
	@Deprecated
	public void setFilesTableNameSpace(String filesTableNameSpace) {
		if(filesTableNameSpace!=null) {
			if(filesTableNameSpace.contains(".")) {
				String [] s = filesTableNameSpace.split("\\.");
				if(s.length==2) {
					this.config.setDatabaseSchemaName(s[0]);
					this.config.setFileTagsTableName(s[1]);
				}
			}else{
				this.config.setDatabaseSchemaName("");
				this.config.setFileTagsTableName(filesTableNameSpace);
			}
			this.ftPersistence = PersistenceConnectionManagerFactory.getIFileTagPersistenceInstance(this.config);
		}
	}
	
	/**
	 * @param schemaName the schemaName to set
	 */
	@Deprecated
	public void setSchemaName(String schemaName) {
		this.config.setDatabaseSchemaName(schemaName);
		this.ftPersistence = PersistenceConnectionManagerFactory.getIFileTagPersistenceInstance(this.config);
	}

}
