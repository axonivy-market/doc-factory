package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class DocumentTemplateFormatsGenerationIT extends DocFactoryTest {

  File template;
  DocumentTemplate documentTemplate;

  @Override
  @Before
  public void setup() throws Exception {
    super.setup();
    File tpl = new File(this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());

    documentTemplate = DocumentTemplate.withTemplate(tpl).putDataAsSourceForSimpleMailMerge(makePerson())
            .useLocale(Locale.forLanguageTag("de-CH"));
  }

  @Test
  public void produceDocument_html() {
    documentTemplate.setOutputFormat("html");

    File resultFile = makeFile("test/documentTemplate/formats/simple_mail_merge_test.html");

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
  }

  @Test
  public void produceDocument_doc() {
    documentTemplate.setOutputFormat("doc");

    File resultFile = makeFile("test/documentTemplate/formats/simple_mail_merge_test.doc");

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
  }

  @Test
  public void produceDocument_docx() {
    documentTemplate.setOutputFormat("docx");

    File resultFile = makeFile("test/documentTemplate/formats/simple_mail_merge_test.docx");

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
  }

  @Test
  public void produceDocument_odt() {
    documentTemplate.setOutputFormat("odt");

    File resultFile = makeFile("test/documentTemplate/formats/simple_mail_merge_test.odt");

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
  }

  @Test
  public void produceDocument_txt() {
    documentTemplate.setOutputFormat("txt");

    File resultFile = makeFile("test/documentTemplate/formats/simple_mail_merge_test.txt");

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
  }

  @Test
  public void produceDocument_pdf() {
    documentTemplate.setOutputFormat("pdf");

    File resultFile = makeFile("test/documentTemplate/formats/simple_mail_merge_test.pdf");

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
  }

}
