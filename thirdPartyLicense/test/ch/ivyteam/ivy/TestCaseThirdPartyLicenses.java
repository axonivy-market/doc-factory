package ch.ivyteam.ivy;


import org.junit.Test;

import com.aspose.words.License;

/**
 * @author mda
 * @since 09.07.2009
 */
public class TestCaseThirdPartyLicenses
{
  @Test
  public void testAsposeLicenseCanBeRead() throws Exception
  {
    License license = new License();
    license.setLicense(ThirdPartyLicenses.getAsposeLicenseStream());
  }
  
  @Test(expected=Exception.class)
  public void testLicenseIsOnlyValidOnServer() throws Exception
  {
    License license = new License();
    license.setLicense(ThirdPartyLicenses.getDocumentFactoryLicense());
  }
  
}
