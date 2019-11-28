package ch.ivyteam.ivy.addons.docfactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;
import ch.ivyteam.ivy.addons.docfactory.options.MergeCleanupOptions;
import ch.ivyteam.ivy.addons.docfactory.options.MultipleDocumentsCreationOptions;
import ch.ivyteam.ivy.addons.docfactory.options.SimpleMergeCleanupOptions;
import ch.ivyteam.ivy.addons.docfactory.response.ResponseHandler;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.CompositeObject;
import ch.ivyteam.ivy.scripting.objects.Recordset;
import ch.ivyteam.ivy.scripting.objects.Tree;


/**
 * @author ec
 * This is the abstract class that every DocFactory class should subclass<br> 
 * You have the possibility to communicate the results, errors, progress of the documents generation methods<br>
 * to the calling Parent Ivy Rich Dialog through CallBack Methods.<br> 
 * Those methods are indicated in the xxxMethodName variables. Those methods have to be published into
 * the Ivy RD Interface. 
 */
public abstract class BaseDocFactory{
	
	private static final String DOCUMENT_FACTORY_IMPLEMENTATION_SYSTEM_PROPERTY = "document.factory";
	
	protected String outputPath;
	protected String outputFormat;
	protected String basicFileName;
		
	/** */
	private ResponseHandler responseHandler;
	
	/** the list of supported formats. Can be different in your implementation*/
	public static final String[] SUPPORTED_OUTPUT_FORMATS = new String[] {"doc", "docx", "html", "txt", "pdf", "odt"};
	
	@Deprecated
	public static final String[] supportedOutputFormats = new String[] {"doc", "docx", "html", "txt", "pdf", "odt"};
	
	/* Format types for convenient use of switch control*/
	public static final int UNSUPPORTED_FORMAT = DocFactoryConstants.UNSUPPORTED_FORMAT;
	public static final int DOC_FORMAT = DocFactoryConstants.DOC_FORMAT;
	public static final int DOCX_FORMAT = DocFactoryConstants.DOCX_FORMAT;
	public static final int HTML_FORMAT = DocFactoryConstants.HTML_FORMAT;
	public static final int TXT_FORMAT = DocFactoryConstants.TXT_FORMAT;
	public static final int PDF_FORMAT = DocFactoryConstants.PDF_FORMAT;
	public static final int ODT_FORMAT = DocFactoryConstants.ODT_FORMAT;
	/** the document factory use a document model like an office dot template to generate serial letters.<br>
	 * This is the template java.io.File Object to be used for this operation.
	 */
	protected File template;
	/** 
	 * This Object is used to gives the RD Panel the result of the documents generation operation.<br>
	 * This fileOperationMessage contains a type indicating if the operation was successful or not, and a List of generated Files.<br>
	 * @see ch.ivyteam.ivy.addons.docfactory.FileOperationMessage
	 */
	protected FileOperationMessage fileOperationMessage;

	/**
	 * Empty constructor
	 *
	 */
	public BaseDocFactory(){
		this.outputFormat=SUPPORTED_OUTPUT_FORMATS[0];
		this.outputPath="defaultDirectory";
		this.basicFileName="defaultFileName";
		this.template=null;
	}
	
	/**
	 * Method to generate a blank document
	 * @param outputName : name of the output File
	 * @param outputPath : where to save the new generated File on the server
	 * @param outputFormat : format ".doc", ".docx", ".pdf", or ".html"
	 *@return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and <br>
	 *  the java.io.File generated in case of success, else null.<br>
	 */
	public abstract FileOperationMessage generateBlankDocument(String outputName, String outputPath, String outputFormat);
	
	/**
	 * Method to generate one document
	 * @param templatePath the template path taht is used as document's model for the fields merging.
	 * @param fileName the name of the File that will be generated
	 * @param outputPath : where to save the new generated File on the server
	 * @param outputFormat : format ".doc", ".docx", ".pdf", or ".html" 
	 * @param list : List of parameters (TemplateMergeField objects).
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and <br>
	 *  the java.io.File generated in case of success, else null.<br>
	 */
	public abstract FileOperationMessage generateDocument(String templatePath, String fileName, String outputPath, String outputFormat, List<TemplateMergeField> list);
	
	/**
	 * Method to generate one document
	 * @param documentTemplate the DocumentTemplate Object containing all the necessary variables for this operation<br>
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and <br>
	 *  the java.io.File generated in case of success, else null.<br>
	 *  @see DocumentTemplate
	 */
	public abstract FileOperationMessage generateDocument(DocumentTemplate documentTemplate);
	
	/**
	 * Method to generate one document
	 * @param list : List of parameters (TemplateMergeField objects).
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and <br>
	 *  the java.io.File generated in case of success, else null.<br>
	
	public abstract fileOperationMessage generateDocument(List<TemplateMergeField> list);
	 */
	
	/**
	 * Method to generate a String in TXT.
	 * @param templatePath : where to find the template
	 * @param list : List of parameters (TemplateMergeField objects).
	 * @return the TXT String generated.
	 */
	public abstract String generateTxt(String templatePath, List<TemplateMergeField>  list);
	
	/**
	 * Method to generate a String in HTML format.
	 * @param templatePath : where to find the template
	 * @param list : List of parameters (TemplateMergeField objects).
	 * @return the HTML String generated.
	 */
	public abstract String generateHTML(String templatePath, List<TemplateMergeField> list);
	
	/**
	 * Method to generate a list from Files that can be used for HTML representation.<br>
	 * This this typical if you use a template with Objects like Images in it.<br>
	 * During the html File Generation, each image will be saved into a jpg or gif File.<br>
	 * If you want to correctly get all the Files involved into the HTML rendering, you have to get the list of images also.
	 * @param templatePath : where to find the template
	 * @param list : List of parameters (TemplateMergeField objects).
	 * @return the HTML and media Files that were generated to represent an HTML page.
	 */
	public abstract ArrayList<java.io.File> generateFilesForHTML(String templatePath, List<TemplateMergeField> list);
	
	/**
	 * Method to generate one or more documents<br>
	 * with a list of DocumentTemplates Objects containing all the necessary variables for this operation.<br>
	 * Here each of the DocumentTemplate will generate a single File.<br>
	 * @param documentTemplates : List of DocumentTemplates. <br>
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and null File Object<br>
	 *  @see DocumentTemplate
	 */
	public abstract FileOperationMessage generateDocuments(List<DocumentTemplate> documentTemplates);
	
	/**
	 * Similar to {@link BaseDocFactory#generateDocuments(List)} but with the {@link DocumentCreationOptions} object for fine options.<br />
	 * By default the options are set for creating each DocumentTemplate File separately, you can turn this off with {@link DocumentCreationOptions#createSingleFileForEachDocument(boolean)}.
	 * If the given {@link DocumentCreationOptions#isCreateOneFileByAppendingAllTheDocuments()} returns true, 
	 * then all the given DocumentTemplate objects will also generate a file appending all the documents together.<br />
	 * The {@link DocumentCreationOptions} holds also a {@link ch.ivyteam.ivy.addons.docfactory.options.FileAppenderOptions} object for setting the fine options in the case you append all the documents in one file.
	 * @param documentTemplates The List of the document templates. Each of them generates a file by mail merging.
	 * @param multipleDocumentsCreationOptions {@link DocumentCreationOptions} object for setting the options for this operation (should it generate single files, appends all the files in one ...)
	 * @return A FileOperationMessage with the list of the java.io.File produced by this operation.
	 */
	public abstract FileOperationMessage generateDocuments(List<DocumentTemplate> documentTemplates, MultipleDocumentsCreationOptions multipleDocumentsCreationOptions);
	
	
	
	/**
	 * Method to generate one or more documents in the same destination folder<br>
	 * By convention, the name of each File is given by the first TemplateMergeField, which key name must be "filename".
	 * @param templatePath : where to find the template
	 * @param outputPath : where to save the new generated File on the server
	 * @param outputFormat : format ".doc", ".docx", ".pdf", ".txt" or ".html" 
	 * @param list : List of List of parameters (TemplateMergeField objects). <br>
	 * Each List of TemplateMergeField objects in the primary List will be turned into a new document.
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and null File Object<br>
	 */
	public abstract FileOperationMessage generateDocuments(String templatePath, String outputPath, String outputFormat, List<List<TemplateMergeField>> list);
	
	/**
	 * Method to generate one or more documents each one can be saved in a different destination folder.<br>
	 * By convention, the name of each File is given by the first TemplateMergeField, which key name must be "filename".<br>
	 * By convention, the name of each destination folder is given by the second TemplateMergeField, which key name must be "destinationPath".<br>
	 * @param templatePath : where to save the new generated File on the server
	 * @param outputFormat : format ".doc", ".docx", ".pdf", ".txt" or ".html"  
	 * @param list : List of List of parameters (TemplateMergeField objects). <br>
	 * Each List of TemplateMergeField objects in the primary List will be turned into a new document.
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and null File Object<br>
	 */
	public abstract FileOperationMessage generateDocumentsWithDifferentDestination(String templatePath, String outputFormat, List<List<TemplateMergeField>> list);
	
	
	/**
	 * Method to generate one document with merge mail. Mail Merge with regions supported.<br>
	 * @param templatePath: the template path that is used as document's model for the fields merging.
	 * @param utoutName: optional String for the name of the created document
	 * @param outputPath: optional String for the destination folder path
	 * @param outputFormat: format ".doc", ".docx", ".pdf", ".txt" or ".html" 
	 * @param mergefields: a List of ch.ivyteam.ivy.addons.docfactory.TemplateMergeField Objects for "normal" mail merge.
	 * @param tablesNamesAndFieldsmap: HashMap<String, List<CompositeObject>> HashMap containing the tables names (merge regions) from the template an the Lists of dataClasses Objects to feed the tables. 
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and null File Object<br>
	 */
	public abstract FileOperationMessage generateDocumentWithRegions(String templatePath, String outputName, String outputPath, String outputFormat, List<TemplateMergeField> mergefields, Map<String, List<CompositeObject>> tablesNamesAndFieldsmap);
	
	/**
	 * Method to generate one document with merge mail. Mail Merge with regions supported.<br>
	 * @param templatePath: the template path that is used as document's model for the fields merging.
	 * @param outoutName: optional String for the name of the created document
	 * @param outputPath: optional String for the destination folder path
	 * @param outputFormat: format ".doc", ".docx", ".pdf", ".txt" or ".html" 
	 * @param mergefields: a List of ch.ivyteam.ivy.addons.docfactory.TemplateMergeField Objects for "normal" mail merge.
	 * @param hashtable: Hashtable<String, Recordset> each String is the table Name in  the template with its corresponding Recordset to feed the table. <br>
	 * Each recordset column name should correspond to a merge region field name in its corresponding table.
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and null File Object<br>
	 */
	public abstract FileOperationMessage generateDocumentWithRegions(String templatePath, String outputName, String outputPath, String outputFormat, List<TemplateMergeField> mergefields, Hashtable<String, Recordset> hashtable);
	
	/**
	 * Method to generate one document with merge mail. Mail Merge with regions supported.<br>
	 * @param templatePath: the template path that is used as document's model for the fields merging.
	 * @param outoutName: optional String for the name of the created document
	 * @param outputPath: optional String for the destination folder path
	 * @param outputFormat: format ".doc", ".docx", ".pdf", ".txt" or ".html" 
	 * @param mergefields: a List of ch.ivyteam.ivy.addons.docfactory.TemplateMergeField Objects for "normal" mail merge.
	 * @param tablesNames: list of String representing the table names in the template
	 * @param tables_fieldsNames: for each table a list of String representing each merge field with region name (column name)
	 * @param tables_rowsValues: for each table a list of list of values (list of rows) to feed the corresponding table.
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and null File Object<br>
	 */
	public abstract FileOperationMessage generateDocumentWithRegions(String templatePath, String outputName, String outputPath, String outputFormat, List<TemplateMergeField> mergefields, List<String> tablesNames, List<List<String>> tables_fieldsNames, List<List<List<Object>>> tables_rowsValues);
	
	/**
	 * 
	 * @param templatePath
	 * @param outputName
	 * @param outputPath
	 * @param outputFormat
	 * @param mergefields
	 * @param parentDataSource
	 * @param childrenDataSources
	 * @return
	 */
	public abstract FileOperationMessage generateDocumentWithNestedRegions(String templatePath, String outputName, String outputPath, String outputFormat, List<TemplateMergeField> mergefields, ch.ivyteam.ivy.scripting.objects.List<CompositeObject> parentDataSource, ch.ivyteam.ivy.scripting.objects.List<ch.ivyteam.ivy.scripting.objects.List<CompositeObject>> childrenDataSources);
	
	/**
	 * 
	 * @param templatePath
	 * @param outputName
	 * @param outputPath
	 * @param outputFormat
	 * @param mergefields
	 * @param nestedDataSource
	 * @return
	 */
	public abstract FileOperationMessage generateDocumentWithNestedRegions(String templatePath, String outputName, String outputPath, String outputFormat, List<TemplateMergeField> mergefields, ch.ivyteam.ivy.scripting.objects.List<CompositeObject> nestedDataSource);
	
	/**
	 * 
	 * @param templatePath
	 * @param outputName
	 * @param outputPath
	 * @param outputFormat
	 * @param mergefields
	 * @param treeDataSource
	 * @return
	 */
	public abstract FileOperationMessage generateDocumentWithNestedRegions(String templatePath, String outputName, String outputPath, String outputFormat, List<TemplateMergeField> mergefields, Tree treeDataSource);
	
	/**
	 * Method to generate one or more documents each one can be saved in a different destination folder.<br>
	 * Each DocumentTemplate Object represents a letter that should be generated. It contains the fileName, its format, path,
	 * and all the TemplateMergefields.
	 * @param list : List of List of DocumentTemplate. <br>
	 * Each List of TemplateMergeField objects in the primary List will be turned into a new document.
	 * @return The List of fileOperationMessage objects (one for each DocumentTemplate) containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and File Object<br>
	 *  @see DocumentTemplate
	 */
	public abstract List<FileOperationMessage> generateDocumentsWithDifferentDestination(List<DocumentTemplate> list);
	
	/**
	 * Method to generate multiple documents appended in one File
	 * @param outputName: the name of the new created letter if null or empty, we try to get the first<br>
	 * MergeField form the first element in the List. If it has the Key name "filename", we take it, else we take "serialLetter".
	 * @param outputPath : where to save the new generated File on the server
	 * @param outputFormat : format ".doc", ".docx", ".pdf", or ".html" 
	 * @param list : List of List of parameters (TemplateMergeField objects). <br>
	 * Each List of TemplateMergeField objects in the primary List will be turned into a new page in the final File.
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and <br>
	 *  the java.io.File generated in case of success, else null.<br> 
	 
	public abstract fileOperationMessage generateMultipleDocumentsInOne(String outputName, String outputPath, String outputFormat,List<List<TemplateMergeField>> list);
	*/
	
	/**
	 * Method to generate multiple documents appended in one File
	 * @param templatePath : the path leading to the template (document model) used to produce the letters with mail merging
	 * @param outputName : the name of the generated java.io.File
	 * @param outputPath : where to save the new generated File on the server
	 * @param outputFormat : format ".doc", ".docx", ".pdf", or ".html" 
	 * @param list : List of List of parameters (TemplateMergeField objects). <br>
	 * Each List of TemplateMergeField objects in the primary List will be turned into a new page in the final File.
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and <br>
	 *  the java.io.File generated in case of success, else null.<br> 
	 */
	public abstract FileOperationMessage generateMultipleDocumentsInOne(String templatePath, String outputName, String outputPath, String outputFormat,List<List<TemplateMergeField>> list);
	
	/**
	 * Method to generate multiple documents appended in one File<br>
	 * Here the template path has to be indicated into the documentTemplate objects
	 * @param outputPath : where to save the new generated File on the server (can have a default like "ivy_RIA_files")
	 * @param outputName : the desired Name for the document (can have a default like "letter_nanotime")
	 * @param outputFormat : format ".doc" (can be the default), ".docx", ....
	 * @param list : List DocumentTemplate Objects. Each one contains the necessary variables for its output. 
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and <br>
	 *  the java.io.File generated in case of success, else null.<br> 
	 * @see DocumentTemplate
	 */
	public abstract FileOperationMessage generateMultipleDocumentsInOne(String outputPath, String outputName, String outputFormat,List<DocumentTemplate> list);
	
	/**
	 * returns the list of the Merge Fields names in a template
	 * @param templatePath : the path to the template File
	 * @return the list of the Fields Name in the Template
	 */
	public abstract ArrayList<String> getTemplateFields(String templatePath);
	
	/**
	 * get the Class Object of the current BaseDocFactory implementation Class
	 * @return the class Object of the current BaseDocFactory implementation Class
	 */
	public abstract Class<?> getFactoryClass();
	
	/**
	 * This method should check if the format denoted by the String param is supported by your document Factory
	 * @param _format
	 * @return true if the given format is supported, else false
	 */
	public abstract boolean isFormatSupported(String _format);
	
	/**
	 * set the output path where the generated Files should be saved
	 * @param outputPath
	 */
	public void setOutputPath(String outputPath){
		this.outputPath=outputPath;
	}
	/**
	 * Get the outputPath where the generated Files are saved
	 * @return the outputPath
	 */
	public String getOutputPath(){
		return this.outputPath;
	}
	/**
	 * set the desired Format
	 * @param format : the string representation of the desired outputFormat. Cannot be blank.
	 */
	public void setFormat(String format) {
		this.outputFormat = setFormatWithoutLeadingDot(format);
	}

	protected String setFormatWithoutLeadingDot(String format) {
		API.checkNotBlank(format, "the format");
		return format.startsWith(".") ? format.substring(1) : format;
	}
	
	/**
	 * get the outputFormat as String
	 * @return The outputFormat
	 */
	public String getFormat(){
		return this.outputFormat;
	}

	/**
	 * get the supported file output formats
	 * @return an array of Strings representing the supported formats
	 */
	public static String[] getSupportedFormats(){
		return SUPPORTED_OUTPUT_FORMATS;
	}
	
	
	/**
	 * get the basic File name that is going to be used to name the generated documents
	 * @return the basic filename used to generate multiple documents. If null or blank, the name of the file should be given as parameter
	 */
	public String getBasicFileName() {
		return basicFileName;
	}
	
	/**
	 * set the basicfilename used to generate multiple documents. If null or blank, the name of the file should be given as parameter
	 * @param basicFileName 
	 */
	public void setBasicFileName(String basicFileName) {
		this.basicFileName = basicFileName;
	}
	/**
	 * get the template that is used currenty for document generation
	 * @return the template as java.io.File
	 */
	public File getTemplate() {
		return template;
	}
	/**
	 * Set the template File to be used for the documents generation
	 * @param template java.io.File
	 */
	public void setTemplate(File template) {
		this.template = template;
	}
	
	/**
	 * This method allows getting an instance of the actual document factory that is implemented.
	 * If a System property "document.factory" is set, then it will try to return a new Instance of this factory object.<br>
	 * This system property must contain the full namespace of the document factory class.
	 * @return an Instance of the implemented Document Factory
	 */
	public static BaseDocFactory getInstance() {
		try {
			if(StringUtils.isBlank(System.getProperty(DOCUMENT_FACTORY_IMPLEMENTATION_SYSTEM_PROPERTY))){
				return new AsposeDocFactory();
			}else{
				return (BaseDocFactory) Class.forName(System.getProperty("document.factory")).getDeclaredConstructor().newInstance();
			}
		} catch (Exception e) {
			Ivy.log().error("Exception generating the docFactory. "+e.getMessage(),e);
			return null;
		} 
	}
	
	/**
	 * Set the response handler. 
	 * A ResponseHandler must implement the handleDocFactoryResponse method accepting a {@link FileOperationMessage} object.
	 * @param responseHandler
	 * @return
	 */
	public BaseDocFactory withResponseHandler(ResponseHandler responseHandler) {
		this.responseHandler = responseHandler;
		return this;
	}
	
	protected ResponseHandler getResponsHandler() {
		return this.responseHandler;
	}
	
	protected Object fieldMergingCallback;
	
	/**
	 * @deprecated
	 * Deprecated because of typo in method name. Please use {@link #withFieldMergingCallBack(Object)} instead.
	 * Will be removed in the future.
	 * @param fieldMergingCallback
	 * @return
	 */
	@Deprecated
	public <T> BaseDocFactory withFielMergingCallBack(T fieldMergingCallback) {
		return this.withFieldMergingCallBack(fieldMergingCallback);
	}
	
	/**
	 * Some DocFactory may allow injecting its own FieldMerging plug-in class for performing mail merging.
	 * A good example is the {@link ch.ivyteam.ivy.addons.docfactory.aspose.AsposeFieldMergingCallback} for Aspose.<br>
	 * <b>Note:</b> at the time of the writing of this method, the default DocFactory is based on Aspose. 
	 * As there is already a default one, You don't need to set the fieldMergingCallback unless you really need it.
	 * @param fieldMergingCallback
	 * @return the baseDocFactory with the given fieldMergingCallback set.
	 */
	public <T> BaseDocFactory withFieldMergingCallBack(T fieldMergingCallback) {
		API.checkNotNull(fieldMergingCallback, "fieldMergingCallback");
		this.fieldMergingCallback = fieldMergingCallback;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getFieldMergingCallBack() {
		return (T) fieldMergingCallback;
	}
	
	protected Object documentWorker;
	
	/**
	 * Some DocFactory may allow injecting a DocumentWorker class which can apply some custom logic 
	 * on the document before or after mail merging and before it has been returned to the user.
	 * <b>Note:</b> at the time of the writing of this method, the default DocFactory is based on Aspose.
	 * A DocumentWorker Interface has been made for the AsposeDocFactory. See: {@link ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker} <br>
	 * The AsposeDocFactory implements also its own {@link AsposeDocFactory#withDocumentWorker(DocumentWorker)} method, 
	 * in the case some instantiate the AsposeDocFactory and not through {@link #getInstance()}.<br>
	 * For the moment, only implementations of {@link ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker} are supported. Other kind of object will be ignored.
	 * @param documentWorker
	 * @return the DocFactory with the DocumentWorker set
	 */
	public <T> BaseDocFactory withDocumentWorker(T documentWorker) {
		this.documentWorker = documentWorker;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getDocumentWorker() {
		return (T) documentWorker;
	}
	
	/**
	 * Allows defining exactly how the document will be cleaned after the simple mail merge. 
	 * Use this only if you need to change the way the cleanup is done after simple mail merging.
	 * By Default, the blank fields are removed and all the lines containing only blank fields are also removed.
	 * @param simpleMergeCleanupOption cannot be null.
	 * @return the baseDocfactory which simpleMergeCleanupOptions are set.
	 */
	public abstract BaseDocFactory withSimpleMergeCleanupOption (SimpleMergeCleanupOptions simpleMergeCleanupOption);
	
	public abstract SimpleMergeCleanupOptions getSimpleMergeCleanupOptions();
	
	/**
	 * Allows defining exactly how the document will be cleaned after the mail merge with regions.
	 * Use this only if you need to change the way the cleanup is done after mail merging with regions.
	 * by default, and additionally to the default of the SimpleMergeCleanupOptions, 
	 * the empty and unused regions, the unused fields and the containing empty fields are removed.
	 * @param mergeCleanupOption cannot be null.
	 * @return the baseDocfactory which MergeCleanupOptions are set.
	 */
	public abstract BaseDocFactory withRegionsMergeCleanupOption (MergeCleanupOptions mergeCleanupOption);
	
	public abstract MergeCleanupOptions getRegionsMergeCleanupOptions();
	
	DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance();

	public BaseDocFactory withDocumentCreationOptions(DocumentCreationOptions documentCreationOptions) {
		API.checkNotNull(documentCreationOptions, "documentCreationOptions");
		this.documentCreationOptions = documentCreationOptions;
		return this;
	}
}
