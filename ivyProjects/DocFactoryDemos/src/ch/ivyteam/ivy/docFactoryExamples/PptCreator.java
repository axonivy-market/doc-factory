package ch.ivyteam.ivy.docFactoryExamples;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;

import com.aspose.slides.AutoShape;
import com.aspose.slides.IPPImage;
import com.aspose.slides.IPictureFrame;
import com.aspose.slides.IRow;
import com.aspose.slides.IShape;
import com.aspose.slides.ISlide;
import com.aspose.slides.ISlideCollection;
import com.aspose.slides.ITable;
import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;
import com.aspose.slides.ShapeType;

class PptCreator
{
  private final DocumentCreator service;
  private final String slideTitle;
  private final int numberOfSlide;

  PptCreator(DocumentCreator service)
  {
    this.service = service;
    
    slideTitle = "DocFactory Demos: Aspose PPT example";
    numberOfSlide = 1;
  }

  public java.io.File create(java.io.File template) throws IOException
  {
    Presentation presentation = new Presentation(template.toPath().toString());

    ch.ivyteam.ivy.scripting.objects.File tempFileIvy = new ch.ivyteam.ivy.scripting.objects.File(
            "ivy_DocFactoryDemo/PowerPointDocument.pptx", false);
    tempFileIvy.createNewFile();
    
    ISlideCollection slds = presentation.getSlides();
    ISlide slideOne = slds.get_Item(0);

    // Binding shape data
    updateText(slideOne, "{title}", slideTitle);
    updateText(slideOne, "{name}", service.getName());
    updateText(slideOne, "{date}", service.dateFormat.format(service.getDate()));

    // Binding image
    byte[] image = service.getImage();
    if (ArrayUtils.isNotEmpty(image))
    {
      IPPImage imgx = presentation.getImages().addImage(image);
      IShape imagePlaceHolder = findShape(slideOne, "{image}");
      IPictureFrame pf = slideOne.getShapes().addPictureFrame(ShapeType.Rectangle, imagePlaceHolder.getX(),
              imagePlaceHolder.getY(), imagePlaceHolder.getWidth(), imagePlaceHolder.getHeight(), imgx);
      pf.setAlternativeText("My inserted Image");
      // Do other things with image, Setting relative scale width and height
      // pf.setRelativeScaleHeight(0.8f);

      // remove image placeholder
      slideOne.getShapes().remove(imagePlaceHolder);
    }

    // Binding table data
    ITable table = findTable(slideOne);

    java.util.List<String> expectations = service.getExpectations();
    for (int i = 0; i < expectations.size(); i++)
    {
      IRow row = table.getRows().get_Item(1);
      row.get_Item(0).getTextFrame().setText(i + 1 + "");
      row.get_Item(1).getTextFrame().setText(expectations.get(i));
      table.getRows().addClone(row, false);
    }

    // remove empty row
    table.getRows().removeAt(1, true);

    if (numberOfSlide > 1)
    {
      for (int i = 0; i < numberOfSlide - 1; i++)
      {
        ISlide clone = presentation.getSlides().get_Item(0);
        presentation.getSlides().addClone(clone);
      }
    }

    presentation.save(tempFileIvy.getAbsolutePath(), SaveFormat.Pptx);
    return tempFileIvy.getJavaFile();
  }

  private static void updateText(ISlide slide, String textToReplace, String newText)
  {
    AutoShape shape = findShape(slide, textToReplace);
    if (shape != null)
    {
      shape.getTextFrame().setText(newText);
    }
  }

  private static AutoShape findShape(ISlide slide, String alttext)
  {
    for (int i = 0; i < slide.getShapes().size(); i++)
    {
      if (slide.getShapes().get_Item(i) instanceof AutoShape)
      {
        if (((AutoShape) slide.getShapes().get_Item(i)).getTextFrame().getText().trim()
                .equalsIgnoreCase(alttext))
        {
          return (AutoShape) slide.getShapes().get_Item(i);
        }
      }
    }

    return null;
  }

  private static ITable findTable(ISlide slide)
  {
    for (IShape shape : slide.getShapes())
    {
      if (shape instanceof ITable)
      {
        return (ITable) shape;
      }
    }
    return null;
  }

}