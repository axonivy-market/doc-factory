/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileManagementHandlersFactory;
import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.ItemTranslation;
import ch.ivyteam.ivy.addons.filemanager.Language;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IItemTranslationPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.TranslatedFileManagerItemsEnum;
import ch.ivyteam.ivy.addons.filemanager.database.sql.TranslateItemSQLQueries;
import ch.ivyteam.ivy.environment.Ivy;

/**
 * @author ec
 *
 */
public class ItemTranslationSQLPersistence implements
		IItemTranslationPersistence {

	private BasicConfigurationController configuration;
	private IPersistenceConnectionManager<Connection> connectionManager =null;
	private String tableNameSpace;
	private List<Language> langs;
	private TranslatedFileManagerItemsEnum itemType;
	
	@SuppressWarnings("unused")
	private ItemTranslationSQLPersistence() {}
	
	@SuppressWarnings("unchecked")
	public ItemTranslationSQLPersistence (BasicConfigurationController config, TranslatedFileManagerItemsEnum itemType) throws Exception {
		this.itemType = itemType;
		this.configuration=config;
		this.connectionManager =  (IPersistenceConnectionManager<Connection>) PersistenceConnectionManagerFactory.getPersistenceConnectionManagerInstance(config);
		this.langs = PersistenceConnectionManagerFactory.getILanguagePersistenceInstance(config).getList();
		this.initialize();
	}
	
	private void initialize() {
		if(this.configuration.getDatabaseSchemaName()!=null  && this.configuration.getDatabaseSchemaName().length()>0) {
			if(this.itemType==TranslatedFileManagerItemsEnum.FOLDER_ON_SERVER) {
				this.tableNameSpace = this.configuration.getDatabaseSchemaName()+"."+this.configuration.getDirectoriesTranslationTableName();
			} else if(this.itemType==TranslatedFileManagerItemsEnum.FILETYPE){
				this.tableNameSpace = this.configuration.getDatabaseSchemaName()+"."+this.configuration.getFileTypesTranslationTableName();
			}
		}else{
			if(this.itemType==TranslatedFileManagerItemsEnum.FOLDER_ON_SERVER) {
				this.tableNameSpace = this.configuration.getDirectoriesTranslationTableName();
			} else if(this.itemType==TranslatedFileManagerItemsEnum.FILETYPE){
				this.tableNameSpace = this.configuration.getFileTypesTranslationTableName();
			}
		}
	}
	
	/**
	 * Not public for tests only
	 * @param config
	 * @throws Exception
	 */
	protected ItemTranslationSQLPersistence (BasicConfigurationController config, int i, List<Language> langs) throws Exception {
		if(i==1) { 
			this.itemType = TranslatedFileManagerItemsEnum.FILETYPE ;
		} else this.itemType=TranslatedFileManagerItemsEnum.FOLDER_ON_SERVER;
		
		this.configuration=config;
		this.langs =langs;
		//this.connectionManager =  (IPersistenceConnectionManager<Connection>) PersistenceConnectionManagerFactory.getPersistenceConnectionManagerInstance(config);
		//this.langs = PersistenceConnectionManagerFactory.getILanguagePersistenceInstance(config).getList();
		this.initialize();
	}
	
	/**
	 * NOT PUBLIC, for tests only.
	 * @param connectionManager
	 */
	protected void setIPersistenceConnectionManager(IPersistenceConnectionManager<Connection> connectionManager) {
		this.connectionManager = connectionManager;
	}
	
	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#create(java.lang.Object)
	 */
	@Override
	public ItemTranslation create(ItemTranslation itemTranslation)
			throws Exception {
		assert(itemTranslation !=null && itemTranslation.getTranslatedItemId()>0):"IllegalArgumentException in create ItemTranslationSQLPersistence.";
		ItemTranslation tmp = this.getExistingTranslation(itemTranslation.getTranslatedItemId());
		if(tmp!=null && !tmp.getTranslations().isEmpty()) {
			//a translation already exists for the directory
			return this.get(itemTranslation.getTranslatedItemId());
		}
		String query = TranslateItemSQLQueries.CREATE_ITEM_TRANSLATION.replace(TranslateItemSQLQueries.TRANSLATION_TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		
		PreparedStatement stmt=null;
		try {
			HashMap<String,String> map = itemTranslation.getTranslations();
			//Ensure all filemanager languages are represented
			for(Language l : this.langs) {
				if(!map.containsKey(l.getIsoName())) {
					map.put(l.getIsoName(), "");
				}
			}
			Iterator<Entry<String,String>> iter =map.entrySet().iterator();
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1,itemTranslation.getTranslatedItemId());
			while(iter.hasNext()) {
				Entry<String,String> e = iter.next();
				stmt.setString(2, e.getKey().toUpperCase());
				stmt.setString(3, e.getValue());
				stmt.executeUpdate();
			}
		}finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch( SQLException ex) {
					Ivy.log().error("PreparedStatement cannot be closed in create method, LanguageSQLPersistence.",ex);
				}
			}
			this.connectionManager.closeConnection();
		}
		return this.get(itemTranslation.getTranslatedItemId());
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#update(java.lang.Object)
	 */
	@Override
	public ItemTranslation update(ItemTranslation itemTranslation) throws Exception {
		assert(itemTranslation !=null && itemTranslation.getTranslatedItemId()>0):"IllegalArgumentException in update ItemTranslationSQLPersistence.";
		
		String query = TranslateItemSQLQueries.UPDATE_ITEM_TRANSLATION.replace(TranslateItemSQLQueries.TRANSLATION_TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		try {
			ItemTranslation temp = this.getExistingTranslation(itemTranslation.getTranslatedItemId());
			if(temp==null) {
				return this.create(itemTranslation);
			}
			HashMap<String,String> map2 =temp.getTranslations();
			HashMap<String,String> map3 = new HashMap<String,String>();
			//Ensure all filemanager languages are represented
			for(Language l : this.langs) {
				if(!map2.containsKey(l.getIsoName())) {
					//We put the missing languages
					map3.put(l.getIsoName(), "");
				}
			}
			HashMap<String,String> map = itemTranslation.getTranslations();
			if(!map3.isEmpty()) {
				//We save the missing languages for the given item id.
				String q = TranslateItemSQLQueries.CREATE_ITEM_TRANSLATION.replace(TranslateItemSQLQueries.TRANSLATION_TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
				Iterator<Entry<String,String>> it =map3.entrySet().iterator();
				try {
					stmt = this.connectionManager.getConnection().prepareStatement(q);
					stmt.setLong(1,itemTranslation.getTranslatedItemId());
					while(it.hasNext()) {
						Entry<String,String> e = it.next();
						stmt.setString(2, e.getKey().toUpperCase());
						stmt.setString(3, e.getValue());
						stmt.executeUpdate();
					}
				}finally {
					if(stmt!=null) {
						try {
							stmt.close();
						} catch( SQLException ex) {
							Ivy.log().error("PreparedStatement cannot be closed in create method, LanguageSQLPersistence.",ex);
						}
					}
				}
				map3 = this.getExistingTranslation(itemTranslation.getTranslatedItemId()).getTranslations();
				it =map3.entrySet().iterator();
				while(it.hasNext()) {
					Entry<String,String> en = it.next();
					if(!map.containsKey(en.getKey())) {
						map.put(en.getKey(), en.getValue());
					}
				}
			}
			
			Iterator<Entry<String,String>> iter =map.entrySet().iterator();
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(2,itemTranslation.getTranslatedItemId());
			while(iter.hasNext()) {
				Entry<String,String> e = iter.next();
				stmt.setString(1, e.getValue());
				stmt.setString(3, e.getKey().toUpperCase());
				stmt.executeUpdate();
			}
		}finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch( SQLException ex) {
					Ivy.log().error("PreparedStatement cannot be closed in create method, LanguageSQLPersistence.",ex);
				}
			}
			this.connectionManager.closeConnection();
		}
		return this.get(itemTranslation.getTranslatedItemId());
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#get(java.lang.String)
	 */
	@Override
	public ItemTranslation get(String uniqueDescriptor) throws Exception {
		assert(uniqueDescriptor!=null && uniqueDescriptor.trim().length()>0):"IllegalArgumentException in get(long id) ItemTranslationSQLPersistence.";
		long id = 0;
		if(this.itemType==TranslatedFileManagerItemsEnum.FOLDER_ON_SERVER){
			FolderOnServer fos = FileManagementHandlersFactory.getFileSecurityHandlerInstance(this.configuration).getDirectoryWithPath(uniqueDescriptor);
			if(fos!=null) {
				id = fos.getId();
			}
		} else if(this.itemType==TranslatedFileManagerItemsEnum.FILETYPE) {
			DocumentOnServer doc = FileManagementHandlersFactory.getFileManagementHandlerInstance(this.configuration).getDocumentOnServer(uniqueDescriptor);
			if(doc!=null && doc.getFileType()!=null){
				id = doc.getFileType().getId();
			}
		} 
		if(id>0) {
			return this.get(id);
		}else {
			return null;
		}
		
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#get(long)
	 */
	/**
	 * This overriden Implementation of the get method, never returns null if no ItemTranslation was. Found. In such a case, the returned translation is empty.
	 */
	@Override
	public ItemTranslation get(long id) throws Exception {
		assert(id>0):"IllegalArgumentException in get(long id) ItemTranslationSQLPersistence.";
		
		String query = TranslateItemSQLQueries.SELECT_TRANSLATIONS_BY_TRANSLATED_ITEMID.replace(TranslateItemSQLQueries.TRANSLATION_TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);

		PreparedStatement stmt=null;
		ItemTranslation tr = new ItemTranslation(id, null);
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, id);
			ResultSet rst = stmt.executeQuery();
			HashMap<String,String> map = new HashMap<String,String>();
			while(rst.next()) {
				map.put(rst.getString("lang"), rst.getString("tr"));
			}
			for(Language l : this.langs) {
				if(!map.containsKey(l.getIsoName())) {
					map.put(l.getIsoName(), "");
				}
			}
			tr.setTranslations(map);
			
		}finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch( SQLException ex) {
					Ivy.log().error("PreparedStatement cannot be closed in create method, LanguageSQLPersistence.",ex);
				}
			}
			this.connectionManager.closeConnection();
		}
		
		return tr;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#delete(java.lang.Object)
	 */
	@Override
	public boolean delete(ItemTranslation itemTranslation) throws Exception {
		assert(itemTranslation !=null && itemTranslation.getTranslatedItemId()>0):"IllegalArgumentException in update ItemTranslationSQLPersistence.";
		
		String query = TranslateItemSQLQueries.DELETE_ITEM_TRANSLATION_BY_ID.replace(TranslateItemSQLQueries.TRANSLATION_TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		boolean flag = false;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, itemTranslation.getTranslatedItemId());
			flag = stmt.executeUpdate()>0;
		}finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch( SQLException ex) {
					Ivy.log().error("PreparedStatement cannot be closed in create method, LanguageSQLPersistence.",ex);
				}
			}
			this.connectionManager.closeConnection();
		}
		return flag;
	}
	
	protected ItemTranslation getExistingTranslation(long id) throws Exception {
		assert(id>0):"IllegalArgumentException in get(long id) ItemTranslationSQLPersistence.";
		
		String query = TranslateItemSQLQueries.SELECT_TRANSLATIONS_BY_TRANSLATED_ITEMID.replace(TranslateItemSQLQueries.TRANSLATION_TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		ItemTranslation tr = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, id);
			ResultSet rst = stmt.executeQuery();
			
			HashMap<String,String> map = new HashMap<String,String>();
			while(rst.next()) {
				if(tr==null) {
					tr = new ItemTranslation(id, null);
				}
				map.put(rst.getString("lang"), rst.getString("tr"));
			}
			if(tr!=null) {
				tr.setTranslations(map);
			}
		}finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch( SQLException ex) {
					Ivy.log().error("PreparedStatement cannot be closed in create method, LanguageSQLPersistence.",ex);
				}
			}
			this.connectionManager.closeConnection();
		}
		
		return tr;
	}

}
