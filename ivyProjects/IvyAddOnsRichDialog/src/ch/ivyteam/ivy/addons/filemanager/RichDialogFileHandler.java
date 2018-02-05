package ch.ivyteam.ivy.addons.filemanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ch.ivyteam.ivy.environment.Ivy;

import com.ulcjava.base.application.ClientContext;
import com.ulcjava.base.application.tree.TreePath;
import com.ulcjava.base.application.util.IFileLoadHandler;
import com.ulcjava.base.application.util.IFileStoreHandler;

public class RichDialogFileHandler {

	/**
	 * This static method attempts to launch an automatic upload process<br>
	 * The given File on the client will be uploaded to the given String serverPath.<br>
	 * This process should be ASYNCHRONOUS<br>
	 * <b>Important: </b>This method doesn't check if the File already exists on the server<br>
	 * @param _fileToUpload : the file java.io.File from the client to upload on the server.
	 * @param _serverPath : the path on the server where the File has to be downloaded
	 * 
	 */
	public static void upload(final File _fileToUpload, String _serverPath){
		if(_fileToUpload==null || _serverPath==null || _serverPath.trim().equals(""))
		{
			Ivy.log().error("One of the parameter is not valid in the FileHandler static uploadFile method.");
			return;
		}
		//Control about server side file location
		final StringBuilder path= new StringBuilder(FileHandler.formatPath(_serverPath));
		String fileSeparator = ClientContext.getSystemProperty("file.separator");
		if(fileSeparator==null || fileSeparator.trim().length()==0)
		{
			fileSeparator ="/";
		}
		File serverDir = new File(path.toString());
		if((serverDir.exists() && !serverDir.isDirectory()) || !serverDir.exists())
			serverDir.mkdirs();
		if(path.lastIndexOf(File.separator) != path.length() - 1) 
			path.append(java.io.File.separator);
		
		String filePath = _fileToUpload.getPath().contains("\\")?_fileToUpload.getPath().replace("\\", fileSeparator):
			( _fileToUpload.getPath().contains("/")?_fileToUpload.getPath().replace("/", fileSeparator):
				_fileToUpload.getPath());
		
		final String finalFilename= filePath.substring(filePath.lastIndexOf(fileSeparator)+1);
		Ivy.log().info("File uploaded from Client from following path "+filePath);
		ClientContext.setFileTransferMode(ClientContext.ASYNCHRONOUS_MODE);
		ClientContext.loadFile(new IFileLoadHandler(){
			public void onFailure(int reason, String description)
			{
				Ivy.log().error("onFailure Exception "+description);
				return;
			}

			public void onSuccess(InputStream ins[], String filePaths[], String fileNames[])
			{
				try{
					Ivy.log().info("Try to File uploaded from Client To Server at "+path.toString());
					BufferedInputStream preparedFile = new BufferedInputStream(ins[0]);
					path.append(finalFilename);
					File serverFile = new File(path.toString());
					Ivy.log().info("File uploaded from Client To Server at "+path.toString());
					OutputStream server = new BufferedOutputStream(new FileOutputStream(serverFile));
					byte b[] = new byte[1024];
					int intRead; 
					while((intRead= preparedFile.read(b)) != -1){
						server.write(b,0,intRead);
					}
					server.close();
				}
				catch(Exception _ex) { 
					Ivy.log().error("onSuccess Exception "+_ex.getMessage(), _ex);
				}
			}
		}, filePath);// end of ClientContext.loadFile
	}



	/**
	 * This method attempts to download automatically a File from the server,
	 * to the client given clientPath<br>
	 * If the File is an HTML File, it will download automatically all the attached media Files<br>
	 * Those Media Files should be in the same directory like the HTML File and their name should begins<br>
	 * with the HTML-File name plus a point.<br>
	 * @param _FiletoDownload File: the file from the Server to download
	 * @param _clientPath : the client side path where we have to store the file
	 * @return true if the download process was successful, else false.
	 */
	public static boolean download(final File _FiletoDownload, String _clientPath){

		final StringBuilder sb = new StringBuilder();
		
		if(FileHandler.getFileExtension(_FiletoDownload.getName()).equalsIgnoreCase("html")|| FileHandler.getFileExtension(_FiletoDownload.getName()).equalsIgnoreCase("htm")){
			ArrayList <File> files = new ArrayList<File>();
			String fileDirPath=null;
			try{
				fileDirPath = FileHandler.getFileDirectoryPath(_FiletoDownload);
			}catch(Exception _ex){
				//do nothing
			}
			if(fileDirPath!=null){
				files.addAll(FileHandler.getFilesWithPattern("\\.[0-9].*$",FileHandler.getFileNameWithoutExt(_FiletoDownload.getName()), fileDirPath));
			}
			if(files.size()>0){
				download(files,_clientPath);
			}
		}
		_clientPath= FileHandler.formatPathWithEndSeparator(_clientPath, false);
		_clientPath= _clientPath+_FiletoDownload.getName();
		_clientPath=org.apache.commons.lang.StringUtils.replace(_clientPath,"\\", "/");
		
		try {
			ClientContext.storeFile(new IFileStoreHandler(){
				public void onFailure(int reason, String description)
				{
					sb.append(false);
					return;
				}

				public void prepareFile(OutputStream data) {
					String fileOnServerPath= FileHandler.formatPath(_FiletoDownload.getPath());
					File f = new File(fileOnServerPath);
					if(!f.exists()){
						this.onFailure(IFileStoreHandler.FAILED, "File not found");
					}
					try {
						FileInputStream fis = new FileInputStream(f);
						byte b[] = new byte[1024]; 
						int c=0;
						while((c= fis.read(b)) != -1){
							data.write(b,0,c);
						}
						fis.close();
						sb.append(true);
					} catch (FileNotFoundException e) {
						sb.append(false);
						e.printStackTrace();
					} catch (IOException ioe){
						sb.append(false);
					}
				}

				public void onSuccess(String filePath, String fileName){

				}
			},_clientPath);
		} catch (Exception e) {
			sb.append(false);
		}
		if(sb.toString().contains("false"))
			return false;
		else return true;
	}
	
	/**
	 * This method attempts to download automatically a File from the server,
	 * to the client given clientPath<br>
	 * If the File is an HTML File, it will download automatically all the attached media Files<br>
	 * Those Media Files should be in the same directory like the HTML File and their name should begins<br>
	 * with the HTML-File name plus a point.<br>
	 * @param _FiletoDownload File: the file from the Server to download
	 * @param _clientPath : the client side path where we have to store the file
	 * @return true if the download process was successful, else false.
	 */
	public static boolean download(final File _FiletoDownload, String _clientPath, String _clientFileSeparator){

		final StringBuilder sb = new StringBuilder();

		if(_clientFileSeparator !=null &&_clientFileSeparator.trim().length()>0)
		{
			_clientPath = _clientPath.replace("\\", _clientFileSeparator);
			_clientPath = _clientPath.replace("/", _clientFileSeparator);
		}
		if(FileHandler.getFileExtension(_FiletoDownload.getName()).equalsIgnoreCase("html")|| FileHandler.getFileExtension(_FiletoDownload.getName()).equalsIgnoreCase("htm")){
			ArrayList <File> files = new ArrayList<File>();
			String fileDirPath=null;
			try{
				fileDirPath = FileHandler.getFileDirectoryPath(_FiletoDownload);
			}catch(Exception _ex){
				//do nothing
			}
			if(fileDirPath!=null){
				files.addAll(FileHandler.getFilesWithPattern("\\.[0-9].*$",FileHandler.getFileNameWithoutExt(_FiletoDownload.getName()), fileDirPath));
			}
			if(files.size()>0){
				download(files,_clientPath);
			}
		}
		_clientPath= _clientPath+_FiletoDownload.getName();
		try {
			ClientContext.storeFile(new IFileStoreHandler(){
				public void onFailure(int reason, String description)
				{
					sb.append(false);
					return;
				}

				public void prepareFile(OutputStream data) {
					String fileOnServerPath= FileHandler.formatPath(_FiletoDownload.getPath());
					File f = new File(fileOnServerPath);
					if(!f.exists()){
						this.onFailure(IFileStoreHandler.FAILED, "File not found");
					}
					try {
						FileInputStream fis = new FileInputStream(f);
						byte b[] = new byte[1024]; 
						int c=0;
						while((c= fis.read(b)) != -1){
							data.write(b,0,c);
						}
						fis.close();
						sb.append(true);
					} catch (FileNotFoundException e) {
						sb.append(false);
						e.printStackTrace();
					} catch (IOException ioe){
						sb.append(false);
					}
				}

				public void onSuccess(String filePath, String fileName){

				}
			},_clientPath);
		} catch (Exception e) {
			sb.append(false);
		}
		if(sb.toString().contains("false"))
			return false;
		else return true;
	}

	/**
	 * This method attempts to download automatically a list of Files from the server,<br>
	 * to the client given clientPath
	 * @param _FilestoDownload Lsit<File>: the files from the Server to download
	 * @param _clientPath : the client side path where we have to store the files
	 * @return true if the download process was successful, else false.
	 */
	public static boolean download(final java.util.List<File> _FilestoDownload, String _clientPath){
		final StringBuilder sb = new StringBuilder();
		String fileSeparator = getClientFileSeparator();
		if(fileSeparator!=null){
			_clientPath = _clientPath.replaceAll("\\\\", fileSeparator);
			_clientPath = _clientPath.replaceAll("/", fileSeparator);
			if(_clientPath.lastIndexOf(fileSeparator) != _clientPath.length() - 1){
				_clientPath= _clientPath+fileSeparator;
			}
		}
		for(final File file: _FilestoDownload){
			String path = _clientPath+file.getName();

			//File clientFile = new File(path.toString());
			try {
				ClientContext.storeFile(new IFileStoreHandler(){
					public void onFailure(int reason, String description)
					{
						sb.append(false);
						return;
					}
					public void prepareFile(OutputStream data) {
						String fileOnServerPath= FileHandler.formatPath(file.getPath());
						File f = new File(fileOnServerPath);
						if(!f.exists()){
							this.onFailure(IFileStoreHandler.FAILED, "File not found");
						}
						try {
							FileInputStream fis = new FileInputStream(f);
							byte b[] = new byte[1024]; 
							int c=0;
							while((c= fis.read(b)) != -1){
								data.write(b,0,c);
							}
							fis.close();
							sb.append(true);
						} catch (FileNotFoundException e) {
							sb.append(false);
							e.printStackTrace();
						} catch (IOException ioe){
							sb.append(false);
						}
					}

					public void onSuccess(String filePath, String fileName){

					}
				},path);
			} catch (Exception e) {
				sb.append(false);
			}
		}
		if(sb.toString().contains("false"))
			return false;
		else return true;
	}

	/**
	 * <b>ONLY FOR TEST ON WINDOWS CLIENTS</b><br>
	 * This method attempts to download automatically a File from the server,<br>
	 * to the client "c:/temp/" path. <br>
	 * @param _file the File to download
	 * @return true if the download process was successfull, else false.
	 */
	public static boolean download (final File _file){
		final StringBuilder sb = new StringBuilder();
		try {
			final File file= new File("c:/temp/"+_file.getName());
			ClientContext.storeFile(new IFileStoreHandler() {

				public void onFailure(int reason, String description) {
					sb.append(false);
					System.out.print(description);
				}

				public void prepareFile(OutputStream data) {

					byte b[] = new byte[1024]; 
					FileInputStream fis;
					try {
						fis = new FileInputStream(_file);
						int c=0;
						while((c= fis.read(b)) != -1){
							data.write(b,0,c);
						}
						fis.close();
						sb.append(true);
					} catch (FileNotFoundException e) {
						sb.append(false);
						System.out.print(e.getMessage());
						e.printStackTrace();
					}catch (IOException e) {
						sb.append(false);
						System.out.print(e.getMessage());
						e.printStackTrace();
					}
				}
				public void onSuccess(String s, String s2){
					sb.append(false);
					System.out.print(s+s2);
				}
			}, file.getPath());
		} catch (Exception e) {
			sb.append(false);
			System.out.print(e.getMessage());
		}
		if(sb.toString().contains("false"))
			return false;
		else return true;
	}
	
	/**
	 * return the TreePath of FolderOnServer Objects corresponding to a Folder to select in the Tree view.<br>
	 * If the given path to select is outside the Tree rootpath, then the method returns null.<br>
	 * If the folder corresponding to the given path to select doesn't exits but would be under the Tree root path,<br>
	 * then it will be created.
	 * @param rootPath : the path from the root of the Tree, String
	 * @param pathToSelect : The path of the Folder to select into the Tree view.
	 * @return the ULC TreePath leading to the Folder to select, or null if it is outside of the root of the Tree.
	 */
	public static TreePath getTreePath(String rootPath, String pathToSelect){
		TreePath treepath=null;
		if(rootPath== null || pathToSelect==null || pathToSelect.trim().equals(""))
			return null;
		//IF the root path is not the root of the server
		try{
			if(!rootPath.trim().equals("")){
				ArrayList<FolderOnServer> foldersPaths= new ArrayList<FolderOnServer>();

				String rPath= FileHandler.formatPathWithEndSeparator(rootPath, true);
				String sPath = FileHandler.formatPathWithEndSeparator(pathToSelect, false);
				StringBuilder sBuilder = new StringBuilder(sPath);
				//if the begin of the path to select is not the rootpath, then it is outside
				if(sBuilder.indexOf(rPath)!=0)
					return null;
				// we ensure that the path to select will be created if it doesn't exist.
				sPath = FileHandler.formatPathWithEndSeparator(pathToSelect, true);

				int r = rPath.length();
				sPath = sBuilder.substring(r, sPath.length()-1);
				String regex ="\\\\";
				if(File.separator.equals("/"))
					regex =File.separator;

				String folders [] = sPath.split(regex);

				FolderOnServer root = new FolderOnServer();
				root.setName(FileHandler.getFolderNameFromPath(rPath));
				root.setPath(rPath);
				foldersPaths.add(root);
				for(int i=0; i<folders.length;i++){
					FolderOnServer fos = new FolderOnServer();
					fos.setName(folders[i]);
					fos.setPath(FileHandler.formatPathWithEndSeparator(foldersPaths.get(i).getPath(),false)+folders[i]+File.separator);
					foldersPaths.add(fos);
				}
				FolderOnServer treeFolder[] = new FolderOnServer[foldersPaths.size()];
				for(int i=0; i<foldersPaths.size(); i++){
					treeFolder[i]=foldersPaths.get(i);

				}
				treepath= new TreePath(treeFolder);
			}
		}catch(Exception e){
			Ivy.log().debug("Exception in getTreePath: "+e.getMessage());
			return null;
		}
		return treepath;
	}

	/**
	 * retrieves the clientContext property Names
	 * @return String[]: the Array of the clientContext property Names
	 */
	public static String[] getSystemPropertyNames(){
		return ClientContext.getSystemPropertyNames();
	}
	
	/**
	 *Get the User java parameters names from the clientContext Object
	 * @return array of String, each String is a parameter name
	 */
	public static String[] getUserParametersNames(){
		return ClientContext.getUserParameterNames();
	}

	/**
	 * 
	 * @param _path
	 * @return the path as String with the client side File separator
	 */
	public static String formatPathWithClientSeparator(String _path){
		String clientPath = _path;
		String fileSeparator = getClientFileSeparator();
		if(fileSeparator!=null && _path != null && !_path.trim().equalsIgnoreCase("")){
			clientPath = org.apache.commons.lang.StringUtils.replace(clientPath,"\\", File.separator);
			clientPath = org.apache.commons.lang.StringUtils.replace(clientPath,"/", File.separator);
			if(clientPath.lastIndexOf(fileSeparator) != clientPath.length() - 1) 
				clientPath= clientPath+fileSeparator;
		}

		return clientPath;
	}

	/**
	 * returns the client File separator as a String.<br>
	 * @return String: the client File separator. If this property is not available returns null.
	 */
	public static String getClientFileSeparator(){
		String retour="/";

		String[] sp = ClientContext.getSystemPropertyNames();
		for(String s:sp){
			
			if(s.equals("file.separator")){
				retour= ClientContext.getSystemProperty("file.separator");
				break;
			}
		}
		return retour;

	}

}
