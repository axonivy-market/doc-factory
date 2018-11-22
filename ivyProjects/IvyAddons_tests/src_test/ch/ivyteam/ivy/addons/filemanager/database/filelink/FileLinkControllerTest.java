package ch.ivyteam.ivy.addons.filemanager.database.filelink;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileLinkPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFolderOnServerPersistence;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.log.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Ivy.class})
public class FileLinkControllerTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	IFileLinkPersistence fileLinkPersistence;
	
	@Mock
	IFolderOnServerPersistence folderPersistence;

	@Before
	public void setUp() throws Exception {
		Logger mockLogger = mock(Logger.class);
		doNothing().when(mockLogger).error(any(String.class));
		doNothing().when(mockLogger).info(any(String.class));
		doNothing().when(mockLogger).debug(any(String.class));
		doNothing().when(mockLogger).warn(any(String.class));
		
		mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mockLogger);
		
		fileLinkPersistence = mock(IFileLinkPersistence.class);
		folderPersistence = mock(IFolderOnServerPersistence.class);
	}

	@Test
	public void getFileLink_thow_IAE_if_id_less_than_one() throws Exception {
		FileLinkController flc = new FileLinkController();
		
		thrown.expect(IllegalArgumentException.class);
		flc.getFileLink(0);
	}
	
	@Test
	public void getFileLink_with_path_thow_IAE_if_path_null() throws Exception {
		FileLinkController flc = new FileLinkController();
		
		thrown.expect(IllegalArgumentException.class);
		flc.getFileLink(null);
	}
	
	@Test
	public void getFileLink_with_path_thow_IAE_if_path_empty() throws Exception {
		FileLinkController flc = new FileLinkController();
		
		thrown.expect(IllegalArgumentException.class);
		flc.getFileLink(" ");
	}
	
	@Test
	public void getFileLinkWithJavaFile_thow_IAE_if_id_less_than_one() throws Exception {
		FileLinkController flc = new FileLinkController();
		
		thrown.expect(IllegalArgumentException.class);
		flc.getFileLinkWithJavaFile(0);
	}
	
	@Test
	public void getFileLinksInDirectory_thow_IAE_if_id_less_than_one() throws Exception {
		FileLinkController flc = new FileLinkController();
		
		thrown.expect(IllegalArgumentException.class);
		flc.getFileLinksInDirectory(0, false);
	}
	
	@Test
	public void getFileLinksForFile_thow_IAE_if_id_less_than_one() throws Exception {
		FileLinkController flc = new FileLinkController();
		
		thrown.expect(IllegalArgumentException.class);
		flc.getFileLinksForFile(0);
	}
	
	@Test
	public void getFileLinksByFileIdAndVersionNumber_thow_IAE_if_fileId_less_than_one() throws Exception {
		FileLinkController flc = new FileLinkController();
		long fileId = 0;
		int versionNumber = 1;
		
		thrown.expect(IllegalArgumentException.class);
		flc.getFileLinksByFileIdAndVersionNumber(fileId, versionNumber);
	}
	
	@Test
	public void getFileLinksByFileIdAndVersionNumber_thow_IAE_if_versionNumber_less_than_one() throws Exception {
		FileLinkController flc = new FileLinkController();
		long fileId = 1;
		int versionNumber = 0;
		
		thrown.expect(IllegalArgumentException.class);
		flc.getFileLinksByFileIdAndVersionNumber(fileId, versionNumber);
	}
	
	@Test
	public void updateFileLinksVersionId_thow_IAE_if_fileId_less_than_one() throws Exception {
		FileLinkController flc = new FileLinkController();
		long fileId = 0;
		int versionNumber = 1;
		
		thrown.expect(IllegalArgumentException.class);
		flc.updateFileLinksVersionId(fileId, versionNumber);
	}
	
	@Test
	public void updateFileLinksVersionId_thow_IAE_if_versionNumber_less_than_one() throws Exception {
		FileLinkController flc = new FileLinkController();
		long fileId = 1;
		int versionNumber = 0;
		
		thrown.expect(IllegalArgumentException.class);
		flc.updateFileLinksVersionId(fileId, versionNumber);
	}
	
	@Test
	public void getFileLinksUnderPath_thow_IAE_if_path_null() throws Exception {
		FileLinkController flc = new FileLinkController();
		
		thrown.expect(IllegalArgumentException.class);
		flc.getFileLinksUnderPath(null, false);
	}
	
	@Test
	public void getFileLinksUnderPath_thow_IAE_if_path_empty() throws Exception {
		FileLinkController flc = new FileLinkController();
		
		thrown.expect(IllegalArgumentException.class);
		flc.getFileLinksUnderPath(" ", false);
	}
	
	@Test
	public void updateFileLink_thow_IAE_if_FileLink_is_null() throws Exception {
		FileLinkController flc = new FileLinkController();
		
		thrown.expect(IllegalArgumentException.class);
		flc.updateFileLink(null);
	}
	
	@Test
	public void updateFileLink_thow_IAE_if_FileLink_FileLinkId_not_set() throws Exception {
		FileLinkController flc = new FileLinkController();
		FileLink fileLinkToUpdate = new FileLink();
		fileLinkToUpdate.setFileLinkId(0);
		fileLinkToUpdate.setDirectoryId(564);
		fileLinkToUpdate.setFileID("213");
		
		thrown.expect(IllegalArgumentException.class);
		flc.updateFileLink(fileLinkToUpdate);
	}
	
	@Test
	public void updateFileLink_thow_IAE_if_FileLink_directoryId_not_set() throws Exception {
		FileLinkController flc = new FileLinkController();
		FileLink fileLinkToUpdate = new FileLink();
		fileLinkToUpdate.setFileLinkId(456);
		fileLinkToUpdate.setDirectoryId(0);
		fileLinkToUpdate.setFileID("213");
		
		thrown.expect(IllegalArgumentException.class);
		flc.updateFileLink(fileLinkToUpdate);
	}
	
	@Test
	public void updateFileLink_thow_IAE_if_FileLink_FIleID_not_set() throws Exception {
		FileLinkController flc = new FileLinkController();
		FileLink fileLinkToUpdate = new FileLink();
		fileLinkToUpdate.setFileLinkId(456);
		fileLinkToUpdate.setDirectoryId(456);
		fileLinkToUpdate.setFileID("");
		
		thrown.expect(IllegalArgumentException.class);
		flc.updateFileLink(fileLinkToUpdate);
	}
	
	@Test
	public void createFileLinkForDocumentOnServer_thow_IAE_if_documentOnServer_is_null() throws Exception {
		FileLinkController flc = new FileLinkController();
		
		thrown.expect(IllegalArgumentException.class);
		flc.createFileLinkForDocumentOnServer(null, "optionalFileLinkName", "optionalDirectoryPath");
	}
	
	@Test
	public void createFileLinkForDocumentOnServer_thow_IAE_if_documentOnServer_id_is_null() throws Exception {
		FileLinkController flc = new FileLinkController();
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID(null);
		doc.setFilename("aFile.txt");
		doc.setPath("aPath/aFile.txt");
		
		thrown.expect(IllegalArgumentException.class);
		flc.createFileLinkForDocumentOnServer(doc, "optionalFileLinkName", "optionalDirectoryPath");
	}
	
	@Test
	public void createFileLinkForDocumentOnServer_thow_IAE_if_documentOnServer_id_is_empty() throws Exception {
		FileLinkController flc = new FileLinkController();
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID(" ");
		doc.setFilename("aFile.txt");
		doc.setPath("aPath/aFile.txt");
		
		thrown.expect(IllegalArgumentException.class);
		flc.createFileLinkForDocumentOnServer(doc, "optionalFileLinkName", "optionalDirectoryPath");
	}
	
	@Test
	public void createFileLinkForDocumentOnServer_thow_IAE_if_documentOnServer_id_is_NaN() throws Exception {
		FileLinkController flc = new FileLinkController();
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("abc");
		doc.setFilename("aFile.txt");
		doc.setPath("aPath/aFile.txt");
		
		thrown.expect(IllegalArgumentException.class);
		flc.createFileLinkForDocumentOnServer(doc, "optionalFileLinkName", "optionalDirectoryPath");
	}
	
	@Test
	public void createFileLinkForDocumentOnServer_thow_IAE_if_documentOnServer_fileName_is_null() throws Exception {
		FileLinkController flc = new FileLinkController();
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("abc");
		doc.setFilename(null);
		doc.setPath("aPath/aFile.txt");
		
		thrown.expect(IllegalArgumentException.class);
		flc.createFileLinkForDocumentOnServer(doc, "optionalFileLinkName", "optionalDirectoryPath");
	}
	
	@Test
	public void createFileLinkForDocumentOnServer_thow_IAE_if_documentOnServer_fileName_is_empty() throws Exception {
		FileLinkController flc = new FileLinkController();
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("abc");
		doc.setFilename(" ");
		doc.setPath("aPath/aFile.txt");
		
		thrown.expect(IllegalArgumentException.class);
		flc.createFileLinkForDocumentOnServer(doc, "optionalFileLinkName", "optionalDirectoryPath");
	}
	
	@Test
	public void createFileLinkForDocumentOnServer_thow_IllegalStateException_if_the_directory_where_to_create_link_cannot_be_found() throws Exception {
		FileLinkController flc = new FileLinkController(fileLinkPersistence, folderPersistence);
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("10");
		doc.setFilename("aFile.txt");
		doc.setPath("aPath/aFile.txt");
		
		when(folderPersistence.get(any(String.class))).thenReturn(null);
		
		thrown.expect(IllegalStateException.class);
		flc.createFileLinkForDocumentOnServer(doc, "optionalFileLinkName", "optionalDirectoryPath");
	}
	
	@Test
	public void createFileLinkForDocumentOnServer_no_exception_thrown() throws Exception {
		FileLinkController flc = new FileLinkController(fileLinkPersistence, folderPersistence);
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("10");
		doc.setFilename("aFile.txt");
		doc.setPath("aPath/aFile.txt");
		
		when(folderPersistence.get(any(String.class))).thenReturn(makeFolder());
		when(fileLinkPersistence.create(any(FileLink.class))).thenReturn(makeFileLink());
		
		flc.createFileLinkForDocumentOnServer(doc, "optionalFileLinkName", "optionalDirectoryPath");
	}

	private FileLink makeFileLink() {
		return new FileLink();
	}

	private FolderOnServer makeFolder() {
		FolderOnServer fos = new FolderOnServer();
		fos.setId(156);
		return fos;
	}
	
	
	
}
