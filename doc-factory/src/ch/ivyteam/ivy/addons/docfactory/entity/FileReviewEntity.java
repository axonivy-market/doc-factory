package ch.ivyteam.ivy.addons.docfactory.entity;

public class FileReviewEntity {
  private String fileName;
  private String contentType;
  private byte[] fileContent;

  public FileReviewEntity(String fileName, String contentType, byte[] fileContent) {
    this.fileName = fileName;
    this.contentType = contentType;
    this.fileContent = fileContent;
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
