package ch.ivyteam.ivy.addons.filemanager.database.search;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.filemanager.configuration.FileManagerFullQualifiedTableNamesBuilder;

class DocumentSearchQueryBuilder {

	static String buildPreparedStatementQuery(DocumentSearchCriterias criteria, FileManagerFullQualifiedTableNamesBuilder fileManagerTablesNamespace, String escapeChar) {
		API.checkNotNull(criteria, "The DocumentSearchCriteria cannot be null.");
		API.checkNotNull(fileManagerTablesNamespace, "The FileManagerTablesNamespace cannot be null.");
		API.checkNotEmpty(escapeChar, "The escape character for the sql queries cannot be blank.");
		StringBuilder sb = new StringBuilder(makeSelectDocumentsQuery(fileManagerTablesNamespace)).
				append(getFileTypeQueryPart(criteria, fileManagerTablesNamespace, escapeChar)).
				append(getFileTagsQueryPart(criteria, fileManagerTablesNamespace)).
				append(getFilepathQueryPart(criteria, fileManagerTablesNamespace, escapeChar));
		
		return sb.toString();
	}

	public static String makeSelectDocumentsQuery(FileManagerFullQualifiedTableNamesBuilder fileManagerTablesNamespace) {
		return new StringBuilder("SELECT * FROM ").
				append(fileManagerTablesNamespace.getDocumentFullQualifiedTableName()).toString();
	}
	
	

	private static String getFileTagsQueryPart(DocumentSearchCriterias criteria, FileManagerFullQualifiedTableNamesBuilder fileManagerTablesNamespace) {
		if(criteria.getTagsName().isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder(" INNER JOIN ").
				append(fileManagerTablesNamespace.getFileTagsFullQualifiedTableName()).
				append(" ON ").
				append(fileManagerTablesNamespace.getDocumentFullQualifiedTableName()).
				append(".fileid = ").
				append(fileManagerTablesNamespace.getFileTagsFullQualifiedTableName()).
				append(".fileid");
		criteria.getTagsName().forEach(tag -> sb.append(" AND ? IN (SELECT tag FROM ").
				append(fileManagerTablesNamespace.getFileTagsFullQualifiedTableName()).append(" WHERE ").
				append(fileManagerTablesNamespace.getDocumentFullQualifiedTableName()).append(".fileid = ").
				append(fileManagerTablesNamespace.getFileTagsFullQualifiedTableName()).append(".fileid )")
				);
		return sb.toString();
	}

	private static String getFileTypeQueryPart(DocumentSearchCriterias criteria, FileManagerFullQualifiedTableNamesBuilder fileManagerTablesNamespace, String escapeChar) {
		if(StringUtils.isBlank(criteria.getFileTypeName())) {
			return "";
		}
		StringBuilder sb = new StringBuilder(" INNER JOIN ").
				append(fileManagerTablesNamespace.getFileTypeFullQualifiedTableName()).
				append(" ON ").
				append(fileManagerTablesNamespace.getDocumentFullQualifiedTableName()).append(".filetypeid = ").
				append(fileManagerTablesNamespace.getFileTypeFullQualifiedTableName()).append(".id AND filetype.name LIKE ? ESCAPE '").append(escapeChar).append("'");
		return sb.toString();
	}

	private static String getFilepathQueryPart(DocumentSearchCriterias criteria, FileManagerFullQualifiedTableNamesBuilder fileManagerTablesNamespace, String escapeChar) {
		StringBuilder sb = new StringBuilder(" WHERE ")
		.append(fileManagerTablesNamespace.getDocumentFullQualifiedTableName())
		.append(".filepath LIKE ? ESCAPE '").append(escapeChar).append("'");
		return sb.toString();

	}

}
