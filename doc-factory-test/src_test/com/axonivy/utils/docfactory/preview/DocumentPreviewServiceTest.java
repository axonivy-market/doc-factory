package com.axonivy.utils.docfactory.preview;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;
import org.primefaces.model.StreamedContent;
import ch.ivyteam.ivy.addons.docfactory.entity.DocumentPreview;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.PDF_CONTENT_TYPE;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.XLSX_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.TXT_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.DOCX_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.EML_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.XLSX_CONTENT_TYPE;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.WORD_CONTENT_TYPE;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.EML_CONTENT_TYPE;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.TEXT_CONTENT_TYPE;

public class DocumentPreviewServiceTest {
  private static final String TEST_FILE_NAME = "test";

  @Test
  void testGenerateStreamedContent_ExcelXlsx() throws Exception {
    String fileName = TEST_FILE_NAME.concat(XLSX_EXTENSION);
    var workbook = new com.aspose.cells.Workbook();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    workbook.save(out, com.aspose.cells.SaveFormat.XLSX);

    DocumentPreview preview = new DocumentPreview(fileName, XLSX_CONTENT_TYPE, out.toByteArray());
    StreamedContent content = DocumentPreviewService.generateStreamedContent(preview);

    assertNotNull(content);
    assertEquals(fileName, content.getName());
    assertEquals(PDF_CONTENT_TYPE, content.getContentType());
  }

  @Test
  void testGenerateStreamedContent_WordDocx() throws Exception {
    String fileName = TEST_FILE_NAME.concat(DOCX_EXTENSION);
    var doc = new com.aspose.words.Document();
    doc.getFirstSection().getBody().appendParagraph("Test Word");
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    doc.save(out, com.aspose.words.SaveFormat.DOCX);

    DocumentPreview preview = new DocumentPreview(fileName, WORD_CONTENT_TYPE, out.toByteArray());
    StreamedContent content = DocumentPreviewService.generateStreamedContent(preview);

    assertNotNull(content);
    assertEquals(fileName, content.getName());
    assertEquals(PDF_CONTENT_TYPE, content.getContentType());
  }

  @Test
  void testGenerateStreamedContent_Eml() throws Exception {
    String fileName = TEST_FILE_NAME.concat(EML_EXTENSION);
    var mail = new com.aspose.email.MailMessage();
    mail.setSubject("Test Mail");
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    mail.save(out, com.aspose.email.SaveOptions.getDefaultEml());

    DocumentPreview preview = new DocumentPreview(fileName, EML_CONTENT_TYPE, out.toByteArray());
    StreamedContent content = DocumentPreviewService.generateStreamedContent(preview);
    mail.close();
    assertNotNull(content);
    assertEquals(fileName, content.getName());
    assertEquals(PDF_CONTENT_TYPE, content.getContentType());
  }

  @Test
  void testGenerateStreamedContent_UnsupportedFile() throws Exception {
    String fileName = TEST_FILE_NAME.concat(TXT_EXTENSION);
    byte[] dummy = "Just a text".getBytes();
    DocumentPreview preview = new DocumentPreview(fileName, TEXT_CONTENT_TYPE, dummy);
    StreamedContent content = DocumentPreviewService.generateStreamedContent(preview);

    assertNotNull(content);
    assertEquals(fileName, content.getName());
    assertEquals(TEXT_CONTENT_TYPE, content.getContentType());
  }
}
