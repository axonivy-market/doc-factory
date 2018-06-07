package ch.ivyteam.ivy.addons.filemanager.database.search;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.*;


import ch.ivyteam.ivy.addons.filemanager.configuration.FileManagerFullQualifiedTableNamesBuilder;

public class DocumentSearchQueryBuilderTest {
	
	private final static String DOCUMENT_TABLE_FULL_QUALIFIED_NAME = "public.uploadedfiles";
	private final static String FILETYPES_TABLE_FULL_QUALIFIED_NAME = "public.filetypes";
	private final static String FILETAGS_TABLE_FULL_QUALIFIED_NAME = "public.filetags";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	
	@Mock
	private FileManagerFullQualifiedTableNamesBuilder fileManagerFullQualifiedTableNamesBuilder;
	
	private DocumentSearchCriterias criteria;
	
	private String ESCAPE_CHAR = "\\";
	
	@Before
	public void setup() {
		fileManagerFullQualifiedTableNamesBuilder = mock(FileManagerFullQualifiedTableNamesBuilder.class);
		when(fileManagerFullQualifiedTableNamesBuilder.getDocumentFullQualifiedTableName()).thenReturn(DOCUMENT_TABLE_FULL_QUALIFIED_NAME);
		when(fileManagerFullQualifiedTableNamesBuilder.getFileTypeFullQualifiedTableName()).thenReturn(FILETYPES_TABLE_FULL_QUALIFIED_NAME);
		when(fileManagerFullQualifiedTableNamesBuilder.getFileTagsFullQualifiedTableName()).thenReturn(FILETAGS_TABLE_FULL_QUALIFIED_NAME);
		
		criteria = DocumentSearchCriterias.withFilePathPattern("path/to/search");
	}

	@Test(expected = IllegalArgumentException.class)
	public void illegalArgumentException_in_buildForPreparedStatement_if_Criteria_is_null() {
		DocumentSearchCriterias criteria = null;
		
		DocumentSearchQueryBuilder.buildPreparedStatementQuery(criteria, fileManagerFullQualifiedTableNamesBuilder, ESCAPE_CHAR);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void illegalArgumentException_in_buildForPreparedStatement_if_FileManagerFullQualifiedTableNamesBuilder_is_null() {
		DocumentSearchQueryBuilder.buildPreparedStatementQuery(criteria, null, ESCAPE_CHAR);
	}
	
	@Test
	public void buildForPreparedStatement_with_only_filepath_criteria() {
		criteria = DocumentSearchCriterias.withFilePathPattern("path/to/search");
		
		String query = DocumentSearchQueryBuilder.buildPreparedStatementQuery(criteria, fileManagerFullQualifiedTableNamesBuilder, ESCAPE_CHAR);
		assertThat(query, is("SELECT * FROM public.uploadedfiles WHERE public.uploadedfiles.filepath LIKE ? ESCAPE '\\'"));
	}
	
	@Test
	public void buildForPreparedStatement_with_filepath_and_fileType_criteria() {
		criteria = DocumentSearchCriterias.withFilePathPattern("path/to/search").havingFileType("BILL");
		
		String query = DocumentSearchQueryBuilder.buildPreparedStatementQuery(criteria, fileManagerFullQualifiedTableNamesBuilder, ESCAPE_CHAR);
		assertThat(query, is("SELECT * FROM public.uploadedfiles "
				+ "INNER JOIN public.filetypes ON public.uploadedfiles.filetypeid = public.filetypes.id AND filetype.name LIKE ? ESCAPE '\\' "
				+ "WHERE public.uploadedfiles.filepath LIKE ? ESCAPE '\\'")
				);
	}
	
	@Test
	public void buildForPreparedStatement_with_filepath_and_nullfileType_criteria_no_filetype_in_query() {
		criteria = DocumentSearchCriterias.withFilePathPattern("path/to/search").havingFileType(null);
		
		String query = DocumentSearchQueryBuilder.buildPreparedStatementQuery(criteria, fileManagerFullQualifiedTableNamesBuilder, ESCAPE_CHAR);
		assertThat(query, is("SELECT * FROM public.uploadedfiles "
				+ "WHERE public.uploadedfiles.filepath LIKE ? ESCAPE '\\'")
				);
	}
	
	@Test
	public void buildForPreparedStatement_with_filepath_and_emptyfileType_criteria_no_filetype_in_query() {
		criteria = DocumentSearchCriterias.withFilePathPattern("path/to/search").havingFileType(" ");
		
		String query = DocumentSearchQueryBuilder.buildPreparedStatementQuery(criteria, fileManagerFullQualifiedTableNamesBuilder, ESCAPE_CHAR);
		assertThat(query, is("SELECT * FROM public.uploadedfiles "
				+ "WHERE public.uploadedfiles.filepath LIKE ? ESCAPE '\\'")
				);
	}
	
	@Test
	public void buildForPreparedStatement_with_filepath_and_fileTags_criteria() {
		criteria = DocumentSearchCriterias.withFilePathPattern("path/to/search").havingAllTags("TYPE=INVOICE", "STATUS=APPROVED");
		
		String query = DocumentSearchQueryBuilder.buildPreparedStatementQuery(criteria, fileManagerFullQualifiedTableNamesBuilder, ESCAPE_CHAR);
		assertThat(query, is("SELECT * FROM public.uploadedfiles "
				+ "INNER JOIN public.filetags ON public.uploadedfiles.fileid = public.filetags.fileid "
				+ 	"AND ? IN (SELECT tag FROM public.filetags WHERE public.uploadedfiles.fileid = public.filetags.fileid ) "
				+ 	"AND ? IN (SELECT tag FROM public.filetags WHERE public.uploadedfiles.fileid = public.filetags.fileid ) "
				+ "WHERE public.uploadedfiles.filepath LIKE ? ESCAPE '\\'"));
	}
	
	@Test
	public void buildForPreparedStatement_with_filepath_and_nullOrEmptyfileTags_criterias_no_innerjoin_tag_in_query() {
		criteria = DocumentSearchCriterias.withFilePathPattern("path/to/search").havingAllTags("", " ", null);
		
		String query = DocumentSearchQueryBuilder.buildPreparedStatementQuery(criteria, fileManagerFullQualifiedTableNamesBuilder, ESCAPE_CHAR);
		assertThat(query, is("SELECT * FROM public.uploadedfiles "
				+ "WHERE public.uploadedfiles.filepath LIKE ? ESCAPE '\\'"));
	}
	
	@Test
	public void buildForPreparedStatement_with_filepath_andfiletype_and_fileTags_criteria() {
		criteria = DocumentSearchCriterias.withFilePathPattern("path/to/search").havingFileType("BILL").havingAllTags("TYPE=INVOICE", "STATUS=APPROVED");
		
		String query = DocumentSearchQueryBuilder.buildPreparedStatementQuery(criteria, fileManagerFullQualifiedTableNamesBuilder, ESCAPE_CHAR);
		assertThat(query, is("SELECT * FROM public.uploadedfiles "
				+ "INNER JOIN public.filetypes ON public.uploadedfiles.filetypeid = public.filetypes.id AND filetype.name LIKE ? ESCAPE '\\' "
				+ "INNER JOIN public.filetags ON public.uploadedfiles.fileid = public.filetags.fileid "
				+ 	"AND ? IN (SELECT tag FROM public.filetags WHERE public.uploadedfiles.fileid = public.filetags.fileid ) "
				+ 	"AND ? IN (SELECT tag FROM public.filetags WHERE public.uploadedfiles.fileid = public.filetags.fileid ) "
				+ "WHERE public.uploadedfiles.filepath LIKE ? ESCAPE '\\'"));
	}

}
