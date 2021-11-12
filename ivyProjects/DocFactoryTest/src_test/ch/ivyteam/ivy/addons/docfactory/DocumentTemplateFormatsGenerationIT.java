package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class DocumentTemplateFormatsGenerationIT extends DocFactoryTest {
	
	File template;
	DocumentTemplate documentTemplate;
	
	@Before
	public void setup() throws Exception {
		super.setup();
		File template = new File(this.getClass().getResource(TEMPLATE_PERSON_DOCX).toURI().getPath());
		
		documentTemplate = DocumentTemplate.
				withTemplate(template).
				putDataAsSourceForSimpleMailMerge(makePerson()).
				useLocale(Locale.forLanguageTag("de-CH"));
	}

		

	@Test
	public void produceDocument_html() throws URISyntaxException {
		documentTemplate.setOutputFormat("html");

		File resultFile = makeFile("test/documentTemplate/formats/simple_mail_merge_test.html");
		
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void produceDocument_doc() throws URISyntaxException {
		documentTemplate.setOutputFormat("doc");

		File resultFile = makeFile("test/documentTemplate/formats/simple_mail_merge_test.doc");
		
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void produceDocument_docx() throws URISyntaxException {
		documentTemplate.setOutputFormat("docx");

		File resultFile = makeFile("test/documentTemplate/formats/simple_mail_merge_test.docx");
		
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void produceDocument_odt() throws URISyntaxException {
		documentTemplate.setOutputFormat("odt");

		File resultFile = makeFile("test/documentTemplate/formats/simple_mail_merge_test.odt");
		
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void produceDocument_txt() throws URISyntaxException {
		documentTemplate.setOutputFormat("txt");

		File resultFile = makeFile("test/documentTemplate/formats/simple_mail_merge_test.txt");
		
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void produceDocument_pdf() throws URISyntaxException {
		documentTemplate.setOutputFormat("pdf");

		File resultFile = makeFile("test/documentTemplate/formats/simple_mail_merge_test.pdf");
		
		FileOperationMessage result = documentTemplate.produceDocument(resultFile);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

}
