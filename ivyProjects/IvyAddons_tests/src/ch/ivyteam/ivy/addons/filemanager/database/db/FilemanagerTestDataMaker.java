package ch.ivyteam.ivy.addons.filemanager.database.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FilemanagerTestDataMaker {

	public static long insertFileType(String applicationName, String tagName) {
		HsqlDBFilemanager filemanager =  new HsqlDBFilemanager();
		PreparedStatement pstmt = null;
		long insertedId = 0;
		try {
			pstmt = filemanager.getConnection().prepareStatement("INSERT INTO filetype (name, appname) VALUES (?, ?)");
			pstmt.setString(1, tagName);
			pstmt.setString(2, applicationName);
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = filemanager.getConnection().prepareStatement("SELECT id FROM  filetype WHERE name = ? AND appname = ?");
			pstmt.setString(1, tagName);
			pstmt.setString(2, applicationName);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			insertedId = rs.getLong(1);
			rs.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			try {
				filemanager.closeConnection();
			} catch (Exception e) {
			}
		}
		return insertedId;
	}

	public static long insertDocumentWithOptionalTagsInTestDb(String filename, String filepath, long filetypeId, String ... tags) {
		long insertedId = insertDocumentInTestDb(filename, filepath, filetypeId);
		insertTagForDocumentInTestDb(insertedId, tags);
		return insertedId;
	}

	public static long insertDocumentInTestDb(String filename, String filepath, long filetypeId) {
		HsqlDBFilemanager filemanager =  new HsqlDBFilemanager();
		PreparedStatement pstmt = null;
		long insertedId = 0;
		try {
			pstmt = filemanager.getConnection().prepareStatement("INSERT INTO uploadedfiles (FileName, FilePath, CreationUserId, FileSize, versionnumber, filetypeid) "
					+ "VALUES (?, ?, 'testuser@axonivy.com', '27Kb', 1, ?)");
			pstmt.setString(1, filename);
			pstmt.setString(2, filepath);
			pstmt.setLong(3, filetypeId);
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = filemanager.getConnection().prepareStatement("SELECT FileId FROM uploadedfiles WHERE FilePath LIKE ?");
			pstmt.setString(1, filepath);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			insertedId = rs.getLong(1);
			rs.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			try {
				filemanager.closeConnection();
			} catch (Exception e) {
			}
		}
		return insertedId;
	}

	public static void insertTagForDocumentInTestDb(long documentId, String... tags) {
		if(tags== null || tags.length == 0) {
			return;
		}
		HsqlDBFilemanager filemanager =  new HsqlDBFilemanager();
		PreparedStatement pstmt = null;
		try{
			pstmt = filemanager.getConnection().prepareStatement("INSERT INTO tags (fileid, tag) VALUES (?, ?)");
			for(int i = 0; i < tags.length ; i++) {
				pstmt.setLong(1, documentId);
				pstmt.setString(2, tags[i]);
				pstmt.execute();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			try {
				filemanager.closeConnection();
			} catch (Exception e) {
			}
		}

	}

	public static void clearTestDb() {
		HsqlDBFilemanager filemanager =  new HsqlDBFilemanager();
		PreparedStatement pstmt = null;
		try{
			pstmt = filemanager.getConnection().prepareStatement("DELETE FROM tags WHERE id >= 0");
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = filemanager.getConnection().prepareStatement("DELETE FROM uploadedfiles WHERE FileId >= 0");
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = filemanager.getConnection().prepareStatement("DELETE FROM filetype WHERE id >= 0");
			pstmt.executeUpdate();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			try {
				filemanager.closeConnection();
			} catch (Exception e) {
			}
		}
		
	}

}
