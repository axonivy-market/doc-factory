package ch.ivyteam.ivy.addons.docfactory.log;

import java.io.File;
import java.util.Collection;

import javax.faces.bean.ApplicationScoped;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import ch.ivyteam.di.restricted.DiCore;
import ch.ivyteam.ivy.config.IFileAccess;
import ch.ivyteam.ivy.environment.Ivy;

@SuppressWarnings("restriction")
@ApplicationScoped
public class IvyLogDirectoryRetriever implements DocFactoryLogDirectoryRetriever {

  private static File ivyLogDirectory;

  /**
   * Gets the directory where the Ivy log files are stored.
   * @return The directory of the ivy log files. May be null.
   */
  @Override
  public File getLogDirectory() {
    if (ivyLogDirectory != null) {
      return ivyLogDirectory;
    }
    try {
      IOFileFilter filter = new SuffixFileFilter(".log");
      IFileAccess files = DiCore.getGlobalInjector().getInstance(IFileAccess.class);
      Collection<File> logs = files.getLogFiles(filter);
      File log = logs.stream().findFirst().orElse(null);
      if (log != null) {
        ivyLogDirectory = log.getParentFile();
      }
    } catch (Exception ex) {
      Ivy.log().error("An exception occured while getting the Ivy log directory", ex);
    }
    return ivyLogDirectory;
  }

}
