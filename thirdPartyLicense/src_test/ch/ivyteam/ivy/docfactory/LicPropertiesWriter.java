package ch.ivyteam.ivy.docfactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.ThirdPartyLicenses.Version;

public class LicPropertiesWriter
{

  public static void main(String[] args)
  {
    File resources = resolveTargetPropsFile();
    try (OutputStream licPropsOut = new FileOutputStream(resources))
    {
      Properties licenses = new Properties();
      licenses.put(Version.ASPOSE_2015.getLicenceName(), readLicence("docfactory_2015_08_28.lic"));
      licenses.put(Version.ASPOSE_2019.getLicenceName(), readLicence("docfactory_2019.lic"));
      licenses.store(licPropsOut, "internal");
      System.out.println("wrote licenses to " + resources);
    }
    catch (IOException ex)
    {
      System.err.println(ex);
    }
  }

  private static Object readLicence(String licenceFileName) throws IOException
  {
    try (InputStream asposeLic = LicPropertiesWriter.class.getResourceAsStream(licenceFileName))
    {
      return IOUtils.toString(asposeLic, StandardCharsets.UTF_8);
    }
  }

  private static File resolveTargetPropsFile()
  {
    File myClassDir = new File(LicPropertiesWriter.class.getResource("").getFile());
    try
    {
      File resources = new File(myClassDir, "../../../../../../resources").getCanonicalFile();
      File props = new File(resources, ThirdPartyLicenses.PROPERTIES_PATH);
      props.getParentFile().mkdirs();
      return props;
    }
    catch (IOException ex)
    {
      throw new RuntimeException("Failed to resolve license properties file");
    }
  }

}
