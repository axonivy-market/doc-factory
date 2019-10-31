DocFactory
************

A Document Factory is a system that allows generating automatically documents
like serial letters with the help of Microsoft Office Templates (:file:`.dot` or :file:`.dotx` files).

The Document Factory can be extended to cover your project requirements. At the moment it is implemented 
with the commercial Java Library `Aspose <https://www.aspose.com>`_ that is included in Axon.ivy platform.

.. tip::

   The Document Factory currently bundles the modules: aspose-words, aspose-cells, aspose-pdf, aspose-slides.
   However there are further aspose modules such as aspose-barcode, aspose-ocr, aspose-diagram and more, that you can use in Axon.ivy.
   For that you have to add modules from the `Aspose repository <https://repository.aspose.com/repo/com/aspose/>`_ to your ivy project 
   and call the java API. You can find the complete documentation on the aspose web site at `Aspose <https://www.aspose.com>`_. 

Microsoft Office templates contains fixed text and dynamic merges fields. 
A Merge field is a placeholder for text information. Each merge field has a name and will be filled with the information
you provide to the document Factory system. For example a merge field could be the address or the name of a correspondent for a letter.

.. table:: Terms and Definitions used in the DocFactory chapter

   +------------------+---------------------------------------------------------------------------------------------------------------------+
   | Document Factory | The document Factory System implemented in the Ivy Add-ons Project                                                  |
   +------------------+---------------------------------------------------------------------------------------------------------------------+
   | Template         | A Microsoft Office template (document model)                                                                        |
   +------------------+---------------------------------------------------------------------------------------------------------------------+
   | Merge field      | Placeholder for text information in the template                                                                    |
   +------------------+---------------------------------------------------------------------------------------------------------------------+
   | Document         | If not defined, this is a letter or document generated with the help of a template after the mail merging operation |
   +------------------+---------------------------------------------------------------------------------------------------------------------+
   | Mail merging     | Document generation with the replacement of the merge fields with the corresponding information                     |
   +------------------+---------------------------------------------------------------------------------------------------------------------+
   | Format           | The format in which the serial document has to be saved (doc, txt, html…)                                           |
   +------------------+---------------------------------------------------------------------------------------------------------------------+

Read more about the Document Factory API and the merging of fields in microsoftt office templates in the sections below.

.. toctree::
   :maxdepth: 1
   :caption: Doc Factory

   doc-factory/mail-merging
   doc-factory/callable-processes
   doc-factory/demos
   doc-factory/doc-factory-object
