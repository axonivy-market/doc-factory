/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.filetag;

import ch.ivyteam.ivy.addons.filemanager.FileTag;

/**
 * @author ec
 *
 */
public abstract class AbstractFileTagsController {

	/**
	 * creates a new Tag for the document corresponding to the given id (fileId).<br>
	 * The fileId - tag value association must be unique. So you cannot create two tags for a file with the same value.
	 * @param fileId: the id of the file that will be associated with the tag
	 * @param tag: the tag as String
	 * @return the new Tag. 
	 * @throws Exception If the fileId is not a positive number above zero or the tag value is empty. Also Exception if any SQL Exception is thrown.
	 */
	public abstract FileTag createTag(long fileId, String tag) throws Exception;
	
	/**
	 * modify an existing tag.<br>
	 * @param tagId the id of the tag to be modified
	 * @param tag the new String value of the tag
	 * @return the modified FileTag object
	 * @throws Exception If the tagId is not a positive number above zero or the tag value is empty. Also Exception if any SQL Exception is thrown.
	 */
	public abstract FileTag modifyTag(long tagId, String tag) throws Exception;
	
	/**
	 * deletes the tag corresponding to the given id
	 * @param tagId
	 * @throws Exception
	 */
	public abstract void deleteTag(long tagId) throws Exception;
	
	/**
	 * Returns a precise Tag for a document. Here the given tag must match exactly the file tag stored value.
	 * @param fileId: the file id corresponding to the document.
	 * @param tag: the tag value.
	 * @return the found ch.ivyteam.ivy.addons.filemanager.FileTag object. This object is empty if no corresponding tag was found.
	 * @throws Exception If the fileId is not a positive number above zero or the tag value is empty. Also Exception if any SQL Exception is thrown.
	 */
	public abstract FileTag getTag(long fileId, String tag) throws Exception;
	
	/**
	 * Returns the Tag corresponding to the given tag id.
	 * @param tagId the tag id to be returned
	 * @return the FileTag object found. this object is empty if no 
	 * @throws Exception
	 */
	public abstract FileTag getTagById(long tagId) throws Exception;
	
	/**
	 * Returns a list of FileTag objects that match the given tag pattern.<br>
	 * If the fileId parameter is greater than zero, then only the FileTag objects from the corresponding document will be returned.<br>
	 * If the fileId parameter is zero, then all the tags matching the pattern will be returned, regardless which document they belong to.
	 * @param fileId: optional, the id of the document.
	 * @param tagPattern: the tag pattern to be searched
	 * @return a java.util.list of FileTag objects that match the given tag pattern
	 * @throws Exception If the pattern tag parameter value is empty. Also Exception if any SQL Exception is thrown.
	 */
	public abstract java.util.List<FileTag> getTagsWithPattern(long fileId, String tagPattern) throws Exception;
	
	/**
	 * Returns all the tags concerning a file
	 * @param fileId the id of the file
	 * @return a java.util.list of FileTag objects that belong to the file
	 * @throws Exception Exception If the fileId parameter <= 0. Also Exception if any SQL Exception is thrown.
	 */
	public abstract java.util.List<FileTag> getFileTags(long fileId) throws Exception;
	
	/**
	 * Get all the tags beginning with the given search String. The search is case insensitive.
	 * @param searchFor the String for the search
	 * @return a java.util.List<String> containing the tags beginning with the given search String.<br>
	 * If no tags could be found, the result is null.<br>
	 * If the search String is null or empty, then the list contains all the tags.
	 * @throws Exception
	 */
	public abstract java.util.List<String> searchAvailableTags(String searchFor) throws Exception;
}
