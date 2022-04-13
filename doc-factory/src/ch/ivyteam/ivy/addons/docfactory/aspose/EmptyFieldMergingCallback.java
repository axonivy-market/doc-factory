package ch.ivyteam.ivy.addons.docfactory.aspose;

import com.aspose.words.FieldMergingArgs;
import com.aspose.words.IFieldMergingCallback;
import com.aspose.words.ImageFieldMergingArgs;

public class EmptyFieldMergingCallback implements IFieldMergingCallback {

  @Override
  public void fieldMerging(FieldMergingArgs arg0) throws Exception {
    return;
  }

  @Override
  public void imageFieldMerging(ImageFieldMergingArgs arg0) throws Exception {
    return;
  }

}
