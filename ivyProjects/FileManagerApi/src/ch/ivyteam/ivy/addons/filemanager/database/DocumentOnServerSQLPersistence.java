package ch.ivyteam.ivy.addons.filemanager.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.FileManagementHandlersFactory;
import ch.ivyteam.ivy.addons.filemanager.FileTag;
import ch.ivyteam.ivy.addons.filemanager.FileType;
import ch.ivyteam.ivy.addons.filemanager.KeyValuePair;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.filelink.AbstractFileLinkController;
import ch.ivyteam.ivy.addons.filemanager.database.filelink.FileLink;
import ch.ivyteam.ivy.addons.filemanager.database.filetype.AbstractFileTypesController;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IDocumentOnServerPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.SqlPersistenceHelper;
import ch.ivyteam.ivy.addons.filemanager.database.search.DocumentCreationDateSearch;
import ch.ivyteam.ivy.addons.filemanager.database.sql.DocumentOnServerSQLQueries;
import ch.ivyteam.ivy.addons.filemanager.database.sql.FileExtractor;
import ch.ivyteam.ivy.addons.filemanager.database.sql.PersistenceConnectionManagerReleaser;
import ch.ivyteam.ivy.addons.filemanager.exception.FileManagementException;
import ch.ivyteam.ivy.addons.filemanager.restricted.database.sql.DocumentOnServerGeneratorHelper;
import ch.ivyteam.ivy.addons.filemanager.restricted.database.sql.FolderOnServerTranslationHelper;
import ch.ivyteam.ivy.addons.filemanager.util.DateUtil;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.Record;

/**
 * The DocumentOnServerSQLPersistence is a particular IDocumentOnServerPersistence.<br>
 * It uses a database java.sql.Connection to be able to communicate with the database.
 * @author ec
 *
 */
public class DocumentOnServerSQLPersistence implements
IDocumentOnServerPersistence {

	private IPersistenceConnectionManager<Connection> connectionManager =null;
	private BasicConfigurationController configuration;
	private String fileTableNameSpace;
	private String fileContentTableNameSpace;
	private String escapeChar = "\\";
	private AbstractFileTypesController fileTypesController = null;
	
	private AbstractFileLinkController fileLinkController = null;

	/**
	 * 
	 * @param config
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	protected DocumentOnServerSQLPersistence (BasicConfigurationController config) throws Exception {
		this.configuration=config;
		this.connectionManager =  (IPersistenceConnectionManager<Connection>) PersistenceConnectionManagerFactory
				.getPersistenceConnectionManagerInstance(config);
		this.initialize();
	}

	/**
	 * For Unit tests only
	 * @param config
	 * @param connectionManager
	 */
	protected DocumentOnServerSQLPersistence(BasicConfigurationController config, IPersistenceConnectionManager<Connection> connectionManager) {
		this.configuration=config;
		this.connectionManager = connectionManager;
	}

	private void initialize() throws Exception {
		if(this.configuration.getDatabaseSchemaName()!=null  && this.configuration.getDatabaseSchemaName().length()>0) {
			this.fileContentTableNameSpace = this.configuration.getDatabaseSchemaName()+"."+this.configuration.getFilesContentTableName();
			this.fileTableNameSpace = this.configuration.getDatabaseSchemaName()+"."+this.configuration.getFilesTableName();
		}else{
			this.fileContentTableNameSpace = this.configuration.getFilesContentTableName();
			this.fileTableNameSpace = this.configuration.getFilesTableName();
		}
		if(this.configuration.isActivateFileType()) {
			try {
				this.fileTypesController = FileManagementHandlersFactory.getFileTypesControllerInstance(this.configuration);
			} catch (Exception e) {
				Ivy.log().error(e.getMessage(),e);
			}
		}
		fileLinkController = FileManagementHandlersFactory.getFileLinkControllerInstance(configuration);
		try {
			DatabaseMetaData dbmd = this.connectionManager.getConnection().getMetaData();
			String prod = dbmd.getDatabaseProductName().toLowerCase();
			if(prod.contains("mysql") || (prod.contains("postgre") && 
					Double.valueOf(dbmd.getDatabaseMajorVersion()+"."+dbmd.getDatabaseMinorVersion())<9.1 )){
				this.escapeChar="\\\\";
			}
		} finally {
			try {
				this.connectionManager.closeConnection();
			} catch (Exception e) {
				Ivy.log().error(e.getMessage(),e);
			}
		}

	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.IDatabasePersistency#create(java.lang.Object)
	 */
	/**
	 * Inserts a new DocumentOnServer. The given DocumentOnServer object contains all the necessary informations 
	 */
	@Override
	public DocumentOnServer create(DocumentOnServer document) throws Exception {
		this.checkDocumentOnServer(document);
		//make sure the document is new
		if(this.get(document.getPath())!=null) {
			// this document exists already
			throw new FileManagementException("The document already exists and cannot be recreated : "+document.getPath());
		}
		if(document instanceof FileLink) {
			checkFileLinkController("Cannot create new FileLink because the FileLinkController has not been instanciated.");
			return this.fileLinkController.createFileLink((FileLink) document);
		}
		//make sure the a file exists for the document to insert
		java.io.File jf = docToJavaFile(document);

		String query = DocumentOnServerSQLQueries.INSERT_Q
				.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileTableNameSpace);
		this.setDocumentDatesAndTimes(document);
		PreparedStatement stmt=null;
		ResultSet rs = null;
		try {
			boolean flag = true;
			//write the file meta informations
			try {
				stmt = this.connectionManager.getConnection().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			}catch(SQLFeatureNotSupportedException fex) {
				//The JDBC Driver doesn't accept the PreparedStatement.RETURN_GENERATED_KEYS
				stmt = this.connectionManager.getConnection().prepareStatement(query);
				flag=false;
			}
			stmt.setString(1, (document.getFilename()==null || document.getFilename().trim().length()==0)? 
					document.getJavaFile().getName():document.getFilename());
			stmt.setString(2, PathUtil.escapeBackSlash(document.getPath()));
			stmt.setString(3, (document.getUserID()==null || document.getUserID().trim().length()==0)?
					Ivy.session().getSessionUserName():document.getUserID());
			stmt.setString(4,document.getCreationDate());
			stmt.setString(5, document.getCreationTime());
			stmt.setString(6, document.getFileSize());
			stmt.setInt(7, 0);
			stmt.setString(8, "");
			stmt.setString(9, document.getUserID());
			stmt.setString(10, document.getModificationDate());
			stmt.setString(11, document.getModificationTime());
			stmt.setString(12, document.getDescription()==null?"":document.getDescription());

			stmt.executeUpdate();

			long insertedId = 0;
			try {
				rs = stmt.getGeneratedKeys();
				if ( rs!=null && rs.next() ) {
					// Retrieve the auto generated key(s).
					insertedId = rs.getLong(1);
					rs.close();
				}
			} catch(Exception ex) {
				//ignore the exception: happens if system is ORACLE and so on....
			}

			if(!flag || insertedId<=0) {
				try {
					insertedId=Integer.parseInt(this.get(PathUtil.escapeBackSlash(document.getPath())).getFileID());
				} catch(Exception ex) {
					Ivy.log().error(ex.getMessage(),ex);
				}
			}
			if(insertedId<=0) {
				throw new FileManagementException("The inserted document id is invalid.");
			}
			stmt.close();
			document.setFileID(String.valueOf(insertedId));
			if(this.configuration.isStoreFilesInDB()) {
				//INSERT THE FILE CONTENT IN THE CONTENT TABLE
				Ivy.log().debug("Inserted Id in file table : "+ insertedId);
				query = DocumentOnServerSQLQueries.INSERT_CONTENT_Q
						.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileContentTableNameSpace);
				stmt = this.connectionManager.getConnection().prepareStatement(query);
				FileInputStream is=null;
				try {
					is = new FileInputStream ( jf );   
					stmt.setLong(1, insertedId);
					stmt.setBinaryStream (2, is, (int) jf.length() ); 
					stmt.executeUpdate();
				} finally {
					if(is!=null) {
						is.close();
					}
				}
			}
			rs.close();

		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rs, "create", this.getClass());
		}
		return document;
	}

	

	/**
	 * <b>Not public API<b>
	 * @param docsWithJavaFile
	 * @throws Exception
	 */
	protected void insertContentInBlob(List<DocumentOnServer> docsWithJavaFile) throws Exception{
		PreparedStatement stmt=null;
		try{
			String query = DocumentOnServerSQLQueries.INSERT_CONTENT_Q
					.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileContentTableNameSpace);
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			for(DocumentOnServer doc: docsWithJavaFile) {
				if(doc.getJavaFile()!=null && doc.getJavaFile().isFile()) {
					FileInputStream is=null;
					try {
						is = new FileInputStream ( doc.getJavaFile() );   
						stmt.setLong(1, Long.parseLong(doc.getFileID()));
						stmt.setBinaryStream (2, is, (int) doc.getJavaFile().length()); 
						stmt.executeUpdate();
					} finally {
						if(is!=null) {
							is.close();
						}
					}
				}
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "insertContentInBlob", this.getClass());
		}
	}

	private java.io.File docToJavaFile(DocumentOnServer document)
			throws FileNotFoundException {
		java.io.File jf = null;
		if(document.getJavaFile()==null || !document.getJavaFile().exists()) {
			if(document.getIvyFile() !=null && document.getIvyFile().exists()) {
				jf = document.getIvyFile().getJavaFile();
			} else if(document.getPath()!=null && !document.getPath().trim().equals("")) {
				jf =new java.io.File(document.getPath().trim());
			}
		} else {
			jf = document.getJavaFile();
		}
		if(jf == null || !jf.exists()) {
			throw new FileNotFoundException("The file to insert in the database was not found.");
		}
		document.setFileSize(FileHandler.getFileSize(jf));
		return jf;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.IDatabasePersistency#update(java.lang.Object)
	 */
	/**
	 * Updates the given DocumentOnServer object. 
	 */
	@Override
	public DocumentOnServer update(DocumentOnServer document)
			throws Exception {
		if(document == null || document.getFileID()==null || document.getFileID().trim().isEmpty() ||
				Long.parseLong(document.getFileID())<=0){
			throw new IllegalArgumentException("The documentOnServer must not be null and its id must be greater than zero.");
		}
		if(document instanceof FileLink) {
			checkFileLinkController("Cannot update FileLink because the FileLinkController has not been instanciated.");
			return this.fileLinkController.updateFileLink((FileLink) document);
		}
		if(this.fileLinkController != null) {
			int vn = this.get(Long.parseLong(document.getFileID())).getVersionnumber().intValue();
			if(document.getVersionnumber().intValue() != vn) {
				this.fileLinkController.updateFileLinksVersionId(Long.parseLong(document.getFileID()), vn);
			}
			
		}
		String query = DocumentOnServerSQLQueries.UPDATE_BY_ID_Q
				.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileTableNameSpace);
		if(!this.configuration.isActivateFileType()) {
			query = DocumentOnServerSQLQueries.UPDATE_BY_ID_NO_FILETYPE_Q
					.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileTableNameSpace);
		}
		PreparedStatement stmt=null;
		this.setDocumentDatesAndTimes(document);
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, document.getFilename());
			stmt.setString(2, document.getPath());
			stmt.setString(3, (document.getJavaFile()!=null && document.getJavaFile().isFile())?
					FileHandler.getFileSize(document.getJavaFile()):document.getFileSize());
			stmt.setInt(4, Integer.parseInt(StringUtils.isBlank(document.getLocked()) ? "0" : document.getLocked()));
			stmt.setString(5, document.getLockingUserID()== null? "" : document.getLockingUserID());
			stmt.setString(6, (document.getModificationUserID() == null || document.getModificationUserID().trim().isEmpty())?
					Ivy.session().getSessionUserName():document.getModificationUserID());
			stmt.setString(7, document.getModificationDate());
			stmt.setString(8, document.getModificationTime());
			stmt.setString(9, document.getDescription());
			stmt.setInt(10, document.getVersionnumber()!=null?document.getVersionnumber().intValue():1);
			if(this.configuration.isActivateFileType()) {
				stmt.setInt(11, document.getFileType()!=null && document.getFileType().getId()!=null? document.getFileType().getId().intValue():0);
				stmt.setLong(12, Long.parseLong(document.getFileID()));
			}else {
				stmt.setLong(11, Long.parseLong(document.getFileID()));
			}
			stmt.executeUpdate();
			try {
				stmt.close();
			} catch( SQLException ex) {
				Ivy.log().error("PreparedStatement cannot be closed in update method.",ex);
			}
			if(this.configuration.isStoreFilesInDB() && document.getJavaFile()!=null && document.getJavaFile().isFile()) {
				query = DocumentOnServerSQLQueries.UPDATE_CONTENT_Q
						.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileContentTableNameSpace);
				stmt = this.connectionManager.getConnection().prepareStatement(query);
				FileInputStream is=null;
				try {
					is = new FileInputStream ( document.getJavaFile() );   

					stmt.setBinaryStream (1, is, (int) document.getJavaFile().length() ); 
					stmt.setLong(2, Long.decode(document.getFileID()));
					stmt.executeUpdate();
				} finally {
					if(is!=null) {
						is.close();
					}
				}
			}

		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "update", this.getClass());
		}
		return document;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.IDatabasePersistency#get(java.lang.String)
	 */
	/**
	 * Returns the DocumentOnServer object that is stored in the given file path.<br> 
	 * If no Document can be found at this path, returns null.<br>
	 * This object contains only the meta informations about the document. 
	 * If the document content is stored in the database then you have to use one of the public getDocumentOnServerWithJavaFile methods 
	 * if you need the javaFile field of the documentOnServer.
	 * @param filepath the String path of the document to return.
	 * @return the DocumentOnServer object found at this path. Null if no document was found.
	 */
	@Override
	public DocumentOnServer get(String filepath) throws Exception {

		assert (filepath!=null && filepath.trim().length()>0):"IllegalArgumentException: The given filepath is invalid in get(String). ".concat(this.getClass().toString());
		
		if(this.fileLinkController != null) {
			FileLink fl = this.fileLinkController.getFileLink(filepath);
			if(fl != null) {
				return fl;
			}
		}
		
		String query = DocumentOnServerSQLQueries.SELECT_WITH_PATH_Q
				.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileTableNameSpace)
				.replace(DocumentOnServerSQLQueries.ESCAPECHAR_PLACEHOLDER, this.escapeChar);

		PreparedStatement stmt=null;
		DocumentOnServer doc = null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, PathUtil.escapeUnderscoreInPath(PathUtil.escapeBackSlash(filepath)));
			rst = stmt.executeQuery();
			Ivy.log().debug("{0} {1}", filepath, query);
			if(rst.next()) {
				doc = DocumentOnServerGeneratorHelper.buildDocumentOnServerWithResulSetRow(rst, configuration);
			}
			rst.close();
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "get(String filePath)", this.getClass());
		}
		if(this.configuration.isActivateFileType() && this.fileTypesController!=null) {
			try {
				if(doc!=null && doc.getFileType()!=null && doc.getFileType().getId()!=null && doc.getFileType().getId()>0) {
					doc.setFileType(this.fileTypesController.getFileTypeWithId(doc.getFileType().getId()));
				}
			}catch(Exception ex) {
				//do nothing the file type is not mandatory
				Ivy.log().warn("WARNING while getting the file type on "+doc.getFilename()+ " "+ex.getMessage(), ex);
			}
		}
		if(doc == null && this.fileLinkController != null) {
			doc = this.fileLinkController.getFileLink(filepath);
		}
		return doc;
	}

	/**
	 * Returns the DocumentOnServer object that corresponds to the given id.<br> 
	 * If no Document can be found, returns null.<br>
	 * This object contains only the meta informations about the document. 
	 * If the document content is stored in the database then you have to use one of the public getDocumentOnServerWithJavaFile methods 
	 * if you need the javaFile field of the documentOnServer.
	 * @param id the id of the document to return as long.
	 * @return the DocumentOnServer object found. Null if no document was found.
	 */
	@Override
	public DocumentOnServer get(long id) throws Exception {
		assert id >0:"IllegalArgumentException: The given id is invalid.";
		String query = DocumentOnServerSQLQueries.SELECT_WITH_ID_Q
				.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileTableNameSpace);
		PreparedStatement stmt=null;
		DocumentOnServer doc = null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, id);
			rst = stmt.executeQuery();

			if(rst.next()) {
				doc = DocumentOnServerGeneratorHelper.buildDocumentOnServerWithResulSetRow(rst, configuration);
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "get(long id)", this.getClass());
		}
		if(this.configuration.isActivateFileType() && this.fileTypesController!=null) {
			try {
				if(doc!=null && doc.getFileType()!=null && doc.getFileType().getId()!=null && doc.getFileType().getId()>0) {
					doc.setFileType(this.fileTypesController.getFileTypeWithId(doc.getFileType().getId()));
				}
			}catch(Exception ex) {
				//do nothing the file type is not mandatory
				Ivy.log().warn("WARNING while getting the file type on "+doc.getFilename()+ " "+ex.getMessage(), ex);
			}
		}
		return doc;
	}

	/**
	 * Returns a list of documentOnServer contained in a directory given by its path.<br>
	 * If the recursive parameter is true, it will search for all the documents under the directory tree.
	 */
	@Override
	public List<DocumentOnServer> getList(String searchpath, boolean recursive) throws Exception {

		assert (searchpath!=null && searchpath.trim().length()>0):"The given filepath is invalid.";
		String formatedPath = searchpath;
		formatedPath = PathUtil.formatPathForDirectoryWithoutLastSeparator(searchpath)+"/";
		formatedPath = PathUtil.escapeUnderscoreInPath(formatedPath);
		String query = ( recursive? 
				DocumentOnServerSQLQueries.SELECT_WITH_PATH_Q
				.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileTableNameSpace)
				.replace(DocumentOnServerSQLQueries.ESCAPECHAR_PLACEHOLDER, this.escapeChar) :
					DocumentOnServerSQLQueries.SELECT_NOT_RECURSIVE_WITH_PATH_Q
					.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileTableNameSpace)
					.replace(DocumentOnServerSQLQueries.ESCAPECHAR_PLACEHOLDER, this.escapeChar)
				);
		PreparedStatement stmt=null;
		ArrayList<DocumentOnServer> docs = new ArrayList<DocumentOnServer>();
		ResultSet rst = null;
		try{
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setString(1, formatedPath+"%");
			if(!recursive) {
				stmt.setString(2, formatedPath+"%/%");
			}
			Ivy.log().debug(query + " "+formatedPath);
			rst  = stmt.executeQuery();
			while(rst.next()) {
				DocumentOnServer doc = DocumentOnServerGeneratorHelper.buildDocumentOnServerWithResulSetRow(rst, configuration);
				docs.add(doc);
			}
			rst.close();
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getList", this.getClass());
		}
		if(this.configuration.isActivateFileType() && this.fileTypesController!=null) {
			for(DocumentOnServer doc:docs) {
				try{
					if(doc.getFileType()!=null && doc.getFileType().getId()!=null && doc.getFileType().getId()>0) {
						doc.setFileType(this.fileTypesController.getFileTypeWithId(doc.getFileType().getId()));
					}
				}catch(Exception ex)
				{
					//do nothing the file type is not mandatory
					Ivy.log().warn("WARNING while getting the file type on "+doc.getFilename()+ " "+ex.getMessage(), ex);
				}
			}
		}
		
		if(this.fileLinkController != null) {
			List<FileLink> fll = this.fileLinkController.getFileLinksUnderPath(searchpath, recursive);
			docs.addAll(fll);
		}
		
		if(this.configuration.isActivateDirectoryTranslation()) {
			for(DocumentOnServer doc: docs) {
				String path = FolderOnServerTranslationHelper.getTranslatedPathForFolderOnServer(PathUtil.getParentDirectoryPath(doc.getPath()), configuration);
				doc.setDisplayedPath(path+"/"+doc.getFilename());
			}
		}
		
		return docs;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.IDatabasePersistency#delete(java.lang.Object)
	 */
	@Override
	public boolean delete(DocumentOnServer docToDelete) throws Exception {
		assert (docToDelete !=null):"IllegalArgumentException: the document to delete is invalid.";
		
		if(docToDelete instanceof FileLink) {
			checkFileLinkController("Cannot delete FileLink because the FileLinkController has not been instanciated.");
			return this.fileLinkController.deleteFileLink((FileLink) docToDelete);
		}
		
		if(docToDelete.getFileID() == null || docToDelete.getFileID().trim().length()==0) {
			//no id in the document to delete, we try to get it
			DocumentOnServer doc = this.get(docToDelete.getPath());
			if(doc==null) {
				return false;
			}
			docToDelete.setFileID(doc.getFileID());
		}else if(this.get(Long.parseLong(docToDelete.getFileID()))==null){
			return false;
		}
		String query = DocumentOnServerSQLQueries.DELETE_FILE_Q
				.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileTableNameSpace);
		PreparedStatement stmt=null;
		boolean flag = false;
		try{
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, Long.decode(docToDelete.getFileID()));
			flag = stmt.executeUpdate()>0;
			try {
				stmt.close();
			} catch( SQLException ex) {
				Ivy.log().error("PreparedStatement cannot be closed in get method.",ex);
			}

			Ivy.log().debug("DocumentOnServer found and deleted? {0}",flag);
			if(this.configuration.isStoreFilesInDB()) {
				String query2 = DocumentOnServerSQLQueries.DELETE_CONTENT_FILE_Q
						.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileContentTableNameSpace);
				stmt = this.connectionManager.getConnection().prepareStatement(query2);
				stmt.setLong(1, Long.decode(docToDelete.getFileID()));
				stmt.executeUpdate();
			}else {
				java.io.File f = new java.io.File(docToDelete.getPath());
				f.delete();
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "delete", this.getClass());
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.IDocumentOnServerPersistency#getDocumentOnServerWithJavaFile(long)
	 */
	@Override
	public DocumentOnServer getDocumentOnServerWithJavaFile(long id) throws Exception {
		DocumentOnServer doc = this.get(id);
		if(doc!=null){
			this.setGivenDocumentOnServerJavaFile(doc);
		}

		return doc;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.IDocumentOnServerPersistency#getDocumentOnServerWithJavaFile(java.lang.String)
	 */
	@Override
	public DocumentOnServer getDocumentOnServerWithJavaFile(String path) throws Exception {
		DocumentOnServer doc = this.get(path);
		if(doc!=null) {
			if(this.configuration.isStoreFilesInDB()) {
				doc.setJavaFile(this.getJavaFileFromDbForDocumentOnServer(doc));
			} else {
				java.io.File f = new java.io.File(path);
				assert f.isFile():"FileNotFoundException. The file could not be found on the file system at "+path;
				doc.setJavaFile(f);
			}
		} else if( this.fileLinkController != null){
			FileLink fl = this.fileLinkController.getFileLink(path);
			return this.fileLinkController.getFileLinkWithJavaFile(fl.getFileLinkId());
		}
		return doc;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.IDocumentOnServerPersistency#getDocumentOnServerWithJavaFile(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer)
	 */
	@Override
	public void setGivenDocumentOnServerJavaFile(DocumentOnServer doc) throws Exception {
		assert doc!=null && doc.getPath()!=null && doc.getPath().trim().length()>0:"IllegalArgumentException: The given DocumentOnServer is invalid.";
		if(this.configuration.isStoreFilesInDB()) {
			doc.setJavaFile(this.getJavaFileFromDbForDocumentOnServer(doc));
		} else {
			java.io.File f = new java.io.File(doc.getPath());
			assert f.isFile():"FileNotFoundException. The file could not be found on the file system at "+doc.getPath();
			doc.setJavaFile(f);
		}
	}

	@Override
	public void unlockDocumentsUnderPathEditedByUserWithOptionalRecursivity(
			String _path, String _user, boolean _recursive) throws Exception {
		String folderPath = PathUtil.escapeBackSlash(FileHandler.formatPathWithEndSeparator(_path, false));
		folderPath=PathUtil.escapeUnderscoreInPath(folderPath);
		String query="";
		if(_recursive) {
			query=DocumentOnServerSQLQueries.UNLOCK_DOCUMENTS_UNDERPATH_RECURSIVELY_BY_USER_Q
					.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileTableNameSpace)
					.replace(DocumentOnServerSQLQueries.ESCAPECHAR_PLACEHOLDER, this.escapeChar);
		}else {
			query=DocumentOnServerSQLQueries.UNLOCK_DOCUMENTS_UNDERPATH_NOT_RECURSIVE_BY_USER_Q
					.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileTableNameSpace)
					.replace(DocumentOnServerSQLQueries.ESCAPECHAR_PLACEHOLDER, this.escapeChar);
		}
		PreparedStatement stmt = null;
		try{
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			if(_recursive) {
				stmt.setString(1, "");
				stmt.setString(2, _user.trim());
				stmt.setString(3, folderPath+"%");
				stmt.executeUpdate();
			}
			else {
				stmt.setString(1, "");
				stmt.setString(2, _user.trim());
				stmt.setString(3, folderPath+"%");
				stmt.setString(4, folderPath+"%/%");
				stmt.executeUpdate();
			}

		}finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, null, "unlockDocumentsUnderPathEditedByUserWithOptionalRecursivity", this.getClass());
		}
	}

	private java.io.File getJavaFileFromDbForDocumentOnServer (DocumentOnServer doc) throws Exception {
		assert ( doc!=null && doc.getFileID()!=null && doc.getFileID().trim().length()>0)
		:"IllegalArgumentException: The given DocumentOnServer is invalid.";
		if(doc instanceof FileLink) {
			checkFileLinkController("Cannot get FileLink with its java file because the FileLinkController has not been instanciated.");
			FileLink fl = (FileLink) doc;
			return this.fileLinkController.getFileLinkWithJavaFile(fl.getFileLinkId()).getJavaFile();
		}
		long id = Long.decode(doc.getFileID());
		assert id >0:"IllegalArgumentException: The DocumentOnServer id is invalid.";
		String query = DocumentOnServerSQLQueries.SELECT_FILE_CONTENT_Q.replace(
				DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileContentTableNameSpace);
		PreparedStatement stmt=null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			stmt.setLong(1, id);
			rst = stmt.executeQuery();
			if(rst.next()) {
				doc.setIvyFile(FileExtractor.extractInputStreamToTemporaryIvyFile(rst.getBinaryStream("file_content"), doc.getFilename()));
				doc.setJavaFile(doc.getIvyFile().getJavaFile());
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getJavaFileFromDbForDocumentOnServer", this.getClass());
		}
		return doc.getJavaFile();
	}

	@Override
	public List<DocumentOnServer> getDocuments(List<String> _conditions) throws Exception {
		ArrayList<DocumentOnServer>  al = new ArrayList<DocumentOnServer>();
		//Recordset rset = null;
		List<Record> recordList=null;
		StringBuilder query=new StringBuilder("");

		query.append("SELECT * FROM "+this.fileTableNameSpace+" WHERE ");
		if(_conditions==null || _conditions.isEmpty()) {
			return al;
		}
		if(_conditions.size()==1) {
			query.append(_conditions.get(0));
		}else {
			int numConditions= _conditions.size()-1;
			for(int i=0; i<numConditions;i++) {
				query.append(_conditions.get(i)+" AND ");
			}
			query.append(_conditions.get(numConditions));
		}
		PreparedStatement stmt = null;
		ResultSet rst = null;
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query.toString());
			Ivy.log().debug(query.toString());
			rst = stmt.executeQuery();
			recordList=SqlPersistenceHelper.getRecordsListFromResulSet(rst);
			if(recordList!=null) {
				al.addAll(DocumentOnServerGeneratorHelper.makeDocsWithRecordList(recordList, configuration));
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getDocuments(List<String> conditions)", this.getClass());
		}
		if(this.configuration.isActivateFileType() && this.fileTypesController!=null) {
			for(DocumentOnServer doc:al) {
				try {
					if(doc.getFileType()!=null && doc.getFileType().getId()!=null && doc.getFileType().getId()>0) {
						doc.setFileType(this.fileTypesController.getFileTypeWithId(doc.getFileType().getId()));
					}
				}catch(Exception ex) {
					//do nothing the file type is not mandatory
					Ivy.log().debug("WARNING while getting the file type on "+doc.getFilename()+ " "+ex.getMessage(), ex);
				}
			}
		}
		return al;
	}

	protected int updateDocumentWithQuery(String _query) throws Exception {
		Statement stmt = null;
		int row = 0;
		try {
			stmt = this.connectionManager.getConnection().createStatement();
			row = stmt.executeUpdate(_query);
		} finally {
			if(stmt!=null) {
				try {
					stmt.close();
				} catch( SQLException ex) {
					Ivy.log().error("PreparedStatement cannot be closed in get method.",ex);
				}
			}
			this.connectionManager.closeConnection();
		}
		return row;

	}

	protected int updateDocuments(List<KeyValuePair> _KVP, List<String> _conditions) throws Exception {
		int rows=0;
		if(_KVP.isEmpty()){//do nothing if the list of KeyValuePairs is empty
			return 0;
		}
		//build the SQL Query with the keyValue pairs and the list of conditions
		StringBuilder sql = new StringBuilder("UPDATE "+this.fileTableNameSpace+" SET");

		for(KeyValuePair kvp: _KVP) {
			if(kvp.getValue() instanceof String) {
				sql.append(" "+kvp.getKey().toString()+"= '"+kvp.getValue()+"' ,");
			} else {
				sql.append(" "+kvp.getKey().toString()+"= "+kvp.getValue()+" ,");
			}
		}
		sql=sql.deleteCharAt(sql.lastIndexOf(","));

		sql.append(" WHERE ");
		int numConditions= _conditions.size()-1;
		for(int i=0; i<numConditions;i++){
			sql.append(_conditions.get(i).trim()+" AND ");
		}
		sql.append(_conditions.get(numConditions));
		sql.trimToSize();
		String _query = sql.toString();
		rows = this.updateDocumentWithQuery(_query);
		return rows;
	}

	protected String getEscapeChar() {
		return this.escapeChar;
	}

	protected IPersistenceConnectionManager<Connection> getConnectionManager(){
		return this.connectionManager;
	}

	private DocumentOnServer setDocumentDatesAndTimes(DocumentOnServer doc){
		String date = DateUtil.getNewDateAsString();
		if(doc.getCreationDate()==null || doc.getCreationDate().trim().isEmpty()){
			doc.setCreationDate(date);
		}else {
			doc.setCreationDate(new Date(doc.getCreationDate()).format("dd.MM.yyyy"));
		}
		if(doc.getModificationDate()==null || doc.getModificationDate().trim().isEmpty()){
			doc.setModificationDate(date);
		}else{
			doc.setModificationDate(new Date(doc.getModificationDate()).format("dd.MM.yyyy"));
		}
		doc.setCreationTime(DateUtil.formatTime(doc.getCreationTime()));
		doc.setModificationTime(DateUtil.formatTime(doc.getModificationTime()));
		return doc;
	}

	private void checkDocumentOnServer(DocumentOnServer document) {
		if(document == null) {
			throw new IllegalArgumentException("The Document to create must not be null.");
		}
		if(document.getPath()==null || document.getPath().trim().isEmpty()) {
			throw new IllegalArgumentException("The Document's path must not be null or an empty String.");
		}
		if((document.getFilename()==null || document.getFilename().trim().isEmpty()) && document.getJavaFile()==null) {
			throw new IllegalArgumentException("The Document's filename must not be null or an empty String, " +
					"or in that case it should at least references a java.io.File.");
		}
	}
	
	private void checkFileLinkController(String message) throws FileManagementException {
		if(this.fileLinkController == null) {
			throw new FileManagementException(StringUtils.isBlank(message)?"Cannot handle FileLink operations because the FileLinkController has not been instanciated.": message);
		}
	}

	@Override
	public List<DocumentOnServer> getDocumentsFilteredby(String filepathCondition, String filetypeCondition, String tagCondition, DocumentCreationDateSearch creationDateCondition) throws Exception {
		API.checkNotEmpty(filepathCondition, "Search condition for the file path");
		PreparedStatement stmt = null;
		ResultSet rst = null;
		String query = DocumentOnServerSQLQueries.SELECT_ALL_DOCUMENTS_BY_FILTERING_Q.replace(DocumentOnServerSQLQueries.TABLENAMESPACE_PLACEHOLDER, this.fileTableNameSpace);
		boolean hasFileTypeCondition = true;
		boolean hasFileTagCondition = true;
		List<DocumentOnServer> docs = new ArrayList<>();
		
		if(StringUtils.isBlank(filetypeCondition)) {
			query = query.replace("AND filetype.name LIKE ?", "");
			hasFileTypeCondition = false;
		}
		if(StringUtils.isBlank(tagCondition)) {
			query = query.replace("AND tags.tag LIKE ?", "");
			hasFileTagCondition = false;
		}
		if(creationDateCondition == null) {
			query = query.replace("-CREATIONDATE CONDITION-", "");
		} else {
			query = query.replace("-CREATIONDATE CONDITION-", "AND" + creationDateCondition.getQuery());
		}
		Ivy.log().debug(query);
		
		try {
			stmt = this.connectionManager.getConnection().prepareStatement(query);
			if(hasFileTypeCondition && hasFileTagCondition) {
				stmt.setString(1, filetypeCondition);
				stmt.setString(2, tagCondition);
				stmt.setString(3, filepathCondition);
			} else if(hasFileTypeCondition) {
				stmt.setString(1, filetypeCondition);
				stmt.setString(2, filepathCondition);
			} else if(hasFileTagCondition) {
				stmt.setString(1, tagCondition);
				stmt.setString(2, filepathCondition);
			} else {
				stmt.setString(1, filepathCondition);
			}
			rst = stmt.executeQuery();
			
			while(rst.next()) {
				DocumentOnServer doc = new DocumentOnServer();
				
				doc.setFileID(rst.getString(1));
				doc.setFilename(rst.getString(2));
				doc.setPath(rst.getString(3));
				doc.setUserID(rst.getString(4));
				doc.setCreationDate(rst.getString(5));
				doc.setCreationTime(rst.getString(6));
				doc.setFileSize(rst.getString(7));
				doc.setLocked("0");
				doc.setLockingUserID(rst.getString(9));
				doc.setModificationUserID(rst.getString(10));
				doc.setModificationDate(rst.getString(11));
				doc.setModificationTime(rst.getString(12));
				doc.setDescription(rst.getString(13));
				doc.setVersionnumber(rst.getInt(14));
				FileType fileType = new FileType();
				fileType.setId(rst.getLong(15));
				fileType.setFileTypeName(rst.getString(17));
				fileType.setDisplayName(rst.getString(17));
				fileType.setApplicationName(rst.getString(18));
				doc.setFileType(fileType);
				List<FileTag> tags = new ArrayList<>();
				FileTag tag = new FileTag();
				tag.setId(rst.getLong(19));
				tag.setFileId(rst.getLong(20));
				tag.setTag(rst.getString(21));
				tags.add(tag);
				doc.setTags(tags);
				try{
					doc.setExtension(doc.getFilename().substring(doc.getFilename().lastIndexOf(".")+1));
				}catch(Exception ex){
					//Ignore the Exception here
				}
				docs.add(doc);
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, stmt, rst, "getDocumentsFilteredby", this.getClass());
		}
		
		return docs;
	}

}
