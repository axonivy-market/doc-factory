/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileActionType;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.FileManagementHandlersFactory;
import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.KeyValuePair;
import ch.ivyteam.ivy.addons.filemanager.LockedFolder;
import ch.ivyteam.ivy.addons.filemanager.ReturnedMessage;
import ch.ivyteam.ivy.addons.filemanager.ZipHandler;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.fileaction.AbstractFileActionHistoryController;
import ch.ivyteam.ivy.addons.filemanager.database.filelink.AbstractFileLinkController;
import ch.ivyteam.ivy.addons.filemanager.database.filelink.FileLink;
import ch.ivyteam.ivy.addons.filemanager.database.filetag.AbstractFileTagsController;
import ch.ivyteam.ivy.addons.filemanager.database.filetype.AbstractFileTypesController;
import ch.ivyteam.ivy.addons.filemanager.database.filetype.FileTypesController;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IDocumentOnServerPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFolderOnServerPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.security.AbstractDirectorySecurityController;
import ch.ivyteam.ivy.addons.filemanager.database.security.DirectorySecurityUtil;
import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityHandler;
import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityHandlerChain;
import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityResponse;
import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityRightsEnum;
import ch.ivyteam.ivy.addons.filemanager.database.versioning.AbstractFileVersioningController;
import ch.ivyteam.ivy.addons.filemanager.database.versioning.FileVersionProtectedException;
import ch.ivyteam.ivy.addons.filemanager.database.versioning.FileVersioningController;
import ch.ivyteam.ivy.addons.filemanager.document.filter.DocumentListFilterUtil;
import ch.ivyteam.ivy.addons.filemanager.event.SystemEventNames;
import ch.ivyteam.ivy.addons.filemanager.event.restricted.SystemEventDispatcher;
import ch.ivyteam.ivy.addons.filemanager.exception.DocumentLockException;
import ch.ivyteam.ivy.addons.filemanager.exception.FileManagementException;
import ch.ivyteam.ivy.addons.filemanager.folderonserver.FolderAction;
import ch.ivyteam.ivy.addons.filemanager.listener.FileActionEvent;
import ch.ivyteam.ivy.addons.filemanager.listener.FileActionListener;
import ch.ivyteam.ivy.addons.filemanager.util.DateUtil;
import ch.ivyteam.ivy.addons.filemanager.util.MethodArgumentsChecker;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.File;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.scripting.objects.Time;
import ch.ivyteam.ivy.scripting.objects.Tree;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.IUser;

/**
 * @author ec
 *
 */
public class FileStoreDBHandler extends AbstractFileSecurityHandler {

	private boolean securityActivated = false;
	private SecurityHandler securityController = null; // the file security controller if the security is activated.

	private boolean activateFileTypesFeature = false;
	private AbstractFileTypesController fileTypesController = null;

	private boolean activateFileVersioning = false;
	private AbstractFileVersioningController fileVersionController = null;

	private boolean activateFileTagFeature = false;
	private AbstractFileTagsController fileTagsController = null;

	private boolean activateFileActionHistory = false;
	private AbstractFileActionHistoryController fileActionHistoryController = null;

	private AbstractFileLinkController fileLinkController = null;

	private BasicConfigurationController config = null;

	private IDocumentOnServerPersistence docPersistence;
	private IFolderOnServerPersistence dirPersistence;

	private ArrayList<FileActionListener> listeners = new ArrayList<FileActionListener>();

	/**
	 * @throws Exception 
	 * 
	 */
	public FileStoreDBHandler() throws Exception {
		this(new BasicConfigurationController());
	}

	/**
	 * Creates a new FileStoreHandler with possibility to overrides the ivy global variables settings for the two given parameters.<br />
	 * The other variables (security activated, database schema and directories table name) will be set with the corresponding global variables.
	 * @param _ivyDBConnectionName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _tableName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @throws Exception 
	 */
	public FileStoreDBHandler(String _ivyDBConnectionName, String _tableName) throws Exception {
		this(_ivyDBConnectionName,_tableName,null,null,null,new BasicConfigurationController().isActivateSecurity());
	}


	/**
	 * Creates a new FileStoreHandler with possibility to overrides the ivy global variables settings for the five given parameters.<br />
	 * The other variable (security activated) will be set with the corresponding global variables.
	 * @param _ivyDBConnectionName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _fileTableName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _fileContentTableName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _dirTableName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _schemaName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @throws Exception
	 */
	public FileStoreDBHandler(String _ivyDBConnectionName, String _fileTableName, String _fileContentTableName, String _dirTableName, 
			String _schemaName) 
					throws Exception {
		this(_ivyDBConnectionName,_fileTableName,_fileContentTableName,_dirTableName,_schemaName,new BasicConfigurationController().isActivateSecurity());
	}

	/**
	 * Creates a new FileStoreHandler with possibility to overrides the ivy global variables settings for the six given parameters.
	 * @param _ivyDBConnectionName: String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _fileTableName:String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _fileContentTableName:String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _dirTableName:String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _schemaName:String, if null or empty, the corresponding ivy global variable will be taken.
	 * @param _securityActivated: Boolean, set the security activation.
	 * @throws Exception
	 */
	public FileStoreDBHandler(String _ivyDBConnectionName, String _fileTableName, String _fileContentTableName, String _dirTableName, 
			String _schemaName, boolean _securityActivated) throws Exception {

		this.config = new BasicConfigurationController();
		this.config.setIvyDBConnectionName(_ivyDBConnectionName);
		this.config.setFilesTableName(_fileTableName);
		this.config.setFilesContentTableName(_fileContentTableName);
		this.config.setDirectoriesTableName(_dirTableName);
		this.config.setDatabaseSchemaName(_schemaName);
		this.config.setActivateSecurity(_securityActivated);
		this.securityActivated = this.config.isActivateSecurity();
		if(this.securityActivated) {
			this.securityController =new SecurityHandlerChain(null,this.config);
			this.config.setSecurityHandler(this.securityController);
		}
		this.docPersistence = PersistenceConnectionManagerFactory.getIDocumentOnServerPersistenceInstance(this.config);
		this.dirPersistence = PersistenceConnectionManagerFactory.getIFolderOnServerPersistenceInstance(this.config);
	}

	/**
	 * Creates a new FileStoreHandler with the given BasicConfigurationController
	 * @param _conf BasicConfigurationController
	 * @throws Exception NullpointerException if the BasicConfigurationController is null
	 */
	public FileStoreDBHandler(BasicConfigurationController _conf) throws Exception {
		initializeAllExceptSecurityController(_conf);

		this.securityActivated = this.config.isActivateSecurity();
		if(this.securityActivated) {
			if(this.config.getSecurityHandler()==null) {
				this.securityController =new SecurityHandlerChain(null,this.config);
				this.config.setSecurityHandler(this.securityController);
			}else {
				this.securityController = this.config.getSecurityHandler();
			}
		}
	}

	/**
	 * <b>This is not a Public API method</b><br>This method may change in the future, use at own risk.<br>
	 * Same behavior as the constructor with the BasicConfigurationController as parameter, 
	 * except that you pass the AbstractDirectorySecurityController by parameter if the security is activated.<br>
	 * This Constructor does not instantiate the directorySecurityController if the given one is null.
	 * If the given one is null, this FileStoreDBHandler will consider that the security is disabled<br>
	 *  even if the BasicConfigurationController parameter has its isActivateSecurity property set to true.
	 * @param _conf
	 * @param directorySecurityController
	 * @throws Exception
	 */
	public FileStoreDBHandler(BasicConfigurationController _conf, AbstractDirectorySecurityController directorySecurityController) throws Exception {
		initializeAllExceptSecurityController(_conf);

		this.securityActivated = this.config.isActivateSecurity() && directorySecurityController!=null;
		this.securityController = directorySecurityController;

		if(this.securityActivated && this.config.getSecurityHandler()==null) {
			this.config.setSecurityHandler((SecurityHandler) this.securityController);
		}
	}

	private void initializeAllExceptSecurityController(
			BasicConfigurationController _conf) throws Exception {
		this.config = _conf;

		this.activateFileTypesFeature=_conf.isActivateFileType();
		if(this.activateFileTypesFeature) {
			this.fileTypesController = FileManagementHandlersFactory.getFileTypesControllerInstance(config);
		}

		this.activateFileVersioning = this.config.isActivateFileVersioning();
		if(this.activateFileVersioning) {
			this.fileVersionController = FileManagementHandlersFactory.getFileVersioningControllerInstance(config);
		}

		this.activateFileTagFeature = this.config.isActivateFileTags();
		if(this.activateFileTagFeature) {
			this.fileTagsController = FileManagementHandlersFactory.getFileTagsControllerInstance(_conf);
		}

		if(_conf.getFileActionListeners()!=null) {
			for(FileActionListener fal : _conf.getFileActionListeners()) {
				this.addFileEventListener(fal);
			}
		}
		if(this.config.isActivateFileLink()) {
			try {
				this.fileLinkController = FileManagementHandlersFactory.getFileLinkControllerInstance(_conf);
				if(this.activateFileVersioning) {
					this.fileVersionController.addFileVersionActionListener(fileLinkController);
				}
			} catch (Exception ex) {
				Ivy.log().debug("Cannot activate the fileLink feature", ex);
			}
		}

		this.activateFileActionHistory = this.config.getFileActionHistoryConfiguration()!=null 
				&& this.config.getFileActionHistoryConfiguration().isActivateFileActionHistory();
		if(this.activateFileActionHistory) {
			this.fileActionHistoryController = FileManagementHandlersFactory.getFileActionHistoryControllerInstance(this.config);
		}

		this.docPersistence = PersistenceConnectionManagerFactory.getIDocumentOnServerPersistenceInstance(this.config);
		this.dirPersistence =  PersistenceConnectionManagerFactory.getIFolderOnServerPersistenceInstance(this.config);
	}

	/**
	 * used for tests only
	 * @param fvc
	 */
	protected FileStoreDBHandler(AbstractFileVersioningController fvc) {
		this.fileVersionController = fvc;
	}

	/**
	 * Not public API, used for tests
	 * @param config
	 * @param docPersistence
	 * @param dirPersistence
	 */
	protected FileStoreDBHandler(BasicConfigurationController config, 
			IDocumentOnServerPersistence docPersistence, 
			IFolderOnServerPersistence dirPersistence) {
		this.config=config;
		this.docPersistence = docPersistence;
		this.dirPersistence = dirPersistence;
	}

	public BasicConfigurationController getConfig() {
		return config;
	}

	public FileTypesController getFileTypesController() {
		return (FileTypesController) this.fileTypesController;
	}

	public FileVersioningController getFileVersioningController() {
		return (FileVersioningController) this.fileVersionController;
	}
	
	public AbstractFileLinkController getFileLinkController() {
		return this.fileLinkController;
	}


	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#changeModificationInformations(java.io.File, java.lang.String)
	 */
	@Override
	public void changeModificationInformations(java.io.File _file, String _userID) throws Exception {
		if(_file == null || !_file.exists() || _userID == null) {
			throw new Exception("File null or doesn't exist, or userID null in changeModificationInformations method.");
		}
		DocumentOnServer doc = this.docPersistence.get(escapeBackSlash(_file.getPath()));
		doc.setModificationDate(new Date().format("dd.MM.yyyy"));
		doc.setModificationTime(new Time().format("HH:mm.ss"));
		doc.setModificationUserID(_userID);
		this.docPersistence.update(doc);
		this.fireChangedEvent(doc, false);
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#createDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public ReturnedMessage createDirectory(String _destinationPath,
			String _newDirectoryName) throws Exception {
		if(_destinationPath==null || _newDirectoryName==null || _newDirectoryName.trim().equals("")) {
			throw new IllegalArgumentException("One of the parameters in "+this.getClass().getName()+", method createDirectory(String destinationPath, String newDirectoryName) is not set.");
		}
		if(!_destinationPath.trim().equals("")){
			_destinationPath=PathUtil.formathPathForDirectoryWithoutFirstSeparatorWithEndSeparator(_destinationPath);
		}
		return this.createDirectory(_destinationPath+_newDirectoryName.trim());
	}

	@Override
	public ReturnedMessage createDirectory(String _newDirectoryPath) throws Exception {
		if( _newDirectoryPath==null || _newDirectoryPath.trim().equals("")) {
			throw new IllegalArgumentException("One of the parameters in "+this.getClass().getName()+", method createDirectory(String _newDirectoryPath) is not set.");
		}
		if(_newDirectoryPath.contains("%")) {
			throw new IllegalArgumentException("The directories name cannot contain a percent sign (%).");
		}
		ReturnedMessage message = new ReturnedMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/information/directoryCreationSuccessPlain")
				, ReturnedMessage.SUCCESS_MESSAGE);

		if(this.directoryExists(_newDirectoryPath.trim())) {//already exists
			return new ReturnedMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/information/directoryToCreateAlreadyExistsPlain"),
					ReturnedMessage.ERROR_MESSAGE);
		}

		if(this.config.isActivateSecurity()) {
			this.createDirectoryWithParentSecurity(_newDirectoryPath);
			return message;
		}
		String parent = PathUtil.getParentDirectoryPath(_newDirectoryPath);
		if(parent!= null && parent.trim().length()>0) {
			//if the parent directory does not exist, we have to create is also.
			if(!this.directoryExists(parent)) {
				this.createDirectory(parent);
			}
		}
		FolderOnServer fos = new FolderOnServer();
		fos.setPath(_newDirectoryPath);
		fos = this.dirPersistence.create(fos);
		return message;
	}

	@Override
	public FolderOnServer moveDirectory(FolderOnServer folderToMove,
			String destinationParentPath) throws DocumentLockException, FileManagementException, Exception {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheArgumentIsNull("The FolderOnServer parameter cannot be null.", folderToMove);
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheStringArgumentIsNullOrEmpty("The FolderOnServer name and path must be valid.", 
				folderToMove.getPath(), folderToMove.getName());

		String destination = destinationParentPath==null || destinationParentPath.trim().isEmpty()?"": 
			PathUtil.formathPathForDirectoryWithoutFirstSeparatorWithEndSeparator(destinationParentPath);
		if(this.directoryExists(destination+folderToMove.getName())) {
			throw new FileManagementException("The directory already exists at the move destination " + destination+folderToMove.getName());
		}

		if(!this.getDocumentsLocked(folderToMove.getPath(), true).isEmpty()) {
			throw new ch.ivyteam.ivy.addons.filemanager.exception.DocumentLockException("At least one document is edited in the directory tree that has to be moved." +
					"This operation is not possible.");
		}
		if(folderToMove.getId() <= 0 ) {
			String path = folderToMove.getPath();
			folderToMove = this.dirPersistence.get(path);
			if(folderToMove == null){
				throw new FileManagementException("The directory with path " + path + " does not exist. It cannot be moved.");
			}
		}
		this.createDirectory(destination);

		destination += folderToMove.getName()+"/";
		java.util.List<FolderOnServer> childrenDirs = this.dirPersistence.getList(folderToMove.getPath(), true);
		java.util.List<DocumentOnServer> docs = this.docPersistence.getList(folderToMove.getPath(), true);
		String pattern = Pattern.quote(folderToMove.getPath()+"/");
		for(FolderOnServer fos: childrenDirs) {
			fos.setPath(fos.getPath().replaceFirst(pattern, destination));
			this.dirPersistence.update(fos);
		}
		for(DocumentOnServer doc: docs) {
			if(doc instanceof FileLink) {
				continue;
			}
			doc.setPath(doc.getPath().replaceFirst(pattern, destination));
			this.docPersistence.update(doc);
		}
		folderToMove.setPath(PathUtil.formatPathForDirectoryWithoutLastSeparator(destination));
		this.dirPersistence.update(folderToMove);
		SystemEventDispatcher.sendSystemEvent(SystemEventNames.DIRECTORY_MOVED, folderToMove);
		return folderToMove;
	}

	/**
	 * 
	 * @param _path
	 * @return
	 * @throws Exception
	 */
	public boolean directoryExists(String _path) throws Exception {
		_path=PathUtil.formatPathForDirectoryWithoutLastAndFirstSeparator(_path);
		if(_path==null || _path.length()==0) {
			return false;
		}
		return this.dirPersistence.get(_path)!=null;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#deleteDirectory(java.lang.String)
	 */
	@Override
	public ReturnedMessage deleteDirectory(String _directoryPath)
			throws Exception {
		if(_directoryPath==null || _directoryPath.trim().equals("")) {
			throw new IllegalArgumentException("The 'directoryPath' parameter in "+this.getClass().getName()+", method deleteDirectory(String directoryPath) is not set.");
		}

		_directoryPath=PathUtil.formatPathForDirectoryWithoutLastSeparator(_directoryPath.trim());
		if(this.config.isActivateSecurity()) {
			//Check if user allowed to delete a directory
			java.util.List<FolderOnServer> lFos = this.dirPersistence.getList(_directoryPath, true);
			lFos.add(this.dirPersistence.get(_directoryPath));
			IUser user = Ivy.session().getSessionUser();
			for(FolderOnServer fos:lFos) {
				if(!this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.DELETE_DIRECTORY_RIGHT, 
						fos, user, null).isAllow()){
					return new ReturnedMessage("The user '"+user+"' doesn't have the right to delete the directory " + fos.getPath(), ReturnedMessage.ERROR_MESSAGE);
				}
				if(!this.docPersistence.getList(fos.getPath(), false).isEmpty() && 
						!this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.DELETE_FILES_RIGHT, 
								fos, user,null).isAllow()) {
					return new ReturnedMessage("The user '"+user+"' doesn't have the right to delete the files present under " + fos.getPath(), ReturnedMessage.ERROR_MESSAGE);
				}
			}
		}

		return this.deleteDirectoryAsAdministrator(_directoryPath);
	}

	@Override
	public ReturnedMessage deleteDirectoryAsAdministrator(String _directoryPath)
			throws Exception {
		if(_directoryPath==null || _directoryPath.trim().equals("")) {
			throw new IllegalArgumentException("The 'directoryPath' parameter in "+this.getClass().getName()+", method deleteDirectory(String directoryPath) is not set.");
		}
		_directoryPath=PathUtil.formatPathForDirectoryWithoutLastSeparator(_directoryPath.trim());
		//Check if the directory exists, if not return
		if(!this.directoryExists(_directoryPath)) {
			return new ReturnedMessage("The directory to delete does not exist.", ReturnedMessage.ERROR_MESSAGE);
		}
		ReturnedMessage message = this.deleteAllFilesUnderDirectory(_directoryPath);
		if(message.getType()!=FileHandler.SUCCESS_MESSAGE) {
			return message;
		}

		java.util.List<FolderOnServer> lFos = this.dirPersistence.getList(_directoryPath, true);
		for(FolderOnServer fos:lFos) {
			this.dirPersistence.delete(fos);
		}
		FolderOnServer fos = this.dirPersistence.get(_directoryPath);
		this.dirPersistence.delete(fos);
		return message;
	}


	/**
	 * Deletes the actual content of the document and makes the last version as the active document.<br><br>
	 * <b>Warnings: </b>
	 * <ul><li>Only available if the file versioning feature is activated.<br>
	 * If the file versioning feature is not activated, the ReturnedMessage object
	 * has the FileHandler.ERROR_MESSAGE type and nothing is done. Its text field contains an appropriate error message.<br>
	 * <li>If the given document has no version (its version number is 1), this method will have no effect.<br>
	 * In that case the ReturnedMessage object has the FileHandler.INFORMATION_MESSAGE type. Its text field contains an appropriate information message.
	 * <li>If the given document is locked, it mights be edited by somebody. In that case nothing will be done and an appropriate ERROR_MESSAGE will be returned.
	 * </ul>
	 * @param doc the DocumentOnServer object. At least it must have a valid fileId. If not an IllegalArgumentException will be thrown. 
	 * @return ReturnedMessage reflecting the status of the operation. Its type is FileHandler.SUCCESS_MESSAGE if the operation was successful.
	 * @throws Exception if any SQL Exceptions are thrown.
	 * @throws NumberFormatException  if the given DocumentOnServer is null or does not have a valid fileId.
	 */
	public ReturnedMessage rollBackToPreviousVersion(DocumentOnServer doc) throws NumberFormatException, Exception {

		if(doc==null || doc.getFileID().trim().equals("")) {
			throw new IllegalArgumentException("The 'document' parameter in "+this.getClass().getName()+", method rollBackToPreviousVersion(DocumentOnServer document) is not valid.");
		}
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		if(!this.activateFileVersioning) {
			message.setText("The File Versioning Feature is not activated. This method can not be used.");
			message.setType(FileHandler.ERROR_MESSAGE); 
			return message;
		}
		long id = Long.parseLong(doc.getFileID());
		if(doc.getVersionnumber().intValue()<=0) {//the doc just contains the file id

			doc= this.getDocumentOnServerById(id, false);
			if(doc==null || doc.getFileID().trim().length()==0) {
				message.setText("The Document with fileId "+id+" does not exist.");
				message.setType(FileHandler.ERROR_MESSAGE); 
				return message;
			}
		}
		if(!doc.getLocked().equals("0")) {
			message.setText("The Document is actually locked by "+doc.getLockingUserID()+". This operation can not be performed.");
			message.setType(FileHandler.ERROR_MESSAGE); 
			return message;
		}
		if(doc.getVersionnumber().intValue()==1) {
			message.setType(FileHandler.INFORMATION_MESSAGE);
			message.setText("The document contains no previous version to rollback.");
			return message;
		}
		boolean rolledback = false;
		try{
			rolledback = this.fileVersionController.rollbackLastVersionAsActiveDocument(id);
		}catch(FileVersionProtectedException ex) {
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText(String.format(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/version/FileDeleteAbordedBecauseArchiveProtection"), doc.getVersionnumber(), doc.getFilename()));
			return message;
		}
		if(rolledback) {

			if(this.activateFileActionHistory && this.fileActionHistoryController.getConfig().isDeleteFileTracked()) {
				this.fileActionHistoryController.createNewActionHistory(Long.parseLong(doc.getFileID()), FileActionType.FILE_VERSION_ROLLBACK_ACTION,
						Ivy.session().getSessionUserName(), "Version "+doc.getVersionnumber().intValue());
			}
			DocumentOnServer rolledbackDoc = this.docPersistence.get(id);
//			if(this.fileLinkController != null && this.config.isLinkToVersion()) {
//				this.fileLinkController.updateFileLinksVersionId(id, rolledbackDoc.getVersionnumber().intValue());
//			}
			this.fireRollBackVersionEvent(rolledbackDoc);

			message.setType(FileHandler.SUCCESS_MESSAGE);
			message.setText("The document was successfully rolledback to its previous version.");
			return message;
		} else {
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText("The document could not be rolledback to its previous version.");
			return message;
		}
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getDirectoryTree(java.lang.String)
	 */
	@Override
	public Tree getDirectoryTree(String rootPath) throws Exception {
		Tree RDTree = new Tree();
		if(rootPath==null || rootPath.trim().equals(""))
		{
			throw new Exception("");
		}
		rootPath = PathUtil.formatPathForDirectoryWithoutLastAndFirstSeparator(rootPath);
		if(!directoryExists(rootPath))
		{//create the directory
			String parentPath="";
			if(rootPath.lastIndexOf("/")>1)
			{
				parentPath = rootPath.substring(0,rootPath.lastIndexOf("/"));
				String dirname = rootPath.substring(rootPath.lastIndexOf("/")+1, rootPath.length());
				this.createDirectory(parentPath, dirname);
			}else{
				this.createDirectory("", rootPath);
			}

		}
		ArrayList<FolderOnServer> l;
		l = this.getListDirectoriesUnderPath(rootPath);

		if(!l.isEmpty()){
			RDTree.setValue(l.get(0));
			RDTree.setInfo(l.get(0).getName());
			l.remove(0);
			fillRDTree(RDTree, l);
		}
		return RDTree;
	}

	/**
	 * For internal use. Recursive method to fill a Directory Tree.<br>
	 * Used in public Tree getDirectoryTree(String rootPath)
	 * @param tree The tree to fill. If the Tree represents a locked Folder, It will not display its children nodes.
	 * @param dirs The list of Children FolderOnServer nodes
	 */
	private void fillRDTree(Tree tree, ArrayList<FolderOnServer> dirs){
		if(dirs==null || dirs.isEmpty() || tree ==null)
		{
			return;
		}
		if(!(tree.getValue() instanceof FolderOnServer)){
			return;
		}

		FolderOnServer fos = (FolderOnServer) tree.getValue();
		ArrayList<FolderOnServer> children = getDirectChildFoldersInList(fos, dirs);
		for(FolderOnServer f:children){
			tree.createChild(f, f.getName());
			if(!(f instanceof LockedFolder)){
				fillRDTree(tree.getLastChild(),dirs);
			}
		}
	}

	/**
	 * Finds out all the direct children from a parent directory in a list of given directories.<br>
	 * @param parent the parent directory (FolderOnServer object)
	 * @param dirs the potential FolderOnServer children in an ArrayList<FolderOnServer>
	 * @return the ArrayList<FolderOnServer> filled with all the direct children of the parent
	 */
	public ArrayList<FolderOnServer> getDirectChildFoldersInList(FolderOnServer parent, ArrayList<FolderOnServer> dirs){
		ArrayList<FolderOnServer> l = new ArrayList<FolderOnServer>();
		if(parent==null || parent.getPath()==null || parent.getPath().trim().length()==0 || dirs==null || dirs.isEmpty()){
			return l;
		}
		String path = parent.getPath();
		for(FolderOnServer f:dirs) {
			if(f.getPath().startsWith(path) && f.getPath().lastIndexOf("/")==path.length()) {
				l.add(f);
			}
		}
		return l;
	}

	/**
	 * Returns all the directories present recursively under the given path. <br> 
	 * The directory represented by the given path is also included in this list.
	 * @param rootPath
	 * @return
	 */
	public ArrayList<FolderOnServer> getListDirectoriesUnderPath(String rootPath) throws Exception {

		if(this.config.isActivateSecurity()) {
			return this.getListDirectoriesUnderPathWithSecurityInfos(rootPath, Ivy.session().getSessionUserName());
		}else {
			ArrayList<FolderOnServer> al = new ArrayList<FolderOnServer>();
			FolderOnServer fos = this.dirPersistence.get(rootPath);
			if(fos!=null) {
				java.util.List<FolderOnServer> l = this.dirPersistence.getList(rootPath, true);
				al.add(fos);
				al.addAll(l);
			}
			return al;
		}

	}

	/**
	 * 
	 */
	public ArrayList<FolderOnServer> getListDirectDirectoriesUnderPath(String rootPath) throws Exception {
		ArrayList<FolderOnServer> dirs = (ArrayList<FolderOnServer>) this.dirPersistence.getList(rootPath, false);
		if(this.config.isActivateSecurity()) {
			for(FolderOnServer fos:dirs) {
				this.getUserRightsOnFolderOnServer(fos, Ivy.session().getSessionUser());
			}
		}
		return dirs;

	}




	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getDocumentOnServer(java.lang.String)
	 */
	@Override
	public DocumentOnServer getDocumentOnServer(String filePath)
			throws Exception {
		return this.docPersistence.get(filePath);
	}

	@Override
	public boolean fileExists(String filePath) throws Exception {
		if(this.docPersistence.get(filePath)!=null) {
			return true;
		}
		if(this.fileLinkController != null) {
			return this.fileLinkController.getFileLink(filePath) != null;
		}
		return false;
	}

	/**
	 * return the id of the DocumentOnServer denoted by the given path
	 * @param _path: the path of the document
	 * @return the id of the document or -1 if not found
	 */
	public int getDocIdWithPath(String _path) throws Exception {
		DocumentOnServer doc = this.docPersistence.get(_path);
		return doc==null? -1 : Integer.parseInt(doc.getFileID());
	}



	/**
	 * Deprecated: use the getFileIdsAsLongUnderPath instead.
	 * returns an array of all the file ids contained under a specifically path
	 * @param _path
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public int[] getFileIdsUnderPath(String _path) throws Exception {
		int[] i =null;
		if(_path==null || _path.trim().equals(""))
		{
			return i;
		}
		java.util.List<DocumentOnServer> docs = this.docPersistence.getList(_path, false);
		i= new int[docs.size()];
		int j=0;
		for(DocumentOnServer doc:docs) {
			try{
				i[j]=Integer.parseInt(doc.getFileID());
			}catch(Exception ex)
			{
				i[j]=0;
			}
			j++;
		}
		return i;
	}

	/**
	 * returns an array of all the file ids contained under a specifically path as long[]
	 * @param _path the path under the files are stored. if Null or empty, the method returns null
	 * @return
	 * @throws Exception
	 */
	public long[] getFileIdsAsLongUnderPath(String _path) throws Exception {
		long[] i =null;
		if(_path==null || _path.trim().equals("")) {
			return i;
		}
		java.util.List<DocumentOnServer> docs = this.docPersistence.getList(_path, false);
		i= new long[docs.size()];
		int j=0;
		for(DocumentOnServer doc:docs) {
			try {
				i[j]=Long.decode(doc.getFileID());
			} catch(Exception ex) {
				i[j]=0;
			}
			j++;
		}
		return i;
	}

	@Override
	public boolean documentOnServerExists(DocumentOnServer document, String path)
			throws Exception {
		if(document== null || document.getFilename()==null || document.getFilename().trim().equals("") || path==null || path.trim().equals(""))
		{
			return false;
		}
		path=PathUtil.formathPathForDirectoryWithoutFirstSeparatorWithEndSeparator(path);
		if(document instanceof FileLink) {
			return this.fileExists(path.concat(((FileLink) document).getFileLinkName()));
		}
		return this.fileExists(path.concat(document.getFilename()));
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getDocumentOnServerWithJavaFile(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer)
	 */
	@Override
	public DocumentOnServer getDocumentOnServerWithJavaFile(DocumentOnServer docu)
			throws Exception {
		if(docu instanceof FileLink) {
			FileLink fl = (FileLink) docu;
			Ivy.log().info("Getting the FileLink java File in FileStoreDBHandler");
			return this.fileLinkController.getFileLinkWithJavaFile(fl.getFileLinkId());
		}
		this.docPersistence.setGivenDocumentOnServerJavaFile(docu);
		return docu;
	}



	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getDocumentOnServerWithJavaFile(java.lang.String)
	 */
	@Override
	public DocumentOnServer getDocumentOnServerWithJavaFile(String filePath)
			throws Exception {
		return this.docPersistence.getDocumentOnServerWithJavaFile(filePath);
	}

	@Override
	public DocumentOnServer getDocumentOnServerWithJavaFile(long fileid)
			throws Exception {
		return this.docPersistence.getDocumentOnServerWithJavaFile(fileid);
	}

	@Override
	public DocumentOnServer getDocumentOnServerById(long fileid, boolean getJavaFile) throws Exception {
		if(getJavaFile){
			return this.docPersistence.getDocumentOnServerWithJavaFile(fileid);
		}else{
			return this.docPersistence.get(fileid);
		}
	}


	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getDocumentOnServersInDirectory(java.lang.String, boolean)
	 */
	@Override
	public List<DocumentOnServer> getDocumentOnServersInDirectory(
			String _path, boolean _isrecursive)
					throws Exception {
		List<DocumentOnServer> l = List.create(DocumentOnServer.class);
		List<DocumentOnServer> al = List.create(DocumentOnServer.class);
		l.addAll(DocumentListFilterUtil.filterDocumentListIfFilterNotNull(this.docPersistence.getList(_path, _isrecursive), this.config.getDocumentListFilter()));

		java.util.List<FolderOnServer> lFos = new ArrayList<FolderOnServer>();
		IUser u = null;
		if(_isrecursive && this.securityActivated) {
			lFos.addAll(this.getListDirectoriesUnderPath(_path));
			u = Ivy.session().getSessionUser();
			for(DocumentOnServer doc:l) {
				String documentParentPath = (doc instanceof FileLink)?  
						PathUtil.getParentDirectoryPath(((FileLink) doc).getFileLinkPath()) : 
							PathUtil.getParentDirectoryPath(doc.getPath());
				for (FolderOnServer fos: lFos) {
					if(fos.getPath().equals(documentParentPath)) {
						if(this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.OPEN_DIRECTORY_RIGHT, fos, u, null).isAllow()) {
							doc.setCanUserDelete(this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.DELETE_FILES_RIGHT, fos, u, null).isAllow());
							doc.setCanUserRead(true);
							doc.setCanUserWrite(this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.UPDATE_FILES_RIGHT, fos, u, null).isAllow());
							al.add(doc);
						}
						break;
					}
				}
			}
		}else {
			al.addAll(l);
		}
		return al;
	}

	/**
	 * for internal private use only. build Documents with a RecordList.
	 * @param recordList
	 * @return
	 */


	public List<FolderOnServer> getDirsUserCanOpenUnderTree(Tree dirs, List<FolderOnServer> folders)
	{
		if(folders==null)
		{
			folders = List.create(FolderOnServer.class);
		}
		FolderOnServer fos = (FolderOnServer) dirs.getValue();
		if(fos.getCanUserOpenDir())
		{
			folders.add(fos);
			if(dirs.getChildCount()>0)
			{
				List<Tree> d = dirs.getChildren();
				for(Tree t:d)
				{
					getDirsUserCanOpenUnderTree(t,folders);
				}
			}
		}
		return folders;
	}

	/**
	 * 
	 * @param dirs
	 * @param paths
	 * @return
	 */
	public List<String> getDirPathsUserCanOpenInTree(Tree dirs, List<String> paths)
	{
		if(paths==null)
		{
			paths = List.create(String.class);
		}
		FolderOnServer fos = (FolderOnServer) dirs.getValue();
		if(fos.getCanUserOpenDir())
		{
			paths.add(fos.getPath());
			if(dirs.getChildCount()>0)
			{
				List<Tree> d = dirs.getChildren();
				for(Tree t:d)
				{
					getDirPathsUserCanOpenInTree(t,paths);
				}
			}
		}
		return paths;

	}

	/**
	 * Returns all the DocumentOnServer stores in the FileManager table of the Ivy System DB.<br>
	 * The SQL query is built with the given list of 'AND' conditions.<br>
	 * If the list is null or empty, it will retrieve all the DocumentOnServer from the DB.<br>
	 * Each condition should be written like : "FileId > 1000", "FileName NOT LIKE 'test.doc'", "FileName LIKE 'test.doc'"...<br>
	 * @param _conditions: List<String> representing the conditions to perform the search in the DB
	 * @return an ArrayList of {@link DocumentOnServer} Objects.<br>
	 * Each DocumentOnServer object represents a File with several informations (name, path, size, CreationDate, creationUser...)
	 * @throws Exception
	 */
	public ArrayList<DocumentOnServer> getDocuments(List<String> _conditions)throws Exception {
		ArrayList<DocumentOnServer> docs = new ArrayList<>();
		docs.addAll(DocumentListFilterUtil.filterDocumentListIfFilterNotNull(this.docPersistence.getDocuments(_conditions), this.config.getDocumentListFilter()));
		return docs;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getDocumentsInPath(java.lang.String, boolean)
	 */
	@Override
	public ArrayList<DocumentOnServer> getDocumentsInPath(String _path, boolean _isRecursive) throws Exception {
		ArrayList<DocumentOnServer> al = new ArrayList<DocumentOnServer>();
		al.addAll(this.getDocumentOnServersInDirectory(_path, _isRecursive));
		return al;
	}

	/**
	 * return all the Locked documents under a given path
	 * @param _path: the path where to look for the Locked files
	 * @param _isRecursive: if is recursive, look in all the sub directories under the path
	 * @return the list of DocumentOnServer as ArrayList<DocumentOnServer> that are Locked
	 * @throws Exception
	 */
	public ArrayList<DocumentOnServer> getDocumentsLocked(String _path, boolean _isrecursive) throws Exception{
		if(_path==null || _path.trim().length()==0) {
			throw new Exception("Invalid path in getDocumentsInPath method");
		}
		ArrayList<DocumentOnServer>  al = new ArrayList<DocumentOnServer>();
		java.util.List<DocumentOnServer> docs = this.docPersistence.getList(_path, _isrecursive);
		for(DocumentOnServer doc:docs) {
			if(doc.getIsLocked()) {
				al.add(doc);
			}
		}
		return al;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getDocumentsWithJavaFileInPath(java.lang.String, boolean)
	 */
	@Override
	public ArrayList<DocumentOnServer> getDocumentsWithJavaFileInPath(String _path, boolean _isRecursive) throws Exception {
		if(_path==null || _path.trim().length()==0) {
			throw new Exception("Invalid path in getDocumentsInPath method");
		}

		ArrayList<DocumentOnServer>  al = new ArrayList<DocumentOnServer>();
		java.util.List<DocumentOnServer> docs = DocumentListFilterUtil.filterDocumentListIfFilterNotNull(this.docPersistence.getList(_path, _isRecursive),
				this.config.getDocumentListFilter());
		java.util.List<FolderOnServer> l = null;
		IUser u = null;
		//If the security is activated we have to prepare the security check
		if(this.securityActivated) {
			l = this.dirPersistence.getList(_path, _isRecursive);
			l.add(0, this.dirPersistence.get(_path));
			u = Ivy.session().getSessionUser();
		}

		for(DocumentOnServer doc:docs) {
			this.docPersistence.setGivenDocumentOnServerJavaFile(doc);
			if(this.securityActivated) {
				for (FolderOnServer fos: l) {
					if(fos.getPath().equals(PathUtil.getParentDirectoryPath(doc.getPath()))) {
						if(this.securityController.hasRight(null, SecurityRightsEnum.OPEN_DIRECTORY_RIGHT, fos, u, null).isAllow()) {
							doc.setCanUserDelete(this.securityController.hasRight(null, SecurityRightsEnum.DELETE_FILES_RIGHT, fos, u, null).isAllow());
							doc.setCanUserRead(true);
							doc.setCanUserWrite(this.securityController.hasRight(null, SecurityRightsEnum.UPDATE_FILES_RIGHT, fos, u, null).isAllow());
							al.add(doc);
						}
					}
				}
			}
		}
		if(!this.securityActivated) {
			al.addAll(docs);
		}

		return al;
	}





	/**
	 * get the Class Object of the current AbstractFileManagementHandler implementation Class
	 * @return the class Object of the current AbstractFileManagementHandler implementation Class
	 */
	public Class<? extends AbstractFileManagementHandler> getFileManagementHandlerClass() {
		return this.getClass();
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#getFileStorageType()
	 */
	@Override
	public int getFileStorageType() {
		return AbstractFileManagementHandler.FILE_STORAGE_DATABASE;
	}

	/**
	 * Insert a List of DocumentOnServer Objects into DB<br>
	 * It checks if the documents already exits, if so, it delete them first.
	 * To be able to store the content of the Files in the DB, this method works as following:<br>
	 * - if the given DocumentOnServer object contains a valid Ivy File Object, then its content will be stored in the DB,<br>
	 * - else if the given DocumentOnServer object contains a valid java.io.File Object, then its content will be stored in the DB,<br>
	 * - else it will use the path of the DocumentOnServer object, to retrieve the java.io.File<br>
	 * - if no content can be found, it will ignore the documentOnServer Object and will treat the following one.
	 * @param _documents: the List<DocumentOnServer> to insert in the DB
	 * @return the number of inserted documents
	 * @throws Exception
	 */
	public int insertDocuments(List<DocumentOnServer> _documents)
			throws Exception {
		int insertedIDs = -1;
		if(_documents==null || _documents.size()==0)
			return 0;
		boolean doHist = this.activateFileActionHistory && this.fileActionHistoryController.getConfig().isFileCreationTracked();
		for(DocumentOnServer doc : _documents) {
			this.docPersistence.delete(doc);
			this.docPersistence.create(doc);
			if(doHist) {
				this.fileActionHistoryController.createNewActionHistory(Long.parseLong(doc.getFileID()), 
						FileActionType.FILE_CREATED_ACTION, doc.getUserID(), "");
			}
			this.fireCreatedEvent(doc);
		}
		return insertedIDs;
	}




	/*
	 * This method is now concurrent processes compatible.
	 * See Issue #28265, database deadlock in some of the FileManagement methods used concurrently in a lot of different processes.<br>
	 * (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#insertFiles(ch.ivyteam.ivy.scripting.objects.List, java.lang.String, java.lang.String)
	 */
	@Deprecated
	@Override
	public int insertFile(java.io.File _file, String _destinationPath)throws Exception {
		if(_file== null || _destinationPath == null) 
			return 0;

		return this.insertFile(_file, _destinationPath, "");
	}


	/*
	 * This method is now concurrent processes compatible.
	 * See Issue #28265, database deadlock in some of the FileManagement methods used concurrently in a lot of different processes.<br>
	 * (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#insertFiles(ch.ivyteam.ivy.scripting.objects.List, java.lang.String, java.lang.String)
	 */
	@Override
	public int insertFile(java.io.File _file, String _destinationPath, String _user)throws Exception {
		assert (_file != null && _destinationPath!=null):"";

		if(!_file.exists())
		{
			throw new FileNotFoundException("The file that should be inserted does not exist.");
		}
		_destinationPath=PathUtil.formathPathForDirectoryWithoutFirstSeparatorWithEndSeparator(_destinationPath);
		if(!this.directoryExists(_destinationPath)){
			this.createDirectory(_destinationPath);
		}
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFilename(_file.getName());
		doc.setExtension(FileHandler.getFileExtension(_file.getName()));
		doc.setPath(_destinationPath+_file.getName());
		doc.setUserID(_user);
		doc.setModificationUserID(_user);
		doc.setFileSize(FileHandler.getFileSize(_file));
		doc.setJavaFile(_file);
		this.docPersistence.delete(doc);
		doc = this.docPersistence.create(doc);
		if(this.activateFileActionHistory && this.fileActionHistoryController.getConfig().isFileCreationTracked()) {
			this.fileActionHistoryController.createNewActionHistory(Long.parseLong(doc.getFileID()), 
					FileActionType.FILE_CREATED_ACTION, doc.getUserID(), "");
		}
		this.fireCreatedEvent(doc);
		return Integer.parseInt(doc.getFileID());
	}

	/*
	 * This method is now concurrent processes compatible.
	 * See Issue #28265, database deadlock in some of the FileManagement methods used concurrently in a lot of different processes.<br>
	 * (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#insertFiles(ch.ivyteam.ivy.scripting.objects.List, java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	public int insertFiles(List<java.io.File> _files, String _user) throws Exception{
		if(_files==null || _files.size()==0)
			return 0;
		int insertedIDs = 0;
		for(java.io.File f : _files) {
			this.insertFile(f, f.getPath(), _user);
			insertedIDs++;
		}
		return insertedIDs;
	}

	/*
	 * This method is now concurrent processes compatible.
	 * See Issue #28265, database deadlock in some of the FileManagement methods used concurrently in a lot of different processes.<br>
	 * (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#insertFiles(ch.ivyteam.ivy.scripting.objects.List, java.lang.String, java.lang.String)
	 */
	@Override
	public int insertFiles(List<java.io.File> _files, String _destinationPath, String _user) throws Exception{
		int insertedIDs = -1;
		if(_files==null || _files.size()==0)
			return 0;
		for(java.io.File f : _files) {
			this.insertFile(f,  _destinationPath, _user);
			insertedIDs++;
		}
		return insertedIDs;
	}

	/**
	 * Insert a  DocumentOnServer Object into the DB storing System.<br>
	 * To be able to store the content of the File in the DB, this method works as following:<br>
	 * - if the given DocumentOnServer object contains a valid Ivy File Object, then its content will be stored in the DB,<br>
	 * - else if the given DocumentOnServer object contains a valid java.io.File Object, then its content will be stored in the DB,<br>
	 * - else it will use the path of the DocumentOnServer object, to retrieve the java.io.File<br>
	 * - if no content can be found, it will throw an Exception.
	 * @param _document: DocumentOnServer that has to be inserted into the File storing system
	 * @return 1 if successful
	 * @throws Exception 
	 */
	public int insertOneDocument(DocumentOnServer _document)throws Exception {
		if(_document == null) {
			return 0;
		}
		this.docPersistence.delete(_document);
		java.io.File dir = new java.io.File(_document.getPath()).getParentFile();
		if(!this.directoryExists(dir.getPath())) {
			this.createDirectory(dir.getPath());
		}
		DocumentOnServer doc = this.docPersistence.create(_document);

		if(this.activateFileTypesFeature && doc.getFileType() !=null && doc.getFileType().getId() != null && doc.getFileType().getId()>0) {
			fileTypesController.setDocumentFileType(doc, doc.getFileType().getId());
		}
		if(this.activateFileActionHistory && this.fileActionHistoryController.getConfig().isFileCreationTracked()) {
			this.fileActionHistoryController.createNewActionHistory(Long.parseLong(doc.getFileID()), 
					FileActionType.FILE_CREATED_ACTION, doc.getUserID(), "");
		}
		this.fireCreatedEvent(doc);
		return 1;
	}

	/**
	 * Private use only.
	 * Insert a  DocumentOnServer Object into the DB storing System.<br>
	 * To be able to store the content of the File in the DB, this method works as following:<br>
	 * - if the given DocumentOnServer object contains a valid Ivy File Object, then its content will be stored in the DB,<br>
	 * - else if the given DocumentOnServer object contains a valid java.io.File Object, then its content will be stored in the DB,<br>
	 * - else it will use the path of the DocumentOnServer object, to retrieve the java.io.File<br>
	 * - if no content can be found, it will throw an Exception.
	 * @param _document: DocumentOnServer that has to be inserted into the File storing system
	 * @return 1 if successful
	 * @throws Exception 
	 */
	private int insertOneDocumentWithoutHistory(DocumentOnServer _document)throws Exception {
		if(_document== null) 
			return 0;
		this.docPersistence.delete(_document);
		java.io.File dir = new java.io.File(_document.getPath()).getParentFile();
		if(!this.directoryExists(dir.getPath())){
			this.createDirectory(dir.getPath());
		}
		DocumentOnServer doc = this.docPersistence.create(_document);

		if(this.activateFileTypesFeature && doc.getFileType() !=null && doc.getFileType().getId() != null && doc.getFileType().getId()>0)
		{
			fileTypesController.setDocumentFileType(doc, doc.getFileType().getId());
		}
		this.fireCreatedEvent(doc);

		return 1;
	}

	/**
	 * Looks if a DocumentOnServer is actually Locked by a user<br>
	 * If you don't have a locking System for File edition, just override this method with no action in it and return always false.
	 * @param _doc: the DocumentOnServer to check
	 * @return true if the DocumentOnServer is Locked, else false
	 * @throws Exception
	 */
	public boolean isDocumentOnServerLocked(DocumentOnServer _doc)throws Exception{
		if(_doc == null || _doc.getPath()==null || _doc.getPath().trim().length()==0) {
			throw new IllegalArgumentException("Invalid DocumentOnServer Object or invalid username in isDocumentOnServerLocked method.");
		}
		DocumentOnServer doc = this.docPersistence.get(PathUtil.formatPath(_doc.getPath()));
		return (doc.getIsLocked());
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#isDocumentOnServerLocked(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer, java.lang.String)
	 */
	@Override
	public boolean isDocumentOnServerLocked(DocumentOnServer _doc, String _user)
			throws Exception {
		if(_doc == null || _doc.getPath()==null || _doc.getPath().trim().length()==0 || _user == null || _user.trim().length()==0) {
			throw new IllegalArgumentException("Invalid DocumentOnServer Object or invalid username in isDocumentOnServerLocked method.");
		}
		DocumentOnServer doc = this.docPersistence.get(PathUtil.formatPath(_doc.getPath()));
		return (doc.getIsLocked() && !doc.getLockingUserID().equals(_user));
	}

	/**
	 * get the Ivy DB connection name
	 * @return the Ivy DB connection name as String
	 */
	public String getIvyDBConnectionName() {
		return config.getIvyDBConnectionName();
	}

	/**
	 * set the Ivy DB connection name
	 * @param _ivyDBConnectionName as String
	 * @throws Exception 
	 */
	public void setIvyDBConnectionName(String _ivyDBConnectionName) throws Exception {
		if(_ivyDBConnectionName==null || _ivyDBConnectionName.trim().length()==0)
		{
			return;
		}
		this.config.setIvyDBConnectionName(_ivyDBConnectionName);
		this.docPersistence = PersistenceConnectionManagerFactory.getIDocumentOnServerPersistenceInstance(config);
	}

	/**
	 * get the Database Table name used to store the properties of the files
	 * @return the Database table name
	 */
	public String getTableName() {
		return this.config.getFilesTableName();
	}

	/**
	 * set the Database table name used to store the properties of the files
	 * @param _tableName as String
	 * @throws Exception 
	 */
	public void setTableName(String _tableName) throws Exception {
		if(_tableName== null || _tableName.trim().length()==0)
		{
			return;
		}
		this.config.setFilesTableName(_tableName);
		this.docPersistence = PersistenceConnectionManagerFactory.getIDocumentOnServerPersistenceInstance(this.config);
	}

	/**
	 * returns the tableNameSpace in the form of schema.table if the schemaName is set, else just tableName.
	 * @return
	 */
	public String getTableNameSpace() {
		return (this.config.getDatabaseSchemaName()!=null && this.config.getDatabaseSchemaName().trim().length()>0)?
				this.config.getDatabaseSchemaName()+"."+this.config.getFilesTableName():this.config.getFilesTableName();
	}

	/**
	 * Look if a File is actually Locked by a user<br>
	 * If you don't have a locking System for File edition, just override this method with no action in it and return always false.
	 * @param _file: the java.io.File to check
	 * @return true if the file is Locked, else false
	 * @throws Exception
	 */
	public boolean isFileLocked(java.io.File _file)throws Exception{
		if(_file == null) {
			throw new IllegalArgumentException("Invalid file Object in isFileLocked method.");
		}
		return this.docPersistence.get(PathUtil.escapeBackSlash(_file.getPath())).getIsLocked();
	}

	/**
	 * Look if a File is actually Locked by a user that is not the given user<br>
	 * If you don't have a locking System for File edition, just override this method with no action in it and return always false. 
	 * @param _file the java.io.File to check
	 * @param _user the user who has not to lock the file
	 * @return true if the file is Locked by another user as the given one.
	 * @throws Exception
	 */
	public boolean isFileLockedByAnotherUser(java.io.File _file, String _user)throws Exception{
		if(_file==null || _user ==null || _user.trim().equals("")) {
			return false;
		}
		DocumentOnServer doc = this.docPersistence.get(PathUtil.escapeBackSlash(_file.getPath()));
		return (doc.getIsLocked() && doc.getLockingUserID().equals(_user));
	}

	/**
	 * Lock a document in the DB if not already Locked by another user
	 * @param _doc: the DocumentOnServer Object that has to be Locked
	 * @param _userIn the user who locks this document
	 * @return true if the document was Locked, else false
	 * @throws Exception
	 */
	public boolean lockDocument(DocumentOnServer doc, String _userIn) throws Exception{
		if(doc == null || _userIn == null || _userIn.trim().length()==0) {
			throw new IllegalArgumentException("Invalid DocumentOnServer Object or invalid username in lockDocument method.");
		}
		DocumentOnServer docRef = this.docPersistence.get(PathUtil.escapeBackSlash(doc.getPath()));
		if(docRef==null || (docRef.getIsLocked() && !docRef.getLockingUserID().equals(_userIn))) {
			return false;
		}
		if(docRef.getIsLocked() && !docRef.getLockingUserID().equals(_userIn)) {
			return true;
		}

		if(doc.getFileID()==null || doc.getFileID().trim().length()==0) {
			doc = docRef;
		}

		doc.setIsLocked(true);
		doc.setLocked("1");
		doc.setLockingUserID(_userIn);
		return this.docPersistence.update(doc).getLocked().equals("1");
	}

	/**
	 * Lock a File in the DB if not already Locked by another user.<br>
	 * The File here just holds the informations stored in the DB (path, etc...). The File does not exit on the FileSystem.
	 * @param _file: the java.io.File to lock
	 * @param _userIn the user who locks this document
	 * @return true if the document was Locked, else false
	 * @throws Exception
	 */
	public boolean lockFile(java.io.File _file, String _userIn) throws Exception{
		if(_file == null || _userIn == null || _userIn.trim().length()==0) {
			throw new IllegalArgumentException("Invalid file Object or invalid username in lockFile method.");
		}
		boolean flag = false;	
		DocumentOnServer doc = this.docPersistence.get(PathUtil.escapeBackSlash(_file.getPath()));
		if(doc!=null && !doc.getIsLocked()) {
			doc.setIsLocked(true);
			doc.setLocked("1");
			doc.setLockingUserID(_userIn);
			this.docPersistence.setGivenDocumentOnServerJavaFile(doc);
			flag = true;
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#pasteCopiedDocumentOnServers(ch.ivyteam.ivy.scripting.objects.List, java.lang.String)
	 */
	@Override
	public ReturnedMessage pasteCopiedDocumentOnServers(
			List<DocumentOnServer> documents, String fileDestinationPath) throws Exception {
		ReturnedMessage message = new ReturnedMessage();

		if(documents==null || fileDestinationPath==null || fileDestinationPath.trim().equals("")) {
			return new ReturnedMessage("Unable to paste the given DocumentOnServer objects. " +
					"One of the parameter is invalid " +
					"in pasteCopiedDocumentOnServers(List<DocumentOnServer> documents, String fileDestinationPath) in class "
					+this.getClass().getName(), ReturnedMessage.ERROR_MESSAGE);
		}
		List<DocumentOnServer> pasteDocs = List.create(DocumentOnServer.class);
		String dest = PathUtil.formatPathForDirectoryWithoutLastSeparator(fileDestinationPath)+"/";
		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");
		String user =Ivy.session().getSessionUserName();

		for(DocumentOnServer doc: documents) {
			if(doc instanceof FileLink) {
				pasteDocs.add(this.fileLinkController.pasteCopiedFileLinkToDestination((FileLink) doc, dest)); 
				continue;
			}
			int i = getNextCopiedFileNumber(doc.getFilename(),dest);
			if(i<0) {
				continue;
			}
			DocumentOnServer docJ = new DocumentOnServer();
			docJ.setJavaFile(this.getDocumentOnServerWithJavaFile(doc).getJavaFile());
			//Ivy.log().info("Doc with Java File retrieved ");
			if(docJ.getJavaFile()==null || !docJ.getJavaFile().isFile()) {
				continue;
			}
			String newFile=dest;
			String fname="";
			String ext= FileHandler.getFileExtension(doc.getFilename());
			if(i==0) {
				newFile+=doc.getFilename();
				fname=doc.getFilename();
			} else {
				fname=FileHandler.getFileNameWithoutExt(doc.getFilename())+"_Copy"+i+"."+ext;
				newFile+=fname;
			}
			docJ.setDescription("");
			docJ.setCreationDate(date);
			docJ.setCreationTime(time);
			docJ.setFilename(fname);
			docJ.setFileSize(doc.getFileSize());
			docJ.setLocked("0");
			docJ.setLockingUserID("");
			docJ.setModificationDate(date);
			docJ.setModificationTime(time);
			docJ.setModificationUserID("");
			docJ.setPath(newFile);
			docJ.setJavaFile(docJ.getJavaFile());
			docJ.setFileType(doc.getFileType());
			if(this.securityActivated) {
				FolderOnServer fos = this.getDirectoryWithPath(fileDestinationPath);
				docJ.setCanUserDelete(fos.getCanUserDeleteFiles());
				docJ.setCanUserRead(fos.getCanUserOpenDir());
				docJ.setCanUserWrite(fos.getCanUserWriteFiles());
			} else {
				docJ.setCanUserDelete(true);
				docJ.setCanUserRead(true);
				docJ.setCanUserWrite(true);
			}
			try{
				docJ.setExtension(docJ.getFilename().substring(docJ.getFilename().lastIndexOf(".")+1));
			}catch(Exception ex){
				//Ignore the Exception here
			}
			docJ.setUserID(user);
			int j = this.insertOneDocumentWithoutHistory(docJ);
			if(j>0){
				pasteDocs.add(docJ);
				if(this.activateFileActionHistory && this.fileActionHistoryController.getConfig().isCopyFileTracked()) {
					this.fileActionHistoryController.createNewActionHistory(Long.parseLong(docJ.getFileID()), FileActionType.FILE_CREATED_ACTION, 
							Ivy.session().getSessionUserName(), "Copy of / Kopie von / copie de: "+doc.getPath());
					this.fileActionHistoryController.createNewActionHistory(Long.parseLong(doc.getFileID()), FileActionType.FILE_COPY_PASTE_ACTION, 
							Ivy.session().getSessionUserName(), docJ.getPath());
				}
			}
			this.fireCreatedEventFromCopy(docJ, doc);
		}
		message.getDocumentOnServers().addAll(pasteDocs);
		return message;
	}

	/**
	 * 
	 * @param _fileName
	 * @param _dest
	 * @return
	 * @throws Exception
	 */
	public int getNextCopiedFileNumber(String _fileName, String _dest) throws Exception{
		if(_dest==null|| _dest.trim().equals("") || _fileName == null || _fileName.trim().equals("")) {
			return -1;
		}
		_dest = PathUtil.formatPathForDirectoryAllowingSlashesAndBackslashesAtBeginOfPath(_dest);
		String search = FileHandler.getFileNameWithoutExt(_fileName)+"_Copy";
		if(this.docPersistence.get(_dest+_fileName)==null) {
			return 0;
		}
		String s1= "";
		if(this.dirPersistence.isOracle()) {
			s1= PathUtil.escapeSpecialSQLSignsForOracleInPath(_dest+search)+"%";
		} else {
			s1= PathUtil.escapeSpecialSQLSignsInPath(_dest+search)+"%";
		}
		java.util.List<String> conditions = new ArrayList<String>();
		conditions.add("FilePath LIKE '"+s1+"' ESCAPE '"+this.getEscapeChar()+"'");
		conditions.add("FilePath NOT LIKE '"+s1+"/%' ESCAPE '"+this.getEscapeChar()+"'");
		java.util.List<DocumentOnServer> docs = this.docPersistence.getDocuments(conditions);
		if(docs.isEmpty()) {
			return 1;
		}
		int i =0;
		int tmpi=i;
		for(DocumentOnServer doc: docs) {
			String n=FileHandler.getFileNameWithoutExt(doc.getFilename());
			try {
				n = n.substring(n.lastIndexOf("_Copy")+5);
				tmpi=Integer.parseInt(n)+1;
				if(tmpi>i) {
					i=tmpi;
				}
			}catch (Exception ex) {
				//do nothing
			}
		}
		return i;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#renameDirectory(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ReturnedMessage renameDirectory(String path, String newName) throws Exception {
		ReturnedMessage message = new ReturnedMessage();

		if(path==null || newName==null || newName.trim().equals("")) {
			message.setText("One of the parameter was invalid for the method renameDirectory in "+this.getClass().getName());
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}
		newName= PathUtil.formatPathForDirectoryWithoutLastSeparator(newName);

		if(newName.contains("/") || newName.contains("\"")) {
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/invalidCharacterInDirName"));
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}
		FolderOnServer fos = this.dirPersistence.get(path);
		//look if directory exists
		if(fos==null) {
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/folderOnServer/rename/renameFailFolderCannotBeFound").replace("GOAL", path));
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}
		if(this.securityActivated) {
			SecurityResponse resp = this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.RENAME_DIRECTORY_RIGHT, 
					fos, Ivy.session().getSessionUser(), null);
			if(!resp.isAllow()){
				message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/folderOnServer/rename/renameFailSecurityException").replace("GOAL", fos.getName()));
				message.setType(FileHandler.ERROR_MESSAGE);
				return message;
			}
		}
		//format the path
		String newPath="";
		path= fos.getPath();

		if(!path.contains("/")) {
			//path is composed just by the directory old name
			newPath=newName;
		}else {
			//We get the old directory name
			newPath= path.substring(0,path.lastIndexOf("/"))+"/"+newName;
		}
		//Check if new path exists
		if(this.directoryExists(newPath)) {
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/folderOnServer/rename/renameFailFolderAlreadyExists").replace("GOAL", newPath));
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}
		//Select all the files in the dir structure. If one file is edited, cannot rename the directory.
		List<DocumentOnServer> docs = this.getDocumentOnServersInDirectory(path, true);

		String p = path+"/";
		for(DocumentOnServer doc:docs) {
			String s = doc.getPath().replaceFirst("\\Q"+ p+"\\E", newPath+"/");
			doc.setPath(s);
			this.docPersistence.update(doc);
		}
		ArrayList<FolderOnServer> dirs = this.getListDirectoriesUnderPath(path+"/");
		if(dirs.size()>0 && dirs.get(0).getPath().equals(path)) {
			//We remove the directory to rename from the list
			dirs.remove(0);
		}
		for (FolderOnServer dir: dirs) {
			dir.setPath(dir.getPath().replaceFirst("\\Q"+ p+"\\E", newPath+"/"));
			this.dirPersistence.update(dir);
		}
		fos.setPath(newPath);
		fos.setName(newName);
		this.dirPersistence.update(fos);
		message.getFiles().add(0, new java.io.File(path));
		message.getFiles().add(1, new java.io.File(newPath));
		message.setActionType(FolderAction.RENAME);
		message.setType(FileHandler.SUCCESS_MESSAGE);
		message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/folderOnServer/rename/renameSuccess"));
		return message;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#renameDocument(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean renameDocument(DocumentOnServer _doc, String newName, String _userID) throws Exception {
		if(_doc==null || _doc.getPath().trim().equals("") || _doc.getFilename().trim().equals("") || newName==null || newName.trim().equals("")) {
			throw new FileManagementException("Unable to rename the given DocumentOnServer. " +
					"One of the parameter is invalid in renameDocumentOnServer(DocumentOnServer document, String newName) " +
					"in class "+this.getClass().getName());
		}
		String ext = "."+FileHandler.getFileExtension(_doc.getPath());
		String newPath =PathUtil.escapeBackSlash( _doc.getPath());
		newPath= newPath.substring(0,newPath.lastIndexOf("/"))+"/"+(newName.endsWith(ext)?newName:newName+ext);
		if(this.docPersistence.get(newPath)!=null) {
			throw new FileManagementException(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/RenameFileAlreadyExists"));
		}
		if(_doc instanceof FileLink) {
			FileLink fl = (FileLink) _doc;
			fl.setFileLinkName(newName.endsWith(ext)?newName:newName+ext);
			this.fileLinkController.updateFileLink(fl);
			return true;
		}
		String oldName = _doc.getFilename();
		_doc.setPath(newPath);
		_doc.setFilename(newName.endsWith(ext)?newName:newName+ext);
		_doc.setModificationDate(DateUtil.getNewDateAsString());
		_doc.setModificationTime(DateUtil.getNewTimeAsString());
		_doc.setModificationUserID((_userID!=null && _userID.trim().length()>0)?_userID:Ivy.session().getSessionUserName());
		this.docPersistence.update(_doc);
		this.fireChangedEvent(_doc,false);
		if(this.activateFileActionHistory && this.fileActionHistoryController.getConfig().isRenameFileTracked()) {
			this.fileActionHistoryController.createNewActionHistory(Long.decode(_doc.getFileID()), 
					FileActionType.FILE_RENAMED_ACTION, Ivy.session().getSessionUserName(), oldName +" -> "+newName+ext);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#renameDocumentOnServer(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer, java.lang.String)
	 */
	@Override
	public ReturnedMessage renameDocumentOnServer(DocumentOnServer _doc,
			String newName) throws Exception {
		ReturnedMessage message = new ReturnedMessage();
		try {
			message.setFiles(List.create(java.io.File.class));
			this.renameDocument(_doc, newName, null);
			message.setDocumentOnServer(_doc);
		}catch(Exception ex) {
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText(ex.getMessage());
		}
		return message;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#saveDocumentOnServer(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer, java.lang.String)
	 */
	@Override
	public ReturnedMessage saveDocumentOnServer(DocumentOnServer document,
			String fileDestinationPath) throws Exception {
		return this.saveDocumentOnServer(document, fileDestinationPath, true);
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#saveDocumentOnServer(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer, java.lang.String, boolean)
	 */
	@Override
	public ReturnedMessage saveDocumentOnServer(DocumentOnServer document,
			String fileDestinationPath, boolean updateModificationMetaInfos) throws Exception {
		ReturnedMessage message = new ReturnedMessage();

		if(document == null || document.getPath()==null || document.getPath().trim().equals("") || document.getFilename()==null
				|| document.getFilename().trim().equals("")) {
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText("Unable to save the given DocumentOnServer. It may be null, or its path or filename field is null or empty. " +
					"saveDocumentOnServer(DocumentOnServer document, String fileDestinationPath) in class "+this.getClass().getName());
			return message;
		}

		if(fileDestinationPath==null || fileDestinationPath.trim().equals("")) {
			//no destination path => the file goes in the same path as document
			fileDestinationPath=PathUtil.formatPath(document.getPath().trim());
		}else {
			// format the destinationPath to fit to the file management path convention
			fileDestinationPath= PathUtil.formatPath(fileDestinationPath);
		}

		if(document.getJavaFile()==null || !document.getJavaFile().isFile()) {
			if(document.getIvyFile()!=null && document.getIvyFile().isFile()) {
				document.setJavaFile(document.getIvyFile().getJavaFile());
			}
		}

		int id=0;
		try {
			id=Integer.parseInt(document.getFileID().trim());
		} catch(Exception ex) { 
			/*do nothing the document may be new if it cannot be found by its path */ 
		}
		if(id<=0) { 
			//then it may be a new document we check if this document already exists
			DocumentOnServer doc = this.getDocumentOnServer(document.getPath().trim());
			if(doc!=null && doc.getFileID()!=null) {
				try {
					id=Integer.parseInt(doc.getFileID().trim());
				} catch(Exception ex) { 
					/* do nothing, we suppose it is a new document */
				}
			}
			if(id<=0) {
				//new
				this.insertOneDocument(document);
				doc = this.getDocumentOnServer(document.getPath().trim());
				int j = 0;
				if(doc!=null && doc.getFileID()!=null) {
					try {
						j=Integer.parseInt(doc.getFileID().trim());
					} catch(Exception ex) { 
						/* do nothing, we suppose it is a new document */
					}
				}
				if(j>0) {
					//Success
					message.setDocumentOnServer(doc);
					this.fireCreatedEvent(doc);
					return message;
				} else {
					//error
					message.setType(FileHandler.ERROR_MESSAGE);
					message.setText("Failed to insert the new documentOnServer object in saveDocumentOnServer(DocumentOnServer document, String fileDestinationPath) in class "+this.getClass().getName());
					return message;
				}
			} else {
				// it will overwrite an existing document
				document.setFileID(doc.getFileID().trim());
			}
		}
		// id is set, we update the existing DocumentOnServer
		if(updateModificationMetaInfos) {
			String date = new Date().format("dd.MM.yyyy");
			String time = new Time().format("HH:mm:ss");
			String user = Ivy.session().getSessionUserName();
			document.setModificationDate(date);
			document.setModificationTime(time);
			document.setModificationUserID(user);
		}
		if(!fileDestinationPath.endsWith(document.getFilename())) {
			//the file path has to include the filename
			fileDestinationPath = fileDestinationPath+"/"+document.getFilename();
		}
		document.setPath(fileDestinationPath);
		this.docPersistence.update(document);
		this.fireChangedEvent(document,(document.getJavaFile()!=null && document.getJavaFile().isFile()));

		if(this.activateFileTypesFeature) {
			long ftId =0;
			if(document.getFileType() != null && document.getFileType().getId()!=null) {
				ftId = document.getFileType().getId();
			}
			this.fileTypesController.setDocumentFileType(document, ftId);
		}
		return message;
	}

	@Override
	public ReturnedMessage setFileDescription(String path, String description)
			throws Exception {
		ReturnedMessage message = new ReturnedMessage();

		if(path==null || path.trim().equals("")) {
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText("Unable to set the description. One of the parameter is invalid in setFileDescription(String path, String description) in class "+this.getClass().getName());
			return message;
		}
		DocumentOnServer doc = this.docPersistence.get(PathUtil.escapeBackSlash(path));
		if(doc==null){
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/fileNotfound")+" "+path);
			return message;
		}
		return this.setFileDescription(doc, description);
	}

	@Override
	public ReturnedMessage setFileDescription(DocumentOnServer document,
			String description) throws Exception {
		ReturnedMessage message = new ReturnedMessage();

		if(document==null){
			return new ReturnedMessage("Unable to set the description. DocumentOnServer parameter is null in " +
					"setFileDescription(DocumentOnServer document, String description) in class "+this.getClass().getName(), ReturnedMessage.ERROR_MESSAGE);
		}

		if(description == null) {
			description="";
		}
		document.setDescription(description);
		this.docPersistence.update(document);
		if(this.activateFileActionHistory && this.fileActionHistoryController.getConfig().isChangeFileDescriptionTracked()) {
			this.fileActionHistoryController.createNewActionHistory(Long.decode(document.getFileID()), 
					FileActionType.FILE_DESCRIPTION_CHANGED_ACTION, Ivy.session().getSessionUserName(), "");
		}
		this.fireChangedEvent(document,false);
		message.setDocumentOnServer(document);
		return message;
	}

	@Override
	public ReturnedMessage moveDocumentOnServer(DocumentOnServer document, String destination) throws Exception {
		if(document == null || document.getPath()==null || document.getPath().trim().equals("") || document.getFilename()==null
				|| document.getFilename().trim().equals("") || destination==null || destination.trim().equals("")) {
			return new ReturnedMessage("Unable to save the given DocumentOnServer. " +
					"One of the parameter is invalid in saveDocumentOnServer(DocumentOnServer document, String fileDestinationPath) " +
					"in class "+this.getClass().getName(), ReturnedMessage.ERROR_MESSAGE);
		}
		destination = PathUtil.formathPathForDirectoryWithoutFirstSeparatorWithEndSeparator(destination);
		long id = 0;
		String actionMessage = "";
		if(document instanceof FileLink) {
			FileLink fl = (FileLink) document;
			String actualPath = fl.getFileLinkPath();
			fl.setFileLinkPath(destination+fl.getFileLinkName());
			this.fileLinkController.moveFileLink((FileLink) document, destination);
			id = fl.getFileLinkId();
			actionMessage = actualPath +" -> " + fl.getFileLinkPath();
		} else {
			String oldPlace = document.getPath();
			document.setPath(destination+document.getFilename());
			this.docPersistence.update(document);
			id = Long.parseLong(document.getFileID());
			actionMessage = oldPlace +" -> " + destination+document.getFilename();
		}
		if(this.activateFileActionHistory && this.fileActionHistoryController.getConfig().isMoveFileTracked()) {
			this.fileActionHistoryController.createNewActionHistory(id, 
					FileActionType.FILE_MOVED_ACTION, 
					Ivy.session().getSessionUserName(), actionMessage);
		}
		this.fireChangedEvent(document, false);

		if(this.securityActivated) {
			FolderOnServer fos = this.getDirectoryWithPath(destination);
			document.setCanUserDelete(fos.getCanUserDeleteFiles());
			document.setCanUserRead(fos.getCanUserOpenDir());
			document.setCanUserWrite(fos.getCanUserWriteFiles());
		} else {
			document.setCanUserDelete(true);
			document.setCanUserRead(true);
			document.setCanUserWrite(true);
		}
		ReturnedMessage message = new ReturnedMessage();
		message.setDocumentOnServer(document);
		return message;
	}

	/**
	 * unLock a document in the DB 
	 * @param _doc: the DocumentOnServer Object that has to be Locked
	 * @return true if the document was unLocked, else false
	 * @throws Exception
	 */
	public boolean unlockDocument(DocumentOnServer _doc) throws Exception{
		if(_doc == null )
		{
			throw new Exception("Invalid DocumentOnServer Object in unlockDocument method.");
		}
		_doc.setLocked("0");
		_doc.setLockingUserID("");
		_doc = this.docPersistence.update(_doc);
		return (_doc.getIsLocked());

	}

	/**
	 * unLock a document in the DB with check if given user is the same who has Locked the document.<br>
	 * If it is not the same, the File won't be Locked.
	 * @param _doc: the DocumentOnServer Object that has to be Locked
	 * @param _user the user who locks this document
	 * @return true if the document was unLocked, else false
	 * @throws Exception
	 */
	public boolean unlockDocumentWithUSerCheck(DocumentOnServer _doc, String _user)throws Exception{
		if(_doc == null )
		{
			throw new Exception("Invalid DocumentOnServer Object in unlockDocument method.");
		}
		if(!_doc.getLockingUserID().equals(_user)) {
			return false;
		}
		_doc.setLocked("0");
		_doc.setLockingUserID("");
		_doc = this.docPersistence.update(_doc);
		return (_doc.getIsLocked());
	}

	/**
	 * unLock a File in the DB<br>
	 * The File here just holds the informations stored in the DB (path, etc...). The File does not exit on the FileSystem.
	 * @param _file: the java.io.File to lock
	 * @return true if the document was unLocked, else false
	 * @throws Exception
	 */
	public boolean unlockFile(java.io.File _file) throws Exception{
		if(_file == null )
		{
			throw new Exception("Invalid DocumentOnServer Object in unlockDocument method.");
		}
		DocumentOnServer doc = this.docPersistence.get(PathUtil.escapeBackSlash(_file.getPath()));
		return this.unlockDocument(doc);
	}

	/**
	 * unLock a File in the DB with check if given user is the same who has Locked the document.<br>
	 * If it is not the same, the File won't be Locked.<br>
	 * The File here just holds the informations stored in the DB (path, etc...). The File does not exit on the FileSystem.
	 * @param _file: the java.io.File to lock
	 * @param _user the user who locks this document
	 * @return true if the document was unLocked, else false
	 * @throws Exception
	 */
	public boolean unlockFileWithUSerCheck(java.io.File _file, String _user) throws Exception{
		if(_file == null )
		{
			throw new Exception("Invalid DocumentOnServer Object in unlockDocument method.");
		}
		DocumentOnServer doc = this.docPersistence.get(escapeBackSlash(_file.getPath()));
		return this.unlockDocumentWithUSerCheck(doc, _user);
	}

	/**
	 * this Method should be used to unlock all the files edited by a given user under a given path.
	 * If the boolean argument "recursive" is true, then all the files in the children directories
	 * are going to be unLocked. Else just the files directly under the given path are going to be unLocked.
	 * This method can be used when you close an application for example.
	 * @param _path the path where to look for the Locked files
	 * @param _user the ivy user name 
	 * @param _recursive true or false. If is recursive, look in all the sub directories under the path
	 * @throws Exception
	 */
	public void unlockFilesEdited(String _path, String _user, boolean _recursive)throws Exception {
		this.docPersistence.unlockDocumentsUnderPathEditedByUserWithOptionalRecursivity(_path, _user, _recursive);

	}

	/**
	 * update the documents with one complete SQL Query as argument
	 * @param query: the SQL Query as String
	 * @throws Exception
	 */
	public void updateDocuments(String _query) throws Exception{
		if(this.docPersistence instanceof DocumentOnServerSQLPersistence) {
			((DocumentOnServerSQLPersistence) this.docPersistence).updateDocumentWithQuery(_query);
		}
	}

	/**
	 * Allows executing an update on documents with the given Key/Value pairs and conditions for filtering
	 * @param KVP: List<KeyValuePair> that represents the new values for the given properties (Keys)
	 * @param conditions: List<String> the list of the conditions to filter the update
	 * @return a RecordSet of the updated documents
	 * @throws Exception
	 */
	public int updateDocuments(List<KeyValuePair> _KVP, List<String> _conditions) throws Exception{
		if(this.docPersistence instanceof DocumentOnServerSQLPersistence) {
			return ((DocumentOnServerSQLPersistence) this.docPersistence).updateDocuments(_KVP, _conditions);
		} else {
			return 0;
		}
	}

	/** DELETE DOCUMENTS OPERATIONS **/

	@Override
	public ReturnedMessage deleteDocumentOnServers(List<DocumentOnServer> documents) throws Exception {
		ReturnedMessage message;
		try {
			this.deleteDocuments(documents);
			message = new ReturnedMessage();
		}catch(Exception ex){
			Ivy.log().error("An error occurred while deleting documents", ex);
			message = new ReturnedMessage(ex.getMessage(), ReturnedMessage.ERROR_MESSAGE);
			message.setException(ex);
		}

		return message;
	}

	/**
	 * delete documents from the DB
	 * @param _documents the list of the DocumentOnServer to delete
	 * @return the number of items deleted
	 * @throws Exception
	 */
	public int deleteDocuments(List<DocumentOnServer> _documents)throws Exception {
		int i  = this.deleteDocumentsInDBOnly(_documents);
		return i;
	}

	/**
	 * delete files from the DB
	 * @param _files the list of the java.io.File to delete
	 * @return the number of items deleted
	 * @throws Exception
	 */
	public int deleteFiles(List<java.io.File> _files)throws Exception{
		return this.deleteFilesInDBOnly(_files, null);

	}

	/**
	 * delete all the files from the db that are in the file Structure under a directory.
	 * @param _directoryPath
	 * @return
	 * @throws Exception
	 */
	public ReturnedMessage deleteAllFilesUnderDirectory(String _directoryPath) throws Exception {
		ReturnedMessage message = new ReturnedMessage();
		message.setType(FileHandler.SUCCESS_MESSAGE);
		if(_directoryPath==null || _directoryPath.trim().equals("")) {
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText("The directory to delete does not exist.");
			return message;
		}
		boolean deleteHistory = this.activateFileActionHistory && 
				this.fileActionHistoryController.getConfig().isDeleteFileTracked();
		java.util.List<DocumentOnServer> docs = this.docPersistence.getList(_directoryPath, true);
		message = checkAllDocumentsCanBeRemoved(docs);
		if(message.getType()!=FileHandler.SUCCESS_MESSAGE) {
			return message;
		}
		try {
			for(DocumentOnServer doc: docs) {
				this.deleteAllDocumentVersionsIfFileVersioningEnabled(doc);
				this.docPersistence.delete(doc);
				fireDeleteEvent(doc);
				if(deleteHistory) {
					this.fileActionHistoryController.createNewActionHistory(Long.decode(doc.getFileID()), FileActionType.FILE_DELETED_ACTION, 
							Ivy.session().getSessionUserName(), _directoryPath);
				}
				if(this.activateFileTagFeature) {
					this.fileTagsController.deleteFileTags(Long.parseLong(doc.getFileID()));
				}
			}
		} catch (Exception ex) {
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText("An Exception occured while deleting the files in "+_directoryPath);
			Ivy.log().error("An Exception occured while deleting the files in "+_directoryPath+" : "+ex.getMessage(),ex);
		}
		return message;
	}

	private ReturnedMessage checkAllDocumentsCanBeRemoved(java.util.List<DocumentOnServer> docs) throws Exception {
		ReturnedMessage message = new ReturnedMessage();
		for(DocumentOnServer doc: docs) {
			if(!doc.getLocked().equals("0")) {
				message.setType(FileHandler.ERROR_MESSAGE);
				message.setText("The documents cannot be deleted because at least one of them is locked. " +
						"Locked document at: "+doc.getPath()+" by user "+doc.getLockingUserID());
				return message;
			}
			if(this.config.isFileArchiveProtectionEnabled() && 
					this.fileVersionController.wasFileVersionArchived(Long.parseLong(doc.getFileID()), doc.getVersionnumber().intValue())) {
				message.setType(FileHandler.ERROR_MESSAGE);
				message.setText(String.format(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/version/FileDeleteAbordedBecauseArchiveProtection"), doc.getVersionnumber().toString(), doc.getFilename()));
				return message;
			}
		}
		return message;
	}


	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler#deleteDocumentOnServer(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer)
	 */
	@Override
	public ReturnedMessage deleteDocumentOnServer(DocumentOnServer document) throws Exception {
		if(document==null || document.getPath() ==null || document.getPath().trim().equals("")) {
			throw new IllegalArgumentException("The 'document' parameter in "+this.getClass().getName()+", method deleteDocumentOnServer(DocumentOnServer document) is not set.");
		}
		ReturnedMessage message = new ReturnedMessage();
		try {
			List<DocumentOnServer> docs = List.create(DocumentOnServer.class);
			docs.add(document);
			if(this.deleteDocumentsInDBOnly(docs) == 1) {
				return new ReturnedMessage("The document has been successfully removed.", ReturnedMessage.SUCCESS_MESSAGE);
			} else {
				return new ReturnedMessage("The document has not been removed.", ReturnedMessage.ERROR_MESSAGE);
			}
		}catch(FileVersionProtectedException ex) {
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText(String.format(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/version/FileDeleteAbordedBecauseArchiveProtection"), document.getFilename()));
			Ivy.log().error("An Exception occured while deleting the document "+document.getPath()+" : "+ex.getMessage(),ex);
			message.setException(ex);
		} catch(Exception ex) {
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText(ex.getMessage());
			Ivy.log().error("An Exception occured while deleting the document "+document.getPath()+" : "+ex.getMessage(),ex);
		}
		return message;
	}


	/**
	 * delete documents from the DB, and all their related infos (Tags, Types if any...)
	 * @param _documents the list of the DocumentOnServer to delete
	 * @return the number of items deleted
	 * @throws Exception
	 */
	public int deleteDocumentsInDBOnly(List<DocumentOnServer> _documents) throws Exception {
		if(_documents==null || _documents.size()==0) {
			return 0;
		}
		int deletedFiles=0;
		boolean deleteHistory = this.activateFileActionHistory && this.fileActionHistoryController.getConfig().isDeleteFileTracked();
		for(DocumentOnServer doc: _documents) {
			if(doc instanceof FileLink) {
				this.fileLinkController.deleteFileLink((FileLink) doc);
				deletedFiles++;
				continue;
			}
			int id=0;
			if(doc.getFileID()!=null) {
				try{ id=Integer.parseInt(doc.getFileID()); }catch(Exception ex){/*do nothing*/}
			}
			if(id<=0) {
				id=this.getDocIdWithPath(escapeBackSlash(doc.getPath()));
			}
			if(id>0) {
				deleteAllFileLinksForFile(Long.parseLong(doc.getFileID()));
				deleteAllDocumentVersionsIfFileVersioningEnabled(doc);
				this.docPersistence.delete(doc);
				deletedFiles++;
				this.fireDeleteEvent(doc);
				if(deleteHistory) {
					this.fileActionHistoryController.createNewActionHistory(id, 
							FileActionType.FILE_DELETED_ACTION, Ivy.session().getSessionUserName(), 
							doc.getPath());
				}
				if(this.activateFileTagFeature) {
					this.fileTagsController.deleteFileTags(id);
				}
			}
		}
		return deletedFiles;
	}

	/**
	 * @param doc
	 * @throws Exception
	 * @throws FileVersionProtectedException
	 */
	private void deleteAllDocumentVersionsIfFileVersioningEnabled(DocumentOnServer doc) throws Exception, FileVersionProtectedException {
		if(doc instanceof FileLink) {
			return;
		}
		if(this.activateFileVersioning) {
			if(this.config.isFileArchiveProtectionEnabled()
					&& this.fileVersionController.wasFileVersionArchived(Long.parseLong(doc.getFileID()), doc.getVersionnumber().intValue())) {
				throw new FileVersionProtectedException(FileVersioningController.DELETE_VERSION_NOT_PERMITTED);
			}
			if(doc.getVersionnumber().intValue()>1) {
				this.fileVersionController.deleteAllVersionsFromFile(Long.parseLong(doc.getFileID()));
			}
		}
	}

	private void deleteAllFileLinksForFile(long fileId) throws Exception {
		if(this.fileLinkController == null) {
			return;
		}
		java.util.List<FileLink> fileLinks = this.fileLinkController.getFileLinksForFile(fileId);
		for(FileLink fl: fileLinks) {
			this.fileLinkController.deleteFileLink(fl);
		}
	}

	/**
	 * For private use only.
	 * Deletes the given java File from the database.
	 * @param _files List of files that should be deleted
	 * @param parentDirectoryPath the path of the directory in the database where the files are stored.
	 * @return the number of deleted files.
	 * @throws Exception
	 */
	private int deleteFilesInDBOnly(List<java.io.File> _files, String parentDirectoryPath)throws Exception{
		int deletedFiles=0;
		if(_files==null || _files.size()==0) {
			return 0;
		}
		if(parentDirectoryPath!=null && parentDirectoryPath.trim().length()==0) {
			parentDirectoryPath=null;
		}else {
			parentDirectoryPath=parentDirectoryPath.trim();
		}
		boolean deleteHistory = this.activateFileActionHistory && this.fileActionHistoryController.getConfig().isDeleteFileTracked();
		for(java.io.File file: _files) {
			DocumentOnServer doc = this.docPersistence.get(file.getPath());
			if(doc!=null) {
				long id= Long.decode(doc.getFileID());

				boolean b = this.docPersistence.delete(doc);
				this.fireDeleteEvent(doc);
				if(b) {
					deletedFiles++;
					if(deleteHistory) {
						this.fileActionHistoryController.
						createNewActionHistory(id, FileActionType.FILE_DELETED_ACTION, 
								Ivy.session().getSessionUserName(), (parentDirectoryPath==null?
										file.getPath():parentDirectoryPath+file.getName()));
					}
					if(this.activateFileTagFeature) {
						this.fileTagsController.deleteFileTags(id);
					}
				}

			}
		}
		return deletedFiles;
	}

	/**
	 * @return the dirTableName
	 */
	public String getDirTableName() {
		return this.config.getDirectoriesTableName();
	}

	/**
	 * @param dirTableName the dirTableName to set
	 * @throws Exception 
	 */
	public void setDirTableName(String dirTableName) throws Exception {
		this.config.setDirectoriesTableName(dirTableName);
		this.dirPersistence =  PersistenceConnectionManagerFactory.getIFolderOnServerPersistenceInstance(this.config);
	}

	/**
	 * @return the dirTableNameSpace
	 */
	public String getDirTableNameSpace() {
		return (this.config.getDatabaseSchemaName()!=null && this.config.getDatabaseSchemaName().trim().length()>0)?
				this.config.getDatabaseSchemaName()+"."+this.config.getDirectoriesTableName():this.config.getDirectoriesTableName();
	}

	/**
	 * @param dirTableNameSpace the dirTableNameSpace to set
	 * @throws Exception 
	 */
	public void setDirTableNameSpace(String dirTableNameSpace) throws Exception {
		if(dirTableNameSpace==null || dirTableNameSpace.trim().length()==0 || dirTableNameSpace.trim().equals(".")) {
			return;
		}
		if(dirTableNameSpace.contains(".")) {
			String schema = dirTableNameSpace.substring(0, dirTableNameSpace.indexOf("."));
			this.config.setDatabaseSchemaName(schema);
			this.setDirTableName(dirTableNameSpace.substring(dirTableNameSpace.indexOf(".")+1));
		}else{
			this.setDirTableName(dirTableNameSpace);
		}
		this.docPersistence = PersistenceConnectionManagerFactory.getIDocumentOnServerPersistenceInstance(this.config);
		this.dirPersistence =  PersistenceConnectionManagerFactory.getIFolderOnServerPersistenceInstance(this.config);
	}

	@Override
	public boolean isDirectoryEmpty(String directoryPath) throws Exception {
		if(directoryPath==null || directoryPath.trim().equals(""))
		{
			throw new IllegalArgumentException("Illegal directory Path in method isDirectoryEmpty(String directoryPath) in "+this.getClass().toString());
		}
		if(directoryExists(directoryPath))
		{
			if(getDocumentsInPath(directoryPath, true).size()>0)
			{
				return false;
			}
			if(getListDirectoriesUnderPath(directoryPath).size()>1)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * @param securityOn the securityOn to set
	 */
	public void setSecurityOn(boolean securityOn) {
		this.securityActivated = securityOn;
		if(this.securityActivated && this.securityController==null){
			try {
				this.securityController = FileManagementHandlersFactory.getDirectorySecurityControllerInstance(this.config);
			} catch (Exception e) {

			}
		}
	}

	/**
	 * @return the securityOn
	 */
	public boolean isSecurityOn() {
		return securityActivated;
	}

	@Override
	public ReturnedMessage zipDocumentOnServers(
			List<DocumentOnServer> documents, String dirPath, String zipName,
			boolean checkIfExists) throws Exception {
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		message.setDocumentOnServers(List.create(DocumentOnServer.class));

		if(dirPath == null || dirPath.trim().length()<=0 || documents==null || documents.size()==0)
		{
			throw new IllegalArgumentException("One of the parameter is not set in zipDocumentOnServers(List<DocumentOnServer> documents, String dirPath, String zipName,boolean checkIfExists) in "+ this.getClass());
		}

		dirPath=PathUtil.formathPathForDirectoryWithoutFirstSeparatorWithEndSeparator(dirPath);
		zipName = zipName.endsWith(".zip")?zipName:zipName+".zip";
		boolean exists = this.fileExists(dirPath+zipName);

		if(checkIfExists && exists){
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/zipfileAlreadyExistsCannotCreateIt"));
			return message;
		}else if(exists)
		{
			this.deleteFile(dirPath+zipName);
		}

		ArrayList<java.io.File> zipFiles = new ArrayList<java.io.File>();
		for(DocumentOnServer doc : documents){
			if(doc.getJavaFile()!=null && doc.getJavaFile().isFile())
			{
				zipFiles.add(doc.getJavaFile());
			}else{
				java.io.File f = this.getDocumentOnServerWithJavaFile(doc).getJavaFile();
				if(f!=null && f.isFile()){
					zipFiles.add(f);
				}
			}
		}

		String date = new Date().format("dd.MM.yyyy");
		String time = new Time().format("HH:mm:ss");
		String user ="IVYSYSTEM";
		//we create a temp file on the server 
		String tmpPath="tmp/"+System.nanoTime();
		File ivyFile = new File(tmpPath+"/"+zipName,true);
		ivyFile.createNewFile();

		java.io.File zip = ZipHandler.makeZip(tmpPath, zipName, zipFiles);
		try{
			user = Ivy.session().getSessionUserName();
		}catch(Exception ex)
		{
			user ="IVYSYSTEM";
		}
		DocumentOnServer doc = new DocumentOnServer();
		doc.setCreationDate(date);
		doc.setCreationTime(time);
		doc.setUserID(user);
		doc.setDescription("");
		doc.setFilename(zipName);
		doc.setJavaFile(zip);
		doc.setFileSize(FileHandler.getFileSize(zip));
		doc.setLocked("0");
		doc.setLockingUserID("");
		doc.setModificationDate(date);
		doc.setModificationTime(time);
		doc.setModificationUserID(user);
		doc.setPath(dirPath+zipName);
		return this.saveDocumentOnServer(doc, "");

	}

	/**
	 * @throws FileVersionProtectedException if the archive protection is enabled and the actual document version that should be implicitly deleted
	 * has been an archive in the past.
	 * @throws Exception In case of SQL or persistence exception
	 */
	@Override
	public boolean deleteFile(String _filepath) throws FileVersionProtectedException, Exception {
		DocumentOnServer document = this.docPersistence.get(_filepath);
		if(document!=null) {
			deleteAllFileLinksForFile(Long.parseLong(document.getFileID()));
			deleteAllDocumentVersionsIfFileVersioningEnabled(document);
			return this.docPersistence.delete(document);
		}
		return false;
	}


	@Override
	@Deprecated
	public AbstractDirectorySecurityController getSecurityController()
			throws Exception {
		if(this.securityController instanceof AbstractDirectorySecurityController) {
			return (AbstractDirectorySecurityController) this.securityController;
		} else if(this.securityController instanceof SecurityHandlerChain){
			SecurityHandlerChain chain  = (SecurityHandlerChain) this.securityController;
			if(chain.getSecurityHandlers().size()>0) {
				SecurityHandler sh = chain.getSecurityHandlers().get(chain.getSecurityHandlers().size()-1);
				if(sh instanceof AbstractDirectorySecurityController) {
					return (AbstractDirectorySecurityController) sh;
				}
			}
		}
		//nothing could be found
		return null;
	}

	@Override
	public SecurityHandler getSecurityHandler() {
		return this.securityController;
	}

	@Override
	public int getFile_content_storage_type() {
		return AbstractFileManagementHandler.FILE_STORAGE_DATABASE;
	}

	public synchronized void addFileEventListener(FileActionListener listener)  {
		listeners.add(listener);
	}

	public synchronized void removeFileEventListener(FileActionListener listener)   {
		listeners.remove(listener);
	}

	private synchronized void fireDeleteEvent(DocumentOnServer doc) {
		FileActionEvent event = new FileActionEvent(doc);
		Iterator<FileActionListener> i = listeners.iterator();
		while(i.hasNext())  {
			((FileActionListener) i.next()).fileDeleted(event);
		}
	}

	private synchronized void fireChangedEvent(DocumentOnServer doc, boolean contentChanged) {
		FileActionEvent event = new FileActionEvent(doc);
		event.setContentChanged(contentChanged);
		Iterator<FileActionListener> i = listeners.iterator();
		while(i.hasNext())  {
			((FileActionListener) i.next()).fileChanged(event);
		}
	}

	private synchronized void fireCreatedEvent(DocumentOnServer doc) {
		FileActionEvent event = new FileActionEvent(doc);
		Iterator<FileActionListener> i = listeners.iterator();
		while(i.hasNext())  {
			((FileActionListener) i.next()).fileCreated(event);
		}
	}

	private synchronized void fireRollBackVersionEvent(DocumentOnServer doc) {
		FileActionEvent event = new FileActionEvent(doc);
		Iterator<FileActionListener> i = listeners.iterator();
		while(i.hasNext())  {
			((FileActionListener) i.next()).fileVersionRollbacked(event);
		}
	}

	private synchronized void fireCreatedEventFromCopy(DocumentOnServer doc, DocumentOnServer parentDoc) {
		FileActionEvent event = new FileActionEvent(doc);
		event.setFileACopy(true);
		event.setCopiedDocumentOnServer(parentDoc);
		Iterator<FileActionListener> i = listeners.iterator();
		while(i.hasNext())  {
			((FileActionListener) i.next()).fileCreated(event);
		}
	}

	@Override
	public List<String> setRightOnDirectory(String _path, SecurityRightsEnum rightType,
			List<String> allowedIvyRoleNames) throws Exception {
		String path=PathUtil.formatPathForDirectoryWithoutLastSeparator(_path);	
		if(path==null || path.length()==0 || allowedIvyRoleNames==null || allowedIvyRoleNames.size()==0 || rightType == null)
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method removeRightOnDirectory(String path, " +
					"int rightType, List<String> disallowedIvyRoleNames) in "+this.getClass());
		}
		FolderOnServer fos = this.getDirectoryWithPath(path);
		if(fos != null) {
			allowedIvyRoleNames = this.ensureAdminRoleInList(allowedIvyRoleNames);
			switch (rightType) {
			case CREATE_DIRECTORY_RIGHT:
				fos.setCcd(allowedIvyRoleNames);
				break;
			case CREATE_FILES_RIGHT:
				fos.setCcf(allowedIvyRoleNames);
				break;
			case DELETE_DIRECTORY_RIGHT:
				fos.setCdd(allowedIvyRoleNames);
				break;
			case DELETE_FILES_RIGHT:
				fos.setCdf(allowedIvyRoleNames);
				break;
			case MANAGE_SECURITY_RIGHT:
				fos.setCmrd(allowedIvyRoleNames);
				break;
			case OPEN_DIRECTORY_RIGHT:
				fos.setCod(allowedIvyRoleNames);
				break;
			case RENAME_DIRECTORY_RIGHT:
				fos.setCrd(allowedIvyRoleNames);
				break;
			case TRANSLATE_DIRECTORY_RIGHT:
				fos.setCtd(allowedIvyRoleNames);
				break;
			case UPDATE_DIRECTORY_RIGHT:
				fos.setCud(allowedIvyRoleNames);
				break;
			case UPDATE_FILES_RIGHT:
				fos.setCuf(allowedIvyRoleNames);
				break;
			case WRITE_FILES_RIGHT:
				fos.setCwf(allowedIvyRoleNames);
				break;
			default:
				break;
			}
			this.saveFolderOnServer(fos);
			return allowedIvyRoleNames;
		}
		return null;
	}

	@Override
	public List<String> AddRightOnDirectoryForIvyRole(String _path,
			SecurityRightsEnum rightType, String ivyRoleName) throws Exception {
		String path=PathUtil.formatPathForDirectoryWithoutLastSeparator(_path);	
		if(path==null || path.length()==0 || ivyRoleName==null || ivyRoleName.trim().length()==0 || rightType == null)
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method AddRightOnDirectoryForIvyRole(String path, " +
					"int rightType, String ivyRoleName) in "+this.getClass());
		}
		List<String> roles = this.getRolesNamesAllowedForRightOnDirectory(_path, rightType);
		if(!roles.contains(ivyRoleName)) {
			roles.add(ivyRoleName);
			return this.setRightOnDirectory(_path, rightType, roles);
		}
		return roles;
	}

	@Override
	public List<String> removeRightOnDirectory(String _path, SecurityRightsEnum rightType,
			List<String> disallowedIvyRoleNames) throws Exception {
		String path=PathUtil.formatPathForDirectoryWithoutLastSeparator(_path);	
		if(path==null || path.length()==0 || rightType == null)
		{
			throw  new IllegalArgumentException("The path or rightType parameter is not set in method removeRightOnDirectory(String path, " +
					"int rightType, String ivyRoleName) in "+this.getClass());
		}
		List<String> roles = this.getRolesNamesAllowedForRightOnDirectory(_path, rightType);
		roles.removeAll(disallowedIvyRoleNames);
		return this.setRightOnDirectory(_path, rightType, roles);

	}

	@Override
	public List<String> getRolesNamesAllowedForRightOnDirectory(String _path,
			SecurityRightsEnum rightType) throws Exception {
		List<String> roles = List.create(String.class);
		FolderOnServer fos = this.getDirectoryWithPath(_path);
		switch (rightType) {
		case CREATE_DIRECTORY_RIGHT:
			roles.addAll(fos.getCcd());
			break;
		case CREATE_FILES_RIGHT:
			roles.addAll(fos.getCcf());
			break;
		case DELETE_DIRECTORY_RIGHT:
			roles.addAll(fos.getCdd());
			break;	
		case DELETE_FILES_RIGHT:
			roles.addAll(fos.getCdf());
			break;	
		case MANAGE_SECURITY_RIGHT:
			roles.addAll(fos.getCmrd());
			break;	
		case OPEN_DIRECTORY_RIGHT:
			roles.addAll(fos.getCod());
			break;	
		case RENAME_DIRECTORY_RIGHT:
			roles.addAll(fos.getCrd());
			break;	
		case TRANSLATE_DIRECTORY_RIGHT:
			roles.addAll(fos.getCtd());
			break;	
		case UPDATE_DIRECTORY_RIGHT:
			roles.addAll(fos.getCud());
			break;	
		case UPDATE_FILES_RIGHT:
			roles.addAll(fos.getCdf());
			break;	
		case WRITE_FILES_RIGHT:
			roles.addAll(fos.getCwf());
			break;	
		default:
			break;
		}
		return roles;
	}

	@Override
	public FolderOnServer createIndestructibleDirectory(String directoryPath,
			List<String> allowedIvyRoleNames) throws Exception {
		//Check incoming parameters
		directoryPath = PathUtil.formatPathForDirectoryWithoutLastSeparator(directoryPath);
		if(directoryPath == null || directoryPath.length()==0) {
			throw  new IllegalArgumentException("The parameter 'directoryPath' is null in method createIndestructibleDirectory " +
					" in "+this.getClass());
		}
		if(allowedIvyRoleNames==null || allowedIvyRoleNames.isEmpty()) {
			allowedIvyRoleNames = List.create(String.class);
			allowedIvyRoleNames.add("Everybody");
		}
		directoryPath = PathUtil.formatPathForDirectoryWithoutLastSeparator(directoryPath);
		FolderOnServer fos = new FolderOnServer();
		fos.setPath(directoryPath);
		fos.setName(PathUtil.getDirectoryNameFromPath(directoryPath));

		List<String> grantedIvyRoleNamesToManageRights = List.create(String.class);
		String admin = this.getFileManagerAdminRoleName();
		grantedIvyRoleNamesToManageRights.add(admin);

		fos.setCmrd(grantedIvyRoleNamesToManageRights);
		fos.setCod(allowedIvyRoleNames);
		fos.setCcf(allowedIvyRoleNames);
		fos.setCuf(allowedIvyRoleNames);
		fos.setCdf(allowedIvyRoleNames);
		fos.setCwf(allowedIvyRoleNames);
		fos.setCcd(allowedIvyRoleNames);
		fos.setCdd(grantedIvyRoleNamesToManageRights);
		fos.setCrd(grantedIvyRoleNamesToManageRights);
		fos.setCtd(grantedIvyRoleNamesToManageRights);
		fos.setCud(grantedIvyRoleNamesToManageRights);
		fos.setIs_protected(true);
		return this.createDirectory(fos);
	}

	@Override
	public FolderOnServer createOpenDirectory(String directoryPath)
			throws Exception {
		directoryPath = PathUtil.formatPathForDirectoryWithoutLastSeparator(directoryPath);
		if(directoryPath == null || directoryPath.length()==0){
			throw  new IllegalArgumentException("The parameter 'directoryPath' is null in method  createOpenDirectory " +
					" in "+this.getClass());
		}
		String admin = getFileManagerAdminRoleName();

		List<String> l1 = List.create(String.class);
		List<String> l2 = List.create(String.class);
		l1.add(admin);
		l2.add("Everybody");
		FolderOnServer fos = new FolderOnServer();
		fos.setPath(directoryPath);
		fos.setName(PathUtil.getDirectoryNameFromPath(directoryPath));
		fos.setIs_protected(true);
		fos.setCmrd(l1);
		fos.setCod(l2);
		fos.setCrd(l2);
		fos.setCtd(l2);
		fos.setCcd(l2);
		fos.setCud(l2);
		fos.setCdf(l2);
		fos.setCuf(l2);
		fos.setCwf(l2);
		fos.setCcf(l2);
		fos.setCdd(l2);

		return this.createDirectory(fos);
	}

	@Override
	public FolderOnServer createDirectory(String _directoryPath,
			List<String> grantedIvyRoleNamesToManageRights,
			List<String> grantedIvyRoleNamesToDeleteDirectory,
			List<String> grantedIvyRoleNamesToUpdateDirectory,
			List<String> grantedIvyRoleNamesToOpenDirectory,
			List<String> grantedIvyRoleNamesToWriteFiles,
			List<String> grantedIvyRoleNamesToDeleteFiles) throws Exception {
		String directoryPath = PathUtil.formatPathForDirectoryWithoutLastSeparator(_directoryPath);
		if(directoryPath == null || directoryPath.length()==0){
			throw  new IllegalArgumentException("The parameter 'directoryPath' is null in method createDirectory " +
					" in "+this.getClass());
		}
		FolderOnServer fos = new FolderOnServer();
		fos.setPath(directoryPath);
		fos.setCdd(grantedIvyRoleNamesToDeleteDirectory);
		fos.setCdf(grantedIvyRoleNamesToDeleteFiles);
		fos.setCmrd(grantedIvyRoleNamesToManageRights);
		fos.setCod(grantedIvyRoleNamesToOpenDirectory);
		fos.setCud(grantedIvyRoleNamesToUpdateDirectory);
		fos.setCwf(grantedIvyRoleNamesToWriteFiles);
		fos.setIs_protected(true);
		fos.setPath(directoryPath);
		return this.createDirectory(fos);
	}

	@Override
	public FolderOnServer createDirectory(FolderOnServer fos) throws Exception {
		if(fos == null || fos.getPath() == null || fos.getPath().length()==0){
			throw  new IllegalArgumentException("The parameter FolderOnServer is invalid in method createDirectory " +
					" in "+this.getClass());
		}
		fos.setPath(PathUtil.formatPathForDirectoryWithoutLastSeparator(fos.getPath()));
		String parent = PathUtil.getParentDirectoryPath(fos.getPath());
		if(parent!=null && parent.trim().length()>0) {
			if(!this.directoryExists(parent)) {
				// the parent does not exit, we have to create it.
				FolderOnServer fosp = new FolderOnServer();
				fosp.setPath(parent);
				fosp.setCcd(fos.getCcd());
				fosp.setCcf(fos.getCcf());
				fosp.setCdd(fos.getCdd());
				fosp.setCdf(fos.getCdf());
				fosp.setCmrd(fos.getCmrd());
				fosp.setCod(fos.getCod());
				fosp.setCrd(fos.getCrd());
				fosp.setCtd(fos.getCtd());
				fosp.setCud(fos.getCud());
				fosp.setCuf(fos.getCuf());
				fosp.setCwf(fos.getCwf());
				this.createDirectory(fosp );
			}
		}
		fos = this.ensureRightsIntegrityInDirectory(fos);
		fos.setName(PathUtil.getDirectoryNameFromPath(fos.getPath()));
		fos = this.dirPersistence.create(fos);
		return fos;
	}

	@Override
	public FolderOnServer createDirectoryWithParentSecurity(
			String _directoryPath) throws Exception {
		String directoryPath = PathUtil.formatPathForDirectoryWithoutLastSeparator(_directoryPath);
		if(directoryPath == null || directoryPath.length()==0){
			throw  new IllegalArgumentException("The parameter 'directoryPath' is invalid in method createDirectoryWithParentSecurity " +
					" in "+this.getClass());
		}
		if(!directoryPath.contains("/"))
		{//the directory has no parent, it is a root directory
			return this.createIndestructibleDirectory(directoryPath, null);
		}
		String parentPath = PathUtil.getParentDirectoryPath(directoryPath);
		if(!this.directoryExists(parentPath))
		{//creates recursively all the missing parents
			this.createDirectoryWithParentSecurity(parentPath);
		}
		FolderOnServer parent = this.getDirectoryWithPath(parentPath);
		parent.setPath(directoryPath);
		parent.setName(PathUtil.getDirectoryNameFromPath(directoryPath));
		parent = this.createDirectory(parent);
		return parent;
	}

	@Override
	public FolderOnServer createDirectoryWithUserAsRightsGuideline(
			String _directoryPath, String ivyUserName) throws Exception {
		String directoryPath = PathUtil.formatPathForDirectoryWithoutLastSeparator(_directoryPath);
		if(directoryPath == null || directoryPath.length()==0){
			throw  new IllegalArgumentException("The parameter 'directoryPath' is null in method  createDirectoryWithUserAsRightsGuideline " +
					" in "+this.getClass());
		}
		if(ivyUserName==null || ivyUserName.trim().length()==0)
		{
			throw  new IllegalArgumentException("The parameter 'ivyUserName' is null in method  createDirectoryWithUserAsRightsGuideline " +
					" in "+this.getClass());
		}
		IUser user=null;
		try{
			user = Ivy.session().getSecurityContext().findUser(ivyUserName);
		}catch (Exception ex){
			throw  new Exception("Cannot get the Ivy User associated with the user name in method  createDirectoryWithUserAsRightsGuideline " +
					" in "+this.getClass() +" "+ex.getMessage());
		}
		if(user==null)
		{
			throw  new IllegalArgumentException("The parameter 'ivyUserName' is invalid in method  createDirectoryWithUserAsRightsGuideline " +
					" in "+this.getClass());
		}
		List<IRole> roles = List.create(IRole.class);
		roles.addAll(user.getRoles());
		List<String> roleNames = List.create(String.class);
		for(IRole r:roles){
			if(!r.getName().equalsIgnoreCase("Everybody"))
			{
				roleNames.add(r.getName());
			}
		}
		return this.createIndestructibleDirectory(directoryPath, roleNames);
	}

	@Override
	public FolderOnServer getDirectoryWithPath(String _directoryPath)
			throws Exception {
		FolderOnServer fos = this.dirPersistence.get(_directoryPath);
		if(this.config.isActivateSecurity()) {
			//this.securityController.getUserRightsInFolderOnServer(fos, Ivy.session().getSessionUserName());
			this.getUserRightsOnFolderOnServer(fos, Ivy.session().getSessionUser());
		}
		return fos;
	}

	@Override
	public FolderOnServer saveFolderOnServer(FolderOnServer fos)
			throws Exception {
		if(fos == null || fos.getPath() == null || fos.getPath().length()==0){
			throw  new IllegalArgumentException("The parameter FolderOnServer is invalid in method createDirectory " +
					" in "+this.getClass());
		}
		boolean exists = this.directoryExists(fos.getPath());
		if(exists) {
			Ivy.log().debug("Dir to save in FileStoreDBHandler: {0}",fos);
			return this.dirPersistence.update(fos);
		} else {
			return this.createDirectory(fos);
		}
	}

	@Override
	public ArrayList<FolderOnServer> getListDirectoriesUnderPathWithSecurityInfos(
			String rootPath, String ivyUserName) throws Exception {

		ArrayList<FolderOnServer> al = new ArrayList<FolderOnServer>();

		//first we get all the directories with their security Info.
		FolderOnServer dir = this.dirPersistence.get(rootPath);
		if(dir==null) {
			return al;
		}
		IUser user = Ivy.session().getSecurityContext().findUser(ivyUserName);
		dir.setIsRoot(true);
		java.util.List<FolderOnServer> l = this.dirPersistence.getList(rootPath, true);

		al.add(dir);
		al.addAll(l);

		for(FolderOnServer d:al) {
			this.getUserRightsWithSecurityHandlerChain(d,user);
		}

		ArrayList<FolderOnServer> dirs2 =new ArrayList<FolderOnServer>();

		for(FolderOnServer fos: al) {
			if(!fos.getCanUserOpenDir()) {
				LockedFolder fos1 = new LockedFolder(fos);
				dirs2.add(fos1);
			} else {
				dirs2.add(fos);
			}
		}

		return dirs2;
	}

	private void getUserRightsWithSecurityHandlerChain(FolderOnServer fos, IUser user) {
		if(fos==null) {
			return;
		}
		fos.setCanUserCreateDirectory(this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.CREATE_DIRECTORY_RIGHT, fos, user, null).isAllow());
		fos.setCanUserCreateFiles(this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.CREATE_FILES_RIGHT, fos, user, null).isAllow());
		fos.setCanUserDeleteDir(this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.DELETE_DIRECTORY_RIGHT, fos, user, null).isAllow());
		fos.setCanUserDeleteFiles(this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.DELETE_FILES_RIGHT, fos, user, null).isAllow());
		fos.setCanUserManageRights(this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.MANAGE_SECURITY_RIGHT, fos, user, null).isAllow());
		fos.setCanUserOpenDir(this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.OPEN_DIRECTORY_RIGHT, fos, user, null).isAllow());
		fos.setCanUserRenameDirectory(this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.RENAME_DIRECTORY_RIGHT, fos, user, null).isAllow());
		fos.setCanUserTranslateDirectory(this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.TRANSLATE_DIRECTORY_RIGHT, fos, user, null).isAllow());
		fos.setCanUserUpdateDir(this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.UPDATE_DIRECTORY_RIGHT, fos, user, null).isAllow());
		fos.setCanUserUpdateFiles(this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.UPDATE_FILES_RIGHT, fos, user, null).isAllow());
		fos.setCanUserWriteFiles(this.config.getSecurityHandler().hasRight(null, SecurityRightsEnum.WRITE_FILES_RIGHT, fos, user, null).isAllow());
	}

	@Override
	public String getFileManagerAdminRoleName() throws Exception {
		return this.config.getAdminRole();
	}

	@Override
	public boolean isUserFileManagerAdmin(String ivyUserName) throws Exception {
		if (ivyUserName == null || ivyUserName.trim().length() == 0) {
			throw new IllegalArgumentException(
					"The parameter is not set in method isUserFileManagerAdmin(String ivyUserName) "
							+ " in " + this.getClass());
		}
		if (this.getFileManagerAdminRoleName() == null || this.getFileManagerAdminRoleName().length() == 0) {
			return false;
		}
		List<IRole> userRoles = List.create(IRole.class);
		try {
			userRoles.addAll(Ivy.wf().getSecurityContext()
					.findUser(ivyUserName).getAllRoles());
		} catch (Throwable t) {
		}
		boolean found = false;
		if (userRoles == null || userRoles.isEmpty()) {
			Ivy.log().debug("isUserFileManagerAdmin user roles empty");
			return false;
		} else {
			for (IRole r : userRoles) {
				if (r.getName().equals(this.getFileManagerAdminRoleName())) {
					found = true;
					break;
				}
			}
		}
		return found;
	}

	/**
	 * Deprecated: use PathUtil.getListFromString(s, list_sep) instead.
	 * transforms a String that represents a list of token separated with a delimiter into a List<String>
	 * @param s: the String 
	 * @param list_sep: the delimiter
	 * @return the List<String>
	 */
	@Deprecated
	public List<String> getListFromString(String s, String list_sep){
		return PathUtil.getListFromString(s, list_sep);
	}

	private FolderOnServer ensureRightsIntegrityInDirectory(FolderOnServer fos) {
		return DirectorySecurityUtil.ensureRightsIntegrityInDirectory(config.getAdminRole(), fos);
	}

	/**
	 * Takes a list of names of Ivy Roles and:<br>
	 * . Creates a new List<String> if this List of names of Ivy Roles is null,
	 * . Adds the administrator Role name returned by this.getFileManagerAdminRoleName() in the list if it is not already in it.
	 * @param roles: the initial List<String> of names of Ivy Roles
	 * @return the List<String> of names of Ivy Roles containing the administrator Role name if it exists.
	 */
	private List<String> ensureAdminRoleInList(List<String> roles) {
		try {
			return DirectorySecurityUtil.putRoleInListIfNotPresent(this.getFileManagerAdminRoleName(), roles);
		} catch (Exception e) {
		}
		return roles;

	}

	@Override
	public String getEscapeChar() {
		if(this.docPersistence instanceof DocumentOnServerSQLPersistence) {
			return ((DocumentOnServerSQLPersistence) this.docPersistence).getEscapeChar();
		} else {
			return super.getEscapeChar();
		}
	}



	@Override
	public AbstractFileActionHistoryController getFileActionHistoryController() {
		return this.fileActionHistoryController;
	}

	/**
	 * Not public.
	 * @param fvc
	 */
	protected void setFileVersioningController(AbstractFileVersioningController fvc) {
		if(fvc!=null) {
			this.fileVersionController = fvc;
		}
	}

	/**
	 * Not public
	 * @param activateFileVersioning
	 */
	protected void setActivateFileVersioning(boolean activateFileVersioning) {
		this.activateFileVersioning= activateFileVersioning;
	}

	/**
	 * Not public.
	 * @param dirPersistence
	 */
	protected void setDirPersistence(IFolderOnServerPersistence dirPersistence) {
		if(dirPersistence!=null) {
			this.dirPersistence=dirPersistence;
		}
	}

	/**
	 * Not Public.
	 * @param docPersistence
	 */
	protected void setDocPersistence(IDocumentOnServerPersistence docPersistence) {
		if(docPersistence!=null) {
			this.docPersistence=docPersistence;
		}
	}

	private void getUserRightsOnFolderOnServer(FolderOnServer fos, IUser user) {
		if(fos==null) {
			return;
		}
		fos.setCanUserCreateDirectory(this.securityController.hasRight(null, 
				SecurityRightsEnum.CREATE_DIRECTORY_RIGHT, fos, user, null).isAllow());
		fos.setCanUserCreateFiles(this.securityController.hasRight(null, 
				SecurityRightsEnum.CREATE_FILES_RIGHT, fos, user, null).isAllow());
		fos.setCanUserDeleteDir(this.securityController.hasRight(null, 
				SecurityRightsEnum.DELETE_DIRECTORY_RIGHT, fos, user, null).isAllow());
		fos.setCanUserDeleteFiles(this.securityController.hasRight(null, 
				SecurityRightsEnum.DELETE_FILES_RIGHT, fos, user, null).isAllow());
		fos.setCanUserManageRights(this.securityController.hasRight(null, 
				SecurityRightsEnum.MANAGE_SECURITY_RIGHT, fos, user, null).isAllow());
		fos.setCanUserOpenDir(this.securityController.hasRight(null, 
				SecurityRightsEnum.OPEN_DIRECTORY_RIGHT, fos, user, null).isAllow());
		fos.setCanUserRenameDirectory(this.securityController.hasRight(null, 
				SecurityRightsEnum.RENAME_DIRECTORY_RIGHT, fos, user, null).isAllow());
		fos.setCanUserTranslateDirectory(this.securityController.hasRight(null, 
				SecurityRightsEnum.TRANSLATE_DIRECTORY_RIGHT, fos, user, null).isAllow());
		fos.setCanUserUpdateDir(this.securityController.hasRight(null, 
				SecurityRightsEnum.UPDATE_DIRECTORY_RIGHT, fos, user, null).isAllow());
		fos.setCanUserUpdateFiles(this.securityController.hasRight(null, 
				SecurityRightsEnum.UPDATE_FILES_RIGHT, fos, user, null).isAllow());
		fos.setCanUserWriteFiles(this.securityController.hasRight(null, 
				SecurityRightsEnum.WRITE_FILES_RIGHT, fos, user, null).isAllow());
	}
}
