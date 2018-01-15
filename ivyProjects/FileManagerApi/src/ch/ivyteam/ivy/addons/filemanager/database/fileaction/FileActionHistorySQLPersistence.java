package ch.ivyteam.ivy.addons.filemanager.database.fileaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import ch.ivyteam.ivy.addons.filemanager.FileAction;
import ch.ivyteam.ivy.addons.filemanager.FileActionType;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileActionHistoryPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.sql.FileActionSQLQueries;
import ch.ivyteam.ivy.addons.filemanager.database.sql.PersistenceConnectionManagerReleaser;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.Time;

public class FileActionHistorySQLPersistence implements IFileActionHistoryPersistence {

	private IPersistenceConnectionManager<Connection> connectionManager = null;
	private FileActionConfiguration config;

	@SuppressWarnings("unchecked")
	public FileActionHistorySQLPersistence(FileActionConfiguration fileActionConfiguration) {
		if(fileActionConfiguration==null) {
			this.config = new FileActionConfiguration();
		}else {
			this.config = fileActionConfiguration;
		}
		BasicConfigurationController configuration = new BasicConfigurationController();
		configuration.setIvyDBConnectionName(this.config.getIvyDBConnectionName());
		configuration.setFileActionHistoryConfiguration(fileActionConfiguration);
		configuration.setStoreFilesInDB(true);
		this.connectionManager =  ((IPersistenceConnectionManager<Connection>) PersistenceConnectionManagerFactory
				.getPersistenceConnectionManagerInstance(configuration));

	}

	@Override
	public FileAction create(FileAction fileAction) throws Exception {
		this.checkFileActionArgument(fileAction);
		String query = FileActionSQLQueries.INSERT_FILEACTION.replace(FileActionSQLQueries.TABLENAMESPACE_FOR_FILEACTIONS_PLACEHOLDER, this.config.getFileActionHistoryTableNameSpace());
		PreparedStatement stmt = null;
		try {
			try{
				stmt = this.connectionManager.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			} catch (SQLFeatureNotSupportedException ex) {
				stmt = this.connectionManager.getConnection().prepareStatement(query);
			}
			
			stmt.setLong(1, fileAction.getFileid());
			stmt.setShort(2, (Short) fileAction.getFileActionType().getType());
			stmt.setString(3, fileAction.getUsername());
			stmt.setString(4, fileAction.getFullUsername()==null?"unkown":fileAction.getFullUsername());
			stmt.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
			stmt.setTime(6, new java.sql.Time(new java.util.Date().getTime()));
			stmt.setString(7, fileAction.getInfo().trim());

			stmt.executeUpdate();
			try {
				ResultSet keys = stmt.getGeneratedKeys();
				if(keys.next()) {
					fileAction.setId(stmt.getGeneratedKeys().getLong(1));
				}
			} catch( SQLException ex) {
				// do nothing especially oracle: SQLException: Invalid column type: getLong not implemented for class oracle.jdbc.driver.T4CRowidAccessor
			}

		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "create", this.getClass());
		}
		return fileAction;
	}

	@Override
	public FileAction update(FileAction fileAction) throws Exception {
		this.checkFileActionArgument(fileAction);
		if(fileAction.getId() <= 0) {
			throw new IllegalArgumentException("The fileaction id must be greater than zero.");
		}
		String sql = FileActionSQLQueries.UPDATE_FILEACTION.replace(FileActionSQLQueries.TABLENAMESPACE_FOR_FILEACTIONS_PLACEHOLDER, this.config.getFileActionHistoryTableNameSpace());
		PreparedStatement stmt = null;
		try{
			stmt = this.connectionManager.getConnection().prepareStatement(sql);
			stmt.setShort(1, (Short) fileAction.getFileActionType().getType());
			stmt.setString(2, fileAction.getUsername());
			stmt.setString(3, fileAction.getFullUsername());
			stmt.setDate(4, new java.sql.Date(new java.util.Date().getTime()));
			stmt.setTime(5, new java.sql.Time(new java.util.Date().getTime()));
			stmt.setString(6, fileAction.getInfo().trim());
			stmt.setLong(7, fileAction.getId());
			stmt.executeUpdate();
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "update", this.getClass());
		}
		return get(fileAction.getId());
	}

	@Override
	public FileAction get(String fileActionIdAsString) throws Exception {
		if(fileActionIdAsString == null || fileActionIdAsString.trim().isEmpty()) {
			throw new IllegalArgumentException("The fileActionIdAsString argument is null or empty.");
		}
		return get(Long.parseLong(fileActionIdAsString));
	}

	@Override
	public FileAction get(long id) throws Exception {
		if(id <=0) {
			throw new IllegalArgumentException("Invalid id parameter in FileActionHistorySQLPersistence.get");
		}
		String sql = FileActionSQLQueries.GET_FILEACTION_BY_ID.
				replace(FileActionSQLQueries.TABLENAMESPACE_FOR_FILEACTIONS_PLACEHOLDER, this.config.getFileActionHistoryTableNameSpace()).
				replace(FileActionSQLQueries.TABLENAMESPACE_FOR_ACTIONTYPE_PLACEHOLDER, this.config.getFileActionTypeNameSpace());
		PreparedStatement stmt = null;
		ResultSet rst = null;
		FileAction fa = null;
		try{
			Ivy.log().debug(sql);
			stmt = this.connectionManager.getConnection().prepareStatement(sql);
			stmt.setLong(1, id);

			rst = stmt.executeQuery();
			String lang = "en";
			try{
				lang = this.getUserLanguage();
			} catch(Exception ex) {
				//do nothing
			}
			if(rst.next()) {
				fa = new FileAction();
				fa.setId(rst.getLong("id"));
				fa.setFileid(rst.getLong("file_id"));
				fa.setUsername(rst.getString("usern")); 
				fa.setFullUsername(rst.getString("uname"));	
				fa.setInfo(rst.getString("adesc"));
				try {
					fa.setDesc(rst.getString(lang.trim().toLowerCase()));
				} catch (Exception ex) {// if the desired language column does not exist, we switch to English per default
					fa.setDesc(rst.getString("en"));
				}
				DateFormat formatter = null;
				if(lang.equalsIgnoreCase("en")) {
					formatter = new SimpleDateFormat("yyyy-MM-dd");
				} else {
					formatter = new SimpleDateFormat("dd.MM.yyyy");
				}
				fa.setDate(new Date(formatter.format(rst.getDate("ddate"))));
				fa.setTime(new Time(DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.GERMAN).format(rst.getTime("ttime"))));

			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "get(long id)", this.getClass());
		}
		return fa;
	}

	@Override
	public boolean delete(FileAction fileAction) throws Exception {
		if(fileAction == null || fileAction.getId() <= 0) {
			throw new IllegalArgumentException("The fileaction to delete cannot be null and its id must be greater than zero.");
		}
		boolean result = false;
		String sql = FileActionSQLQueries.DELETE_FILEACTION_BYID.replace(FileActionSQLQueries.TABLENAMESPACE_FOR_FILEACTIONS_PLACEHOLDER, this.config.getFileActionHistoryTableNameSpace());
		PreparedStatement stmt = null;
		try{
			stmt = this.connectionManager.getConnection().prepareStatement(sql);
			stmt.setLong(1, fileAction.getId());
			result = stmt.executeUpdate()>0;
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "delete", this.getClass());
		}
		return result;
	}

	@Override
	public List<FileActionType> getAllFileActionTypesInSpecifiedLangOrDefaultLang(String lang) throws Exception {
		if(lang == null || lang.trim().length()==0) {
			lang="en";
		}
		List<FileActionType> atypes = new ArrayList<FileActionType>();
		String sql = FileActionSQLQueries.GET_ALL_FILEACTIONTYPES
				.replace(FileActionSQLQueries.TABLENAMESPACE_FOR_ACTIONTYPE_PLACEHOLDER, this.config.getFileActionTypeNameSpace());
		PreparedStatement stmt = null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(sql);
			rst = stmt.executeQuery();
			while(rst.next()) {
				FileActionType fa = new FileActionType();
				fa.setId(rst.getInt("id"));
				fa.setType(rst.getInt("atype"));
				try {
					fa.setDesc(rst.getString(lang.trim().toLowerCase()));
				}catch(Exception ex) {// if the desired language column does not exist, we switch to english per default
					fa.setDesc(rst.getString("en"));
				}
				atypes.add(fa);
			}

		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getAllFileActionTypesInSpecifiedLangOrDefaultLang", this.getClass());
		}
		return atypes;
	}

	

	@Override
	public List<FileAction> getFileActionsForFileInSpecifiedLangOrDefaultLang(long fileid, String lang) throws Exception {
		if(fileid <=0) {
			throw new IllegalArgumentException("Invalid fileid parameter in FileActionHistorySQLPersistence.getAllFileActionTypesInSpecifiedLangOrDefaultLang");
		}
		if(lang == null || lang.trim().length()==0) {
			lang = "en";
		}
		List<FileAction> actions = new ArrayList<FileAction>();
		String sql = FileActionSQLQueries.GET_FILEACTIONS_BY_FILEID.
				replace(FileActionSQLQueries.TABLENAMESPACE_FOR_FILEACTIONS_PLACEHOLDER, this.config.getFileActionHistoryTableNameSpace()).
				replace(FileActionSQLQueries.TABLENAMESPACE_FOR_ACTIONTYPE_PLACEHOLDER, this.config.getFileActionTypeNameSpace());

		PreparedStatement stmt = null;
		ResultSet rst = null;
		try{
			Ivy.log().debug(sql);
			stmt = this.connectionManager.getConnection().prepareStatement(sql);
			stmt.setLong(1, fileid);

			rst = stmt.executeQuery();
			while(rst.next()) {
				FileAction fa = new FileAction();
				fa.setId(rst.getLong("id"));
				fa.setFileid(rst.getLong("file_id"));
				fa.setUsername(rst.getString("usern")); 
				fa.setFullUsername(rst.getString("uname"));	
				fa.setInfo(rst.getString("adesc"));
				try {
					fa.setDesc(rst.getString(lang.trim().toLowerCase()));
				} catch(Exception ex) {// if the desired language column does not exist, we switch to English per default
					fa.setDesc(rst.getString("en"));
				}
				DateFormat formatter = null;
				if(lang.equalsIgnoreCase("en")) {
					formatter = new SimpleDateFormat("yyyy-MM-dd");
				} else {
					formatter = new SimpleDateFormat("dd.MM.yyyy");
				}
				fa.setDate(new Date(formatter.format(rst.getDate("ddate"))));
				fa.setTime(new Time(DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.GERMAN).format(rst.getTime("ttime"))));

				actions.add(fa);
			}
			rst.close();
		}finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getFileActionsForFileInSpecifiedLangOrDefaultLang", this.getClass());
		}
		return actions;
	}

	@Override
	public boolean actionTypesExistInGivenLanguage(String lang) throws Exception {
		if(lang == null || lang.trim().isEmpty()) {
			throw new IllegalArgumentException("The language parameter cannot be null or empty in actionTypeExistsInGivenLanguage method.");
		}
		String sql = FileActionSQLQueries.GET_ALL_FILEACTIONTYPES
				.replace(FileActionSQLQueries.TABLENAMESPACE_FOR_ACTIONTYPE_PLACEHOLDER, this.config.getFileActionTypeNameSpace());
		PreparedStatement stmt = null;
		ResultSet rst = null;
		boolean result = false;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(sql);
			rst = stmt.executeQuery();
			ResultSetMetaData rsmd = rst.getMetaData();
			int nb = rsmd.getColumnCount();
			for(int i=0; i<nb; i++) {
				if(lang.equals(rsmd.getColumnName(i))) {
					result = true;
					break;
				}
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "actionTypesExistInGivenLanguage", this.getClass());
		}
		return result;
	}

	private void checkFileActionArgument(final FileAction fileAction) throws Exception {
		if(fileAction == null) {
			throw new IllegalArgumentException("The file action parameter cannot be null");
		}
		if(fileAction.getFileid() <= 0) {
			throw new IllegalArgumentException("The file action parameter must be related to a file. Its fileId must be a valid id.");
		}
		if(fileAction.getFileActionType() == null ) {
			throw new IllegalArgumentException("The file action parameter must be related to an existing fileActionType. Its fileActionType field is null.");
		}
		if(!actionTypeExists(fileAction.getFileActionType().getType())) {
			throw new IllegalArgumentException(
					String.format("The file action parameter must be related to an existing fileActionType. " +
							"The fileActionType number %s does not exist.", fileAction.getFileActionType().getType()));
		}

		if(fileAction.getUsername() == null || fileAction.getUsername().trim().isEmpty()) {
			fileAction.setUsername(Ivy.session().getSessionUserName());
		}

		if(fileAction.getFullUsername() == null || fileAction.getFullUsername().trim().isEmpty()) {
			try {
				String uname = Ivy.session().getSecurityContext().executeAsSystemUser(new Callable<String> (){

					@Override
					public String call() throws Exception {
						return Ivy.session().getSecurityContext().findUser(fileAction.getUsername()).getFullName();
					}
					
				});
				fileAction.setFullUsername(uname);
			}catch(Exception ex) {
				fileAction.setFullUsername("unknown");
			}
		}
		if(fileAction.getInfo() == null) {
			fileAction.setInfo("");
		} else if(fileAction.getInfo().trim().length()>1600) {
			fileAction.setInfo(fileAction.getInfo().trim().substring(0, 1600));
		}
	}

	@Override
	public boolean actionTypeExists(Number type) throws Exception {
		boolean r = false;
		String query = FileActionSQLQueries.GET_FILEACTIONTYPE_ID_BY_TYPE_NUMBER.replace(FileActionSQLQueries.TABLENAMESPACE_FOR_ACTIONTYPE_PLACEHOLDER, 
				this.config.getFileActionTypeNameSpace());
		PreparedStatement stmt = null;
		ResultSet rst = null;
		try{
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setShort(1, (Short) type);
			rst = stmt.executeQuery();
			r = rst.next();
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "actionTypeExists", this.getClass());
		}

		return r;
	}

	private String getUserLanguage() {
		return Ivy.session().getContentLocale().getLanguage().toLowerCase();
	}
	
}
