/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database.security;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.addons.filemanager.FileManagementHandlersFactory;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.ivy.implemented.MyCMS;
import ch.ivyteam.ivy.addons.filemanager.ivy.implemented.MyGlobalVariableContext;
import ch.ivyteam.ivy.addons.filemanager.ivy.implemented.MyIRole;
import ch.ivyteam.ivy.cm.IContentManagementSystem;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.workflow.IWorkflowSession;

/**
 * @author ec
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Ivy.class, IContentManagementSystem.class,FileManagementHandlersFactory.class})
public class SecurityHandlerChainRolesTest {

	@Test
	public void testSecurityHandlerChainHas_IvySessionUserRolesAtTheBeginning_IfGivenUserisNull() throws Exception {
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		MyGlobalVariableContext var = mock(MyGlobalVariableContext.class);
		when(var.get(any(String.class))).thenReturn("");

		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		when(Ivy.var()).thenReturn(var);
		when(Ivy.cms().co(any(String.class))).thenReturn("");
		mockStatic(FileManagementHandlersFactory.class);
		when(FileManagementHandlersFactory.getDirectorySecurityControllerInstance(any(BasicConfigurationController.class))).thenReturn(null);

		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getAllRoles()).thenReturn(this.makeListIRoles());

		BasicConfigurationController config = new BasicConfigurationController();
		
		SecurityHandlerChain shc = new SecurityHandlerChain(null, config);
		shc.hasRight(null, SecurityRightsEnum.CREATE_FILES_RIGHT, null, null, null);
		assertTrue(shc.getRolesInChain().containsAll(makeListRoles()));
	}
	
	@Test
	public void testSecurityHandlerChainHas_GivenUserRolesAtTheBeginning_AndNotIvySessionUserRoles_IfGivenUserNotNull() throws Exception {
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		MyGlobalVariableContext var = mock(MyGlobalVariableContext.class);
		when(var.get(any(String.class))).thenReturn("");

		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		when(Ivy.var()).thenReturn(var);
		when(Ivy.cms().co(any(String.class))).thenReturn("");
		mockStatic(FileManagementHandlersFactory.class);
		when(FileManagementHandlersFactory.getDirectorySecurityControllerInstance(any(BasicConfigurationController.class))).thenReturn(null);

		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getAllRoles()).thenReturn(this.makeListIRoles());
		IUser user2 = mock(IUser.class);
		when(user2.getAllRoles()).thenReturn(this.makeListIRoles2());
		
		BasicConfigurationController config = new BasicConfigurationController();
		
		SecurityHandlerChain shc = new SecurityHandlerChain(null, config);
		shc.hasRight(null, SecurityRightsEnum.CREATE_FILES_RIGHT, null, user2, null);
		assertTrue(shc.getRolesInChain().containsAll(makeListRoles2()));
		assertFalse(shc.getRolesInChain().containsAll(makeListRoles()));
	}
	
	@Test
	public void testSecurityHandlerChainHas_IvySessionUserRolesAtTheBeginning_IfGivenUserisNull_AndAllTheDynamicallyPassedRoles() throws Exception {
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		MyGlobalVariableContext var = mock(MyGlobalVariableContext.class);
		when(var.get(any(String.class))).thenReturn("");

		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		when(Ivy.var()).thenReturn(var);
		when(Ivy.cms().co(any(String.class))).thenReturn("");
		mockStatic(FileManagementHandlersFactory.class);
		when(FileManagementHandlersFactory.getDirectorySecurityControllerInstance(any(BasicConfigurationController.class))).thenReturn(null);

		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getAllRoles()).thenReturn(this.makeListIRoles());
		
		BasicConfigurationController config = new BasicConfigurationController();
		
		SecurityHandlerChain shc = new SecurityHandlerChain(null, config);
		shc.hasRight(null, SecurityRightsEnum.CREATE_FILES_RIGHT, null, null, makeListRoles2());
		assertTrue(shc.getRolesInChain().containsAll(makeListRoles2()));
		assertTrue(shc.getRolesInChain().containsAll(makeListRoles()));
	}
	
	@Test
	public void testSecurityHandlerChainHas_GivenUserRolesAtTheBeginning_AndAllTheDynamicallyPassedRoles_IfGivenUserNotNull() throws Exception {
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		MyGlobalVariableContext var = mock(MyGlobalVariableContext.class);
		when(var.get(any(String.class))).thenReturn("");

		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		when(Ivy.var()).thenReturn(var);
		when(Ivy.cms().co(any(String.class))).thenReturn("");
		mockStatic(FileManagementHandlersFactory.class);
		when(FileManagementHandlersFactory.getDirectorySecurityControllerInstance(any(BasicConfigurationController.class))).thenReturn(null);

		IWorkflowSession session = mock(IWorkflowSession.class);
		IUser user = mock(IUser.class);
		when(Ivy.session()).thenReturn(session);
		when(session.getSessionUser()).thenReturn(user);
		when(user.getAllRoles()).thenReturn(this.makeListIRoles());
		IUser user2 = mock(IUser.class);
		when(user2.getAllRoles()).thenReturn(this.makeListIRoles2());
		
		BasicConfigurationController config = new BasicConfigurationController();
		
		SecurityHandlerChain shc = new SecurityHandlerChain(null, config);
		shc.hasRight(null, SecurityRightsEnum.CREATE_FILES_RIGHT, null, user2, makeListRoles());
		assertTrue(shc.getRolesInChain().containsAll(makeListRoles2()));
		assertTrue(shc.getRolesInChain().containsAll(makeListRoles()));
	}
	
	private ch.ivyteam.ivy.scripting.objects.List<String> makeListRoles() {
		ch.ivyteam.ivy.scripting.objects.List<String> roles =ch.ivyteam.ivy.scripting.objects.List.create(String.class);
		roles.add("role1");
		roles.add("role2");
		return roles;
	}
	
	private ch.ivyteam.ivy.scripting.objects.List<IRole> makeListIRoles() {
		ch.ivyteam.ivy.scripting.objects.List<IRole> roles =ch.ivyteam.ivy.scripting.objects.List.create(IRole.class);
		MyIRole r = new MyIRole();
		r.setName("role1");
		roles.add(r);
		r = new MyIRole();
		r.setName("role2");
		roles.add(r);
		return roles;
	}
	
	private ch.ivyteam.ivy.scripting.objects.List<IRole> makeListIRoles2() {
		ch.ivyteam.ivy.scripting.objects.List<IRole> roles =ch.ivyteam.ivy.scripting.objects.List.create(IRole.class);
		MyIRole r = new MyIRole();
		r.setName("role3");
		roles.add(r);
		r = new MyIRole();
		r.setName("role4");
		roles.add(r);
		return roles;
	}

	private ch.ivyteam.ivy.scripting.objects.List<String> makeListRoles2() {
		ch.ivyteam.ivy.scripting.objects.List<String> roles =ch.ivyteam.ivy.scripting.objects.List.create(String.class);
		roles.add("role3");
		roles.add("role4");
		return roles;
	}
}
