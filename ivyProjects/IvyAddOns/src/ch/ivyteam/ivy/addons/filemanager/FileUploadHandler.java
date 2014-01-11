/**
 * (c)2006-2007 by Soreco AG, CH-8603 Schwerzenbach. http://www.soreco.ch
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of 
 * Soreco AG. You shall not disclose such confidential information and 
 * shall use it only in accordance with the terms of the license 
 * agreement you entered into with Soreco.
 * 
 */

package ch.ivyteam.ivy.addons.filemanager;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.ReturnedMessage;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler;
import ch.ivyteam.ivy.addons.filemanager.database.security.DocumentFilter;
import ch.ivyteam.ivy.addons.filemanager.database.security.DocumentFilterActionEnum;
import ch.ivyteam.ivy.addons.filemanager.database.security.DocumentFilterAnswer;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;
import ch.ivyteam.ivy.addons.util.RDCallbackMethodHandler;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.scripting.objects.Time;

import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCPollingTimer;
import com.ulcjava.base.shared.FileChooserConfig;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.IActionListener;
import com.ulcjava.base.application.util.IFileLoadHandler;
import com.ulcjava.base.application.util.IFileChooseHandler;

/**
 * 
 * @author Emmanuel Comba<br><br>
 * The public Class FileUploadHandler eases the java.io.File upload process from a client machine to the server. <br>
 * It gives also methods for general java.io.File management at the server side.<br>
 * To be usable with all its features, it has to be instantiated with a reference to its parent ULComponent implementing IRichDialogPanel.<br>
 * With such a reference, the FileUploadHandler can communicate directly with its parent component through call back methods.<br>
 * The name of the call back methods has to be indicated in the constructor or with their setter methods. <br>
 * (In the case of call back reference, its parent must be a ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel).<br><br>
 * 
 * It relies on the ch.ivyteam.ivy.addons.filemanager.ReturnedMessage dataclass for returning the result of most Upload process actions.<br><br>
 * 
 * An Upload process begins with the choose of a java.io.File to Upload within a FileChooser Component.<br>
 * This File chooser reflects the client side java.io.File system.<br>
 * 
 * The destination path on the Server is determined by an intern private String variable. The server path variable is given at the instantiation within the constructor, or can be later changed with the setServerPath method.
 * For security purposes, if the server path is set to null or points to the root of the server, it takes automatically a default value ("uploadedFiles").
 * <br>
 */
public class FileUploadHandler<T extends ULCComponent & IRichDialogPanel>
{
	private java.io.File uploadedFile;
	private String serverPath;
	private String serverFilePath;
	private String errorMethodeName;
	private String uploadSuccessMethodName;
	private String askIfOverWriteFileMethodName;
	private String progressMethodName;
	private String releaseUploadUIMethodName;
	private ReturnedMessage returnedMessage;
	private int maxSize=0;
	private boolean areFilesStoredInDB = false;
	private String filesDestinationPathForDB="";
	private AbstractFileManagementHandler fileHandlerMgt=null;
	public int fileUnit=100;
	private BasicConfigurationController config;

	//private final int FILE_FILTERED = 1000000;

	private String MULTIFILE_UPLOAD_WINDOW_TITLE = Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/windowTitles/chooseSomeFileToUpload");
	private String SINGLEFILE_UPLOAD_WINDOW_TITLE = Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/windowTitles/chooseOneFileToUpload");
	private String UPLOAD_BUTTON = Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/upload") ;
	private String ACTION_CANCELLED = Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/information/actionCancelled");
	private String FILESIZE_TOO_BIG = Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/uploadFailedfileTooBig");
	private String ERROR_UNKOWN= Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/errorUnknown");
	private String UPLOAD_SUCCESSFUL= Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/information/uploadSuccess");
	private String FILE_ALREADY_EXISTS= Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/information/fileAlreadyExists");
	private String MAX_SINGLEFILESIZE_ALLOWED=Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/windowTitles/chooseOneFileToUploadLimitedSize");
	private String MAX_MULTIPLEFILESIZE_ALLOWED=Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/windowTitles/chooseSomeFileToUploadLimitedSize");
	private String PREPARING_NEXT_FILE_UPLOAD = Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/information/preparingNextFileUpload");


	private T ulcPane;
	ULCPollingTimer timer =null;

	/**
	 * Constructor for FileUploadHandler<br>
	 * A FileUploadHandler Object allows an ULCApplication to manage FileUploads from the client to the server<br>
	 *
	 */
	public FileUploadHandler()
	{
		this(null, "", "", "","", "");
	}
	/**
	 * Constructor for FileUploadHandler<br>
	 * A FileUploadHandler Object allows an ULCApplication to manage FileUploads from the client to the server<br>
	 * @param String _filePath : this is the entry point into the server Filesystem
	 */
	public FileUploadHandler(String _filePath)
	{
		this(null, "", "", "","","", _filePath);
	}

	/**
	 * Constructor for FileUploadHandler<br>
	 * A FileUploadHandler Object allows an ULCApplication to manage FileUploads from the client to the server<br>
	 * @param _ulcPane: the ULComponent implementing IRichDialogPanel that instantiates this Object<br>
	 * @param _errorMethode: the name of the method of the IRichDialogPanel Rich Dialog interface <br>
	 * that manages error occurred during the Upload process<br>
	 * @param _uploadSuccessMethode: the name of the method of the IRichDialogPanel Rich Dialog interface<br>
	 * that is called back to get the uploaded java.io.File<br>
	 * @param _askForChangeMethod: the name of the method of the IRichDialogPanel Rich Dialog interface<br>
	 * that is called back when a java.io.File to be uploaded already exits on the server<br>
	 * @param _progressMethod: the name of the method of the IRichDialogPanel Rich Dialog interface<br>
	 * that is called back to follow the progress of the java.io.File Upload and download.<br>
	 * @param _filePath: this is the entry point into the server File system<br>
	 * <b>Important :</b>if the file path is null or is an empty String, it is going to be set to "uploadedFiles".
	 */
	public FileUploadHandler(T _ulcPane, String _errorMethode, String _uploadSuccessMethode, String _askForChangeMethod, String _progressMethod, String _filePath)
	{

		this(_ulcPane,_errorMethode,_uploadSuccessMethode,_askForChangeMethod,_progressMethod,"_changeUploadButtonState",_filePath);

	}

	/**
	 * 
	 * @param _ulcPane: the ULComponent implementing IRichDialogPanel that instantiates this Object<br>
	 * @param _errorMethode: the name of the method of the IRichDialogPanel Rich Dialog interface <br>
	 * that manages error occurred during the Upload process<br>
	 * @param _uploadSuccessMethode: the name of the method of the IRichDialogPanel Rich Dialog interface<br>
	 * that is called back to get the uploaded java.io.File<br>
	 * @param _askForChangeMethod: the name of the method of the IRichDialogPanel Rich Dialog interface<br>
	 * that is called back when a java.io.File to be uploaded already exits on the server<br>
	 * @param _progressMethod: the name of the method of the IRichDialogPanel Rich Dialog interface<br>
	 * that is called back to follow the progress of the java.io.File Upload and Download.<br>
	 * @param _releaseUploadUIMethod: the name of the method of the IRichDialogPanel Rich Dialog interface that is called to re-enabled the upload Button after the upload process is finished.
	 * @param _filePath: this is the entry point into the server File system<br>
	 * <b>Important :</b>if the file path is null or is an empty String, it is going to be set to "uploadedFiles".
	 */
	public FileUploadHandler(T _ulcPane, String _errorMethode, String _uploadSuccessMethode, String _askForChangeMethod, String _progressMethod, String _releaseUploadUIMethod, String _filePath)
	{
		//Check if the given ulcPane implements ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel
		if(_ulcPane instanceof IRichDialogPanel)
			this.ulcPane = _ulcPane;
		else this.ulcPane=null;
		// setting the call back methods names
		errorMethodeName = _errorMethode==null?"":_errorMethode.trim();
		uploadSuccessMethodName = _uploadSuccessMethode==null?"":_uploadSuccessMethode.trim();
		askIfOverWriteFileMethodName = _askForChangeMethod==null?"":_askForChangeMethod.trim();
		progressMethodName=_progressMethod==null?"":_progressMethod.trim();
		releaseUploadUIMethodName=_releaseUploadUIMethod==null?"":_releaseUploadUIMethod.trim();
		//Initialisation of the returnedMessage Object
		returnedMessage = new ReturnedMessage();
		returnedMessage.setFiles(List.create(java.io.File.class));
		//Checking the given server path
		if(_filePath==null || _filePath.trim().equals("") || _filePath.trim().equals("/")|| _filePath.trim().equals("\\"))
			serverPath = "uploadedFiles";
		else
			serverPath = _filePath;
		formatServerPath();
		try{
			this.maxSize = Integer.parseInt(Ivy.var().get("xivy_addons_fileManager_max_upload_size"));
		}catch(Exception ex){
			Ivy.log().error("Cannot set the max size. "+ ex.getMessage(),ex);
		}
	}

	/**
	 * 
	 * @param _ulcPane: the ULComponent implementing IRichDialogPanel that instantiates this Object<br>
	 * @param _errorMethode: the name of the method of the IRichDialogPanel Rich Dialog interface <br>
	 * that manages error occurred during the Upload process<br>
	 * @param _uploadSuccessMethode: the name of the method of the IRichDialogPanel Rich Dialog interface<br>
	 * that is called back to get the uploaded java.io.File<br>
	 * @param _askForChangeMethod: the name of the method of the IRichDialogPanel Rich Dialog interface<br>
	 * that is called back when a java.io.File to be uploaded already exits on the server<br>
	 * @param _progressMethod: the name of the method of the IRichDialogPanel Rich Dialog interface<br>
	 * that is called back to follow the progress of the java.io.File Upload and Download.<br>
	 * @param _releaseUploadUIMethod: the name of the method of the IRichDialogPanel Rich Dialog interface that is called to re-enabled the upload Button after the upload process is finished.
	 * @param _filePath: this is the entry point into the server File system<br>
	 * <b>Important :</b>if the file path is null or is an empty String, it is going to be set to "uploadedFiles".
	 * @param _areFilesStoredInDb: boolean if True the file content is going to be stored in a DB and not on the Server FileSystem. This implies a special mechanism for the 
	 */
	public FileUploadHandler(T _ulcPane, String _errorMethode, String _uploadSuccessMethode, String _askForChangeMethod, String _progressMethod, String _releaseUploadUIMethod, String _filePath, boolean _areFilesStoredInDb)
	{
		//Check if the given ulcPane implements ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel
		if(_ulcPane instanceof IRichDialogPanel)
			this.ulcPane = _ulcPane;
		else this.ulcPane=null;
		// setting the call back methods names
		errorMethodeName = _errorMethode==null?"":_errorMethode.trim();
		uploadSuccessMethodName = _uploadSuccessMethode==null?"":_uploadSuccessMethode.trim();
		askIfOverWriteFileMethodName = _askForChangeMethod==null?"":_askForChangeMethod.trim();
		progressMethodName=_progressMethod==null?"":_progressMethod.trim();
		releaseUploadUIMethodName=_releaseUploadUIMethod==null?"":_releaseUploadUIMethod.trim();
		//Initialisation of the returnedMessage Object
		returnedMessage = new ReturnedMessage();
		returnedMessage.setFiles(List.create(java.io.File.class));
		//Checking the given server path
		if(_filePath==null || _filePath.trim().equals("") || _filePath.trim().equals("/")|| _filePath.trim().equals("\\"))
			serverPath = "uploadedFiles";
		else
			serverPath = _filePath;
		formatServerPath();

		areFilesStoredInDB=_areFilesStoredInDb;
		try{
			this.maxSize = Integer.parseInt(Ivy.var().get("xivy_addons_fileManager_max_upload_size"));
		}catch(Exception ex){
			Ivy.log().error("Cannot set the max size. "+ ex.getMessage(),ex);
		}
	}

	/**
	 * This method allows uploading one java.io.File from the client Filesystem to the server Filesystem.<br>
	 * The user is prompted to choose a java.io.File to upload by a FileChooser.<br>
	 * The upload process is interrupted 10 times with an ULCPollingTimer to be able to update the UI for progress (ProgressBar...)<br>
	 * <b>Important: </b>if the java.io.File already exists on the server, the Upload process will stop and a callback method will be called.<br>
	 * If the FileUploadHandler was instantiated with a reference to a IRichDialogPanel, this method will try to call the following<br>
	 * callback methods if they were also referenced:<br>
	 * <ul>
	 * <li>errorMethodeName, it will be called when an error occurs with an errorMessage as parameter,
	 * <li>askIfOverWriteFileMethodName, it will be called if the file to be uploaded already exists on the server. <br>
	 * A FILE Object reference to the choose java.io.File on the client and <br>
	 * the String path of the java.io.File on the server will be send back to the IRichDialogPanel. <br>
	 * You can then let the user choose if the existing java.io.File has to be overwritten.<br>
	 * <li>progressMethod: the upload progress will be shown with the use of this method at RD parent side
	 * <li>a uploadSuccessMethodName, it will be called if the Upload succeeds, it sends the java.io.File object that was Uploaded.
	 * </ul>
	 * @return ReturnedMessage returnedMessage <br>
	 * This Object contains the type of message (FileHandler.ERROR_MESSAGE, FileHandler.Information_MESSAGE or FileHandler.SUCCESS_MESSAGE),<br>
	 * the message text as String and the java.io.File that was choosen for the Upload.<br>
	 * If the returnedMessage Type is ERROR, the java.io.File object may be null. So always check this java.io.File before use in your process.
	 */
	public ReturnedMessage upload()
	{
		return this.uploadMultiFile(false);
	}

	/**
	 * This method allows uploading one or more java.io.File from the client Files system to the server File system.<br>
	 * The user is prompted to choose a java.io.File to upload by a FileChooser.<br>
	 * Depending on the given parameter(true = multiple /false = single) this process will be set as Single or multiple files upload.
	 * The upload process uses an ULCPollingTimer to be able to update the UI for progress (ProgressBar...)<br>
	 * <b>Important: </b>if the java.io.File already exists on the server, the Upload process will stop and a callback method will be called.<br>
	 * If the FileUploadHandler was instantiated with a reference to a IRichDialogPanel, this method will try to call the following<br>
	 * callback methods if they were also referenced:<br>
	 * <ul>
	 * <li>errorMethodeName, it will be called when an error occurs with an errorMessage as parameter,
	 * <li>askIfOverWriteFileMethodName, it will be called if the file to be uploaded already exists on the server. <br>
	 * A FILE Object reference to the chosen java.io.File on the client and <br>
	 * the String path of the java.io.File on the server will be send back to the IRichDialogPanel. <br>
	 * You can then let the user choose if the existing java.io.File has to be overwritten.<br>
	 * <li>progressMethod: the upload progress will be shown with the use of this method at RD parent side
	 * <li>a uploadSuccessMethodName, it will be called if the Upload succeeds, it sends the java.io.File object that was Uploaded.
	 * </ul>
	 * @param multipleFile boolean if true upload MULTIPLE FILES else SINGLE FILE
	 * @return ReturnedMessage returnedMessage <br>
	 * This Object contains the type of message (FileHandler.ERROR_MESSAGE, FileHandler.Information_MESSAGE or FileHandler.SUCCESS_MESSAGE),<br>
	 * the message text as String and the java.io.File that was choose for the Upload.<br>
	 * If the returnedMessage Type is ERROR, the java.io.File object may be null. So always check this java.io.File before use in your process.
	 */
	public ReturnedMessage uploadMultiFile(boolean multipleFile){
		return this.uploadMultiFile(multipleFile, 0);
	}

	/**
	 * This method allows uploading one or more java.io.File from the client Files system to the server File system.<br>
	 * The user is prompted to choose a java.io.File to upload by a FileChooser.<br>
	 * Depending on the given parameter(true = multiple /false = single) this process will be set as Single or multiple files upload.
	 * The upload process uses an ULCPollingTimer to be able to update the UI for progress (ProgressBar...)<br>
	 * <b>Important: </b>if the java.io.File already exists on the server, the Upload process will stop and a callback method will be called.<br>
	 * If the FileUploadHandler was instantiated with a reference to a IRichDialogPanel, this method will try to call the following<br>
	 * callback methods if they were also referenced:<br>
	 * <ul>
	 * <li>errorMethodeName, it will be called when an error occurs with an errorMessage as parameter,
	 * <li>askIfOverWriteFileMethodName, it will be called if the file to be uploaded already exists on the server. <br>
	 * A FILE Object reference to the chosen java.io.File on the client and <br>
	 * the String path of the java.io.File on the server will be send back to the IRichDialogPanel. <br>
	 * You can then let the user choose if the existing java.io.File has to be overwritten.<br>
	 * <li>progressMethod: the upload progress will be shown with the use of this method at RD parent side
	 * <li>a uploadSuccessMethodName, it will be called if the Upload succeeds, it sends the java.io.File object that was Uploaded.
	 * </ul>
	 * @param multipleFile boolean if true upload MULTIPLE FILES else SINGLE FILE
	 * @param _maxSize: maximum size allowed for files IN Kb. If a file is choose and it is too big, an error message will be displayed.
	 * @return ReturnedMessage returnedMessage <br>
	 * This Object contains the type of message (FileHandler.ERROR_MESSAGE, FileHandler.Information_MESSAGE or FileHandler.SUCCESS_MESSAGE),<br>
	 * the message text as String and the java.io.File that was choose for the Upload.<br>
	 * If the returnedMessage Type is ERROR, the java.io.File object may be null. So always check this java.io.File before use in your process.
	 */
	public ReturnedMessage uploadMultiFile(boolean multipleFile, int _maxSize)
	{
		if(_maxSize>0)
			this.maxSize=_maxSize*1024;
		this.returnedMessage = new ReturnedMessage();
		this.returnedMessage.setType(FileHandler.INFORMATION_MESSAGE);
		this.returnedMessage.setText("");
		this.returnedMessage.setFile(null);
		this.returnedMessage.setFiles(List.create(java.io.File.class));
		uploadedFile = null;
		final boolean multiFile= multipleFile;
		FileChooserConfig fcConfig = new FileChooserConfig();

		fcConfig.setFileSelectionMode(FileChooserConfig.FILES_ONLY);

		if(multiFile){
			fcConfig.setDialogTitle(MAX_MULTIPLEFILESIZE_ALLOWED+" "+this.getMaxUploadSize());
		}
		else{
			fcConfig.setDialogTitle(MAX_SINGLEFILESIZE_ALLOWED+" "+this.getMaxUploadSize());
		}
		fcConfig.setMultiSelectionEnabled(multiFile);        
		fcConfig.setApproveButtonText(UPLOAD_BUTTON);
		ClientContext.setFileTransferMode(ClientContext.SYNCHRONOUS_MODE);
		ClientContext.loadFile(new IFileLoadHandler(){
			public void onFailure(int reason, String description)
			{
				onFailureCall(reason);
			}

			public void onSuccess(InputStream ins[], String filePaths[], String fileNames[]){

				final int n = ins.length;
				final InputStream fIns[] = ins;
				//final String fFilePaths[] = filePaths;
				final String fFileNames[]= fileNames;
				formatServerPath();

				try{
					boolean goesOn = choosedFilesExist(filePaths, fileNames).getExistingDocs().isEmpty();
					if(goesOn) { //we can upload without asking for overwriting files
						cleanTimer();
						callPanelReleaseUploadUIMethod(false); //disables the Upload GUI launchers
						fileUnit = getInputStreamPercentLength(fIns[0],20);
						Ivy.log().debug("BufferedInputStream preparedFile: {0} fileUnit = {1}",fFileNames[0],fileUnit);
						timer.addActionListener(new IActionListener() {
							private static final long serialVersionUID = -3025252014358720080L;
							byte b[] = new byte[1024];
							int totalKBUploaded =0 ;
							int intRead;
							boolean done=false;
							int fileNumber = 0;
							//we read the first File
							BufferedInputStream preparedFile = new BufferedInputStream(fIns[0]);
							ArrayList<java.io.File> files= new ArrayList<java.io.File>();
							//we prepare the first File on the server
							String fileOnServer = serverPath + fFileNames[0];
							String fileName= fFileNames[0];
							java.io.File serverFile = new java.io.File(fileOnServer);

							BufferedOutputStream server = new BufferedOutputStream(new FileOutputStream(serverFile));
							public void actionPerformed(ActionEvent arg0) {
								int progressDone=0;
								while(!done && progressDone< fileUnit){
									try{
										if((intRead= preparedFile.read(b)) != -1){
											server.write(b,0,intRead);
											progressDone++;
											totalKBUploaded++;
										}else{ 
											//we close the current File
											server.close();
											//if we are at the last File then we are done
											if(fileNumber==n-1){
												//we add the last uploaded File to the list
												files.add(serverFile);
												//and we are done
												done=true;
											}else{
												//we add the last uploaded File to the list
												files.add(serverFile);
												//we get the next file to upload
												fileNumber=fileNumber+1;
												preparedFile = new BufferedInputStream(fIns[fileNumber]);
												fileUnit = getInputStreamPercentLength(fIns[fileNumber],20);
												Ivy.log().debug("BufferedInputStream preparedFile: {0} fileUnit = {1}", preparedFile.toString(),fileUnit);
												fileOnServer = serverPath + fFileNames[fileNumber];
												serverFile = new java.io.File(fileOnServer);
												server = new BufferedOutputStream(new FileOutputStream(serverFile));
												fileName= fFileNames[fileNumber];
												totalKBUploaded =0 ;
											}
										}
									}catch(Exception e) {
										done=true;
										callPanelErrorMethode(e.getMessage());
									}
								}

								if(!done){
									String s = fileName+" "+totalKBUploaded+" Kb uploaded";
									RDCallbackMethodHandler.callRDMethod(ulcPane, progressMethodName, new Object[] { s });
									timer.restart(); // restart the timer because the file is not completely uploaded
								}else{
									try{
										String s = fileName+" upload finished";
										List<java.io.File> lFiles = List.create(java.io.File.class);
										lFiles.addAll(files);			
										returnedMessage.setType(FileHandler.SUCCESS_MESSAGE);
										returnedMessage.setText(UPLOAD_SUCCESSFUL);
										returnedMessage.setFile(serverFile);
										RDCallbackMethodHandler.callRDMethod(ulcPane, progressMethodName, new Object[] { s });
										if(areFilesStoredInDB){
											ReturnedMessage msg = createReturnedMessageForUploadToDB(lFiles);
											msg.setFile(serverFile);
											callPanelUploadSuccessMethod(msg);
										}else{
											callPanelUploadSuccessMethod(lFiles);
										}
									}catch(Exception e){
										callPanelErrorMethode(e.getMessage());
									}finally{
										callPanelReleaseUploadUIMethod(true); //enables the Upload GUI launchers
									}
								}//end if else	
							}// end of actionPerformed
						});// end of timer
						timer.start();
					}// end of file doesn't exists
				}catch(IOException ioe){
					callPanelErrorMethode(ioe.getMessage());
				}catch(Exception e) { 
					callPanelErrorMethode(e.getMessage());
				}
			}//end of onSuccess
		}, fcConfig, ulcPane, new Integer(this.maxSize));// end of ClientContext.loadFile
		return returnedMessage;	
	}

	/**
	 * This method allows uploading one java.io.File from the client File system to the server File system Without showing any Progress.<br>
	 * The user is prompted to choose a java.io.File to upload by a FileChooser.<br>
	 * <b>Important: </b>if the java.io.File already exists on the server, the Upload process will stop and a callback method will be called.<br>
	 * If the FileUploadHandler was instantiated with a reference to a IRichDialogPanel, this method will try to call the following<br>
	 * callback methods if they were also referenced:<br>
	 * <ul>
	 * <li>errorMethodeName, it will be called when an error occurs with an errorMessage as parameter,
	 * <li>askIfOverWriteFileMethodName, it will be called if the file to be uploaded already exists on the server. <br>
	 * A FILE Object reference to the choosen java.io.File on the client and <br>
	 * the String path of the java.io.File on the server will be send back to the IRichDialogPanel. <br>
	 * You can then let the user choose if the existing java.io.File has to be overwritten.<br>
	 * <li>a uploadSuccessMethodName, it will be called if the Upload succeeds, it sends the java.io.File object that was Uploaded.
	 * </ul>
	 * @return ReturnedMessage returnedMessage <br>
	 * This Object contains the type of message (FileHandler.ERROR_MESSAGE, FileHandler.Information_MESSAGE or FileHandler.SUCCESS_MESSAGE),<br>
	 * the message text as String and the java.io.File that was choosen for the Upload.<br>
	 * If the returnedMessage Type is ERROR, the java.io.File object may be null. So always check this java.io.File before use in your process.
	 */
	public ReturnedMessage uploadWithoutShowingProgress()
	{
		this.returnedMessage = new ReturnedMessage();
		this.returnedMessage.setType(FileHandler.INFORMATION_MESSAGE);
		this.returnedMessage.setText("");
		this.returnedMessage.setFile(null);
		this.returnedMessage.setFiles(List.create(java.io.File.class));
		uploadedFile = null;

		FileChooserConfig fcConfig = new FileChooserConfig();
		fcConfig.setDialogTitle(SINGLEFILE_UPLOAD_WINDOW_TITLE);
		fcConfig.setApproveButtonText(UPLOAD_BUTTON);
		fcConfig.setFileSelectionMode(FileChooserConfig.FILES_ONLY);
		fcConfig.setMultiSelectionEnabled(false);
		ClientContext.setFileTransferMode(ClientContext.ASYNCHRONOUS_MODE);
		ClientContext.loadFile(new IFileLoadHandler(){
			public void onFailure(int reason, String description){
				onFailureCall(reason);
			}

			public void onSuccess(InputStream ins[], String filePaths[], String fileNames[]){
				try{
					final BufferedInputStream preparedFile = new BufferedInputStream(ins[0]);
					serverFilePath = fileNames[0];
					formatServerPath();
					serverFilePath = (new StringBuilder(String.valueOf(serverPath))).append(serverFilePath).toString();
					Ivy.log().debug("File to upload: "+serverFilePath.toString());
					final java.io.File serverFile = new java.io.File(serverFilePath);
					boolean goesOn = choosedFilesExist(filePaths, fileNames).getExistingDocs().isEmpty();
					if(goesOn)
					{//the file does not exists already
						Ivy.log().debug("File doesn't exist: "+serverFilePath.toString());
						uploadedFile = serverFile;
						final BufferedOutputStream server = new BufferedOutputStream(new FileOutputStream(serverFile));
						callPanelReleaseUploadUIMethod(false); //disables the Upload GUI launchers
						int intRead;
						byte b[] = new byte[1024];
						while((intRead= preparedFile.read(b)) != -1){
							server.write(b,0,intRead);
						}
						server.close();    			
						try{
							List<java.io.File> lFiles = List.create(java.io.File.class);
							lFiles.add(serverFile);			
							if(areFilesStoredInDB){
								callPanelUploadSuccessMethod(createReturnedMessageForUploadToDB(lFiles));
							}else{
								callPanelUploadSuccessMethod(lFiles);
							}
						}catch(Exception e){
							callPanelErrorMethode(e.getMessage());
						}
					}//end if else

				}// end of try	
				catch(IOException ioe){
					callPanelErrorMethode(ioe.getMessage());
				}catch(Exception e) { 
					callPanelErrorMethode(e.getMessage());
				}finally{
					callPanelReleaseUploadUIMethod(true); //enables the Upload GUI launchers
				}
			}
		}, fcConfig, ulcPane,new Integer(this.maxSize));// end of ClientContext.loadFile
		return returnedMessage;
	}

	/**
	 * This method allows uploading one or more java.io.File from the client File system to the server File system Without showing any Progress.<br>
	 * The user is prompted to choose one or more java.io.File to upload by a FileChooser.<br>
	 * <b>Important: </b>if one of the choose java.io.File already exists on the server, the Upload process will be cancelled for this File and a call back method will be called.<br>
	 * If the FileUploadHandler was instantiated with a reference to a IRichDialogPanel, this method will try to call the following<br>
	 * call back methods if they were also referenced:<br>
	 * <ul>
	 * <li>errorMethodeName, it will be called when an error occurs with an errorMessage as parameter,
	 * <li>askIfOverWriteFileMethodName, it will be called if the file to be uploaded already exists on the server. <br>
	 * A FILE Object reference to the choose java.io.File on the client and <br>
	 * the String path of the java.io.File on the server will be send back to the IRichDialogPanel. <br>
	 * You can then let the user choose if the existing java.io.File has to be overwritten.<br>
	 * <li>a uploadSuccessMethodName, it will be called if the Upload succeeds, it sends the java.io.File object that was Uploaded.
	 * </ul>
	 * @param multiFile: type is boolean. If true, multiFile upload is set, else the user can upload just one File at a time.
	 * @return ReturnedMessage returnedMessage <br>
	 * This Object contains the type of message (FileHandler.ERROR_MESSAGE, FileHandler.Information_MESSAGE or FileHandler.SUCCESS_MESSAGE),<br>
	 * the message text as String and the java.io.File that was choose for the Upload.<br>
	 * In this case, the File Object has no signification because it contains just the last File uploaded
	 */
	public ReturnedMessage uploadWithoutShowingProgress(boolean multiFile){
		return this.uploadWithoutShowingProgress(multiFile, false);
	}

	/**
	 * This method allows uploading one or more java.io.File from the client File system to the server File system Without showing any Progress.<br>
	 * The user is prompted to choose one or more java.io.File to upload by a FileChooser.<br>
	 * <b>Important: </b>if one of the choose java.io.File already exists on the server, the Upload process will be cancelled for this File and a call back method will be called.<br>
	 * If the FileUploadHandler was instantiated with a reference to a IRichDialogPanel, this method will try to call the following<br>
	 * call back methods if they were also referenced:<br>
	 * <ul>
	 * <li>errorMethodeName, it will be called when an error occurs with an errorMessage as parameter,
	 * <li>askIfOverWriteFileMethodName, it will be called if the file to be uploaded already exists on the server. <br>
	 * A FILE Object reference to the choose java.io.File on the client and <br>
	 * the String path of the java.io.File on the server will be send back to the IRichDialogPanel. <br>
	 * You can then let the user choose if the existing java.io.File has to be overwritten.<br>
	 * <li>a uploadSuccessMethodName, it will be called if the Upload succeeds, it sends the java.io.File object that was Uploaded.
	 * </ul>
	 * @param multiFile: type is boolean. If true, multiFile upload is set, else the user can upload just one File at a time.
	 * @param synchrone: type is boolean, if true, the upload will be synchrone, the UI will be blocked until the upload is finished. Else it will be asynchrone.
	 * @return ReturnedMessage returnedMessage <br>
	 * This Object contains the type of message (FileHandler.ERROR_MESSAGE, FileHandler.Information_MESSAGE or FileHandler.SUCCESS_MESSAGE),<br>
	 * the message text as String and the java.io.File that was choose for the Upload.<br>
	 * In this case, the File Object has no signification because it contains just the last File uploaded
	 * 
	 * @return
	 */
	public ReturnedMessage uploadWithoutShowingProgress(boolean multiFile, boolean synchrone){

		this.returnedMessage = new ReturnedMessage();
		this.returnedMessage.setType(FileHandler.INFORMATION_MESSAGE);
		this.returnedMessage.setText("");
		this.returnedMessage.setFile(null);
		this.returnedMessage.setFiles(List.create(java.io.File.class));
		uploadedFile = null;

		FileChooserConfig fcConfig = new FileChooserConfig();

		if(multiFile)
			fcConfig.setDialogTitle(MULTIFILE_UPLOAD_WINDOW_TITLE);
		else
			fcConfig.setDialogTitle(SINGLEFILE_UPLOAD_WINDOW_TITLE);

		fcConfig.setApproveButtonText(UPLOAD_BUTTON);
		fcConfig.setFileSelectionMode(FileChooserConfig.FILES_ONLY);
		fcConfig.setMultiSelectionEnabled(multiFile);
		if(synchrone){
			ClientContext.setFileTransferMode(ClientContext.SYNCHRONOUS_MODE);
		}else{
			ClientContext.setFileTransferMode(ClientContext.ASYNCHRONOUS_MODE);
		}

		ClientContext.loadFile(new IFileLoadHandler(){
			public void onFailure(int reason, String description)
			{
				onFailureCall(reason);
			}

			public void onSuccess(InputStream ins[], String filePaths[], String fileNames[]){
				int n = ins.length;
				boolean goesOn=choosedFilesExist(filePaths, fileNames).getExistingDocs().isEmpty();
				if(goesOn){
					//no file to override
					callPanelReleaseUploadUIMethod(false); //disables the Upload GUI launchers
					for(int i=0; i<n; i++){
						try{
							final BufferedInputStream preparedFile = new BufferedInputStream(ins[i]);
							serverFilePath = fileNames[i];
							formatServerPath();
							serverFilePath = FileHandler.formatPath((new StringBuilder(String.valueOf(serverPath))).append(serverFilePath).toString());
							Ivy.log().debug("File to upload in: "+serverFilePath.toString());
							final java.io.File serverFile = new java.io.File(serverFilePath);
							uploadedFile = serverFile;

							final BufferedOutputStream server = new BufferedOutputStream(new FileOutputStream(serverFile));
							//final java.io.File choosedFile= new java.io.File(filePaths[i]);
							int intRead;
							byte b[] = new byte[1024];
							while((intRead= preparedFile.read(b)) != -1){
								server.write(b,0,intRead);
								//Ivy.log().info("Writing the File: "+serverFilePath.toString());
							}
							server.close();    			
							try {
								List<java.io.File> lFiles = List.create(java.io.File.class);
								lFiles.add(serverFile);			
								if(areFilesStoredInDB){
									callPanelUploadSuccessMethod(createReturnedMessageForUploadToDB(lFiles));
								}else{
									callPanelUploadSuccessMethod(lFiles);
								}
							}catch(Exception e){
								callPanelErrorMethode(e.getMessage());
							}

						}// end of try	
						catch(IOException ioe){
							callPanelErrorMethode(ioe.getMessage());
						}catch(Exception e) { 
							callPanelErrorMethode(e.getMessage());
						}finally{
							callPanelReleaseUploadUIMethod(true); //enables the Upload GUI launchers
						}
					}
				}
			}
		}, fcConfig, ulcPane, new Integer(this.maxSize));// end of ClientContext.loadFile

		return returnedMessage;
	}

	/**
	 * This method allows to upload a java.io.File from the client Filesystem to the server Filesystem.<br>
	 * The upload process is interrupted 10 times with an ULCPollingTimer to be able to update the UI for progress (ProgressBar...)<br>
	 * The user is prompt to choose a java.io.File to upload by a FileChooser.<br>
	 * <b>Important: </b>This method doesn't check if the java.io.File already exists. The existing java.io.File will be then overwritten.<br>
	 * If the FileUploadHandler was instantiated with a reference to a IRichDialogPanel, this method will try to call the following<br>
	 * callback methods if they were also referenced:<br>
	 * <ul>
	 * <li>errorMethodeName, it will be called when an error occurs with a returnedMessage Object as parameter,
	 * <li>progressMethod: the upload progress will be shown with the use of this method at RD parent side
	 * <li>a uploadSuccessMethodName, it will be called if the Upload succeeds, it sends a returnedMessage Object with a reference to the java.io.File object that was Uploaded.
	 * </ul>
	 * @return ReturnedMessage returnedMessage <br>
	 * This Object contains the type of message (FileHandler.ERROR_MESSAGE, FileHandler.Information_MESSAGE or FileHandler.SUCCESS_MESSAGE),<br>
	 * the message text as String and the java.io.File that was choosen for the Upload.<br>
	 * If the returnedMessage Type is ERROR, the java.io.File object may be null. So always check this java.io.File before use in your process.
	 */
	public ReturnedMessage forceUpload()
	{
		this.returnedMessage = new ReturnedMessage();
		this.returnedMessage.setType(FileHandler.INFORMATION_MESSAGE);
		this.returnedMessage.setText("");
		this.returnedMessage.setFile(null);
		this.returnedMessage.setFiles(List.create(java.io.File.class));
		uploadedFile = null;
		FileChooserConfig fcConfig = new FileChooserConfig();
		fcConfig.setDialogTitle(SINGLEFILE_UPLOAD_WINDOW_TITLE);
		fcConfig.setApproveButtonText(UPLOAD_BUTTON);
		fcConfig.setFileSelectionMode(FileChooserConfig.FILES_ONLY);
		fcConfig.setMultiSelectionEnabled(false);
		ClientContext.loadFile(new IFileLoadHandler() {
			public void onFailure(int reason, String description)
			{
				onFailureCall(reason);
			}

			public void onSuccess(InputStream ins[], String filePaths[], String fileNames[]){
				try{
					callPanelReleaseUploadUIMethod(false); //disables the Upload GUI launchers
					final BufferedInputStream preparedFile = new BufferedInputStream(ins[0]);
					fileUnit = getInputStreamPercentLength(ins[0],20);
					Ivy.log().debug("BufferedInputStream preparedFile: {0} fileUnit = {1}", preparedFile.toString(),fileUnit);
					serverFilePath = fileNames[0];
					formatServerPath();
					serverFilePath = (new StringBuilder(String.valueOf(serverPath))).append(serverFilePath).toString();
					final java.io.File serverFile = new java.io.File(serverFilePath);
					uploadedFile = serverFile;
					@SuppressWarnings("resource")
					final BufferedOutputStream server = new BufferedOutputStream(new FileOutputStream(serverFile));
					final java.io.File choosedFile= new java.io.File(filePaths[0]);
					final String fileName = fileNames[0];
					cleanTimer();
					timer.addActionListener(new IActionListener() {
						private static final long serialVersionUID = -3025252014358720080L;
						byte b[] = new byte[1024];

						int totalKBUploaded =0 ;
						int intRead;
						boolean done=false;
						public void actionPerformed(ActionEvent arg0) {
							int progressDone =0;
							try{
								while(!done && progressDone< fileUnit){	
									if((intRead= preparedFile.read(b)) != -1){
										server.write(b,0,intRead);
										progressDone++;
										totalKBUploaded++;
									}else{ 
										done=true;
										server.close();
									}
								}
								if(!done){
									progressDone=0;
								}
							}catch(IOException e){
								done=true;
								try {
									server.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								callPanelErrorMethode(e.getMessage());
							}// end try/catch
							if(!done){
								String s = fileName+" "+totalKBUploaded+" Kb uploaded";
								RDCallbackMethodHandler.callRDMethod(ulcPane, progressMethodName, new Object[] { s });
								timer.restart(); // restart the timer because the file is not completely uploaded
							}else{
								String s = fileName+" upload finished";
								returnedMessage.setType(FileHandler.SUCCESS_MESSAGE);
								returnedMessage.setText(UPLOAD_SUCCESSFUL+" "+choosedFile.getName());
								returnedMessage.setFile(serverFile);
								RDCallbackMethodHandler.callRDMethod(ulcPane, progressMethodName, new Object[] { s });
								try{
									List<java.io.File> lFiles = List.create(java.io.File.class);
									lFiles.add(serverFile);			
									if(areFilesStoredInDB){
										callPanelUploadSuccessMethod(createReturnedMessageForUploadToDB(lFiles));
									}else{
										callPanelUploadSuccessMethod(lFiles);
									}
								}catch(Exception e){
									callPanelErrorMethode(e.getMessage());
								}
							}//end if else
						}// end of actionPerformed
					});// end of timer
					timer.start();
				}// end of try	
				catch(IOException ioe){
					callPanelErrorMethode(ioe.getMessage());
				}
				catch(Exception exception) {
					callPanelErrorMethode(exception.getMessage());
				}finally{
					callPanelReleaseUploadUIMethod(true); //disables the Upload GUI launchers
				}
			}
		}, fcConfig, ulcPane,new Integer(this.maxSize));
		return returnedMessage;
	}

	/**
	 * This method allows to choose a java.io.File from the client Filesystem for a future upload.<br>
	 * The user is prompt to choose a java.io.File to upload by a FileChooser.<br>
	 * If the FileUploadHandler was instantiated with a reference to a IRichDialogPanel, this method will try to call the following<br>
	 * callback methods if they were also referenced:<br>
	 * <ul>
	 * <li>errorMethodeName, it will be called when an error occurs with a returnedMessage Object as parameter,
	 * </ul>
	 * @return ReturnedMessage returnedMessage <br>
	 * This Object contains the type of message (FileHandler.ERROR_MESSAGE, FileHandler.Information_MESSAGE or FileHandler.SUCCESS_MESSAGE),<br>
	 * the message text as String and the java.io.File that was choosen for the Upload.<br>
	 * If the returnedMessage Type is ERROR, the java.io.File object may be null. So always check this java.io.File before use in your process.
	 */
	public ReturnedMessage prepareUpload()
	{
		this.returnedMessage = new ReturnedMessage();
		this.returnedMessage.setType(FileHandler.INFORMATION_MESSAGE);
		this.returnedMessage.setText("");
		this.returnedMessage.setFile(null);
		this.returnedMessage.setFiles(List.create(java.io.File.class));
		uploadedFile = null;
		FileChooserConfig fcConfig = new FileChooserConfig();
		fcConfig.setDialogTitle(SINGLEFILE_UPLOAD_WINDOW_TITLE);
		fcConfig.setApproveButtonText(UPLOAD_BUTTON);
		fcConfig.setFileSelectionMode(FileChooserConfig.FILES_ONLY);
		ClientContext.chooseFile(new IFileChooseHandler(){

			public void onFailure(int reason, String description)
			{
				onFailureCall(reason);
			}

			public void onSuccess(String filePaths[], String fileNames[])
			{
				try{
					uploadedFile = new java.io.File(filePaths[0]);
					returnedMessage.setType(FileHandler.SUCCESS_MESSAGE);
					returnedMessage.setText("The java.io.File was successfully prepared for future upload or use.");
					returnedMessage.setFile(uploadedFile);
				}
				catch(Exception exception) { 
					callPanelErrorMethode(exception.getMessage());
				}
			}
		}, fcConfig, ulcPane);// end of ClientContext.loadFile
		return returnedMessage;
	}

	/**
	 * method that prepares a list of choose Files for future upload
	 * @param panel: the RDPanel that calls this method
	 * @param callbackMethodName: the name of the method from the RDPanel that should receive the choose files.
	 * @return the list of choose java.io.File
	 */
	public ArrayList<java.io.File> prepareFilesForUpload(final ULCComponent panel, final String callbackMethodName){
		final ArrayList<java.io.File> files = new ArrayList<java.io.File>();

		FileChooserConfig fcConfig = new FileChooserConfig();

		fcConfig.setDialogTitle(MULTIFILE_UPLOAD_WINDOW_TITLE);


		fcConfig.setApproveButtonText(UPLOAD_BUTTON); 
		fcConfig.setFileSelectionMode(FileChooserConfig.FILES_ONLY);
		fcConfig.setMultiSelectionEnabled(true);
		ClientContext.setFileTransferMode(ClientContext.SYNCHRONOUS_MODE);
		ClientContext.chooseFile(new IFileChooseHandler(){
			@Override
			public void onFailure(int reason, String description) {
				onFailureCall(reason);
			}

			@Override
			public void onSuccess(String[] filePaths, String[] fileNames) {
				Ivy.log().debug("Number of Files choosed: "+filePaths.length);
				try{
					for(int i=0; i<filePaths.length; i++){
						Ivy.log().debug(filePaths[i]);
						files.add(new java.io.File(filePaths[i]));
					}
					if(!callbackMethodName.trim().equals(""))
					{
						List<java.io.File> f = List.create(java.io.File.class);
						f.addAll(files);
						RDCallbackMethodHandler.callRDMethodFromULCComponent(panel, callbackMethodName, new Object[] { f });
					}

				}
				catch(Exception exception) { 
					callPanelErrorMethode(exception.getMessage());
				}
			}
		}
		, fcConfig, panel);
		return files;
	}

	/**
	 * method that prepares a list of choose Files for future upload
	 * @param panel: the RDPanel that calls this method
	 * @param callbackMethodName: the name of the method from the RDPanel that should receive the choose files.
	 * @param _maxSize: the maximum size allowed for upload in Kb. If not set = no limit
	 * @return the list of choose java.io.File
	 */
	public ArrayList<java.io.File> prepareFilesForUpload(final ULCComponent panel, final String callbackMethodName, int _maxSize){
		final ArrayList<java.io.File> files = new ArrayList<java.io.File>();

		FileChooserConfig fcConfig = new FileChooserConfig();
		if(_maxSize>0)
			this.maxSize=_maxSize*1024;
		fcConfig.setDialogTitle(MAX_MULTIPLEFILESIZE_ALLOWED+" "+this.getMaxUploadSize());

		fcConfig.setApproveButtonText(UPLOAD_BUTTON); 
		fcConfig.setFileSelectionMode(FileChooserConfig.FILES_ONLY);
		fcConfig.setMultiSelectionEnabled(true);
		ClientContext.setFileTransferMode(ClientContext.SYNCHRONOUS_MODE);

		ClientContext.chooseFile(new IFileChooseHandler(){

			public void onFailure(int reason, String description)
			{
				onFailureCall(reason);
			}

			public void onSuccess(String filePaths[], String fileNames[])
			{
				Ivy.log().debug("Number of Files choosed: "+filePaths.length);
				try{
					for(int i=0; i<filePaths.length; i++){
						Ivy.log().debug(filePaths[i]);
						files.add(new java.io.File(filePaths[i]));
					}
					if(!callbackMethodName.trim().equals(""))
					{
						List<java.io.File> f = List.create(java.io.File.class);
						f.addAll(files);
						RDCallbackMethodHandler.callRDMethodFromULCComponent(panel, callbackMethodName, new Object[] { f });
					}

				}
				catch(Exception exception) { 
					callPanelErrorMethode(exception.getMessage());
				}
			}
		}, fcConfig, panel);// end of ClientContext.loadFile
		return files;
	}

	/**
	 * This method allows to upload a java.io.File from the client Filesystem to the server Filesystem.<br>
	 * The uploaded java.io.File is given as input parameter. No File chooser appears.<br>
	 * The upload process is interrupted 10 times with an ULCPollingTimer to be able to update the UI for progress (ProgressBar...)<br>
	 * This method doesn't check if the java.io.File exists<br>
	 * If the FileUploadHandler was instantiated with a reference to a IRichDialogPanel, this method will try to call the following<br>
	 * callback methods if they were also referenced:<br>
	 * <ul>
	 * <li>errorMethodeName, it will be called when an error occurs with a returnedMessage Object as parameter,
	 * <li>progressMethod: the upload progress will be shown with the use of this method at RD parent side
	 * <li>a uploadSuccessMethodName, it will be called if the Upload succeeds, it sends a returnedMessage Object <br>
	 * with a reference to the java.io.File object that was Uploaded.
	 * </ul>
	 * @return ReturnedMessage returnedMessage <br>
	 * This Object contains the type of message (FileHandler.ERROR_MESSAGE, FileHandler.Information_MESSAGE or FileHandler.SUCCESS_MESSAGE),<br>
	 * the message text as String and the java.io.File that was choosen for the Upload.<br>
	 * If the returnedMessage Type is ERROR, the java.io.File object may be null. So always check this java.io.File before use in your process.
	 */
	public ReturnedMessage uploadPreparedUpload(final java.io.File f)
	{
		this.returnedMessage = new ReturnedMessage();
		this.returnedMessage.setType(FileHandler.INFORMATION_MESSAGE);
		this.returnedMessage.setText("");
		this.returnedMessage.setFile(null);
		this.returnedMessage.setFiles(List.create(java.io.File.class));
		uploadedFile = null;
		String filePath = f.getPath().replace("\\", "/");
		ClientContext.loadFile(new IFileLoadHandler(){
			public void onFailure(int reason, String description)
			{
				onFailureCall(reason);
			}

			public void onSuccess(InputStream ins[], String filePaths[], String fileNames[])
			{
				try{
					callPanelReleaseUploadUIMethod(false); //disables the Upload GUI launchers
					uploadedFile = f;
					final String fileName= f.getName();
					final BufferedInputStream preparedFile = new BufferedInputStream(ins[0]);
					fileUnit = getInputStreamPercentLength(ins[0],20);
					Ivy.log().debug("BufferedInputStream preparedFile: {0} fileUnit = {1}", preparedFile.toString(),fileUnit);
					serverFilePath = uploadedFile.getName();
					formatServerPath();
					serverFilePath = (new StringBuilder(String.valueOf(serverPath))).append(serverFilePath).toString();
					final java.io.File serverFile = new java.io.File(serverFilePath);
					uploadedFile = new java.io.File(serverFilePath);
					@SuppressWarnings("resource")
					final BufferedOutputStream server = new BufferedOutputStream(new FileOutputStream(serverFile));
					cleanTimer();
					timer.addActionListener(new IActionListener() {
						private static final long serialVersionUID = -3025252014358720080L;
						byte b[] = new byte[1024];
						int totalKBUploaded =0 ;
						int intRead;
						boolean done=false;
						public void actionPerformed(ActionEvent arg0) {
							int progressDone =0;
							try{
								while(!done && progressDone< fileUnit){	
									if((intRead= preparedFile.read(b)) != -1){
										server.write(b,0,intRead);
										progressDone++;
										totalKBUploaded++;
									}else{ 
										done=true;
										server.close();
									}
								}
								if(!done){
									progressDone=0;
								}
							}catch(IOException e){
								done=true;
								try {
									server.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								callPanelErrorMethode(e.getMessage());
							}// end try/catch
							if(!done){
								String s = fileName+" "+totalKBUploaded+" Kb uploaded";
								RDCallbackMethodHandler.callRDMethod(ulcPane, progressMethodName, new Object[] { s });
								timer.restart(); // restart the timer because the file is not completely uploaded
							}else{
								String s = fileName+" upload finished";
								returnedMessage.setType(FileHandler.SUCCESS_MESSAGE);
								returnedMessage.setText(UPLOAD_SUCCESSFUL);
								returnedMessage.setFile(serverFile);
								RDCallbackMethodHandler.callRDMethod(ulcPane, progressMethodName, new Object[] { s });
								try{
									List<java.io.File> lFiles = List.create(java.io.File.class);
									lFiles.add(serverFile);			
									if(areFilesStoredInDB){
										ReturnedMessage msg = createReturnedMessageForUploadToDB(lFiles);
										msg.setFile(serverFile);
										callPanelUploadSuccessMethod(msg);
									}else{
										callPanelUploadSuccessMethod(lFiles);
									}
								}catch(Exception e){
									callPanelErrorMethode(e.getMessage());
								}
							}//end if else
						}// end of actionPerformed
					});// end of timer
					timer.start();

				}// end of try
				catch(Exception exception) { 
					callPanelErrorMethode(exception.getMessage());
				}finally{
					callPanelReleaseUploadUIMethod(true); //disables the Upload GUI launchers
				}
			}
		}, filePath, new Integer(this.maxSize));// end of ClientContext.loadFile
		return returnedMessage;
	}

	/**
	 * uploadPreparedFiles to the server path directory
	 * @param files
	 */
	public void uploadPreparedFiles(List<java.io.File> files){
		this.uploadPreparedFiles(files, 0);
	}

	/**
	 * uploadPreparedFiles to the server path directory
	 * @param files
	 * @param _maxSize
	 */
	public void uploadPreparedFiles(final List<java.io.File> files, final int _maxSize){
		this.uploadPreparedFiles(files, _maxSize, true);
	}

	private void uploadPreparedFiles(final List<java.io.File> files, final int _maxSize, boolean clearList){
		if(clearList) {
			this.returnedMessage = new ReturnedMessage();
			this.returnedMessage.setType(FileHandler.INFORMATION_MESSAGE);
			this.returnedMessage.setText("");
			this.returnedMessage.setFile(null);
			this.returnedMessage.setFiles(List.create(java.io.File.class));
			// we exclude all null files from the list.
			if(files!=null) {
				List<java.io.File> purge = List.create(java.io.File.class);
				for(java.io.File f : files) {
					if(f!=null){
						purge.add(f);
					}
				}
				files.clear();
				files.addAll(purge);
			}

		}

		if(_maxSize>0)
			this.maxSize=_maxSize*1024;

		ClientContext.setFileTransferMode(ClientContext.SYNCHRONOUS_MODE);
		if(files != null && files.size()>0){
			final String filePath = files.get(0).getPath().replace("\\","/");
			Ivy.log().debug("Choosed File path: "+filePath);
			files.remove(0);
			ClientContext.loadFile(new IFileLoadHandler(){
				public void onFailure(int reason, String description) {
					onFailureCall(reason, new java.io.File(filePath).getName());
					if(files.size()>0){
						//upload the next files
						Ivy.log().error("Calling recursively uploadPreparedFiles from onFailure {0}",files);
						String s = PREPARING_NEXT_FILE_UPLOAD.concat(" ").concat(files.get(0).getName());
						RDCallbackMethodHandler.callRDMethod(ulcPane, progressMethodName, new Object[] { s });
						uploadPreparedFiles(files, _maxSize, false);
					}else {
						//This was the last file to upload: we call the callback methods
						if(areFilesStoredInDB) {
							callPanelUploadSuccessMethod(createReturnedMessageForUploadToDB(returnedMessage.getFiles()));
						}else {
							callPanelUploadSuccessMethod(returnedMessage.getFiles());
						}
						//enables the Upload GUI launchers
						callPanelReleaseUploadUIMethod(true); 
					}
				}

				public void onSuccess(InputStream ins[], String filePaths[], String fileNames[]){
					try{
						Ivy.log().info("Uploading begin preparedFile: {0}", filePaths[0]);
						final java.io.File fileToUpload = new java.io.File(filePath);
						final String fileName= fileToUpload.getName();
						final BufferedInputStream preparedFile = new BufferedInputStream(ins[0]);
						fileUnit = getInputStreamPercentLength(ins[0],20);
						Ivy.log().debug("BufferedInputStream preparedFile: {0} fileUnit = {1}", preparedFile.toString(),fileUnit);
						formatServerPath();
						final java.io.File serverFile = new java.io.File(serverPath.concat(fileNames[0]));
						@SuppressWarnings("resource")
						final BufferedOutputStream serverBOS = new BufferedOutputStream(new FileOutputStream(serverFile));
						Ivy.log().debug("BufferedOutputStream serverBOS: "+serverFile);
						cleanTimer();
						callPanelReleaseUploadUIMethod(false); //disables the Upload GUI launchers
						timer.addActionListener(new IActionListener() {
							private static final long serialVersionUID = -3025252014358720080L;
							byte b[] = new byte[1024];
							int totalKBUploaded =0 ;
							int intRead;
							boolean done=false;
							public void actionPerformed(ActionEvent arg0) {
								int progressDone =0;
								try{
									while(!done && progressDone< fileUnit){	
										if((intRead= preparedFile.read(b)) != -1){
											serverBOS.write(b,0,intRead);
											progressDone++;
											totalKBUploaded++;
										}else{ 
											done=true;
											try {
												serverBOS.close();
											} catch (IOException e1) {
												Ivy.log().error("Error while trying to close BufferedOutputStream after upload for {0}",serverFile);
											}
										}
									}
									if(!done){
										progressDone=0;
									}
								}catch(IOException e){
									done=true;
									try {
										serverBOS.close();
									} catch (IOException e1) {
										Ivy.log().error("Error while trying to close BufferedOutputStream after upload for {0}",serverFile);
									}
									callPanelErrorMethode(e.getMessage());
								}// end try/catch
								if(!done){
									String s = fileName+" "+totalKBUploaded+" Kb uploaded";
									RDCallbackMethodHandler.callRDMethod(ulcPane, progressMethodName, new Object[] { s });
									timer.restart(); // restart the timer because the file is not completely uploaded
								}else{
									//The file was uploaded completely

									returnedMessage.setType(FileHandler.SUCCESS_MESSAGE);
									returnedMessage.setText(UPLOAD_SUCCESSFUL);
									returnedMessage.getFiles().add(serverFile);
									//RDCallbackMethodHandler.callRDMethod(ulcPane, progressMethodName, new Object[] { s });
									if(files.size()>0){
										//upload the next files
										Ivy.log().info("Calling recursively uploadPreparedFiles from onSuccess {0}",files);
										String s = PREPARING_NEXT_FILE_UPLOAD.concat(" ").concat(files.get(0).getName());
										RDCallbackMethodHandler.callRDMethod(ulcPane, progressMethodName, new Object[] { s });
										uploadPreparedFiles(files, _maxSize, false);
									}else {
										//This was the last file to upload: we call the callback methods
										if(areFilesStoredInDB) {
											callPanelUploadSuccessMethod(createReturnedMessageForUploadToDB(returnedMessage.getFiles()));
										}else {
											callPanelUploadSuccessMethod(returnedMessage.getFiles());
										}
										//enables the Upload GUI launchers
										callPanelReleaseUploadUIMethod(true); 
									}
								}//end if else
							}// end of actionPerformed
						});// end of timer
						timer.start();

					}catch(IOException ioe){
						callPanelErrorMethode(ioe.getMessage());
					}catch(Exception e) { 
						callPanelErrorMethode(e.getMessage());
					}
				}//end of onSuccess
			}, filePath, new Integer(this.maxSize));// end of ClientContext.loadFile
		}

	}

	/**
	 * This method allows to upload a java.io.File from the client Filesystem to the server Filesystem.<br>
	 * The uploaded java.io.File is given as input parameter. No Filechooser appears.<br>
	 * The upload process is interrupted 10 times with an ULCPollingTimer to be able to update the UI for progress (ProgressBar...)<br>
	 * This method doesn't check if the java.io.File exists<br>
	 * If the FileUploadHandler was instanciated with a reference to a IRichDialogPanel, this method will try to call the following<br>
	 * callback methods if they were also referenced:<br>
	 * <ul>
	 * <li>errorMethodeName, it will be called when an error occurs with a returnedMessage Object as parameter,
	 * <li>progressMethod: the upload progress will be shown with the use of this method at RD parent side
	 * <li>a uploadSuccessMethodName, it will be called if the Upload succeeds, it sends a returnedMessage Object <br>
	 * with a reference to the java.io.File object that was Uploaded.
	 * </ul>
	 * @return ReturnedMessage returnedMessage <br>
	 * This Object contains the type of message (FileHandler.ERROR_MESSAGE, FileHandler.Information_MESSAGE or FileHandler.SUCCESS_MESSAGE),<br>
	 * the message text as String and the java.io.File that was choosen for the Upload.<br>
	 * If the returnedMessage Type is ERROR, the java.io.File object may be null. So always check this java.io.File before use in your process.
	 */
	public ReturnedMessage uploadPreparedUploadWithoutShowingProgress(final java.io.File f)
	{
		this.returnedMessage = new ReturnedMessage();
		this.returnedMessage.setType(FileHandler.INFORMATION_MESSAGE);
		this.returnedMessage.setText("");
		this.returnedMessage.setFile(null);
		this.returnedMessage.setFiles(List.create(java.io.File.class));
		String filePath = f.getPath().replace("\\", "/");
		ClientContext.loadFile(new IFileLoadHandler(){
			public void onFailure(int reason, String description)
			{
				onFailureCall(reason, f!=null?f.getName():"");
			}

			public void onSuccess(InputStream ins[], String filePaths[], String fileNames[])
			{
				try{
					uploadedFile = f;
					final BufferedInputStream preparedFile = new BufferedInputStream(ins[0]);
					serverFilePath = uploadedFile.getName();
					formatServerPath();
					serverFilePath = (new StringBuilder(String.valueOf(serverPath))).append(serverFilePath).toString();
					final java.io.File serverFile = new java.io.File(serverFilePath);
					uploadedFile = new java.io.File(serverFilePath);
					final BufferedOutputStream server = new BufferedOutputStream(new FileOutputStream(serverFile));
					byte b[] = new byte[1024];
					int intRead;
					while((intRead= preparedFile.read(b)) != -1){
						server.write(b,0,intRead);
					}
					server.close();
					returnedMessage.setType(FileHandler.SUCCESS_MESSAGE);
					returnedMessage.setText(UPLOAD_SUCCESSFUL+" "+f.getName());
					returnedMessage.setFile(serverFile);
					RDCallbackMethodHandler.callRDMethod(ulcPane, progressMethodName, new Object[] { 100 });
					try{
						List<java.io.File> lFiles = List.create(java.io.File.class);
						lFiles.add(serverFile);			
						if(areFilesStoredInDB){
							callPanelUploadSuccessMethod(createReturnedMessageForUploadToDB(lFiles));
						}else{
							callPanelUploadSuccessMethod(lFiles);
						}
					}catch(Exception e){
						callPanelErrorMethode(e.getMessage());
					}


				}// end of try
				catch(Exception exception) { 
					callPanelErrorMethode(exception.getMessage());
				}
			}
		}, filePath ,new Integer(this.maxSize));// end of ClientContext.loadFile
		return returnedMessage;
	}

	/**
	 * This method allows to upload  java.io.File objects from the client Filesystem to the server Filesystem.<br>
	 * The uploaded java.io.Files are given as input parameter. No Filechooser appears.<br>
	 * This method doesn't check if the java.io.Files exist<br>
	 * The File transfer is ASYNCHRONOUS<br>
	 * If the FileUploadHandler was instanciated with a reference to a IRichDialogPanel, this method will try to call the following<br>
	 * callback methods if they were also referenced:<br>
	 * <ul>
	 * <li>errorMethodeName, it will be called when an error occurs with a returnedMessage Object as parameter (List of files empty),
	 * <li>a uploadSuccessMethodName, it will be called if the Upload succeeds, it sends a returnedMessage Object with the List of java.io.Files that were successfully uploaded<br>
	 * </ul>
	 * @return ReturnedMessage returnedMessage <br>
	 * This Object contains the type of message (FileHandler.ERROR_MESSAGE, FileHandler.Information_MESSAGE or FileHandler.SUCCESS_MESSAGE),<br>
	 * the message text as String and the List of java.io.Files that were successfully uploaded.<br>
	 */
	public ReturnedMessage uploadPreparedFilesWithoutShowingProgress(final List<java.io.File> files) {
		return this.uploadPreparedFilesWithoutShowingProgress(files,0);
	}

	public ReturnedMessage uploadPreparedFilesWithoutShowingProgress(final List<java.io.File> files,final int _maxSize) {

		this.uploadPreparedFilesWithoutShowingProgress(files, _maxSize, true);

		return returnedMessage;
	}

	private ReturnedMessage uploadPreparedFilesWithoutShowingProgress(final List<java.io.File> files,final int _maxSize, boolean clearList) {
		if(clearList) {
			this.returnedMessage = new ReturnedMessage();
			this.returnedMessage.setType(FileHandler.INFORMATION_MESSAGE);
			this.returnedMessage.setText("");
			this.returnedMessage.setFile(null);
			this.returnedMessage.setFiles(List.create(java.io.File.class));
		}

		if(_maxSize>0)
			this.maxSize=_maxSize*1024;

		ClientContext.setFileTransferMode(ClientContext.SYNCHRONOUS_MODE);
		if(files != null && files.size()>0){
			final String filePath = files.get(0).getPath().replace("\\","/");
			Ivy.log().debug("Choosed File path: {0} \n max size {1}",filePath,this.maxSize);
			files.remove(0);
			ClientContext.loadFile(new IFileLoadHandler(){
				public void onFailure(int reason, String description) {
					onFailureCall(reason, new java.io.File(filePath).getName());
					if(files.size()>0){
						//upload the next files
						Ivy.log().debug("Calling recursively uploadPreparedFiles from onFailure {0}",files);
						uploadPreparedFiles(files, _maxSize, false);
					}else {
						//This was the last file to upload: we call the callback methods
						if(areFilesStoredInDB) {
							callPanelUploadSuccessMethod(createReturnedMessageForUploadToDB(returnedMessage.getFiles()));
						}else {
							callPanelUploadSuccessMethod(returnedMessage.getFiles());
						}
						//enables the Upload GUI launchers
						callPanelReleaseUploadUIMethod(true); 
					}
				}

				public void onSuccess(InputStream ins[], String filePaths[], String fileNames[]){
					try{
						Ivy.log().debug("Uploading begin preparedFile: {0}", filePaths[0]);
						final BufferedInputStream preparedFile = new BufferedInputStream(ins[0]);
						fileUnit = getInputStreamPercentLength(ins[0],20);
						Ivy.log().debug("BufferedInputStream preparedFile: {0} fileUnit = {1}", preparedFile.toString(),fileUnit);
						formatServerPath();
						final java.io.File serverFile = new java.io.File(serverPath.concat(fileNames[0]));
						final BufferedOutputStream serverBOS = new BufferedOutputStream(new FileOutputStream(serverFile));
						Ivy.log().debug("BufferedOutputStream serverBOS: "+serverFile);
						byte b[] = new byte[1024];
						int intRead;
						while((intRead= preparedFile.read(b)) != -1){
							serverBOS.write(b,0,intRead);
						}
						serverBOS.close();
						returnedMessage.setType(FileHandler.SUCCESS_MESSAGE);
						returnedMessage.setText(UPLOAD_SUCCESSFUL);
						returnedMessage.getFiles().add(serverFile);
						if(files.size()>0){
							//upload the next files
							Ivy.log().debug("Calling recursively uploadPreparedFiles from onSuccess {0} \n max size{1}",files,maxSize);
							uploadPreparedFilesWithoutShowingProgress(files, _maxSize, false);
						}else {
							//This was the last file to upload: we call the callback methods
							if(areFilesStoredInDB) {
								callPanelUploadSuccessMethod(createReturnedMessageForUploadToDB(returnedMessage.getFiles()));
							}else {
								callPanelUploadSuccessMethod(returnedMessage.getFiles());
							}
							//enables the Upload GUI launchers
							callPanelReleaseUploadUIMethod(true); 
						}
					}catch(IOException ioe){
						callPanelErrorMethode(ioe.getMessage());
					}catch(Exception e) { 
						callPanelErrorMethode(e.getMessage());
					}
				}//end of onSuccess
			}, filePath,  new Integer(this.maxSize));// end of ClientContext.loadFile
		}
		return this.returnedMessage;
	}

	/**
	 * 
	 * For private use only
	 * Format the serverPath of this FileUploadHandler Object.<br>
	 * Puts the right java.io.File.separator depending on the System.
	 * If the Directory doesn't exit on the server Filesystem, it will attempt to create it.<br>
	 * It puts a java.io.File.separator character at the end of the path, to be sure taht the java.io.File path will be correct.
	 */
	private void formatServerPath()
	{
		if(serverPath != null && !serverPath.equals("")){
			serverPath = serverPath.replace("\\", java.io.File.separator);
			serverPath = serverPath.replace("/", java.io.File.separator);

			java.io.File serverDir = new java.io.File(serverPath);
			if(serverDir.exists() && !serverDir.isDirectory() || !serverDir.exists())
				serverDir.mkdirs();
			if(serverPath.lastIndexOf(java.io.File.separator) != serverPath.length() - 1) 
				serverPath=serverPath+java.io.File.separator;
		}
	}

	/**
	 * Set the calling ULComponent implementing IRichDialogPanel 
	 * @param T ulcPane
	 * If the ulcPane parameter is not an instance of IRichDialogPanel, then it will be set to null
	 */
	public void setUlcPane(T ulcPane)
	{
		// Check if the given ulcPane implements ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel
		if(ulcPane instanceof IRichDialogPanel)
			this.ulcPane = ulcPane;
		else this.ulcPane=null;
	}

	/**
	 * Set the serverPath<br>
	 * The serverPath is the path on the server Filesystem where the UploadHandler has to work on.
	 * @param String serverPath
	 */
	public void setServerPath(String path)
	{
		if(path==null || path.trim().equals("") || path.trim().equals("/")|| path.trim().equals("\\"))
			this.serverPath = "uploadedFiles";
		else this.serverPath=path.trim();
		formatServerPath();
	}

	/**
	 * Set the callback method name for the error handling.<br>
	 * The callback methods are a way to interact with the process of the referenced Panel
	 * that implements the following interface ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel
	 * @param String errorMethodeName
	 */
	public void setErrorMethodName(String errorMethodeName)
	{
		this.errorMethodeName = errorMethodeName.trim();
	}

	/**
	 * Set the callback method name to get the java.io.File back from a success upload.<br>
	 * The callback methods are a way to interact with the process of the referenced Panel
	 * that implements the following interface ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel
	 * @param String uploadSuccessMethodName
	 */
	public void setUploadSuccessMethodName(String uploadSuccessMethodName)
	{
		this.uploadSuccessMethodName = uploadSuccessMethodName.trim();
	}

	/**
	 * @deprecated
	 * Set the callback method name to ask if a java.io.File should be overwriten or not.<br>
	 * The callback methods are a way to interact with the process of the referenced Panel
	 * that implements the following interface ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel
	 * @param String askIfOverWriteFileMethodName
	 */
	public void setAskForChangeFileMethodName(String askForChangeFileMethodName)
	{
		this.askIfOverWriteFileMethodName = askForChangeFileMethodName.trim();
	}

	/**
	 *
	 * Set the callback method name to show upload progress.<br>
	 * The callback methods are a way to interact with the process of the referenced Panel
	 * that implements the following interface ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel
	 * @param String progressMethodName
	 */
	public void setProgressMethodName(String progressMethodName)
	{
		this.progressMethodName = progressMethodName.trim();
	}

	/**
	 * Allows to delete a java.io.File
	 * @param String filepath: the Filepath of the java.io.File to delete.
	 * If the FileUploadHandler was instanciated with a reference to a IRichDialogPanel, this method will try to call the following<br>
	 * callback methods if they were also referenced:<br>
	 * <ul>
	 * <li>errorMethodeName, it will be called when an error occurs with a returnedMessage Object as parameter,<br>
	 * In case of an error, the reference to the deleted java.io.File in the returnedMessage Object will be always null.<br>
	 * If the file is a directory, this is considered as an error and this method returns a returnMessage with ERROR_MESSAGE as Type and the directory won’t be deleted.
	 * </ul>
	 * @return ReturnedMessage
	 */
	public ReturnedMessage deleteFile(String filepath)
	{
		returnedMessage.setType(FileHandler.ERROR_MESSAGE);
		returnedMessage.setText("Begin of the method delete.");
		returnedMessage.setFile(null);
		returnedMessage = FileHandler.deleteFile(filepath);

		if(returnedMessage.getType().intValue()==FileHandler.ERROR_MESSAGE)
		{
			RDCallbackMethodHandler.callRDMethod(ulcPane, errorMethodeName, new Object[] { returnedMessage });
		}
		return returnedMessage;
	}

	/**
	 * Allows to delete a java.io.File and its directory if no files anymore
	 * @param String filepath: the Filepath of the java.io.File to delete.
	 * If the FileUploadHandler was instantiated with a reference to a RichDialogGridBagPanel, this method will try to call the following<br>
	 * callback methods if they were also referenced:<br>
	 * <ul>
	 * <li>errorMethodeName, it will be called when an error occurs with a returnedMessage Object as parameter,<br>
	 * In case of an error, the reference to the deleted java.io.File in the returnedMessage Object will be always null.
	 * If the file is a directory, this is considered as an error and this method returns a returnMessage with ERROR_MESSAGE as Type and the directory won’t be deleted.
	 * </ul>
	 * @return ReturnedMessage
	 */
	public ReturnedMessage deleteFileAndDirectoryIfNoFile(String filepath)
	{
		java.io.File fileToDelete = new java.io.File(filepath);
		String dirPath=null;
		try{
			dirPath = FileHandler.getFileDirectoryPath(fileToDelete);
		}catch(Exception _ex){
			//do nothing here
		}

		returnedMessage.setType(FileHandler.ERROR_MESSAGE);
		returnedMessage.setText("Begin of the method delete.");
		returnedMessage.setFile(null);
		returnedMessage.setFiles(List.create(java.io.File.class));
		if(dirPath==null){
			return returnedMessage;
		}
		returnedMessage = FileHandler.deleteFile(filepath);

		if(returnedMessage.getType().intValue()==FileHandler.ERROR_MESSAGE)
			RDCallbackMethodHandler.callRDMethod(ulcPane, errorMethodeName, new Object[] { returnedMessage });
		else{
			java.util.ArrayList <DocumentOnServer> docList= FileHandler.getDocumentsInPathAll(dirPath);
			if(docList.size()<1){
				ReturnedMessage rm = new ReturnedMessage();
				rm= FileHandler.deleteDirectory(dirPath);
				if(rm.getType().intValue()==FileHandler.ERROR_MESSAGE){
					returnedMessage.setType(rm.getType());
					returnedMessage.setText(returnedMessage.getText()+ " BUT " +rm.getText());
				}
			}
		}
		return returnedMessage;
	}

	/**
	 * 
	 *For internal use only
	 */
	private void cleanTimer(){
		if(this.timer == null)
			this.timer=new ULCPollingTimer(100,null);
		IActionListener [] ia = this.timer.getActionListeners();
		for(IActionListener i: ia){
			this.timer.removeActionListener(i);
		}
		this.timer.setRepeats(false);
	}

	/**
	 * 
	 * @param s
	 */
	private void callPanelErrorMethode(String s){
		if(ulcPane!= null && errorMethodeName!= null && errorMethodeName.trim().length()>0){
			returnedMessage.setType(FileHandler.ERROR_MESSAGE);
			returnedMessage.setText(s);
			returnedMessage.setFile(null);
			RDCallbackMethodHandler.callRDMethod(ulcPane, errorMethodeName, new Object[] { returnedMessage });
		}
	}

	/**
	 * 
	 * @param _rmessage
	 */
	private void callPanelErrorMethode(ReturnedMessage _rmessage){
		if(ulcPane!= null && errorMethodeName!= null && errorMethodeName.trim().length()>0){
			RDCallbackMethodHandler.callRDMethod(ulcPane, errorMethodeName, new Object[] { _rmessage });
		}
	}

	/**
	 * calls the parent Rich Dialog method responsible to enabled/disabled the UI buttons, menuItems... responsible for launching the Upload process.
	 * @param reason
	 */
	private void callPanelReleaseUploadUIMethod(Boolean _b){
		if(_b!=null && this.ulcPane != null && this.releaseUploadUIMethodName!=null && !this.releaseUploadUIMethodName.trim().equals(""))
		{
			Ivy.log().debug("callPanelReleaseUploadUIMethod with Boolean {0}",_b);
			RDCallbackMethodHandler.callRDMethod(ulcPane, this.releaseUploadUIMethodName, new Object[] { _b });
		}
	}

	/**
	 * 
	 * @param reason
	 */
	private void onFailureCall(int reason){
		String msg;
		switch(reason)
		{
		case 1:
			msg = ACTION_CANCELLED;
			returnedMessage.setType(FileHandler.INFORMATION_MESSAGE);
			break;
		case 2:
			msg = ERROR_UNKOWN;
			returnedMessage.setType(FileHandler.ERROR_MESSAGE);
			break;
		case 3:
			msg = FILESIZE_TOO_BIG.replace("FSIZE", this.getMaxUploadSize());
			returnedMessage.setType(FileHandler.ERROR_MESSAGE);
			break;
		default:
			msg = ERROR_UNKOWN;
			returnedMessage.setType(FileHandler.ERROR_MESSAGE);
			break;
		}
		returnedMessage.setText(msg);
		returnedMessage.setFile(null);
		callPanelErrorMethode(returnedMessage);
	}

	/**
	 * 
	 * @param reason
	 * @param _FileName
	 */
	private void onFailureCall(int reason, String _FileName){
		String msg;
		Ivy.log().debug("Upload failure on {0} for reason {1}",_FileName,
				reason==1?"Action Cancelled":
					reason == 3 ?"File too big":"Error Unknown");
		switch(reason)
		{
		case 1:
			msg = ACTION_CANCELLED;
			returnedMessage.setType(FileHandler.INFORMATION_MESSAGE);
			break;
		case 2:
			msg = ERROR_UNKOWN;
			returnedMessage.setType(FileHandler.ERROR_MESSAGE);
			break;
		case 3:
			msg = FILESIZE_TOO_BIG.replace("FSIZE", this.getMaxUploadSize());
			returnedMessage.setType(FileHandler.ERROR_MESSAGE);
			break;
		default:
			msg = ERROR_UNKOWN;
			returnedMessage.setType(FileHandler.ERROR_MESSAGE);
			break;
		}
		returnedMessage.setText(msg);
		if(_FileName!=null && _FileName.trim().length()>0) {
			returnedMessage.setFile(new java.io.File(_FileName));
		}else {
			returnedMessage.setFile(null);
		}
		callPanelErrorMethode(returnedMessage);
	}

	private String getMaxUploadSize() {
		String s;
		if(this.maxSize>=1024) {
			float f = (float) this.maxSize;
			if(f%1024>0) {
				Double d = new Double(f/1024);
				s=new DecimalFormat("0.00").format(d);
			}else{
				double i = f/1024.0;
				Ivy.log().debug("Max size: "+i);
				s=""+i;
			}
			s+=" Mb";
		}else{
			s=""+this.maxSize+" Kb";
		}
		return s;
	}

	/**
	 * 
	 * @param allChoosedFiles
	 * @param filesToOverWrite
	 */
	private void callPanelAskIfOverwriteFiles(List<java.io.File> allChoosedFiles, List<java.io.File> filesToOverWrite){
		if(ulcPane!= null && askIfOverWriteFileMethodName!= null && askIfOverWriteFileMethodName.trim().length()>0){
			RDCallbackMethodHandler.callRDMethod(ulcPane, askIfOverWriteFileMethodName, new Object[] { allChoosedFiles, filesToOverWrite});
		}
	}

	private void callPanelAskIfOverwriteDocs(List<java.io.File> allChoosedFiles, List<DocumentOnServer> allChoosedDocs, List<DocumentOnServer> docsToOverwrite, String destination){
		if(ulcPane!= null && askIfOverWriteFileMethodName!= null && askIfOverWriteFileMethodName.trim().length()>0){
			RDCallbackMethodHandler.callRDMethod(ulcPane, askIfOverWriteFileMethodName, new Object[] { allChoosedFiles, allChoosedDocs, docsToOverwrite, destination});
		}
	}

	/**
	 * 
	 * @param l
	 */
	private void callPanelUploadSuccessMethod(List<java.io.File> l){
		if(ulcPane != null && this.uploadSuccessMethodName!=null && this.uploadSuccessMethodName.trim().length()>0 && l!=null){
			Ivy.log().info("callPanelUploadSuccessMethod with List<java.io.File> {0}",l);
			RDCallbackMethodHandler.callRDMethod(ulcPane, uploadSuccessMethodName, new Object[] { l });
		}
	}

	/**
	 * 
	 * @param r
	 */
	private void callPanelUploadSuccessMethod(ReturnedMessage r){
		if(ulcPane != null && this.uploadSuccessMethodName!=null && this.uploadSuccessMethodName.trim().length()>0 && r!=null){
			Ivy.log().debug("callPanelUploadSuccessMethod with returnedMessage {0}",r);
			RDCallbackMethodHandler.callRDMethod(ulcPane, uploadSuccessMethodName, new Object[] { r });
		}
	}

	/**
	 * Test Method for Bug 
	 */
	public void testUpload(){

		FileChooserConfig fcConfig= new FileChooserConfig();
		fcConfig.setFileSelectionMode(FileChooserConfig.FILES_ONLY);
		fcConfig.setMultiSelectionEnabled(false);

		ClientContext.loadFile(
				new com.ulcjava.base.application.util.IFileLoadHandler(){
					public void onFailure(int reason, String description){
						//do something
					}

					public void onSuccess(InputStream ins[], String filePaths[], String fileNames[]){
						//do something
					}
				},fcConfig,null);
	}
	public String getAskIfOverWriteFileMethodName() {
		return askIfOverWriteFileMethodName;
	}
	public void setAskIfOverWriteFileMethodName(String askIfOverWriteFileMethodName) {
		this.askIfOverWriteFileMethodName = askIfOverWriteFileMethodName;
	}
	public String getServerPath() {
		return serverPath;
	}
	public String getServerFilePath() {
		return serverFilePath;
	}
	public void setServerFilePath(String serverFilePath) {
		this.serverFilePath = serverFilePath;
	}
	public String getErrorMethodeName() {
		return errorMethodeName;
	}
	public void setErrorMethodeName(String errorMethodeName) {
		this.errorMethodeName = errorMethodeName;
	}
	public String getUploadSuccessMethodName() {
		return uploadSuccessMethodName;
	}
	public String getProgressMethodName() {
		return progressMethodName;
	}
	/**
	 * @param maxSize the maxSize to set
	 */
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	/**
	 * @return the maxSize
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * @return the areFilesStoredInDB
	 */
	public boolean isAreFilesStoredInDB() {
		return areFilesStoredInDB;
	}
	/**
	 * @param areFilesStoredInDB the areFilesStoredInDB to set
	 */
	public void setAreFilesStoredInDB(boolean areFilesStoredInDB) {
		this.areFilesStoredInDB = areFilesStoredInDB;
	}
	/**
	 * @return the filesDestinationPathForDB
	 */
	public String getFilesDestinationPathForDB() {
		return filesDestinationPathForDB;
	}
	/**
	 * @param filesDestinationPathForDB the filesDestinationPathForDB to set
	 */
	public void setFilesDestinationPathForDB(String filesDestinationPathForDB) {
		this.filesDestinationPathForDB = PathUtil.formathPathForDirectoryWithoutFirstSeparatorWithEndSeparator(filesDestinationPathForDB);
	}

	/**
	 * 
	 * @param li
	 * @throws Exception
	 */
	private ReturnedMessage createReturnedMessageForUploadToDB(List<java.io.File> li)
	{
		ReturnedMessage msg = new ReturnedMessage();
		msg.setType(FileHandler.SUCCESS_MESSAGE);
		msg.setText(this.returnedMessage.getText());
		if(this.returnedMessage.getFile()!=null) {
			msg.setFile(this.returnedMessage.getFile());
		}
		if(li!=null && !li.isEmpty()) {
			msg.setDocumentOnServers(List.create(DocumentOnServer.class));
			msg.setFiles(List.create(java.io.File.class));
			msg.getFiles().addAll(li);

			String user = Ivy.session().getSessionUserName();
			String date = new Date().format("dd.MM.yyyy");
			String time = new Time().format();
			for(java.io.File f: li)
			{
				DocumentOnServer doc = new DocumentOnServer();
				doc.setFilename(f.getName());
				doc.setPath(this.filesDestinationPathForDB+f.getName());
				if(f.exists()){
					doc.setFileSize(FileHandler.getFileSize(f));
				}
				doc.setCreationDate(date);
				doc.setCreationTime(time);
				try{
					doc.setExtension(f.getName().substring(f.getName().lastIndexOf(".")));
				}catch(Exception ex){

				}
				doc.setUserID(user);
				doc.setDescription("");
				doc.setJavaFile(f);
				doc.setLocked("0");
				doc.setModificationDate(date);
				doc.setModificationTime(time);
				doc.setModificationUserID(user);

				msg.getDocumentOnServers().add(doc);
			}
		}
		this.returnedMessage.getFiles().clear();
		return msg;
	}
	/**
	 * @return the fileHandlerMgt
	 */
	public AbstractFileManagementHandler getFileHandlerMgt() {
		return fileHandlerMgt;
	}
	/**
	 * @param fileHandlerMgt the fileHandlerMgt to set
	 */
	public void setFileHandlerMgt(AbstractFileManagementHandler fileHandlerMgt) {
		this.fileHandlerMgt = fileHandlerMgt;
	}

	/**
	 * 
	 * @param in InputStream which byte length has to be computed
	 * @param percent (int) must be greater than 0 and less or egal to 100.
	 * @return The number of Kb representing the given percentage of the InputStream as an int approximation.<br>
	 * Return -1 if something went wrong. <br>
	 * Returns 1 if the InputStream contains lees then one Kb.<br>
	 * {@code Example: a give file's size is 1Mb. Calling this method with percent = 10 will return 102. 10% from the file size is about 102 Kb}
	 */
	private final static int getInputStreamPercentLength(InputStream in, int percent)
	{
		int total=0;
		int intRead=0;
		try{
			assert percent>0 && percent<=100:percent;
			in.mark(Integer.MAX_VALUE);
			byte b[] = new byte[102400];

			while(total<1024000 && (intRead= in.read(b))!=-1){
				total+=intRead;
			}

		}catch(Throwable t){
			total=-1;
			Ivy.log().error(t.getMessage());
		}finally
		{
			try{
				in.reset();
				in.close();
			}catch(Exception ex){
				Ivy.log().error(ex.getMessage());
			}
		}
		total=intRead==-1?(int) ((total/1024)*percent/100):600;
		if(total==0){
			total=1;
		}
		return total;
	}
	/**
	 * Upload files from the client to the server.<br>
	 * The files to upload are selected with the help of a File Chooser
	 * @param multifiles if true more several files can be uploaded at a time.
	 * @param maxSize: max file size pro file.
	 * @param showProgress if true each file upload will be interrupted 5 times to be able to show a progress.
	 */
	public void uploadFiles(boolean multifiles, final int maxSize, final boolean showProgress) {
		this.chooseFiles(multifiles, maxSize, showProgress);
	}

	private void chooseFiles(boolean multifiles, final int maxSize, final boolean showProgress){
		FileChooserConfig fcConfig = new FileChooserConfig();
		fcConfig.setDialogTitle(multifiles?MULTIFILE_UPLOAD_WINDOW_TITLE:SINGLEFILE_UPLOAD_WINDOW_TITLE);
		fcConfig.setMultiSelectionEnabled(multifiles);
		fcConfig.setFileSelectionMode(FileChooserConfig.FILES_ONLY);
		ClientContext.setFileTransferMode(ClientContext.ASYNCHRONOUS_MODE);
		final ArrayList<java.io.File> files = new ArrayList<java.io.File>();
		ClientContext.chooseFile(new IFileChooseHandler(){
			@Override
			public void onFailure(int reason, String description) {
				onFailureCall(reason);
			}
			@Override
			public void onSuccess(String[] filePaths, String[] fileNames) {
				try{
					for(int i=0; i<filePaths.length; i++) {
						files.add(new java.io.File(filePaths[i]));
					}
					List<java.io.File> fList = List.create(java.io.File.class);
					fList.addAll(files);
					if(showProgress) {
						if(choosedFilesExist(filePaths, fileNames).getExistingDocs().isEmpty()) {
							Ivy.log().debug("Found NO existing files.");
							uploadPreparedFiles(fList, maxSize);
						}else {
							Ivy.log().debug("Found existing files. chooseFiles method CLientCOntext");
						}
					} else {
						uploadPreparedFilesWithoutShowingProgress(fList, maxSize);
					}
				}
				catch(Exception exception) { 
					callPanelErrorMethode(exception.getMessage());
				}
			}
		}
		, fcConfig, this.ulcPane);
	}

	protected FileExistingReport choosedFilesExist(String[] paths, String fileNames[]) {
		List<DocumentOnServer> choosedDocs = List.create(DocumentOnServer.class);
		List<DocumentOnServer> existingDocs = List.create(DocumentOnServer.class);
		List<java.io.File> allChoosedFiles = List.create(java.io.File.class);
		List<java.io.File> existingFiles = List.create(java.io.File.class);
		FileExistingReport report = new FileExistingReport();
		ArrayList<Integer> ints = new ArrayList<Integer>();
		boolean goesOn =true;
		int n = paths.length;
		for(int i = 0; i<paths.length;i++){
			allChoosedFiles.add(new java.io.File(paths[i]));
		}
		try{
			if(!areFilesStoredInDB) {
				//we check if we will overwrite some files
				for(int i=0; i<n;i++){
					String fileOnServer = serverPath + fileNames[i];
					java.io.File serverFile = new java.io.File(fileOnServer);
					DocumentOnServer doc = new DocumentOnServer();
					doc.setFilename(fileNames[i]);
					doc.setPath(serverPath + fileNames[i]);
					choosedDocs.add(doc);
					if(serverFile.exists()){
						existingFiles.add(serverFile); 
						returnedMessage.setFile(serverFile);
						existingDocs.add(doc);
					}else {
						ints.add(i);
					}
				}
				if(existingFiles.size()>0){ // we try to call the callback method from the RDC to ask if we overwrite 
					goesOn=false;
					//callPanelAskIfOverwriteFiles(allChoosedFiles, existingFiles);
				}
			}else{
				if(fileHandlerMgt!=null){
					for(int i = 0; i<n;i++){
						DocumentOnServer doc = new DocumentOnServer();
						doc.setFilename(fileNames[i]);
						doc.setPath(filesDestinationPathForDB+fileNames[i]);
						choosedDocs.add(doc);
						if(fileHandlerMgt.documentOnServerExists(doc, filesDestinationPathForDB)){
							Ivy.log().debug("Document Exist !!! {0}",doc);
							existingDocs.add(doc);
							Ivy.log().debug("Now existing docs {0}",existingDocs);
							goesOn=false;
						}else {
							ints.add(i);
						}	
					}
				}
			}
			if(!goesOn){//We have to ask if override
				Ivy.log().debug("In under method files found ! {0}",existingDocs);
				report.getExistingDocs().addAll(existingDocs);
				ReturnedMessage msg = new ReturnedMessage();
				msg.setType(FileHandler.INFORMATION_MESSAGE);
				msg.setText(FILE_ALREADY_EXISTS);
				msg.setFiles(allChoosedFiles);
				msg.setDocumentOnServers(existingDocs);
				if(!areFilesStoredInDB){
					RDCallbackMethodHandler.callRDMethod(ulcPane, askIfOverWriteFileMethodName, new Object[] { msg });
					callPanelAskIfOverwriteFiles(allChoosedFiles,existingFiles);
				}else{
					Ivy.log().debug("Now existing docs {0}",existingDocs);
					callPanelAskIfOverwriteDocs(allChoosedFiles, choosedDocs,existingDocs,filesDestinationPathForDB);
					RDCallbackMethodHandler.callRDMethod(ulcPane, askIfOverWriteFileMethodName, new Object[] { msg });
				}
				callPanelReleaseUploadUIMethod(true); //enables the Upload GUI launchers
			}
		}catch(Exception ex){
			Ivy.log().error("Error in UploadHandler protected FileExistingReport choosedFilesExist(String[] paths, String fileNames[])", ex);
			returnedMessage.setType(FileHandler.ERROR_MESSAGE);
			returnedMessage.setText("");
			callPanelErrorMethode(returnedMessage);
			callPanelReleaseUploadUIMethod(true);
		}
		report.setAllDocs(choosedDocs);
		report.setAllFiles(allChoosedFiles);
		//report.setExistingDocs(existingDocs);
		Ivy.log().debug("Report will be sent with {0}",existingDocs);
		report.setExistingFiles(existingFiles);
		report.setIndexForInputStreamsWithoutOverwritting(ints);
		return report;
	}

	/**
	 * @return the config
	 */
	public BasicConfigurationController getConfig() {
		return config;
	}
	/**
	 * @param config the config to set
	 */
	public void setConfig(BasicConfigurationController config) {
		this.config = config;
	}

	@SuppressWarnings("unchecked")
	protected java.util.List<Object> checkUploadFilteringReturnAcceptedAndRejectedFiles(InputStream ins[], String filePaths[], String serverPath) {

		java.util.List<Object> result = new java.util.ArrayList<Object>() ;
		result.add(0, List.create(java.io.File.class));
		result.add(1,  List.create(java.io.File.class));
		result.add(2, new ArrayList<InputStream>());
		if(filePaths.length>0 && this.config!=null && this.config.getDocumentFilter()!=null) {
			DocumentFilter filter = this.config.getDocumentFilter();
			for(int i = 0; i<filePaths.length;i++) {
				java.io.File f = new java.io.File(filePaths[i]);
				DocumentOnServer doc = new DocumentOnServer();
				doc.setFilename(f.getName());
				doc.setExtension(FileHandler.getFileExtension(f.getName()));
				doc.setPath(serverPath!=null?serverPath+f.getName():f.getPath());
				if(filter.allowAction(doc, DocumentFilterActionEnum.UPLOAD).isAllow()) {
					((List<java.io.File>) result.get(0)).add(f);
					if(ins!=null && ins.length>i){
						((ArrayList<InputStream>) result.get(2)).add(ins[i]);
					}
				}else {
					((List<java.io.File>) result.get(1)).add(f);
				}
			}
		}else {
			List<java.io.File> allChoosedFiles = makeChoosedFiles(filePaths);
			((List<java.io.File>) result.get(0)).addAll(allChoosedFiles);
		}
		return result;
	}

	protected DocumentFilterAnswer checkUploadFilteringReturnIfFileAccepted(java.io.File choosedFile, String serverPath) {

		if(choosedFile!=null && this.config!=null && this.config.getDocumentFilter()!=null) {
			DocumentFilter filter = this.config.getDocumentFilter();

			DocumentOnServer doc = new DocumentOnServer();
			doc.setFilename(choosedFile.getName());
			doc.setExtension(FileHandler.getFileExtension(choosedFile.getName()));
			doc.setPath(serverPath!=null?serverPath+choosedFile.getName():choosedFile.getPath());
			return filter.allowAction(doc, DocumentFilterActionEnum.UPLOAD);
		}else {
			DocumentFilterAnswer answer = new DocumentFilterAnswer();
			answer.setAllow(true);
			return answer;
		}
	}

	private List<java.io.File> makeChoosedFiles(String[] filePaths) {
		List<java.io.File> allChoosedFiles = List.create(java.io.File.class);
		if(filePaths==null) {
			return allChoosedFiles;
		}
		for(int i =0; i<filePaths.length; i++) {
			allChoosedFiles.add(new java.io.File(filePaths[i]));
		}
		return allChoosedFiles;
	}

	private class FileExistingReport {
		private ArrayList<Integer> indexForInputStreamsWithoutOverwritting;
		private List<java.io.File> existingFiles;
		private List<java.io.File> allFiles;
		private List<DocumentOnServer> existingDocs;
		private List<DocumentOnServer> allDocs;

		public FileExistingReport(){
			indexForInputStreamsWithoutOverwritting = new ArrayList<Integer>();
			existingFiles = List.create(java.io.File.class);
			allFiles = List.create(java.io.File.class);
			existingDocs = List.create(DocumentOnServer.class);
			allDocs = List.create(DocumentOnServer.class);
		}
		public void setIndexForInputStreamsWithoutOverwritting(
				ArrayList<Integer> indexForInputStreamsWithoutOverwritting) {
			this.indexForInputStreamsWithoutOverwritting = indexForInputStreamsWithoutOverwritting;
		}

		@SuppressWarnings("unused")
		public ArrayList<Integer> getIndexForInputStreamsWithoutOverwritting() {
			return indexForInputStreamsWithoutOverwritting;
		}

		public void setExistingFiles(List<java.io.File> existingFiles) {
			this.existingFiles = existingFiles;
		}

		@SuppressWarnings("unused")
		public List<java.io.File> getExistingFiles() {
			return existingFiles;
		}

		public void setAllFiles(List<java.io.File> allFiles) {
			this.allFiles = allFiles;
		}

		@SuppressWarnings("unused")
		public List<java.io.File> getAllFiles() {
			return allFiles;
		}
		@SuppressWarnings("unused")
		public void setExistingDocs(List<DocumentOnServer> existingDocs) {
			this.existingDocs = existingDocs;
		}

		public List<DocumentOnServer> getExistingDocs() {
			return existingDocs;
		}

		public void setAllDocs(List<DocumentOnServer> allDocs) {
			this.allDocs = allDocs;
		}

		@SuppressWarnings("unused")
		public java.util.List<DocumentOnServer> getAllDocs() {
			return allDocs;
		}
	}

}
