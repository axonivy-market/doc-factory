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

public class LicPropertiesWriter {

  public static void main(String[] args)
  {
    File resources = resolveTargetPropsFile();
    try(InputStream asposeLic = LicPropertiesWriter.class.getResourceAsStream("docfactory_2015_08_28.lic");
        OutputStream licPropsOut = new FileOutputStream(resources))
    {
      Properties licenses = new Properties();
      licenses.put("aspose2015", IOUtils.toString(asposeLic, StandardCharsets.UTF_8));
      licenses.store(licPropsOut, "internal");
      System.out.println("wrote licenses to "+licPropsOut);
    }
    catch (IOException ex)
    {
      System.err.println(ex);
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
