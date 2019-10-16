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
   | mergeFieldName  | String | Name of the merge field. If the **mergeFieldName** is in the form of *“Image:ImageName”* or  |
   |                 |        | “Image_ImageName” where “Image:” or “Image_” is constant, then this indicates that an image  |
   |                 |        | should be placed into the respective merge field.                                            |
   +-----------------+--------+----------------------------------------------------------------------------------------------+
   | mergeFieldValue | String | The value of the merge field. If image merge fields are used (see above) then this indicates |
   |                 |        | the path to the image, for example: :file:`file:///Icons/warning48.png` or                   |
   |                 |        | :file:`C:/Icons/warning48.png`                                                               |
   +-----------------+--------+----------------------------------------------------------------------------------------------+


FileOperationMessage
^^^^^^^^^^^^^^^^^^^^


DocumentTemplate
^^^^^^^^^^^^^^^^