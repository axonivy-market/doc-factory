/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.thumbnailer.persistence;

import java.io.File;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.ReturnedMessage;
import ch.ivyteam.ivy.addons.filemanager.html.table.model.ThumbnailData;
import ch.ivyteam.ivy.scripting.objects.List;

/**
 * @author ec
 *
 */
public abstract class AbstractThumbnailHandler {

	public abstract boolean insertDefaulThumbnail(long _original_file_id) throws Exception;

	public abstract boolean insertThumbnail(File thumbnailFile, long _original_file_id) throws Exception;

	public abstract ReturnedMessage copyThumbnail(long sourceFileId, long desFileId) throws Exception;

	public abstract ReturnedMessage createThumbnailForZipFile(String dirPath,
			String zipName);

	public abstract boolean deleteThumbnail(long org_file_id, boolean deleteRealFile)
			throws Exception;

	public abstract ReturnedMessage deleteAndRollBackThumbnail(
			DocumentOnServer prevDocument, File thumbnailFile,
			boolean useDefaultFlag, boolean createRealFile) throws Exception;

	public abstract List<ThumbnailData> getThumbnailsWithJavaFile(
			List<DocumentOnServer> documents, boolean includeJavaFile,
			boolean exportDefaultThumb) throws Exception;

	public abstract List<DocumentOnServer> getDocumentsNoneThumbnail(
			List<DocumentOnServer> documentOnServers,
			boolean isDeleteOutOfDateThumb) throws Exception;

	public abstract void cleanThumbnailFolder() throws Exception;
	
	
	public abstract boolean checkIfThumbnailExistsForDocumentOnServerId(long org_file_id) throws Exception;
	
	public abstract ThumbnailData getThumbnailForDocumentOnServerIdWithOptionalThumbnailJavaFile(long org_file_id, boolean withJavaFile) throws Exception;
	
}
