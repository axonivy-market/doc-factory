package ch.ivyteam.ivy.addons.filemanager.database.security;

import java.util.HashSet;
import java.util.Set;

import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.scripting.objects.List;

public class DirectorySecurityUtil {

	protected static List<String> getRolesInFolderForUpdateFile(FolderOnServer fos) {
		List<String> roles = List.create(String.class);
		Set<String> rolesSet = new HashSet<String>();
		rolesSet.addAll(fos.getCwf());
		rolesSet.addAll(fos.getCuf());
		rolesSet.addAll(fos.getCdf());
		roles.addAll(rolesSet);
		return roles;
	}
	
	protected static List<String> getRolesInFolderForWriteFile(FolderOnServer fos) {
		List<String> roles = List.create(String.class);
		Set<String> rolesSet = new HashSet<String>();
		rolesSet.addAll(fos.getCwf());
		rolesSet.addAll(fos.getCdf());
		roles.addAll(rolesSet);
		return roles;
	}
	
	protected static List<String> getRolesInFolderForCreateFile(FolderOnServer fos) {
		List<String> roles = List.create(String.class);
		Set<String> rolesSet = new HashSet<String>();
		rolesSet.addAll(fos.getCwf());
		rolesSet.addAll(fos.getCcf());
		rolesSet.addAll(fos.getCdf());
		roles.addAll(rolesSet);
		return roles;
	}
	
	protected static List<String> getRolesInFolderForCreateDirectory(FolderOnServer fos) {
		List<String> roles = List.create(String.class);
		Set<String> rolesSet = new HashSet<String>();
		rolesSet.addAll(fos.getCud());
		rolesSet.addAll(fos.getCcd());
		roles.addAll(rolesSet);
		return roles;
	}
	
	protected static List<String> getRolesInFolderForRenameDirectory(FolderOnServer fos) {
		List<String> roles = List.create(String.class);
		Set<String> rolesSet = new HashSet<String>();
		rolesSet.addAll(fos.getCud());
		rolesSet.addAll(fos.getCrd());
		rolesSet.addAll(fos.getCtd());
		roles.addAll(rolesSet);
		return roles;
	}
	
	protected static List<String> getRolesInFolderForOpenDirectory(FolderOnServer fos) {
		List<String> roles = List.create(String.class);
		Set<String> rolesSet = new HashSet<String>();
		rolesSet.addAll(fos.getCud());
		rolesSet.addAll(fos.getCrd());
		rolesSet.addAll(fos.getCcd());
		rolesSet.addAll(fos.getCcf());
		rolesSet.addAll(fos.getCdd());
		rolesSet.addAll(fos.getCdf());
		rolesSet.addAll(fos.getCod());
		rolesSet.addAll(fos.getCtd());
		rolesSet.addAll(fos.getCuf());
		rolesSet.addAll(fos.getCwf());
		roles.addAll(rolesSet);
		return roles;
	}
	
	protected static List<String> getRolesInFolderForRight(FolderOnServer fos, SecurityRightsEnum rightType) {
		if(fos==null){
			return null;
		}
		initializeFolderOnServerNullListsOfRoles(fos);
		switch(rightType) {
		case MANAGE_SECURITY_RIGHT:
			return fos.getCmrd();
		case OPEN_DIRECTORY_RIGHT:
			return getRolesInFolderForOpenDirectory(fos);
		case UPDATE_DIRECTORY_RIGHT:
			return fos.getCud();
		case CREATE_DIRECTORY_RIGHT:
			return getRolesInFolderForCreateDirectory(fos);
		case RENAME_DIRECTORY_RIGHT:
			return getRolesInFolderForRenameDirectory(fos);
		case TRANSLATE_DIRECTORY_RIGHT:
			return fos.getCtd();
		case DELETE_DIRECTORY_RIGHT:
			return fos.getCdd();
		case WRITE_FILES_RIGHT:
			return getRolesInFolderForWriteFile(fos);
		case CREATE_FILES_RIGHT:
			return getRolesInFolderForCreateFile(fos);
		case UPDATE_FILES_RIGHT:
			return getRolesInFolderForUpdateFile(fos);
		case DELETE_FILES_RIGHT:
			return fos.getCdf();
		default:
			return List.create(String.class);
		}
	}
	
	/**
	 * Ensure the rights integrity is followed in the different rights lists of the given FolderOnServer
	 * @param fos the directory represented by a ch.ivyteam.ivy.addons.filemanager.FolderOnServer object
	 * @return the ch.ivyteam.ivy.addons.filemanager.FolderOnServer object which rights lists follow the following rules:
	 * <ul>
	 * <li> If a Role is in the admin list, then it will be present in all the other lists (it has all the rights),
	 * <li> If a Role is in the "delete directory list", then it will be present in the "open directory" and "update directory" lists,
	 * <li> If a Role is in the "update directory list", then it will be present in the "open directory" list,
	 * <li> If a Role is in the "delete files list", then it will be present in the "open directory" and "write files" lists,
	 * <li> If a Role is in the "write files list", then it will be present in the "open directory".
	 * </ul>
	 * If the given FolderOnServer was null, then null is returned.
	 */
	public static FolderOnServer ensureRightsIntegrityInDirectory(String adminRoleName, FolderOnServer fos) {
		if (fos == null) {
			return null;
		}
		initializeFolderOnServerNullListsOfRoles(fos);

		// CHeck integrity in the rights

		// ensure admin is in it + administrators should have all the rights
		fos.setCmrd(putRoleInListIfNotPresent(adminRoleName,fos.getCmrd()));
		for (String s : fos.getCmrd()) {
			if (!fos.getCdd().contains(s)) {
				fos.getCdd().add(s);
			}
			if (!fos.getCud().contains(s)) {
				fos.getCud().add(s);
			}
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
			if (!fos.getCdf().contains(s)) {
				fos.getCdf().add(s);
			}
			if (!fos.getCwf().contains(s)) {
				fos.getCwf().add(s);
			}
			if (!fos.getCcd().contains(s)) {
				fos.getCcd().add(s);
			}
			if (!fos.getCrd().contains(s)) {
				fos.getCrd().add(s);
			}
			if (!fos.getCtd().contains(s)) {
				fos.getCtd().add(s);
			}
			if (!fos.getCcf().contains(s)) {
				fos.getCcf().add(s);
			}
			if (!fos.getCuf().contains(s)) {
				fos.getCuf().add(s);
			}
		}

		// Who can delete a directory should be able to open and update it, and if old security version rename it
		for (String s : fos.getCdd()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
			if (!fos.getCud().contains(s)) {
				fos.getCud().add(s);
			}
			if (!fos.getCrd().contains(s)) {
				fos.getCrd().add(s);
			}
		}
		
		// Who can translate a directory should be able to open, rename it
		for (String s : fos.getCtd()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
			if (!fos.getCrd().contains(s)) {
				fos.getCrd().add(s);
			}
		}

		// Who can rename a directory should be able to open
		for (String s : fos.getCrd()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
		}

		// Who can create a directory should be able to open and update it
		for (String s : fos.getCcd()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
		}

		// Who can update a directory should be able to open it and if old security version rename and create it
		for (String s : fos.getCud()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
			if (!fos.getCrd().contains(s)) {
				fos.getCrd().add(s);
			}
			if (!fos.getCcd().contains(s)) {
				fos.getCcd().add(s);
			}
		}
		
		// Who can create a file should be able to open the directory
		for (String s : fos.getCcf()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
		}
		
		// Who can create a file should be able to open the directory
		for (String s : fos.getCuf()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
		}

		// Who can delete a file should be able to update it and to open the
		// directory
		for (String s : fos.getCdf()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
			if (!fos.getCwf().contains(s)) {
				fos.getCwf().add(s);
			}
			if (!fos.getCcf().contains(s)) {
				fos.getCcf().add(s);
			}
			if (!fos.getCuf().contains(s)) {
				fos.getCuf().add(s);
			}
		}

		// Who can write a file should be able to open the directory and if old security version rename and create it
		for (String s : fos.getCwf()) {
			if (!fos.getCod().contains(s)) {
				fos.getCod().add(s);
			}
			if (!fos.getCuf().contains(s)) {
				fos.getCuf().add(s);
			}
			if (!fos.getCcf().contains(s)) {
				fos.getCcf().add(s);
			}
		}

		return fos;
	}
	
	private static void initializeFolderOnServerNullListsOfRoles(FolderOnServer fos) {
		if (fos.getCmrd() == null) {
			fos.setCmrd(List.create(String.class));
		}
		if (fos.getCod() == null) {
			fos.setCod(List.create(String.class));
		}
		if (fos.getCud() == null) {
			fos.setCud(List.create(String.class));
		}
		if (fos.getCdd() == null) {
			fos.setCdd(List.create(String.class));
		}
		if (fos.getCdf() == null) {
			fos.setCdf(List.create(String.class));
		}
		if (fos.getCwf() == null) {
			fos.setCwf(List.create(String.class));
		}
		if (fos.getCcd() == null) {
			fos.setCcd(List.create(String.class));
		}
		if (fos.getCrd() == null) {
			fos.setCrd(List.create(String.class));
		}
		if (fos.getCtd() == null) {
			fos.setCtd(List.create(String.class));
		}
		if (fos.getCcf() == null) {
			fos.setCcf(List.create(String.class));
		}
		if (fos.getCuf() == null) {
			fos.setCuf(List.create(String.class));
		}
	}
	
	/**
	 * Takes a list of names of Ivy Roles and:<br>
	 * . Creates a new List<String> if this List of names of Ivy Roles is null,
	 * . Adds the given Role name in the list if it is not already in it.
	 * @param roleName: the role name
	 * @param roles: the initial List<String> of names of Ivy Roles
	 * @return the List<String> of names of Ivy Roles containing the given Role name if it exists.
	 */
	public static List<String> putRoleInListIfNotPresent(String roleName, List<String> roles) {
		if(roles==null) {
			roles=List.create(String.class);
		}
		
		if(roleName!=null && !roles.contains(roleName)) {
			roles.add(roleName);
		}
		return roles;
	}
}
