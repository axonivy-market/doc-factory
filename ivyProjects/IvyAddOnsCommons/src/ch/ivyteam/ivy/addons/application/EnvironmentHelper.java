package ch.ivyteam.ivy.addons.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import ch.ivyteam.ivy.addons.util.AddonsRuntimeException;
import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.application.restricted.IEnvironment;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.persistence.PersistencyException;
import ch.ivyteam.ivy.security.SecurityManagerFactory;
import ch.ivyteam.ivy.workflow.IWorkflowSession;

/**
 * It provides helper methods related to the ivy environment {@link IEnvironment} 
 * 
 * @author Rifat Binjos, TI-Informatique
 * @since October 2012
 * 
 */
public class EnvironmentHelper
{
  
  private static final String DEFAULT_ENVIRONMENT_NAME = "Default";
  
  
  /**
   * It's simplified version of {@link EnvironmentHelper#getActiveEnvironment(IWorkflowSession)}
   * where workflow session is solved with <code>Ivy.session()</code>.
   * 
   * @return
   */
  public static String getActiveEnvironment()
  {
    try
    {
      return getActiveEnvironment(Ivy.session());
    }
    catch (Exception e)
    {
      throw new AddonsRuntimeException(e);
    }
  }

  /**
   * It returns the active environment according to the following logic:
   * <ol>
   *    <li>If session active environment is defined: 
   *            if it's default then return null; otherwise return environment name.</li>
   *    <li>If session active environment is not defined then get application active environment:
   *            if it's default then return null; otherwise return environment name.</li>
   * </ol>
   * @param session for which the active environment has to be calculated
   * @return the active environment name
   * @throws PersistencyException
   */
  public static String getActiveEnvironment(IWorkflowSession session) throws PersistencyException
  {
    try
    {
      String sessionActiveEnvironment = null;
      String applicationActiveEnvironment = null;
      IApplication application = null;

      application = session.getWorkflowContext().getApplication();
      sessionActiveEnvironment = session.getActiveEnvironment();

      if (sessionActiveEnvironment != null)
      {
        if (isDefaultEnvironment(application.getName(), sessionActiveEnvironment))
        {
          return null; 
        }
        else
        {
          return sessionActiveEnvironment;
        }
      }
      else
      {
        applicationActiveEnvironment = application.getActiveEnvironment();
        
        if (applicationActiveEnvironment == null || isDefaultEnvironment(application.getName(), applicationActiveEnvironment))
        {
          return null; 
        }
        else
        {
          return applicationActiveEnvironment; 
        }
      }

    }
    catch (Exception e)
    {
      throw new AddonsRuntimeException(e);
    }
  }

  /**
   * It returns a list of environment names of the given {@link IApplication}
   * <p>
   * <b>Warning: </b> The default environment is never in the returned list.
   * </p>
   * 
   * @param application to look on
   * @return list of environment names <b> except </b> the Default environment
   * @throws Exception
   */
  public static List<String> getEnvironments(final IApplication application) throws Exception
  {
    try
    {

      return SecurityManagerFactory.getSecurityManager().executeAsSystem(new Callable<List<String>>()
        {
          public List<String> call() throws Exception
          {
            List<IEnvironment> ienvironments;
            List<String> environments;

            ienvironments = application.getEnvironments();
            environments = new ArrayList<String>();

            for (IEnvironment environment : ienvironments)
            {
              if (!isDefaultEnvironment(application.getName(), environment.getName()))
              {
                environments.add(environment.getName());
              }
            }

            return environments;
          }

        });
    }
    catch (Exception e)
    {
      throw new AddonsRuntimeException(e);
    }
  }

  /**
   * It returns true if the given environment is the default environment. It uses the following default
   * environment name pattern according to Ivy 4.2 and 4.3 servers:
   * 
   * <pre>
   * Default_{{@link IApplication#getName()}}
   * </pre>
   * 
   * @param applicationName is the application name
   * @param environmentName is the environment name
   * @return
   * @throws PersistencyException
   */
  private static boolean isDefaultEnvironment(String applicationName, String environmentName)
          throws PersistencyException
  {
    return (getDefaultEnvironmentName() + "_" + applicationName).equals(environmentName);
  }

  public static String getDefaultEnvironmentName()
  {
    return DEFAULT_ENVIRONMENT_NAME;
  }

}