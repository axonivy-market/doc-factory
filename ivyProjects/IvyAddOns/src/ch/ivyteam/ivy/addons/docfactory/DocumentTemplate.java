
package ch.ivyteam.ivy.addons.docfactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;

import ch.ivyteam.ivy.scripting.objects.CompositeObject;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.scripting.objects.Recordset;
import ch.ivyteam.ivy.scripting.objects.Tree;

/**
 * @author ec<br>
 * @since 29.10.2009
 * This class represents a Document Template.<br>
 * It contains all the informations necessary to produce a new Document through Mail merging.
 */
public class DocumentTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6318771680896773119L;

	/** the path where to find the template */
	private String templatePath=null;

	/**  the path where to save the new generated File */
	private String outputPath=null;

	/** the name of the new generated File*/
	private String outputName=null;

	/** the format of the new generated File<br>
	 * @see BaseDocFactory#getSupportedFormats()
	 */
	private String outputFormat = null;

	/** the list of TemplateMergeFields. Each TemplateMergeFields is a Key/value pair.<br>
	 * The key is the name of the mergeField that can be found in the template<br>
	 * The value is the String that will replace the mergeField in the template during the template merging.<br>
	 * @see TemplateMergeField
	 * */
	private List<TemplateMergeField> mergeFields= null;

	/**
	 * DataClass whose parameters are going to be taken to fill the merge fields of an office template.<br>
	 * The names of the dataClass parameters have to be the same as the names of the fields in the templates.
	 */
	private CompositeObject data=null;

	/** the document factory used to parse the template and to perform the mailMerging.<br>
	 * @see BaseDocFactory#getInstance()
	 */
	private BaseDocFactory documentFactory=null;

	/** The fileOperationMessage is a convenient Object to get the results of a Mail Merge from a Document Factory Object.<br>
	 * @see ch.ivyteam.ivy.addons.docfactory.FileOperationMessage
	 */
	private FileOperationMessage fileOperationMessage= null;

	/**
	 * Used in case of merge mail with regions for reporting.
	 */
	private HashMap<String, java.util.List<CompositeObject>> tablesNamesAndFieldsmap = null;
	/**
	 * Used in case of merge mail with regions for reporting.
	 */
	private Hashtable<String, Recordset> tablesNamesAndFieldsHashtable = null;
	
	/**
	 * Used in case of merge mail with nested tables
	 */
	private List<CompositeObject> parentDataSourceForNestedMailMerge = null;
	/**
	 * Used in case of merge mail with nested tables
	 */
	private List<List<CompositeObject>> childrenDataSourcesForNestedMailMerge = null;

	/**
	 * Used in case of merge mail with nested tables
	 */
	private List<CompositeObject> nestedDataSourceForNestedMailMerge = null;
	
	/**
	 * Used as possible Data for MergeMail with Nested Regions
	 */
	private Tree treeData = null;
	
	/**
	 * empty constructor
	 */
	public DocumentTemplate() {
		this("","","","",List.create(TemplateMergeField.class));
	}

	/**
	 * Constructor, instantiate the DocumentTemplate's variable
	 * @param _templatePath : the path where to find the template
	 * @param _outputPath : the path where to save the new generated File
	 * @param _outputName : the name of the new generated File
	 * @param _outputFormat : the format of the new generated File
	 * @param _mergeFields : the list of TemplateMergeFields. Each TemplateMergeFields is a Key/value pair.<br>
	 * The key is the name of the mergeField that can be found in the template<br>
	 * The value is the String that will replace the mergeField in the template during the template merging.
	 */
	public DocumentTemplate(String _templatePath, String _outputPath, String _outputName, String _outputFormat, List<TemplateMergeField> _mergeFields) {
		super();
		initializeConstructorVariables(_templatePath, _outputPath, _outputName,
				_outputFormat);
		if(_mergeFields != null) {
			this.mergeFields = _mergeFields;
		}

		initializeFileOperationMessage();
	}

	/**
	 * Constructor, instantiate the DocumentTemplate's variable
	 * @param _templatePath : the path where to find the template
	 * @param _outputPath : the path where to save the new generated File
	 * @param _outputName : the name of the new generated File
	 * @param _outputFormat : the format of the new generated File
	 * @param _mergeFields : the list of TemplateMergeFields. Each TemplateMergeFields is a Key/value pair.<br>
	 * The key is the name of the mergeField that can be found in the template<br>
	 * The value is the String that will replace the mergeField in the template during the template merging.
	 * @param _tablesNamesAndFieldsmap: an HashMap containing the tables name contained in the template as keys and the compositeObjects whose values should be inserted in the corresponding table.
	 * if not null or not empty and if the template contains Merge Regions, the Merge Regions are going to be filled as table with the values.
	 */
	public DocumentTemplate(String _templatePath, String _outputPath, String _outputName, String _outputFormat, List<TemplateMergeField> _mergeFields, HashMap<String , java.util.List<CompositeObject>> _tablesNamesAndFieldsmap) {
		super();
		initializeConstructorVariables(_templatePath, _outputPath, _outputName,
				_outputFormat);
		if(_mergeFields != null) {
			this.mergeFields = _mergeFields;
		}

		this.setTablesNamesAndFieldsmap(_tablesNamesAndFieldsmap);

		initializeFileOperationMessage();
	}

	/**
	 * Constructor, instantiate the DocumentTemplate's variable
	 * @param _templatePath : the path where to find the template
	 * @param _outputPath : the path where to save the new generated File
	 * @param _outputName : the name of the new generated File
	 * @param _outputFormat : the format of the new generated File
	 * @param _mergeFields : the list of TemplateMergeFields. Each TemplateMergeFields is a Key/value pair.<br>
	 * The key is the name of the mergeField that can be found in the template<br>
	 * The value is the String that will replace the mergeField in the template during the template merging.
	 * @param _tablesNamesAndFieldsHashtable: an Hashtable containing the tables name contained in the template as keys and the values should be inserted in the corresponding table as a ch.ivyteam.ivy.scripting.objects.Recordset.<br>
	 * if not null or not empty and if the template contains Merge Regions, the Merge Regions are going to be filled as table with the values.
	 */
	public DocumentTemplate(String _templatePath, String _outputPath, String _outputName, String _outputFormat, List<TemplateMergeField> _mergeFields, Hashtable<String , Recordset> _tablesNamesAndFieldsHashtable) {
		super();
		initializeConstructorVariables(_templatePath, _outputPath, _outputName,
				_outputFormat);
		if(_mergeFields != null) {
			this.mergeFields = _mergeFields;
		}

		this.setTablesNamesAndFieldsHashtable(_tablesNamesAndFieldsHashtable);

		initializeFileOperationMessage();
	}

	/**
	 * Constructor, instantiates the DocumentTemplate's variable.
	 * Supports Nested merge mail regions for reporting.
	 * @param _templatePath : the path where to find the template
	 * @param _outputPath : the path where to save the new generated File
	 * @param _outputName : the name of the new generated File
	 * @param _outputFormat : the format of the new generated File
	 * @param _data : An initialised DataClass that contains the informations that should be inserted in the document.<br>
	 * The merge fields of the template have to be the same as the names of the dataClass fields.
	 * The key is the name of the mergeField that can be found in the template<br>
	 * The value is the String that will replace the mergeField in the template during the template merging.
	 */
	public DocumentTemplate(String _templatePath, String _outputPath, String _outputName, String _outputFormat, CompositeObject _data) {
		super();
		initializeConstructorVariables(_templatePath, _outputPath, _outputName,
				_outputFormat);
		if(_data != null) {
			this.mergeFields = DataClassToMergefields.transformDataClassInMergeField(_data);
			this.tablesNamesAndFieldsHashtable = DataClassToMergefields.transformDataClassInTablesNamesAndFields(_data);
			this.nestedDataSourceForNestedMailMerge = List.create(CompositeObject.class);
			this.nestedDataSourceForNestedMailMerge.add(_data);
		}

		initializeFileOperationMessage();
	}

	/**
	 * Constructor, instantiates the DocumentTemplate's variable
	 * @param _templatePath : the path where to find the template
	 * @param _outputPath : the path where to save the new generated File
	 * @param _outputName : the name of the new generated File
	 * @param _outputFormat : the format of the new generated File
	 * @param _data : An initialised DataClass that contains the informations that should be inserted in the document.<br>
	 * The merge fields of the template have to be the same as the names of the dataClass fields.
	 * The key is the name of the mergeField that can be found in the template<br>
	 * The value is the String that will replace the mergeField in the template during the template merging.
	 * @param _tablesNamesAndFieldsmap: an HashMap containing the tables name contained in the template as keys and the compositeObjects whose values should be inserted in the corresponding table.
	 * if not null or not empty and if the template contains Merge Regions, the Merge Regions are going to be filled as table with the values.
	 */
	public DocumentTemplate(String _templatePath, String _outputPath, String _outputName, String _outputFormat, CompositeObject _data, HashMap<String , java.util.List<CompositeObject>> _tablesNamesAndFieldsmap) {
		super();
		initializeConstructorVariables(_templatePath, _outputPath, _outputName,
				_outputFormat);
		if(_data != null) {
			this.mergeFields = DataClassToMergefields.transformDataClassInMergeField(_data);
			this.tablesNamesAndFieldsHashtable = DataClassToMergefields.transformDataClassInTablesNamesAndFields(_data);
		}

		this.setTablesNamesAndFieldsmap(_tablesNamesAndFieldsmap);

		initializeFileOperationMessage();
	}

	/**
	 * Constructor, instantiates the DocumentTemplate's variable
	 * @param _templatePath : the path where to find the template
	 * @param _outputPath : the path where to save the new generated File
	 * @param _outputName : the name of the new generated File
	 * @param _outputFormat : the format of the new generated File
	 * @param _data : An initialised DataClass that contains the informations that should be inserted in the document.<br>
	 * The merge fields of the template have to be the same as the names of the dataClass fields.
	 * The key is the name of the mergeField that can be found in the template<br>
	 * The value is the String that will replace the mergeField in the template during the template merging.
	 * @param _tablesNamesAndFieldsHashtable: a java.util.Hashtable containing the tables name contained in the template as keys and the values should be inserted in the corresponding table as a ch.ivyteam.ivy.scripting.objects.Recordset.<br>
	 * if not null or not empty and if the template contains Merge Regions, the Merge Regions are going to be filled as table with the values.
	 */
	public DocumentTemplate(String _templatePath, String _outputPath, String _outputName, String _outputFormat, CompositeObject _data, Hashtable<String , Recordset> _tablesNamesAndFieldsHashtable) {
		super();
		initializeConstructorVariables(_templatePath, _outputPath, _outputName,
				_outputFormat);
		if(_data != null) {
			this.mergeFields = DataClassToMergefields.transformDataClassInMergeField(_data);
			this.tablesNamesAndFieldsHashtable = DataClassToMergefields.transformDataClassInTablesNamesAndFields(_data);
		}

		this.setTablesNamesAndFieldsHashtable(_tablesNamesAndFieldsHashtable);

		initializeFileOperationMessage();
	}
	
	/**
	 * Constructor, instantiates the DocumentTemplate's variable
	 * @param _templatePath : the path where to find the template
	 * @param _outputPath : the path where to save the new generated File
	 * @param _outputName : the name of the new generated File
	 * @param _outputFormat : the format of the new generated File
	 * @param _data : An initialised DataClass that contains the informations that should be inserted in the document.<br>
	 * The merge fields of the template have to be the same as the names of the dataClass fields.
	 * The key is the name of the mergeField that can be found in the template<br>
	 * The value is the String that will replace the mergeField in the template during the template merging.
	 * @param _parentDataSourceForNestedMailMerge: List<CompositeObject> parent data
	 * @param _childrenDataSourcesForNestedMailMerge: List<List<CompositeObject>> child data
	 * The two last parameters are used together for mail merge with Nested regions. 
	 * The parentDataSource List contains the DataClasses corresponding to the parent table and the
	 * childrenDataSource contains the list of DataClasses that are nested in the parent table.
	 * the parentDataSourceForNestedMailMerge list and childrenDataSourcesForNestedMailMerge list must have the same number of elements.
	 * The first parent dataclass object correspond to the first child list of dataclasses, the second one to the second one and so on... . 
	 * Here only one level of nested table is supported.
	 */
	public DocumentTemplate(String _templatePath, String _outputPath, String _outputName, String _outputFormat, CompositeObject _data, List<CompositeObject> _parentDataSourceForNestedMailMerge,List<List<CompositeObject>> _childrenDataSourcesForNestedMailMerge) {
		super();
		initializeConstructorVariables(_templatePath, _outputPath, _outputName,
				_outputFormat);
		if(_data != null) {
			this.mergeFields = DataClassToMergefields.transformDataClassInMergeField(_data);
			this.tablesNamesAndFieldsHashtable = DataClassToMergefields.transformDataClassInTablesNamesAndFields(_data);
		}

		if(_parentDataSourceForNestedMailMerge!=null && !_parentDataSourceForNestedMailMerge.isEmpty()) {
			this.setParentDataSourceForNestedMailMerge(_parentDataSourceForNestedMailMerge);
			if(_childrenDataSourcesForNestedMailMerge!=null && !_childrenDataSourcesForNestedMailMerge.isEmpty()) {
				this.setChildrenDataSourcesForNestedMailMerge(_childrenDataSourcesForNestedMailMerge);
			}
		}

		initializeFileOperationMessage();
	}
	
	/**
	 * Constructor, instantiates the DocumentTemplate's variable
	 * @param _templatePath : the path where to find the template
	 * @param _outputPath : the path where to save the new generated File
	 * @param _outputName : the name of the new generated File
	 * @param _outputFormat : the format of the new generated File
	 * @param _data : An initialised DataClass that contains the informations that should be inserted in the document.<br>
	 * The merge fields of the template have to be the same as the names of the dataClass fields.
	 * The key is the name of the mergeField that can be found in the template<br>
	 * The value is the String that will replace the mergeField in the template during the template merging.
	 * @param _nestedDataSourceForNestedMailMerge: List of CompositeObject - Used for mail merge with Nested regions. 
	 * In this case each dataclass may contain lists of other nested dataclasses and so on... .
	 * There is no limit in nesting regions.
	 */
	public DocumentTemplate(String _templatePath, String _outputPath, String _outputName, String _outputFormat, CompositeObject _data, List<CompositeObject> _nestedDataSourceForNestedMailMerge) {
		super();
		initializeConstructorVariables(_templatePath, _outputPath, _outputName,
				_outputFormat);
		if(_data != null) {
			this.mergeFields = DataClassToMergefields.transformDataClassInMergeField(_data);
			this.tablesNamesAndFieldsHashtable = DataClassToMergefields.transformDataClassInTablesNamesAndFields(_data);
		}

		if(_nestedDataSourceForNestedMailMerge!=null && !_nestedDataSourceForNestedMailMerge.isEmpty()) {
			this.setNestedDataSourceForNestedMailMerge(_nestedDataSourceForNestedMailMerge);
		}

		initializeFileOperationMessage();
	}
	
	/**
	 * Constructor, instantiates the DocumentTemplate's variable
	 * @param _templatePath : the path where to find the template
	 * @param _outputPath : the path where to save the new generated File
	 * @param _outputName : the name of the new generated File
	 * @param _outputFormat : the format of the new generated File
	 * @param _treeData : Tree used to fill the merge fields contain in the template with nested mail merge regions.<br>
	 * The root node may contain an initialised DataClass that contains the informations that should be inserted in the document outside of the nested regions.<br>
	 * The merge fields of the template have to be the same as the names of the dataClass fields.
	 * The key is the name of the mergeField that can be found in the template<br>
	 * The value is the String that will replace the mergeField in the template during the template merging.
	 */
	public DocumentTemplate(String _templatePath, String _outputPath, String _outputName, String _outputFormat, Tree _treeData) {
		super();
		initializeConstructorVariables(_templatePath, _outputPath, _outputName,
				_outputFormat);
		if(_treeData!=null) {
			if( _treeData.getValue()!=null) {
				CompositeObject obj;
				try {
					obj = (CompositeObject) _treeData.getValue(); 
					this.mergeFields = DataClassToMergefields.transformDataClassInMergeField(obj);
					this.tablesNamesAndFieldsHashtable = DataClassToMergefields.transformDataClassInTablesNamesAndFields(obj);
				}catch(Exception ex) {
					//ignore the Exception the _treeData does not have a real DataClass object, 
					//so we do not have any merge fields.
				}
			}
			this.treeData=_treeData;
		}
		
		initializeFileOperationMessage();
	}

	/**
	 * Try to generate the document with his objects variables.<br>
	 * Mail merge with regions and mail merge with nested regions are supported.
	 * @return the fileOperationMessage that results of the Document Factory mail Merge and File Creation
	 * 
	 */
	public FileOperationMessage generateDocument(){
		if(this.documentFactory==null) {
			//check if the document factory was already instantiated
			this.documentFactory=BaseDocFactory.getInstance();
		}
		if(this.tablesNamesAndFieldsmap!= null && !this.tablesNamesAndFieldsmap.isEmpty()) {
			this.fileOperationMessage = this.documentFactory.generateDocumentWithRegions(
					this.templatePath, 
					this.outputName, 
					this.outputPath, 
					this.outputFormat, 
					this.mergeFields, 
					this.tablesNamesAndFieldsmap);
		}else if(this.tablesNamesAndFieldsHashtable!=null && !this.tablesNamesAndFieldsHashtable.isEmpty()) {
			this.fileOperationMessage = this.documentFactory.generateDocumentWithRegions(
					this.templatePath, 
					this.outputName, 
					this.outputPath, 
					this.outputFormat, 
					this.mergeFields, 
					this.tablesNamesAndFieldsHashtable);
		}else if(this.parentDataSourceForNestedMailMerge!=null && !this.parentDataSourceForNestedMailMerge.isEmpty()) {
			this.fileOperationMessage = this.documentFactory.generateDocumentWithNestedRegions(
					this.templatePath, 
					this.outputName, 
					this.outputPath, 
					this.outputFormat, 
					this.mergeFields, 
					this.parentDataSourceForNestedMailMerge, 
					this.childrenDataSourcesForNestedMailMerge);
		}else if(this.nestedDataSourceForNestedMailMerge!=null && !this.nestedDataSourceForNestedMailMerge.isEmpty()) {
			this.fileOperationMessage = this.documentFactory.generateDocumentWithNestedRegions(
					this.templatePath, 
					this.outputName, 
					this.outputPath, 
					this.outputFormat, 
					this.mergeFields, 
					this.nestedDataSourceForNestedMailMerge);
		}else if(this.treeData!=null) {
			this.fileOperationMessage = this.documentFactory.generateDocumentWithNestedRegions(
					this.templatePath, 
					this.outputName, 
					this.outputPath, 
					this.outputFormat, 
					this.mergeFields, 
					this.treeData);
		}else {
			this.fileOperationMessage = this.documentFactory.generateDocument(
					this.templatePath, 
					this.outputName, 
					this.outputPath, 
					this.outputFormat, 
					this.mergeFields);
		}
		return this.fileOperationMessage;
	}

	/**
	 * @return the documentFactory
	 */
	public BaseDocFactory getDocumentFactory() {
		return documentFactory;
	}

	/**
	 * @param documentFactory the documentFactory to set
	 */
	public void setDocumentFactory(BaseDocFactory documentFactory) {
		this.documentFactory = documentFactory;
	}

	/**
	 * @return the mergeFields
	 */
	public List<TemplateMergeField> getMergeFields() {
		return mergeFields;
	}

	/**
	 * @param mergeFields the mergeFields to set
	 */
	public void setMergeFields(List<TemplateMergeField> mergeFields) {
		this.mergeFields = mergeFields;
	}

	/**
	 * Set the Dataclass that has to be taken to fill the template's merge fields.
	 * If the data is not null, the merge field List is going to be set with the list of the data parameters. 
	 * @param _data the data to set
	 */
	public void setData(CompositeObject _data) {
		this.data = _data;
		if(_data == null) {
			this.mergeFields = List.create(TemplateMergeField.class);
		}else {
			this.mergeFields =DataClassToMergefields.transformDataClassInMergeField(_data);
			this.tablesNamesAndFieldsHashtable = DataClassToMergefields.transformDataClassInTablesNamesAndFields(_data);
		}
	}

	/**
	 * @return the data
	 */
	public CompositeObject getData() {
		return data;
	}

	/**
	 * @return the outputFormat
	 */
	public String getOutputFormat() {
		return outputFormat;
	}

	/**
	 * @param outputFormat the outputFormat to set
	 */
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	/**
	 * @return the outputName
	 */
	public String getOutputName() {
		return outputName;
	}

	/**
	 * @param outputName the outputName to set
	 */
	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}

	/**
	 * @return the outputPath
	 */
	public String getOutputPath() {
		return outputPath;
	}

	/**
	 * @param outputPath the outputPath to set
	 */
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	/**
	 * @return the templatePath
	 */
	public String getTemplatePath() {
		return templatePath;
	}

	/**
	 * @param templatePath the templatePath to set
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	/**
	 * @return the fileOperationMessage
	 */
	public FileOperationMessage getFileOperationMessage() {
		return fileOperationMessage;
	}
	
	/**
	 * set the fileOperationMessage Object of the DocumentTemplate.<br>
	 * There should be no reason to use this method, since the fileOperationMessage is the result of the generateDocument() method.
	 * @param _fop the fileOperationMessage
	 */
	@Deprecated
	public void setFileOperationMessage(FileOperationMessage _fop)
	{
		if(_fop!=null) {
			this.fileOperationMessage = _fop;
		}
	}

	/**
	 * set the tableNames/Fields values HashMap Object. It clears also the tableNames/Fields values java.util.Hashtable object, <br>
	 * because we use only one of the two objects for the merge mail with regions.
	 * @param _tablesNamesAndFieldsmap the _tablesNamesAndFieldsmap to set
	 */
	public void setTablesNamesAndFieldsmap(HashMap<String, java.util.List<CompositeObject>> _tablesNamesAndFieldsmap) {
		this.tablesNamesAndFieldsmap = _tablesNamesAndFieldsmap;
		//we clear the hashTable if not null because we only use one of the two possible data Input 
		//for Mail merge with regions.
		if(this.tablesNamesAndFieldsHashtable != null) {
			this.tablesNamesAndFieldsHashtable.clear();
		}
	}

	/**
	 * @return the _tablesNamesAndFieldsmap
	 */
	public HashMap<String, java.util.List<CompositeObject>> getTablesNamesAndFieldsmap() {
		return tablesNamesAndFieldsmap;
	}

	/**
	 * set the tableNames/Fields values java.util.Hashtable Object. It clears also the tableNames/Fields values HashMap object, <br>
	 * because we use only one of the two objects for the merge mail with regions.
	 * @param tablesNamesAndFieldsHashtable the tablesNamesAndFieldsHashtable to set
	 */
	public void setTablesNamesAndFieldsHashtable(
			Hashtable<String, Recordset> tablesNamesAndFieldsHashtable) {
		this.tablesNamesAndFieldsHashtable = tablesNamesAndFieldsHashtable;
		//we clear the hashMap if not null because we only use one of the two possible data Input 
		//for Mail merge with regions.
		if(this.tablesNamesAndFieldsmap !=null) {
			this.tablesNamesAndFieldsmap.clear();
		}

	}

	/**
	 * @return the tablesNamesAndFieldsHashtable
	 */
	public Hashtable<String, Recordset> getTablesNamesAndFieldsHashtable() {
		return tablesNamesAndFieldsHashtable;
	}

	/**
	 * @return the parentDataSourceForNestedMailMerge
	 */
	public List<CompositeObject> getParentDataSourceForNestedMailMerge() {
		return parentDataSourceForNestedMailMerge;
	}

	/**
	 * @param parentDataSourceForNestedMailMerge the parentDataSourceForNestedMailMerge to set
	 */
	public void setParentDataSourceForNestedMailMerge(
			List<CompositeObject> parentDataSourceForNestedMailMerge) {
		this.parentDataSourceForNestedMailMerge = parentDataSourceForNestedMailMerge;
	}

	/**
	 * @return the childrenDataSourcesForNestedMailMerge
	 */
	public List<List<CompositeObject>> getChildrenDataSourcesForNestedMailMerge() {
		return childrenDataSourcesForNestedMailMerge;
	}

	/**
	 * @param childrenDataSourcesForNestedMailMerge the childrenDataSourcesForNestedMailMerge to set
	 */
	public void setChildrenDataSourcesForNestedMailMerge(
			List<List<CompositeObject>> childrenDataSourcesForNestedMailMerge) {
		this.childrenDataSourcesForNestedMailMerge = childrenDataSourcesForNestedMailMerge;
	}

	/**
	 * @return the nestedDataSourceForNestedMailMerge
	 */
	public List<CompositeObject> getNestedDataSourceForNestedMailMerge() {
		return nestedDataSourceForNestedMailMerge;
	}

	/**
	 * @param nestedDataSourceForNestedMailMerge the nestedDataSourceForNestedMailMerge to set
	 */
	public void setNestedDataSourceForNestedMailMerge(
			List<CompositeObject> nestedDataSourceForNestedMailMerge) {
		this.nestedDataSourceForNestedMailMerge = nestedDataSourceForNestedMailMerge;
	}

	/**
	 * @return the treeData
	 */
	public Tree getTreeData() {
		return treeData;
	}

	/**
	 * @param treeData the treeData to set
	 */
	public void setTreeData(Tree treeData) {
		this.treeData = treeData;
	}
	
	private void initializeFileOperationMessage() {
		this.fileOperationMessage= FileOperationMessage.generateInformationTypeFileOperationMessage("");
	}

	private void initializeConstructorVariables(String _templatePath,
			String _outputPath, String _outputName, String _outputFormat) {
		this.templatePath = (_templatePath==null)?"":_templatePath;
		this.outputPath = (_outputPath==null)?"":_outputPath;
		this.outputName = (_outputName==null)?"":_outputName;
		this.outputFormat = (_outputFormat==null)?"":_outputFormat;
		this.mergeFields = List.create(TemplateMergeField.class);
	}

}
