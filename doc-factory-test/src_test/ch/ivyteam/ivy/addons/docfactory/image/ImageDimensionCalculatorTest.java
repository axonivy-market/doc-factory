package ch.ivyteam.ivy.addons.docfactory.image;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class ImageDimensionCalculatorTest {

  private static final String WIDTH_MARKER = "__WIDTH:";
  private static final String HEIGHT_MARKER = "__HEIGHT:";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void calculateImageDimensionForMergingInTemplate_throws_IAE_if_inputStream_null()
          throws IOException {
    ImageDimensionCalculator idc = ImageDimensionCalculatorFactory.getInstance();
    String imageFieldName = "Image:myImage";
    InputStream is = null;

    thrown.expect(IllegalArgumentException.class);
    idc.calculateImageDimensionForMergingInTemplate(is, imageFieldName);

  }

  @Test
  public void calculateImageDimensionForMergingInTemplate_throws_IAE_if_imageFieldName_null()
          throws Exception {
    ImageDimensionCalculator idc = ImageDimensionCalculatorFactory.getInstance();
    File image = new File(this.getClass().getResource("logo-euro.jpg").toURI().getPath());
    InputStream is = new FileInputStream(image);
    String imageFieldName = null;

    thrown.expect(IllegalArgumentException.class);
    idc.calculateImageDimensionForMergingInTemplate(is, imageFieldName);
  }

  @Test
  public void calculateImageDimensionForMergingInTemplate_throws_IAE_if_imageFieldName_blank()
          throws Exception {
    ImageDimensionCalculator idc = ImageDimensionCalculatorFactory.getInstance();
    File image = new File(this.getClass().getResource("logo-euro.jpg").toURI().getPath());
    InputStream is = new FileInputStream(image);
    String imageFieldName = " ";

    thrown.expect(IllegalArgumentException.class);
    idc.calculateImageDimensionForMergingInTemplate(is, imageFieldName);
  }

  @Test
  public void calculateImageDimensionForMergingInTemplate_returns_zero_dimension_if_width_and_height_empty_in_fieldname()
          throws Exception {
    ImageDimensionCalculator idc = ImageDimensionCalculatorFactory.getInstance();
    File image = new File(this.getClass().getResource("logo-euro.jpg").toURI().getPath());
    InputStream is = new FileInputStream(image);

    String imageFieldName = "Image:myImage" + WIDTH_MARKER + HEIGHT_MARKER;
    Dimension dimension = idc.calculateImageDimensionForMergingInTemplate(is, imageFieldName);
    Dimension expectedDimension = new Dimension(0, 0);

    assertThat(dimension, equalTo(expectedDimension));
  }

  @Test
  public void calculateImageDimensionForMergingInTemplate_returns_zero_dimension_if_height_and_width_empty_in_fieldname()
          throws Exception {
    ImageDimensionCalculator idc = ImageDimensionCalculatorFactory.getInstance();
    File image = new File(this.getClass().getResource("logo-euro.jpg").toURI().getPath());
    InputStream is = new FileInputStream(image);

    Dimension dimension = idc.calculateImageDimensionForMergingInTemplate(is,
            "Image:myImage:__HEIGHT:__WIDTH");
    Dimension expectedDimension = new Dimension(0, 0);

    assertThat(dimension, equalTo(expectedDimension));
  }

  @Test
  public void calculateImageDimensionForMergingInTemplate_returns_zero_dimension_if_no_width_or_height_specified_in_fieldname()
          throws Exception {
    ImageDimensionCalculator idc = ImageDimensionCalculatorFactory.getInstance();
    File image = new File(this.getClass().getResource("logo-euro.jpg").toURI().getPath());
    InputStream is = new FileInputStream(image);

    Dimension dimension = idc.calculateImageDimensionForMergingInTemplate(is, "Image:myImage");
    Dimension expectedDimension = new Dimension(0, 0);

    assertThat(dimension, equalTo(expectedDimension));
  }

  @Test
  public void calculateImageDimensionForMergingInTemplate_returns_dimension_with_width_specified_in_fieldname()
          throws Exception {
    ImageDimensionCalculator idc = ImageDimensionCalculatorFactory.getInstance();
    File image = new File(this.getClass().getResource("logo-euro.jpg").toURI().getPath());
    InputStream is = new FileInputStream(image);

    Dimension dimension = idc.calculateImageDimensionForMergingInTemplate(is, "Image:myImage__WIDTH:50");

    assertThat(dimension.getWidth(), equalTo((double) 50));
  }

  @Test
  public void calculateImageDimensionForMergingInTemplate_returns_dimension_with_zero_width_if_height_empty_in_fieldname()
          throws Exception {
    ImageDimensionCalculator idc = ImageDimensionCalculatorFactory.getInstance();
    File image = new File(this.getClass().getResource("logo-euro.jpg").toURI().getPath());
    InputStream is = new FileInputStream(image);

    Dimension dimension = idc.calculateImageDimensionForMergingInTemplate(is, "Image:myImage__HEIGHT:");
    Dimension expectedDimension = new Dimension(0, 0);

    assertThat(dimension, equalTo(expectedDimension));
  }

  @Test
  public void calculateImageDimensionForMergingInTemplate_returns_dimension_with_zero_width_if_width_empty_in_fieldname()
          throws Exception {
    ImageDimensionCalculator idc = ImageDimensionCalculatorFactory.getInstance();
    File image = new File(this.getClass().getResource("logo-euro.jpg").toURI().getPath());
    InputStream is = new FileInputStream(image);

    Dimension dimension = idc.calculateImageDimensionForMergingInTemplate(is, "Image:myImage__WIDTH:");
    Dimension expectedDimension = new Dimension(0, 0);

    assertThat(dimension, equalTo(expectedDimension));
  }

  @Test
  public void calculateImageDimensionForMergingInTemplate_returns_scaled_height_if_only_width_specified_in_fieldname()
          throws Exception {
    ImageDimensionCalculator idc = ImageDimensionCalculatorFactory.getInstance();
    File image = new File(this.getClass().getResource("logo-euro.jpg").toURI().getPath());
    InputStream is = new FileInputStream(image);
    InputStream is2 = new FileInputStream(image);

    Dimension dimension1 = idc.calculateImageDimensionForMergingInTemplate(is, "Image:myImage__WIDTH:50");
    Dimension dimension2 = idc.calculateImageDimensionForMergingInTemplate(is2, "Image:myImage__WIDTH:100");

    assertThat(dimension2.height, equalTo(dimension1.height * 2));
  }

  @Test
  public void calculateImageDimensionForMergingInTemplate_returns_scaled_height_if_width_specified_and_height_empty_in_fieldname()
          throws Exception {
    ImageDimensionCalculator idc = ImageDimensionCalculatorFactory.getInstance();
    File image = new File(this.getClass().getResource("logo-euro.jpg").toURI().getPath());
    InputStream is = new FileInputStream(image);
    InputStream is2 = new FileInputStream(image);

    Dimension dimension1 = idc.calculateImageDimensionForMergingInTemplate(is,
            "Image:myImage__WIDTH:50__HEIGHT:");
    Dimension dimension2 = idc.calculateImageDimensionForMergingInTemplate(is2,
            "Image:myImage__WIDTH:100__HEIGHT:");

    assertThat(dimension2.height, equalTo(dimension1.height * 2));
  }

  @Test
  public void calculateImageDimensionForMergingInTemplate_returns_scaled_height_if_height_empty_and_width_specified_in_fieldname()
          throws Exception {
    ImageDimensionCalculator idc = ImageDimensionCalculatorFactory.getInstance();
    File image = new File(this.getClass().getResource("logo-euro.jpg").toURI().getPath());
    InputStream is = new FileInputStream(image);
    InputStream is2 = new FileInputStream(image);

    Dimension dimension1 = idc.calculateImageDimensionForMergingInTemplate(is,
            "Image:myImage__HEIGHT:__WIDTH:50");
    Dimension dimension2 = idc.calculateImageDimensionForMergingInTemplate(is2,
            "Image:myImage__HEIGHT:__WIDTH:100");

    assertThat(dimension2.height, equalTo(dimension1.height * 2));
  }

}
