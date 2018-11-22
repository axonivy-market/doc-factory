package ch.ivyteam.ivy.addons.filemanager.database.versioning;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileVersionPersistence;

public class FileVersioningControllerFileVersionArchiveProtectionTest {
	
	@Rule
	public ExpectedException thrown= ExpectedException.none();

	@Test
	public void rollbackLastVersionAsActiveDocument_throws_FileVersionProtectedException_if_FileVersionArchiveProtectionEnabled_and_activeVersionWasAlreadyArchived() throws Exception{
		FileVersioningController fvc = new FileVersioningController(makeFileVersionPersistenceWithReturnedBooleanForMethodWasFileVersionArchived(true));
		fvc.setActivateArchiveProtection(true);
		
		thrown.expect(FileVersionProtectedException.class);
		fvc.rollbackLastVersionAsActiveDocument(2);
	}
	
	@Test
	public void rollbackLastVersionAsActiveDocument_success_if_FileVersionArchiveProtectionEnabled_and_activeVersionWasNotAlreadyArchived() throws Exception{
		FileVersioningController fvc = new FileVersioningController(makeFileVersionPersistenceWithReturnedBooleanForMethodWasFileVersionArchived(false));
		fvc.setActivateArchiveProtection(true);
		
		assertTrue("The last file version can be rolledback as active document if the version protection feature is enabled " +
				"and the active document has never been archived as version.",fvc.rollbackLastVersionAsActiveDocument(2));
	}
	
	@Test
	public void rollbackLastVersionAsActiveDocument_success_if_FileVersionArchiveProtectionDisabled() throws Exception{
		FileVersioningController fvc = new FileVersioningController(makeFileVersionPersistenceWithReturnedBooleanForMethodWasFileVersionArchived(true));
		fvc.setActivateArchiveProtection(false);
		
		assertTrue("The last file version can be rolledback as active document if the version protection feature is disabled.", fvc.rollbackLastVersionAsActiveDocument(2));
	}
	
	@Test
	public void deleteAllVersionsFromFile_throws_FileVersionProtectedException_if_FileVersionArchiveProtectionEnabled() throws Exception{
		FileVersioningController fvc = new FileVersioningController(makeFileVersionPersistenceWithReturnedBooleanForMethodWasFileVersionArchived(false));
		fvc.setActivateArchiveProtection(true);
		
		thrown.expect(FileVersionProtectedException.class);
		fvc.deleteAllVersionsFromFile(2);
	}
	
	@Test
	public void deleteAllVersionsFromFile_throws_no_exception_if_FileVersionArchiveProtectionDisabled() throws Exception{
		FileVersioningController fvc = new FileVersioningController(makeFileVersionPersistenceWithReturnedBooleanForMethodWasFileVersionArchived(false));
		fvc.setActivateArchiveProtection(false);
		
		fvc.deleteAllVersionsFromFile(2);
	}
	
	private IFileVersionPersistence makeFileVersionPersistenceWithReturnedBooleanForMethodWasFileVersionArchived(boolean wasFileVersionArchived){
		return new FileVersionPersistenceMock(wasFileVersionArchived);
	}

}
