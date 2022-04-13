package ch.ivyteam.ivy.addons.docfactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import org.junit.Test;

import ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker;
import ch.ivyteam.ivy.addons.docfactory.exception.DocumentGenerationException;
import ch.ivyteam.ivy.addons.docfactory.test.data.Person;
import ch.ivyteam.ivy.scripting.objects.CompositeObject;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.scripting.objects.Recordset;
import ch.ivyteam.ivy.scripting.objects.Tree;
import ivyAddons_tests.Data;

public class DocumentTemplateTest extends DocFactoryTest {

	private static final String OUTPUT_FORMAT = ".pdf";
	private static final String OUTPUT_FILENAME = "output_filename";
	private static final String OUTPUT_PATH = "/outputpath";
	private static final String TEMPLATE_PATH = "resources/template.docx";

	private DocumentWorker documentWorker = new PageColorDocumentWorker();

	@Test
	public void new_documentTemplate_with_empty_constructor_docFactory_not_null(){
		DocumentTemplate documentTemplate = new DocumentTemplate().
				withDocumentWorker(documentWorker);

		assertThat(documentTemplate.getDocumentFactory(), notNullValue());
		assertThat(documentTemplate.getDocumentFactory().getDocumentWorker(), is(documentWorker));
	}

	@Test
	public void new_documentTemplate_with_dataClass_param_docFactory_not_null(){
		DocumentTemplate documentTemplate = new DocumentTemplate(TEMPLATE_PATH, OUTPUT_PATH, OUTPUT_FILENAME, OUTPUT_FORMAT, new Data()).
				withDocumentWorker(documentWorker);

		assertThat(documentTemplate.getDocumentFactory(), notNullValue());
		assertThat(documentTemplate.getDocumentFactory().getDocumentWorker(), is(documentWorker));
	}

	@Test
	public void new_documentTemplate_with_List_param_docFactory_not_null(){
		DocumentTemplate documentTemplate = new DocumentTemplate(TEMPLATE_PATH, OUTPUT_PATH, OUTPUT_FILENAME, OUTPUT_FORMAT, List.create(TemplateMergeField.class)).
				withDocumentWorker(documentWorker);

		assertThat(documentTemplate.getDocumentFactory(), notNullValue());
		assertThat(documentTemplate.getDocumentFactory().getDocumentWorker(), is(documentWorker));
	}

	@Test
	public void new_documentTemplate_with_TreeData_param_docFactory_not_null(){
		DocumentTemplate documentTemplate = new DocumentTemplate(TEMPLATE_PATH, OUTPUT_PATH, OUTPUT_FILENAME, OUTPUT_FORMAT, new Tree()).
				withDocumentWorker(documentWorker);

		assertThat(documentTemplate.getDocumentFactory(), notNullValue());
		assertThat(documentTemplate.getDocumentFactory().getDocumentWorker(), is(documentWorker));
	}

	@Test
	public void new_documentTemplate_with_HashMapDataClass_param_docFactory_not_null(){
		DocumentTemplate documentTemplate = new DocumentTemplate(TEMPLATE_PATH, OUTPUT_PATH, OUTPUT_FILENAME, OUTPUT_FORMAT, new Data(),
				new HashMap<String, java.util.List<CompositeObject>>()).
				withDocumentWorker(documentWorker);

		assertThat(documentTemplate.getDocumentFactory(), notNullValue());
		assertThat(documentTemplate.getDocumentFactory().getDocumentWorker(), is(documentWorker));
	}

	@Test
	public void new_documentTemplate_with_HashtableRecordSet_param_docFactory_not_null(){
		DocumentTemplate documentTemplate = new DocumentTemplate(TEMPLATE_PATH, OUTPUT_PATH, OUTPUT_FILENAME, OUTPUT_FORMAT, new Data(),
				new Hashtable<String , Recordset>()).
				withDocumentWorker(documentWorker);

		assertThat(documentTemplate.getDocumentFactory(), notNullValue());
		assertThat(documentTemplate.getDocumentFactory().getDocumentWorker(), is(documentWorker));
	}

	@Test
	public void withTemplate_null_throws_IAE() {
		thrown.expect(IllegalArgumentException.class);
		DocumentTemplate.withTemplate(null);
	}

	@Test
	public void withTemplate_returns_DocumentTemplate_if_template_not_null() {
		DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(new File("path/to/template.doc"));

		assertNotNull(documentTemplate);
	}

	@Test
	public void putDataAsSourceForMailMerge_throws_IAE_if_data_null() {
		DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(new File("path/to/template.doc"));

		thrown.expect(IllegalArgumentException.class);
		documentTemplate.putDataAsSourceForMailMerge(null);
	}

	@Test
	public void putDataAsSourceForMailMerge_with_data_fills_mergeFields() {
		DocumentTemplate documentTemplate = DocumentTemplate.withTemplate(new File("path/to/template.doc"));

		documentTemplate.putDataAsSourceForSimpleMailMerge(makePerson());

		assertThat(documentTemplate.getMergeFields(), hasSize(12));
	}

	@Test
	public void produceDocument_throws_DocumentGenerationException_if_template_does_not_exist() {
		File template = new File("path/to/template.doc");

		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				useLocale(Locale.forLanguageTag("de-CH"));

		File resultFile = makeFile("test/documentTemplate/mail_merge_without_template.pdf");

		thrown.expect(DocumentGenerationException.class);
		documentTemplate.produceDocument(resultFile);
	}

	@Test
	public void produceDocument_with_serializable_produces_document() throws URISyntaxException {
		File template = new File(this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());

		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				useLocale(Locale.forLanguageTag("de-CH"));

		File resultFile = makeFile("test/documentTemplate/simple_mail_merge_test.pdf");
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

	@Test
	public void produceDocument_with_some_HTML_formatted_text() throws URISyntaxException {
		File template = new File(this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());

		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForSimpleMailMerge(makePersonWithHTML()).
				useLocale(Locale.forLanguageTag("de-CH"));

		File resultFile = makeFile("test/documentTemplate/simple_mail_merge_with_html_input.pdf");
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

	@Test
	public void produceDocument_with_some_CSS_formatted_text() throws URISyntaxException {
		File template = new File(this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());

		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForSimpleMailMerge(makePersonWithHTMLFormattedInCssFile()).
				useLocale(Locale.forLanguageTag("de-CH"));

		File resultFile = makeFile("test/documentTemplate/simple_mail_merge_with_html_and_css_input.pdf");
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

	@Test
	public void produceDocument_with_file_to_insert() throws URISyntaxException {
		File template = new File(this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());
		File otherDocumentToInject = new File(this.getClass().getResource("resources/files/another_doc.doc").toURI().getPath());

		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				addMergeField("document_person.paySlip", otherDocumentToInject).
				useLocale(Locale.GERMAN);

		File resultFile = makeFile("test/documentTemplate/mergefield_getting_external_document.pdf");
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

	@Test
	public void produceDocument_with_DataTable() throws Exception {
		File template = new File(this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());

		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				putTableDataForMergingATableInDocument(makeDataTable()).
				useLocale(Locale.GERMAN);

		File resultFile = makeFile("test/documentTemplate/simple_mailmerge_and_datatable.pdf");
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

	@Test
	public void produceDocument_with_DataTable_as_object_ignored() throws Exception {
		File template = new File(this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());

		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				putTableDataForMergingATableInDocument(Person.emptyPerson()).
				useLocale(Locale.GERMAN);

		File resultFile = makeFile("test/documentTemplate/simple_mailmerge_and_ignored_datatable.pdf");
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

}
