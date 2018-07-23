package ch.ivyteam.ivy.addons.docfactory;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;

public class DocumentTemplateWithDocumentCreationOptionsTest extends DocFactoryTest {
	
	File template;
	
	@Before
	public void setup() throws Exception {
		super.setup();
		template = new File(this.getClass().getResource("template_with_field_form.docx").toURI().getPath());
	}

	@Test
	public void default_documentTemplate_DocumentCreationOptions_pdfFormFields_not_editable() {
		DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template);
		
		DocumentCreationOptions documentCreationOptions = documentTemplate.getDocumentCreationOptions();
		
		assertThat(documentCreationOptions.isKeepFormFieldsEditableInPdf(), is(false));
	}
	
	@Test
	public void default_documentTemplate_DocumentCreationOptions_backed_in_documentFactory() {
		DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template);
		
		DocumentCreationOptions documentCreationOptions = documentTemplate.getDocumentCreationOptions();
		
		assertThat(documentTemplate.getDocumentFactory().documentCreationOptions, is(documentCreationOptions));
	}
	
	@Test
	public void set_documentTemplate_null_DocumentCreationOptions_throws_IAE() {
		thrown.expect(IllegalArgumentException.class);
		DocumentTemplate.withTemplate(template).withDocumentCreationOptions(null);
	}
	
	@Test
	public void set_documentTemplate_DocumentCreationOptions() {
		
		DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template).
				withDocumentCreationOptions(DocumentCreationOptions.getInstance().keepFormFieldsEditableInPdf(false));
		
		DocumentCreationOptions documentCreationOptions = documentTemplate.getDocumentCreationOptions();
		
		assertThat(documentCreationOptions.isKeepFormFieldsEditableInPdf(), is(false));
	}
	
	@Test
	public void documentTemplate_set_DocumentCreationOptions_backed_in_documentFactory() {
		DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template).
				withDocumentCreationOptions(DocumentCreationOptions.getInstance().keepFormFieldsEditableInPdf(false));
		
		DocumentCreationOptions documentCreationOptions = documentTemplate.getDocumentCreationOptions();
		
		assertThat(documentTemplate.getDocumentFactory().documentCreationOptions, is(documentCreationOptions));
	}
	
	
	
	

}
