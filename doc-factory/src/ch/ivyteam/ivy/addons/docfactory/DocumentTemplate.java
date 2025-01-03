
package ch.ivyteam.ivy.addons.docfactory;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.mergefield.internal.MergeFieldsExtractor;
import ch.ivyteam.ivy.addons.docfactory.options.DocumentCreationOptions;
import ch.ivyteam.ivy.addons.docfactory.options.MergeCleanupOptions;
import ch.ivyteam.ivy.addons.docfactory.options.SimpleMergeCleanupOptions;
import ch.ivyteam.ivy.addons.docfactory.response.ResponseHandler;
import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.scripting.objects.CompositeObject;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.scripting.objects.Recordset;
import ch.ivyteam.ivy.scripting.objects.Tree;
import ch.ivyteam.ivy.scripting.objects.util.MetaType;

/**
 * @author ec<br>
 * @since 29.10.2009 This class represents a Document Template.<br>
 *        It contains all the informations necessary to produce a new Document
 *        through Mail merging.
 */
public class DocumentTemplate implements Serializable {

  private static final long serialVersionUID = 6318771680896773119L;

  /** the path where to find the template */
  private String templatePath = null;

  /** the path where to save the new generated File */
  private String outputPath = null;

  /** the name of the new generated File */
  private String outputName = null;

  /**
   * the format of the new generated File<br>
   * @see BaseDocFactory#getSupportedFormats()
   */
  private String outputFormat = null;

  /**
   * the list of TemplateMergeFields. Each TemplateMergeFields is a Key/value
   * pair.<br>
   * The key is the name of the mergeField that can be found in the template<br>
   * The value is the String that will replace the mergeField in the template
   * during the template merging.<br>
   * @see TemplateMergeField
   */
  private java.util.List<TemplateMergeField> mergeFields = new ArrayList<>();

  /**
   * DataClass whose parameters are going to be taken to fill the merge fields
   * of an office template.<br>
   * The names of the dataClass parameters have to be the same as the names of
   * the fields in the templates.
   */
  private Serializable data = null;

  /**
   * the document factory used to parse the template and to perform the
   * mailMerging.<br>
   * @see BaseDocFactory#getInstance()
   */
  private BaseDocFactory documentFactory = null;

  /**
   * The fileOperationMessage is a convenient Object to get the results of a
   * Mail Merge from a Document Factory Object.<br>
   * @see ch.ivyteam.ivy.addons.docfactory.FileOperationMessage
   */
  private FileOperationMessage fileOperationMessage = null;

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
   * Locale used for Date and Number formatting
   */
  private Locale locale = DocFactoryConstants.DEFAULT_LOCALE;

  private Collection<Object> dataTables = new ArrayList<>();

  private DocumentCreationOptions documentCreationOptions = DocumentCreationOptions.getInstance();

  /**
   * Generates a DocumentTemplate with the given Template file
   * @param template a File as Template. Cannot be null.
   * @return the DocumentTemplate
   */
  public static DocumentTemplate withTemplate(java.io.File template) {
    if (template == null) {
      throw new IllegalArgumentException("The template parameter cannot be null");
    }
    DocumentTemplate documentTemplate = new DocumentTemplate(template.getPath(), "", "", "",
            List.create(TemplateMergeField.class));
    documentTemplate.setDocumentFactory(BaseDocFactory.getInstance()
            .withDocumentCreationOptions(documentTemplate.documentCreationOptions));
    return documentTemplate;
  }

  /**
   * empty constructor
   */
  public DocumentTemplate() {
    this("", "", "", "", List.create(TemplateMergeField.class));
  }

  /**
   * Constructor, instantiate the DocumentTemplate's variable
   * @param _templatePath : the path where to find the template
   * @param _outputPath : the path where to save the new generated File
   * @param _outputName : the name of the new generated File
   * @param _outputFormat : the format of the new generated File
   * @param _mergeFields : the list of TemplateMergeFields. Each
   *          TemplateMergeFields is a Key/value pair.<br>
   *          The key is the name of the mergeField that can be found in the
   *          template<br>
   *          The value is the String that will replace the mergeField in the
   *          template during the template merging.
   */
  public DocumentTemplate(String _templatePath, String _outputPath, String _outputName,
          String _outputFormat, List<TemplateMergeField> _mergeFields) {
    super();
    initializeConstructorVariables(_templatePath, _outputPath, _outputName,
            _outputFormat);
    if (_mergeFields != null) {
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
   * @param _mergeFields : the list of TemplateMergeFields. Each
   *          TemplateMergeFields is a Key/value pair.<br>
   *          The key is the name of the mergeField that can be found in the
   *          template<br>
   *          The value is the String that will replace the mergeField in the
   *          template during the template merging.
   * @param _tablesNamesAndFieldsmap an HashMap containing the tables name
   *          contained in the template as keys and the compositeObjects whose
   *          values should be inserted in the corresponding table. if not null
   *          or not empty and if the template contains Merge Regions, the Merge
   *          Regions are going to be filled as table with the values.
   */
  public DocumentTemplate(String _templatePath, String _outputPath, String _outputName,
          String _outputFormat, List<TemplateMergeField> _mergeFields,
          HashMap<String, java.util.List<CompositeObject>> _tablesNamesAndFieldsmap) {
    super();
    initializeConstructorVariables(_templatePath, _outputPath, _outputName,
            _outputFormat);
    if (_mergeFields != null) {
      setMergeFields(_mergeFields);
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
   * @param _mergeFields : the list of TemplateMergeFields. Each
   *          TemplateMergeFields is a Key/value pair.<br>
   *          The key is the name of the mergeField that can be found in the
   *          template<br>
   *          The value is the String that will replace the mergeField in the
   *          template during the template merging.
   * @param _tablesNamesAndFieldsHashtable an Hashtable containing the tables
   *          name contained in the template as keys and the values should be
   *          inserted in the corresponding table as a
   *          ch.ivyteam.ivy.scripting.objects.Recordset.<br>
   *          if not null or not empty and if the template contains Merge
   *          Regions, the Merge Regions are going to be filled as table with
   *          the values.
   */
  public DocumentTemplate(String _templatePath, String _outputPath, String _outputName,
          String _outputFormat, List<TemplateMergeField> _mergeFields,
          Hashtable<String, Recordset> _tablesNamesAndFieldsHashtable) {
    super();
    initializeConstructorVariables(_templatePath, _outputPath, _outputName,
            _outputFormat);
    if (_mergeFields != null) {
      setMergeFields(_mergeFields);
    }

    this.setTablesNamesAndFieldsHashtable(_tablesNamesAndFieldsHashtable);

    initializeFileOperationMessage();
  }

  /**
   * Constructor, instantiates the DocumentTemplate's variable. Supports Nested
   * merge mail regions for reporting.
   * @param _templatePath : the path where to find the template
   * @param _outputPath : the path where to save the new generated File
   * @param _outputName : the name of the new generated File
   * @param _outputFormat : the format of the new generated File
   * @param _data : An initialized DataClass that contains the informations that
   *          should be inserted in the document.<br>
   *          The merge fields of the template have to be the same as the names
   *          of the dataClass fields. The key is the name of the mergeField
   *          that can be found in the template<br>
   *          The value is the String that will replace the mergeField in the
   *          template during the template merging.
   */
  public DocumentTemplate(String _templatePath, String _outputPath, String _outputName, String _outputFormat,
          CompositeObject _data) {
    super();
    initializeConstructorVariables(_templatePath, _outputPath, _outputName,
            _outputFormat);
    if (_data != null) {
      this.mergeFields = DataClassToMergefields.transformDataClassInMergeField(_data);
      this.tablesNamesAndFieldsHashtable = DataClassToMergefields
              .transformDataClassInTablesNamesAndFields(_data);
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
   * @param _data : An initialized DataClass that contains the informations that
   *          should be inserted in the document.<br>
   *          The merge fields of the template have to be the same as the names
   *          of the dataClass fields. The key is the name of the mergeField
   *          that can be found in the template<br>
   *          The value is the String that will replace the mergeField in the
   *          template during the template merging.
   * @param _tablesNamesAndFieldsmap an HashMap containing the tables name
   *          contained in the template as keys and the compositeObjects whose
   *          values should be inserted in the corresponding table. if not null
   *          or not empty and if the template contains Merge Regions, the Merge
   *          Regions are going to be filled as table with the values.
   */
  public DocumentTemplate(String _templatePath, String _outputPath, String _outputName, String _outputFormat,
          CompositeObject _data, HashMap<String, java.util.List<CompositeObject>> _tablesNamesAndFieldsmap) {
    super();
    initializeConstructorVariables(_templatePath, _outputPath, _outputName,
            _outputFormat);
    if (_data != null) {
      this.mergeFields = DataClassToMergefields.transformDataClassInMergeField(_data);
      this.tablesNamesAndFieldsHashtable = DataClassToMergefields
              .transformDataClassInTablesNamesAndFields(_data);
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
   * @param _data : An initialized DataClass that contains the informations that
   *          should be inserted in the document.<br>
   *          The merge fields of the template have to be the same as the names
   *          of the dataClass fields. The key is the name of the mergeField
   *          that can be found in the template<br>
   *          The value is the String that will replace the mergeField in the
   *          template during the template merging.
   * @param _tablesNamesAndFieldsHashtable a java.util.Hashtable containing the
   *          tables name contained in the template as keys and the values
   *          should be inserted in the corresponding table as a
   *          ch.ivyteam.ivy.scripting.objects.Recordset.<br>
   *          if not null or not empty and if the template contains Merge
   *          Regions, the Merge Regions are going to be filled as table with
   *          the values.
   */
  public DocumentTemplate(String _templatePath, String _outputPath, String _outputName, String _outputFormat,
          CompositeObject _data, Hashtable<String, Recordset> _tablesNamesAndFieldsHashtable) {
    super();
    initializeConstructorVariables(_templatePath, _outputPath, _outputName,
            _outputFormat);
    if (_data != null) {
      this.mergeFields = DataClassToMergefields.transformDataClassInMergeField(_data);
      this.tablesNamesAndFieldsHashtable = DataClassToMergefields
              .transformDataClassInTablesNamesAndFields(_data);
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
   * @param _data : An initialized DataClass that contains the informations that
   *          should be inserted in the document.<br>
   *          The merge fields of the template have to be the same as the names
   *          of the dataClass fields. The key is the name of the mergeField
   *          that can be found in the template<br>
   *          The value is the String that will replace the mergeField in the
   *          template during the template merging.
   * @param _parentDataSourceForNestedMailMerge List<CompositeObject> parent
   *          data
   * @param _childrenDataSourcesForNestedMailMerge List<List<CompositeObject>>
   *          child data The two last parameters are used together for mail
   *          merge with Nested regions. The parentDataSource List contains the
   *          DataClasses corresponding to the parent table and the
   *          childrenDataSource contains the list of DataClasses that are
   *          nested in the parent table. the parentDataSourceForNestedMailMerge
   *          list and childrenDataSourcesForNestedMailMerge list must have the
   *          same number of elements. The first parent dataclass object
   *          correspond to the first child list of dataclasses, the second one
   *          to the second one and so on... . Here only one level of nested
   *          table is supported.
   */
  public DocumentTemplate(String _templatePath, String _outputPath, String _outputName, String _outputFormat,
          CompositeObject _data, List<CompositeObject> _parentDataSourceForNestedMailMerge,
          List<List<CompositeObject>> _childrenDataSourcesForNestedMailMerge) {
    super();
    initializeConstructorVariables(_templatePath, _outputPath, _outputName,
            _outputFormat);
    if (_data != null) {
      setMergeFields(DataClassToMergefields.transformDataClassInMergeField(_data));
      this.tablesNamesAndFieldsHashtable = DataClassToMergefields
              .transformDataClassInTablesNamesAndFields(_data);
    }

    if (_parentDataSourceForNestedMailMerge != null && !_parentDataSourceForNestedMailMerge.isEmpty()) {
      this.setParentDataSourceForNestedMailMerge(_parentDataSourceForNestedMailMerge);
      if (_childrenDataSourcesForNestedMailMerge != null
              && !_childrenDataSourcesForNestedMailMerge.isEmpty()) {
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
   * @param _data : An initialized DataClass that contains the informations that
   *          should be inserted in the document.<br>
   *          The merge fields of the template have to be the same as the names
   *          of the dataClass fields. The key is the name of the mergeField
   *          that can be found in the template<br>
   *          The value is the String that will replace the mergeField in the
   *          template during the template merging.
   * @param _nestedDataSourceForNestedMailMerge List of CompositeObject - Used
   *          for mail merge with Nested regions. In this case each dataclass
   *          may contain lists of other nested dataclasses and so on... . There
   *          is no limit in nesting regions.
   */
  public DocumentTemplate(String _templatePath, String _outputPath, String _outputName, String _outputFormat,
          CompositeObject _data, List<CompositeObject> _nestedDataSourceForNestedMailMerge) {
    super();
    initializeConstructorVariables(_templatePath, _outputPath, _outputName,
            _outputFormat);
    if (_data != null) {
      this.mergeFields = DataClassToMergefields.transformDataClassInMergeField(_data);
      this.tablesNamesAndFieldsHashtable = DataClassToMergefields
              .transformDataClassInTablesNamesAndFields(_data);
    }

    if (_nestedDataSourceForNestedMailMerge != null && !_nestedDataSourceForNestedMailMerge.isEmpty()) {
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
   * @param _treeData : Tree used to fill the merge fields contain in the
   *          template with nested mail merge regions.<br>
   *          The root node may contain an initialized DataClass that contains
   *          the informations that should be inserted in the document outside
   *          of the nested regions.<br>
   *          The merge fields of the template have to be the same as the names
   *          of the dataClass fields. The key is the name of the mergeField
   *          that can be found in the template<br>
   *          The value is the String that will replace the mergeField in the
   *          template during the template merging.
   */
  public DocumentTemplate(String _templatePath, String _outputPath, String _outputName, String _outputFormat,
          Tree _treeData) {
    super();
    initializeConstructorVariables(_templatePath, _outputPath, _outputName,
            _outputFormat);
    if (_treeData != null) {
      if (_treeData.getValue() != null) {
        CompositeObject obj;
        try {
          obj = (CompositeObject) _treeData.getValue();
          this.mergeFields = DataClassToMergefields.transformDataClassInMergeField(obj);
          this.tablesNamesAndFieldsHashtable = DataClassToMergefields
                  .transformDataClassInTablesNamesAndFields(obj);
        } catch (Exception ex) {
          // ignore the Exception the _treeData does not have a real DataClass
          // object,
          // so we do not have any merge fields.
        }
      }
      this.treeData = _treeData;
    }

    initializeFileOperationMessage();
  }

  private void initializeConstructorVariables(String _templatePath,
          String _outputPath, String _outputName, String _outputFormat) {
    this.documentFactory = BaseDocFactory.getInstance();
    this.documentFactory.withDocumentCreationOptions(this.documentCreationOptions);
    this.templatePath = (_templatePath == null) ? "" : _templatePath;
    this.outputPath = (_outputPath == null) ? "" : _outputPath;
    this.outputName = (_outputName == null) ? "" : _outputName;
    this.outputFormat = (_outputFormat == null) ? "" : _outputFormat;
    this.mergeFields = List.create(TemplateMergeField.class);
  }

  /**
   * Set the responseHandler that will be used as callBack Response in the
   * document Factory see
   * {@link BaseDocFactory#withResponseHandler(ResponseHandler)}
   * @param responseHandler ResponseHandler . May be null.
   * @return the document template object with the ResponseHandler set in its
   *         document factory
   */
  public DocumentTemplate withResponseHandler(ResponseHandler responseHandler) {
    if (this.documentFactory == null) {
      this.documentFactory = BaseDocFactory.getInstance();
    }
    this.documentFactory.withResponseHandler(responseHandler);
    return this;
  }

  /**
   * Some DocFactory may allow injecting a DocumentWorker class which can apply
   * some custom logic on the document after it has been produced by the factory
   * and before it has been returned to the user. <b>Note:</b> at the time of
   * the writing of this method, the default DocFactory is based on Aspose. A
   * DocumentWorker Interface has been made for the AsposeDocFactory. See:
   * {@link ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker} <br>
   * For the moment, only implementations of
   * {@link ch.ivyteam.ivy.addons.docfactory.aspose.DocumentWorker} are
   * supported. Other kind of object will be ignored.
   * @param documentWorker
   */
  public <T> DocumentTemplate withDocumentWorker(T documentWorker) {
    this.documentFactory.withDocumentWorker(documentWorker);
    return this;
  }

  /**
   * Allows specifying a Locale for formatting the Numbers and Dates objects
   * @param newLocale
   * @return The DocumenTemplate which Locale as been set.
   */
  public DocumentTemplate useLocale(Locale newLocale) {
    if (newLocale != null) {
      this.locale = newLocale;
    }
    for (TemplateMergeField tmf : this.mergeFields) {
      tmf.useLocaleAndResetNumberFormatAndDateFormat(newLocale);
    }
    return this;
  }

  /**
   * Allows using a Serializable bean which attributes accessible with public
   * getters will be used as MergeFields. The Nested Serializable in this
   * Serializable are supported.<br>
   * Example: a Person Serializable which holds a name, an Address with a
   * zipCode. The following MergeFields will be retrieved:<br>
   * person.name and person.address.zipCode <br>
   * You can call this method several times with several Data. All the
   * MergeFields will be added. <br>
   * <b>Be aware that no reporting with tables is supported</b> (merge mail with
   * regions or nested regions). If the given bean has some nested Collections,
   * these collections are not parsed for merging the tables. Use
   * {@link DocumentTemplate#putDataAsSourceForMailMerge(Serializable)} instead
   * if you want that the MailMergeWithRegions support
   * @param d the data, cannot be null.
   * @return the DocumentTemplate which MergFields List is completed with the
   *         MergeFields retrieved from the given Data
   *
   */
  public DocumentTemplate putDataAsSourceForSimpleMailMerge(Serializable d) {
    if (d == null) {
      throw new IllegalArgumentException("The data parameter cannot be null");
    }
    this.data = d;
    this.mergeFields.addAll(MergeFieldsExtractor.getMergeFieldsForSimpleMerge(d));
    if (!locale.equals(DocFactoryConstants.DEFAULT_LOCALE)) {
      for (TemplateMergeField tmf : this.mergeFields) {
        tmf.useLocaleAndResetNumberFormatAndDateFormat(locale);
      }
    }
    return this;
  }

  /**
   * Allows using a Serializable bean which attributes accessible with public
   * getters will be used as MergeFields. The Nested Serializable in this
   * Serializable are supported.<br>
   * Collections of Serializables in this Serializable are going to be used as
   * sources for mail merge regions.<br>
   * <b>Important</b> Due to the way the DocFactory introspects the data for
   * retrieving and building the mail merge regions sources, having some
   * collection of objects with cyclic relationships will end to an OutOfMemory
   * Exception.<br>
   * Example: Person with a Collection of Addresses and each Address holds a
   * reference to the Person object. This is not supported.<br>
   * <br>
   * You can call this method several times with several Data. All the
   * MergeFields will be added.
   * @param d the data, cannot be null.
   * @return the DocumentTemplate which MergFields List is completed with the
   *         MergeFields retrieved from the given Data
   */
  public DocumentTemplate putDataAsSourceForMailMerge(Serializable d) {
    if (d == null) {
      throw new IllegalArgumentException("The data parameter cannot be null");
    }
    this.data = d;
    this.mergeFields.addAll(MergeFieldsExtractor.getMergeFields(d));
    if (!locale.equals(DocFactoryConstants.DEFAULT_LOCALE)) {
      for (TemplateMergeField tmf : this.mergeFields) {
        tmf.useLocaleAndResetNumberFormatAndDateFormat(locale);
      }
    }
    return this;
  }

  /**
   * Put an object that will be used for merging a table in the template. The
   * DataTable Object must hold enough information for being placed in the
   * template.<br>
   * Example by using Aspose: the DataTable name must be the same as the
   * Region-MergeField. You can use this method several time for injecting
   * several DataTables in different regions of your template.
   * @param dataTable an Object that the underlying Document Factory will be
   *          able to use as data source for merging a table in a region on in a
   *          merge field.<br>
   *          If the underlying Document Factory cannot use this object, it
   *          should ignore it.
   * @return the Document Template.
   */
  public DocumentTemplate putTableDataForMergingATableInDocument(Object dataTable) {
    if (dataTable == null) {
      throw new IllegalArgumentException("The data parameter cannot be null");
    }
    this.dataTables.add(dataTable);
    return this;
  }

  public DocumentTemplate withDocFactory(BaseDocFactory docFactory) {
    if (docFactory == null) {
      throw new IllegalArgumentException("The docFactory parameter cannot be null");
    }
    this.documentFactory = docFactory;
    return this;
  }

  public boolean hasDataTable() {
    return !this.dataTables.isEmpty();
  }

  public Collection<?> getDataTable() {
    return this.dataTables;
  }

  /**
   * Add a specific mergeField.<br>
   * Note that the MergeField will be added as simple value and won't be
   * introspected for merge mail with regions.
   *
   * @param mergeFieldName the name of the merge field as it should appears in
   *          the template, cannot be blank.
   * @param value The value: cannot be blank
   * @return the documentTemplate which contains a new "simple mail merge"
   *         TemplateMergeField.
   */
  public DocumentTemplate addMergeField(String mergeFieldName, Object value) {
    if (StringUtils.isBlank(mergeFieldName) || value == null) {
      throw new IllegalArgumentException("The mergeFieldName cannot be blank and the value cannot be null");
    }
    this.mergeFields.add(TemplateMergeField.withName(mergeFieldName).asSimpleValue(value)
            .useLocaleAndResetNumberFormatAndDateFormat(this.locale));
    return this;
  }

  /**
   * Produces the document
   * @param destinationDocument
   */
  public FileOperationMessage produceDocument(File destinationDocument) {
    if (destinationDocument == null) {
      throw new IllegalArgumentException("The destinationDocument parameter cannot be null");
    }
    this.outputPath = destinationDocument.getParent();
    if (this.outputPath == null) {
      this.outputPath = IApplication.current().getSessionFileArea().getPath();
    }
    this.outputFormat = FilenameUtils.getExtension(destinationDocument.getName());
    try {
      this.outputPath = FilenameUtils.getFullPathNoEndSeparator(destinationDocument.getPath());
      this.outputName = FilenameUtils.getBaseName(destinationDocument.getName());
    } catch (Exception e) {
      this.fileOperationMessage = FileOperationMessage.generateErrorTypeFileOperationMessage(e.getMessage());
      return this.fileOperationMessage;
    }
    return this.documentFactory.generateDocument(this);

  }

  /**
   * Try to generate the document with his objects variables.<br>
   * Mail merge with regions and mail merge with nested regions are supported.
   * @return the fileOperationMessage that results of the Document Factory mail
   *         Merge and File Creation
   *
   */
  public FileOperationMessage generateDocument() {
    if (this.documentFactory == null) {
      // check if the document factory was already instantiated
      this.documentFactory = BaseDocFactory.getInstance();
    }
    return this.documentFactory.generateDocument(this);
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
    return List.forJavaList(MetaType.valueOf(TemplateMergeField.class), this.mergeFields);
  }

  /**
   * @param mergeFields the mergeFields to set
   */
  public void setMergeFields(List<TemplateMergeField> mergeFields) {
    this.mergeFields.clear();
    this.mergeFields.addAll(mergeFields);
  }

  /**
   * Set the Dataclass that has to be taken to fill the template's merge fields.
   * If the data is not null, the merge field List is going to be set with the
   * list of the data parameters.
   * @param _data the data to set
   */
  public void setData(CompositeObject _data) {
    this.data = _data;
    if (_data == null) {
      this.mergeFields = List.create(TemplateMergeField.class);
    } else {
      this.mergeFields = DataClassToMergefields.transformDataClassInMergeField(_data);
      this.tablesNamesAndFieldsHashtable = DataClassToMergefields
              .transformDataClassInTablesNamesAndFields(_data);
    }
  }

  /**
   * @return the data
   */
  public CompositeObject getData() {
    if (this.data instanceof CompositeObject) {
      return (CompositeObject) data;
    }

    return null;
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
   * set the tableNames/Fields values HashMap Object. It clears also the
   * tableNames/Fields values java.util.Hashtable object, <br>
   * because we use only one of the two objects for the merge mail with regions.
   * @param _tablesNamesAndFieldsmap the _tablesNamesAndFieldsmap to set
   */
  public void setTablesNamesAndFieldsmap(
          HashMap<String, java.util.List<CompositeObject>> _tablesNamesAndFieldsmap) {
    this.tablesNamesAndFieldsmap = _tablesNamesAndFieldsmap;
    // we clear the hashTable if not null because we only use one of the two
    // possible data Input
    // for Mail merge with regions.
    if (this.tablesNamesAndFieldsHashtable != null) {
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
   * set the tableNames/Fields values java.util.Hashtable Object. It clears also
   * the tableNames/Fields values HashMap object, <br>
   * because we use only one of the two objects for the merge mail with regions.
   * @param tablesNamesAndFieldsHashtable the tablesNamesAndFieldsHashtable to
   *          set
   */
  public void setTablesNamesAndFieldsHashtable(
          Hashtable<String, Recordset> tablesNamesAndFieldsHashtable) {
    this.tablesNamesAndFieldsHashtable = tablesNamesAndFieldsHashtable;
    // we clear the hashMap if not null because we only use one of the two
    // possible data Input
    // for Mail merge with regions.
    if (this.tablesNamesAndFieldsmap != null) {
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
   * @param parentDataSourceForNestedMailMerge the
   *          parentDataSourceForNestedMailMerge to set
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
   * @param childrenDataSourcesForNestedMailMerge the
   *          childrenDataSourcesForNestedMailMerge to set
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
   * @param nestedDataSourceForNestedMailMerge the
   *          nestedDataSourceForNestedMailMerge to set
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

  public DocumentTemplate withSimpleMergeCleanupOptions(
          SimpleMergeCleanupOptions simpleMergeCleanupOptions) {
    API.checkNotNull(simpleMergeCleanupOptions, "simpleMergeCleanupOptions");
    this.getDocumentFactory().withSimpleMergeCleanupOption(simpleMergeCleanupOptions);
    return this;
  }

  public DocumentTemplate withRegionsMergeCleanupOptions(
          MergeCleanupOptions mergeCleanupOptions) {
    API.checkNotNull(mergeCleanupOptions, "mergeCleanupOptions");
    this.getDocumentFactory().withRegionsMergeCleanupOption(mergeCleanupOptions);
    return this;
  }

  private void initializeFileOperationMessage() {
    this.fileOperationMessage = FileOperationMessage.generateInformationTypeFileOperationMessage("");
  }

  public DocumentCreationOptions getDocumentCreationOptions() {
    return documentCreationOptions;
  }

  public DocumentTemplate withDocumentCreationOptions(DocumentCreationOptions options) {
    API.checkNotNull(options, "options");
    this.documentCreationOptions = options;
    this.documentFactory.withDocumentCreationOptions(this.documentCreationOptions);
    return this;
  }

}
