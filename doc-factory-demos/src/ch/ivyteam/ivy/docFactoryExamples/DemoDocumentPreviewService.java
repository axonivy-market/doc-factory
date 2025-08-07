package ch.ivyteam.ivy.docFactoryExamples;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

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

public class DemoDocumentPreviewService {

  public static IDocument handleFileUpload(FileUploadEvent event) throws IOException {
    return upload(event.getFile().getFileName(), event.getFile().getInputStream());
  }

  public static StreamedContent previewDocument(IDocument document) throws IOException {
    File file = new File(document.getPath().asString());
    byte[] fileContent = documentsOf(Ivy.wfCase()).get(document.uuid()).read().asStream().readAllBytes();
    String contentType = Files.probeContentType(file.getJavaFile().toPath());
    var entity = new DocumentPreview(document.getName(), contentType, fileContent);
    SubProcessCallResult callResult = SubProcessCall.withPath("Functional Processes/previewDocument")
        .withStartName("previewDocument").withParam("documentReview", entity).call();
    return (StreamedContent) callResult.get("streamedContent");
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
}
