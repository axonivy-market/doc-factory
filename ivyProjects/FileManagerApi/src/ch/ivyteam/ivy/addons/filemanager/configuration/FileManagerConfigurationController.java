/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.configuration;

/**
 * @author Emmanuel Comba<br>
 * @since 30.05.2012<br>
 * This class is used by the FileManager RDC to control its configuration.<br>
 * It extends the ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController class.
 * @see ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController
 *
 */
public class FileManagerConfigurationController extends
		BasicConfigurationController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2041056437350105136L;
	/**
	 * If true then the folder tree is visible. Default is true.
	 */
	private boolean folderTreeVisible = true;
	/**
	 * If true then the tool bar is visible. Default is true.
	 */
	private boolean toolBarVisible = true;
	/**
	 * If true then the files are displayed recursively or not. Default is false.
	 */
	private boolean diplayFilesRecursively = false;
	/**
	 * Flag used for some actions like delete directories. This should not be used anymore.<br>
	 * Use the security feature instead.
	 * @See  ch.ivyteam.ivy.addons.filemanager.database.security.DirectorySecurityController
	 */
	private boolean advancedActionsEnabled = true;
	/**
	 * Flag used if the file type feature is activated.<br>
	 * If this flag is true and the file type feature is activated,<br> 
	 * then a column showing the file types will be shown in the FileManager Rich Dialog.<br>
	 * Default is true.
	 */
	private boolean showFileTypeInTable = true;
	/**
	 * Flag used if the file type feature is activated.<br>
	 * If this flag is true and the file type feature is activated,<br> 
	 * then a context menu item will allow the user to popup the file types manager.
	 * Per Default it is false.
	 */
	private boolean showFileTypeManagement = false;
	/**
	 * Flag used if the file type feature is activated.<br>
	 * If this flag is true and the file type feature is activated,<br> 
	 * then a context menu item will allow the user to set the file type on the selected DocumentOnServer.
	 * Per Default it is false.
	 */
	private boolean allowUserToSetDocumentFileTypes = false;
	
	/**
	 * Flag used if the file tag feature is activated.<br>
	 * If this flag is true and the file tag feature is activated,<br> 
	 * then the FileTagDialog that the user opens from the context menu will allow deleting, editing and creating new Tags.<br>
	 * Default is false
	 */
	private boolean allowUserToSetDocumentTags = false;
	
	/**
	 * Flag used if the file action history feature is activated.<br>
	 * If this flag is true (default) and the file action history feature is also activated,<br>
	 * then the user will be able to open the file action history window with a menuItem.
	 */
	private boolean showFileHistoryMenuItem = true;
	
	private boolean showUserMessageByMoveDirectory = true;
	/**
	 * Tells if the folderTree should be visible
	 * @return the folderTreeVisible
	 */
	public boolean isFolderTreeVisible() {
		return folderTreeVisible;
	}
	
	/**
	 * set the folderTree visibility
	 * @param folderTreeVisible the folderTreeVisible to set
	 */
	public void setFolderTreeVisible(boolean folderTreeVisible) {
		this.folderTreeVisible = folderTreeVisible;
	}
	
	/**
	 * Tells if the toolBar is visible
	 * @return the toolBarVisible
	 */
	public boolean isToolBarVisible() {
		return toolBarVisible;
	}
	
	/**
	 * Set the toolBar visibility
	 * @param toolBarVisible the toolBarVisible to set
	 */
	public void setToolBarVisible(boolean toolBarVisible) {
		this.toolBarVisible = toolBarVisible;
	}

	/**
	 * tells if the files are displayed recursively or not.
	 * @return the diplayFilesRecursively
	 */
	public boolean isDiplayFilesRecursively() {
		return this.diplayFilesRecursively;
	}

	/**
	 * Set if the files should be displayed recursively or not.<br>
	 * If the security is activated, isDiplayFilesRecursively() returns always false, so in that case the value you give in won't have any effect.
	 * @param diplayFilesRecursively the diplayFilesRecursively to set
	 */
	public void setDiplayFilesRecursively(boolean diplayFilesRecursively) {
		this.diplayFilesRecursively = diplayFilesRecursively;
	}

	/**
	 * This flag was used to enable/disable some buttons representing "advanced" actions like delete directory.<br>
	 * A more complete security management has been implemented yet. Please use this Security management now.
	 * @See  ch.ivyteam.ivy.addons.filemanager.database.security.DirectorySecurityController
	 * @param advancedActionsEnabled the advancedActionsEnabled to set
	 */
	public void setAdvancedActionsEnabled(boolean advancedActionsEnabled) {
		this.advancedActionsEnabled = advancedActionsEnabled;
	}

	/**
	 * This flag was used to enable/disable some buttons representing "advanced" actions like delete directory.<br>
	 * A more complete security management has been implemented yet. Please use this Security management now.
	 * @See  ch.ivyteam.ivy.addons.filemanager.database.security.DirectorySecurityController
	 * @return the advancedActionsEnabled
	 */
	public boolean isAdvancedActionsEnabled() {
		return advancedActionsEnabled;
	}

	/**
	 * Flag used if the file type feature is activated.<br>
	 * If this flag is true and the file type feature is activated,<br> 
	 * then a column showing the file types will be shown in the FileManager Rich Dialog.<br>
	 * Per Default it is true.
	 * @param showFileTypeInTable the showFileTypeInTable to set
	 */
	public void setShowFileTypeInTable(boolean showFileTypeInTable) {
		this.showFileTypeInTable = showFileTypeInTable;
	}

	/**
	 * Flag used if the file type feature is activated.<br>
	 * If this flag is true and the file type feature is activated,<br> 
	 * then a column showing the file types will be shown in the FileManager Rich Dialog.<br>
	 * Per Default it is true.<br>
	 * If the isActivateFileType option is disabled, then this method returns always false.
	 * @return the showFileTypeInTable
	 */
	public boolean isShowFileTypeInTable() {
		return (super.isActivateFileType() && showFileTypeInTable);
	}

	/**
	 * Flag used if the file type feature is activated.<br>
	 * If this flag is true and the file type feature is activated,<br> 
	 * then a context menu item will allow the user to popup the file types manager.
	 * Per Default it is false.
	 * @param showFileTypeManagement the showFileTypeManagement to set
	 */
	public void setShowFileTypeManagement(boolean showFileTypeManagement) {
		this.showFileTypeManagement = showFileTypeManagement;
	}

	/**
	 * Flag used if the file type feature is activated.<br>
	 * If this flag is true and the file type feature is activated,<br> 
	 * then a context menu item will allow the user to popup the file types manager.
	 * Per Default it is false.<br>
	 * If the isActivateFileType option is disabled, then this method returns always false.
	 * @return the showFileTypeManagement
	 */
	public boolean isShowFileTypeManagement() {
		return (super.isActivateFileType() && showFileTypeManagement);
	}

	/**
	 * Flag used if the file type feature is activated.<br>
	 * If this flag is true and the file type feature is activated,<br> 
	 * then a context menu item will allow the user to set the file type on the selected DocumentOnServer.
	 * Per Default it is false.<br>
	 * @param allowUserToSetDocumentFileTypes the allowUserToSetDocumentFileTypes to set
	 */
	public void setAllowUserToSetDocumentFileTypes(
			boolean allowUserToSetDocumentFileTypes) {
		this.allowUserToSetDocumentFileTypes = allowUserToSetDocumentFileTypes;
	}

	/**
	 * Flag used if the file type feature is activated.<br>
	 * If this flag is true and the file type feature is activated,<br> 
	 * then a context menu item will allow the user to set the file type on the selected DocumentOnServer.
	 * Per Default it is false.<br>
	 * If the isActivateFileType option is disabled, then this method returns always false.
	 * @return the allowUserToSetDocumentFileTypes
	 */
	public boolean isAllowUserToSetDocumentFileTypes() {
		return (super.isActivateFileType() && allowUserToSetDocumentFileTypes);
	}

	/**
	 * Flag used if the file tag feature is activated.<br>
	 * If this flag is true and the file tag feature is activated,<br> 
	 * then the FileTagDialog that the user opens from the context menu will allow deleting, editing and creating new Tags.<br>
	 * Per Default it is false.<br>
	 * If the isActivateFileType option is disabled, then this method returns always false.
	 * @param allowUserToSetDocumentTags the allowUserToSetDocumentTags to set
	 */
	public void setAllowUserToSetDocumentTags(boolean allowUserToSetDocumentTags) {
		this.allowUserToSetDocumentTags = allowUserToSetDocumentTags;
	}

	/**
	 * Flag used if the file tag feature is activated.<br>
	 * If this flag is true and the file tag feature is activated,<br> 
	 * then the FileTagDialog that the user opens from the context menu will allow deleting, editing and creating new Tags.<br>
	 * Per Default it is false.<br>
	 * If the isActivateFileType option is disabled, then this method returns always false.
	 * @return the allowUserToSetDocumentTags
	 */
	public boolean isAllowUserToSetDocumentTags() {
		return (super.isActivateFileTags() && allowUserToSetDocumentTags);
	}

	/**
	 * @param showFileHistoryMenuItem the showFileHistoryMenuItem to set
	 */
	public void setShowFileHistoryMenuItem(boolean showFileHistoryMenuItem) {
		this.showFileHistoryMenuItem = showFileHistoryMenuItem;
	}

	/**
	 * @return the showFileHistoryMenuItem
	 */
	public boolean isShowFileHistoryMenuItem() {
		return super.getFileActionHistoryConfiguration().isActivateFileActionHistory() && showFileHistoryMenuItem;
	}

	/**
	 * @return the showUserMessageByMoveDirectory
	 */
	public boolean isShowUserMessageByMoveDirectory() {
		return showUserMessageByMoveDirectory;
	}

	/**
	 * @param showUserMessageByMoveDirectory the showUserMessageByMoveDirectory to set
	 */
	public void setShowUserMessageByMoveDirectory(
			boolean showUserMessageByMoveDirectory) {
		this.showUserMessageByMoveDirectory = showUserMessageByMoveDirectory;
	}
	

}
