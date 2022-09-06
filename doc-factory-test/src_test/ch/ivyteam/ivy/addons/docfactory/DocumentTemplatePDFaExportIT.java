package ch.ivyteam.ivy.addons.docfactory;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;
import ch.ivyteam.ivy.addons.docfactory.pdf.PdfAType;
import ch.ivyteam.ivy.addons.docfactory.pdf.PdfOptions;
import ch.ivyteam.ivy.addons.docfactory.pdfbox.PdfAValidator;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class DocumentTemplatePDFaExportIT {

  private static File TEMPLATE;

  static {
    try {
      TEMPLATE = new File(
              DocumentTemplatePDFaExportIT.class.getResource(DocFactoryTest.TEMPLATE_PERSON_DOCX).toURI().getPath());
    } catch (URISyntaxException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  @Test
  public void produce_pdfA_1A() throws IOException {
    // we instantiate the DocumentCreationOptions with the PdfOptions in it
    PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_1A);
    DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

    // we delete the file from a previous test run if needed
    File resultFile = DocFactoryTest.makeFile("test/documentTemplate/pdfA/test_pdfA_1A.pdf");

    // we use the DocumentTemplate Object for producing the PDF with the needed
    // options
    FileOperationMessage result = DocumentTemplate.withTemplate(TEMPLATE).withDocumentCreationOptions(options)
            .putDataAsSourceForSimpleMailMerge(DocFactoryTest.makePerson()).useLocale(Locale.forLanguageTag("de-CH"))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
    assertThat(result.getFiles().get(0).getName()).isEqualTo("test_pdfA_1A.pdf");
    // PDFBox can validate the PDF/A result
    // This tool cannot validate it: https: www.pdf-online.com/osa/validate.aspx
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(result.getFiles().get(0))).isTrue();
  }

  @Test
  public void produce_pdfA_1B() throws IOException {
    // we instantiate the DocumentCreationOptions with the PdfOptions in it
    PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_1B);
    DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

    // we delete the file from a previous test run if needed
    File resultFile = DocFactoryTest.makeFile("test/documentTemplate/pdfA/test_pdfA_1B.pdf");

    // we use the DocumentTemplate Object for producing the PDF with the needed
    // options
    FileOperationMessage result = DocumentTemplate.withTemplate(TEMPLATE).withDocumentCreationOptions(options)
            .putDataAsSourceForSimpleMailMerge(DocFactoryTest.makePerson()).useLocale(Locale.forLanguageTag("de-CH"))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    // assertThat(result.getFiles(),
    // org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
    assertThat(result.getFiles().get(0).getName()).isEqualTo("test_pdfA_1B.pdf");
    // PDFBox can validate the PdfAType.PDF_A_1B result
    // this tool also: https://www.pdf-online.com/osa/validate.aspx
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(result.getFiles().get(0))).isTrue();
  }

  @Test
  public void produce_pdfA_2A() throws IOException {
    // we instantiate the DocumentCreationOptions with the PdfOptions in it
    PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_2A);
    DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

    // we delete the file from a previous test run if needed
    File resultFile = DocFactoryTest.makeFile("test/documentTemplate/pdfA/test_pdfA_2A.pdf");

    // we use the DocumentTemplate Object for producing the PDF with the needed
    // options
    FileOperationMessage result = DocumentTemplate.withTemplate(TEMPLATE).withDocumentCreationOptions(options)
            .putDataAsSourceForSimpleMailMerge(DocFactoryTest.makePerson()).useLocale(Locale.forLanguageTag("de-CH"))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
    assertThat(result.getFiles().get(0).getName()).isEqualTo("test_pdfA_2A.pdf");
    // PDFBox cannot validate the PdfAType.PDF_A_2A result.
    // it seems there is a bug with aspose with PdfAType.PDF_A_2A,
    // PdfAType.PDF_A_3A see
    // https://forum.aspose.com/t/aspose-pdf-java-pdf-to-pdf-a-convertion-does-not-produce-valid-pdf-a-1a-pdf-a-2a-pdf-a-3a/212847
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(result.getFiles().get(0))).isFalse();
  }

  @Test
  public void produce_pdfA_2B() throws IOException {
    // we instantiate the DocumentCreationOptions with the PdfOptions in it
    PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_2B);
    DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

    // we delete the file from a previous test run if needed
    File resultFile = DocFactoryTest.makeFile("test/documentTemplate/pdfA/test_pdfA_2B.pdf");

    // we use the DocumentTemplate Object for producing the PDF with the needed
    // options
    FileOperationMessage result = DocumentTemplate.withTemplate(TEMPLATE).withDocumentCreationOptions(options)
            .putDataAsSourceForSimpleMailMerge(DocFactoryTest.makePerson()).useLocale(Locale.forLanguageTag("de-CH"))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
    assertThat(result.getFiles().get(0).getName()).isEqualTo("test_pdfA_2B.pdf");
    // PDFBox cannot validate the PdfAType.PDF_A_2B result
    // but this tool can validate it:
    // https://www.pdf-online.com/osa/validate.aspx
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(result.getFiles().get(0))).isFalse();
  }

  @Test
  public void produce_pdfA_3A() throws IOException {
    // we instantiate the DocumentCreationOptions with the PdfOptions in it
    PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_3A);
    DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

    // we delete the file from a previous test run if needed
    File resultFile = DocFactoryTest.makeFile("test/documentTemplate/pdfA/test_pdfA_3A.pdf");

    // we use the DocumentTemplate Object for producing the PDF with the needed
    // options
    FileOperationMessage result = DocumentTemplate.withTemplate(TEMPLATE).withDocumentCreationOptions(options)
            .putDataAsSourceForSimpleMailMerge(DocFactoryTest.makePerson()).useLocale(Locale.forLanguageTag("de-CH"))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
    assertThat(result.getFiles().get(0).getName()).isEqualTo("test_pdfA_3A.pdf");
    // PDFBox cannot validate the PdfAType.PDF_A_3A result.
    // it seems there is a bug with aspose with PdfAType.PDF_A_2A,
    // PdfAType.PDF_A_3A see
    // https://forum.aspose.com/t/aspose-pdf-java-pdf-to-pdf-a-convertion-does-not-produce-valid-pdf-a-1a-pdf-a-2a-pdf-a-3a/212847
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(result.getFiles().get(0))).isFalse();
  }

  @Test
  public void produce_pdfA_3B() throws IOException {
    // we instantiate the DocumentCreationOptions with the PdfOptions in it
    PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_3B);
    DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

    // we delete the file from a previous test run if needed
    File resultFile = DocFactoryTest.makeFile("test/documentTemplate/pdfA/test_pdfA_3B.pdf");

    // we use the DocumentTemplate Object for producing the PDF with the needed
    // options
    FileOperationMessage result = DocumentTemplate.withTemplate(TEMPLATE).withDocumentCreationOptions(options)
            .putDataAsSourceForSimpleMailMerge(DocFactoryTest.makePerson()).useLocale(Locale.forLanguageTag("de-CH"))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
    assertThat(result.getFiles().get(0).getName()).isEqualTo("test_pdfA_3B.pdf");
    // PDFBox cannot validate the PdfAType.PDF_A_3B result
    // but this tool can validate it:
    // https://www.pdf-online.com/osa/validate.aspx
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(result.getFiles().get(0))).isFalse();
  }

  @Test
  public void produce_pdfA_2U() throws IOException {
    // we instantiate the DocumentCreationOptions with the PdfOptions in it
    PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_2U);
    DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

    // we delete the file from a previous test run if needed
    File resultFile = DocFactoryTest.makeFile("test/documentTemplate/pdfA/test_pdfA_2U.pdf");

    // we use the DocumentTemplate Object for producing the PDF with the needed
    // options
    FileOperationMessage result = DocumentTemplate.withTemplate(TEMPLATE).withDocumentCreationOptions(options)
            .putDataAsSourceForSimpleMailMerge(DocFactoryTest.makePerson()).useLocale(Locale.forLanguageTag("de-CH"))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
    assertThat(result.getFiles().get(0).getName()).isEqualTo("test_pdfA_2U.pdf");
    // PDFBox cannot validate the PdfAType.PDF_A_2U result.
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(result.getFiles().get(0))).isFalse();
  }

  @Test
  public void produce_pdfA_3U() throws IOException {
    // we instantiate the DocumentCreationOptions with the PdfOptions in it
    PdfOptions pdfOptions = PdfOptions.getInstance().withPdfAType(PdfAType.PDF_A_3U);
    DocumentCreationOptions options = DocumentCreationOptions.getInstance().withPdfOptions(pdfOptions);

    // we delete the file from a previous test run if needed
    File resultFile = DocFactoryTest.makeFile("test/documentTemplate/pdfA/test_pdfA_3U.pdf");

    // we use the DocumentTemplate Object for producing the PDF with the needed
    // options
    FileOperationMessage result = DocumentTemplate.withTemplate(TEMPLATE).withDocumentCreationOptions(options)
            .putDataAsSourceForSimpleMailMerge(DocFactoryTest.makePerson()).useLocale(Locale.forLanguageTag("de-CH"))
            .produceDocument(resultFile);

    assertThat(result.isSuccess()).isTrue();
    assertThat(result.getFiles()).contains(resultFile);
    assertThat(result.getFiles().get(0).getName()).isEqualTo("test_pdfA_3U.pdf");
    // PDFBox cannot validate the PdfAType.PDF_A_3U result.
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(result.getFiles().get(0))).isFalse();
  }

}
