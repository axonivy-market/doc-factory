package ch.ivyteam.ivy.addons.docfactory;

import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.makeFile;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.makePerson;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;
import ch.ivyteam.ivy.addons.docfactory.test.data.Person;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class BooleanToCheckBoxOptionIT {

  File template;
  DocumentTemplate documentTemplate;

  @BeforeEach
  public void setup() throws Exception {
    File tmpl = new File(this.getClass().getResource(DocFactoryTest.TEMPLATE_PERSON_DOCX).toURI().getPath());
    documentTemplate = DocumentTemplate.withTemplate(tmpl).useLocale(Locale.forLanguageTag("de-CH"));
  }

  @Test
  public void default_False_mergeFieldValue_notDisplayedAsCheckBox() {
    File resultFile = makeFile("test/documentTemplate/checkBox/default_false_not_displayed_as_checkbox.pdf");

    Person data = makePerson();
    data.acceptToBeContacted(false);

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(data)
            .produceDocument(resultFile);
    
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void default_True_mergeFieldValue_notDisplayedAsCheckBox() {
    File resultFile = makeFile("test/documentTemplate/checkBox/default_true_not_displayed_as_checkbox.pdf");

    Person data = makePerson();
    data.acceptToBeContacted(true);

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(data)
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void false_mergeFieldValue_displayed_as_unselectedCheckBox_output_pdf() {
    File resultFile = makeFile("test/documentTemplate/checkBox/false_displayed_as_unselected_checkbox.pdf");

    Person data = makePerson();
    data.acceptToBeContacted(false);

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(data)
            .withDocumentCreationOptions(
                    DocumentCreationOptions.getInstance().displayBooleanValuesAsCheckBox(true))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void true_mergeFieldValue_displayed_as_selectedCheckBox_output_pdf() {
    File resultFile = makeFile("test/documentTemplate/checkBox/true_displayed_as_selected_checkbox.pdf");

    Person data = makePerson();
    data.acceptToBeContacted(true);

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(data)
            .withDocumentCreationOptions(
                    DocumentCreationOptions.getInstance().displayBooleanValuesAsCheckBox(true))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void false_mergeFieldValue_displayed_as_unselectedCheckBox_output_doc() {
    File resultFile = makeFile("test/documentTemplate/checkBox/false_displayed_as_unselected_checkbox.doc");

    Person data = makePerson();
    data.acceptToBeContacted(false);

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(data)
            .withDocumentCreationOptions(
                    DocumentCreationOptions.getInstance().displayBooleanValuesAsCheckBox(true))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void true_mergeFieldValue_displayed_as_selectedCheckBox_output_doc() {
    File resultFile = makeFile("test/documentTemplate/checkBox/true_displayed_as_selected_checkbox.doc");

    Person data = makePerson();
    data.acceptToBeContacted(true);

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(data)
            .withDocumentCreationOptions(
                    DocumentCreationOptions.getInstance().displayBooleanValuesAsCheckBox(true))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void false_mergeFieldValue_displayed_as_unselectedCheckBox_output_docx() {
    File resultFile = makeFile("test/documentTemplate/checkBox/false_displayed_as_unselected_checkbox.docx");

    Person data = makePerson();
    data.acceptToBeContacted(false);

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(data)
            .withDocumentCreationOptions(
                    DocumentCreationOptions.getInstance().displayBooleanValuesAsCheckBox(true))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void true_mergeFieldValue_displayed_as_selectedCheckBox_output_docx() {
    File resultFile = makeFile("test/documentTemplate/checkBox/true_displayed_as_selected_checkbox.docx");

    Person data = makePerson();
    data.acceptToBeContacted(true);

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(data)
            .withDocumentCreationOptions(
                    DocumentCreationOptions.getInstance().displayBooleanValuesAsCheckBox(true))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void false_mergeFieldValue_displayed_as_unselectedCheckBox_output_odt() {
    File resultFile = makeFile("test/documentTemplate/checkBox/false_displayed_as_unselected_checkbox.odt");

    Person data = makePerson();
    data.acceptToBeContacted(false);

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(data)
            .withDocumentCreationOptions(
                    DocumentCreationOptions.getInstance().displayBooleanValuesAsCheckBox(true))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void true_mergeFieldValue_displayed_as_selectedCheckBox_output_odt() {
    File resultFile = makeFile("test/documentTemplate/checkBox/true_displayed_as_selected_checkbox.odt");

    Person data = makePerson();
    data.acceptToBeContacted(true);

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(data)
            .withDocumentCreationOptions(
                    DocumentCreationOptions.getInstance().displayBooleanValuesAsCheckBox(true))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void false_mergeFieldValue_not_displayed_as_checkbox_in_txt() {
    File resultFile = makeFile(
            "test/documentTemplate/checkBox/false_not_displayed_as_unselected_checkbox.txt");

    Person data = makePerson();
    data.acceptToBeContacted(false);

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(data)
            .withDocumentCreationOptions(
                    DocumentCreationOptions.getInstance().displayBooleanValuesAsCheckBox(true))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void true_mergeFieldValue_not_displayed_as_checkbox_in_txt() {
    File resultFile = makeFile("test/documentTemplate/checkBox/true_not_displayed_as_selected_checkbox.txt");

    Person data = makePerson();
    data.acceptToBeContacted(true);

    FileOperationMessage result = documentTemplate.putDataAsSourceForMailMerge(data)
            .withDocumentCreationOptions(
                    DocumentCreationOptions.getInstance().displayBooleanValuesAsCheckBox(true))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void false_mergeFieldValue_displayed_as_unselectedCheckBox_output_html() {
    File resultFile = makeFile("test/documentTemplate/checkBox/false_displayed_as_unselected_checkbox.html");

    Person data = makePerson();
    data.acceptToBeContacted(false);
    var result = documentTemplate.putDataAsSourceForMailMerge(data)
            .withDocumentCreationOptions(DocumentCreationOptions.getInstance().displayBooleanValuesAsCheckBox(true))
            .produceDocument(resultFile);
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void true_mergeFieldValue_displayed_as_selectedCheckBox_output_html() {
    File resultFile = makeFile("test/documentTemplate/checkBox/true_displayed_as_selected_checkbox.html");
    Person data = makePerson();
    data.acceptToBeContacted(true);
    var result = documentTemplate.putDataAsSourceForMailMerge(data)
            .withDocumentCreationOptions(DocumentCreationOptions.getInstance().displayBooleanValuesAsCheckBox(true))
            .produceDocument(resultFile);
    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
  }
}
