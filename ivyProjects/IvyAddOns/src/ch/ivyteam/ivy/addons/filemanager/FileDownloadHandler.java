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

import static java.lang.Math.round;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.ivy.addons.docfactory.FileOperationMessage;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;
import ch.ivyteam.ivy.addons.util.RDCallbackMethodHandler;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.xpertline.ulc.server.headless.ULCXDesktop;
import ch.xpertline.ulc.server.headless.ULCXDesktop.OnDesktopExceptionEvent;
import ch.xpertline.ulc.server.headless.ULCXDesktop.OnDesktopExceptionListener;

import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.ULCComponent;
import com.ulcjava.base.application.ULCPollingTimer;
import com.ulcjava.base.application.event.ActionEvent;
import com.ulcjava.base.application.event.IActionListener;
import com.ulcjava.base.application.util.IFileChooseHandler;
import com.ulcjava.base.application.util.IFileStoreHandler;
import com.ulcjava.base.shared.FileChooserConfig;


/**
 * @author ec<br>
 * FileDownloadHandler class<br>
 * A FileDownloadHandler Object manages the download process of a file from the Server to the client.<br>
 * To be usable with all its features, it has to be instanciated with a reference to its parent ULComponent implementing IRichDialogPanel.<br>
 * With such a reference, the FileDownloadHandler can communicate directly with its parent component through callback methods. <br>
 * The name of the callback methods has to be indicated in the constructor or with their setter methods (see further). <br>
 * (In the case of callback reference, its parent must be a RichDialogGridBagPanel).<br><br>
 * 
 * It relies on the ch.xpertline.ria.util.file.FileOperationMessage dataclass for returning the result of most Upload process actions.<br><br>
 * 
 * To be able to download a File from the server, you have first to select a File on the server FileSystem.<br>
 * The updownloadPane Rich Dialog provides a table where the user can choose this File. <br><br>
 * 
 * The user will be asked to choose a place on his client File system where to save the downloaded File. This is performed with a FileChooser.<br><br>
 * 
 * The source path on the Server is determined by an intern private String variable. <br>
 * The server path variable is given at the instantiation within the constructor, or can be later changed with the setServerPath method.
 * For security purposes, if the server path is set to null or points to the root of the server, it takes automatically a default value.
 * 
 */
public class FileDownloadHandler<T extends ULCComponent & IRichDialogPanel>  implements OnDesktopExceptionListener /*implements GetFilesListReturnedListener*/{
	private String serverPath;
	private T ulcPane = null;
	private String errorMethodeName, downloadSuccessMethodeName, progressMethodName, callbackForOverriding;
	private FileOperationMessage fileOperationMessage;
	private String downloadTitle="";
	private String chooseButton="";
	final ULCPollingTimer timer =new ULCPollingTimer(100,null);
	private ULCXDesktop desktop=null;
	private Method dGetFilesAtClientSideMethod = null;
	@SuppressWarnings("unused")
	private DownloadGetDirectoryInfoListener listener = null;
	private List<File> preparedFiles;
	private String preparedPath;
	
	private long errorInterval;
	
	public FileDownloadHandler(){
		this(null, "", "", "","");
	}
	
	/**
	 * @author ec new FileDownloadHandler 
	 * @param RichDialogGridBagPanel ulcPane<br>
	 * This ULCPane is important if you want that this Object can callback some specified methods of the Panel. 
	 * @param serverpath: the String representation of the serverpath entrypoint for the application
	 * 
	 */
	public FileDownloadHandler(final T ulcPane,
			String serverPath) {
		this(ulcPane,"", "","", serverPath);
		
	}
	
	/**
	 * @author ec new FileDownloadHandler 
	 * @param T ulcPane<br>
	 * This ULCPane is important if you want that this Object can callback some specified methods of the Panel.<br>
	 * It has to be an ULComponent implementing IRichDialogPanel. 
	 * @param String errorMethode
	 * The name of the methode that is used by the ulcPane to manage the download errors messages.
	 * Can be an empty String if no such method exits.
	 * @param String successMethode
	 * The name of the methode that is used by the ulcPane to manage the download success messages
	 * Can be an empty String if no such method exits.
	 * @param String progressMethode
	 * The name of the methode that is used by the ulcPane to show the progress of long tasks
	 * Can be an empty String if no such method exits.
	 * @param serverpath: the String representation of the serverpath entrypoint for the application. 
	 */
	public FileDownloadHandler(final T ulcPane, String errorMethode, String successMethode, 
			String progressMethod, String path) {
		if(ulcPane instanceof ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel)
        	this.ulcPane = ulcPane;
        else this.ulcPane=null;
		this.errorMethodeName=errorMethode.trim();
		this.downloadSuccessMethodeName= successMethode.trim();
		this.progressMethodName= progressMethod.trim();
		if(path==null || path.trim().equals("") || path.trim().equals("/")|| path.trim().equals("\\"))
            serverPath = "uploadedFiles";
        else
            serverPath = path;
        formatServerPath();
		this.fileOperationMessage = new FileOperationMessage();
		this.downloadTitle =Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/download/label/chooseDirectory");
		this.chooseButton = Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/download/label/chooseButton");
		timer.setRepeats(false);
		
		this.desktop=new ULCXDesktop();
		this.desktop.addOnDesktopExceptionListener(this);
		try{
			this.dGetFilesAtClientSideMethod = this.desktop.getClass().getMethod("getDirectoryInfoUnderPath", String.class);
			this.listener=new DownloadGetDirectoryInfoListener(this.desktop, this);
		}catch (Exception ex) {
			Ivy.log().info("The method getDirectoryInfoUnderPath is not implemented in the ULC exctension.");
		}
	}
	
	/**
	 * Downloads a File directly to the client. The parameter is just the String name of the chosen File to download.<br>
	 * If the FileDownloadHandler was instantiated with a parent RD reference and callbackmethods names,<br>
	 * it will be able to communicate with its parent RD<br>
	 * No progress is shown for one File download.<br>
	 * @param final String choosedFileName : the chosen filename to download<br>
	 * This File has to be in the actual serverPath.<br>
	 * 
	 * 
	 */
	public FileOperationMessage download(final String choosedFileName) {
		fileOperationMessage.setType(FileOperationMessage.INFORMATION_MESSAGE);
		fileOperationMessage.setMessage("");
		fileOperationMessage.emptyFileList();
		
		final FileChooserConfig fcConfig = new FileChooserConfig();
		fcConfig.setDialogTitle(this.downloadTitle);
		
		fcConfig.setMultiSelectionEnabled(false); // We accept just one file at time
		fcConfig.setApproveButtonText(this.chooseButton);

		fcConfig.setSelectedFile(choosedFileName);
		try {
			ClientContext.storeFile(new IFileStoreHandler() {
				public void onFailure(int reason, String description) {
					makeError(reason,description);
				}

				public void prepareFile(OutputStream data) {
					try {
						formatServerPath();
						File serverDir = new File(serverPath);
						if((serverDir.exists() && !serverDir.isDirectory()) || !serverDir.exists()){
							fileOperationMessage.setType(FileOperationMessage.ERROR_MESSAGE);
		            		fileOperationMessage.setMessage("The directory supposed to contain the File to download doesn't exit. "+serverPath);
		            		fileOperationMessage.emptyFileList();
		            		RDCallbackMethodHandler.callRDMethod(ulcPane, errorMethodeName, new Object[] { fileOperationMessage });
						}
						else {
							File f = new File(serverPath+choosedFileName);
							if(!f.exists()){
								fileOperationMessage.setType(FileOperationMessage.ERROR_MESSAGE);
			            		fileOperationMessage.setMessage("The file you try to download doesn't exit.");
			            		fileOperationMessage.emptyFileList();
			            		RDCallbackMethodHandler.callRDMethod(ulcPane, errorMethodeName, new Object[] { fileOperationMessage });
							}else{
								FileInputStream fis = new FileInputStream(f);
								byte b[] = new byte[1024]; 
			            		int c=0;
			            		while((c= fis.read(b)) != -1){
			            			data.write(b,0,c);
			            		}
			            		fis.close();
			            		
								fileOperationMessage.setType(FileOperationMessage.SUCCESS_MESSAGE);
			            		fileOperationMessage.addFile(f);
							}
						}
					} catch (IOException ioe) {
						fileOperationMessage.setType(FileOperationMessage.ERROR_MESSAGE);
	            		fileOperationMessage.setMessage(ioe.getMessage());
	            		fileOperationMessage.emptyFileList();
	            		RDCallbackMethodHandler.callRDMethod(ulcPane, errorMethodeName, new Object[] { fileOperationMessage });
					}
				}

				public void onSuccess(String filePath, String fileName) {
					RDCallbackMethodHandler.callRDMethod(ulcPane, downloadSuccessMethodeName, new Object[] { fileOperationMessage });
				}
			}, fcConfig, ulcPane);
		} catch (Exception e) {
			fileOperationMessage.setType(FileOperationMessage.ERROR_MESSAGE);
    		fileOperationMessage.setMessage(e.getMessage());
    		fileOperationMessage.emptyFileList();
    		RDCallbackMethodHandler.callRDMethod(ulcPane, errorMethodeName, new Object[] { fileOperationMessage });
		}
		return fileOperationMessage;
	}
	
	/**
	 * download a File directly to the client
	 * If the FileDownloadHandler was instanciated with a parent RD reference and callbackmethods names,<br>
	 * it will be able to communicate with its parent RD<br>
	 * No progress is shown for one File download.<br>
	 * @param final File file : the choosed File to download
	 * 
	 */
	public FileOperationMessage download(final File file) {
		fileOperationMessage.setType(FileOperationMessage.INFORMATION_MESSAGE);
		fileOperationMessage.setMessage("");
		fileOperationMessage.emptyFileList();
		
		final FileChooserConfig fcConfig = new FileChooserConfig();
		fcConfig.setDialogTitle(this.downloadTitle);
		
		fcConfig.setMultiSelectionEnabled(false); // We accept just one file at time
		fcConfig.setApproveButtonText(this.chooseButton);

		fcConfig.setSelectedFile(file.getName());
		try {
			ClientContext.storeFile(new IFileStoreHandler() {
				public void onFailure(int reason, String description) {
					makeError(reason,description);
				}

				public void prepareFile(OutputStream data) {
					try {
						formatServerPath();
						if(!file.exists()){
							fileOperationMessage.setType(FileOperationMessage.ERROR_MESSAGE);
							fileOperationMessage.setMessage("The file you try to download doesn't exit.");
							fileOperationMessage.emptyFileList();
							RDCallbackMethodHandler.callRDMethod(ulcPane, errorMethodeName, new Object[] { fileOperationMessage });
						}else{
							FileInputStream fis = new FileInputStream(file);
							byte b[] = new byte[1024]; 
							int c=0;
							while((c= fis.read(b)) != -1){
								data.write(b,0,c);
							}
							fis.close();

							fileOperationMessage.setType(FileOperationMessage.SUCCESS_MESSAGE);
							fileOperationMessage.addFile(file);
						}
						
					} catch (IOException ioe) {
						fileOperationMessage.setType(FileOperationMessage.ERROR_MESSAGE);
	            		fileOperationMessage.setMessage(ioe.getMessage());
	            		fileOperationMessage.emptyFileList();
	            		RDCallbackMethodHandler.callRDMethod(ulcPane, errorMethodeName, new Object[] { fileOperationMessage });
					}
				}

				public void onSuccess(String filePath, String fileName) {
					RDCallbackMethodHandler.callRDMethod(ulcPane, downloadSuccessMethodeName, new Object[] { fileOperationMessage });
				}
			}, fcConfig, ulcPane);
		} catch (Exception e) {
			fileOperationMessage.setType(FileOperationMessage.ERROR_MESSAGE);
    		fileOperationMessage.setMessage(e.getMessage());
    		fileOperationMessage.emptyFileList();
    		RDCallbackMethodHandler.callRDMethod(ulcPane, errorMethodeName, new Object[] { fileOperationMessage });
		}
		return fileOperationMessage;
	}
	
	/**
	 * download a File directly to the client
	 * If the FileDownloadHandler was instanciated with a parent RD reference and callbackmethods names,<br>
	 * it will be able to communicate with its parent RD<br>
	 * No progress is shown for one File download.<br>
	 * @param final String choosedFileName : the choosed filename to download
	 * @param final String serverFilePath: the serverFilePath where is the file to download 
	 */
	public FileOperationMessage download(final String choosedFileName,final String serverFilePath) {
		
		fileOperationMessage.setType(FileOperationMessage.INFORMATION_MESSAGE);
		fileOperationMessage.setMessage("");
		fileOperationMessage.emptyFileList();
		
		FileChooserConfig fcConfig = new FileChooserConfig();
		fcConfig.setDialogTitle(this.downloadTitle);
		
		fcConfig.setMultiSelectionEnabled(false); // We accept just one file at time
		fcConfig.setApproveButtonText(this.chooseButton);

		fcConfig.setSelectedFile(choosedFileName);
		try {
			ClientContext.storeFile(new IFileStoreHandler() {
				
				public void onFailure(int reason, String description) {
					makeError(reason,description);
				}

				public void prepareFile(OutputStream data) {
					try {
						String pathOnServer = formatServerPath(serverFilePath);
						
						File f = new File(pathOnServer);
						if(!f.exists()){
							fileOperationMessage.setType(FileOperationMessage.ERROR_MESSAGE);
		            		fileOperationMessage.setMessage("The file you try to download doesn't exit.");
		            		fileOperationMessage.emptyFileList();
		            		RDCallbackMethodHandler.callRDMethod(ulcPane, errorMethodeName, new Object[] { fileOperationMessage });
						}else{
							FileInputStream fis = new FileInputStream(f);
							byte b[] = new byte[1024]; 
		            		int c=0;
		            		
		            		while((c= fis.read(b)) != -1){
		            			data.write(b,0,c);
		            		}
		            		fis.close();
							fileOperationMessage.setType(FileOperationMessage.SUCCESS_MESSAGE);
		            		fileOperationMessage.setMessage("The File was successfully downloaded.");
		            		fileOperationMessage.addFile(f);
						}
					} catch (IOException ioe) {
						fileOperationMessage.setType(FileOperationMessage.ERROR_MESSAGE);
	            		fileOperationMessage.setMessage(ioe.getMessage());
	            		fileOperationMessage.emptyFileList();
	            		RDCallbackMethodHandler.callRDMethod(ulcPane, errorMethodeName, new Object[] { fileOperationMessage });
					}
				}

				public void onSuccess(String filePath, String fileName) {
					RDCallbackMethodHandler.callRDMethod(ulcPane, downloadSuccessMethodeName, new Object[] { fileOperationMessage });
				}
			}, fcConfig, ulcPane);
		} catch (Exception e) {
			fileOperationMessage.setType(FileOperationMessage.ERROR_MESSAGE);
    		fileOperationMessage.setMessage(e.getMessage());
    		fileOperationMessage.emptyFileList();
    		RDCallbackMethodHandler.callRDMethod(ulcPane, errorMethodeName, new Object[] { fileOperationMessage });
		}
		return fileOperationMessage;
	}
	
	/**
	 * Methods to be able to download several Files <br>
	 * The download progress will be shown if a progressMethod was provided<br>
	 * A timer is then used to stop the download process 10 times to be able to update the RD parent.<br>
	 * @param files: a List<File> Object. The list of the files to be downloaded.
	 */
	public void downloadFiles(final List<File> files){
		this.preparedFiles = files;
		this.selectDestinationDirectory();
	}
	
	/**
	 * This methods allows the user to choose a directory on his FileSystem<br>
	 * The returned path can be used for further download place.<br>
	 * If the process is successfull, a success method will be called back. 
	 * @return fileOperationMessage. If successfull, it contains the path choosed as String in its Text filed,
	 * the File directory in the File field.<br>
	 * If the Type is FileOperationMessage.ERROR_MESSAGE, the File is null and the text contains an error message.
	 */
	public FileOperationMessage selectDirOnClient(){
		this.fileOperationMessage=new FileOperationMessage();
		this.fileOperationMessage.setType(FileOperationMessage.SUCCESS_MESSAGE);
    	final StringBuilder clientPath = new StringBuilder();
    	ClientContext.setFileTransferMode(ClientContext.SYNCHRONOUS_MODE);
    	final FileChooserConfig fcConfig = new FileChooserConfig();
    	
		fcConfig.setDialogTitle(this.downloadTitle);
		fcConfig.setFileSelectionMode(FileChooserConfig.DIRECTORIES_ONLY);
		fcConfig.setMultiSelectionEnabled(false); // We accept just one directory at time
		fcConfig.setApproveButtonText(this.chooseButton);
		ClientContext.chooseFile(new IFileChooseHandler(){

			public void onFailure(int reason, String description) {
				makeError(reason,description);
			}

			public void onSuccess(String[] arg0, String[] arg1) {
				clientPath.append(arg0[0]);
				fileOperationMessage.setType(FileOperationMessage.SUCCESS_MESSAGE);
				fileOperationMessage.addFile(new File(arg0[0]));
				fileOperationMessage.setMessage(arg0[0]);
				RDCallbackMethodHandler.callRDMethod(ulcPane, downloadSuccessMethodeName, new Object[] { fileOperationMessage });
			}
			
		}, fcConfig, ulcPane);
		return fileOperationMessage;
    }

	/**
	 * Format the serverpath attribute of this object, so that it will contains the right File.separator
	 *
	 */
	private void formatServerPath(){
		if(serverPath!=null && !serverPath.equals("")){
			serverPath= serverPath.replace("\\",java.io.File.separator);
			serverPath= serverPath.replace("/",java.io.File.separator);
			
			if(serverPath.lastIndexOf(java.io.File.separator)!=serverPath.length()-1)
				serverPath = serverPath + java.io.File.separator;
		}
	}
	
	/**
	 * format a path parameter, so that it will contains the right File.separator
	 * @param path: the string to format
	 * @return String: returnedPath
	 */
	private String formatServerPath(String path){
		String returnedPath = path;
		if(path!=null && !path.equals("")){
			returnedPath= returnedPath.replace("\\",java.io.File.separator);
			returnedPath= returnedPath.replace("/",java.io.File.separator);
		}
		return returnedPath;
	}
	
	/**
	 * Set the serverpath entrypoint of this Object
	 * @param serverPath
	 */
	public void setServerPath(String serverPath){ this.serverPath= serverPath;}

	/**
     * Set the calling ULComponent implementing IRichDialogPanel 
     * @param ulcPane : ULComponent implementing IRichDialogPanel 
     */
    public void setUlcPane(T ulcPane)
    {
    	// Check if the given ulcPane implements ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel
        if(ulcPane instanceof ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel)
        	this.ulcPane = ulcPane;
        else this.ulcPane=null;
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
     * Set the callback method name to get the prepared File for a future upload.<br>
     * The callback methods are a way to interact with the process of the referenced Panel
     * that implements the following interface ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel
     * @param String uploadSuccessMethodName
     */
    public void setDownloadSuccessMethodName(String downloadSuccessMethodeName)
    {
        this.downloadSuccessMethodeName = downloadSuccessMethodeName.trim();
    }
    
    /**
     * Set the callback method name to show the progress of the download<br>
     * The callback methods are a way to interact with the process of the referenced Panel
     * that implements the following interface ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel
     * @param String name: the name of the progress method
     */
    public void setProgressMethodName(String name)
    {
        this.progressMethodName = name.trim();
    }
    
    public String getCallbackForOverriding() {
		return callbackForOverriding;
	}

	public void setCallbackForOverriding(String callbackForOverriding) {
		this.callbackForOverriding = callbackForOverriding;
	}

	private void cleanTimer(){
		IActionListener [] ia = timer.getActionListeners();
		for(IActionListener i: ia){
			timer.removeActionListener(i);
		}
	}
    
    private void selectDestinationDirectory() {
    	final FileChooserConfig fcConfig = new FileChooserConfig();

    	fcConfig.setDialogTitle(this.downloadTitle);
    	fcConfig.setFileSelectionMode(FileChooserConfig.DIRECTORIES_ONLY);
    	fcConfig.setMultiSelectionEnabled(false); // We accept just one directory at time
    	fcConfig.setApproveButtonText(this.chooseButton);
    	fcConfig.setDialogType(FileChooserConfig.SAVE_DIALOG);
    	ClientContext.chooseFile(new IFileChooseHandler(){
    		public void onFailure(int reason, String description) {
    			makeError(reason,description);
    		}

    		public void onSuccess(String[] arg0, String[] arg1) {

    			final String path= arg0[0];
    			preparedPath=path;
    			boolean flag = false;
    			if(dGetFilesAtClientSideMethod!=null) {
    				try{
    					Ivy.log().info("Before getting Infos on the directory choosed for download");
    					dGetFilesAtClientSideMethod.invoke(desktop, path);
    					flag =true;
    				}catch(Exception ex) {
    					Ivy.log().info("The method getFilesListUnderPath is not implemented in the ULC exctension.");
    				}
    			}
    			if(!flag) {
    				Ivy.log().info("Download 3");
    				downloadFilesAfterOverridingCheck(preparedFiles);
    			}
    		}

    	}, fcConfig, ulcPane);
    }
    
    private ch.ivyteam.ivy.scripting.objects.List<File> getIfDownloadOverrideFiles(String[] paths) {
    	ch.ivyteam.ivy.scripting.objects.List<File> files = ch.ivyteam.ivy.scripting.objects.List.create(java.io.File.class);
    	if(paths!=null) {
    		for(int i=0; i<paths.length;i++) {
    			String fname = new java.io.File(paths[i]).getName();
    			for(File f: this.preparedFiles) {
    				if(fname.compareToIgnoreCase(f.getName())==0) {
    					Ivy.log().debug("File 2 get "+f);
    					files.add(f);
    					break;
    				}
    			}
    		}
    	}
    	return files;
    }
    
	/**
	 * <font color="red"><b>NOT PUBLIC API<b></font>, Do not call it
	 */
	public void clientDirectoryInfoReturned(String[] paths, boolean hasWriteRight) {
		Ivy.log().info("File list get "+paths);
		if(!hasWriteRight){
			this.makeError(IFileStoreHandler.FAILED, "Caused by: java.security.PrivilegedActionException");
			return;
		}
		ch.ivyteam.ivy.scripting.objects.List<File> files = this.getIfDownloadOverrideFiles(paths);
		if(files.isEmpty()) {
			this.downloadFilesAfterOverridingCheck(this.preparedFiles);
		} else {
			this.fileOperationMessage = new FileOperationMessage();
			this.fileOperationMessage.setFiles(files);
			String message = Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/download/message/overwriteExistingFiles");
			
			this.fileOperationMessage.setMessage(message);
			Ivy.log().debug("Calling {0} \n{1} \nAt that point preparedFiles {2}",this.callbackForOverriding,this.fileOperationMessage,this.preparedFiles);
			RDCallbackMethodHandler.callRDMethod(ulcPane, this.callbackForOverriding, new Object[] { this.fileOperationMessage });
		}
	}
	
	/**
	 * Download Files that were prepared with the possibility to exclude the given list of files.<br>
	 * If this list is null or empty, then all the preparedFiles will be downloaded.
	 * @param files
	 */
	public void downloadPreparedFilesWithPossibilityToExclude(ch.ivyteam.ivy.scripting.objects.List<File> files) {
		Ivy.log().debug("downloadFilesAfterOverridingCheck \n{0} \npreparedFiles: {1}",files,this.preparedFiles);
		if(files==null || files.isEmpty()) {
			this.downloadFilesAfterOverridingCheck(this.preparedFiles);
		}else{
			List<File> fl = new ArrayList<File>();
			for(File f : this.preparedFiles) {
				boolean found = false;
				for(File f1: files) {
					if(f.getName().equalsIgnoreCase(f1.getName())) {
						found=true;
						break;
					}
				}
				if(!found) {
					fl.add(f);
				}
			}
			this.downloadFilesAfterOverridingCheck(fl);
		}
	}
	
	private void downloadFilesAfterOverridingCheck (final List<File> files){
		Ivy.log().debug("downloadFilesAfterOverridingCheck \n{0}",files);
		this.preparedPath = PathUtil.formatPathForDirectoryAllowingSlashesAndBackslashesAtBeginOfPath(this.preparedPath);
		cleanTimer();
		if(files.isEmpty()){
			return;
		}
		timer.addActionListener(new IActionListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 6672475106005492782L;
			int percentDone=0;
			int nb= files.size();
			int fileValue = round(100/nb);
			@Override
			public void actionPerformed(ActionEvent event) {
				try{
					if(!downloadFile(files.get(0), preparedPath)) {
						return;
					}
				}catch(Exception ex){
					return;
				}
				files.remove(0);
				if(!files.isEmpty()){
					percentDone+=fileValue;
					RDCallbackMethodHandler.callRDMethod(ulcPane,progressMethodName, new Object[] { percentDone });
            		timer.restart(); // restart the timer because the file is not completely uploaded
				}
			}
			
		});
		timer.start();
	}
	
	private boolean downloadFile(final java.io.File file, String clientPath) throws Exception{
		clientPath= PathUtil.formatPathForDirectoryAllowingSlashesAndBackslashesAtBeginOfPath(clientPath);
		clientPath= clientPath+file.getName();
		final boolean[] response = {true};
		ClientContext.setFileTransferMode(ClientContext.SYNCHRONOUS_MODE);
		ClientContext.storeFile(new IFileStoreHandler(){
			public void onFailure(int reason, String description)
			{
				Ivy.log().error(description);
				makeError(reason,description);
				response[0]=false;
			}

			public void prepareFile(OutputStream data) {
				String fileOnServerPath= PathUtil.formatPath(file.getPath());
				File f = new File(fileOnServerPath);
				if(!f.exists()){
					this.onFailure(IFileStoreHandler.FAILED, "File not found");
				}
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(f);
					byte b[] = new byte[1024]; 
					int c=0;
					while((c= fis.read(b)) != -1){
						data.write(b,0,c);
					}
					
				} catch (FileNotFoundException e) {
					this.onFailure(IFileStoreHandler.FAILED, "FileNotFoundException File not found");
				} catch (IOException ioe){
					this.onFailure(IFileStoreHandler.FAILED, "IOException : "+ioe.getMessage());
				} finally {
					if(fis !=null) {
						try {
							fis.close();
						} catch (IOException e) {}
					}
				}
			}
			public void onSuccess(String filePath, String fileName){
				if(file!=null && file.getName().trim().length()>0) {
					fileOperationMessage.setType(FileOperationMessage.SUCCESS_MESSAGE);
					fileOperationMessage.setMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/download/message/downloadSuccess").replace("FFILE", "\""+file.getName()+"\""));
					fileOperationMessage.emptyFileList();
					RDCallbackMethodHandler.callRDMethod(ulcPane, downloadSuccessMethodeName, new Object[] { fileOperationMessage });
				}
			}
		},clientPath);
		return response[0];
	}
	
	protected void makeError(int reason, String description) {
		if(reason==IFileStoreHandler.CANCELLED){
			return;
		}
		String msg = "";
		ReturnedMessage message = new ReturnedMessage();
		String add="";
		ClientContext.getSystemProperty("");
		if(description!=null) {
			if(description.equals("Caused by: java.security.PrivilegedActionException"))
			{
				add= " "+Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/download/message/clientSideRightException");
			} else if(description.contains("Caused by: java.security.PrivilegedActionException")) {
				String s = description.substring(description.indexOf("Caused by: java.security.PrivilegedActionException"));
				if(s.contains("\n")){
					s = s.substring(0, s.indexOf("\n"));
				}else if(s.contains("\r")) {
					s = s.substring(0, s.indexOf("\r"));
				}
				s=s.replace("Caused by: java.security.PrivilegedActionException", "");
				if(s.startsWith(": java.")){
					s=s.substring(2);
					if(s.contains(": ")) {
						s=s.substring(s.indexOf(": ")+2);
					}
				}
				add="<br>"+s;
			} else if(description.equals("No read privilege")) {
				add= " "+Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/download/message/clientSideNoReadRightException");
			} else if(description.contains("The directory does not exist,")){
				String n = description.substring(description.indexOf(",")+1).trim();
				add=" "+Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/download/message/clientDirectoryCannotBeFound").replace("DDIR", n);
			}
		}
		msg = Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/download/message/downloadFailed")+add;
		message.setType(FileOperationMessage.ERROR_MESSAGE);
		message.setText(msg);
		RDCallbackMethodHandler.callRDMethod(ulcPane, errorMethodeName, new Object[] { message });
	}

	protected class DownloadGetDirectoryInfoListener {
		ULCXDesktop desktop=null;
		boolean result = false;
		protected DownloadGetDirectoryInfoListener(ULCXDesktop _desktop, final FileDownloadHandler<?> _handler) {
			desktop = _desktop;
			Class<?> c1=null;
			Object myListener;
			try{
				try {
					c1 = Class.forName("ch.xpertline.ulc.server.headless.ULCXDesktop$GetDirectoryInfoListener");
					result = true;
				} catch(Exception ex) {
					Ivy.log().debug("Class ch.xpertline.ulc.server.headless.ULCXDesktop$GetDirectoryInfoListener cannot be found.",ex);
				}
				if(result) {
					myListener = java.lang.reflect.Proxy.newProxyInstance(c1.getClassLoader(), new java.lang.Class[] { c1 }, new java.lang.reflect.InvocationHandler() {

						@Override
						public Object invoke(Object proxy, Method method, Object[] args)
						throws Throwable {
							if(method.getName().equals("directoryInfoReturned")) {
								if(args==null || args.length==0) {
									_handler.clientDirectoryInfoReturned(null,false);
									return null;
								}else{
									Class<?> c2 = Thread.currentThread().getContextClassLoader().loadClass("ch.xpertline.ulc.server.headless.ULCXDesktop$DirectoryInfoReturnedEvent");
									Object arg = args[0];
									Object event = c2.cast(arg);
									Method m = c2.getMethod("getReturnedFilesPaths", new Class<?>[0] );
									String[] paths = (String[]) m.invoke(event, new Object[0]);
									m = c2.getMethod("getWriteRight", new Class<?>[0]);
									boolean b = (Boolean) m.invoke(event, new Object[0]);
									_handler.clientDirectoryInfoReturned(paths,b);
									return paths;
								}
							}
							return null;
						}

					});
					Method add = desktop.getClass().getMethod("addGetDirectoryInfoListener", c1);
					add.invoke(desktop, myListener);
				}
			}
			catch(Exception ex) {
				Ivy.log().error("Error in Proxy "+ex.getMessage(),ex);
			}
		}
	}
    
	/**
	 * <b>NOT PUBLIC API</b>
	 */
	@Override
	public void desktopException(OnDesktopExceptionEvent arg0) {
		Ivy.log().error(arg0.getDesktopExceptionMessage());
		if(arg0.getDesktopExceptionMessage().contains("The directory does not exist")) {
			this.errorInterval = System.nanoTime();
			String[] cuts = arg0.getDesktopExceptionMessage().split("\\.");
			String dirname = "";
			int i = 0;
			for(String s : cuts) {
				if(i>0 && i<cuts.length-1) {
					if(i==1)
						dirname+=s;
					else
						dirname+="."+s;
				}
				i++;
			}
			this.makeError(IFileStoreHandler.FAILED, "The directory does not exist, "+dirname);
		}else if(arg0.getDesktopExceptionMessage().contains("Exception occured while trying to list")) {
			long elapsed =(long) 8E10;
			if(this.errorInterval>0) {
				elapsed =System.nanoTime() - this.errorInterval;
			}
			if(elapsed> (long) 2E9)
				this.makeError(IFileStoreHandler.FAILED, "No read privilege");
		}else {
			this.makeError(IFileStoreHandler.FAILED, "");
		}
	}
	
}
