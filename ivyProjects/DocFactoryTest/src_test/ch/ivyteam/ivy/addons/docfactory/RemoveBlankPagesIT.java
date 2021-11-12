package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;

public class RemoveBlankPagesIT extends DocFactoryTest {
	
	File template;
	DocumentTemplate documentTemplate;
	
	@Before
	public void setup() throws Exception {
		super.setup();
		File template = new File(this.getClass().getResource("resources/template_with_blank_pages.docx").toURI().getPath());
		
		documentTemplate = DocumentTemplate.
				withTemplate(template).
				useLocale(Locale.forLanguageTag("de-CH"));
	}
	
	@Test
	public void default_not_remove_blank_pages_pdf() {
		File resultFile = makeFile("test/documentTemplate/blankPages/default_not_remove_blank_pages.pdf");
		
		FileOperationMessage result = documentTemplate.
				putDataAsSourceForMailMerge(makePerson()).
				produceDocument(resultFile);
		
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void default_not_remove_blank_pages_docx() {
		File resultFile = makeFile("test/documentTemplate/blankPages/default_not_remove_blank_pages.docx");
		
		FileOperationMessage result = documentTemplate.
				putDataAsSourceForMailMerge(makePerson()).
				produceDocument(resultFile);
		
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void default_not_remove_blank_pages_doc() {
		File resultFile = makeFile("test/documentTemplate/blankPages/default_not_remove_blank_pages.doc");
		
		FileOperationMessage result = documentTemplate.
				putDataAsSourceForMailMerge(makePerson()).
				produceDocument(resultFile);
		
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void default_not_remove_blank_pages_odt() {
		File resultFile = makeFile("test/documentTemplate/blankPages/default_not_remove_blank_pages.odt");
		
		FileOperationMessage result = documentTemplate.
				putDataAsSourceForMailMerge(makePerson()).
				produceDocument(resultFile);
		
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void remove_blank_pages_pdf() {
		File resultFile = makeFile("test/documentTemplate/blankPages/remove_blank_pages.pdf");
		
		DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance().removeBlankPages(true);
		
		FileOperationMessage result = documentTemplate.
				withDocumentCreationOptions(documentCreationOptions).
				putDataAsSourceForMailMerge(makePerson()).
				produceDocument(resultFile);
		
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void remove_blank_pages_docx() {
		File resultFile = makeFile("test/documentTemplate/blankPages/remove_blank_pages.docx");
		
		DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance().removeBlankPages(true);
		
		FileOperationMessage result = documentTemplate.
				withDocumentCreationOptions(documentCreationOptions).
				putDataAsSourceForMailMerge(makePerson()).
				produceDocument(resultFile);
		
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void remove_blank_pages_doc() {
		File resultFile = makeFile("test/documentTemplate/blankPages/remove_blank_pages.doc");
		
		DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance().removeBlankPages(true);
		
		FileOperationMessage result = documentTemplate.
				withDocumentCreationOptions(documentCreationOptions).
				putDataAsSourceForMailMerge(makePerson()).
				produceDocument(resultFile);
		
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}
	
	@Test
	public void remove_blank_pages_odt() {
		File resultFile = makeFile("test/documentTemplate/blankPages/remove_blank_pages.odt");
		
		DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance().removeBlankPages(true);
		
		FileOperationMessage result = documentTemplate.
				withDocumentCreationOptions(documentCreationOptions).
				putDataAsSourceForMailMerge(makePerson()).
				produceDocument(resultFile);
		
		assertTrue(result.isSuccess());
		assertThat(result.getFiles(), org.hamcrest.core.IsCollectionContaining.hasItem(resultFile));
	}

}
