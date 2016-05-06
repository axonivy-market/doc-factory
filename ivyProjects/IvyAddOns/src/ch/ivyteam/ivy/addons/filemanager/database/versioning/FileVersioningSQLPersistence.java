package ch.ivyteam.ivy.addons.filemanager.database.versioning;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.ivyteam.db.jdbc.DatabaseUtil;
import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileVersion;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IDocumentOnServerPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileVersionPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.sql.FileExtractor;
import ch.ivyteam.ivy.addons.filemanager.database.sql.FileVersioningSQLQueries;
import ch.ivyteam.ivy.addons.filemanager.database.sql.PersistenceConnectionManagerReleaser;
import ch.ivyteam.ivy.addons.filemanager.document.FilemanagerItemMetaData;
import ch.ivyteam.ivy.addons.filemanager.util.DateUtil;
import ch.ivyteam.ivy.addons.filemanager.util.MethodArgumentsChecker;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.File;
import ch.ivyteam.ivy.scripting.objects.Record;
import ch.ivyteam.ivy.scripting.objects.Time;

public class FileVersioningSQLPersistence implements IFileVersionPersistence {

	private IPersistenceConnectionManager<Connection> connectionManager = null;
	private BasicConfigurationController configuration;
	private String fileTableName;
	private String fileContentTableName;
	private String fileVersionTableName;
	private String fileVersionContentTableName;
	private String fileArchiveTrackingTableName;
	private IDocumentOnServerPersistence docPersistence;

	@SuppressWarnings("unchecked")
	public FileVersioningSQLPersistence(BasicConfigurationController config) throws Exception {
		this.configuration = config;
		this.connectionManager = ((IPersistenceConnectionManager<Connection>) PersistenceConnectionManagerFactory
				.getPersistenceConnectionManagerInstance(config));
		this.initialize();
	}
	
	private void initialize() throws Exception {
		this.docPersistence = PersistenceConnectionManagerFactory.getIDocumentOnServerPersistenceInstance(this.configuration);
		if(this.configuration.getDatabaseSchemaName()!=null  && this.configuration.getDatabaseSchemaName().length()>0) {
			this.fileTableName = this.configuration.getDatabaseSchemaName() + "." + this.configuration.getFilesTableName();
			this.fileContentTableName = this.configuration.getDatabaseSchemaName() + "." + this.configuration.getFilesContentTableName();
			this.fileVersionTableName = this.configuration.getDatabaseSchemaName() + "." + this.configuration.getFilesVersionTableName();
			this.fileArchiveTrackingTableName = this.configuration.getDatabaseSchemaName() + "." + this.configuration.getFilesVersionArchiveTrackingTableName();
			
		}else {
			this.fileTableName = this.configuration.getFilesTableName();
			this.fileContentTableName = this.configuration.getFilesContentTableName();
			this.fileVersionTableName = this.configuration.getFilesVersionTableName();
			this.fileVersionContentTableName = this.configuration.getFilesVersionContentTableName();
			this.fileArchiveTrackingTableName = this.configuration.getFilesVersionArchiveTrackingTableName();
		}
	}

	/**
	 * This method takes the file id contained in the given FileVersion to create a new version of the corresponding document.
	 */
	@Override
	public FileVersion create(FileVersion itemToCreate) throws Exception {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheArgumentIsNull("The FileVersion object to create cannot be null.", itemToCreate);
		
		if(itemToCreate.getFileid()<=0) {
			return null;
		}
		FilemanagerItemMetaData data = new FilemanagerItemMetaData();
		data.setCreationUserId(itemToCreate.getUser());
		data.setModificationUserId(itemToCreate.getUser());
		return this.createNewVersion(itemToCreate.getFileid(), data);
	}

	@Override
	public FileVersion update(FileVersion itemToSave) throws Exception {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheArgumentIsNull("The FileVersion object to update cannot be null.", itemToSave);
		if(itemToSave.getFileid()<=0) {
			throw new IllegalArgumentException("The FileVersion to update is not related to any active document. Its fileid is zero. This is vorbidden.");
		}
		if(itemToSave.getId()<=0) {
			// we have to create it
			return this.create(itemToSave);
		}
		String query = FileVersioningSQLQueries.UPDATE_FILEVERSION.replace(FileVersioningSQLQueries.FILEVERSION_TABLENAMESPACE_PLACEHOLDER, this.fileVersionTableName);
		
		PreparedStatement stmt=null;
		try {
			stmt= this.connectionManager.getConnection().prepareStatement(query);
			stmt.setInt(1, itemToSave.getVersionNumber());
			stmt.setString(2, itemToSave.getFilename());
			stmt.setString(3, itemToSave.getUser());
			stmt.setString(4, itemToSave.getDate()!=null? DateUtil.getIvyDateAsString(itemToSave.getDate()) : DateUtil.getNewDateAsString());
			stmt.setString(5, itemToSave.getTime()!=null? DateUtil.getIvyTimeAsString(itemToSave.getTime()): DateUtil.getNewTimeAsString());
			stmt.setLong(6, itemToSave.getId());
			stmt.executeUpdate();
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "update", this.getClass());
		}
		return this.get(itemToSave.getId());
	}

	/**
	 * uniqueDescriptor can be a Long as String, in this case it will be considered to be the fileVersion id from the file version to be returned,<br>
	 * or it can be a file path: in that case we return the last fileVersion from the corresponding DocumentOnServer
	 */
	@Override
	public FileVersion get(String uniqueDescriptor) throws Exception {
		long id ;
		try {
			id= Long.parseLong(uniqueDescriptor);
			return this.get(id);
		} catch(NumberFormatException ex){
			// not a number
		}
		//We suppose that the 
		DocumentOnServer doc = this.docPersistence.get(uniqueDescriptor);
		if(doc!=null) {
			List<FileVersion> l = this.getFileVersions(Long.parseLong(doc.getFileID()));
			if(!l.isEmpty()) {
				return l.get(0);
			}
		}
		
		return null;
	}

	@Override
	public FileVersion get(long id) throws Exception {
		FileVersion fv = null;
		if(id<=0) {
			return fv;
		}
		String query = FileVersioningSQLQueries.GET_FILEVERSION_BY_ID.replace(FileVersioningSQLQueries.FILEVERSION_TABLENAMESPACE_PLACEHOLDER, this.fileVersionTableName);
		PreparedStatement stmt=null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, id);
			rst = stmt.executeQuery();
			fv = makeFileVersionWithResultSet(rst,false,null);
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "get(long id)", this.getClass());
		}
		return fv;
	}

	@Override
	public boolean delete(FileVersion itemToDelete) throws Exception {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheArgumentIsNull("The fileVersion to delete cannot be null.", itemToDelete);
		if(itemToDelete.getId()<=0) {
			return false;
		}
		String q1 = FileVersioningSQLQueries.DELETE_FILEVERSION_BY_ID.replace(FileVersioningSQLQueries.FILEVERSION_TABLENAMESPACE_PLACEHOLDER, this.fileVersionTableName);
		String q2 = FileVersioningSQLQueries.DELETE_FILE_VERSION_CONTENT_BY_FILECONTENT_ID.replace(FileVersioningSQLQueries.FILEVERSION_CONTENT_TABLENAMESPACE_PLACEHOLDER, this.fileVersionContentTableName);
		PreparedStatement stmt = null;
		try{
			stmt = this.connectionManager.getConnection().prepareStatement(q1);
			stmt.setLong(1, itemToDelete.getId());
			if(stmt.executeUpdate()==1) {
				try {
					stmt.close();
				} catch( SQLException ex) {
					Ivy.log().error("PreparedStatement cannot be closed in create method, FileTagSQLPersistence.",ex);
				}
				stmt = this.connectionManager.getConnection().prepareStatement(q2);
				stmt.setLong(1, itemToDelete.getVersionContentId());
				return stmt.executeUpdate()==1;
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "delete", this.getClass());
		}
		return false;
	}

	@Override
	public List<FileVersion> getFileVersions(long fileId) throws Exception {
		return this.getFileVersionsWithJavaFileOption(fileId, null);
	}
	
	/**
	 * returns a list of file version related to a file denoted to a fileid.
	 * If the given path is not null, all the file versions content are written as java files to this path.
	 * @param fileId
	 * @param path
	 * @return
	 * @throws Exception
	 */
	private List<FileVersion> getFileVersionsWithJavaFileOption(long fileId, String path) throws Exception {
		List<FileVersion> fversions = new ArrayList<FileVersion>();
		if(fileId<=0) {
			return fversions;
		}
		String query = FileVersioningSQLQueries.GET_FILEVERSIONS_BY_FILEID.replace(FileVersioningSQLQueries.FILEVERSION_TABLENAMESPACE_PLACEHOLDER, this.fileVersionTableName);
		PreparedStatement stmt=null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileId);
			rst = stmt.executeQuery();
			while(rst.next()) {
				fversions.add(makeFileVersionWithResultSet(rst,false,path));
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getFileVersionsWithJavaFileOption", this.getClass());
		}
		return fversions;
	}

	@Override
	public int getNextVersionNumberForFile(long fileId) throws Exception {
		if (fileId <= 0) {
			return -1;
		}
		int nvn = 1;
		String query = FileVersioningSQLQueries.GET_NEXT_FILEVERSIONNUMBER_BY_FILEID.replace(FileVersioningSQLQueries.FILEVERSION_TABLENAMESPACE_PLACEHOLDER, this.fileVersionTableName);
		PreparedStatement stmt=null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileId);
			rst = stmt.executeQuery();
			while(rst.next()) {
				nvn++;
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getNextVersionNumberForFile", this.getClass());
		}
		return nvn;
	}

	@Override
	public FileVersion createNewVersion(long fileId, FilemanagerItemMetaData filemanagerItemMetaData) throws Exception {
		FileVersion fv = new FileVersion();
		fv.setFileid(fileId);
		// the new id of the version content
		long vcid = 0;
		// the new id of the version
		long vid = 0;
		
		DocumentOnServer doc = this.getParentFileWithoutContent(fileId);
		int vn = doc.getVersionnumber().intValue();
		if(filemanagerItemMetaData==null || filemanagerItemMetaData.getCreationDate()==null) {
			fv.setDate(new Date());
		} else {
			fv.setDate(filemanagerItemMetaData.getCreationDate());
		}
		String q1 = FileVersioningSQLQueries.INSERT_FILEVERSION.replace(FileVersioningSQLQueries.FILEVERSION_TABLENAMESPACE_PLACEHOLDER, this.fileVersionTableName);
		// Query to insert the new Version Content
		String q2 = FileVersioningSQLQueries.INSERT_FILEVERSION_CONTENT.replace(FileVersioningSQLQueries.FILECONTENT_TABLENAMESPACE_PLACEHOLDER, this.fileContentTableName)
				.replace(FileVersioningSQLQueries.FILEVERSION_CONTENT_TABLENAMESPACE_PLACEHOLDER, this.fileVersionContentTableName);
		String q2bis =FileVersioningSQLQueries.INSERT_FILEVERSION_CONTENT_MSSQL2005.replace(FileVersioningSQLQueries.FILECONTENT_TABLENAMESPACE_PLACEHOLDER, this.fileContentTableName)
				.replace(FileVersioningSQLQueries.FILEVERSION_CONTENT_TABLENAMESPACE_PLACEHOLDER, this.fileVersionContentTableName);

		// Query to update the new file version "file version content Id" field
		String q3 = FileVersioningSQLQueries.UPDATE_FILEVERSIONCONTENT_ID_BY_FILECONTENTID.replace(FileVersioningSQLQueries.FILEVERSION_TABLENAMESPACE_PLACEHOLDER, this.fileVersionTableName);
		// Query to update the number version in the main file table
		String q4 = FileVersioningSQLQueries.UPDATE_FILE_INFORMATION.replace(FileVersioningSQLQueries.FILE_TABLENAMESPACE_PLACEHOLDER, this.fileTableName);
		PreparedStatement stmt=null;
		ResultSet rs = null;
		try {
			boolean flag = true;
			try {
				stmt = this.connectionManager.getConnection().prepareStatement(q1,PreparedStatement.RETURN_GENERATED_KEYS);
			} catch (SQLFeatureNotSupportedException fex) {
				stmt = this.connectionManager.getConnection().prepareStatement(q1);
				flag = false;
			}
			
			stmt.setLong(1, fileId);
			stmt.setInt(2, vn);
			stmt.setDate(3, DateUtil.getSqlDateFromDateAsString(doc.getCreationDate()));
			stmt.setTime(4, DateUtil.getSqlTimeFromTimeAsString(doc.getCreationTime()));
			stmt.setString(5, doc.getUserID());
			stmt.setString(6, doc.getFilename());
			stmt.executeUpdate();
			try {
				rs = stmt.getGeneratedKeys();
				if (rs != null && rs.next()) {
					// Retrieve the auto generated key.
					vid = rs.getInt(1);
				}
			} catch (Exception fex) {
				// The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS... ignore
			} finally {
				if(rs!=null) {
					DatabaseUtil.close(rs);
				}
			}
			closeStatement(stmt);
			if (!flag || vid <= 0) {
				// The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS we have to get the inserted Id manually....
				vid = this.getFileVersionWithParentFileIdAndVersionNumber(fileId, vn).getId();
			}
			Ivy.log().debug("File Version id created "+vid);
			if(this.configuration.isFileArchiveProtectionEnabled() && !this.wasFileVersionArchived(fileId, vn)) {
				trackArchive(fileId, vn, stmt);
				Ivy.log().debug("File Version tracked");
			}
			// insert the new version content
			flag = true;
			try {
				stmt = this.connectionManager.getConnection().prepareStatement(q2,PreparedStatement.RETURN_GENERATED_KEYS);
			} catch (SQLFeatureNotSupportedException fex) {
				
				flag = false;
			}
			if(!flag){
				stmt = this.connectionManager.getConnection().prepareStatement(q2);
			}
			stmt.setLong(1, fileId);
			stmt.setLong(2, vid);
			boolean error = false;
			try {
				stmt.executeUpdate();
			} catch (SQLException ex) {
				// Older Version from MS SQL , like  MS SQL 2005 may throw an  Exception because of the nested  query.
				error = true;
			}
			if (error) {
				flag = true;
				closeStatement(stmt);
				try {
					stmt = this.connectionManager.getConnection().prepareStatement(q2bis,PreparedStatement.RETURN_GENERATED_KEYS);
				} catch (SQLFeatureNotSupportedException fex) {
					flag = false;
				}
				if(!flag) {
					stmt = this.connectionManager.getConnection().prepareStatement(q2bis);
				}
				stmt.setLong(1, vid);
				stmt.setLong(2, fileId);
				stmt.executeUpdate();
			}
			Ivy.log().debug("File Version created "+vid);
			rs = null;
			try {
				rs = stmt.getGeneratedKeys();
				if (rs != null && rs.next()) {
					// Retrieve the auto generated key.
					vcid = rs.getInt(1);
				}
			} catch (Exception fex) {
				// The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS ignore
			}

			if (!flag || vcid <= 0) {
				// The JDBC Driver doesn't accept the  PreparedStatement.RETURN_GENERATED_KEYS  we have to get the inserted Id manually....
				vcid = this.getLastInsertedFileVersionContent(vid);
			}
			closeStatement(stmt);
			// update the new file version "file version content Id" field
			stmt = this.connectionManager.getConnection().prepareStatement(q3);
			stmt.setLong(1, vcid);
			stmt.setLong(2, vid);
			stmt.executeUpdate();
			closeStatement(stmt);
			// update the version number in the main file table
			stmt = this.connectionManager.getConnection().prepareStatement(q4);
			
			vn += 1;// increment the new version number of the main file
			Date d = new Date();
			Time t = new Time();
			Ivy.log().debug(q4);
			Ivy.log().debug("CREATING VERSION for "+fileId +" New version number is "+vn);
			stmt.setInt(1, vn);
			stmt.setString(2, (filemanagerItemMetaData==null || filemanagerItemMetaData.getCreationDate()==null)?
					DateUtil.getIvyDateAsString(d) : DateUtil.getIvyDateAsString(filemanagerItemMetaData.getCreationDate()));
			stmt.setString(3, (filemanagerItemMetaData==null || filemanagerItemMetaData.getCreationTime()==null)?
					DateUtil.getIvyTimeAsString(t) : DateUtil.getIvyTimeAsString(filemanagerItemMetaData.getCreationTime()));
			stmt.setString(4, (filemanagerItemMetaData==null || filemanagerItemMetaData.getCreationUserId().isEmpty())?
					Ivy.session().getSessionUserName() : filemanagerItemMetaData.getCreationUserId());
			stmt.setString(5, (filemanagerItemMetaData==null || filemanagerItemMetaData.getModificationDate()==null)?
					DateUtil.getIvyDateAsString(d) :  DateUtil.getIvyDateAsString(filemanagerItemMetaData.getModificationDate()));
			stmt.setString(6, (filemanagerItemMetaData==null || filemanagerItemMetaData.getModificationTime()==null)?
					DateUtil.getIvyTimeAsString(t) : DateUtil.getIvyTimeAsString(filemanagerItemMetaData.getModificationTime()));
			stmt.setString(7, filemanagerItemMetaData==null || filemanagerItemMetaData.getModificationUserId().isEmpty()?
					Ivy.session().getSessionUserName() : filemanagerItemMetaData.getModificationUserId());
			stmt.setLong(8, fileId);
			stmt.executeUpdate();
			fv = new FileVersion();
			fv.setDate((filemanagerItemMetaData==null || filemanagerItemMetaData.getCreationDate()==null)?
					d : filemanagerItemMetaData.getCreationDate());
			fv.setFileid(fileId);
			fv.setFilename(doc.getFilename());
			fv.setTime((filemanagerItemMetaData==null || filemanagerItemMetaData.getCreationTime()==null)?
					t : filemanagerItemMetaData.getCreationTime());
			fv.setUser((filemanagerItemMetaData==null || filemanagerItemMetaData.getCreationUserId().isEmpty())?
					Ivy.session().getSessionUserName():filemanagerItemMetaData.getCreationUserId());
			fv.setVersionNumber(vn);
			rs.close();
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rs, "createNewVersion", this.getClass());
		}
		return fv;
	}

	/**
	 * @param fileId
	 * @param vn
	 * @param stmt
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	private PreparedStatement trackArchive(long fileId, int vn,
			PreparedStatement stmt) throws SQLException, Exception {
		stmt = this.connectionManager.getConnection().prepareStatement("INSERT INTO "+this.fileArchiveTrackingTableName+" (fileid, versionnumber) VALUES (?,?)");
		stmt.setLong(1, fileId);
		stmt.setInt(2, vn);
		stmt.executeUpdate();
		closeStatement(stmt);
		return stmt;
	}

	private void closeStatement(PreparedStatement stmt) {
		try {
			if(stmt!=null) {
				stmt.close();
			}
		} catch( SQLException ex) {
			Ivy.log().error("PreparedStatement cannot be closed.",ex);
		}
	}

	@Override
	public DocumentOnServer getParentFileWithoutContent(long fileId)
			throws Exception {
		return this.docPersistence.get(fileId);
	}

	@Override
	public FileVersion getFileVersionWithParentFileIdAndVersionNumber(
			long fileId, int versionNumber) throws Exception {
		Iterator<FileVersion> iter = this.getFileVersions(fileId).iterator();
		while(iter.hasNext()) {
			FileVersion fv = iter.next();
			if(fv.getVersionNumber()==versionNumber) {
				return fv;
			}
		}
		return null;
	}

	@Override
	public List<FileVersion> extractVersionsToPath(long parentFileId, String _path) throws Exception {
		if (parentFileId <= 0 || _path == null || _path.trim().length() == 0) {
			throw new Exception(
					"Invalid paramaters in the method extractVersionsToPath in "
							+ this.getClass().getName());
		}
		java.io.File dir = new java.io.File(_path);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new Exception(
						"Exception method extractVersionsToPath in "
								+ this.getClass().getName()
								+ ". Unpossible to create directory for following path: "
								+ _path);
			}
		}
		if (!dir.isDirectory()) {
			throw new Exception("Exception method extractVersionsToPath in "
					+ this.getClass().getName() + ". The following path: "
					+ _path + " is not a valid diectory path.");
		}
		return this.getFileVersionsWithJavaFileOption(parentFileId, _path);
	}

	@Override
	public FileVersion getFileVersionWithJavaFile(long fileVersionId) throws Exception {
		FileVersion fv = null;
		if (fileVersionId <= 0) {
			return fv;
		}
		String query = "SELECT * FROM " + this.fileVersionTableName
				+ " INNER JOIN " + this.fileVersionContentTableName + " ON "
				+ this.fileVersionTableName + ".fvc_id = "
				+ this.fileVersionContentTableName + ".fvcid" + " WHERE "
				+ this.fileVersionTableName + ".versionid = ? ";
		ResultSet rst = null;
		PreparedStatement stmt = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileVersionId);
			rst = stmt.executeQuery();
			if (rst.next()) {
				fv = this.makeFileVersionWithResultSet(rst, true, null);
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getFileVersionWithJavaFile", this.getClass());
		}
		return fv;
	}

	@Override
	public void deleteAllVersionsFromFile(long fileId) throws Exception {
		if (fileId <= 0) {
			return;
		}
		List<FileVersion> fvs = this.getFileVersions(fileId);
		String q1 = "DELETE FROM " + this.fileVersionTableName + " WHERE versionid = ?";
		String q2 = "DELETE FROM " + this.fileVersionContentTableName + " WHERE version_id = ?";
		PreparedStatement stmt = null;
		try {
			for (FileVersion fv : fvs) {
				stmt = this.connectionManager.getConnection().prepareStatement(q1);
				stmt.setLong(1, fv.getId());
				stmt.executeUpdate();
				closeStatement(stmt);
				stmt = this.connectionManager.getConnection().prepareStatement(q2);
				stmt.setLong(1, fv.getId());
				stmt.executeUpdate();
				closeStatement(stmt);
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "getFileVersionWithJavaFile", this.getClass());
		} 
	}

	@Override
	public FileVersion rollbackLastVersionAsActiveDocument(long fileId)
			throws Exception {
		if (fileId <= 0) {
			return null;
		}
		// Query to get all the infos concerning the last version to rollback
		String q0 = "SELECT * FROM " + this.fileVersionTableName+" WHERE version_number = "
				+ "( SELECT MAX(version_number) FROM "+ this.fileVersionTableName + " WHERE file_id = ?) "
				+ "AND file_id=?";
		// Rollback the last version content into the file content
		String q1 = "UPDATE " + this.fileContentTableName
				+ " SET file_content=" + "(SELECT "+ this.fileVersionContentTableName + ".content FROM "
				+ this.fileVersionContentTableName + " WHERE "
				+ this.fileVersionContentTableName + ".fvcid = ?) WHERE file_id = ?";
		// update the meta Infos
		String q2 = "UPDATE "+ this.fileTableName+ " SET ModificationUserId = ?, ModificationDate=?, ModificationTime=?, "
				+ "versionnumber=?, CreationDate=?, CreationTime=?, CreationUserId=? WHERE FileId =?";
		// delete the last version content
		String q3 = "DELETE FROM " + this.fileVersionContentTableName
				+ " WHERE fvcid = ?";
		// delete the last version
		String q4 = "DELETE FROM " + this.fileVersionTableName
				+ " WHERE versionid = ?";
		PreparedStatement stmt = null;
		ResultSet rst = null;
		FileVersion fv = null;
		try {
			//get last version
			stmt = this.connectionManager.getConnection().prepareStatement(q0);
			stmt.setLong(1, fileId);
			stmt.setLong(2, fileId);
			rst = stmt.executeQuery();
			
			if (rst.next()) {
				fv = this.makeFileVersionWithResultSet(rst, false, null);
			}
			if(fv==null || fv.getId()==0) {
				return null;
			}
			closeStatement(stmt);
			//rollback the last version content
			stmt = this.connectionManager.getConnection().prepareStatement(q1);
			stmt.setLong(1, fv.getVersionContentId());
			stmt.setLong(2, fileId);
			stmt.executeUpdate();
			closeStatement(stmt);
			//update the file meta information
			stmt = this.connectionManager.getConnection().prepareStatement(q2);
			stmt.setString(1, Ivy.session().getSessionUserName());
			stmt.setString(2, DateUtil.getNewDateAsString());
			stmt.setString(3, DateUtil.getNewTimeAsString());
			stmt.setInt(4, fv.getVersionNumber());
			stmt.setString(5, DateUtil.getIvyDateAsString(fv.getDate()));
			stmt.setString(6, DateUtil.getIvyTimeAsString(fv.getTime()));
			stmt.setString(7, fv.getUser());
			stmt.setLong(8,fileId);
			stmt.executeUpdate();
			closeStatement(stmt);
			//Delete the last version content
			stmt = this.connectionManager.getConnection().prepareStatement(q3);
			stmt.setLong(1, fv.getVersionContentId());
			stmt.executeUpdate();
			closeStatement(stmt);
			//Delete the last version content
			stmt = this.connectionManager.getConnection().prepareStatement(q4);
			stmt.setLong(1, fv.getVersionContentId());
			stmt.executeUpdate();

		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "rollbackLastVersionAsActiveDocument", this.getClass());
		}
		this.docPersistence.update(this.docPersistence.getDocumentOnServerWithJavaFile(fileId));
		return fv;
	}
	
	@Override
	public boolean wasFileVersionArchived(long fileid, int versionnumber)
			throws Exception {
		if(fileid <= 0 || versionnumber <= 0) {
			throw new IllegalArgumentException("The fileid and the versionnumber parameters must not be zero or less than zero.");
		}
		String query = "SELECT * FROM " + this.fileArchiveTrackingTableName + " WHERE fileid=? AND versionnumber=?";
		PreparedStatement stmt = null;
		ResultSet rst = null;
		boolean result = false;
		try{
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, fileid);
			stmt.setInt(2, versionnumber);
			rst = stmt.executeQuery();
			result = rst.next();
		}finally{
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "wasFileVersionArchived", this.getClass());
		}
		return result;
	}
	
	private FileVersion makeFileVersionWithResultSet(ResultSet rst, boolean getFile, String path) throws Exception {
		FileVersion fv = null;
		fv = new FileVersion();
		fv.setId(rst.getLong("versionid"));
		fv.setFileid(rst.getLong("file_id"));
		fv.setVersionNumber(rst.getInt("version_number"));
		fv.setVersionContentId(rst.getLong("fvc_id"));
		fv.setDate(new Date(rst.getDate("cdate")));
		fv.setTime(DateUtil.getIvyTimeFromSqlTime(rst.getTime("ctime")));
		fv.setUser(rst.getString("cuser"));
		String filename =  rst.getString("file_name");
		fv.setFilename(filename);
		if (getFile) {
			File ivyFile = FileExtractor.extractInputStreamToTemporaryIvyFile(rst.getBinaryStream("content"), filename);
			fv.setIvyFile(ivyFile);
			fv.setJavaFile(ivyFile.getJavaFile());
		}
		return fv;
	}
	
	private long getLastInsertedFileVersionContent(long versionId)
			throws Exception {
		long id = 0;
		if (versionId <= 0) {
			return 0;
		}
		String query = "SELECT fvcid FROM " + this.fileVersionContentTableName + " WHERE version_id=?";

		PreparedStatement stmt = null;
		List<Record> recordList = new ArrayList<Record>();
		try{
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, versionId);
			recordList = executeStmt(stmt);
			if (recordList.size() > 0) {
				Record rec = recordList.get(0);
				id = Long.parseLong(rec.getField("fvcid").toString());
			}
		} finally {
			closeStatement(stmt);
		}

		return id;
	}
	
	private static List<Record> executeStmt(PreparedStatement _stmt) throws Exception {
		if(_stmt == null){
			throw(new SQLException("Invalid PreparedStatement","PreparedStatement Null"));
		}

		ResultSet rst = null;
		List<Record> recordList= new ArrayList<Record>();
		
		try{
			rst=_stmt.executeQuery();
			ResultSetMetaData rsmd = rst.getMetaData();
			int numCols = rsmd.getColumnCount();
			List<String> colNames= new ArrayList<String>();
			for(int i=1; i<=numCols; i++) {
				colNames.add(rsmd.getColumnName(i));
			}
			while(rst.next()) {
				List<Object> values = new ArrayList<Object>();
				for(int i=1; i<=numCols; i++){
					if(rst.getString(i)==null) {
						values.add(" ");
					} else { 
						values.add(rst.getString(i));
					}
				}
				Record rec = new Record(colNames,values);
				recordList.add(rec);
			}
			rst.close();
		}catch(Exception ex){
			Ivy.log().error(ex.getMessage(), ex);
		}finally {
			DatabaseUtil.close(rst);
		}
		return recordList;
	}
}
