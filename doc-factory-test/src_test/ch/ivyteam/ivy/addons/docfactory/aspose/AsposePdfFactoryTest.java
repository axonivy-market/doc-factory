package ch.ivyteam.ivy.addons.docfactory.aspose;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.exception.DocFactoryException;
import ch.ivyteam.ivy.addons.docfactory.pdf.PdfAType;
import ch.ivyteam.ivy.addons.docfactory.pdfbox.PdfAValidator;

public class AsposePdfFactoryTest {

  private static java.io.File PDF_FILE;
  private static com.aspose.pdf.License PDF_LICENSE;
  private static com.aspose.words.License WORDS_LICENSE;

  AsposePdfFactory asposePdfFactory;

  @BeforeAll
  public static void setupClass() throws URISyntaxException {
    try (InputStream licIn = AsposePdfFactoryTest.class
            .getResourceAsStream("resources/docfactory_2019_09_04.lic")) {
      PDF_LICENSE = new com.aspose.pdf.License();
      PDF_LICENSE.setLicense(licIn);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    try (InputStream licIn = AsposePdfFactoryTest.class
            .getResourceAsStream("resources/docfactory_2019_09_04.lic")) {
      WORDS_LICENSE = new com.aspose.words.License();
      WORDS_LICENSE.setLicense(licIn);
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    PDF_FILE = new java.io.File(
            AsposePdfFactoryTest.class.getResource("../resources/files/simplePDF.pdf").toURI().getPath());
  }

  @BeforeEach
  public void setup() throws Exception {
    asposePdfFactory = new AsposePdfFactory(false);
  }

  @Test
  public void convertToPdfA_thows_IllegalArgumentException_with_null_arg() throws Exception {
    assertThatThrownBy(() -> asposePdfFactory.convertToPdfA(null, PdfAType.PDF_A_1A)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void convertToPdfA_thows_IllegalArgumentException_with_emptyString_arg() throws Exception {
    assertThatThrownBy(() -> asposePdfFactory.convertToPdfA(" ", PdfAType.PDF_A_1A)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void convertToPdfA_thows_DocFactoryException_for_non_existing_file() throws Exception {
    java.io.File nonExistingPdf = new java.io.File("test/nonExistingPdf.pdf");
    assertThat(nonExistingPdf.exists()).isFalse();
    assertThatThrownBy(() -> asposePdfFactory.convertToPdfA(nonExistingPdf.getAbsolutePath(), PdfAType.PDF_A_1A)).isInstanceOf(DocFactoryException.class);
  }

  @Test
  public void convertToPdfA_thows_IllegalArgumentException_for_file_other_than_pdf() throws Exception {
    assertThatThrownBy(() -> asposePdfFactory.convertToPdfA("document.docx", PdfAType.PDF_A_1A)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void convertToPdfA_PDF_A_1A() throws Exception {
    java.io.File resultFile = makePDFToConvert("test/pdfA/pdf_A_1A.pdf");

    asposePdfFactory.convertToPdfA(resultFile.getAbsolutePath(), PdfAType.PDF_A_1A);

    assertThat(resultFile.isFile()).isTrue();
    // PDFBox can validate the PDF/A result
    // This tool cannot validate it: https: www.pdf-online.com/osa/validate.aspx
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(resultFile)).isTrue();
  }

  @Test
  public void convertToPdfA_PDF_A_1B() throws DocFactoryException, IOException {
    java.io.File resultFile = makePDFToConvert("test/pdfA/pdf_A_1B.pdf");

    asposePdfFactory.convertToPdfA(resultFile.getAbsolutePath(), PdfAType.PDF_A_1B);

    assertThat(resultFile.isFile()).isTrue();
    // PDFBox can validate the PdfAType.PDF_A_1B result
    // this tool also: https://www.pdf-online.com/osa/validate.aspx
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(resultFile)).isTrue();
  }

  @Test
  public void convertToPdfA_PDF_A_2A() throws DocFactoryException, IOException {
    java.io.File resultFile = makePDFToConvert("test/pdfA/pdf_A_2A.pdf");

    asposePdfFactory.convertToPdfA(resultFile.getAbsolutePath(), PdfAType.PDF_A_2A);

    assertThat(resultFile.isFile()).isTrue();
    // PDFBox cannot validate the PdfAType.PDF_A_2A result.
    // it seems there is a bug with aspose with PdfAType.PDF_A_2A,
    // PdfAType.PDF_A_3A see
    // https://forum.aspose.com/t/aspose-pdf-java-pdf-to-pdf-a-convertion-does-not-produce-valid-pdf-a-1a-pdf-a-2a-pdf-a-3a/212847
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(resultFile)).isFalse();
  }

  @Test
  public void convertToPdfA_PDF_A_2B() throws DocFactoryException, IOException {
    java.io.File resultFile = makePDFToConvert("test/pdfA/pdf_A_2B.pdf");

    asposePdfFactory.convertToPdfA(resultFile.getAbsolutePath(), PdfAType.PDF_A_2B);

    assertThat(resultFile.isFile()).isTrue();
    // PDFBox cannot validate the PdfAType.PDF_A_2B result. But other online
    // tools can.
    // Example: https://www.pdf-online.com/osa/validate.aspx can validate the
    // result of this unit test.
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(resultFile)).isFalse();
  }

  @Test
  public void convertToPdfA_PDF_A_3A() throws DocFactoryException, IOException {
    java.io.File resultFile = makePDFToConvert("test/pdfA/pdf_A_3A.pdf");

    asposePdfFactory.convertToPdfA(resultFile.getAbsolutePath(), PdfAType.PDF_A_3A);

    assertThat(resultFile.isFile()).isTrue();
    // PDFBox cannot validate the PdfAType.PDF_A_3A result.
    // it seems there is a bug with aspose with PdfAType.PDF_A_2A,
    // PdfAType.PDF_A_3A see
    // https://forum.aspose.com/t/aspose-pdf-java-pdf-to-pdf-a-convertion-does-not-produce-valid-pdf-a-1a-pdf-a-2a-pdf-a-3a/212847
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(resultFile)).isFalse();
  }

  @Test
  public void convertToPdfA_PDF_A_3B() throws DocFactoryException, IOException {
    java.io.File resultFile = makePDFToConvert("test/pdfA/pdf_A_3B.pdf");

    asposePdfFactory.convertToPdfA(resultFile.getAbsolutePath(), PdfAType.PDF_A_3B);

    assertThat(resultFile.isFile()).isTrue();
    // PDFBox cannot validate the PdfAType.PDF_A_3B result. But other online
    // tools can.
    // Example: https://www.pdf-online.com/osa/validate.aspx can validate the
    // result of this unit test.
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(resultFile)).isFalse();
  }

  @Test
  public void convertToPdfA_PDF_A_2U() throws DocFactoryException, IOException {
    java.io.File resultFile = makePDFToConvert("test/pdfA/pdf_A_2U.pdf");

    asposePdfFactory.convertToPdfA(resultFile.getAbsolutePath(), PdfAType.PDF_A_2U);

    assertThat(resultFile.isFile()).isTrue();
    // PDFBox cannot validate the PdfAType.PDF_A_2U result. But other online
    // tools can.
    // Example: https://www.pdf-online.com/osa/validate.aspx can validate the
    // result of this unit test.
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(resultFile)).isFalse();
  }

  @Test
  public void convertToPdfA_PDF_A_3U() throws DocFactoryException, IOException {
    java.io.File resultFile = makePDFToConvert("test/pdfA/pdf_A_3U.pdf");

    asposePdfFactory.convertToPdfA(resultFile.getAbsolutePath(), PdfAType.PDF_A_3U);

    assertThat(resultFile.isFile()).isTrue();
    // PDFBox cannot validate the PdfAType.PDF_A_3U result. But other online
    // tools can.
    // Example: https://www.pdf-online.com/osa/validate.aspx can validate the
    // result of this unit test.
    // We should in general be careful with PDF/A validation as it does not seem
    // to be consistent across different tools.
    assertThat(PdfAValidator.isPDFACompliant(resultFile)).isFalse();
  }

  private java.io.File makePDFToConvert(String pdfToConvertPath) throws IOException {
    java.io.File pdfToConvert = new java.io.File(pdfToConvertPath);
    if (pdfToConvert.exists()) {
      pdfToConvert.delete();
    }
    if (!pdfToConvert.getParentFile().isDirectory()) {
      pdfToConvert.getParentFile().mkdirs();
    }
    Files.copy(PDF_FILE.toPath(), pdfToConvert.toPath()).toFile();
    return pdfToConvert;
  }

}
