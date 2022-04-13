package ch.ivyteam.ivy.addons.docfactory;

import java.awt.Color;

import com.aspose.words.Document;

import ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker;

public class PageColorDocumentWorker implements DocumentWorker {

  @Override
  public void prepare(Document document) {
    document.setPageColor(Color.green);
  }

}
