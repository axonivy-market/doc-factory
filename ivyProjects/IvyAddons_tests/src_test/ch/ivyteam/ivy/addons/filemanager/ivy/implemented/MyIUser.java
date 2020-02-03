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

@SuppressWarnings("deprecation")
public class MyIUser implements IUser {

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String getDisplayName() throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public long getId() {
		return 0;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.HIDDEN)
	@Deprecated
	public int getIdentifier() {
		return 0;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String getMemberName() throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public ISecurityContext getSecurityContext() throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public boolean isMember(IUserToken userToken, boolean useSessionRoles)
			throws PersistencyException {
		return false;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public boolean isUser() {
		return false;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void addRole(IRole role) throws PersistencyException {

	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public IUserAbsence createAbsence(Date startDateInclusive,
			Date stopDateInclusive, String description)
			throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public IUserSubstitute createSubstitute(IUser mySubstitute,
			IRole forThisRole, String description) throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void deleteAbsence(IUserAbsence absence) throws PersistencyException {
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void deleteSubstitute(IUserSubstitute substitute)
			throws PersistencyException {
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public List<IUserAbsence> getAbsences() throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public List<String> getAllPropertyNames() throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public List<IRole> getAllRoles() throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String getEMailAddress() throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public Locale getEMailLanguage() throws PersistencyException {
		return null;
	}

	@Override
	@Deprecated
	public EnumSet<EMailNotificationKind> getEMailNotificationKind()
			throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public IUserEMailNotificationSettings getEMailNotificationSettings()
			throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public String getExternalSecurityName() throws PersistencyException {
		return null;
	}

	@Override
	public String getExternalName() {
		return null;
	}
	
	@Override
	public String getExternalId() {
		return null;
	}
	
	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String getFullName() throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String getName() throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public String getProperty(String name) throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public List<IRole> getRoles() throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public List<IUserSubstitute> getSubstitutes() throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public List<IUserSubstitute> getSubstitutions() throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public IUserToken getUserToken() throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public boolean isAbsent() throws PersistencyException {
		return false;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.HIDDEN)
	public boolean isPropertyBacked(String name) throws PersistencyException {
		return false;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public String removeProperty(String name) throws PersistencyException {
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void removeRole(IRole role) throws PersistencyException {
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void setEMailAddress(String eMailAddress)
			throws PersistencyException {
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void setEMailLanguage(Locale defaultLanguage)
			throws PersistencyException {

	}

	@Override
	@Deprecated
	public void setEMailNotificationKind(EnumSet<EMailNotificationKind> arg0)
			throws PersistencyException {
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public void setEMailNotificationSettings(
			IUserEMailNotificationSettings settings)
			throws PersistencyException {
	}

	@Override
	@PublicAPI(IvyScriptVisibility.HIDDEN)
	public void setExternalSecurityName(String externalSecurityName)
			throws PersistencyException {
	}
	
	@Override
	public void setExternalName(String externalName) {
		
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void setFullName(String fullUserName) throws PersistencyException {
	}

	@Override
	@PublicAPI(IvyScriptVisibility.EXPERT)
	public void setPassword(String password) throws PersistencyException {
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public void setProperty(String name, String value)
			throws PersistencyException {
	}

	@Override
	public ILocationService locations() {
		return null;
	}

	@Override
	public IUserSubstitute createSubstitute(IUser mySubstitute, String description, SubstitutionType type)
			throws PersistencyException {
		return null;
	}

	@Override
	public List<IUserSubstitute> getActiveSubstitutions() throws PersistencyException {
		return null;
	}

}
