/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.ReturnedMessage;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.configuration.IvyGlobalVariableHandler;
import ch.ivyteam.ivy.addons.filemanager.database.versioning.FileVersioningController;
import ch.ivyteam.ivy.addons.filemanager.listener.AbstractFileActionListener;
import ch.ivyteam.ivy.addons.filemanager.listener.FileActionEvent;



/**
 * @author ec
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(IvyGlobalVariableHandler.class)
public class FileStoreDBHandlerRollBackToPreviousVersionTest {
	
	BasicConfigurationController config = null;
	@Before
	public void prepareMock() {
		mockStatic(IvyGlobalVariableHandler.class);
		when(IvyGlobalVariableHandler.getGlobalVariable(any(String.class))).thenReturn("0");
		when(IvyGlobalVariableHandler.isGlobalVariableEqualTo(any(String.class), any(String.class))).thenReturn(false);
		when(IvyGlobalVariableHandler.returnsGlobalVariableValueIfProposedValueIsBlank(any(String.class), any(String.class))).thenReturn("0");
		config = new BasicConfigurationController();
	}
	
	@Test
	public void rollBackToPreviousVersionVersioningActivatedWithUnlockedDocumentSuccess() throws Exception{
		
		FileVersioningController mockedFileVersioningController = mock(FileVersioningController.class);
		when(mockedFileVersioningController.rollbackLastVersionAsActiveDocument(1)).thenReturn(true);
		AbstractFileActionListener mockedAbstractFileActionListener = mock(AbstractFileActionListener.class);
		
		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedFileVersioningController);
		fileHandler.addFileEventListener(mockedAbstractFileActionListener);
		fileHandler.setFileVersioningController(mockedFileVersioningController);
		fileHandler.setActivateFileVersioning(true);
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		when(mockedDocPersistence.get(any(Long.class))).thenReturn(new DocumentOnServer());
		fileHandler.setDocPersistence(mockedDocPersistence);
		
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("1");
		doc.setFilename("test.pdf");
		doc.setPath("root/test/test.pdf");
		doc.setLocked("0");
		doc.setVersionnumber(4);

		ReturnedMessage m = fileHandler.rollBackToPreviousVersion(doc);
		verify(mockedAbstractFileActionListener, times(1)).fileVersionRollbacked((FileActionEvent) anyObject());
		assertTrue(m.getType()==FileHandler.SUCCESS_MESSAGE);
	}
	
	@Test
	public void rollBackToPreviousVersionVersioningActivatedWithlockedDocumentFailed() throws Exception{
		
		FileVersioningController mockedFileVersioningController = mock(FileVersioningController.class);
		when(mockedFileVersioningController.rollbackLastVersionAsActiveDocument(1)).thenReturn(true);
		
		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedFileVersioningController);
		fileHandler.addFileEventListener(new MyFileActionListener());
		fileHandler.setFileVersioningController(mockedFileVersioningController);
		fileHandler.setActivateFileVersioning(true);
		
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("1");
		doc.setFilename("test.pdf");
		doc.setPath("root/test/test.pdf");
		doc.setLocked("1");
		doc.setLockingUserID("ec");
		doc.setVersionnumber(4);

		ReturnedMessage m = fileHandler.rollBackToPreviousVersion(doc);
		
		assertTrue(m.getType()==FileHandler.ERROR_MESSAGE);
	}
	
	@Test
	public void rollBackToPreviousVersionWithNoVersioningActivatedFailed() throws Exception{
		FileVersioningController mockedFileVersioningController = mock(FileVersioningController.class);
		when(mockedFileVersioningController.rollbackLastVersionAsActiveDocument(1)).thenReturn(true);
		
		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedFileVersioningController);
		fileHandler.addFileEventListener(new MyFileActionListener());
		fileHandler.setFileVersioningController(mockedFileVersioningController);
		fileHandler.setActivateFileVersioning(false);
		
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("1");
		doc.setFilename("test.pdf");
		doc.setPath("root/test/test.pdf");
		doc.setLocked("0");
		doc.setVersionnumber(4);

		ReturnedMessage m = fileHandler.rollBackToPreviousVersion(doc);
		assertTrue(m.getType()==FileHandler.ERROR_MESSAGE);
	}
	
	@Test
	public void rollBackToPreviousVersionWithVersioningActivatedVersionControllerReturnFalseFailed() throws Exception{

		FileVersioningController mockedFileVersioningController = mock(FileVersioningController.class);
		when(mockedFileVersioningController.rollbackLastVersionAsActiveDocument(1)).thenReturn(false);
		
		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedFileVersioningController);
		fileHandler.addFileEventListener(new MyFileActionListener());
		fileHandler.setFileVersioningController(mockedFileVersioningController);
		fileHandler.setActivateFileVersioning(true);
		
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("1");
		doc.setFilename("test.pdf");
		doc.setPath("root/test/test.pdf");
		doc.setLocked("0");
		doc.setVersionnumber(4);

		ReturnedMessage m = fileHandler.rollBackToPreviousVersion(doc);
		
		assertTrue(m.getType()==FileHandler.ERROR_MESSAGE);
	}
	
	@Test
	public void rollBackToPreviousVersionWithDocumentWithoutVersionFailedWithInformationMessage() throws Exception{

		FileVersioningController mockedFileVersioningController = mock(FileVersioningController.class);
		when(mockedFileVersioningController.rollbackLastVersionAsActiveDocument(1)).thenReturn(true);
		
		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedFileVersioningController);
		fileHandler.addFileEventListener(new MyFileActionListener());
		fileHandler.setFileVersioningController(mockedFileVersioningController);
		fileHandler.setActivateFileVersioning(true);
		
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("1");
		doc.setFilename("test.pdf");
		doc.setPath("root/test/test.pdf");
		doc.setLocked("0");
		doc.setVersionnumber(1);

		ReturnedMessage m = fileHandler.rollBackToPreviousVersion(doc);
		
		assertTrue(m.getType()==FileHandler.INFORMATION_MESSAGE);
	}

}
