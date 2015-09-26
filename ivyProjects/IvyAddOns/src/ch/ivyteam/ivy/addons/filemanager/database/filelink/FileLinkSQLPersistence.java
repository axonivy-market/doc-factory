package ch.ivyteam.ivy.addons.filemanager.database.filelink;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.beanutils.BeanUtils;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileManagementHandlersFactory;
import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.filetype.AbstractFileTypesController;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileLinkPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFolderOnServerPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.sql.DocumentOnServerSQLQueries;
import ch.ivyteam.ivy.addons.filemanager.database.sql.FileExtractor;
import ch.ivyteam.ivy.addons.filemanager.database.sql.FileLinkSQLQueries;
import ch.ivyteam.ivy.addons.filemanager.database.sql.FileVersioningSQLQueries;
import ch.ivyteam.ivy.addons.filemanager.database.sql.PersistenceConnectionManagerReleaser;
import ch.ivyteam.ivy.addons.filemanager.document.FieldValueUtil;
import ch.ivyteam.ivy.addons.filemanager.exception.FileManagementException;
import ch.ivyteam.ivy.addons.filemanager.restricted.database.sql.DocumentOnServerGeneratorHelper;
import ch.ivyteam.ivy.addons.filemanager.util.MethodArgumentsChecker;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.File;
import ch.ivyteam.ivy.scripting.objects.Time;

public class FileLinkSQLPersistence implements IFileLinkPersistence {

	private IPersistenceConnectionManager<Connection> connectionManager = null;
	private BasicConfigurationController configuration;
	private String tableNamespace, fileContentTableNamespace, fileTableNamespace, fileVersionTableNamespace, fileVersionContentTableNamespace;
	private AbstractFileTypesController fileTypeController;
	private IFolderOnServerPersistence dirPersistence;
	
	private DateFormat formatter = null;

	@SuppressWarnings("unchecked")
	public FileLinkSQLPersistence(BasicConfigurationController configuration) throws FileManagementException {
		this.configuration = configuration;
		this.connectionManager =  ((IPersistenceConnectionManager<Connection>) PersistenceConnectionManagerFactory
				.getPersistenceConnectionManagerInstance(configuration));

		try {
			this.initialize();
		} catch (Exception e) {
			throw new FileManagementException("An Exception occurred while initializing the FileLinkSQLPersistence", e);
		}
	}

	private void initialize() throws Exception {
		if(this.configuration.getDatabaseSchemaName()!=null  && this.configuration.getDatabaseSchemaName().length()>0) {
			this.tableNamespace = this.configuration.getDatabaseSchemaName()+"."+this.configuration.getFileLinkTableName();
			this.fileContentTableNamespace = this.configuration.getDatabaseSchemaName()+"."+this.configuration.getFilesContentTableName();
			this.fileTableNamespace = this.configuration.getDatabaseSchemaName()+"."+this.configuration.getFilesTableName();
			this.fileVersionContentTableNamespace = this.configuration.getDatabaseSchemaName()+"."+this.configuration.getFilesVersionContentTableName();
			this.fileVersionTableNamespace = this.configuration.getDatabaseSchemaName()+"."+this.configuration.getFilesVersionTableName();
		}else {
			this.tableNamespace = this.configuration.getFileLinkTableName();
			this.fileContentTableNamespace = this.configuration.getFilesContentTableName();
			this.fileTableNamespace = this.configuration.getFilesTableName();
			this.fileVersionContentTableNamespace = this.configuration.getFilesVersionContentTableName();
			this.fileVersionTableNamespace = this.configuration.getFilesVersionTableName();
		}
		if(this.configuration.isActivateFileType()) {
			this.fileTypeController = FileManagementHandlersFactory.getFileTypesControllerInstance(configuration);
		}
		dirPersistence = PersistenceConnectionManagerFactory.getIFolderOnServerPersistenceInstance(configuration);
		if(Ivy.session().getContentLocale().getLanguage().equalsIgnoreCase("en")) {
			formatter = new SimpleDateFormat("yyyy-MM-dd");
		} else {
			formatter = new SimpleDateFormat("dd.MM.yyyy");
		}
	}

	@Override
	public FileLink create(FileLink fileLink) throws Exception {
		FileLinkValidator.throwIllegalArgumentExceptionIfInvalid(fileLink, false);

		if(getRawFileLinkByDirectoryAndName(fileLink.getDirectoryId(), fileLink.getFileLinkName()) != null) {
			throw new FileManagementException(String.format("Cannot create the fileLink named %s in the direcotry with id %s, " +
					"because such a filelink already exists.", fileLink.getDirectoryId(), fileLink.getFileLinkName()));
		}

		long fileid = FieldValueUtil.getLongValueForStringId(fileLink.getFileID());
		fillDocumentOnServerPropertiesAndPath(fileLink, false);
		long contentId =  getFileContentId(fileid);
		//INSERT INTO TABLENAMESPACE_PLACEHOLDER (name, directory_id, file_id, content_id, version_number, version_content_id) VALUES (?,?,?,?,?,?)
		String query = FileLinkSQLQueries.INSERT_FILELINK.replace(FileLinkSQLQueries.TABLENAMESPACE_PLACEHOLDER, tableNamespace);

		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, fileLink.getFileLinkName());
			stmt.setLong(2, fileLink.getDirectoryId());
			stmt.setLong(3, fileid);
			stmt.setLong(4, contentId);
			if(fileLink.isReferenceVersion()) {
				stmt.setInt(5, fileLink.getLinkedVersionNumber());
				stmt.setLong(6, fileLink.getVersionId());
			} else {
				stmt.setInt(5, fileLink.getVersionnumber().intValue());
				stmt.setLong(6, 0);
			}
			
			stmt.executeUpdate();
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "create", this.getClass());
		}
		return getFileLinkByDirectoryAndName(fileLink.getDirectoryId(), fileLink.getFileLinkName());
	}

	@Override
	public FileLink update(FileLink fileLink) throws Exception {
		FileLinkValidator.throwIllegalArgumentExceptionIfInvalid(fileLink, true);
		//UPDATE TABLENAMESPACE_PLACEHOLDER SET name = ? , directory_id = ? , content_id = ? , version_content_id = ? , reference_version = ? WHERE id = ?
		String query = FileLinkSQLQueries.UPDATE_FILELINK.replace(FileLinkSQLQueries.TABLENAMESPACE_PLACEHOLDER, tableNamespace);
		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, fileLink.getFileLinkName());
			stmt.setLong(2, fileLink.getDirectoryId());
			stmt.setLong(3, fileLink.getContentId());
			stmt.setLong(4, fileLink.getVersionId());
			stmt.setLong(5, fileLink.getFileLinkId());
			stmt.executeUpdate();
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "create", this.getClass());
		}
		return fileLink;
	}
	
	@Override
	public int updateFileLinksVersionId(long fileId, int versionNumber)
			throws Exception {
		if(!this.configuration.isLinkToVersion()) {
			return 0;
		}
		Ivy.log().info("finding fileLink to update with File version for fileid {0} and versionNumber {1}", fileId, versionNumber);
		List<FileLink> fileLinks = new ArrayList<>();
		for(FileLink fl: getFileLinksForFile(fileId)) {
			if(fl.getLinkedVersionNumber() == versionNumber) {
				Ivy.log().info("found fileLink to update with File version {0}", fl.getFileLinkName());
				fileLinks.add(fl);
			}
		}
		if(fileLinks.isEmpty()) {
			return 0;
		}
		long versionId = getVersionIdWithFileIdAndVersionNumber(fileId, versionNumber);
		for(FileLink fl: fileLinks) {
			fl.setVersionId(versionId);
			this.update(fl);
		}
		return fileLinks.size();
	}

	private long getVersionIdWithFileIdAndVersionNumber(long fileId,
			int versionNumber) throws Exception {
		String query = FileVersioningSQLQueries.GET_FILEVERSION_BY_FILEID_AND_VERSION_NUMBER
				.replace(FileVersioningSQLQueries.FILEVERSION_TABLENAMESPACE_PLACEHOLDER, this.fileVersionTableNamespace);
		ResultSet rst = null;
		PreparedStatement stmt = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileId);
			stmt.setInt(2, versionNumber);
			rst = stmt.executeQuery();
			if(rst.next()) {
				Ivy.log().info("found the version id for fileid {0} and version_number {1}", fileId, versionNumber);
				return rst.getLong("versionid");
			}
			Ivy.log().info("DID NOT found the version id for fileid {0} and version_number {1}", fileId, versionNumber);
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getVersionIdWithFileIdAndVersionNumber", this.getClass());
		}
		
		return 0;
	}

	@Override
	public FileLink get(String path) throws Exception {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheStringArgumentIsNullOrEmpty("The path cannot be null or empty.", path);
		
		FolderOnServer fos = dirPersistence.get(PathUtil.getParentDirectoryPath(path));
		if(fos == null || fos.getId() < 1) {
			return null;
		}
		String name = new java.io.File(path).getName();
		for(FileLink fl: getListInDirectory(fos.getId(), false)) {
			if(name.equals(fl.getFileLinkName())) {
				return fl;
			}
		}
		return null;
	}
	
	@Override
	public boolean fileExist(String path, String name) throws Exception {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheStringArgumentIsNullOrEmpty("The path or name cannot be null or empty.", path, name);
		FolderOnServer fos = dirPersistence.get(path);
		if(fos == null || fos.getId() < 1) {
			throw new IllegalStateException(String.format("The directory with path %s cannot be found.", path));
		}
		if(getRawFileLinkByDirectoryAndName(fos.getId(), name) != null) {
			return true;
		}
		String query = FileLinkSQLQueries.GET_FILE_ID_WITH_PATH.replace(FileLinkSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileTableNamespace);
		ResultSet rst = null;
		PreparedStatement stmt = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, PathUtil.formatPathForDirectory(path).concat(name));
			rst = stmt.executeQuery();
			if(rst.next()) {
				return true;
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "get(long id)", this.getClass());
		}
		return false;
	}

	@Override
	public FileLink getFileLinkByDirectoryAndName(long directoryId,
			String fileLinkName) throws Exception {
		FileLink fl = getRawFileLinkByDirectoryAndName(directoryId, fileLinkName);
		if(fl != null) {
			fillDocumentOnServerPropertiesAndPath(fl, false);
		}
		return fl;
	}

	private FileLink getRawFileLinkByDirectoryAndName(long directoryId,
			String fileLinkName) throws Exception {
		String query = FileLinkSQLQueries.SELECT_FILELINK_BY_DIRECTORY_ID_AND_NAME.replace(FileLinkSQLQueries.TABLENAMESPACE_PLACEHOLDER, tableNamespace);

		FileLink fl = null;
		PreparedStatement stmt = null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, directoryId);
			stmt.setString(2, fileLinkName);
			rst = stmt.executeQuery();
			if(rst.next()) {
				fl = new FileLink();
				fillFileLink(fl, rst);
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "get(long id)", this.getClass());
		}
		return fl;
	}

	@Override
	public FileLink get(long id) throws Exception {
		FileLink fl = getRawFileLink(id);
		fillDocumentOnServerPropertiesAndPath(fl, false);
		return fl;
	}

	private FileLink getRawFileLink(long id) throws SQLException, Exception {
		String query = FileLinkSQLQueries.SELECT_FILELINK_BY_ID.replace(FileLinkSQLQueries.TABLENAMESPACE_PLACEHOLDER, tableNamespace);
		FileLink fl = new FileLink();
		PreparedStatement stmt=null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, id);
			rst = stmt.executeQuery();
			if(!rst.next()) {
				return null;
			}
			fillFileLink(fl, rst);
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "get(long id)", this.getClass());
		}
		return fl;
	}

	@Override
	public boolean delete(FileLink fileLink) throws Exception {
		FileLinkValidator.throwIllegalArgumentExceptionIfInvalid(fileLink, true);

		String query = FileLinkSQLQueries.DELETE_FILELINK.replace(FileLinkSQLQueries.TABLENAMESPACE_PLACEHOLDER, tableNamespace);
		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileLink.getFileLinkId());
			return stmt.executeUpdate() == 1;
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "delete(FileLink fileLink)", this.getClass());
		}
	}

	@Override
	public int deleteFileLinksForFileId(long fileId) throws Exception {
		checkLong(fileId, "deleteFileLinksForFileId");

		String query = FileLinkSQLQueries.DELETE_FILELINKS_BY_FILEID.replace(FileLinkSQLQueries.TABLENAMESPACE_PLACEHOLDER, tableNamespace);
		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileId);
			return stmt.executeUpdate();
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "deleteFileLinksForFileId(long fileId)", this.getClass());
		}
	}

	@Override
	public int deleteFileLinksForFileVersionId(long fileVersionId)
			throws Exception {
		checkLong(fileVersionId, "deleteFileLinksForFileId");

		String query = FileLinkSQLQueries.DELETE_FILELINKS_BY_FILEVERSION_ID.replace(FileLinkSQLQueries.TABLENAMESPACE_PLACEHOLDER, tableNamespace);
		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileVersionId);
			return stmt.executeUpdate();
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "deleteFileLinksForFileId(long fileId)", this.getClass());
		}
	}

	@Override
	public int deleteFileLinksInDirectory(long directoryId) throws Exception {
		checkLong(directoryId, "deleteFileLinksInDirectory");

		String query = FileLinkSQLQueries.DELETE_FILELINKS_BY_DIRECTORY.replace(FileLinkSQLQueries.TABLENAMESPACE_PLACEHOLDER, tableNamespace);
		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, directoryId);
			return stmt.executeUpdate();
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "deleteFileLinksInDirectory(long directoryId)", this.getClass());
		}
	}

	@Override
	public FileLink getFileLinkWithJavaFile(long id) throws Exception {
		checkLong(id, "getFileLinkWithJavaFile");

		FileLink fl = getRawFileLink(id);
		if(fl == null) {
			throw new IllegalStateException(String.format("The fileLink with id %s cannot be found.", id));
		}
		fillDocumentOnServerPropertiesAndPath(fl, true);
		

		return fl;
	}

	@Override
	public List<FileLink> getFileLinksForFile(long fileId) throws Exception {
		checkLong(fileId, "getFileLinksForFile");

		List<FileLink> fileLinks = new ArrayList<>();
		String query = FileLinkSQLQueries.SELECT_FILELINKS_BY_FILE_ID.replace(FileLinkSQLQueries.TABLENAMESPACE_PLACEHOLDER, tableNamespace);
		PreparedStatement stmt=null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileId);
			rst = stmt.executeQuery();
			while(rst.next()) {
				FileLink fl = new FileLink();
				fillFileLink(fl, rst);
				fileLinks.add(fl);
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getFileLinksForFile(long fileId)", this.getClass());
		}
		for(FileLink fl: fileLinks) {
			fillDocumentOnServerPropertiesAndPath(fl, false);
		}
		return fileLinks;
	}

	@Override
	public List<FileLink> getFileLinksForFileVersion(long fileVersionId) throws Exception {
		checkLong(fileVersionId, "getFileLinksForFileVersion");
		
		List<FileLink> fileLinks = new ArrayList<>();
		if(!this.configuration.isActivateFileVersioning()) {
			return fileLinks;
		}
		String query = FileLinkSQLQueries.SELECT_FILELINKS_BY_FILE_VERSION_ID.replace(FileLinkSQLQueries.TABLENAMESPACE_PLACEHOLDER, tableNamespace);
		PreparedStatement stmt=null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileVersionId);
			rst = stmt.executeQuery();
			while(rst.next()) {
				FileLink fl = new FileLink();
				fillFileLink(fl, rst);
				fileLinks.add(fl);
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getFileLinksForFile(long fileId)", this.getClass());
		}
		for(FileLink fl: fileLinks) {
			fillDocumentOnServerPropertiesAndPath(fl, false);
		}
		return fileLinks;
	}

	@Override
	public List<FileLink> getListInDirectory(long directoryId, boolean recursive) throws Exception {
		List<FileLink> fileLinks = new ArrayList<>();
		List<FolderOnServer> fosList = new ArrayList<>();
		FolderOnServer folderOnServer = dirPersistence.get(directoryId);
		if(folderOnServer == null) {
			return fileLinks;
		}
		fosList.add(folderOnServer);
		if(recursive) {
			fosList.addAll(dirPersistence.getList(folderOnServer.getPath(), true));
		}
		String query = FileLinkSQLQueries.SELECT_FILELINKS_BY_DIRECTORY.replace(FileLinkSQLQueries.TABLENAMESPACE_PLACEHOLDER, tableNamespace);
		PreparedStatement stmt=null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			for(FolderOnServer fos : fosList) {
				stmt.setInt(1, fos.getId());
				rst = stmt.executeQuery();
				while(rst.next()) {
					FileLink fl = new FileLink();
					fillFileLink(fl, rst);
					fileLinks.add(fl);
				}
				rst.close();
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "deleteFileLinksInDirectory(long directoryId)", this.getClass());
		}
		for(FileLink fl: fileLinks) {
			fillDocumentOnServerPropertiesAndPath(fl, false);
		}
		return fileLinks;
	}

	private long fillFileLink(FileLink fl, ResultSet rst) throws SQLException {
		fl.setFileLinkId(rst.getLong("id"));
		long fileId = rst.getLong("file_id");
		fl.setFileID(String.valueOf(fileId));
		fl.setDirectoryId(rst.getLong("directory_id"));
		fl.setFileLinkName(rst.getString("name"));
		fl.setContentId(rst.getLong("content_id"));
		fl.setVersionId(rst.getLong("version_id"));
		fl.setVersionnumber(rst.getInt("version_number"));
		fl.setLinkedVersionNumber(rst.getInt("version_number"));
		fl.setLinkCreationDate(new Date(formatter.format(rst.getDate("creationdate"))));
		fl.setLinkCreationTime(new Time(DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.GERMAN).format(rst.getTime("creationtime"))));
		return fileId;
	}

	private void fillDocumentOnServerPropertiesAndPath(FileLink fl, boolean deserializeJavaFile) throws Exception, IllegalAccessException,
	InvocationTargetException {
		
		String query = DocumentOnServerSQLQueries.SELECT_WITH_ID_Q.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileTableNamespace);
		PreparedStatement stmt = null;
		DocumentOnServer doc = null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, Long.parseLong(fl.getFileID()));
			rst = stmt.executeQuery();

			if(rst.next()) {
				doc = DocumentOnServerGeneratorHelper.buildDocumentOnServerWithResulSetRow(rst, configuration);
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "fillDocumentOnServerPropertiesAndPath", this.getClass());
		}
		
		if(doc == null) {
			throw new IllegalStateException(String.format("The DocumentOnServer with id %s referenced by " +
					"the fileLink with id %s cannot be found." , fl.getFileID(), fl.getFileLinkId()));
		}
		if(this.fileTypeController!=null) {
			try {
				if(doc.getFileType()!=null && doc.getFileType().getId()!=null && doc.getFileType().getId()>0) {
					doc.setFileType(this.fileTypeController.getFileTypeWithId(doc.getFileType().getId()));
				}
			}catch(Exception ex) {
				//do nothing the file type is not mandatory
				Ivy.log().warn("WARNING while getting the file type on "+doc.getFilename()+ " "+ex.getMessage(), ex);
			}
		}
		
		BeanUtils.copyProperties(fl, doc);
		if(deserializeJavaFile) {
			getContentAsTempFile(fl);
		}
		makeFileLinkPath(fl);
	}

	@Override
	public File getContentAsTempFile(FileLink fl) throws Exception {
		if(fl.isReferenceVersion()) {
			Ivy.log().info("FileLink is a version. getting the version content as file");
			File f = getFileVersionWithJavaFile(fl);
			fl.setIvyFile(f);
			fl.setJavaFile(f.getJavaFile());
			return f;
		}
		Ivy.log().info("FileLink is not a version. getting the content as file");
		this.getFileContentAsTempFile(fl);
		return fl.getIvyFile();
	}

	private File getFileVersionWithJavaFile(FileLink fl) throws Exception {
		String query = FileLinkSQLQueries.SELECT_FILE_VERSION_CONTENT.replace(FileLinkSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileVersionContentTableNamespace );
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fl.getVersionId());
			rs = stmt.executeQuery();
			if(rs.next()) {
				return FileExtractor.extractInputStreamToTemporaryIvyFile(rs.getBinaryStream(1), fl.getFileLinkName());
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rs, "getFileContentId", this.getClass());
		}
		return null;
	}

	private void makeFileLinkPath(FileLink fl) throws Exception {
		FolderOnServer dir = dirPersistence.get(fl.getDirectoryId());
		if(dir == null) {
			throw new IllegalStateException(String.format("The directory with id %s that should contain " +
					"the fileLink with id %s cannot be found.", fl.getDirectoryId(), fl.getFileLinkId()));
		}
		fl.setFileLinkPath(PathUtil.formatPathForDirectory(dir.getPath()).concat(fl.getFileLinkName()));
	}
	
	private long getFileContentId(long fileId) throws Exception {
		String query = FileLinkSQLQueries.SELECT_FILE_CONTENT_ID.replace(FileLinkSQLQueries.TABLENAMESPACE_PLACEHOLDER, fileContentTableNamespace);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileId);
			rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rs, "getFileContentId", this.getClass());
		}
		return 0;
	}
	
	private void getFileContentAsTempFile(FileLink fl) throws Exception {
		String query = DocumentOnServerSQLQueries.SELECT_FILE_CONTENT_Q.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, fileContentTableNamespace);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, Long.parseLong(fl.getFileID()));
			rs = stmt.executeQuery();
			if(rs.next()) {
				File ivyFile = FileExtractor.extractInputStreamToTemporaryIvyFile(rs.getBinaryStream(1), fl.getFileLinkName());
				fl.setIvyFile(ivyFile);
				fl.setJavaFile(ivyFile.getJavaFile());
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rs, "getFileContentAsTempFile", this.getClass());
		}
	}

	private void checkLong(long id, String methodName) {
		if(id == 0) {
			throw new IllegalArgumentException(String.format("The given id must be greater than zero. Method %s in class %s", methodName, this.getClass()));
		}
	}

}
