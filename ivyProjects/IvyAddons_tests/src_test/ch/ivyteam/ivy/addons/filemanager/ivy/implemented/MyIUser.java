package ch.ivyteam.ivy.addons.filemanager.ivy.implemented;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import ch.ivyteam.api.IvyScriptVisibility;
import ch.ivyteam.api.PublicAPI;
import ch.ivyteam.ivy.location.ILocationService;
import ch.ivyteam.ivy.persistence.PersistencyException;
import ch.ivyteam.ivy.security.EMailNotificationKind;
import ch.ivyteam.ivy.security.IRole;
import ch.ivyteam.ivy.security.ISecurityContext;
import ch.ivyteam.ivy.security.IUser;
import ch.ivyteam.ivy.security.IUserAbsence;
import ch.ivyteam.ivy.security.IUserEMailNotificationSettings;
import ch.ivyteam.ivy.security.IUserSubstitute;
import ch.ivyteam.ivy.security.IUserToken;
import ch.ivyteam.ivy.security.SubstitutionType;

public class MyIUser implements IUser {

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String getDisplayName() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

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
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void addRole(IRole role) throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public IUserAbsence createAbsence(Date startDateInclusive,
			Date stopDateInclusive, String description)
			throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public IUserSubstitute createSubstitute(IUser mySubstitute,
			IRole forThisRole, String description) throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void deleteAbsence(IUserAbsence absence) throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void deleteSubstitute(IUserSubstitute substitute)
			throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public List<IUserAbsence> getAbsences() throws PersistencyException {
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
	public List<IRole> getAllRoles() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String getEMailAddress() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public Locale getEMailLanguage() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public EnumSet<EMailNotificationKind> getEMailNotificationKind()
			throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public IUserEMailNotificationSettings getEMailNotificationSettings()
			throws PersistencyException {
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
	public String getFullName() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String getName() throws PersistencyException {
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
	public List<IRole> getRoles() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public List<IUserSubstitute> getSubstitutes() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public List<IUserSubstitute> getSubstitutions() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public IUserToken getUserToken() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public boolean isAbsent() throws PersistencyException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.HIDDEN)
	public boolean isPropertyBacked(String name) throws PersistencyException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public String removeProperty(String name) throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void removeRole(IRole role) throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void setEMailAddress(String eMailAddress)
			throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void setEMailLanguage(Locale defaultLanguage)
			throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@Deprecated
	public void setEMailNotificationKind(EnumSet<EMailNotificationKind> arg0)
			throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public void setEMailNotificationSettings(
			IUserEMailNotificationSettings settings)
			throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.HIDDEN)
	public void setExternalSecurityName(String externalSecurityName)
			throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void setFullName(String fullUserName) throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void setPassword(String password) throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public void setProperty(String name, String value)
			throws PersistencyException {
		// TODO Auto-generated method stub

	}

	@Override
	public ILocationService locations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUserSubstitute createSubstitute(IUser mySubstitute, IRole forThisRole, String description,
			SubstitutionType type) throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IUserSubstitute> getActiveSubstitutions() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

}
