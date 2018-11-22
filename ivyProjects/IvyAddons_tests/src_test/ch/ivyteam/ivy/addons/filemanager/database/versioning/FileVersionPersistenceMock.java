package ch.ivyteam.ivy.addons.filemanager.database.versioning;


import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileVersion;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileVersionPersistence;
import ch.ivyteam.ivy.addons.filemanager.document.FilemanagerItemMetaData;
import ch.ivyteam.ivy.addons.filemanager.util.MethodArgumentsChecker;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.Time;

public class FileVersionPersistenceMock implements IFileVersionPersistence {
	
	boolean returnedBooleanForMethodWasFileVersionArchived;

	public FileVersionPersistenceMock(boolean returnedBooleanForMethodWasFileVersionArchived) {
		this.returnedBooleanForMethodWasFileVersionArchived = returnedBooleanForMethodWasFileVersionArchived;
	}
	
	@Override
	public FileVersion create(FileVersion itemToCreate) throws Exception {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheArgumentIsNull("The FileVersion object to create cannot be null.", itemToCreate);
		itemToCreate.setId(1);
		itemToCreate.setDate(new Date());
		itemToCreate.setTime(new Time());
		return itemToCreate;
	}

	@Override
	public FileVersion update(FileVersion itemToSave) throws Exception {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheArgumentIsNull("The FileVersion object to update cannot be null.", itemToSave);
		if(itemToSave.getFileid()<=0) {
			throw new IllegalArgumentException("The FileVersion to update is not related to any active document. Its fileid is zero. This is vorbidden.");
		}
		return itemToSave;
	}

	@Override
	public FileVersion get(String uniqueDescriptor) throws Exception {
		return makeFileVersion(null,0,0,0);
	}

	@Override
	public FileVersion get(long id) throws Exception {
		FileVersion fv = makeFileVersion(null,0,0,0);
		fv.setId(id);
		return fv;
	}

	@Override
	public boolean delete(FileVersion itemToDelete) throws Exception {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheArgumentIsNull("The fileVersion to delete cannot be null.", itemToDelete);
		return true;
	}

	@Override
	public List<FileVersion> getFileVersions(long fileId) throws Exception {
		return makeFileVersionsList(4, fileId);
	}

	@Override
	public int getNextVersionNumberForFile(long fileId) throws Exception {
		return 0;
	}

	@Override
	public FileVersion createNewVersion(long fileId,
			FilemanagerItemMetaData filemanagerItemMetaData) throws Exception {
		return null;
	}

	@Override
	public DocumentOnServer getParentFileWithoutContent(long fileId)
			throws Exception {
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID(String.valueOf(fileId));
		doc.setFilename("aTest.pdf");
		doc.setVersionnumber(2);
		doc.setPath("root/myDir/aTest.pdf");
		return doc;
	}

	@Override
	public FileVersion getFileVersionWithParentFileIdAndVersionNumber(
			long fileId, int versionNumber) throws Exception {
		return null;
	}

	@Override
	public List<FileVersion> extractVersionsToPath(long parentFileId,
			String _path) throws Exception {
		return null;
	}

	@Override
	public FileVersion getFileVersionWithJavaFile(long fileVersionId)
			throws Exception {
		return null;
	}

	@Override
	public void deleteAllVersionsFromFile(long fileId) throws Exception {
		
	}

	@Override
	public FileVersion rollbackLastVersionAsActiveDocument(long fileId)
			throws Exception {
		return new FileVersion();
	}
	
	private FileVersion makeFileVersion(String filename, int id, long fileId, int versionnumber) {
		FileVersion fv = new FileVersion();
		fv.setId(id>0?id:1);
		fv.setFileid(fileId>0?fileId:1256);
		fv.setDate(new Date());
		fv.setTime(new Time());
		fv.setFilename(filename!=null?filename:"test.doc");
		fv.setVersionNumber(versionnumber>0?versionnumber:2);
		return fv;
	}
	
	private List<FileVersion> makeFileVersionsList(int listSize, long fileId) {
		int i = listSize>0?listSize:2;
		List<FileVersion> l = new ArrayList<FileVersion>();
		for(int j = i; j > 0 ; j--) {
			l.add(makeFileVersion("aFile.docx", 1000+j, fileId, j));
		}
		return l;
	}

	@Override
	public boolean wasFileVersionArchived(long fileid, int versionnumber)
			throws Exception {
		if(fileid <= 0 || versionnumber <= 0) {
			throw new IllegalArgumentException("The fileid and the versionnumber parameters must not be zero or less than zero.");
		}
		return this.returnedBooleanForMethodWasFileVersionArchived;
	}

}
