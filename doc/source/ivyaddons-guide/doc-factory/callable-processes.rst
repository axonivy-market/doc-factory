.. _df-callable-processes:

Callable Processes for generating Serial Letters and documents
==============================================================

Two Callable Processes are available from the AddOns project and at your
disposal, to cover a maximum of your needs in the letter production. They use
three kinds of Java Objects to perform their task.


Java Classes
------------

The Java classes of those objects are located in the package
:file:`ch.ivyteam.ivy.addons.docfactory`.

TemplateMergeField
^^^^^^^^^^^^^^^^^^

The **TemplateMergeField** class is the representation of a merge field in a
template. It has two fields: a name and a value, both a String. It is used to
provide information to the Mail merge Engine to fill the corresponding merge
fields in the template.

.. table:: TemplateMergeField attributes

   +-----------------+--------+----------------------------------------------------------------------------------------------+
   | Field           | Type   | Description                                                                                  |
   +=================+========+==============================================================================================+
   | mergeFieldName  | String | Name of the merge field. If the **mergeFieldName** is in the form of **“Image:ImageName”**   |
   |                 |        | or **“Image_ImageName”** where **“Image:”** or **“Image_”** is constant, then this indicates |
   |                 |        | that an image should be placed into the respective merge field.                              |
   +-----------------+--------+----------------------------------------------------------------------------------------------+
   | mergeFieldValue | String | The value of the merge field. If image merge fields are used (see above) then this indicates |
   |                 |        | the path to the image, for example: :file:`file:///Icons/warning48.png` or                   |
   |                 |        | :file:`C:/Icons/warning48.png`                                                               |
   +-----------------+--------+----------------------------------------------------------------------------------------------+

There is one constructor: :file:`public TemplateMergeField(String mergeFieldName, String mergeFieldValue)`.


FileOperationMessage
^^^^^^^^^^^^^^^^^^^^

The **FileOperationMessage** class is a convenience class to return the result of
mail merging and File generation. It contains three variables:

.. table:: FileOperationMessage attributes

   +---------+--------------------+----------------------------------------------------------------------------------------+
   | Field   | Type               | Description                                                                            |
   +=========+====================+========================================================================================+
   | message | String             | The text message to return (like “File generation successful” or “An error occurred”…) |
   +---------+--------------------+----------------------------------------------------------------------------------------+
   | type    | String             | Number indicating which kind of message it is (SUCCESS, ERROR or INFORMATION). The     |
   |         |                    | possible return values are available as static final constants on this class.          |
   +---------+--------------------+----------------------------------------------------------------------------------------+
   | files   | List<java.io.File> | All the Files produced during the mail merge operation. They can be used for further   |
   |         |                    | actions like printing, editing, downloading….                                          |
   +---------+--------------------+----------------------------------------------------------------------------------------+

There are two constructors: The default constructor :file:`public
FileOperationMessage()` and :file:`public FileOperationMessage(String message,
List<File> files, int type)`

DocumentTemplate
^^^^^^^^^^^^^^^^

The **DocumentTemplate** class represents a whole letter or document to be
generated. It contains all the necessary information to be able to generate a
new document with a mail merge engine. The information is placed into the
following variables, each of them can be accessed through get/set methods:

.. table:: DocumentTemplate attributes

   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | Field                                 | Type                             | Description                                                                                 |
   +=======================================+==================================+=============================================================================================+
   | templatePath                          | String                           | File path of the template to use for the mail merging. This path is server side.            |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | outputPath                            | String                           | The path of the folder where to store the produced document after mail merging.             |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | outputName                            | String                           | The name of the File or produced document.                                                  |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | outputFormat                          | String                           | The desired Format of the document (doc, docx…).                                            |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | mergeFields                           | List<TemplateMergeField>         | The list of the Merge Fields: each merge field should be present into the template.         |
   |                                       |                                  | The values of the merge fields are going to be inserted into the corresponding template’s   |
   |                                       |                                  | merge fields.                                                                               |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | data                                  | CompositeObject                  | The DocumentTemplate object accepts a DataClass as alternative to a List of                 |
   |                                       | (like an Ivy DataClass)          | TemplateMergeFields. Each attribute of the dataclass will be linked with a template         |
   |                                       |                                  | merge field with the same name. The String value from the attributll be taken to fill       |
   |                                       |                                  | the corresponding mergefield.                                                               |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | documentFactory                       | BaseDocFactory                   | This is the Engine responsible of the mail merging. See last section of this chapter.       |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | fileOperationMessage                  | FileOperationMessage             | The FileOperationMessage returned by the mail merging operation.                            |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | tablesNamesAnd-                       | java.util.HashMap<String,        | This parameter is used for mail merge with regions. This object consists of key/values      |
   | Fieldsmap*                            | java.util.List<CompositeObject>> | pairs, where the keys (String) are the table starts names and the lists of CompositeObjects | 
   |                                       |                                  | contain the datas. Each CompositeObject (Ivy DataClass) represents a row in a table. The    |
   |                                       |                                  | value of the dataclass attributes whose names match mergefields' names of the table will    |
   |                                       |                                  | used to fill these mergefields.                                                             |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | tablesNamesAnd-                       | java.util.Hashtable<String,      | This parameter is used for mail merge with regions. This object consists of key/values      |
   | FieldsHashtable*                      | Recordset>                       | pairs, where the keys (String) are the table starts names and the recordsets contain the    |
   |                                       |                                  | datas for the corresponding tables. Each Record represents a row in a table. A Record's     |
   |                                       |                                  | value will be inserted in the mergefield that corresponds to its field's name.              |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | parentDataSource-                     | List<CompositeObject>            | Used for mail merge with Nested regions. With these attributes set, only one nested         |
   | ForNestedMailMerge,                   | List<List<CompositeObject>>      | level is supported: the parent list contains the information for the parent table/region,   |
   | childrenDataSources-                  |                                  | each child List<CompositeObject> contains the information for one parent data object and    |
   | ForNestedMailMerge                    |                                  | is nested in this parent.                                                                   |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | nestedDataSourceFor-                  | List<CompositeObject>            | Used for mail merge with Nested regions. In this case each dataclass may contain lists      |
   | NestedMailMerge                       |                                  | of other nested dataclasses and so on... . There is no limit in nesting regions.            |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | treeData                              | Tree                             | Used for mail merge with Nested regions. There is no limit in nesting regions.              |
   |                                       |                                  | In this case the data structure corresponding to the nested regions in the template is      |
   |                                       |                                  | stored in a Tree. The first root node of the Tree contains a dataclass as value object.     |
   |                                       |                                  | The attributes of this dataclass are used to fill merge fields outside of the nested        |
   |                                       |                                  | regions area. All the children nodes at each level of the Tree contain dataclasses as       |
   |                                       |                                  | value that are used to fill the corresponding nested region.                                |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+


\* The two attributes tablesNamesAndFieldsmap and tablesNamesAndFieldsHashtable
cannot be used together to perform mail merge with regions. If one is set, then
the other one is cleared. So you have to decide if you use Recordsets or List of
CompositeObjects to fill your merge regions.