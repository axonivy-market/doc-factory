package com.axonivy.utils.docfactory.preview;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;
import com.aspose.email.MailMessage;
import com.aspose.words.Document;
import com.aspose.words.LoadFormat;
import com.aspose.words.LoadOptions;
import com.aspose.words.SaveFormat;

import ch.ivyteam.ivy.addons.docfactory.entity.DocumentPreview;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.PDF_CONTENT_TYPE;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.XLSX_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.XLS_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.DOC_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.DOCX_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.EML_EXTENSION;

public class DocumentPreviewService {

  public static StreamedContent generateStreamedContent(DocumentPreview documentReview) throws Exception {
    String fileName = documentReview.getFileName();
    String contentType = documentReview.getContentType();
    InputStream inputStream = documentReview.getInputStream();
    StreamedContent content = null;
    if (fileName.endsWith(XLSX_EXTENSION) || fileName.endsWith(XLS_EXTENSION)) {
      content = convertExcelToPdfStreamedContent(inputStream, fileName);
    } else if (fileName.endsWith(DOC_EXTENSION) || fileName.endsWith(DOCX_EXTENSION)) {
      content = convertWordToPdfStreamedContent(inputStream, fileName);
    } else if (fileName.endsWith(EML_EXTENSION)) {
      content = convertEmlToPdfStreamedContent(inputStream, fileName);
    } else {
      content = convertOutputStreamToStreamedContent(fileName, contentType, inputStream);
    }
    return content;
  }

  private static StreamedContent convertExcelToPdfStreamedContent(InputStream data, String fileName) throws Exception {
    try (ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {
      Workbook workbook = new Workbook(data);
      PdfSaveOptions options = new PdfSaveOptions();
      options.setOnePagePerSheet(false);
      options.setAllColumnsInOnePagePerSheet(true);
      workbook.save(pdfOut, options);
      return convertOutputStreamToStreamedContent(pdfOut, fileName);
    }
  }

  private static StreamedContent convertWordToPdfStreamedContent(InputStream data, String fileName) throws Exception {
    try (ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {
      LoadOptions loadOptions = new LoadOptions();
      loadOptions.setLoadFormat(LoadFormat.AUTO);
      Document document = new Document(data, loadOptions);
      document.save(pdfOut, SaveFormat.PDF);
      return convertOutputStreamToStreamedContent(pdfOut, fileName);
    }
  }

  private static StreamedContent convertEmlToPdfStreamedContent(InputStream data, String fileName) throws Exception {
    try (ByteArrayOutputStream mhtmlStream = new ByteArrayOutputStream();
        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {
      MailMessage mailMsg = MailMessage.load(data);
      mailMsg.save(mhtmlStream, com.aspose.email.SaveOptions.getDefaultMhtml());
      var loadOptions = new LoadOptions();
      loadOptions.setLoadFormat(LoadFormat.MHTML);

      try (InputStream mhtInput = new ByteArrayInputStream(mhtmlStream.toByteArray())) {
        Document doc = new Document(mhtInput);
        doc.save(pdfOut, SaveFormat.PDF);
      }
      return convertOutputStreamToStreamedContent(pdfOut, fileName);
    }
  }

  private static StreamedContent convertOutputStreamToStreamedContent(ByteArrayOutputStream pdfOut, String fileName) {
    byte[] pdfBytes = pdfOut.toByteArray();
    return DefaultStreamedContent.builder().contentType(PDF_CONTENT_TYPE).name(fileName)
        .stream(() -> new ByteArrayInputStream(pdfBytes)).build();
  }

  private static StreamedContent convertOutputStreamToStreamedContent(String fileName, String contentType,
      InputStream inputStream) {
    return DefaultStreamedContent.builder().contentType(contentType).name(fileName).stream(() -> inputStream).build();
  }
}
