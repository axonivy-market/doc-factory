package ch.ivyteam.ivy.addons.docfactory.options;

public class DocumentCreationOptions {
	
	private boolean keepFormFieldsEditableInPdf;
	private boolean removeWhiteSpaceInPdfEditableFields;
	private boolean displayBooleanValuesAsCheckBox;
	private boolean removeBlankPages;
	
	private DocumentCreationOptions() {}
	
	public static DocumentCreationOptions getInstance() {
		DocumentCreationOptions documentCreationOptions  = new DocumentCreationOptions();
		return documentCreationOptions;
	}
	
	public DocumentCreationOptions keepFormFieldsEditableInPdf(boolean keepFormFieldsEditable) {
		this.keepFormFieldsEditableInPdf = keepFormFieldsEditable;
		return this;
	}
	
	public DocumentCreationOptions removeWhiteSpaceInPdfEditableFields(boolean removeWhiteSpaceInPdfEditableFields) {
		this.setRemoveWhiteSpaceInPdfEditableFields(removeWhiteSpaceInPdfEditableFields);
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

	public boolean isKeepFormFieldsEditableInPdf() {
		return keepFormFieldsEditableInPdf;
	}

	public void setKeepFormFieldsEditableInPdf(boolean keepFormFieldsEditable) {
		this.keepFormFieldsEditableInPdf = keepFormFieldsEditable;
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

	public boolean isRemoveWhiteSpaceInPdfEditableFields() {
		return removeWhiteSpaceInPdfEditableFields;
	}

	public void setRemoveWhiteSpaceInPdfEditableFields(
			boolean removeWhiteSpaceInPdfEditableFields) {
		this.removeWhiteSpaceInPdfEditableFields = removeWhiteSpaceInPdfEditableFields;
	}

	
}
