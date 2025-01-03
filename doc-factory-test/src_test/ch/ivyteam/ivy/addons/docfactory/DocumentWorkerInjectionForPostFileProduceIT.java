package ch.ivyteam.ivy.addons.docfactory;

import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.TEMPLATE_PERSON_DOCX;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.makeFile;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryTest.makePerson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.aspose.pdf.CryptoAlgorithm;
import com.aspose.pdf.Permissions;
import com.aspose.words.Document;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker;
import ch.ivyteam.ivy.addons.docfactory.exception.DocumentGenerationException;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class DocumentWorkerInjectionForPostFileProduceIT {

  private static final String PROTECTED_FILE_PATH = "test/documentWorker/aPasswordProtectedFile.pdf";

  @Test
  public void noDocumentWorkerInjected() throws URISyntaxException {
    java.io.File template = new java.io.File(
            this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());

    DocumentTemplate documentTemplate = DocumentTemplate
            .withTemplate(template)
            .putDataAsSourceForSimpleMailMerge(makePerson())
            .useLocale(Locale.forLanguageTag("de-CH"));

    File resultFile = makeFile("test/documentWorker/aFile.pdf");

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);
    
    assertThat(result).isNotNull();
   	assertThat(result.isSuccess()).isTrue();
   	assertThat(result.getFiles()).contains(resultFile);

    try (com.aspose.pdf.Document pdfDoc = new com.aspose.pdf.Document(resultFile.getAbsolutePath())) {
		assertThat(pdfDoc.isEncrypted()).isFalse();
	}
  }

  @Test
  public void injectDocumentWorkerImplementing_onGeneratedFile_example_with_pdfEncrypter()
          throws URISyntaxException {
    java.io.File template = new java.io.File(
            this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());
    String secret = "mySecret";

    DocumentWorker pdfFileEncrypter = PdfFileEncrypter.withUserSecret(secret);

    DocumentTemplate documentTemplate = DocumentTemplate
            .withTemplate(template)
            .putDataAsSourceForSimpleMailMerge(makePerson())
            .useLocale(Locale.forLanguageTag("de-CH"))
            .withDocumentWorker(pdfFileEncrypter);

    deletePreviousProtectedFile(PROTECTED_FILE_PATH, secret);

    File resultFile = makeFile(PROTECTED_FILE_PATH);

    FileOperationMessage result = documentTemplate.produceDocument(resultFile);
    assertThat(result).isNotNull();
   	assertThat(result.isSuccess()).isTrue();
   	assertThat(result.getFiles()).contains(resultFile);

    try (com.aspose.pdf.Document pdfDoc = new com.aspose.pdf.Document(resultFile.getAbsolutePath(), secret)) {
		assertThat(pdfDoc.isEncrypted()).isTrue();
	}
  }

  @Test
  public void injectDocumentWorkerImplementing_onGeneratedFile_which_returns_null_DocumentGenerationException_thrown()
          throws URISyntaxException {
    java.io.File template = new java.io.File(
            this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());

    DocumentWorker DocumentWorkerReturningNull = new DocumentWorkerReturningNull();

    DocumentTemplate documentTemplate = DocumentTemplate
            .withTemplate(template)
            .putDataAsSourceForSimpleMailMerge(makePerson())
            .useLocale(Locale.forLanguageTag("de-CH"))
            .withDocumentWorker(DocumentWorkerReturningNull);

    File resultFile = new File("test/documentWorker/anotherFile.pdf");

    assertThatThrownBy(() -> documentTemplate.produceDocument(resultFile)).isInstanceOf(DocumentGenerationException.class);
  }

  @Test
  public void injectDocumentWorkerImplementing_onGeneratedFile_which_returns_notExistingFile_DocumentGenerationException_thrown()
          throws URISyntaxException {
    java.io.File template = new java.io.File(
            this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());

    DocumentWorker documentWorkerReturningNotExistingFile = new DocumentWorkerReturningNotExistingFile();

    DocumentTemplate documentTemplate = DocumentTemplate
            .withTemplate(template)
            .putDataAsSourceForSimpleMailMerge(makePerson())
            .useLocale(Locale.forLanguageTag("de-CH"))
            .withDocumentWorker(documentWorkerReturningNotExistingFile);

    File resultFile = new File("test/documentWorker/anotherFile.pdf");

    assertThatThrownBy(() -> documentTemplate.produceDocument(resultFile)).isInstanceOf(DocumentGenerationException.class);
  }

  private void deletePreviousProtectedFile(String passwordProtectedFilePath, String password) {
    File file = new File(passwordProtectedFilePath);
    try (com.aspose.pdf.Document pdfDoc = new com.aspose.pdf.Document(file.getAbsolutePath(), password)) {
      pdfDoc.decrypt();
      file.delete();
    } catch (Exception ex) {
      // Ignore the exception here
    }
  }

  public static class PdfFileEncrypter implements DocumentWorker {

    private String userSecret;

    private PdfFileEncrypter() {}

    public static PdfFileEncrypter withUserSecret(String userSecret) {
      API.checkNotBlank(userSecret, "the user secret");
      PdfFileEncrypter pdfEncrypter = new PdfFileEncrypter();
      pdfEncrypter.userSecret = userSecret;
      return pdfEncrypter;
    }

    @Override
    public File onGeneratedFile(Document document, File producedFile) {
      if (producedFile.getName().endsWith("pdf")) {
        try (com.aspose.pdf.Document pdfDoc = new com.aspose.pdf.Document(producedFile.getAbsolutePath())) {
			pdfDoc.encrypt(userSecret, "ownerSecret", Permissions.PrintDocument, CryptoAlgorithm.AESx256);
			pdfDoc.save(producedFile.getAbsolutePath());
		}
      }
      return producedFile;
    }

  }

  public static class DocumentWorkerReturningNull implements DocumentWorker {

    public DocumentWorkerReturningNull() {}

    @Override
    public File onGeneratedFile(Document document, File producedFile) {
      return null;
    }

  }

  public static class DocumentWorkerReturningNotExistingFile implements DocumentWorker {

    public DocumentWorkerReturningNotExistingFile() {}

    @Override
    public File onGeneratedFile(Document document, File producedFile) {
      return new File("I do not exist.doc");
    }

  }

}
