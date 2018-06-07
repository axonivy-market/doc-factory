package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.addons.docfactory.response.ResponseHandler;
import ch.ivyteam.ivy.cm.IContentManagementSystem;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.log.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Ivy.class, ThirdPartyLicenses.class})
public class BaseDocFactoryTest {
	
	private static final String TEST_DIRECTORY_NAME = "test_directory";
	
	@Before
	public void setup() throws Exception {
		Logger mockLogger = mock(Logger.class);
		doNothing().when(mockLogger).error(any(String.class));
		doNothing().when(mockLogger).info(any(String.class));
		doNothing().when(mockLogger).debug(any(String.class));
		
		IContentManagementSystem mockedCms = mock(IContentManagementSystem.class);
		when(mockedCms.co(any(String.class))).thenReturn("");
		
		mockStatic(ThirdPartyLicenses.class);
		mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mockLogger);
		when(Ivy.cms()).thenReturn(mockedCms);
		when(ThirdPartyLicenses.getDocumentFactoryLicense()).thenReturn(null);
	}
	
	@After
	public void clean_generated_test_documents() {
		java.io.File directory_for_testing = new java.io.File(TEST_DIRECTORY_NAME);
		if(directory_for_testing.isDirectory()) {
			for (java.io.File f : directory_for_testing.listFiles()) {
				f.delete();
			}
			directory_for_testing.delete();
		}
	}

	@Test
	public void getInstance_returns_not_null_docFactory() {
		BaseDocFactory docFactory = BaseDocFactory.getInstance();
		
		assertNotNull("Getting an instance of the Doc Factory returns a non null DocFactory.", docFactory);
	}
	
	@Test
	public void getInstance_with_null_ResponseHandler_returns_not_null_docFactory() {
		BaseDocFactory docFactory = BaseDocFactory.getInstance().withResponseHandler(null);
		
		assertNotNull("Getting an instance of the Doc Factory returns a non null DocFactory.", docFactory);
	}
	
	@Test
	public void getInstance_withResponseHandler_returns_not_null_docFactory() {
		BaseDocFactory docFactory = BaseDocFactory.getInstance().withResponseHandler(new MyResponseHandler());
		
		assertNotNull("Getting an instance of the Doc Factory returns a non null DocFactory.", docFactory);
	}
	
	@Test
	public void getResponseHandler_returns_null_if_no_responseHandler_specified() {
		BaseDocFactory docFactory = BaseDocFactory.getInstance();
		ResponseHandler responseHandler = docFactory.getResponsHandler();
		
		assertNull("A Doc Factory instanciated without ResponseHandler has a null ResponseHandler", responseHandler);
	}
	
	@Test
	public void getResponseHandler_returns_null_if_no_responseHandler_set_with_null() {
		BaseDocFactory docFactory = BaseDocFactory.getInstance().withResponseHandler(null);
		ResponseHandler responseHandler = docFactory.getResponsHandler();
		
		assertNull("A Doc Factory instanciated with null ResponseHandler has a null ResponseHandler", responseHandler);
	}
	
	@Test
	public void getResponseHandler_returns_responseHandler_set() {
		ResponseHandler responseHandler = new MyResponseHandler();
		BaseDocFactory docFactory = BaseDocFactory.getInstance().withResponseHandler(responseHandler);
		ResponseHandler result = docFactory.getResponsHandler();
		
		assertEquals(responseHandler, result);
	}
	
	@Test
	public void generateBlankDocument_creates_one_file() {
		ResponseHandler responseHandler = new MyResponseHandler();
		BaseDocFactory docFactory = BaseDocFactory.getInstance().withResponseHandler(responseHandler);

		FileOperationMessage result = docFactory.generateBlankDocument("test", TEST_DIRECTORY_NAME, "pdf");
		assertTrue("generating a blank document creates one File", result.getFiles().get(0).exists());
	}
	
	private class MyResponseHandler implements ResponseHandler {

		@Override
		public void handleDocFactoryResponse(
				FileOperationMessage fileOperationMessage) {
			System.out.println("Error occurred: " + fileOperationMessage.getMessage());
			return;
		}
		
	}

}
