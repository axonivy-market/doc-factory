package ch.ivyteam.ivy.addons.docfactory;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class DocFactoryDocumentWorkerTest {

  @Test
  public void BaseDocFactoryNoDocumentWorker() {
    var docFactory = BaseDocFactory.getInstance();
    assertThat((Object) docFactory.getDocumentWorker()).isNull();
  }

  @Test
  public void BaseDocFactoryWithDocumentWorker() {
    var documentWorker = new WatermarkTextDocumentWorker("CONFIDENTIAL");
    var docFactory = BaseDocFactory.getInstance().withDocumentWorker(documentWorker);
    assertThat((Object) docFactory.getDocumentWorker()).isEqualTo(documentWorker);
  }

  @Test
  public void AsposeDocFactoryNoDocumentWorker() {
    var docFactory = new AsposeDocFactory();
    assertThat((Object) docFactory.getDocumentWorker()).isNull();
  }

  @Test
  public void AsposeDocFactoryWithDocumentWorker() {
    var documentWorker = new WatermarkTextDocumentWorker("CONFIDENTIAL");
    var docFactory = new AsposeDocFactory().withDocumentWorker(documentWorker);
    assertThat((Object) docFactory.getDocumentWorker()).isEqualTo(documentWorker);
  }

  @Test
  public void DocFactoryWithDummyObjectAsDocumentWorkerAsposeDoesNotRecognizeIt() {
    var o = new String("I am a document worker");
    var docFactory = BaseDocFactory.getInstance().withDocumentWorker(o);
    assertThat(docFactory).isInstanceOf(AsposeDocFactory.class);
    assertThat((Object) docFactory.getDocumentWorker()).isNull();
  }

  @Test
  public void DocumentTemplateWithDocumentWorker() throws Exception {
    var template = new java.io.File(this.getClass().getResource(DocFactoryTest.TEMPLATE_PERSON_DOCX).toURI().getPath());
    var documentWorker = new WatermarkTextDocumentWorker("CONFIDENTIAL");
    var documentTemplate = DocumentTemplate
            .withTemplate(template)
            .withDocumentWorker(documentWorker);
    assertThat((Object) documentTemplate.getDocumentFactory().getDocumentWorker()).isEqualTo(documentWorker);
  }

  @Test
  public void produceDocumentWithDocumentWorker_for_postcreate() throws URISyntaxException {
    java.io.File template = new java.io.File(
            this.getClass().getResource(DocFactoryTest.TEMPLATE_PERSON_DOCX).toURI().getPath());
    DocumentWorker documentWorker = new WatermarkTextDocumentWorker("DRAFT");

    DocumentTemplate documentTemplate = DocumentTemplate
            .withTemplate(template)
            .putDataAsSourceForSimpleMailMerge(DocFactoryTest.makePerson())
            .useLocale(Locale.forLanguageTag("de-CH"))
            .withDocumentWorker(documentWorker);

    File resultFile = DocFactoryTest.makeFile("test/documentWorker/watermark_with_postcreate.pdf");
    FileOperationMessage result = documentTemplate.produceDocument(resultFile);
    
    assertThat(result).isNotNull();
   	assertThat(result.isSuccess()).isTrue();
   	assertThat(result.getFiles()).contains(resultFile);
  }

  @Test
  public void produceDocumentWithDocumentWorker_for_prepare() throws URISyntaxException {
    java.io.File template = new java.io.File(
            this.getClass().getResource(DocFactoryTest.TEMPLATE_PERSON_DOCX).toURI().getPath());
    DocumentWorker documentWorker = new PageColorDocumentWorker();

    DocumentTemplate documentTemplate = DocumentTemplate
            .withTemplate(template)
            .putDataAsSourceForSimpleMailMerge(DocFactoryTest.makePerson())
            .useLocale(Locale.forLanguageTag("de-CH"))
            .withDocumentWorker(documentWorker);

    File resultFile = DocFactoryTest.makeFile("test/documentWorker/pagecolor_with_prepare.pdf");
    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertThat(result).isNotNull();
   	assertThat(result.isSuccess()).isTrue();
   	assertThat(result.getFiles()).contains(resultFile);
  }
}
