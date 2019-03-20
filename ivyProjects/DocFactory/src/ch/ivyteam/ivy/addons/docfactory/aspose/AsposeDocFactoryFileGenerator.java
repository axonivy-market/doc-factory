package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants;
import ch.ivyteam.ivy.addons.docfactory.FileOperationMessage;
import ch.ivyteam.ivy.addons.docfactory.aspose.document.DocumentBlankPageRemover;
import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;
import ch.ivyteam.ivy.environment.Ivy;

import com.aspose.words.DocSaveOptions;
import com.aspose.words.Document;
import com.aspose.words.HtmlSaveOptions;
import com.aspose.words.OdtSaveOptions;
import com.aspose.words.OoxmlCompliance;
import com.aspose.words.OoxmlSaveOptions;
import com.aspose.words.PdfSaveOptions;
import com.aspose.words.SaveFormat;
import com.aspose.words.SaveOptions;
import com.aspose.words.TxtSaveOptions;

public class AsposeDocFactoryFileGenerator {
	
	private DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance();
	
	private AsposeDocFactoryFileGenerator() {}
	
	public static AsposeDocFactoryFileGenerator getInstance() {
		return new AsposeDocFactoryFileGenerator();
	}
	
	public AsposeDocFactoryFileGenerator withDocumentCreationOptions(DocumentCreationOptions documentCreationOptions) {
		API.checkNotNull(documentCreationOptions, "documentCreationOptions");
		this.documentCreationOptions = documentCreationOptions;
		return this;
	}
	
	/**
	 * Saves the given aspose Document to the given baseFilePath with the outputFormat
	 * @param document the Aspose Document cannot be null.
	 * @param baseFilePath the file path without format ending, cannot be blank.
	 * @param outputFormat the output format int representation see {@link DocFactoryConstants#DOC_FORMAT}, {@link DocFactoryConstants#DOCX_FORMAT} ... 
	 * @return the FileOperationMessage result which files list contain the produced java.io.File.
	 * @throws Exception
	 */
	public FileOperationMessage exportDocumentToFile(Document document, String baseFilePath, int outputFormat) throws Exception {
		API.checkNotNull(document, "document");
		API.checkNotEmpty(baseFilePath, "baseFilePath");
		API.checkRange(outputFormat, "outputFormat", DocFactoryConstants.DOC_FORMAT, DocFactoryConstants.ODT_FORMAT);
		
		applyDocumentCreationOptions(document);
		
		String filePath = makeFilePath(baseFilePath, outputFormat);
		SaveOptions saveOptions = getSaveOptionsForFormat(outputFormat);
		document.save(filePath, saveOptions);
		
		if(outputFormat == DocFactoryConstants.PDF_FORMAT && this.documentCreationOptions.isRemoveWhiteSpaceInPdfEditableFields()) {
			this.clearWhiteSpacePdfFieldsFromPdf(filePath);
		}
		return buildResult(new File(filePath));
	}
	
	private void applyDocumentCreationOptions(Document document) throws Exception {
		if(documentCreationOptions.isRemoveBlankPages()) {
			DocumentBlankPageRemover.removesBlankPage(document);
		}
	}

	private SaveOptions getSaveOptionsForFormat(int outputFormat) {
		SaveOptions saveOptions;
		if(outputFormat == DocFactoryConstants.DOCX_FORMAT) {
			saveOptions = new OoxmlSaveOptions();
			((OoxmlSaveOptions) saveOptions).setCompliance(OoxmlCompliance.ISO_29500_2008_TRANSITIONAL);
			saveOptions.setSaveFormat(SaveFormat.DOCX);
		} else if(outputFormat == DocFactoryConstants.PDF_FORMAT) {
			saveOptions = new PdfSaveOptions();
			((PdfSaveOptions) saveOptions).setPreserveFormFields(documentCreationOptions.isKeepFormFieldsEditableInPdf());
		} else if(outputFormat == DocFactoryConstants.HTML_FORMAT) {
			saveOptions = new HtmlSaveOptions(getAsposeSaveFormat(outputFormat));
			saveOptions.setSaveFormat(SaveFormat.HTML);
		} else if(outputFormat == DocFactoryConstants.ODT_FORMAT) {
			saveOptions = new OdtSaveOptions(getAsposeSaveFormat(outputFormat));
			saveOptions.setSaveFormat(SaveFormat.ODT);
		} else if(outputFormat == DocFactoryConstants.TXT_FORMAT) {
			saveOptions = new TxtSaveOptions();
			saveOptions.setSaveFormat(SaveFormat.TEXT);
		} else {
			saveOptions = new DocSaveOptions();
			saveOptions.setSaveFormat(getAsposeSaveFormat(outputFormat));
		}
		return saveOptions;
	}
	
	private String makeFilePath(String baseFilePath, int outputFormat) {
		return baseFilePath + (outputFormat == DocFactoryConstants.PDF_FORMAT ? DocFactoryConstants.PDF_EXTENSION : 
			outputFormat == DocFactoryConstants.DOCX_FORMAT ? DocFactoryConstants.DOCX_EXTENSION : 
				outputFormat == DocFactoryConstants.DOC_FORMAT ? DocFactoryConstants.DOC_EXTENSION : 
					outputFormat == DocFactoryConstants.HTML_FORMAT ? DocFactoryConstants.HTML_EXTENSION : 
						outputFormat == DocFactoryConstants.ODT_FORMAT ? DocFactoryConstants.ODT_EXTENSION : DocFactoryConstants.TXT_EXTENSION);
	}
	
	private int getAsposeSaveFormat(int outputFormat) {
		return  (outputFormat == DocFactoryConstants.PDF_FORMAT ? SaveFormat.PDF : 
			outputFormat == DocFactoryConstants.DOCX_FORMAT ? SaveFormat.DOCX : 
				outputFormat == DocFactoryConstants.DOC_FORMAT ? SaveFormat.DOC : 
					outputFormat == DocFactoryConstants.HTML_FORMAT ? SaveFormat.HTML : 
						outputFormat == DocFactoryConstants.ODT_FORMAT ? SaveFormat.ODT : SaveFormat.DOC);
	}

	private void clearWhiteSpacePdfFieldsFromPdf(String filePath) {
		try {
			LicenseLoader.loadLicenseforProduct(AsposeProduct.PDF);
		} catch (Exception e) {
			Ivy.log().error("AsposeDocFactoryFileGenerator#clearWhiteSpacePdfFieldsFromPdf Aspose PDF Licence error. "
					+ "Cannot clear the Pdf fields whitespace by the file {0}", filePath, e);
			return;
		}
		com.aspose.pdf.Document pdfDoc = new com.aspose.pdf.Document(filePath);
		
		cleanPdfFields(pdfDoc.getForm().getFields());
		pdfDoc.save(filePath);
	}
	
	private static void cleanPdfFields(com.aspose.pdf.Field[] fields) {
		Stream.of(fields).forEach(field -> {
			if (Objects.nonNull(field.getValue())) {
				field.setValue(field.getValue().trim());
			}
		});
	}

	private static FileOperationMessage buildResult(File file)
			throws IOException, Exception {
		FileOperationMessage fom;
		try {
			if(!file.isFile()) {
				throw new IOException("The file "+file.getName()+" could not be produced. the Aspose Document could not be exported as File.");
			}
		} catch (SecurityException ex) {
			throw new Exception("A SecurityException occurred " + ex.getMessage(), ex);
		}
		fom = FileOperationMessage.generateSuccessTypeFileOperationMessage(
				Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/serialLetterSuccess")+ " \n" + 
						file.getName()
			);
		fom.addFile(file);
		
		return fom;
	}
}
