package ch.ivyteam.ivy.addons.filemanager.FileManager;
import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.configuration.FileManagerConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.filelink.FileLink;
import ch.ivyteam.ivy.environment.Ivy;

public class FileManagerPanelGuiManager {

	public static void manageDirectorySelectionChange(FileManagerPanel panel, FileManagerConfigurationController configuration, boolean isUserFileManagerAdmin) {
		try {
			//Disabling the files actions buttons & menuitems
			panel.getUnlockButton().setEnabled(false);
			panel.getUnlockMenuItem().setEnabled(false);
			setFilesActionMenusEnableStatus(panel, false);

			if(panel.getOrdnersTree().getSelectedTreeNode().getValue() instanceof FolderOnServer){
				FolderOnServer selectedFolderOnServer = (FolderOnServer) panel.getOrdnersTree().getSelectedTreeNode().getValue();

				if(configuration.isActivateSecurity()) {
					setDirectoriesActionMenusEnableStateWithSecurity(panel, selectedFolderOnServer);

					//compute the files actions rights
					setFilesActionMenusStateWithSecurity(panel, configuration,
							isUserFileManagerAdmin, selectedFolderOnServer);

				} else {//no security but some restrictions apply to the root directory
					panel.getDeleteDirectoryMenuItem().setEnabled(!selectedFolderOnServer.getIsRoot());
					panel.getRenameDirMenuItem().setEnabled(!selectedFolderOnServer.getIsRoot());
					panel.getDeleteDirButton().setEnabled(!selectedFolderOnServer.getIsRoot());
				}

				if(configuration.isActivateFileVersioningExtended()) {
					panel.getEditButton().setToolTipText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/tooltips/read"));
					panel.getEditButton().setIconUri(Ivy.cms().cr("/ch/ivyteam/ivy/addons/icons/file/48"));
					panel.getRefreshSelectedFolderButton().setIconUri(Ivy.cms().cr("/ch/ivyteam/ivy/addons/icons/write/16"));
					panel.getRefreshSelectedFolderButton().setToolTipText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/tooltips/edit"));
					panel.getRefreshSelectedFolderButton().setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/edit"));
					panel.getRefreshSelectedFolderButton().setRolloverIconUri(Ivy.cms().cr("/ch/ivyteam/ivy/addons/icons/write/24"));
					panel.getRefreshSelectedFolderButton().setPressedIconUri(Ivy.cms().cr("/ch/ivyteam/ivy/addons/icons/write/24"));
					panel.getRefreshSelectedFolderButton().setEnabled(false);
				}

				if(configuration.isActivateSecurity() && !selectedFolderOnServer.getCanUserCreateFiles()) {
					panel.getUploadButton().setEnabled(false);
				}
			}
		}catch(Throwable t) {

		}
	}

	public static void manageDocumentSelectionChange(FileManagerPanel panel, FileManagerConfigurationController configuration, 
			FolderOnServer selectedDirectory, boolean isUserFileManagerAdmin) {
		DocumentOnServer doc = (DocumentOnServer) panel.getFilesTable().getSelectedListEntry();
		if(panel.getFilesTable().getSelectedListEntry() instanceof FileLink) {
			doc =  (FileLink) panel.getFilesTable().getSelectedListEntry();
		}

		doc.setVersionnumber(doc.getVersionnumber().intValue() > 1 ? doc.getVersionnumber():1);
		doc.setIsLocked((doc.getLocked().compareTo("1")==0));

		panel.getFileNameInvisibleTextField().setText(doc.getFilename());

		boolean lock = false;
		for(Object o: panel.getFilesTable().getSelectedListEntries()){
			DocumentOnServer tempDoc = (DocumentOnServer) o;
			lock=(tempDoc.getLockingUserID()!=null && tempDoc.getLockingUserID().trim().equalsIgnoreCase(Ivy.session().getSessionUserName()));
			if(lock) {
				break;
			}
		}

		panel.getUnlockButton().setEnabled(lock);
		panel.getUnlockMenuItem().setEnabled(lock);

		if(configuration.isActivateSecurity()){
			//compute the files actions rights
			setFilesActionMenusStateWithSecurity(panel, configuration, isUserFileManagerAdmin, selectedDirectory);

			if(configuration.isDiplayFilesRecursively()){
				panel.getDeleteMenuItem().setEnabled(doc.getCanUserDelete());
				panel.getDeleteButton().setEnabled(doc.getCanUserDelete());
				panel.getDelete2Button().setEnabled(doc.getCanUserDelete());

				panel.getSetDescriptionMenuItem().setEnabled(doc.getCanUserWrite());
				panel.getNewVersionMenuItem().setEnabled(doc.getCanUserWrite());
				panel.getMakeVersionMenuItem().setEnabled(doc.getCanUserWrite());
				panel.getFileTypeChooserMenuItem().setEnabled(doc.getCanUserWrite());
				panel.getFileTypeManagerMenuItem().setEnabled(doc.getCanUserWrite());
				panel.getEditMenuItem().setEnabled(doc.getCanUserWrite());
				panel.getEditButton().setIconUri(doc.getCanUserWrite()?
						Ivy.cms().cr("/ch/ivyteam/ivy/addons/icons/write/48") : Ivy.cms().cr("/ch/ivyteam/ivy/addons/icons/file/48"));
				panel.getEditButton().setToolTipText(doc.getCanUserWrite()?
						Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/tooltips/edit") : 
							Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/tooltips/read"));
			}
		}
		else{
			setFilesActionMenusEnableStatus(panel, true);
		}
		if(configuration.isActivateFileVersioningExtended()) {
			panel.getEditButton().setToolTipText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/tooltips/read"));
			panel.getEditButton().setIconUri(Ivy.cms().cr("/ch/ivyteam/ivy/addons/icons/file/48"));
			panel.getRefreshSelectedFolderButton().setIconUri(Ivy.cms().cr("/ch/ivyteam/ivy/addons/icons/write/16"));
			panel.getRefreshSelectedFolderButton().setToolTipText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/tooltips/edit"));
			panel.getRefreshSelectedFolderButton().setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/edit"));
			panel.getRefreshSelectedFolderButton().setRolloverIconUri(Ivy.cms().cr("/ch/ivyteam/ivy/addons/icons/write/24"));
			panel.getRefreshSelectedFolderButton().setEnabled(configuration.isActivateSecurity()? selectedDirectory.getCanUserUpdateFiles() : true);
			panel.getEditButton().setEnabled(true);
			panel.getReadFileMenuItem().setEnabled(true);
			if(doc.getIsLocked() && doc.getLockingUserID().compareTo(Ivy.session().getSessionUserName())==0){
				//Not possible to read only a file the same user is editing
				panel.getEditButton().setEnabled(false);
				panel.getReadFileMenuItem().setEnabled(false);
			}
			if(configuration.isDiplayFilesRecursively() && configuration.isActivateSecurity()){
				panel.getRefreshSelectedFolderButton().setEnabled(doc.getCanUserWrite());
			}
		}
		panel.getOpenVersionsMenuItem().setEnabled(doc.getVersionnumber().intValue() > 1);
		if(doc instanceof FileLink) {
			panel.getEditButton().setIconUri(Ivy.cms().cr("/ch/ivyteam/ivy/addons/icons/file/48"));
			panel.getEditButton().setToolTipText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/tooltips/read"));
			panel.getZipMenuItem().setEnabled(false);
			panel.getZip2Button().setEnabled(false);
			panel.getEditMenuItem().setEnabled(false);
			panel.getSetDescriptionMenuItem().setEnabled(false);
			panel.getMakeVersionMenuItem().setEnabled(false);
		}

	}

	private static void setFilesActionMenusEnableStatus(FileManagerPanel panel, boolean enabled) {
		if(enabled) {
			panel.getEditButton().setIconUri(Ivy.cms().cr("/ch/ivyteam/ivy/addons/icons/write/48"));
			panel.getEditButton().setToolTipText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/tooltips/edit"));
		}
		
		panel.getDeleteMenuItem().setEnabled(enabled);
		panel.getDeleteButton().setEnabled(enabled);
		panel.getDelete2Button().setEnabled(enabled);
		panel.getZipMenuItem().setEnabled(enabled);
		panel.getZip2Button().setEnabled(enabled);
		panel.getEditMenuItem().setEnabled(enabled);
		panel.getSetDescriptionMenuItem().setEnabled(enabled);
		panel.getNewVersionMenuItem().setEnabled(enabled);
		panel.getMakeVersionMenuItem().setEnabled(enabled);
		panel.getFileTypeChooserMenuItem().setEnabled(enabled);
		panel.getDownloadButton().setEnabled(enabled);
		panel.getDownloadMenuItem().setEnabled(enabled);
		panel.getDownload2Button().setEnabled(enabled);
		panel.getTagsMenuItem().setEnabled(enabled);
		panel.getNewCopy2Button().setEnabled(enabled);
		panel.getNewCopyButton().setEnabled(enabled);
	}
	
	private static void setFilesActionMenusStateWithSecurity(FileManagerPanel panel, FileManagerConfigurationController configuration, 
			boolean isUserFileManagerAdmin, FolderOnServer selectedFolderOnServer) {
		panel.getUploadMenuItem().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserCreateFiles());
		panel.getUploadButton().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserCreateFiles());
		panel.getPasteButton().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserCreateFiles());
		panel.getPasteMenuItemTable().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserCreateFiles());
		panel.getPasteMenuItem().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserCreateFiles());
		panel.getFileTypeManagerMenuItem().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserUpdateFiles()? configuration.isShowFileTypeManagement() : false);
		panel.getEditButton().setIconUri(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserCreateFiles()?
				Ivy.cms().cr("/ch/ivyteam/ivy/addons/icons/write/48"):
					Ivy.cms().cr("/ch/ivyteam/ivy/addons/icons/file/48"));
		panel.getEditButton().setToolTipText(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserUpdateFiles()?
				Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/tooltips/edit"):
					Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/tooltips/read"));
		
		panel.getDeleteMenuItem().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserDeleteFiles());
		panel.getDeleteButton().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserDeleteFiles());
		panel.getDelete2Button().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserDeleteFiles());
		panel.getZipMenuItem().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserCreateFiles());
		panel.getZip2Button().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserCreateFiles());

		panel.getFileTypeChooserMenuItem().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserUpdateFiles()? configuration.isAllowUserToSetDocumentFileTypes() : false);
		panel.getEditMenuItem().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserUpdateFiles());
		panel.getSetDescriptionMenuItem().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserUpdateFiles());
		panel.getNewVersionMenuItem().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserUpdateFiles());
		panel.getMakeVersionMenuItem().setEnabled(isUserFileManagerAdmin || selectedFolderOnServer.getCanUserUpdateFiles());
		panel.getTagsMenuItem().setEnabled(true);
	}

	private static void setDirectoriesActionMenusEnableStateWithSecurity(
			FileManagerPanel panel, FolderOnServer selectedFolderOnServer) {
		panel.getToolbarGridBagLayoutPane().setEnabled(true);
		panel.getButtonsGridBagLayoutPane().setEnabled(true);
		//calculate the directories actions rights
		panel.getDeleteDirectoryMenuItem().setEnabled(selectedFolderOnServer.getIsRoot()? false : selectedFolderOnServer.getCanUserDeleteDir());
		panel.getDeleteDirButton().setEnabled(selectedFolderOnServer.getIsRoot()? false : selectedFolderOnServer.getCanUserDeleteDir());
		panel.getMakeNewDirButton().setEnabled(selectedFolderOnServer.getCanUserCreateDirectory());
		panel.getMakeNewDirAtRoot2Button().setEnabled(selectedFolderOnServer.getCanUserCreateDirectory());
		panel.getNewDirAtRootMenuItem().setEnabled(selectedFolderOnServer.getCanUserCreateDirectory());
		panel.getRenameDirMenuItem().setEnabled(selectedFolderOnServer.getIsRoot()? false : selectedFolderOnServer.getCanUserRenameDirectory());
		panel.getToogleRecursiveMenuItem().setEnabled(selectedFolderOnServer.getCanUserOpenDir());
		panel.getToogleFileListingButton().setEnabled(selectedFolderOnServer.getCanUserOpenDir());
		panel.getDownloadDirContentMenuItem().setEnabled(selectedFolderOnServer.getCanUserOpenDir());
		panel.getDownloadAllFilesButton().setEnabled(selectedFolderOnServer.getCanUserOpenDir());
		panel.getDownloadAllFiles2Button().setEnabled(selectedFolderOnServer.getCanUserOpenDir());
	}

}
