package ch.ivyteam.ivy.addons.docfactory.aspose;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.aspose.pdf.Document;

import ch.ivyteam.ivy.addons.docfactory.DocFactoryTest;
import ch.ivyteam.ivy.addons.docfactory.PdfFactory;
import ch.ivyteam.ivy.scripting.objects.File;

@PrepareForTest(Document.class)
public class PdfFactoryTest extends DocFactoryTest {
	
	@Mock
	File f;
	
	private static java.io.File PDF1;
	private static java.io.File PDF2;
	
	@BeforeClass
	public static void setupClass() throws URISyntaxException {
		PDF1 = new java.io.File(PdfFactoryTest.class.getResource("../resources/files/pdf1.pdf").toURI().getPath());
		PDF2 = new java.io.File(PdfFactoryTest.class.getResource("../resources/files/pdf2.pdf").toURI().getPath());
	}
	
	@Before
	public void setup() throws Exception {
		super.setup();
		
		f = mock(File.class);   
		when(f.getAbsolutePath()).thenReturn("test/bigPdf.pdf");
		PowerMockito.whenNew(File.class).withArguments("bigPdf.pdf", true).thenReturn(f);
	}

	@Test
	public void get() {
		assertThat(PdfFactory.get(), instanceOf(PdfFactory.class));
	}

	@Test
	public void appendPdfFiles() throws Exception {
		List<java.io.File> filesToAppend = new ArrayList<>();
		filesToAppend.add(PDF1);
		filesToAppend.add(PDF2);
		
		java.io.File jf = new java.io.File(f.getAbsolutePath());
		if(jf.isFile()) {
			jf.delete();
		}
		jf.createNewFile();
		
		java.io.File result = PdfFactory.get().appendPdfFiles("appended_pdf_files.pdf", filesToAppend).getJavaFile();
		assertTrue(result.isFile());
	}
	
	@Test
	public void close_is_called_on_leadingDocument() throws URISyntaxException, IOException{
		List<java.io.File> filesToAppend = new ArrayList<>();
		filesToAppend.add(PDF1);
		filesToAppend.add(PDF2);
		
		java.io.File jf = new java.io.File(f.getAbsolutePath());
		if(jf.isFile()) {
			jf.delete();
		}
		jf.createNewFile();
		
		AsposePdfFactory factory = (AsposePdfFactory) PdfFactory.get();
		Document leadingDocument = mock(Document.class);
		
		Document baseDocument = new Document(new FileInputStream(PDF2));
		when(leadingDocument.getPages()).thenReturn(baseDocument.getPages());
		
		factory.appendFilesToDocument(leadingDocument, filesToAppend, "appended_pdf_files.pdf");
		
		verify(leadingDocument).close();
	}
	
	@Test
	public void close_is_called_on_appendedDocument() throws FileNotFoundException, URISyntaxException{
		AsposePdfFactory factory = (AsposePdfFactory) PdfFactory.get();
		Document pdfDocument = mock(Document.class);
		
		Document baseDocument = new Document(new FileInputStream(PDF2));
		when(pdfDocument.getPages()).thenReturn(baseDocument.getPages());
		
		factory.appendPdfDocuments(new Document(new FileInputStream(PDF2)), pdfDocument);
		
		verify(pdfDocument).close();
	}

}
