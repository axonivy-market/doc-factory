package ch.ivyteam.ivy.addons.filemanager.database.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.security.DirectorySecurityVersionDetector;
import ch.ivyteam.ivy.environment.Ivy;

public class DatabaseMetaDataAnalyzer {
	
	private boolean isMsSql;
	private boolean isOracle;
	private int securityVersion = 1;
	private String escapeChar="\\";
	private boolean supportsGetGeneratedKeys;
	
	public DatabaseMetaDataAnalyzer (BasicConfigurationController configuration) {
		if(configuration==null) {
			throw new IllegalArgumentException("The BasicConfigurationController object cannot be null.");
		}
		@SuppressWarnings("unchecked")
		IPersistenceConnectionManager<Connection> connectionManager = (IPersistenceConnectionManager<Connection>) PersistenceConnectionManagerFactory
				.getPersistenceConnectionManagerInstance(configuration);
				
		try {
			DatabaseMetaData dbmd = connectionManager.getConnection().getMetaData();
			this.supportsGetGeneratedKeys = dbmd.supportsGetGeneratedKeys();
			String prod = dbmd.getDatabaseProductName().toLowerCase();
			if(prod.contains(SqlConstants.MYSQL_PRODUCT_NAME) || (prod.contains(SqlConstants.POSTGRESQL_PRODUCT_NAME) && 
					Double.valueOf(dbmd.getDatabaseMajorVersion()+"."+dbmd.getDatabaseMinorVersion())<9.1 )){
				this.escapeChar="\\\\";
			}
			this.isMsSql = prod.contains(SqlConstants.MSSQL_PRODUCT_NAME);
			this.isOracle = prod.contains(SqlConstants.ORACLE_PRODUCT_NAME);
			this.securityVersion = DirectorySecurityVersionDetector.getDirectorySecurityVersion(configuration, dbmd);
		} catch(Exception ex) {
			Ivy.log().error(ex.getMessage(),ex);
		} finally {
			try {
				connectionManager.closeConnection();
			} catch (Exception e) {
				Ivy.log().error(e.getMessage(),e);
			}
		}		
	}

	public boolean isMsSql() {
		return isMsSql;
	}

	public boolean isOracle() {
		return isOracle;
	}

	public int getSecurityVersion() {
		return securityVersion;
	}

	public String getEscapeChar() {
		return escapeChar;
	}

	public boolean isSupportsGetGeneratedKeys() {
		return supportsGetGeneratedKeys;
	}

}
