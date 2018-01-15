package ch.ivyteam.ivy.addons.filemanager;

import ch.ivyteam.ivy.scripting.objects.CompositeObject;
import ch.ivyteam.ivy.scripting.objects.List;

public class ReturnedMessage extends CompositeObject {
	
	public final static int SUCCESS_MESSAGE = 1;
	public final static int ERROR_MESSAGE = 2;
	public final static int INFORMATION_MESSAGE = 3;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3914854183693921867L;

	/**
	 * Type of the message : 1 = SUCCESS_MESSAGE, 2 = ERROR_MESSAGE, 3 and other = INFORMATION_MESSAGE
	 */
	private transient java.lang.Integer type;
	
	/**
	 * the message
	 */
	private transient java.lang.String text;
	/**
	 * File concerned by the action. Can be null
	 */
	private transient java.io.File file;
	/**
	 * List of java.io.File concerned by the action
	 */
	private transient ch.ivyteam.ivy.scripting.objects.List<java.io.File> files;
	/**
	 * List<ch.ivyteam.ivy.addons.filemanager.DocumentOnServer> concerned by the action 
	 */
	private transient ch.ivyteam.ivy.scripting.objects.List<ch.ivyteam.ivy.addons.filemanager.DocumentOnServer> documentOnServers;
	/**
	 * A second List of java.io.File concerned by the action
	 */
	private transient ch.ivyteam.ivy.scripting.objects.List<java.io.File> files2;
	/**
	 * Type of the action reported.
	 */
	private transient java.lang.Integer actionType;
	/**
	 * This field can be used to return several coupled files. Ex: a file should be moved: oldFile/newFile etc...
	 */
	private transient java.util.HashMap<java.io.File,java.io.File> hashMapFiles;
	/**
	 * can be used to propagate an exception
	 */
	private transient java.lang.Throwable exception;
	
	public ReturnedMessage() { this("", SUCCESS_MESSAGE); }
	
	public ReturnedMessage(String message, int message_type) {
		this.setText(message);
		this.setType(message_type);
		this.setFiles(List.create(java.io.File.class));
		this.setFiles2(List.create(java.io.File.class));
		this.setDocumentOnServers(List.create(DocumentOnServer.class));
	}

	/**
	 * Gets the field type.
	 * @return the value of the field type; may be null.
	 */
	public java.lang.Integer getType()
	{
		return type;
	}

	/**
	 * Sets the field type.
	 * @param _type the new value of the field type.
	 */
	public void setType(java.lang.Integer _type)
	{
		type = _type;
	}

	/**
	 * Gets the field text.
	 * @return the value of the field text; may be null.
	 */
	public java.lang.String getText()
	{
		return text;
	}

	/**
	 * Sets the field text.
	 * @param _text the new value of the field text.
	 */
	public void setText(java.lang.String _text)
	{
		text = _text;
	}
	
	/**
	 * Gets the field file.
	 * @return the value of the field file; may be null.
	 */
	public java.io.File getFile()
	{
		return file;
	}

	/**
	 * Sets the field file.
	 * @param _file the new value of the field file.
	 */
	public void setFile(java.io.File _file)
	{
		file = _file;
	}

	/**
	 * Gets the field files.
	 * @return the value of the field files; may be null.
	 */
	public ch.ivyteam.ivy.scripting.objects.List<java.io.File> getFiles()
	{
		return files;
	}

	/**
	 * Sets the field files.
	 * @param _files the new value of the field files.
	 */
	public void setFiles(ch.ivyteam.ivy.scripting.objects.List<java.io.File> _files)
	{
		files = _files;
	}

	/**
	 * Gets the field documentOnServers.
	 * @return the value of the field documentOnServers; may be null.
	 */
	public ch.ivyteam.ivy.scripting.objects.List<ch.ivyteam.ivy.addons.filemanager.DocumentOnServer> getDocumentOnServers()
	{
		return documentOnServers;
	}

	/**
	 * Sets the field documentOnServers.
	 * @param _documentOnServers the new value of the field documentOnServers.
	 */
	public void setDocumentOnServers(ch.ivyteam.ivy.scripting.objects.List<ch.ivyteam.ivy.addons.filemanager.DocumentOnServer> _documentOnServers)
	{
		documentOnServers = _documentOnServers;
	}

	/**
	 * DocumentOnServer concerned by the action.
	 */
	private transient ch.ivyteam.ivy.addons.filemanager.DocumentOnServer documentOnServer;

	/**
	 * Gets the field documentOnServer.
	 * @return the value of the field documentOnServer; may be null.
	 */
	public ch.ivyteam.ivy.addons.filemanager.DocumentOnServer getDocumentOnServer()
	{
		return documentOnServer;
	}

	/**
	 * Sets the field documentOnServer.
	 * @param _documentOnServer the new value of the field documentOnServer.
	 */
	public void setDocumentOnServer(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer _documentOnServer)
	{
		documentOnServer = _documentOnServer;
	}

	/**
	 * Gets the field files2.
	 * @return the value of the field files2; may be null.
	 */
	public ch.ivyteam.ivy.scripting.objects.List<java.io.File> getFiles2()
	{
		return files2;
	}

	/**
	 * Sets the field files2.
	 * @param _files2 the new value of the field files2.
	 */
	public void setFiles2(ch.ivyteam.ivy.scripting.objects.List<java.io.File> _files2)
	{
		files2 = _files2;
	}

	/**
	 * Gets the field actionType.
	 * @return the value of the field actionType; may be null.
	 */
	public java.lang.Integer getActionType()
	{
		return actionType;
	}

	/**
	 * Sets the field actionType.
	 * @param _actionType the new value of the field actionType.
	 */
	public void setActionType(java.lang.Integer _actionType)
	{
		actionType = _actionType;
	}

	private transient java.lang.Number panelId;

	/**
	 * Gets the field panelId.
	 * @return the value of the field panelId; may be null.
	 */
	public java.lang.Number getPanelId()
	{
		return panelId;
	}

	/**
	 * Sets the field panelId.
	 * @param _panelId the new value of the field panelId.
	 */
	public void setPanelId(java.lang.Number _panelId)
	{
		panelId = _panelId;
	}

	/**
	 * Gets the field hashMapFiles.
	 * @return the value of the field hashMapFiles; may be null.
	 */
	public java.util.HashMap<java.io.File,java.io.File> getHashMapFiles()
	{
		return hashMapFiles;
	}

	/**
	 * Sets the field hashMapFiles.
	 * @param _hashMapFiles the new value of the field hashMapFiles.
	 */
	public void setHashMapFiles(java.util.HashMap<java.io.File,java.io.File> _hashMapFiles)
	{
		hashMapFiles = _hashMapFiles;
	}

	/**
	 * Gets the field exception.
	 * @return the value of the field exception; may be null.
	 */
	public java.lang.Throwable getException()
	{
		return exception;
	}

	/**
	 * Sets the field exception.
	 * @param _exception the new value of the field exception.
	 */
	public void setException(java.lang.Throwable _exception)
	{
		exception = _exception;
	}

}
