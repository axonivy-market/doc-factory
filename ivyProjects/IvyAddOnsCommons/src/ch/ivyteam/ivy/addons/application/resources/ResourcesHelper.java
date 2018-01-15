package ch.ivyteam.ivy.addons.application.resources;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import ch.ivyteam.ivy.addons.application.EnvironmentHelper;
import ch.ivyteam.ivy.addons.restricted.util.PublicAPIHelper;
import ch.ivyteam.ivy.addons.util.AddonsRuntimeException;
import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.application.restricted.IGlobalVariable;
import ch.ivyteam.ivy.environment.Ivy;

/**
 * It returns resources paths like resources root path, application resources path, case documents resources path and so on.
 * 
 * @author Rifat Binjos, TI-Informatique
 * @since October 2012
 * 
 */
@SuppressWarnings("restriction")
public class ResourcesHelper
{

  public static final String RESOURCES_PATH_GLOBAL_VARIABLE_NAME = "xivy_addons_resourcesPath";

  /**
   * Message format for the application resources root folder under which there is folder per {@see
   * ResourcesHelper#APPLICATION_RESOURCES_ROOT_PATH_FORMAT}
   */
  private static final MessageFormat APPLICATION_RESOURCES_ROOT_PATH_FORMAT = new MessageFormat("{0}/res");

  private static final MessageFormat APPLICATION_RESOURCES_PATH_FORMAT = new MessageFormat("{0}/res/{1}/{2}");

  private static final MessageFormat CASE_DOCUMENTS_PATH_FORMAT = new MessageFormat("{0}/files/{1}/{2}/Documents");

  private static final MessageFormat CASE_RESOURCES_PATH_FORMAT = new MessageFormat("{0}/files/{1}/{2}/Resources");

  /**
   * This is simplified version of the
   * {@link ResourcesHelper#getApplicationResourcesPath(IApplication, String, String)} where application is
   * solved with {@code Ivy.request().getApplication()}
   * @param applicationCode
   * @param applicationModule
   * @return
   */
  public static String getApplicationResourcesPath(String applicationCode, String applicationModule)
  {
    try
    {
      return getApplicationResourcesPath(Ivy.request().getApplication(), applicationCode, applicationModule);
    }
    catch (Exception e)
    {
      throw new AddonsRuntimeException(e);
    }
  }

  /**
   * It's simplified version of {@link ResourcesHelper#getApplicationResourcesRootPath(IApplication)}
   * where application is solved with {@code Ivy.request().getApplication()}
   * @return
   */
  public static String getApplicationResourcesRootPath()
  {
    try
    {
      return getApplicationResourcesRootPath(Ivy.request().getApplication());
    }
    catch (Exception e)
    {
      throw new AddonsRuntimeException(e);
    }
  }

  /**
   * It returns the absolute application resources root path according to following logic:<br/>
   * 
   * <code>{0}/res</code>
   * 
   * <br/>
   * where:
   * <ul>
   * <li>{0} is the resources absolute path; see {@link ResourcesHelper#getResourcesRootPath()}</li>
   * </ul>
   * 
   * @param application - the ivy application to look on
   * @return
   */
  public static String getApplicationResourcesRootPath(IApplication application)
  {
    try
    {

      List<Object> formatObjects = null;
      String applicationResourcesAbsoluteRootPath = null;

      // build the parameters to place on place holders
      formatObjects = new ArrayList<Object>();

      // resolve the resources absolute root path
      String resourcesAbsoluteRootPath = ResourcesHelper.getResourcesRootPath(application);
      formatObjects.add(resourcesAbsoluteRootPath);

      applicationResourcesAbsoluteRootPath = APPLICATION_RESOURCES_ROOT_PATH_FORMAT.format(formatObjects.toArray());

      return applicationResourcesAbsoluteRootPath;
    }
    catch (Exception e)
    {
      throw new AddonsRuntimeException(e);
    }
  }

  /**
   * It returns the absolute application resources path according to following logic:<br/>
   * 
   * <code>{0}/res/{1}/{2}</code>
   * 
   * <br/>
   * where:
   * <ul>
   * <li>{0} is the resources absolute path; see {@link ResourcesHelper#getResourcesRootPath()}</li>
   * <li>{1} is environment name; the default environment will be written as "Default"</li>
   * <li>{2} application (software or tower) code_module. Ex: xgov_a4y</li>
   * </ul>
   * 
   * @param applicationCode - the application code like xgov, xhrm, and so on. It can not be null or having
   *          empty string.
   * @param applicationModule - the application module (functional part of the given application) like a4y. It
   *          can be null.
   * 
   * @return the absolute path for the application resources
   * 
   */
  public static String getApplicationResourcesPath(IApplication application, String applicationCode,
          String applicationModule)
  {
    try
    {
      List<Object> formatObjects = null;
      String applicationResourcesAbsolutePath = null;
      String resourcesAbsolutePath = null;
      String activeEnvironment = null;
      String applicationCodeAndModule = null;

      // build the parameters to place on place holders
      formatObjects = new ArrayList<Object>();

      // resolve the resources absolute path
      resourcesAbsolutePath = ResourcesHelper.getResourcesRootPath(application);
      formatObjects.add(resourcesAbsolutePath);

      // resolve active environment
      activeEnvironment = EnvironmentHelper.getActiveEnvironment();
      if (activeEnvironment == null)
      {
        // this is the default environment
        activeEnvironment = EnvironmentHelper.getDefaultEnvironmentName();
      }
      formatObjects.add(activeEnvironment);

      // resolve application code and application module
      if (applicationCode == null || "".equals(applicationCode))
      {
        throw new AddonsRuntimeException("Application code can not be null or empty string");
      }

      applicationCodeAndModule = applicationCode;
      if (applicationModule != null && !("".equals(applicationModule)))
      {
        applicationCodeAndModule += "_" + applicationModule;
      }

      formatObjects.add(applicationCodeAndModule);

      applicationResourcesAbsolutePath = APPLICATION_RESOURCES_PATH_FORMAT.format(formatObjects.toArray());

      return applicationResourcesAbsolutePath;
    }
    catch (Exception e)
    {
      throw new AddonsRuntimeException(e);
    }
  }

  /**
   * It's simplified version of the method {@link ResourcesHelper#getCaseDocumentsPath(IApplication, Number)}
   * where application is solved with <code>Ivy.request().getApplication()</code>.
   * 
   * @param caseIdentifier
   * @return
   */
  public static String getCaseDocumentsPath(Number caseIdentifier)
  {
    try
    {
      return getCaseDocumentsPath(Ivy.request().getApplication(), caseIdentifier);
    }
    catch (Exception e)
    {
      throw new AddonsRuntimeException(e);
    }
  }

  /**
   * Case documents stands for documents like pdf files, bills, certificates, salary sheets and so on that are
   * visible in WFUI documents tab. The method returns the case documents absolute path according to following
   * logic:<br/>
   * 
   * <code>{0}/files/{1}/{2}/Documents</code>
   * 
   * <br/>
   * where:
   * <ul>
   * <li>{0} is the resources absolute path; see {@link ResourcesHelper#getResourcesRootPath()}</li>
   * <li>{1} is environment name; the default environment will be written as "Default"</li>
   * <li>{2} is the case identifier.</li>
   * </ul>
   * 
   * @param application - the ivy application to look on
   * @param caseIdentifier - the case identifier for which the case root folder absolute path has to be found
   * 
   * @return the absolute path for the case documents
   */
  public static String getCaseDocumentsPath(IApplication application, Number caseIdentifier)
  {
    String caseDocumentsAbsolutePath = getCaseSpecificSubPath(application, caseIdentifier,
            CASE_DOCUMENTS_PATH_FORMAT);

    return caseDocumentsAbsolutePath;
  }

  /**
   * It's simplified version of the method {@link ResourcesHelper#getCaseResourcesPath(IApplication, Number)}
   * where application is solved with <code>Ivy.request().getApplication()</code>.
   * 
   * @param caseIdentifier
   * @return
   */
  public static String getCaseResourcesPath(Number caseIdentifier)
  {
    try
    {
      return getCaseResourcesPath(Ivy.request().getApplication(), caseIdentifier);
    }
    catch (Exception e)
    {
      throw new AddonsRuntimeException(e);
    }
  }

  /**
   * Case documents stands for documents like pdf files, bills, certificates, salary sheets and so on that are
   * visible in WFUI documents tab. The method returns the case documents absolute path according to following
   * logic:<br/>
   * 
   * <code>{0}/files/{1}/{2}/Resources</code>
   * 
   * <br/>
   * where:
   * <ul>
   * <li>{0} is the resources absolute path; see {@link ResourcesHelper#getResourcesRootPath()}</li>
   * <li>{1} is environment name; the default environment will be written as "Default"</li>
   * <li>{2} is the case identifier.</li>
   * </ul>
   * 
   * @param application - the ivy application to look on
   * @param caseIdentifier - the case identifier for which the case root folder absolute path has to be found
   * 
   * @return the absolute path for the case documents
   */
  public static String getCaseResourcesPath(IApplication application, Number caseIdentifier)
  {
    String caseDocumentsAbsolutePath = getCaseSpecificSubPath(application, caseIdentifier,
            CASE_RESOURCES_PATH_FORMAT);

    return caseDocumentsAbsolutePath;
  }

  private static String getCaseSpecificSubPath(IApplication application, Number caseIdentifier,
          MessageFormat format)
  {
    try
    {
      List<Object> formatObjects;
      String resourcesAbsolutePath;
      String activeEnvironment;
      String caseSpecificSubAbsolutePath = null;

      // build the parameters to place on place holders
      formatObjects = new ArrayList<Object>();

      // resolve the resources absolute path
      resourcesAbsolutePath = ResourcesHelper.getResourcesRootPath(application);
      formatObjects.add(resourcesAbsolutePath);

      // define environment
      activeEnvironment = EnvironmentHelper.getActiveEnvironment();
      if (activeEnvironment == null)
      {
        // this is the default environment
        activeEnvironment = EnvironmentHelper.getDefaultEnvironmentName();
      }
      formatObjects.add(activeEnvironment);

      if (caseIdentifier == null || caseIdentifier.intValue() <= 0)
      {
        throw new AddonsRuntimeException("Case identifier can not be null or negative.");
      }
      // attention: the case identifier should not be formatted; ex: 1'034
      formatObjects.add("" + caseIdentifier.intValue());

      caseSpecificSubAbsolutePath = format.format(formatObjects.toArray());

      return caseSpecificSubAbsolutePath;
    }
    catch (Exception e)
    {
      throw new AddonsRuntimeException(e);
    }

  }


  /**
   * It returns the resources absolute root path according to following rule:
   * <ol>
   * <li>It returns the configured in the global variable <code>xivy_addons_resourcesPath,</code>
   * </li>
   * <li>If not configured, then it returns the <code>{application file area}.</code></li>
   * </ol>
   * @return resources absolute path
   */
  public static String getResourcesRootPath(IApplication application)
  {
    try
    {
      IGlobalVariable globalVariable = null;
      String resourcesPath = null;

      globalVariable = application.findGlobalVariable(RESOURCES_PATH_GLOBAL_VARIABLE_NAME);

      if (globalVariable != null && globalVariable.getValue() != null)
      {
        resourcesPath = globalVariable.getValue();
      }

      // if global variable is not set then use the application file area
      if (resourcesPath == null || "".equals(resourcesPath))
      {
        resourcesPath = PublicAPIHelper.getApplicationFileAreaAbsolutePath();
      }

      return resourcesPath;

    }
    catch (Exception e)
    {
      throw new AddonsRuntimeException(e);
    }

  }

}