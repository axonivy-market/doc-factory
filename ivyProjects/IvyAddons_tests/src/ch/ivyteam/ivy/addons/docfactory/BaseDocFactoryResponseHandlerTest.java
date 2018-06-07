package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.addons.docfactory.response.ResponseHandler;
import ch.ivyteam.ivy.cm.IContentManagementSystem;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.log.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Ivy.class, ThirdPartyLicenses.class})
public class BaseDocFactoryResponseHandlerTest {
	
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

	@Test
	public void withResponseHandler_set_responseHandler() {
		ResponseHandler responseHandler = new MyResponseHandler();
		BaseDocFactory docFactory = BaseDocFactory.getInstance().withResponseHandler(responseHandler);
		
		assertEquals(responseHandler, docFactory.getResponsHandler());
	}
	
	@Test
	public void ifResponseHandler_notSet_then_docFactory_ResponseHandler_is_null() {
		BaseDocFactory docFactory = BaseDocFactory.getInstance();
		
		assertNull(docFactory.getResponsHandler());
	}
	
	@Test
	public void if_responseHandler_set_it_is_called_by_generating_a_blank_document() {
		ResponseHandler responseHandler = mock(MyResponseHandler.class);
		Mockito.doNothing().when(responseHandler).handleDocFactoryResponse(any(FileOperationMessage.class));
		
		BaseDocFactory docFactory = BaseDocFactory.getInstance().
				withResponseHandler(responseHandler); // Inject the ResponseHandler
		
		docFactory.generateBlankDocument("blank_test", "test", "pdf");
		
		verify(responseHandler, times(1)).handleDocFactoryResponse(any(FileOperationMessage.class));
	}
	
	@Test
	public void if_responseHandler_set_it_is_called_by_generating_a_document_with_mail_merge() throws URISyntaxException {
		
		ResponseHandler responseHandler = mock(MyResponseHandler.class);
		Mockito.doNothing().when(responseHandler).handleDocFactoryResponse(any(FileOperationMessage.class));
		
		File template = new File(this.getClass().getResource("template_person.docx").toURI().getPath());

		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				addMergeField("person.name", "Gauch").
				addMergeField("person.firstname", "Daniel").
				withResponseHandler(responseHandler); // Inject the ResponseHandler

		FileOperationMessage result = null;
		File resultFile = new File("test/test5.pdf");
		try {
			if(resultFile.isFile()) {
				resultFile.delete();
			}
			result = documentTemplate.produceDocument(resultFile);
		} catch (Exception ex) {
			System.out.println("Exception : " + ex.toString());
		}

		assertTrue(result.isSuccess());
		
		verify(responseHandler, times(1)).handleDocFactoryResponse(result);
	}
	
	public class MyResponseHandler implements ResponseHandler {

		@Override
		public void handleDocFactoryResponse(
				FileOperationMessage fileOperationMessage) {
			if(fileOperationMessage.isError()) {
				// Oh my God ! An error !
			}
			if(fileOperationMessage.isSuccess()) {
				// I am the king of the world !
				// do whatever you want with the generated file(s) fileOperationMessage.getFiles();
			}
		}
	}

}
