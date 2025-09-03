package ch.ivyteam.ivy.docFactoryExamples;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.persistence.PersistencyException;
import ch.ivyteam.ivy.process.call.SubProcessCall;
import ch.ivyteam.ivy.process.call.SubProcessCallResult;
import ch.ivyteam.ivy.security.exec.Sudo;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.workflow.document.IDocument;
import ch.ivyteam.ivy.workflow.document.IDocumentService;

public class DemoDocumentPreviewService {
  
  private static final String PREVIEW_DOCUMENT_PROCESS_PATH = "Functional Processes/previewDocument";
  private static final String STREAMED_CONTENT = "streamedContent";

  public static IDocument handleFileUpload(FileUploadEvent event) throws IOException {
    return upload(event.getFile().getFileName(), event.getFile().getInputStream());
  }

  public static StreamedContent previewDocument(IDocument document) throws IOException {
    SubProcessCallResult callResult = 
        SubProcessCall.withPath(PREVIEW_DOCUMENT_PROCESS_PATH)
            .withStartName("previewDocument")
            .withParam("file", document.read().asJavaFile())
            .call();
    return (StreamedContent) callResult.get(STREAMED_CONTENT);
  }

  public static StreamedContent previewDocumentViaStreamContent(IDocument document) throws IOException {
    SubProcessCallResult callResult =
        SubProcessCall.withPath(PREVIEW_DOCUMENT_PROCESS_PATH)
            .withStartName("previewDocumentByStream")
            .withParam(STREAMED_CONTENT, fileToStreamedContent(document.read().asJavaFile()))
            .call();

    return (StreamedContent) callResult.get(STREAMED_CONTENT);
  }

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

  private static StreamedContent fileToStreamedContent(java.io.File file) throws IOException, java.io.IOException {
    String fileName = file.getName();
    String contentType = Files.probeContentType(file.toPath());

    return DefaultStreamedContent.builder()
        .name(fileName)
        .contentType(contentType != null ? contentType : "application/octet-stream")
        .stream(() -> {
          try {
            return new FileInputStream(file);
          } catch (FileNotFoundException e) {
            Ivy.log().error(e.getMessage());
          }
          return new ByteArrayInputStream(new byte[0]);
        }).build();
  }

 
}