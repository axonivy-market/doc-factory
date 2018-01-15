package ch.ivyteam.ivy.addons.filemanager;

/**
 * A File Action represents an Action that was performed on a File by a user. These actions are tracked and stored in the file manager if the fileActionHistory feature is activated.
 */
@SuppressWarnings("all")
@javax.annotation.Generated(comments="This is the java file of the ivy data class FileAction", value={"ch.ivyteam.ivy.scripting.streamInOut.IvyScriptJavaClassBuilder"})
public class FileAction extends ch.ivyteam.ivy.scripting.objects.CompositeObject
{
	/** SerialVersionUID */
	private static final long serialVersionUID = -8283373558609943413L;

	private transient java.lang.Long id;

	/**
	 * Gets the field id.
	 * @return the value of the field id; may be null.
	 */
	public java.lang.Long getId()
	{
		return id;
	}

	/**
	 * Sets the field id.
	 * @param _id the new value of the field id.
	 */
	public void setId(java.lang.Long _id)
	{
		id = _id;
	}

	private transient java.lang.Long fileid;

	/**
	 * Gets the field fileid.
	 * @return the value of the field fileid; may be null.
	 */
	public java.lang.Long getFileid()
	{
		return fileid;
	}

	/**
	 * Sets the field fileid.
	 * @param _fileid the new value of the field fileid.
	 */
	public void setFileid(java.lang.Long _fileid)
	{
		fileid = _fileid;
	}

	private transient java.lang.String username;

	/**
	 * Gets the field username.
	 * @return the value of the field username; may be null.
	 */
	public java.lang.String getUsername()
	{
		return username;
	}

	/**
	 * Sets the field username.
	 * @param _username the new value of the field username.
	 */
	public void setUsername(java.lang.String _username)
	{
		username = _username;
	}

	private transient java.lang.String desc;

	/**
	 * Gets the field desc.
	 * @return the value of the field desc; may be null.
	 */
	public java.lang.String getDesc()
	{
		return desc;
	}

	/**
	 * Sets the field desc.
	 * @param _desc the new value of the field desc.
	 */
	public void setDesc(java.lang.String _desc)
	{
		desc = _desc;
	}

	private transient java.lang.String fullUsername;

	/**
	 * Gets the field fullUsername.
	 * @return the value of the field fullUsername; may be null.
	 */
	public java.lang.String getFullUsername()
	{
		return fullUsername;
	}

	/**
	 * Sets the field fullUsername.
	 * @param _fullUsername the new value of the field fullUsername.
	 */
	public void setFullUsername(java.lang.String _fullUsername)
	{
		fullUsername = _fullUsername;
	}

	private transient ch.ivyteam.ivy.scripting.objects.Date date;

	/**
	 * Gets the field date.
	 * @return the value of the field date; may be null.
	 */
	public ch.ivyteam.ivy.scripting.objects.Date getDate()
	{
		return date;
	}

	/**
	 * Sets the field date.
	 * @param _date the new value of the field date.
	 */
	public void setDate(ch.ivyteam.ivy.scripting.objects.Date _date)
	{
		date = _date;
	}

	private transient ch.ivyteam.ivy.scripting.objects.Time time;

	/**
	 * Gets the field time.
	 * @return the value of the field time; may be null.
	 */
	public ch.ivyteam.ivy.scripting.objects.Time getTime()
	{
		return time;
	}

	/**
	 * Sets the field time.
	 * @param _time the new value of the field time.
	 */
	public void setTime(ch.ivyteam.ivy.scripting.objects.Time _time)
	{
		time = _time;
	}

	private transient java.lang.String info;

	/**
	 * Gets the field info.
	 * @return the value of the field info; may be null.
	 */
	public java.lang.String getInfo()
	{
		return info;
	}

	/**
	 * Sets the field info.
	 * @param _info the new value of the field info.
	 */
	public void setInfo(java.lang.String _info)
	{
		info = _info;
	}

	/**
	 * The action type
	 */
	private transient FileActionType fileActionType;

	/**
	 * Gets the field fileActionType.
	 * @return the value of the field fileActionType; may be null.
	 */
	public FileActionType getFileActionType() {
		return fileActionType;
	}

	/**
	 * Sets the field fileActionType.
	 * @param _fileActionType the new value of the field fileActiontype.
	 */
	public void setFileActionType(FileActionType _fileActionType) {
		this.fileActionType = _fileActionType;
	}



}
