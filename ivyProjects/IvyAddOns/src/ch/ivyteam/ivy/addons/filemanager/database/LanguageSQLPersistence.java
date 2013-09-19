/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.Language;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.ILanguagePersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.sql.LanguagesSQLQueries;
import ch.ivyteam.ivy.environment.Ivy;

/**
 * @author ec
 *
 */
public class LanguageSQLPersistence implements ILanguagePersistence {
	
	private BasicConfigurationController configuration;
	private IPersistenceConnectionManager<Connection> connectionManager =null;
	private String tableNameSpace;
	
	@SuppressWarnings("unused")
	private LanguageSQLPersistence() {
		
	}

	@SuppressWarnings("unchecked")
	public LanguageSQLPersistence(BasicConfigurationController config) {
		this.configuration=config;
		this.connectionManager =  (IPersistenceConnectionManager<Connection>) PersistenceConnectionManagerFactory.getPersistenceConnectionManagerInstance(config);
		initialize();
	}
	
	private void initialize() {
		if(this.configuration.getDatabaseSchemaName()!=null  && this.configuration.getDatabaseSchemaName().length()>0) {
			this.tableNameSpace = this.configuration.getDatabaseSchemaName()+"."+this.configuration.getLanguageTableName();
		}else{
			this.tableNameSpace = this.configuration.getLanguageTableName();
		}
	}
	
	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#create(java.lang.Object)
	 */
	@Override
	public Language create(Language lang) throws Exception {
		assert(lang !=null && lang.getIsoName().trim().length()>0):"IllegalArgumentException in create LanguageSQLPersistence.";
		
		String query = LanguagesSQLQueries.CREATE_LANGUAGE.replace(LanguagesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, lang.getIsoName());
			stmt.executeUpdate();
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
		return this.get(lang.getIsoName());
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#update(java.lang.Object)
	 */
	@Override
	public Language update(Language lang) throws Exception {
		assert(lang !=null && lang.getIsoName().trim().length()>0):"IllegalArgumentException in update LanguageSQLPersistence.";
		long id = lang.getId();
		if(id<=0) {
			Language l = this.get(lang.getIsoName());
			id = l.getId();
		}
		if(id<=0) {
			return this.create(lang);
		}
		
		String query = LanguagesSQLQueries.UPDATE_LANGUAGE_BY_ID.replace(LanguagesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, lang.getIsoName());
			stmt.setLong(2, id);
			stmt.executeUpdate();
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
		return this.get(lang.getIsoName());
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#get(java.lang.String)Long
	 */
	@Override
	public Language get(String languageIsoName) throws Exception {
		assert(languageIsoName !=null && languageIsoName.trim().length()>0):"IllegalArgumentException in get(String languageIsoName) LanguageSQLPersistence.";
		String query = LanguagesSQLQueries.SELECT_LANGUAGE_BY_NAME.replace(LanguagesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		Language lang = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, languageIsoName.trim().toUpperCase());
			ResultSet rst = stmt.executeQuery();
			lang = new Language(rst.getLong("id"),rst.getString("isoname"));
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
		
		return lang;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#get(long)
	 */
	@Override
	public Language get(long id) throws Exception {
		assert(id>0):"IllegalArgumentException in get(long id) LanguageSQLPersistence.";
		String query = LanguagesSQLQueries.SELECT_LANGUAGE_BY_ID.replace(LanguagesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		Language lang = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, id);
			ResultSet rst = stmt.executeQuery();
			if(rst.next()) {
				lang = new Language(rst.getLong("id"),rst.getString("isoname"));
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
		
		return lang;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#delete(java.lang.Object)
	 */
	@Override
	public boolean delete(Language lang) throws Exception {
		assert(lang !=null && lang.getIsoName().trim().length()>0):"IllegalArgumentException in update LanguageSQLPersistence.";
		String query = LanguagesSQLQueries.DELETE_LANGUAGE.replace(LanguagesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		boolean flag = false;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, lang.getIsoName());
			flag = stmt.executeUpdate()==1;
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

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.ILanguagePersistence#getList()
	 */
	@Override
	public List<Language> getList() throws Exception {
		String query = LanguagesSQLQueries.SELECT_ALL_LANGUAGES.replace(LanguagesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		List<Language> langs = new ArrayList<Language>();
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			ResultSet rst = stmt.executeQuery();
			while(rst.next()) {
				langs.add(new Language(rst.getLong("id"),rst.getString("isoname")));
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
		return langs;
	}

}
