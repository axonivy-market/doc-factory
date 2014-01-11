/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.thumbnailer.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IDocumentOnServerPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IThumbnailPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.sql.ThumbnailSQLQueries;
import ch.ivyteam.ivy.addons.filemanager.html.table.model.ThumbnailData;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.ThumbnailConstants;
import ch.ivyteam.ivy.cm.IContentObjectValue;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.File;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.scripting.objects.Time;

/**
 * @author ec
 * This class is responsible for the Thumbnail persistence in a database. It does not create the Thumbnails with the use of the Aspose and other libs.<br>
 * This task is from the responsibility of the ThumbnailCreator class.
 */
public class ThumbnailSQLPersistence implements IThumbnailPersistence {

	private BasicConfigurationController configuration;
	private IPersistenceConnectionManager<Connection> connectionManager =null;
	private IDocumentOnServerPersistence docPersistence;
	private String tableNameSpace;
	
	public ThumbnailSQLPersistence(BasicConfigurationController config) throws Exception {
		if(config==null || !config.isStoreFilesInDB()) {
			throw new IllegalArgumentException("Illegal BasicConfigurationController in ThumbnailSQLPersistence.");
		}
		this.configuration = config;
		this.initialize();
	}
	
	@SuppressWarnings("unchecked")
	private void initialize() throws Exception {
		this.connectionManager =  ((IPersistenceConnectionManager<Connection>) PersistenceConnectionManagerFactory
				.getPersistenceConnectionManagerInstance(this.configuration));
		this.docPersistence = PersistenceConnectionManagerFactory.getIDocumentOnServerPersistenceInstance(this.configuration);
		if(this.configuration.getDatabaseSchemaName()!=null  && this.configuration.getDatabaseSchemaName().length()>0) {
			this.tableNameSpace = this.configuration.getDatabaseSchemaName()+"."+this.configuration.getThumbnailTableName();
		}else {
			this.tableNameSpace = this.configuration.getThumbnailTableName();
		}
	}
	
	/**
	 * <b>NOT PUBLIC API</b>
	 * @param docPersistence
	 */
	protected void setDocPersistence(IDocumentOnServerPersistence docPersistence) {
		this.docPersistence = docPersistence;
	}
	
	/**
	 * Creates a new Thumbnail Object.
	 * @param itemToCreate the ThumbnailData which id is 0 and that contains the needed informations.<br>
	 * If the Thumbnail Object contains a reference to a Thumbnail java.io.File then the content of this file will be inserted as BLOB content.<br>
	 * Else the Thumbnail data will be marked to use the default Thumbnail.
	 * @return the created ThumbnailData object
	 */
	@Override
	public ThumbnailData create(ThumbnailData itemToCreate) throws Exception {
		this.checkThumbnail(itemToCreate, "create");
		if(itemToCreate.getThumbnailFile()!=null && itemToCreate.getThumbnailFile().isFile()) {
			//The thumbnail file exists we use the insert method that inserts the content.
			return this.insertThumbnail(itemToCreate.getThumbnailFile(), Long.parseLong(itemToCreate.getOrgFileId()));
		}
		DocumentOnServer doc = this.docPersistence.get(Long.parseLong(itemToCreate.getOrgFileId()));
		if(doc==null) {
			throw new Exception("The parent document with the id "+itemToCreate.getOrgFileId()+" could not be found to create a new Thumbnail Data");
		}
		String query = ThumbnailSQLQueries.INSERT_THUMBNAIL_WITHOUT_CONTENT.replace(ThumbnailSQLQueries.THUMBNAIL_TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		try {
			String date = new Date().format("dd.MM.yyyy");
			String time = new Time().format("HH:mm:ss");
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, Long.parseLong(doc.getFileID()));
			stmt.setString(2, date);
			stmt.setString(3, time);
			stmt.setInt(4, 1); //We don't create any Thumbnail content: this file will use the default empty image.
			stmt.setString(5, doc.getModificationDate()==null?date:doc.getModificationDate());
			stmt.setString(6, doc.getModificationTime()==null?time:doc.getModificationTime());
			stmt.executeUpdate();
		}finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch( SQLException ex) {
					Ivy.log().error("PreparedStatement cannot be closed in create method, ThumbnailSQLPersistence.",ex);
				}
			}
			this.connectionManager.closeConnection();
		}
		return this.get(Long.parseLong(itemToCreate.getOrgFileId()));
	}
	
	@Override
	public ThumbnailData insertThumbnail(java.io.File _file, long _original_file_id) throws Exception {
		if(_file==null || _original_file_id<=0) {
			throw new IllegalArgumentException("Illegal Argument in ThumbnailSQLPersistence " +
					"insertThumbnail(File _file, long _original_file_id, boolean createFile).");
		}
		
		if (!_file.exists()) {
			throw new FileNotFoundException(
					"The thumbnail file that should be inserted does not exist. ThumbnailSQLPersistence insertThumbnail method.");
		}
		this.deleteThumbnail(_original_file_id, false);
		DocumentOnServer doc = this.docPersistence.get(_original_file_id);
		
		String query = ThumbnailSQLQueries.INSERT_THUMBNAIL_WITH_CONTENT.replace(ThumbnailSQLQueries.THUMBNAIL_TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		FileInputStream is = null;
		try {
			is= new FileInputStream(_file);
			String date = new Date().format("dd.MM.yyyy");
			String time = new Time().format("HH:mm:ss");
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, _original_file_id);
			stmt.setBinaryStream(2, is, (int) _file.length());
			stmt.setString(3, date);
			stmt.setString(4, time);
			stmt.setInt(5, 0);
			stmt.setString(6, doc.getModificationDate()==null?date:doc.getModificationDate());
			stmt.setString(7, doc.getModificationTime()==null?time:doc.getModificationTime());
			stmt.executeUpdate();
		} finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch( SQLException ex) {
					Ivy.log().error("PreparedStatement cannot be closed in " +
							"insertThumbnail(File _file, long _original_file_id,boolean createFile) method, " +
							"ThumbnailSQLPersistence.",ex);
				}
			}
			if(is!=null) {
				try{
					is.close();
				} catch(Exception ex) {
					Ivy.log().error("Thumbnail FileInputStream cannot be closed in " +
							"insertThumbnail(File _file, long _original_file_id,boolean createFile) method, " +
							"ThumbnailSQLPersistence.",ex);
				}
			}
			String tmpPath = ThumbnailConstants.DEFAULT_THUMB_FOLDER + _original_file_id + ThumbnailConstants.DEFAULT_THUMBNAIL_TYPE;
			FileUtils.copyFile(_file, new java.io.File(tmpPath));
			this.connectionManager.closeConnection();
		}
		return this.get(_original_file_id);
	}

	/**
	 * Updating a Thumbnail means: delete it and recreate it.
	 */
	@Override
	public ThumbnailData update(ThumbnailData itemToSave) throws Exception {
		this.checkThumbnail(itemToSave, "update");
		this.delete(itemToSave);
		return this.create(itemToSave);
	}

	/**
	 * Returns the Thumbnail corresponding to the file denoted by its path.<br>
	 * The returned ThumbnailData does not contain any thumbnail physical file. It just contains the thumbnail meta data.
	 * @param the parent filepath
	 * @return the ThumbnailData corresponding to the parent file. Null is returned if no parent file can be found at that path.
	 */
	@Override
	public ThumbnailData get(String filePath) throws Exception {
		if(filePath == null || filePath.trim().isEmpty()) {
			throw new IllegalArgumentException("Illegal Argument in ThumbnailSQLPersistence get(String filePath).");
		}
		DocumentOnServer doc = this.docPersistence.get(filePath);
		if(doc == null || doc.getFileID()==null || doc.getFileID().trim().isEmpty()) {
			return null;
		}
		long l = 0;
		try{
			l = Long.parseLong(doc.getFileID());
		}catch(Exception ex) {
			//do nothing
		}
		if(l>0) {
			return this.get(l);
		}else {
			return null;
		}
	}

	/**
	 * Returns the Thumbnail corresponding to the given file id.<br>
	 * The returned ThumbnailData does not contain any thumbnail physical file. It just contains the thumbnail meta data.
	 * @param the parent file id
	 * @return the ThumbnailData corresponding to the parent file. Null is returned if no thumbnail can be found.
	 */
	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.persistence.IItemPersistence#get(long)
	 */
	@Override
	public ThumbnailData get(long id) throws Exception {
		return this.getThumbnail(id, false);
	}
	
	@Override
	public ThumbnailData getThumbnail(long id,
			boolean withThumbnailFile) throws Exception {
		if(id<=0) {
			throw new IllegalArgumentException("Illegal Argument in ThumbnailSQLPersistence get(long id)).");
		}
		String query = ThumbnailSQLQueries.SELECT_THUMBNAIL_BY_FILE_ID.replace(
				ThumbnailSQLQueries.THUMBNAIL_TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		ThumbnailData data = null;
		Blob bl = null;
		byte[] byt = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, id);
			ResultSet rst = stmt.executeQuery();
			if(rst.next()) {
				data = new ThumbnailData();
				data.setThumbnailId(rst.getLong("id"));
				data.setTimeStamp(System.currentTimeMillis() + "");
				data.setOrgFileId(String.valueOf(rst.getInt("org_file_id")));
				data.setCreationDate(rst.getString("creation_date"));
				data.setCreationTime(rst.getString("creation_time"));
				data.setModificationDate(rst.getString("org_modificationDate"));
				data.setModificationTime(rst.getString("org_modificationTime"));
				data.setUseDefault(rst.getInt("use_default")!=0);
				if(withThumbnailFile) {
					try {
						bl = rst.getBlob("thumb_content");
					} catch (Throwable t) {
						try {
							byt = rst.getBytes("thumb_content");
						} catch (Throwable t2) {
		
						}
					}
				}
			}
		}finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch( SQLException ex) {
					Ivy.log().error("PreparedStatement cannot be closed in create method, ThumbnailSQLPersistence.",ex);
				}
			}
			this.connectionManager.closeConnection();
		}
		if(withThumbnailFile) {
			if(!data.isUseDefault() && (bl!=null || byt!=null)) {
				String tmpPath = ThumbnailConstants.DEFAULT_THUMB_FOLDER_NAME + data.getOrgFileId() + ThumbnailConstants.DEFAULT_THUMBNAIL_TYPE;
				byte[] allBytesInBlob = bl != null ? bl.getBytes(1,
						(int) bl.length()) : byt;
				data.setThumbnailFile(this.fillFile(allBytesInBlob, tmpPath));
			} else{
				String tmpPath = ThumbnailConstants.DEFAULT_THUMB_FOLDER + ThumbnailConstants.DEFAULT_THUMBNAIL_NO_IMAGE;
				java.io.File f = new java.io.File(tmpPath);
				data.setThumbnailFile(f);
			}
		} 
		return data;
	}


	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.persistence.IItemPersistence#delete(java.lang.Object)
	 */
	/**
	 * Deletes a thumbnail in the database without deleting the file
	 */
	@Override
	public boolean delete(ThumbnailData itemToDelete) throws Exception {
		if(itemToDelete == null || itemToDelete.getOrgFileId()==null ||Long.parseLong(itemToDelete.getOrgFileId())<=0) {
			throw new IllegalArgumentException("The ThumbnailData is not valid in the method delete(ThumbnailData itemToDelete)");
		}
		return this.deleteThumbnail(Long.parseLong(itemToDelete.getOrgFileId()), false);
	}
	
	private void checkThumbnail(ThumbnailData data, String method) throws Exception{
		if(data == null || ((data.getOrgFileId()==null || data.getOrgFileId().isEmpty()))){
			throw new IllegalArgumentException("The ThumbnailData is not valid in the method ".concat(method));
		}
		if(Long.parseLong(data.getOrgFileId())<=0) {
			throw new IllegalArgumentException("The ThumbnailData id is not valid in the method ".concat(method));
		}
		
	}

	@Override
	public boolean deleteThumbnail(long org_file_id, boolean deleteRealFile)
			throws Exception {
		if(org_file_id<=0) {
			throw new IllegalArgumentException("The Thumbnail parent file id is not valid in the method deleteThumbnail(long org_file_id, boolean deleteRealFile)");
		}
		String query = ThumbnailSQLQueries.DELETE_TUMBNAIL_BY_FILE_ID.replace(
				ThumbnailSQLQueries.THUMBNAIL_TABLENAMESPACE_PLACEHOLDER, this.tableNameSpace);
		PreparedStatement stmt=null;
		boolean flag = false;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, org_file_id);
			int n = stmt.executeUpdate();
			if(n==1 && deleteRealFile){
				String tmpPath = ThumbnailConstants.DEFAULT_THUMB_FOLDER + org_file_id + ThumbnailConstants.DEFAULT_THUMBNAIL_TYPE;
				java.io.File f = new java.io.File(tmpPath);
				flag = f.delete();
			}else{
				flag = (n==1);
			}
		}finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch( SQLException ex) {
					Ivy.log().error("PreparedStatement cannot be closed in create method, ThumbnailSQLPersistence.",ex);
				}
			}
			this.connectionManager.closeConnection();
		}
		return flag;
	}
	
	private java.io.File fillFile(byte[] allBytesInBlob, String path) throws Exception {
		File ivyFile = null;
		FileOutputStream fos = null;
		ivyFile = new File(path, false);
		if(ivyFile.exists()) {
			ivyFile.delete();
		}
		ivyFile.createNewFile();
		String relPath = ivyFile.getAbsolutePath();
		
		try {
			fos = new FileOutputStream(relPath);
			fos.write(allBytesInBlob);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		return ivyFile.getJavaFile();
	}

	@Override
	public List<ThumbnailData> getThumbnailsForDocuments(
			List<DocumentOnServer> documents, boolean includeJavaFile,
			boolean exportDefaultThumb) throws Exception {
		List<ThumbnailData> datas = List.create(ThumbnailData.class);
		if(documents ==null || documents.isEmpty()) {
			return datas;
		}
		String ids ="";
		for(DocumentOnServer doc: documents) {
			if(doc!=null && doc.getFileID()!=null && !doc.getFileID().trim().isEmpty()) {
				ids+=doc.getFileID()+",";
			}
		}
		if(!ids.isEmpty()) {
			ids = ids.substring(0, ids.lastIndexOf(","));
		}else{
			return datas;
		}
		String query = ThumbnailSQLQueries.SELECT_THUMBNAILS_LIST_BY_FILE_IDS.replace(ThumbnailSQLQueries.THUMBNAIL_TABLENAMESPACE_PLACEHOLDER, 
				this.tableNameSpace).replace(ThumbnailSQLQueries.FILE_IDS_PLACEHOLDER, ids);
		PreparedStatement stmt=null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			ResultSet rst = stmt.executeQuery();
			datas.addAll(this.makeThumbnailDocumentFromResultSet(rst, includeJavaFile, exportDefaultThumb));
		}finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch( SQLException ex) {
					Ivy.log().error("PreparedStatement cannot be closed in create method, ThumbnailSQLPersistence.",ex);
				}
			}
			this.connectionManager.closeConnection();
		}
		return datas;
	}
	
	private List<ThumbnailData> makeThumbnailDocumentFromResultSet(
			ResultSet rst, boolean includeJavaFile, boolean exportDefaultThumb) throws Exception {

		List<ThumbnailData> al = List.create(ThumbnailData.class);
		if (rst == null) {
			return null;
		}

		try {
			// if can find a thumbnail at least
			File ivyFile = null;
			FileOutputStream fos = null;
			String tmpPath = "";
			java.io.File javaFile = null;
			ThumbnailData data = null;
			int useDefaultFlag = -1;
			boolean isFirstLoadDefault = true;
			File ivyFileDefault = null;
			java.io.File javaFileDefault = null;
			//DocumentOnServer originalDoc = null;
			while (rst.next()) {
				data = new ThumbnailData();
				data.setThumbnailId(rst.getLong("id"));
				data.setOrgFileId(String.valueOf(rst.getInt("org_file_id")));
				data.setCreationDate(rst.getString("creation_date"));
				data.setCreationTime(rst.getString("creation_time"));
				data.setModificationDate(rst.getString("org_modificationDate"));
				data.setModificationTime(rst.getString("org_modificationTime"));
				useDefaultFlag = rst.getInt("use_default");
				
				if (includeJavaFile) {
					if (useDefaultFlag == 0) {
						// init file path
						tmpPath = ThumbnailConstants.DEFAULT_THUMB_FOLDER_NAME + data.getOrgFileId() + ThumbnailConstants.DEFAULT_THUMBNAIL_TYPE;
						// get java file
						Blob bl = null;
						byte[] byt = null;
						try {
							bl = rst.getBlob("thumb_content");
						} catch (Throwable t) {
							try {
								byt = rst.getBytes("thumb_content");
							} catch (Throwable t2) {

							}
						}
						ivyFile = new File(tmpPath, true);
						ivyFile.createNewFile();
						ivyFile = ivyFile.makePersistent(true);
						//get absolute path
						String relPath = ivyFile.getAbsolutePath();

						data.setUrl(relPath);
						byte[] allBytesInBlob = bl != null ? bl.getBytes(1,
								(int) bl.length()) : byt;

						try {
							javaFile = ivyFile.getJavaFile();
							fos = new FileOutputStream(relPath);
							fos.write(allBytesInBlob);
							data.setThumbnailFile(javaFile);

							// add to list
							al.add(data);
						} finally {
							if (fos != null) {
								fos.close();
							}
						}
						ivyFile = null;
						if (fos != null) {
							fos.close();
						}
					} else {
						if(exportDefaultThumb){
							try {
								// just load unknown file image for the first image
								if (isFirstLoadDefault) {
									// we create a temp file on the server
									tmpPath = ThumbnailConstants.DEFAULT_THUMB_FOLDER_NAME + ThumbnailConstants.DEFAULT_THUMBNAIL_IMAGE;
									ivyFileDefault = new File(tmpPath, true);
									ivyFileDefault.createNewFile();
									ivyFileDefault.makePersistent(true);
									data.setUrl(ivyFileDefault.getAbsolutePath());
	
									IContentObjectValue obj = Ivy.cms().getContentObjectValue(
													"/ch/ivyteam/ivy/addons/filemanager/Common/Images/unknowsFile",null);
									if (obj != null) {
										javaFileDefault = ivyFileDefault.getJavaFile();
										obj.exportContentToFile(javaFileDefault,"UTF-8");
										byte[] contentInByte = obj.getContentAsByteArray();
										fos = new FileOutputStream(
												javaFileDefault.getPath());
										fos.write(contentInByte);
										data.setThumbnailFile(javaFileDefault);
										// add to list
										al.add(data);
									}
									isFirstLoadDefault = false;
								} else {
									// set loaded unknown file to documentOnServer
									data.setUrl(ivyFileDefault.getAbsolutePath());
									data.setThumbnailFile(javaFileDefault);
									// add to list
									al.add(data);
								}
							} finally {
								if (fos != null) {
									fos.close();
								}
							}
	
							if (fos != null) {
								fos.close();
							}
						} else {
							data.setUrl( ThumbnailConstants.DEFAULT_THUMB_FOLDER + ThumbnailConstants.DEFAULT_THUMBNAIL_IMAGE);
							al.add(data);
						}
					}
				} else {
					//set path if actual file is not exported
					if (useDefaultFlag == 0) {
						//path to real file (exported in future).
						data.setUrl(ThumbnailConstants.DEFAULT_THUMB_FOLDER + data.getOrgFileId() + ThumbnailConstants.DEFAULT_THUMBNAIL_TYPE);
					}else{
						//default image
						data.setUrl( ThumbnailConstants.DEFAULT_THUMB_FOLDER + ThumbnailConstants.DEFAULT_THUMBNAIL_IMAGE);	
					}
					// add to list
					al.add(data);
				}
			}

		} catch (Exception ex) {
			Ivy.log().error(
					"[@65LKG] Exception makeThumbnailDocumentFromResultSet(): "
							+ ex.getMessage(), ex);
			throw ex;
		}

		// Ivy.log().info("finish");
		return al;
	}

	
}
