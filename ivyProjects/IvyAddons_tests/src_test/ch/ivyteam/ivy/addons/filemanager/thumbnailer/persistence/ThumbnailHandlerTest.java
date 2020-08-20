package ch.ivyteam.ivy.addons.filemanager.thumbnailer.persistence;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.ReturnedMessage;
import ch.ivyteam.ivy.addons.filemanager.database.DocumentOnServerSQLPersistence;
import ch.ivyteam.ivy.addons.filemanager.html.table.model.ThumbnailData;
import ch.ivyteam.ivy.addons.filemanager.ivy.implemented.MyIIvyScriptObjectEnvironment;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.persistence.ThumbnailHandler;
import ch.ivyteam.ivy.addons.filemanager.thumbnailer.persistence.ThumbnailSQLPersistence;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.util.IIvyScriptObjectEnvironment;
import ch.ivyteam.ivy.scripting.objects.util.IvyScriptObjectEnvironmentContext;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IvyScriptObjectEnvironmentContext.class,Ivy.class})
public class ThumbnailHandlerTest {

	@Test
	public void testInsertDefaulThumbnailSuccess() throws Exception {
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		ThumbnailSQLPersistence mockedThumbnailPersistence = mock(ThumbnailSQLPersistence.class);
		when(mockedThumbnailPersistence.create(any(ThumbnailData.class))).thenReturn(this.createThumbnailData("2596",true));
		ThumbnailHandler th = new ThumbnailHandler();
		th.setDocPersistence(mockedDocPersistence);
		th.setPersistenceManager(mockedThumbnailPersistence);
		
		assertTrue(th.insertDefaulThumbnail(2596));
	}
	
	@Test(expected=Exception.class)
	public void testInsertDefaulThumbnailExceptionExpectedBecauseParentFileIdZero() throws Exception {
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		ThumbnailSQLPersistence mockedThumbnailPersistence = mock(ThumbnailSQLPersistence.class);
		mockedThumbnailPersistence.setDocPersistence(mockedDocPersistence);
		ThumbnailHandler th = new ThumbnailHandler();
		th.setDocPersistence(mockedDocPersistence);
		th.setPersistenceManager(mockedThumbnailPersistence);
		Exception ex=null;
		try{
			th.insertDefaulThumbnail(0);
		}catch(Exception e){
			System.out.println(e);
			ex=e;
		}
		if(ex!=null){
			throw ex;
		}
	}
	
	@Test
	public void testInsertDefaulThumbnailNullReturnedBecauseParentFileCannotBeFound() throws Exception {
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		when(mockedDocPersistence.get(any(Long.class))).thenReturn(null);
		
		ThumbnailSQLPersistence mockedThumbnailPersistence = mock(ThumbnailSQLPersistence.class);
		mockedThumbnailPersistence.setDocPersistence(mockedDocPersistence);
		
		ThumbnailHandler th = new ThumbnailHandler();
		th.setPersistenceManager(mockedThumbnailPersistence);

		assertNull(th.insertDefaulThumbnailReturnInsertedData(1596));
	
	}

	@Test
	public void testInsertThumbnailSuccess() throws Exception {
		File f = mock(File.class);
		when(f.exists()).thenReturn(true);
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		ThumbnailSQLPersistence mockedThumbnailPersistence = mock(ThumbnailSQLPersistence.class);
		when(mockedThumbnailPersistence.insertThumbnail(f, 2596)).thenReturn(this.createThumbnailData("2596",true));
		ThumbnailHandler th = new ThumbnailHandler();
		th.setDocPersistence(mockedDocPersistence);
		th.setPersistenceManager(mockedThumbnailPersistence);
		
		assertTrue(th.insertThumbnail(f, 2596));
	}

	@Test
	public void testCopyThumbnailDefaultSuccess() throws Exception {
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		ThumbnailSQLPersistence mockedThumbnailPersistence = mock(ThumbnailSQLPersistence.class);
		when(mockedThumbnailPersistence.getThumbnail(2596, true)).thenReturn(this.createThumbnailData("2596",true));
		when(mockedThumbnailPersistence.create(any(ThumbnailData.class))).thenReturn(this.createThumbnailData("2597",true));
		ThumbnailHandler th = new ThumbnailHandler();
		th.setDocPersistence(mockedDocPersistence);
		th.setPersistenceManager(mockedThumbnailPersistence);
		
		ReturnedMessage r = th.copyThumbnail(2596, 2597);
		assertTrue(r.getType() == FileHandler.SUCCESS_MESSAGE);
	}
	
	@Test
	public void testCopyThumbnailDefaultFailThePersistenceSystemCouldNotInsertDefaultThumbnail() throws Exception {
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		ThumbnailSQLPersistence mockedThumbnailPersistence = mock(ThumbnailSQLPersistence.class);
		when(mockedThumbnailPersistence.getThumbnail(2596, true)).thenReturn(this.createThumbnailData("2596",true));
		when(mockedThumbnailPersistence.create(any(ThumbnailData.class))).thenReturn(this.createThumbnailData(0,"0",true));
		ThumbnailHandler th = new ThumbnailHandler();
		th.setDocPersistence(mockedDocPersistence);
		th.setPersistenceManager(mockedThumbnailPersistence);
		
		ReturnedMessage r = th.copyThumbnail(2596, 2597);
		assertTrue(r.getType() == FileHandler.ERROR_MESSAGE);
	}
	
	@Test
	public void testCopyThumbnailNotDefaultSuccess() throws Exception {
		File f = mock(File.class);
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		ThumbnailSQLPersistence mockedThumbnailPersistence = mock(ThumbnailSQLPersistence.class);
		when(mockedThumbnailPersistence.getThumbnail(2596, true)).thenReturn(this.createThumbnailData("2596",false));
		ThumbnailData dt = this.createThumbnailData("2596",false);
		dt.setThumbnailFile(f);
		when(mockedThumbnailPersistence.insertThumbnail(f, 2596)).thenReturn(this.createThumbnailData("2597",false));
		ThumbnailHandler th = new ThumbnailHandler();
		th.setDocPersistence(mockedDocPersistence);
		th.setPersistenceManager(mockedThumbnailPersistence);
		
		ReturnedMessage r = th.copyThumbnail(2596, 2597);
		assertTrue(r.getType() == FileHandler.ERROR_MESSAGE);
	}
	
	@Test
	public void testCopyThumbnailNotDefaultFailThePersistenceSystemCouldNotInsertDefaultThumbnail() throws Exception {
		File f = mock(File.class);
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		ThumbnailSQLPersistence mockedThumbnailPersistence = mock(ThumbnailSQLPersistence.class);
		when(mockedThumbnailPersistence.getThumbnail(2596, true)).thenReturn(this.createThumbnailData("2596",false));
		when(mockedThumbnailPersistence.insertThumbnail(f, 2596)).thenReturn(this.createThumbnailData("0",false));
		ThumbnailHandler th = new ThumbnailHandler();
		th.setDocPersistence(mockedDocPersistence);
		th.setPersistenceManager(mockedThumbnailPersistence);
		
		ReturnedMessage r = th.copyThumbnail(2596, 2597);
		assertTrue(r.getType() == FileHandler.ERROR_MESSAGE);
	}

	
	@Test
	public void testCreateThumbnailForZipFileFailWithnullParameter() {
		ThumbnailHandler th = new ThumbnailHandler();
		ReturnedMessage r = th.createThumbnailForZipFile(null, "myZip.zip");
		assertTrue(r.getType() == FileHandler.ERROR_MESSAGE);
	}
	
	@Test
	public void testCreateThumbnailForZipFileFailCauseCannotGetZipDocument() throws Exception {
		ThumbnailHandler th = new ThumbnailHandler();
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		when(mockedDocPersistence.get(any(String.class))).thenReturn(null);
		th.setDocPersistence(mockedDocPersistence);
		ReturnedMessage r = th.createThumbnailForZipFile("root/test/", "myZip.zip");
		assertTrue(r.getType() == FileHandler.ERROR_MESSAGE);
	}
	
	@Test
	public void testCreateThumbnailForZipFileFailCauseCreateThumbnailFails() throws Exception {
		ThumbnailHandler th = new ThumbnailHandler();
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("23156");
		when(mockedDocPersistence.get(any(String.class))).thenReturn(doc);
		ThumbnailSQLPersistence mockedThumbnailPersistence = mock(ThumbnailSQLPersistence.class);
		when(mockedThumbnailPersistence.create(any(ThumbnailData.class))).thenReturn(this.createThumbnailData(0,"0",false));
		th.setDocPersistence(mockedDocPersistence);
		th.setPersistenceManager(mockedThumbnailPersistence);
		
		ReturnedMessage r = th.createThumbnailForZipFile("root/test/", "myZip.zip");
		assertTrue(r.getType() == FileHandler.ERROR_MESSAGE);
	}
	
	@Test
	public void testCreateThumbnailForZipFileSuccess() throws Exception {
		ThumbnailHandler th = new ThumbnailHandler();
		DocumentOnServerSQLPersistence mockedDocPersistence = mock(DocumentOnServerSQLPersistence.class);
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("23156");
		when(mockedDocPersistence.get(any(String.class))).thenReturn(doc);
		ThumbnailSQLPersistence mockedThumbnailPersistence = mock(ThumbnailSQLPersistence.class);
		when(mockedThumbnailPersistence.create(any(ThumbnailData.class))).thenReturn(this.createThumbnailData("3456",false));
		th.setDocPersistence(mockedDocPersistence);
		th.setPersistenceManager(mockedThumbnailPersistence);
		
		ReturnedMessage r = th.createThumbnailForZipFile("root/test/", "myZip.zip");
		assertTrue(r.getType() == FileHandler.SUCCESS_MESSAGE);
	}

	@Test
	public void testDeleteThumbnailSuccess() throws Exception {
		IIvyScriptObjectEnvironment my = new MyIIvyScriptObjectEnvironment();
		mockStatic(IvyScriptObjectEnvironmentContext.class);
		when(IvyScriptObjectEnvironmentContext.getIvyScriptObjectEnvironment()).thenReturn(my);
		
		ThumbnailSQLPersistence mockedThumbnailPersistence = mock(ThumbnailSQLPersistence.class);
		when(mockedThumbnailPersistence.delete(any(ThumbnailData.class))).thenReturn(true);
		ThumbnailHandler th = new ThumbnailHandler();
		th.setPersistenceManager(mockedThumbnailPersistence);
		assertTrue(th.deleteThumbnail(2135, true));
	}
	
	@Test
	public void testDeleteThumbnailFailCausePersistenceCannotDelete() throws Exception {
		IIvyScriptObjectEnvironment my = new MyIIvyScriptObjectEnvironment();
		mockStatic(IvyScriptObjectEnvironmentContext.class);
		when(IvyScriptObjectEnvironmentContext.getIvyScriptObjectEnvironment()).thenReturn(my);
		
		ThumbnailSQLPersistence mockedThumbnailPersistence = mock(ThumbnailSQLPersistence.class);
		when(mockedThumbnailPersistence.delete(any(ThumbnailData.class))).thenReturn(false);
		ThumbnailHandler th = new ThumbnailHandler();
		th.setPersistenceManager(mockedThumbnailPersistence);
		assertFalse(th.deleteThumbnail(2135, true));
	}

	@Test
	public void testDeleteAndRollBackThumbnailFailGivenPreviousDocIsNull() throws Exception {
		ThumbnailHandler th = new ThumbnailHandler();
		ReturnedMessage r = th.deleteAndRollBackThumbnail(null, new java.io.File(""), true, true);
		assertTrue(r.getType() == FileHandler.ERROR_MESSAGE);
	}
	
	@Test
	public void testDeleteAndRollBackThumbnailFailGivenThumbnailFileDoesNotExists() throws Exception {
		ThumbnailHandler th = new ThumbnailHandler();
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("23156");
		ReturnedMessage r = th.deleteAndRollBackThumbnail(doc, new java.io.File("gdfgadga"), true, true);
		assertTrue(r.getType() == FileHandler.ERROR_MESSAGE);
	}
	
	@Test
	public void testDeleteAndRollBackThumbnailFailPersistenceCannotDeleteThumbnail() throws Exception {
		ThumbnailSQLPersistence mockedThumbnailPersistence = mock(ThumbnailSQLPersistence.class);
		when(mockedThumbnailPersistence.delete(any(ThumbnailData.class))).thenReturn(false);
		ThumbnailHandler th = new ThumbnailHandler();
		th.setPersistenceManager(mockedThumbnailPersistence);
		File f = mock(File.class);
		when(f.exists()).thenReturn(true);
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("23156");
		ReturnedMessage r = th.deleteAndRollBackThumbnail(doc, f, true, true);
		assertTrue(r.getType() == FileHandler.ERROR_MESSAGE);
	}
	
	@Test
	public void testDeleteAndRollBackThumbnailSuccess() throws Exception {
		ThumbnailSQLPersistence mockedThumbnailPersistence = mock(ThumbnailSQLPersistence.class);
		when(mockedThumbnailPersistence.delete(any(ThumbnailData.class))).thenReturn(true);
		when(mockedThumbnailPersistence.create(any(ThumbnailData.class))).thenReturn(this.createThumbnailData("13516", true));
		ThumbnailHandler th = new ThumbnailHandler();
		th.setPersistenceManager(mockedThumbnailPersistence);
		File f = mock(File.class);
		when(f.exists()).thenReturn(true);
		DocumentOnServer doc = new DocumentOnServer();
		doc.setFileID("23156");
		doc.setVersionnumber(2);
		ReturnedMessage r = th.deleteAndRollBackThumbnail(doc, f, true, true);
		assertTrue(r.getType() == FileHandler.SUCCESS_MESSAGE);
	}

	@Test
	public void testGetThumbnailsWithJavaFile() {
	}

	@Test
	public void testGetDocumentsNoneThumbnail() {
	}

	@Test
	public void testCleanThumbnailFolder() {
	}
	
	private ThumbnailData createThumbnailData(String fileid,boolean useDefault) {
		ThumbnailData data = new ThumbnailData();
		data.setOrgFileId(fileid);
		data.setUseDefault(useDefault);
		data.setThumbnailId(1);
		return data;
	}
	
	private ThumbnailData createThumbnailData(long thumbnailId, String fileid,boolean useDefault) {
		ThumbnailData data = new ThumbnailData();
		data.setOrgFileId(fileid);
		data.setUseDefault(useDefault);
		data.setThumbnailId(thumbnailId);
		return data;
	}
	
	

}
