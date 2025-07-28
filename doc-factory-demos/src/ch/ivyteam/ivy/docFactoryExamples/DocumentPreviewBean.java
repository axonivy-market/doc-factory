package ch.ivyteam.ivy.docFactoryExamples;

import java.io.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.lang3.ObjectUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;
import com.aspose.words.Document;
import com.aspose.words.LoadFormat;
import com.aspose.words.LoadOptions;
import com.aspose.words.SaveFormat;
import com.aspose.email.MailMessage;

@ManagedBean
@SessionScoped
public class DocumentPreviewBean {
  private StreamedContent streamedContent;
  private boolean image;

  @PostConstruct
  public void init() {
    image = false;
    streamedContent = null;
  }

  public void handleFileUpload(FileUploadEvent event) throws Exception {
    String fileName = event.getFile().getFileName();
    String contentType = event.getFile().getContentType();
    byte[] fileContent = event.getFile().getContent();
    if (fileName.endsWith(".xlsx")) {
      image = false;
      streamedContent = convertExcelToPdfStreamedContent(fileContent);
    } else if (fileName.endsWith(".doc")) {
      image = false;
      streamedContent = convertWordToPdfStreamedContent(fileContent);
    } else if (fileName.endsWith(".eml")) {
      image = false;
      streamedContent = convertEmlToPdfStreamedContent(fileContent);
    } else if (contentType.startsWith("image/")) {
      image = true;
      streamedContent = DefaultStreamedContent.builder().contentType(contentType)
          .stream(() -> new ByteArrayInputStream(fileContent)).build();
    } else {
      image = false;
      streamedContent = convertPdfToStreamedContent(fileContent);
    }
  }

  public StreamedContent convertExcelToPdfStreamedContent(byte[] data) throws Exception {
    try (InputStream inputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {
      Workbook workbook = new Workbook(inputStream);
      PdfSaveOptions options = new PdfSaveOptions();
      options.setOnePagePerSheet(true);
      workbook.save(pdfOut, options);
      return convertOutputStreamToStreamedContent(pdfOut);
    }
  }

  public StreamedContent convertWordToPdfStreamedContent(byte[] data) throws Exception {
    try (InputStream inputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()) {
      Document document = new Document(inputStream);
      document.save(pdfOut, SaveFormat.PDF);
      return convertOutputStreamToStreamedContent(pdfOut);
    }
  }

  public StreamedContent convertEmlToPdfStreamedContent(byte[] data) throws Exception {
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
      return convertOutputStreamToStreamedContent(pdfOut);
    }
  }

  public StreamedContent convertPdfToStreamedContent(byte[] data) throws IOException {
    ByteArrayInputStream inputPdfStream = new ByteArrayInputStream(data);
    com.aspose.pdf.Document pdfDocument = new com.aspose.pdf.Document(inputPdfStream);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    pdfDocument.save(outputStream);
    return convertOutputStreamToStreamedContent(outputStream);
  }


  private StreamedContent convertOutputStreamToStreamedContent(ByteArrayOutputStream pdfOut) {
    byte[] pdfBytes = pdfOut.toByteArray();
    return DefaultStreamedContent.builder().contentType("application/pdf")
        .stream(() -> new ByteArrayInputStream(pdfBytes)).build();
  }


  public StreamedContent getStreamedContent() {
    return streamedContent;
  }

  public boolean isImage() {
    return image;
  }

  public void setImage(boolean image) {
    this.image = image;
  }

  public boolean isEmpty() {
    return ObjectUtils.isEmpty(streamedContent);
  }
}
