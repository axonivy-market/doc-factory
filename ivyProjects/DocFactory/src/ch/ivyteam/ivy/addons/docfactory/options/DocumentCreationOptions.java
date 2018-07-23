package ch.ivyteam.ivy.addons.docfactory.options;

public class DocumentCreationOptions {
	
	private boolean keepFormFieldsEditableInPdf;
	
	private DocumentCreationOptions() {}
	
	public static DocumentCreationOptions getInstance() {
		DocumentCreationOptions documentCreationOptions  = new DocumentCreationOptions();
		return documentCreationOptions;
	}
	
	public DocumentCreationOptions keepFormFieldsEditableInPdf(boolean keepFormFieldsEditable) {
		this.keepFormFieldsEditableInPdf = keepFormFieldsEditable;
		return this;
	}

	public boolean isKeepFormFieldsEditableInPdf() {
		return keepFormFieldsEditableInPdf;
	}

	public void setKeepFormFieldsEditableInPdf(boolean keepFormFieldsEditable) {
		this.keepFormFieldsEditableInPdf = keepFormFieldsEditable;
	}

	
	
}
