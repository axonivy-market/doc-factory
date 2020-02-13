package ch.ivyteam.ivy.addons.filemanager.ivy.implemented;

import java.util.List;

import ch.ivyteam.api.IvyScriptVisibility;
import ch.ivyteam.api.PublicAPI;
import ch.ivyteam.ivy.persistence.PersistencyException;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.security.IUserToken;
import ch.ivyteam.ivy.security.user.IRoleUsers;

public class MyIRole implements IRole {
	
	private String name ="";

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.HIDDEN)
	@Deprecated
	public int getIdentifier() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String getMemberName() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public ISecurityContext getSecurityContext() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public boolean isMember(IUserToken userToken, boolean useSessionRoles)
			throws PersistencyException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public boolean isUser() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IRole createChildRole(String arg0, String arg1, String arg2,
			boolean arg3) throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRole createChildRole(String arg0, String arg1, String arg2,
			String arg3, boolean arg4) throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete() throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllChildRoles() throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public IRole findChildRole(String roleName) throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public List<String> getAllPropertyNames() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public List<IUser> getAllUsers() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public List<IRole> getChildRoles() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String getDisplayDescription() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public String getDisplayDescriptionTemplate() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String getDisplayName() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String getDisplayNameTemplate() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public String getExternalSecurityName() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String getName() throws PersistencyException {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public IRole getParent() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public String getProperty(String name) throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public List<IUser> getUsers() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public IRoleUsers users() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public boolean isRole(IRole role) throws PersistencyException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void move(IRole arg0) throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public String removeProperty(String name) throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void setDisplayDescriptionTemplate(String displayDescriptionTemplate)
			throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void setDisplayNameTemplate(String displayNameTemplate)
			throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void setExternalSecurityName(String externalSecurityName)
			throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public void setProperty(String name, String value)
			throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRoleMember(IRole role) throws PersistencyException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<IRole> getRoleMembers() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeRoleMember(IRole role) throws PersistencyException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<IRole> getRoles() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IRole> getAllRoles() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDynamic() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
