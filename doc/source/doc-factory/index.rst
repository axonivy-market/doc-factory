DocFactory
************

A Document Factory is a system that allows generating automatically documents
like serial letters with the help of documents models like Microsoft Office
Templates (:file:`.dot` or :file:`.dotx` files).

The document Factory system in the Ivy Add-ons project is open and can be
extended. At the moment it is only implemented with the commercial Java API
`Aspose <www.aspose.com>`_. You can add your own implementation with another
API. :ref:`Read more <df-doc-factory-object>` in the doc factory section below.

This solution works with Microsoft Office Templates. Each template contains
fixed text and dynamic merges fields. A Merge field is a placeholder for text
information. Each merge field has a name and will be filled with the information
you provide to the document Factory system. For example a merge field could be
the address or the name of a correspondent for a letter.

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

.. toctree::
   :maxdepth: 1

   mail-merging
   complex-mergefields
   callable-processes
   demos
   doc-factory-object
