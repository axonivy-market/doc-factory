package ch.ivyteam.ivy.addons.docfactory.aspose;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
class LicenseLoaderTest {

  @BeforeEach
  public void setup() throws Exception {
    LicenseLoader.clearLoadedLicenses();
  }

  @Test
  public void before_loading_licenses_they_are_not_loaded() {
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS)).isFalse();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF)).isFalse();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS)).isFalse();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES)).isFalse();
  }

  @Test
  public void after_loadLicenseForAllProducts_licenses_are_loaded() throws Exception {
    LicenseLoader.loadLicenseForAllProducts();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS)).isTrue();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF)).isTrue();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS)).isTrue();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES)).isTrue();
  }

  @Test
  public void clearLoadedLicenses_unload_all_licenses() throws Exception {
    LicenseLoader.loadLicenseForAllProducts();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS)).isTrue();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF)).isTrue();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS)).isTrue();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES)).isTrue();

    LicenseLoader.clearLoadedLicenses();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS)).isFalse();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF)).isFalse();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS)).isFalse();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES)).isFalse();
  }

  @Test
  public void after_loadLicenseForWords_wordsLicenseIsLoaded() throws Exception {
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS)).isFalse();
    LicenseLoader.loadLicenseforProduct(AsposeProduct.WORDS);

    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS)).isTrue();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF)).isFalse();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS)).isFalse();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES)).isFalse();
  }

  @Test
  public void after_loadLicenseForPdf_pdfLicenseIsLoaded() throws Exception {
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF)).isFalse();

    LicenseLoader.loadLicenseforProduct(AsposeProduct.PDF);
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF)).isTrue();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS)).isFalse();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS)).isFalse();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES)).isFalse();
  }

  @Test
  public void after_loadLicenseForCells_cellsLicenseIsLoaded() throws Exception {
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS)).isFalse();

    LicenseLoader.loadLicenseforProduct(AsposeProduct.CELLS);
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS)).isTrue();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS)).isFalse();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF)).isFalse();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES)).isFalse();
  }

  @Test
  public void after_loadLicenseForSlides_slidesLicenseIsLoaded() throws Exception {
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES)).isFalse();

    LicenseLoader.loadLicenseforProduct(AsposeProduct.SLIDES);
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES)).isTrue();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS)).isFalse();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF)).isFalse();
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS)).isFalse();
  }

  @Test
  void ensureNotEmptyStream_shouldReturnNull_whenInputIsNull() {
    InputStream result = LicenseLoader.ensureNotEmptyStream(null);
    assertNull(result, "Expected null when input stream is null");
  }

  @Test
  void ensureNotEmptyStream_shouldReturnNull_whenStreamIsEmpty() {
    InputStream emptyStream = new ByteArrayInputStream(new byte[0]);
    InputStream result = LicenseLoader.ensureNotEmptyStream(emptyStream);
    assertNull(result, "Expected null for empty input stream");
  }

  @Test
  void ensureNotEmptyStream_shouldReturnBufferedStream_whenStreamNotEmpty() throws Exception {
    final String CONTENT = "abc";
    byte[] data = CONTENT.getBytes();
    InputStream original = new ByteArrayInputStream(data);

    InputStream result = LicenseLoader.ensureNotEmptyStream(original);

    assertNotNull(result, "Non-empty stream should return a stream");
    assertTrue(result instanceof BufferedInputStream, "Result must be a BufferedInputStream");

    byte[] read = result.readNBytes(CONTENT.length());
    assertArrayEquals(data, read, "Stream should still contain all original data");
  }

  @Test
  void ensureNotEmptyStream_shouldReturnNull_whenStreamThrowsException() {
    InputStream brokenStream = new InputStream() {
      @Override
      public int read() throws IOException {
        throw new IOException("error");
      }
    };

    InputStream result = LicenseLoader.ensureNotEmptyStream(brokenStream);
    assertNull(result, "If stream throws exception, method must return null");
  }
}
