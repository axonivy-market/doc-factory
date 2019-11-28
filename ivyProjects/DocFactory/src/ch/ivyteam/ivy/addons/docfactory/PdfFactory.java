package ch.ivyteam.ivy.addons.docfactory;

import java.util.List;

import ch.ivyteam.ivy.addons.docfactory.aspose.AsposePdfFactory;
import ch.ivyteam.ivy.addons.docfactory.exception.DocFactoryException;
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

}
