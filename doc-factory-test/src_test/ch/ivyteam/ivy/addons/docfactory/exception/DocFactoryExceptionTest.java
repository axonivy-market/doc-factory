package ch.ivyteam.ivy.addons.docfactory.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DocFactoryExceptionTest {

  @Test
  void docFactoryExceptionInstanciatedWithExceptionOnlyHasMessageOfThisException() {
    var ex = new Exception("An Exception occurred");
    var docFactoryException = new DocFactoryException(ex);
    assertThat(docFactoryException.getMessage()).isEqualTo(ex.getMessage());
  }

  @Test
  void docFactoryExceptionInstanciatedWithExceptionAndMessage() {
    var ex = new Exception("An Exception occurred");
    var docFactoryException = new DocFactoryException("dummy message", ex);
    assertThat(docFactoryException.getMessage()).isEqualTo("dummy message");
  }

  @Test
  void docFactoryExceptionInstanciatedWithExceptionAndMessageGetCause() {
    var ex = new Exception("An Exception occurred");
    var docFactoryException = new DocFactoryException("dummy message", ex);
    assertThat(docFactoryException.getCause()).isEqualTo(ex);
  }

  @Test
  void docFactoryExceptionInstanciatedWithExceptionGetCause() {
    var ex = new Exception("An Exception occurred");
    var docFactoryException = new DocFactoryException(ex);
    assertThat(docFactoryException.getCause()).isEqualTo(ex);
  }
}
