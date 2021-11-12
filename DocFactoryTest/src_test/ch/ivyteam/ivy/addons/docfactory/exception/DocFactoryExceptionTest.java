package ch.ivyteam.ivy.addons.docfactory.exception;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.addons.docfactory.exception.DocFactoryException;
import ch.ivyteam.ivy.addons.docfactory.aspose.LicenseLoader;
import ch.ivyteam.ivy.cm.IContentManagementSystem;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.log.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Ivy.class, ThirdPartyLicenses.class, LicenseLoader.class})
public class DocFactoryExceptionTest {
	
	@Before
	public void setup() throws Exception {
		Logger mockLogger = mock(Logger.class);
		doNothing().when(mockLogger).error(any(String.class));
		doNothing().when(mockLogger).info(any(String.class));
		doNothing().when(mockLogger).debug(any(String.class));
		
		IContentManagementSystem mockedCms = mock(IContentManagementSystem.class);
		when(mockedCms.co(any(String.class))).thenReturn("");
		
		mockStatic(ThirdPartyLicenses.class);
		mockStatic(LicenseLoader.class);
		mockStatic(Ivy.class);
		when(Ivy.log()).thenReturn(mockLogger);
		when(Ivy.cms()).thenReturn(mockedCms);
		when(ThirdPartyLicenses.getDocumentFactoryLicense()).thenReturn(null);
	}

	@Test
	public void docFactoryExceptionInstanciatedWithExceptionOnlyHasMessageOfThisException() {
		Exception ex = new Exception("An Exception occurred");
		DocFactoryException docFactoryException = new DocFactoryException(ex);
		
		assertThat(docFactoryException.getMessage(), CoreMatchers.is(ex.getMessage()));
	}
	
	@Test
	public void docFactoryExceptionInstanciatedWithExceptionAndMessage() {
		Exception ex = new Exception("An Exception occurred");
		DocFactoryException docFactoryException = new DocFactoryException("dummy message", ex);
		
		assertThat(docFactoryException.getMessage(), CoreMatchers.is("dummy message"));
	}
	
	@Test
	public void docFactoryExceptionInstanciatedWithExceptionAndMessageGetCause() {
		Exception ex = new Exception("An Exception occurred");
		DocFactoryException docFactoryException = new DocFactoryException("dummy message", ex);
		
		assertThat(docFactoryException.getCause(), CoreMatchers.is(ex));
	}
	
	@Test
	public void docFactoryExceptionInstanciatedWithExceptionGetCause() {
		Exception ex = new Exception("An Exception occurred");
		DocFactoryException docFactoryException = new DocFactoryException(ex);
		
		assertThat(docFactoryException.getCause(), CoreMatchers.is(ex));
	}

}
