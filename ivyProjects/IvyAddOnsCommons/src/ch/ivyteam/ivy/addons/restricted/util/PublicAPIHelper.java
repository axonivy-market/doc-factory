package ch.ivyteam.ivy.addons.restricted.util;

import java.util.concurrent.Callable;

import ch.ivyteam.ivy.addons.util.AddonsRuntimeException;
import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.SecurityManagerFactory;
import ch.ivyteam.ivy.workflow.ICase;

/**
 * It helps to call public api as SYSTEM
 * 
 * @author tirib, TI-Informatique
 * @since 16.11.2010
 * 
 */

public class PublicAPIHelper
{

  /**
   * It find case by case identifier
   * 
   * @param caseIdentifier
   * @return case, if not found it returns null
   */
  public static ICase findCase(final long caseIdentifier)
  {
    ICase wfCase = null;

    try
    {

      wfCase = SecurityManagerFactory.getSecurityManager().executeAsSystem(new Callable<ICase>()
        {
          public ICase call() throws Exception
          {
            return Ivy.wf().findCase(caseIdentifier);
          }
        });
    }
    catch (Exception e)
    {
      throw new AddonsRuntimeException(e);
    }

    return wfCase;
  }

  /**
   * It returns the Ivy application file area absolute path.
   * It's simplified method of {@link PublicAPIHelper#getApplicationFileAreaAbsolutePath(IApplication)}
   * where application input parameter is <code>Ivy.request().getApplication()</code>
   * 
   * @return
   */
  public static String getApplicationFileAreaAbsolutePath()
  {
    try
    {

      return getApplicationFileAreaAbsolutePath(Ivy.request().getApplication());
    }
    catch (Exception e)
    {
      throw new AddonsRuntimeException(e);
    }
  }

  /**
   * It returns the Ivy application file area absolute path
   * 
   * @return application file area absolute path
   */
  public static String getApplicationFileAreaAbsolutePath(final IApplication application)
  {
    String fileAreaAbsolutePath = "";

    try
    {
      fileAreaAbsolutePath = SecurityManagerFactory.getSecurityManager().executeAsSystem(
              new Callable<String>()
                {
                  public String call() throws Exception
                  {
                    return application.getFileArea().getAbsolutePath();
                  }
                });
    }
    catch (Exception e)
    {
      throw new AddonsRuntimeException(e);
    }

    return fileAreaAbsolutePath;

  }

}