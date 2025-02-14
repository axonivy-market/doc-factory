package ch.ivyteam.ivy.addons.docfactory.pdfbox;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.parser.PreflightParser;

public class PdfAValidator {

  /**
   * This method checks the validity of the PDF/A files.
   * @param pdf the file to validate
   * @return true if PDF box can validate the given file. Else false.
   * @throws IOException
   */
  public static boolean isPDFACompliant(File pdf) throws IOException {
    ValidationResult validationResult = PreflightParser.validate(pdf);
    return validationResult.isValid();
  }

}