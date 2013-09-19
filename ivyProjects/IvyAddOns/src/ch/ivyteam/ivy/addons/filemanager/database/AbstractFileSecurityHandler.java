/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database;

import java.util.ArrayList;

import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityRightsEnum;
import ch.ivyteam.ivy.scripting.objects.List;


/**
 * @author ec
 *
 */
public abstract class AbstractFileSecurityHandler extends
		AbstractFileManagementHandler {
	
	protected String escapeChar="\\";
	
	/**
	 * Set the given right on the directory denoted by the given path.<br>
	 * The right type is given by one of the Enum SecurityRightsEnum type. <br>
	 * @param _path: the directory path
	 * @param rightType: Enum (SecurityRightsEnum) indicating the right type
	 * @param allowedIvyRoleNames: list of Ivy Role names removed from the given right
	 * @return the list of the ivy roles granted on the right after the operation. <br>
	 * The List should be equal to the given allowedIvyRoleNames List if all succeeded.
	 * @throws Exception if the directory Path is null or an empty String,<br>
	 *  Or if an Exception is thrown while accessing and creating the data in the database.
	 */
	public abstract List<String> setRightOnDirectory(String _path, SecurityRightsEnum rightType, List<String> allowedIvyRoleNames) throws Exception;
	
	/**
	 * Grants a given IvyRole a right on a given directory.<br>
	 * The right type is given by one of the Enum SecurityRightsEnum type. <br>
	 * @param _path: the directory path
	 * @param rightType: Enum (SecurityRightsEnum) indicating the right type
	 * @param ivyRoleName: the Ivy Role Name to grant
	 * @return the list of the ivy roles granted on the right after the operation. <br>
	 * This method ensures that the admin role is present in the list of granted roles.<br>
	 * The admin Role ist returned by the method getFileManagerAdminRoleName().<br>
	 * The List should contains the ivyRoleName if the operation succeeded.
	 * @throws Exception if the directory Path is null or an empty String,<br>
	 *  Or if an Exception is thrown while accessing and creating the data in the database.
	 */
	public abstract List<String> AddRightOnDirectoryForIvyRole(String _path, SecurityRightsEnum rightType, String ivyRoleName) throws Exception;
	
	/**
	 * Removes the given right on the directory denoted by the given path.<br>
	 * The right type is given by one of the Enum SecurityRightsEnum type. <br>
	 * @param _path: the directory path
	 * @param rightType: Enum (SecurityRightsEnum) indicating the right type
	 * @param disallowedIvyRoleNames: list of Ivy Role names granted to the given right
	 * @return the list of the ivy roles granted on the right after the operation. <br>
	 * The List should contain none of the roles name from disallowedIvyRoleNames List if all succeeded.
	 * @throws Exception if the directory Path is null or an empty String,<br>
	 *  Or if an Exception is thrown while accessing and creating the data in the database.
	 */
	public abstract List<String> removeRightOnDirectory(String _path, SecurityRightsEnum rightType, List<String> disallowedIvyRoleNames) throws Exception;
	
	/**
	 * Returns the list of ivy Role names granted for the given right type.
	 * The right type is given by one of the Enum SecurityRightsEnum type. <br>
	 * @param _path: the directory path
	 * @param rightType: Enum (SecurityRightsEnum) indicating the right type
	 * @return the list of ivy Role names granted for the given right type.
	 * @throws Exception if the directory Path is null or an empty String,<br>
	 *  Or if an Exception is thrown while accessing and creating the data in the database.
	 */
	public abstract List<String> getRolesNamesAllowedForRightOnDirectory(String _path, SecurityRightsEnum rightType) throws Exception;
	
	/**
	 * Creates a directory that nobody can delete or rename, all the files rights are granted to the given list of Ivy roles.
	 * @param directoryPath: the directory path to create
	 * @param allowedIvyRoleNames: List of Ivy roles that can open and work on the files in this directory.
	 * @return the ch.ivyteam.ivy.addons.filemanager.FolderOnServer created in this operation.<br> 
	 * Can be null under circumstances like: no path or directory already exists. If no IvyRoleNames are provided,<br>
	 * everybody will be able to open this directory and to write and delete the files.
	 * @throws Exception if the directory Path is null or an empty String,<br>
	 *  Or if an Exception is thrown while accessing and creating the data in the database.
	 */
	public abstract FolderOnServer createIndestructibleDirectory(String directoryPath, List<String> allowedIvyRoleNames) throws Exception;
	
	/**
	 * Creates a directory that everybody can delete or rename, all the files rights are granted to everybody also.
	 * @param directoryPath: the directory path to create
	 * @throws Exception if the directory Path is null or an empty String,<br>
	 *  Or if an Exception is thrown while accessing and creating the data in the database.
	 */
	public abstract FolderOnServer createOpenDirectory(String directoryPath) throws Exception;
	
	/**
	 * Creates a directory with detailed security management. 
	 * @param _directoryPath: the path of the new directory
	 * @param grantedIvyRoleNamesToManageRights: list of ivy roles names that are granted to this right.
	 * @param grantedIvyRoleNamesToDeleteDirectory: list of ivy roles names that are granted to this right.
	 * @param grantedIvyRoleNamesToUpdateDirectory: list of ivy roles names that are granted to this right.
	 * @param grantedIvyRoleNamesToOpenDirectory: list of ivy roles names that are granted to this right.
	 * @param grantedIvyRoleNamesToWriteFiles: list of ivy roles names that are granted to this right.
	 * @param grantedIvyRoleNamesToDeleteFiles: list of ivy roles names that are granted to this right.
	 * @return the created ch.ivyteam.ivy.addons.filemanager.FolderOnServer 
	 * @throws Exception if the directory Path is null or an empty String,<br>
	 *  Or if an Exception is thrown while accessing and creating the data in the database.
	 */
	public abstract FolderOnServer createDirectory(String _directoryPath, 
			List<String> grantedIvyRoleNamesToManageRights, 
			List<String> grantedIvyRoleNamesToDeleteDirectory,
			List<String> grantedIvyRoleNamesToUpdateDirectory,
			List<String> grantedIvyRoleNamesToOpenDirectory,
			List<String> grantedIvyRoleNamesToWriteFiles,
			List<String> grantedIvyRoleNamesToDeleteFiles) throws Exception;
	
	/**
	 * 
	 * Creates a directory with detailed security management.
	 * @param fos the FOlderOnServer to create. It holds the path of the new directory to create and
	 *  all the List<String> of roles names granted for the different actions.
	 * @return the created FolderOnServer object
	 * @throws Exception
	 */
	public abstract FolderOnServer createDirectory(FolderOnServer fos) throws Exception;
	
	/**
	 * Creates a directory with the security settings of the parent dir. <br>
	 * If this directory is a root one, then it is the same as createIndestructibleDirectory method. <br>
	 * If this directory's parent does not exist, all the directories (parent and this one) will be created with <br>
	 * the createIndestructibleDirectory method.
	 * @param _directoryPath: the path of the new directory
	 * @return the created ch.ivyteam.ivy.addons.filemanager.FolderOnServer
	 * @throws Exception if the directory Path is null or an empty String,<br>
	 *  Or if an Exception is thrown while accessing and creating the data in the database.
	 */
	public abstract FolderOnServer createDirectoryWithParentSecurity(String _directoryPath) throws Exception;
	
	/**
	 * Creates a new Directory with rights driven by the given user.<br>
	 * The roles owned by this user other than 'everybody' will to be able to open the directory and to work on the files (edit + delete).<br>
	 * These roles are not going to be able to delete or rename the directory.<br>
	 * If the user doesn't have any roles other than 'everybody', <br>
	 * than 'everybody' will be able to open the directory and to work on the files (edit + delete). <br>
	 * If the parent directories don't exist, they are going to be created with the same rules.
	 * @param _directoryPath: the directory Path
	 * @param _ivyUserName: the ivyUsername. If null or an empty String, or this userName do not correspond to a valid Ivy User, then an Exception will be thrown.
	 * @return the created ch.ivyteam.ivy.addons.filemanager.FolderOnServer
	 * @throws Exception  if the directory Path is null or an empty String,<br>
	 *  or the userName is null or an empty String, or the userName do not correspond to a valid Ivy User,<br>
	 *  or if an Exception is thrown while accessing and creating the data in the database.
	 */
	public abstract FolderOnServer createDirectoryWithUserAsRightsGuideline(String _directoryPath, String _ivyUserName) throws Exception;
	
	/**
	 * This method saves the given FolderOnServer security roles lists.<br>
	 * If the given directory does not exit, it creates a new one.
	 * @param fos
	 * @return
	 * @throws Exception
	 */
	public abstract FolderOnServer saveFolderOnServer(FolderOnServer fos) throws Exception;
	
	/**
	 * Retrieves the list of directories (FolderOnServer Objects) under a given Path.<br>
	 * The FolderOnServer Objects contain informations about the given user rights on the different actions (delete directory, open...).<br>
	 * If the given User name is empty or null, the Ivy session User will be used for that.<br>
	 * If the user cannot open one directory, a ch.ivyteam.ivy.addons.filemanager.LockedFolder Object will be put in the list.<br>
	 * LockedFolder Object extends the FolderOnServer Class.
	 * @param rootPath: the path that should be listed. If the rootPath is null or an empty String an empty ArrayList will be returned.
	 * @param ivyUserName: the user name who should be tested against the different rights.
	 * @return the ArrayList of directories (FolderOnServer Objects) under the given Path.
	 * @throws Exception : if an Exception is thrown while accessing and creating the data in the database.
	 */
	public abstract ArrayList<FolderOnServer> getListDirectoriesUnderPathWithSecurityInfos(String rootPath, String ivyUserName) throws Exception;
	
	/**
	 * Checks if the directory exists.<br>
	 * @param _directoryPath: the directory Path
	 * @return true if the directory exists.<br>
	 * if _directoryPath is null or "" or no directory could be found, then false will be returned.
	 * @throws Exception if the directory Path is null or an empty String,<br>
	 *  Or if an Exception is thrown while accessing and creating the data in the database.
	 */
	public abstract boolean directoryExists(String _directoryPath) throws Exception;
	
	/**
	 * Gets the directory with its path.<br>
	 * @param _directoryPath: the directory Path
	 * @return ch.ivyteam.ivy.addons.filemanager.FolderOnServer which path is _directoryPath.<br>
	 * if _directoryPath is null or "" or no directory could be found, then an empty FolderOnServer object<br>
	 * will be returned ( id==0, no path, no name etc...)
	 * @throws Exception if the directory Path is null or an empty String,<br>
	 *  Or if an Exception is thrown while accessing and creating the data in the database.
	 */
	public abstract FolderOnServer getDirectoryWithPath(String _directoryPath) throws Exception;
	
	/**
	 * returns the name of the FileManager administrator role name.<br>
	 * Depending on your implementation it can be stored in a global variable, in a file etc...
	 * @return the name of the FileManager administrator Ivy role.
	 * @throws Exception
	 */
	public abstract String getFileManagerAdminRoleName() throws Exception;
	
	/**
	 * tells if the given user can is administrator of the file management security.
	 * @param ivyUserName: the ivy user name
	 * @return true if the user has the Ivy Role returned by getFileManagerAdminRoleName()
	 * @throws Exception
	 */
	public abstract boolean isUserFileManagerAdmin(String ivyUserName) throws Exception;
	
	
	/**
	 * Returns a String composed by String members of a list of String objects each separated by a colon.
	 * @param stringList
	 * @return
	 */
	public String returnStringFromList(List<String> stringList)
	{
		if(stringList ==null || stringList.isEmpty()){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int n = stringList.size()-1;
		for(int i =0; i<n;i++){
			sb.append(stringList.get(i)+",");
		}
		sb.append(stringList.get(n));
		return sb.toString();
	}
	
	/**
	 * returns the SQL escape char used by the SQL DB engine
	 */
	public String getEscapeChar() {
		return escapeChar;
	}

	/**
	 * Set the SQL escape char used by the SQL DB engine
	 */
	public void setEscapeChar(String escapeChar) {
		if(escapeChar!=null && escapeChar.trim().length()>0){
			this.escapeChar = escapeChar;
		}
	}
	

}
