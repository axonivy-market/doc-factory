package ch.ivyteam.ivy.addons.filemanager.database;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.db.FilemanagerTestDataMaker;
import ch.ivyteam.ivy.addons.filemanager.database.db.HsqlDBFilemanager;
import ch.ivyteam.ivy.addons.filemanager.database.search.DocumentCreationDateSearch;
import ch.ivyteam.ivy.environment.Ivy;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Ivy.class)
public class FileStoreDBHandlerGetDocumentsFilteredByTest {
	
	private final static String DOCUMENT_TABLE_FULL_QUALIFIED_NAME = "uploadedfiles";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	HsqlDBFilemanager filemanager =  new HsqlDBFilemanager();
	
	DocumentOnServerSQLPersistence docPersistence;
	
	@Mock
	BasicConfigurationController config;
	
	@Mock 
	ch.ivyteam.log.Logger logger;
	
	FileStoreDBHandler fileStoreDBHandler;
	
	private Set<Long> documentsIdsInTestDb = new HashSet<>();
	
	private final static String FILETYPENAME_CONDITION = "BILL";
	private final static String FILETAGNAME_CONDITION = "STATUS=ACCEPTED";
	private DocumentCreationDateSearch dateCriteriaToday;
	
	
	@BeforeClass
	public static void start() {
		HsqlDBFilemanager.startServer();
	}
	
	@AfterClass
	public static void shutdown() {
		HsqlDBFilemanager.shutdownServer();
	}

	@Before
	public void setUp() throws Exception {
		logger = mock(ch.ivyteam.log.Logger.class);
		Mockito.doNothing().when(logger).debug(any(String.class));
		
		mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(logger);
		DocumentCreationDateSearch.equalTo(Date.from(Instant.now()));
		docPersistence = new DocumentOnServerSQLPersistence(config, filemanager);
		docPersistence.setFileTableNamSpace(DOCUMENT_TABLE_FULL_QUALIFIED_NAME);
		
		fileStoreDBHandler = new FileStoreDBHandler(config, docPersistence, null);
		
		dateCriteriaToday = DocumentCreationDateSearch.equalTo(Date.from(Instant.now()));
		
		initDbData();
	}
	
	private void initDbData() {
		FilemanagerTestDataMaker.clearTestDb();
		documentsIdsInTestDb.clear();
		
		long typeId1 = FilemanagerTestDataMaker.insertFileType("KLARA", "BILL");
		long typeId2 = FilemanagerTestDataMaker.insertFileType("KLARA", "PAYSLIP");
		
		FilemanagerTestDataMaker.insertDocumentWithOptionalTagsInTestDb("test1.doc", "luz/tenant1/employees/test1.doc", typeId1, "STATUS=REVIEW_PENDING", "DOC_TYPE=OFFICE/WORD");
		FilemanagerTestDataMaker.insertDocumentWithOptionalTagsInTestDb("test2.doc", "luz/tenant1/employees/test2.doc", typeId2, "STATUS=ACCEPTED", "DOC_TYPE=OFFICE/WORD");
		FilemanagerTestDataMaker.insertDocumentWithOptionalTagsInTestDb("test3.doc", "luz/tenant2/employees/test3.doc", typeId2, "STATUS=REVIEW_PENDING", "DOC_TYPE=OFFICE/WORD", "EMPLOYEE=ALL");
		FilemanagerTestDataMaker.insertDocumentWithOptionalTagsInTestDb("test4.doc", "luz/tenant2/employees/test4.doc", typeId1, "STATUS=ACCEPTED", "DOC_TYPE=OFFICE/WORD", "EMPLOYEE=88");
		FilemanagerTestDataMaker.insertDocumentWithOptionalTagsInTestDb("test5.doc", "luz/tenant3/employees/test5.doc", typeId2, "STATUS=ACCEPTED", "DOC_TYPE=OFFICE/WORD", "EMPLOYEE=44");
		FilemanagerTestDataMaker.insertDocumentWithOptionalTagsInTestDb("test6.pdf", "luz/tenant3/employees/test6.pdf", typeId1, "DOC_TYPE=PDF");
		FilemanagerTestDataMaker.insertDocumentWithOptionalTagsInTestDb("test7.doc", "luz/tenant1/employees/test7.doc", typeId2, "DOC_TYPE=OFFICE/WORD");
	}

	@Test
	public void getDocumentsFilteredby_with_null_filepath() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		fileStoreDBHandler.getDocumentsFilteredby(null, FILETYPENAME_CONDITION, FILETAGNAME_CONDITION, dateCriteriaToday);
	}
	
	@Test
	public void getDocumentsFilteredby_with_empty_filepath() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		fileStoreDBHandler.getDocumentsFilteredby(" ", FILETYPENAME_CONDITION, FILETAGNAME_CONDITION, dateCriteriaToday);
	}
	
	@Test
	public void getDocumentsFilteredby_with_wildcard_filepath_and_tag() throws Exception {
		ArrayList<DocumentOnServer> result = fileStoreDBHandler.getDocumentsFilteredby("luz/tenant1/%", null, "DOC_TYPE=OFFICE/WORD", null);
		for(DocumentOnServer doc: result) {
			System.out.println(doc.getFileID() + " " + doc.getFilename());
		}
		assertThat(result.size(), is(3));
	}
	
	


}
