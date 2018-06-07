package ch.ivyteam.ivy.addons.filemanager.database.filetag;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ch.ivyteam.ivy.addons.filemanager.FileTag;
import ch.ivyteam.ivy.addons.filemanager.database.db.FilemanagerTestDataMaker;
import ch.ivyteam.ivy.addons.filemanager.database.db.HsqlDBFilemanager;

public class FileTagsControllerTest {
	
	FileTagsController fileTagsController;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private long fileId = 1;
	private final static String OLD_TAG_VALUE = "DOC_TYPE=OFFICE/WORD";
	private final static String NEW_TAG_VALUE = "DOC_TYPE=PDF";
	
	private final static String FILETAGS_TABLE_FULL_QUALIFIED_NAME = "tags";
	
	HsqlDBFilemanager filemanager =  new HsqlDBFilemanager();
	
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
		fileTagsController = new FileTagsController(new FileTagSQLPersistence(filemanager, FILETAGS_TABLE_FULL_QUALIFIED_NAME));
		
		FilemanagerTestDataMaker.clearTestDb();
		long typeId1 = FilemanagerTestDataMaker.insertFileType("KLARA", "BILL");
		fileId = FilemanagerTestDataMaker.insertDocumentWithOptionalTagsInTestDb("test1.doc", "luz/tenant1/employees/test1.doc", typeId1, "STATUS=REVIEW_PENDING", OLD_TAG_VALUE);
	}

	@Test
	public void updateFileTag_throw_IAE_if_fileId_is_zero() throws Exception {
		
		thrown.expect(IllegalArgumentException.class);
		fileTagsController.updateFileTag(0, OLD_TAG_VALUE, NEW_TAG_VALUE);
	}
	
	@Test
	public void updateFileTag_throw_IAE_if_fileId_is_less_then_zero() throws Exception {
		
		thrown.expect(IllegalArgumentException.class);
		fileTagsController.updateFileTag(-5, OLD_TAG_VALUE, NEW_TAG_VALUE);
	}
	
	@Test
	public void updateFileTag_throw_IAE_if_oldTag_is_null() throws Exception {
		
		thrown.expect(IllegalArgumentException.class);
		fileTagsController.updateFileTag(fileId, null, NEW_TAG_VALUE);
	}
	
	@Test
	public void updateFileTag_throw_IAE_if_oldTag_is_empty() throws Exception {
		
		thrown.expect(IllegalArgumentException.class);
		fileTagsController.updateFileTag(fileId, " ", NEW_TAG_VALUE);
	}
	
	@Test
	public void updateFileTag_throw_IAE_if_newTag_is_null() throws Exception {
		
		thrown.expect(IllegalArgumentException.class);
		fileTagsController.updateFileTag(fileId, OLD_TAG_VALUE, null);
	}
	
	@Test
	public void updateFileTag_throw_IAE_if_newTag_is_empty() throws Exception {
		
		thrown.expect(IllegalArgumentException.class);
		fileTagsController.updateFileTag(fileId, OLD_TAG_VALUE, " ");
	}
	
	@Test
	public void updateFileTag_returns_null_if_no_fileTag_for_fileId_and_tagValue_exists() throws Exception {
		FileTag tag = fileTagsController.getTag(fileId, "NOT EXISTING TAG VALUE");
		assertThat(tag, IsNull.nullValue());
		
		FileTag result = fileTagsController.updateFileTag(fileId, "NOT EXISTING TAG VALUE", NEW_TAG_VALUE);
		assertThat(result, IsNull.nullValue());
	}
	
	@Test
	public void updateFileTag_returns_updateFileTag() throws Exception {
		FileTag fileTagToUpdate = fileTagsController.getTag(fileId, OLD_TAG_VALUE);
		assertThat(fileTagToUpdate, IsNull.notNullValue());
		
		FileTag result = fileTagsController.updateFileTag(fileId, OLD_TAG_VALUE, NEW_TAG_VALUE);
		assertThat(result.getFileId(), Is.is(fileTagToUpdate.getFileId()));
		assertThat(result.getId(), Is.is(fileTagToUpdate.getId()));
		assertThat(result.getTag(), Is.is(NEW_TAG_VALUE));
	}
	
}
