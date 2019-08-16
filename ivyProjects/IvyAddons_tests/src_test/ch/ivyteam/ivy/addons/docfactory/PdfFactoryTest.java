package ch.ivyteam.ivy.addons.docfactory;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

import ch.ivyteam.ivy.scripting.objects.File;

// tell powermock to ignore things different in java 11 
// see https://github.com/mockito/mockito/issues/1562
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "com.aspose.pdf.*"}) 
public class PdfFactoryTest extends DocFactoryTest {
	
	@Mock
	File f;
	
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
		java.io.File pdf1 = new java.io.File(PdfFactoryTest.class.getResource("resources/files/pdf1.pdf").toURI().getPath());
		java.io.File pdf2 = new java.io.File(PdfFactoryTest.class.getResource("resources/files/pdf2.pdf").toURI().getPath());
		
		List<java.io.File> filesToAppend = new ArrayList<>();
		filesToAppend.add(pdf1);
		filesToAppend.add(pdf2);
		
		java.io.File jf = new java.io.File(f.getAbsolutePath());
		if(jf.isFile()) {
			jf.delete();
		}
		jf.createNewFile();
		
		java.io.File result = PdfFactory.get().appendPdfFiles("appended_pdf_files.pdf", filesToAppend).getJavaFile();
		assertTrue(result.isFile() );
	}

}
