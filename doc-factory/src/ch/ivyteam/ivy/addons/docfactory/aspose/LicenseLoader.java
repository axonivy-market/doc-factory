package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.environment.Ivy;

public final class LicenseLoader {

  private final static Map<AsposeProduct, Object> LOADED_ASPOSE_LICENSES = new HashMap<>();

  private LicenseLoader() {}

  public static void loadLicenseforProduct(AsposeProduct product) throws Exception {
    if (isLicenseForProductAlreadyLoaded(product)) {
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
      case EMAIL:
        LOADED_ASPOSE_LICENSES.put(product, loadAsposeEmailsLicense(in));
        break;
      default:
        break;
    }
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
    InputStream safeStream = ensureNotEmptyStream(in);
    if (safeStream != null) {
      lic.setLicense(safeStream);
    }
    return lic;
  }

  private static com.aspose.cells.License loadAsposeCellsLicense(InputStream in) throws Exception {
    com.aspose.cells.License lic = new com.aspose.cells.License();
    InputStream safeStream = ensureNotEmptyStream(in);
    if (safeStream != null) {
      lic.setLicense(safeStream);
    }
    return lic;
  }

  private static com.aspose.pdf.License loadAsposePdfLicense(InputStream in) throws Exception {
    com.aspose.pdf.License lic = new com.aspose.pdf.License();
    InputStream safeStream = ensureNotEmptyStream(in);
    if (safeStream != null) {
      lic.setLicense(safeStream);
    }
    return lic;
  }

  private static com.aspose.slides.License loadAsposeSlidesLicense(InputStream in) throws Exception {
    com.aspose.slides.License lic = new com.aspose.slides.License();
    InputStream safeStream = ensureNotEmptyStream(in);
    if (safeStream != null) {
      lic.setLicense(safeStream);
    }
    return lic;
  }

  private static com.aspose.email.License loadAsposeEmailsLicense(InputStream in) throws Exception {
    com.aspose.email.License lic = new com.aspose.email.License();
    InputStream safeStream = ensureNotEmptyStream(in);
    if (safeStream != null) {
      lic.setLicense(safeStream);
    }
    return lic;
  }

  public static InputStream ensureNotEmptyStream(InputStream in) {
    if (in == null) {
      return null;
    }
    try {
      BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
      bufferedInputStream.mark(1);
      int firstByte = bufferedInputStream.read();
      if (firstByte == -1) {
        bufferedInputStream.close();
        return null;
      }
      bufferedInputStream.reset();
      return bufferedInputStream;
    } catch (Exception e) {
      closeInputStream(in);
      Ivy.log().error("There is an error when check empty stream: " + e.getMessage());
      return null;
    }
  }

  private static void closeInputStream(InputStream in) {
    try {
      in.close();
    } catch (Exception ex) {
      Ivy.log().error("Cannot close the inputStream: " + ex.getMessage());
    }
  }
}
