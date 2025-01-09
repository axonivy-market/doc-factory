package ch.ivyteam.ivy.addons.docfactory;

import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.TEMPLATE_FOR_TESTING_NULL_VALUES_DOCX;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.TEMPLATE_PERSON_DOCX;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.TEMPLATE_WITH_FIELDS_FORM_DOCX;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.makePerson;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.options.DocumentAppendingStart;
import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;
import ch.ivyteam.ivy.addons.docfactory.options.FileAppenderOptions;
import ch.ivyteam.ivy.addons.docfactory.options.MultipleDocumentsCreationOptions;
import ch.ivyteam.ivy.addons.docfactory.pdf.PdfOptions;
import ch.ivyteam.ivy.addons.docfactory.test.data.Person;

public class BaseDocFactoryGenerateDocumentsIT {

  private static final String TEST_DIRECTORY_RELATIVE_PATH = "test/generateDocuments";
  private static File TEMPLATE_1, TEMPLATE_2, TEMPLATE_3;

  static {
    try {
      TEMPLATE_1 = new File(
              BaseDocFactoryGenerateDocumentsIT.class.getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());
      TEMPLATE_2 = new File(BaseDocFactoryGenerateDocumentsIT.class
              .getResource(TEMPLATE_FOR_TESTING_NULL_VALUES_DOCX).toURI().getPath());
      TEMPLATE_3 = new File(BaseDocFactoryGenerateDocumentsIT.class
              .getResource(TEMPLATE_WITH_FIELDS_FORM_DOCX).toURI().getPath());
    } catch (URISyntaxException e) {
      System.err.println("BaseDocFactoryGenerateDocumentsIT Test error " + e.getMessage());
    }
  }

  private BaseDocFactory docFactory;

  private DocumentTemplate documentTemplate1, documentTemplate2, documentTemplate3;

  @BeforeEach
  public void setUp() throws Exception {
    docFactory = BaseDocFactory.getInstance().withDocumentCreationOptions(DocumentCreationOptions.getInstance()
        .withPdfOptions(PdfOptions.getInstance().hasToKeepFormFieldsEditable(true)));

    Person person = makePerson();
    documentTemplate1 = DocumentTemplate.withTemplate(TEMPLATE_1).putDataAsSourceForSimpleMailMerge(person)
            .useLocale(Locale.GERMAN);
    documentTemplate2 = DocumentTemplate.withTemplate(TEMPLATE_2).putDataAsSourceForSimpleMailMerge(person)
            .useLocale(Locale.GERMAN);
    documentTemplate3 = DocumentTemplate.withTemplate(TEMPLATE_3).putDataAsSourceForSimpleMailMerge(person)
            .useLocale(Locale.GERMAN);

    documentTemplate1.setOutputPath(TEST_DIRECTORY_RELATIVE_PATH);
    documentTemplate2.setOutputPath(TEST_DIRECTORY_RELATIVE_PATH);
    documentTemplate3.setOutputPath(TEST_DIRECTORY_RELATIVE_PATH);

    documentTemplate1.setOutputFormat(DocFactoryConstants.DOC_EXTENSION);
    documentTemplate2.setOutputFormat(DocFactoryConstants.DOCX_EXTENSION);
    documentTemplate3.setOutputFormat(DocFactoryConstants.PDF_EXTENSION);
  }

  @Test
  public void generateDocuments_eachHavingADifferentTemplate_singleDocs_and_appendedDoc_created()
          throws Exception {
    documentTemplate1.setOutputName("file1");
    documentTemplate2.setOutputName("file2");
    documentTemplate3.setOutputName("file3");

    List<DocumentTemplate> documentTemplates = new ArrayList<>();
    documentTemplates.add(documentTemplate1);
    documentTemplates.add(documentTemplate2);
    documentTemplates.add(documentTemplate3);

    FileOperationMessage result = docFactory.generateDocuments(documentTemplates,
            MultipleDocumentsCreationOptions.getInstance()
                    .createOneFileByAppendingAllTheDocuments(true)
                    .createSingleFileForEachDocument(true)
                    .withFileAppenderOptions(
                            FileAppenderOptions.getInstance()
                                    .withAppendedFileParentDirectoryPath(TEST_DIRECTORY_RELATIVE_PATH)
                                    .withAppendedFileName("bigFile")));
    List<String> resultFilePaths = new ArrayList<>();
    for (File file : result.getFiles()) {
      resultFilePaths.add(file.getAbsolutePath());
    }

    File resultFile1 = new File(TEST_DIRECTORY_RELATIVE_PATH + "/file1.doc");
    File resultFile2 = new File(TEST_DIRECTORY_RELATIVE_PATH + "/file2.docx");
    File resultFile3 = new File(TEST_DIRECTORY_RELATIVE_PATH + "/file3.pdf");
    File resultFile4 = new File(TEST_DIRECTORY_RELATIVE_PATH + "/bigFile.pdf");

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).hasSize(4);
    assertThat(resultFilePaths).contains(resultFile1.getAbsolutePath(), resultFile2.getAbsolutePath(),
                    resultFile3.getAbsolutePath(), resultFile4.getAbsolutePath());
    assertThat(resultFile1.isFile() && resultFile2.isFile() && resultFile3.isFile() && resultFile4.isFile()).isTrue();
  }

  @Test
  public void generateDocuments_eachHavingADifferentTemplate_appendedDoc_contains_asmanypages_as_the_documents_number()
          throws Exception {
    documentTemplate1.setOutputName("file1");
    documentTemplate2.setOutputName("file2");
    documentTemplate3.setOutputName("file3");

    List<DocumentTemplate> documentTemplates = new ArrayList<>();
    documentTemplates.add(documentTemplate1);
    documentTemplates.add(documentTemplate2);
    documentTemplates.add(documentTemplate3);

    FileOperationMessage result = docFactory.generateDocuments(documentTemplates,
            MultipleDocumentsCreationOptions.getInstance()
                    .createOneFileByAppendingAllTheDocuments(true)
                    .createSingleFileForEachDocument(true)
                    .withFileAppenderOptions(
                            FileAppenderOptions.getInstance()
                                    .withAppendedFileParentDirectoryPath(TEST_DIRECTORY_RELATIVE_PATH)
                                    .withAppendedFileName("bigFile")));

    File appendedFile = result.getFiles().get(result.getFiles().size() - 1);
    try (com.aspose.pdf.Document pdfDocument = new com.aspose.pdf.Document(appendedFile.getAbsolutePath())) {
		assertThat(pdfDocument.getPages()).hasSize(documentTemplates.size());
	}
  }

  @Test
  public void generateDocuments_allHavingSameTemplate_eachSingleDoc_and_appendedDoc_created()
          throws Exception {
    List<DocumentTemplate> documentTemplates = makeDocumentTemplatesWithSameTemplateFile();

    FileOperationMessage result = docFactory.generateDocuments(documentTemplates,
            MultipleDocumentsCreationOptions.getInstance()
                    .createOneFileByAppendingAllTheDocuments(true)
                    .createSingleFileForEachDocument(true)
                    .withFileAppenderOptions(
                            FileAppenderOptions.getInstance()
                                    .withAppendedFileParentDirectoryPath(TEST_DIRECTORY_RELATIVE_PATH)
                                    .withAppendedFileName("bigFileSameTemplate")));
    List<String> resultFilePaths = new ArrayList<>();
    for (File file : result.getFiles()) {
      resultFilePaths.add(file.getAbsolutePath());
    }

    File resultFile1 = new File(TEST_DIRECTORY_RELATIVE_PATH + "/fileSameTemplate1.pdf");
    File resultFile2 = new File(TEST_DIRECTORY_RELATIVE_PATH + "/fileSameTemplate2.pdf");
    File resultFile3 = new File(TEST_DIRECTORY_RELATIVE_PATH + "/fileSameTemplate3.pdf");
    File resultFile4 = new File(TEST_DIRECTORY_RELATIVE_PATH + "/bigFileSameTemplate.pdf");

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).hasSize(4);
    assertThat(resultFilePaths).contains(resultFile1.getAbsolutePath(), resultFile2.getAbsolutePath(),
                    resultFile3.getAbsolutePath(), resultFile4.getAbsolutePath());
    assertThat(resultFile1.isFile() && resultFile2.isFile() && resultFile3.isFile() && resultFile4.isFile()).isTrue();
  }

  @Test
  public void generateDocuments_allHavingSameTemplate_appendedDoc_contains_asmanypages_as_the_documents_number()
          throws Exception {
    List<DocumentTemplate> documentTemplates = makeDocumentTemplatesWithSameTemplateFile();

    FileOperationMessage result = docFactory.generateDocuments(documentTemplates,
            MultipleDocumentsCreationOptions.getInstance()
                    .createOneFileByAppendingAllTheDocuments(true)
                    .createSingleFileForEachDocument(true)
                    .withFileAppenderOptions(
                            FileAppenderOptions.getInstance()
                                    .withAppendedFileParentDirectoryPath(TEST_DIRECTORY_RELATIVE_PATH)
                                    .withAppendedFileName("bigFileSameTemplate")));

    File appendedFile = result.getFiles().get(result.getFiles().size() - 1);
    try (com.aspose.pdf.Document pdfDocument = new com.aspose.pdf.Document(appendedFile.getAbsolutePath())) {
		assertThat(pdfDocument.getPages()).hasSize(documentTemplates.size());
	}
  }

  @Test
  public void generateDocuments_onlySingleDoc_created() throws Exception {
    documentTemplate1.setOutputName("file4");
    documentTemplate2.setOutputName("file5");
    documentTemplate3.setOutputName("file6");

    List<DocumentTemplate> documentTemplates = new ArrayList<>();
    documentTemplates.add(documentTemplate1);
    documentTemplates.add(documentTemplate2);
    documentTemplates.add(documentTemplate3);

    FileOperationMessage result = docFactory.generateDocuments(documentTemplates,
            MultipleDocumentsCreationOptions.getInstance()
                    .createOneFileByAppendingAllTheDocuments(false)
                    .createSingleFileForEachDocument(true));
    List<String> resultFilePaths = new ArrayList<>();
    for (File file : result.getFiles()) {
      resultFilePaths.add(file.getAbsolutePath());
    }

    File resultFile1 = new File(TEST_DIRECTORY_RELATIVE_PATH + "/file4.doc");
    File resultFile2 = new File(TEST_DIRECTORY_RELATIVE_PATH + "/file5.docx");
    File resultFile3 = new File(TEST_DIRECTORY_RELATIVE_PATH + "/file6.pdf");

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).hasSize(3);
    assertThat(resultFilePaths).
            contains(resultFile1.getAbsolutePath(), resultFile2.getAbsolutePath(),
                    resultFile3.getAbsolutePath());
    assertThat(resultFile1.isFile() && resultFile2.isFile() && resultFile3.isFile()).isTrue();
  }
  
  @Test
  public void generateDocuments_onlyAppendedDoc_created() throws Exception {
    List<DocumentTemplate> documentTemplates = new ArrayList<>();
    documentTemplates.add(documentTemplate1);
    documentTemplates.add(documentTemplate2);
    documentTemplates.add(documentTemplate3);

    FileOperationMessage result = docFactory.generateDocuments(documentTemplates,
            MultipleDocumentsCreationOptions.getInstance()
                    .createOneFileByAppendingAllTheDocuments(true)
                    .createSingleFileForEachDocument(false)
                    .withFileAppenderOptions(
                            FileAppenderOptions.getInstance()
                                    .withAppendedFileParentDirectoryPath(TEST_DIRECTORY_RELATIVE_PATH)
                                    .withAppendedFileName("singleAppendedFile")
                                    .withAppendedFileOutputFormat(DocFactoryConstants.DOCX_FORMAT)));
    List<String> resultFilePaths = new ArrayList<>();
    for (File file : result.getFiles()) {
      resultFilePaths.add(file.getAbsolutePath());
    }

    File resultFile = new File(TEST_DIRECTORY_RELATIVE_PATH + "/singleAppendedFile.docx");

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).hasSize(1);
    assertThat(resultFilePaths).contains(resultFile.getAbsolutePath());
    assertThat(resultFile.isFile()).isTrue();

  }

  @Test
  public void generateDocuments_onlyAppendedDoc_created_with_option_continuous() throws Exception {
    List<DocumentTemplate> documentTemplates = new ArrayList<>();
    documentTemplates.add(documentTemplate1);
    documentTemplates.add(documentTemplate2);
    documentTemplates.add(documentTemplate3);

    FileOperationMessage result = docFactory.generateDocuments(documentTemplates,
            MultipleDocumentsCreationOptions.getInstance()
                    .createOneFileByAppendingAllTheDocuments(true)
                    .createSingleFileForEachDocument(false)
                    .withFileAppenderOptions(
                            FileAppenderOptions.getInstance()
                                    .withAppendedFileParentDirectoryPath(TEST_DIRECTORY_RELATIVE_PATH)
                                    .withAppendedFileName("singleAppendedFileContinuous")
                                    .withAppendedFileOutputFormat(DocFactoryConstants.DOCX_FORMAT)
                                    .withDocumentAppendingStart(DocumentAppendingStart.CONTINUOUS)));
    List<String> resultFilePaths = new ArrayList<>();
    for (File file : result.getFiles()) {
      resultFilePaths.add(file.getAbsolutePath());
    }

    File resultFile = new File(TEST_DIRECTORY_RELATIVE_PATH + "/singleAppendedFileContinuous.docx");

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).hasSize(1);
    assertThat(resultFilePaths).contains(resultFile.getAbsolutePath());
    assertThat(resultFile.isFile()).isTrue();
  }

  @Test
  public void generateDocuments_without_MultipleDocumentsCreationOptions() throws Exception {
    documentTemplate1.setOutputName("file7");
    documentTemplate2.setOutputName("file8");
    documentTemplate3.setOutputName("file9");

    List<DocumentTemplate> documentTemplates = new ArrayList<>();
    documentTemplates.add(documentTemplate1);
    documentTemplates.add(documentTemplate2);
    documentTemplates.add(documentTemplate3);

    FileOperationMessage result = docFactory.generateDocuments(documentTemplates);
    List<String> resultFilePaths = new ArrayList<>();
    for (File file : result.getFiles()) {
      resultFilePaths.add(file.getAbsolutePath());
    }

    File resultFile1 = new File(TEST_DIRECTORY_RELATIVE_PATH + "/file7.doc");
    File resultFile2 = new File(TEST_DIRECTORY_RELATIVE_PATH + "/file8.docx");
    File resultFile3 = new File(TEST_DIRECTORY_RELATIVE_PATH + "/file9.pdf");

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).hasSize(3);
    assertThat(resultFilePaths).contains(resultFile1.getAbsolutePath(), resultFile2.getAbsolutePath(),
                    resultFile3.getAbsolutePath());
    assertThat(resultFile1.isFile() && resultFile2.isFile() && resultFile3.isFile()).isTrue();

  }

  private List<DocumentTemplate> makeDocumentTemplatesWithSameTemplateFile() {
    Person person = makePerson();
    documentTemplate1 = DocumentTemplate.withTemplate(TEMPLATE_1).putDataAsSourceForSimpleMailMerge(person)
            .useLocale(Locale.GERMAN);
    person = makePerson();
    person.setName("Abcde");
    person.setFirstname("Fghij");
    documentTemplate2 = DocumentTemplate.withTemplate(TEMPLATE_1).putDataAsSourceForSimpleMailMerge(person)
            .useLocale(Locale.GERMAN);
    person = makePerson();
    person.setName("Klmnop");
    person.setFirstname("Qurstw");
    documentTemplate3 = DocumentTemplate.withTemplate(TEMPLATE_1).putDataAsSourceForSimpleMailMerge(person)
            .useLocale(Locale.GERMAN);

    documentTemplate1.setOutputPath(TEST_DIRECTORY_RELATIVE_PATH);
    documentTemplate2.setOutputPath(TEST_DIRECTORY_RELATIVE_PATH);
    documentTemplate3.setOutputPath(TEST_DIRECTORY_RELATIVE_PATH);

    documentTemplate1.setOutputName("fileSameTemplate1");
    documentTemplate2.setOutputName("fileSameTemplate2");
    documentTemplate3.setOutputName("fileSameTemplate3");

    List<DocumentTemplate> documentTemplates = new ArrayList<>();
    documentTemplates.add(documentTemplate1);
    documentTemplates.add(documentTemplate2);
    documentTemplates.add(documentTemplate3);
    return documentTemplates;
  }

}
