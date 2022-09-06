package ch.ivyteam.ivy.addons.docfactory;

import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.TEMPLATE_PERSON_DOCX;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.response.ResponseHandler;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class BaseDocFactoryResponseHandlerTest {

  @Test
  public void withResponseHandler_set_responseHandler() {
    ResponseHandler responseHandler = new MyResponseHandler();
    BaseDocFactory docFactory = BaseDocFactory.getInstance().withResponseHandler(responseHandler);
    assertThat(responseHandler).isEqualTo(docFactory.getResponsHandler());
  }

  @Test
  public void ifResponseHandler_notSet_then_docFactory_ResponseHandler_is_null() {
    BaseDocFactory docFactory = BaseDocFactory.getInstance();
    assertThat(docFactory.getResponsHandler()).isNull();
  }

  @Test
  public void if_responseHandler_set_it_is_called_by_generating_a_blank_document() {
    BaseDocFactory docFactory = BaseDocFactory.getInstance().withResponseHandler(new MyResponseHandler()); 
    docFactory.generateBlankDocument("blank_test", "test", "pdf");
  }

  @Test
  public void if_responseHandler_set_it_is_called_by_generating_a_document_with_mail_merge()
          throws URISyntaxException {

    File template = new File(this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());

    DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template)
            .addMergeField("person.name", "Gauch").addMergeField("person.firstname", "Daniel")
            .withResponseHandler(new MyResponseHandler());

    FileOperationMessage result = null;
    File resultFile = new File("test/test5.pdf");
    try {
      if (resultFile.isFile()) {
        resultFile.delete();
      }
      result = documentTemplate.produceDocument(resultFile);
    } catch (Exception ex) {
      System.out.println("Exception : " + ex.toString());
    }
    if (result == null) {
      throw new IllegalStateException();
    }
    assertThat(result.isSuccess()).isTrue();
  }

  public class MyResponseHandler implements ResponseHandler {

    @Override
    public void handleDocFactoryResponse(
            FileOperationMessage fileOperationMessage) {
      if (fileOperationMessage.isError()) {
        // Oh my God ! An error !
      }
      if (fileOperationMessage.isSuccess()) {
        // I am the king of the world !
        // do whatever you want with the generated file(s)
        // fileOperationMessage.getFiles();
      }
    }
  }

}
