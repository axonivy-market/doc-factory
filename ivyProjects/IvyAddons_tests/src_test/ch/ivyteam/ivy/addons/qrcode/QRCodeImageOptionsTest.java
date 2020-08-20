package ch.ivyteam.ivy.addons.qrcode;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.scripting.objects.util.IIvyScriptObjectEnvironment;
import ch.ivyteam.ivy.scripting.objects.util.IvyScriptObjectEnvironmentContext;

@RunWith(PowerMockRunner.class)
@PrepareForTest({IvyScriptObjectEnvironmentContext.class})
public class QRCodeImageOptionsTest {
	
	private static java.io.File DEFAULT_TEMP_DIR = new java.io.File("temp");
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Mock
	private IIvyScriptObjectEnvironment ivyScriptObjectEnvironment;
	
	@Before
	public void setup() {
		ivyScriptObjectEnvironment = mock(IIvyScriptObjectEnvironment.class);
		when(ivyScriptObjectEnvironment.getTempFileArea()).thenReturn(DEFAULT_TEMP_DIR);
		
		mockStatic(IvyScriptObjectEnvironmentContext.class);
		when(IvyScriptObjectEnvironmentContext.getIvyScriptObjectEnvironment()).thenReturn(ivyScriptObjectEnvironment);
	}

	@Test
	public void forSquare_with_size_under_one_throws_IAE() {
		thrown.expect(IllegalArgumentException.class);
		QRCodeImageOptions.forSquare(0);
	}
	
	@Test
	public void withExportFile_with_fileType_not_image_throws_IAE() {
		QRCodeImageOptions options = QRCodeImageOptions.forSquare(10);
		
		thrown.expect(IllegalArgumentException.class);
		options.withExportFile(new java.io.File("test.pdf"));
	}
	
	@Test
	public void withExportFile_with_ImagefileType_returns_QRCodeImageOptions_with_specified_exportFile() {
		java.io.File exportFile = new java.io.File("test.png");
		QRCodeImageOptions options = QRCodeImageOptions.forSquare(10).withExportFile(exportFile);
		
		assertThat(options.getExporFile(), equalTo(exportFile));
	}
	
	@Test
	public void QRCodeImageOptions_no_exportFile_specified_has_default_exportFile() {
		QRCodeImageOptions options = QRCodeImageOptions.forSquare(10);
		
		assertTrue( options.getExporFile() != null);
		assertTrue( options.getExporFile().getParent().equalsIgnoreCase((ivyScriptObjectEnvironment.getTempFileArea().getPath())));
	}

}
