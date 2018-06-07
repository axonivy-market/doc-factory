package ch.ivyteam.ivy.addons.filemanager.database.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.hsqldb.Server;

import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;

@SuppressWarnings("restriction")
public class HsqlDBFilemanager implements IPersistenceConnectionManager<Connection> {

	private Connection conn = null;

	public HsqlDBFilemanager() {
		createFilemanagerTables();
	}
	
	@Override
	public Connection getConnection() throws Exception {
		if(conn == null) {
			Class.forName("org.hsqldb.jdbcDriver");
			conn = DriverManager.getConnection("jdbc:hsqldb:mem:filemanager;ifexists=true", "sa", "");
		}
		return conn;

	}

	@Override
	public void closeConnection() throws Exception {
		if(conn != null) {
			conn.close();
		}
		conn = null;
	}

	private void createFilemanagerTables() {
		try {
			getConnection().createStatement().execute(getCreateTablesQuery());
		} catch (Exception ex) {
			if(isTableAlreadyExistsException(ex)) {
				System.out.println("No need to create tables, they already exist.");
			} else {
				System.err.println(ex.getClass().getName() + " " + ex.getMessage());
			}
		} finally {
			try {
				closeConnection();
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + " " + e.getMessage());
			}
		}
	}

	private String getCreateTablesQuery() throws IOException {
		InputStream inputStream = HsqlDBFilemanager.class.getResourceAsStream("filemanager.sql");
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}

	private boolean isTableAlreadyExistsException(Exception ex) {
		return ex.getMessage() != null && ex.getMessage().startsWith("Table already exists");
	}

	public static void startServer() {
		Server server = new Server();
		server.setDatabaseName(0, "filemanager");
		server.setDatabasePath(0, "mem:filemanager");
		server.setPort(9001); // this is the default port
		server.start();

	}

	public static void shutdownServer() {
		Connection connection = null;
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			connection =  DriverManager.getConnection("jdbc:hsqldb:mem:filemanager;ifexists=true", "sa", "");
			connection.createStatement().execute("SHUTDOWN");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + " " + e.getMessage());
		}  finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					System.err.println(e.getClass().getName() + " " + e.getMessage());
				}
			}
		}

	}

}
