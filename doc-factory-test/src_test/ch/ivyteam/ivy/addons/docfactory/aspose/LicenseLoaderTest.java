package ch.ivyteam.ivy.addons.docfactory.aspose;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.environment.Ivy;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Ivy.class, ThirdPartyLicenses.class})
public class LicenseLoaderTest {

  @Before
  public void setup() throws Exception {
    mockStatic(ThirdPartyLicenses.class);
    when(ThirdPartyLicenses.getDocumentFactoryLicense()).thenReturn(null);
    LicenseLoader.clearLoadedLicenses();
  }

  @Test
  public void before_loading_licenses_they_are_not_loaded() {
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS), is(false));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF), is(false));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS), is(false));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES), is(false));
  }

  @Test
  public void after_loadLicenseForAllProducts_licenses_are_loaded() throws Exception {
    LicenseLoader.loadLicenseForAllProducts();

    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS), is(true));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF), is(true));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS), is(true));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES), is(true));
  }

  @Test
  public void clearLoadedLicenses_unload_all_licenses() throws Exception {
    LicenseLoader.loadLicenseForAllProducts();

    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS), is(true));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF), is(true));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS), is(true));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES), is(true));

    LicenseLoader.clearLoadedLicenses();

    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS), is(false));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF), is(false));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS), is(false));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES), is(false));
  }

  @Test
  public void after_loadLicenseForWords_wordsLicenseIsLoaded() throws Exception {
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS), is(false));

    LicenseLoader.loadLicenseforProduct(AsposeProduct.WORDS);

    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS), is(true));

    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF), is(false));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS), is(false));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES), is(false));
  }

  @Test
  public void after_loadLicenseForPdf_pdfLicenseIsLoaded() throws Exception {
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF), is(false));

    LicenseLoader.loadLicenseforProduct(AsposeProduct.PDF);

    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF), is(true));

    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS), is(false));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS), is(false));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES), is(false));
  }

  @Test
  public void after_loadLicenseForCells_cellsLicenseIsLoaded() throws Exception {
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS), is(false));

    LicenseLoader.loadLicenseforProduct(AsposeProduct.CELLS);

    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.CELLS), is(true));

    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS), is(false));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF), is(false));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES), is(false));
  }

  @Test
  public void after_loadLicenseForSlides_slidesLicenseIsLoaded() throws Exception {
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES), is(false));

    LicenseLoader.loadLicenseforProduct(AsposeProduct.SLIDES);

    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.SLIDES), is(true));

    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.WORDS), is(false));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF), is(false));
    assertThat(LicenseLoader.isLicenseForProductAlreadyLoaded(AsposeProduct.PDF), is(false));
  }

}
