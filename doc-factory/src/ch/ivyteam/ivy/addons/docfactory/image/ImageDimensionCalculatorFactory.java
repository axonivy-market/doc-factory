package ch.ivyteam.ivy.addons.docfactory.image;

public class ImageDimensionCalculatorFactory {

  public static ImageDimensionCalculator getInstance() {
    return new ImageDimensionCalculatorFromImageMergeFieldName();
  }

}
