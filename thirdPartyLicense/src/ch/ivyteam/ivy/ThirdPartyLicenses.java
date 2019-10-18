package ch.ivyteam.ivy;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import ch.ivyteam.ivy.docfactory.Server;

public class ThirdPartyLicenses
{
  public static final String PROPERTIES_PATH = "ch/ivyteam/ivy/docfactory/licenses.props";

  /**
   * @return lic stream
   * @throws FileNotFoundException
   */
  public static InputStream getDocumentFactoryLicense() throws IOException
  {
    return getDocumentFactoryLicense("aspose2019");
  }

  /**
   * Returns the document factory license as inputStream to be used in the
   * different aspose products.
   * @param licName identifier
   * @return lic stream
   * @throws FileNotFoundException
   */
  private static InputStream getDocumentFactoryLicense(String licName) throws IOException
  {
    if (!Server.get().isRunning())
    {
      return new ByteArrayInputStream(new byte[0]); // empty license
    }

    Properties props = new Properties();
    try (InputStream propsIn = ThirdPartyLicenses.class.getResourceAsStream("docfactory/licenses.props"))
    {
      props.load(propsIn);
    }
    String val = props.getProperty(licName);
    return new ByteArrayInputStream(val.getBytes());
  }

  /**
   * @Deprecated Will be removed in the future.
   */
  @Deprecated
  public static boolean isAsposeLicenseValidForAsposeWords() throws Exception
  {
    return true;
  }

}
