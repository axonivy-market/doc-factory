package ch.ivyteam.ivy.addons.filemanager.database.search;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.filemanager.util.PathUtil;

public final class DocumentSearchCriterias implements Serializable {
	
	protected static final String EXCEPTION_MESSAGE_IF_FILEPATH_BLANK = "The filePath criteria must be specified for document search";

	private static final long serialVersionUID = -6898515349628123984L;
	
	private String fileTypeName;
	private Set<String> tagsName = new HashSet<>();
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
	
	public boolean hasFileTypeCriteria() {
		return !StringUtils.isBlank(fileTypeName);
	}
	
	public DocumentSearchCriterias havingAllTags(String ... tags) {
		if(tags.length == 0) {
			return this;
		}
		Arrays.asList(tags).stream().filter(tag -> !StringUtils.isBlank(tag)).forEach(tag -> tagsName.add(tag));
		return this;
	}
	
	public DocumentSearchCriterias clearTags() {
		tagsName.clear();
		return this;
	}
	
	public boolean hasHavingAllTagsCriteria() {
		return !this.tagsName.isEmpty();
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

	public Set<String> getTagsName() {
		return tagsName;
	}

	public String getFilePathSearchCriteria() {
		return filePath;
	}


}