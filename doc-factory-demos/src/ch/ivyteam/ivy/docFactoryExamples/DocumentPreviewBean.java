package ch.ivyteam.ivy.docFactoryExamples;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.commons.lang3.ObjectUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import ch.ivyteam.ivy.addons.docfactory.entity.FileReviewEntity;
import ch.ivyteam.ivy.process.call.SubProcessCall;
import ch.ivyteam.ivy.process.call.SubProcessCallResult;

@ManagedBean
@SessionScoped
public class DocumentPreviewBean {
  private StreamedContent streamedContent;
  private boolean image;

  @PostConstruct
  public void init() {
    image = false;
    streamedContent = null;
  }

  public void handleFileUpload(FileUploadEvent event) throws Exception {
    String fileName = event.getFile().getFileName();
    String contentType = event.getFile().getContentType();
    byte[] fileContent = event.getFile().getContent();
    FileReviewEntity entity = new FileReviewEntity(fileName, contentType, fileContent);
    SubProcessCallResult callResult = SubProcessCall.withPath("Functional Processes/reviewDocument")
        .withStartName("reviewDocument").withParam("fileReviewEntity", entity).call();
    image = contentType != null && contentType.startsWith("image/");
    streamedContent = (StreamedContent) callResult.get("streamedContent");
  }

  public StreamedContent getStreamedContent() {
    return streamedContent;
  }

  public boolean isImage() {
    return image;
  }

  public void setImage(boolean image) {
    this.image = image;
  }

  public boolean isEmpty() {
    return ObjectUtils.isEmpty(streamedContent);
  }
}
