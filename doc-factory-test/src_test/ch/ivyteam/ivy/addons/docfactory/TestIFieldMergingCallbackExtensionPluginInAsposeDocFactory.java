package ch.ivyteam.ivy.addons.docfactory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeFieldMergingCallback;
import ch.ivyteam.ivy.environment.IvyTest;

import com.aspose.words.FieldMergingArgs;
import com.aspose.words.ImageFieldMergingArgs;

@IvyTest
public class TestIFieldMergingCallbackExtensionPluginInAsposeDocFactory {

  @Test
  public void NoIFieldMergingCallbackExtensionInput_IFieldMergingCallbackNotNull() {
    BaseDocFactory fact = BaseDocFactory.getInstance();
    assertThat((Object) fact.getFieldMergingCallBack()).isNotNull();
  }

  @Test
  public void NoIFieldMergingCallbackExtensionInput_IFieldMergingCallback_InstanceOf_AsposeFieldMergingCallback() {
    BaseDocFactory fact = BaseDocFactory.getInstance();
    assertThat(fact.getFieldMergingCallBack() instanceof AsposeFieldMergingCallback).isTrue();
  }

  @Test
  public void IFieldMergingCallbackExtensionSetInConstructor_IFieldMergingCallback_NotNull() {
    BaseDocFactory fact = BaseDocFactory.getInstance()
            .withFieldMergingCallBack(new AsposeFieldMergingCallback());
    assertThat((Object) fact.getFieldMergingCallBack()).isNotNull();
  }

  @Test
  public void IFieldMergingCallbackExtensionSetInConstructor_IFieldMergingCallback_InstanceOf_TypeSetInConstructor() {
    BaseDocFactory fact = BaseDocFactory.getInstance()
            .withFieldMergingCallBack(new MyIFieldMergingCallback());
    assertThat(fact.getFieldMergingCallBack() instanceof MyIFieldMergingCallback).isTrue();
  }

  protected class MyIFieldMergingCallback extends AsposeFieldMergingCallback {

    @Override
    public void fieldMerging(FieldMergingArgs arg0) throws Exception {
      // Do whatever you want.

    }

    @Override
    public void imageFieldMerging(ImageFieldMergingArgs arg0)
            throws Exception {
      // Do whatever you want. You can get a look at the
      // AsposeDocFactory.HandleMergeImageField inner class.
      // Do not forget to handle the case of Image merge fields like in the the
      // AsposeDocFactory.HandleMergeImageField inner class.

    }
  }
}
