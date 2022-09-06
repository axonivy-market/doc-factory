package ch.ivyteam.ivy.addons.docfactory;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
class DocumentTemplateFormatsGenerationIT {

  File template;
  DocumentTemplate documentTemplate;

  @BeforeEach
  void setup() throws Exception {
    var tpl = new File(this.getClass().getResource(DocFactoryTest.TEMPLATE_PERSON_DOCX).toURI().getPath());
    documentTemplate = DocumentTemplate.withTemplate(tpl).putDataAsSourceForSimpleMailMerge(DocFactoryTest.makePerson())
            .useLocale(Locale.forLanguageTag("de-CH"));
  }

  @Test
  void produceDocument_html() {
    documentTemplate.setOutputFormat("html");
    var resultFile = DocFactoryTest.makeFile("test/documentTemplate/formats/simple_mail_merge_test.html");
    var result = documentTemplate.produceDocument(resultFile);
    assertThat(result).isNotNull();
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  void produceDocument_doc() {
    documentTemplate.setOutputFormat("doc");
    var resultFile = DocFactoryTest.makeFile("test/documentTemplate/formats/simple_mail_merge_test.doc");
    var result = documentTemplate.produceDocument(resultFile);
    assertThat(result).isNotNull();
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  void produceDocument_docx() {
    documentTemplate.setOutputFormat("docx");
    var resultFile = DocFactoryTest.makeFile("test/documentTemplate/formats/simple_mail_merge_test.docx");
    var result = documentTemplate.produceDocument(resultFile);
    assertThat(result).isNotNull();
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  void produceDocument_odt() {
    documentTemplate.setOutputFormat("odt");
    var resultFile = DocFactoryTest.makeFile("test/documentTemplate/formats/simple_mail_merge_test.odt");
    var result = documentTemplate.produceDocument(resultFile);
    assertThat(result).isNotNull();
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  void produceDocument_txt() {
    documentTemplate.setOutputFormat("txt");
    var resultFile = DocFactoryTest.makeFile("test/documentTemplate/formats/simple_mail_merge_test.txt");
    var result = documentTemplate.produceDocument(resultFile);
    assertThat(result).isNotNull();
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  void produceDocument_pdf() {
    documentTemplate.setOutputFormat("pdf");
    var resultFile = DocFactoryTest.makeFile("test/documentTemplate/formats/simple_mail_merge_test.pdf");
    var result = documentTemplate.produceDocument(resultFile);
    assertThat(result).isNotNull();
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }
}
