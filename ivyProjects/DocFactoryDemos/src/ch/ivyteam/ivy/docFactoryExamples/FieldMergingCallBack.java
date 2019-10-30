package ch.ivyteam.ivy.docFactoryExamples;

import java.io.ByteArrayInputStream;

import org.apache.commons.lang3.StringUtils;

import com.aspose.words.DocumentBuilder;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.ImageFieldMergingArgs;
import com.aspose.words.MergeFieldImageDimension;
import com.aspose.words.Paragraph;

import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeFieldMergingCallback;

/*
 * Custom call back that allows scaling for images
 */
public class FieldMergingCallBack extends AsposeFieldMergingCallback
{
  private final int width;
  private final int height;

  public FieldMergingCallBack(int width, int height)
  {
    this.width = width;
    this.height = height;
  }

  @Override
  public void fieldMerging(FieldMergingArgs fieldMergingArgs) throws Exception
  {
    if (fieldMergingArgs.getFieldValue() == null)
    {
      removeBlankLine(fieldMergingArgs);
      return;
    }
  }

  @Override
  public void imageFieldMerging(ImageFieldMergingArgs imageFieldMergingArgs) throws Exception
  {
    // The field value is a byte array, cast it and create a stream on it.
    if (imageFieldMergingArgs.getFieldValue() != null)
    {
      ByteArrayInputStream imageStream = new ByteArrayInputStream(
              (byte[]) imageFieldMergingArgs.getFieldValue());
      // Now the mail merge engine will retrieve the image from the stream.
      imageFieldMergingArgs.setImageStream(imageStream);
      imageFieldMergingArgs.setImageWidth(new MergeFieldImageDimension(this.width));
      imageFieldMergingArgs.setImageHeight(new MergeFieldImageDimension(this.height));
    }
  }

  private static void removeBlankLine(FieldMergingArgs fieldMergingArgs) throws Exception
  {
    DocumentBuilder builder = new DocumentBuilder(fieldMergingArgs.getDocument());
    builder.moveToMergeField(fieldMergingArgs.getFieldName());
    Paragraph paragraph = builder.getCurrentParagraph();
    if (StringUtils.isBlank(paragraph.getText()))
    {
      paragraph.remove();
    }
  }

}
