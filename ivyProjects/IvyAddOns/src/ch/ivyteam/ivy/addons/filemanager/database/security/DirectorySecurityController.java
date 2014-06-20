/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.security;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import ch.ivyteam.ivy.addons.filemanager.FileManagementHandlersFactory;
import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.AbstractFileSecurityHandler;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;
import ch.ivyteam.ivy.environment.EnvironmentNotAvailableException;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.IUser;



/**
 * @author Emmanuel Comba<br>
 * Allows controlling the security feature in the file management.
 * The Security feature is explained in the IvyAddons documentation.
 */
public class DirectorySecurityController extends AbstractDirectorySecurityController {

	private String ivyDBConnectionName = null; // the user friendly connection name to Database in Ivy

	private String dirTableName = null; // the table that stores directories infos
	private String schemaName=null;
	private String securityAdmin="";
	private int secVersion=1;
	private AbstractFileSecurityHandler fileSecurityHandler =null;
	private BasicConfigurationController config;


	/**
	 * empty default constructor
	 */
	public DirectorySecurityController() {
		this(null,null,null);
	}
	
	/**
	 * Constructor
	 * @param _ivyDBConnectionName
	 * @param _dirTableName
	 * @param _schemaName
	 * @param _securityAdmin
	 */
	public DirectorySecurityController(String _ivyDBConnectionName,
			String _dirTableName, String _schemaName, String _securityAdmin) {
		this.ivyDBConnectionName = _ivyDBConnectionName;
		this.dirTableName = _dirTableName;
		this.schemaName = _schemaName;
		this.securityAdmin = _securityAdmin;
		this.makeConfigurationObject();
		this.computeSecVersion();
	}

	/**
	 * Constructor
	 * @param _ivyDBConnectionName
	 * @param _dirTableName
	 * @param _schemaName
	 */
	public DirectorySecurityController(String _ivyDBConnectionName,
			String _dirTableName, String _schemaName) {
		super();
		if(_ivyDBConnectionName==null || _ivyDBConnectionName.trim().length()==0)
		{//if ivy user friendly name of database configuration not set used default
			this.ivyDBConnectionName = Ivy.var().get("xivy_addons_fileManager_ivyDatabaseConnectionName");
		}else{
			this.ivyDBConnectionName = _ivyDBConnectionName.trim();
		}
		if(_dirTableName==null || _dirTableName.trim().length()==0)
		{//if ivy table name not set used default
			this.dirTableName=Ivy.var().get("xivy_addons_fileManager_directoriesTableName");
		}else{
			this.dirTableName=_dirTableName.trim();
		}
		if(_schemaName!=null && _schemaName.trim().length()>0)
		{//set the schema name variable

			this.schemaName = _schemaName.trim();

		}else if(Ivy.var().get("xivy_addons_fileManager_databaseSchemaName")!=null && 
				Ivy.var().get("xivy_addons_fileManager_databaseSchemaName").trim().length()>0)
		{
			this.schemaName=Ivy.var().get("xivy_addons_fileManager_databaseSchemaName").trim();
		}

		try{
			this.securityAdmin=Ivy.var().get("xivy_addons_fileManager_admin_roleName");
		}catch(EnvironmentNotAvailableException ex)
		{
			Ivy.log().error("Error in constructor DirectorySecurityController. "+ex.getMessage(),ex);
		}
		this.makeConfigurationObject();
		
		this.computeSecVersion();
	}

	private void makeConfigurationObject() {
		this.config = new BasicConfigurationController();
		config.setStoreFilesInDB(true);
		config.setIvyDBConnectionName(this.ivyDBConnectionName);
		config.setActivateSecurity(true);
		config.setDatabaseSchemaName(this.schemaName);
		config.setDirectoriesTableName(this.dirTableName);
		config.setAdminRole(this.securityAdmin);
	}
	
	private void makeFileSecurityHandlerIfNull() throws Exception {
		if(this.fileSecurityHandler==null){
			this.fileSecurityHandler = FileManagementHandlersFactory.getFileSecurityHandlerInstance(config,this);
		}
	}

	/**
	 * Compute the internal security version. 
	 */
	private void computeSecVersion() {
		IPersistenceConnectionManager<?> conManager=null;
		ResultSet rst = null;
		try {
			conManager = PersistenceConnectionManagerFactory.getPersistenceConnectionManagerInstance(this.config);
			if(conManager.getConnection() instanceof java.sql.Connection){
				DatabaseMetaData dbmd = ((java.sql.Connection) conManager.getConnection()).getMetaData();
				rst = dbmd.getColumns(null, this.schemaName, this.dirTableName, "ctd");
				if(rst.next()){
					this.secVersion=2;
				}
			}
		} catch (Exception e) {
			//Ivy.log().error("Error while computing the internal security version in computeSecVersion from DirectorySecurityController. "+e.getMessage(),e);
		}finally {
			if(rst!=null) {
				try {
					rst.close();
				} catch (SQLException e) {
					Ivy.log().error("DirectorySecurityController computeSecVersion error closing ResultSet "+e.getMessage(),e);
				}
			}
			if(conManager!=null ) {
				try {
					conManager.closeConnection();
				} catch (Exception e) {
					//Ivy.log().error("Could not release the connection Manager in computeSecVersion from DirectorySecurityController.", e);
				}
			}
		}
	}
	
	/**
	 * Returns the security version. This version is internally computed.
	 * @return
	 */
	public int getSecurityVersion(){
		return this.secVersion;
	}

	private SecurityRightsEnum getRight(int type) {
		SecurityRightsEnum right = null;
		switch(type){
		case AbstractDirectorySecurityController.CREATE_DIRECTORY_RIGHT:
			right =  SecurityRightsEnum.CREATE_DIRECTORY_RIGHT;
			break;
		case AbstractDirectorySecurityController.CREATE_FILES_RIGHT:
			right = SecurityRightsEnum.CREATE_FILES_RIGHT;
			break;
		case AbstractDirectorySecurityController.DELETE_DIRECTORY_RIGHT:
			right = SecurityRightsEnum.DELETE_DIRECTORY_RIGHT;
			break;
		case AbstractDirectorySecurityController.DELETE_FILES_RIGHT:
			right = SecurityRightsEnum.DELETE_FILES_RIGHT;
			break;
		case AbstractDirectorySecurityController.MANAGE_SECURITY_RIGHT:
			right = SecurityRightsEnum.MANAGE_SECURITY_RIGHT;
			break;
		case AbstractDirectorySecurityController.OPEN_DIRECTORY_RIGHT:
			right = SecurityRightsEnum.OPEN_DIRECTORY_RIGHT;
			break;
		case AbstractDirectorySecurityController.RENAME_DIRECTORY_RIGHT:
			right = SecurityRightsEnum.RENAME_DIRECTORY_RIGHT;
			break;
		case AbstractDirectorySecurityController.TRANSLATE_DIRECTORY_RIGHT:
			right = SecurityRightsEnum.TRANSLATE_DIRECTORY_RIGHT;
			break;
		case AbstractDirectorySecurityController.UPDATE_DIRECTORY_RIGHT:
			right = SecurityRightsEnum.UPDATE_DIRECTORY_RIGHT;
			break;
		case AbstractDirectorySecurityController.UPDATE_FILES_RIGHT:
			right = SecurityRightsEnum.UPDATE_FILES_RIGHT;
			break;
		case AbstractDirectorySecurityController.WRITE_FILES_RIGHT:
			right = SecurityRightsEnum.WRITE_FILES_RIGHT;
			break;
		default:
			right = SecurityRightsEnum.OPEN_DIRECTORY_RIGHT;
		}
		return right;
	}
	
	@Override
	@Deprecated
	public List<String> AddRightOnDirectoryForIvyRole(String path,
			int rightType, String ivyRoleName) throws Exception {
		
		makeFileSecurityHandlerIfNull();
		return this.fileSecurityHandler.AddRightOnDirectoryForIvyRole(path, getRight(rightType), ivyRoleName);
		
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#addRoleToDeleteDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	public boolean addRoleToDeleteDirectory(String path, String ivyRoleName)
			throws Exception {
		makeFileSecurityHandlerIfNull();
		this.fileSecurityHandler.AddRightOnDirectoryForIvyRole(path, SecurityRightsEnum.DELETE_DIRECTORY_RIGHT, ivyRoleName);
		return true;
		
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#addRoleToDeleteFilesInDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	public boolean addRoleToDeleteFilesInDirectory(String path,
			String ivyRoleName) throws Exception {
		makeFileSecurityHandlerIfNull();
		this.fileSecurityHandler.AddRightOnDirectoryForIvyRole(path, SecurityRightsEnum.DELETE_FILES_RIGHT, ivyRoleName);
		return true;
		
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#addRoleToEditFilesInDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	public boolean addRoleToEditFilesInDirectory(String path, String ivyRoleName)
			throws Exception {
		makeFileSecurityHandlerIfNull();
		this.fileSecurityHandler.AddRightOnDirectoryForIvyRole(path, SecurityRightsEnum.WRITE_FILES_RIGHT, ivyRoleName);
		return true;
		
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#addRoleToManageDirectorySecurity(java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	public boolean addRoleToManageDirectorySecurity(String path,
			String ivyRoleName) throws Exception {
		makeFileSecurityHandlerIfNull();
		this.fileSecurityHandler.AddRightOnDirectoryForIvyRole(path, SecurityRightsEnum.MANAGE_SECURITY_RIGHT, ivyRoleName);
		return true;
		
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#addRoleToOpenDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	public boolean addRoleToOpenDirectory(String path, String ivyRoleName)
			throws Exception {
		makeFileSecurityHandlerIfNull();
		this.fileSecurityHandler.AddRightOnDirectoryForIvyRole(path, SecurityRightsEnum.OPEN_DIRECTORY_RIGHT, ivyRoleName);
		return true;
		
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#addRoleToUpdateDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	public boolean addRoleToUpdateDirectory(String path, String ivyRoleName)
			throws Exception {
		makeFileSecurityHandlerIfNull();
		this.fileSecurityHandler.AddRightOnDirectoryForIvyRole(path, SecurityRightsEnum.UPDATE_DIRECTORY_RIGHT, ivyRoleName);
		return true;
		
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#canRoleDeleteDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean canRoleDeleteDirectory(String path, String ivyRoleName)
			throws Exception {
		path=formatPathForDirectoryWithoutLastSeparator(path);
		if(path==null || path.length()==0 || ivyRoleName==null || ivyRoleName.trim().length()==0)
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method canRoleDeleteDirectory(String path, String ivyRoleName) " +
					" in "+this.getClass());
		}
		if(ivyRoleName.trim().equals(this.securityAdmin))
		{
			return true;
		}
		List<String> roles = this.getRolesNamesAllowedForRightOnDirectory(path, DELETE_DIRECTORY_RIGHT);
		if(roles.contains(ivyRoleName.trim())){
			return true;
		}else{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#canRoleDeleteFilesInDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean canRoleDeleteFilesInDirectory(String path, String ivyRoleName)
			throws Exception {
		path=formatPathForDirectoryWithoutLastSeparator(path);
		if(path==null || path.length()==0 || ivyRoleName==null || ivyRoleName.trim().length()==0)
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method canRoleDeleteFilesInDirectory(String path, String ivyRoleName) " +
					" in "+this.getClass());
		}
		if(ivyRoleName.trim().equals(this.securityAdmin))
		{
			return true;
		}
		List<String> roles = this.getRolesNamesAllowedForRightOnDirectory(path, DELETE_FILES_RIGHT);
		if(roles.contains(ivyRoleName.trim())){
			return true;
		}else{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#canRoleEditFilesInDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean canRoleEditFilesInDirectory(String path, String ivyRoleName)
			throws Exception {
		path=formatPathForDirectoryWithoutLastSeparator(path);
		if(path==null || path.length()==0 || ivyRoleName==null || ivyRoleName.trim().length()==0)
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method canRoleEditFilesInDirectory(String path, String ivyRoleName) " +
					" in "+this.getClass());
		}
		if(ivyRoleName.trim().equals(this.securityAdmin))
		{
			return true;
		}
		List<String> roles = this.getRolesNamesAllowedForRightOnDirectory(path, WRITE_FILES_RIGHT);
		if(roles.contains(ivyRoleName.trim())){
			return true;
		}else{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#canRoleManageDirectorySecurity(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean canRoleManageDirectorySecurity(String path,
			String ivyRoleName) throws Exception {
		path=formatPathForDirectoryWithoutLastSeparator(path);
		if(path==null || path.length()==0 || ivyRoleName==null || ivyRoleName.trim().length()==0)
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method canRoleManageDirectorySecurity(String path, String ivyRoleName) " +
					" in "+this.getClass());
		}
		if(ivyRoleName.trim().equals(this.securityAdmin))
		{
			return true;
		}
		List<String> roles = this.getRolesNamesAllowedForRightOnDirectory(path, MANAGE_SECURITY_RIGHT);
		if(roles.contains(ivyRoleName.trim())){
			return true;
		}else{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#canRoleOpenDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean canRoleOpenDirectory(String path, String ivyRoleName)
			throws Exception {
		path=formatPathForDirectoryWithoutLastSeparator(path);
		if(path==null || path.length()==0 || ivyRoleName==null || ivyRoleName.trim().length()==0)
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method canRoleOpenDirectory(String path, String ivyRoleName) " +
					" in "+this.getClass());
		}
		if(ivyRoleName.trim().equals(this.securityAdmin))
		{
			return true;
		}
		List<String> roles = this.getRolesNamesAllowedForRightOnDirectory(path, OPEN_DIRECTORY_RIGHT);
		if(roles.contains(ivyRoleName.trim())){
			return true;
		}else{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#canRoleUpdateDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean canRoleUpdateDirectory(String path, String ivyRoleName)
			throws Exception {
		path=formatPathForDirectoryWithoutLastSeparator(path);
		if(path==null || path.length()==0 || ivyRoleName==null || ivyRoleName.trim().length()==0)
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method canRoleUpdateDirectory(String path, String ivyRoleName) " +
					" in "+this.getClass());
		}
		if(ivyRoleName.trim().equals(this.securityAdmin))
		{
			return true;
		}
		List<String> roles = this.getRolesNamesAllowedForRightOnDirectory(path, UPDATE_DIRECTORY_RIGHT);
		if(roles.contains(ivyRoleName.trim())){
			return true;
		}else{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#canUserDeleteDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean canUserDeleteDirectory(String path, String ivyUserName)
			throws Exception {
		path=formatPathForDirectoryWithoutLastSeparator(path);
		if(path==null || path.length()==0 || ivyUserName==null || ivyUserName.trim().length()==0)
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method canUserDeleteDirectory(String path, String ivyUserName) " +
					" in "+this.getClass());
		}
		if(this.isUserFileManagerAdmin(ivyUserName)){
			return true;
		}
		List <IRole> userRoles = List.create(IRole.class);
		try{
			userRoles.addAll(Ivy.wf().getSecurityContext().findUser(ivyUserName).getAllRoles());
		}catch(Throwable t){

		}
		boolean found = false;
		if(userRoles==null || userRoles.isEmpty()) {
			return false;
		}
		else{
			List <String> roles = List.create(String.class);
			roles=this.getRolesNamesAllowedForRightOnDirectory(path, DELETE_DIRECTORY_RIGHT);
			for(IRole r : userRoles)
			{
				if(roles.contains(r.getName()))
				{
					found = true;
					break;
				}
			}

		}
		return found;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#canUserDeleteFilesInDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean canUserDeleteFilesInDirectory(String path, String ivyUserName)
			throws Exception {
		path=formatPathForDirectoryWithoutLastSeparator(path);
		if(path==null || path.length()==0 || ivyUserName==null || ivyUserName.trim().length()==0)
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method canUserDeleteFilesInDirectory(String path, String ivyUserName) " +
					" in "+this.getClass());
		}
		if(this.isUserFileManagerAdmin(ivyUserName)){
			return true;
		}
		List <IRole> userRoles = List.create(IRole.class);
		try{
			userRoles.addAll(Ivy.wf().getSecurityContext().findUser(ivyUserName).getAllRoles());
		}catch(Throwable t){

		}
		boolean found = false;
		if(userRoles==null || userRoles.isEmpty())
		{
			return false;
		}
		else{
			List <String> roles = List.create(String.class);
			roles=this.getRolesNamesAllowedForRightOnDirectory(path, DELETE_FILES_RIGHT);
			for(IRole r : userRoles)
			{
				if(roles.contains(r.getName()))
				{
					found = true;
					break;
				}
			}

		}
		return found;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#canUserEditFilesInDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean canUserEditFilesInDirectory(String path, String ivyUserName)
			throws Exception {
		path=formatPathForDirectoryWithoutLastSeparator(path);
		if(path==null || path.length()==0 || ivyUserName==null || ivyUserName.trim().length()==0)
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method canUserEditFilesInDirectory(String path, String ivyUserName) " +
					" in "+this.getClass());
		}
		if(this.isUserFileManagerAdmin(ivyUserName)){
			return true;
		}
		List <IRole> userRoles = List.create(IRole.class);
		try{
			userRoles.addAll(Ivy.wf().getSecurityContext().findUser(ivyUserName).getAllRoles());
		}catch(Throwable t){

		}
		boolean found = false;
		if(userRoles==null || userRoles.isEmpty())
		{
			return false;
		}
		else{
			List <String> roles = List.create(String.class);
			roles=this.getRolesNamesAllowedForRightOnDirectory(path, WRITE_FILES_RIGHT);
			for(IRole r : userRoles)
			{
				if(roles.contains(r.getName()))
				{
					found = true;
					break;
				}
			}

		}
		return found;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#canUserManageDirectorySecurity(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean canUserManageDirectorySecurity(String path,
			String ivyUserName) throws Exception {
		path=formatPathForDirectoryWithoutLastSeparator(path);
		if(path==null || path.length()==0 || ivyUserName==null || ivyUserName.trim().length()==0)
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method canUserManageDirectorySecurity(String path, String ivyUserName) " +
					" in "+this.getClass());
		}
		if(this.isUserFileManagerAdmin(ivyUserName)){
			return true;
		}
		List <IRole> userRoles = List.create(IRole.class);
		try{
			userRoles.addAll(Ivy.wf().getSecurityContext().findUser(ivyUserName).getAllRoles());
		}catch(Throwable t){

		}
		boolean found = false;
		if(userRoles==null || userRoles.isEmpty())
		{
			return false;
		}
		else{
			List <String> roles = List.create(String.class);
			roles=this.getRolesNamesAllowedForRightOnDirectory(path,MANAGE_SECURITY_RIGHT);
			for(IRole r : userRoles)
			{
				if(roles.contains(r.getName()))
				{
					found = true;
					break;
				}
			}

		}
		return found;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#canUserOpenDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean canUserOpenDirectory(String path, String ivyUserName)
			throws Exception {
		path=formatPathForDirectoryWithoutLastSeparator(path);
		if(path==null || path.length()==0 || ivyUserName==null || ivyUserName.trim().length()==0)
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method canUserOpenDirectory(String path, String ivyUserName) " +
					" in "+this.getClass());
		}
		if(this.isUserFileManagerAdmin(ivyUserName)){
			return true;
		}
		List <IRole> userRoles = List.create(IRole.class);
		try{
			userRoles.addAll(Ivy.wf().getSecurityContext().findUser(ivyUserName).getAllRoles());
		}catch(Throwable t){

		}
		boolean found = false;
		if(userRoles==null || userRoles.isEmpty())
		{
			return false;
		}
		else{
			List <String> roles = List.create(String.class);
			roles=this.getRolesNamesAllowedForRightOnDirectory(path, OPEN_DIRECTORY_RIGHT);
			for(IRole r : userRoles)
			{
				if(roles.contains(r.getName()))
				{
					found = true;
					break;
				}
			}

		}
		return found;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#canUserUpdateDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean canUserUpdateDirectory(String path, String ivyUserName)
			throws Exception {
		path=formatPathForDirectoryWithoutLastSeparator(path);
		if(path==null || path.length()==0 || ivyUserName==null || ivyUserName.trim().length()==0)
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method canUserUpdateDirectory(String path, String ivyUserName) " +
					" in "+this.getClass());
		}
		if(this.isUserFileManagerAdmin(ivyUserName)){
			return true;
		}
		List <IRole> userRoles = List.create(IRole.class);
		try{
			userRoles.addAll(Ivy.wf().getSecurityContext().findUser(ivyUserName).getAllRoles());
		}catch(Throwable t){

		}
		boolean found = false;
		if(userRoles==null || userRoles.isEmpty())
		{
			return false;
		}
		else{
			List <String> roles = List.create(String.class);
			roles=this.getRolesNamesAllowedForRightOnDirectory(path, UPDATE_DIRECTORY_RIGHT);
			for(IRole r : userRoles)
			{
				if(roles.contains(r.getName()))
				{
					found = true;
					break;
				}
			}

		}
		return found;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#createDirectory(java.lang.String, ch.ivyteam.ivy.scripting.objects.List, ch.ivyteam.ivy.scripting.objects.List, ch.ivyteam.ivy.scripting.objects.List, ch.ivyteam.ivy.scripting.objects.List, ch.ivyteam.ivy.scripting.objects.List, ch.ivyteam.ivy.scripting.objects.List)
	 */
	@Override
	@Deprecated
	public FolderOnServer createDirectory(String directoryPath,
			List<String> grantedIvyRoleNamesToManageRights,
			List<String> grantedIvyRoleNamesToDeleteDirectory,
			List<String> grantedIvyRoleNamesToUpdateDirectory,
			List<String> grantedIvyRoleNamesToOpenDirectory,
			List<String> grantedIvyRoleNamesToWriteFiles,
			List<String> grantedIvyRoleNamesToDeleteFiles) throws Exception {

		directoryPath = formatPathForDirectoryWithoutLastSeparator(directoryPath);
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
		if(this.fileSecurityHandler!=null) {
			return this.fileSecurityHandler.createDirectory(fos);
		}
		return null;
	}
	
	@Override
	@Deprecated
	public FolderOnServer createDirectory(FolderOnServer fos) throws Exception {
		makeFileSecurityHandlerIfNull();
		return this.fileSecurityHandler.createDirectory(fos);
	}
	
	@Override
	@Deprecated
	public FolderOnServer saveFolderOnServer(FolderOnServer fos) throws Exception {
		makeFileSecurityHandlerIfNull();
		return this.fileSecurityHandler.saveFolderOnServer(fos);
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#createDirectoryWithParentSecurity(java.lang.String)
	 */
	@Override
	@Deprecated
	public FolderOnServer createDirectoryWithParentSecurity(String directoryPath)
			throws Exception {
		makeFileSecurityHandlerIfNull();
		return this.fileSecurityHandler.createDirectoryWithParentSecurity(directoryPath);
	}

	@Override
	@Deprecated
	public FolderOnServer createDirectoryWithUserAsRightsGuideline(
			String directoryPath, String ivyUserName) throws Exception {
		makeFileSecurityHandlerIfNull();
		return this.fileSecurityHandler.createDirectoryWithUserAsRightsGuideline(directoryPath,ivyUserName);
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#createIndestructibleDirectory(java.lang.String, ch.ivyteam.ivy.scripting.objects.List)
	 */
	@Override
	@Deprecated
	public FolderOnServer createIndestructibleDirectory(String directoryPath,
			List<String> allowedIvyRoleNames) throws Exception {
		makeFileSecurityHandlerIfNull();
		return this.fileSecurityHandler.createIndestructibleDirectory(directoryPath,allowedIvyRoleNames);
	}

	/**
	 * Takes a list of names of Ivy Roles and:<br>
	 * . Creates a new List<String> if this List of names of Ivy Roles is null,
	 * . Adds the administrator Role name returned by this.getFileManagerAdminRoleName() in the list if it is not already in it.
	 * @param roles: the initial List<String> of names of Ivy Roles
	 * @return the List<String> of names of Ivy Roles containing the administrator Role name if it exists.
	 */
	public List<String> ensureAdminRoleInList(List<String> roles)
	{
		if(roles==null)
		{
			roles=List.create(String.class);
		}
		String admin =null;
		try{
			admin = this.getFileManagerAdminRoleName();
		}catch(Throwable t){

		}
		if(admin!=null && !roles.contains(admin))
		{
			roles.add(admin);
		}
		return roles;

	}

	/**
	 * Ensure the rights integrity is followed in the different rights lists of the given FolderOnServer
	 * @param fos the directory represented by a ch.ivyteam.ivy.addons.filemanager.FolderOnServer object
	 * @return the ch.ivyteam.ivy.addons.filemanager.FolderOnServer object which rights lists follow the following rules:
	 * <ul>
	 * <li> If a Role is in the admin list, then it will be present in all the other lists (it has all the rights),
	 * <li> If a Role is in the "delete directory list", then it will be present in the "open directory" and "update directory" lists,
	 * <li> If a Role is in the "update directory list", then it will be present in the "open directory" list,
	 * <li> If a Role is in the "delete files list", then it will be present in the "open directory" and "write files" lists,
	 * <li> If a Role is in the "write files list", then it will be present in the "open directory".
	 * </ul>
	 * If the given FolderOnServer was null, then null is returned.
	 */
	public FolderOnServer ensureRightsIntegrityInDirectory(FolderOnServer fos) {
		if (fos == null) {
			return null;
		}
		// check the roles lists objects
		if (fos.getCmrd() == null) {
			fos.setCmrd(List.create(String.class));
		}
		if (fos.getCod() == null) {
			fos.setCod(List.create(String.class));
		}
		if (fos.getCud() == null) {
			fos.setCud(List.create(String.class));
		}
		if (fos.getCdd() == null) {
			fos.setCdd(List.create(String.class));
		}
		if (fos.getCdf() == null) {
			fos.setCdf(List.create(String.class));
		}
		if (fos.getCwf() == null) {
			fos.setCwf(List.create(String.class));
		}
		if (fos.getCcd() == null) {
			fos.setCcd(List.create(String.class));
		}
		if (fos.getCrd() == null) {
			fos.setCrd(List.create(String.class));
		}
		if (fos.getCtd() == null) {
			fos.setCtd(List.create(String.class));
		}
		if (fos.getCcf() == null) {
			fos.setCcf(List.create(String.class));
		}
		if (fos.getCuf() == null) {
			fos.setCuf(List.create(String.class));
		}

		// CHeck integrity in the rights

		// ensure admin is in it + administrators should have all the rights
		fos.setCmrd(ensureAdminRoleInList(fos.getCmrd()));
		for (String s : fos.getCmrd()) {
			if (!fos.getCdd().contains(s)) {
				fos.getCdd().add(s);
			}
			if (!fos.getCud().contains(s)) {
				fos.getCud().add(s);
			}
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
			if (!fos.getCdf().contains(s)) {
				fos.getCdf().add(s);
			}
			if (!fos.getCwf().contains(s)) {
				fos.getCwf().add(s);
			}
			if (!fos.getCcd().contains(s)) {
				fos.getCcd().add(s);
			}
			if (!fos.getCrd().contains(s)) {
				fos.getCrd().add(s);
			}
			if (!fos.getCtd().contains(s)) {
				fos.getCtd().add(s);
			}
			if (!fos.getCcf().contains(s)) {
				fos.getCcf().add(s);
			}
			if (!fos.getCuf().contains(s)) {
				fos.getCuf().add(s);
			}
		}

		// Who can delete a directory should be able to open and update it, and if old security version rename it
		for (String s : fos.getCdd()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
			if (!fos.getCud().contains(s)) {
				fos.getCud().add(s);
			}
			if(this.secVersion==1) {
				if (!fos.getCrd().contains(s)) {
					fos.getCrd().add(s);
				}
			}
		}
		
		// Who can translate a directory should be able to open, rename it
		for (String s : fos.getCtd()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
			if (!fos.getCrd().contains(s)) {
				fos.getCrd().add(s);
			}
		}

		// Who can rename a directory should be able to open
		for (String s : fos.getCrd()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
		}

		// Who can create a directory should be able to open and update it
		for (String s : fos.getCcd()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
		}

		// Who can update a directory should be able to open it and if old security version rename and create it
		for (String s : fos.getCud()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
			if(this.secVersion==1) {
				if (!fos.getCrd().contains(s)) {
					fos.getCrd().add(s);
				}
				if (!fos.getCcd().contains(s)) {
					fos.getCcd().add(s);
				}
			}
		}
		
		// Who can create a file should be able to open the directory
		for (String s : fos.getCcf()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
		}
		
		// Who can create a file should be able to open the directory
		for (String s : fos.getCuf()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
		}

		// Who can delete a file should be able to update it and to open the
		// directory
		for (String s : fos.getCdf()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
			if (!fos.getCwf().contains(s)) {
				fos.getCwf().add(s);
			}
			if (!fos.getCcf().contains(s)) {
				fos.getCcf().add(s);
			}
			if (!fos.getCuf().contains(s)) {
				fos.getCuf().add(s);
			}
		}

		// Who can write a file should be able to open the directory and if old security version rename and create it
		for (String s : fos.getCwf()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
			if(this.secVersion==1) {
				if (!fos.getCuf().contains(s)) {
					fos.getCuf().add(s);
				}
				if (!fos.getCcf().contains(s)) {
					fos.getCcf().add(s);
				}
			}
		}

		return fos;
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#getFileManagerAdminRoleName()
	 */
	@Override
	public String getFileManagerAdminRoleName() throws Exception {
		return this.securityAdmin;
	}
	
	public void setFileManagerAdminRoleName(String adminRole) {
		if(adminRole !=null && !adminRole.isEmpty()) {
			this.securityAdmin = adminRole;
		}
	}

	public boolean containsAdminRole(List<IRole> userRoles) {
		if (this.securityAdmin == null || this.securityAdmin.length() == 0) {
			return false;
		}
		boolean found = false;
		if (userRoles == null || userRoles.isEmpty()) {
			return false;
		} else {
			for (IRole r : userRoles) {
				if (r.getName().equals(this.securityAdmin)) {
					found = true;
					break;
				}
			}
		}
		return found;
	}
	
	@Override
	public boolean isUserFileManagerAdmin(String ivyUserName) throws Exception {
		if (ivyUserName == null || ivyUserName.trim().length() == 0) {
			throw new IllegalArgumentException(
					"The parameter is not set in method isUserFileManagerAdmin(String ivyUserName) "
							+ " in " + this.getClass());
		}
		List<IRole> userRoles = List.create(IRole.class);
		try {
			userRoles.addAll(Ivy.wf().getSecurityContext()
					.findUser(ivyUserName).getAllRoles());
		} catch (Throwable t) {
		}
		return containsAdminRole(userRoles);
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#removeRightOnDirectory(java.lang.String, int, ch.ivyteam.ivy.scripting.objects.List)
	 */
	@Override
	@Deprecated
	public List<String> removeRightOnDirectory(String path, int rightType,
			List<String> disallowedIvyRoleNames) throws Exception {
		makeFileSecurityHandlerIfNull();
		return this.fileSecurityHandler.removeRightOnDirectory(path, this.getRight(rightType),disallowedIvyRoleNames);
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#removeRoleFromDeleteDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	public boolean removeRoleFromDeleteDirectory(String path, String ivyRoleName)
			throws Exception {
		makeFileSecurityHandlerIfNull();
		List<String> l = List.create(String.class);
		l.add(ivyRoleName);
		List<String> l1 = this.fileSecurityHandler.removeRightOnDirectory(path, SecurityRightsEnum.DELETE_DIRECTORY_RIGHT,l);
		return !l1.contains(ivyRoleName);
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#removeRoleFromDeleteFilesInDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	public boolean removeRoleFromDeleteFilesInDirectory(String path,
			String ivyRoleName) throws Exception {
		makeFileSecurityHandlerIfNull();
		List<String> l = List.create(String.class);
		l.add(ivyRoleName);
		List<String> l1 = this.fileSecurityHandler.removeRightOnDirectory(path, SecurityRightsEnum.DELETE_FILES_RIGHT,l);
		return !l1.contains(ivyRoleName);
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#removeRoleFromEditFilesInDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	public boolean removeRoleFromEditFilesInDirectory(String path,
			String ivyRoleName) throws Exception {
		makeFileSecurityHandlerIfNull();
		List<String> l = List.create(String.class);
		l.add(ivyRoleName);
		List<String> l1 = this.fileSecurityHandler.removeRightOnDirectory(path, SecurityRightsEnum.WRITE_FILES_RIGHT,l);
		return !l1.contains(ivyRoleName);
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#removeRoleFromManageDirectorySecurity(java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	public boolean removeRoleFromManageDirectorySecurity(String path,
			String ivyRoleName) throws Exception {
		makeFileSecurityHandlerIfNull();
		List<String> l = List.create(String.class);
		l.add(ivyRoleName);
		List<String> l1 = this.fileSecurityHandler.removeRightOnDirectory(path, SecurityRightsEnum.MANAGE_SECURITY_RIGHT,l);
		return !l1.contains(ivyRoleName);
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#removeRoleFromOpenDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	public boolean removeRoleFromOpenDirectory(String path, String ivyRoleName)
			throws Exception {
		makeFileSecurityHandlerIfNull();
		List<String> l = List.create(String.class);
		l.add(ivyRoleName);
		List<String> l1 = this.fileSecurityHandler.removeRightOnDirectory(path, SecurityRightsEnum.OPEN_DIRECTORY_RIGHT,l);
		return !l1.contains(ivyRoleName);
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#removeRoleFromUpdateDirectory(java.lang.String, java.lang.String)
	 */
	@Override
	@Deprecated
	public boolean removeRoleFromUpdateDirectory(String path, String ivyRoleName)
			throws Exception {
		makeFileSecurityHandlerIfNull();
		List<String> l = List.create(String.class);
		l.add(ivyRoleName);
		List<String> l1 = this.fileSecurityHandler.removeRightOnDirectory(path, SecurityRightsEnum.UPDATE_DIRECTORY_RIGHT,l);
		return !l1.contains(ivyRoleName);
	}

	/* (non-Javadoc)
	 * @see ch.ivyteam.ivy.addons.filemanager.database.security.AbstractFileSecurityController#setRightOnDirectory(java.lang.String, int, ch.ivyteam.ivy.scripting.objects.List)
	 */
	@Override
	@Deprecated
	public List<String> setRightOnDirectory(String path, int rightType,
			List<String> allowedIvyRoleNames) throws Exception {
		makeFileSecurityHandlerIfNull();
		List<String> l1 = this.fileSecurityHandler.setRightOnDirectory(path, this.getRight(rightType),allowedIvyRoleNames);
		return l1;
	}

	@Override
	@Deprecated
	public List<String> getRolesNamesAllowedForRightOnDirectory(String path,
			int rightType) throws Exception {
		path=formatPathForDirectoryWithoutLastSeparator(path);	
		if(path==null || path.length()==0 )
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method removeRightOnDirectory(String path, " +
					"int rightType, List<String> disallowedIvyRoleNames) in "+this.getClass());
		}

		if(rightType<1 || rightType>UPDATE_FILES_RIGHT )
		{
			throw  new IllegalArgumentException("The right type parameter is not valid in method removeRightOnDirectory(String path, " +
					"int rightType, List<String> disallowedIvyRoleNames) in "+this.getClass());
		}

		FolderOnServer fos = this.getDirectoryWithPath(path);
		if(fos==null)
		{
			return null;
		}
		switch (rightType){
		case AbstractDirectorySecurityController.DELETE_DIRECTORY_RIGHT:
			return fos.getCdd();
		case AbstractDirectorySecurityController.DELETE_FILES_RIGHT:
			return fos.getCdf();
		case AbstractDirectorySecurityController.OPEN_DIRECTORY_RIGHT:
			return fos.getCod();
		case AbstractDirectorySecurityController.UPDATE_DIRECTORY_RIGHT:
			return fos.getCud();
		case AbstractDirectorySecurityController.WRITE_FILES_RIGHT:
			return fos.getCwf();
		case AbstractDirectorySecurityController.MANAGE_SECURITY_RIGHT:
			return fos.getCmrd();
		case AbstractDirectorySecurityController.CREATE_DIRECTORY_RIGHT:
			return fos.getCcd();
		case AbstractDirectorySecurityController.RENAME_DIRECTORY_RIGHT:
			return fos.getCrd();
		case AbstractDirectorySecurityController.TRANSLATE_DIRECTORY_RIGHT:
			return fos.getCtd();
		case AbstractDirectorySecurityController.CREATE_FILES_RIGHT:
			return fos.getCcf();
		case AbstractDirectorySecurityController.UPDATE_FILES_RIGHT:
			return fos.getCuf();
		}

		return null;
	}
	
	public List<String> getRolesNamesAllowedForRightOnDirectory(String path,
			SecurityRightsEnum rightType) throws Exception {
		path=formatPathForDirectoryWithoutLastSeparator(path);	
		if(path==null || path.length()==0 )
		{
			throw  new IllegalArgumentException("One of the parameter is not set in method removeRightOnDirectory(String path, " +
					"int rightType, List<String> disallowedIvyRoleNames) in "+this.getClass());
		}

		FolderOnServer fos = this.getDirectoryWithPath(path);
		if(fos==null)
		{
			return null;
		}
		
		switch(rightType){
			case MANAGE_SECURITY_RIGHT:
				return fos.getCmrd();
			case OPEN_DIRECTORY_RIGHT:
				return fos.getCod();
			case UPDATE_DIRECTORY_RIGHT:
				return fos.getCud();
			case CREATE_DIRECTORY_RIGHT:
				return fos.getCcd();
			case RENAME_DIRECTORY_RIGHT:
				return fos.getCrd();
			case TRANSLATE_DIRECTORY_RIGHT:
				return fos.getCtd();
			case DELETE_DIRECTORY_RIGHT:
				return fos.getCdd();
			case WRITE_FILES_RIGHT:
				return fos.getCwf();
			case CREATE_FILES_RIGHT:
				return fos.getCcf();
			case UPDATE_FILES_RIGHT:
				return fos.getCuf();
			case DELETE_FILES_RIGHT:
				return fos.getCdf();
			default:
				return List.create(String.class);
		}
	}
	


	@Override
	@Deprecated
	public boolean directoryExists(String _path) throws Exception{
		makeFileSecurityHandlerIfNull();
		return this.fileSecurityHandler.directoryExists(_path);
	}

	@Override
	@Deprecated
	public FolderOnServer getDirectoryWithPath(String _path)
			throws Exception {
		makeFileSecurityHandlerIfNull();
		return this.fileSecurityHandler.getDirectoryWithPath(_path);
	}
	
	protected FolderOnServer fillUserRightsInFolderOnServer(FolderOnServer fos, List<IRole> userRoles) {
		if(fos==null) {
			return null;
		}
		try {
			if(this.containsAdminRole(userRoles)) {
				fos.setCanUserOpenDir(true);
				fos.setCanUserUpdateDir(true);
				fos.setCanUserCreateDirectory(true);
				fos.setCanUserRenameDirectory(true);
				fos.setCanUserTranslateDirectory(true);
				fos.setCanUserDeleteDir(true);
				fos.setCanUserWriteFiles(true);
				fos.setCanUserCreateFiles(true);
				fos.setCanUserUpdateFiles(true);
				fos.setCanUserDeleteFiles(true);
				fos.setCanUserManageRights(true);
				return fos;
			}

		}catch(Throwable t) {
			//If a problem occurs with the username (no existing IvyUser, null etc...) the returned fos will have false in all the fields.
		}

		fos.setCanUserOpenDir(false);
		fos.setCanUserUpdateDir(false);
		fos.setCanUserCreateDirectory(false);
		fos.setCanUserRenameDirectory(false);
		fos.setCanUserTranslateDirectory(false);
		fos.setCanUserDeleteDir(false);
		fos.setCanUserWriteFiles(false);
		fos.setCanUserCreateFiles(false);
		fos.setCanUserUpdateFiles(false);
		fos.setCanUserDeleteFiles(false);
		fos.setCanUserManageRights(false);
		if(userRoles==null || userRoles.isEmpty()) {
			return fos;
		}
		for(IRole r: userRoles) {
			try {
				String roleName = r.getName();
				if(fos.getCmrd().contains(roleName)) {
					fos.setCanUserOpenDir(true);
					fos.setCanUserUpdateDir(true);
					fos.setCanUserCreateDirectory(true);
					fos.setCanUserRenameDirectory(true);
					fos.setCanUserTranslateDirectory(true);
					fos.setCanUserDeleteDir(true);
					fos.setCanUserWriteFiles(true);
					fos.setCanUserCreateFiles(true);
					fos.setCanUserUpdateFiles(true);
					fos.setCanUserDeleteFiles(true);
					fos.setCanUserManageRights(true);
					return fos;
				}
				if(fos.getCdd().contains(roleName)) {
					fos.setCanUserDeleteDir(true);
					fos.setCanUserCreateDirectory(true);
					fos.setCanUserRenameDirectory(true);
					fos.setCanUserOpenDir(true);
				}
				if(fos.getCud().contains(roleName)) {
					fos.setCanUserUpdateDir(true);
					fos.setCanUserOpenDir(true);
					if(this.secVersion==1) {
						fos.setCanUserCreateDirectory(true);
						fos.setCanUserRenameDirectory(true);
					}
				}
				if(fos.getCcd().contains(roleName)) {
					fos.setCanUserCreateDirectory(true);
					fos.setCanUserOpenDir(true);
				}
				if(fos.getCrd().contains(roleName)) {
					fos.setCanUserRenameDirectory(true);
					fos.setCanUserOpenDir(true);
				}
				if(fos.getCtd().contains(roleName)) {
					fos.setCanUserRenameDirectory(true);
					fos.setCanUserTranslateDirectory(true);
					fos.setCanUserOpenDir(true);
				}
				if(fos.getCod().contains(roleName))
				{
					fos.setCanUserOpenDir(true);
				}
				if(fos.getCdf().contains(roleName))
				{
					fos.setCanUserDeleteFiles(true);
					fos.setCanUserOpenDir(true);
					fos.setCanUserCreateFiles(true);
					fos.setCanUserUpdateFiles(true);
					fos.setCanUserWriteFiles(true);
				}
				if(fos.getCwf().contains(roleName))
				{
					fos.setCanUserWriteFiles(true);
					fos.setCanUserOpenDir(true);
					if(this.secVersion==1){
						fos.setCanUserCreateFiles(true);
						fos.setCanUserUpdateFiles(true);
					}
				}
				if(fos.getCcf().contains(roleName))
				{
					fos.setCanUserCreateFiles(true);
					fos.setCanUserOpenDir(true);
				}
				if(fos.getCuf().contains(roleName))
				{
					fos.setCanUserUpdateFiles(true);
					fos.setCanUserOpenDir(true);
				}
			}catch(Exception ex){
				// if persistencyException, we only try to get the next Role.
			}
		}
		return fos;

	}

	@Override
	public FolderOnServer getUserRightsInFolderOnServer(FolderOnServer fos, String ivyUserName)
	{
		List <IRole> userRoles = List.create(IRole.class);
		try{
			if(ivyUserName== null || ivyUserName.trim().length()==0){
				ivyUserName = Ivy.session().getSessionUserName();
			}
			userRoles.addAll(Ivy.wf().getSecurityContext().findUser(ivyUserName).getAllRoles());

		}catch(Exception ex){
			//If a problem occurs with the username (no existing IvyUser, null etc...) the returned fos will have false in all the fields.
		}

		return fillUserRightsInFolderOnServer(fos, userRoles);

	}

	/*
	public boolean isDirectoryProtected(String path) throws Exception {

		return this.getDirectoryWithPath(path).getIs_protected();
	}
	 */
	@Override
	@Deprecated
	public ArrayList<FolderOnServer> getListDirectoriesUnderPath(String rootPath, String ivyUserName) throws Exception {
		makeFileSecurityHandlerIfNull();
		return this.fileSecurityHandler.getListDirectoriesUnderPathWithSecurityInfos(rootPath,ivyUserName);
	}

	/**
	 * Deprecated: use the PathUtil.getListFromString(s, list_sep) method instead
	 * transforms a String that represents a list of token separated with a delimiter into a List<String>
	 * @param s: the String 
	 * @param list_sep: the delimiter
	 * @return the List<String>
	 */
	@Deprecated
	public List<String> getListFromString(String s, String list_sep){
		return PathUtil.getListFromString(s, list_sep);
	}

	/**
	 * 
	 * @param roles
	 * @param ivyUserName
	 * @return
	 */
	public boolean isOneUserRoleInList(List<String> roles, String ivyUserName){
		if(roles ==null || roles.isEmpty())
		{
			return false;
		}
		List <IRole> userRoles = List.create(IRole.class);
		try{
			if(ivyUserName== null || ivyUserName.trim().length()==0){
				ivyUserName = Ivy.session().getSessionUserName();
			}
			userRoles.addAll(Ivy.wf().getSecurityContext().findUser(ivyUserName).getAllRoles());
		}catch(Throwable t)
		{
			//If a problem occurs with the username (no existing IvyUser, null etc...) the returned fos will have false in all the fields.
		}
		boolean found = false;
		for(IRole r: userRoles)
		{
			if(roles.contains(r.getName())){
				found = true;
				break;
			}
		}
		return found;
	}

	private List<String> getRolesInFolderForRight(FolderOnServer fos, SecurityRightsEnum rightType) {
		if(fos==null){
			return null;
		}
		switch(rightType){
		case MANAGE_SECURITY_RIGHT:
			return fos.getCmrd();
		case OPEN_DIRECTORY_RIGHT:
			return fos.getCod();
		case UPDATE_DIRECTORY_RIGHT:
			return fos.getCud();
		case CREATE_DIRECTORY_RIGHT:
			return fos.getCcd();
		case RENAME_DIRECTORY_RIGHT:
			return fos.getCrd();
		case TRANSLATE_DIRECTORY_RIGHT:
			return fos.getCtd();
		case DELETE_DIRECTORY_RIGHT:
			return fos.getCdd();
		case WRITE_FILES_RIGHT:
			return fos.getCwf();
		case CREATE_FILES_RIGHT:
			return fos.getCcf();
		case UPDATE_FILES_RIGHT:
			return fos.getCuf();
		case DELETE_FILES_RIGHT:
			return fos.getCdf();
		default:
			return List.create(String.class);
		}
	}
	
	@Override
	public SecurityResponse hasRight(Iterator<SecurityHandler> handlerInterator, SecurityRightsEnum rightType,
			FolderOnServer folder, IUser u, java.util.List<String> roles) {
		boolean b = false;
		try {
			if(roles!=null && !roles.isEmpty()){
				if(roles.contains(this.securityAdmin)) {
					b = true;
				}
				if(folder!=null && !b){
					this.ensureRightsIntegrityInDirectory(folder);
					List<String> rs = getRolesInFolderForRight(folder, rightType);
					if (rs != null) {
						for (String r : roles) {
							//for each roles owned by the user check if it is allowed to perform the action.
							if (rs.contains(r)) {
								b = true;
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Ivy.log().error(e.getMessage());
		}
		SecurityResponse resp = new SecurityResponse();
		resp.setAllow(b);
		resp.setMessage(b ? "User has right to perform the operation."
				: "User does not has right to perform the operation.");
		return resp;
	}

	protected AbstractFileSecurityHandler getSecurityHandler() {
		return fileSecurityHandler;
	}

	public void setSecurityHandler(AbstractFileSecurityHandler securityHandler) {
		this.fileSecurityHandler = securityHandler;
	}

	@Override
	public FolderOnServer createOpenDirectory(String directoryPath)
			throws Exception {
		makeFileSecurityHandlerIfNull();
		return this.fileSecurityHandler.createOpenDirectory(directoryPath);
	}
}
