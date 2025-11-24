package com.axonivy.utils.docfactory.preview;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;
import com.aspose.email.MailMessage;
import com.aspose.words.Document;
import com.aspose.words.LoadFormat;
import com.aspose.words.LoadOptions;
import com.aspose.words.Orientation;
import com.aspose.words.PageSetup;
import com.aspose.words.SaveFormat;
import com.aspose.words.Section;

import ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants;
import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeProduct;
import ch.ivyteam.ivy.addons.docfactory.aspose.LicenseLoader;
import ch.ivyteam.ivy.environment.Ivy;

import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.PDF_CONTENT_TYPE;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.XLSX_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.XLS_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.DOC_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.DOCX_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.EML_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.MSG_EXTENSION;;

public class DocumentPreviewService {

  private static final DocumentPreviewService INSTANCE = new DocumentPreviewService();

  private DocumentPreviewService() {}

  public static DocumentPreviewService getInstance() {
    return INSTANCE;
  }

  public List<String> getSupportedTypeForPreview() {
    return DocFactoryConstants.SUPPORTED_TYPES_FOR_PREVIEW;
  }

  public StreamedContent generateStreamedContent(File file) throws Exception {
    String fileName = file.getName();
    String contentType = Files.probeContentType(file.toPath());
    byte[] fileContent = getDataFromFile(file);
    return convertToStreamContent(fileName, fileContent, contentType);
  }

  public StreamedContent generateStreamedContent(StreamedContent streamedContent) throws Exception {
    String fileName = streamedContent.getName();
    byte[] fileContent;
    try (InputStream input = streamedContent.getStream().get()) {
      fileContent = input.readAllBytes();
    }
    String contentType = streamedContent.getContentType();
    return convertToStreamContent(fileName, fileContent, contentType);
  }
  
  public InputStream generateInputStream(String fileName, String contentType, InputStream inputStream)
      throws Exception {
    StreamedContent content = convertToStreamContent(fileName, inputStream.readAllBytes(), contentType);
    return content.getStream().get();
  }

  private StreamedContent convertToStreamContent(String fileName, byte[] fileContent, String contentType)
      throws Exception {
    StreamedContent content = null;
    if (fileName.endsWith(XLSX_EXTENSION) || fileName.endsWith(XLS_EXTENSION)) {
      LicenseLoader.loadLicenseforProduct(AsposeProduct.CELLS);
      content = convertExcelToPdfStreamedContent(fileContent, fileName);
    } else if (fileName.endsWith(DOC_EXTENSION) || fileName.endsWith(DOCX_EXTENSION)) {
      LicenseLoader.loadLicenseforProduct(AsposeProduct.WORDS);
      content = convertWordToPdfStreamedContent(fileContent, fileName);
    } else if (fileName.endsWith(EML_EXTENSION) || fileName.endsWith(MSG_EXTENSION)) {
      LicenseLoader.loadLicenseforProduct(AsposeProduct.WORDS);
      LicenseLoader.loadLicenseforProduct(AsposeProduct.EMAIL);
      content = convertEmlToPdfStreamedContent(fileContent, fileName);
    } else {
      content = convertOutputStreamToStreamedContent(fileName, contentType, fileContent);
    }
    return content;
  }

  private byte[] getDataFromFile(File file) {
    try (FileInputStream fis = new FileInputStream(file)) {
      return fis.readAllBytes();
    } catch (IOException e) {
      Ivy.log().error(e.getMessage());
    }
    return new byte[0];
  }

  private StreamedContent convertExcelToPdfStreamedContent(byte[] data, String fileName) throws Exception {
    try (InputStream inputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {
      Workbook workbook = new Workbook(inputStream);
      PdfSaveOptions options = new PdfSaveOptions();
      options.setOnePagePerSheet(false);
      options.setAllColumnsInOnePagePerSheet(true);
      workbook.save(pdfOut, options);
      return convertOutputStreamToStreamedContent(pdfOut, fileName);
    }
  }

  private StreamedContent convertWordToPdfStreamedContent(byte[] data, String fileName) throws Exception {
    try (InputStream inputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {
      LoadOptions loadOptions = new LoadOptions();
      loadOptions.setLoadFormat(LoadFormat.AUTO);
      Document document = new Document(inputStream, loadOptions);
      document.save(pdfOut, SaveFormat.PDF);
      return convertOutputStreamToStreamedContent(pdfOut, fileName);
    }
  }

  private StreamedContent convertEmlToPdfStreamedContent(byte[] data, String fileName) throws Exception {
    try (InputStream inputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream mhtmlStream = new ByteArrayOutputStream();
        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {
      MailMessage mailMsg = MailMessage.load(inputStream);
      mailMsg.save(mhtmlStream, com.aspose.email.SaveOptions.getDefaultMhtml());
      var loadOptions = new LoadOptions();
      loadOptions.setLoadFormat(LoadFormat.MHTML);

      try (InputStream mhtmlInput = new ByteArrayInputStream(mhtmlStream.toByteArray())) {
        Document doc = new Document(mhtmlInput);
        for (Section section : doc.getSections()) {
          PageSetup pageSetup = section.getPageSetup();
          pageSetup.setOrientation(Orientation.LANDSCAPE);
        }
        doc.save(pdfOut, SaveFormat.PDF);
      }
      return convertOutputStreamToStreamedContent(pdfOut, fileName);
    }
  }

  private StreamedContent convertOutputStreamToStreamedContent(ByteArrayOutputStream pdfOut, String fileName) {
    byte[] pdfBytes = pdfOut.toByteArray();
    return convertOutputStreamToStreamedContent(fileName, PDF_CONTENT_TYPE, pdfBytes);
  }

  private StreamedContent convertOutputStreamToStreamedContent(String fileName, String contentType,
      byte[] fileContent) {
    return DefaultStreamedContent.builder().contentType(contentType).name(fileName)
        .stream(() -> new ByteArrayInputStream(fileContent)).build();
  }
}
