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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Adler32;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.scripting.objects.Time;
import ch.ivyteam.ivy.scripting.objects.Tree;

/**
 * @author Emmanuel Comba <br>
 * The abstract FileHandler class contains static methods that ease the File management on the server.<br>
 * Some of those methods are used by the FileUploadHandler and FileDownloadHandler java classes from the same package.<br>
 * 
 * It relies on the ch.xpertline.ria.util.file.ReturnedMessage, ch.xpertline.ria.util.file.FolderOnServer and ch.xpertline.ria.util.file.DocumentOnServer dataclasses for returning the result of most of the methods.
 * 
 * This Class has the following static fields:
 * <ul>
 * <li>public static final int SUCCESS_MESSAGE = 1;
 * <li>public static final int ERROR_MESSAGE = 2;
 * <li>public static final int INFORMATION_MESSAGE = 3;
 *</ul>
 * They are used to set the returnedMessage type.

 */

public abstract class FileHandler
{
	/** */
	public static final int SUCCESS_MESSAGE = 1;
	/** */
	public static final int ERROR_MESSAGE = 2;
	/** */
	public static final int INFORMATION_MESSAGE = 3;	

	/**
	 * Formats a given path in a path with the right system File.separator characters<br>
	 * It doesn't check if there is a File.separator at the end of the path.
	 * @param _path
	 * @return formatted path with the system File.separator
	 */
	public static String formatPath(String _path)
	{
		if(_path != null && !_path.equals(""))
		{
			//path = path.replaceAll("\\\\", File.separator);
			_path = org.apache.commons.lang.StringUtils.replace(_path,"\\", File.separator);
			//path = path.replaceAll("/", File.separator);
			_path = org.apache.commons.lang.StringUtils.replace(_path,"/", File.separator);
		}
		return _path;
	}

	/**
	 * Formats a given path in a path with the right system File.separator characters<br>
	 * It checks if there is a File.separator at the end of the path. If not it adds one.<br>
	 * It checks if the directory exists, if not it makes it.
	 * @param _path
	 * @return formatted path with the system File.separator
	 */
	public static String formatPathWithEndSeparator(String _path)
	{
		if(_path != null && !_path.equals(""))
		{
//			path = path.replaceAll("\\\\", File.separator);
			_path = org.apache.commons.lang.StringUtils.replace(_path,"\\", File.separator);
			//path = path.replaceAll("/", File.separator);
			_path = org.apache.commons.lang.StringUtils.replace(_path,"/", File.separator);
			File serverDir = new File(_path);
			if((serverDir.exists() && !serverDir.isDirectory()) || !serverDir.exists())
				serverDir.mkdirs();
			if(_path.lastIndexOf(File.separator) != _path.length() - 1) 
				_path=_path+java.io.File.separator;
		}
		return _path;
	}

	/**
	 * Formats a given path in a path with the right system File.separator characters<br>
	 * It checks if there is a File.separator at the end of the path. If not it adds one.<br>
	 * If the boolean argument is true, it checks if the directory exists and creates it if it doesn't exist.
	 * @param _path the path to format with the right end file separator
	 * @param _createDirIfNotExits boolean telling if has to check for the directory existence
	 * @return formatted path with the system File.separator
	 */
	public static String formatPathWithEndSeparator(String _path, boolean _createDirIfNotExits)
	{
		if(_path != null && !_path.equals(""))
		{

			_path = org.apache.commons.lang.StringUtils.replace(_path,"\\", File.separator);
			_path = org.apache.commons.lang.StringUtils.replace(_path,"/", File.separator);
			if(_createDirIfNotExits){
				File serverDir = new File(_path);
				if((serverDir.exists() && !serverDir.isDirectory()) || !serverDir.exists())
					serverDir.mkdirs();
			}
			if(_path.lastIndexOf(File.separator) != _path.length() - 1) 
			{
				_path=_path+java.io.File.separator;
			}
		}
		return _path;
	}

	/**
	 * Deletes the File pointed by the given filepath
	 * @param _filepath The path of the File to delete.
	 * @return ReturnedMessage: the returned message dataclass Object with message Type(SUCCESS_MESSAGE, ERROR_MESSAGE),<br>
	 * message text and a reference to the deleted File. <br>
	 * If returnedMessage.type==SUCCESS_MESSAGE then the file was deleted. <br>
	 * The returnedMessage contains the reference to the deleted file in its file Field if it was not deleted<br>
	 * <b>Important :</b> in the case of an error, the file Field of the returnedMessage File will be null,<br>
	 * so check this File in your Process before using it. In this case returnedMessage.type==ERROR_MESSAGE.
	 */
	public static ReturnedMessage deleteFile(String _filepath)
	{
		String file = formatPath(_filepath);
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		File fileToDelete = new File(file);
		if(!fileToDelete.exists())
		{
			message.setType(Integer.valueOf(ERROR_MESSAGE));
			message.setText("The File you want to delete doesn't exit.\n Wrong path : "+_filepath);
			message.setFile(fileToDelete);
			return message;
		}
		if(!fileToDelete.isFile())
		{
			message.setType(Integer.valueOf(ERROR_MESSAGE));
			message.setText("The File you want to delete is not a file.\n Wrong path : "+_filepath);
			message.setFile(fileToDelete);
			return message;
		}
		if(fileToDelete.delete())
		{
			message.setType(Integer.valueOf(SUCCESS_MESSAGE));
			message.setText("The following file has been deleted : "+_filepath);
			message.setFile(null);
			return message;
		} else
		{
			message.setType(Integer.valueOf(ERROR_MESSAGE));
			message.setText("There was an error while deleting the following file:\n Wrong path : "+_filepath);
			message.setFile(fileToDelete);
			return message;
		}
	}

	/**
	 * Deletes the files contained into the given List
	 * @param _filesList List of java.io.File to delete.
	 * @return ReturnedMessage: the returned message dataclass Object with message Type(SUCCESS_MESSAGE, ERROR_MESSAGE),<br>
	 * and message text. <br>
	 * If returnedMessage.type==SUCCESS_MESSAGE then all the files were deleted. <br>
	 * The File field of the ReturnedMessage is always null<br>
	 * the file list of the returnedMessage contains the deleted Files
	 */
	public static ReturnedMessage deleteFiles(java.util.List <File> _filesList)
	{
		ReturnedMessage message = new ReturnedMessage();
		message.setFile(null);
		message.setFiles(List.create(java.io.File.class));
		message.setType(SUCCESS_MESSAGE);
		int nb=_filesList.size();
		ArrayList <String> fileNames = new ArrayList<String>();
		for(File file: _filesList){
			if(!file.isFile())
			{
				fileNames.add("NOT A FILE "+ file.toString());
			}
			else if(!file.delete())
			{
				fileNames.add("NOT DELETED "+ file.toString());
			}
			else
			{
				message.getFiles().add(file);
			}
		}
		String text= new String();
		if(fileNames.size()!=0){
			message.setType(ERROR_MESSAGE);
			for(String s: fileNames)
				text+=s+";";     	
			message.setText(text);
			nb=nb-fileNames.size();
		}
		String finalMessage= new String();
		finalMessage+=nb+" files deleted.";
		if(fileNames.size()!=0){
			finalMessage+=" " + fileNames.size()+" files couldn't be deleted. "+ text;
		}
		message.setText(finalMessage);
		return message;

	}

	/**
	 * Deletes the directory pointed by the given filepath
	 * @param _dirpath : The path of the directory to delete.
	 * @return ReturnedMessage: the returned message dataclass Object with message Type(SUCCESS_MESSAGE, ERROR_MESSAGE),<br>
	 * message text and a reference to the deleted directory. <br>
	 * If returnedMessage.type==SUCCESS_MESSAGE then the directory was deleted. <br>
	 * The returnedMessage contains the reference to the deleted directory in its file Field<br>
	 * <b>Important :</b> in the case of an error, the file Field of the returnedMessage directory will be null,<br>
	 * so check this File in your Process before using it. In this case returnedMessage.type==ERROR_MESSAGE.
	 */
	public static ReturnedMessage deleteDirectory(String _dirpath)
	{
		String dir = formatPath(_dirpath);
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		File dirToDelete = new File(dir);

		if(!dirToDelete.isDirectory())
		{
			message.setType(Integer.valueOf(ERROR_MESSAGE));
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/directoryToDeleteNotVald"));
			message.setFile(null);
			return message;
		}
		if(dirToDelete.list().length > 0)
		{
			message.setType(Integer.valueOf(ERROR_MESSAGE));
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/cannotDeleteMoreThanOneDirectory"));
			message.setFile(null);
			return message;
		}
		if(dirToDelete.delete())
		{
			message.setType(Integer.valueOf(SUCCESS_MESSAGE));
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/deleteFolderSuccess"));
			message.setFile(dirToDelete);
			return message;
		} 
		message.setType(Integer.valueOf(ERROR_MESSAGE));
		message.setFile(null);
		message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/ErrorDeletingFolder"));
		return message;
	}

	/**
	 * Try to delete the given directory and all the files and directories that are under this Directory.<br>
	 * 
	 * @param _dirPath : the path of the directory to delete
	 */
	public static void deleteAllUnderDirectory(String _dirPath){
		if(_dirPath != null && !_dirPath.trim().equalsIgnoreCase("")){
			try
			{
				String s = _dirPath.replace("/", java.io.File.separator);
				s= s.replace("\\", java.io.File.separator);


				java.io.File f = new java.io.File(s);
				if(f.isDirectory() && f.listFiles().length==0)
				{// the path points to a directory that is empty
					f.delete();
				}
				else if(!f.isDirectory())
				{// the path points to a File
					f.delete();
				}
				else if(f.isDirectory())
				{// the path points to a dir that is not empty
					java.io.File[] files = f.listFiles();
					for(java.io.File ff:files)
						deleteAll(ff);
					deleteAllUnderDirectory(s);
				}
			}
			catch(SecurityException ex)
			{
				Ivy.log().error("Security Exception in ch.ivyteam.ivy.addons.filemanager.FileHandler#deleteAllUnderDirectory "+ex.getMessage());
			}catch(Throwable t){
				Ivy.log().error("Throwable in ch.ivyteam.ivy.addons.filemanager.FileHandler#deleteAllUnderDirectory "+t.getMessage());
			}
		}
	}

	/**
	 * For private use only.<br>
	 * Recursive method to delete all the files and directories under a given java.io.File.<br>
	 * The given File will be also deleted.
	 * @param _file : the java.io.File to delete
	 */
	private static void deleteAll(java.io.File _file){
		try{
			if(_file!=null){
				if(_file.isDirectory() && _file.listFiles().length==0)
				{// f is a directory and is empty => delete it
					_file.delete();
				}
				else if(!_file.isDirectory())
				{// f is not a directory => delete the file
					_file.delete();
				}
				else
				{// f is a directory and not empty => call recursively this method for each of his children
					java.io.File[] files = _file.listFiles();
					for(java.io.File ff:files)
						deleteAll(ff);
				}
			}
		}
		catch(SecurityException e)
		{
			Ivy.log().error("Security Exception in ch.ivyteam.ivy.addons.filemanager.FileHandler#deleteAll "+e.getMessage());
		}catch(Throwable t){
			Ivy.log().error("Throwable in ch.ivyteam.ivy.addons.filemanager.FileHandler#deleteAll "+t.getMessage());
		}
	}

	/**
	 * This method attempts to create the Directory pointed by the given dirpath
	 * @param _dirpath : The path of the directory to create.
	 * @return ReturnedMessage: the returned message dataclass Object with message Type(SUCCESS_MESSAGE, ERROR_MESSAGE),<br>
	 * message text and a reference to the created directory java.io.File Object. <br><br>
	 * <b>Important :</b> in the case of an error, the returned File can be null, so check this File in your Process before using it.
	 */
	public static ReturnedMessage makeDirectory(String _dirpath)
	{
		String dir = formatPath(_dirpath);
		ReturnedMessage message = new ReturnedMessage();
		message.setFiles(List.create(java.io.File.class));
		File dirToCreate = new File(dir);
		if(dirToCreate.isDirectory())
		{
			message.setType(ERROR_MESSAGE);
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/directoryNotCreatedBecauseExists")+"\n "+_dirpath);
			message.setFile(null);
			return message;
		}
		if(dirToCreate.mkdirs())
		{
			message.setType(SUCCESS_MESSAGE);
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/information/FolderCreationSuccess")+"\n "+_dirpath);
			message.setFile(dirToCreate);
			return message;
		} else
		{
			message.setType(ERROR_MESSAGE);
			message.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/generalDirCreationError")+"\n "+_dirpath);
			message.setFile(null);
			return message;
		}
	}



	/**
	 * This Method allows creating a new version of a File with naming convention.<br>
	 * The naming convention is that the files should end with "###" plus a number.<br>
	 * The number is the version's number.
	 * @param _file
	 * @return the created java.io.File
	 */
	public static File makeNewVersion(File _file){
		Ivy.log().debug(_file.getPath());
		int greaterVersion = getGreaterVersionNumber(_file);
		String fileName= getFileNameWithoutExt(_file.getName());
		
		String filedir= null;
		try{
			filedir= formatPathWithEndSeparator(getFileDirectoryPath(_file));
		}catch(Exception _ex) {
			// do nothing
		}
		
		if (filedir == null) {
			return null;
		}
		
		int nbPlace = fileName.lastIndexOf("###");
		File newVersion=null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try{
			if(greaterVersion==-1){
				newVersion = new File(getFileNameWithoutExt(_file.getPath())+"###1."+getFileExtension(_file.getName()));
					if(newVersion.createNewFile()){
						fis = new FileInputStream(_file);
						fos = new FileOutputStream(newVersion);
						byte b[] = new byte[1024];
						int c=0;
						while((c= fis.read(b)) != -1){
							fos.write(b,0,c);
						}
					}else
						newVersion=null;
				
			} else if(greaterVersion==0){
				newVersion = new File(getFileNameWithoutExt(_file.getPath())+"1."+getFileExtension(_file.getName()));
				
					if(newVersion.createNewFile()){
						fis = new FileInputStream(_file);
						fos = new FileOutputStream(newVersion);
						byte b[] = new byte[1024];
						int c=0;
						while((c= fis.read(b)) != -1){
							fos.write(b,0,c);
						}
					}else
						newVersion=null;
			} else{
				if(nbPlace==-1)
					nbPlace=fileName.length();
				int ver= greaterVersion+1;
				String version= filedir+getFileNameWithoutExt(_file.getName()).substring(0, nbPlace)+"###"+ver+"."+getFileExtension(_file.getName());
				newVersion = new File(version);
				
					if(newVersion.createNewFile()){
						fis = new FileInputStream(_file);
						fos = new FileOutputStream(newVersion);
						byte b[] = new byte[1024];
						int c=0;
						while((c= fis.read(b)) != -1){
							fos.write(b,0,c);
						}
					}else
						newVersion=null;
			}
		} catch (FileNotFoundException _ex) {
			Ivy.log().error(_ex.getMessage(),_ex);
		} catch (IOException _ex) {
			Ivy.log().error(_ex.getMessage(),_ex);
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					
				}
			}
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					
				}
			}
		}
		return newVersion;
	}

	/**
	 * returns the content of a File as String
	 * @param _file : the File to be read
	 * @return the String Content of the File
	 */
	public static String getFileAsString(java.io.File _file){
		String fileAsString ="";
		if(!_file.exists()){
			Ivy.log().error("Error in ch.ivyteam.ivy.addons.filemanager.FileHandler getFileAsString(). The File "+_file.getPath()+" doesn't exist.");
			return fileAsString;
		}
		try{
			StringBuffer fileData = new StringBuffer();
			BufferedReader reader = new BufferedReader(
					new FileReader(_file));
			char[] buf = new char[1024];
			int numRead=0;
			while((numRead=reader.read(buf)) != -1){
				fileData.append(buf, 0, numRead);
			}
			reader.close();
			fileAsString =fileData.toString();
		}catch (Exception e){
			Ivy.log().error("Error in ch.ivyteam.ivy.addons.filemanager.FileHandler getFileAsString(). "+e.getMessage());
		}


		return fileAsString;
	}

	/**
	 * For internal use only
	 * @param _file
	 * @return the greater file version number
	 */
	private static int getGreaterVersionNumber(File _file){
		int greaterVer =-1;
		String fileName = getFileNameWithoutExt(_file.getName());
		int _place = fileName.lastIndexOf("###");
		if(_place==-1)
			_place=fileName.length();

		fileName=fileName.substring(0, _place);
		Ivy.log().debug(fileName + " from getGreaterVersionNumber");
		String pat ="###[0-9].*$";
		String fileDir=null;
		try{
			fileDir=getFileDirectoryPath(_file);
		}catch(Exception _ex){
			//do nothing here
		}
		ArrayList<File> files = new ArrayList<File> ();
		if(fileDir!=null){
			files = getFilesWithPattern(pat, fileName,fileDir );
		}

		if(files.size()>0){
			for(File f: files){
				String fName = getFileNameWithoutExt(f.getName());
				String version = fName.substring(_place+3,fName.length());
				Ivy.log().debug(version);
				Pattern pattern = Pattern.compile("[0-9].*$");
				Matcher m = pattern.matcher(version);
				if(m.matches()){
					int ver =Integer.valueOf(version);
					if(ver > greaterVer)
						greaterVer= ver;
				}
			}
		}
		Ivy.log().debug("Greater version returned: "+greaterVer);
		return greaterVer;
	}

	/**
	 * Returns all the File from a directory whose name is composed by the String p and the given String pattern pat.<br>
	 * It is very useful to find the images that are included in an HTML file.<br>
	 * For example, if a HTML file named test.html contains test.001.jpeg and test.002.jpeg,<br>
	 * it is easy to list those images with getFilesWithPattern("\\.[0-9].*$", "test", "")<br>
	 * Of course the name of the images included in the file must follow the given pattern.
	 * @param _pat : the String pattern used to search the matching Files
	 * @param _p : the basic name of the file without extension
	 * @param _serverPath : the path where the file is stored
	 * @return an ArrayList of the File whose name match the pattern p+pat
	 */
	public static ArrayList<File> getFilesWithPattern(String _pat, String _p, String _serverPath){
		File files[] = getFiles(_serverPath);
		ArrayList<File> filesMatch = new ArrayList<File>();

		//Pattern pattern = Pattern.compile(p+"\\.[0-9].*$");
		Pattern pattern = Pattern.compile(_p+_pat);
		for(File f: files){
			Matcher m = pattern.matcher(f.getName());
			if(m.matches()){
				filesMatch.add(f);
			}
		}
		return filesMatch;
	}

	/**
	 * Makes a copy of the given File. The new File has the given parameter name<br>
	 * If the boolean control parameter is set to true, the new creation will failed if the new File already exists.
	 * @param _fileToCopy File, the file to be copied
	 * @param _newName  String the name of the copy
	 * @param _controlIfExists  boolean, if true it will control if the new File already exists
	 * @return returnedMessage, the ReturnedMessage Object. If the Type is FileHandler.SUCCESS_MESSAGE, then all went fine.
	 */
	public static ReturnedMessage makeNewVersion(File _fileToCopy, String _newName, boolean _controlIfExists){
		ReturnedMessage returnedMessage = new ReturnedMessage();
		returnedMessage.setFiles(List.create(java.io.File.class));
		if(_fileToCopy==null || _newName==null)
		{
			returnedMessage.setText("One of the parameter is null in makeNewVersion");
			returnedMessage.setType(ERROR_MESSAGE);
			returnedMessage.setFile(null);
			return returnedMessage;
		}
		String ext= getFileExtension(_fileToCopy.getName());
		if(!ext.equalsIgnoreCase(getFileExtension(_newName)))
		{
			_newName=_newName+"."+ext;
		}
		String fileDir= null;
		try{
			fileDir= getFileDirectoryPath(_fileToCopy);
		}catch(Exception _ex){
			// do nothing here
		}
		if(fileDir==null)
		{
			returnedMessage.setText("Exception in getFileDirectoryPath for file");
			returnedMessage.setType(ERROR_MESSAGE);
			returnedMessage.setFile(null);
			return returnedMessage;
		}
		
		File newFile=new File(formatPathWithEndSeparator(fileDir)+ _newName);
		
		if(_controlIfExists && newFile.exists())
		{
			returnedMessage.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/information/fileAlreadyExists"));
			returnedMessage.setType(ERROR_MESSAGE);
			returnedMessage.setFile(newFile);
			return returnedMessage;
		}
		else
		{
			if(newFile.exists()){
				if(!newFile.delete()){
					returnedMessage.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/overwriteError"));
					returnedMessage.setType(ERROR_MESSAGE);
					returnedMessage.setFile(newFile);
					return returnedMessage;
				}
			}
			try {
				if(newFile.createNewFile())
				{
					FileInputStream fis = new FileInputStream(_fileToCopy);
					FileOutputStream fos = new FileOutputStream(newFile);
					byte b[] = new byte[1024];
					int c=0;
					while((c= fis.read(b)) != -1)
					{
						fos.write(b,0,c);
					}
					fis.close();
					fos.close();
				}
				else
				{
					returnedMessage.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/fileCreationError"));
					returnedMessage.setType(ERROR_MESSAGE);
					returnedMessage.setFile(newFile);
					return returnedMessage;
				}

			} catch (IOException e) 
			{
				returnedMessage.setText("IOException: "+e.getMessage());
				returnedMessage.setType(ERROR_MESSAGE);
				returnedMessage.setFile(newFile);
				return returnedMessage;
			}
		}
		returnedMessage.setText(Ivy.cms().co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/information/copieSuccess"));
		returnedMessage.setType(SUCCESS_MESSAGE);
		returnedMessage.setFile(newFile);
		return returnedMessage;
	}

	/**
	 * Moves a File and deletes the original File
	 * @param _fileToMove : The File to move
	 * @param _newPath : the new path where to copy the File
	 * @param _controlIfExists : if true will not overwrite the existing File
	 * @return returnedMessage
	 */
	public static ReturnedMessage moveFile(File _fileToMove, String _newPath, boolean _controlIfExists){
		ReturnedMessage returnedMessage = new ReturnedMessage();
		returnedMessage.setFiles(List.create(java.io.File.class));
		File movedFile=new File(formatPathWithEndSeparator(_newPath)+ _fileToMove.getName());
		if(_controlIfExists && movedFile.exists()){
			returnedMessage.setText("The File you want to move already exists in the destination location, do you really want to overwrite it?");
			returnedMessage.setType(INFORMATION_MESSAGE);
			returnedMessage.setFile(movedFile);
			return returnedMessage;
		}else{
			if(movedFile.exists())
				if(!movedFile.delete()){
					returnedMessage.setText("The File could'nt be overwritten. The operation has failed.");
					returnedMessage.setType(ERROR_MESSAGE);
					returnedMessage.setFile(movedFile);
					return returnedMessage;
				}
			try {
				if(movedFile.createNewFile()){
					FileInputStream fis = new FileInputStream(_fileToMove);
					FileOutputStream fos = new FileOutputStream(movedFile);
					byte b[] = new byte[1024];
					int c=0;
					while((c= fis.read(b)) != -1){
						fos.write(b,0,c);
					}
					fis.close();
					fos.close();
					_fileToMove.delete();
				}else{
					returnedMessage.setText("Error while writing the moved File. Please try later again.");
					returnedMessage.setType(ERROR_MESSAGE);
					returnedMessage.setFile(movedFile);
					return returnedMessage;
				}

			} catch (IOException e) {
				returnedMessage.setText("IOException: "+e.getMessage());
				returnedMessage.setType(ERROR_MESSAGE);
				returnedMessage.setFile(movedFile);
				return returnedMessage;
			}
		}
		returnedMessage.setText("Move successfull.");
		returnedMessage.setType(SUCCESS_MESSAGE);
		returnedMessage.setFile(movedFile);
		return returnedMessage;
	}

	/**
	 * Paste a copied File to a destination folder.
	 * @param fileToPaste : java.io.File the File to copy
	 * @param newPath : String the destination folder's path
	 * @param controlIfExists : boolean <br>if true, the method doesn't overwrite the File if it already exits and returns
	 * a returnedMessage with returnedMessage.Type=INFORMATION_MESSAGE.<br>
	 * If false, if there is a file with the same name in the destination folder, this File will be overwriten.
	 * @return returnedMessage: ReturnedMessage indicating the result of the operation
	 */
	public static ReturnedMessage pasteFile(File fileToPaste, String newPath, boolean controlIfExists){
		ReturnedMessage returnedMessage = new ReturnedMessage();
		returnedMessage.setFiles(List.create(java.io.File.class));
		File movedFile=new File(formatPathWithEndSeparator(newPath)+ fileToPaste.getName());
		if(controlIfExists && movedFile.exists()){
			returnedMessage.setText("The File you want to paste already exists in the destination folder, do you really want to overwrite it?");
			returnedMessage.setType(INFORMATION_MESSAGE);
			returnedMessage.setFile(movedFile);
			return returnedMessage;
		}else{
			if(movedFile.exists())
				if(!movedFile.delete()){
					returnedMessage.setText("The File could'nt be overwritten. The operation has failed.");
					returnedMessage.setType(ERROR_MESSAGE);
					returnedMessage.setFile(movedFile);
					return returnedMessage;
				}
			try {
				if(movedFile.createNewFile()){
					FileInputStream fis = new FileInputStream(fileToPaste);
					FileOutputStream fos = new FileOutputStream(movedFile);
					byte b[] = new byte[1024];
					int c=0;
					while((c= fis.read(b)) != -1){
						fos.write(b,0,c);
					}
					fis.close();
					fos.close();
				}else{
					returnedMessage.setText("Error while creating the new File. Please try later again.");
					returnedMessage.setType(ERROR_MESSAGE);
					returnedMessage.setFile(movedFile);
					return returnedMessage;
				}

			} catch (IOException e) {
				returnedMessage.setText("IOException: "+e.getMessage());
				returnedMessage.setType(ERROR_MESSAGE);
				returnedMessage.setFile(movedFile);
				return returnedMessage;
			}
		}
		returnedMessage.setText("Move successfull.");
		returnedMessage.setType(SUCCESS_MESSAGE);
		returnedMessage.setFile(movedFile);
		return returnedMessage;
	}

	/**
	 * Paste a list of copied Files to a destination folder.
	 * If the files exists, the copied Files will have a name base on FileName_Copyx
	 * @param _fileToPaste : java.io.File the File to copy
	 * @param _newPath : String the destination folder's path
	 * @return returnedMessage: ReturnedMessage indicating the result of the operation,<br>
	 * contains the list of successful new created files.
	 */
	public static ReturnedMessage pasteCopiedFile(List<java.io.File> _filesToPaste, String _newPath){
		ReturnedMessage returnedMessage = new ReturnedMessage();
		returnedMessage.setFiles(List.create(java.io.File.class));
		for(java.io.File file: _filesToPaste)
		{
			File movedFile=new File(formatPathWithEndSeparator(_newPath)+ file.getName());
			if(movedFile.isFile())
			{
				String ext= getFileExtension(file.getName());
				String n = getFileNameWithoutExt(file.getName())+"_Copy";

				boolean exists=true;
				int i=1;
				while(exists)
				{
					if(new File(formatPathWithEndSeparator(_newPath)+ n+i+"."+ext).isFile())
					{
						i++;
					}
					else{
						movedFile=new File(formatPathWithEndSeparator(_newPath)+ n+i+"."+ext);
						exists=false;
					}
				}
			}
			
			try {
				FileInputStream fis=null;
				FileOutputStream fos=null;
				try{
					if(movedFile.createNewFile()){
						fis = new FileInputStream(file);
						fos = new FileOutputStream(movedFile);
						byte b[] = new byte[1024];
						int c=0;
						while((c= fis.read(b)) != -1){
							fos.write(b,0,c);
						}

						returnedMessage.getFiles().add(movedFile);
					}
				}finally
				{
					if(fis!=null)
					{
						fis.close();
					}
					if(fos!=null)
					{
						fos.close();
					}
				}
			} catch(IOException ex)
			{
				// do nothing
			}

		}
		return returnedMessage;
	}

	/**
	 * Lists all the documents (Files that are not directories) that are recursively under the specified path.<br>
	 * All the directories and sub directories under the specified path are going to be listed.
	 * @param givenPath String: The path to be listed
	 * @return ArrayList<DocumentOnServer> : All the documents as DocumentOnServer Objects that are in the tree under the path.<br>
	 * This arrayList can be directly used as List<DocumentOnServer> within the Ivy.designer 
	 */
	public static ArrayList<DocumentOnServer> getDocumentsInPathAll(String givenPath)
	{
		String path = formatPath(givenPath);
		ArrayList <DocumentOnServer> ArrayListDocuments = new ArrayList<DocumentOnServer> ();
		File dir = new File(path);
		if(!dir.exists())
		{
			dir.mkdirs();
			return ArrayListDocuments;
		}
		ArrayList<File> ArrayListDirectories = getSubDirectories(dir);
		ArrayListDocuments.addAll(getDocumentsInDir(dir));
		for(int i = 0; i < ArrayListDirectories.size(); i++)
			ArrayListDocuments.addAll(getDocumentsInDir(ArrayListDirectories.get(i)));

		ArrayListDocuments.trimToSize();
		return ArrayListDocuments;
	}

	/**
	 * Lists all the documents (Files that are not directories) that are directly under the specified path.<br>
	 * The directories and sub directories under the specified path are NOT going to be listed.
	 * @param directory String: The path to be listed
	 * @return ArrayList<DocumentOnServer> : All the documents as DocumentOnServer Objects that are directely in the path.<br>
	 * This arrayList can be directly used as List<DocumentOnServer> within the Ivy.designer 
	 */
	public static ArrayList<DocumentOnServer> getDocumentsInDir(File directory)
	{
		ArrayList<DocumentOnServer>  al = new ArrayList<DocumentOnServer>();
		if(!directory.isDirectory())
		{
			return al;
		}
			
		File files[] = directory.listFiles();
		String today = new Date().format("dd.MM.yyyy");
		String now = new Time().format();
		for(int i = 0; i < files.length; i++)
			if(files[i].isFile())
			{
				DocumentOnServer doc = new DocumentOnServer();
				doc.setFileID("");
				doc.setPath(files[i].getPath().replace("\\", "/"));
				doc.setFilename(files[i].getName());
				doc.setFileSize(getFileSize(files[i]));
				doc.setUserID("IVYSYSTEM");
				doc.setCreationDate(today);
				doc.setCreationTime(now);
				doc.setModificationUserID("IVYSYSTEM");
				doc.setModificationDate(today);
				doc.setModificationTime(now);
				doc.setDescription("");
				doc.setJavaFile(files[i]);
				al.add(doc);
			}

		al.trimToSize();
		return al;
	}

	/**
	 * Lists all the java.io.File (Files that are not directories) that are directly under the specified path.<br>
	 * The directories and subdirectories under the specified path are NOT going to be listed.
	 * @param directory String: The path to be listed
	 * @return List<java.io.File><br>
	 * This List can be directly used as List<java.io.File> within the Ivy.designer 
	 */
	public static List<java.io.File> getFilesInDir(File directory)
	{
		List<java.io.File>  al = List.create(java.io.File.class);
		if(directory==null || !directory.exists() || !directory.isDirectory())
			return al;
		File files[] = directory.listFiles();
		for(int i = 0; i < files.length; i++)
		{
			if(files[i].isFile())
			{
				al.add(files[i]);
			}
		}
		return al;
	}

	/**
	 * This method lists all the directories that are under the specified path.<br>
	 * All the directories and subdirectories under the specified path are going to be listed.
	 * @param parentDir File: the directory that has to be listed
	 * @return ArrayList<File> : All the directories as File Objects.<br>
	 * This arrayList can be directly used as List<File> within the Ivy.designer 
	 */
	public static ArrayList<File> getSubDirectories(File parentDir)
	{
		ArrayList<File> dirs = new ArrayList<File>();
		if(!parentDir.exists() && !parentDir.isDirectory())
			return dirs;
		int j = 0;
		File files[] = parentDir.listFiles();
		for(int i = 0; i < files.length; i++)
			if(files[i].isDirectory())
				j++;

		if(j == 0)
			return dirs;
		for(int i = 0; i < files.length; i++)
			if(files[i].isDirectory())
			{
				dirs.add(files[i]);
				if(getSubDirectories(files[i]) != null)
					dirs.addAll(getSubDirectories(files[i]));
			}

		return dirs;
	}

	/**
	 * This method allows to get back a list of documents that are in a documents List and not an other one.<br>
	 * @param firstList : ArrayList  of documents (DocumentOnServer) that has to be checked
	 * @param secondList : ArrayList of documents (DocumentOnServer)
	 * @return ArrayList<DocumentOnServer> : All the documents as DocumentOnServer Objects that are in the second List and not in the first one.<br>
	 * This arrayList can be directly used as List<DocumentOnServer> within the Ivy.designer 
	 */
	public static ArrayList<DocumentOnServer> getDocumentsListDiff(ArrayList <DocumentOnServer> firstList, ArrayList <DocumentOnServer> secondList)
	{
		ArrayList<DocumentOnServer>  documentsDiff = new ArrayList<DocumentOnServer>();
		for(DocumentOnServer doc: secondList){
			boolean flag = false;
			String pathToCheck = doc.getPath();
			for(DocumentOnServer doc2 : firstList){
				if(pathToCheck.equals(doc2.getPath())){
					flag=true;
					break;
				}
			}
			if(!flag)
			{
				documentsDiff.add(doc);
			}
		}

		documentsDiff.trimToSize();
		return documentsDiff;

	}

	/**
	 * This method allows to get back a list of documents that are in two documents List.<br>
	 * @param firstList : ArrayList of documents (DocumentOnServer)
	 * @param secondList : ArrayList of documents (DocumentOnServer)
	 * @return ArrayList<DocumentOnServer> : All the documents as DocumentOnServer Objects that are in both list.<br>
	 * This arrayList can be directly used as List<DocumentOnServer> within the Ivy.designer 
	 */
	public static ArrayList<DocumentOnServer> getDocumentsListInCommon(ArrayList <DocumentOnServer> firstList, ArrayList <DocumentOnServer> secondList)
	{
		ArrayList<DocumentOnServer>  documentsInCommon = new ArrayList<DocumentOnServer>();
		for(DocumentOnServer doc: firstList){
			String pathToCheck = formatPath(doc.getPath());
			for(DocumentOnServer doc2 : secondList){
				//if in the first list and in the second one we add it
				if(pathToCheck.equals(formatPath(doc2.getPath()))){
					documentsInCommon.add(doc);
					break;
				}
			}
		}
		documentsInCommon.trimToSize();
		return documentsInCommon;
	}

	/**
	 * search all the DocumentOnServer Objects in a list, that have the same path.<br>
	 * return a list with the document on server that should be deleted.
	 * @param docListe : an ArrayList of {@link DocumentOnServer} Objects. In IvyScript you can use directly a List.
	 * @return all the DocumentOnServer Objects in a list, that have the same path.
	 */
	public static ArrayList<DocumentOnServer> getDuplicateDocuments(ArrayList <DocumentOnServer> docListe){
		ArrayList<DocumentOnServer>  duplicateList = new ArrayList<DocumentOnServer>();
		Collection<String> pathListTocheck=new ArrayList<String>();
		ArrayList<String> duplicatePath = new ArrayList<String>();
		for(DocumentOnServer doc: docListe){
			pathListTocheck.add(doc.getPath());
		}
		for(String s:pathListTocheck){
			//We find the path more than one time => all the paths over the first should be deleted from the DB
			int freq=java.util.Collections.frequency(pathListTocheck, s);
			if(freq>1){
				if(java.util.Collections.frequency(duplicatePath, s)==0){ //this path wasn't allready found so we get the duplicated DocumentOnServer Objects
					duplicatePath.add(s);
					int i=0;
					for(DocumentOnServer doc: docListe){
						if(doc.getPath().equals(s)){
							if(i>0){
								duplicateList.add(doc);
							}
							i++;
						}
					}
				}
			}
		}
		return duplicateList;
	}

	/**
	 * This method lists all the directories under a specified path (entrypoint)
	 * Each directory is going to be listed as a FolderOnServer Object
	 * @param entryPoint : String, the path to be checked
	 * @return ArrayList<FolderOnServer> : all the directories that are under this path (inclusive all subdirectories)
	 */
	public static ArrayList<FolderOnServer> getFolderOnServerAll(String entryPoint)
	{
		ArrayList<FolderOnServer> ordnerOnServerAll = new ArrayList<FolderOnServer>();
		String entryPath = formatPath(entryPoint);
		File dir = new File(entryPath);
		if(!dir.exists())
		{
			dir.mkdirs();
			return ordnerOnServerAll;
		}
		int j = 0;
		File files[] = dir.listFiles();
		for(int i = 0; i < files.length; i++)
			if(files[i].isDirectory())
				j++;

		if(j == 0)
			return ordnerOnServerAll;
		for(int i = 0; i < files.length; i++)
			if(files[i].isDirectory())
			{
				FolderOnServer o = new FolderOnServer();
				o.setName(files[i].getName());
				o.setPath(files[i].getPath());
				ordnerOnServerAll.add(o);
				ArrayList <FolderOnServer>oos = new ArrayList<FolderOnServer>();
				oos = getFolderOnServerAll(files[i].getPath());
				if(oos != null)
					ordnerOnServerAll.addAll(oos);
			}

		return ordnerOnServerAll;
	}

	/**
	 * This method lists all the directories that are directly under a specified path (entrypoint)
	 * Each directory is going to be listed as a FolderOnServer Object
	 * @param entryPoint : String, the path to be checked
	 * @return ArrayList<FolderOnServer> : all the directories that are under this path. The subdirectories are not going to be listed.
	 */
	public static ArrayList<FolderOnServer> getFolderOnServer(String entryPoint)
	{
		ArrayList<FolderOnServer> folderOnServer = new ArrayList<FolderOnServer>();
		String entryPath = formatPath(entryPoint);
		File dir = new File(entryPath);
		if(!dir.exists())
		{
			dir.mkdirs();
			return folderOnServer;
		}
		int j = 0;
		File files[] = dir.listFiles();
		for(int i = 0; i < files.length; i++)
			if(files[i].isDirectory())
				j++;

		if(j == 0)
			return folderOnServer;
		for(int i = 0; i < files.length; i++)
			if(files[i].isDirectory())
			{
				FolderOnServer o = new FolderOnServer();
				o.setName(files[i].getName());
				o.setPath(files[i].getPath());
				folderOnServer.add(o);
			}

		return folderOnServer;
	}

	/**
	 * This method allows to build a directory tree structure to be displayed in an RTREE WIDGET<br>
	 * Each Tree in the Tree is going to be a FolderOnServer Object
	 * @param entryPoint : the String representation of the path entrypoint of the tree structure. It will be the root.
	 * @return Tree: the complete Tree representing the directories Tree structure
	 */
	public static Tree makeRDTree(String entryPoint)
	{
		Tree RDTree = new Tree();
		String entryPath = formatPath(entryPoint);
		File dir = new File(entryPath);
		if(!dir.exists())
			dir.mkdirs();
		FolderOnServer o = new FolderOnServer();
		o.setName(dir.getName());
		o.setPath(dir.getPath());
		RDTree.setValue(o);
		RDTree.setInfo(o.getName());
		fillRDTree(entryPath, RDTree);
		return RDTree;
	}

	/**
	 * Recursive method used by the makeRDTree method
	 * @param entryPoint
	 * @param myTree
	 */
	private static void fillRDTree(String entryPoint, Tree myTree)
	{
		String entryPath = formatPath(entryPoint);
		File dir = new File(entryPath);
		if(!dir.exists() || !dir.isDirectory())
		{
			dir.mkdirs();
			return;
		}
		int j = 0;
		File files[] = dir.listFiles();
		for(int i = 0; i < files.length; i++)
			if(files[i].isDirectory())
				j++;

		if(j == 0)
			return;
		for(int i = 0; i < files.length; i++)
			if(files[i].isDirectory())
			{
				FolderOnServer o = new FolderOnServer();
				o.setName(files[i].getName());
				o.setPath(files[i].getPath());
				myTree.createChild(o, files[i].getName());
				fillRDTree(files[i].getPath(), myTree.getLastChild());
			}

	}


	/**
	 * get the folder Name with a given path
	 * @param _p the path
	 * @return the folder name corresponding to the given path
	 */
	public static String getFolderNameFromPath(String _p){
		try{
			String regex =File.separator;
			String path= formatPathWithEndSeparator(_p,false);

			path = path.substring(0,path.length()-1);
			//Ivy.log().info("Path is "+path +" "+ path.lastIndexOf(regex));
			return path.substring(path.lastIndexOf(regex)+1);
		}catch(Exception e){
			Ivy.log().debug("Exception in getFolderNameFromPath: "+e.getMessage());
			return null;
		}
	}

	/**
	 * This method lists all the Files that are in a specified directory.<br>
	 * The directory is specified as a String representation of the dirpath
	 * @param serverPath : the path on the server that has to be listed (String)
	 * @return File[]: an array of the files that are contained into the directory
	 */
	public static File[] getFiles(String serverPath)
	{
		formatPath(serverPath);
		File dir = new File(serverPath);
		if(!dir.exists())
			dir.mkdirs();
		File files[] = dir.listFiles();
		int j = 0;
		for(int i = 0; i < files.length; i++)
			if(!files[i].isDirectory())
				j++;

		File arrayOfFiles[] = new File[j];
		j = 0;
		for(int i = 0; i < files.length; i++)
			if(!files[i].isDirectory())
			{
				arrayOfFiles[j] = files[i];
				j++;
			}
		return arrayOfFiles;
	}



	/**
	 * 
	 * @param f : the File which length should be returned
	 * @return The String representation of the File Size.<br>
	 * If the size exceeds the Mb unit, then it will be returned in Mb (e.g. "3.45 Mb")<br>
	 * If the size exceeds the Kb unit, then it will be returned in Kb (e.g. "10 Kb"). In that case, the decimal part will not be shown.<br>
	 * If the size is under the Kb unit, then it will be returned in bytes (e.g. "120 bytes")<br>
	 */
	public static String getFileSize(File f){
		StringBuffer size= new StringBuffer();
		if(f == null || !f.exists())
			size.append("0 byte");
		else{
			float l = f.length();
			if(l>1048576){
				java.text.NumberFormat formatter = new java.text.DecimalFormat("#.##");
				float s = l/1048576;
				String nb =formatter.format(s);
				size.append(nb);
				size.append(" Mb");
			}
			else if(l>1024){
				java.text.NumberFormat formatter = new java.text.DecimalFormat("#");
				float s = l/1024;
				String nb =formatter.format(s);
				size.append(nb);
				size.append(" Kb");
			}else {
				java.text.NumberFormat formatter = new java.text.DecimalFormat("#");
				String nb =formatter.format(l);
				size.append(nb);
				size.append(" bytes");
			}
		}
		return size.toString();
	}

	/**
	 * return the file name without the file extension
	 * @param fileName the string filename 
	 * @return file name without the file extension
	 */
	public static String getFileNameWithoutExt(String fileName){
		String s="";
		if(fileName.lastIndexOf(".")>0)
			s = fileName.substring(0,fileName.lastIndexOf("."));
		else s=fileName;

		return s;
	}

	/**
	 * Returns the File extension without "." from a given file name
	 * @param fileName the string filename
	 * @return the file extension
	 */
	public static String getFileExtension(String fileName){
		String s="";
		if(fileName.lastIndexOf(".")>0)
			s = fileName.substring(fileName.lastIndexOf(".")+1);

		return s;
	}



	/**
	 * get the File Directory Path
	 * @param file the java.io.File
	 * @return the file path without the file name
	 * @throws exception in case of outOufBoundIndexException or in case of NullPointerException
	 */
	public static String getFileDirectoryPath(File file) throws Exception {
		if(file == null){
			return "";
		}
		if(file.getPath().lastIndexOf(File.separator) <=0)
		{
			return "";
		}
		String s = file.getPath().substring(0, file.getPath().lastIndexOf(File.separator));
		return s;
	}

	/**
	 * Returns the checksum Adler32 value of a File on the server
	 * @param file
	 * @return the Adler32 Checksum, long value
	 */
	public static long getAdler32(File file){
		Adler32 checksum = new Adler32();
		checksum.reset();
		try{
			InputStream in = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) >= 0) {
				checksum.update(buffer, 0, bytesRead);
			}
			in.close();
		}catch(IOException ioe){
			//todo
		}
		return checksum.getValue();
	}
}
