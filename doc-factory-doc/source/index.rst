DocFactory
************

A Document Factory is a system that automatically generates documents like mail
merge letters with the help of Microsoft Office Templates (:file:`.dot` or
:file:`.dotx` files).

You can extend the DocFactory to cover your project requirements. At the
moment, it is implemented with the commercial Java Library `Aspose
<https://www.aspose.com>`_ that we ship with the |ivy| Platform.

.. tip::

   The DocFactory currently bundles the modules aspose-words, aspose-cells,
   aspose-pdf, aspose-slides. There are additional Aspose modules such as
   aspose-barcode, aspose-ocr, aspose-diagram, that you can use in |ivy|. You
   have to add these modules from the `Aspose repository
   <https://repository.aspose.com/repo/com/aspose/>`_ to your |ivy| project and
   call the Java API. You can find the complete documentation on the Aspose
   website at `Aspose <https://www.aspose.com>`_. 

Microsoft Office templates contain fixed text and dynamic merge fields. A merge
field is a placeholder for text information. Each merge field has a name and
will be filled with the information you provide to the DocFactory.
For example, a merge field could be the address or the name of a correspondent
for a letter.

.. table:: Terms and Definitions used in the DocFactory chapter

+------------------+-----------------------------------------------------------------------------------------------------------+
| Document Factory | The Document Factory System implemented in the DocFactory Project                                         |
+------------------+-----------------------------------------------------------------------------------------------------------+
| Template         | A Microsoft Office template (document model)                                                              |
+------------------+-----------------------------------------------------------------------------------------------------------+
| Merge field      | Placeholder in the template for data supplied                                                             |
+------------------+-----------------------------------------------------------------------------------------------------------+
| Document         | If not defined, this is a letter or document generated based on a template after the mail merge operation |
+------------------+-----------------------------------------------------------------------------------------------------------+
| Mail merging     | Document generation with the replacement of the merge fields with the corresponding information           |
+------------------+-----------------------------------------------------------------------------------------------------------+
| Format           | The format in which the serial document has to be saved (doc, txt, html…)                                 |
+------------------+-----------------------------------------------------------------------------------------------------------+

Read more about the DocFactory API and the merging of fields in Microsoft
Office templates in the sections below.

.. toctree::
   :maxdepth: 1
   :caption: Doc Factory

   doc-factory/mail-merging
   doc-factory/callable-processes
   doc-factory/demos
   doc-factory/doc-factory-object
   doc-factory/complex-mergefields
