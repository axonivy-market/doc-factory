/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.persistence;


import ch.ivyteam.ivy.addons.filemanager.FileTag;

/**
 * @author ec
 *
 */
public interface IFileTagPersistence extends IItemPersistence<FileTag>{

	/**
	 * Get all the tags beginning with the given search String. The search is case insensitive.
	 * @param searchFor the String for the search
	 * @return a java.util.List<String> containing the tags beginning with the given search String.<br>
	 * If no tags could be found, the result is null.<br>
	 * If the search String is null or empty, then the list contains all the tags.
	 * @throws Exception
	 */
	public java.util.List<String> searchAvailableTags(String searchFor) throws Exception;
	
	/**
	 * Returns the Tag for the file denoted by its id and the tag value.
	 * @param fileid the file unique id as long
	 * @param tagValue the tag value as String
	 * @return the corresponding Tag or null if no Tag could be found
	 * @throws Exception
	 */
	public FileTag getFileTag(long fileid, String tagValue) throws Exception;
	
	/**
	 * Returns all the fileTags existing for a given file denoted by its id.
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public java.util.List<FileTag> getFileTags(long fileId) throws Exception;
	
	/**
	 * Returns a list of FileTag objects that match the given tag pattern.<br>
	 * If the fileId parameter is greater than zero, then only the FileTag objects from the corresponding document will be returned.<br>
	 * If the fileId parameter is zero, then all the tags matching the pattern will be returned, regardless which document they belong to.
	 * @param fileId: optional, the id of the document.
	 * @param tagPattern: the tag pattern to be searched
	 * @return a java.util.list of FileTag objects that match the given tag pattern
	 * @throws Exception If the pattern tag parameter value is empty. Also Exception if any SQL Exception is thrown.
	 */
	public java.util.List<FileTag> getTagsWithPatternAndOptionalFileId(long fileId, String tagPattern) throws Exception;
	
}
