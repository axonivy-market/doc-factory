package ch.ivyteam.ivy.addons.docfactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;

import org.junit.Test;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker;

import com.aspose.pdf.CryptoAlgorithm;
import com.aspose.pdf.Permissions;
import com.aspose.words.Document;

public class DocumentWorkerInjectionForPostFileProduceTest extends DocFactoryTest {
	
	private static final String PROTECTED_FILE_PATH = "test/documentWorker/aPasswordProtectedFile.pdf";


	@Test
	public void noDocumentWorkerInjected() throws URISyntaxException  {
		java.io.File template = new java.io.File(this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());
		
		DocumentTemplate documentTemplate = DocumentTemplate
				.withTemplate(template)
				.putDataAsSourceForSimpleMailMerge(makePerson())
				.useLocale(Locale.forLanguageTag("de-CH"));

		File resultFile = makeFile("test/documentWorker/aFile.pdf");
		
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);
		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), hasItem(resultFile));
		
		com.aspose.pdf.Document pdfDoc = new com.aspose.pdf.Document(resultFile.getAbsolutePath());
		assertThat(pdfDoc.isEncrypted(), is(false));
	}
	
	@Test
	public void injectDocumentWorkerImplementing_PostFileProduce_example_with_pdfEncrypter() throws URISyntaxException  {
		java.io.File template = new java.io.File(this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());
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
		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), hasItem(resultFile));
		
		com.aspose.pdf.Document pdfDoc = new com.aspose.pdf.Document(resultFile.getAbsolutePath(), secret);
		assertThat(pdfDoc.isEncrypted(), is(true));
		
	}
	
	
	private void deletePreviousProtectedFile(String passwordProtectedFilePath, String password) {
		File file = new File(passwordProtectedFilePath);
		try {
			com.aspose.pdf.Document pdfDoc = new com.aspose.pdf.Document(file.getAbsolutePath(), password);
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
			if(producedFile.getName().endsWith("pdf")) {
				com.aspose.pdf.Document pdfDoc = new com.aspose.pdf.Document(producedFile.getAbsolutePath());
				pdfDoc.encrypt(userSecret, "ownerSecret", Permissions.PrintDocument, CryptoAlgorithm.AESx256);
				pdfDoc.save(producedFile.getAbsolutePath());
			}
			return producedFile;
		}
		
	}

}
