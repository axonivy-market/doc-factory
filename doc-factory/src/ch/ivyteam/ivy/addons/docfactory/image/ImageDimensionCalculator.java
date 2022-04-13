package ch.ivyteam.ivy.addons.docfactory.image;

import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;

public interface ImageDimensionCalculator {

  Dimension calculateImageDimensionForMergingInTemplate(InputStream imageStream, String fieldname,
          Object... objects) throws IOException;

}
