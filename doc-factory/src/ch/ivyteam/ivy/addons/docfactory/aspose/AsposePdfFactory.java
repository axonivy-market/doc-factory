package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.aspose.pdf.ConvertErrorAction;
import com.aspose.pdf.Document;
import com.aspose.pdf.PdfFormat;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.PdfFactory;
import ch.ivyteam.ivy.addons.docfactory.exception.DocFactoryException;
import ch.ivyteam.ivy.addons.docfactory.pdf.PdfAType;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;
import ch.ivyteam.log.Logger;

public class AsposePdfFactory extends PdfFactory {

  private static final Logger LOGGER = Logger.getLogger(AsposePdfFactory.class);
  private static final String PDF_EXTENSION = ".pdf";

  public AsposePdfFactory() {
    init();
  }

  /**
   * visible for unit tests
   * @param loadLicense
   */
  protected AsposePdfFactory(boolean loadLicense) {
    if (loadLicense) {
      init();
    }
  }

  private void init() {
    try {
      LicenseLoader.loadLicenseforProduct(AsposeProduct.PDF);
    } catch (Exception e) {
      Ivy.log().error("An error occurred while loading the Aspose PDF license.", e);
    }
  }

  @Override
  public File appendPdfFiles(String resultFilePath, List<java.io.File> pdfsToAppend) throws DocFactoryException {
    API.checkNotEmpty(resultFilePath, "resultFileName");
    API.checkNotNull(pdfsToAppend, "pdfsToAppend");
    API.checkNotEmpty(pdfsToAppend, "pdfsToAppend");

    try {
      Document pdfDocument = new Document(new FileInputStream(pdfsToAppend.get(0)));
      return appendFilesToDocument(pdfDocument, pdfsToAppend.subList(1, pdfsToAppend.size()), resultFilePath);
    } catch (Exception ex) {
      throw new DocFactoryException("An error occurred while generating the pdf file. " + ex.getMessage(), ex);
    }
  }

  @Override
  public void convertToPdfA(String filePath, PdfAType pdfAType)
          throws IllegalArgumentException, DocFactoryException {
    if (StringUtils.isBlank(filePath) || !filePath.toLowerCase().endsWith(PDF_EXTENSION)) {
      throw new IllegalArgumentException("The filePath parameter must point to a PDF file.");
    }
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try (InputStream in = new FileInputStream(filePath);
      var pdfDocument = new com.aspose.pdf.Document(in)) {
      pdfDocument.convert(bos, getAsposePDFAType(pdfAType), ConvertErrorAction.Delete);
      pdfDocument.setLinearized(true);
      pdfDocument.save(filePath);
    } catch (Exception ex) {
      throw new DocFactoryException(ex);
    } finally {
      var logs = new String(bos.toByteArray());
      if (!logs.isBlank()) {
        logs.lines().forEachOrdered(logLine -> {
          LOGGER.error(logLine);
        });
      }
    }
  }

  private PdfFormat getAsposePDFAType(PdfAType pdfAType) {
    switch (pdfAType) {
      case PDF_A_1A:
        return com.aspose.pdf.PdfFormat.PDF_A_1A;
      case PDF_A_2A:
        return com.aspose.pdf.PdfFormat.PDF_A_2A;
      case PDF_A_3A:
        return com.aspose.pdf.PdfFormat.PDF_A_3A;
      case PDF_A_1B:
        return com.aspose.pdf.PdfFormat.PDF_A_1B;
      case PDF_A_2B:
        return com.aspose.pdf.PdfFormat.PDF_A_2B;
      case PDF_A_3B:
        return com.aspose.pdf.PdfFormat.PDF_A_3B;
      case PDF_A_2U:
        return com.aspose.pdf.PdfFormat.PDF_A_2U;
      case PDF_A_3U:
        return com.aspose.pdf.PdfFormat.PDF_A_3U;
      default:
        return com.aspose.pdf.PdfFormat.PDF_A_1A;
    }
  }

  /**
   * Only visible for testing
   */
  protected File appendFilesToDocument(Document document, List<java.io.File> pdfsToAppend,
          String resultFilePath) throws IOException {
    try {
      for (java.io.File pdf : pdfsToAppend) {
        appendPdfDocuments(document, new Document(new FileInputStream(pdf)));
      }
      File result = new File(resultFilePath, true);
      document.save(result.getAbsolutePath());
      return result;
    } finally {
      if (document != null) {
        document.close();
      }
    }
  }

  /**
   * Only visible for testing
   */
  protected void appendPdfDocuments(Document pdfDocument, Document appendedPdfDocument) {
    try {
      pdfDocument.getPages().add(appendedPdfDocument.getPages());
    } finally {
      if (appendedPdfDocument != null) {
        appendedPdfDocument.close();
      }
    }
  }

}
