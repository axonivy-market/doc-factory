package ch.ivyteam.ivy.addons.filemanager.database.filelink;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class FileLinkValidatorTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();


	@Test
	public void throwIllegalArgumentException_if_FileLink_null() {
		thrown.expect(IllegalArgumentException.class);
		FileLinkValidator.throwIllegalArgumentExceptionIfInvalid(null, false);
	}
	
	@Test
	public void throwIllegalArgumentException_if_FileLink_directoryId() {
		boolean checkId  = true;
		FileLink fl = makeFileLink();
		fl.setDirectoryId(0);
		
		thrown.expect(IllegalArgumentException.class);
		FileLinkValidator.throwIllegalArgumentExceptionIfInvalid(fl, checkId);
	}
	
	@Test
	public void throwIllegalArgumentException_if_FileLink_ReferencedFIlePathAndId_not_set() {
		boolean checkId  = true;
		FileLink fl = makeFileLink();
		fl.setPath("");
		fl.setFileID("");
		
		thrown.expect(IllegalArgumentException.class);
		FileLinkValidator.throwIllegalArgumentExceptionIfInvalid(fl, checkId);
	}
	
	@Test
	public void throwIllegalArgumentException_if_FileLink_id_not_set_and_checkId_true() {
		boolean checkId  = true;
		FileLink fl = makeFileLink();
		fl.setFileLinkId(0);
		
		thrown.expect(IllegalArgumentException.class);
		FileLinkValidator.throwIllegalArgumentExceptionIfInvalid(fl, checkId);
	}
	
	@Test
	public void no_IllegalArgumentException_if_FileLink_id_not_set_and_checkId_false() {
		boolean checkId  = false;
		FileLink fl = makeFileLink();
		fl.setFileLinkId(0);
		
		FileLinkValidator.throwIllegalArgumentExceptionIfInvalid(fl, checkId);
	}
	
	@Test
	public void no_IllegalArgumentException_if_FileLink_valid() {
		boolean checkId  = true;
		FileLink fl = makeFileLink();
		
		FileLinkValidator.throwIllegalArgumentExceptionIfInvalid(fl, checkId);
	}
	
	private FileLink makeFileLink() {
		FileLink fl = new FileLink();
		fl.setPath("test/test1/referecedFile.txt");
		fl.setFileID("324234");
		fl.setFileLinkId(345626);
		fl.setDirectoryId(459841);
		return fl;
	}
}
