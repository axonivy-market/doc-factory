package ch.ivyteam.ivy.addons.docfactory.options;

import ch.ivyteam.api.API;

/**
 * The {@link MultipleDocumentsCreationOptions} is used in operations where
 * multiple documents are merged and are appended together. It defines options
 * like:
 * <ul>
 * <li>If each DocumentTemplate should produce its own file
 * ({@link MultipleDocumentsCreationOptions#createSingleFileForEachDocument(boolean)}
 * <li>If a file containing all the documents appended together should be
 * produced
 * ({@link MultipleDocumentsCreationOptions#createOneFileByAppendingAllTheDocuments(boolean)}
 * <li>The FileAppenderOptions containing the options for producing a file
 * appending other files or documents.
 * </ul>
 */
@SuppressWarnings("hiding")
public class MultipleDocumentsCreationOptions {

  private boolean createSingleFileForEachDocument = true;
  private boolean createOneFileByAppendingAllTheDocuments;
  private FileAppenderOptions fileAppenderOptions = FileAppenderOptions.getInstance();

  private MultipleDocumentsCreationOptions() {}

  public static MultipleDocumentsCreationOptions getInstance() {
    return new MultipleDocumentsCreationOptions();
  }

  /**
   * Used by the DocFactory in its
   * {@link ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateDocuments(java.util.List, MultipleDocumentsCreationOptions)}
   * method.
   * @param createSingleFileForEachDocument if true each given documentTemplate
   *          will generate one single document file. By default this is true.
   *          You can set it false, if you want to only generate a single file
   *          by appending all the Documents in one file. See
   *          {@link MultipleDocumentsCreationOptions#createOneFileByAppendingAllTheDocuments(boolean)}
   */
  public MultipleDocumentsCreationOptions createSingleFileForEachDocument(
          boolean createSingleFileForEachDocument) {
    this.createSingleFileForEachDocument = createSingleFileForEachDocument;
    return this;
  }

  /**
   * Used by the DocFactory in its
   * {@link ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateDocuments(java.util.List, MultipleDocumentsCreationOptions)}
   * method.
   * @param createOneFileByAppendingAllTheDocuments if true, the
   *          DocumentTemplates will be appended in one file which name is set
   *          it this DocumentAppenderOptions object by its method.
   */
  public MultipleDocumentsCreationOptions createOneFileByAppendingAllTheDocuments(
          boolean createOneFileByAppendingAllTheDocuments) {
    this.createOneFileByAppendingAllTheDocuments = createOneFileByAppendingAllTheDocuments;
    return this;
  }

  /**
   * Used by the DocFactory in its
   * {@link ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateDocuments(java.util.List, MultipleDocumentsCreationOptions)}
   * method. The FileAppenderOptions contains the options for producing a file
   * appending other files or documents.
   * @param fileAppenderOptions FileAppenderOptions contains the options for
   *          producing a file appending other files or documents. Cannot be
   *          null.
   */
  public MultipleDocumentsCreationOptions withFileAppenderOptions(FileAppenderOptions fileAppenderOptions) {
    this.setFileAppenderOptions(fileAppenderOptions);
    return this;
  }

  public boolean isCreateSingleFileForEachDocument() {
    return createSingleFileForEachDocument;
  }

  public void setCreateSingleFileForEachDocument(
          boolean createSingleFileForEachDocument) {
    this.createSingleFileForEachDocument = createSingleFileForEachDocument;
  }

  public boolean isCreateOneFileByAppendingAllTheDocuments() {
    return createOneFileByAppendingAllTheDocuments;
  }

  public void setCreateOneFileByAppendingAllTheDocuments(
          boolean createOneFileByAppendingAllTheDocuments) {
    this.createOneFileByAppendingAllTheDocuments = createOneFileByAppendingAllTheDocuments;
  }

  public FileAppenderOptions getFileAppenderOptions() {
    return fileAppenderOptions;
  }

  /**
   * The FileAppenderOptions contains the options for producing a file appending
   * other files or documents.
   * @param fileAppenderOptions FileAppenderOptions containing the options for
   *          producing a file appending other files or documents. Cannot be
   *          null.
   */
  public void setFileAppenderOptions(FileAppenderOptions fileAppenderOptions) {
    API.checkNotNull(fileAppenderOptions, "FileAppenderOptions fileAppenderOptions");
    this.fileAppenderOptions = fileAppenderOptions;
  }

}
