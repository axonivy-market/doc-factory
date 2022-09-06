package ch.ivyteam.ivy.addons.docfactory.aspose;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.PdfFactory;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
class PdfFactoryTest {

  private static File PDF1;
  private static File PDF2;

  @BeforeAll
  static void setupClass() throws URISyntaxException {
    PDF1 = new File(PdfFactoryTest.class.getResource("../resources/files/pdf1.pdf").toURI().getPath());
    PDF2 = new File(PdfFactoryTest.class.getResource("../resources/files/pdf2.pdf").toURI().getPath());
  }

  @Test
  void get() {
	assertThat(PdfFactory.get()).isInstanceOf(PdfFactory.class);
  }

  @Test
  void appendPdfFiles() throws Exception {
    var files = List.of(PDF1, PDF2);
    var result = PdfFactory.get().appendPdfFiles("appended_pdf_files.pdf", files).getJavaFile();
    assertThat(result.isFile()).isTrue();
  }
}
