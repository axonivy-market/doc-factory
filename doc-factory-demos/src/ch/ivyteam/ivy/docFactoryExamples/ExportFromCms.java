package ch.ivyteam.ivy.docFactoryExamples;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;

public class ExportFromCms {

  public static java.io.File export(String cmsUri, String ext) throws IOException {
    var file = StringUtils.removeStart(cmsUri, "/") + "." + ext;
    var tempFile = new File(file, true).getJavaFile();
    tempFile.getParentFile().mkdirs();
    var cov = Ivy.cms().root().child().file(cmsUri, ext).value().get();
    try (var in = cov.read().inputStream();
            var fos = new FileOutputStream(tempFile)) {
      IOUtils.copy(in, fos);
    }
    return tempFile;
  }
}
