package ch.ivyteam.ivy.addons.filemanager.database.search;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.junit.Test;

public class DocumentSearchCriteriasTest {
	
	private final static String FILE_PATH_SEARCH_PATTERN = "/abc/def/%";
	private final static String FILE_TYPE_SEARCHED_NAME = "BILL";

	@Test(expected = IllegalArgumentException.class)
	public void withFilePathPattern_IAE_if_filepath_null() {
		DocumentSearchCriterias.withFilePathPattern(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void withFilePathPattern_IAE_if_filepath_empty() {
		DocumentSearchCriterias.withFilePathPattern(" ");
	}
	
	@Test
	public void getFilePathSearchCriteria_returns_filePath_Set() {
		DocumentSearchCriterias documentSearchCriterias = DocumentSearchCriterias.withFilePathPattern(FILE_PATH_SEARCH_PATTERN);
		
		assertThat(documentSearchCriterias.getFilePathSearchCriteria(), is(FILE_PATH_SEARCH_PATTERN));
	}
	
	@Test
	public void clearAllExcludingFilePath_does_not_clear_filePath() {
		DocumentSearchCriterias documentSearchCriterias = DocumentSearchCriterias.withFilePathPattern(FILE_PATH_SEARCH_PATTERN);
		
		documentSearchCriterias.clearAllExcludingFilePath();
		
		assertThat(documentSearchCriterias.getFilePathSearchCriteria(), is(FILE_PATH_SEARCH_PATTERN));
	}
	
	@Test
	public void getFileTypeName_returns_null_if_not_set() {
		DocumentSearchCriterias documentSearchCriterias = DocumentSearchCriterias.withFilePathPattern(FILE_PATH_SEARCH_PATTERN);
		
		assertThat(documentSearchCriterias.getFileTypeName(), nullValue());
	}
	
	@Test
	public void getFileTypeName_returns_fileTypeName_set() {
		DocumentSearchCriterias documentSearchCriterias = DocumentSearchCriterias.withFilePathPattern(FILE_PATH_SEARCH_PATTERN);
		
		documentSearchCriterias.havingFileType(null);
		assertThat(documentSearchCriterias.getFileTypeName(), nullValue());
		
		documentSearchCriterias.havingFileType("");
		assertThat(documentSearchCriterias.getFileTypeName(), is(""));
		
		documentSearchCriterias.havingFileType(" ");
		assertThat(documentSearchCriterias.getFileTypeName(), is(" "));
		
		documentSearchCriterias.havingFileType(FILE_TYPE_SEARCHED_NAME);
		assertThat(documentSearchCriterias.getFileTypeName(), is(FILE_TYPE_SEARCHED_NAME));
	}
	
	@Test
	public void clearAllExcludingFilePath_clears_fileTypeName() {
		DocumentSearchCriterias documentSearchCriterias = DocumentSearchCriterias.withFilePathPattern(FILE_PATH_SEARCH_PATTERN);
		
		documentSearchCriterias.havingFileType(FILE_TYPE_SEARCHED_NAME);
		assertThat(documentSearchCriterias.getFileTypeName(), is(FILE_TYPE_SEARCHED_NAME));
		
		documentSearchCriterias.clearAllExcludingFilePath();
		assertThat(documentSearchCriterias.getFileTypeName(), nullValue());
	}
	
	@Test
	public void getTagsName_returns_emptyList_if_not_set() {
		DocumentSearchCriterias documentSearchCriterias = DocumentSearchCriterias.withFilePathPattern(FILE_PATH_SEARCH_PATTERN);
		
		Set<String> tagsName = documentSearchCriterias.getTagsName();
		
		assertThat(tagsName.size(), is(0));
	}
	
	@Test
	public void getTagsName_returns_tags() {
		DocumentSearchCriterias documentSearchCriterias = DocumentSearchCriterias.withFilePathPattern(FILE_PATH_SEARCH_PATTERN);
		
		documentSearchCriterias.havingAllTags("TAG1", "TAG2");
		
		assertThat(documentSearchCriterias.getTagsName(), hasItems("TAG1", "TAG2"));
	}
	
	@Test
	public void havingAllTags_does_not_insert_duplicate_tags() {
		DocumentSearchCriterias documentSearchCriterias = DocumentSearchCriterias.withFilePathPattern(FILE_PATH_SEARCH_PATTERN);
		
		documentSearchCriterias.havingAllTags("TAG1", "TAG2", "TAG1", "TAG2");
		
		assertThat(documentSearchCriterias.getTagsName(), hasItems("TAG1", "TAG2"));
		assertThat(documentSearchCriterias.getTagsName().size(), is(2));
	}
	
	@Test
	public void havingAllTags_insert_tags_calling_havingAllTags_join_tags() {
		DocumentSearchCriterias documentSearchCriterias = DocumentSearchCriterias.withFilePathPattern(FILE_PATH_SEARCH_PATTERN);
		
		documentSearchCriterias.havingAllTags("TAG1", "TAG2");
		
		assertThat(documentSearchCriterias.getTagsName(), hasItems("TAG1", "TAG2"));
		assertThat(documentSearchCriterias.getTagsName().size(), is(2));
		
		documentSearchCriterias.havingAllTags("TAG3", "TAG4");
		assertThat(documentSearchCriterias.getTagsName(), hasItems("TAG1", "TAG2", "TAG3", "TAG4"));
		assertThat(documentSearchCriterias.getTagsName().size(), is(4));
	}
	
	@Test
	public void clearTags_clear_the_tags() {
		DocumentSearchCriterias documentSearchCriterias = DocumentSearchCriterias.withFilePathPattern(FILE_PATH_SEARCH_PATTERN);
		
		documentSearchCriterias.havingAllTags("TAG1", "TAG2");
		
		assertThat(documentSearchCriterias.getTagsName(), hasItems("TAG1", "TAG2"));
		
		documentSearchCriterias.clearTags();
		
		assertThat(documentSearchCriterias.getTagsName().size(), is(0));
	}
	
	@Test
	public void clearAllExcludingFilePath_clear_the_tags() {
		DocumentSearchCriterias documentSearchCriterias = DocumentSearchCriterias.withFilePathPattern(FILE_PATH_SEARCH_PATTERN);
		
		documentSearchCriterias.havingAllTags("TAG1", "TAG2");
		
		assertThat(documentSearchCriterias.getTagsName(), hasItems("TAG1", "TAG2"));
		
		documentSearchCriterias.clearAllExcludingFilePath();
		
		assertThat(documentSearchCriterias.getTagsName().size(), is(0));
	}
	
	

}
