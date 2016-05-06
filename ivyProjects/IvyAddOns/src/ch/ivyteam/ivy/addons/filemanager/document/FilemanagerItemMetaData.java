/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.document;

import java.io.Serializable;

import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.Time;

/**
 * @author ec
 * This POJO contains meta data information that can be used for filemanager items (Folders, Documents...)
 */
public class FilemanagerItemMetaData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4774452104544396997L;
	
	private Date creationDate;
	private Time creationTime;
	private String creationUserId;
	private Date modificationDate;
	private Time modificationTime;
	private String modificationUserId;
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Time getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Time creationTime) {
		this.creationTime = creationTime;
	}
	public String getCreationUserId() {
		if(this.creationUserId == null) {
			this.creationUserId="";
		}
		return creationUserId.trim();
	}
	public void setCreationUserId(String creationUserId) {
		this.creationUserId = creationUserId;
	}
	public Date getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}
	public Time getModificationTime() {
		return modificationTime;
	}
	public void setModificationTime(Time modificationTime) {
		this.modificationTime = modificationTime;
	}
	public String getModificationUserId() {
		if(this.modificationUserId == null) {
			this.modificationUserId="";
		}
		return modificationUserId.trim();
	}
	public void setModificationUserId(String modificationUserId) {
		this.modificationUserId = modificationUserId;
	}
}
