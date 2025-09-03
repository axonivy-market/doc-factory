package com.axonivy.utils.docfactory.preview;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeProduct;
import ch.ivyteam.ivy.addons.docfactory.aspose.LicenseLoader;

import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.PDF_CONTENT_TYPE;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.XLSX_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.TXT_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.DOCX_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.EML_EXTENSION;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.TEXT_CONTENT_TYPE;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.XLSX_CONTENT_TYPE;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.WORD_CONTENT_TYPE;
import static ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants.EML_CONTENT_TYPE;

public class DocumentPreviewServiceTest {
  private static final String TEST_FILE_NAME = "test";
  private static DocumentPreviewService documentPreviewService = DocumentPreviewService.getInstance();

  @BeforeAll
  @SuppressWarnings("unchecked")
  static void setup() throws Exception {
    Field field = LicenseLoader.class.getDeclaredField("LOADED_ASPOSE_LICENSES");
    field.setAccessible(true);
    Map<AsposeProduct, Object> licenses = (Map<AsposeProduct, Object>) field.get(null);
    licenses.put(AsposeProduct.CELLS, new Object());
    licenses.put(AsposeProduct.WORDS, new Object());
    licenses.put(AsposeProduct.EMAIL, new Object());
  }

  @Test
  void testGenerateStreamedContent_ExcelXlsx() throws Exception {
    File tempFile = File.createTempFile(TEST_FILE_NAME, XLSX_EXTENSION);
    StreamedContent content = documentPreviewService.generateStreamedContent(tempFile);

    assertNotNull(content);
    assertTrue(content.getName().contains(TEST_FILE_NAME));
    assertEquals(PDF_CONTENT_TYPE, content.getContentType());
    tempFile.deleteOnExit();
  }

  @Test
  void testGenerateStreamedContent_WordDocx() throws Exception {
    File tempFile = File.createTempFile(TEST_FILE_NAME, DOCX_EXTENSION);
    StreamedContent content = documentPreviewService.generateStreamedContent(tempFile);
    assertNotNull(content);
    assertTrue(content.getName().contains(TEST_FILE_NAME));
    assertEquals(PDF_CONTENT_TYPE, content.getContentType());
    tempFile.deleteOnExit();
  }

  @Test
  void testGenerateStreamedContent_Eml() throws Exception {
    File tempFile = File.createTempFile(TEST_FILE_NAME, EML_EXTENSION);
    StreamedContent content = documentPreviewService.generateStreamedContent(tempFile);
    assertNotNull(content);
    assertTrue(content.getName().contains(TEST_FILE_NAME));
    assertEquals(PDF_CONTENT_TYPE, content.getContentType());
    tempFile.deleteOnExit();
  }

  @Test
  void testGenerateStreamedContent_UnsupportedFile() throws Exception {
    File tempFile = File.createTempFile(TEST_FILE_NAME, TXT_EXTENSION);
    StreamedContent content = documentPreviewService.generateStreamedContent(tempFile);

    assertNotNull(content);
    assertTrue(content.getName().contains(TEST_FILE_NAME));
    assertEquals(TEXT_CONTENT_TYPE, content.getContentType());
    tempFile.deleteOnExit();
  }



  @Test
  void testGenerateStreamedContent_FromStreamedContent_Xlsx() throws Exception {
    String fileName = TEST_FILE_NAME.concat(XLSX_EXTENSION);
    byte[] dummyContent = "excel".getBytes();
    StreamedContent input = mockStreamContent(fileName, XLSX_CONTENT_TYPE, dummyContent);

    StreamedContent result = documentPreviewService.generateStreamedContent(input);

    assertNotNull(result);
    assertEquals(fileName, result.getName());
    assertEquals(PDF_CONTENT_TYPE, result.getContentType());
  }

  @Test
  void testGenerateStreamedContent_FromStreamedContent_Docx() throws Exception {
    String fileName = TEST_FILE_NAME.concat(DOCX_EXTENSION);
    byte[] dummyContent = "word".getBytes();
    StreamedContent input = mockStreamContent(fileName, WORD_CONTENT_TYPE, dummyContent);

    StreamedContent result = documentPreviewService.generateStreamedContent(input);

    assertNotNull(result);
    assertEquals(fileName, result.getName());
    assertEquals(PDF_CONTENT_TYPE, result.getContentType());
  }

  @Test
  void testGenerateStreamedContent_FromStreamedContent_Eml() throws Exception {
    String fileName = TEST_FILE_NAME.concat(EML_EXTENSION);
    byte[] dummyContent = "mail".getBytes();
    StreamedContent input = mockStreamContent(fileName, EML_CONTENT_TYPE, dummyContent);

    StreamedContent result = documentPreviewService.generateStreamedContent(input);

    assertNotNull(result);
    assertEquals(fileName, result.getName());
    assertEquals(PDF_CONTENT_TYPE, result.getContentType());
  }

  @Test
  void testGenerateStreamedContent_FromStreamedContent_Unsupported() throws Exception {
    String fileName = TEST_FILE_NAME.concat(TXT_EXTENSION);
    byte[] dummyContent = "text".getBytes();
    StreamedContent input = mockStreamContent(fileName, TEXT_CONTENT_TYPE, dummyContent);

    StreamedContent result = documentPreviewService.generateStreamedContent(input);

    assertNotNull(result);
    assertEquals(fileName, result.getName());
    assertEquals(TEXT_CONTENT_TYPE, result.getContentType());
  }

  private StreamedContent mockStreamContent(String fileName, String contentType, byte[] data) {
    return DefaultStreamedContent.builder().name(fileName).contentType(contentType)
        .stream(() -> new ByteArrayInputStream(data)).build();
  }
}