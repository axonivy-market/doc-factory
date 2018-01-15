package ch.ivyteam.ivy.addons.filemanager.html.configuration;

import ch.ivyteam.ivy.addons.filemanager.configuration.FileManagerConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.html.enums.Environment;
import ch.ivyteam.ivy.scripting.objects.List;

public class HTMLConfigurationController extends
		FileManagerConfigurationController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7655333926304881742L;
	/**
	 * If true, the edit,print feature will be activated.
	 */
	private boolean activateEditPrint = false;
	private boolean clientsideJavaAvailable = false;

	/**
	 * If true the file preview feature is activated. Default to false.
	 */
	private boolean activateFilePreview = false;
	private boolean isInitialized;
	/**
	 * with and height of thumbnail file
	 */
	private int widthThumbnailFile;
	private int heightThumbnailFile;
	private Environment environment;

	private List<String> ivyRoleListForLogined;
	
	/**
	 * The role name of the current session user that's used to login to file manager
	 */
	private String loggedInRoleName = "Everybody";	

	public String getLoggedInRoleName() {
		return loggedInRoleName;
	}

	public void setLoggedInRoleName(String loggedInRoleName) {
		this.loggedInRoleName = loggedInRoleName;
	}

	public int getWidthThumbnailFile() {
		return widthThumbnailFile;
	}

	public boolean isActivateEditPrint() {
		return activateEditPrint;
	}

	public void setActivateEditPrint(boolean activateEditPrint) {
		this.activateEditPrint = activateEditPrint;
	}

	public void setWidthThumbnailFile(int widthThumbnailFile) {
		this.widthThumbnailFile = widthThumbnailFile;
	}

	public int getHeightThumbnailFile() {
		return heightThumbnailFile;
	}

	public void setHeightThumbnailFile(int heightThumbnailFile) {
		this.heightThumbnailFile = heightThumbnailFile;
	}

	public boolean isClientsideJavaAvailable() {
		return clientsideJavaAvailable;
	}

	public void setClientsideJavaAvailable(boolean clientsideJavaAvailable) {
		this.clientsideJavaAvailable = clientsideJavaAvailable;
	}

	public boolean isActivateFilePreview() {
		return activateFilePreview;
	}

	public void setActivateFilePreview(boolean activateFilePreview) {
		this.activateFilePreview = activateFilePreview;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	public List<String> getIvyRoleListForLogined() {
		return ivyRoleListForLogined;
	}

	public void setIvyRoleListForLogined(List<String> ivyRoleListForLogined) {
		this.ivyRoleListForLogined = ivyRoleListForLogined;
	}

	private int maxFileUploadSize = 0;

	/**
	 * @return the maxFileUploadSize
	 */
	@Override
	public int getMaxFileUploadSize() {
		return maxFileUploadSize;
	}

	/**
	 * @param maxFileUploadSize the maxFileUploadSize to set
	 */
	@Override
	public void setMaxFileUploadSize(int maxFileUploadSize) {
		this.maxFileUploadSize = maxFileUploadSize;
	}
}
