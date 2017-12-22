package ch.ivyteam.ivy.addons.filemanager.database.search;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.configuration.FileManagerFullQualifiedTableNamesBuilder;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.sql.DatabaseMetaDataAnalyzer;
import ch.ivyteam.ivy.addons.filemanager.database.sql.PersistenceConnectionManagerReleaser;
import ch.ivyteam.ivy.addons.filemanager.restricted.database.sql.DocumentOnServerGeneratorHelper;
import ch.ivyteam.ivy.environment.Ivy;

public class DocumentSearchService {
	
	private IPersistenceConnectionManager<Connection> connectionManager =null;
	private FileManagerFullQualifiedTableNamesBuilder fileManagerFullQualifiedTableNamesBuilder;	
	DatabaseMetaDataAnalyzer databaseMetaDataAnalyzer;
	
	@SuppressWarnings("unchecked")
	public DocumentSearchService(BasicConfigurationController basicConfigurationController) {
		fileManagerFullQualifiedTableNamesBuilder = new FileManagerFullQualifiedTableNamesBuilder(basicConfigurationController);
		this.connectionManager =  (IPersistenceConnectionManager<Connection>) PersistenceConnectionManagerFactory
				.getPersistenceConnectionManagerInstance(basicConfigurationController);
		databaseMetaDataAnalyzer = new DatabaseMetaDataAnalyzer(basicConfigurationController);
	}
	
	public List<DocumentOnServer> search(DocumentSearchCriterias searchCriteria) throws Exception {
		String query = DocumentSearchQueryBuilder.build(searchCriteria, fileManagerFullQualifiedTableNamesBuilder, databaseMetaDataAnalyzer.getEscapeChar());
		List<DocumentOnServer> result = new ArrayList<>();
		ResultSet rst = null;
		try {
			List<DocumentOnServer> docs = new ArrayList<>();
			Ivy.log().debug("Searching documents with the following query: {0}" , query);
			rst = this.connectionManager.getConnection().createStatement().executeQuery(query);
			while(rst.next()) {
				docs.add(DocumentOnServerGeneratorHelper.buildDocumentOnServer(rst));
			}
			docs.stream().distinct().forEach(doc -> result.add(doc));
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, null, rst, "search", this.getClass());
		}
		return result;
	}

}
