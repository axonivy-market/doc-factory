package ch.ivyteam.ivy.docFactoryExamples;

import java.io.IOException;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;
import ch.ivyteam.ivy.workflow.document.IDocument;

public class SimpleDocumentViewer {
  private String documentId;
  private String documentPath;

  public void init() {
    IDocument document = Ivy.wfCase().documents().get(documentId);
    try {
      // Load document URI
      documentPath = Ivy.html().fileLink(new File(document.getPath().toString())).getRelative();
    } catch (IOException e) {
      Ivy.log().error(e);
    }
  }

  public String getDocumentId() {
    return documentId;
  }

  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }

  public String getDocumentPath() {
    return documentPath;
  }

  public void setDocumentPath(String documentPath) {
    this.documentPath = documentPath;
  }
}
