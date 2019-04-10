package ch.ivyteam.ivy.addons.docfactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeDocFactoryFileGenerator;
import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeDocumentAppender;
import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeFieldMergingCallback;
import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeProduct;
import ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker;
import ch.ivyteam.ivy.addons.docfactory.aspose.LicenseLoader;
import ch.ivyteam.ivy.addons.docfactory.aspose.MailMergeDataSource;
import ch.ivyteam.ivy.addons.docfactory.aspose.MailMergeDataSourceGenerator;
import ch.ivyteam.ivy.addons.docfactory.aspose.options.AsposeMergeCleanupOptions;
import ch.ivyteam.ivy.addons.docfactory.exception.DocumentGenerationException;
import ch.ivyteam.ivy.addons.docfactory.mergefield.SimpleMergeFieldsHandler;
import ch.ivyteam.ivy.addons.docfactory.options.MergeCleanupOptions;
import ch.ivyteam.ivy.addons.docfactory.options.MultipleDocumentsCreationOptions;
import ch.ivyteam.ivy.addons.docfactory.options.SimpleMergeCleanupOptions;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.CompositeObject;
import ch.ivyteam.ivy.scripting.objects.Recordset;
import ch.ivyteam.ivy.scripting.objects.Tree;

import com.aspose.words.DataTable;
import com.aspose.words.Document;
import com.aspose.words.IMailMergeDataSource;
import com.aspose.words.MailMergeCleanupOptions;
import com.aspose.words.SaveFormat;
import com.aspose.words.SectionStart;


/**
 * @author ec<br>
 * @since 01.01.2009 in SharedResources. In IvyAddons since 01.11.2009
 * This the implementation of the BaseDocFactory class that uses the Aspose proprietary API.<br>
 * http://www.aspose.com<br>
 * You have the possibility to communicate the results and errors of the documents generation methods<br>
 * to the calling Parent Ivy Rich Dialog through CallBack Methods.<br> 
 * Those methods are indicated in the xxxMethodName variables. Those methods have to be published into
 * the Ivy RD Interface.  
 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#errorMethodName
 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#successMethodName
 * 
 */
public class AsposeDocFactory extends BaseDocFactory {
	
	private static final String EMPTY_MERGE_FIELDS_MESSAGE_CMS_PATH = "/ch/ivyteam/ivy/addons/docfactory/messages/EmptyMergeFields";

	private static final String INVALID_FILE_NAME_CMS_MESSAGE_PATH = "/ch/ivyteam/ivy/addons/docfactory/messages/invalidFileName";

	private static final String FILENAME = "filename";

	private static final String DEFAULT_FILE_OUTPUT_PREFIX = "serialLetter_";

	private static final String OBJECTS_FOR_REGIONS_NOT_FROM_SAME_TYPE_ERROR_MESSAGE = "The DataClass Objects provided to generateDocumentWithRegions are not all from the same Type. This is not allowed. The process will ignore the table {0}";

	private static final String DEFAULT_OUTPUT_DIRECTORY_NAME = "ivy_RIA_files";

	/** Aspose.Word Document objects used to perform the document merge (letter generation with MergeFields)*/
	private Document doc;	
	private Document docDest;

	/** String used to stored the fileName used to generate a document */
	String outputName;
	

	
	public AsposeDocFactory(){
		super();
		parseLicense();
		this.fieldMergingCallback = new AsposeFieldMergingCallback();
	}
	
	/**
	 * Set a DocumentWorker for applying one's own logic on the produced com.aspose.words.Document.
	 * @see {@link DocumentWorker}
	 * @param documentWorker
	 * @return the AsposeDocFactory containing the given DocumentWorker.
	 */
	public AsposeDocFactory withDocumentWorker(DocumentWorker documentWorker) {
		this.documentWorker = documentWorker;
		return this;
	}
	
	/**
	 * Gets the DocumentWorker (see {@link DocumentWorker}) that have been set by {@link #withDocumentWorker(DocumentWorker)}.
	 * @return the DocumentWorker or null if none has been set.
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DocumentWorker getDocumentWorker() {
		if(!(this.documentWorker instanceof DocumentWorker)) {
			return null;
		}
		return (DocumentWorker) this.documentWorker;
	}

	private void parseLicense() {
		try {
			LicenseLoader.loadLicenseforProduct(AsposeProduct.WORDS);
		} catch (Exception e) {
			Ivy.log().error("Aspose Words Licence error", e);
			this.fileOperationMessage= FileOperationMessage.generateErrorTypeFileOperationMessage("Aspose Words Licence error " + e.getMessage());
			handleResponse(false);
		}
	}

	/**
	 * generate a blank document. For test purpose.
	 * The generated document will be empty, will have the given String name, will be created in the 
	 * given String path and in the given format.
	 * @return a fileOperationMessage Object
	 * @see ch.ivyteam.ivy.addons.util.FileOperationMessage
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateBlankDocument(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public FileOperationMessage generateBlankDocument(String outputName, String outputPath, String outputFormat) {
		this.fileOperationMessage=new FileOperationMessage();
		//we check if the given file name is valid
		if(!FileUtil.isFileNameValid(outputName)) {
			this.fileOperationMessage= FileOperationMessage.generateErrorTypeFileOperationMessage(Ivy.cms().co(INVALID_FILE_NAME_CMS_MESSAGE_PATH));
			handleResponse(true);
		}
		this.setBasicFileName(outputName);

		setOutputPath(formatGivenPathSetToDefaultIfBlank(outputPath));
		this.setFormat(outputFormat);
		
		String baseDocPath= this.outputPath+this.basicFileName;
		int format = getFormatPosition(outputFormat,UNSUPPORTED_FORMAT);
		
		try {
			this.fileOperationMessage = getFileGenerator().
					exportDocumentToFile(new Document(), baseDocPath, format);
		} catch (Exception e) {
			this.fileOperationMessage= FileOperationMessage.generateErrorTypeFileOperationMessage(e.getMessage());
		}
		handleResponse(true);
		return fileOperationMessage;
	}

	/** 
	 * Method to generate one document<br>
	 * Mail merge with regions is now supported
	 * @param documentTemplate the DocumentTemplate Object containing all the necessary variables for this operation<br>
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and <br>
	 *  the java.io.File generated in case of success, else null.<br>
	 *  @see DocumentTemplate
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateDocument(ch.ivyteam.ivy.addons.docfactory.DocumentTemplate)
	 */
	@Override
	public FileOperationMessage generateDocument(DocumentTemplate documentTemplate) {
		try {
			this.setFormat(documentTemplate.getOutputFormat());
			this.doc = this.doMailMerge(documentTemplate);
			this.fileOperationMessage =  getFileGenerator().
					exportDocumentToFile(this.doc, 
							this.formatGivenPathSetToDefaultIfBlank(documentTemplate.getOutputPath()) + documentTemplate.getOutputName(), 
							getFormatPosition(documentTemplate.getOutputFormat(), PDF_FORMAT)
					);
		} catch (Exception e) {
			this.fileOperationMessage = FileOperationMessage.generateErrorTypeFileOperationMessage(e.getMessage());
		}
		handleResponse(true);
		return this.fileOperationMessage;
	}

	/**
	 * Generates a document. The File generated will have the given name, will be at the given outputPath, in the given format,<br>
	 *  and with the help from the List of Mergefields.<br>
	 *  The templatePath of the document model is also given as parameter.<br>
	 *  If the templatePath is null or an empty String, the current one (previously setted by setTemplate(String), will be taken.<br>
	 *  If the filename is null or an empty String we look if the first mergeField contains the filename, <br>
	 *  if not by default the filename will be "serialLetter_System.nanoTime()".<br>
	 *  @return the fileOperationMessage resulting of this operation. If its type is SUCCESS_MESSAGE, than the File was generated.<br>
	 * @see ch.ivyteam.ivy.addons.util.FileOperationMessage
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#setTemplate(File)
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateDocument(String, String, String, String, List)
	 */
	@Override
	public FileOperationMessage generateDocument(String templatePath, String outputName, String outputPath, String outputFormat, List<TemplateMergeField> mergeFields) {
		//Check the parameters
		this.resetAndCheckCommonParameters(templatePath, outputName, outputPath, outputFormat, mergeFields);

		if(this.fileOperationMessage.isError()) {
			handleResponse(true);
			return this.fileOperationMessage;
		}

		ArrayList<TemplateMergeField> templateParamslist = new ArrayList<>();
		templateParamslist.addAll(mergeFields);

		if(outputName == null || outputName.trim().equalsIgnoreCase("")) {
			//if the filename was not indicated, we look if the first mergeField Object could contain the fileName
			if(templateParamslist.get(0).getMergeFieldName().trim().equalsIgnoreCase(FILENAME)) {
				// it contains the file name
				this.outputName = templateParamslist.get(0).getMergeFieldValue().trim();
			} else {
				// no file Name indicated, we have to take a default one
				this.outputName = DEFAULT_FILE_OUTPUT_PREFIX + System.nanoTime();
			}
		} else {
			this.outputName = outputName;
		}

		if(!FileUtil.isFileNameValid(outputName)) {
			// if the filename contains invalid characters
			this.fileOperationMessage = FileOperationMessage.generateErrorTypeFileOperationMessage(
					Ivy.cms().co(INVALID_FILE_NAME_CMS_MESSAGE_PATH));
		} else {
			this.fileOperationMessage = FileOperationMessage.generateSuccessTypeFileOperationMessage(
					Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/serialLetterSuccess"));
			this.setFormat(outputFormat);

			try {
				File file = this.doMailMerge(templateParamslist);
				this.fileOperationMessage.addFile(file);
			} catch (Exception e) {
				this.fileOperationMessage = FileOperationMessage.generateErrorTypeFileOperationMessage(e.getMessage());
			}
		}
		handleResponse(true);
		return fileOperationMessage;
	}

	/**
	 * Method to generate a String in TXT.
	 * @param templatePath : where to find the template
	 * @param list : List of parameters (TemplateMergeField objects).
	 * @return the TXT String generated.
	 */
	@Override
	public String generateTxt(String templatePath, List<TemplateMergeField> list){
		String emailText ="";
		try {
			emailText = this.doMailMerge(templatePath, list, null, null).toString(SaveFormat.TEXT);
		} catch (Exception e) {
			this.fileOperationMessage = FileOperationMessage.generateErrorTypeFileOperationMessage(e.getMessage());
			handleResponse(true);	
		}
		return emailText;
	}

	/**
	 * Method to generate a String in HTML format.
	 * @param templatePath : where to find the template
	 * @param list : List of parameters (TemplateMergeField objects).
	 * @return the HTML String generated.
	 */
	@Override
	public String generateHTML(String templatePath, List<TemplateMergeField> list) {
		String emailText ="";
		if(StringUtils.isBlank(this.outputPath)) {
			this.outputPath = DEFAULT_OUTPUT_DIRECTORY_NAME;
		}
		this.outputPath = FileUtil.formatPathWithEndSeparator(this.outputPath);
		
		String randomPart = Long.toString(System.nanoTime());
		String tempHtmlFilePath = this.outputPath + "tmpHTML_" + randomPart +".html";
		try(BufferedReader reader = new BufferedReader(new FileReader(tempHtmlFilePath))) {
			this.doMailMerge(templatePath, list, null, null).save(tempHtmlFilePath, SaveFormat.HTML);

			StringBuilder fileData = new StringBuilder();
			char[] buf = new char[1024];
			int numRead=0;
			while((numRead=reader.read(buf)) != -1){
				fileData.append(buf, 0, numRead);
			}
			emailText =fileData.toString();
		} catch (Exception e) {
			this.fileOperationMessage = FileOperationMessage.generateErrorTypeFileOperationMessage(e.getMessage());
			handleResponse(true);	
		} 
		return emailText;
	}


	/**
	 * Method to generate a list of Files that can be used for HTML representation.<br>
	 * This this typical if you use a template with Objects like Images in it.<br>
	 * During the html File Generation, each image will be saved into a jpg File.<br>
	 * If you want to get correctly all the Files involved into the HTML rendering, you have to get the list of images also.
	 * @param templatePath : where to find the template
	 * @param templateMergeFields : List of parameters (TemplateMergeField objects).
	 * @return the HTML and media Files that were generated to represent the HTML page.
	 */
	@Override
	public ArrayList<java.io.File> generateFilesForHTML(String templatePath, List<TemplateMergeField> templateMergeFields) {
		ArrayList<java.io.File> htmlfiles = new ArrayList<>();
		this.outputName = "tmpHTML_" + Long.toString(System.nanoTime());
		if(this.outputPath == null || this.outputPath.trim().equalsIgnoreCase("")) {
			this.outputPath = DEFAULT_OUTPUT_DIRECTORY_NAME;
		}
		this.outputPath = FileUtil.formatPathWithEndSeparator(this.outputPath);

		if(templateMergeFields.get(0).getMergeFieldName().trim().equalsIgnoreCase(FILENAME)) {
			this.outputName=templateMergeFields.get(0).getMergeFieldValue().trim();
		}

		try {
			String s = this.outputPath+this.outputName+".html";
			this.doMailMerge(templatePath, templateMergeFields, null, null).save(s, SaveFormat.HTML);
			htmlfiles.add(new java.io.File(s));
			htmlfiles.addAll(FileUtil.getFilesWithPattern("\\.[0-9].*$", this.outputName, this.outputPath));
		} catch (Exception e) {
			this.fileOperationMessage=FileOperationMessage.generateErrorTypeFileOperationMessage(e.getMessage());
			handleResponse(true);	
		}
		return htmlfiles;
	}


	/**
	 * Generate Documents with a List of DocumentTemplate objects.<br>
	 * Here each of the DocumentTemplate will generate a single File.<br>
	 * Each single DocumentTemplate can have its own format, path, template...
	 * The Merge Mail with regions (Table) is now supported.
	 * @param documentTemplates of DocumentTemplate: List<DocumentTemplate> list
	 * @return a fileOperationMessage. This object contains the type of Message (SUCCESS, ERROR), the message, <br>
	 * and the List of generated Files. If a DocumentTemplate didn't contain valid parameters, its output File will be null. <br>
	 * If you want to have an exact trace of the operation's result for each DocumentTemplate, <br>
	 * use the generateDocumentsWithDifferentDestination(List<DocumentTemplate> list) method instead. This method returns a fileOperationMessage for each DocumentTemplate.
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateDocuments(java.util.List)
	 * @see #generateDocuments(String, String, String, List)
	 */
	@Override
	public FileOperationMessage generateDocuments(List<DocumentTemplate> documentTemplates) {
		return generateDocuments(documentTemplates, MultipleDocumentsCreationOptions
				.getInstance()
				.createSingleFileForEachDocument(true)
				.createOneFileByAppendingAllTheDocuments(false)
		);
	}
	
	/**
	 * Generate Documents with a List of DocumentTemplate objects and given options for specific multiple documents creation.<br>
	 * @param documentTemplates List<DocumentTemplate>
	 * @param multipleDocumentsCreationOptions : The {@link MultipleDocumentsCreationOptions} object defining options for the documents creation. 
	 * Cannot be null and at least one of these two method should return true:<br />
	 * {@link MultipleDocumentsCreationOptions#isCreateOneFileByAppendingAllTheDocuments()} or {@link MultipleDocumentsCreationOptions#isCreateSingleFileForEachDocument()}
	 * @return a fileOperationMessage. This object contains the type of Message (SUCCESS, ERROR), the message, <br>
	 * and the List of generated Files.
	 */
	@Override
	public FileOperationMessage generateDocuments(List<DocumentTemplate> documentTemplates, MultipleDocumentsCreationOptions multipleDocumentsCreationOptions) {
		API.checkNotNull(multipleDocumentsCreationOptions, "MultipleDocumentsCreationOptions multipleDocumentsCreationOptions");
		if(!multipleDocumentsCreationOptions.isCreateOneFileByAppendingAllTheDocuments() && !multipleDocumentsCreationOptions.isCreateSingleFileForEachDocument()) {
			throw new IllegalArgumentException("The given MultipleDocumentsCreationOptions object is not set for creating any document.");
		}
		if(documentTemplates == null || documentTemplates.isEmpty()) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(Ivy.cms().co(EMPTY_MERGE_FIELDS_MESSAGE_CMS_PATH));
			this.fileOperationMessage.emptyFileList();
			return this.fileOperationMessage;
		} 
		this.fileOperationMessage = FileOperationMessage.generateSuccessTypeFileOperationMessage("Success");
		List<java.io.File> files = new ArrayList<>();
		List<Document> mergedDocuments = new ArrayList<>();
		documentTemplates.forEach(dt -> {
					String resultFilePath = FileUtil.formatPathWithEndSeparator(dt.getOutputPath()) + dt.getOutputName();
					int format = getFormatPosition(dt.getOutputFormat(), PDF_FORMAT);
					try {
						mergedDocuments.add(this.doMailMerge(dt));
						if (multipleDocumentsCreationOptions.isCreateSingleFileForEachDocument()) {
							FileOperationMessage result = getFileGenerator().exportDocumentToFile(mergedDocuments.get(mergedDocuments.size() - 1),
											resultFilePath, format);
							if (result.isSuccess() && result.getFiles().size() == 1) {
								files.add(result.getFiles().get(0));
							}
						}
					} catch (Exception e) {
						Ivy.log().warn("[DOCFACTORY] An exception occurred while generating files from a list of DocumentTemplate. "
										+ "The following file could not be produced: {0}. Processing the next DocumentTemplate.",
										e, resultFilePath);
					}
				});
		if (multipleDocumentsCreationOptions.isCreateOneFileByAppendingAllTheDocuments()) {
			try {
				FileOperationMessage result = getFileGenerator().exportDocumentToFile(
						AsposeDocumentAppender.appendDocuments(mergedDocuments, multipleDocumentsCreationOptions.getFileAppenderOptions()),
						multipleDocumentsCreationOptions.getFileAppenderOptions().getAppendedFileAbsolutePathWithoutFormatEnding(),
						multipleDocumentsCreationOptions.getFileAppenderOptions().getAppendedFileOutputFormat()
				);
				files.add(result.getFiles().get(0));
			} catch (Exception e) {
				Ivy.log().error("[DOCFACTORY] An exception occurred while appendending a list of merged documents together. "
								+ "The following file could not be produced: {0}.",
								e, multipleDocumentsCreationOptions.getFileAppenderOptions().getAppendedFileAbsolutePath());
				this.fileOperationMessage = FileOperationMessage.generateErrorTypeFileOperationMessage(
						"[DOCFACTORY] An exception occurred while appendending a list of merged documents together. " + e.getMessage());
			}
		}

		this.fileOperationMessage.addFiles(files);

		handleResponse(true);
		return this.fileOperationMessage;
	}
	
	

	/**
	 * Method to generate one or more documents in the same destination folder<br>
	 * By convention, the name of each File is given by the first TemplateMergeField, which key name must be "filename".<br>
	 * @param outputPath : where to save the new generated File on the server
	 * @param outputFormat : format ".doc", ".docx", ".pdf", or ".html" 
	 * @param documentsTemplateMergeFieldsList : List of List of parameters (TemplateMergeField objects). <br>
	 * Each List of TemplateMergeField objects in the primary List will be turned into a new document.
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and null File Object<br>
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateDocuments(String, String, String, List)
	 * @see ch.ivyteam.ivy.addons.util.FileOperationMessage
	 */
	@Override
	public FileOperationMessage generateDocuments(String templatePath, String outputPath, 
			final String outputFormat, final List<List<TemplateMergeField>> documentsTemplateMergeFieldsList) {
		//series of check to see if no exceptions
		this.template = new java.io.File(FileUtil.formatPath(templatePath));
		if(!doCheckBeforeDocGeneration(outputFormat, documentsTemplateMergeFieldsList)) {
			return this.fileOperationMessage;
		}

		this.setOutputPath(formatGivenPathSetToDefaultIfBlank(outputPath));

		this.setFormat(outputFormat);
		int format = getFormatPosition(outputFormat, PDF_FORMAT);

		//reset the fileOperationMessage object
		this.fileOperationMessage=FileOperationMessage.generateSuccessTypeFileOperationMessage("");

		//iterator to parse all of the List<TemplateMergeField>, each one is a different letter
		final Iterator<List<TemplateMergeField>> iter = documentsTemplateMergeFieldsList.iterator();

		while(iter.hasNext()) {// there are still letters that have not been written
			ArrayList<TemplateMergeField> templateParamslist = new ArrayList<>();
			templateParamslist.addAll(iter.next());

			//Try to get the filename from the first template merge field found
			if(templateParamslist.get(0).getMergeFieldName().trim().equalsIgnoreCase(FILENAME)) {
				this.outputName=templateParamslist.get(0).getMergeFieldValue().trim();
			}else {//we didn't find the filename merge field, we take the default serial letter name
				this.outputName=DEFAULT_FILE_OUTPUT_PREFIX+System.nanoTime();
			}
			String baseDocPath= this.outputPath+this.outputName;
			try {
				FileOperationMessage fom  = getFileGenerator().exportDocumentToFile(
						this.doMailMerge(templatePath, templateParamslist, null, null), baseDocPath, format
						);
				if(!fom.isError()) {
					this.fileOperationMessage.addFiles(fom.getFiles());
				}
			} catch (Exception ex) {
				if(!this.fileOperationMessage.isInformation()) {
					this.fileOperationMessage.setType(FileOperationMessage.INFORMATION_MESSAGE);
					this.fileOperationMessage.setMessage("The following documents could not be created: "+this.outputName);
				} else {
					this.fileOperationMessage.setMessage(this.fileOperationMessage.getMessage()+", "+this.outputName);
				}
			}
		}
		handleResponse(true);
		return this.fileOperationMessage;
	}

	/**
	 * Method to generate one or more documents each one can be saved in a different destination folder.<br>
	 * By convention, the name of each File is given by the first TemplateMergeField, which key name must be "filename".<br>
	 * By convention, the name of each destination folder is given by the second TemplateMergeField, which key name must be "destinationPath".<br>
	 * @param templatePath : where to find the template for the Merging operation
	 * @param outputFormat : format ".doc", ".docx", or ".html" 
	 * @param documentsTemplateMergeFieldsList : List of List of parameters (TemplateMergeField objects). <br>
	 * Each List of TemplateMergeField objects in the primary List will be turned into a new document.
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and null File Object<br>
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateDocumentsWithDifferentDestination(String, String, List)
	 * @see ch.ivyteam.ivy.addons.util.FileOperationMessage
	 */
	@Override
	public FileOperationMessage generateDocumentsWithDifferentDestination(String templatePath, final String outputFormat, 
			final List<List<TemplateMergeField>> list) {

		if(!doCheckBeforeDocGeneration(outputFormat, list)) {
			return this.fileOperationMessage;
		}
		
		this.setFormat(outputFormat);
		int format = getFormatPosition(outputFormat,PDF_FORMAT);

		this.fileOperationMessage=FileOperationMessage.generateSuccessTypeFileOperationMessage("");
		
		final Iterator<List<TemplateMergeField>> iter = list.iterator();
		while(iter.hasNext()) {
			ArrayList<TemplateMergeField> templateParamslist =new ArrayList<TemplateMergeField>();
			templateParamslist.addAll(iter.next());

			//get the name of the document
			if(templateParamslist.get(0).getMergeFieldName().trim().equalsIgnoreCase("filename")) {
				outputName = templateParamslist.get(0).getMergeFieldValue().trim();
			} else {
				outputName = "serialLetter_" + System.nanoTime();
			}

			String destinationPath = DEFAULT_OUTPUT_DIRECTORY_NAME;
			if(templateParamslist.get(1).getMergeFieldName().trim().equalsIgnoreCase("destinationPath")) {
				destinationPath = templateParamslist.get(1).getMergeFieldValue().trim();
			}
			destinationPath = FileUtil.formatPathWithEndSeparator(destinationPath, true);
			String baseDocPath = destinationPath + this.outputName;
			Ivy.log().warn("OUTPUT PATH " + baseDocPath);
			try {
				FileOperationMessage fom = getFileGenerator().
						exportDocumentToFile(this.doMailMerge(templatePath, templateParamslist, null, null), baseDocPath, format);
				if(!fom.isError()) {
					this.fileOperationMessage.addFiles(fom.getFiles());
				}
			} catch (Exception ex) {
				if(!this.fileOperationMessage.isInformation()) {
					this.fileOperationMessage.setType(FileOperationMessage.INFORMATION_MESSAGE);
					this.fileOperationMessage.setMessage("The following documents could not be created: "+ this.outputName);
				} else {
					this.fileOperationMessage.setMessage(this.fileOperationMessage.getMessage() + ", " + this.outputName);
				}
			}
		}
		handleResponse(true);
		return fileOperationMessage;
	}


	/**
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateDocumentsWithDifferentDestination(java.util.List)
	 * Here the mergeMail with region is now supported
	 */
	@Override
	public List<FileOperationMessage> generateDocumentsWithDifferentDestination(List<DocumentTemplate> list) {
		ArrayList <FileOperationMessage> l = new ArrayList <>();
		for(DocumentTemplate dt: list) {
			l.add(this.generateDocument(dt));
		}
		return l;
	}

	/**
	 * Method to generate multiple documents appended in one File.<br>
	 * With this method all the letters inside the serial letter are done with the same Template.
	 * @param templatePath : where to find the template for the Merging operation
	 * @param outputName : the name of the new created letter if null or empty, we try to get the first<br>
	 * MergeField form the first element in the List. If it has the Key name "filename", we take it, else we take "serialLetter".
	 * @param outputPath : where to save the new generated File on the server
	 * @param outputFormat : format ".doc", ".docx", ".pdf", or ".html" 
	 * @param list : List of List of parameters (TemplateMergeField objects). <br>
	 * Each List of TemplateMergeField objects in the primary List will be turned into a new page in the final File.
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and <br>
	 *  the java.io.File generated in case of success, else empty List<java.io.File>.<br> 
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateMultipleDocumentsInOne(java.lang.String, java.lang.String, java.lang.String, java.util.List)
	 * @see ch.ivyteam.ivy.addons.util.FileOperationMessage
	 */
	@Override
	public FileOperationMessage generateMultipleDocumentsInOne(String templatePath, 
			String outputName, String outputPath, String outputFormat, List<List<TemplateMergeField>> list) {
		//template File
		this.template = new java.io.File(FileUtil.formatPath(templatePath));
		setOutputPath(formatGivenPathSetToDefaultIfBlank(outputPath));
		// 		set the format
		this.setFormat(outputFormat);
		//		set the output name
		if(outputName == null || outputName.trim().equalsIgnoreCase("") || !FileUtil.isFileNameValid(outputName)) {
			// fileName parameter is invalid. We take the default name SerialLetter + System nanoTime String
			this.outputName=DEFAULT_FILE_OUTPUT_PREFIX+System.nanoTime();
		}else {
			this.outputName = outputName;
		}
		// series of check to see if no exceptions -> this method checks the template, the format and if the list is not empty
		if(!doCheckBeforeDocGeneration(outputFormat, list)) {
			// The inputParameters are not all valid. We return the actual fileOperationMessage with the error Message
			return this.fileOperationMessage;
		}
		//the input parameters are checked and valid	
		int format = getFormatPosition(outputFormat,UNSUPPORTED_FORMAT);
		this.fileOperationMessage=FileOperationMessage.generateSuccessTypeFileOperationMessage("");

		//iterator to parse all of the List<TemplateMergeField>, each one is a different letter
		final Iterator<List<TemplateMergeField>> iter = list.iterator();
		try  {
			docDest = doMailMerge(templatePath, iter.next(), null, null);
			//appends the rest of the documents to the first one
			while(iter.hasNext()) {
				doc = doMailMerge(templatePath, iter.next(), null, null);
				doc.getFirstSection().getPageSetup().setSectionStart(SectionStart.NEW_PAGE);
				AsposeDocumentAppender.appendDoc(docDest, doc);
			}
			this.fileOperationMessage  = getFileGenerator().exportDocumentToFile(this.docDest, this.outputPath+this.outputName, format);
		}
		catch (Exception ex) {
			this.fileOperationMessage = FileOperationMessage.generateErrorTypeFileOperationMessage(ex.getMessage());
		}
		handleResponse(true);
		return this.fileOperationMessage;
	}

	/**
	 * With this method each letter inside the serial letter can be made with its own template.<br>
	 * The MailMerge With regions is supported
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateMultipleDocumentsInOne(String, String, String, List)
	 */
	@Override
	public FileOperationMessage generateMultipleDocumentsInOne(String outputPath, 
			String outputName, String outputFormat, List<DocumentTemplate> documentTemplates) {
		//series of check to see if no exceptions
		if(documentTemplates == null || documentTemplates.isEmpty()) {
			// the list is empty: the FielOperationMessage Object contains the error and we return it
			this.fileOperationMessage = FileOperationMessage.generateErrorTypeFileOperationMessage(
					Ivy.cms().co(EMPTY_MERGE_FIELDS_MESSAGE_CMS_PATH));
			handleResponse(true);
			return this.fileOperationMessage;
		}	

		setOutputPath(formatGivenPathSetToDefaultIfBlank(outputPath));
		//		set the output name
		if(outputName == null || outputName.trim().equalsIgnoreCase("") || !FileUtil.isFileNameValid(outputName)) {
			// fileName parameter is invalid. We take the default name SerialLetter + System nanoTime String
			this.outputName=DEFAULT_FILE_OUTPUT_PREFIX+System.nanoTime();
		}else {
			this.outputName = outputName;
		}
		this.setFormat(outputFormat);

		this.fileOperationMessage=FileOperationMessage.generateSuccessTypeFileOperationMessage("");
		
		int format = getFormatPosition(this.outputFormat,UNSUPPORTED_FORMAT);
		
		//iterator to parse all of the DocumentTemplate objects, each one is a different letter
		final Iterator<DocumentTemplate> iter = documentTemplates.iterator();
		try  {
			// we do the mailMerge for the first DocumentTemplate
			docDest = this.doMailMerge(iter.next());
			
			//appends the rest of the documents to the first one
			while(iter.hasNext()) {
				doc = doMailMerge(iter.next());
				doc.getFirstSection().getPageSetup().setSectionStart(SectionStart.NEW_PAGE);
				AsposeDocumentAppender.appendDoc(docDest, doc);
			}
			this.fileOperationMessage = getFileGenerator().exportDocumentToFile(this.docDest, this.outputPath + this.outputName, format);
		}
		catch (Exception ex) {
			this.fileOperationMessage = FileOperationMessage.generateErrorTypeFileOperationMessage(ex.getMessage());
		}
		handleResponse(true);
		return this.fileOperationMessage;
	}

	/**
	 * For private use,<br> NEW the mergeMail with regions is supported
	 * @param documentTemplate
	 * @return the document object resulting of the mail merging operation
	 * @throws Exception
	 */
	private Document doMailMerge(DocumentTemplate documentTemplate) throws Exception {
		return doMailMerge(documentTemplate.getTemplatePath(), documentTemplate.getMergeFields(), 
				extractMailMergeDataSources(documentTemplate),  documentTemplate.getDataTable());
	}

	private Collection<IMailMergeDataSource> extractMailMergeDataSources(DocumentTemplate documentTemplate) throws Exception {
		ArrayList<IMailMergeDataSource> mmds = new ArrayList<>();
		mmds.addAll(getMailMergeDataSourceFromDocumentTemplateHashTable(documentTemplate));
		mmds.addAll(getMailMergeDataSourceFromDocumentTemplateTableMap(documentTemplate));
		 

		if(documentTemplate.getParentDataSourceForNestedMailMerge()!=null 
				&& !documentTemplate.getParentDataSourceForNestedMailMerge().isEmpty()) {
			mmds.add(new MailMergeDataSource(documentTemplate.getParentDataSourceForNestedMailMerge(), 
					documentTemplate.getChildrenDataSourcesForNestedMailMerge()));
		}

		if(documentTemplate.getNestedDataSourceForNestedMailMerge()!=null 
				&& !documentTemplate.getNestedDataSourceForNestedMailMerge().isEmpty()) {
			mmds.add(new MailMergeDataSource(documentTemplate.getNestedDataSourceForNestedMailMerge()));
		}
		if(documentTemplate.getTreeData()!=null) {
			mmds.add(new MailMergeDataSource(documentTemplate.getTreeData()));
		}
		mmds.addAll(getMailMergeDataSourceFromTableDataTemplateMergeFields(documentTemplate.getMergeFields()));
		return mmds;
	}

	private List<IMailMergeDataSource> getMailMergeDataSourceFromTableDataTemplateMergeFields(List<TemplateMergeField> templateMergeFields) {
		List<IMailMergeDataSource> mmds = new ArrayList<>();
		for(TemplateMergeField tmf: templateMergeFields) {
			if(tmf.isPossibleTableData() && tmf.isCollection()) {
				IMailMergeDataSource dataSource = MailMergeDataSourceGenerator.getFromCollectionTypeTemplateMergeField(tmf);
				if(dataSource != null) {
					mmds.add(dataSource);
				}
			}
		}
		return mmds;
	}
	
	private List<IMailMergeDataSource> getMailMergeDataSourceFromDocumentTemplateHashTable(DocumentTemplate documentTemplate) {
		if(documentTemplate.getTablesNamesAndFieldsHashtable() == null 
				|| documentTemplate.getTablesNamesAndFieldsHashtable().isEmpty()) {
			return new ArrayList<>();

		}
		List<IMailMergeDataSource> mmds = new ArrayList<>();
		Set<String> tables = documentTemplate.getTablesNamesAndFieldsHashtable().keySet();
		Iterator<String> iter = tables.iterator();
		while(iter.hasNext()){
			String key = iter.next();
			try {
				mmds.add(new MailMergeDataSource(documentTemplate.getTablesNamesAndFieldsHashtable().get(key), key));
			}catch(Exception ex) {
				Ivy.log()
				.error(OBJECTS_FOR_REGIONS_NOT_FROM_SAME_TYPE_ERROR_MESSAGE, ex, key);
			}
		}
		return mmds;
	}

	private List<IMailMergeDataSource> getMailMergeDataSourceFromDocumentTemplateTableMap(DocumentTemplate documentTemplate) {
		if(documentTemplate.getTablesNamesAndFieldsmap() == null 
				|| documentTemplate.getTablesNamesAndFieldsmap().isEmpty()) {
			return new ArrayList<>();
		}
		List<IMailMergeDataSource> mmds = new ArrayList<>();
		Set<String> tables = documentTemplate.getTablesNamesAndFieldsmap().keySet();
		Iterator<String> iter = tables.iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			try {
				mmds.add(DataClassToMergefields.transfomDataClassInMergeDataSource(documentTemplate.getTablesNamesAndFieldsmap().get(key), key));
			}catch(Exception ex) {
				Ivy.log().error(OBJECTS_FOR_REGIONS_NOT_FROM_SAME_TYPE_ERROR_MESSAGE, ex, key);
			}
		}
		return mmds;
	}
	
	private DataTable makeEmptyDataTable() {
		DataTable dt = new DataTable("aDataTable");
		dt.getColumns().add("dummy");
		return dt;
	}
	
	private Document doMailMerge(String templatePath, List<TemplateMergeField> fields, 
			Collection<IMailMergeDataSource> mailMergeDataSources, Collection<?> dataTables) throws Exception {
		Document document = new Document(FileUtil.formatPath(templatePath));
		if(this.getDocumentWorker() != null) {
			((DocumentWorker) documentWorker).prepare(document);
		}
		
		SimpleMergeFieldsHandler simpleMergeFieldsHandler = SimpleMergeFieldsHandler.forTemplateMergeFields(fields);
		
		document.getMailMerge().setFieldMergingCallback(this.getFieldMergingCallBack());
		
		setSimpleMergeFieldCleanupOtions(document);
		
		document.getMailMerge().execute(simpleMergeFieldsHandler.getMergeFieldNames(), simpleMergeFieldsHandler.getMergeFieldValues());
		if(mailMergeDataSources != null) {
			for(IMailMergeDataSource m : mailMergeDataSources) {
				document.getMailMerge().executeWithRegions(m);
			}
		}
		
		if(dataTables != null) {
			for(Object obj: dataTables) {
				if(obj instanceof DataTable) {
					document.getMailMerge().executeWithRegions((DataTable) obj);
				}
			}
		}
		
		setRegionsMergeFieldCleanupOtions(document);
		
		document.getMailMerge().executeWithRegions(makeEmptyDataTable());
		document.getMailMerge().deleteFields();
		if(this.getDocumentWorker() != null) {
			((DocumentWorker) documentWorker).postCreate(document);
		}
		return document;
	}

	private void setSimpleMergeFieldCleanupOtions(Document document) {
		setRemoveNullValueLines();
		if(this.simpleMergeCleanupOptions.isRemovesEmptyParagraphs()) {
			document.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS);
		}
	}

	private void setRegionsMergeFieldCleanupOtions(Document document) {
		setRemoveNullValueLines();
		document.getMailMerge().setCleanupOptions(AsposeMergeCleanupOptions.getCleanupOptionIntValues(this.regionMergeCleanupOptions));
	}
	
	private void setRemoveNullValueLines() {
		if((this.getFieldMergingCallBack() instanceof AsposeFieldMergingCallback)) {
			((AsposeFieldMergingCallback) this.getFieldMergingCallBack()).removeNullValuesLines(this.simpleMergeCleanupOptions.isRemovesBlankLines());
		}
	}
	
	/**
	 * performs a mail merge with a template. Gives the generated File back.
	 * @param parameters : the list of TemplateMergeField objects that correspond to the template's fields
	 * @return the java.io.File that was generated.
	 * @throws Exception
	 */
	private java.io.File doMailMerge(List<TemplateMergeField> parameters) throws Exception {
		File file = null;
		doc = doMailMerge(this.template.getPath(), parameters, null, null);
		
		String baseFilePath = this.outputPath + this.outputName;
		int format = getFormatPosition(this.outputFormat, UNSUPPORTED_FORMAT);
		this.fileOperationMessage  = getFileGenerator().exportDocumentToFile(this.doc, baseFilePath, format);
		if(this.fileOperationMessage.isSuccess() && !this.fileOperationMessage.getFiles().isEmpty()) {
			file = this.fileOperationMessage.getFiles().get(0);
		}
		return file;
	}


	@Override
	public Class<? extends AsposeDocFactory> getFactoryClass() {
		return this.getClass();
	}

	/**
	 * private use
	 * @param format
	 * @return the position of the given format in the format supported list
	 */
	private int getFormatPosition(String format, int defaultFormatPositionIfNotFound) {
		int result = defaultFormatPositionIfNotFound;
		String formatWithoutLeadingDot = format;
		if(format.startsWith(".")) {
			formatWithoutLeadingDot = format.substring(1);
		}
		for(int i = 0; i < SUPPORTED_OUTPUT_FORMATS.length; i++) {
			if(formatWithoutLeadingDot.trim().equalsIgnoreCase(SUPPORTED_OUTPUT_FORMATS[i])) {
				result = i;
				break;
			}
		}
		return result;
	}

	/**
	 * returns a String Array containing all the fieldNames in a document
	 * @param doc the aspose Word Document Object
	 * @return the ArrayList<String> of the MergeFields
	 */
	public String[] getTemplateFields(Document doc) {
		try {
			return doc.getMailMerge().getFieldNames();
		} catch (Exception e) {
			Ivy.log().error("getTemplateFields error " + e.getMessage(), e);
		}
		return null;
	}


	/**
	 * Gives the merge fields list from a template back.
	 * @param templatePath the template path
	 * @return the ArrayList<String> of the MergeFields
	 */
	@Override
	public ArrayList<String> getTemplateFields(String templatePath) {
		return getFields(templatePath);
	}

	/**
	 * Gives the merge fields list from a template back.
	 * @param templatePath the template path
	 * @return the ArrayList<String> of the MergeFields
	 */
	public static ArrayList<String> getFields(String templatePath) {
		ArrayList<String> templateFields = new ArrayList<>();
		try {
			templateFields.addAll(Arrays.asList(new Document(templatePath).getMailMerge().getFieldNames()));
		} catch (Exception e) {
			Ivy.log().error("getTemplateFields error " +e.getMessage(), e);
		}
		return templateFields;
	}

	/**
	 * for Private use.
	 * Check if the given format is supported by the Aspose implementation.<br>
	 * And if the template can be found and the list of MergeFileds is not null.
	 * @param format
	 * @param list
	 * @return true if this document Factory can be used to produce documents. Else false because some parameters are invalid.
	 */
	private boolean doCheckBeforeDocGeneration(String format, List<?> list) {
		this.fileOperationMessage=new FileOperationMessage();

		if(!isFormatSupported(format)) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/formatNotSupported")+" \n"+format);
			this.fileOperationMessage.emptyFileList();
			handleResponse(true);
			return false;
		}
		if(this.template==null || !this.template.exists()) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/templateCannotBefFound"));
			this.fileOperationMessage.emptyFileList();
			handleResponse(true);
			return false;
		}
		if(list == null) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(Ivy.cms().co(EMPTY_MERGE_FIELDS_MESSAGE_CMS_PATH));
			this.fileOperationMessage.emptyFileList();
			handleResponse(true);
			return false;
		}
		return true;
	}

	/**
	 * Check if the desired document output format is supported
	 * @param format : the String Format to check, format without the point
	 * @return true if supported, else false
	 */
	@Override
	public boolean isFormatSupported(String format) {
		if (StringUtils.isBlank(format)) {
			return false;
		}
		
		if (format.startsWith(".")) {
			Arrays.asList(SUPPORTED_OUTPUT_FORMATS).contains(format.substring(1).toLowerCase());
		}
		return Arrays.asList(SUPPORTED_OUTPUT_FORMATS).contains(format.toLowerCase());

	}

	/**
	 *
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#getSupportedFormats()
	 */
	public static String[] getSupportedFormats(){
		return SUPPORTED_OUTPUT_FORMATS;
	}



	/**
	 * We set the format. If the given format is invalid, we set it to "doc" per default.
	 * @param format like (doc, docx, pdf ...). If blank or not supported, the "doc" format will be automatically set by default.
	 */
	@Override
	public void setFormat(String format) {
		if( StringUtils.isBlank(format) || !isFormatSupported(format)) {
			this.outputFormat = "doc";
			return;
		}
		this.outputFormat = setFormatWithoutLeadingDot(format);
	}

	/**
	 * Utility private method to make all checks before beginning to produce documents.<br>
	 * During the check some empty or null parameters are going to be set with default values.
	 * @param _templatePath: the template path to check, this path cannot be null or empty and must drives to a valid java.io.File.
	 * @param outputName: the outputName to check, if empty or null will be set with a random generated name.
	 * @param outputPath: the output path to check, if empty or null will be set to "ivy_RIA_files".
	 * @param outputFormat: the output format to check, if empty or null will be set with "doc"
	 * @param _fields: the templateMergeFields parameter to check cannot be null, may be an empty list.
	 * @return fileOperationMessage with ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE as Type if one of the parameter is invalid,<br>
	 * else the type will be ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.SUCCESS_MESSAGE.
	 */
	private FileOperationMessage resetAndCheckCommonParameters(String templatePath, String outputName, 
			String outputPath, String outputFormat, List<TemplateMergeField> mergefields){
		this.fileOperationMessage = new FileOperationMessage();
		this.fileOperationMessage.emptyFileList();
		
		if(!StringUtils.isBlank(templatePath)) {
			this.template = new java.io.File(FileUtil.formatPath(templatePath));
		} else {
			this.setTemplate(null);
		}

		setOutputPath(formatGivenPathSetToDefaultIfBlank(outputPath));
		if(!doCheckBeforeDocGeneration(outputFormat, mergefields)) {
			return this.fileOperationMessage;
		}

		ArrayList<TemplateMergeField> templateParamslist = new ArrayList<>();
		templateParamslist.addAll(mergefields);

		if(outputName==null || outputName.trim().equalsIgnoreCase("")) {
			//if the filename was not indicated, we look if the first mergeField Object could contain the fileName
			if(templateParamslist.get(0).getMergeFieldName().trim().equalsIgnoreCase(FILENAME)) {
				// it contains the file name
				this.outputName=templateParamslist.get(0).getMergeFieldValue().trim();
			}
			else {
				// no file Name indicated, we have to take a default one
				this.outputName=DEFAULT_FILE_OUTPUT_PREFIX+System.nanoTime();
			}
		} else {
			this.outputName=outputName;
		}

		if(!FileUtil.isFileNameValid(outputName)) {
			// if the filename contains invalid characters
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.emptyFileList();
			this.fileOperationMessage.setMessage(Ivy.cms().co(INVALID_FILE_NAME_CMS_MESSAGE_PATH));
			return this.fileOperationMessage;
		}

		this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.SUCCESS_MESSAGE);
		this.fileOperationMessage.setMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/serialLetterSuccess"));
		this.setFormat(outputFormat);

		return this.fileOperationMessage; 
	}
	
	private String formatGivenPathSetToDefaultIfBlank(String outputPath) {
		if(StringUtils.isBlank(outputPath)) {
			return DEFAULT_OUTPUT_DIRECTORY_NAME + "/";
		}
		return FileUtil.formatPathWithEndSeparator(outputPath);
	}

	/**
	 * Generates the java.io.File corresponding to the Aspose Document Object.<br>
	 * It takes the already saved output path, output name and output format.
	 * @param document
	 * @return the generated java.io.File
	 * @throws Exception
	 */
	private java.io.File makeFileWithDocument(Document document) throws Exception {
		java.io.File file=null;
		int format = getFormatPosition(this.outputFormat,UNSUPPORTED_FORMAT);
		String baseFilePath = this.outputPath + this.outputName;
		this.fileOperationMessage  = getFileGenerator().exportDocumentToFile(document, baseFilePath, format);

		if(!this.fileOperationMessage.getFiles().isEmpty()) {
			file = this.fileOperationMessage.getFiles().get(0);
		}
		handleResponse(true);
		return file;
	}

	@Override
	public FileOperationMessage generateDocumentWithRegions(String templatePath, 
			String outputName, String outputPath, String outputFormat, 
			List<TemplateMergeField> mergeFields, 
			Map<String, List<CompositeObject>> tablesNamesAndFieldsmap){
		//Check the parameters
		this.resetAndCheckCommonParameters(templatePath, outputName, outputPath, outputFormat, mergeFields);
		if(this.fileOperationMessage.getType() == ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE)
		{// Check failed
			handleResponse(true);
			return this.fileOperationMessage;
		}

		ArrayList<TemplateMergeField> templateParamslist =new ArrayList<>();
		templateParamslist.addAll(mergeFields);
		ArrayList<IMailMergeDataSource> mmds = new ArrayList<>();
		try  {
			if(tablesNamesAndFieldsmap!=null && !tablesNamesAndFieldsmap.isEmpty())
			{
				Set<String> tables = tablesNamesAndFieldsmap.keySet();
				Iterator<String> iter = tables.iterator();
				while(iter.hasNext()){
					String key = iter.next();
					try {
						mmds.add(DataClassToMergefields.transfomDataClassInMergeDataSource(tablesNamesAndFieldsmap.get(key), key));
					} catch(Exception ex) {
						Ivy.log().error("The DataClass Objects provided to generateDocumentWithRegions "
								+ "are not all from the same Type. This is not allowed. The process will ignore the table " + key, ex);
					}
				}
			}
			this.doc = doMailMerge(this.template.getPath(), templateParamslist, mmds, null);
			fileOperationMessage.addFile(makeFileWithDocument(this.doc));

		} catch (Exception e) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(e.getMessage());
			this.fileOperationMessage.emptyFileList();
		}
		handleResponse(true);
		return fileOperationMessage;
	}

	@Override
	public FileOperationMessage generateDocumentWithRegions(String templatePath, 
			String outputName, String outputPath, String outputFormat, 
			List<TemplateMergeField> mergeFields, Hashtable<String, Recordset> hashtable) {
		this.resetAndCheckCommonParameters(templatePath, outputName, outputPath, outputFormat, mergeFields);
		if(this.fileOperationMessage.getType() == ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE) {
			handleResponse(true);
			return this.fileOperationMessage;
		}

		ArrayList<TemplateMergeField> templateParamslist =new ArrayList<>();
		templateParamslist.addAll(mergeFields);
		ArrayList<IMailMergeDataSource> mmds = new ArrayList<>();
		try {
			if(hashtable!=null && !hashtable.isEmpty()) {
				Set<String> tables = hashtable.keySet();
				Iterator<String> iter = tables.iterator();
				while(iter.hasNext()){
					String key = iter.next();
					try {
						mmds.add(new MailMergeDataSource(hashtable.get(key), key));
					} catch(Exception ex) {
						Ivy.log().error("The DataClass Objects provided to generateDocumentWithRegions are not all from the same Type. This is not allowed. The process will ignore the table "+key, ex);
					}
				}
			}
			this.doc = doMailMerge(this.template.getPath(), templateParamslist, mmds, null);
			fileOperationMessage.addFile(makeFileWithDocument(this.doc));
		} catch (Exception e) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(e.getMessage());
			this.fileOperationMessage.emptyFileList();
		}
		handleResponse(true);
		return fileOperationMessage;
	}

	@Override
	public FileOperationMessage generateDocumentWithRegions(String templatePath, String outputName, 
			String outputPath, String outputFormat, List<TemplateMergeField> mergeFields, 
			List<String> tablesNames, List<List<String>> tablesFieldsNames, 
			List<List<List<Object>>> tablesRowsValues) {
		Hashtable<String, Recordset> hashtable = new Hashtable<>();
		try {
			for(int i = 0 ; i<tablesNames.size();i++) {//for each table
				//Create the recordset with fields Names as columns
				Recordset rs = new Recordset(tablesFieldsNames.get(i));
				//add all the table List of rows
				rs.addAll(tablesRowsValues.get(i));
				hashtable.put(tablesNames.get(i), rs);
			}
			this.generateDocumentWithRegions(templatePath, outputName, outputPath, outputFormat, mergeFields, hashtable);
		} catch(Exception ex) {
			this.fileOperationMessage.setMessage(ex.getMessage());
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
		}

		return this.fileOperationMessage;
	}

	@Override
	public ch.ivyteam.ivy.addons.docfactory.FileOperationMessage generateDocumentWithNestedRegions(
			String templatePath, String outputName, String outputPath,
			String outputFormat, List<TemplateMergeField> mergeFields,
			ch.ivyteam.ivy.scripting.objects.List<CompositeObject> parentDataSource,
			ch.ivyteam.ivy.scripting.objects.List<ch.ivyteam.ivy.scripting.objects.List<CompositeObject>> childrenDataSources)  {
		this.resetAndCheckCommonParameters(templatePath, outputName, outputPath, outputFormat, mergeFields);
		if(this.fileOperationMessage.getType() == ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE) {// Check failed
			return this.fileOperationMessage;
		}
		ArrayList<TemplateMergeField> templateParamslist =new ArrayList<>();
		templateParamslist.addAll(mergeFields);
		ArrayList<IMailMergeDataSource> mmds = new ArrayList<>();
		try {
			if(parentDataSource!=null && !parentDataSource.isEmpty()) {
				mmds.add(new MailMergeDataSource(parentDataSource, childrenDataSources));
			}
			this.doc = doMailMerge(this.template.getPath(), templateParamslist, mmds, null);
			fileOperationMessage.addFile(makeFileWithDocument(this.doc));
		} catch (Exception e) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(e.getMessage());
			this.fileOperationMessage.emptyFileList();
		}
		handleResponse(true);
		return this.fileOperationMessage;
	}

	@Override
	public ch.ivyteam.ivy.addons.docfactory.FileOperationMessage generateDocumentWithNestedRegions(
			String templatePath, String outputName, String outputPath,
			String outputFormat, List<TemplateMergeField> mergeFields,
			ch.ivyteam.ivy.scripting.objects.List<CompositeObject> _nestedDataSource) {
		this.resetAndCheckCommonParameters(templatePath, outputName, outputPath, outputFormat, mergeFields);
		if(this.fileOperationMessage.isError()) {
			return this.fileOperationMessage;
		}
		ArrayList<TemplateMergeField> templateParamslist = new ArrayList<>();
		templateParamslist.addAll(mergeFields);
		ArrayList<IMailMergeDataSource> mmds = new ArrayList<>();
		try {
			if(_nestedDataSource!=null && !_nestedDataSource.isEmpty()) {
				mmds.add(new MailMergeDataSource(_nestedDataSource));
			}
			this.doc = doMailMerge(this.template.getPath(), templateParamslist, mmds, null);
			fileOperationMessage.addFile(makeFileWithDocument(this.doc));
		} catch (Exception e) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(e.getMessage());
			this.fileOperationMessage.emptyFileList();
		}
		handleResponse(true);
		return this.fileOperationMessage;
	}

	@Override
	public ch.ivyteam.ivy.addons.docfactory.FileOperationMessage generateDocumentWithNestedRegions(
			String templatePath, String outputName, String outputPath,
			String outputFormat, List<TemplateMergeField> mergefields,
			Tree treeDataSource) {
		this.resetAndCheckCommonParameters(templatePath, outputName, outputPath, outputFormat, mergefields);
		if(this.fileOperationMessage.getType() == ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE) {// Check failed
			return this.fileOperationMessage;
		}
		ArrayList<TemplateMergeField> templateParamslist = new ArrayList<>();
		templateParamslist.addAll(mergefields);
		ArrayList<IMailMergeDataSource> mmds = new ArrayList<>();
		try {
			if(treeDataSource!=null) {
				mmds.add(new MailMergeDataSource(treeDataSource));
			}
			this.doc = doMailMerge(this.template.getPath(), templateParamslist, mmds, null);
			fileOperationMessage.addFile(makeFileWithDocument(this.doc));
		} catch (Exception e) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(e.getMessage());
			this.fileOperationMessage.emptyFileList();
		}
		handleResponse(true);
		return this.fileOperationMessage;
	}
	
	private void handleResponse(boolean throwExceptionIfErrorAndNoResponseHandlerAndNoParentRd) {
		if(getResponsHandler() != null) {
			getResponsHandler().handleDocFactoryResponse(fileOperationMessage);
			return;
		} 
		if(this.fileOperationMessage.isError() && throwExceptionIfErrorAndNoResponseHandlerAndNoParentRd) {
			throw new DocumentGenerationException(fileOperationMessage);
		}
	}

	/**
	 * Returns the AsposeFieldMergingCallback object that is responsible for applying special rules for mail merging for some merge fields.<br>
	 * If the MergeFieldCallBack set in the DocFactory with the {@link BaseDocFactory#withFieldMergingCallBack(Object)} 
	 * is not an AsposeFieldMergingCallback, then an IllegalStateException is thrown.<br>
	 * So prefer calling the {@link #getFieldMergingCallBack()} method. This will ever work, even if the FieldMergingCallBack set is not an AsposeFieldMergingCallback.
	 * @return the AsposeFieldMergingCallback object
	 */
	public AsposeFieldMergingCallback getAsposeFieldMergingCallback() {
		if(!(this.fieldMergingCallback instanceof AsposeFieldMergingCallback)) {
			throw new IllegalStateException("The fieldMergingCallback set in the AsposeDocFactory is not AsposeFieldMergingCallback instance.");
		}
		AsposeFieldMergingCallback asposeMergeFieldCallback = (AsposeFieldMergingCallback) this.fieldMergingCallback;
		if(this.documentCreationOptions != null) {
			asposeMergeFieldCallback.withDocumentCreationOptions(documentCreationOptions);
		}
		asposeMergeFieldCallback.setoutputFormat(this.outputFormat);
		return asposeMergeFieldCallback;
	}
	

	/**
	 * Returns the FieldMergingCallBack object set for the DocFactory. 
	 * The purpose of such a FieldMergingCallBack is to infer the way the mail merge (replacement of merge fields by their values) is done in particular situations.
	 * <br>For the AsposeDocFactory a default AsposeFieldMergingCallback is automatically instantiated and should cover the vast majority of the cases. 
	 * <br>You may need to extend the AsposeFieldMergingCallback for some special cases that are not covered. In that case use the {@link BaseDocFactory#withFieldMergingCallBack(Object)} for setting it.
	 * Currently as we use Aspose it is recommended to extend the AsposeFieldMergingCallback in such situations. 
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected <T> T getFieldMergingCallBack() {
		if(this.fieldMergingCallback instanceof AsposeFieldMergingCallback) {
			return (T) this.getAsposeFieldMergingCallback();
		}
		return (T) this.fieldMergingCallback;
	}

	/**
	 * Sets the AsposeFieldMergingCallback object used to apply special rules to merge fields while performing the mail merge operation.<br><br>
	 * A typical use of FieldMergingCallback objects is to insert images in merge fields if these merge fields name are declared as "Image:fieldName". 
	 * In such a case the merge-field-value corresponding to this field can be a String (path of the image file) or the images itself as ByteArray.<br><br>
	 * You can provide your own AsposeFieldMergingCallback object that covers your special needs.
	 * For example, you should override the <br>
	 * {@code public void fieldMerging(FieldMergingArgs arg0)}<br>
	 * method to apply special rules to HTML merge fields. See {@link www.aspose.com/community/forums/thread/380671/html-text-with-merge-field.aspx}
	 * @param fieldMergingCallback
	 * @throws IllegalArgumentException if the parameter is null.
	 * @deprecated use the {@link BaseDocFactory#getInstance()} for instantiating the DocFactory 
	 * and the {@link BaseDocFactory#withFieldMergingCallBack(Object)} for setting its fieldMergingCallback
	 */
	@Deprecated
	public void setAsposeFieldMergingCallback(AsposeFieldMergingCallback fieldMergingCallback) throws IllegalArgumentException {
		if(fieldMergingCallback == null){
			throw new IllegalArgumentException("The AsposeFieldMergingCallback must not be null.");
		}
		this.fieldMergingCallback = fieldMergingCallback;
	}
	
	private SimpleMergeCleanupOptions simpleMergeCleanupOptions = SimpleMergeCleanupOptions.getRecommendedMergeCleanupOptionsForSimpleMerging();
	private MergeCleanupOptions regionMergeCleanupOptions = MergeCleanupOptions.getRecommendedMergeCleanupOptionsForMergingWithRegions();

	@Override
	public BaseDocFactory withSimpleMergeCleanupOption(SimpleMergeCleanupOptions simpleMergeCleanupOption) {
		API.checkNotNull(simpleMergeCleanupOption, "simpleMergeCleanupOptions");
		this.simpleMergeCleanupOptions = simpleMergeCleanupOption;
		return this;
	}

	@Override
	public SimpleMergeCleanupOptions getSimpleMergeCleanupOptions() {
		return this.simpleMergeCleanupOptions;
	}

	@Override
	public BaseDocFactory withRegionsMergeCleanupOption(
			MergeCleanupOptions mergeCleanupOption) {
		API.checkNotNull(mergeCleanupOption, "mergeCleanupOptions");
		this.regionMergeCleanupOptions = mergeCleanupOption;
		return this;
	}

	@Override
	public MergeCleanupOptions getRegionsMergeCleanupOptions() {
		return this.regionMergeCleanupOptions;
	}
	
	private AsposeDocFactoryFileGenerator getFileGenerator() {
		return AsposeDocFactoryFileGenerator.getInstance().
				withDocumentCreationOptions(documentCreationOptions).withDocumentWorker(getDocumentWorker());
	}
}