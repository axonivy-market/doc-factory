package ch.ivyteam.ivy.addons.docfactory.options;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.pdf.PdfOptions;

public class DocumentCreationOptions {

  private PdfOptions pdfOptions = PdfOptions.getInstance();
  private boolean displayBooleanValuesAsCheckBox;
  private boolean removeBlankPages;

  private DocumentCreationOptions() {}

  public static DocumentCreationOptions getInstance() {
    DocumentCreationOptions documentCreationOptions = new DocumentCreationOptions();
    return documentCreationOptions;
  }

  /**
   * returns the DocumentCreationOptions which {@link PdfOptions} has been set.
   * @param options the {@link PdfOptions} to set. Cannot be null.
   */
  public DocumentCreationOptions withPdfOptions(PdfOptions options) {
    API.checkNotNull(options, "options");
    this.pdfOptions = options;
    return this;
  }

  public DocumentCreationOptions displayBooleanValuesAsCheckBox(boolean checkbox) {
    this.displayBooleanValuesAsCheckBox = checkbox;
    return this;
  }

  public DocumentCreationOptions removeBlankPages(boolean remove) {
    this.removeBlankPages = remove;
    return this;
  }

  public boolean isDisplayBooleanValuesAsCheckBox() {
    return displayBooleanValuesAsCheckBox;
  }

  public void setDisplayBooleanValuesAsCheckBox(
          boolean displayBooleanValuesAsCheckBox) {
    this.displayBooleanValuesAsCheckBox = displayBooleanValuesAsCheckBox;
  }

  public boolean isRemoveBlankPages() {
    return removeBlankPages;
  }

  public void setRemoveBlankPages(boolean removeBlankPages) {
    this.removeBlankPages = removeBlankPages;
  }

  public PdfOptions getPdfOptions() {
    return this.pdfOptions;
  }

  /**
   * @param pdfOptions the {@link PdfOptions} to set. Cannot be null.
   */
  public void setPdfOptions(PdfOptions pdfOptions) {
    API.checkNotNull(pdfOptions, "the pdfOptions");
    this.pdfOptions = pdfOptions;
  }

}
