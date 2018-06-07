package ch.ivyteam.ivy.addons.docfactory;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.addons.docfactory.aspose.LicenseLoader;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;
import ch.ivyteam.log.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Ivy.class, ThirdPartyLicenses.class, File.class, LicenseLoader.class})
public class PdfFactoryTest {
	
	@Mock
	File f;
	
	@Before
	public void setup() throws Exception {
		Logger mockLogger = mock(Logger.class);
		doNothing().when(mockLogger).error(any(String.class));
		doNothing().when(mockLogger).info(any(String.class));
		doNothing().when(mockLogger).debug(any(String.class));

		mockStatic(ThirdPartyLicenses.class);
		mockStatic(LicenseLoader.class);
		mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mockLogger);	
		when(ThirdPartyLicenses.getDocumentFactoryLicense()).thenReturn(null);
		
		f = mock(File.class);   
		when(f.getAbsolutePath()).thenReturn(System.getProperty("java.io.tmpdir") + "bigPdf.pdf");
		PowerMockito.whenNew(File.class).withArguments("bigPdf.pdf", true).thenReturn(f);
	}

	@Test
	public void get() {
		assertThat(PdfFactory.get(), instanceOf(PdfFactory.class));
	}

	//@Test
	public void appendPdfFiles() throws Exception {
		java.io.File pdf1 = new java.io.File(PdfFactoryTest.class.getResource("pdf1.pdf").toURI().getPath());
		java.io.File pdf2 = new java.io.File(PdfFactoryTest.class.getResource("pdf2.pdf").toURI().getPath());
		
		List<java.io.File> filesToAppend = new ArrayList<>();
		filesToAppend.add(pdf1);
		filesToAppend.add(pdf2);
		
		System.out.println(f.getAbsolutePath());
		java.io.File jf = new java.io.File(f.getAbsolutePath());
		if(jf.isFile()) {
			jf.delete();
		}
		jf.createNewFile();
		
		java.io.File result = PdfFactory.get().appendPdfFiles("bigPdf.pdf", filesToAppend).getJavaFile();
		assertTrue(result.isFile() );
	}

}
