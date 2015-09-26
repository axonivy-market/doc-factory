package ch.ivyteam.ivy.addons.docfactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import ch.ivyteam.ivy.scripting.objects.CompositeObject;
import ch.ivyteam.ivy.scripting.objects.Recordset;
import ch.ivyteam.ivy.scripting.objects.Tree;
import ch.ivyteam.ivy.addons.docfactory.TemplateMergeField;
import ch.ivyteam.ivy.addons.util.RDCallbackMethodHandler;
import ch.ivyteam.ivy.addons.docfactory.BaseDocFactory;
import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeDocFactoryFileGenerator;
import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeFieldMergingCallback;
import ch.ivyteam.ivy.addons.docfactory.aspose.MailMergeDataSource;

import com.aspose.words.Document;
import com.aspose.words.ImportFormatMode;
import com.aspose.words.MailMergeCleanupOptions;
import com.aspose.words.Node;
import com.aspose.words.SaveFormat;
import com.aspose.words.Section;
import com.aspose.words.SectionStart;


/**
 * @author ec<br>
 * @since 01.01.2009 in SharedResources. In IvyAddons since 01.11.2009
 * This the implementation of the BaseDocFactory class that uses the Aspose proprietary API.<br>
 * http://www.aspose.com<br>
 * You have the possibility to communicate the resultsand errors of the documents generation methods<br>
 * to the calling Parent Ivy Rich Dialog through CallBack Methods.<br> 
 * Those methods are indicated in the xxxMethodName variables. Those methods have to be published into
 * the Ivy RD Interface.  
 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#errorMethodName
 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#successMethodName
 * 
 */
public class AsposeDocFactory extends BaseDocFactory{

	/** The supportedFormat in this implementation of the BaseDocFactory */
	static final public String[] supportedOutputFormats = new String[] {"doc", "docx", "html", "txt", "pdf"};

	/** Aspose.Word Document objects used to perform the document merge (letter generation with MergeFields)*/
	private Document doc, docDest;

	private AsposeFieldMergingCallback fieldMergingCallback=null;
	

	/** Aspose Licence*/
	private com.aspose.words.License license = null;

	/** String used to stored the fileName used to generate a document */
	String outputName;

	/**
	 * Constructor
	 *
	 */
	public AsposeDocFactory(){
		super();
		parseLicence();

	}

	/**
	 * Constructor with the following parameters:
	 * @param parent : the IRichDialogPanel from the RIA where this docFactory is used. Usefull to communicate through callback methods.
	 * @param errorMethod @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#errorMethodName
	 * @param successMethod @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#successMethodName
	 * @param progressMethod @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#progressMethodName
	 */
	public AsposeDocFactory(IRichDialogPanel parent, String errorMethod, String successMethod, String progressMethod){
		super(parent, errorMethod, successMethod, progressMethod);
		parseLicence();
	}

	/**
	 * Not public API, for Tests only.
	 * @param parent
	 * @param errorMethod
	 * @param successMethod
	 * @param progressMethod
	 * @param fileMergingCallback
	 */
	protected AsposeDocFactory(IRichDialogPanel parent, String errorMethod, String successMethod, String progressMethod, AsposeFieldMergingCallback fieldMergingCallback) {
		super(parent, errorMethod, successMethod, progressMethod);
		this.fieldMergingCallback = fieldMergingCallback;
	}

	private void parseLicence() {
		this.license = new com.aspose.words.License();
		try {
			InputStream in = ThirdPartyLicenses.getDocumentFactoryLicense();
			this.license.setLicense(in);
			in.close();
		} catch (Exception e) {
			Ivy.log().error("Aspose Words Licence error",e);
			this.fileOperationMessage= FileOperationMessage.generateErrorTypeFileOperationMessage("Aspose Words Licence error "+e.getMessage());
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
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
	public FileOperationMessage generateBlankDocument(String _outputName, String _outputPath, String _outputFormat) {
		this.fileOperationMessage=new FileOperationMessage();
		//we check if the given file name is valid
		if(!FileUtil.isFileNameValid(_outputName)) {
			this.fileOperationMessage= FileOperationMessage.generateErrorTypeFileOperationMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/invalidFileName"));
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
		}
		this.setBasicFileName(_outputName);

		if(_outputPath == null || _outputPath.trim().equalsIgnoreCase("")) {
			this.setOutputPath(FileUtil.formatPathWithEndSeparator("ivy_RIA_files"));
		}else {
			this.setOutputPath(FileUtil.formatPathWithEndSeparator(_outputPath));
		}
		this.setFormat(_outputFormat);
		
		String baseDocPath= this.outputPath+this.basicFileName;
		int format = getFormatPosition(_outputFormat,UNSUPPORTED_FORMAT);
		
		try {
			this.fileOperationMessage = AsposeDocFactoryFileGenerator.exportDocumentToFile(null, baseDocPath, format, true);
		} catch (Exception e) {
			this.fileOperationMessage= FileOperationMessage.generateErrorTypeFileOperationMessage(e.getMessage());
		}
		if(this.fileOperationMessage.isError()) {
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
		}
		return fileOperationMessage;
	}



	/** 
	 * Method to generate one document<br>
	 * Mail merge with regions is now supported
	 * @param _documentTemplate the DocumentTemplate Object containing all the necessary variables for this operation<br>
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and <br>
	 *  the java.io.File generated in case of success, else null.<br>
	 *  @see DocumentTemplate
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateDocument(ch.ivyteam.ivy.addons.docfactory.DocumentTemplate)
	 */
	@Override
	public FileOperationMessage generateDocument(DocumentTemplate _documentTemplate) {
		if(_documentTemplate != null)
		{ //The DocumentTemplate is not null
			if(_documentTemplate.getTablesNamesAndFieldsHashtable()!=null && !_documentTemplate.getTablesNamesAndFieldsHashtable().isEmpty()) {
				return generateDocumentWithRegions(_documentTemplate.getTemplatePath(),
						_documentTemplate.getOutputName(),
						_documentTemplate.getOutputPath(),
						_documentTemplate.getOutputFormat(),
						_documentTemplate.getMergeFields(), 
						_documentTemplate.getTablesNamesAndFieldsHashtable());
			}else if(_documentTemplate.getTablesNamesAndFieldsmap()!=null && !_documentTemplate.getTablesNamesAndFieldsmap().isEmpty()) {
				return generateDocumentWithRegions(_documentTemplate.getTemplatePath(),
						_documentTemplate.getOutputName(),
						_documentTemplate.getOutputPath(),
						_documentTemplate.getOutputFormat(),
						_documentTemplate.getMergeFields(), 
						_documentTemplate.getTablesNamesAndFieldsmap());
			}else if(_documentTemplate.getParentDataSourceForNestedMailMerge()!=null && !_documentTemplate.getParentDataSourceForNestedMailMerge().isEmpty()) {
				return generateDocumentWithNestedRegions(_documentTemplate.getTemplatePath(),
						_documentTemplate.getOutputName(),
						_documentTemplate.getOutputPath(),
						_documentTemplate.getOutputFormat(),
						_documentTemplate.getMergeFields(), 
						_documentTemplate.getParentDataSourceForNestedMailMerge(), 
						_documentTemplate.getChildrenDataSourcesForNestedMailMerge());
			}else if(_documentTemplate.getNestedDataSourceForNestedMailMerge()!=null && !_documentTemplate.getNestedDataSourceForNestedMailMerge().isEmpty()) {
				return generateDocumentWithNestedRegions(_documentTemplate.getTemplatePath(),
						_documentTemplate.getOutputName(),
						_documentTemplate.getOutputPath(),
						_documentTemplate.getOutputFormat(),
						_documentTemplate.getMergeFields(), 
						_documentTemplate.getNestedDataSourceForNestedMailMerge());
			}else if(_documentTemplate.getTreeData()!=null) {
				return generateDocumentWithNestedRegions(_documentTemplate.getTemplatePath(),
						_documentTemplate.getOutputName(),
						_documentTemplate.getOutputPath(),
						_documentTemplate.getOutputFormat(),
						_documentTemplate.getMergeFields(), 
						_documentTemplate.getTreeData());
			}else {
				return generateDocument(_documentTemplate.getTemplatePath(),
						_documentTemplate.getOutputName(),
						_documentTemplate.getOutputPath(),
						_documentTemplate.getOutputFormat(),
						_documentTemplate.getMergeFields());
			}
		}else
		{//will return a fileOperationMessage with an Error
			return generateDocument(null, null, null, null, null);
		}
	}

	/**
	 * Generates a document. The File generated will have the given name, will be at the given _outputpath, in the given format,<br>
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
	public FileOperationMessage generateDocument(String _templatePath, String _outputName, String _outputPath, String _outputFormat, List<TemplateMergeField> _mergeFields) {
		//Check the parameters
		this.resetAndCheckCommonParameters(_templatePath, _outputName, _outputPath, _outputFormat, _mergeFields);

		if(this.fileOperationMessage.isError()) {
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
			return this.fileOperationMessage;
		}

		ArrayList<TemplateMergeField> templateParamslist =new ArrayList<TemplateMergeField>();
		templateParamslist.addAll(_mergeFields);

		if(_outputName==null || _outputName.trim().equalsIgnoreCase("")) {
			//if the filename was not indicated, we look if the first mergeField Object could contain the fileName
			if(templateParamslist.get(0).getMergeFieldName().trim().equalsIgnoreCase("filename")) {
				// it contains the file name
				this.outputName=templateParamslist.get(0).getMergeFieldValue().trim();
			} else {
				// no file Name indicated, we have to take a default one
				this.outputName="serialLetter_"+System.nanoTime();
			}
		} else {
			this.outputName=_outputName;
		}

		if(!FileUtil.isFileNameValid(_outputName)) {
			// if the filename contains invalid characters
			this.fileOperationMessage=FileOperationMessage.generateErrorTypeFileOperationMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/invalidFileName"));
		} else {
			this.fileOperationMessage=FileOperationMessage.generateSuccessTypeFileOperationMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/serialLetterSuccess"));
			this.setFormat(_outputFormat);

			try {
				File file = this.doMailMerge(templateParamslist);
				this.fileOperationMessage.addFile(file);
			} catch (Exception e) {
				this.fileOperationMessage=FileOperationMessage.generateErrorTypeFileOperationMessage(e.getMessage());
			}
		}
		if(this.fileOperationMessage.isError()) {
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
		}
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
			emailText = this.doMailMerge(templatePath, list).toTxt();
		} catch (Exception e) {
			this.fileOperationMessage=FileOperationMessage.generateErrorTypeFileOperationMessage(e.getMessage());
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });	
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
		if(this.outputPath==null || this.outputPath.trim().equalsIgnoreCase(""))
		{//set the outputPath if not yet done.
			this.outputPath="ivy_RIA_files";
		}
		this.outputPath = FileUtil.formatPathWithEndSeparator(this.outputPath);
		try {
			String randomPart= new Long(System.nanoTime()).toString();
			String s = this.outputPath+"tmpHTML_" + randomPart+".html";
			this.doMailMerge(templatePath, list).save(s, SaveFormat.HTML);

			StringBuffer fileData = new StringBuffer();
			BufferedReader reader = new BufferedReader(
					new FileReader(s));
			char[] buf = new char[1024];
			int numRead=0;
			while((numRead=reader.read(buf)) != -1){
				fileData.append(buf, 0, numRead);
			}
			reader.close();
			emailText =fileData.toString();
		} catch (Exception e) {
			this.fileOperationMessage=FileOperationMessage.generateErrorTypeFileOperationMessage(e.getMessage());
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });	
		}
		return emailText;
	}


	/**
	 * Method to generate a list of Files that can be used for HTML representation.<br>
	 * This this typical if you use a template with Objects like Images in it.<br>
	 * During the html File Generation, each image will be saved into a jpg File.<br>
	 * If you want to get correctly all the Files involved into the HTML rendering, you have to get the list of images also.
	 * @param templatePath : where to find the template
	 * @param list : List of parameters (TemplateMergeField objects).
	 * @return the HTML and media Files that were generated to represent the HTML page.
	 */
	@Override
	public ArrayList<java.io.File> generateFilesForHTML(String templatePath, List<TemplateMergeField> list) {
		ArrayList<java.io.File> HTMLfiles=new ArrayList<java.io.File>();
		this.outputName="tmpHTML_"+ new Long(System.nanoTime()).toString();
		if(this.outputPath==null | this.outputPath.trim().equalsIgnoreCase("")) {
			this.outputPath="ivy_RIA_files";
		}
		this.outputPath = FileUtil.formatPathWithEndSeparator(this.outputPath);

		if(list.get(0).getMergeFieldName().trim().equalsIgnoreCase("filename")) {
			this.outputName=list.get(0).getMergeFieldValue().trim();
		}

		try {
			String s = this.outputPath+this.outputName+".html";
			this.doMailMerge(templatePath, list).save(s, SaveFormat.HTML);
			HTMLfiles.add(new java.io.File(s));
			HTMLfiles.addAll(FileUtil.getFilesWithPattern("\\.[0-9].*$", this.outputName, this.outputPath));
		} catch (Exception e) {
			this.fileOperationMessage=FileOperationMessage.generateErrorTypeFileOperationMessage(e.getMessage());
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });	
		}
		return HTMLfiles;
	}


	/**
	 * Generate Documents with a List of DocumentTemplate objects.<br>
	 * Here each of the DocumentTemplate will generate a single File.<br>
	 * Each single DocumentTemplate can have its own format, path, template...
	 * The Merge Mail with regions (Table) is now supported.
	 * @param list of DocumentTemplate: List<DocumentTemplate> list
	 * @return a fileOperationMessage. This object contains the type of Message (SUCCESS, ERROR), the message, <br>
	 * and the List of generated Files. If a DocumentTemplate didn't contain valid parameters, its output File will be null. <br>
	 * If you want to have an exact trace of the operation's result for each DocumentTemplate, <br>
	 * use the generateDocumentsWithDifferentDestination(List<DocumentTemplate> list) method instead. This method returns a fileOperationMessage for each DocumentTemplate.
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateDocuments(java.util.List)
	 * @see #generateDocuments(String, String, String, List)
	 */
	@Override
	public FileOperationMessage generateDocuments(List<DocumentTemplate> list) {
		FileOperationMessage fom = new FileOperationMessage();
		if(list==null || list.isEmpty()) {
			//list null or empty => we return an ERROR
			fom.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			fom.setMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/EmptyMergeFields"));
			fom.emptyFileList();
		} else { // we go through the list of Document template Objects and make a new File with each of them
			Iterator<DocumentTemplate> iter = list.iterator();
			ArrayList<java.io.File> files = new ArrayList<java.io.File>();
			while(iter.hasNext()) {
				try {
					DocumentTemplate dt = iter.next();
					String s = FileUtil.formatPathWithEndSeparator(dt.getOutputPath())+dt.getOutputName();
					int format = getFormatPosition(dt.getOutputFormat(),PDF_FORMAT);
					FileOperationMessage fomessage  = AsposeDocFactoryFileGenerator.exportDocumentToFile(this.doMailMerge(dt), s, format, false);
					
					if(fomessage.isSuccess() && fomessage.getFiles().size()==1) {
						files.add(fomessage.getFiles().get(0));
					}
				} catch (Exception e) {
					//do nothing and generate the next one.
				}
			}
			fom.addFiles(files);
		}
		return fom;
	}

	/**
	 * Method to generate one or more documents in the same destination folder<br>
	 * By convention, the name of each File is given by the first TemplateMergeField, which key name must be "filename".<br>
	 * @param _outputPath : where to save the new generated File on the server
	 * @param _outputFormat : format ".doc", ".docx", ".pdf", or ".html" 
	 * @param list : List of List of parameters (TemplateMergeField objects). <br>
	 * Each List of TemplateMergeField objects in the primary List will be turned into a new document.
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and null File Object<br>
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateDocuments(String, String, String, List)
	 * @see ch.ivyteam.ivy.addons.util.FileOperationMessage
	 */
	@Override
	public FileOperationMessage generateDocuments(String templatePath, String _outputPath, final String _outputFormat, final List<List<TemplateMergeField>> list) {
		//series of check to see if no exceptions
		this.template = new java.io.File(FileUtil.formatPath(templatePath));
		if(!doCheckBeforeDocGeneration(_outputFormat, list)) {
			return this.fileOperationMessage;
		}

		if(_outputPath == null || _outputPath.trim().equalsIgnoreCase("")) {
			this.setOutputPath(FileUtil.formatPathWithEndSeparator("ivy_RIA_files"));
		}else {
			this.setOutputPath(FileUtil.formatPathWithEndSeparator(_outputPath));
		}

		this.setFormat(_outputFormat);
		int format = getFormatPosition(_outputFormat,PDF_FORMAT);

		//reset the fileOperationMessage object
		this.fileOperationMessage=FileOperationMessage.generateSuccessTypeFileOperationMessage("");

		//iterator to parse all of the List<TemplateMergeField>, each one is a different letter
		final Iterator<List<TemplateMergeField>> iter = list.iterator();

		while(iter.hasNext()) {// there are still letters that have not been written
			ArrayList<TemplateMergeField> templateParamslist =new ArrayList<TemplateMergeField>();
			templateParamslist.addAll(iter.next());

			//Try to get the filename from the first template merge field found
			if(templateParamslist.get(0).getMergeFieldName().trim().equalsIgnoreCase("filename")) {
				this.outputName=templateParamslist.get(0).getMergeFieldValue().trim();
			}else {//we didn't find the filename merge field, we take the default serial letter name
				this.outputName="serialLetter_"+System.nanoTime();
			}
			String baseDocPath= this.outputPath+this.outputName;
			try {
				FileOperationMessage fom  = AsposeDocFactoryFileGenerator.exportDocumentToFile(this.doMailMerge(templatePath, templateParamslist), baseDocPath, format, false);
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
		return this.fileOperationMessage;
	}

	/**
	 * Method to generate one or more documents each one can be saved in a different destination folder.<br>
	 * By convention, the name of each File is given by the first TemplateMergeField, which key name must be "filename".<br>
	 * By convention, the name of each destination folder is given by the second TemplateMergeField, which key name must be "destinationPath".<br>
	 * @param templatePath : where to find the template for the Merging operation
	 * @param _outputFormat : format ".doc", ".docx", or ".html" 
	 * @param list : List of List of parameters (TemplateMergeField objects). <br>
	 * Each List of TemplateMergeField objects in the primary List will be turned into a new document.
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and null File Object<br>
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateDocumentsWithDifferentDestination(String, String, List)
	 * @see ch.ivyteam.ivy.addons.util.FileOperationMessage
	 */


	@Override
	public FileOperationMessage generateDocumentsWithDifferentDestination(String templatePath, final String _outputFormat, final List<List<TemplateMergeField>> list) {

		if(!doCheckBeforeDocGeneration(_outputFormat, list)) {
			return this.fileOperationMessage;
		}
		
		this.setFormat(_outputFormat);
		int format = getFormatPosition(_outputFormat,PDF_FORMAT);

		this.fileOperationMessage=FileOperationMessage.generateSuccessTypeFileOperationMessage("");
		final Iterator<List<TemplateMergeField>> iter = list.iterator();

		while(iter.hasNext())
		{// there are still letters that have not been written
			ArrayList<TemplateMergeField> templateParamslist =new ArrayList<TemplateMergeField>();
			templateParamslist.addAll(iter.next());

			String destinationPath;// the specific destination path for this letter
			//get the name of the document
			if(templateParamslist.get(0).getMergeFieldName().trim().equalsIgnoreCase("filename")) {
				outputName=templateParamslist.get(0).getMergeFieldValue().trim();
			} else {
				outputName="serialLetter_"+System.nanoTime();
			}

			//get the destination folder of the document
			if(templateParamslist.get(1).getMergeFieldName().trim().equalsIgnoreCase("destinationPath")) {
				destinationPath= FileUtil.formatPathWithEndSeparator(templateParamslist.get(1).getMergeFieldValue().trim(), true);
			}
			else {
				destinationPath= FileUtil.formatPathWithEndSeparator("ivy_RIA_files", true);
			}
			String baseDocPath= destinationPath+this.outputName;
			try {
				FileOperationMessage fom = AsposeDocFactoryFileGenerator
						.exportDocumentToFile(this.doMailMerge(templatePath, templateParamslist), baseDocPath, format, false);
				if(fom.isError()) {
					RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { fom });
				}else {
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
		return fileOperationMessage;
	}


	/**
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateDocumentsWithDifferentDestination(java.util.List)
	 * Here the mergeMail with region is now supported
	 */
	@Override
	public List<FileOperationMessage> generateDocumentsWithDifferentDestination(List<DocumentTemplate> list) {
		ArrayList <FileOperationMessage> l = new ArrayList <FileOperationMessage>();
		for(DocumentTemplate dt: list) {
			l.add(this.generateDocument(dt));
		}
		return l;
	}

	/**
	 * Method to generate multiple documents appended in one File.<br>
	 * With this method all the letters inside the serial letter are done with the same Template.
	 * @param templatePath : where to find the template for the Merging operation
	 * @param _outputName : the name of the new created letter if null or empty, we try to get the first<br>
	 * MergeField form the first element in the List. If it has the Key name "filename", we take it, else we take "serialLetter".
	 * @param _outputPath : where to save the new generated File on the server
	 * @param _outputFormat : format ".doc", ".docx", ".pdf", or ".html" 
	 * @param list : List of List of parameters (TemplateMergeField objects). <br>
	 * Each List of TemplateMergeField objects in the primary List will be turned into a new page in the final File.
	 * @return The fileOperationMessage object containing the Type of the message (FileHandler.SUCCESS, ERROR, INFORMATION_MESSAGE),<br>
	 *  the text of the message and <br>
	 *  the java.io.File generated in case of success, else empty List<java.io.File>.<br> 
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateMultipleDocumentsInOne(java.lang.String, java.lang.String, java.lang.String, java.util.List)
	 * @see ch.ivyteam.ivy.addons.util.FileOperationMessage
	 */
	@Override

	public FileOperationMessage generateMultipleDocumentsInOne(String templatePath, String _outputName, String _outputPath, String _outputFormat, List<List<TemplateMergeField>> list) {
		//template File
		this.template = new java.io.File(FileUtil.formatPath(templatePath));
		//		set the outPutPath
		if(_outputPath == null || _outputPath.trim().equalsIgnoreCase("")) {
			this.setOutputPath(FileUtil.formatPathWithEndSeparator("ivy_RIA_files"));
		}else {
			this.setOutputPath(FileUtil.formatPathWithEndSeparator(_outputPath));
		}
		// 		set the format
		this.setFormat(_outputFormat);
		//		set the output name
		if(_outputName == null || _outputName.trim().equalsIgnoreCase("") || !FileUtil.isFileNameValid(_outputName)) {
			// fileName parameter is invalid. We take the default name SerialLetter + System nanoTime String
			this.outputName="serialLetter_"+System.nanoTime();
		}else {
			this.outputName = _outputName;
		}
		// series of check to see if no exceptions -> this method checks the template, the format and if the list is not empty
		if(!doCheckBeforeDocGeneration(_outputFormat, list)) {
			// The inputParameters are not all valid. We return the actual fileOperationMessage with the error Message
			return this.fileOperationMessage;
		}
		//the input parameters are checked and valid	
		int format = getFormatPosition(_outputFormat,UNSUPPORTED_FORMAT);
		this.fileOperationMessage=FileOperationMessage.generateSuccessTypeFileOperationMessage("");

		//iterator to parse all of the List<TemplateMergeField>, each one is a different letter
		final Iterator<List<TemplateMergeField>> iter = list.iterator();
		try  {
			docDest = doMailMerge(templatePath, iter.next());
			//appends the rest of the documents to the first one
			while(iter.hasNext()) {
				doc = doMailMerge(templatePath, iter.next());
				doc.getFirstSection().getPageSetup().setSectionStart(SectionStart.NEW_PAGE);

				AppendDoc(docDest, doc);
			}
			this.fileOperationMessage  = AsposeDocFactoryFileGenerator.exportDocumentToFile(this.docDest, this.outputPath+this.outputName, format, false);
		}
		catch (Exception ex) {
			this.fileOperationMessage=FileOperationMessage.generateErrorTypeFileOperationMessage(ex.getMessage());
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
		}
		return this.fileOperationMessage;
	}


	/**
	 * With this method each letter inside the serial letter can be made with its own template.<br>
	 * The MailMerge With regions is supported
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#generateMultipleDocumentsInOne(String, String, String, List)
	 */
	@Override
	public FileOperationMessage generateMultipleDocumentsInOne(String _outputPath, String _outputName, String _outputFormat, List<DocumentTemplate> list) {
		//series of check to see if no exceptions
		if(list == null || list.isEmpty()) {
			// the list is empty: the FielOperationMessage Object contains the error and we return it
			this.fileOperationMessage = FileOperationMessage.generateErrorTypeFileOperationMessage(
					Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/EmptyMergeFields"));
			return this.fileOperationMessage;
		}	

		if(_outputPath == null || _outputPath.trim().equalsIgnoreCase("")) {
			this.setOutputPath(FileUtil.formatPathWithEndSeparator("ivy_RIA_files"));
		}else {
			this.setOutputPath(FileUtil.formatPathWithEndSeparator(_outputPath));
		}
		//		set the output name
		if(_outputName == null || _outputName.trim().equalsIgnoreCase("") || !FileUtil.isFileNameValid(_outputName)) {
			// fileName parameter is invalid. We take the default name SerialLetter + System nanoTime String
			this.outputName="serialLetter_"+System.nanoTime();
		}else {
			this.outputName = _outputName;
		}
		this.setFormat(_outputFormat);

		this.fileOperationMessage=FileOperationMessage.generateSuccessTypeFileOperationMessage("");
		
		int format = getFormatPosition(this.outputFormat,UNSUPPORTED_FORMAT);
		
		//iterator to parse all of the DocumentTemplate objects, each one is a different letter
		final Iterator<DocumentTemplate> iter = list.iterator();
		try  {
			// we do the mailMerge for the first DocumentTemplate
			docDest = this.doMailMerge(iter.next());

			//appends the rest of the documents to the first one
			while(iter.hasNext()) {
				doc=doMailMerge(iter.next());
				doc.getFirstSection().getPageSetup().setSectionStart(SectionStart.NEW_PAGE);
				AppendDoc(docDest, doc);
			}
			this.fileOperationMessage  = AsposeDocFactoryFileGenerator.exportDocumentToFile(this.docDest, this.outputPath+this.outputName, format, false);
		}
		catch (Exception ex) {
			this.fileOperationMessage=FileOperationMessage.generateErrorTypeFileOperationMessage(ex.getMessage());
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
		}
		return this.fileOperationMessage;
	}


	/**
	 * For private use,<br> NEW the mergeMail with regions is supported
	 * @param _documentTemplate
	 * @return the document object resulting of the mail merging operation
	 * @throws Exception
	 */
	private Document doMailMerge(DocumentTemplate _documentTemplate) throws Exception{
		String t = FileUtil.formatPath(_documentTemplate.getTemplatePath());
		Document document = new Document(t);
		List<TemplateMergeField> fields = _documentTemplate.getMergeFields();

		//Get the mail with region data sources if there are any...
		ArrayList<MailMergeDataSource> mmds = new ArrayList<MailMergeDataSource>();
		if(_documentTemplate.getTablesNamesAndFieldsHashtable() != null 
				&& !_documentTemplate.getTablesNamesAndFieldsHashtable().isEmpty()) {
			Set<String> tables = _documentTemplate.getTablesNamesAndFieldsHashtable().keySet();
			Iterator<String> iter = tables.iterator();
			while(iter.hasNext()){
				String key = iter.next();
				try {
					mmds.add(new MailMergeDataSource(_documentTemplate.getTablesNamesAndFieldsHashtable().get(key), key));
				}catch(Exception ex) {
					Ivy.log().error("The DataClass Objects provided to generateDocumentWithRegions are not all from the same Type. This is not allowed. The process will ignore the table "+key, ex);
				}
			}

		}else if(_documentTemplate.getTablesNamesAndFieldsmap() != null 
				&& !_documentTemplate.getTablesNamesAndFieldsmap().isEmpty()) {
			Set<String> tables = _documentTemplate.getTablesNamesAndFieldsmap().keySet();
			Iterator<String> iter = tables.iterator();
			while(iter.hasNext()) {
				String key = iter.next();
				try {
					mmds.add(DataClassToMergefields.transfomDataClassInMergeDataSource(_documentTemplate.getTablesNamesAndFieldsmap().get(key), key));
				}catch(Exception ex) {
					Ivy.log().error("The DataClass Objects provided to generateDocumentWithRegions are not all from the same Type. This is not allowed. The process will ignore the table "+key, ex);
				}
			}
		}

		if(_documentTemplate.getParentDataSourceForNestedMailMerge()!=null 
				&& !_documentTemplate.getParentDataSourceForNestedMailMerge().isEmpty()) {
			mmds.add(new MailMergeDataSource(_documentTemplate.getParentDataSourceForNestedMailMerge(), _documentTemplate.getChildrenDataSourcesForNestedMailMerge()));
		}

		if(_documentTemplate.getNestedDataSourceForNestedMailMerge()!=null 
				&& !_documentTemplate.getNestedDataSourceForNestedMailMerge().isEmpty()) {
			mmds.add(new MailMergeDataSource(_documentTemplate.getNestedDataSourceForNestedMailMerge()));
		}
		if(_documentTemplate.getTreeData()!=null) {
			mmds.add(new MailMergeDataSource(_documentTemplate.getTreeData()));
		}

		//Do normal Mail merge
		String [] paramName= new String[fields.size()];
		String [] paramValue= new String[fields.size()];

		Iterator<TemplateMergeField> iterator = fields.iterator();
		int i=0;
		while(iterator.hasNext()) {
			TemplateMergeField tmf = iterator.next();
			String s = tmf.getMergeFieldName();
			if(s.startsWith("Image:") || s.startsWith("Image_")) {
				//The merge field name indicates an Image, we take only the second part of the name
				paramName[i]=s.substring(6);
			}
			else {
				//The merge field is a text field
				paramName[i]=s;
			}

			paramValue[i]=tmf.getMergeFieldValue();
			i++;
		}

		//Set up the event handler for image fields and perform mail merge.
		document.getMailMerge().setFieldMergingCallback(this.getAsposeFieldMergingCallback());
		document.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS);
		document.getMailMerge().execute(paramName,paramValue);
		//do mail merge with regions if necessary
		if(!mmds.isEmpty()) {
			for(MailMergeDataSource m : mmds) {
				document.getMailMerge().executeWithRegions(m);
			}
		}
		//document.getMailMerge().setRemoveEmptyParagraphs(true);
		document.getMailMerge().deleteFields();

		return document;
	}

	/**
	 * 
	 * @param templatePath
	 * @param fields
	 * @return the document object resulting of the mail merging operation
	 * @throws Exception
	 */
	private Document doMailMerge(String templatePath, List<TemplateMergeField> fields) throws Exception{
		String t = FileUtil.formatPath(templatePath);
		Document document = new Document(t);
		String [] paramName= new String[fields.size()];
		String [] paramValue= new String[fields.size()];
		Iterator<TemplateMergeField> iterator = fields.iterator();
		int i=0;
		while(iterator.hasNext()) {
			TemplateMergeField tmf = iterator.next();
			String s = tmf.getMergeFieldName();
			if(s.startsWith("Image:") || s.startsWith("Image_")) {
				//The merge field name indicates an Image, we take only the second part of the name
				paramName[i]=s.substring(6);
			}
			else {//The merge field is a text field
				paramName[i]=s;
			}

			paramValue[i]=tmf.getMergeFieldValue();
			i++;
		}
		//Set up the event handler for image fields and perform mail merge.
		document.getMailMerge().setFieldMergingCallback(this.getAsposeFieldMergingCallback());
		document.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS);
		document.getMailMerge().execute(paramName,paramValue);
		//document.getMailMerge().setRemoveEmptyParagraphs(true);
		document.getMailMerge().deleteFields();
		return document;
	}

	/**
	 * performs a mail merge with a template. Gives the generated File back.
	 * @param parameters : the list of TemplateMergeField objects that correspond to the template's fields
	 * @return the java.io.File taht was generated.
	 * @throws Exception
	 */
	private java.io.File doMailMerge(ArrayList<TemplateMergeField> parameters) throws Exception {
		File file=null;
		String [] paramName= new String[parameters.size()];
		String [] paramValue= new String[parameters.size()];
		for(int i=0; i<parameters.size();i++){
			String s = parameters.get(i).getMergeFieldName();
			if(s.startsWith("Image:") || s.startsWith("Image_")) {
				//The merge field name indicates an Image, we take only the second part of the name
				paramName[i]=s.substring(6);
			}
			else {
				//The merge field is a text field
				paramName[i]=s;
			}

			paramValue[i]=parameters.get(i).getMergeFieldValue();
		}

		doc = new Document(this.template.getPath());
		int format = getFormatPosition(this.outputFormat,UNSUPPORTED_FORMAT);
		String baseFilePath = this.outputPath+this.outputName;
		//Set up the event handler for image fields and perform mail merge.
		doc.getMailMerge().setFieldMergingCallback(this.getAsposeFieldMergingCallback());
		doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS);
		doc.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
		doc.getMailMerge().execute(paramName,paramValue);

		//doc.getMailMerge().setRemoveEmptyParagraphs(true);
		//doc.getMailMerge().setRemoveEmptyRegions(true);
		doc.getMailMerge().deleteFields();
		this.fileOperationMessage  = AsposeDocFactoryFileGenerator.exportDocumentToFile(this.doc, baseFilePath, format, false);
		if(this.fileOperationMessage.isSuccess() && !this.fileOperationMessage.getFiles().isEmpty()) {
			file = this.fileOperationMessage.getFiles().get(0);
		}
			
		return file;
	}


	/**
	 * A useful function that you can use to easily append one document to another.
	 * @param dstDoc : The destination document where to append to
	 * @param srcDoc : The source document.
	 */
	private void AppendDoc(Document dstDoc, Document srcDoc) throws Exception {
		// Loop through all sections in the source document.
		// Section nodes are immediate children of the Document node so we can just enumerate the Document.
		int x = srcDoc.getSections().getCount();
		for (int i=0; i<x;i++) {
			// Because we are copying a section from one document to another,
			// it is required to import the Section node into the destination document.
			// This adjusts any document-specific references to styles, lists, etc.
			//
			// Importing a node creates a copy of the original node, but the copy
			// is ready to be inserted into the destination document.
			Section srcSection = srcDoc.getSections().get(i);
			Node dstSection = dstDoc.importNode(srcSection, true, ImportFormatMode.KEEP_SOURCE_FORMATTING);

			// Now the new section node can be appended to the destination document.
			dstDoc.appendChild(dstSection);
		}
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
	private int getFormatPosition(String format, int default_format_position_if_not_found) {
		int a=default_format_position_if_not_found;
		for(int i =0; i<supportedOutputFormats.length;i++) {
			if(format.trim().equalsIgnoreCase(supportedOutputFormats[i])) {
				a=i;
				break;
			}
		}
		return a;
	}

	/**
	 * returns a String Array containing all the fieldNames in a document
	 * @param _doc the aspose Word Document Object
	 * @return the ArrayList<String> of the MergeFields
	 */
	public String[] getTemplateFields(Document _doc) {
		String [] templateFields=null;
		try {
			templateFields= _doc.getMailMerge().getFieldNames();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return templateFields;
	}


	/**
	 * Gives the merge fields list from a template back.
	 * @param templatePath the template path
	 * @return the ArrayList<String> of the MergeFields
	 */
	@Override
	public ArrayList<String> getTemplateFields(String templatePath) {

		ArrayList<String> templateFields=new ArrayList<String>();
		try {
			Document _doc = new Document(templatePath);
			String [] sArray = _doc.getMailMerge().getFieldNames();
			for(String s: sArray) {
				templateFields.add(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Ivy.log().error("getTemplateFields error " +e.getMessage());
		}

		return templateFields;

	}

	/**
	 * Gives the merge fields list from a template back.
	 * @param templatePath the template path
	 * @return the ArrayList<String> of the MergeFields
	 */
	public static ArrayList<String> getFields(String templatePath){

		ArrayList<String> templateFields=new ArrayList<String>();
		try {
			Document doc = new Document(templatePath);
			String [] sArray = doc.getMailMerge().getFieldNames();
			for(String s: sArray){
				templateFields.add(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Ivy.log().error("getTemplateFields error " +e.getMessage());
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
		boolean flag=true;
		this.fileOperationMessage=new FileOperationMessage();
		if(format.startsWith(".")) format=format.substring(1);
		if(format==null || !isFormatSupported(format)) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/formatNotSupported")+" \n"+format);
			this.fileOperationMessage.emptyFileList();
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
			flag=false;
		}
		if(this.template==null || !this.template.exists()) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/templateCannotBefFound"));
			this.fileOperationMessage.emptyFileList();
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
			flag=false;
		}
		if(list == null) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/EmptyMergeFields"));
			this.fileOperationMessage.emptyFileList();
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
			flag=false;
		}
		return flag;
	}

	/**
	 * Check if the desired document output format is supported
	 * @param format : the String Format to chek, format without the point
	 * @return true if supported, else false
	 */
	@Override
	public boolean isFormatSupported(String format) {
		boolean flag = false;
		String s = format;
		if(s.startsWith(".")) s=format.substring(1);
		for(int i =0; i<supportedOutputFormats.length;i++) {
			if(s.trim().equalsIgnoreCase(supportedOutputFormats[i])) {
				flag=true;
				break;
			}
		}
		return flag;
	}

	/**
	 *
	 * @see ch.ivyteam.ivy.addons.docfactory.BaseDocFactory#getSupportedFormats()
	 */
	public static String[] getSupportedFormats(){
		return supportedOutputFormats;
	}



	/**
	 * We set the format. If the given format is invalid, we set it to "doc" per default.
	 */
	@Override
	public void setFormat(String _format) {
		if( _format==null|| _format.trim().equalsIgnoreCase("") || !isFormatSupported(_format)) {
			_format ="doc";
		}
		if(_format.startsWith(".")) {
			this.outputFormat = _format.substring(1);
		}else {
			this.outputFormat=_format;
		}
	}

	/**
	 * Utility private method to make all checks before beginning to produce documents.<br>
	 * During the check some empty or null parameters are going to be set with default values.
	 * @param _templatePath: the template path to check, this path cannot be null or empty and must drives to a valid java.io.File.
	 * @param _outputName: the outputName to check, if empty or null will be set with a random generated name.
	 * @param _outputPath: the output path to check, if empty or null will be set to "ivy_RIA_files".
	 * @param _outputFormat: the output format to check, if empty or null will be set with "doc"
	 * @param _fields: the templateMergeFields parameter to check cannot be null, may be an empty list.
	 * @return fileOperationMessage with ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE as Type if one of the parameter is invalid,<br>
	 * else the type will be ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.SUCCESS_MESSAGE.
	 */
	private FileOperationMessage resetAndCheckCommonParameters(String _templatePath, String _outputName, String _outputPath, String _outputFormat, List<TemplateMergeField> _fields){
		//reset the fileOperationMessage
		this.fileOperationMessage=new FileOperationMessage();
		this.fileOperationMessage.emptyFileList();

		if(_templatePath!=null && !_templatePath.trim().equalsIgnoreCase("")) {
			// if the template path param is valid
			this.template = new java.io.File(FileUtil.formatPath(_templatePath));
		}else {
			this.setTemplate(null);
		}

		if(_outputPath==null || _outputPath.trim().equalsIgnoreCase("")) {
			this.outputPath=FileUtil.formatPathWithEndSeparator("ivy_RIA_files");
		}
		else {
			this.setOutputPath(FileUtil.formatPathWithEndSeparator(_outputPath));
		}

		if(!doCheckBeforeDocGeneration(_outputFormat, _fields)) {
			// if the format or the list of MergeFields or the Template are not valid
			//we return the fileOperationMessage generated by doCheckBeforeDocGeneration(_format, list)
			return this.fileOperationMessage;
		}

		ArrayList<TemplateMergeField> templateParamslist =new ArrayList<TemplateMergeField>();
		templateParamslist.addAll(_fields);

		if(_outputName==null || _outputName.trim().equalsIgnoreCase("")) {
			//if the filename was not indicated, we look if the first mergeField Object could contain the fileName
			if(templateParamslist.get(0).getMergeFieldName().trim().equalsIgnoreCase("filename")) {
				// it contains the file name
				this.outputName=templateParamslist.get(0).getMergeFieldValue().trim();
			}
			else {
				// no file Name indicated, we have to take a default one
				this.outputName="serialLetter_"+System.nanoTime();
			}
		} else {
			this.outputName=_outputName;
		}

		if(!FileUtil.isFileNameValid(_outputName)) {
			// if the filename contains invalid characters
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.emptyFileList();
			this.fileOperationMessage.setMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/invalidFileName"));
			return this.fileOperationMessage;
		}

		this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.SUCCESS_MESSAGE);
		this.fileOperationMessage.setMessage(Ivy.cms().co("/ch/ivyteam/ivy/addons/docfactory/messages/serialLetterSuccess"));
		this.setFormat(_outputFormat);

		return this.fileOperationMessage; 
	}

	/**
	 * Performs mail merge on already provided template with normal mergeFields values, and mail merge with region support.
	 * The empty fields are going to be deleted in the Document.
	 * @param _normalMergeFields: the normal mail merge fields
	 * @param dataSourcesForMergeWithRegions: the mail Merge with region data sources.
	 * @return an com.aspose.words.Document generated by this operations
	 * @throws Exception
	 */
	private Document returnDocumentFromMailMerge(ArrayList<TemplateMergeField> _normalMergeFields, List<MailMergeDataSource> dataSourcesForMergeWithRegions) throws Exception{

		String [] paramName= new String[_normalMergeFields.size()];
		String [] paramValue= new String[_normalMergeFields.size()];
		for(int i=0; i<_normalMergeFields.size();i++){
			String s = _normalMergeFields.get(i).getMergeFieldName();
			if(s.startsWith("Image:") || s.startsWith("Image_")) {
				//The merge field name indicates an Image, we take only the second part of the name
				paramName[i]=s.substring(6);
			}
			else {
				//The merge field is a text field
				paramName[i]=s;
			}

			paramValue[i]=_normalMergeFields.get(i).getMergeFieldValue();
		}

		Document document = new Document(this.template.getPath());

		//Set up the event handler for image fields and perform mail merge.
		document.getMailMerge().setFieldMergingCallback(this.getAsposeFieldMergingCallback());
		document.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_EMPTY_PARAGRAPHS);
		document.getMailMerge().execute(paramName,paramValue);
		if(dataSourcesForMergeWithRegions!=null && !dataSourcesForMergeWithRegions.isEmpty()) {
			Iterator<MailMergeDataSource> iter = dataSourcesForMergeWithRegions.iterator();
			while(iter.hasNext()) {
				document.getMailMerge().executeWithRegions(iter.next());
			}
		}
		document.getMailMerge().setCleanupOptions(MailMergeCleanupOptions.REMOVE_UNUSED_REGIONS);
		//document.getMailMerge().setRemoveEmptyParagraphs(true);
		//document.getMailMerge().setRemoveEmptyRegions(true);
		document.getMailMerge().deleteFields();
		return document;
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
		String baseFilePath=this.outputPath+this.outputName;
		this.fileOperationMessage  = AsposeDocFactoryFileGenerator.exportDocumentToFile(document, baseFilePath, format, false);
		if(this.fileOperationMessage.isError()) {
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
		}
		if(!this.fileOperationMessage.getFiles().isEmpty()) {
			file = this.fileOperationMessage.getFiles().get(0);
		}
		return file;
	}


	@Override
	public FileOperationMessage generateDocumentWithRegions(String _templatePath, String _outputName, String _outputPath, String _outputFormat, List<TemplateMergeField> _mergeFields, HashMap<String, List<CompositeObject>> _tablesNamesAndFieldsmap){
		//Check the parameters
		this.resetAndCheckCommonParameters(_templatePath, _outputName, _outputPath, _outputFormat, _mergeFields);
		if(this.fileOperationMessage.getType() == ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE)
		{// Check failed
			return this.fileOperationMessage;
		}

		ArrayList<TemplateMergeField> templateParamslist =new ArrayList<TemplateMergeField>();
		templateParamslist.addAll(_mergeFields);
		ArrayList<MailMergeDataSource> mmds = new ArrayList<MailMergeDataSource>();
		try 
		{
			if(_tablesNamesAndFieldsmap!=null && !_tablesNamesAndFieldsmap.isEmpty())
			{
				Set<String> tables = _tablesNamesAndFieldsmap.keySet();
				Iterator<String> iter = tables.iterator();
				while(iter.hasNext()){
					String key = iter.next();
					try{
						mmds.add(DataClassToMergefields.transfomDataClassInMergeDataSource(_tablesNamesAndFieldsmap.get(key), key));
					}catch(Exception ex)
					{
						Ivy.log().error("The DataClass Objects provided to generateDocumentWithRegions are not all from the same Type. This is not allowed. The process will ignore the table "+key, ex);
					}
				}
			}
			this.doc = this.returnDocumentFromMailMerge(templateParamslist, mmds);
			fileOperationMessage.addFile(makeFileWithDocument(this.doc));

		} catch (Exception e) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(e.getMessage());
			this.fileOperationMessage.emptyFileList();
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
		}
		return fileOperationMessage;
	}

	@Override
	public FileOperationMessage generateDocumentWithRegions(String _templatePath, String _outputName, String _outputPath, String _outputFormat, List<TemplateMergeField> _mergeFields, Hashtable<String, Recordset> _hashtable){
		//Check the parameters
		this.resetAndCheckCommonParameters(_templatePath, _outputName, _outputPath, _outputFormat, _mergeFields);
		if(this.fileOperationMessage.getType() == ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE)
		{// Check failed
			return this.fileOperationMessage;
		}

		ArrayList<TemplateMergeField> templateParamslist =new ArrayList<TemplateMergeField>();
		templateParamslist.addAll(_mergeFields);
		ArrayList<MailMergeDataSource> mmds = new ArrayList<MailMergeDataSource>();
		try {
			if(_hashtable!=null && !_hashtable.isEmpty())
			{
				Set<String> tables = _hashtable.keySet();
				Iterator<String> iter = tables.iterator();
				while(iter.hasNext()){
					String key = iter.next();
					try{
						mmds.add(new MailMergeDataSource(_hashtable.get(key), key));
					}catch(Exception ex)
					{
						Ivy.log().error("The DataClass Objects provided to generateDocumentWithRegions are not all from the same Type. This is not allowed. The process will ignore the table "+key, ex);
					}
				}
			}
			this.doc = this.returnDocumentFromMailMerge(templateParamslist, mmds);
			fileOperationMessage.addFile(makeFileWithDocument(this.doc));
		} catch (Exception e) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(e.getMessage());
			this.fileOperationMessage.emptyFileList();
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
		}
		return fileOperationMessage;

	}

	@Override
	public FileOperationMessage generateDocumentWithRegions(String _templatePath, String _outputName, String _outputPath, String _outputFormat, List<TemplateMergeField> _mergeFields, List<String> _tablesNames, List<List<String>> _tables_fieldsNames, List<List<List<Object>>> tables_rowsValues){

		Hashtable<String, Recordset> _hashtable = new Hashtable<String, Recordset>();
		try{
			for(int i = 0 ; i<_tablesNames.size();i++)
			{//for each table
				//Create the recordset with fields Names as columns
				Recordset rs = new Recordset(_tables_fieldsNames.get(i));
				//add all the table List of rows
				rs.addAll(tables_rowsValues.get(i));
				_hashtable.put(_tablesNames.get(i), rs);
			}
			this.generateDocumentWithRegions(_templatePath, _outputName, _outputPath, _outputFormat, _mergeFields, _hashtable);
		}catch(Exception ex){
			this.fileOperationMessage.setMessage(ex.getMessage());
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
		}

		return this.fileOperationMessage;
	}

	@Override
	public ch.ivyteam.ivy.addons.docfactory.FileOperationMessage generateDocumentWithNestedRegions(
			String _templatePath, String _outputName, String _outputPath,
			String _outputFormat, List<TemplateMergeField> _mergeFields,
			ch.ivyteam.ivy.scripting.objects.List<CompositeObject> _parentDataSource,
			ch.ivyteam.ivy.scripting.objects.List<ch.ivyteam.ivy.scripting.objects.List<CompositeObject>> _childrenDataSources) 
	{
		this.resetAndCheckCommonParameters(_templatePath, _outputName, _outputPath, _outputFormat, _mergeFields);
		if(this.fileOperationMessage.getType() == ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE)
		{// Check failed
			return this.fileOperationMessage;
		}
		ArrayList<TemplateMergeField> templateParamslist =new ArrayList<TemplateMergeField>();
		templateParamslist.addAll(_mergeFields);
		ArrayList<MailMergeDataSource> mmds = new ArrayList<MailMergeDataSource>();
		try {
			if(_parentDataSource!=null && !_parentDataSource.isEmpty())
			{

				mmds.add(new MailMergeDataSource(_parentDataSource, _childrenDataSources));
			}
			this.doc = this.returnDocumentFromMailMerge(templateParamslist, mmds);
			fileOperationMessage.addFile(makeFileWithDocument(this.doc));
		} catch (Exception e) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(e.getMessage());
			this.fileOperationMessage.emptyFileList();
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
		}
		return this.fileOperationMessage;
	}

	@Override
	public ch.ivyteam.ivy.addons.docfactory.FileOperationMessage generateDocumentWithNestedRegions(
			String _templatePath, String _outputName, String _outputPath,
			String _outputFormat, List<TemplateMergeField> _mergeFields,
			ch.ivyteam.ivy.scripting.objects.List<CompositeObject> _nestedDataSource) 
	{
		this.resetAndCheckCommonParameters(_templatePath, _outputName, _outputPath, _outputFormat, _mergeFields);
		if(this.fileOperationMessage.getType() == ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE)
		{// Check failed
			return this.fileOperationMessage;
		}
		ArrayList<TemplateMergeField> templateParamslist =new ArrayList<TemplateMergeField>();
		templateParamslist.addAll(_mergeFields);
		ArrayList<MailMergeDataSource> mmds = new ArrayList<MailMergeDataSource>();
		try {
			if(_nestedDataSource!=null && !_nestedDataSource.isEmpty())
			{
				Ivy.log().debug("We do the merge in nested "+_nestedDataSource);
				mmds.add(new MailMergeDataSource(_nestedDataSource));
			}
			this.doc = this.returnDocumentFromMailMerge(templateParamslist, mmds);
			fileOperationMessage.addFile(makeFileWithDocument(this.doc));
		} catch (Exception e) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(e.getMessage());
			this.fileOperationMessage.emptyFileList();
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
		}
		return this.fileOperationMessage;
	}

	@Override
	public ch.ivyteam.ivy.addons.docfactory.FileOperationMessage generateDocumentWithNestedRegions(
			String _templatePath, String _outputName, String _outputPath,
			String _outputFormat, List<TemplateMergeField> _mergefields,
			Tree _treeDataSource) {
		this.resetAndCheckCommonParameters(_templatePath, _outputName, _outputPath, _outputFormat, _mergefields);
		if(this.fileOperationMessage.getType() == ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE)
		{// Check failed
			return this.fileOperationMessage;
		}
		ArrayList<TemplateMergeField> templateParamslist =new ArrayList<TemplateMergeField>();
		templateParamslist.addAll(_mergefields);
		ArrayList<MailMergeDataSource> mmds = new ArrayList<MailMergeDataSource>();
		try {
			if(_treeDataSource!=null)
			{
				Ivy.log().debug("We do the merge in nested "+_treeDataSource);
				mmds.add(new MailMergeDataSource(_treeDataSource));
			}
			this.doc = this.returnDocumentFromMailMerge(templateParamslist, mmds);
			fileOperationMessage.addFile(makeFileWithDocument(this.doc));
		} catch (Exception e) {
			this.fileOperationMessage.setType(ch.ivyteam.ivy.addons.docfactory.FileOperationMessage.ERROR_MESSAGE);
			this.fileOperationMessage.setMessage(e.getMessage());
			this.fileOperationMessage.emptyFileList();
			RDCallbackMethodHandler.callRDMethod(this.parentRD, errorMethodName, new Object[] { this.fileOperationMessage });
		}
		return this.fileOperationMessage;
	}

	/**
	 * Returns the AsposeFieldMergingCallback object that is responsible for applying special rules for mail merging for some merge fields.<br>
	 * This method never returns null. If this object is not set, a new AsposeFieldMergingCallback object is instantiated and returned.
	 * @return the AsposeFieldMergingCallback object
	 */
	public AsposeFieldMergingCallback getAsposeFieldMergingCallback() {
		if(this.fieldMergingCallback==null) {
			this.fieldMergingCallback = new AsposeFieldMergingCallback();
		}
		return this.fieldMergingCallback;
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
	 */
	public void setAsposeFieldMergingCallback(AsposeFieldMergingCallback fieldMergingCallback) throws IllegalArgumentException {
		if(fieldMergingCallback == null){
			throw new IllegalArgumentException("The AsposeFieldMergingCallback must not be null.");
		}
		this.fieldMergingCallback = fieldMergingCallback;
	}
}