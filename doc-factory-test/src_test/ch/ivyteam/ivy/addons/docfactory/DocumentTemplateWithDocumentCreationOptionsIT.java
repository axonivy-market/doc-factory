package ch.ivyteam.ivy.addons.docfactory;

import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.makeFile;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.makePerson;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class DocumentTemplateWithDocumentCreationOptionsIT {

  File template;

  @BeforeEach
  public void setup() throws Exception {
    template = new File(this.getClass().getResource(DocFactoryTest.TEMPLATE_WITH_FIELDS_FORM_DOCX).toURI().getPath());
  }

  @Test
  public void default_produces_fieldForm_not_editablePDF() {
    var documentTemplate = DocumentTemplate.withTemplate(template).putDataAsSourceForMailMerge(makePerson());
    var resultFile = makeFile("test/documentCreationOptions/default_field_form_not_editable.pdf");
    var result = documentTemplate.produceDocument(resultFile);
    assertThat(result).isNotNull();
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void with_documentCreationOptions_producing_fieldForm_editablePDF() {
    @SuppressWarnings("deprecation")
    var options = DocumentCreationOptions.getInstance().keepFormFieldsEditableInPdf(true);
    var documentTemplate = DocumentTemplate.withTemplate(template)
            .putDataAsSourceForMailMerge(makePerson()).withDocumentCreationOptions(options);
    var resultFile = makeFile("test/documentCreationOptions/field_form_editable.pdf");
    var result = documentTemplate.produceDocument(resultFile);
    assertThat(result).isNotNull();
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void with_documentCreationOptions_producing_fieldForm_not_editablePDF() {
    @SuppressWarnings("deprecation")
    var options = DocumentCreationOptions.getInstance()
            .keepFormFieldsEditableInPdf(false);
    var documentTemplate = DocumentTemplate.withTemplate(template).putDataAsSourceForMailMerge(makePerson()).withDocumentCreationOptions(options);
    var resultFile = makeFile("test/documentCreationOptions/field_form_not_editable.pdf");
    var result = documentTemplate.produceDocument(resultFile);
    assertThat(result).isNotNull();
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }
}
