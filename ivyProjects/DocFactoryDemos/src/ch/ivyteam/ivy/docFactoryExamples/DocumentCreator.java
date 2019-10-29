package ch.ivyteam.ivy.docFactoryExamples;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeProduct;
import ch.ivyteam.ivy.addons.docfactory.aspose.LicenseLoader;
import ch.ivyteam.ivy.environment.Ivy;

public class DocumentCreator
{
  private String name;
  private Date date;
  private byte[] image;
  private String imageName;
  private ch.ivyteam.ivy.scripting.objects.File ivyFile;
  private java.util.List<String> expectations;
  private String newExpectation;
  private String memberType;

  private String slideTitle;
  private int numberOfSlide;

  public final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

  public void init()
  {
    expectations = new ArrayList<>();
    expectations.add("Sunshine");
    expectations.add("Beach Bar");
    memberType = "1";
    slideTitle = "DocFactory Demos: Aspose PPT example";
    numberOfSlide = 1;

    // Load license for aspose slides & cell
    try
    {
      LicenseLoader.loadLicenseforProduct(AsposeProduct.SLIDES);
      LicenseLoader.loadLicenseforProduct(AsposeProduct.CELLS);
    }
    catch (Exception e)
    {
      Ivy.log().error(e);
    }
  }

  public File createWordDocument() throws IOException
  {
    File template = new LocalResource("resources/myDocumentCreatorTemplate.docx").asFile();
    File result = new DocxCreator(this).create(template);
    return result;
  }

  public File createMultiDocument() throws IOException
  {
    File template = new LocalResource("resources/mySimpleDocTemplate.docx").asFile();
    File result = new DocxCreator(this).createMulti(template);
    return result;
  }

  public File createSimpleDocument()
  {
    File template = new LocalResource("resources/myDocumentCreatorTemplate.docx").asFile();
    File result = new DocxCreator(this).createSimple(template);
    return result;
  }

  public File createPowerPoint() throws IOException
  {
    java.io.File template = new LocalResource("resources/myPowerPointTemplate.pptx").asFile();
    java.io.File result = new PptCreator(this).create(template);
    return result;
  }

  public File createExcel() throws Exception
  {
    return new XlsxCreator(this).create();
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Date getDate()
  {
    return date;
  }

  public void setDate(Date date)
  {
    this.date = date;
  }

  public java.util.List<String> getExpectations()
  {
    return expectations;
  }

  public void setExpectations(java.util.List<String> expectations)
  {
    this.expectations = expectations;
  }

  public String getNewExpectation()
  {
    return newExpectation;
  }

  public void setNewExpectation(String newExpectation)
  {
    this.newExpectation = newExpectation;
  }

  public byte[] getImage()
  {
    return image;
  }

  public void setImage(byte[] image)
  {
    this.image = image;
  }

  public String getImageName()
  {
    return imageName;
  }

  public void setImageName(String imageName)
  {
    this.imageName = imageName;
  }

  public ch.ivyteam.ivy.scripting.objects.File getIvyFile()
  {
    return ivyFile;
  }

  public void setIvyFile(ch.ivyteam.ivy.scripting.objects.File ivyFile)
  {
    this.ivyFile = ivyFile;
  }

  public String getMemberType()
  {
    return memberType;
  }

  public void setMemberType(String memberType)
  {
    this.memberType = memberType;
  }

  public String getSlideTitle()
  {
    return slideTitle;
  }

  public void setSlideTitle(String slideTitle)
  {
    this.slideTitle = slideTitle;
  }

  public int getNumberOfSlide()
  {
    return numberOfSlide;
  }

  public void setNumberOfSlide(int numberOfSlide)
  {
    this.numberOfSlide = numberOfSlide;
  }
}
