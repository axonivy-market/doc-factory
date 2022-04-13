package ch.ivyteam.ivy.addons.docfactory;

import java.io.File;

import ch.ivyteam.ivy.scripting.objects.List;

/**
 * @author ec
 * @since 27.10.2009 This class describes an object that can be returned after
 *        any operation on or about Files (java.io.File)<br>
 *        Such an operation can be an Upload, download, a letter generation with
 *        a document factory...
 *
 */
public class FileOperationMessage {

  /** the message that is returned by the operation */
  private String message;
  /** the Files that can be implied, created etc... by the operation */
  private List<java.io.File> files;
  /**
   * the type of the message (SUCCESS_MESSAGE, ERROR_MESSAGE,
   * INFORMATION_MESSAGE)
   */
  private int type;
  /** */
  public static final int SUCCESS_MESSAGE = 1;
  /** */
  public static final int ERROR_MESSAGE = 2;
  /** */
  public static final int INFORMATION_MESSAGE = 3;

  /**
   * Constructor Generates the fileOperationMessage object with empty message,
   * empty File List<br>
   * and as an information Type
   */
  public FileOperationMessage() {
    this("", List.create(java.io.File.class), 3);

  }

  /**
   * Constructor with the message, the List of the Files and the type
   * @param _message
   * @param _files if null the inner files list will be an empty files list
   * @param _type
   */
  public FileOperationMessage(String _message, List<File> _files, int _type) {
    if (_message != null) {
      this.message = _message;
    } else {
      this.message = "";
    }

    if (_files != null) {
      this.files = _files;
    } else {
      this.files = List.create(java.io.File.class);
    }

    if (SUCCESS_MESSAGE <= _type && _type <= INFORMATION_MESSAGE) {
      // if type indicated supported
      this.type = _type;
    } else {
      // type indicated not supported, we set the type to Information's one
      this.type = 3;
    }

  }

  /**
   * @return the files
   */
  public List<java.io.File> getFiles() {
    if (this.files == null) {
      this.files = List.create(java.io.File.class);
    }
    return files;
  }

  /**
   * @param _files the files to set, if null the files list will be cleared.
   */
  public void setFiles(List<java.io.File> _files) {
    if (_files != null) {
      this.getFiles().clear();
      this.files.addAll(_files);
    } else {
      this.files = List.create(java.io.File.class);
    }
  }

  /**
   * Clears the files List and creates the file list if it is null.
   */
  public void emptyFileList() {
    if (this.files == null) {
      this.files = List.create(java.io.File.class);
    } else {
      this.files.clear();
    }

  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * @return the type
   */
  public int getType() {
    return type;
  }

  /**
   * @param type the type to set
   */
  public void setType(int type) {
    this.type = type;
  }

  /**
   * add a file to the list
   * @param _file
   */
  public void addFile(java.io.File _file) {
    if (_file != null) {
      this.getFiles().add(_file);
    }
  }

  /**
   * add files to the list
   * @param list
   */
  public void addFiles(java.util.List<File> list) {
    if (list != null) {
      this.getFiles().addAll(list);
    }
  }

  /**
   * returns true if the fileOperationMessage represents a success message
   * @return true if the fileOperationMessage represents a success message, else
   *         false
   */
  public boolean isSuccess() {
    return this.type == SUCCESS_MESSAGE;
  }

  /**
   * returns true if the fileOperationMessage represents an error message
   * @return true if the fileOperationMessage represents an error message, else
   *         false
   */
  public boolean isError() {
    return this.type == ERROR_MESSAGE;
  }

  /**
   * returns true if the fileOperationMessage represents an information message
   * @return true if the fileOperationMessage represents an information message,
   *         else false
   */
  public boolean isInformation() {
    return this.type == INFORMATION_MESSAGE;
  }

  public static FileOperationMessage generateInformationTypeFileOperationMessage(String message) {
    return new FileOperationMessage(message, null, INFORMATION_MESSAGE);
  }

  public static FileOperationMessage generateSuccessTypeFileOperationMessage(String message) {
    return new FileOperationMessage(message, null, SUCCESS_MESSAGE);
  }

  public static FileOperationMessage generateErrorTypeFileOperationMessage(String message) {
    return new FileOperationMessage(message, null, ERROR_MESSAGE);
  }

}
