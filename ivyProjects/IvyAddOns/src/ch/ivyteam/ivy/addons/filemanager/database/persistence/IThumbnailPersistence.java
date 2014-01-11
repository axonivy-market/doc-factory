package ch.ivyteam.ivy.addons.filemanager.database.persistence;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.html.table.model.ThumbnailData;
import ch.ivyteam.ivy.scripting.objects.List;

public interface IThumbnailPersistence extends IItemPersistence<ThumbnailData> {
	
	/**
	 * delete the Thumbnail given by its parent file id, with the option to delete the image file.
	 * @param org_file_id parent file id as long
	 * @param deleteRealFile if true the image file will be deleted
	 * @return true if the thumbnail was 
	 * @throws Exception
	 */
	public boolean deleteThumbnail(long org_file_id, boolean deleteRealFile) throws Exception;
	
	/**
	 * Insert a Thumbnail with the following parameteres:
	 * @param _file type java.io.file content of thumbnail file. It's created by AsposeThumbnailer
	 * @param _original_file_id type long Id of original file that thumbnail belong to.
	 * @param createFile boolean export thumbnail to disk or not.
	 * @return the ThumbnailData that was created
	 * @throws Exception
	 */
	public ThumbnailData insertThumbnail(java.io.File _file, long _original_file_id)
			throws Exception;
	
	/**
	 * returns the Thumbnail with or without thumbnail file depending on the value of the withThumbnailFile parameter
	 * @param _original_file_id
	 * @param withThumbnailFile
	 * @return
	 */
	public ThumbnailData getThumbnail(long _original_file_id, boolean withThumbnailFile)
			throws Exception;

	
	public List<ThumbnailData> getThumbnailsForDocuments(List<DocumentOnServer> documents, boolean includeJavaFile, boolean exportDefaultThumb)
			throws Exception;
}
