package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import ch.ivyteam.ivy.ThirdPartyLicenses;

public final class LicenseLoader {

  private final static Map<AsposeProduct, Object> LOADED_ASPOSE_LICENSES = new HashMap<>();
  private static boolean skipLicenseLoading = false;
  
  private LicenseLoader() {}

  public static void loadLicenseforProduct(AsposeProduct product) throws Exception {
    if (skipLicenseLoading || isLicenseForProductAlreadyLoaded(product)) {
      return;
    }
    InputStream in = ThirdPartyLicenses.getDocumentFactoryLicense();

    switch (product) {
      case WORDS:
        LOADED_ASPOSE_LICENSES.put(product, loadAsposeWordsLicense(in));
        break;
      case PDF:
        LOADED_ASPOSE_LICENSES.put(product, loadAsposePdfLicense(in));
        break;
      case CELLS:
        LOADED_ASPOSE_LICENSES.put(product, loadAsposeCellsLicense(in));
        break;
      case SLIDES:
        LOADED_ASPOSE_LICENSES.put(product, loadAsposeSlidesLicense(in));
        break;
      case EMAILS:
        LOADED_ASPOSE_LICENSES.put(product, loadAsposeEmailsLicense(in));
      default:
        break;
    }
  }
  
  public static void setSkipLicenseLoading(boolean skipLicenseLoading) {
    LicenseLoader.skipLicenseLoading = skipLicenseLoading;
  }

  public static void loadLicenseForAllProducts() throws Exception {
    for (AsposeProduct product : AsposeProduct.values()) {
      loadLicenseforProduct(product);
    }
  }

  public static boolean isLicenseForProductAlreadyLoaded(AsposeProduct product) {
    return LOADED_ASPOSE_LICENSES.containsKey(product);
  }

  /**
   * Clears the internal loaded licenses store.
   */
  public static void clearLoadedLicenses() {
    LOADED_ASPOSE_LICENSES.clear();
  }

  private static com.aspose.words.License loadAsposeWordsLicense(InputStream in) throws Exception {
    com.aspose.words.License lic = new com.aspose.words.License();
    if (in != null) {
      lic.setLicense(in);
    }
    return lic;
  }

  private static com.aspose.cells.License loadAsposeCellsLicense(InputStream in) throws Exception {
    com.aspose.cells.License lic = new com.aspose.cells.License();
    if (in != null) {
      lic.setLicense(in);
    }
    return lic;
  }

  private static com.aspose.pdf.License loadAsposePdfLicense(InputStream in) throws Exception {
    com.aspose.pdf.License lic = new com.aspose.pdf.License();
    if (in != null) {
      lic.setLicense(in);
    }
    return lic;
  }

  private static com.aspose.slides.License loadAsposeSlidesLicense(InputStream in) throws Exception {
    com.aspose.slides.License lic = new com.aspose.slides.License();
    if (in != null) {
      lic.setLicense(in);
    }
    return lic;
  }
  
  private static com.aspose.email.License loadAsposeEmailsLicense(InputStream in) throws Exception {
    com.aspose.email.License lic = new com.aspose.email.License();
    if (in != null) {
      lic.setLicense(in);
    }
    return lic;
  }
}
