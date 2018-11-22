package ch.ivyteam.ivy.addons.filemanager.database;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.ReturnedMessage;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityHandler;
import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityResponse;
import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityRightsEnum;
import ch.ivyteam.ivy.addons.filemanager.ivy.implemented.MyCMS;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.IWorkflowSession;
@RunWith(PowerMockRunner.class)
@PrepareForTest(Ivy.class)
public class FileStoreDbHandlerRenameDirectory {

	@Test
	public void testRenameDirectoryWithNullParameters_ErrorMessageReturned() {
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateSecurity()).thenReturn(false);
		
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		FolderOnServerSQLPersistence mockedDirPersistence =  mock(FolderOnServerSQLPersistence.class);
		
		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedConfig,mockedDocPersistence,mockedDirPersistence);
		ReturnedMessage message = new ReturnedMessage();
		try {
			message = fileHandler.renameDirectory(null, null);
		} catch (Exception e) {
			
		}
		assertTrue(message.getType()==FileHandler.ERROR_MESSAGE);
	}
	
	@Test
	public void testRenameDirectory_goalDirectoryExists_ErrorMessageReturned() throws Exception {
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateSecurity()).thenReturn(false);
		
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		FolderOnServerSQLPersistence mockedDirPersistence =  mock(FolderOnServerSQLPersistence.class);
		when(mockedDirPersistence.get("root/test")).thenReturn(getRenamedFolder());
		
		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedConfig,mockedDocPersistence,mockedDirPersistence);
		ReturnedMessage message = fileHandler.renameDirectory("root/old", "test");
		
		assertTrue(message.getType()==FileHandler.ERROR_MESSAGE);
	}
	
	@Test
	public void testRenameDirectory_DirectoryToRenameDoesNotExists_ErrorMessageReturned() throws Exception {
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateSecurity()).thenReturn(false);
		
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		FolderOnServerSQLPersistence mockedDirPersistence =  mock(FolderOnServerSQLPersistence.class);
		when(mockedDirPersistence.get(any(String.class))).thenReturn(null);
		
		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedConfig,mockedDocPersistence,mockedDirPersistence);
		ReturnedMessage message = fileHandler.renameDirectory("root/old", "test");
		
		assertTrue(message.getType()==FileHandler.ERROR_MESSAGE);
	}
	
	@Test
	public void testRenameDirectory_SecurityActivated_RenameNotAllowed_ErrorMessageReturned() throws Exception {
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateSecurity()).thenReturn(true);
		SecurityHandler sec = mock(SecurityHandler.class);
		SecurityResponse resp = new SecurityResponse();
		resp.setAllow(false);
		when(sec.hasRight(null, SecurityRightsEnum.RENAME_DIRECTORY_RIGHT, getOldFolder(), user, null)).thenReturn(resp);
		
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		FolderOnServerSQLPersistence mockedDirPersistence =  mock(FolderOnServerSQLPersistence.class);
		when(mockedDirPersistence.get(any(String.class))).thenReturn(getOldFolder());
		
		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedConfig,mockedDocPersistence,mockedDirPersistence);
		ReturnedMessage message = fileHandler.renameDirectory("root/old", "test");
		
		assertTrue(message.getType()==FileHandler.ERROR_MESSAGE);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRenameDirectory_SecurityActivated_RenameAllowed_SuccessMessageReturned() throws Exception {
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(session.getSessionUserName()).thenReturn("ec");
		ISecurityContext secContext = mock(ISecurityContext.class);
		when(session.getSecurityContext()).thenReturn(secContext);
		when(secContext.findUser(any(String.class))).thenReturn(user);
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateSecurity()).thenReturn(true);
		SecurityHandler sec = mock(SecurityHandler.class);
		SecurityResponse resp = new SecurityResponse();
		resp.setAllow(true);
		when(sec.hasRight(any(Iterator.class),any(SecurityRightsEnum.class), any(FolderOnServer.class), any(IUser.class), any(List.class))).thenReturn(resp);
		
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		when(mockedDocPersistence.getList(any(String.class),any(Boolean.class))).thenReturn(new ArrayList<DocumentOnServer>());
		
		FolderOnServerSQLPersistence mockedDirPersistence =  mock(FolderOnServerSQLPersistence.class);
		when(mockedDirPersistence.get("root/test")).thenReturn(null);
		when(mockedDirPersistence.get("root/old")).thenReturn(getOldFolder());
		when(mockedDirPersistence.getList(any(String.class),any(Boolean.class))).thenReturn(new ArrayList<FolderOnServer>());
		
		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedConfig,mockedDocPersistence,mockedDirPersistence);
		ReturnedMessage message = fileHandler.renameDirectory("root/old", "test");
		
		assertTrue(message.getType()==FileHandler.SUCCESS_MESSAGE);
	}
	
	@Test
	public void testRenameDirectory_SecurityNotActivated_SuccessMessageReturned() throws Exception {
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(session.getSessionUserName()).thenReturn("ec");
		ISecurityContext secContext = mock(ISecurityContext.class);
		when(session.getSecurityContext()).thenReturn(secContext);
		when(secContext.findUser(any(String.class))).thenReturn(user);
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateSecurity()).thenReturn(false);
		
		
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		when(mockedDocPersistence.getList(any(String.class),any(Boolean.class))).thenReturn(new ArrayList<DocumentOnServer>());
		
		FolderOnServerSQLPersistence mockedDirPersistence =  mock(FolderOnServerSQLPersistence.class);
		when(mockedDirPersistence.get("root/test")).thenReturn(null);
		when(mockedDirPersistence.get("root/old")).thenReturn(getOldFolder());
		when(mockedDirPersistence.getList(any(String.class),any(Boolean.class))).thenReturn(new ArrayList<FolderOnServer>());
		
		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedConfig,mockedDocPersistence,mockedDirPersistence);
		ReturnedMessage message = fileHandler.renameDirectory("root/old", "test");
		
		assertTrue(message.getType()==FileHandler.SUCCESS_MESSAGE);
	}
	
	private FolderOnServer getOldFolder() {
		FolderOnServer fos = new FolderOnServer();
		fos.setId(1);
		fos.setName("old");
		fos.setPath("root/old");
		return fos;
	}
	
	private FolderOnServer getRenamedFolder() {
		FolderOnServer fos = new FolderOnServer();
		fos.setId(1);
		fos.setName("test");
		fos.setPath("root/test");
		return fos;
	}

}
