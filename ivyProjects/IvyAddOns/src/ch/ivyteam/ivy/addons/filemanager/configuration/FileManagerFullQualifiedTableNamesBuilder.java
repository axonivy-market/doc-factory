package ch.ivyteam.ivy.addons.filemanager.configuration;

import org.apache.commons.lang.StringUtils;

import ch.ivyteam.api.API;

public class FileManagerFullQualifiedTableNamesBuilder {
	
	private String schemaName;
	private String documentTableName;
	private String documentContentTableName;
	
	private String languageTableName;
	
	private String directoriesTableName;
	private String directoriesTranslationTableName;
	
	private String fileTagsTableName;
	
	private String fileTypeTableName;
	private String fileTypeTranslationTableName;
	
	private String fileVersionsTableName;
	private String fileVersionsContentTableName;
	private String fileVersionsArchiveTrackingTableName;
	
	private String fileLinksTableName;
	
	private String fileActionsHistoryTableName;
	private String fileActionsTypeTableName;
	
	
	public FileManagerFullQualifiedTableNamesBuilder(BasicConfigurationController config) {
		API.checkNotNull(config, "The configuration parameter cannot be null.");
		
		this.schemaName = config.getDatabaseSchemaName();
		setDocumentTables(config);
		this.languageTableName = config.getLanguageTableName();
		setDirectoriesTable(config);
		this.fileTagsTableName = config.getFileTagsTableName();
		setFileTypeTables(config);
		setFileVersionTables(config);
		this.fileLinksTableName = config.getFileLinkTableName();
		setFileActionTables(config);
	}
	
	public FileManagerFullQualifiedTableNamesBuilder setSchemaName(String schemaName) {
		API.checkNotNull(schemaName, "The schema name cannot be null");
		this.schemaName = schemaName.trim();
		return this;
	}

	private void setDocumentTables(BasicConfigurationController config) {
		this.documentTableName = config.getFilesTableName();
		this.documentContentTableName = config.getFilesContentTableName();
	}
	
	private void setDirectoriesTable(BasicConfigurationController config) {
		this.directoriesTableName = config.getDirectoriesTableName();
		this.directoriesTranslationTableName = config.getDirectoriesTranslationTableName();
	}
	
	private void setFileTypeTables(BasicConfigurationController config) {
		this.fileTypeTableName = config.getFileTypeTableName();
		this.fileTypeTranslationTableName = config.getFileTypesTranslationTableName();
	}
	
	private void setFileVersionTables(BasicConfigurationController config) {
		this.fileVersionsTableName = config.getFilesVersionTableName();
		this.fileVersionsContentTableName = config.getFilesVersionContentTableName();
		this.fileVersionsArchiveTrackingTableName = config.getFilesVersionArchiveTrackingTableName();
	}

	private void setFileActionTables(BasicConfigurationController config) {
		if(config.getFileActionHistoryConfiguration() == null) {
			return;
		}
		this.fileActionsHistoryTableName = config.getFileActionHistoryConfiguration().getFileActionHistoryTableName();
		this.fileActionsTypeTableName = config.getFileActionHistoryConfiguration().getFileActionTypeTableName();
	}

	public String getSchemaName() {
		return schemaName;
	}

	public String getDocumentFullQualifiedTableName() {
		return makeFullQualifiedTableNameWithSchema(this.documentTableName);
	}

	public String getDocumentContentFullQualifiedTableName() {
		return makeFullQualifiedTableNameWithSchema(this.documentContentTableName);
	}

	public String getLanguageFullQualifiedTableName() {
		return makeFullQualifiedTableNameWithSchema(this.languageTableName);
	}

	public String getDirectoriesFullQualifiedTableName() {
		return makeFullQualifiedTableNameWithSchema(this.directoriesTableName);
	}

	public String getDirectoriesTranslationFullQualifiedTableName() {
		return makeFullQualifiedTableNameWithSchema(this.directoriesTranslationTableName);
	}

	public String getFileTagsFullQualifiedTableName() {
		return makeFullQualifiedTableNameWithSchema(this.fileTagsTableName);
	}

	public String getFileTypeFullQualifiedTableName() {
		return makeFullQualifiedTableNameWithSchema(this.fileTypeTableName);
	}

	public String getFileTypeTranslationFullQualifiedTableName() {
		return makeFullQualifiedTableNameWithSchema(this.fileTypeTranslationTableName);
	}

	public String getFileVersionsFullQualifiedTableName() {
		return makeFullQualifiedTableNameWithSchema(this.fileVersionsTableName);
	}

	public String getFileVersionsContentFullQualifiedTableName() {
		return makeFullQualifiedTableNameWithSchema(this.fileVersionsContentTableName);
	}

	public String getFileVersionsArchiveTrackingFullQualifiedTableName() {
		return makeFullQualifiedTableNameWithSchema(this.fileVersionsArchiveTrackingTableName);
	}

	public String getFileLinksFullQualifiedTableName() {
		return makeFullQualifiedTableNameWithSchema(this.fileLinksTableName);
	}

	public String getFileActionsHistoryFullQualifiedTableName() {
		return makeFullQualifiedTableNameWithSchema(this.fileActionsHistoryTableName);
	}

	public String getFileActionsTypeFullQualifiedTableName() {
		return makeFullQualifiedTableNameWithSchema(this.fileActionsTypeTableName);
	}
	
	private boolean hasSchema() {
		return StringUtils.isNotBlank(this.schemaName);
	}
	
	private String makeFullQualifiedTableNameWithSchema(String tableName) {
		if(hasSchema()) {
			return (new StringBuilder(this.schemaName).append('.').append(tableName)).toString();
		}
		return tableName;
	}
	
}
