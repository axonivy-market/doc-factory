package ch.ivyteam.ivy.docFactoryExamples.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.primefaces.model.file.UploadedFile;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;

public class FilesUtil
{
  /*
   * set file reference into the session
   */
  public static void setFileRef(File ivyFile)
  {
    Ivy.session().setAttribute("docRef", Ivy.html().fileref(ivyFile));
    Ivy.session().setAttribute("docFilename", ivyFile.getName());
  }

  /*
   * set file reference into the session
   */
  public static void setFileRef(java.io.File file) throws IOException
  {
    File ivyFile = new File(file.getParentFile().getName() + "/" + file.getName(), false);
    setFileRef(ivyFile);
  }

  public static ch.ivyteam.ivy.scripting.objects.File primeToIvyFile(UploadedFile file)
          throws IOException
  {
    var ivyFile = new ch.ivyteam.ivy.scripting.objects.File(file.getFileName(), true);
    FileUtils.writeByteArrayToFile(ivyFile.getJavaFile(), file.getContent());
    return ivyFile;
  }

  public static void downloadJsf(java.io.File file) throws IOException
  {
    if (file == null) {
      Ivy.log().info("Template NULL");
      return;
    }

    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

    response.reset();
    response.setHeader("Content-Type",
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    response.setHeader("Content-Disposition",
      "attachment;filename=" + file.getName());

    OutputStream responseOutputStream = response.getOutputStream();
    responseOutputStream.write(Files.readAllBytes(file.toPath()));
    responseOutputStream.flush();
    responseOutputStream.close();
    facesContext.responseComplete();
  }

}
