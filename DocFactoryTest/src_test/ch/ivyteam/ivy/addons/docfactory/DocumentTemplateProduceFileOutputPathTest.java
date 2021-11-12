package ch.ivyteam.ivy.addons.docfactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BaseDocFactory.class)
public class DocumentTemplateProduceFileOutputPathTest {
	
	@Mock
	BaseDocFactory docfactory;
	
	
	
	@Before
	public void setup() {
		docfactory = mock(BaseDocFactory.class);
		when(docfactory.generateDocument(any(DocumentTemplate.class))).thenReturn(new FileOperationMessage());
		
		mockStatic(BaseDocFactory.class);
		when(BaseDocFactory.getInstance()).thenReturn(docfactory);
	}

	@Test
	public void produceDocument_OnWindowsPath_correct_outputPath() {
		File resultFile = new File("C:/designer/ivy/files/application/letters/letter.pdf");
		DocumentTemplate documentTemplate = new DocumentTemplate();
		
		documentTemplate.produceDocument(resultFile);
		
		String expectedOuputPath = "C:/designer/ivy/files/application/letters".replace('/', File.separatorChar);
		
		assertThat(documentTemplate.getOutputPath(), is(expectedOuputPath));
	}
	
	@Test
	public void produceDocument_OnUnixPath_correct_outputPath() {
		File resultFile = new File("tmp/ivy/files/application/letters/letter.pdf");
		DocumentTemplate documentTemplate = new DocumentTemplate();
		
		documentTemplate.produceDocument(resultFile);
		
		String expectedOuptPath = "tmp/ivy/files/application/letters".replace('/', File.separatorChar);
		assertThat(documentTemplate.getOutputPath(), is(expectedOuptPath));
	}

}
