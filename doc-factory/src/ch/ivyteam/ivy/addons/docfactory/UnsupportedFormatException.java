package ch.ivyteam.ivy.addons.docfactory;

public class UnsupportedFormatException extends Exception {

  private static final long serialVersionUID = -713589582184992872L;

  private String message;

  public UnsupportedFormatException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  public void setMessage(String m) {
    this.message = m;
  }
}
