package ch.ivyteam.ivy.addons.docfactory;

import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.makeFile;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.makePerson;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class RemoveBlankPagesIT {

  DocumentTemplate documentTemplate;

  @BeforeEach
  public void setup() throws Exception {
    File tpl = new File(
            this.getClass().getResource("resources/template_with_blank_pages.docx").toURI().getPath());

    documentTemplate = DocumentTemplate.withTemplate(tpl).useLocale(Locale.forLanguageTag("de-CH"));
  }

  @Test
  public void default_not_remove_blank_pages_pdf() {
    File resultFile = makeFile("test/documentTemplate/blankPages/default_not_remove_blank_pages.pdf");

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(makePerson())
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void default_not_remove_blank_pages_docx() {
    File resultFile = makeFile("test/documentTemplate/blankPages/default_not_remove_blank_pages.docx");

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(makePerson())
    		.produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void default_not_remove_blank_pages_doc() {
    File resultFile = makeFile("test/documentTemplate/blankPages/default_not_remove_blank_pages.doc");

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(makePerson())
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void default_not_remove_blank_pages_odt() {
    File resultFile = makeFile("test/documentTemplate/blankPages/default_not_remove_blank_pages.odt");

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(makePerson())
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void remove_blank_pages_pdf() {
    File resultFile = makeFile("test/documentTemplate/blankPages/remove_blank_pages.pdf");

    DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance()
            .removeBlankPages(true);

    FileOperationMessage result = documentTemplate.withDocumentCreationOptions(documentCreationOptions)
            .putDataAsSourceForMailMerge(makePerson()).produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void remove_blank_pages_docx() {
    File resultFile = makeFile("test/documentTemplate/blankPages/remove_blank_pages.docx");

    DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance()
            .removeBlankPages(true);

    FileOperationMessage result = documentTemplate.withDocumentCreationOptions(documentCreationOptions)
            .putDataAsSourceForMailMerge(makePerson()).produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void remove_blank_pages_doc() {
    File resultFile = makeFile("test/documentTemplate/blankPages/remove_blank_pages.doc");

    DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance()
            .removeBlankPages(true);

    FileOperationMessage result = documentTemplate.withDocumentCreationOptions(documentCreationOptions)
            .putDataAsSourceForMailMerge(makePerson()).produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void remove_blank_pages_odt() {
    File resultFile = makeFile("test/documentTemplate/blankPages/remove_blank_pages.odt");

    DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance()
            .removeBlankPages(true);

    FileOperationMessage result = documentTemplate.withDocumentCreationOptions(documentCreationOptions)
            .putDataAsSourceForMailMerge(makePerson()).produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }
}
