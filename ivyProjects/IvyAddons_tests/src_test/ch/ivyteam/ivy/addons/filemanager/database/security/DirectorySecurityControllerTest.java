/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.security;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.database.security.DirectorySecurityController;
import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityResponse;
import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityRightsEnum;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.globalvars.IGlobalVariableContext;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.IWorkflowSession;
import ch.ivyteam.log.Logger;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
/**
 * @author ec
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Ivy.class, Logger.class})
public class DirectorySecurityControllerTest {

	@Test
	public void canOpenDirectory() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		
		fos.setCod(roles);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.OPEN_DIRECTORY_RIGHT, fos, null, roles);
		
		assertTrue(resp.isAllow());
	}
	
	@Test
	public void canNotOpenDirectory() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCod(roles);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.OPEN_DIRECTORY_RIGHT, fos, null, List.create(String.class));
		
		assertFalse(resp.isAllow());
	}
	
	@Test
	public void canNotUpdateDirectory() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		List<String> roles2 = List.create(String.class);
		roles2.add("role1");
		fos.setCod(roles2);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.UPDATE_DIRECTORY_RIGHT, fos, null, roles);
		assertFalse(resp.isAllow());
	}
	
	@Test
	public void canUpdateDirectory() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		List<String> roles2 = List.create(String.class);
		roles2.add("role1");
		fos.setCud(roles2);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.UPDATE_DIRECTORY_RIGHT, fos, null, roles);
		
		assertTrue(resp.isAllow());
	}
	
	@Test
	public void canNotDeleteDirectory() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		List<String> roles2 = List.create(String.class);
		roles2.add("role1");
		fos.setCod(roles2);
		fos.setCud(roles2);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.DELETE_DIRECTORY_RIGHT, fos, null, roles);
		
		assertFalse(resp.isAllow());
	}
	
	@Test
	public void canDeleteDirectory() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCdd(roles);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.DELETE_DIRECTORY_RIGHT, fos, null, roles);
		
		assertTrue(resp.isAllow());
	}
	
	@Test
	public void canRenameDirectoryIfCanUpdateDirectoryAndSecurityVersionIsOne() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		List<String> roles2 = List.create(String.class);
		roles2.add("role1");
		fos.setCod(roles2);
		fos.setCud(roles2);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.RENAME_DIRECTORY_RIGHT, fos, null, roles);
		
		assertTrue(resp.isAllow());
	}
	
	@Test
	public void canRenameDirectory() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCrd(roles);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.RENAME_DIRECTORY_RIGHT, fos, null, roles);
		
		assertTrue(resp.isAllow());
	}
	
	@Test
	public void canNotCreateDirectory() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		List<String> roles2 = List.create(String.class);
		roles2.add("role1");
		fos.setCod(roles2);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.CREATE_DIRECTORY_RIGHT, fos, null, roles);
		
		assertFalse(resp.isAllow());
	}
	
	@Test
	public void canCreateDirectory() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCcd(roles);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.CREATE_DIRECTORY_RIGHT, fos, null, roles);
		
		assertTrue(resp.isAllow());
	}
	
	@Test
	public void canNotTranslateDirectory() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		List<String> roles2 = List.create(String.class);
		roles2.add("role1");
		fos.setCod(roles2);
		fos.setCud(roles2);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.TRANSLATE_DIRECTORY_RIGHT, fos, null, roles);
		
		assertFalse(resp.isAllow());
	}
	
	@Test
	public void canTranslateDirectory() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCtd(roles);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.TRANSLATE_DIRECTORY_RIGHT, fos, null, roles);
		
		assertTrue(resp.isAllow());
	}
	
	@Test
	public void canNotCreateFile() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		List<String> roles2 = List.create(String.class);
		roles2.add("role1");
		fos.setCod(roles2);
		fos.setCud(roles2);
		fos.setCuf(roles2);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.CREATE_FILES_RIGHT, fos, null, roles);
		
		assertFalse(resp.isAllow());
	}
	
	@Test
	public void canCreateFile() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCcf(roles);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.CREATE_FILES_RIGHT, fos, null, roles);
		
		assertTrue(resp.isAllow());
	}
	
	@Test
	public void canNotwriteFile() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		List<String> roles2 = List.create(String.class);
		roles2.add("role1");
		fos.setCod(roles2);
		fos.setCud(roles2);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.WRITE_FILES_RIGHT, fos, null, roles);
		
		assertFalse(resp.isAllow());
	}
	
	@Test
	public void canWriteFile() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCwf(roles);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.WRITE_FILES_RIGHT, fos, null, roles);
		
		assertTrue(resp.isAllow());
	}
	
	@Test
	public void canNotDeleteFile() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		List<String> roles2 = List.create(String.class);
		roles2.add("role1");
		fos.setCod(roles2);
		fos.setCud(roles2);
		fos.setCwf(roles2);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.DELETE_FILES_RIGHT, fos, null, roles);
		
		assertFalse(resp.isAllow());
	}
	
	@Test
	public void canDeleteFile() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCdf(roles);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.DELETE_FILES_RIGHT, fos, null, roles);
		
		assertTrue(resp.isAllow());
	}
	
	@Test
	public void canNotUpdateFile() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		List<String> roles2 = List.create(String.class);
		roles2.add("role1");
		fos.setCod(roles2);
		fos.setCud(roles2);
		fos.setCcf(roles2);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.UPDATE_FILES_RIGHT, fos, null, roles);
		
		assertFalse(resp.isAllow());
	}
	
	@Test
	public void canUpdateFile() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCuf(roles);
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		SecurityResponse resp  = dsc.hasRight(null, SecurityRightsEnum.UPDATE_FILES_RIGHT, fos, null, roles);
		
		assertTrue(resp.isAllow());
	}
	
	@Test
	public void canCreateDirectoryWhenHavingUpdateDirectoryRight(){
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mock(Logger.class));
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		FolderOnServer fos = createDummyFolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCud(roles);
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		dsc.fillUserRightsInFolderOnServer(fos, toListOfIRoles(roles));
		assertTrue(fos.getCanUserCreateDirectory());
	}
	
	@Test
	public void canRenameDirectoryWhenHavingUpdateDirectoryRight(){
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mock(Logger.class));
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		FolderOnServer fos = createDummyFolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCud(roles);
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		dsc.fillUserRightsInFolderOnServer(fos, toListOfIRoles(roles));
		assertTrue(fos.getCanUserRenameDirectory());
	}
	
	@Test
	public void canCreateFilesWhenHavingWriteFilesRight(){
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mock(Logger.class));
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		FolderOnServer fos = createDummyFolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCwf(roles);
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		dsc.fillUserRightsInFolderOnServer(fos, toListOfIRoles(roles));
		assertTrue(fos.getCanUserCreateFiles());
	}
	
	@Test
	public void canCreateAndUpdateFilesWhenHavingWriteFilesRight(){
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mock(Logger.class));
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		FolderOnServer fos = createDummyFolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCwf(roles);
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		dsc.fillUserRightsInFolderOnServer(fos, toListOfIRoles(roles));
		assertTrue(fos.getCanUserCreateFiles() && fos.getCanUserUpdateFiles());
	}
	
	@Test
	public void canNotWriteFilesWhenHavingCreateFilesRight(){
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mock(Logger.class));
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		FolderOnServer fos = createDummyFolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCcf(roles);
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		dsc.fillUserRightsInFolderOnServer(fos, toListOfIRoles(roles));
		assertFalse(fos.getCanUserWriteFiles());
	}
	
	@Test
	public void canOpenDirectoryWhenHavingWriteFilesRight(){
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mock(Logger.class));
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		FolderOnServer fos = createDummyFolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCwf(roles);
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		dsc.fillUserRightsInFolderOnServer(fos, toListOfIRoles(roles));
		assertTrue(fos.getCanUserOpenDir());
	}
	
	@Test
	public void canAllWhenHavingManagementRight(){
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mock(Logger.class));
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		FolderOnServer fos = createDummyFolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCmrd(roles);
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		dsc.fillUserRightsInFolderOnServer(fos, toListOfIRoles(roles));
		assertTrue(hasAllRights(fos));
	}
	
	@Test
	public void canNotAllWhenNotHavingManagementRight(){
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mock(Logger.class));
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		FolderOnServer fos = createDummyFolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCwf(roles);
		fos.setCdd(roles);
		fos.setCdf(roles);
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		dsc.fillUserRightsInFolderOnServer(fos, toListOfIRoles(roles));
		assertFalse(hasAllRights(fos));
	}

	private boolean hasAllRights(FolderOnServer fos) {
		return fos.getCanUserOpenDir() && fos.getCanUserDeleteDir() && fos.getCanUserDeleteFiles() && fos.getCanUserManageRights() &&
				fos.getCanUserUpdateDir() && fos.getCanUserWriteFiles() && fos.getCanUserCreateDirectory() && fos.getCanUserCreateFiles() &&
				fos.getCanUserRenameDirectory() && fos.getCanUserTranslateDirectory() && fos.getCanUserUpdateFiles();
	}
	
	@Test
	public void canAllWhenCwfCddCdf(){
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mock(Logger.class));
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		FolderOnServer fos = createDummyFolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCwf(roles);
		fos.setCdd(roles);
		fos.setCdf(roles);
		
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		dsc.fillUserRightsInFolderOnServer(fos, toListOfIRoles(roles));
		assertFalse(fos.getCanUserOpenDir() && fos.getCanUserDeleteDir() && fos.getCanUserDeleteFiles() &&
				fos.getCanUserUpdateDir() && fos.getCanUserWriteFiles() && fos.getCanUserCreateDirectory() && fos.getCanUserCreateFiles() &&
				fos.getCanUserRenameDirectory() && fos.getCanUserTranslateDirectory() && fos.getCanUserUpdateFiles());
	}
	
	@Test
	public void hasRightToWriteFilesWhenWriteFilesRightIsIntoRoleList() {
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mock(Logger.class));
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		FolderOnServer fos = createDummyFolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCwf(roles);
		fos.setCdd(roles);
		fos.setCdf(roles);
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		assertTrue(dsc.hasRight(null, SecurityRightsEnum.WRITE_FILES_RIGHT, fos, null, roles).isAllow());
	}
	
	@Test
	public void hasRightToUpdateFilesWhenWriteFilesRightIsIntoRoleList() {
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mock(Logger.class));
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		FolderOnServer fos = createDummyFolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCwf(roles);
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		assertTrue(dsc.hasRight(null, SecurityRightsEnum.UPDATE_FILES_RIGHT, fos, null, roles).isAllow());
	}
	
	@Test
	public void hasRightToUpdateFilesWhenDeleteFilesRightIsIntoRoleList() {
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mock(Logger.class));
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		FolderOnServer fos = createDummyFolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCdf(roles);
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		assertTrue(dsc.hasRight(null, SecurityRightsEnum.UPDATE_FILES_RIGHT, fos, null, roles).isAllow());
	}
	
	@Test
	public void hasRightToCreateFilesWhenWriteFilesRightIsIntoRoleList() {
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mock(Logger.class));
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		FolderOnServer fos = createDummyFolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCwf(roles);
		fos.setCdd(roles);
		fos.setCdf(roles);
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		assertTrue(dsc.hasRight(null, SecurityRightsEnum.CREATE_FILES_RIGHT, fos, null, roles).isAllow());
	}
	
	@Test
	public void hasNotRightToWriteFilesWhenUpdateFilesRightIsIntoRoleListAndWriteFilesAndDeleteFilesNotIn() {
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mock(Logger.class));
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		FolderOnServer fos = createDummyFolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCuf(roles);
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		dsc.fillUserRightsInFolderOnServer(fos, toListOfIRoles(roles));
		assertFalse(dsc.hasRight(null, SecurityRightsEnum.WRITE_FILES_RIGHT, fos, null, roles).isAllow());
	}
	
	@Test
	public void hasRightToWriteFilesWhenDeleteFilesRightIsIntoRoleList() {
		PowerMockito.mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mock(Logger.class));
		
		mockStatic(Ivy.class);
		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getName()).thenReturn("TODO");
		Assert.assertEquals("TODO", Ivy.session().getSessionUser().getName());
		IGlobalVariableContext var = mock(IGlobalVariableContext.class);
		when(Ivy.var()).thenReturn(var);
		when(var.get(anyString())).thenReturn("");
		
		FolderOnServer fos = createDummyFolderOnServer();
		List<String> roles = List.create(String.class);
		roles.add("role1");
		fos.setCdd(roles);
		fos.setCdf(roles);
		DirectorySecurityController dsc = new DirectorySecurityController("dummy", "dummy", "dummy", "dummy");
		dsc.fillUserRightsInFolderOnServer(fos, toListOfIRoles(roles));
		assertTrue(dsc.hasRight(null, SecurityRightsEnum.WRITE_FILES_RIGHT, fos, null, roles).isAllow());
	}
	
	private FolderOnServer createDummyFolderOnServer() {
		FolderOnServer fos = new FolderOnServer();
		List<String> roles = List.create(String.class);
		fos.setCmrd(roles);
		fos.setCcd(roles);
		fos.setCod(roles);
		fos.setCud(roles);
		fos.setCrd(roles);
		fos.setCtd(roles);
		fos.setCdd(roles);
		fos.setCwf(roles);
		fos.setCcf(roles);
		fos.setCuf(roles);
		fos.setCdf(roles);
		fos.setPath("dummy");

		return fos;
	}
	
	private List<IRole> toListOfIRoles(List<String> roles) {
		List<IRole> ivyRoles = List.create(IRole.class);
		
		for (String roleString : roles) {
			IRole role = mock(IRole.class); 
			when(role.getName()).thenReturn(roleString);
			ivyRoles.add(role);
		}
		
		return ivyRoles;
	}
	

}
