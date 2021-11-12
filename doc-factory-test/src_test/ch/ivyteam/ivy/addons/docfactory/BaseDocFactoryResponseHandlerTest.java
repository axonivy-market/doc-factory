package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Test;
import org.mockito.Mockito;

import ch.ivyteam.ivy.addons.docfactory.response.ResponseHandler;

public class BaseDocFactoryResponseHandlerTest extends DocFactoryTest {
	
	
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
		
		File template = new File(this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());

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
