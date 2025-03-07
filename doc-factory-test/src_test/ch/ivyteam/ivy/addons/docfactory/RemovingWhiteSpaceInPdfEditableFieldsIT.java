package ch.ivyteam.ivy.addons.docfactory;

import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.makeFile;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;
import ch.ivyteam.ivy.addons.docfactory.pdf.PdfOptions;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class RemovingWhiteSpaceInPdfEditableFieldsIT {

  File template;
  DocumentTemplate documentTemplate;

  @BeforeEach
  public void setup() throws Exception {
    File tpl = new File(
            this.getClass().getResource(DocFactoryTest.TEMPLATE_FOR_TESTING_LONG_EDITABLE_FIELDS_DOCX).toURI().getPath());

    documentTemplate = DocumentTemplate.withTemplate(tpl).useLocale(Locale.forLanguageTag("de-CH"))
            .putDataAsSourceForMailMerge(DocFactoryTest.makePerson());
  }

  @Test
  public void defaultDoesNotRemoveWhiteSpaceInPdfEditableFields() {
    DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance()
        .withPdfOptions(PdfOptions.getInstance().hasToKeepFormFieldsEditable(true));
    documentTemplate.withDocumentCreationOptions(documentCreationOptions);

    File resultFile = DocFactoryTest.makeFile(
            "test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/default_not_remove_fieldSpaces.pdf");

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void removeWhiteSpaceInPdfEditableFields() {
    DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance().withPdfOptions(
        PdfOptions.getInstance().hasToKeepFormFieldsEditable(true).hasToRemoveWhiteSpaceInPdfEditableFields(true));
    documentTemplate.withDocumentCreationOptions(documentCreationOptions);

    File resultFile = makeFile(
            "test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/remove_fieldSpaces.pdf");

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void removeWhiteSpaceInPdfEditableFields_has_no_effect_if_fieldsNotEditable() {
    DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance().withPdfOptions(
        PdfOptions.getInstance().hasToKeepFormFieldsEditable(false).hasToRemoveWhiteSpaceInPdfEditableFields(true));
    documentTemplate.withDocumentCreationOptions(documentCreationOptions);

    File resultFile = makeFile(
            "test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/fieldsNotEditables.pdf");

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void removeWhiteSpaceInPdfEditableFields_has_no_effect_if_output_is_doc() {
    DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance().withPdfOptions(
        PdfOptions.getInstance().hasToKeepFormFieldsEditable(true).hasToRemoveWhiteSpaceInPdfEditableFields(true));
    documentTemplate.withDocumentCreationOptions(documentCreationOptions);

    File resultFile = makeFile("test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/aDoc.doc");

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void removeWhiteSpaceInPdfEditableFields_has_no_effect_if_output_is_docx() {    
    DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance().withPdfOptions(
        PdfOptions.getInstance().hasToKeepFormFieldsEditable(true).hasToRemoveWhiteSpaceInPdfEditableFields(true));
    documentTemplate.withDocumentCreationOptions(documentCreationOptions);

    File resultFile = makeFile("test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/aDocx.docx");

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void removeWhiteSpaceInPdfEditableFields_has_no_effect_if_output_is_odt() {    
    DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance().withPdfOptions(
        PdfOptions.getInstance().hasToKeepFormFieldsEditable(true).hasToRemoveWhiteSpaceInPdfEditableFields(true));
    documentTemplate.withDocumentCreationOptions(documentCreationOptions);

    File resultFile = makeFile("test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/anOdt.odt");

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void removeWhiteSpaceInPdfEditableFields_has_no_effect_if_output_is_html() {
    DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance().withPdfOptions(
        PdfOptions.getInstance().hasToKeepFormFieldsEditable(true).hasToRemoveWhiteSpaceInPdfEditableFields(true));
    documentTemplate.withDocumentCreationOptions(documentCreationOptions);

    File resultFile = makeFile("test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/anHtml.html");

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void removeWhiteSpaceInPdfEditableFields_has_no_effect_if_output_is_txt() {
    DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance().withPdfOptions(
        PdfOptions.getInstance().hasToKeepFormFieldsEditable(true).hasToRemoveWhiteSpaceInPdfEditableFields(true));
    documentTemplate.withDocumentCreationOptions(documentCreationOptions);

    File resultFile = makeFile("test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/aTxt.txt");

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }
}
