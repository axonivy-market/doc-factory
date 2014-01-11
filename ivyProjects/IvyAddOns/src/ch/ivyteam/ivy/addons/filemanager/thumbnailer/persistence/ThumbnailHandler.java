/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.thumbnailer.persistence;

import java.io.FileNotFoundException;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.ReturnedMessage;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IDocumentOnServerPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IThumbnailPersistence;
import ch.ivyteam.ivy.addons.filemanager.html.table.model.ThumbnailData;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.ThumbnailConstants;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.List;

/**
 * @author ec
 * 
 */
public class ThumbnailHandler extends AbstractThumbnailHandler {

	BasicConfigurationController configuration;
	IThumbnailPersistence thumbPersistence;
	IDocumentOnServerPersistence docPersistence;

	/**
	 * <b>Not public API</b>
	 */
	protected ThumbnailHandler() {
	}

	public ThumbnailHandler(BasicConfigurationController config)
			throws Exception {
		this.configuration = config;
		this.initHandler();
	}

	private void initHandler() throws Exception {
		this.thumbPersistence = PersistenceConnectionManagerFactory
				.getIThumbnailPersistenceInstance(this.configuration);
		this.docPersistence = PersistenceConnectionManagerFactory
				.getIDocumentOnServerPersistenceInstance(this.configuration);
	}

	/**
	 * <b>Not public API</b>
	 */
	protected void setPersistenceManager(IThumbnailPersistence pers) {
		this.thumbPersistence = pers;
	}

	/**
	 * <b>Not public API</b>
	 */
	protected void setDocPersistence(IDocumentOnServerPersistence docPers) {
		this.docPersistence = docPers;
	}

	/**
	 * 
	 * @param _original_file_id
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean insertDefaulThumbnail(long _original_file_id)
			throws Exception {
		if (_original_file_id <= 0) {
			throw new IllegalArgumentException(
					"The ThumbnailData parent file id is not valid in ThumbnailHandler.insertDefaulThumbnail(long _original_file_id)");
		}
		ThumbnailData data =this.insertDefaulThumbnailReturnInsertedData(_original_file_id);
		return (data != null && data.getThumbnailId() > 0);
	}

	protected ThumbnailData insertDefaulThumbnailReturnInsertedData(
			long _original_file_id) throws Exception {
		ThumbnailData data = new ThumbnailData();
		data.setOrgFileId(String.valueOf(_original_file_id));
		return this.thumbPersistence.create(data);

	}

	/**
	 * 
	 * @param thumbnailFile
	 * @param _original_file_id
	 * @param createFile
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean insertThumbnail(java.io.File thumbnailFile,
			long _original_file_id) throws Exception {
		ThumbnailData data = this.insertThumbnailReturnInsertedData(thumbnailFile, _original_file_id);
		return (data != null && data.getThumbnailId() > 0);
	}

	private ThumbnailData insertThumbnailReturnInsertedData(
			java.io.File thumbnailFile, long _original_file_id) throws Exception {
		if (_original_file_id <= 0) {
			throw new IllegalArgumentException(
					"The ThumbnailData parent file id is not valid in ThumbnailHandler.insertThumbnailReturnInsertedData(java.io.File thumbnailFile, long _original_file_id, boolean createFile)");
		}
		return this.thumbPersistence.insertThumbnail(thumbnailFile,
				_original_file_id);

	}

	/**
	 * 
	 * @param sourceFileId
	 * @param desFileId
	 * @param createRealFile
	 * @return
	 * @throws Exception
	 */
	@Override
	public ReturnedMessage copyThumbnail(long sourceFileId, long desFileId) throws Exception {
		ReturnedMessage message = new ReturnedMessage();
		ThumbnailData srcData = this.thumbPersistence.getThumbnail(
				sourceFileId, true);
		if (srcData.isUseDefault()) {
			// insert a default thumbnail
			if (this.insertDefaulThumbnail(desFileId)) {
				message.setType(FileHandler.SUCCESS_MESSAGE);
			} else {
				message.setType(FileHandler.ERROR_MESSAGE);
			}
		} else {
			ThumbnailData tdata = this.thumbPersistence.insertThumbnail(
					srcData.getThumbnailFile(), desFileId);
			if (tdata != null && !tdata.getOrgFileId().isEmpty()
					&& Long.parseLong(tdata.getOrgFileId()) > 0) {
				message.setType(FileHandler.SUCCESS_MESSAGE);
			} else {
				message.setType(FileHandler.ERROR_MESSAGE);
			}
		}
		return message;
	}

	/**
	 * 
	 * @param dirPath
	 * @param zipName
	 * @return
	 */
	@Override
	public ReturnedMessage createThumbnailForZipFile(String dirPath,
			String zipName) {
		ReturnedMessage message = new ReturnedMessage();
		message.setType(FileHandler.ERROR_MESSAGE);
		// check valid data
		if (dirPath != null && !dirPath.isEmpty() && zipName != null
				&& !zipName.isEmpty()) {
			dirPath = PathUtil
					.formatPathForDirectoryWithoutLastAndFirstSeparator(dirPath)
					+ "/";
			zipName = zipName.endsWith(".zip") ? zipName : zipName + ".zip";
			try {
				// get id of zip file
				DocumentOnServer zipFile = this.docPersistence.get(dirPath
						+ zipName);
				if (zipFile != null && zipFile.getFileID().trim().length() > 0) {
					// insert thumbnail to DB
					if (this.insertDefaulThumbnail(Long.parseLong(zipFile.getFileID()))) {
						message.setType(FileHandler.SUCCESS_MESSAGE);
					}
				}
			} catch (Exception e) {
				message.setText(e.getMessage());
				message.setType(FileHandler.ERROR_MESSAGE);
				return message;
			}

		}
		return message;
	}

	/**
	 * 
	 * @param org_file_id
	 * @param deleteRealFile
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean deleteThumbnail(long org_file_id, boolean deleteRealFile)
			throws Exception {
		ThumbnailData data = new ThumbnailData();
		data.setOrgFileId(String.valueOf(org_file_id));
		boolean flag = this.thumbPersistence.delete(data);
		if (flag && deleteRealFile) {
			String tmpPath = ThumbnailConstants.DEFAULT_THUMB_FOLDER
					+ org_file_id + ThumbnailConstants.DEFAULT_THUMBNAIL_TYPE;
			java.io.File javaFile = new java.io.File(tmpPath);
			if (javaFile.exists()) {
				javaFile.delete();
			}
		}
		return flag;
	}

	/**
	 * 
	 * @param prevDocument
	 * @param thumbnailFile
	 * @param useDefaultFlag
	 * @param createRealFile
	 * @return
	 * @throws Exception
	 */
	@Override
	public ReturnedMessage deleteAndRollBackThumbnail(
			DocumentOnServer prevDocument, java.io.File thumbnailFile,
			boolean useDefaultFlag, boolean createRealFile) throws Exception {

		ReturnedMessage message = new ReturnedMessage();
		message.setType(FileHandler.SUCCESS_MESSAGE);
		if (prevDocument == null
				|| prevDocument.getFileID().trim().length() == 0
				|| thumbnailFile == null || !thumbnailFile.exists()) {
			message.setText("The Document does not exist.");
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}
		boolean flag = false;
		try {
			// delete thumbnail
			flag = this.deleteThumbnail(
					Long.parseLong(prevDocument.getFileID()), false);

		} catch (Exception e) {
			Ivy.log().error("[@34R56] Exception:" + e.getMessage());
			message.setType(FileHandler.ERROR_MESSAGE);
			message.setText(e.getMessage());
		}
		if (!flag) {
			message.setText("The Thumbnail could not be deleted.");
			message.setType(FileHandler.ERROR_MESSAGE);
			return message;
		}

		// check revision
		if (prevDocument.getVersionnumber().intValue() > 0) {
			if (useDefaultFlag) {
				this.insertDefaulThumbnail(Long.parseLong(prevDocument
						.getFileID()));
				message.setText("TRUE");
			} else {
				// insert new thumbnail
				this.insertThumbnailReturnInsertedData(thumbnailFile,
						Long.parseLong(prevDocument.getFileID()));
				message.setText("FALSE");
			}
		}
		return message;
	}

	/**
	 * 
	 * @param documents
	 * @param includeJavaFile
	 * @param exportDefaultThumb
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ThumbnailData> getThumbnailsWithJavaFile(
			List<DocumentOnServer> documents, boolean includeJavaFile,
			boolean exportDefaultThumb) throws Exception {
		return this.thumbPersistence.getThumbnailsForDocuments(documents,
				includeJavaFile, exportDefaultThumb);
	}

	/**
	 * get list documents that don't have thumbnail yet
	 * 
	 * @param documentOnServers
	 * @return
	 */
	@Override
	public List<DocumentOnServer> getDocumentsNoneThumbnail(
			List<DocumentOnServer> documentOnServers,
			boolean isDeleteOutOfDateThumb) throws Exception {
		List<DocumentOnServer> result = List.create(DocumentOnServer.class);
		if (documentOnServers == null || documentOnServers.size() <= 0) {
			return result;
		}
		List<ThumbnailData> thumbs = null;
		String tmpPath = ThumbnailConstants.DEFAULT_THUMB_FOLDER;
		String filePath = "";
		try {
			int sizeDoc = documentOnServers.size();
			// get list thumb without javafile
			// Ivy.log().info("CHECKING THUMBNAIL ON THE FLY");
			thumbs = this.getThumbnailsWithJavaFile(documentOnServers, false,
					false);
			// Ivy.log().info("size thumb:" + thumbs.size());
			if (thumbs != null && thumbs.size() > 0) {
				ThumbnailData thumb = null;
				DocumentOnServer doc = null;
				DocumentOnServer doc2 = null;
				String orgModificationDate = "";
				String orgModificationTime = "";
				String modificationDate = "";
				String modificationTime = "";
				// loop to find
				int sizeThumb = thumbs.size();
				boolean bFound;

				for (int i = 0; i < sizeDoc; i++) {
					doc = documentOnServers.get(i);
					bFound = false;
					// loop on list thumbnail
					for (int j = 0; j < sizeThumb; j++) {
						thumb = thumbs.get(j);
						if (thumb.getOrgFileId().equalsIgnoreCase(
								doc.getFileID())) {
							// check date
							doc2 = this.docPersistence.get(Long.parseLong(doc
									.getFileID()));
							if (doc2 != null) {
								modificationDate = doc2.getModificationDate()
										.trim();
								modificationTime = doc2.getModificationTime()
										.trim();
							}
							orgModificationDate = thumb.getModificationDate();
							orgModificationTime = thumb.getModificationTime();
							// found a document that have thumbnail
							if (modificationDate
									.equalsIgnoreCase(orgModificationDate)
									&& modificationTime
											.equalsIgnoreCase(orgModificationTime)) {
								bFound = true;
								break;
							}
						}
					}

					if (!bFound) {
						// add to list
						if (doc.getJavaFile() == null) {
							// Ivy.log().info("This Document dont have javaFile: "
							// + doc.getFileID());
							result.add(this.docPersistence.get(Long
									.parseLong(doc.getFileID())));
						} else {
							// Ivy.log().info("This Document have java file: " +
							// doc.getFileID());
							result.add(doc);
						}
						// delete thumbnail if it is out of date
						if (isDeleteOutOfDateThumb) {
							filePath = tmpPath + doc.getFileID()
									+ ThumbnailConstants.DEFAULT_THUMBNAIL_TYPE;
							java.io.File javFile = new java.io.File(filePath);
							// delete
							if (javFile.exists()) {
								javFile.delete();
							}
						}
					}
				}

			} else {
				// all document don't have thumbnail yet
				return documentOnServers;
			}

		} catch (Exception ex) {
			Ivy.log().error(
					"[@764FD] Exception at getDocumentsNoneThumbnail(): "
							+ ex.getMessage());
			Ivy.log().error(ex.getStackTrace());
			throw ex;
		}
		return result;
	}

	/**
	 * delete thumbnail folder after exiting preview mode
	 */
	@Override
	public void cleanThumbnailFolder() throws Exception {
		try {
			String tmpPath = ThumbnailConstants.DEFAULT_THUMB_FOLDER;
			java.io.File javFile = new java.io.File(tmpPath);
			// Ivy.log().info(javFile.getAbsolutePath());
			if (javFile.isDirectory()) {
				for (java.io.File c : javFile.listFiles()) {
					Ivy.log().info("delete file:" + c.getAbsolutePath());
					c.delete();
				}
			}
			if (!javFile.delete())
				throw new FileNotFoundException("Failed to delete file: "
						+ javFile);
		} catch (Exception e) {
			Ivy.log().error(e.getMessage());
			throw e;
		}
	}

	@Override
	public boolean checkIfThumbnailExistsForDocumentOnServerId(long org_file_id)
			throws Exception {
		return this.thumbPersistence.getThumbnail(org_file_id, false) != null;
	}

	@Override
	public ThumbnailData getThumbnailForDocumentOnServerIdWithOptionalThumbnailJavaFile(
			long org_file_id, boolean withJavaFile) throws Exception {
		return this.thumbPersistence.getThumbnail(org_file_id, withJavaFile);
	}

}
