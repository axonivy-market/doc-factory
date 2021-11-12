package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;
import ch.ivyteam.ivy.addons.docfactory.pdf.PdfAType;
import ch.ivyteam.ivy.addons.docfactory.pdf.PdfOptions;
import ch.ivyteam.ivy.addons.docfactory.pdfbox.PdfAValidator;

@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.management.*", "com.sun.org.apache.xerces.*", 
	"javax.xml.*", "org.xml.*", "org.w3c.dom.*"}) 
public class DocumentTemplatePDFaExportIT extends DocFactoryTest {

	private static File TEMPLATE;

	static {
		try {
			TEMPLATE = new File(DocumentTemplatePDFaExportIT.class.getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());
		} catch (URISyntaxException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	@Test
	public void produce_pdfA_1A() throws URISyntaxException, IOException {
		// we instantiate the DocumentCreationOptions with the PdfOptions in it
		PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_1A);
		DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

		// we delete the file from a previous test run if needed
		File resultFile = makeFile("test/documentTemplate/pdfA/test_pdfA_1A.pdf");

		// we use the DocumentTemplate Object for producing the PDF with the needed options
		FileOperationMessage result = DocumentTemplate.
				withTemplate(TEMPLATE).
				withDocumentCreationOptions(options).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				useLocale(Locale.forLanguageTag("de-CH")).
				produceDocument(resultFile);

		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
		assertThat(result.getFiles().get(0).getName(), Is.is("test_pdfA_1A.pdf"));
		// PDFBox can validate the PDF/A result
		// This tool cannot validate it: https: www.pdf-online.com/osa/validate.aspx
		// We should in general be careful with PDF/A validation as it does not seem to be consistent across different tools.
		assertTrue(PdfAValidator.isPDFACompliant(result.getFiles().get(0)));
	}

	@Test
	public void produce_pdfA_1B() throws URISyntaxException, IOException {
		// we instantiate the DocumentCreationOptions with the PdfOptions in it
		PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_1B);
		DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

		// we delete the file from a previous test run if needed
		File resultFile = makeFile("test/documentTemplate/pdfA/test_pdfA_1B.pdf");

		// we use the DocumentTemplate Object for producing the PDF with the needed options
		FileOperationMessage result = DocumentTemplate.
				withTemplate(TEMPLATE).
				withDocumentCreationOptions(options).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				useLocale(Locale.forLanguageTag("de-CH")).
				produceDocument(resultFile);

		assertTrue(result.isSuccess());
		//assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
		assertThat(result.getFiles().get(0).getName(), Is.is("test_pdfA_1B.pdf"));
		// PDFBox can validate the PdfAType.PDF_A_1B result
		// this tool also: https://www.pdf-online.com/osa/validate.aspx
		// We should in general be careful with PDF/A validation as it does not seem to be consistent across different tools.
		assertTrue(PdfAValidator.isPDFACompliant(result.getFiles().get(0)));
	}
	
	@Test
	public void produce_pdfA_2A() throws URISyntaxException, IOException {
		// we instantiate the DocumentCreationOptions with the PdfOptions in it
		PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_2A);
		DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

		// we delete the file from a previous test run if needed
		File resultFile = makeFile("test/documentTemplate/pdfA/test_pdfA_2A.pdf");

		// we use the DocumentTemplate Object for producing the PDF with the needed options
		FileOperationMessage result = DocumentTemplate.
				withTemplate(TEMPLATE).
				withDocumentCreationOptions(options).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				useLocale(Locale.forLanguageTag("de-CH")).
				produceDocument(resultFile);

		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
		assertThat(result.getFiles().get(0).getName(), Is.is("test_pdfA_2A.pdf"));
		// PDFBox cannot validate the PdfAType.PDF_A_2A result. 
		// it seems there is a bug with aspose with PdfAType.PDF_A_2A, PdfAType.PDF_A_3A see
		// https://forum.aspose.com/t/aspose-pdf-java-pdf-to-pdf-a-convertion-does-not-produce-valid-pdf-a-1a-pdf-a-2a-pdf-a-3a/212847
		// We should in general be careful with PDF/A validation as it does not seem to be consistent across different tools.
		assertFalse(PdfAValidator.isPDFACompliant(result.getFiles().get(0)));
	}

	@Test
	public void produce_pdfA_2B() throws URISyntaxException, IOException {
		// we instantiate the DocumentCreationOptions with the PdfOptions in it
		PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_2B);
		DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

		// we delete the file from a previous test run if needed
		File resultFile = makeFile("test/documentTemplate/pdfA/test_pdfA_2B.pdf");

		// we use the DocumentTemplate Object for producing the PDF with the needed options
		FileOperationMessage result = DocumentTemplate.
				withTemplate(TEMPLATE).
				withDocumentCreationOptions(options).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				useLocale(Locale.forLanguageTag("de-CH")).
				produceDocument(resultFile);

		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
		assertThat(result.getFiles().get(0).getName(), Is.is("test_pdfA_2B.pdf"));
		// PDFBox cannot validate the PdfAType.PDF_A_2B result
		// but this tool can validate it: https://www.pdf-online.com/osa/validate.aspx
		// We should in general be careful with PDF/A validation as it does not seem to be consistent across different tools.
		assertFalse(PdfAValidator.isPDFACompliant(result.getFiles().get(0)));
	}
	
	@Test
	public void produce_pdfA_3A() throws URISyntaxException, IOException {
		// we instantiate the DocumentCreationOptions with the PdfOptions in it
		PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_3A);
		DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

		// we delete the file from a previous test run if needed
		File resultFile = makeFile("test/documentTemplate/pdfA/test_pdfA_3A.pdf");

		// we use the DocumentTemplate Object for producing the PDF with the needed options
		FileOperationMessage result = DocumentTemplate.
				withTemplate(TEMPLATE).
				withDocumentCreationOptions(options).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				useLocale(Locale.forLanguageTag("de-CH")).
				produceDocument(resultFile);

		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
		assertThat(result.getFiles().get(0).getName(), Is.is("test_pdfA_3A.pdf"));
		// PDFBox cannot validate the PdfAType.PDF_A_3A result. 
		// it seems there is a bug with aspose with PdfAType.PDF_A_2A, PdfAType.PDF_A_3A see
		// https://forum.aspose.com/t/aspose-pdf-java-pdf-to-pdf-a-convertion-does-not-produce-valid-pdf-a-1a-pdf-a-2a-pdf-a-3a/212847
		// We should in general be careful with PDF/A validation as it does not seem to be consistent across different tools.
		assertFalse(PdfAValidator.isPDFACompliant(result.getFiles().get(0)));
	}

	@Test
	public void produce_pdfA_3B() throws URISyntaxException, IOException {
		// we instantiate the DocumentCreationOptions with the PdfOptions in it
		PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_3B);
		DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

		// we delete the file from a previous test run if needed
		File resultFile = makeFile("test/documentTemplate/pdfA/test_pdfA_3B.pdf");

		// we use the DocumentTemplate Object for producing the PDF with the needed options
		FileOperationMessage result = DocumentTemplate.
				withTemplate(TEMPLATE).
				withDocumentCreationOptions(options).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				useLocale(Locale.forLanguageTag("de-CH")).
				produceDocument(resultFile);

		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
		assertThat(result.getFiles().get(0).getName(), Is.is("test_pdfA_3B.pdf"));
		// PDFBox cannot validate the PdfAType.PDF_A_3B result
		// but this tool can validate it: https://www.pdf-online.com/osa/validate.aspx
		// We should in general be careful with PDF/A validation as it does not seem to be consistent across different tools.
		assertFalse(PdfAValidator.isPDFACompliant(result.getFiles().get(0)));
	}
	
	@Test
	public void produce_pdfA_2U() throws URISyntaxException, IOException {
		// we instantiate the DocumentCreationOptions with the PdfOptions in it
		PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_2U);
		DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

		// we delete the file from a previous test run if needed
		File resultFile = makeFile("test/documentTemplate/pdfA/test_pdfA_2U.pdf");

		// we use the DocumentTemplate Object for producing the PDF with the needed options
		FileOperationMessage result = DocumentTemplate.
				withTemplate(TEMPLATE).
				withDocumentCreationOptions(options).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				useLocale(Locale.forLanguageTag("de-CH")).
				produceDocument(resultFile);

		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
		assertThat(result.getFiles().get(0).getName(), Is.is("test_pdfA_2U.pdf"));
		// PDFBox cannot validate the PdfAType.PDF_A_2U result.
		// We should in general be careful with PDF/A validation as it does not seem to be consistent across different tools.
		assertFalse(PdfAValidator.isPDFACompliant(result.getFiles().get(0)));
	}

	@Test
	public void produce_pdfA_3U() throws URISyntaxException, IOException {
		// we instantiate the DocumentCreationOptions with the PdfOptions in it
		PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_3U);
		DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

		// we delete the file from a previous test run if needed
		File resultFile = makeFile("test/documentTemplate/pdfA/test_pdfA_3U.pdf");

		// we use the DocumentTemplate Object for producing the PDF with the needed options
		FileOperationMessage result = DocumentTemplate.
				withTemplate(TEMPLATE).
				withDocumentCreationOptions(options).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				useLocale(Locale.forLanguageTag("de-CH")).
				produceDocument(resultFile);

		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
		assertThat(result.getFiles().get(0).getName(), Is.is("test_pdfA_3U.pdf"));
		// PDFBox cannot validate the PdfAType.PDF_A_3U result.
		// We should in general be careful with PDF/A validation as it does not seem to be consistent across different tools.
		assertFalse(PdfAValidator.isPDFACompliant(result.getFiles().get(0)));
	}


}
