package ch.ivyteam.ivy.addons.docfactory.pdfbox;

import java.io.IOException;

import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.parser.PreflightParser;

public class PdfAValidator {

  /**
   * This method checks the validity of the PDF/A files.
   * https://pdfbox.apache.org/1.8/cookbook/pdfavalidation.html
   * @param pdf the file to validate
   * @return true if pdf box can validate the given file. Else false.
   * @throws IOException
   */
  public static boolean isPDFACompliant(java.io.File pdf) throws IOException {
    boolean isValid = false;
    PreflightDocument document = null;
    try {
      PreflightParser parser = new PreflightParser(pdf);
      parser.parse();
      document = parser.getPreflightDocument();
      document.validate();
      isValid = document.getResult().isValid();
    } catch (IOException e) {

    } finally {
      if (document != null) {
        document.close();
      }
    }
    return isValid;
  }

}
