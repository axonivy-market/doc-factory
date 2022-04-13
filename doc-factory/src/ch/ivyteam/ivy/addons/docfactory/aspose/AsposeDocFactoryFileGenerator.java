package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.aspose.words.DocSaveOptions;
import com.aspose.words.Document;
import com.aspose.words.HtmlSaveOptions;
import com.aspose.words.OdtSaveOptions;
import com.aspose.words.OoxmlCompliance;
import com.aspose.words.OoxmlSaveOptions;
import com.aspose.words.PdfSaveOptions;
import com.aspose.words.SaveFormat;
import com.aspose.words.SaveOptions;
import com.aspose.words.TxtSaveOptions;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.BaseDocFactory;
import ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants;
import ch.ivyteam.ivy.addons.docfactory.FileOperationMessage;
import ch.ivyteam.ivy.addons.docfactory.PdfFactory;
import ch.ivyteam.ivy.addons.docfactory.UnsupportedFormatException;
import ch.ivyteam.ivy.addons.docfactory.aspose.document.DocumentBlankPageRemover;
import ch.ivyteam.ivy.addons.docfactory.exception.DocFactoryException;
import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;
import ch.ivyteam.ivy.environment.Ivy;

/**
 * This AsposeDocFactoryFileGenerator is used internally by the DocumentTemplate
 * and the AsposeDocFatory for generating a File with given Aspose Document
 * after mail merging. <br />
 * <b>You should not use this Class</b> but instead the
 * {@link ch.ivyteam.ivy.addons.docfactory.DocumentTemplate#produceDocument(File)}
 * or an instance of the
 * {@link ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#getInstance()} class
 * with its generateDocument methods.
 *
 */
@SuppressWarnings("hiding")
public class AsposeDocFactoryFileGenerator {

  private DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance();
  private DocumentWorker documentWorker;

  private AsposeDocFactoryFileGenerator() {}

  public static AsposeDocFactoryFileGenerator getInstance() {
    return new AsposeDocFactoryFileGenerator();
  }

  /**
   * The DocumentCreationOptions contains diverse options for the final file
   * generation (e.g should a PDF be editable ...)
   * @param documentCreationOptions the DocumentCreationOptions object, cannot
   *          be null.
   * @return the AsposeDocFactoryFileGenerator object which
   *         DocumentCreationOptions has been set.
   */
  public AsposeDocFactoryFileGenerator withDocumentCreationOptions(
          DocumentCreationOptions documentCreationOptions) {
    API.checkNotNull(documentCreationOptions, "documentCreationOptions");
    this.documentCreationOptions = documentCreationOptions;
    return this;
  }

  /**
   * Set the {@link ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker} for
   * this AsposeDocFactoryFileGenerator. If set the
   * {@link ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker#onGeneratedFile(Document, File)}
   * logic will be applied after the file has been generated
   * @param documentWorker the
   *          {@link ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker}
   *          implementation to set. Null is accepted.
   * @return the AsposeDocFactoryFileGenerator object which documentWorker has
   *         been set.
   */
  public AsposeDocFactoryFileGenerator withDocumentWorker(DocumentWorker documentWorker) {
    this.documentWorker = documentWorker;
    return this;
  }

  /**
   * Saves the given aspose Document to the given baseFilePath with the
   * outputFormat
   * @param document the Aspose Document cannot be null.
   * @param baseFilePath the file path without format ending, cannot be blank.
   * @param outputFormat the output format int representation see
   *          {@link DocFactoryConstants#DOC_FORMAT},
   *          {@link DocFactoryConstants#DOCX_FORMAT} ...
   * @return the FileOperationMessage result which files list contain the
   *         produced java.io.File.
   * @throws Exception
   */
  public FileOperationMessage exportDocumentToFile(Document document, String baseFilePath, int outputFormat)
          throws Exception {
    API.checkNotNull(document, "document");
    API.checkNotEmpty(baseFilePath, "baseFilePath");
    API.checkRange(outputFormat, "outputFormat", DocFactoryConstants.DOC_FORMAT,
            DocFactoryConstants.ODT_FORMAT);

    applyDocumentCreationOptions(document);

    String filePath = makeFilePath(baseFilePath, outputFormat);
    SaveOptions saveOptions = getSaveOptionsForFormat(outputFormat);
    document.save(filePath, saveOptions);

    if (outputFormat == DocFactoryConstants.PDF_FORMAT) {
      applyPdfSpecificActions(filePath);
    }

    if (this.documentWorker != null) {
      return buildResult(this.documentWorker.onGeneratedFile(document, new File(filePath)));
    }
    return buildResult(new File(filePath));
  }

  private void applyPdfSpecificActions(String filePath) throws IllegalArgumentException, DocFactoryException {
    if (this.documentCreationOptions.getPdfOptions().isRemoveWhiteSpaceInPdfEditableFields()) {
      this.clearWhiteSpacePdfFieldsFromPdf(filePath);
    }
    if (this.documentCreationOptions.getPdfOptions().getPdfAType() != null) {
      PdfFactory.get().convertToPdfA(filePath, this.documentCreationOptions.getPdfOptions().getPdfAType());
    }

  }

  private void applyDocumentCreationOptions(Document document) throws Exception {
    if (documentCreationOptions.isRemoveBlankPages()) {
      DocumentBlankPageRemover.removesBlankPage(document);
    }
  }

  private SaveOptions getSaveOptionsForFormat(int outputFormat) {
    SaveOptions saveOptions;
    if (outputFormat == DocFactoryConstants.DOCX_FORMAT) {
      saveOptions = new OoxmlSaveOptions();
      ((OoxmlSaveOptions) saveOptions).setCompliance(OoxmlCompliance.ISO_29500_2008_TRANSITIONAL);
      saveOptions.setSaveFormat(SaveFormat.DOCX);
    } else if (outputFormat == DocFactoryConstants.PDF_FORMAT) {
      saveOptions = new PdfSaveOptions();
      ((PdfSaveOptions) saveOptions)
              .setPreserveFormFields(documentCreationOptions.getPdfOptions().isKeepFormFieldsEditableInPdf());
    } else if (outputFormat == DocFactoryConstants.HTML_FORMAT) {
      saveOptions = new HtmlSaveOptions(getAsposeSaveFormat(outputFormat));
      saveOptions.setSaveFormat(SaveFormat.HTML);
    } else if (outputFormat == DocFactoryConstants.ODT_FORMAT) {
      saveOptions = new OdtSaveOptions(getAsposeSaveFormat(outputFormat));
      saveOptions.setSaveFormat(SaveFormat.ODT);
    } else if (outputFormat == DocFactoryConstants.TXT_FORMAT) {
      saveOptions = new TxtSaveOptions();
      saveOptions.setSaveFormat(SaveFormat.TEXT);
    } else {
      saveOptions = new DocSaveOptions();
      saveOptions.setSaveFormat(getAsposeSaveFormat(outputFormat));
    }
    return saveOptions;
  }

  private String makeFilePath(String baseFilePath, int outputFormat) {
    return baseFilePath + (outputFormat == DocFactoryConstants.PDF_FORMAT ? DocFactoryConstants.PDF_EXTENSION
            : outputFormat == DocFactoryConstants.DOCX_FORMAT ? DocFactoryConstants.DOCX_EXTENSION
                    : outputFormat == DocFactoryConstants.DOC_FORMAT ? DocFactoryConstants.DOC_EXTENSION
                            : outputFormat == DocFactoryConstants.HTML_FORMAT
                                    ? DocFactoryConstants.HTML_EXTENSION
                                    : outputFormat == DocFactoryConstants.ODT_FORMAT
                                            ? DocFactoryConstants.ODT_EXTENSION
                                            : DocFactoryConstants.TXT_EXTENSION);
  }

  private int getAsposeSaveFormat(int outputFormat) {
    return (outputFormat == DocFactoryConstants.PDF_FORMAT ? SaveFormat.PDF
            : outputFormat == DocFactoryConstants.DOCX_FORMAT ? SaveFormat.DOCX
                    : outputFormat == DocFactoryConstants.DOC_FORMAT ? SaveFormat.DOC
                            : outputFormat == DocFactoryConstants.HTML_FORMAT ? SaveFormat.HTML
                                    : outputFormat == DocFactoryConstants.ODT_FORMAT ? SaveFormat.ODT
                                            : SaveFormat.DOC);
  }

  private void clearWhiteSpacePdfFieldsFromPdf(String filePath) {
    try {
      LicenseLoader.loadLicenseforProduct(AsposeProduct.PDF);
    } catch (Exception e) {
      Ivy.log().error(
              "AsposeDocFactoryFileGenerator#clearWhiteSpacePdfFieldsFromPdf Aspose PDF Licence error. "
                      + "Cannot clear the Pdf fields whitespace by the file {0}",
              filePath, e);
      return;
    }
    com.aspose.pdf.Document pdfDoc = new com.aspose.pdf.Document(filePath);

    cleanPdfFields(pdfDoc.getForm().getFields());
    pdfDoc.save(filePath);
  }

  private static void cleanPdfFields(com.aspose.pdf.Field[] fields) {
    Stream.of(fields).forEach(field -> {
      if (Objects.nonNull(field.getValue())) {
        field.setValue(field.getValue().trim());
      }
    });
  }

  /**
   * @deprecated This is not used anymore by the AsposeDocFactory because it was
   *             not flexible enough for getting new Options. Please set the
   *             document options with
   *             {@link #withDocumentCreationOptions(DocumentCreationOptions)}
   *             and use {@link #exportDocumentToFile(Document, String, int)}
   *             instead. <b>Will be removed in the Docfactory
   *             7.3.0-SNAPSHOT</b>
   */
  @Deprecated
  public static FileOperationMessage exportDocumentToFile(Document document, String baseFilePath,
          int outputFormat,
          boolean generateBlankDocumentIfGivenDocIsNull) throws Exception {
    File file = null;

    if (document == null && generateBlankDocumentIfGivenDocIsNull) {
      document = new Document();
    }

    if (document == null) {
      throw new IllegalArgumentException("document is null");
    }
    if (StringUtils.isEmpty(baseFilePath)) {
      throw new IllegalArgumentException("baseFilePath is empty");
    }

    switch (outputFormat) {
      case BaseDocFactory.DOC_FORMAT:
        document.save(baseFilePath + DocFactoryConstants.DOC_EXTENSION, SaveFormat.DOC);
        file = new File(baseFilePath + DocFactoryConstants.DOC_EXTENSION);
        break;
      case BaseDocFactory.DOCX_FORMAT:
        OoxmlSaveOptions ooxmlSaveOptions = new OoxmlSaveOptions();
        ooxmlSaveOptions.setCompliance(OoxmlCompliance.ISO_29500_2008_TRANSITIONAL);
        ooxmlSaveOptions.setSaveFormat(SaveFormat.DOCX);
        document.getCompatibilityOptions().setUICompat97To2003(true);
        document.save(baseFilePath + DocFactoryConstants.DOCX_EXTENSION, ooxmlSaveOptions);
        file = new File(baseFilePath + DocFactoryConstants.DOCX_EXTENSION);
        break;
      case BaseDocFactory.PDF_FORMAT:
        document.save(baseFilePath + DocFactoryConstants.PDF_EXTENSION);
        file = new File(baseFilePath + DocFactoryConstants.PDF_EXTENSION);

        break;
      case BaseDocFactory.HTML_FORMAT:
        document.save(baseFilePath + DocFactoryConstants.HTML_EXTENSION, SaveFormat.HTML);
        file = new File(baseFilePath + DocFactoryConstants.HTML_EXTENSION);
        break;
      case BaseDocFactory.TXT_FORMAT:
        document.save(baseFilePath + DocFactoryConstants.TXT_EXTENSION, SaveFormat.TEXT);
        file = new File(baseFilePath + DocFactoryConstants.TXT_EXTENSION);
        break;
      case BaseDocFactory.ODT_FORMAT:
        document.save(baseFilePath + DocFactoryConstants.ODT_EXTENSION, SaveFormat.ODT);
        file = new File(baseFilePath + DocFactoryConstants.ODT_EXTENSION);
        break;
      default:
        throw new UnsupportedFormatException(
                Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/formatNotSupported"));
    }

    return buildResult(file);
  }

  private static FileOperationMessage buildResult(File file)
          throws Exception {
    FileOperationMessage fom;
    try {
      if (!file.isFile()) {
        throw new IOException("The file " + file.getName()
                + " could not be produced. the Aspose Document could not be exported as File.");
      }
    } catch (SecurityException ex) {
      throw new Exception("A SecurityException occurred " + ex.getMessage(), ex);
    }
    fom = FileOperationMessage.generateSuccessTypeFileOperationMessage(
            Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/serialLetterSuccess") + " \n" +
                    file.getName());
    fom.addFile(file);

    return fom;
  }

}
