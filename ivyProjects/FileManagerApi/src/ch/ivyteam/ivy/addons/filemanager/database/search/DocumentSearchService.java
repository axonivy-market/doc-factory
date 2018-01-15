package ch.ivyteam.ivy.addons.filemanager.database.search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.configuration.FileManagerFullQualifiedTableNamesBuilder;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.sql.DatabaseMetaDataAnalyzer;
import ch.ivyteam.ivy.addons.filemanager.database.sql.PersistenceConnectionManagerReleaser;
import ch.ivyteam.ivy.addons.filemanager.restricted.database.sql.DocumentOnServerGeneratorHelper;

public class DocumentSearchService {

	private IPersistenceConnectionManager<Connection> connectionManager =null;
	private FileManagerFullQualifiedTableNamesBuilder fileManagerFullQualifiedTableNamesBuilder;	
	private DatabaseMetaDataAnalyzer databaseMetaDataAnalyzer;

	@SuppressWarnings("unchecked")
	public DocumentSearchService(BasicConfigurationController basicConfigurationController) {
		fileManagerFullQualifiedTableNamesBuilder = new FileManagerFullQualifiedTableNamesBuilder(basicConfigurationController);
		this.connectionManager =  (IPersistenceConnectionManager<Connection>) PersistenceConnectionManagerFactory
				.getPersistenceConnectionManagerInstance(basicConfigurationController);
		databaseMetaDataAnalyzer = new DatabaseMetaDataAnalyzer(basicConfigurationController);
	}
	
	/**
	 * for unit tests
	 * @param connectionManager
	 * @param fileManagerFullQualifiedTableNamesBuilder
	 * @param databaseMetaDataAnalyzer
	 */
	DocumentSearchService(IPersistenceConnectionManager<Connection> connectionManager, 
			FileManagerFullQualifiedTableNamesBuilder fileManagerFullQualifiedTableNamesBuilder, 
			DatabaseMetaDataAnalyzer databaseMetaDataAnalyzer) {
		this.connectionManager = connectionManager;
		this.fileManagerFullQualifiedTableNamesBuilder = fileManagerFullQualifiedTableNamesBuilder;
		this.databaseMetaDataAnalyzer = databaseMetaDataAnalyzer;
	}

	public List<DocumentOnServer> search(DocumentSearchCriterias searchCriteria) throws Exception {
		String query = DocumentSearchQueryBuilder.
				buildPreparedStatementQuery(searchCriteria, fileManagerFullQualifiedTableNamesBuilder, databaseMetaDataAnalyzer.getEscapeChar());
		List<DocumentOnServer> result = new ArrayList<>();
		ResultSet rst = null;
		PreparedStatement pstmt = null;
		try {
			List<DocumentOnServer> docs = new ArrayList<>();
			pstmt = makePreparedStatement(query, searchCriteria);
			rst = pstmt.executeQuery();
			while(rst.next()) {
				docs.add(DocumentOnServerGeneratorHelper.buildDocumentOnServer(rst));
			}
			docs.stream().distinct().forEach(doc -> result.add(doc));
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, pstmt, rst, "search", this.getClass());
		}
		return result;
	}
	
	public List<DocumentOnServer> searchWithIds(long ... ids) throws Exception {
		API.checkNotNull(ids, "ids");
		List<DocumentOnServer> result = new ArrayList<>();
		if(ids.length == 0) {
			return result;
		}
		Set<Long> idToSearch = new HashSet<>();
		Arrays.stream(ids).forEach(id -> idToSearch.add(id));
		ResultSet rst = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = makePreparedStatementForDocumentSearchWithIds(idToSearch);
			rst = pstmt.executeQuery();
			while(rst.next()) {
				result.add(DocumentOnServerGeneratorHelper.buildDocumentOnServer(rst));
			}
		} finally {
			PersistenceConnectionManagerReleaser.release(this.connectionManager, pstmt, rst, "search", this.getClass());
		}
		return result;
	}

	private PreparedStatement makePreparedStatementForDocumentSearchWithIds(Set<Long> idToSearch) throws Exception {
		StringBuilder querySb = new StringBuilder(DocumentSearchQueryBuilder.makeSelectDocumentsQuery(fileManagerFullQualifiedTableNamesBuilder)).
				append(" WHERE fileid IN (");
		idToSearch.stream().forEach(id -> querySb.append(id).append(','));

		querySb.deleteCharAt(querySb.length() - 1).append(')');
		return this.connectionManager.getConnection().prepareStatement(querySb.toString());
	}

	private PreparedStatement makePreparedStatement(String query, DocumentSearchCriterias searchCriteria) throws Exception {
		PreparedStatement pstmt = this.connectionManager.getConnection().prepareStatement(query);
		int i = 1;
		if(searchCriteria.hasFileTypeCriteria()) {
			pstmt.setString(i, escapeUnderscoreInString(searchCriteria.getFileTypeName()));
			i++;
		}

		for(String tag : searchCriteria.getTagsName()) {
			pstmt.setString(i, tag);
			i++;
		}
		
		pstmt.setString(i, escapeUnderscoreInString(searchCriteria.getFilePathSearchCriteria()));

		return pstmt;

	}
	
	private String escapeUnderscoreInString(String path) {	
		return path.replaceAll("_", "\\\\_");
	}

}
