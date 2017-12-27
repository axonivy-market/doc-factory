package ch.ivyteam.ivy.addons.filemanager.database.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;

public class DocumentSearchCriterias implements Serializable {
	
	protected static final String EXCEPTION_MESSAGE_IF_FILEPATH_BLANK = "The filePath criteria must be specified for document search";

	private static final long serialVersionUID = -6898515349628123984L;
	
	private String fileTypeName;
	private List<String> tagsName = new ArrayList<>();
	private String filePath;
	
	private DocumentSearchCriterias() {
		// should not be instantiated
	}
	
	public static DocumentSearchCriterias withFilePathPattern(String filePathSearchCriteria) {
		API.checkNotBlank(filePathSearchCriteria, EXCEPTION_MESSAGE_IF_FILEPATH_BLANK);
		
		DocumentSearchCriterias documentSearchCriteria = new DocumentSearchCriterias();
		documentSearchCriteria.filePath = PathUtil.formatPath(filePathSearchCriteria);
		return documentSearchCriteria;
	}
	
	public DocumentSearchCriterias havingFileType(String fileTypeName) {
		this.fileTypeName = fileTypeName;
		return this;
	}
	
	public DocumentSearchCriterias havingAllTags(String ... tags) {
		if(tags.length == 0) {
			return this;
		}
		Arrays.asList(tags).stream().filter(tag -> (!StringUtils.isBlank(tag) && !tagsName.contains(tag))).forEach(tag -> tagsName.add(tag));
		return this;
	}
	
	public DocumentSearchCriterias clearTags() {
		tagsName.clear();
		return this;
	}
	
	public DocumentSearchCriterias clearAllExcludingFilePath() {
		fileTypeName = null;
		tagsName.clear();
		return this;
	}
	
	public DocumentSearchCriterias setFilePathSearchCriteria(String filePathSearchCriteria) {
		API.checkNotBlank(filePathSearchCriteria, EXCEPTION_MESSAGE_IF_FILEPATH_BLANK);
		this.filePath = filePathSearchCriteria;
		return this;
	}

	public String getFileTypeName() {
		return fileTypeName;
	}

	public List<String> getTagsName() {
		return tagsName;
	}

	public String getFilePathSearchCriteria() {
		return filePath;
	}


}
