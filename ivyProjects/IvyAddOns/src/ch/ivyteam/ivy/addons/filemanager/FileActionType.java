package ch.ivyteam.ivy.addons.filemanager;

/**
 */
@SuppressWarnings("all")
@javax.annotation.Generated(comments="This is the java file of the ivy data class FileActionType", value={"ch.ivyteam.ivy.scripting.streamInOut.IvyScriptJavaClassBuilder"})
public class FileActionType extends ch.ivyteam.ivy.scripting.objects.CompositeObject
{
	/** SerialVersionUID */
	private static final long serialVersionUID = -3627669673303517088L;

	public static final short FILE_CREATED_ACTION = 1;
	public static final short FILE_CONTENT_CHANGED_ACTION = 2;
	public static final short FILE_DESCRIPTION_CHANGED_ACTION = 3;
	public static final short FILE_RENAMED_ACTION = 4;
	public static final short FILE_DELETED_ACTION = 5;
	public static final short FILE_DOWNLOADED_ACTION = 6;
	public static final short FILE_PRINTED_ACTION = 7;
	public static final short FILE_OPENED_ACTION = 8;
	public static final short FILE_COPY_PASTE_ACTION = 9;
	public static final short FILE_MOVED_ACTION = 10;
	public static final short FILE_NEW_VERSION_ACTION = 11;
	public static final short FILE_VERSION_ROLLBACK_ACTION = 12;

	/**
	 * the file action type id
	 */
	private transient java.lang.Number id;

	/**
	 * Gets the field id.
	 * @return the value of the field id; may be null.
	 */
	public java.lang.Number getId()
	{
		return id;
	}

	/**
	 * Sets the field id.
	 * @param _id the new value of the field id.
	 */
	public void setId(java.lang.Number _id)
	{
		id = _id;
	}

	/**
	 * the type number (ex: 1 = file created, 2 = file content changed...)
	 */
	private transient java.lang.Number type;

	/**
	 * Gets the field type.
	 * @return the value of the field type; may be null.
	 */
	public java.lang.Number getType()
	{
		return type;
	}

	/**
	 * Sets the field type.
	 * @param _type the new value of the field type.
	 */
	public void setType(java.lang.Number _type)
	{
		type = _type;
	}

	/**
	 * the description of the file action. For the moment it is translated in en, fr, de. 
	 */
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

}
