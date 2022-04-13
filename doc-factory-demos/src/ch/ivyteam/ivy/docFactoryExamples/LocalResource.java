package ch.ivyteam.ivy.docFactoryExamples;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;

/*
 * Loader for files from resources folder.
 */
public class LocalResource {
  private final String path;

  public LocalResource(String relativeFilePath) {
    this.path = relativeFilePath;
  }

  public Path asPath() {
    return asFile().toPath();
  }

  public File asFile() {
    try {
      ch.ivyteam.ivy.scripting.objects.File file = new ch.ivyteam.ivy.scripting.objects.File(path, true);
      file.createNewFile();
      try (InputStream is = asStream()) {
        String res = IOUtils.toString(is, StandardCharsets.ISO_8859_1);
        file.write(res, StandardCharsets.ISO_8859_1.name());
        return file.getJavaFile();
      }
    } catch (IOException ex) {
      throw new RuntimeException("Failed to load resource " + path, ex);
    }
  }

  private InputStream asStream() {
    return LocalResource.class.getResourceAsStream("/" + path);
  }

}
