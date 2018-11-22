package ch.ivyteam.ivy.addons.filemanager.database.search;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hamcrest.core.IsNot;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.configuration.FileManagerFullQualifiedTableNamesBuilder;
import ch.ivyteam.ivy.addons.filemanager.database.db.FilemanagerTestDataMaker;
import ch.ivyteam.ivy.addons.filemanager.database.db.HsqlDBFilemanager;
import ch.ivyteam.ivy.addons.filemanager.database.sql.DatabaseMetaDataAnalyzer;

public class DocumentSearchServiceTest {
	
	private final static String DOCUMENT_TABLE_FULL_QUALIFIED_NAME = "uploadedfiles";
	private final static String FILETYPES_TABLE_FULL_QUALIFIED_NAME = "filetype";
	private final static String FILETAGS_TABLE_FULL_QUALIFIED_NAME = "tags";
	
	HsqlDBFilemanager filemanager =  new HsqlDBFilemanager();
	
	@Mock
	DatabaseMetaDataAnalyzer databaseMetaDataAnalyzer;
	
	@Mock
	FileManagerFullQualifiedTableNamesBuilder fileManagerFullQualifiedTableNamesBuilder;
	
	DocumentSearchService documentSearchService;
	
	private Set<Long> documentsIdsInTestDb = new HashSet<>();
	
	private long docId1, docId2, docId3, docId4, docId5, docId6, docId7;
	
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
		databaseMetaDataAnalyzer = mock(DatabaseMetaDataAnalyzer.class);
		when(databaseMetaDataAnalyzer.getEscapeChar()).thenReturn("\\");
		
		fileManagerFullQualifiedTableNamesBuilder = mock(FileManagerFullQualifiedTableNamesBuilder.class);
		when(fileManagerFullQualifiedTableNamesBuilder.getDocumentFullQualifiedTableName()).thenReturn(DOCUMENT_TABLE_FULL_QUALIFIED_NAME);
		when(fileManagerFullQualifiedTableNamesBuilder.getFileTypeFullQualifiedTableName()).thenReturn(FILETYPES_TABLE_FULL_QUALIFIED_NAME);
		when(fileManagerFullQualifiedTableNamesBuilder.getFileTagsFullQualifiedTableName()).thenReturn(FILETAGS_TABLE_FULL_QUALIFIED_NAME);
		
		documentSearchService = new DocumentSearchService(filemanager, fileManagerFullQualifiedTableNamesBuilder, databaseMetaDataAnalyzer);
		
		initDbData();
	}

	private void initDbData() {
		FilemanagerTestDataMaker.clearTestDb();
		documentsIdsInTestDb.clear();
		
		long typeId1 = FilemanagerTestDataMaker.insertFileType("KLARA", "BILL");
		long typeId2 = FilemanagerTestDataMaker.insertFileType("KLARA", "PAYSLIP");
		
		docId1 = FilemanagerTestDataMaker.insertDocumentWithOptionalTagsInTestDb("test1.doc", "luz/tenant1/employees/test1.doc", typeId1, "STATUS=REVIEW_PENDING", "DOC_TYPE=OFFICE/WORD");
		docId2 = FilemanagerTestDataMaker.insertDocumentWithOptionalTagsInTestDb("test2.doc", "luz/tenant1/employees/test2.doc", typeId2, "STATUS=ACCEPTED", "DOC_TYPE=OFFICE/WORD");
		docId3 = FilemanagerTestDataMaker.insertDocumentWithOptionalTagsInTestDb("test3.doc", "luz/tenant2/employees/test3.doc", typeId2, "STATUS=REVIEW_PENDING", "DOC_TYPE=OFFICE/WORD", "EMPLOYEE=ALL");
		docId4 = FilemanagerTestDataMaker.insertDocumentWithOptionalTagsInTestDb("test4.doc", "luz/tenant2/employees/test4.doc", typeId1, "STATUS=ACCEPTED", "DOC_TYPE=OFFICE/WORD", "EMPLOYEE=88");
		docId5 = FilemanagerTestDataMaker.insertDocumentWithOptionalTagsInTestDb("test5.doc", "luz/tenant3/employees/test5.doc", typeId2, "STATUS=ACCEPTED", "DOC_TYPE=OFFICE/WORD", "EMPLOYEE=44");
		docId6 = FilemanagerTestDataMaker.insertDocumentWithOptionalTagsInTestDb("test6.pdf", "luz/tenant3/employees/test6.pdf", typeId1, "DOC_TYPE=PDF");
		docId7 = FilemanagerTestDataMaker.insertDocumentWithOptionalTagsInTestDb("test7.doc", "luz/tenant1/employees/test7.doc", typeId2, "DOC_TYPE=OFFICE/WORD");
	}
	
	@Test
	public void search_with_generic_filepath_returns_all_documents() throws Exception {
		List<DocumentOnServer> docs = documentSearchService.search(DocumentSearchCriterias.withFilePathPattern("%"));
		assertThat(docs.size(), is(7));
	}
	
	@Test
	public void search_with_filePath() throws Exception {
		List<DocumentOnServer> docs = documentSearchService.search(DocumentSearchCriterias.withFilePathPattern("luz/tenant3/%"));
		assertThat(docs.size(), is(2));
	}
	
	@Test
	public void search_with_filetype() throws Exception {
		List<DocumentOnServer> docs = documentSearchService.search(DocumentSearchCriterias.withFilePathPattern("%").havingFileType("PAYSLIP"));
		assertThat(docs.size(), is(4));
	}
	
	@Test
	public void search_with_filePath_filetype() throws Exception {
		List<DocumentOnServer> docs = documentSearchService.search(DocumentSearchCriterias.withFilePathPattern("luz/tenant3/%").havingFileType("PAYSLIP"));
		assertThat(docs.size(), is(1));
	}
	
	@Test
	public void search_with_filePath_filetype_not_found() throws Exception {
		List<DocumentOnServer> docs = documentSearchService.search(DocumentSearchCriterias.withFilePathPattern("luz/tenant3/%").havingFileType("FILETYPE_NOT_IN_DB"));
		assertThat(docs.size(), is(0));
	}
	
	@Test
	public void search_with_filetags() throws Exception {
		DocumentSearchCriterias searchCriterias = DocumentSearchCriterias.withFilePathPattern("%").havingAllTags("DOC_TYPE=OFFICE/WORD", "STATUS=ACCEPTED");
		List<DocumentOnServer> docs = documentSearchService.search(searchCriterias);
		assertThat(docs.size(), is(3));
		
		searchCriterias.clearTags();
		docs = documentSearchService.search(searchCriterias);
		assertThat(docs.size(), is(7));
		
		searchCriterias.havingAllTags("DOC_TYPE=OFFICE/WORD");
		docs = documentSearchService.search(searchCriterias);
		assertThat(docs.size(), is(6));
	}
	
	@Test
	public void search_with_filePath_filetype_tags() throws Exception {
		List<DocumentOnServer> docs = documentSearchService.search(
				DocumentSearchCriterias
				.withFilePathPattern("luz/tenant3/%")
				.havingFileType("PAYSLIP")
				.havingAllTags("EMPLOYEE=44", "STATUS=ACCEPTED")
				);
		assertThat(docs.size(), is(1));
	}
	
	@Test
	public void search_with_filePath_filetype_oneTagNotFound() throws Exception {
		List<DocumentOnServer> docs = documentSearchService.search(
				DocumentSearchCriterias
				.withFilePathPattern("luz/tenant3/%")
				.havingFileType("PAYSLIP")
				.havingAllTags("EMPLOYEE=ALL_NOT_FOUND", "STATUS=ACCEPTED")
				);
		assertThat(docs.size(), is(0));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void searchWithIds_IAE_if_ids_array_null() throws Exception {
		documentSearchService.searchWithIds(null);
	}
	
	@Test
	public void searchWithIds_returns_empty_collection_if_ids_array_empty() throws Exception {
		long[] ids = new long[0];
		assertThat(documentSearchService.searchWithIds(ids).size(), is(0));
	}
	
	@Test
	public void searchWithIds_returns_docs_found() throws Exception {
		List<DocumentOnServer> docs = documentSearchService.searchWithIds(docId1, docId3);
		assertThat(docs.size(), is(2));
		
		assertThat(docs.get(0).getId(), is(docId1));
		assertThat(docs.get(1).getId(), is(docId3));
	}
	
	@Test
	public void searchWithIds_returns_documents_sorted_by_id_asc() throws Exception {
		List<DocumentOnServer> docs = documentSearchService.searchWithIds(docId7, docId6, docId5, docId3, docId4, docId2, docId1);
		assertThat(docs.size(), is(7));
		
		assertThat(docs.get(0).getId(), is(docId1));
		assertThat(docs.get(1).getId(), is(docId2));
		assertThat(docs.get(2).getId(), is(docId3));
		assertThat(docs.get(3).getId(), is(docId4));
		assertThat(docs.get(4).getId(), is(docId5));
		assertThat(docs.get(5).getId(), is(docId6));
		assertThat(docs.get(6).getId(), is(docId7));
	}
	
	@Test
	public void searchWithIds_ignores_duplicated_ids() throws Exception {
		List<DocumentOnServer> docs = documentSearchService.searchWithIds(docId1, docId3, docId1, docId3, docId2);
		assertThat(docs.size(), is(3));
		
		assertThat(docs.get(0).getId(), is(docId1));
		assertThat(docs.get(1).getId(), is(docId2));
		assertThat(docs.get(2).getId(), is(docId3));
	}
	
	@Test
	public void searchWithIds_ignores_not_present_searchIds() throws Exception {
		long IdNotPresent1 = 846512555;
		long IdNotPresent2 = 5462132;
		List<DocumentOnServer> docs = documentSearchService.searchWithIds(docId1, IdNotPresent1, docId3, IdNotPresent2);
		assertThat(docs.size(), is(2));
		
		assertThat(docs.get(0).getId(), is(docId1));
		assertThat(docs.get(1).getId(), is(docId3));
		
		assertThat(docs.get(0).getId(), IsNot.not(IdNotPresent1));
		assertThat(docs.get(0).getId(), IsNot.not(IdNotPresent2));
		assertThat(docs.get(1).getId(), IsNot.not(IdNotPresent1));
		assertThat(docs.get(1).getId(), IsNot.not(IdNotPresent2));
	}
	
	

}
