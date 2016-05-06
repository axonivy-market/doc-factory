package ch.ivyteam.ivy.addons.filemanager.database.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.environment.Ivy;

public final class TableChecker {
	
	private TableChecker() {}
	
	public static boolean FileLinkTableExist(BasicConfigurationController configuration) {
		return tableExist(configuration, configuration.getFileLinkTableName());
	}
	
	private static boolean tableExist(BasicConfigurationController configuration, String tableName) {
		if(configuration==null) {
			throw new IllegalArgumentException("The BasicConfigurationController object cannot be null.");
		}
		boolean result = false;
		@SuppressWarnings("unchecked")
		IPersistenceConnectionManager<Connection> connectionManager = (IPersistenceConnectionManager<Connection>) PersistenceConnectionManagerFactory
				.getPersistenceConnectionManagerInstance(configuration);
		ResultSet rst = null;		
		try {
			DatabaseMetaData dbmd = connectionManager.getConnection().getMetaData();
			String schema = StringUtils.isBlank(configuration.getDatabaseSchemaName()) ? null : configuration.getDatabaseSchemaName();
			rst = dbmd.getTables(null, schema, tableName, new String[] {"TABLE"});
			while(rst.next()) {
				if(rst.getString("TABLE_NAME").equalsIgnoreCase(tableName)) {
					result = true;
					break;
				}
			}
			if(!result) {
				if(rst!=null) {
					rst.close();
				}
				rst = dbmd.getTables(null, schema, tableName.toUpperCase(), new String[] {"TABLE"});
				while(rst.next()) {
					if(rst.getString("TABLE_NAME").equalsIgnoreCase(tableName)) {
						result = true;
						break;
					}
				}
			}
		} catch(Exception ex) {
			Ivy.log().error(ex.getMessage(), ex);
		} finally {
			try{
				if(rst!=null) {
					rst.close();
				}
			} catch (Exception e) {
				Ivy.log().error(e.getMessage(), e);
			}
			try {
				connectionManager.closeConnection();
			} catch (Exception e) {
				Ivy.log().error(e.getMessage(), e);
			}
		}	
		return result;
	}
}
