package ch.ivyteam.ivy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * @author mda
 * @since 09.07.2009
 */
public class ThirdPartyLicenses
{
  private static final Logger LOGGER = Logger.getLogger(ThirdPartyLicenses.class);
  private static Properties thirdPartyLicense = new Properties();
  
  static
  {
    try
    {
      InputStream propertyStream = ThirdPartyLicenses.class.getResourceAsStream("ThirdPartyLicenses.properties");
      thirdPartyLicense.load(propertyStream);
    }
    catch (Throwable th)
    {
      LOGGER.error("Could not read third party licenses", th);
    }
  }
  
  static InputStream getAsposeLicenseStream()
  {
    String aspose = thirdPartyLicense.getProperty("aspose","");
    return new ByteArrayInputStream(aspose.getBytes());
  }
  
  /**
   * Gets the license for Aspose.Words and returns it as an input stream
   * @return the Aspose.words license as stream
   */
  public static InputStream getDocumentFactoryLicense()
  {
    if (!serverIsRunning())
    {
      return new ByteArrayInputStream(new byte[0]); // empty license
    }
    
    return getAsposeLicenseStream();
  }

  private static boolean serverIsRunning()
  {
    try
    {
      Class<?> ivyServer = Class.forName("ch.ivyteam.ivy.server.IServer");
      Class<?> diCore = Class.forName("ch.ivyteam.di.restricted.DiCore");
      Object injector = diCore.getMethod("getGlobalInjector").invoke(null);
      Object server = injector.getClass().getMethod("getInstance", Class.class).invoke(injector, ivyServer);
      Boolean isRunning = (Boolean) ivyServer.getMethod("isRunning").invoke(server);
      return isRunning.booleanValue();
    }
    catch (Exception ex)
    {
      LOGGER.error("Could not read third party licenses as the IvyServer is not running");
      return false;
    }
  }
  
}
