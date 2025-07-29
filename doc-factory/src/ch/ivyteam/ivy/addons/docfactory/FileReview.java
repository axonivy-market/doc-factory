package ch.ivyteam.ivy.addons.docfactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

import ch.ivyteam.ivy.addons.docfactory.entity.FileReviewEntity;

public class FileReview {
  private static final String PDF_CONTENT_TYPE = "application/pdf";

  public static StreamedContent generateStreamedContent(FileReviewEntity fileReviewEntity) throws Exception {
    String fileName = fileReviewEntity.getFileName();
    String contentType = fileReviewEntity.getContentType();
    byte[] fileContent = fileReviewEntity.getFileContent();
    StreamedContent content = null;
    if (fileName.endsWith(".xlsx")) {
      content = convertExcelToPdfStreamedContent(fileContent, fileName);
    } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
      content = convertWordToPdfStreamedContent(fileContent, fileName);
    } else if (fileName.endsWith(".eml")) {
      content = convertEmlToPdfStreamedContent(fileContent, fileName);
    } else if (fileName.endsWith(".pdf")) {
      content = convertPdfToStreamedContent(fileContent, fileName);
    } else {
      content = DefaultStreamedContent.builder().contentType(contentType).name(fileName)
          .stream(() -> new ByteArrayInputStream(fileContent)).build();
    }
    return content;
  }

  private static StreamedContent convertExcelToPdfStreamedContent(byte[] data, String fileName) throws Exception {
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

  private static StreamedContent convertWordToPdfStreamedContent(byte[] data, String fileName) throws Exception {
    try (InputStream inputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {
      LoadOptions loadOptions = new LoadOptions();
      loadOptions.setLoadFormat(LoadFormat.AUTO);
      Document document = new Document(inputStream, loadOptions);
      document.save(pdfOut, SaveFormat.PDF);
      return convertOutputStreamToStreamedContent(pdfOut, fileName);
    }
  }

  private static StreamedContent convertEmlToPdfStreamedContent(byte[] data, String fileName) throws Exception {
    try (InputStream inputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream mhtStream = new ByteArrayOutputStream();
        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {
      MailMessage mailMsg = MailMessage.load(inputStream);
      mailMsg.save(mhtStream, com.aspose.email.SaveOptions.getDefaultMhtml());
      var loadOptions = new LoadOptions();
      loadOptions.setLoadFormat(LoadFormat.MHTML);

      try (InputStream mhtInput = new ByteArrayInputStream(mhtStream.toByteArray())) {
        Document doc = new Document(mhtInput);
        doc.save(pdfOut, SaveFormat.PDF);
      }
      return convertOutputStreamToStreamedContent(pdfOut, fileName);
    }
  }

  private static StreamedContent convertPdfToStreamedContent(byte[] data, String fileName) throws IOException {
    ByteArrayInputStream inputPdfStream = new ByteArrayInputStream(data);
    com.aspose.pdf.Document pdfDocument = new com.aspose.pdf.Document(inputPdfStream);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    pdfDocument.save(outputStream);
    return convertOutputStreamToStreamedContent(outputStream, fileName);
  }


  private static StreamedContent convertOutputStreamToStreamedContent(ByteArrayOutputStream pdfOut, String fileName) {
    byte[] pdfBytes = pdfOut.toByteArray();
    return DefaultStreamedContent.builder().contentType(PDF_CONTENT_TYPE).name(fileName)
        .stream(() -> new ByteArrayInputStream(pdfBytes)).build();
  }
}
