package ch.ivyteam.ivy.addons.docfactory.aspose;

import java.io.File;
import java.io.IOException;

import ch.ivyteam.ivy.addons.docfactory.BaseDocFactory;
import ch.ivyteam.ivy.addons.docfactory.DocFactoryConstants;
import ch.ivyteam.ivy.addons.docfactory.FileOperationMessage;
import ch.ivyteam.ivy.addons.docfactory.UnsupportedFormatException;
import ch.ivyteam.ivy.environment.Ivy;

import com.aspose.words.Document;
import com.aspose.words.OoxmlCompliance;
import com.aspose.words.OoxmlSaveOptions;
import com.aspose.words.SaveFormat;

public class AsposeDocFactoryFileGenerator {

	/**
	 * Used to export an aspose word document to a file. <br>
	 * Typically the Document has been generated during a mail merge operation
	 * and is ready to be saved on disc.
	 * 
	 * @param document
	 *            the com.aspose.words.Document that has to be exported as file
	 * @param baseFilePath
	 *            the file path without file extension. The extension is
	 *            provided by the outputFormat parameter.
	 * @param outputFormat The number representation of the wanted format (doc, docx, pdf...)
	 * @param generateBlankDocumentIfGivenDocIsNull if true and the given Document is null, then a blank document will be generated, else if false and document is null an UnsupportedFormatException
	 * will be thrown.
	 * @return FileOperationMessage, if success the files List of this FileOperationMessage contains the only File generated with the given Document in the given format.
	 * @throws Exception 
	 */
	public static FileOperationMessage exportDocumentToFile(Document document, String baseFilePath, int outputFormat,
			boolean generateBlankDocumentIfGivenDocIsNull) throws Exception {
		FileOperationMessage fom = FileOperationMessage.generateErrorTypeFileOperationMessage("");
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
		default:
			throw new UnsupportedFormatException(Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/formatNotSupported"));
		}
		
		try {
			if(!file.isFile()) {
				throw new IOException("The file "+file.getName()+" could not be produced. the Aspose Document could not be exported as File.");
			}
		} catch (SecurityException ex) {
			throw new Exception("A SecurityException occurred " + ex.getMessage(), ex);
		}
		fom = FileOperationMessage.generateSuccessTypeFileOperationMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/serialLetterSuccess")+ " \n" + file.getName());
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
