/**
 * 
 */
package ch.ivyteam.ivy.addons.docfactory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeFieldMergingCallback;

import com.aspose.words.FieldMergingArgs;
import com.aspose.words.ImageFieldMergingArgs;


public class TestIFieldMergingCallbackExtensionPluginInAsposeDocFactory extends DocFactoryTest {

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
