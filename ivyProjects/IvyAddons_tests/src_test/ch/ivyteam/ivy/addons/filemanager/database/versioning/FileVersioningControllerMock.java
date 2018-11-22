package ch.ivyteam.ivy.addons.filemanager.database.versioning;

import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileVersionPersistence;

public class FileVersioningControllerMock extends FileVersioningController {
	
	public FileVersioningControllerMock(IFileVersionPersistence p) {
		super(p);
	}
	
	public void setActivateArchiveProtection(boolean b) {
		super.setActivateArchiveProtection(b);
	}
	

	@Override
	public boolean rollbackLastVersionAsActiveDocument(long fileId)
			throws FileVersionProtectedException, Exception {
		if(super.wasFileVersionArchived(fileId, 2)) {
			throw new FileVersionProtectedException("");
		}
		return true;
	}
}