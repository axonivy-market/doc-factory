.. _df-callable-processes:

Callable Processes
==================

Two Callable Processes are available from the DocFactory project and at your
service, to cover a maximum of your needs in the letter production. They use
three kinds of Java Objects to perform their task.


Java Classes
------------

The package :file:`ch.ivyteam.ivy.addons.docfactory` contains the Java classes
of those objects.

TemplateMergeField
^^^^^^^^^^^^^^^^^^

The **TemplateMergeField** class is the representation of a merge field in a
template. It has two attributes: a name and a value. Both are of type String.
The class provides information to the mail merge engine to fill the
corresponding merge fields in the template.

.. table:: TemplateMergeField attributes

   +-----------------+--------+----------------------------------------------------------------------------------------------+
   |      Field      |  Type  |                                         Description                                          |
   +=================+========+==============================================================================================+
   | mergeFieldName  | String | Name of the merge field. If the **mergeFieldName** is in the form of **“Image:ImageName”**   |
   |                 |        | or **“Image_ImageName”** where **“Image:”** or **“Image_”** is constant, then this indicates |
   |                 |        | that an image will be placed into the respective merge field.                                |
   +-----------------+--------+----------------------------------------------------------------------------------------------+
   | mergeFieldValue | String | The value of the merge field. If image merge fields are used, (see above), then this         |
   |                 |        | indicates the path to the image, for example: :file:`file:///Icons/warning48.png` or         |
   |                 |        | :file:`C:/Icons/warning48.png`                                                               |
   +-----------------+--------+----------------------------------------------------------------------------------------------+

There is one constructor: :file:`public TemplateMergeField(String mergeFieldName, String mergeFieldValue)`.


FileOperationMessage
^^^^^^^^^^^^^^^^^^^^

The **FileOperationMessage** class conveniently returns the results of mail
merging and file generation. It contains three variables:

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

There are two constructors: The default constructor 
:file:`public FileOperationMessage()` and :file:`public FileOperationMessage(String message,
List<File> files, int type)`

.. _df-callable-processes-document-template:

DocumentTemplate
^^^^^^^^^^^^^^^^

The **DocumentTemplate** class represents a whole letter or document to be
generated. It contains all the necessary information to be able to generate a
new document with a mail merge engine. The information is inserted into the
following variables, each of which can be accessed through get/set methods:

.. tip::
   For more information about the methods, please look at the **JavaDoc**.

.. table:: DocumentTemplate attributes

   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   |                 Field                 |               Type               |                                         Description                                         |
   +=======================================+==================================+=============================================================================================+
   | templatePath                          | String                           | File path of the template to use for the mail merging. This path is on the Engine.          |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | outputPath                            | String                           | The path of the folder where to store the produced document after mail merging.             |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | outputName                            | String                           | The name of the file or document produced.                                                  |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | outputFormat                          | String                           | The desired Format of the output document (doc, docx, etc.).                                |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | mergeFields                           | List<TemplateMergeField>         | The list of the merge fields: each merge field should be present in the template.           |
   |                                       |                                  | The values of the merge fields will be inserted into the corresponding template merge       |
   |                                       |                                  | fields.                                                                                     |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | data                                  | CompositeObject                  | The DocumentTemplate object accepts a DataClass as an alternative to a List of              |
   |                                       | (like a DataClass)               | TemplateMergeFields. Each attribute of the dataclass is linked to a template                |
   |                                       |                                  | merge field with the same name. The String value of the attribute is inserted into the      |
   |                                       |                                  | corresponding merge field.                                                                  |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | documentFactory                       | BaseDocFactory                   | This is the engine responsible for the mail merge. For details, refer to the last           |
   |                                       |                                  | section on this page.                                                                       |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | fileOperationMessage                  | FileOperationMessage             | The FileOperationMessage returned by the mail merging operation.                            |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | tablesNamesAndFieldsmap*              | java.util.HashMap<String,        | This parameter is used for mail merge with regions. This object consists of key/value       |
   |                                       | java.util.List<CompositeObject>> | pairs, where the keys (String) are the table start names and the lists of CompositeObjects  |
   |                                       |                                  | contain the data. Each CompositeObject (i.e., DataClass) represents a row in a table. The   |
   |                                       |                                  | value of the dataclass attributes whose names match merge field names of the table will     |
   |                                       |                                  | used to fill these merge fields.                                                            |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | tablesNamesAndFieldsHashtable*        | java.util.Hashtable<String,      | This parameter is used for mail merge with regions. This object consists of key/value       |
   |                                       | Recordset>                       | pairs, where the keys (String) are the table start names, and the recordsets contain the    |
   |                                       |                                  | data for the corresponding tables. Each record represents a row in a table. A record        |
   |                                       |                                  | value will be inserted in the merge field that corresponds to its field's name.             |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | parentDataSourceForNestedMailMerge,   | List<CompositeObject>            | Used for mail merge with nested regions. With these attributes set, only one nested         |
   | childrenDataSourcesForNestedMailMerge | List<List<CompositeObject>>      | level is supported: The parent list contains the information for the parent table/region,   |
   |                                       |                                  | each child List<CompositeObject> contains the information for one parent data object and    |
   |                                       |                                  | is nested in this parent.                                                                   |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | nestedDataSourceFor                   | List<CompositeObject>            | Used for mail merge with nested regions. Each dataclass may contain lists                   |
   |                                       |                                  | of other nested dataclasses and so on. There is no limit for nesting regions.               |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+
   | treeData                              | Tree                             | Used for mail merge with nested regions. There is no limit for nesting regions.             |
   |                                       |                                  | The data structure corresponding to the nested regions in the template is                   |
   |                                       |                                  | stored in a tree. The first root node of the tree contains a dataclass as its object value. |
   |                                       |                                  | The attributes of this dataclass are inserted into merge fields outside of the nested       |
   |                                       |                                  | regions area. All the children nodes at each level of the tree contain dataclasses as       |
   |                                       |                                  | values that are inserted into the corresponding nested region.                              |
   +---------------------------------------+----------------------------------+---------------------------------------------------------------------------------------------+


\* The two attributes tablesNamesAndFieldsmap and tablesNamesAndFieldsHashtable
cannot be used together to perform mail merge with regions. If one is set, then
the other one is cleared. So you have to decide if you use Recordsets or List of
CompositeObjects to fill your merge regions.

There are several constructors; they are all documented in JavaDoc.


Callable Processes
------------------

Two callable processes are available to ease the use of the document factory. They
are located in the DocFactory *Functional Processes* folder:

.. figure:: /_static/images/callable-processes.png


writeSerialLetterToOneCorrespondent
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The Callable named **writeSerialLetterToOneCorrespondent** contains several
callable subprocess starts. 

The first one (writeSerialLetterToOneCorrespondant) allows writing a single
document with a template by providing a single list of **TemplateMergeField**
objects. Here is the description of this callable that you can find in the ivy
process file:

.. figure:: /_static/images/callable-processes-1.png

The second one (writeDocumentWithOneDataClass) allows writing a single document
with a template by providing a **CompositeObject (DataClass)**. Here is the
description of this callable that you can find in the Axon Ivy process file:

.. figure:: /_static/images/callable-processes-3.png

Other ones (writeDocumentWithMailMergeTable) allow writing a single document
with a template that can contain merge mail with regions (tables). Here is the
description of this callable that you can find in the Axon Ivy process file:

.. figure:: /_static/images/callable-processes-4.png

The last three allow writing a single document with a template that contains
mail merge data with nested regions (tables). 

   * writeDocumentWithMailMergeNestedTable(String,String,String,String, 
     List<CompositeObject>,List<List<CompositeObject>>)
   * writeDocumentWithMailMergeNestedTableWithTree(String,String,String,
     String,Tree)
   * writeDocumentWithMailMergeNestedTableWithListOfDatas(String,String,
     String,String,CompositeObject, List<CompositeObject>)

.. figure:: /_static/images/mail-merge-nested-5.png


writeSerialLetterToManyCorrespondents 
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ 

The callable named **writeSerialLetterToManyCorrespondents** allows writing more
than one document with a list of **DocumentTemplate** objects. Each
**DocumentTemplate** object will produce a document. Mail merge with
regions and mail merge with nested regions are now supported, because the
DocumentTemplate Object encapsulates the necessary parameters to perform such
merges (:ref:`DocumentTemplate <df-callable-processes-document-template>`). Here
is the description of this callable that you can find in the Axon Ivy process file:

.. figure:: /_static/images/callable-processes-2.png