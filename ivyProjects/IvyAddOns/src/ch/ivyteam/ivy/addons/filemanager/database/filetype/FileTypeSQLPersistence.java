/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.filetype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileType;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileTypePersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IItemTranslationPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.TranslatedFileManagerItemsEnum;
import ch.ivyteam.ivy.addons.filemanager.database.sql.FileTypesSQLQueries;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.persistence.PersistencyException;
import ch.ivyteam.ivy.scripting.objects.List;

/**
 * @author ec
 *
 */
public class FileTypeSQLPersistence implements IFileTypePersistence {

	private IPersistenceConnectionManager<Connection> connectionManager =null;
	private BasicConfigurationController configuration;
	private String tableNameSpace;
	private String filesTableNameSpace;
	private IItemTranslationPersistence ftI18nPersistence;
	
	@SuppressWarnings("unchecked")
	public FileTypeSQLPersistence(BasicConfigurationController config) {
		this.configuration=config;
		this.connectionManager =  ((IPersistenceConnectionManager<Connection>) PersistenceConnectionManagerFactory
				.getPersistenceConnectionManagerInstance(config));
		this.initialize();
	}
	
	private void initialize() {
		if(this.configuration.getDatabaseSchemaName()!=null  && this.configuration.getDatabaseSchemaName().length()>0) {
			this.tableNameSpace = this.configuration.getDatabaseSchemaName()+"."+this.configuration.getFileTypeTableName();
			this.filesTableNameSpace = this.configuration.getDatabaseSchemaName()+"."+this.configuration.getFilesTableName();
		}else {
			this.tableNameSpace = this.configuration.getFileTypeTableName();
			this.filesTableNameSpace = this.configuration.getFilesTableName();
		}
		if(this.configuration.isActivateFileTypeTranslation()) {
			try {
				this.ftI18nPersistence = PersistenceConnectionManagerFactory.getIItemTranslationPersistenceInstance(this.configuration, TranslatedFileManagerItemsEnum.FILETYPE);
			} catch (Exception e) {
				Ivy.log().error(e.getMessage(),e);
			}
		}
	}
	
	@Override
	public FileType create(FileType fileType) throws Exception {
		assert(fileType != null && fileType.getFileTypeName()!=null && fileType.getFileTypeName().trim().length()>0):"IllegalArgumentException in create FileTypeSQLPersistence.";
		if(fileType.getApplicationName()==null) {
			fileType.setApplicationName("");
		}
		String query = FileTypesSQLQueries.INSERT_FILETYPE.replace(FileTypesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, fileType.getFileTypeName());
			stmt.setString(2, fileType.getApplicationName());
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
		FileType ft = this.get(fileType.getFileTypeName()+"*SEPARATE*"+fileType.getApplicationName());;
		if(this.ftI18nPersistence!=null) {
			this.setFileTypeTranslation(ft);
		}
		return ft;
	}

	@Override
	public FileType update(FileType fileType) throws Exception {
		assert(fileType != null && fileType.getId()>0 && fileType.getFileTypeName()!=null 
				&& fileType.getFileTypeName().trim().length()>0):"IllegalArgumentException in update FileTypeSQLPersistence.";
		if(fileType.getApplicationName()==null) {
			fileType.setApplicationName("");
		}
		String query = FileTypesSQLQueries.UPDATE_FILETYPE.replace(FileTypesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, fileType.getFileTypeName());
			stmt.setString(2, fileType.getApplicationName());
			stmt.setLong(3, fileType.getId());
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
		if(this.ftI18nPersistence!=null) {
			this.ftI18nPersistence.update(fileType.getTranslation());
		}
		return this.get(fileType.getId());
	}

	@Override
	public FileType get(String uniqueDescriptor) throws Exception {
		assert(uniqueDescriptor!=null && uniqueDescriptor.trim().length()>0):"IllegalArgumentException in get(String uniqueDescriptor) FileTypeSQLPersistence.";
		String[] params = uniqueDescriptor.split("\\*SEPARATE\\*");
		String name;
		String appname;
		String query = FileTypesSQLQueries.SELECT_FILETYPE_BY_NAME_AND_BY_APPNAME.replace(FileTypesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		FileType ft = new FileType();
		if(params.length==1) {
			name = params[0].trim();
			appname="";
		} else if(params.length>1) {
			name = params[0].trim();
			appname=params[1].trim();
		} else {
			return ft;
		}
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, name);
			stmt.setString(2, appname);
			ResultSet rst = stmt.executeQuery();
			if(rst.next()) {
				ft.setId(rst.getLong("id"));
				ft.setApplicationName(rst.getString("appname"));
				ft.setFileTypeName(rst.getString("name"));
				ft.setDisplayName(rst.getString("name"));
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
		if(ft.getId()>0 && this.ftI18nPersistence!=null) {
			this.setFileTypeTranslation(ft);
		}
		return ft;
	}

	@Override
	public FileType get(long id) throws Exception {
		assert(id>0):"IllegalArgumentException in get(long id) FileTypeSQLPersistence.";
		String query = FileTypesSQLQueries.SELECT_FILETYPE_BY_ID.replace(FileTypesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		FileType ft = new FileType();
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, id);
			ResultSet rst = stmt.executeQuery();
			if(rst.next()) {
				ft.setId(id);
				ft.setApplicationName(rst.getString("appname"));
				ft.setFileTypeName(rst.getString("name"));
				ft.setDisplayName(rst.getString("name"));
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
		if(ft.getId()>0 && this.ftI18nPersistence!=null) {
			this.setFileTypeTranslation(ft);
		}
		return ft;
	}

	@Override
	public boolean delete(FileType fileType) throws Exception {
		assert(fileType != null && fileType.getId()>0):"IllegalArgumentException in delete FileTypeSQLPersistence.";
		String query = FileTypesSQLQueries.DELETE_FILETYPE_BY_ID.replace(FileTypesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		boolean flag = false;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileType.getId());
			flag = stmt.executeUpdate()>0;
			query = FileTypesSQLQueries.REMOVE_FILETYPE_FROM_DOCUMENTS.replace(FileTypesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.filesTableNameSpace);
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileType.getId());
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
		if(this.ftI18nPersistence!=null) {
			this.ftI18nPersistence.delete(fileType.getTranslation());
		}
		return flag;
	}
	
	@Override
	public java.util.List<FileType> getFileTypesWithAppName(String applicationName) 
			throws PersistencyException, SQLException, Exception {
		List<FileType> ftl = List.create(FileType.class);

		String query = FileTypesSQLQueries.SELECT_FILETYPES_BY_APPNAME.replace(FileTypesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);

		applicationName = applicationName==null?"":applicationName.trim();
		PreparedStatement stmt = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, applicationName);
			ResultSet rst = stmt.executeQuery();
			while(rst.next()) {
				FileType ft = new FileType();
				ft.setId(rst.getLong("id"));
				ft.setFileTypeName(rst.getString("name"));
				ft.setDisplayName(rst.getString("name"));
				ft.setApplicationName(rst.getString("appname"));
				ftl.add(ft);
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
		if(this.ftI18nPersistence!=null) {
			this.setFileTypesTranslation(ftl);
		}
		return ftl;
	}

	@Override
	public java.util.List<FileType> getAllFileTypes() throws Exception {
		List<FileType> ftl = List.create(FileType.class);
		String query = FileTypesSQLQueries.SELECT_ALL_FILETYPES.replace(FileTypesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);

		PreparedStatement stmt = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			ResultSet rst = stmt.executeQuery();
			while(rst.next()) {
				FileType ft = new FileType();
				ft.setId(rst.getLong("id"));
				ft.setFileTypeName(rst.getString("name"));
				ft.setDisplayName(rst.getString("name"));
				ft.setApplicationName(rst.getString("appname")==null?"":rst.getString("appname"));
				ftl.add(ft);
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
		if(this.ftI18nPersistence!=null) {
			this.setFileTypesTranslation(ftl);
		}
		return ftl;
	}

	@Override
	public java.util.List<DocumentOnServer> getDocumentsWithFileTypeId(
			long typeId) throws Exception {
		List<DocumentOnServer> docs = List.create(DocumentOnServer.class);
		FileType ft;
		if(typeId <0) {
			typeId=0;
			ft = new FileType();
		} else{
			ft = this.get(typeId);
			if(ft.getId()>0 && this.ftI18nPersistence!=null) {
				this.setFileTypeTranslation(ft);
			}
		}
		String query = FileTypesSQLQueries.SELECT_DOCUMENTS_WITH_FILETYPEID.replace(FileTypesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.filesTableNameSpace);
		
		PreparedStatement stmt = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, typeId);
			ResultSet rst = stmt.executeQuery();
			while(rst.next()) {
				DocumentOnServer doc = new DocumentOnServer();
				doc.setFileID(String.valueOf(rst.getLong("FileId")));
				doc.setFilename(rst.getString("FileName"));
				doc.setPath(rst.getString("FilePath"));
				doc.setFileSize(rst.getString("FileSize"));
				doc.setUserID(rst.getString("CreationUserId"));
				doc.setCreationDate(rst.getString("CreationDate"));
				doc.setCreationTime(rst.getString("CreationTime"));
				doc.setModificationUserID(rst.getString("ModificationUserId"));
				doc.setModificationDate(rst.getString("ModificationDate"));
				doc.setModificationTime(rst.getString("ModificationTime"));
				doc.setLocked(rst.getString("Locked"));
				doc.setLockingUserID(rst.getString("LockingUserId"));
				doc.setDescription(rst.getString("Description"));
				doc.setFileType(ft);
				docs.add(doc);
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
		return docs;
	}

	@Override
	public DocumentOnServer setDocumentFileType(DocumentOnServer doc,
			long typeId) throws Exception {
		if(doc==null || doc.getFileID()==null || Long.parseLong(doc.getFileID())<=0){
			throw new IllegalArgumentException("The documentOnServer must not be null and its id must be greater than zero.");
		}
		String query = FileTypesSQLQueries.UPDATE_DOCUMENT_FILETYPE.replace(FileTypesSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.filesTableNameSpace);
		FileType ft;
		if(typeId <=0) {
			typeId=0;
			ft = new FileType();
		} else {
			ft = this.get(typeId);
			if(ft.getId()>0 && this.ftI18nPersistence!=null) {
				this.setFileTypeTranslation(ft);
			}
		}
		PreparedStatement stmt = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, typeId);
			stmt.setLong(2, Long.parseLong(doc.getFileID()));
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
		doc.setFileType(ft);
		return doc;
	}
	
	private void setFileTypeTranslation(FileType ft) throws Exception {
		String lang = Ivy.session().getContentLocale().getLanguage().toUpperCase();
		ft.setTranslation(this.ftI18nPersistence.get(ft.getId()));
		if(ft.getTranslation().getTranslations().containsKey(lang) && ft.getTranslation().getTranslations().get(lang).trim().length()>0) {
			ft.setDisplayName(ft.getTranslation().getTranslations().get(lang).trim());
		} else {
			ft.setDisplayName(ft.getFileTypeName());
		}
	}
	
	private void setFileTypesTranslation(List<FileType> ftl)
			throws Exception {
		String lang = Ivy.session().getContentLocale().getLanguage().toUpperCase();
		for(FileType f : ftl) {
			f.setTranslation(this.ftI18nPersistence.get(f.getId()));
			if(f.getTranslation().getTranslations().containsKey(lang) && f.getTranslation().getTranslations().get(lang).trim().length()>0) {
				f.setDisplayName(f.getTranslation().getTranslations().get(lang).trim());
			} else {
				f.setDisplayName(f.getFileTypeName());
			}
		}
	}

}
