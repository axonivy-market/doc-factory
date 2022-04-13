package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;

public class DocumentTemplateWithDocumentCreationOptionsIT extends DocFactoryTest {

	File template;

	@Override
  @Before
	public void setup() throws Exception {
		super.setup();
		template = new File(this.getClass().getResource(TEMPLATE_WITH_FIELDS_FORM_DOCX).toURI().getPath());
	}

	@Test
	public void default_produces_fieldForm_not_editablePDF() {
		DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template).
				putDataAsSourceForMailMerge(makePerson());

		File resultFile = makeFile("test/documentCreationOptions/default_field_form_not_editable.pdf");
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));

	}

	@Test
	public void with_documentCreationOptions_producing_fieldForm_editablePDF() {
		@SuppressWarnings("deprecation")
    DocumentCreationOptions options = DocumentCreationOptions.getInstance().keepFormFieldsEditableInPdf(true);

		DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template).
				putDataAsSourceForMailMerge(makePerson()).
				withDocumentCreationOptions(options); // Set the DocumentCreationOptions

		File resultFile = makeFile("test/documentCreationOptions/field_form_editable.pdf");
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));

	}

	@Test
	public void with_documentCreationOptions_producing_fieldForm_not_editablePDF() {
		@SuppressWarnings("deprecation")
    DocumentCreationOptions options = DocumentCreationOptions.getInstance().keepFormFieldsEditableInPdf(false);

		DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(template).
				putDataAsSourceForMailMerge(makePerson()).withDocumentCreationOptions(options);

		File resultFile = makeFile("test/documentCreationOptions/field_form_not_editable.pdf");
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));

	}

}
