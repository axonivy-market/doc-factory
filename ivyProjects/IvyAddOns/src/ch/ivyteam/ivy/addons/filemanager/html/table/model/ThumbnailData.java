package ch.ivyteam.ivy.addons.filemanager.html.table.model;

import ch.ivyteam.ivy.addons.filemanager.thumbnailer.ThumbnailConstants;

public class ThumbnailData {
	
	private long thumbnailId;
	private String timeStamp;
	private String orgFileId;
	private String url;
	private boolean useDefault;
	private java.io.File thumbnailFile;
	private String creationDate;
	private String creationTime;
	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	private String modificationDate;
	private String modificationTime;
	
	public ThumbnailData() {
		
	}
	
	/**
	 * Creates a new ThumbnailData object.
	 * @param orgFileId : the parent DocumentOnServer Id as String
	 * @param url : the physical thumbnail file path
	 * @param timeStamp 
	 */
	public ThumbnailData(String orgFileId, String url, String timeStamp){
		this.orgFileId = orgFileId;
		this.timeStamp = timeStamp;
		this.setUrl(url);
	}
	
	/**
	 * @return the thumbnailId
	 */
	public long getThumbnailId() {
		return thumbnailId;
	}

	/**
	 * @param thumbnailId the thumbnailId to set
	 */
	public void setThumbnailId(long thumbnailId) {
		this.thumbnailId = thumbnailId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	/**
	 * returns the parent DocumentOnServer Id as String
	 * @return
	 */
	public String getOrgFileId() {
		return orgFileId;
	}

	/**
	 * returns the physical thumbnail file path
	 * @return
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Set the physical thumbnail file path
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
		if(this.url!=null) {
			if(this.url.endsWith(ThumbnailConstants.DEFAULT_THUMBNAIL_IMAGE)){
				this.useDefault = true;
			} else {
				this.useDefault = false;
			}
			this.thumbnailFile = new java.io.File(this.url);
		} else {
			this.thumbnailFile = null;
		}
	}
	
	/**
	 * set the parent DocumentOnServer Id as String
	 * @param orgFileId
	 */
	public void setOrgFileId(String orgFileId) {
		this.orgFileId = orgFileId;
	}
	
	public boolean isUseDefault() {
		return useDefault;
	}
	
	public void setUseDefault(boolean useDefault) {
		this.useDefault = useDefault;
	}

	/**
	 * @return the thumbnailFile
	 */
	public java.io.File getThumbnailFile() {
		return thumbnailFile;
	}

	/**
	 * @param thumbnailFile the thumbnailFile to set
	 */
	public void setThumbnailFile(java.io.File thumbnailFile) {
		this.thumbnailFile = thumbnailFile;
		if(this.thumbnailFile!=null) {
			this.url =this.thumbnailFile.getPath();
		}
	}

	public String getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(String modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}


	
}
