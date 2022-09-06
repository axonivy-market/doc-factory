package ch.ivyteam.ivy.addons.docfactory.aspose;

import static org.assertj.core.api.Assertions.assertThat;

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
}
