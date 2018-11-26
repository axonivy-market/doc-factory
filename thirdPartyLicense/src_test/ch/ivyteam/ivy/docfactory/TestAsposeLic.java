package ch.ivyteam.ivy.docfactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import ch.ivyteam.ivy.ThirdPartyLicenses;

public class TestAsposeLic
{

  @Test
  public void licStream_withoutEngine() throws IOException
  {
    InputStream licStream = ThirdPartyLicenses.getDocumentFactoryLicense();
    Assert.assertNotNull("not available in headless env", licStream);
    
    String licVal = IOUtils.toString(licStream, StandardCharsets.UTF_8);
    Assert.assertEquals("empty license when engine not running", StringUtils.EMPTY, licVal);
  }
  
}
