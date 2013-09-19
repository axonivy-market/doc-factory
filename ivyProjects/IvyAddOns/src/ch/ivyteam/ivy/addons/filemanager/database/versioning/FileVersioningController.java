/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.versioning;

import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import ch.ivyteam.db.jdbc.DatabaseUtil;
import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.FileVersion;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;

import ch.ivyteam.ivy.db.IExternalDatabase;
import ch.ivyteam.ivy.db.IExternalDatabaseApplicationContext;
import ch.ivyteam.ivy.db.IExternalDatabaseRuntimeConnection;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.DateTime;
import ch.ivyteam.ivy.scripting.objects.File;
import ch.ivyteam.ivy.scripting.objects.Record;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.Time;

/**
 * @author ec
 * 
 */
public class FileVersioningController extends AbstractFileVersioningController {

	private String ivyDBConnectionName = null;
	private String fileTableName = null;
	private String fileContentTableName = null;
	private String fileVersionTableName = null;
	private String fileVersionContentTableName = null;
	private String databaseSchemaName = null;
	private IExternalDatabase database;

	/**
	 * creates a new FileVersioningController object.<br />
	 * All the parameters for the Database connections are going to retrieved
	 * from the ivy global variables.
	 * 
	 */
	public FileVersioningController() {
		this.databaseSchemaName = Ivy.var().get(
				"xivy_addons_fileManager_databaseSchemaName");
		this.ivyDBConnectionName = Ivy.var().get(
				"xivy_addons_fileManager_ivyDatabaseConnectionName");
		if (databaseSchemaName != null
				&& databaseSchemaName.trim().length() > 0) {
			this.fileTableName = this.databaseSchemaName
					+ "."
					+ Ivy.var().get(
							"xivy_addons_fileManager_fileMetaDataTableName");
			this.fileContentTableName = this.databaseSchemaName
					+ "."
					+ Ivy.var().get(
							"xivy_addons_fileManager_fileContentTableName");
			this.fileVersionTableName = this.databaseSchemaName
					+ "."
					+ Ivy
							.var()
							.get(
									"xivy_addons_fileManager_fileVersioningMetaDataTableName");
			this.fileVersionContentTableName = this.databaseSchemaName
					+ "."
					+ Ivy
							.var()
							.get(
									"xivy_addons_fileManager_fileVersioningContentTableName");
		} else {
			this.fileTableName = Ivy.var().get(
					"xivy_addons_fileManager_fileMetaDataTableName");
			this.fileContentTableName = Ivy.var().get(
					"xivy_addons_fileManager_fileContentTableName");
			this.fileVersionTableName = Ivy.var().get(
					"xivy_addons_fileManager_fileVersioningMetaDataTableName");
			this.fileVersionContentTableName = Ivy.var().get(
					"xivy_addons_fileManager_fileVersioningContentTableName");
		}
	}

	/**
	 * creates a new FileVersioningController object.<br />
	 * You can fine tunes the settings with the input parameters.<br />
	 * If any parameter is null or is an empty String, it will be set with its
	 * ivy global variable.
	 * 
	 * @param _ivyDBConnectionName
	 * @param _fileTableName
	 * @param _fileContentTableName
	 * @param _fileVersionTableName
	 * @param _fileVersionContentTableName
	 * @param _schemaName
	 */
	public FileVersioningController(String _ivyDBConnectionName,
			String _fileTableName, String _fileContentTableName,
			String _fileVersionTableName, String _fileVersionContentTableName,
			String _schemaName) {
		if (_ivyDBConnectionName == null
				|| _ivyDBConnectionName.trim().length() == 0) {
			this.ivyDBConnectionName = Ivy.var().get(
					"xivy_addons_fileManager_ivyDatabaseConnectionName");
		} else {
			this.ivyDBConnectionName = _ivyDBConnectionName.trim();
		}

		if (_schemaName == null || _schemaName.trim().length() == 0) {
			this.databaseSchemaName = Ivy.var().get(
					"xivy_addons_fileManager_databaseSchemaName");
		} else {
			this.databaseSchemaName = _schemaName.trim();
		}

		if (_fileTableName == null || _fileTableName.trim().length() == 0) {
			this.fileTableName = Ivy.var().get(
					"xivy_addons_fileManager_fileMetaDataTableName");
		} else {
			this.fileTableName = _fileTableName.trim();
		}

		if (_fileContentTableName == null
				|| _fileContentTableName.trim().length() == 0) {
			this.fileContentTableName = Ivy.var().get(
					"xivy_addons_fileManager_fileContentTableName");
		} else {
			this.fileContentTableName = _fileContentTableName.trim();
		}

		if (_fileVersionTableName == null
				|| _fileVersionTableName.trim().length() == 0) {
			this.fileVersionTableName = Ivy.var().get(
					"xivy_addons_fileManager_fileVersioningMetaDataTableName");
		} else {
			this.fileVersionTableName = _fileVersionTableName.trim();
		}

		if (_fileVersionContentTableName == null
				|| _fileVersionContentTableName.trim().length() == 0) {
			this.fileVersionContentTableName = Ivy.var().get(
					"xivy_addons_fileManager_fileVersioningContentTableName");
		} else {
			this.fileVersionContentTableName = _fileVersionContentTableName
					.trim();
		}

		if (databaseSchemaName != null
				&& databaseSchemaName.trim().length() > 0) {
			this.fileTableName = this.databaseSchemaName + "."
					+ this.fileTableName;
			this.fileContentTableName = this.databaseSchemaName + "."
					+ this.fileContentTableName;
			this.fileVersionTableName = this.databaseSchemaName + "."
					+ this.fileVersionTableName;
			this.fileVersionContentTableName = this.databaseSchemaName + "."
					+ this.fileVersionContentTableName;
		}
	}

	@Override
	public List<FileVersion> getFileVersions(long fileId) throws Exception {

		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection = connection.getDatabaseConnection();
			return this.getFileVersions(fileId, jdbcConnection);
		} finally {
			if (connection != null) {
				database.giveBackAndUnlockConnection(connection);
			}
		}
	}
	
	@Override
	public List<FileVersion> getFileVersions(long fileId, java.sql.Connection con) throws Exception {
		if(con==null || con.isClosed())
		{
			throw new SQLException("the java.sql.Connection passed to the getFileVersions method is null or closed.");
		}
		List<FileVersion> l = new ArrayList<FileVersion>();
		if (fileId < 0) {
			return l;
		}
		String query = "SELECT * FROM " + this.fileVersionTableName
				+ " WHERE file_id=? ORDER BY version_number DESC";
		List<Record> recordList = new ArrayList<Record>();
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(query);
			stmt.setLong(1, fileId);
			recordList = executeStmt(stmt);
		} finally {
			DatabaseUtil.close(stmt);
		}
		if (recordList != null) {
			for (Record rec : recordList) {
				try {
					FileVersion fv = new FileVersion();
					fv.setId(Long.parseLong(rec.getField("versionid").toString()));
					fv.setFileid(fileId);
					fv.setVersionNumber(Integer.parseInt(rec.getField("version_number").toString()));
					fv.setVersionContentId(Long.parseLong(rec.getField("fvc_id").toString()));
					String s = rec.getField("cdate").toString();
					if(s.length()>19)
					{
						s=s.substring(0, 19);
					}
					try {
						fv.setDate(new Date(s));
					} catch (Exception ex) {
						try {
							fv.setDate(new Date(new DateTime(s).getDate().format("dd.MM.yyyy")));
							
						} catch (Exception e) {
							fv.setDate(new Date());
						}
					}
					try {
						fv.setTime(new Time(new DateTime(rec.getField("ctime")
								.toString()).getTime().format("HH:mm:ss")));
					} catch (Exception e) {
						try {
							if (rec.getField("ctime").toString().length() > 8) {
								fv.setTime(new Time(rec.getField("ctime").toString().substring(
														rec.getField("ctime")
																.toString()
																.length() - 10,
														rec.getField("ctime")
																.toString()
																.length() - 2)));

							} else {
								fv.setTime(new Time(rec.getField("ctime")
										.toString()));
							}
						} catch (Exception ex) {
							fv.setTime(new Time("00:00:00"));
						}
					}

					fv.setUser(rec.getField("cuser").toString());
					fv.setFilename(rec.getField("file_name").toString());
					l.add(fv);
				} catch (Exception ex) {
					Ivy.log().error("An error occurred while parsing the values of a file version from the table. Correspondinmg file id = "
											+ fileId, ex);
				}
			}
		}
		return l;
	}

	/**
	 * Returns the next version number for a given file denoted by its id.<br />
	 * if there are no versions for the file, the returned number will be 1.
	 * 
	 * @param fileId
	 *            fileId the id of the file as stored in the table whose name is
	 *            defined in the ivy var:
	 *            "xivy_addons_fileManager_fileMetaDataTableName"
	 * @return the next file version number or -1 if the fileId <=0
	 * @throws Exception
	 *             in case of a Persistence or SQLException
	 */
	public int getNextVersionNumberForFile(long fileId) throws Exception {
		if (fileId <= 0) {
			return -1;
		}
		int nvn = 1;
		String query = "SELECT version_number FROM "
				+ this.fileVersionTableName + " WHERE file_id=?";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection = connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try {
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileId);
				nvn += executeStmt(stmt).size();
			} finally {
				DatabaseUtil.close(stmt);
			}
		} finally {
			if (connection != null) {
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return nvn;
	}

	@Override
	public FileVersion createNewVersion(long fileId) throws Exception {
		// Ivy.persistence().get("").createEntityManager();
		
		if (fileId <= 0) {
			return null;
		}
		FileVersion fv = null;
		// get the next version number

		// the new id of the version content
		long vcid = 0;
		// the new id of the version
		long vid = 0;
		// get the DocumentOnServer
		DocumentOnServer doc = this.getParentFileWithoutContent(fileId);
		int vn = doc.getVersionnumber().intValue();
		// Query to insert the new version
		String q1 = "INSERT INTO "+ this.fileVersionTableName+ " (file_id,version_number,cdate,ctime,cuser,file_name) VALUES (?,?,?,?,?,?)";
		// Query to insert the new Version Content
		String q2 = "INSERT INTO " + this.fileVersionContentTableName
				+ " (content, version_id) VALUES ((SELECT file_content FROM "
				+ this.fileContentTableName + " WHERE file_id = ?) , ?)";
		String q2bis = "INSERT INTO " + this.fileVersionContentTableName
				+ " (content, version_id) SELECT file_content, ? FROM "
				+ this.fileContentTableName + " WHERE file_id = ? ";

		// Query to update the new file version "file version content Id" field
		String q3 = "UPDATE " + this.fileVersionTableName
				+ " SET fvc_id=? WHERE versionid=?";
		// Query to update the number version in the main file table
		String q4 = "UPDATE "
				+ this.fileTableName
				+ " SET versionnumber=?, creationdate = ?, creationtime = ?, "
				+ "creationuserid = ?, modificationdate = ?, modificationtime = ?, modificationuserid = ?  WHERE fileid=?";
		IExternalDatabaseRuntimeConnection connection = null;
		Ivy.log().info("CREATING VERSION for "+doc.getFileID());
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection = connection.getDatabaseConnection();

			PreparedStatement stmt = null;
			try {
				boolean flag = true;
				// Insert first the new version
				try {
					stmt = jdbcConnection.prepareStatement(q1,PreparedStatement.RETURN_GENERATED_KEYS);
				} catch (SQLFeatureNotSupportedException fex) {
					stmt = jdbcConnection.prepareStatement(q1);
					flag = false;
				}
				Date da = new Date(doc.getCreationDate());

				DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				stmt.setLong(1, fileId);
				stmt.setInt(2, vn);
				stmt.setDate(3, new java.sql.Date(da.toJavaDate().getTime()));
				stmt.setTime(4, new java.sql.Time(sdf.parse(doc.getCreationTime()).getTime()));
				stmt.setString(5, doc.getUserID());
				stmt.setString(6, doc.getFilename());
				stmt.executeUpdate();
				ResultSet rs = null;
				try {
					rs = stmt.getGeneratedKeys();
					if (rs != null && rs.next()) {
						// Retrieve the auto generated key.
						vid = rs.getInt(1);
					}
				} catch (Exception fex) {// The JDBC Driver doesn't accept the
											// PreparedStatement.RETURN_GENERATED_KEYS
					// ignore
				}

				if (!flag || vid <= 0) {// The JDBC Driver doesn't accept the
										// PreparedStatement.RETURN_GENERATED_KEYS
					// we have to get the inserted Id manually....
					vid = this.getFileVersionWithParentFileIdAndVersionNumber(fileId, vn).getId();
				}
				Ivy.log().info("File Version id created "+vid);
				// insert the new version content
				flag = true;
				try {
					stmt = jdbcConnection.prepareStatement(q2,PreparedStatement.RETURN_GENERATED_KEYS);
				} catch (SQLFeatureNotSupportedException fex) {
					
					flag = false;
				}
				if(!flag){
					stmt = jdbcConnection.prepareStatement(q2);
				}
				stmt.setLong(1, fileId);
				stmt.setLong(2, vid);
				boolean error = false;
				try {
					stmt.executeUpdate();
				} catch (SQLException ex) {// Older Version from MS SQL , like
											// MS SQL 2005 may throw an
											// Exception because of the nested
											// query.
					error = true;
				}
				if (error) {
					flag = true;
					try {
						stmt = jdbcConnection.prepareStatement(q2bis,PreparedStatement.RETURN_GENERATED_KEYS);
					} catch (SQLFeatureNotSupportedException fex) {
						flag = false;
					}
					if(!flag){
						stmt = jdbcConnection.prepareStatement(q2bis);
					}

					stmt.setLong(1, vid);
					stmt.setLong(2, fileId);
					stmt.executeUpdate();
				}
				Ivy.log().info("File Version created "+vid);
				rs = null;
				try {
					rs = stmt.getGeneratedKeys();
					if (rs != null && rs.next()) {
						// Retrieve the auto generated key.
						vcid = rs.getInt(1);
					}
				} catch (Exception fex) {// The JDBC Driver doesn't accept the
											// PreparedStatement.RETURN_GENERATED_KEYS
					// ignore
				}

				if (!flag || vcid <= 0) {// The JDBC Driver doesn't accept the
											// PreparedStatement.RETURN_GENERATED_KEYS
					// we have to get the inserted Id manually....
					vcid = this.getLastInsertedFileVersionContent(vid);
				}

				// update the new file version "file version content Id" field
				stmt = jdbcConnection.prepareStatement(q3);
				stmt.setLong(1, vcid);
				stmt.setLong(2, vid);
				stmt.executeUpdate();

				// update the version number in the main file table
				stmt = jdbcConnection.prepareStatement(q4);
				
				vn += 1;// increment the new version number of the main file
				Date d = new Date();
				Time t = new Time();
				Ivy.log().info(q4);
				Ivy.log().info("CREATING VERSION for "+fileId +" New version number is "+vn);
				stmt.setInt(1, vn);
				stmt.setString(2, d.format("dd.MM.yyyy"));
				stmt.setString(3, t.format("HH:mm:ss"));
				stmt.setString(4, Ivy.session().getSessionUserName());
				stmt.setString(5, d.format("dd.MM.yyyy"));
				stmt.setString(6, t.format("HH:mm:ss"));
				stmt.setString(7, Ivy.session().getSessionUserName());
				stmt.setLong(8, fileId);
				stmt.executeUpdate();
				fv = new FileVersion();
				fv.setDate(d);
				fv.setFileid(fileId);
				fv.setFilename(doc.getFilename());
				fv.setTime(t);
				fv.setUser(Ivy.session().getSessionUserName());
				fv.setVersionNumber(vn);
			} finally {
				DatabaseUtil.close(stmt);
			}
		} finally {
			if (connection != null) {
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return fv;
	}

	@Override
	public DocumentOnServer getParentFileWithoutContent(long fileId)
			throws Exception {

		if (fileId <= 0) {
			return null;
		}
		DocumentOnServer doc = new DocumentOnServer();
		List<Record> recordList = new ArrayList<Record>();
		String query = "";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection = connection.getDatabaseConnection();

			query = "SELECT * FROM " + this.fileTableName + " WHERE fileid = ?";
			PreparedStatement stmt = null;
			try {
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileId);
				recordList = executeStmt(stmt);
			} finally {
				DatabaseUtil.close(stmt);
			}
		} finally {
			if (connection != null) {
				database.giveBackAndUnlockConnection(connection);
			}
		}
		if (!recordList.isEmpty()) {
			// we take the first one, normally just one
			Record rec = recordList.get(0);

			doc.setFileID(rec.getField("FileId").toString());
			doc.setFilename(rec.getField("FileName").toString());
			doc.setPath(rec.getField("FilePath").toString());
			doc.setFileSize(rec.getField("FileSize").toString());
			doc.setUserID(rec.getField("CreationUserId").toString());
			doc.setCreationDate(rec.getField("CreationDate").toString());
			doc.setCreationTime(rec.getField("CreationTime").toString());
			doc.setModificationUserID(rec.getField("ModificationUserId").toString());
			doc.setModificationDate(rec.getField("ModificationDate").toString());
			doc.setModificationTime(rec.getField("ModificationTime").toString());
			doc.setLocked(rec.getField("Locked").toString());
			doc.setLockingUserID(rec.getField("LockingUserId").toString());
			doc.setDescription(rec.getField("Description").toString());
			int vn = 1;
			try {
				vn = Integer.parseInt(rec.getField("versionnumber").toString());
			} catch (Exception ex) {
				vn = 1;
			}
			doc.setVersionnumber(vn);
			try {
				doc.setExtension(doc.getFilename().substring(
						doc.getFilename().lastIndexOf(".") + 1));
			} catch (Exception ex) {
				// Ignore the Exception here
			}

		}
		return doc;
	}

	@Override
	public FileVersion getFileVersionWithParentFileIdAndVersionNumber(
			long fileId, int versionNumber) throws Exception {
		FileVersion fv = null;
		if (fileId <= 0 || versionNumber <= 0) {
			return fv;
		}
		String query = "SELECT * FROM " + this.fileVersionTableName
				+ " WHERE file_id=? AND version_number=?";
		// Ivy.log().info("getFileVersionWithParentFileIdAndVersionNumber QUERY "+query);
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection = connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			List<Record> recordList = new ArrayList<Record>();
			try {
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileId);
				stmt.setInt(2, versionNumber);
				recordList = executeStmt(stmt);
				if (recordList.size() > 0) {
					Record rec = recordList.get(0);
					fv = new FileVersion();
					fv.setId(Long.parseLong(rec.getField("versionid")
							.toString()));
					fv.setFileid(fileId);
					fv.setVersionNumber(versionNumber);
					try {
						fv.setVersionContentId(Long.parseLong(rec.getField(
								"fvc_id").toString()));
					} catch (Exception ex) {
						fv.setVersionContentId(0);
					}
					// Ivy.log().info("HERE IS THE DATE OF THE NEW VERSION :"+rec.getField("cdate"));

					fv.setDate(new Date(new DateTime(rec.getField("cdate")
							.toString()).format("dd.MM.yyyy")));
					// Ivy.log().info("HERE IS THE TIME OF THE NEW VERSION :"+rec.getField("ctime"));
					fv.setTime(new Time(new DateTime(rec.getField("ctime")
							.toString()).format("HH:mm:ss")));
					fv.setUser(rec.getField("cuser").toString());
				}
			} finally {
				DatabaseUtil.close(stmt);
			}
		} finally {
			if (connection != null) {
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return fv;
	}


	@Override
	public List<FileVersion> extractVersionsToPath(long parentFileId,
			String _path) throws Exception {
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

		ArrayList<FileVersion> versions = new ArrayList<FileVersion>();

		// get the versions in the versions tables
		String q1 = "SELECT * FROM " + this.fileVersionTableName
				+ " INNER JOIN " + this.fileVersionContentTableName + " ON "
				+ this.fileVersionTableName + ".fvc_id = "
				+ this.fileVersionContentTableName + ".fvcid" + " WHERE "
				+ this.fileVersionTableName + ".file_id = ? ";

		// get the actual version from the
		String q2 = "SELECT * FROM " + this.fileTableName + " INNER JOIN "
				+ this.fileContentTableName + " ON " + this.fileTableName
				+ ".fileid = " + this.fileContentTableName + ".file_id"
				+ " WHERE " + this.fileTableName + ".fileid = ? ";

		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection = connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			String path = PathUtil.formathPathForDirectoryWithoutFirstSeparatorWithEndSeparator(dir.getPath());
			try {
				stmt = jdbcConnection.prepareStatement(q1);
				stmt.setLong(1, parentFileId);
				ResultSet rst = stmt.executeQuery();
				while (rst.next()) {
					FileVersion fv = new FileVersion();
					fv.setId(rst.getLong("versionid"));
					fv.setFileid(rst.getLong("file_id"));
					fv.setVersionNumber(rst.getInt("version_number"));
					fv.setVersionContentId(rst.getLong("fvc_id"));
					fv.setDate(new Date(rst.getDate("cdate")));
					try {
						if (rst.getTime("ctime").toString().length() > 8) {
							fv.setTime(new Time(rst.getTime("ctime").toString().substring(0, 8)));
						} else {
							fv.setTime(new Time(rst.getTime("ctime").toString()));
						}
					} catch (Exception ex) {
						fv.setTime(new Time("00:00:00"));
					}

					fv.setUser(rst.getString("cuser"));
					fv.setFilename(rst.getString("file_name"));
					String vname = FileHandler.getFileNameWithoutExt(rst.getString("file_name"))+ "#"+ rst.getInt("version_number")+ "."
							+ FileHandler.getFileExtension(rst.getString("file_name"));
					Blob bl = null;
					byte[] byt = null;
					try {
						bl = rst.getBlob("content");
					} catch (Throwable t) {
						try {
							byt = rst.getBytes("content");
						} catch (Throwable t2) {

						}
					}

					byte[] allBytesInBlob = bl != null ? bl.getBytes(1,
							(int) bl.length()) : byt;

					FileOutputStream fos = null;
					try {
						java.io.File javaFile = new java.io.File(path + vname);
						fos = new FileOutputStream(javaFile.getPath());
						fos.write(allBytesInBlob);
						fv.setJavaFile(javaFile);
					} finally {
						if (fos != null) {
							fos.close();
						}
					}
					versions.add(fv);
				}

				stmt = jdbcConnection.prepareStatement(q2);
				stmt.setLong(1, parentFileId);
				rst = stmt.executeQuery();
				if (rst.first()) {// should be only one file with the given id
					FileVersion fv = new FileVersion();
					fv.setId(0);
					fv.setFileid(rst.getLong("fileid"));
					fv.setVersionNumber(rst.getInt("versionnumber"));
					fv.setDate(new Date(rst.getString("creationdate")));
					try {
						fv.setTime(new Time(rst.getString("creationtime")));
					} catch (Exception ex) {
						fv.setTime(new Time("00:00:00"));
					}

					fv.setUser(rst.getString("creationuserid"));
					fv.setFilename(rst.getString("filename"));
					String vname = FileHandler.getFileNameWithoutExt(rst
							.getString("filename"))
							+ "#"
							+ rst.getInt("versionnumber")
							+ "."
							+ FileHandler.getFileExtension(rst
									.getString("filename"));
					Blob bl = null;
					byte[] byt = null;
					try {
						bl = rst.getBlob("file_content");
					} catch (Throwable t) {
						try {
							byt = rst.getBytes("file_content");
						} catch (Throwable t2) {

						}
					}

					byte[] allBytesInBlob = bl != null ? bl.getBytes(1,
							(int) bl.length()) : byt;

					FileOutputStream fos = null;
					try {
						java.io.File javaFile = new java.io.File(path + vname);
						fos = new FileOutputStream(javaFile.getPath());
						fos.write(allBytesInBlob);
						fv.setJavaFile(javaFile);
					} finally {
						if (fos != null) {
							fos.close();
						}
					}
					versions.add(fv);
				}
			} finally {
				DatabaseUtil.close(stmt);
			}
		} finally {
			if (connection != null) {
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return versions;
	}

	@Override
	public FileVersion getFileVersionWithJavaFile(long fileVersionId)
			throws Exception {
		FileVersion fv = null;
		if (fileVersionId <= 0) {
			return fv;
		}
		String query = "SELECT * FROM " + this.fileVersionTableName
				+ " INNER JOIN " + this.fileVersionContentTableName + " ON "
				+ this.fileVersionTableName + ".fvc_id = "
				+ this.fileVersionContentTableName + ".fvcid" + " WHERE "
				+ this.fileVersionTableName + ".versionid = ? ";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection = connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try {
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, fileVersionId);
				ResultSet rst = stmt.executeQuery();
				if (rst.next()) {
					fv = this.makeFileVersionWithResultSet(rst, true);
				}
			} finally {
				DatabaseUtil.close(stmt);
			}
		} finally {
			if (connection != null) {
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return fv;
	}

	@Override
	public void deleteAllVersionsFromFile(long fileId) throws Exception {
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection = connection.getDatabaseConnection();
			this.deleteAllVersionsFromFile(fileId, jdbcConnection);
		} finally {
			if (connection != null) {
				database.giveBackAndUnlockConnection(connection);
			}
		}
	}
	
	@Override
	public void deleteAllVersionsFromFile(long fileId, java.sql.Connection con) throws Exception {
		if(con==null || con.isClosed())
		{
			throw new SQLException("the java.sql.Connection passed to the deleteAllVersionsFromFile method is null or closed.");
		}
		if (fileId <= 0) {
			return;
		}
		List<FileVersion> fvs = this.getFileVersions(fileId,con);
		String q1 = "DELETE FROM " + this.fileVersionTableName
				+ " WHERE versionid = ?";
		String q2 = "DELETE FROM " + this.fileVersionContentTableName
				+ " WHERE version_id = ?";
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			stmt = con.prepareStatement(q1);
			stmt2 = con.prepareStatement(q2);
			for (FileVersion fv : fvs) {
				stmt.setLong(1, fv.getId());
				stmt.executeUpdate();

				stmt2.setLong(1, fv.getId());
				stmt2.executeUpdate();
			}
		} finally {
			try {
				DatabaseUtil.close(stmt);
				DatabaseUtil.close(stmt2);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * This method is used to get a last version to the active document
	 * 
	 * @param fileId
	 * @return true if success else false
	 * @throws Exception
	 */
	@Override
	public boolean rollbackLastVersionAsActiveDocument(long fileId)throws Exception {

		if (fileId <= 0) {
			return false;
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
		IExternalDatabaseRuntimeConnection connection = null;
		boolean result = true;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection = connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			try {
				//get last version
				stmt = jdbcConnection.prepareStatement(q0);
				stmt.setLong(1, fileId);
				stmt.setLong(2, fileId);
				ResultSet rst = stmt.executeQuery();
				FileVersion fv = null;
				if (rst.next()) {
					fv = this.makeFileVersionWithResultSet(rst, false);
				}
				if(fv==null || fv.getId()==0)
				{
					result = false;
				}else{
					//rollback the last version content
					stmt = jdbcConnection.prepareStatement(q1);
					stmt.setLong(1, fv.getVersionContentId());
					stmt.setLong(2, fileId);
					stmt.executeUpdate();
					
					//update the file meta information
					stmt = jdbcConnection.prepareStatement(q2);
					stmt.setString(1, Ivy.session().getSessionUserName());
					stmt.setString(2, new Date().format("dd.MM.yyyy"));
					stmt.setString(3, new Time().format("HH:mm:ss"));
					stmt.setInt(4, fv.getVersionNumber());
					stmt.setString(5, fv.getDate().format("dd.MM.yyyy"));
					stmt.setString(6, fv.getTime().format("HH:mm:ss"));
					stmt.setString(7, fv.getUser());
					stmt.setLong(8,fileId);
					stmt.executeUpdate();
					
					//Delete the last version content
					stmt = jdbcConnection.prepareStatement(q3);
					stmt.setLong(1, fv.getVersionContentId());
					stmt.executeUpdate();
					
					//Delete the last version content
					stmt = jdbcConnection.prepareStatement(q4);
					stmt.setLong(1, fv.getVersionContentId());
					stmt.executeUpdate();
				}

			} finally {
				try {
					DatabaseUtil.close(stmt);
				} catch (Exception ex) {
				}
			}
		} finally {
			if (connection != null) {
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param rst
	 * @param getFile
	 * @return
	 * @throws Exception
	 */
	private FileVersion makeFileVersionWithResultSet(ResultSet rst,boolean getFile) throws Exception {
		FileVersion fv = null;
		fv = new FileVersion();
		fv.setId(rst.getLong("versionid"));
		fv.setFileid(rst.getLong("file_id"));
		fv.setVersionNumber(rst.getInt("version_number"));
		fv.setVersionContentId(rst.getLong("fvc_id"));
		fv.setDate(new Date(rst.getDate("cdate")));
		// fv.setDate(new Date(new
		// DateTime(rst.getDate("cdate").toString()).format("dd.MM.yyyy")));
		try {
			if (rst.getTime("ctime").toString().length() > 8) {
				fv.setTime(new Time(rst.getTime("ctime").toString().substring(
						0, 8)));
			} else {
				fv.setTime(new Time(rst.getTime("ctime").toString()));
			}
		} catch (Exception ex) {
			fv.setTime(new Time("00:00:00"));
		}
		// fv.setTime(new Time(new
		// DateTime(rst.getTime("ctime").toString()).format("HH:mm:ss")));
		fv.setUser(rst.getString("cuser"));
		fv.setFilename(rst.getString("file_name"));
		if (getFile) {
			// Blob bl = rst.getBlob("content");
			Blob bl = null;
			byte[] byt = null;
			try {
				bl = rst.getBlob("content");
			} catch (Throwable t) {
				try {
					byt = rst.getBytes("content");
				} catch (Throwable t2) {

				}
			}

			String tmpPath = "tmp/" + System.nanoTime() + "/"
					+ rst.getString("file_name");
			File ivyFile = new File(tmpPath, true);
			ivyFile.createNewFile();
			byte[] allBytesInBlob = bl != null ? bl.getBytes(1, (int) bl
					.length()) : byt;

			FileOutputStream fos = null;
			try {
				java.io.File javaFile = ivyFile.getJavaFile();

				fos = new FileOutputStream(javaFile.getPath());
				fos.write(allBytesInBlob);
				fv.setJavaFile(javaFile);
				fv.setIvyFile(ivyFile);

			} finally {
				if (fos != null) {
					fos.close();
				}
			}
		}

		return fv;
	}

	/**
	 * 
	 * @param versionId
	 * @return
	 * @throws Exception
	 *             in case of a Persistence or SQLException
	 */
	private long getLastInsertedFileVersionContent(long versionId)
			throws Exception {
		long id = 0;
		if (versionId <= 0) {
			return 0;
		}
		String query = "SELECT fvcid FROM " + this.fileVersionContentTableName
				+ " WHERE version_id=?";
		IExternalDatabaseRuntimeConnection connection = null;
		try {
			connection = getDatabase().getAndLockConnection();
			Connection jdbcConnection = connection.getDatabaseConnection();
			PreparedStatement stmt = null;
			List<Record> recordList = new ArrayList<Record>();
			try {
				stmt = jdbcConnection.prepareStatement(query);
				stmt.setLong(1, versionId);
				recordList = executeStmt(stmt);
				if (recordList.size() > 0) {
					Record rec = recordList.get(0);
					id = Long.parseLong(rec.getField("fvcid").toString());

				}
			} finally {
				DatabaseUtil.close(stmt);
			}
		} finally {
			if (connection != null) {
				database.giveBackAndUnlockConnection(connection);
			}
		}
		return id;
	}

	/**
	 * used to get Ivy IExternalDatabase object with given user friendly name of
	 * Ivy Database configuration
	 * 
	 * @param _nameOfTheDatabaseConnection
	 *            : the user friendly name of Ivy Database configuration
	 * @return the IExternalDatabase object
	 * @throws Exception
	 * @throws EnvironmentNotAvailableException
	 */
	private IExternalDatabase getDatabase() throws Exception {
		if (database == null) {
			final String _nameOfTheDatabaseConnection = this.ivyDBConnectionName;
			database = Ivy.session().getSecurityContext().executeAsSystemUser(
					new Callable<IExternalDatabase>() {
						public IExternalDatabase call() throws Exception {
							IExternalDatabaseApplicationContext context = (IExternalDatabaseApplicationContext) Ivy
									.wf()
									.getApplication()
									.getAdapter(
											IExternalDatabaseApplicationContext.class);
							return context
									.getExternalDatabase(_nameOfTheDatabaseConnection);
						}
					});
		}
		return database;
	}

	private static List<Record> executeStmt(PreparedStatement _stmt) throws Exception{

		if(_stmt == null){
			throw(new SQLException("Invalid PreparedStatement","PreparedStatement Null"));
		}

		ResultSet rst = null;
		rst=_stmt.executeQuery();
		List<Record> recordList= new ArrayList<Record>();
		try{
			ResultSetMetaData rsmd = rst.getMetaData();
			int numCols = rsmd.getColumnCount();
			List<String> colNames= new ArrayList<String>();
			for(int i=1; i<=numCols; i++){
				colNames.add(rsmd.getColumnName(i));
				//Ivy.log().debug(rsmd.getColumnName(i));
			}
			while(rst.next()){
				List<Object> values = new ArrayList<Object>();
				for(int i=1; i<=numCols; i++){

					if(rst.getString(i)==null)
						values.add(" ");
					else values.add(rst.getString(i));
				}
				Record rec = new Record(colNames,values);
				recordList.add(rec);
			}
		}catch(Exception ex){
			Ivy.log().error(ex.getMessage(), ex);
		}finally
		{
			DatabaseUtil.close(rst);
		}
		return recordList;
	}

	/**
	 * Returns the Ivy database connection name used to get a connection to the
	 * database,<br />
	 * that contains the file's tables.
	 * 
	 * @return
	 */
	public String getIvyDBConnectionName() {
		return ivyDBConnectionName;
	}

	/**
	 * Set the Ivy database connection name used to get a connection to the
	 * database,<br />
	 * that contains the file's tables.
	 * 
	 * @param ivyDBConnectionName
	 *            the Ivy database connection name used to get a connection to
	 *            the database.
	 */
	public void setIvyDBConnectionName(String ivyDBConnectionName) {
		this.ivyDBConnectionName = ivyDBConnectionName;
	}

	/**
	 * Returns the name of the table that stores the informations about the
	 * files.
	 * 
	 * @return
	 */
	public String getFileTableName() {
		return fileTableName;
	}

	/**
	 * Sets the name of the table that stores the informations about the files.
	 * 
	 * @param fileTableName
	 */
	public void setFileTableName(String fileTableName) {
		this.fileTableName = fileTableName;
	}

	/**
	 * Returns the name of the table that stores the files content.<br />
	 * This table contains the most actual version of the files. The one you get
	 * directly in the File Manager.
	 * 
	 * @return
	 */
	public String getFileContentTableName() {
		return fileContentTableName;
	}

	/**
	 * Sets the name of the table that stores the files content.<br />
	 * This table contains the most actual version of the files. The one you get
	 * directly in the File Manager.
	 * 
	 * @param fileContentTableName
	 */
	public void setFileContentTableName(String fileContentTableName) {
		this.fileContentTableName = fileContentTableName;
	}

	/**
	 * Returns the name of the table that contains the informations about the
	 * old files versions.
	 * 
	 * @return
	 */
	public String getFileVersionTableName() {
		return fileVersionTableName;
	}

	/**
	 * Sets the name of the table that contains the informations about the old
	 * files versions.
	 * 
	 * @param fileVersionTableName
	 */
	public void setFileVersionTableName(String fileVersionTableName) {
		this.fileVersionTableName = fileVersionTableName;
	}

	/**
	 * Returns the name of the table that contains all the files versions
	 * contents.
	 * 
	 * @return
	 */
	public String getFileVersionContentTableName() {
		return fileVersionContentTableName;
	}

	/**
	 * Sets the name of the table that contains all the files versions contents.
	 * 
	 * @param fileVersionContentTableName
	 */
	public void setFileVersionContentTableName(
			String fileVersionContentTableName) {
		this.fileVersionContentTableName = fileVersionContentTableName;
	}

	/**
	 * @return the databaseSchemaName
	 */
	public String getDatabaseSchemaName() {
		return databaseSchemaName;
	}

	/**
	 * @param databaseSchemaName
	 *            the databaseSchemaName to set
	 */
	public void setDatabaseSchemaName(String databaseSchemaName) {

		this.databaseSchemaName = databaseSchemaName;
		if (databaseSchemaName != null
				&& databaseSchemaName.trim().length() > 0) {
			this.fileTableName = this.databaseSchemaName
					+ "."
					+ Ivy.var().get(
							"xivy_addons_fileManager_fileMetaDataTableName");
			this.fileContentTableName = this.databaseSchemaName
					+ "."
					+ Ivy.var().get(
							"xivy_addons_fileManager_fileContentTableName");
			this.fileVersionTableName = this.databaseSchemaName
					+ "."
					+ Ivy
							.var()
							.get(
									"xivy_addons_fileManager_fileVersioningMetaDataTableName");
			this.fileVersionContentTableName = this.databaseSchemaName
					+ "."
					+ Ivy
							.var()
							.get(
									"xivy_addons_fileManager_fileVersioningContentTableName");
		} else {
			this.fileTableName = Ivy.var().get(
					"xivy_addons_fileManager_fileMetaDataTableName");
			this.fileContentTableName = Ivy.var().get(
					"xivy_addons_fileManager_fileContentTableName");
			this.fileVersionTableName = Ivy.var().get(
					"xivy_addons_fileManager_fileVersioningMetaDataTableName");
			this.fileVersionContentTableName = Ivy.var().get(
					"xivy_addons_fileManager_fileVersioningContentTableName");
		}
	}

}
