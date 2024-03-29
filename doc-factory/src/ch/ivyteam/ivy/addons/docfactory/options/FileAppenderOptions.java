package ch.ivyteam.ivy.addons.docfactory.options;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.BaseDocFactory;
import ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants;
import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.environment.Ivy;

/**
 * This class represents different options that may be useful for setting how
 * different files (See {@link ch.ivyteam.ivy.addons.docfactory.FileAppender})
 * or documents (See
 * {@link BaseDocFactory#generateDocuments(java.util.List, MultipleDocumentsCreationOptions)})
 * may be appended together in a common file.
 * @since version 7.3.0
 */
public class FileAppenderOptions {

  private String appendedSingleFileName = DocFactoryConstants.DEFAULT_FILE_NAME;
  private String appendedFileParentDirectoryPath;
  private DocumentAppendingStart documentAppendingStart = DocumentAppendingStart.NEW_PAGE;
  private int appendedFileFormat = DocFactoryConstants.PDF_FORMAT;
  private boolean useHeadersFootersFromLeadingPage = false;
  private boolean restartPageNumbering = false;

  private FileAppenderOptions() {}

  public static FileAppenderOptions getInstance() {
    return new FileAppenderOptions();
  }

  /**
   * Used by the DocFactory in its
   * BaseDocFactory#generateDocument(MultipleDocumentsCreationOptions,
   * ch.ivyteam.ivy.addons.docfactory.DocumentTemplate...) method. Set the file
   * name of the file which will be produced by appending a bunch of other
   * documents or files.<br />
   * Note: if you don't set this property, a default file name will be used for
   * producing the appended file (see
   * {@link DocFactoryConstants#DEFAULT_FILE_NAME}.
   * @param fileName the file name of the file which will be
   *          produced by appending a bunch of other documents or files. Cannot
   *          be blank.
   */
  public FileAppenderOptions withAppendedFileName(String fileName) {
    API.checkNotEmpty(fileName, "fileName");
    this.appendedSingleFileName = fileName;
    return this;
  }

  /**
   * Defines the file output format. Must be between
   * {@link DocFactoryConstants#DOC_FORMAT} and
   * {@link DocFactoryConstants#ODT_FORMAT}
   * @param format the format to be set. By default if the format is not set the
   *          option object uses the PDF format.
   */
  public FileAppenderOptions withAppendedFileOutputFormat(int format) {
    API.checkParameterRange(format, "format", DocFactoryConstants.DOC_FORMAT, DocFactoryConstants.ODT_FORMAT);
    this.setAppendedFileFormat(format);
    return this;
  }

  public FileAppenderOptions withAppendedFileParentDirectoryPath(String outputDirectoryPath) {
    this.setAppendedFileParentDirectoryPath(outputDirectoryPath);
    return this;
  }

  /**
   * See {@linkplain <a href=
   * "https://support.office.com/en-us/article/add-different-page-numbers-or-number-formats-to-different-sections-bb4da2bd-1597-4b0c-9e91-620615ed8c05?ui=en-US&rs=en-US&ad=US">Microsoft
   * Office support article</a>} and {@linkplain <a href=
   * "https://docs.aspose.com/display/wordsjava/Joining+Documents+with+Headers+and+Footers">Aspose
   * documentation illustrating this point</a>} <br />
   * <b>Note: </b><u>Applies only for appending Words documents (doc, docx and
   * odt) together, or for appending documents at mail merge time</u>. Has no
   * effect if used for appending existing PDF files together.
   * @param restart true if you want that each appended document
   *          has its own page numbering. Else leave it false (default).
   */
  public FileAppenderOptions restartPageNumbering(boolean restart) {
    this.restartPageNumbering = restart;
    return this;
  }

  /**
   * See {@linkplain <a href=
   * "https://docs.aspose.com/display/wordsjava/Joining+Documents+with+Headers+and+Footers">Aspose
   * documentation illustrating this point</a>} <br />
   * <b>Note: </b><u>Applies only for appending Words documents (doc, docx and
   * odt) together, or for appending documents at mail merge time</u>. Has no
   * effect if used for appending existing PDF files together.
   * @param useLeading true if you want that each appended
   *          document uses the header and footer from leading page. Else leave
   *          it false (default).
   */
  public FileAppenderOptions useHeadersFootersFromLeadingPage(boolean useLeading) {
    this.useHeadersFootersFromLeadingPage = useLeading;
    return this;
  }

  /**
   * The DocumentAppendingStart enum sets the way the different document will be
   * appended to the first one. By default, each document are added as a new
   * page ({@link DocumentAppendingStart#NEW_PAGE}). The
   * {@link DocumentAppendingStart#CONTINUOUS} allows appending the documents
   * continuously without page break.
   * @param appendigStart
   */
  public FileAppenderOptions withDocumentAppendingStart(DocumentAppendingStart appendigStart) {
    this.documentAppendingStart = appendigStart;
    return this;
  }

  public String getAppendedSingleFileName() {
    return appendedSingleFileName;
  }

  public void setAppendedSingleFileName(String appenderSingleFileName) {
    this.appendedSingleFileName = appenderSingleFileName;
  }

  /**
   * Returns the file output format as int index. Will be between
   * {@link DocFactoryConstants#DOC_FORMAT} and
   * {@link DocFactoryConstants#ODT_FORMAT} By default it is set to
   * {@link DocFactoryConstants#PDF_FORMAT}
   */
  public int getAppendedFileOutputFormat() {
    return appendedFileFormat;
  }

  /**
   * Returns the file format as String representation corresponding to the
   * {@link #getAppendedFileOutputFormat()} int representation.
   */
  public String getAppendedFileFormat() {
    return "." + BaseDocFactory.getSupportedFormats()[appendedFileFormat];
  }

  /**
   * Defines the file output format. Must be between
   * {@link DocFactoryConstants#DOC_FORMAT} and
   * {@link DocFactoryConstants#ODT_FORMAT}
   * @param format the format to be set. By default if the format is not set the
   *          option object uses the PDF format.
   */
  public void setAppendedFileFormat(int format) {
    API.checkParameterRange(format, "format", DocFactoryConstants.DOC_FORMAT, DocFactoryConstants.ODT_FORMAT);
    this.appendedFileFormat = format;
  }

  /**
   * Return the path of the directory where the file resulting of appending the
   * documents together should be produced. If this property was not set, it
   * returns the Ivy Session files area of the current Ivy Session.
   * @return the path of the directory where the file resulting of appending the
   *         documents together should be produced.
   */
  public String getAppendedFileParentDirectoryPath() {
    if (StringUtils.isBlank(this.appendedFileParentDirectoryPath)) {
      return IApplication.current().getSessionFileArea(Ivy.session()).getAbsolutePath();
    }
    return appendedFileParentDirectoryPath;
  }

  /**
   * Returns the absolute path of the file which will be the result of the
   * appending of all the documents. Returns null if the appended file name (see
   * {@link #withAppendedFileName(String)} or
   * {@link #setAppendedSingleFileName(String)}) has not be set.
   * @return absolute path of the file which will be the result of the appending
   *         of all the documents. Null if the appended file name has not be
   *         set.
   */
  public String getAppendedFileAbsolutePath() {
    if (getAppendedSingleFileName() == null) {
      return null;
    }
    return new File(getAppendedFileParentDirectoryPath(),
            getAppendedSingleFileName() + getAppendedFileFormat()).getAbsolutePath();
  }

  /**
   * Similar to the {@link #getAppendedFileAbsolutePath()} but the given file
   * path does not contain the format ending like ".doc" or ".pdf".
   * @return absolute path without format of the file which will be the result
   *         of the appending of all the documents. Null if the appended file
   *         name has not be set.
   */
  public String getAppendedFileAbsolutePathWithoutFormatEnding() {
    if (getAppendedSingleFileName() == null) {
      return null;
    }
    return new File(getAppendedFileParentDirectoryPath(), getAppendedSingleFileName()).getAbsolutePath();
  }

  public void setAppendedFileParentDirectoryPath(String appendedFileParentDirectoryPath) {
    this.appendedFileParentDirectoryPath = appendedFileParentDirectoryPath;
  }

  /**
   * The DocumentAppendingStart enum sets the way the different document will be
   * appended to the first one. By default, each document are added as a new
   * page ({@link DocumentAppendingStart#NEW_PAGE}). The
   * {@link DocumentAppendingStart#CONTINUOUS} allows appending the documents
   * continuously without page break.
   * @return the DocumentAppendingStart (default is
   *         {@link DocumentAppendingStart#NEW_PAGE})
   */
  public DocumentAppendingStart getDocumentAppendingStart() {
    return documentAppendingStart;
  }

  /**
   * The DocumentAppendingStart enum sets the way the different document will be
   * appended to the first one. By default, each document are added as a new
   * page ({@link DocumentAppendingStart#NEW_PAGE}). The
   * {@link DocumentAppendingStart#CONTINUOUS} allows appending the documents
   * continuously without page break.
   * @param documentAppendingStart
   */
  public void setDocumentAppendingStart(DocumentAppendingStart documentAppendingStart) {
    this.documentAppendingStart = documentAppendingStart;
  }

  /**
   * See {@linkplain <a href=
   * "https://docs.aspose.com/display/wordsjava/Joining+Documents+with+Headers+and+Footers">Aspose
   * documentation illustrating this point</a>} <br />
   * <b>Note: </b><u>Applies only for appending existing Words documents (doc,
   * docx and odt) together, or for appending documents at mail merge time</u>.
   * Has no effect if used for appending existing PDF files together.
   * @return true if the footer and headers from leading document page are used
   *         in an appended document. Else false (default).
   */
  public boolean isUseHeadersFootersFromLeadingPage() {
    return useHeadersFootersFromLeadingPage;
  }

  /**
   * See {@linkplain <a href=
   * "https://docs.aspose.com/display/wordsjava/Joining+Documents+with+Headers+and+Footers">Aspose
   * documentation illustrating this point</a>} <br />
   * <b>Note: </b><u>Applies only for appending Words documents (doc, docx and
   * odt) together, or for appending documents at mail merge time</u>. Has no
   * effect if used for appending existing PDF files together.
   * @param useHeadersFootersFromLeadingPage
   */
  public void setUseHeadersFootersFromLeadingPage(
          boolean useHeadersFootersFromLeadingPage) {
    this.useHeadersFootersFromLeadingPage = useHeadersFootersFromLeadingPage;
  }

  /**
   * See {@linkplain <a href=
   * "https://support.office.com/en-us/article/add-different-page-numbers-or-number-formats-to-different-sections-bb4da2bd-1597-4b0c-9e91-620615ed8c05?ui=en-US&rs=en-US&ad=US">Microsoft
   * Office support article</a>} and {@linkplain <a href=
   * "https://docs.aspose.com/display/wordsjava/Joining+Documents+with+Headers+and+Footers">Aspose
   * documentation illustrating this point</a>} <br />
   * <b>Note: </b><u>Applies only for appending Words documents (doc, docx and
   * odt) together, or for appending documents at mail merge time</u>. Has no
   * effect if used for appending existing PDF files together.
   * @return true if the page numbering is restarted for each appended document.
   *         Else false.
   */
  public boolean isRestartPageNumbering() {
    return restartPageNumbering;
  }

  /**
   * If set to true See {@linkplain <a href=
   * "https://support.office.com/en-us/article/add-different-page-numbers-or-number-formats-to-different-sections-bb4da2bd-1597-4b0c-9e91-620615ed8c05?ui=en-US&rs=en-US&ad=US">Microsoft
   * Office support article</a>} and {@linkplain <a href=
   * "https://docs.aspose.com/display/wordsjava/Joining+Documents+with+Headers+and+Footers">Aspose
   * documentation illustrating this point</a>} <br />
   * <b>Note: </b><u>Applies only for appending Words documents (doc, docx and
   * odt) together, or for appending documents at mail merge time</u>. Has no
   * effect if used for appending existing PDF files together.
   * @param restartPageNumbering
   */
  public void setRestartPageNumbering(boolean restartPageNumbering) {
    this.restartPageNumbering = restartPageNumbering;
  }

}
