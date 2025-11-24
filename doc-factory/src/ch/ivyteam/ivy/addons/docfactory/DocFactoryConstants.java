package ch.ivyteam.ivy.addons.docfactory;

import java.util.List;
import java.util.Locale;

public final class DocFactoryConstants {

  public static final String DOC_EXTENSION = ".doc";
  public static final String DOCX_EXTENSION = ".docx";
  public static final String TXT_EXTENSION = ".txt";
  public static final String PDF_EXTENSION = ".pdf";
  public static final String HTML_EXTENSION = ".html";
  public static final String ODT_EXTENSION = ".odt";
  public static final String XLS_EXTENSION = ".xls";
  public static final String XLSX_EXTENSION = ".xlsx";
  public static final String EML_EXTENSION = ".eml";
  public static final String MSG_EXTENSION = ".msg";
  public static final String PDF_CONTENT_TYPE = "application/pdf";
  public static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  public static final String WORD_CONTENT_TYPE =
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
  public static final String EML_CONTENT_TYPE = "message/rfc822";
  public static final String TEXT_CONTENT_TYPE = "text/plain";

  public static final int UNSUPPORTED_FORMAT = -1;
  public static final int DOC_FORMAT = 0;
  public static final int DOCX_FORMAT = 1;
  public static final int HTML_FORMAT = 2;
  public static final int TXT_FORMAT = 3;
  public static final int PDF_FORMAT = 4;
  public static final int ODT_FORMAT = 5;

  public static final String IMAGE_MERGEFIELD_NAME_START = "image";
  public static final String EMBEDDED_DOCUMENT_MERGEFIELD_NAME_START = "document_";

  public static final Locale DEFAULT_LOCALE = Locale.getDefault();

  public static final String DEFAULT_FILE_NAME = "defaultDocFactoryFileName";


  public static final List<String> SUPPORTED_TYPES_FOR_PREVIEW =
      List.of(DOC_EXTENSION, DOCX_EXTENSION, XLS_EXTENSION, XLSX_EXTENSION, EML_EXTENSION, MSG_EXTENSION);
}