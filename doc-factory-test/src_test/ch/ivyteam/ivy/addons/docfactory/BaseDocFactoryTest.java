package ch.ivyteam.ivy.addons.docfactory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.response.ResponseHandler;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class BaseDocFactoryTest {

  private static final String TEST_DIRECTORY_NAME = "test_directory";

  @AfterEach
  public void clean_generated_test_documents() {
    java.io.File directory_for_testing = new java.io.File(TEST_DIRECTORY_NAME);
    if (directory_for_testing.isDirectory()) {
      for (java.io.File f : directory_for_testing.listFiles()) {
        f.delete();
      }
      directory_for_testing.delete();
    }
  }

  @Test
  public void getInstance_returns_not_null_docFactory() {
    BaseDocFactory docFactory = BaseDocFactory.getInstance();
    assertThat(docFactory).isNotNull();
  }

  @Test
  public void getInstance_with_null_ResponseHandler_returns_not_null_docFactory() {
    BaseDocFactory docFactory = BaseDocFactory.getInstance().withResponseHandler(null);
    assertThat(docFactory).isNotNull();
  }

  @Test
  public void getInstance_withResponseHandler_returns_not_null_docFactory() {
    BaseDocFactory docFactory = BaseDocFactory.getInstance().withResponseHandler(new MyResponseHandler());
    assertThat(docFactory).isNotNull();
  }

  @Test
  public void getResponseHandler_returns_null_if_no_responseHandler_specified() {
    BaseDocFactory docFactory = BaseDocFactory.getInstance();
    ResponseHandler responseHandler = docFactory.getResponsHandler();
    assertThat(responseHandler).isNull();
  }

  @Test
  public void getResponseHandler_returns_null_if_no_responseHandler_set_with_null() {
    BaseDocFactory docFactory = BaseDocFactory.getInstance().withResponseHandler(null);
    ResponseHandler responseHandler = docFactory.getResponsHandler();
    assertThat(responseHandler).isNull();
  }

  @Test
  public void getResponseHandler_returns_responseHandler_set() {
    ResponseHandler responseHandler = new MyResponseHandler();
    BaseDocFactory docFactory = BaseDocFactory.getInstance().withResponseHandler(responseHandler);
    ResponseHandler result = docFactory.getResponsHandler();
    assertThat(responseHandler).isEqualTo(result);
  }

  @Test
  public void generateBlankDocument_creates_one_file() {
    ResponseHandler responseHandler = new MyResponseHandler();
    BaseDocFactory docFactory = BaseDocFactory.getInstance().withResponseHandler(responseHandler);
    FileOperationMessage result = docFactory.generateBlankDocument("test", TEST_DIRECTORY_NAME, "pdf");
    assertThat(result.getFiles().get(0).exists()).isTrue();
  }

  private class MyResponseHandler implements ResponseHandler {
    @Override
    public void handleDocFactoryResponse(FileOperationMessage fileOperationMessage) {
      System.out.println("Error occurred: " + fileOperationMessage.getMessage());
      return;
    }
  }
}
