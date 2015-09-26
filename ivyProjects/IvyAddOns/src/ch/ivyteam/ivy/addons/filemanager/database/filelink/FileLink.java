package ch.ivyteam.ivy.addons.filemanager.database.filelink;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.Time;


public class FileLink extends DocumentOnServer {

	private static final long serialVersionUID = -2639656870025674262L;
	
	//The file link id as defined in the fileLink table
	private long fileLinkId;
	//the file link name
	private String fileLinkName;
	// the directory id that contains the fileLink
	private long directoryId;
	// the file link path. Is not stored in the table but can be retrieved through the directory id.
	private String fileLinkPath;
	// the file content id
	private long contentId;
	// the file version id (see the file version table)
	private long versionId;
	// if the file versioning feature is activated, each fileLink links to a fixed version number
	private int linkedVersionNumber;
	
	private Date linkCreationDate;
	private Time linkCreationTime;
	
	public long getFileLinkId() {
		return fileLinkId;
	}
	
	public void setFileLinkId(long id) {
		this.fileLinkId = id;
	}
	
	public String getFileLinkName() {
		return fileLinkName;
	}
	
	public void setFileLinkName(String name) {
		this.fileLinkName = name;
	}

	public String getFileLinkPath() {
		return fileLinkPath;
	}

	public void setFileLinkPath(String fileLinkPath) {
		this.fileLinkPath = fileLinkPath;
	}

	public long getDirectoryId() {
		return directoryId;
	}
	
	public void setDirectoryId(long directoryId) {
		this.directoryId = directoryId;
	}
	
	public long getContentId() {
		return contentId;
	}
	
	public void setContentId(long contentId) {
		this.contentId = contentId;
	}
	
	public long getVersionId() {
		return versionId;
	}
	
	public void setVersionId(long versionId) {
		this.versionId = versionId;
	}
	
	public boolean isReferenceVersion() {
		return this.versionId > 0;
	}

	public int getLinkedVersionNumber() {
		return linkedVersionNumber;
	}

	public void setLinkedVersionNumber(int linked_version_number) {
		this.linkedVersionNumber = linked_version_number;
	}

	public Date getLinkCreationDate() {
		return linkCreationDate;
	}

	public void setLinkCreationDate(Date linkCreationDate) {
		this.linkCreationDate = linkCreationDate;
	}

	public Time getLinkCreationTime() {
		return linkCreationTime;
	}

	public void setLinkCreationTime(Time linkCreationTime) {
		this.linkCreationTime = linkCreationTime;
	}
	
}
