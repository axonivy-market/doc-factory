package ch.ivyteam.ivy.addons.docfactory.options;

public class DocumentCreationOptions {
	
	private boolean keepFormFieldsEditableInPdf;
	private boolean displayBooleanValuesAsCheckBox;
	
	private DocumentCreationOptions() {}
	
	public static DocumentCreationOptions getInstance() {
		DocumentCreationOptions documentCreationOptions  = new DocumentCreationOptions();
		return documentCreationOptions;
	}
	
	public DocumentCreationOptions keepFormFieldsEditableInPdf(boolean keepFormFieldsEditable) {
		this.keepFormFieldsEditableInPdf = keepFormFieldsEditable;
		return this;
	}
	
	public DocumentCreationOptions displayBooleanValuesAsCheckBox(boolean displayBooleanValuesAsCheckBox) {
		this.displayBooleanValuesAsCheckBox = displayBooleanValuesAsCheckBox;
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

}
