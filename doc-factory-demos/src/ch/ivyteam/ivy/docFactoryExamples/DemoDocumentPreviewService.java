package ch.ivyteam.ivy.docFactoryExamples;

import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.PDF_CONTENT_TYPE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
//    SubProcessCallResult callResult = 
//        SubProcessCall.withPath(PREVIEW_DOCUMENT_PROCESS_PATH)
//            .withStartName("previewDocument")
//            .withParam("file", document.read().asJavaFile())
//            .call();
//    return (StreamedContent) callResult.get(STREAMED_CONTENT);
    
    InputStream test = previewDocumentViaInputStream(document.getName(), Files.probeContentType(document.read().asJavaFile().toPath()), document.read().asStream());
    return convertOutputStreamToStreamedContent(document.getName(), PDF_CONTENT_TYPE, test.readAllBytes());
  }

  public static StreamedContent previewDocumentViaStreamContent(IDocument document) throws IOException {
    SubProcessCallResult callResult =
        SubProcessCall.withPath(PREVIEW_DOCUMENT_PROCESS_PATH)
            .withStartName("previewDocumentByStream")
            .withParam(STREAMED_CONTENT, fileToStreamedContent(document.read().asJavaFile()))
            .call();

    return (StreamedContent) callResult.get(STREAMED_CONTENT);
  }
  
  public static InputStream previewDocumentViaInputStream(String fileName, String contentType, InputStream inputStream) throws IOException {
    SubProcessCallResult callResult =
        SubProcessCall.withPath(PREVIEW_DOCUMENT_PROCESS_PATH)
            .withStartName("previewDocumentByInputStream")
            .withParam("fileName", fileName)
            .withParam("contentType", contentType)
            .withParam("inputStream", inputStream)
            .call();

    return (InputStream) callResult.get("inputStream");
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

  private static StreamedContent convertOutputStreamToStreamedContent(ByteArrayOutputStream pdfOut, String fileName) {
    byte[] pdfBytes = pdfOut.toByteArray();
    return convertOutputStreamToStreamedContent(fileName, PDF_CONTENT_TYPE, pdfBytes);
  }

  private static StreamedContent convertOutputStreamToStreamedContent(String fileName, String contentType,
      byte[] fileContent) {
    return DefaultStreamedContent.builder().contentType(contentType).name(fileName)
        .stream(() -> new ByteArrayInputStream(fileContent)).build();
  }
}