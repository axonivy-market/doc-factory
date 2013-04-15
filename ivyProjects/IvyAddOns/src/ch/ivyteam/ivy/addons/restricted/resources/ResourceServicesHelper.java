package ch.ivyteam.ivy.addons.restricted.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import ch.ivyteam.ivy.addons.application.resources.ResourcesHelper;
import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.FileManager.FileManagerPanel;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.workflow.ICase;

/**
 * 
 * It's helper class related to the ResourceServices Sub Process.
 * 
 * @author Rifat Binjos, TI-Informatique
 * @since March 2010
 * 
 */
public class ResourceServicesHelper
{

  private static final char[] ILLEGAL_CHARACTERS = {'/', '\n', '\r', '\t', '\0', '\f', '`', '?', '\\', '<',
      '>', '|', '\"', ':', '!', '*'};

  private static final String REGEXP_ILLEGAL_CHARACTERS = "/\n\r\t\0\f`?\\\\<>|\":!\\*";

  private static final Pattern ILLEGAL_CHARACTERS_PATTERN = Pattern.compile(new String("[^"
          + REGEXP_ILLEGAL_CHARACTERS + "]*"));

  private static final Pattern CHARACTERS_TO_REPLACE_PATTERN = Pattern.compile(new String("["
          + REGEXP_ILLEGAL_CHARACTERS + "]*"));

  public static String readApplicationResourcesPath(String applicationCode, String applicationModule)
  {
    return ResourcesHelper.getApplicationResourcesPath(applicationCode, applicationModule);
  }

  public static String readCaseDocumentsPath(Number caseIdentifier)
  {
    return ResourcesHelper.getCaseDocumentsPath(caseIdentifier);
  }

  public static String readCaseResourcesPath(Number caseIdentifier)
  {
    return ResourcesHelper.getCaseResourcesPath(caseIdentifier);
  }

  /**
   * It creates the case root directory and:
   * <ul>
   * <li>If business main contact name is set, then create the sub directory for it,</li>
   * <li>If business object name is set, then create the sub directory for it.</li>
   * </ul>
   * 
   * @param fileManagerRDC - the File Manager Rich Dialog instance that will be used to create folder
   *          (makeDirectory). <b>Important:</b> He needs to have its server path set.
   * 
   * @param wfCase - the case for which directories has to be created including the check if business main
   *          contact and business object name are set
   * 
   * @throws Exception
   */
  public static void makeDefaultCaseDirectories(FileManagerPanel fileManagerRDC, ICase wfCase)
          throws Exception
  {
    // define the server path
    String serverPath = readCaseDocumentsPath(wfCase.getIdentifier());
    Object callMethodParams[] = null;

    // Object callMethodParams[] = new Object[1];
    // callMethodParams[0] = serverPath;
    //		
    // fileManagerRDC.getPanelAPI().callMethod("setServerPath", callMethodParams);
    //				
    // Ivy.log().debug("The file manager server path is {0}.", serverPath);

    java.io.File directoryToCreateFile;
    String standardizedDirectoryToCreate;

    // build the list of default case directories to create
    List<String> directoriesToCreate = new ArrayList<String>();

    if (wfCase.getBusinessMainContactName() != null && wfCase.getBusinessMainContactName().length() > 0)
    {
      directoriesToCreate.add(wfCase.getBusinessMainContactName());
    }

    if (wfCase.getBusinessObjectName() != null && wfCase.getBusinessObjectName().length() > 0)
    {
      directoriesToCreate.add(wfCase.getBusinessObjectName());
    }

    for (String directoryToCreate : directoriesToCreate)
    {
      if (directoryToCreate.length() > 0)
      {
        standardizedDirectoryToCreate = ResourceServicesHelper.isValidDirectoryName(directoryToCreate) ? directoryToCreate
                : ResourceServicesHelper.standardizeDirectoryName(directoryToCreate);

        directoryToCreateFile = new java.io.File(FileHandler.formatPathWithEndSeparator(serverPath, false)
                + standardizedDirectoryToCreate);

        if (!directoryToCreateFile.exists())
        {
          Ivy.log().info("Case documents: creating of sub directory {0} in {1}",
                  standardizedDirectoryToCreate, serverPath);

          callMethodParams = new Object[1];
          callMethodParams[0] = standardizedDirectoryToCreate;

          fileManagerRDC.getPanelAPI().callMethod("makeDirectory", callMethodParams);

        }
        else
        {
          Ivy.log().info("Case documents: the sub directory {0} of {1} exists already.", directoryToCreate,
                  serverPath);
        }
      }
    }
  }

  /**
   * This method return true if the directory name is valid
   * @param directoryName is the directory name to validate
   * @return true or false
   */
  public static boolean isValidDirectoryName(String directoryName)
  {
    return ILLEGAL_CHARACTERS_PATTERN.matcher(directoryName).matches();
  }

  /**
   * It returns the "normalized" directory name, it means whithout illegal caracters that are not allowed in
   * the folder creation.
   * 
   * @param invalidDirectoryName is the directory name to "standardize"
   * @return the "standardized" directory name
   */
  public static String standardizeDirectoryName(String invalidDirectoryName)
  {
    return invalidDirectoryName.replaceAll(CHARACTERS_TO_REPLACE_PATTERN.pattern(), "");

  }

  /**
   * It returns true if the folder contains files, otherwise false. The check is done on all files including
   * those in sub directories.
   * 
   * @param directoryPath The absolute path of the directory on which search has to be done
   * @return
   * 
   */
  public static Boolean folderContainsFiles(String directoryPath)
  {
    File myDir = new File(directoryPath);
    File currentFile = null;
    Boolean containsFiles = false;

    if (myDir.isDirectory())
    {
      // get all files AND all sub directories are searched as well
      Iterator<File> iterator = FileUtils.iterateFiles(myDir, null, true);

      while (iterator.hasNext())
      {
        currentFile = iterator.next();
        Ivy.log().debug("Analyzing the {0}-{1}.", (currentFile.isFile() ? "File" : "Directory"),
                currentFile.getAbsolutePath());
        if (currentFile.isFile())
        {
          containsFiles = true;
          break;
        }
      }
    }

    return containsFiles;
  }

  public static void main(String args[])
  {
    for (int i = 0; i < ILLEGAL_CHARACTERS.length; i++)
    {
      String fileName = "Bond " + ILLEGAL_CHARACTERS[i] + "James";
      System.out.println(fileName + " is valid file name? : " + isValidDirectoryName(fileName)
              + standardizeDirectoryName(fileName));
    }
  }

}
