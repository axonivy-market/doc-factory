package ch.ivyteam.ivy.addons.docfactory;

import java.util.List;

import ch.ivyteam.ivy.addons.docfactory.aspose.AsposePdfFactory;
import ch.ivyteam.ivy.addons.docfactory.exception.DocFactoryException;
import ch.ivyteam.ivy.addons.docfactory.pdf.PdfAType;
import ch.ivyteam.ivy.scripting.objects.File;

public abstract class PdfFactory {

	public static PdfFactory get() {
		return new AsposePdfFactory();
	}

	/**
	 * Creates one pdf file with all the given pdf files.
	 * @param resultFileName the name of the result file, cannot be blank.
	 * @param pdfsToAppend List the pdf files to append altogether in one pdf. The order of the list will be the order by which the files are appended. Cannot be null or empty.
	 * @return the Ivy File that contains all the pdf files appended together. 
	 * This File is produced in the Ivy Session directory, so it will be lost after the session has been closed. 
	 * You have to ensure that it will be saved in a persistent storage (other directory on the server FileSystem, database...) if you need it later on.
	 * @throws ch.ivyteam.ivy.addons.docfactory.exception.DocumentGenerationException if an exception occurred
	 * @throws DocFactoryException 
	 */
	public abstract File appendPdfFiles(String resultFileName, List<java.io.File> pdfsToAppend) throws DocFactoryException;

	/**
	 * Convert a PDF file into a PDF/A one.<br />
	 * <b>Important note:</b><br />
	 * It seems that the underlying library used for this operation has some bugs with the PDF/A 2A, 3A, 2U and 3U formats.<br />
	 * We recommend the {@link PdfAType#PDF_A_1A} {@link PdfAType#PDF_A_1B} {@link PdfAType#PDF_A_2B} and {@link PdfAType#PDF_A_3B}
	 * @param filePath the path to the file which has to be converted. Must be a PDF file.
	 * @param pdfAType the {@link PdfAType}. See also {@link https://en.wikipedia.org/wiki/PDF/A#Conformance_levels_and_versions}
	 * @throws IllegalArgumentException if the filePath does not point to a PDF file (docx for example).
	 * @throws DocFactoryException if any other exception occurs
	 */
	public abstract void convertToPdfA(String filePath, PdfAType pdfAType) throws IllegalArgumentException, DocFactoryException;

}