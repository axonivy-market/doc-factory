package ch.ivyteam.ivy.addons.docfactory;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeFileAppender;
import ch.ivyteam.ivy.addons.docfactory.options.FileAppenderOptions;
import ch.ivyteam.ivy.addons.docfactory.options.MultipleDocumentsCreationOptions;

/**
 * 
 * @since version 7.3.0
 *
 */
public abstract class FileAppender {
	
	public static FileAppender getInstance() {
		return new AsposeFileAppender();
	}
	
	/**
	 * check if the given file is a pdf.
	 * @param file the File to be checked. Cannot be null.
	 * @return true if the File is a pdf. Else false.
	 */
	public static boolean isPdf(File file) {
		API.checkNotNull(file, "File file");
		return file.getPath().toLowerCase().endsWith(DocFactoryConstants.PDF_EXTENSION);
	}
	
	/**
	 * check if the given file is an office file: doc, docx, odt.
	 * @param file the File to be checked. Cannot be null.
	 * @return true if the File is an office file: doc, docx, odt. Else false.
	 */
	public static boolean isOfficeWord(java.io.File file) {
		API.checkNotNull(file, "File file");
		String filePath = file.getPath().toLowerCase();
		return filePath.endsWith(DocFactoryConstants.DOCX_EXTENSION) || 
				filePath.endsWith(DocFactoryConstants.DOC_EXTENSION) || 
				filePath.endsWith(DocFactoryConstants.ODT_EXTENSION);
	}
	
	/**
	 * Filter the pdf files from the given File list and return them in a List.
	 * @param files the file List to be filtered. Cannot be null.
	 * @return The List of pdf contained in the original File List.
	 */
	public static List<File> filterPdfFiles(List<File> files) {
		API.checkNotNull(files, "List<File> files");
		return files.stream().filter(file -> isPdf(file)).collect(Collectors.toList());
	}
	
	/**
	 * Filter the office files (doc, docx, odt) from the given File list and return them in a List.
	 * @param files the file List to be filtered. Cannot be null.
	 * @return The List of office files (doc, docx, odt) contained in the original File List.
	 */
	public static List<File> filterOfficeWordFiles(List<File> files) {
		API.checkNotNull(files, "List<File> files");
		return files.stream().filter(file -> isOfficeWord(file)).collect(Collectors.toList());
	}
	
	/**
	 * Appends the given pdf files together and produces a File with the options set in the {@link MultipleDocumentsCreationOptions}.
	 * All non pdf files will be ignored.
	 * @param filesToAppendTogether the List of Pdf java.io.File to be appended. The first File will be the first part of the produced document, 
	 * the second one will be the second part ... Cannot be null or empty. All non pdf files will be ignored.
	 * @param multipleDocumentsCreationOptions the MultipleDocumentsCreationOptions object. It has options like the appended file name, output path, format. Cannot be null.
	 * @return the produced file
	 * @throws Exception
	 */
	public abstract File appendPdfFiles(List<File> filesToAppendTogether, FileAppenderOptions fileAppenderOptions) throws Exception;
	
	/**
	 * Appends the given office (doc, docx, odt) files together and produces a File with the options set in the {@link MultipleDocumentsCreationOptions}. 
	 * All non office (doc, docx, odt) files will be ignored.
	 * @param filesToAppendTogether the List of office (doc, docx, odt) java.io.File to be appended. The first File will be the first part of the produced document, 
	 * the second one will be the second part ... Cannot be null or empty. All non office (doc, docx, odt) files will be ignored.
	 * @param multipleDocumentsCreationOptions the MultipleDocumentsCreationOptions object. It has options like the appended file name, output path, format. Cannot be null.
	 * @return the produced file
	 * @throws Exception
	 */
	public abstract File appendOfficeWordFiles(List<File> filesToAppendTogether, FileAppenderOptions fileAppenderOptions) throws Exception;

}
