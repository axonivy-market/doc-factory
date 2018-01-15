package ch.ivyteam.ivy.addons.filemanager.database.security;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.sql.SqlConstants;
import ch.ivyteam.ivy.environment.Ivy;

public class DirectorySecurityVersionDetector {

	private static String COLUMN_METADATA_NAME = "COLUMN_NAME";

	public static int getDirectorySecurityVersion(BasicConfigurationController configuration, DatabaseMetaData dbmd) throws SQLException {
		if(configuration==null || dbmd==null) {
			return 1;
		}
		int secVersion = 1;
		String prod = dbmd.getDatabaseProductName().toLowerCase();
		boolean isOracle = prod.contains(SqlConstants.ORACLE_PRODUCT_NAME);
		ResultSet rst = null;
		try {
			rst = dbmd.getColumns(null, 
					(configuration.getDatabaseSchemaName()!=null &&  configuration.getDatabaseSchemaName().trim().length()==0)?
							null: configuration.getDatabaseSchemaName(), 
							configuration.getDirectoriesTableName(), SecurityConstants.CAN_TRANSLATE_DIRECTORY_COLUMN_NAME);
			if(rst.next()) {
				secVersion=2;
			}
			try {
				rst.close();
			} catch (SQLException e) {
				Ivy.log().error("getDirectorySecurityVersion error closing ResultSet "+e.getMessage(),e);
			}
			if(isOracle && secVersion<2) {
				secVersion = getDirectorySecurityVersionForOracle(rst, dbmd, configuration);
			}
		} finally {
			if(rst!=null) {
				try {
					rst.close();
				} catch (SQLException e) {
					Ivy.log().error("getDirectorySecurityVersion error closing ResultSet "+e.getMessage(),e);
				}
			}
		}
		return secVersion;
	}

	private static int getDirectorySecurityVersionForOracle(ResultSet rst, DatabaseMetaData dbmd, BasicConfigurationController configuration) throws SQLException {
		int secVersion=1;
		try{
			rst=dbmd.getColumns(null, (configuration.getDatabaseSchemaName()!=null &&  configuration.getDatabaseSchemaName().trim().length()==0)?
					null: configuration.getDatabaseSchemaName(),configuration.getDirectoriesTableName(), null);
			while(rst.next()) {
				if(rst.getString(COLUMN_METADATA_NAME).equalsIgnoreCase(SecurityConstants.CAN_TRANSLATE_DIRECTORY_COLUMN_NAME)){
					secVersion=2;
					break;
				}
			}
			try {
				rst.close();
			} catch (SQLException e) {
				Ivy.log().error("getDirectorySecurityVersion error closing ResultSet "+e.getMessage(),e);
			}
			if(secVersion<2) {
				rst=dbmd.getColumns(null, (configuration.getDatabaseSchemaName()!=null &&  configuration.getDatabaseSchemaName().trim().length()==0)?
						null: configuration.getDatabaseSchemaName(),configuration.getDirectoriesTableName().toUpperCase(), null);
				while(rst.next()) {
					if(rst.getString(COLUMN_METADATA_NAME).equalsIgnoreCase(SecurityConstants.CAN_TRANSLATE_DIRECTORY_COLUMN_NAME)){
						secVersion=2;
						break;
					}
				}
				try {
					rst.close();
				} catch (SQLException e) {
					Ivy.log().error("getDirectorySecurityVersion error closing ResultSet "+e.getMessage(),e);
				}
			}
		} finally {
			try {
				rst.close();
			} catch (SQLException e) {
				Ivy.log().error("getDirectorySecurityVersion error closing ResultSet "+e.getMessage(),e);
			}
		}
		return secVersion;
	}
}
