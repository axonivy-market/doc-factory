package ch.ivyteam.ivy.addons.docfactory.aspose.mergefield;

import com.aspose.words.DocumentBuilder;
import com.aspose.words.FieldMergingArgs;
import com.aspose.words.Run;

public class BooleanToCheckBoxTransformer {

  private static final int CHECKED_CHECKBOX_MS_CODE = 0x00FE;
  private static final int UNCHECKED_CHECKBOX_MS_CODE = 0x00A8;
  private static final String WINGDINGS_FONT_NAME = "Wingdings";

  public static void displayFieldAsCheckBox(FieldMergingArgs fieldMergingArgs) throws Exception {
    DocumentBuilder builder = new DocumentBuilder(fieldMergingArgs.getDocument());
    builder.moveToMergeField(fieldMergingArgs.getFieldName());
    Run run = new Run(fieldMergingArgs.getDocument());
    run.getFont().setName(WINGDINGS_FONT_NAME);
    run.setText(buildCheckBox((boolean) fieldMergingArgs.getFieldValue()));
    builder.insertNode(run);
  }

  private static String buildCheckBox(boolean fieldValue) {
    if (fieldValue) {
      return Character.toString((char) CHECKED_CHECKBOX_MS_CODE);
    } else {
      return Character.toString((char) UNCHECKED_CHECKBOX_MS_CODE);
    }
  }

}
