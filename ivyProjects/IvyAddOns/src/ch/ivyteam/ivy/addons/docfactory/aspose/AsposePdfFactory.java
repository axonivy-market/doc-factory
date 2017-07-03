package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.io.FileInputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.PdfFactory;
import ch.ivyteam.ivy.addons.docfactory.exception.DocFactoryException;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.File;

import com.aspose.pdf.Document;

public class AsposePdfFactory extends PdfFactory {
	
	public AsposePdfFactory() {
		init();
	}
	
	private void init() {
		try {
			LicenseLoader.loadLicenseforProduct(AsposeProduct.PDF);
		} catch (Exception e) {
			Ivy.log().error("An error occurred while loading the Aspose PDF license.", e);
		}
	}

	@Override
	public File appendPdfFiles(String resultFileName, List<java.io.File> pdfsToAppend) throws DocFactoryException {
		API.checkNotEmpty(resultFileName, "resultFileName");
		API.checkNotNull(pdfsToAppend, "pdfsToAppend");
		API.checkNotEmpty(pdfsToAppend, "pdfsToAppend");
		
		try {
			Document pdfDocument1 = new Document(new FileInputStream(pdfsToAppend.get(0)));
			
			for(java.io.File pdf: pdfsToAppend.subList(1, pdfsToAppend.size())) {
				Document pdfDocument = new Document(new FileInputStream(pdf));
				pdfDocument1.getPages().add(pdfDocument.getPages());
			}
			File result = new File(resultFileName, true);
			pdfDocument1.save(result.getAbsolutePath());
			return result;
		} catch(Exception ex) {
			Logger.getLogger(AsposePdfFactory.class.getName()).log(Level.SEVERE, "An error occurred while generating the pdf file.", ex);
			throw new DocFactoryException("An error occurred while generating the pdf file. " + ex.getMessage(), ex);
		}
	}

}
