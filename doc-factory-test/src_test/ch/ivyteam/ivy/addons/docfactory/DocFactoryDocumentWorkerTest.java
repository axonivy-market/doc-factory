package ch.ivyteam.ivy.addons.docfactory;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;

import org.junit.Test;

import ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker;

public class DocFactoryDocumentWorkerTest extends DocFactoryTest {

  @Test
  public void BaseDocFactoryNoDocumentWorker() {
    BaseDocFactory docFactory = BaseDocFactory.getInstance();

    assertThat(docFactory.getDocumentWorker(), nullValue());
  }

  @Test
  public void BaseDocFactoryWithDocumentWorker() {
    DocumentWorker documentWorker = new WatermarkTextDocumentWorker("CONFIDENTIAL");
    BaseDocFactory docFactory = BaseDocFactory.getInstance().withDocumentWorker(documentWorker);

    assertThat(docFactory.getDocumentWorker(), is(documentWorker));
  }

  @Test
  public void AsposeDocFactoryNoDocumentWorker() {
    AsposeDocFactory docFactory = new AsposeDocFactory();

    assertThat(docFactory.getDocumentWorker(), nullValue());
  }

  @Test
  public void AsposeDocFactoryWithDocumentWorker() {
    DocumentWorker documentWorker = new WatermarkTextDocumentWorker("CONFIDENTIAL");
    AsposeDocFactory docFactory = new AsposeDocFactory().withDocumentWorker(documentWorker);

    assertThat(docFactory.getDocumentWorker(), is(documentWorker));
  }

  @Test
  public void DocFactoryWithDummyObjectAsDocumentWorkerAsposeDoesNotRecognizeIt() {
    Object o = new String("I am a document worker");
    BaseDocFactory docFactory = BaseDocFactory.getInstance().withDocumentWorker(o);

    assertThat(docFactory, instanceOf(AsposeDocFactory.class));
    assertThat(docFactory.getDocumentWorker(), nullValue());
  }

  @Test
  public void DocumentTemplateWithDocumentWorker() throws Exception {
    java.io.File template = new java.io.File(
            this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());
    DocumentWorker documentWorker = new WatermarkTextDocumentWorker("CONFIDENTIAL");

    DocumentTemplate documentTemplate = DocumentTemplate
            .withTemplate(template)
            .withDocumentWorker(documentWorker);

    assertThat(documentTemplate.getDocumentFactory().getDocumentWorker(), is(documentWorker));
  }

  @Test
  public void produceDocumentWithDocumentWorker_for_postcreate() throws URISyntaxException {
    java.io.File template = new java.io.File(
            this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());
    DocumentWorker documentWorker = new WatermarkTextDocumentWorker("DRAFT");

    DocumentTemplate documentTemplate = DocumentTemplate
            .withTemplate(template)
            .putDataAsSourceForSimpleMailMerge(makePerson())
            .useLocale(Locale.forLanguageTag("de-CH"))
            .withDocumentWorker(documentWorker);

    File resultFile = makeFile("test/documentWorker/watermark_with_postcreate.pdf");
    FileOperationMessage result = documentTemplate.produceDocument(resultFile);
    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
  }

  @Test
  public void produceDocumentWithDocumentWorker_for_prepare() throws URISyntaxException {
    java.io.File template = new java.io.File(
            this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());
    DocumentWorker documentWorker = new PageColorDocumentWorker();

    DocumentTemplate documentTemplate = DocumentTemplate
            .withTemplate(template)
            .putDataAsSourceForSimpleMailMerge(makePerson())
            .useLocale(Locale.forLanguageTag("de-CH"))
            .withDocumentWorker(documentWorker);

    File resultFile = makeFile("test/documentWorker/pagecolor_with_prepare.pdf");
    FileOperationMessage result = documentTemplate.produceDocument(resultFile);

    assertNotNull(result);
    assertTrue(result.isSuccess());
    assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
  }

}
