/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.DirectoryHelper;
import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.ItemTranslation;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFolderOnServerPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IItemTranslationPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.SqlPersistenceHelper;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.TranslatedFileManagerItemsEnum;
import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityConstants;
import ch.ivyteam.ivy.addons.filemanager.database.sql.DatabaseMetaDataAnalyzer;
import ch.ivyteam.ivy.addons.filemanager.database.sql.DocumentOnServerSQLQueries;
import ch.ivyteam.ivy.addons.filemanager.database.sql.FolderOnServerSQLQueries;
import ch.ivyteam.ivy.addons.filemanager.database.sql.PersistenceConnectionManagerReleaser;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.DateTime;
import ch.ivyteam.ivy.scripting.objects.Record;
import ch.ivyteam.ivy.scripting.objects.Time;

/**
 * @author ec
 *
 */
public class FolderOnServerSQLPersistence implements IFolderOnServerPersistence {
	
	private IPersistenceConnectionManager<Connection> connectionManager =null;
	private BasicConfigurationController configuration;
	private String directoriesTableNameSpace;
	private DatabaseMetaDataAnalyzer dbmdAnalyzer;
	private IItemTranslationPersistence dirI18nPersistence;
	
	@SuppressWarnings("unchecked")
	protected  FolderOnServerSQLPersistence (BasicConfigurationController config) {
		this.configuration=config;
		this.connectionManager =  (IPersistenceConnectionManager<Connection>) PersistenceConnectionManagerFactory
				.getPersistenceConnectionManagerInstance(config);
		this.initialize();
	}
	
	private void initialize() {
		if(this.configuration.getDatabaseSchemaName()!=null  && this.configuration.getDatabaseSchemaName().length()>0) {
			this.directoriesTableNameSpace = this.configuration.getDatabaseSchemaName()+"."+this.configuration.getDirectoriesTableName();
		}else{
			this.directoriesTableNameSpace = this.configuration.getDirectoriesTableName();
		}
		if(this.configuration.isActivateDirectoryTranslation()) {
			try {
				this.dirI18nPersistence =PersistenceConnectionManagerFactory.getIItemTranslationPersistenceInstance(this.configuration, TranslatedFileManagerItemsEnum.FOLDER_ON_SERVER);
			} catch (Exception e) {
				Ivy.log().error(e.getMessage(),e);
			}
		}
		this.dbmdAnalyzer = new DatabaseMetaDataAnalyzer(this.configuration);
	}
	
	@Override
	public boolean isOracle() {
		return this.dbmdAnalyzer.isOracle();
	}
	

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#create(java.lang.Object)
	 */
	@Override
	public FolderOnServer create(FolderOnServer fos) throws Exception {
		assert(fos!=null && fos.getPath()!=null) :
			"The FolderOnServer object to save is invalid.";
		fos.setPath(PathUtil.formatPathForDirectoryWithoutLastSeparator(fos.getPath()));
		assert(fos.getPath().length()>1):"The FolderOnServer object path to save is invalid.";
		
		String query = (dbmdAnalyzer.getSecurityVersion()>1?
				FolderOnServerSQLQueries.CREATE_FOLDER_Q2
				.replace(FolderOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.directoriesTableNameSpace):
					FolderOnServerSQLQueries.CREATE_FOLDER_Q1
					.replace(FolderOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.directoriesTableNameSpace));
		java.util.Date d = new java.util.Date();
		long l = d.getTime();
		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1,DirectoryHelper.getDirectoryNameFromPath(fos.getPath()));
			stmt.setString(2,fos.getPath());
			stmt.setString(3, Ivy.session().getSessionUserName());
			stmt.setDate(4, new java.sql.Date(l));
			stmt.setTime(5, new java.sql.Time(l));
			stmt.setString(6, Ivy.session().getSessionUserName());
			stmt.setDate(7, new java.sql.Date(l));
			stmt.setTime(8, new java.sql.Time(l));
			stmt.setInt(9, fos.getIs_protected()?1:0);
			stmt.setString(10, PathUtil.returnStringFromList(fos.getCmrd()));
			stmt.setString(11, PathUtil.returnStringFromList(fos.getCod()));
			stmt.setString(12, PathUtil.returnStringFromList(fos.getCud()));
			stmt.setString(13, PathUtil.returnStringFromList(fos.getCdd()));
			stmt.setString(14, PathUtil.returnStringFromList(fos.getCwf()));
			stmt.setString(15, PathUtil.returnStringFromList(fos.getCdf()));
			if(dbmdAnalyzer.getSecurityVersion()>1){
				stmt.setString(16, PathUtil.returnStringFromList(fos.getCcd()));
				stmt.setString(17, PathUtil.returnStringFromList(fos.getCrd()));
				stmt.setString(18, PathUtil.returnStringFromList(fos.getCtd()));
				stmt.setString(19, PathUtil.returnStringFromList(fos.getCcf()));
				stmt.setString(20, PathUtil.returnStringFromList(fos.getCuf()));
			}
			stmt.executeUpdate();
			Ivy.log().debug("Create folderOnServer query executed: {0}",query);
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "create", this.getClass());
		}
		fos = this.get(fos.getPath());
		if(this.dirI18nPersistence!=null) {
			setFolderTranslation(fos);
		}
		return fos;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#update(java.lang.Object)
	 */
	@Override
	public FolderOnServer update(FolderOnServer fos) throws Exception {
		assert(fos!=null && fos.getPath()!=null) : "The FolderOnServer object to save is invalid.";
		fos.setPath(PathUtil.formatPathForDirectoryWithoutLastSeparator(fos.getPath()));
		assert(fos.getPath().length()>1):"The FolderOnServer object path to save is invalid.";
		PreparedStatement stmt=null;
		if (fos.getId()>0) {
			stmt = this.makePreparedStatementForUpdateById(fos);
		} else {
			stmt = this.makePreparedStatementForUpdateByPath(fos);
		}
		try {
			stmt.executeUpdate();
			Ivy.log().debug("Folder after the update: {0}", fos);
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "update", this.getClass());
		}
		
		if(this.dirI18nPersistence!=null) {
			Ivy.log().debug("Updating translation : {0}", fos);
			this.dirI18nPersistence.update(fos.getTranslation());
		}
		fos = this.get(fos.getPath());
		Ivy.log().debug("Returned Folder : {0}", fos);
		return fos;
	}
	
	private PreparedStatement makePreparedStatementForUpdateById(FolderOnServer fos) throws Exception {
		String query = (dbmdAnalyzer.getSecurityVersion()>1?
				FolderOnServerSQLQueries.UPDATE_FOLDER_BY_ID_Q2
				.replace(FolderOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.directoriesTableNameSpace):
					FolderOnServerSQLQueries.UPDATE_FOLDER_BY_ID_Q1
					.replace(FolderOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.directoriesTableNameSpace));
		java.util.Date d = new java.util.Date();
		PreparedStatement stmt = this.connectionManager.getConnection().prepareStatement(query);
		stmt.setString(1,DirectoryHelper.getDirectoryNameFromPath(fos.getPath()));
		stmt.setString(2,fos.getPath());
		stmt.setString(3, fos.getCreationUser());
		stmt.setDate(4, new java.sql.Date(fos.getCreationDate().toNumber()));
		stmt.setTime(5, new java.sql.Time(fos.getCreationTime().toNumber()));
		stmt.setString(6, Ivy.session().getSessionUserName());
		stmt.setDate(7, new java.sql.Date(d.getTime()));
		stmt.setTime(8, new java.sql.Time(d.getTime()));
		stmt.setInt(9, 0);
		stmt.setString(10, PathUtil.returnStringFromList(fos.getCmrd()));
		stmt.setString(11, PathUtil.returnStringFromList(fos.getCod()));
		stmt.setString(12, PathUtil.returnStringFromList(fos.getCud()));
		stmt.setString(13, PathUtil.returnStringFromList(fos.getCdd()));
		stmt.setString(14, PathUtil.returnStringFromList(fos.getCwf()));
		stmt.setString(15, PathUtil.returnStringFromList(fos.getCdf()));
		
		if(dbmdAnalyzer.getSecurityVersion()>1){
			stmt.setString(16, PathUtil.returnStringFromList(fos.getCcd()));
			stmt.setString(17, PathUtil.returnStringFromList(fos.getCrd()));
			stmt.setString(18, PathUtil.returnStringFromList(fos.getCtd()));
			stmt.setString(19, PathUtil.returnStringFromList(fos.getCcf()));
			stmt.setString(20, PathUtil.returnStringFromList(fos.getCuf()));
			stmt.setLong(21,fos.getId());
		}else{
			stmt.setLong(16,fos.getId());
		}
		
		return stmt;
	}
	
	private PreparedStatement makePreparedStatementForUpdateByPath(FolderOnServer fos) throws Exception {
		String query = (dbmdAnalyzer.getSecurityVersion()>1?
				FolderOnServerSQLQueries.UPDATE_FOLDER_BY_PATH_Q2
				.replace(FolderOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.directoriesTableNameSpace):
					FolderOnServerSQLQueries.UPDATE_FOLDER_BY_PATH_Q1
					.replace(FolderOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.directoriesTableNameSpace));
		java.util.Date d = new java.util.Date();
		PreparedStatement stmt = this.connectionManager.getConnection().prepareStatement(query);
		stmt.setString(1,DirectoryHelper.getDirectoryNameFromPath(fos.getPath()));
		stmt.setString(2, fos.getCreationUser());
		stmt.setDate(3, new java.sql.Date(fos.getCreationDate().toNumber()));
		stmt.setTime(4, new java.sql.Time(fos.getCreationTime().toNumber()));
		stmt.setString(5, Ivy.session().getSessionUserName());
		stmt.setDate(6, new java.sql.Date(d.getTime()));
		stmt.setTime(7, new java.sql.Time(d.getTime()));
		stmt.setInt(8, 0);
		stmt.setString(9, PathUtil.returnStringFromList(fos.getCmrd()));
		stmt.setString(10, PathUtil.returnStringFromList(fos.getCod()));
		stmt.setString(11, PathUtil.returnStringFromList(fos.getCud()));
		stmt.setString(12, PathUtil.returnStringFromList(fos.getCdd()));
		stmt.setString(13, PathUtil.returnStringFromList(fos.getCwf()));
		stmt.setString(14, PathUtil.returnStringFromList(fos.getCdf()));
		if(dbmdAnalyzer.getSecurityVersion()>1){
			stmt.setString(15, PathUtil.returnStringFromList(fos.getCcd()));
			stmt.setString(16, PathUtil.returnStringFromList(fos.getCrd()));
			stmt.setString(17, PathUtil.returnStringFromList(fos.getCtd()));
			stmt.setString(18, PathUtil.returnStringFromList(fos.getCcf()));
			stmt.setString(19, PathUtil.returnStringFromList(fos.getCuf()));
			stmt.setString(20,fos.getPath());
		}else{
			stmt.setString(15,fos.getPath());
		}
		return stmt;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#get(java.lang.String)
	 */
	/**
	 * Returns the FolderOnServer object that is stored in the given file path.<br> 
	 * If no FolderOnServer can be found at this path, returns null.<br>
	 * This object contains only the meta informations about the FolderOnServer.
	 * @param directoryPath the String path of the document to return. This path should be a real path. It should not contain signs like %. 
	 * @return the FolderOnServer object found at this path.
	 */
	@Override
	public FolderOnServer get(String directoryPath) throws Exception {
		assert (directoryPath!=null && directoryPath.trim().length()>0):"The given directoryPath is invalid.";

		String query =FolderOnServerSQLQueries.SELECT_DIRECTORY_BY_PATH
				.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.directoriesTableNameSpace);

		PreparedStatement stmt=null;
		FolderOnServer fos = null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, PathUtil.formatPathForDirectoryWithoutLastSeparator(directoryPath));
			rst = stmt.executeQuery();
			Ivy.log().debug("FolderOnServer get {0} {1}",query,directoryPath);
			List<Record> records = SqlPersistenceHelper.getRecordsListFromResulSet(rst);
			if(!records.isEmpty()) {				
				fos = this.makeFolderOnServerFromRecord(records.get(0),false);
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "get(String directoryPath)", this.getClass());
		}
		if(fos!=null && this.dirI18nPersistence!=null) {
			setFolderTranslation(fos);
			Ivy.log().debug("Display Name update process {0}", fos.getDisplayName());
		}
		return fos;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#get(long)
	 */
	@Override
	public FolderOnServer get(long id) throws Exception {
		assert id>0:"Illegal argument: the id parameter is invalid in get(long id).";
		String query =FolderOnServerSQLQueries.SELECT_DIRECTORY_BY_ID
				.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.directoriesTableNameSpace)
				.replace(DocumentOnServerSQLQueries.ESCAPECHAR_PLACEHOLDER, dbmdAnalyzer.getEscapeChar());
		
		PreparedStatement stmt=null;
		FolderOnServer fos = null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, id);
			rst = stmt.executeQuery();
			Ivy.log().debug("FolderOnServer get {0} {1}",query,id);
			List<Record> records = SqlPersistenceHelper.getRecordsListFromResulSet(rst);
			if(!records.isEmpty()) {				
				fos = this.makeFolderOnServerFromRecord(records.get(0),false);
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "get(long id)", this.getClass());
		}
		if(fos!=null && this.dirI18nPersistence!=null) {
			setFolderTranslation(fos);
			Ivy.log().debug("Display Name update process {0}", fos.getDisplayName());
		}
		return fos;

	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#getList(java.lang.String, boolean)
	 */
	@Override
	public List<FolderOnServer> getList(String rootPath, boolean recursive)
			throws Exception {
		assert (rootPath!=null && rootPath.trim().length()>0):"The given directoryPath is invalid.";

		rootPath = PathUtil.escapeUnderscoreInPath(PathUtil.formatPathForDirectoryWithoutLastSeparator(rootPath));
		ArrayList<FolderOnServer> lFos = new ArrayList<FolderOnServer>();
		String query =(recursive?
				FolderOnServerSQLQueries.SELECT_FOLDER_UNDER_PATH_RECURSIVE_Q
				.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.directoriesTableNameSpace)
				.replace(DocumentOnServerSQLQueries.ESCAPECHAR_PLACEHOLDER, dbmdAnalyzer.getEscapeChar()) :
					FolderOnServerSQLQueries.SELECT_FOLDER_UNDER_PATH_NOT_RECURSIVE_Q
					.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.directoriesTableNameSpace)
					.replace(DocumentOnServerSQLQueries.ESCAPECHAR_PLACEHOLDER, dbmdAnalyzer.getEscapeChar())
				);
		PreparedStatement stmt=null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, rootPath+"/%");
			Ivy.log().debug(query+" "+rootPath);
			if(!recursive) {
				stmt.setString(2, rootPath+"/%/%");
			}
			rst = stmt.executeQuery();
			List<Record> records = SqlPersistenceHelper.getRecordsListFromResulSet(rst);
			Ivy.log().debug("Found folders "+records.size());
			if(!records.isEmpty()) {
				for(Record rec : records) {
					FolderOnServer fos = this.makeFolderOnServerFromRecord(rec,false);
					lFos.add(fos);
				}
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getList", this.getClass());
		}
		if(this.dirI18nPersistence!=null) {
			setFoldersTranslation(lFos);
		}
		return lFos;
	}
	
	/**
	 * For private use only, builds a FolderOnServer object with a record coming from the directories table
	 * @param rec record coming from the directories table
	 * @param isRoot if true, this folderOnserver
	 * @return
	 */
	private FolderOnServer makeFolderOnServerFromRecord(Record rec, boolean isRoot) {
		FolderOnServer fos = new FolderOnServer();
		fos.setId(Integer.parseInt(rec.getField("id").toString()));
		fos.setName(rec.getField("dir_name").toString());
		fos.setPath(PathUtil.formatPathForDirectoryWithoutLastSeparator(rec.getField("dir_path").toString()));
		fos.setCreationUser(rec.getField("creation_user_id").toString());
		fos.setCreationDate(new Date(rec.getField("creation_date").toString().length()>10?
				rec.getField("creation_date").toString().substring(0, 10):rec.getField("creation_date").toString()));
		if(dbmdAnalyzer.isMsSql()) {
			DateTime dt = new DateTime(rec.getField("creation_time").toString().length()>19?
					rec.getField("creation_time").toString().substring(0, 19):rec.getField("creation_time").toString());
			fos.setCreationTime(dt.getTime());
		} else if(dbmdAnalyzer.isOracle()) {
			DateTime dt = new DateTime(rec.getField("creation_time").toString());
			fos.setCreationTime(dt.getTime());
		}else{
			fos.setCreationTime(new Time(rec.getField("creation_time").toString().length()>8?
					rec.getField("creation_time").toString().substring(0, 8):rec.getField("creation_time").toString()));
		}
		if(this.configuration.isActivateSecurity()) {
			fos.setIs_protected(rec.getField("is_protected").toString().equals("1"));
			fos.setCmrd(PathUtil.getListFromString(rec.getField(SecurityConstants.CAN_MANAGE_DIRECTORY_RIGHTS_COLUMN_NAME).toString(),","));
			fos.setCod(PathUtil.getListFromString(rec.getField(SecurityConstants.CAN_OPEN_DIRECTORY_COLUMN_NAME).toString(),","));
			fos.setCud(PathUtil.getListFromString(rec.getField(SecurityConstants.CAN_UPDATE_DIRECTORY_COLUMN_NAME).toString(),","));
			fos.setCdd(PathUtil.getListFromString(rec.getField(SecurityConstants.CAN_DELETE_DIRECTORY_COLUMN_NAME).toString(),","));
			fos.setCwf(PathUtil.getListFromString(rec.getField(SecurityConstants.CAN_WRITE_FILE_COLUMN_NAME).toString(),","));
			fos.setCdf(PathUtil.getListFromString(rec.getField(SecurityConstants.CAN_DELETE_FILE_COLUMN_NAME).toString(),","));
			if(dbmdAnalyzer.getSecurityVersion()>1) {
				fos.setCcd(PathUtil.getListFromString(rec.getField(SecurityConstants.CAN_CREATE_DIRECTORY_COLUMN_NAME).toString(),","));
				fos.setCrd(PathUtil.getListFromString(rec.getField(SecurityConstants.CAN_RENAME_DIRECTORY_COLUMN_NAME).toString(),","));
				fos.setCtd(PathUtil.getListFromString(rec.getField(SecurityConstants.CAN_TRANSLATE_DIRECTORY_COLUMN_NAME).toString(),","));
				fos.setCcf(PathUtil.getListFromString(rec.getField(SecurityConstants.CAN_CREATE_FILE_COLUMN_NAME).toString(),","));
				fos.setCuf(PathUtil.getListFromString(rec.getField(SecurityConstants.CAN_UPDATE_FILE_COLUMN_NAME).toString(),","));
			}else {
				fos.setCcd(fos.getCud());
				fos.setCrd(fos.getCud());
				fos.setCtd(fos.getCmrd());
				fos.setCcf(fos.getCwf());
				fos.setCuf(fos.getCwf());
			}
		}else {
			fos.setCanUserOpenDir(true);
			fos.setCanUserUpdateDir(true);
			fos.setCanUserCreateDirectory(true);
			fos.setCanUserRenameDirectory(!isRoot);
			fos.setCanUserTranslateDirectory(!isRoot);
			fos.setCanUserDeleteDir(!isRoot);
			fos.setCanUserWriteFiles(true);
			fos.setCanUserCreateFiles(true);
			fos.setCanUserUpdateFiles(true);
			fos.setCanUserDeleteFiles(true);
			fos.setCanUserManageRights(false);
		}
		fos.setIsRoot(isRoot);
		return fos;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.IItemPersistence#delete(java.lang.Object)
	 */
	@Override
	public boolean delete(FolderOnServer fos) throws Exception {
		assert(fos!=null && fos.getPath()!=null):"Invalid FolderOnServer argument in delete method. The FolderOnServer or its path is null.";
		String path = PathUtil.formatPathForDirectoryWithoutLastSeparator(fos.getPath());
		assert (path.length()>0):"Invalid folderOnServer path in delete method.";
		String query =FolderOnServerSQLQueries.DELETE_DIRECTORY_BY_PATH
				.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.directoriesTableNameSpace);
		boolean flag = false;

		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, path);
			flag = stmt.executeUpdate()==1;
			
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "delete", this.getClass());
		}
		if(this.dirI18nPersistence!=null) {
			this.dirI18nPersistence.delete(fos.getTranslation());
		}
		return flag;
	}
	
	private void setFolderTranslation(FolderOnServer fos) throws Exception {
		String lang = Ivy.session().getContentLocale().getLanguage().toUpperCase();
		ItemTranslation it = this.dirI18nPersistence.get(fos.getId());
		if(it!=null) {
			fos.setTranslation(it);
			if(fos.getTranslation().getTranslations().containsKey(lang) && fos.getTranslation().getTranslations().get(lang)!=null ) {
				fos.setDisplayName(fos.getTranslation().getTranslations().get(lang).trim());
			} else {
				fos.setDisplayName(fos.getName());
			}
		} else{
			fos.setDisplayName(fos.getName());
		}
	}
	
	private void setFoldersTranslation(ArrayList<FolderOnServer> al)
			throws Exception {
		String lang = Ivy.session().getContentLocale().getLanguage().toUpperCase();
		for(FolderOnServer f : al) {
			ItemTranslation it = this.dirI18nPersistence.get(f.getId());
			if(it!=null) {
				f.setTranslation(it);
				if(f.getTranslation().getTranslations().containsKey(lang) && f.getTranslation().getTranslations().get(lang)!=null) {
					Ivy.log().debug("folder get translation: {0} {1}", new Object[] {f.getName(),f.getTranslation().getTranslations().get(lang)});
					f.setDisplayName(f.getTranslation().getTranslations().get(lang).trim());
				} else {
					f.setDisplayName(f.getName());
				}
			}else{
				f.setDisplayName(f.getName());
			}
		}
	}

}
