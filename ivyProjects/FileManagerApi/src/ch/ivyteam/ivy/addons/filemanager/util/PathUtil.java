/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.util;

import java.util.Scanner;

import ch.ivyteam.ivy.scripting.objects.List;

/**
 * 
 * This class is responsible for providing utility methods for formating paths
 * @author ec
 */
public class PathUtil {
	
	/**
	 * Replaces all the backslashes through "/" in a String All the file paths
	 * in the persistence system should use the "/" char as file separator
	 * 
	 * @param _s
	 *            the String (query) to treat
	 * @return the String with all its backslashes replaced by "/" char
	 */
	static public String escapeBackSlash(String _s) {
		if (_s != null) {
			_s = _s.replaceAll("\\\\", "/");
		}
		return _s;
	}

	/**
	 * Formats a given path with "/" as separator<br>
	 * so that it is always compatible for Windows, Linux and Mac OS for creating a new File object.<br>
	 * 
	 * @param _path
	 * @return formatted path with the system File.separator
	 */
	public static String formatPath(String _path) {
		_path = formatPathForDirectoryWithoutLastSeparator(_path);
		return _path;
	}

	/**
	 * Formats a given directory path with "/" as separator<br>
	 * so that it is always compatible for Windows, Linux and Mac OS.<br>
	 * The resulting path ends always with "/" if it contains at least a
	 * directory name. <br>
	 * Example:<br>
	 *  If you give "\\\\root\\test\\test1" the result will be "//root/test/test1/".<br> 
	 *  If you give "root\\test\\test1" the result will be "root/test/test1/".<br> 
	 *  If you enter "//////", an empty String "" will be returned..
	 * 
	 * @param _path
	 * @return formatted path with "/"
	 */
	public static String formatPathForDirectory(String _path) {
		//_path = formatPathForDirectoryWithoutLastAndFirstSeparator(_path);
		
		if (_path != null && !_path.trim().equals("")) {
			_path = org.apache.commons.lang.StringUtils.replace(_path, "\\","/");
			if (!_path.endsWith("/")) {
				_path = _path + "/";
			}
		}
		return _path;
	}

	/**
	 * Formats a given directory path with "/" as separator<br>
	 * so that it is always compatible for Windows, Linux and Mac OS for creating a new File object.<br>
	 * The resulting path never ends or begins with "/"<br>
	 * Example: <br>
	 * if you give "\\root\\test\\test1\\" the result will be "root/test/test1". <br>
	 * If you enter "//////", an empty String "" will be returned...<br>
	 * 
	 * @param _path
	 * @return formatted path with "/"
	 */
	public static String formatPathForDirectoryWithoutLastAndFirstSeparator(String _path) {
		if (_path != null && !_path.trim().equals("")) {
			_path = _path.trim();
			_path = org.apache.commons.lang.StringUtils.replace(_path, "\\",
					"/");
			while (_path.endsWith("/") && _path.length() > 1) {
				_path = _path.substring(0, _path.length() - 1);
			}
			while (_path.startsWith("/") && _path.length() > 1) {
				_path = _path.substring(1);
			}
			if (_path.startsWith("/")) {
				_path = "";
			}
		}
		return _path;
	}
	
	/**
	 * Formats a given directory path with "/" as separator<br>
	 * so that it is always compatible for Windows, Linux and Mac OS for creating a new File object.<br>
	 * The resulting path never ends "/" but is allowed to begin with "\\" to be able to get files on a server file share.<br>
	 * Example: <br>
	 * If you enter "\\\\root\\test/test1\\" the result will be "//root/test/test1".<br>
	 * * If you enter "root\\test/test1\\" the result will be "root/test/test1".<br>
	 * If you enter "//test/test2\\test3" the result will be "//test/test2/test3".<br>
	 * If you enter "//////" or "\\\\\\", an empty String "" will be returned...
	 * 
	 * @param _path the path to format
	 * @return formatted path with "/" as separator
	 */
	public static String formatPathForDirectoryWithoutLastSeparator(String _path) {
		if (_path != null && !_path.trim().equals("")) {
			_path = _path.trim();

			_path = org.apache.commons.lang.StringUtils.replace(_path, "\\",
					"/");
			
			boolean b = _path.startsWith("/");
			while (_path.endsWith("/") && _path.length() > 1) {
				_path = _path.substring(0, _path.length() - 1);
			}
			int i =0;
			while (_path.startsWith("/") && _path.length() > 1) {
				_path = _path.substring(1);
				i++;
			}
			if (_path.startsWith("/")) {
				_path = "";
			}
			if(b){
				for(int j = 0; j<i; j++){
					_path = "/"+_path;
				}
			}
		}
		return _path;
	}
	
	/**
	 * Formats a given directory path with "/" as separator<br>
	 * so that it is always compatible for Windows, Linux and Mac OS for creating a new File object.<br>
	 * The resulting path always ends  with"/" and never starts with "\\".<br>
	 * @param _path
	 * @return
	 */
	public static String formathPathForDirectoryWithoutFirstSeparatorWithEndSeparator(String _path){
		_path = formatPathForDirectory(_path);
		if (_path != null && !_path.trim().equals("")) {
			
			while (_path.startsWith("/") && _path.length() > 1) {
				_path = _path.substring(1);
			}
			if (_path.startsWith("/")) {
				_path = "";
			}
		}
		return _path;
	}
	
	/**
	 * Formats a given directory path with "/" as separator<br>
	 * so that it is always compatible for Windows, Linux and Mac OS.<br>
	 * The resulting path ends always with "/" if it contains at least a
	 * directory name. Example: if you give "\\root\\test\\test1" the result
	 * will be "/root/test/test1/". 
	 * If you enter "//////", an empty String ""
	 * will be returned..
	 * 
	 * @param _path
	 * @return formatted path with "/"
	 */
	public static String formatPathForDirectoryAllowingSlashesAndBackslashesAtBeginOfPath(String _path) {
		_path = formatPathForDirectoryWithoutLastSeparator(_path);
		if (_path != null && !_path.trim().equals("")) {
			if (!_path.endsWith("/")) {
				_path = _path + "/";
			}
		}
		return _path;
	}

	/**
	 * escape the underscore sign in paths to be able to perform LIKE sql searches.
	 * @param _path
	 * @return
	 */
	public static String escapeUnderscoreInPath(String _path) {
		if (_path != null && !_path.trim().equals("")) {
			_path=_path.replaceAll("_", "\\\\_");
		}
		return _path;
	}
	
	/**
	 * escape the special signs _()' in paths to be able to perform LIKE sql searches.
	 * @param _path
	 * @return
	 */
	public static String escapeSpecialSQLSignsInPath(String _path) {
		if (_path != null && !_path.trim().equals("")) {
			_path=_path.replaceAll("_", "\\\\_");
			_path=_path.replaceAll("\\(", "\\\\(");
			_path=_path.replaceAll("\\)", "\\\\)");
			_path=_path.replaceAll("'", "''");
		}
		return _path;
	}
	/**
	 * escape the special signs _' in paths to be able to perform LIKE sql searches in Oracle.
	 * @param _path
	 * @return
	 */
	public static String escapeSpecialSQLSignsForOracleInPath(String _path) {
		if (_path != null && !_path.trim().equals("")) {
			_path=_path.replaceAll("_", "\\\\_");
			_path=_path.replaceAll("'", "''");
		}
		return _path;
	}
	
	/**
	 * escape the ' sign in paths to be able to perform LIKE sql searches.
	 * @param s
	 * @return
	 */
	public static String escapeSingleQuotesInStringForSQL(String s) {
		if (s != null && !s.trim().equals("")) {
			s=s.replaceAll("'", "''");
		}
		return s;
	}
	
	/**
	 * returns the parent directory path for directory denoted by the given path.<br>
	 * if the given path is null, is an empty String, or is a root path (no parent), it returns an empty String.
	 * @param path: the directory path
	 * @return the parent directory path
	 */
	public static String getParentDirectoryPath(String path){
		if(path==null || path.trim().length()==0) {
			return "";
		}
		path=formatPathForDirectoryWithoutLastSeparator(path);
		if(path.contains("/")) {
			String s = path.substring(0,path.lastIndexOf("/")+1);
			if(formatPathForDirectoryWithoutLastSeparator(s).length()==0) {
				return "";
			}
			return path.substring(0, path.lastIndexOf("/"));
		}else{
			return "";
		}
	}
	
	/**
	 * returns the directory name from a directory path.
	 * @param directoryPath: the directory path
	 * @return the directory name from a directory path. Empty String if the directoryPath parameter is Null or an empty String.
	 */
	public static String getDirectoryNameFromPath(String directoryPath){
		if(directoryPath==null || directoryPath.trim().length()==0)
		{
			return "";
		}
		directoryPath=formatPathForDirectoryWithoutLastSeparator(directoryPath);
		if(!directoryPath.contains("/")) {
			return directoryPath;
		} else {
			String s = directoryPath.substring(0,directoryPath.lastIndexOf("/")+1);
			if(formatPathForDirectoryWithoutLastSeparator(s).length()==0) {
				return directoryPath;
			}
			return directoryPath.substring(directoryPath.lastIndexOf("/")+1);
		}
		
	}
	
	/**
	 * transforms a String that represents a list of token separated with a delimiter into a List<String>
	 * @param s: the String 
	 * @param list_sep: the delimiter
	 * @return the List<String>
	 */
	public static List<String> getListFromString(String s, String list_sep){
		List<String> l = List.create(String.class);
		if(s==null || s.trim().equals("") || list_sep==null || list_sep.trim().equals(""))
		{
			return l;
		}
		Scanner sc = new Scanner(s);
		sc.useDelimiter(list_sep);
		while(sc.hasNext()){
			String t = sc.next().trim();

			if(t.length()>0)
			{
				l.add(t);
			}
		}
		sc.close();
		return l;
	}
	
	/**
	 * Returns a String composed by String members of a list of String objects each separated by a colon.<br>
	 * if the list is null or empty, returns an empty String.
	 * @param stringList
	 * @return
	 */
	public static String returnStringFromList(List<String> stringList)
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
}
