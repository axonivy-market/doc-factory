package ch.ivyteam.ivy;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ch.ivyteam.ivy.docfactory.Server;

public class ThirdPartyLicenses {

    /**
     * Returns the document factory license as inputStream to be used in the different aspose products.
     * @return
     * @throws FileNotFoundException 
     */
    public static InputStream getDocumentFactoryLicense() throws FileNotFoundException {
    	if(!Server.get().isRunning()) {
    		return new ByteArrayInputStream(new byte[0]); // empty license
    	}
        return ThirdPartyLicenses.class.getResourceAsStream("/docfactory_2015_08_28.lic");
    }

    /**
     * @Deprecated Will be removed in the future.
     */
    @Deprecated
    public static boolean isAsposeLicenseValidForAsposeWords() throws Exception {
        return true;
    }
    
    
    
}
