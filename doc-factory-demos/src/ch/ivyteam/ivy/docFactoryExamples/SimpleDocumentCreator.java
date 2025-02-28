package ch.ivyteam.ivy.docFactoryExamples;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.SaveFormat;

import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeProduct;
import ch.ivyteam.ivy.addons.docfactory.aspose.LicenseLoader;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.workflow.document.IDocument;
import ch.ivyteam.ivy.workflow.document.Path;

public class SimpleDocumentCreator {
  private String name;
  private String documentId;

  public void init() {
    // Load license for aspose slides & cell
    try {
      LicenseLoader.loadLicenseforProduct(AsposeProduct.WORDS);
    } catch (Exception e) {
      Ivy.log().error(e);
    }
  }

  public void createHelloWord() throws Exception {

    String bindingName = name.trim().replace(" ", "_");

    ch.ivyteam.ivy.scripting.objects.File tempFileIvy = new ch.ivyteam.ivy.scripting.objects.File(
            "Hello_" + bindingName + ".pdf", true);

    Document doc = new Document();
    DocumentBuilder builder = new DocumentBuilder(doc);
    // Write logo and some text
    builder.insertImage("webapps/ivy/resources/branding/logo_mail.png", 212, 46);
    builder.insertParagraph();
    builder.insertHorizontalRule();
    builder.insertParagraph();
    builder.writeln();
    builder.writeln("Hello " + name);
    builder.writeln();
    builder.writeln("Nice to meet you :)");

    // Save to ivy temporary file
    doc.save(tempFileIvy.getAbsolutePath(), SaveFormat.PDF);

    // Save to ivy database
    IDocument document = Ivy.wfCase().documents()
            .add(new Path("KindOfDocument/" + tempFileIvy.getName()))
            .write()
            .withContentFrom(tempFileIvy.getJavaFile());

    // Get document id for the next dialog
    documentId = document.uuid();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDocumentId() {
    return documentId;
  }

  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }
}
