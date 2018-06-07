package ch.ivyteam.ivy.addons.filemanager.database;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.any;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.document.filter.DocumentListFilter;

@RunWith(PowerMockRunner.class)
public class FileStoreDBHandlerDocumentListFilter {
	
	@Mock
	BasicConfigurationController config;
	
	@Mock
	DocumentOnServerSQLPersistence mockedDocPersistence;
	
	@Mock
	FolderOnServerSQLPersistence mockedDirPersistence;

	@Before
	public void setUp() throws Exception {
		config = mock(BasicConfigurationController.class);
		
		mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		when(mockedDocPersistence.getList(any(String.class), any(boolean.class))).thenReturn(makeListDocumentOnServer());
		
		mockedDirPersistence =  mock(FolderOnServerSQLPersistence.class);
		when(mockedDirPersistence.getList("root", true)).thenReturn(makeListFolderOnServer());
	}
	
	@Test
	public void getDocumentsInPath_returns_nonfiltered_documentsList_if_DocumentListFilter_notSet_in_config() throws Exception {
		when(config.getDocumentListFilter()).thenReturn(null);
		
		FileStoreDBHandler fileHandler = new FileStoreDBHandler(config,mockedDocPersistence,mockedDirPersistence);
		
		java.util.List<DocumentOnServer> docs = fileHandler.getDocumentsInPath("root", false);
		assertTrue(docs.size()==5);
	}

	@Test
	public void getDocumentsInPath_returns_filtered_documentsList_if_DocumentListFilter_set_in_config() throws Exception {
		when(config.getDocumentListFilter()).thenReturn(new DocumentListFilterAcceptsOnlyTxt());
		
		FileStoreDBHandler fileHandler = new FileStoreDBHandler(config,mockedDocPersistence,mockedDirPersistence);
		
		java.util.List<DocumentOnServer> docs = fileHandler.getDocumentsInPath("root", false);
		assertTrue(docs.size()==2);
		for(DocumentOnServer doc:docs) {
			assertTrue(doc.getFilename().endsWith(".txt"));
		}
	}
	
	private List<DocumentOnServer> makeListDocumentOnServer() {
		List<DocumentOnServer> docs = new ArrayList<DocumentOnServer>();
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("3");
		doc.setFilename("test.pdf");
		doc.setPath("root/test1/test.pdf");
		doc.setLocked("0");
		doc.setVersionnumber(4);
		docs.add(doc);
		
		doc = new DocumentOnServer();
		doc.setFileID("44");
		doc.setFilename("test.docx");
		doc.setPath("root/test2/test.docx");
		doc.setLocked("0");
		doc.setVersionnumber(4);
		docs.add(doc);
		
		doc = new DocumentOnServer();
		doc.setFileID("7896");
		doc.setFilename("gnssfgn.txt");
		doc.setPath("root/gnssfgn.txt");
		doc.setLocked("0");
		doc.setVersionnumber(1);
		docs.add(doc);
		
		doc = new DocumentOnServer();
		doc.setFileID("234");
		doc.setFilename("tiptop.docx");
		doc.setPath("root/tiptop.docx");
		doc.setLocked("0");
		doc.setVersionnumber(4);
		docs.add(doc);
		
		doc = new DocumentOnServer();
		doc.setFileID("5");
		doc.setFilename("tiptop.txt");
		doc.setPath("root/tiptop.txt");
		doc.setLocked("0");
		doc.setVersionnumber(4);
		docs.add(doc);
		
		return docs;
	}
	
	private class DocumentListFilterAcceptsOnlyTxt implements DocumentListFilter {

		@Override
		public boolean accept(DocumentOnServer doc) {
			return doc.getFilename().toLowerCase().endsWith(".txt");
		}
		
	}
	
	private List<FolderOnServer> makeListFolderOnServer() {
		List<FolderOnServer> dirs = new ArrayList<FolderOnServer>();
		FolderOnServer fos = new FolderOnServer();
		fos.setPath("root/test1");
		fos.setName("test1");
		fos.setId(223);
		dirs.add(fos);
		
		fos = new FolderOnServer();
		fos.setPath("root/test1/test");
		fos.setName("test1");
		fos.setId(224);
		dirs.add(fos);
		
		fos = new FolderOnServer();
		fos.setPath("root/test2");
		fos.setName("test2");
		fos.setId(225);
		dirs.add(fos);
		
		
		return dirs;
	}

}
