/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.ReturnedMessage;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.DocumentOnServerSQLPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.FileStoreDBHandler;
import ch.ivyteam.ivy.addons.filemanager.database.FolderOnServerSQLPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.fileaction.FileActionConfiguration;
import ch.ivyteam.ivy.addons.filemanager.database.security.DirectorySecurityController;
import ch.ivyteam.ivy.addons.filemanager.database.security.IvyRoleHelper;
import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityResponse;
import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityRightsEnum;
import ch.ivyteam.ivy.addons.filemanager.listener.AbstractFileActionListener;
import ch.ivyteam.ivy.addons.filemanager.listener.FileActionEvent;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.IWorkflowSession;



/**
 * @author ec
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({IvyRoleHelper.class, Ivy.class})
public class FileStoreDBHandlerDeleteDirectoryTest {

	@Test
	public void deleteDirectorySuccessNoSecurityNoFileLocked() throws Exception {
		FolderOnServer fos = new FolderOnServer();
		fos.setPath("root");
		fos.setName("root");
		fos.setId(222);
		List<FolderOnServer> dirs  = this.makeListFolderOnServerUserCanDelete();
		List<DocumentOnServer> docs = this.makeListDocumentOnServerNoLocked();
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateSecurity()).thenReturn(false);
		
		FileActionConfiguration mockedFahc = mock(FileActionConfiguration.class);
		when(mockedFahc.isActivateFileActionHistory()).thenReturn(false);
		when(mockedConfig.getFileActionHistoryConfiguration()).thenReturn(mockedFahc);
		
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		when(mockedDocPersistence.getList("root", true)).thenReturn(docs);
		for(DocumentOnServer doc: docs)
			when(mockedDocPersistence.delete(doc)).thenReturn(true);
		
		FolderOnServerSQLPersistence mockedDirPersistence =  mock(FolderOnServerSQLPersistence.class);
		when(mockedDirPersistence.getList("root", true)).thenReturn(dirs);
		when(mockedDirPersistence.get("root")).thenReturn(fos);
		for(FolderOnServer dir: dirs)
			when(mockedDirPersistence.delete(dir)).thenReturn(true);
		when(mockedDirPersistence.delete(fos)).thenReturn(true);

		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedConfig,mockedDocPersistence,mockedDirPersistence);
		AbstractFileActionListener mockedAbstractFileActionListener = mock(AbstractFileActionListener.class);
		fileHandler.addFileEventListener(mockedAbstractFileActionListener);
		
		ReturnedMessage message = fileHandler.deleteDirectory(fos.getPath());
		verify(mockedAbstractFileActionListener, times(docs.size())).fileDeleted((FileActionEvent) anyObject());
		assertTrue(message.getType()==FileHandler.SUCCESS_MESSAGE);

	}
	
	@Test
	public void deleteDirectorySuccessWithSecurityActivatedUserHasRightToDeleteDirectoryAndFilesNoFileLocked() throws Exception {
		
		FolderOnServer fos = new FolderOnServer();
		fos.setPath("root");
		fos.setName("root");
		fos.setId(222);
		fos.setCdd(makeListRoles());
		
		SecurityResponse respDDR = new SecurityResponse();
		respDDR.setAllow(true);
		SecurityResponse respDFR = new SecurityResponse();
		respDFR.setAllow(true);
		
		mockStatic(IvyRoleHelper.class);
		when(IvyRoleHelper.getUserRolesAsListStrings(any(IUser.class))).thenReturn(makeListRoles());
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		when(IvyRoleHelper.getUserRolesAsListStrings(any(IUser.class))).thenReturn(makeListRoles());
		
		List<FolderOnServer> dirs  = this.makeListFolderOnServerUserCanDelete();
		List<DocumentOnServer> docs = this.makeListDocumentOnServerNoLocked();
		DirectorySecurityController dsc = mock(DirectorySecurityController.class);
		when(dsc.hasRight(null, SecurityRightsEnum.DELETE_DIRECTORY_RIGHT, fos, user, null)).thenReturn(respDDR);
		when(dsc.hasRight(null, SecurityRightsEnum.DELETE_FILES_RIGHT, fos, user, null)).thenReturn(respDFR);
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateSecurity()).thenReturn(true);
		when(mockedConfig.getSecurityHandler()).thenReturn(dsc);
		
		FileActionConfiguration mockedFahc = mock(FileActionConfiguration.class);
		when(mockedFahc.isActivateFileActionHistory()).thenReturn(false);
		when(mockedConfig.getFileActionHistoryConfiguration()).thenReturn(mockedFahc);
		
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		when(mockedDocPersistence.getList("root", true)).thenReturn(docs);
		for(DocumentOnServer doc: docs)
			when(mockedDocPersistence.delete(doc)).thenReturn(true);
		
		FolderOnServerSQLPersistence mockedDirPersistence =  mock(FolderOnServerSQLPersistence.class);
		when(mockedDirPersistence.getList("root/", true)).thenReturn(dirs);
		when(mockedDirPersistence.getList("root", true)).thenReturn(dirs);
		when(mockedDirPersistence.get("root")).thenReturn(fos);
		when(mockedDirPersistence.get("root/")).thenReturn(fos);
		for(FolderOnServer dir: dirs) {
			when(dsc.hasRight(null, SecurityRightsEnum.DELETE_DIRECTORY_RIGHT, dir, user, null)).thenReturn(respDDR);
			when(dsc.hasRight(null, SecurityRightsEnum.DELETE_FILES_RIGHT, dir, user, null)).thenReturn(respDFR);
			when(mockedDirPersistence.delete(dir)).thenReturn(true); 
		}
		when(mockedDirPersistence.delete(fos)).thenReturn(true);

		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedConfig,mockedDocPersistence,mockedDirPersistence);
		AbstractFileActionListener mockedAbstractFileActionListener = mock(AbstractFileActionListener.class);
		fileHandler.addFileEventListener(mockedAbstractFileActionListener);
		
		ReturnedMessage message = fileHandler.deleteDirectory(fos.getPath());
		verify(mockedAbstractFileActionListener, times(docs.size())).fileDeleted((FileActionEvent) anyObject());
		assertTrue(message.getType()==FileHandler.SUCCESS_MESSAGE);

	}
	
	@Test
	public void deleteDirectoryFailedWithSecurityActivatedUserHasNotRightToDeleteDirectoryNoFileLocked() throws Exception {
		
		FolderOnServer fos = new FolderOnServer();
		fos.setPath("root");
		fos.setName("root");
		fos.setId(222);
		fos.setCdd(makeListRoles());
		
		SecurityResponse respDDR = new SecurityResponse();
		respDDR.setAllow(false);
		SecurityResponse respDFR = new SecurityResponse();
		respDFR.setAllow(true);
		
		mockStatic(IvyRoleHelper.class);
		when(IvyRoleHelper.getUserRolesAsListStrings(any(IUser.class))).thenReturn(makeListRoles());
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		when(IvyRoleHelper.getUserRolesAsListStrings(any(IUser.class))).thenReturn(makeListRoles());
		
		List<FolderOnServer> dirs  = this.makeListFolderOnServerUserCanDelete();
		List<DocumentOnServer> docs = this.makeListDocumentOnServerNoLocked();
		DirectorySecurityController dsc = mock(DirectorySecurityController.class);
		when(dsc.hasRight(null, SecurityRightsEnum.DELETE_DIRECTORY_RIGHT, fos, user, null)).thenReturn(respDDR);
		when(dsc.hasRight(null, SecurityRightsEnum.DELETE_FILES_RIGHT, fos, user, null)).thenReturn(respDFR);
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateSecurity()).thenReturn(true);
		when(mockedConfig.getSecurityHandler()).thenReturn(dsc);
		
		FileActionConfiguration mockedFahc = mock(FileActionConfiguration.class);
		when(mockedFahc.isActivateFileActionHistory()).thenReturn(false);
		when(mockedConfig.getFileActionHistoryConfiguration()).thenReturn(mockedFahc);
		
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		when(mockedDocPersistence.getList("root", true)).thenReturn(docs);
		for(DocumentOnServer doc: docs)
			when(mockedDocPersistence.delete(doc)).thenReturn(true);
		
		FolderOnServerSQLPersistence mockedDirPersistence =  mock(FolderOnServerSQLPersistence.class);
		when(mockedDirPersistence.getList("root/", true)).thenReturn(dirs);
		when(mockedDirPersistence.getList("root", true)).thenReturn(dirs);
		when(mockedDirPersistence.get("root")).thenReturn(fos);
		when(mockedDirPersistence.get("root/")).thenReturn(fos);
		for(FolderOnServer dir: dirs) {
			when(dsc.hasRight(null, SecurityRightsEnum.DELETE_DIRECTORY_RIGHT, dir, user, null)).thenReturn(respDDR);
			when(dsc.hasRight(null, SecurityRightsEnum.DELETE_FILES_RIGHT, dir, user, null)).thenReturn(respDFR);
			when(mockedDirPersistence.delete(dir)).thenReturn(true); 
		}
		when(mockedDirPersistence.delete(fos)).thenReturn(true);

		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedConfig,mockedDocPersistence,mockedDirPersistence);
		AbstractFileActionListener mockedAbstractFileActionListener = mock(AbstractFileActionListener.class);
		fileHandler.addFileEventListener(mockedAbstractFileActionListener);
		
		ReturnedMessage message = fileHandler.deleteDirectory(fos.getPath());
		verify(mockedAbstractFileActionListener, times(0)).fileDeleted((FileActionEvent) anyObject());
		System.out.println(message.getText());
		assertTrue(message.getType()==FileHandler.ERROR_MESSAGE);

	}
	
	@Test
	public void deleteDirectoryFailedWithSecurityActivatedUserHasNotRightToDeleteFilesNoFileLocked() throws Exception {
		
		FolderOnServer fos = new FolderOnServer();
		fos.setPath("root");
		fos.setName("root");
		fos.setId(222);
		fos.setCdd(makeListRoles());
		
		SecurityResponse respDDR = new SecurityResponse();
		respDDR.setAllow(true);
		SecurityResponse respDFR = new SecurityResponse();
		respDFR.setAllow(false);
		
		mockStatic(IvyRoleHelper.class);
		when(IvyRoleHelper.getUserRolesAsListStrings(any(IUser.class))).thenReturn(makeListRoles());
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		when(IvyRoleHelper.getUserRolesAsListStrings(any(IUser.class))).thenReturn(makeListRoles());
		
		List<FolderOnServer> dirs  = this.makeListFolderOnServerUserCanDelete();
		List<DocumentOnServer> docs = this.makeListDocumentOnServerNoLocked();
		DirectorySecurityController dsc = mock(DirectorySecurityController.class);
		when(dsc.hasRight(null, SecurityRightsEnum.DELETE_DIRECTORY_RIGHT, fos, user, null)).thenReturn(respDDR);
		when(dsc.hasRight(null, SecurityRightsEnum.DELETE_FILES_RIGHT, fos, user, null)).thenReturn(respDFR);
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateSecurity()).thenReturn(true);
		when(mockedConfig.getSecurityHandler()).thenReturn(dsc);
		
		FileActionConfiguration mockedFahc = mock(FileActionConfiguration.class);
		when(mockedFahc.isActivateFileActionHistory()).thenReturn(false);
		when(mockedConfig.getFileActionHistoryConfiguration()).thenReturn(mockedFahc);
		
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		when(mockedDocPersistence.getList("root", true)).thenReturn(docs);
		when(mockedDocPersistence.getList("root", false)).thenReturn(docs);
		for(DocumentOnServer doc: docs)
			when(mockedDocPersistence.delete(doc)).thenReturn(true);
		
		FolderOnServerSQLPersistence mockedDirPersistence =  mock(FolderOnServerSQLPersistence.class);
		when(mockedDirPersistence.getList("root/", true)).thenReturn(dirs);
		when(mockedDirPersistence.getList("root", true)).thenReturn(dirs);
		when(mockedDirPersistence.get("root")).thenReturn(fos);
		when(mockedDirPersistence.get("root/")).thenReturn(fos);
		for(FolderOnServer dir: dirs) {
			when(dsc.hasRight(null, SecurityRightsEnum.DELETE_DIRECTORY_RIGHT, dir, user, null)).thenReturn(respDDR);
			when(dsc.hasRight(null, SecurityRightsEnum.DELETE_FILES_RIGHT, dir, user, null)).thenReturn(respDFR);
			when(mockedDirPersistence.delete(dir)).thenReturn(true); 
		}
		when(mockedDirPersistence.delete(fos)).thenReturn(true);

		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedConfig,mockedDocPersistence,mockedDirPersistence);
		AbstractFileActionListener mockedAbstractFileActionListener = mock(AbstractFileActionListener.class);
		fileHandler.addFileEventListener(mockedAbstractFileActionListener);
		
		ReturnedMessage message = fileHandler.deleteDirectory(fos.getPath());
		verify(mockedAbstractFileActionListener, times(0)).fileDeleted((FileActionEvent) anyObject());
		System.out.println(message.getText());
		assertTrue(message.getType()==FileHandler.ERROR_MESSAGE);

	}
	
	@Test
	public void deleteDirectoryFailedWithSecurityActivatedUserHasRightToDeleteButAtLeastOneFileIsLocked() throws Exception {
		
		FolderOnServer fos = new FolderOnServer();
		fos.setPath("root");
		fos.setName("root");
		fos.setId(222);
		fos.setCdd(makeListRoles());
		
		SecurityResponse resp = new SecurityResponse();
		resp.setAllow(true);
		
		mockStatic(IvyRoleHelper.class);
		when(IvyRoleHelper.getUserRolesAsListStrings(any(IUser.class))).thenReturn(makeListRoles());
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		when(IvyRoleHelper.getUserRolesAsListStrings(any(IUser.class))).thenReturn(makeListRoles());
		
		List<FolderOnServer> dirs  = this.makeListFolderOnServerUserCanDelete();
		List<DocumentOnServer> docs = this.makeListDocumentOnServerOneLocked();
		DirectorySecurityController dsc = mock(DirectorySecurityController.class);
		//when(dsc.ensureRightsIntegrityInDirectory(fos)).thenReturn(fos);
		when(dsc.hasRight(null, SecurityRightsEnum.DELETE_DIRECTORY_RIGHT, fos, user, null)).thenReturn(resp);
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateSecurity()).thenReturn(true);
		when(mockedConfig.getSecurityHandler()).thenReturn(dsc);
		
		FileActionConfiguration mockedFahc = mock(FileActionConfiguration.class);
		when(mockedFahc.isActivateFileActionHistory()).thenReturn(false);
		when(mockedConfig.getFileActionHistoryConfiguration()).thenReturn(mockedFahc);
		
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		when(mockedDocPersistence.getList("root", true)).thenReturn(docs);
		for(DocumentOnServer doc: docs)
			when(mockedDocPersistence.delete(doc)).thenReturn(true);
		
		FolderOnServerSQLPersistence mockedDirPersistence =  mock(FolderOnServerSQLPersistence.class);
		when(mockedDirPersistence.getList("root/", true)).thenReturn(dirs);
		when(mockedDirPersistence.getList("root", true)).thenReturn(dirs);
		when(mockedDirPersistence.get("root")).thenReturn(fos);
		when(mockedDirPersistence.get("root/")).thenReturn(fos);
		for(FolderOnServer dir: dirs) {
			when(dsc.hasRight(null, SecurityRightsEnum.DELETE_DIRECTORY_RIGHT, dir, user, null)).thenReturn(resp);
			when(mockedDirPersistence.delete(dir)).thenReturn(true); 
		}
		when(mockedDirPersistence.delete(fos)).thenReturn(true);

		FileStoreDBHandler fileHandler = new FileStoreDBHandler(mockedConfig,mockedDocPersistence,mockedDirPersistence);
		AbstractFileActionListener mockedAbstractFileActionListener = mock(AbstractFileActionListener.class);
		fileHandler.addFileEventListener(mockedAbstractFileActionListener);
		
		ReturnedMessage message = fileHandler.deleteDirectory(fos.getPath());
		verify(mockedAbstractFileActionListener, times(0)).fileDeleted((FileActionEvent) anyObject());
		System.out.println(message.getText());
		assertTrue(message.getType()==FileHandler.ERROR_MESSAGE);

	}

	
	private List<DocumentOnServer> makeListDocumentOnServerNoLocked() {
		List<DocumentOnServer> docs = new ArrayList<DocumentOnServer>();
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("3");
		doc.setFilename("test.pdf");
		doc.setPath("root/test1/test.pdf");
		doc.setLocked("0");
		doc.setVersionnumber(4);
		docs.add(doc);
		
		doc = new DocumentOnServer();
		doc.setFileID("44");
		doc.setFilename("test.docx");
		doc.setPath("root/test2/test.docx");
		doc.setLocked("0");
		doc.setVersionnumber(4);
		docs.add(doc);
		
		doc = new DocumentOnServer();
		doc.setFileID("234");
		doc.setFilename("tiptop.docx");
		doc.setPath("root/tiptop.docx");
		doc.setLocked("0");
		doc.setVersionnumber(4);
		docs.add(doc);
		
		return docs;
	}
	
	private List<DocumentOnServer> makeListDocumentOnServerOneLocked() {
		List<DocumentOnServer> docs = new ArrayList<DocumentOnServer>();
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("3");
		doc.setFilename("test.pdf");
		doc.setPath("root/test1/test.pdf");
		doc.setLocked("0");
		doc.setVersionnumber(4);
		docs.add(doc);
		
		doc = new DocumentOnServer();
		doc.setFileID("44");
		doc.setFilename("test.docx");
		doc.setPath("root/test2/test.docx");
		doc.setLocked("1");
		doc.setLockingUserID("SK");
		doc.setVersionnumber(4);
		docs.add(doc);
		
		doc = new DocumentOnServer();
		doc.setFileID("234");
		doc.setFilename("tiptop.docx");
		doc.setPath("root/tiptop.docx");
		doc.setLocked("0");
		doc.setVersionnumber(4);
		docs.add(doc);
		
		return docs;
	}
	
	private List<FolderOnServer> makeListFolderOnServerUserCanDelete() {
		List<FolderOnServer> dirs = new ArrayList<FolderOnServer>();
		FolderOnServer fos = new FolderOnServer();
		fos.setPath("root/test1");
		fos.setName("test1");
		fos.setId(223);
		fos.setCdd(makeListRoles());
		dirs.add(fos);
		
		fos = new FolderOnServer();
		fos.setPath("root/test1/test");
		fos.setName("test1");
		fos.setId(224);
		fos.setCdd(makeListRoles());
		dirs.add(fos);
		
		fos = new FolderOnServer();
		fos.setPath("root/test2");
		fos.setName("test2");
		fos.setId(225);
		fos.setCdd(makeListRoles());
		dirs.add(fos);
		
		
		return dirs;
	}
	
	private ch.ivyteam.ivy.scripting.objects.List<String> makeListRoles() {
		ch.ivyteam.ivy.scripting.objects.List<String> roles =ch.ivyteam.ivy.scripting.objects.List.create(String.class);
		roles.add("role1");
		roles.add("role2");
		return roles;
	}
	
	
}
