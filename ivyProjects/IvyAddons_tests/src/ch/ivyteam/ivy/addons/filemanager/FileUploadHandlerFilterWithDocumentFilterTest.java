package ch.ivyteam.ivy.addons.filemanager;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.addons.filemanager.database.security.DocumentFilter;
import ch.ivyteam.ivy.addons.filemanager.database.security.DocumentFilterActionEnum;
import ch.ivyteam.ivy.addons.filemanager.database.security.DocumentFilterAnswer;
import ch.ivyteam.ivy.addons.filemanager.ivy.implemented.MyCMS;
import ch.ivyteam.ivy.addons.filemanager.ivy.implemented.MyGlobalVariableContext;
import ch.ivyteam.ivy.addons.filemanager.ivy.implemented.MyILogger;
import ch.ivyteam.ivy.cm.IContentManagementSystem;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.List;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

/**
 * @author ec
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Ivy.class, IContentManagementSystem.class})
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FileUploadHandlerFilterWithDocumentFilterTest {

	@Test
	public void checkUploadFilteringReturnAcceptedAndRejectedFilesNoDocumentFilterSetAllAccepted() {
		
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		when(Ivy.cms().co(any(String.class))).thenReturn("");
		when(Ivy.log()).thenReturn(mock(MyILogger.class));
		
		FileUploadHandler<?> fuh = new FileUploadHandler();
		
		List<java.io.File> files = this.makeListOfFilesToUpload();
		
		List<java.io.File> acceptedFiles= (List<File>) fuh.checkUploadFilteringReturnAcceptedAndRejectedFiles(null, makeArrayOfFilePathsToUpload(), "root/test").get(0);
		List<java.io.File> rejectedFiles= (List<File>) fuh.checkUploadFilteringReturnAcceptedAndRejectedFiles(null,  makeArrayOfFilePathsToUpload(), "root/test").get(1);
		assertTrue(acceptedFiles.containsAll(files));
		assertTrue(rejectedFiles.isEmpty());
	}
	
	@Test
	public void checkUploadFilteringReturnAcceptedAndRejectedFilesDocumentFilterSetExeFilesRejected() {
		
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		MyGlobalVariableContext var = mock(MyGlobalVariableContext.class);
		when(var.get(any(String.class))).thenReturn("");
		MyILogger log = mock(MyILogger.class);
		

		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		when(Ivy.var()).thenReturn(var);
		when(Ivy.cms().co(any(String.class))).thenReturn("");
		when(Ivy.log()).thenReturn(log);
		FileUploadHandler<?> fuh = new FileUploadHandler();
		BasicConfigurationController config = new BasicConfigurationController();
		config.setDocumentFilter(makeMyDocumentFilter());
		fuh.setConfig(config);
		List<java.io.File> files = this.makeListOfFilesToUpload();
		
		List<java.io.File> acceptedFiles= (List<File>) fuh.checkUploadFilteringReturnAcceptedAndRejectedFiles(null,  makeArrayOfFilePathsToUpload(), "root/test").get(0);
		List<java.io.File> rejectedFiles= (List<File>) fuh.checkUploadFilteringReturnAcceptedAndRejectedFiles(null,  makeArrayOfFilePathsToUpload(), "root/test").get(1);
		assertFalse(acceptedFiles.containsAll(files));
		assertFalse(rejectedFiles.isEmpty());
		assertTrue(rejectedFiles.contains(new java.io.File("D:/folder/test.exe")));
		assertTrue(acceptedFiles.containsAll(makeListOfFilesToUploadWithoutExeFile()));
	}
	
	@Test
	public void checkUploadFilteringReturnAcceptedAndRejectedFilesDocumentFilterSetNoExeFilesChoosedAllAccepted() {
		
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		MyGlobalVariableContext var = mock(MyGlobalVariableContext.class);
		when(var.get(any(String.class))).thenReturn("");

		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		when(Ivy.var()).thenReturn(var);
		when(Ivy.cms().co(any(String.class))).thenReturn("");
		when(Ivy.log()).thenReturn(mock(MyILogger.class));

		FileUploadHandler<?> fuh = new FileUploadHandler();
		BasicConfigurationController config = new BasicConfigurationController();
		config.setDocumentFilter(makeMyDocumentFilter());
		fuh.setConfig(config);
		List<java.io.File> files = this.makeListOfFilesToUploadWithoutExeFile();
		
		List<java.io.File> acceptedFiles= (List<File>) fuh.checkUploadFilteringReturnAcceptedAndRejectedFiles(null, makeArrayOfFilePathsToUploadWithoutExeFile(), "root/test").get(0);
		List<java.io.File> rejectedFiles= (List<File>) fuh.checkUploadFilteringReturnAcceptedAndRejectedFiles(null, makeArrayOfFilePathsToUploadWithoutExeFile(), "root/test").get(1);
		assertTrue(acceptedFiles.containsAll(files));
		assertTrue(rejectedFiles.isEmpty());
	}
	
	@Test
	public void checkUploadFilteringReturnAcceptedAndRejectedFilesDocumentFilterSetNoFilesChoosedAcceptedAndRejectedFileListsEmpty() {
		
		MyCMS myCMS = mock(MyCMS.class);
		when(myCMS.co(any(String.class))).thenReturn("");
		MyGlobalVariableContext var = mock(MyGlobalVariableContext.class);
		when(var.get(any(String.class))).thenReturn("");

		mockStatic(Ivy.class);
		when(Ivy.cms()).thenReturn(myCMS);
		when(Ivy.var()).thenReturn(var);
		when(Ivy.cms().co(any(String.class))).thenReturn("");
		when(Ivy.log()).thenReturn(mock(MyILogger.class));
		FileUploadHandler<?> fuh = new FileUploadHandler();
		BasicConfigurationController config = new BasicConfigurationController();
		config.setDocumentFilter(makeMyDocumentFilter());
		fuh.setConfig(config);
		
		List<java.io.File> acceptedFiles= (List<File>) fuh.checkUploadFilteringReturnAcceptedAndRejectedFiles(null, new String[0], "root/test").get(0);
		List<java.io.File> rejectedFiles= (List<File>) fuh.checkUploadFilteringReturnAcceptedAndRejectedFiles(null,  new String[0], "root/test").get(1);
		assertTrue(acceptedFiles.isEmpty());
		assertTrue(rejectedFiles.isEmpty());
	}
	
	
	private DocumentFilter makeMyDocumentFilter() {
		return new MyDocumentFilter();
	}
	
	private List<java.io.File> makeListOfFilesToUpload() {
		List<java.io.File> files = List.create(java.io.File.class);
		files.add(new java.io.File("D:/folder/test.doc"));
		files.add(new java.io.File("D:/folder/test.exe"));
		files.add(new java.io.File("D:/folder/test.bat"));
		files.add(new java.io.File("D:/folder/test.pdf"));
		return files;
	}
	
	private List<java.io.File> makeListOfFilesToUploadWithoutExeFile() {
		List<java.io.File> files = List.create(java.io.File.class);
		files.add(new java.io.File("D:/folder/test.doc"));
		files.add(new java.io.File("D:/folder/test.bat"));
		files.add(new java.io.File("D:/folder/test.pdf"));
		return files;
	}
	
	private String[] makeArrayOfFilePathsToUpload() {
		return new String[] {"D:/folder/test.doc","D:/folder/test.exe","D:/folder/test.bat","D:/folder/test.pdf"};
		
	}
	
	private String[] makeArrayOfFilePathsToUploadWithoutExeFile() {
		return new String[] {"D:/folder/test.doc","D:/folder/test.bat","D:/folder/test.pdf"};
	}
	
	private class MyDocumentFilter implements DocumentFilter {

		@Override
		public DocumentFilterAnswer allowAction(DocumentOnServer doc,
				DocumentFilterActionEnum action) {
			DocumentFilterAnswer answer = new DocumentFilterAnswer();
			answer.setAllow(true);
			if(action==DocumentFilterActionEnum.UPLOAD) {
				if(doc.getExtension().equalsIgnoreCase("exe")) {
					answer.setAllow(false);
				}
			} 
			return answer;
		}
		
	}
	


}
