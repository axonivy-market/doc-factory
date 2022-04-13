package ch.ivyteam.ivy.addons.docfactory.options;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.pdf.PdfOptions;

@SuppressWarnings("hiding")
public class DocumentCreationOptions {

	private PdfOptions pdfOptions = PdfOptions.getInstance();
	private boolean displayBooleanValuesAsCheckBox;
	private boolean removeBlankPages;

	private DocumentCreationOptions() {}

	public static DocumentCreationOptions getInstance() {
		DocumentCreationOptions documentCreationOptions  = new DocumentCreationOptions();
		return documentCreationOptions;
	}

	/**
	 * returns the DocumentCreationOptions which {@link PdfOptions} has been set.
	 * @param pdfOptions the {@link PdfOptions} to set. Cannot be null.
	 */
  public DocumentCreationOptions withPdfOptions(PdfOptions pdfOptions){
		API.checkNotNull(pdfOptions, "the pdfOptions");
		this.pdfOptions = pdfOptions;
		return this;
	}

	/**
	 * @deprecated
	 * use {@link #withPdfOptions(PdfOptions)} instead. The {@link PdfOptions} contains this option now.
	 */
	@Deprecated
	public DocumentCreationOptions keepFormFieldsEditableInPdf(boolean keepFormFieldsEditable) {
		pdfOptions.setKeepFormFieldsEditableInPdf(keepFormFieldsEditable);
		return this;
	}

	/**
	 * @deprecated
	 * use {@link #withPdfOptions(PdfOptions)} instead. The {@link PdfOptions} contains this option now.
	 */
	@Deprecated
	public DocumentCreationOptions removeWhiteSpaceInPdfEditableFields(boolean removeWhiteSpaceInPdfEditableFields) {
		pdfOptions.setRemoveWhiteSpaceInPdfEditableFields(removeWhiteSpaceInPdfEditableFields);
		return this;
	}

	public DocumentCreationOptions displayBooleanValuesAsCheckBox(boolean displayBooleanValuesAsCheckBox) {
		this.displayBooleanValuesAsCheckBox = displayBooleanValuesAsCheckBox;
		return this;
	}

	public DocumentCreationOptions removeBlankPages(boolean removeBlankPages) {
		this.removeBlankPages = removeBlankPages;
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

	/**
	 * @deprecated
	 * use {@link #withPdfOptions(PdfOptions)} instead. The {@link PdfOptions} contains this option now.
	 */
	@Deprecated
  public boolean isRemoveWhiteSpaceInPdfEditableFields() {
		return pdfOptions.isRemoveWhiteSpaceInPdfEditableFields();
	}

	/**
	 * @deprecated
	 * use {@link #withPdfOptions(PdfOptions)} instead. The {@link PdfOptions} contains this option now.
	 */
	@Deprecated
  public void setRemoveWhiteSpaceInPdfEditableFields(
			boolean removeWhiteSpaceInPdfEditableFields) {
		this.pdfOptions.setRemoveWhiteSpaceInPdfEditableFields(removeWhiteSpaceInPdfEditableFields);
	}

	/**
	 * @deprecated
	 * use {@link #withPdfOptions(PdfOptions)} instead. The {@link PdfOptions} contains this option now.
	 */
	@Deprecated
  public boolean isKeepFormFieldsEditableInPdf() {
		return pdfOptions.isKeepFormFieldsEditableInPdf();
	}

	/**
	 * @deprecated
	 * use {@link #withPdfOptions(PdfOptions)} instead. The {@link PdfOptions} contains this option now.
	 */
	@Deprecated
  public void setKeepFormFieldsEditableInPdf(boolean keepFormFieldsEditable) {
		this.pdfOptions.setKeepFormFieldsEditableInPdf(keepFormFieldsEditable);
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
