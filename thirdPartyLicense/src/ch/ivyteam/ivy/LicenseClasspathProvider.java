package ch.ivyteam.ivy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.JavaCore;

import ch.ivyteam.ivy.java.IIvyProjectClassPathExtension;

public class LicenseClasspathProvider implements IIvyProjectClassPathExtension
{
  public final String PLUGIN_ID = "ch.ivyteam.ivy.thirdparty.license";

  @Override
  public List<IAccessRule> getCompileClassPathAccessRules(String bundleIdentifier)
  {
    if (PLUGIN_ID.equals(bundleIdentifier))
    {
      return Arrays.asList(new IAccessRule[] {JavaCore.newAccessRule(
              new Path("ch/ivyteam/ivy/*"), IAccessRule.K_ACCESSIBLE),
          EXCLUDE_ALL_OTHER_RULE
      });
    }
    return Collections.emptyList();
  }

  @Override
  public List<String> getCompileClassPath(String bundleIdentifier)
  {
    if (PLUGIN_ID.equals(bundleIdentifier))
    {
      return Arrays.asList(".");
    }
    return Collections.emptyList();
  }

  @Override
  public List<String> getClassLoaderContributingBundles()
  {
    return Arrays.asList(PLUGIN_ID);
  }

  @Override
  public List<String> getCompileClassPathContributingBundles()
  {
    return Arrays.asList(PLUGIN_ID);
  }

}
