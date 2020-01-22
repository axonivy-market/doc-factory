package ch.ivyteam.ivy.addons.docfactory;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
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
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	private java.io.File path;
	
	@Before
	public void setup() throws Exception {
		super.setup();
		
		path = tempFolder.newFile("bigPdf.pdf");
		
		f = mock(File.class);   
		when(f.getAbsolutePath()).thenReturn(path.getAbsolutePath());
		PowerMockito.whenNew(File.class).withArguments(path.getName(), true).thenReturn(f);
	}

	@Test
	public void get() {
		assertThat(PdfFactory.get(), instanceOf(PdfFactory.class));
	}

	@Test
	public void appendPdfFiles() throws Exception {
		List<java.io.File> filesToAppend = new ArrayList<>();
		filesToAppend.add(fromResource("resources/files/pdf1.pdf"));
		filesToAppend.add(fromResource("resources/files/pdf2.pdf"));

		java.io.File result = PdfFactory.get().appendPdfFiles("appended_pdf_files.pdf", filesToAppend).getJavaFile();
		assertTrue(result.isFile());
	}
	
	private java.io.File fromResource(String classRelativePath) throws IOException
	{
		java.io.File ref = new java.io.File(classRelativePath);
		java.io.File resource = tempFolder.newFile(ref.getName());
		try(InputStream is = PdfFactoryTest.class.getResourceAsStream(classRelativePath);
			OutputStream os = new FileOutputStream(resource))
		{
			IOUtils.copy(is, os);
		}
		return resource;
	}

}
