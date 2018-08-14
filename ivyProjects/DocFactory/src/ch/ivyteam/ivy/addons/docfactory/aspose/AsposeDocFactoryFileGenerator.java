package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.io.File;
import java.io.IOException;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.BaseDocFactory;
import ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants;
import ch.ivyteam.ivy.addons.docfactory.FileOperationMessage;
import ch.ivyteam.ivy.addons.docfactory.UnsupportedFormatException;
import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;
import ch.ivyteam.ivy.environment.Ivy;

import com.aspose.words.TxtSaveOptions;
import com.aspose.words.DocSaveOptions;
import com.aspose.words.Document;
import com.aspose.words.HtmlSaveOptions;
import com.aspose.words.OdtSaveOptions;
import com.aspose.words.OoxmlCompliance;
import com.aspose.words.OoxmlSaveOptions;
import com.aspose.words.PdfSaveOptions;
import com.aspose.words.SaveFormat;
import com.aspose.words.SaveOptions;

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
	
	public FileOperationMessage exportDocumentToFile(Document document, String baseFilePath, int outputFormat) throws Exception {
		API.checkNotNull(document, "document");
		API.checkNotEmpty(baseFilePath, "baseFilePath");
		API.checkRange(outputFormat, "outputFormat", DocFactoryConstants.DOC_FORMAT, DocFactoryConstants.ODT_FORMAT);
		
		String filePath = makeFilePath(baseFilePath, outputFormat);
		SaveOptions saveOptions = getSaveOptionsForFormat(outputFormat);
		document.save(filePath, saveOptions);
		return buildResult(new File(filePath));
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

	/**
	 * @deprecated
	 * This is not used anymore by the AsposeDocFactory because it was not flexible enough for getting new Options.
	 */
	@Deprecated
	public static FileOperationMessage exportDocumentToFile(Document document, String baseFilePath, int outputFormat,
			boolean generateBlankDocumentIfGivenDocIsNull) throws Exception {
		File file = null;
		
		if (document == null && generateBlankDocumentIfGivenDocIsNull) {
			document = new Document();
		}
		generateIllegalArgumentExceptionIfOneParameterIsNullOrAnEmptyString("The document or baseFilePath parameter is not valid.", document, baseFilePath);
		switch (outputFormat) {
		case BaseDocFactory.DOC_FORMAT:
			document.save(baseFilePath + DocFactoryConstants.DOC_EXTENSION, SaveFormat.DOC);
			file = new File(baseFilePath + DocFactoryConstants.DOC_EXTENSION);
			break;
		case BaseDocFactory.DOCX_FORMAT:
			OoxmlSaveOptions ooxmlSaveOptions = new OoxmlSaveOptions();
			ooxmlSaveOptions.setCompliance(OoxmlCompliance.ISO_29500_2008_TRANSITIONAL);
			ooxmlSaveOptions.setSaveFormat(SaveFormat.DOCX);
			document.getCompatibilityOptions().setUICompat97To2003(true);
			document.save(baseFilePath + DocFactoryConstants.DOCX_EXTENSION, ooxmlSaveOptions);
			file = new File(baseFilePath + DocFactoryConstants.DOCX_EXTENSION);
			break;
		case BaseDocFactory.PDF_FORMAT:
			document.save(baseFilePath + DocFactoryConstants.PDF_EXTENSION);
			file = new File(baseFilePath + DocFactoryConstants.PDF_EXTENSION);
			
			break;
		case BaseDocFactory.HTML_FORMAT:
			document.save(baseFilePath + DocFactoryConstants.HTML_EXTENSION, SaveFormat.HTML);
			file = new File(baseFilePath + DocFactoryConstants.HTML_EXTENSION);
			break;
		case BaseDocFactory.TXT_FORMAT:
			document.save(baseFilePath + DocFactoryConstants.TXT_EXTENSION, SaveFormat.TEXT);
			file = new File(baseFilePath + DocFactoryConstants.TXT_EXTENSION);
			break;
		case BaseDocFactory.ODT_FORMAT:
			document.save(baseFilePath + DocFactoryConstants.ODT_EXTENSION, SaveFormat.ODT);
			file = new File(baseFilePath + DocFactoryConstants.ODT_EXTENSION);
			break;
		default:
			throw new UnsupportedFormatException(Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/formatNotSupported"));
		}
		
		return buildResult(file);
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
	
	private static void generateIllegalArgumentExceptionIfOneParameterIsNullOrAnEmptyString(
			String message, Object... o) {
		for (Object obj : o) {
			if (o == null) {
				throw new IllegalArgumentException(message);
			}
			if (obj instanceof String) {
				if (((String) obj).trim().isEmpty()) {
					throw new IllegalArgumentException(message);
				}
			}
		}

	}

}
