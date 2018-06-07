package ch.ivyteam.ivy.addons.filemanager.database;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;
import java.util.List;

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
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.event.restricted.SystemEventDispatcher;
import ch.ivyteam.ivy.addons.filemanager.exception.DocumentLockException;
import ch.ivyteam.ivy.addons.filemanager.exception.FileManagementException;
import ch.ivyteam.ivy.cm.IContentManagementSystem;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.event.ISystemEventDispatcher.DispatchStatus;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SystemEventDispatcher.class, Ivy.class})
public class moveDirectoryTest {

	@Mock
	BasicConfigurationController config;
	
	@Mock
	DocumentOnServerSQLPersistence mockedDocPersistence;
	
	@Mock
	FolderOnServerSQLPersistence mockedDirPersistence;
	
	@Mock
	IContentManagementSystem cms;
	
	@Rule
	ExpectedException thrown = ExpectedException.none();
	
	FileStoreDBHandler fileHandler;
	
	private final static String OLD_PATH = "root/test (1)/a_dir/myDirectory";
	private final static String DIR_NAME = "myDirectory";
	
	private final static String DESTINATION_PATH= "root/test (2)/a_dir in other place";

	@Before
	public void setUp() throws Exception {
		cms = mock(IContentManagementSystem.class);
		when(cms.co(any(String.class))).thenReturn("a message");
		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(cms);
		
		mockStatic(SystemEventDispatcher.class);
		when(SystemEventDispatcher.sendSystemEvent(any(String.class), any(String.class))).thenReturn(DispatchStatus.OK);
		
		config = mock(BasicConfigurationController.class);
		
		mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		when(mockedDocPersistence.update(any(DocumentOnServer.class))).thenReturn(new DocumentOnServer());
		
		mockedDirPersistence =  mock(FolderOnServerSQLPersistence.class);
		when(mockedDirPersistence.getList(OLD_PATH, true)).thenReturn(makeListFolderOnServer());
		when(mockedDirPersistence.update(any(FolderOnServer.class))).thenReturn(new FolderOnServer());
		
		when(mockedDirPersistence.get(DESTINATION_PATH+"/"+DIR_NAME)).thenReturn(null);
		
		fileHandler = new FileStoreDBHandler(config,mockedDocPersistence,mockedDirPersistence);
	}
	
	@Test
	public void IAE_Thrown_if_folder_parameter_is_null() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		fileHandler.moveDirectory(null, "");
	}
	
	@Test
	public void IAE_Thrown_if_folder_path_is_null() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		fileHandler.moveDirectory(makeFolderOnServerWithNameAndPath("DIR_NAME", null), DESTINATION_PATH);
	}
	
	@Test
	public void IAE_Thrown_if_folder_path_is_empty() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		fileHandler.moveDirectory(makeFolderOnServerWithNameAndPath("DIR_NAME", " "), DESTINATION_PATH);
	}
	
	@Test
	public void IAE_Thrown_if_folder_name_is_null() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		fileHandler.moveDirectory(makeFolderOnServerWithNameAndPath(null, OLD_PATH), DESTINATION_PATH);
	}
	
	@Test
	public void IAE_Thrown_if_folder_name_is_empty() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		fileHandler.moveDirectory(makeFolderOnServerWithNameAndPath(" ",OLD_PATH), DESTINATION_PATH);
	}
	
	@Test
	public void FileManagementException_Thrown_if_folder_to_move_already_exists_at_destination() throws Exception {
		when(mockedDirPersistence.get(DESTINATION_PATH+"/"+DIR_NAME)).thenReturn(new FolderOnServer());
		
		thrown.expect(FileManagementException.class);
		fileHandler.moveDirectory(makeFolderOnServerWithNameAndPath(DIR_NAME,OLD_PATH), DESTINATION_PATH);
	}
	
	@Test
	public void DocumentLockException_Thrown_if_folderToMove_has_locked_document() throws Exception {
		when(mockedDocPersistence.getList(any(String.class), any(boolean.class))).thenReturn(makeListDocumentOnServerWithLock(true));
		
		thrown.expect(DocumentLockException.class);
		fileHandler.moveDirectory(makeFolderOnServerWithNameAndPath(DIR_NAME, OLD_PATH), DESTINATION_PATH);
	}
	
	@Test
	public void success_if_directoryDoesNotExistsAtDestination_noDocumentsLocked() throws Exception {
		when(mockedDocPersistence.getList(any(String.class), any(boolean.class))).thenReturn(makeListDocumentOnServerWithLock(false));
		
		FolderOnServer fos = fileHandler.moveDirectory(makeFolderOnServerWithNameAndPath(DIR_NAME, OLD_PATH), DESTINATION_PATH);
		assert(fos.getPath().equals(DESTINATION_PATH+"/"+DIR_NAME));
	}

	private List<FolderOnServer> makeListFolderOnServer() {
		List<FolderOnServer> folders = new ArrayList<>();
		folders.add(makeFolderOnServerWithNameAndPath("dir1", OLD_PATH+"/dir1"));
		folders.add(makeFolderOnServerWithNameAndPath("dir2", OLD_PATH+"/dir2"));
		folders.add(makeFolderOnServerWithNameAndPath("dir3", OLD_PATH+"/dir3"));
		folders.add(makeFolderOnServerWithNameAndPath("dir3_4", OLD_PATH+"/dir3/dir3_4"));
		
		return folders;
	}
	
	private FolderOnServer makeFolderOnServerWithNameAndPath(String name, String path){
		FolderOnServer fos = new FolderOnServer();
		fos.setPath(path);
		fos.setName(name);
		fos.setId((int) (Math.random()*100 + 12));
		return fos;
	}
	

	private List<DocumentOnServer> makeListDocumentOnServerWithLock(Boolean isLocked) {
		List<DocumentOnServer> docs = new ArrayList<DocumentOnServer>();
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("3");
		doc.setFilename("test.pdf");
		doc.setPath(OLD_PATH+"/test.pdf");
		doc.setLocked(isLocked?"1":"0");
		doc.setIsLocked(isLocked);
		doc.setVersionnumber(4);
		docs.add(doc);
		
		doc = new DocumentOnServer();
		doc.setFileID("44");
		doc.setFilename("test.docx");
		doc.setPath(OLD_PATH+"/test.docx");
		doc.setLocked(isLocked?"1":"0");
		doc.setIsLocked(isLocked);
		doc.setVersionnumber(4);
		docs.add(doc);
		
		return docs;
	}

}
