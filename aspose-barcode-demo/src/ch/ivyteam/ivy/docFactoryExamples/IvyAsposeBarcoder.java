package ch.ivyteam.ivy.docFactoryExamples;

import java.io.InputStream;

import ch.ivyteam.ivy.ThirdPartyLicenses;

/*
 * Initialize the licence for the barcode library
 */
public class IvyAsposeBarcoder {

  public static void init() throws Exception {
    InputStream in = ThirdPartyLicenses.getDocumentFactoryLicense();
    com.aspose.barcode.License lic = new com.aspose.barcode.License();
    if (in != null) {
      lic.setLicense(in);
    }

  }
}
