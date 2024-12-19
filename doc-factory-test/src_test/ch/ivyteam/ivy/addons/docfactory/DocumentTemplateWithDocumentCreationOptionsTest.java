package ch.ivyteam.ivy.addons.docfactory;

import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.TEMPLATE_WITH_FIELDS_FORM_DOCX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;
import ch.ivyteam.ivy.addons.docfactory.pdf.PdfOptions;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class DocumentTemplateWithDocumentCreationOptionsTest {

  File template;

  @BeforeEach
  public void setup() throws Exception {
    template = new File(this.getClass().getResource(TEMPLATE_WITH_FIELDS_FORM_DOCX).toURI().getPath());
  }

  @Test
  public void default_documentTemplate_DocumentCreationOptions_pdfFormFields_not_editable() {
    DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template);
    DocumentCreationOptions documentCreationOptions = documentTemplate.getDocumentCreationOptions();
    assertThat(documentCreationOptions.getPdfOptions().isKeepFormFieldsEditableInPdf()).isFalse();
  }

  @Test
  public void default_documentTemplate_DocumentCreationOptions_backed_in_documentFactory() {
    DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template);
    DocumentCreationOptions documentCreationOptions = documentTemplate.getDocumentCreationOptions();
    assertThat(documentTemplate.getDocumentFactory().documentCreationOptions).isEqualTo(documentCreationOptions);
  }

  @Test
  public void set_documentTemplate_null_DocumentCreationOptions_throws_IAE() {
	  assertThatThrownBy(() -> DocumentTemplate.withTemplate(template).withDocumentCreationOptions(null));
  }

  @Test
  public void set_documentTemplate_DocumentCreationOptions() {    
    DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template).withDocumentCreationOptions(
            DocumentCreationOptions.getInstance().withPdfOptions(PdfOptions.getInstance().hasToKeepFormFieldsEditable(false)));
    DocumentCreationOptions documentCreationOptions = documentTemplate.getDocumentCreationOptions();
    assertThat(documentCreationOptions.getPdfOptions().isKeepFormFieldsEditableInPdf()).isFalse();
  }

  @Test
  public void documentTemplate_set_DocumentCreationOptions_backed_in_documentFactory() {
    DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template).withDocumentCreationOptions(
            DocumentCreationOptions.getInstance().withPdfOptions(PdfOptions.getInstance().hasToKeepFormFieldsEditable(false)));
    DocumentCreationOptions documentCreationOptions = documentTemplate.getDocumentCreationOptions();
    assertThat(documentTemplate.getDocumentFactory().documentCreationOptions).isEqualTo(documentCreationOptions);
  }
}
