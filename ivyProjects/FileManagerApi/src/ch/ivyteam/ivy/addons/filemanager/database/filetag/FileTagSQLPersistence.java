/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.filetag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.FileTag;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileTagPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.sql.FileTagSQLQueries;
import ch.ivyteam.ivy.addons.filemanager.database.sql.PersistenceConnectionManagerReleaser;

/**
 * @author ec
 *
 */
public class FileTagSQLPersistence implements IFileTagPersistence {

	private IPersistenceConnectionManager<Connection> connectionManager = null;
	private BasicConfigurationController configuration;
	private String tableNameSpace;
	
	@SuppressWarnings("unchecked")
	public FileTagSQLPersistence(BasicConfigurationController config) {
		this.configuration=config;
		this.connectionManager = ((IPersistenceConnectionManager<Connection>) PersistenceConnectionManagerFactory
				.getPersistenceConnectionManagerInstance(config));
		this.initialize();
	}
	
	/**
	 * For unit tests
	 * @param connectionManager
	 * @param tableNameSpace
	 */
	FileTagSQLPersistence(IPersistenceConnectionManager<Connection> connectionManager, String tableNameSpace) {
		this.connectionManager = connectionManager;
		this.tableNameSpace = tableNameSpace;
	}
	
	private void initialize() {
		if(this.configuration.getDatabaseSchemaName()!=null  && this.configuration.getDatabaseSchemaName().length()>0) {
			this.tableNameSpace = this.configuration.getDatabaseSchemaName()+"."+this.configuration.getFileTagsTableName();
		}else {
			this.tableNameSpace = this.configuration.getFileTagsTableName();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.persistence.IItemPersistence#create(java.lang.Object)
	 */
	@Override
	public FileTag create(FileTag fileTag) throws Exception {
		assert(fileTag!=null && fileTag.getFileId()>0):"IllegalArgumentException the fileTag parameter in the FileTagSQLPersistence create Method is invalid";
		FileTag tag = this.getFileTag(fileTag.getFileId(), fileTag.getTag());
		if(tag != null) {
			return tag;
		}
		String query = FileTagSQLQueries.INSERT_FILETAG.replace(FileTagSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		
		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileTag.getFileId());
			stmt.setString(2, fileTag.getTag());
			stmt.executeUpdate();
		}finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "create", this.getClass());
		}
		return this.getFileTag(fileTag.getFileId(), fileTag.getTag());
		
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.persistence.IItemPersistence#update(java.lang.Object)
	 */
	@Override
	public FileTag update(FileTag fileTag) throws Exception {
		assert(fileTag!=null && fileTag.getId()>0):"IllegalArgumentException the fileTag parameter in the FileTagSQLPersistence update Method is invalid";
		FileTag tag = this.get(fileTag.getId());
		assert(tag!=null):"The filetag to update does not exists.";
		
		String query = FileTagSQLQueries.UPDATE_FILETAG_BY_ID.replace(FileTagSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, fileTag.getTag());
			stmt.setLong(2,fileTag.getId());
			stmt.executeUpdate();
		}finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "update", this.getClass());
		}
		
		return this.get(fileTag.getId());
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.persistence.IItemPersistence#get(java.lang.String)
	 */
	@Override
	public FileTag get(String fileTagIdAsString) throws Exception {
		if(fileTagIdAsString == null || fileTagIdAsString.trim().isEmpty()) {
			throw new IllegalArgumentException("The File tag id as String argument is null or empty.");
		}
		return get(Long.parseLong(fileTagIdAsString));
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.persistence.IItemPersistence#get(long)
	 */
	@Override
	public FileTag get(long id) throws Exception {
		assert(id>0):"IllegalArgumentException the id parameter in the FileTagSQLPersistence get long Method is invalid";
		
		String query = FileTagSQLQueries.SELECT_FILETAG_BY_ID.replace(FileTagSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		ResultSet rst = null;
		FileTag tag = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, id);
			rst = stmt.executeQuery();
			if(rst.next()) {
				tag = new FileTag();
				tag.setFileId(rst.getLong("fileid"));
				tag.setId(id);
				tag.setTag(rst.getString("tag").trim());
			}
		}finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "get(long id)", this.getClass());
		}
		return tag;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.persistence.IItemPersistence#delete(java.lang.Object)
	 */
	@Override
	public boolean delete(FileTag fileTag) throws Exception {
		assert(fileTag!=null && fileTag.getId()>0):"IllegalArgumentException the fileTag parameter in the FileTagSQLPersistence delete Method is invalid";
		String query = FileTagSQLQueries.DELETE_FILETAG_BY_ID.replace(FileTagSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		boolean result = false;
		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileTag.getId());
			result = stmt.executeUpdate()==1;
		}finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "delete", this.getClass());
		}
		return result;
	}

	@Override
	public java.util.List<String> searchAvailableTags(String searchFor) throws Exception {
		String query;
		if(searchFor==null || searchFor.trim().isEmpty()) {
			query =  FileTagSQLQueries.SELECT_ALLFILETAGS.replace(FileTagSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		}else {
			query = FileTagSQLQueries.SELECT_ALL_FILETAGS_BY_PATTERN_CASE_INSENSITIVE.replace(FileTagSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
			searchFor=searchFor.toLowerCase();
			if(!searchFor.endsWith("%")) {
				searchFor+="%";
			}
		}
		PreparedStatement stmt=null;
		java.util.List<String> tags = new ArrayList<String>();
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			if(searchFor!=null && !searchFor.trim().isEmpty()) {
				stmt.setString(1, searchFor);
			}
			rst = stmt.executeQuery();
			while(rst.next()) {
				tags.add(rst.getString("tag"));
			}
		}finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "searchAvailableTags", this.getClass());
		}
		
		return tags.isEmpty()?null:tags;
		
	}

	@Override
	public FileTag getFileTag(long fileid, String tagValue) throws Exception {
		assert(fileid>0 && !tagValue.trim().isEmpty()):"IllegalArgumentException parameter in the FileTagSQLPersistence getFileTag(long fileid, String tagValue) Method is invalid";

		String query = FileTagSQLQueries.SELECT_FILETAG_BY_VALUE_AND_FILEID.replace(FileTagSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		ResultSet rst = null;
		FileTag tag = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileid);
			stmt.setString(2,tagValue.trim());
			rst = stmt.executeQuery();
			if(rst.next()) {
				tag = new FileTag();
				tag.setFileId(rst.getLong("fileid"));
				tag.setId(rst.getLong("id"));
				tag.setTag(rst.getString("tag").trim());
			}
		}finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getFileTag", this.getClass());
		}
		return tag;
	}

	
	
	/**
	 * NOT PUBLIC API
	 * @param connectionManager
	 */
	protected void setIPersistenceConnectionManager(
			IPersistenceConnectionManager<Connection> connectionManager) {
		this.connectionManager = (IPersistenceConnectionManager<Connection>) connectionManager;
		
	}

	@Override
	public List<FileTag> getFileTags(long fileId) throws Exception {
		String query = FileTagSQLQueries.SELECT_ALL_FILETAGS_BY_FILEID.replace(FileTagSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		ResultSet rst = null;
		java.util.List<FileTag> tags = new ArrayList<FileTag>();
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileId);
			rst = stmt.executeQuery();
			while(rst.next()) {
				FileTag tag = new FileTag();
				tag.setFileId(rst.getLong("fileid"));
				tag.setId(rst.getLong("id"));
				tag.setTag(rst.getString("tag").trim());
				tags.add(tag);
			}
		}finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getFileTags", this.getClass());
		}
		return tags;
	}
	
	@Override
	public List<FileTag> getTagsWithPatternAndOptionalFileId(long fileId,
			String tagPattern) throws Exception {
		String query;
		if(fileId<=0) {
			query =  FileTagSQLQueries.SELECT_ALL_FILETAGS_BY_PATTERN.replace(FileTagSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		}else {
			query = FileTagSQLQueries.SELECT_ALL_FILETAGS_BY_FILEID_AND_PATTERN.replace(FileTagSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		}
		
		PreparedStatement stmt=null;
		ResultSet rst = null;
		java.util.List<FileTag> tags = new ArrayList<FileTag>();
		try {
			
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			if(fileId<=0) {
				stmt.setString(1, "%"+tagPattern+"%");
			}else {
				stmt.setLong(1, fileId);
				stmt.setString(2, "%"+tagPattern+"%");
			}
			rst = stmt.executeQuery();
			while(rst.next()) {
				FileTag ft = new FileTag();
				ft.setId(rst.getLong("id"));
				ft.setFileId(rst.getLong("fileid"));
				ft.setTag(rst.getString("tag").trim());
				tags.add(ft);
			}
		}finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getTagsWithPatternAndOptionalFileId", this.getClass());
		}
		return tags;
	}

}
