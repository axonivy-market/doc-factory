package ch.ivyteam.ivy.addons.docfactory.aspose;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.aspose.pdf.Document;

import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.addons.docfactory.MyIvyScriptObjectEnvironment;
import ch.ivyteam.ivy.addons.docfactory.PdfFactory;
import ch.ivyteam.ivy.cm.IContentManagementSystem;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;
import ch.ivyteam.ivy.scripting.objects.util.IvyScriptObjectEnvironment;
import ch.ivyteam.log.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Ivy.class, ThirdPartyLicenses.class, File.class, LicenseLoader.class, IvyScriptObjectEnvironment.class, Document.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.management.*", "com.sun.org.apache.xerces.*", 
	"javax.xml.*", "org.xml.*", "org.w3c.dom.*"}) 
public class PdfFactoryTest {
	
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
		Logger mockLogger = mock(Logger.class);
		doNothing().when(mockLogger).error(any(String.class));
		doNothing().when(mockLogger).info(any(String.class));
		doNothing().when(mockLogger).debug(any(String.class));

		IContentManagementSystem mockedCms = mock(IContentManagementSystem.class);
		when(mockedCms.co(any(String.class))).thenReturn("");

		mockStatic(ThirdPartyLicenses.class);
		mockStatic(Ivy.class);
		mockStatic(LicenseLoader.class);
		mockStatic(IvyScriptObjectEnvironment.class);
		
		when(Ivy.log()).thenReturn(mockLogger);
		when(Ivy.cms()).thenReturn(mockedCms);
		when(ThirdPartyLicenses.getDocumentFactoryLicense()).thenReturn(null);
		when(IvyScriptObjectEnvironment.getIvyScriptObjectEnvironment()).thenReturn(new MyIvyScriptObjectEnvironment());
		
		f = mock(File.class);   
		when(f.getAbsolutePath()).thenReturn("test/bigPdf.pdf");
		PowerMockito.whenNew(File.class).withArguments("bigPdf.pdf", true).thenReturn(f);
	}

	@Test
	public void get() {
		assertThat(PdfFactory.get(), instanceOf(PdfFactory.class));
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
