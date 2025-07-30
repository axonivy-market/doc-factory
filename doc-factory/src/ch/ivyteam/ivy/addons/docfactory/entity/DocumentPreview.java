package ch.ivyteam.ivy.addons.docfactory.entity;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

public class DocumentPreview {
  private String fileName;
  private String contentType;
  private byte[] fileContent;

  public DocumentPreview(String fileName, String contentType, byte[] fileContent) {
    this.fileName = fileName;
    this.contentType = contentType;
    this.fileContent = fileContent;
  }

  public DocumentPreview(FileUploadEvent event) {
    this.fileName = event.getFile().getFileName();
    this.contentType = event.getFile().getContentType();
    this.fileContent = event.getFile().getContent();
  }

  public DocumentPreview(UploadedFile file) {
    this.fileName = file.getFileName();
    this.contentType = file.getContentType();
    this.fileContent = file.getContent();
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public byte[] getFileContent() {
    return fileContent;
  }

  public void setFileContent(byte[] fileContent) {
    this.fileContent = fileContent;
  }

}
