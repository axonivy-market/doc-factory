package ch.ivyteam.ivy.addons.filemanager.database.filelink;

import ch.ivyteam.ivy.addons.filemanager.document.FieldValueUtil;
import ch.ivyteam.ivy.addons.filemanager.util.MethodArgumentsChecker;

public final class FileLinkValidator {
	
	private FileLinkValidator() {}
	
	public static void throwIllegalArgumentExceptionIfInvalid(FileLink fileLink, boolean checkId) {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheArgumentIsNull("The FileLink cannot be null.", fileLink);
		
		if(fileLink.getDirectoryId() == 0) {
			throw new IllegalArgumentException("The FileLink is invalid. Its directory id must be set.");
		}
		
		if(!FieldValueUtil.isStringValidLongId(fileLink.getFileID())) {
			throw new IllegalArgumentException("The FileLink is invalid. It  must have its referenced FileId set.");
		}
		
		if(checkId && fileLink.getFileLinkId() <= 0) {
			throw new IllegalArgumentException("The FileLink is invalid. Its id must be greater than 0.");
		}
	}
	
}
