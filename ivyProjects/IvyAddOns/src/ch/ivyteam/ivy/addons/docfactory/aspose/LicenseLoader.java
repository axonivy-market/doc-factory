package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.io.InputStream;

import ch.ivyteam.ivy.ThirdPartyLicenses;

public class LicenseLoader {
	
	private LicenseLoader() {}
	
	public static void loadLicenseforProduct(AsposeProduct product) throws Exception {
		InputStream in = ThirdPartyLicenses.getDocumentFactoryLicense();
		
		switch (product) {
		case WORDS:
			new com.aspose.words.License().setLicense(in);
			break;
		case PDF:
			new com.aspose.pdf.License().setLicense(in);
			break;
		case CELLS:
			new com.aspose.cells.License().setLicense(in);
			break;
		case SLIDES:
			new com.aspose.slides.License().setLicense(in);
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

}
