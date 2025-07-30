package ch.ivyteam.ivy.docFactoryExamples;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.view.ViewScoped;

import org.apache.commons.lang3.ObjectUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import com.aspose.pdf.Document;
import ch.ivyteam.ivy.addons.docfactory.entity.DocumentPreview;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.process.call.SubProcessCall;
import ch.ivyteam.ivy.process.call.SubProcessCallResult;

@ManagedBean
@SessionScoped
public class DocumentPreviewBean implements Serializable {

  private static final long serialVersionUID = 1L;
  private StreamedContent streamedContent;
  private boolean isImage;

  @PostConstruct
  public void init() {
    isImage = false;
    streamedContent = createEmptyPdfStreamedContent();
  }

  public void handleFileUpload(FileUploadEvent event) throws Exception {
    var entity = new DocumentPreview(event);
    SubProcessCallResult callResult = SubProcessCall.withPath("Functional Processes/reviewDocument")
        .withStartName("reviewDocument").withParam("documentReview", entity).call();
    isImage = entity.getContentType() != null && entity.getContentType().startsWith("image/");
    streamedContent = (StreamedContent) callResult.get("streamedContent");
    Ivy.log().warn(streamedContent.getName());
  }

  public StreamedContent getStreamedContent() {
    return streamedContent;
  }

  public void setStreamedContent(StreamedContent streamedContent) {
    this.streamedContent = streamedContent;
  }

  public boolean isImage() {
    return isImage;
  }

  public void setImage(boolean isImage) {
    this.isImage = isImage;
  }

  public boolean isEmpty() {
    return ObjectUtils.isEmpty(streamedContent);
  }

  public StreamedContent createEmptyPdfStreamedContent() {
    // Create a new empty Aspose PDF document
    Document pdfDocument = new Document();

    pdfDocument.getPages().add();

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      // Save the PDF to the output stream
      pdfDocument.save(outputStream);

      // Convert to StreamedContent
      InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

      return DefaultStreamedContent.builder().name("empty.pdf").contentType("application/pdf").stream(() -> inputStream)
          .build();

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
