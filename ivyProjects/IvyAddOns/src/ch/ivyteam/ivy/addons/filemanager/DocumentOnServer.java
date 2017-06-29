package ch.ivyteam.ivy.addons.filemanager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * The dataclass DocumentOnServer represents a java file uploaded by a user on the server.
 * Its elements are the fields found in the DB table used to store the FIles'informations.
 */
@SuppressWarnings("all")
@javax.annotation.Generated(comments="This is the java file of the ivy data class DocumentOnServer", value={"ch.ivyteam.ivy.scripting.streamInOut.IvyScriptJavaClassBuilder"})
public class DocumentOnServer extends ch.ivyteam.ivy.scripting.objects.CompositeObject implements Comparable<DocumentOnServer>
{
	/** SerialVersionUID */
	private static final long serialVersionUID = -7480693556558364577L;

	/**
	 * String representation of the File ID
	 */
	private transient java.lang.String fileID;

	/**
	 * Gets the field fileID.
	 * @return the value of the field fileID; may be null.
	 */
	public java.lang.String getFileID() {
		return fileID;
	}

	/**
	 * Sets the field fileID.
	 * @param _fileID the new value of the field fileID.
	 */
	public void setFileID(java.lang.String _fileID){
		fileID = _fileID;
		if(StringUtils.isBlank(fileID)){
			id = 0L;
		} else {
			try {
				id = Long.parseLong(fileID);
			} catch (NumberFormatException ex) {
				ch.ivyteam.ivy.environment.Ivy.log().warn("The long numeric id field of the documentOnServer could not be set with the given fileId: " + fileID);
			}
		}
	}

	/**
	 * path on the server
	 */
	private transient java.lang.String path;

	/**
	 * Gets the field path.
	 * @return the value of the field path; may be null.
	 */
	public java.lang.String getPath() {
		return path;
	}

	/**
	 * Sets the field path.
	 * @param _path the new value of the field path.
	 */
	public void setPath(java.lang.String _path) {
		path = _path;
	}

	/**
	 * name of the file
	 */
	private transient java.lang.String filename;

	/**
	 * Gets the field filename.
	 * @return the value of the field filename; may be null.
	 */
	public java.lang.String getFilename() {
		return filename;
	}

	/**
	 * Sets the field filename.
	 * @param _filename the new value of the field filename.
	 */
	public void setFilename(java.lang.String _filename) {
		filename = _filename;
	}

	/**
	 * size of the file
	 */
	private transient java.lang.String fileSize;

	/**
	 * Gets the field fileSize.
	 * @return the value of the field fileSize; may be null.
	 */
	public java.lang.String getFileSize() {
		return fileSize;
	}

	/**
	 * Sets the field fileSize.
	 * @param _fileSize the new value of the field fileSize.
	 */
	public void setFileSize(java.lang.String _fileSize) {
		fileSize = _fileSize;
	}

	/**
	 * User who created the file
	 */
	private transient java.lang.String userID;

	/**
	 * Gets the field userID.
	 * @return the value of the field userID; may be null.
	 */
	public java.lang.String getUserID() {
		return userID;
	}

	/**
	 * Sets the field userID.
	 * @param _userID the new value of the field userID.
	 */
	public void setUserID(java.lang.String _userID) {
		userID = _userID;
	}

	/**
	 * Date of creation represented as a String
	 */
	private transient java.lang.String creationDate;

	/**
	 * Gets the field creationDate.
	 * @return the value of the field creationDate; may be null.
	 */
	public java.lang.String getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the field creationDate.
	 * @param _creationDate the new value of the field creationDate.
	 */
	public void setCreationDate(java.lang.String _creationDate) {
		creationDate = _creationDate;
	}

	/**
	 * time of creation represented as a String
	 */
	private transient java.lang.String creationTime;

	/**
	 * Gets the field creationTime.
	 * @return the value of the field creationTime; may be null.
	 */
	public java.lang.String getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the field creationTime.
	 * @param _creationTime the new value of the field creationTime.
	 */
	public void setCreationTime(java.lang.String _creationTime) {
		creationTime = _creationTime;
	}

	/**
	 * User who modified the File at last
	 */
	private transient java.lang.String modificationUserID;

	/**
	 * Gets the field modificationUserID.
	 * @return the value of the field modificationUserID; may be null.
	 */
	public java.lang.String getModificationUserID() {
		return modificationUserID;
	}

	/**
	 * Sets the field modificationUserID.
	 * @param _modificationUserID the new value of the field modificationUserID.
	 */
	public void setModificationUserID(java.lang.String _modificationUserID) {
		modificationUserID = _modificationUserID;
	}

	/**
	 * Date of last Modification  represented as a String
	 */
	private transient java.lang.String modificationDate;

	/**
	 * Gets the field modificationDate.
	 * @return the value of the field modificationDate; may be null.
	 */
	public java.lang.String getModificationDate() {
		return modificationDate;
	}

	/**
	 * Sets the field modificationDate.
	 * @param _modificationDate the new value of the field modificationDate.
	 */
	public void setModificationDate(java.lang.String _modificationDate) {
		modificationDate = _modificationDate;
	}

	/**
	 * Time of last Modification  represented as a String
	 */
	private transient java.lang.String modificationTime;

	/**
	 * Gets the field modificationTime.
	 * @return the value of the field modificationTime; may be null.
	 */
	public java.lang.String getModificationTime() {
		return modificationTime;
	}

	/**
	 * Sets the field modificationTime.
	 * @param _modificationTime the new value of the field modificationTime.
	 */
	public void setModificationTime(java.lang.String _modificationTime) {
		modificationTime = _modificationTime;
	}

	/**
	 * Tells if the file is currently edited by an another user
	 */
	private transient java.lang.String locked;

	/**
	 * Gets the field locked.
	 * @return the value of the field locked; may be null.
	 */
	public java.lang.String getLocked() {
		return locked;
	}

	/**
	 * Sets the field locked.
	 * @param _locked the new value of the field locked.
	 */
	public void setLocked(java.lang.String _locked) {
		locked = _locked;
	}

	/**
	 * The user who currently edits this File
	 */
	private transient java.lang.String lockingUserID;

	/**
	 * Gets the field lockingUserID.
	 * @return the value of the field lockingUserID; may be null.
	 */
	public java.lang.String getLockingUserID() {
		return lockingUserID;
	}

	/**
	 * Sets the field lockingUserID.
	 * @param _lockingUserID the new value of the field lockingUserID.
	 */
	public void setLockingUserID(java.lang.String _lockingUserID) {
		lockingUserID = _lockingUserID;
	}

	/**
	 * The description of the document
	 */
	private transient java.lang.String description;

	/**
	 * Gets the field description.
	 * @return the value of the field description; may be null.
	 */
	public java.lang.String getDescription() {
		return description;
	}

	/**
	 * Sets the field description.
	 * @param _description the new value of the field description.
	 */
	public void setDescription(java.lang.String _description) {
		description = _description;
	}

	/**
	 * The file Extension
	 */
	private transient java.lang.String extension;

	/**
	 * Gets the field extension.
	 * @return the value of the field extension; may be null.
	 */
	public java.lang.String getExtension() {
		return extension;
	}

	/**
	 * Sets the field extension.
	 * @param _extension the new value of the field extension.
	 */
	public void setExtension(java.lang.String _extension) {
		extension = _extension;
	}

	/**
	 * ivy File to be able to edit the corresponding File in case of DB storage.
	 */
	private transient ch.ivyteam.ivy.scripting.objects.File ivyFile;

	/**
	 * Gets the field ivyFile.
	 * @return the value of the field ivyFile; may be null.
	 */
	public ch.ivyteam.ivy.scripting.objects.File getIvyFile() {
		return ivyFile;
	}

	/**
	 * Sets the field ivyFile.
	 * @param _ivyFile the new value of the field ivyFile.
	 */
	public void setIvyFile(ch.ivyteam.ivy.scripting.objects.File _ivyFile) {
		ivyFile = _ivyFile;
	}

	/**
	 * java io File, edit a physically stored File
	 */
	private transient java.io.File javaFile;

	/**
	 * Gets the field javaFile.
	 * @return the value of the field javaFile; may be null.
	 */
	public java.io.File getJavaFile() {
		return javaFile;
	}

	/**
	 * Sets the field javaFile.
	 * @param _javaFile the new value of the field javaFile.
	 */
	public void setJavaFile(java.io.File _javaFile) {
		javaFile = _javaFile;
	}

	/**
	 * if versioning is activated, this is the document version number.
	 */
	private transient java.lang.Number versionnumber;

	/**
	 * Gets the field versionnumber.
	 * @return the value of the field versionnumber; may be null.
	 */
	public java.lang.Number getVersionnumber() {
		return versionnumber;
	}

	/**
	 * Sets the field versionnumber.
	 * @param _versionnumber the new value of the field versionnumber.
	 */
	public void setVersionnumber(java.lang.Number _versionnumber) {
		versionnumber = _versionnumber;
	}

	/**
	 * The FileType if this is implemented. The FileType.id is stored in the file meta data table. Is null if this feature is not implemented.
	 */
	private transient ch.ivyteam.ivy.addons.filemanager.FileType fileType;

	/**
	 * Gets the field fileType.
	 * @return the value of the field fileType; may be null.
	 */
	public ch.ivyteam.ivy.addons.filemanager.FileType getFileType() {
		return fileType;
	}

	/**
	 * Sets the field fileType.
	 * @param _fileType the new value of the field fileType.
	 */
	public void setFileType(ch.ivyteam.ivy.addons.filemanager.FileType _fileType) {
		fileType = _fileType;
	}

	/**
	 * Used as internal flag for history tracking.
	 */
	private transient java.lang.Boolean editedInSession;

	/**
	 * Gets the field editedInSession.
	 * @return the value of the field editedInSession; may be null.
	 */
	public java.lang.Boolean getEditedInSession() {
		return editedInSession;
	}

	/**
	 * Sets the field editedInSession.
	 * @param _editedInSession the new value of the field editedInSession.
	 */
	public void setEditedInSession(java.lang.Boolean _editedInSession) {
		editedInSession = _editedInSession;
	}

	/**
	 * true if user has the right to delete the file
	 */
	private transient java.lang.Boolean canUserDelete;

	/**
	 * Gets the field canUserDelete.
	 * @return the value of the field canUserDelete; may be null.
	 */
	public java.lang.Boolean getCanUserDelete() {
		return canUserDelete;
	}

	/**
	 * Sets the field canUserDelete.
	 * @param _canUserDelete the new value of the field canUserDelete.
	 */
	public void setCanUserDelete(java.lang.Boolean _canUserDelete) {
		canUserDelete = _canUserDelete;
	}

	/**
	 * true if user has the right to update the file (Content write, rename, description change)
	 */
	private transient java.lang.Boolean canUserWrite;

	/**
	 * Gets the field canUserWrite.
	 * @return the value of the field canUserWrite; may be null.
	 */
	public java.lang.Boolean getCanUserWrite() {
		return canUserWrite;
	}

	/**
	 * Sets the field canUserWrite.
	 * @param _canUserWrite the new value of the field canUserWrite.
	 */
	public void setCanUserWrite(java.lang.Boolean _canUserWrite) {
		canUserWrite = _canUserWrite;
	}

	/**
	 * true if user has the right to read the file
	 */
	private transient java.lang.Boolean canUserRead;

	/**
	 * Gets the field canUserRead.
	 * @return the value of the field canUserRead; may be null.
	 */
	public java.lang.Boolean getCanUserRead() {
		return canUserRead;
	}

	/**
	 * Sets the field canUserRead.
	 * @param _canUserRead the new value of the field canUserRead.
	 */
	public void setCanUserRead(java.lang.Boolean _canUserRead) {
		canUserRead = _canUserRead;
	}

	/**
	 * Flag set to true if the doc is locked.
	 */
	private transient java.lang.Boolean isLocked;

	/**
	 * Gets the field isLocked.
	 * @return the value of the field isLocked; may be null.
	 */
	public java.lang.Boolean getIsLocked() {
		return isLocked;
	}

	/**
	 * Sets the field isLocked.
	 * @param _isLocked the new value of the field isLocked.
	 */
	public void setIsLocked(java.lang.Boolean _isLocked) {
		isLocked = _isLocked;
	}

	/**
	 * used for displaying the path, especially if the directories are translated.
	 */
	private transient java.lang.String displayedPath;

	/**
	 * Gets the field displayedPath.
	 * @return the value of the field displayedPath; may be null.
	 */
	public java.lang.String getDisplayedPath() {
		return displayedPath;
	}

	/**
	 * Sets the field displayedPath.
	 * @param _displayedPath the new value of the field displayedPath.
	 */
	public void setDisplayedPath(java.lang.String _displayedPath) {
		displayedPath = _displayedPath;
	}

	private transient java.lang.Long id;

	/**
	 * Gets the field id.
	 * @return the value of the field id; may be null.
	 */
	public java.lang.Long getId() {
		return id;
	}

	/**
	 * Sets the field id.
	 * @param _id the new value of the field id.
	 */
	public void setId(java.lang.Long _id) {
		id = _id;
	}

	/**
	 * store the thumbnail in some cases.
	 */
	private transient ch.ivyteam.ivy.addons.filemanager.html.table.model.ThumbnailData thumbnail;

	/**
	 * Gets the field thumbnail.
	 * @return the value of the field thumbnail; may be null.
	 */
	public ch.ivyteam.ivy.addons.filemanager.html.table.model.ThumbnailData getThumbnail() {
		return thumbnail;
	}

	/**
	 * Sets the field thumbnail.
	 * @param _thumbnail the new value of the field thumbnail.
	 */
	public void setThumbnail(ch.ivyteam.ivy.addons.filemanager.html.table.model.ThumbnailData _thumbnail) {
		thumbnail = _thumbnail;
	}
	
	private transient List<FileTag> tags;
	
	public void setTags(List<FileTag> _tags) {
		this.tags = _tags;
	}
	
	public List<FileTag> getTags() {
		return tags;
	}
	
	public void addTags(List<FileTag> tags) {
		if(this.tags == null) {
			this.tags = new ArrayList<>();
		}
		this.tags.addAll(tags);
	}
	
	public void addTag(FileTag tag) {
		if(this.tags == null) {
			this.tags = new ArrayList<>();
		}
		this.tags.add(tag);
	}

	@Override
	public int compareTo(DocumentOnServer other) {
		if(other == null) {
			return +1;
		}
		
		if(this.id <= 0 && !StringUtils.isBlank(this.fileID)) {
			this.id = Long.parseLong(this.fileID);
		}
		
		if(other.id <= 0 && !StringUtils.isBlank(other.fileID)) {
			other.id = Long.parseLong(other.fileID);
		}
		
		if(this.id > other.id) {
			return +1;
		}
		
		if(this.id < other.id) {
			return -1;
		}
		
		return 0;
	}

}
