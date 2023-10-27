package ch.ivyteam.ivy.docFactoryExamples;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.aspose.email.Attachment;
import com.aspose.email.MailAddress;
import com.aspose.email.MailMessage;
import com.aspose.email.MapiMessage;
import com.aspose.email.MapiMessageFlags;

import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.environment.Ivy;
import doc.factory.demos.OutlockMailData;

/*
 * Initialize the licence for the aspose.email library
 */
public class IvyAsposeEmailer {

  public static void init() throws Exception {
    InputStream in = ThirdPartyLicenses.getDocumentFactoryLicense();
    com.aspose.email.License lic = new com.aspose.email.License();
    if (in != null) {
      lic.setLicense(in);
    }
  }

  // create an outlock .msg file
  public static void createMail(OutlockMailData data) {

    // Create a new instance of MailMessage class
    MailMessage message = new MailMessage();

    // Set sender information
    message.setFrom(new MailAddress("from@axonivy.com", "Sender Name", false));

    // Add recipients
    message.getTo().addMailAddress(new MailAddress(data.getEmailaddress(), data.getName(), false));

    // Set subject of the message
    message.setSubject("New message created by Aspose.Email for Java");

    // Set Html body of the message
    message.setBody("Dear " + data.getName() + "\nThis is just a short note for you.");

    if(data.getAttachment()!=null)
    {
      // get ivyFile from the files directory
      Attachment attachment = new Attachment(data.getAttachment().getAbsolutePath()); // 
      message.addAttachment(attachment);
    }	
    
    // Create an instance of MapiMessage and load the MailMessag instance into
    // it
    MapiMessage mapiMsg = MapiMessage.fromMailMessage(message);

    // Set the MapiMessageFlags as UNSENT and FROMME
    mapiMsg.setMessageFlags(MapiMessageFlags.MSGFLAG_UNSENT | MapiMessageFlags.MSGFLAG_FROMME);

    // Save the MapiMessage to disk
    mapiMsg.save(data.getMsgFile().getAbsolutePath()); 
  }

  // send a outlock msg file as response to the web browser with mime type in
  // the header
  public static void downloadMsgFile(java.io.File file) throws IOException {
    if (file == null) {
      Ivy.log().info("Template NULL");
      return;
    }

    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

    response.reset();

    response.setHeader("Content-Type",
            "application/vnd.openxmlformats");

    response.setHeader("Content-Disposition",
            "attachment;filename=" + file.getName());

    OutputStream responseOutputStream = response.getOutputStream();
    responseOutputStream.write(Files.readAllBytes(file.toPath()));
    responseOutputStream.flush();
    responseOutputStream.close();
    facesContext.responseComplete();
  }
}
