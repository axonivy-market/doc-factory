package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

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
	public File appendPdfFiles(String resultFilePath, List<java.io.File> pdfsToAppend) throws DocFactoryException {
		API.checkNotEmpty(resultFilePath, "resultFileName");
		API.checkNotNull(pdfsToAppend, "pdfsToAppend");
		API.checkNotEmpty(pdfsToAppend, "pdfsToAppend");
		
		try {
			Document pdfDocument = new Document(new FileInputStream(pdfsToAppend.get(0)));
			return appendFilesToDocument(pdfDocument, pdfsToAppend.subList(1, pdfsToAppend.size()), resultFilePath);
		} catch(Exception ex) {
			throw new DocFactoryException("An error occurred while generating the pdf file. " + ex.getMessage(), ex);
		} 
	}
	
	/**
	 * Only visible for testing
	 */
	protected File appendFilesToDocument(Document document, List<java.io.File> pdfsToAppend, String resultFilePath) throws IOException {
		try {
			for(java.io.File pdf: pdfsToAppend) {
				appendPdfDocuments(document, new Document(new FileInputStream(pdf)));
			}
			File result = new File(resultFilePath, true);
			document.save(result.getAbsolutePath());
			return result;
		} finally {
			if(document != null) {
				document.close();
			}
		}
	}

	/**
	 * Only visible for testing
	 */
	protected void appendPdfDocuments(Document pdfDocument, Document appendedPdfDocument)
			throws FileNotFoundException {
		try {
			pdfDocument.getPages().add(appendedPdfDocument.getPages());
		} finally {
			if(appendedPdfDocument != null) {
				appendedPdfDocument.close();
			}
		}
	}

}
