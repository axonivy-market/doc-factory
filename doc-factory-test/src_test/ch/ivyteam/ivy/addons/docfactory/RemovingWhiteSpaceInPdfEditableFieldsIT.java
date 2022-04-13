package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;

@SuppressWarnings("deprecation")
public class RemovingWhiteSpaceInPdfEditableFieldsIT extends DocFactoryTest {

	File template;
	DocumentTemplate documentTemplate;

	@Override
  @Before
	public void setup() throws Exception {
		super.setup();
		File tpl = new File(this.getClass().getResource(TEMPLATE_FOR_TESTING_LONG_EDITABLE_FIELDS_DOCX).toURI().getPath());

		documentTemplate = DocumentTemplate.
				withTemplate(tpl).
				useLocale(Locale.forLanguageTag("de-CH")).
				putDataAsSourceForMailMerge(makePerson());
	}

	@Test
	public void defaultDoesNotRemoveWhiteSpaceInPdfEditableFields() {
		documentTemplate.withDocumentCreationOptions(
				DocumentCreationOptions.getInstance()
				.keepFormFieldsEditableInPdf(true));

		File resultFile = makeFile("test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/default_not_remove_fieldSpaces.pdf");

		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

	@Test
	public void removeWhiteSpaceInPdfEditableFields() {
		documentTemplate.withDocumentCreationOptions(
				DocumentCreationOptions.getInstance()
				.keepFormFieldsEditableInPdf(true)
				.removeWhiteSpaceInPdfEditableFields(true)
		);

		File resultFile = makeFile("test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/remove_fieldSpaces.pdf");

		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

	@Test
	public void removeWhiteSpaceInPdfEditableFields_has_no_effect_if_fieldsNotEditable() {
		documentTemplate.withDocumentCreationOptions(
				DocumentCreationOptions.getInstance()
				.keepFormFieldsEditableInPdf(false)
				.removeWhiteSpaceInPdfEditableFields(true)
		);

		File resultFile = makeFile("test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/fieldsNotEditables.pdf");

		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

  @Test
	public void removeWhiteSpaceInPdfEditableFields_has_no_effect_if_output_is_doc() {
		documentTemplate.withDocumentCreationOptions(
				DocumentCreationOptions.getInstance()
				.keepFormFieldsEditableInPdf(true)
				.removeWhiteSpaceInPdfEditableFields(true)
		);

		File resultFile = makeFile("test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/aDoc.doc");

		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

	@Test
	public void removeWhiteSpaceInPdfEditableFields_has_no_effect_if_output_is_docx() {
		documentTemplate.withDocumentCreationOptions(
				DocumentCreationOptions.getInstance()
				.keepFormFieldsEditableInPdf(true)
				.removeWhiteSpaceInPdfEditableFields(true)
		);

		File resultFile = makeFile("test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/aDocx.docx");

		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

	@Test
	public void removeWhiteSpaceInPdfEditableFields_has_no_effect_if_output_is_odt() {
		documentTemplate.withDocumentCreationOptions(
				DocumentCreationOptions.getInstance()
				.keepFormFieldsEditableInPdf(true)
				.removeWhiteSpaceInPdfEditableFields(true)
		);

		File resultFile = makeFile("test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/anOdt.odt");

		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

	@Test
	public void removeWhiteSpaceInPdfEditableFields_has_no_effect_if_output_is_html() {
		documentTemplate.withDocumentCreationOptions(
				DocumentCreationOptions.getInstance()
				.keepFormFieldsEditableInPdf(true)
				.removeWhiteSpaceInPdfEditableFields(true)
		);

		File resultFile = makeFile("test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/anHtml.html");

		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

	@Test
	public void removeWhiteSpaceInPdfEditableFields_has_no_effect_if_output_is_txt() {
		documentTemplate.withDocumentCreationOptions(
				DocumentCreationOptions.getInstance()
				.keepFormFieldsEditableInPdf(true)
				.removeWhiteSpaceInPdfEditableFields(true)
		);

		File resultFile = makeFile("test/documentTemplate/removingWhiteSpaceInPdfEditableFieldsIT/aTxt.txt");

		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

}
