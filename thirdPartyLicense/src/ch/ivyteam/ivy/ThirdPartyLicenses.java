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

  public enum Version
  {
    ASPOSE_2015("aspose2015"), 
    ASPOSE_2019("aspose2019");

    private final String licenceName;

    private Version(String licenceName)
    {
      this.licenceName = licenceName;
    }

    public String getLicenceName()
    {
      return licenceName;
    }
  }

  /**
   * @return lic stream
   * @throws FileNotFoundException
   * @deprecated will return old Aspose 2015 licence. Use {@link #getDocumentFactoryLicense(Version)} to specify which licence you want to have.
   */
  @Deprecated
  public static InputStream getDocumentFactoryLicense() throws IOException
  {
    return getDocumentFactoryLicense(Version.ASPOSE_2015);
  }

  /**
   * Returns the document factory license as inputStream to be used in the
   * different aspose products.
   * @param version the version of the license
   * @return lic stream
   * @throws FileNotFoundException
   */
  public static InputStream getDocumentFactoryLicense(Version version) throws IOException
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
    String val = props.getProperty(version.licenceName);
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
