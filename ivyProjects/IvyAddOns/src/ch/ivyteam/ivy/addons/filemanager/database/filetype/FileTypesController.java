/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.filetype;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileType;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileTypePersistence;
import ch.ivyteam.ivy.addons.filemanager.listener.AbstractFileActionListener;
import ch.ivyteam.ivy.addons.filemanager.listener.FileActionEvent;
import ch.ivyteam.ivy.addons.filemanager.listener.FileTypeActionEvent;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.persistence.PersistencyException;
import ch.ivyteam.ivy.scripting.objects.List;

/**
 * @author Emmanuel Comba<br>
 *         This class allows managing the file types of document on server
 *         objects.<br>
 *         It provides also convenient public methods to manage the file type on
 *         document on server objects.<br>
 *         The fileType Objects are DataClass
 *         ch.ivyteam.ivy.addons.filemanager.FileType
 * 
 */
public class FileTypesController extends AbstractFileTypesController {

	private String tableName = null; // the table that stores file types info
	private String schemaName = null;
	private IFileTypePersistence ftPersistence;

	private ArrayList<AbstractFileActionListener> listeners = new ArrayList<AbstractFileActionListener>();

	/**
	 * Instantiates a new FileTypesController with the given
	 * BasicConfigurationController Object.<br />
	 * The BasicConfigurationController Object contains all the necessary
	 * informations to create a new fileManagerController,<br />
	 * and to connect to the database that stores the file types table.
	 * 
	 * @param _config
	 *            BasicConfigurationController Object
	 * @see ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController
	 * @throws Exception
	 */
	public FileTypesController(BasicConfigurationController _config)
			throws Exception {
		this.ftPersistence = PersistenceConnectionManagerFactory
				.getIFileTypePersistenceInstance(_config);
		if (_config.getFileActionListeners() != null) {
			listeners.addAll(_config.getFileActionListeners());
		}
	}

	@Override
	public FileType createFileType(String name, String optionalApplicationName)
			throws Exception {
		FileType ft = new FileType();
		ft.setApplicationName(optionalApplicationName);
		ft.setFileTypeName(name);
		ft = this.ftPersistence.create(ft);
		this.fireTypeCreatedEvent(ft);
		return ft;
	}

	@Override
	public FileType modifyFileType(FileType ft) throws Exception {
		FileType filetype = null;
		if (ft.getId() == 0) {
			filetype = this.createFileType(ft.getFileTypeName(),
					ft.getApplicationName());
			 this.fireTypeCreatedEvent(filetype);
		}else{
			filetype = this.ftPersistence.update(ft);
			this.fireTypeChangedEvent(filetype);
		}
		return filetype;
	}

	@Override
	public void deleteFileType(long fileTypeId) throws Exception {
		if (fileTypeId == 0) {
			return;
		}
		FileType ft = this.ftPersistence.get(fileTypeId);
		if (ft != null && ft.getId() > 0) {
			this.ftPersistence.delete(ft);
			this.fireTypeDeletedEvent(ft);
		}
	}

	@Override
	public FileType getFileTypeWithId(long fileTypeId)
			throws PersistencyException, SQLException, Exception {
		return this.ftPersistence.get(fileTypeId);
	}

	/**
	 * Returns the FileType corresponding to the given id.<br>
	 * 
	 * @param fileTypeId
	 *            the FileType id
	 * @param con
	 *            the java.sql.Connection object used to communicate with the
	 *            database.<br>
	 *            <b>IMPORTANT: </b>This method does NOT release this
	 *            Connection. The method that calls this method has to do it.
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 * @throws PersistencyException
	 */
	public FileType getFileTypeWithId(long fileTypeId, java.sql.Connection con)
			throws PersistencyException, SQLException, Exception {
		return this.ftPersistence.get(fileTypeId);
	}

	@Override
	public FileType getFileTypeWithName(String typeName,
			String optionalApplicationName) throws PersistencyException,
			SQLException, Exception {
		if (optionalApplicationName != null) {
			return this.ftPersistence.get(typeName + "*SEPARATE*"
					+ optionalApplicationName);
		} else {
			return this.ftPersistence.get(typeName);
		}
	}

	@Override
	public java.util.List<FileType> getFileTypesWithAppName(
			String applicationName) throws PersistencyException, SQLException,
			Exception {
		return this.ftPersistence.getFileTypesWithAppName(applicationName);
	}

	@Override
	public java.util.List<FileType> getAllFileTypes()
			throws PersistencyException, SQLException, Exception {
		return this.ftPersistence.getAllFileTypes();
	}

	@Override
	public java.util.List<DocumentOnServer> getDocumentsWithFileTypeName(
			String typeName, String optionalApplicationName) throws Exception {
		return this.getDocumentsWithFileTypeId(this.getFileTypeWithName(
				typeName, optionalApplicationName).getId());
	}

	@Override
	public java.util.List<DocumentOnServer> getDocumentsWithApplicationName(
			String applicationName) throws Exception {
		List<DocumentOnServer> docs = List.create(DocumentOnServer.class);
		java.util.List<FileType> ftl = this
				.getFileTypesWithAppName(applicationName);
		for (FileType ft : ftl) {
			docs.addAll(this.getDocumentsWithFileTypeId(ft.getId()));
		}
		return docs;
	}

	@Override
	public java.util.List<DocumentOnServer> getDocumentsWithFileTypeId(
			long typeId) throws Exception {
		return this.ftPersistence.getDocumentsWithFileTypeId(typeId);
	}

	@Override
	public java.util.List<DocumentOnServer> completeDocumentsWithFileTypes(
			java.util.List<DocumentOnServer> _docs) {
		if (_docs == null) {
			return null;
		}
		for (DocumentOnServer doc : _docs) {
			if (doc.getFileType().getId() > 0) {
				try {
					doc.setFileType(this.getFileTypeWithId(doc.getFileType()
							.getId()));
				} catch (Exception ex) {
					// do nothing
				}
			}
		}
		return _docs;
	}

	@Override
	public DocumentOnServer setDocumentFileType(DocumentOnServer doc,
			long fileTypeId) throws Exception {
		Ivy.log().info("Setting the filetype for the document: {0} {1}",doc.getFileID(),fileTypeId);
		this.ftPersistence.setDocumentFileType(doc, fileTypeId);
		this.fireChangedEvent(doc);
		return doc;
	}

	@Override
	@Deprecated
	public DocumentOnServer setDocumentFileType(DocumentOnServer doc,
			long fileTypeId, java.sql.Connection con) throws Exception {
		return this.setDocumentFileType(doc, fileTypeId);
	}
	
	private synchronized void fireChangedEvent(DocumentOnServer doc) {
		FileActionEvent event = new FileActionEvent(doc);
		Iterator<AbstractFileActionListener> i = listeners.iterator();
		while(i.hasNext())  {
			i.next().fileChanged(event);
		}
	}
	
	private synchronized void fireTypeCreatedEvent(FileType ft) {
		FileTypeActionEvent event = new FileTypeActionEvent(ft);
		Iterator<AbstractFileActionListener> i = listeners.iterator();
		while(i.hasNext())  {
			i.next().fileTypeCreated(event);
		}
	}
	
	private synchronized void fireTypeChangedEvent(FileType ft) {
		FileTypeActionEvent event = new FileTypeActionEvent(ft);
		Iterator<AbstractFileActionListener> i = listeners.iterator();
		while(i.hasNext())  {
			i.next().fileTypeChanged(event);
		}
	}
	
	private synchronized void fireTypeDeletedEvent(FileType ft) {
		FileTypeActionEvent event = new FileTypeActionEvent(ft);
		Iterator<AbstractFileActionListener> i = listeners.iterator();
		while(i.hasNext())  {
			i.next().fileTypeDeleted(event);
		}
	}

	/**
	 * @return the tableName
	 */
	@Deprecated
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName
	 *            the tableName to set
	 */
	@Deprecated
	public void setTableName(String tableName) {

	}

	/**
	 * @return the schemaName
	 */
	@Deprecated
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * @param schemaName
	 *            the schemaName to set
	 */
	@Deprecated
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
}
