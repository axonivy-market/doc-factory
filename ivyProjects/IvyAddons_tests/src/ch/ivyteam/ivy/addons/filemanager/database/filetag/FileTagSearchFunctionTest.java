package ch.ivyteam.ivy.addons.filemanager.database.filetag;


import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.ivyteam.ivy.addons.filemanager.MyResultSet;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.IvyExternalDatabaseConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IFileTagPersistence;
import ch.ivyteam.ivy.addons.filemanager.database.persistence.IPersistenceConnectionManager;
import ch.ivyteam.ivy.addons.filemanager.ivy.implemented.MyCMS;
import ch.ivyteam.ivy.addons.filemanager.ivy.implemented.MyGlobalVariableContext;
import ch.ivyteam.ivy.cm.IContentManagementSystem;
import ch.ivyteam.ivy.environment.Ivy;


import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Ivy.class, IContentManagementSystem.class})
public class FileTagSearchFunctionTest {
	
	@Test
	public void searchTagsWithoutSearchStringReturnAll() throws Exception {
		java.util.List<String> tags = this.makeListTags();
		String search = null;
		
		MyGlobalVariableContext var = mock(MyGlobalVariableContext.class);
		when(var.get(any(String.class))).thenReturn("");
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		
		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		when(Ivy.var()).thenReturn(var);
		when(Ivy.cms().co(any(String.class))).thenReturn("");
		
		IvyExternalDatabaseConnectionManager mockedConnManager = new IvyExternalDatabaseConnectionManager("test");
		IPersistenceConnectionManager<Connection> spyMockedConnManager = spy(mockedConnManager);
 
		Connection conn = mock(Connection.class);
		
		PreparedStatement stmt = mock(PreparedStatement.class);
		ResultSet rst = getResultSetWithSearch(search);
		when(stmt.executeQuery()).thenReturn(rst);
		
		when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
		doReturn(conn).when(spyMockedConnManager).getConnection();
		
		BasicConfigurationController config = new BasicConfigurationController();
		when(config.getFilesTableName()).thenReturn("filetags");
		
		IFileTagPersistence pers = PersistenceConnectionManagerFactory.getIFileTagPersistenceInstance(config);
		if(pers instanceof FileTagSQLPersistence) {
			 ((FileTagSQLPersistence) pers).setIPersistenceConnectionManager(spyMockedConnManager);
		}
		java.util.List<String> result = pers.searchAvailableTags(search);
		assertTrue(result.size()==tags.size());
		assertEquals(tags, result);
	}
	
	@Test
	public void searchTagsWithSearchStringReturnMatchingTags() throws Exception {
		String search = "test";
		java.util.List<String> tags = this.makeSearchedListTags(search);
		
		MyGlobalVariableContext var = mock(MyGlobalVariableContext.class);
		when(var.get(any(String.class))).thenReturn("");
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		
		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		when(Ivy.var()).thenReturn(var);
		when(Ivy.cms().co(any(String.class))).thenReturn("");
		
		IvyExternalDatabaseConnectionManager mockedConnManager = new IvyExternalDatabaseConnectionManager("test");
		IPersistenceConnectionManager<Connection> spyMockedConnManager = spy(mockedConnManager);
 
		Connection conn = mock(Connection.class);
		
		PreparedStatement stmt = mock(PreparedStatement.class);
		ResultSet rst = getResultSetWithSearch(search);
		when(stmt.executeQuery()).thenReturn(rst);
		
		when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
		doReturn(conn).when(spyMockedConnManager).getConnection();
		
		BasicConfigurationController config = new BasicConfigurationController();
		when(config.getFilesTableName()).thenReturn("filetags");
		
		IFileTagPersistence pers = PersistenceConnectionManagerFactory.getIFileTagPersistenceInstance(config);
		if(pers instanceof FileTagSQLPersistence) {
			 ((FileTagSQLPersistence) pers).setIPersistenceConnectionManager(spyMockedConnManager);
		}
		java.util.List<String> result = pers.searchAvailableTags(search);
		assertTrue(result.size()==3 && result.contains("TEST.659"));
		assertEquals(tags, result);
	}
	
	@Test
	public void searchTagsWithSearchStringNoMatchingTagsReturnNull() throws Exception {
		String search = "gfrefr";
		MyGlobalVariableContext var = mock(MyGlobalVariableContext.class);
		when(var.get(any(String.class))).thenReturn("");
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		
		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		when(Ivy.var()).thenReturn(var);
		when(Ivy.cms().co(any(String.class))).thenReturn("");
		
		IvyExternalDatabaseConnectionManager mockedConnManager = new IvyExternalDatabaseConnectionManager("test");
		IPersistenceConnectionManager<Connection> spyMockedConnManager = spy(mockedConnManager);
 
		Connection conn = mock(Connection.class);
		
		PreparedStatement stmt = mock(PreparedStatement.class);
		ResultSet rst = getResultSetWithSearch(search);
		when(stmt.executeQuery()).thenReturn(rst);
		
		when(conn.prepareStatement(any(String.class))).thenReturn(stmt);
		doReturn(conn).when(spyMockedConnManager).getConnection();
		
		BasicConfigurationController config = new BasicConfigurationController();
		when(config.getFilesTableName()).thenReturn("filetags");
		
		IFileTagPersistence pers = PersistenceConnectionManagerFactory.getIFileTagPersistenceInstance(config);
		if(pers instanceof FileTagSQLPersistence) {
			 ((FileTagSQLPersistence) pers).setIPersistenceConnectionManager(spyMockedConnManager);
		}
		java.util.List<String> result = pers.searchAvailableTags(search);
		
		assertNull(result);
		
	}
	
	private ResultSet getResultSetWithSearch(String search) throws Exception{
		java.util.List<String> tags = this.makeSearchedListTags(search);
		
		MyResultSet rst = mock(MyResultSet.class);
		if(tags.isEmpty()) {
			when(rst.next()).thenReturn(false);
		}else{
			if(tags.size()==1) {
				when(rst.next()).thenReturn(true, false);
				when(rst.getString(any(String.class))).thenReturn(tags.get(0));
			} else {
				String first = tags.get(0);
				tags.remove(0);
				
				Boolean[] bols = new Boolean[tags.size()+1];
				int i=0;
				while(i<tags.size()) {
					bols[i]=true;
					i++;
				}
				bols[i] = false;
				
				when(rst.next()).thenReturn(true, bols);
				when(rst.getString(any(String.class))).thenReturn(first, tags.toArray(new String [tags.size()]));
			}
		}
		return rst;
	}
	
	private java.util.List<String> makeSearchedListTags(String search) {
		java.util.List<String> tags = this.makeListTags();
		if(search!=null && !search.trim().isEmpty()) {
			ArrayList<String> l = new ArrayList<String>();
			for(String s : tags) {
				if(s.toLowerCase().startsWith(search)) {
					l.add(s);
				}
			}
			tags.clear();
			tags.addAll(l);
		}
		
		return tags;
	}
	
	private java.util.List<String> makeListTags() {
		java.util.List<String> l = new ArrayList<String>();
		l.add("tag.1.test");
		l.add("test.555.cool");
		l.add("Test.575");
		l.add("file.tag.reporting.44");
		l.add("TEST.659");
		return l;
	}
}
