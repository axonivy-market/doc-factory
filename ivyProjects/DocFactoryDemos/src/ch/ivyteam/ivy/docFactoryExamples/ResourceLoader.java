package ch.ivyteam.ivy.docFactoryExamples;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;


/*
 * Loader for files from resources folder.
 */
public class ResourceLoader
{

  public File getResource(String relativFilePath)
  {
    try 
    {
      ch.ivyteam.ivy.scripting.objects.File file = new ch.ivyteam.ivy.scripting.objects.File(relativFilePath, true);
      file.createNewFile();
      try (InputStream is = ResourceLoader.class.getResourceAsStream("/"+relativFilePath))
      {
        String res = IOUtils.toString(is, StandardCharsets.ISO_8859_1);
        file.write(res, StandardCharsets.ISO_8859_1.name());
        return file.getJavaFile();
      }
    } 
    catch (IOException ex)
    {
      throw new RuntimeException("Failed to load resource "+relativFilePath, ex);
    }
  }

}
