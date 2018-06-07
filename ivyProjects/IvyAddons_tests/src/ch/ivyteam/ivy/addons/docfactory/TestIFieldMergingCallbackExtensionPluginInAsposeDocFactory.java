/**
 * 
 */
package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeFieldMergingCallback;
import ch.ivyteam.ivy.addons.docfactory.aspose.LicenseLoader;
import ch.ivyteam.ivy.cm.IContentManagementSystem;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.log.Logger;

import com.aspose.words.FieldMergingArgs;
import com.aspose.words.ImageFieldMergingArgs;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Ivy.class, ThirdPartyLicenses.class, LicenseLoader.class})
public class TestIFieldMergingCallbackExtensionPluginInAsposeDocFactory {
	
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
	public void NoIFieldMergingCallbackExtensionInput_IFieldMergingCallbackNotNull() {
		BaseDocFactory fact = BaseDocFactory.getInstance();
		assertNotNull("IFieldMergingCallback ", fact.getFieldMergingCallBack());
	}

	@Test
	public void NoIFieldMergingCallbackExtensionInput_IFieldMergingCallback_InstanceOf_AsposeFieldMergingCallback() {
		BaseDocFactory fact = BaseDocFactory.getInstance();
		assertTrue("IFieldMergingCallback instanceof AsposeDocFactory.HandleMergeImageField", fact.getFieldMergingCallBack() instanceof AsposeFieldMergingCallback);
	}
	
	@Test
	public void IFieldMergingCallbackExtensionSetInConstructor_IFieldMergingCallback_NotNull() {
		BaseDocFactory fact = BaseDocFactory.getInstance().withFieldMergingCallBack(new AsposeFieldMergingCallback());
		assertNotNull("IFieldMergingCallback ", fact.getFieldMergingCallBack());
	}
	
	@Test
	public void IFieldMergingCallbackExtensionSetInConstructor_IFieldMergingCallback_InstanceOf_TypeSetInConstructor() {
		BaseDocFactory fact = BaseDocFactory.getInstance().withFieldMergingCallBack(new MyIFieldMergingCallback());
		assertTrue("IFieldMergingCallback instanceof AsposeDocFactory.HandleMergeImageField", fact.getFieldMergingCallBack() instanceof MyIFieldMergingCallback);
	}

	protected class MyIFieldMergingCallback extends AsposeFieldMergingCallback {

		@Override
		public void fieldMerging(FieldMergingArgs arg0) throws Exception {
			// Do whatever you want.
			
		}

		@Override
		public void imageFieldMerging(ImageFieldMergingArgs arg0)
				throws Exception {
			// Do whatever you want. You can get a look at the AsposeDocFactory.HandleMergeImageField inner class.
			// Do not forget to handle the case of Image merge fields like in the the AsposeDocFactory.HandleMergeImageField inner class.
			
		}
		
	}

}
