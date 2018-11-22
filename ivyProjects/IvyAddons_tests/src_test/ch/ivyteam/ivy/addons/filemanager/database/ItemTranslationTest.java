/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.database;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doReturn;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileManagementHandlersFactory;
import ch.ivyteam.ivy.addons.filemanager.FileType;
import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.ItemTranslation;
import ch.ivyteam.ivy.addons.filemanager.Language;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.security.IvyRoleHelper;
import ch.ivyteam.ivy.environment.Ivy;

/**
 * @author ec
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({IvyRoleHelper.class, Ivy.class, PersistenceConnectionManagerFactory.class, FileManagementHandlersFactory.class})
public class ItemTranslationTest {

	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.database.ItemTranslationSQLPersistence#create(ch.ivyteam.ivy.addons.filemanager.ItemTranslation)}.
	 * @throws Exception 
	 */
	@Test
	public void testCreateEmptyItemTranslationAndNoLanguagesExistCreatedItemHasNoLanguages() throws Exception {
		ItemTranslation itt = this.initializeEmptyItemTranslation();
		
		IvyExternalDatabaseConnectionManager mockedConnManager = new IvyExternalDatabaseConnectionManager("test");
		IPersistenceConnectionManager<Connection> spyMockedConnManager = spy(mockedConnManager);
 
		Connection conn = mock(Connection.class);
		
		PreparedStatement stmt = mock(PreparedStatement.class);
		when(stmt.executeUpdate()).thenReturn(1);
		
		when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
		doReturn(conn).when(spyMockedConnManager).getConnection();
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateDirectoryTranslation()).thenReturn(true);
		when(mockedConfig.getDirectoriesTranslationTableName()).thenReturn("test");
		when(mockedConfig.getFileTypesTranslationTableName()).thenReturn("test");
		
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,0, this.getEmptyLanguages());
		trans.setIPersistenceConnectionManager(spyMockedConnManager);
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		doReturn(null).when(spyTrans).getExistingTranslation(itt.getTranslatedItemId());
		doReturn(itt).when(spyTrans).get(1);
		
		ItemTranslation it = spyTrans.create(itt);
		assertEquals(itt, it);
	}
	
	@Test
	public void testCreateEmptyItemTranslationAndLanguagesExistCreatedItemTranslationsLanguagesEqualsExistingLanguages() throws Exception {
		ItemTranslation itt = this.initializeEmptyItemTranslation();
		
		IvyExternalDatabaseConnectionManager mockedConnManager = new IvyExternalDatabaseConnectionManager("test");
		IPersistenceConnectionManager<Connection> spyMockedConnManager = spy(mockedConnManager);
 
		Connection conn = mock(Connection.class);
		
		PreparedStatement stmt = mock(PreparedStatement.class);
		when(stmt.executeUpdate()).thenReturn(1);
		
		when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
		doReturn(conn).when(spyMockedConnManager).getConnection();
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateDirectoryTranslation()).thenReturn(true);
		when(mockedConfig.getDirectoriesTranslationTableName()).thenReturn("test");
		when(mockedConfig.getFileTypesTranslationTableName()).thenReturn("test");
		
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,0, this.getLanguages());
		trans.setIPersistenceConnectionManager(spyMockedConnManager);
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		doReturn(null).when(spyTrans).getExistingTranslation(itt.getTranslatedItemId());
		doReturn(itt).when(spyTrans).get(1);
		
		ItemTranslation it = spyTrans.create(itt);
		Iterator<String> iter = it.getTranslations().keySet().iterator();
		List<String> transL = new ArrayList<String>();
		while(iter.hasNext()) {
			transL.add(iter.next());
		}
		
		List<Language> langs = this.getLanguages();
		List<String> transL2 = new ArrayList<String>();
		for(Language l : langs) {
			transL2.add(l.getIsoName());
		}
		assertTrue(transL.containsAll(transL2));
	}
	
	@Test
	public void testCreateItemTranslationAndLanguagesExistCreatedItemTranslationsEqualsLanguages() throws Exception {
		ItemTranslation itt = this.initializeNewItemTranslation();
		
		IvyExternalDatabaseConnectionManager mockedConnManager = new IvyExternalDatabaseConnectionManager("test");
		IPersistenceConnectionManager<Connection> spyMockedConnManager = spy(mockedConnManager);
 
		Connection conn = mock(Connection.class);
		
		PreparedStatement stmt = mock(PreparedStatement.class);
		when(stmt.executeUpdate()).thenReturn(1);
		
		when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
		doReturn(conn).when(spyMockedConnManager).getConnection();
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateDirectoryTranslation()).thenReturn(true);
		when(mockedConfig.getDirectoriesTranslationTableName()).thenReturn("test");
		when(mockedConfig.getFileTypesTranslationTableName()).thenReturn("test");
		
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,0, this.getLanguages());
		trans.setIPersistenceConnectionManager(spyMockedConnManager);
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		doReturn(null).when(spyTrans).getExistingTranslation(itt.getTranslatedItemId());
		doReturn(itt).when(spyTrans).get(itt.getTranslatedItemId());
		
		ItemTranslation it = spyTrans.create(itt);
		Iterator<String> iter = it.getTranslations().keySet().iterator();
		List<String> transL = new ArrayList<String>();
		while(iter.hasNext()) {
			transL.add(iter.next());
		}
		
		List<Language> langs = this.getLanguages();
		List<String> transL2 = new ArrayList<String>();
		for(Language l : langs) {
			transL2.add(l.getIsoName());
		}
		assertTrue(transL.containsAll(transL2));
	}
	
	@Test
	public void testCreateItemTranslationWithNullParameter_null_returned() throws Exception {
		
		IvyExternalDatabaseConnectionManager mockedConnManager = new IvyExternalDatabaseConnectionManager("test");
		IPersistenceConnectionManager<Connection> spyMockedConnManager = spy(mockedConnManager);
 
		Connection conn = mock(Connection.class);
		
		PreparedStatement stmt = mock(PreparedStatement.class);
		when(stmt.executeUpdate()).thenReturn(1);
		
		when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
		doReturn(conn).when(spyMockedConnManager).getConnection();
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateDirectoryTranslation()).thenReturn(true);
		when(mockedConfig.getDirectoriesTranslationTableName()).thenReturn("test");
		when(mockedConfig.getFileTypesTranslationTableName()).thenReturn("test");
		
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,0, this.getLanguages());
		trans.setIPersistenceConnectionManager(spyMockedConnManager);
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		assertNull(spyTrans.create(null));
	}

	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.database.ItemTranslationSQLPersistence#update(ch.ivyteam.ivy.addons.filemanager.ItemTranslation)}.
	 * @throws Exception 
	 */
	@Test(expected=Exception.class)
	public void testUpdateNotExistingItemTranslationThrowsException() throws Exception {
		ItemTranslation itt = this.initializeItemTranslation();
		
		IvyExternalDatabaseConnectionManager mockedConnManager = new IvyExternalDatabaseConnectionManager("test");
		IPersistenceConnectionManager<Connection> spyMockedConnManager = spy(mockedConnManager);
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateDirectoryTranslation()).thenReturn(true);
		when(mockedConfig.getDirectoriesTranslationTableName()).thenReturn("test");
		when(mockedConfig.getFileTypesTranslationTableName()).thenReturn("test");
		
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,0, this.getLanguages());
		trans.setIPersistenceConnectionManager(spyMockedConnManager);
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		doReturn(null).when(spyTrans).getExistingTranslation(itt.getTranslatedItemId());
		
		spyTrans.update(itt);
	}
	
	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.database.ItemTranslationSQLPersistence#update(ch.ivyteam.ivy.addons.filemanager.ItemTranslation)}.
	 * @throws Exception 
	 */
	@Test
	public void testUpdateExistingItemTranslationSuccess() throws Exception {
		ItemTranslation itt = this.initializeEmptyItemTranslation();
		
		IvyExternalDatabaseConnectionManager mockedConnManager = new IvyExternalDatabaseConnectionManager("test");
		IPersistenceConnectionManager<Connection> spyMockedConnManager = spy(mockedConnManager);
		
		Connection conn = mock(Connection.class);
		
		PreparedStatement stmt = mock(PreparedStatement.class);
		when(stmt.executeUpdate()).thenReturn(1);
		
		when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
		doReturn(conn).when(spyMockedConnManager).getConnection();
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateDirectoryTranslation()).thenReturn(true);
		when(mockedConfig.getDirectoriesTranslationTableName()).thenReturn("test");
		when(mockedConfig.getFileTypesTranslationTableName()).thenReturn("test");
		
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,0, this.getLanguages());
		trans.setIPersistenceConnectionManager(spyMockedConnManager);
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		doReturn(itt).when(spyTrans).getExistingTranslation(itt.getTranslatedItemId());
		doReturn(itt).when(spyTrans).get(itt.getTranslatedItemId());
		
		assertEquals(itt,spyTrans.update(itt));
	}

	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.database.ItemTranslationSQLPersistence#get(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetStringForTypeFolderOnServerFolderDoesNotExistReturnNull() throws Exception {
		
		FileStoreDBHandler mockedFileHandler =  mock(FileStoreDBHandler.class);
		when(mockedFileHandler.getDirectoryWithPath(any(String.class))).thenReturn(null);
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateDirectoryTranslation()).thenReturn(true);
		when(mockedConfig.isUseIvySystemDB()).thenReturn(true);
		when(mockedConfig.getDirectoriesTranslationTableName()).thenReturn("test");
		when(mockedConfig.getFileTypesTranslationTableName()).thenReturn("test");
		when(mockedConfig.getFilesTableName()).thenReturn("test");
		when(mockedConfig.getIvyDBConnectionName()).thenReturn("test");
		
		mockStatic(FileManagementHandlersFactory.class);
		when(FileManagementHandlersFactory.getFileSecurityHandlerInstance(mockedConfig)).thenReturn(mockedFileHandler);
		
		
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,0, this.getLanguages());
		
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		ItemTranslation itt = spyTrans.get("test/root/");
		
		assertNull(itt);
	}
	
	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.database.ItemTranslationSQLPersistence#get(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetStringForTypeFolderOnServerFolderExistReturnItemTranslation() throws Exception {
		FolderOnServer fos = new FolderOnServer();
		fos.setPath("root");
		fos.setName("root");
		fos.setId(222);
		
		ItemTranslation itt = this.initializeItemTranslation();
		itt.setTranslatedItemId(fos.getId());
		
		FileStoreDBHandler mockedFileHandler =  mock(FileStoreDBHandler.class);
		when(mockedFileHandler.getDirectoryWithPath(any(String.class))).thenReturn(fos);
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		
		mockStatic(FileManagementHandlersFactory.class);
		when(FileManagementHandlersFactory.getFileSecurityHandlerInstance(mockedConfig)).thenReturn(mockedFileHandler);
		
		
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,0, this.getLanguages());
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		doReturn(itt).when(spyTrans).get(fos.getId());
		
		ItemTranslation itt2 = spyTrans.get("test/root/");
		
		assertEquals(itt,itt2);
	}
	
	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.database.ItemTranslationSQLPersistence#get(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetStringForTypeFileTypeAndFileDoesNotExistReturnNull() throws Exception {
		FileStoreDBHandler mockedFileHandler =  mock(FileStoreDBHandler.class);
		when(mockedFileHandler.getDirectoryWithPath(any(String.class))).thenReturn(null);
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		
		mockStatic(FileManagementHandlersFactory.class);
		when(FileManagementHandlersFactory.getFileManagementHandlerInstance(mockedConfig)).thenReturn(mockedFileHandler);
		
		
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,1, this.getLanguages());
		
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		ItemTranslation itt = spyTrans.get("test/root/myFile.doc");
		
		assertNull(itt);
	}
	
	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.database.ItemTranslationSQLPersistence#get(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetStringForTypeFileAndFileTypeIsNullFileExistsReturnNull() throws Exception {
		DocumentOnServer doc = new DocumentOnServer();

		
		FileStoreDBHandler mockedFileHandler =  mock(FileStoreDBHandler.class);
		when(mockedFileHandler.getDocumentOnServer(any(String.class))).thenReturn(doc);
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		
		mockStatic(FileManagementHandlersFactory.class);
		when(FileManagementHandlersFactory.getFileManagementHandlerInstance(mockedConfig)).thenReturn(mockedFileHandler);
		
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,1, this.getLanguages());
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		
		spyTrans.get("test/root/myFile.doc");
		
		assertNull(spyTrans.get("test/root/myFile.doc"));
	}
	
	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.database.ItemTranslationSQLPersistence#get(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetStringForTypeFileTypeAndFileExistsReturnItemTranslation() throws Exception {
		DocumentOnServer doc = new DocumentOnServer();
		FileType ft = new FileType();
		ft.setId((long) 345);
		ft.setFileTypeName("myFileType");
		doc.setFileType(ft);
		
		ItemTranslation itt = this.initializeItemTranslation();
		itt.setTranslatedItemId(ft.getId());
		
		FileStoreDBHandler mockedFileHandler =  mock(FileStoreDBHandler.class);
		when(mockedFileHandler.getDocumentOnServer(any(String.class))).thenReturn(doc);
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		
		mockStatic(FileManagementHandlersFactory.class);
		when(FileManagementHandlersFactory.getFileManagementHandlerInstance(mockedConfig)).thenReturn(mockedFileHandler);
		
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,1, this.getLanguages());
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		doReturn(itt).when(spyTrans).get(ft.getId());
		
		ItemTranslation itt2 = spyTrans.get("test/root/myFile.doc");
		
		assertEquals(itt,itt2);
	}


	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.database.ItemTranslationSQLPersistence#get(long)}.
	 * @throws Exception 
	 */
	@Test(expected=Exception.class)
	public void testGetLongAndLongZeroThrowsException() throws Exception {
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,1, this.getLanguages());
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		spyTrans.get(0);
	}
	
	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.database.ItemTranslationSQLPersistence#get(long)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetLongAndNoItemTranslationFoundReturnTranslationsWithEmptyText() throws Exception {
		
		IvyExternalDatabaseConnectionManager mockedConnManager = new IvyExternalDatabaseConnectionManager("test");
		IPersistenceConnectionManager<Connection> spyMockedConnManager = spy(mockedConnManager);
		
		Connection conn = mock(Connection.class);
		
		PreparedStatement stmt = mock(PreparedStatement.class);
		ResultSet rst = mock(ResultSet.class);
		when(rst.next()).thenReturn(false);
		when(stmt.executeQuery()).thenReturn(rst);
		
		when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
		doReturn(conn).when(spyMockedConnManager).getConnection();
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateDirectoryTranslation()).thenReturn(true);
		when(mockedConfig.getDirectoriesTranslationTableName()).thenReturn("test");
		when(mockedConfig.getFileTypesTranslationTableName()).thenReturn("test");
		
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,0, this.getLanguages());
		trans.setIPersistenceConnectionManager(spyMockedConnManager);
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		System.out.println(spyTrans.get(1).getTranslations());
		boolean empty = true;
		HashMap<String,String> m = spyTrans.get(1).getTranslations();
		Iterator<Entry<String, String>> map = m.entrySet().iterator();
		while(map.hasNext()) {
			if(!map.next().getValue().isEmpty()){
				empty=false;
				break;
			}
		}
		assertTrue(empty && m.size() == this.getLanguages().size());
	}
	
	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.database.ItemTranslationSQLPersistence#get(long)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetLongAndNoItemTranslationFoundReturnItemTranslation() throws Exception {
		
		IvyExternalDatabaseConnectionManager mockedConnManager = new IvyExternalDatabaseConnectionManager("test");
		IPersistenceConnectionManager<Connection> spyMockedConnManager = spy(mockedConnManager);
		
		Connection conn = mock(Connection.class);
		
		PreparedStatement stmt = mock(PreparedStatement.class);
		ResultSet rst = mock(ResultSet.class);
		when(rst.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
		when(rst.getString("lang")).thenReturn("EN").thenReturn("DE").thenReturn("FR");
		when(rst.getString("tr")).thenReturn("cancel").thenReturn("Abbrechen").thenReturn("Annuler");
		when(stmt.executeQuery()).thenReturn(rst);
		
		when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
		doReturn(conn).when(spyMockedConnManager).getConnection();
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateDirectoryTranslation()).thenReturn(true);
		when(mockedConfig.getDirectoriesTranslationTableName()).thenReturn("test");
		when(mockedConfig.getFileTypesTranslationTableName()).thenReturn("test");
		
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,0, this.getLanguages());
		trans.setIPersistenceConnectionManager(spyMockedConnManager);
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		ItemTranslation itt = spyTrans.get(1);
		
		assertTrue(!itt.getTranslations().get("EN").trim().isEmpty());
	}

	/**
	 * Test method for {@link ch.ivyteam.ivy.addons.filemanager.database.ItemTranslationSQLPersistence#delete(ch.ivyteam.ivy.addons.filemanager.ItemTranslation)}.
	 * @throws Exception 
	 */
	@Test(expected=Exception.class)
	public void testDeleteNullItemThrowsException() throws Exception {
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,1, this.getLanguages());
		trans.delete(null);
		//ItemTranslationSQLPersistence spyTrans = spy(trans);
		//spyTrans.delete(null);
	}
	
	@Test
	public void testDeleteNonExistingItemReturnFalse() throws Exception {
		IvyExternalDatabaseConnectionManager mockedConnManager = new IvyExternalDatabaseConnectionManager("test");
		IPersistenceConnectionManager<Connection> spyMockedConnManager = spy(mockedConnManager);
		
		Connection conn = mock(Connection.class);
		
		PreparedStatement stmt = mock(PreparedStatement.class);
		when(stmt.executeUpdate()).thenReturn(0);
		
		when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
		doReturn(conn).when(spyMockedConnManager).getConnection();
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateDirectoryTranslation()).thenReturn(true);
		when(mockedConfig.getDirectoriesTranslationTableName()).thenReturn("test");
		when(mockedConfig.getFileTypesTranslationTableName()).thenReturn("test");
		
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,0, this.getLanguages());
		trans.setIPersistenceConnectionManager(spyMockedConnManager);
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		assertFalse(spyTrans.delete(this.initializeItemTranslation()));
	}
	
	@Test
	public void testDeleteExistingItemReturnTrue() throws Exception {
		IvyExternalDatabaseConnectionManager mockedConnManager = new IvyExternalDatabaseConnectionManager("test");
		IPersistenceConnectionManager<Connection> spyMockedConnManager = spy(mockedConnManager);
		
		Connection conn = mock(Connection.class);
		
		PreparedStatement stmt = mock(PreparedStatement.class);
		when(stmt.executeUpdate()).thenReturn(1);
		
		when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
		doReturn(conn).when(spyMockedConnManager).getConnection();
		
		BasicConfigurationController mockedConfig = mock(BasicConfigurationController.class);
		when(mockedConfig.isActivateDirectoryTranslation()).thenReturn(true);
		when(mockedConfig.getDirectoriesTranslationTableName()).thenReturn("test");
		when(mockedConfig.getFileTypesTranslationTableName()).thenReturn("test");
		
		ItemTranslationSQLPersistence trans = new ItemTranslationSQLPersistence(mockedConfig,0, this.getLanguages());
		trans.setIPersistenceConnectionManager(spyMockedConnManager);
		ItemTranslationSQLPersistence spyTrans = spy(trans);
		assertTrue(trans.delete(this.initializeItemTranslation()));
	}
	
	private ItemTranslation initializeEmptyItemTranslation() {
		ItemTranslation itt = new ItemTranslation(1, null);
		return itt;
	}
	
	private ItemTranslation initializeItemTranslation() {
		ItemTranslation itt = new ItemTranslation(1, null);
		List<Language> langs = this.getLanguages();
		HashMap<String,String> map = new HashMap<String,String>();
		for(Language l:langs){
			map.put(l.getIsoName(), "Test "+l.getIsoName());
		}
		itt.setTranslations(map);
		return itt;
	}
	
	private ItemTranslation initializeNewItemTranslation() {
		ItemTranslation itt = new ItemTranslation(0, null);
		List<Language> langs = this.getLanguages();
		HashMap<String,String> map = new HashMap<String,String>();
		for(Language l:langs){
			map.put(l.getIsoName(), "Test "+l.getIsoName());
		}
		itt.setTranslations(map);
		return itt;
	}
	
	private List<Language> getEmptyLanguages() {
		List<Language> langs = new ArrayList<Language>();
		return langs;
	}
	
	private List<Language> getLanguages() {
		List<Language> langs = new ArrayList<Language>();
		langs.add(new Language(1,"DE"));
		langs.add(new Language(2,"FR"));
		langs.add(new Language(3,"EN"));
		return langs;
	}
	
	@SuppressWarnings("unused")
	private Connection getConnection() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection hsqlConn = null;
		try {
			hsqlConn = DriverManager.getConnection("jdbc:hsqldb:mem:aname", "sa", "");

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return hsqlConn;
	}

}
