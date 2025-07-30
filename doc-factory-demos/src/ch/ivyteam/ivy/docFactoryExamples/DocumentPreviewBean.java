package ch.ivyteam.ivy.docFactoryExamples;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import org.apache.commons.lang3.ObjectUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import com.aspose.pdf.Document;

import ch.ivyteam.ivy.addons.docfactory.entity.DocumentPreview;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.persistence.PersistencyException;
import ch.ivyteam.ivy.process.call.SubProcessCall;
import ch.ivyteam.ivy.process.call.SubProcessCallResult;
import ch.ivyteam.ivy.scripting.objects.File;
import ch.ivyteam.ivy.security.exec.Sudo;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.document.IDocument;
import ch.ivyteam.ivy.workflow.document.IDocumentService;

// @ManagedBean
// @ViewScoped
public class DocumentPreviewBean {

  // private static final long serialVersionUID = 1L;
  private List<IDocument> uploadedFiles = new ArrayList<>();
  private StreamedContent streamedContent;
  private boolean isImage;

  // @PostConstruct
  // public void init() {
  // uploadedFiles = new ArrayList<>();
  // isImage = false;
  // streamedContent = createEmptyPdfStreamedContent();
  // Ivy.log().warn("sadasddasads");
  // }

  public void handleFileUpload(FileUploadEvent event) throws Exception {
    var entity = new DocumentPreview(event);
    SubProcessCallResult callResult = SubProcessCall.withPath("Functional Processes/reviewDocument")
        .withStartName("reviewDocument").withParam("documentReview", entity).call();
    isImage = entity.getContentType() != null && entity.getContentType().startsWith("image/");
    streamedContent = (StreamedContent) callResult.get("streamedContent");
    Ivy.log().warn(streamedContent.getName());
  }

  public static IDocument handleFileUpload2(FileUploadEvent event) throws IOException {
    IDocument document = upload(event.getFile().getFileName(), event.getFile().getInputStream());
    Ivy.log().warn(document.getName());
    Ivy.log().warn(document.getId());
    return document;

  }

  public static StreamedContent reviewFile(IDocument document) {
    InputStream inputStream = documentsOf(Ivy.wfCase()).get(Long.valueOf(document.getId())).read().asStream();
    return DefaultStreamedContent.builder().name("empty.pdf").contentType("application/pdf").stream(() -> inputStream)
        .build();
  }

  // public static StreamedContent reviewFile(IDocument document) throws IOException {
  // InputStream inputStream = documentsOf(Ivy.wfCase()).get(Long.valueOf(document.getId())).read().asStream();
  // File file = new File(document.getPath().asString());
  // var entity =
  // new DocumentPreview(document.getName(), Files.probeContentType(file.getJavaFile().toPath()), inputStream);
  // SubProcessCallResult callResult = SubProcessCall.withPath("Functional Processes/reviewDocument")
  // .withStartName("reviewDocument").withParam("documentReview", entity).call();
  // return (StreamedContent) callResult.get("streamedContent");
  // }

  // public StreamedContent download(IvyDocument document) {
  // InputStream inputStream = documentsOf(iCase).get(Long.valueOf(document.getId())).read().asStream();
  // return DefaultStreamedContent.builder().stream(() -> inputStream).contentType(document.getContentType())
  // .name(document.getName()).build();
  // }

  // public IvyDocument transform(IDocument document) {
  // try {
  // File file = new File(document.getPath().asString());
  // return IvyDocument.builder()
  // .id(String.valueOf(document.getId()))
  // .name(document.getName())
  // .path(document.getPath().asString())
  // .size(document.getSize())
  // .creation(document.getCreation())
  // .lastModification(document.getLastModification())
  // .uuid(document.uuid())
  // .contentType(Files.probeContentType(file.getJavaFile().toPath()))
  // .create();
  // } catch (IOException e) {
  // Ivy.log().error(e);
  // return new IvyDocument();
  // }
  // }

  public static IDocument upload(String filename, InputStream content) {
    try {
      return documentsOf(Ivy.wfCase()).add(filename).write().withContentFrom(content);
    } catch (PersistencyException e) {
      Ivy.log().error("Error in uploading the document {0} ", e, filename);
      return null;
    }
  }

  private static IDocumentService documentsOf(ICase iCase) {
    try {
      return Sudo.call(iCase::documents);
    } catch (Exception e) {
      return null;
    }

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

  public List<IDocument> getUploadedFiles() {
    return uploadedFiles;
  }

  public void setUploadedFiles(List<IDocument> uploadedFiles) {
    this.uploadedFiles = uploadedFiles;
  }


}
