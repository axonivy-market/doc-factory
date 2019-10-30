.. _df-doc-factory-object:

Document Factory Object
=======================

The Document Factory Object in this Ivy Addons implementation is made with the
help of the commercial Aspose Java API. To be able to allow developing other
Document Factories that work the same way as this one, a DocumentFactory
implementation should always extend the abstract class
:code:`ch.ivyteam.ivy.addons.docfactory.BaseDocFactory` and must provide a
default constructor (no arguments).

This Class declares all the public methods a document factory should implement.
For example it declares the :code:`generateDocument(..)` method that takes a
**DocumentTemplate** as parameter and returns a **FileOperationMessage** as
result.

The **AsposeDocFactory** is the provided default implementation of this
**BaseDocFactory**. It is easy to get an instance of an AsposeDocFactory: The
public static method :code:`BaseDocFactory.getInstance()` returns such an
object.

If you want to use your own implementation class of the **BaseDocFactory**, you have
to set a special Java System property on the server that gives the exact
qualified name of your document factory class. The name of this property has to
be **document.factory** and its value has to be
:code:`com.acme.docfactory.MyDocFactory`, for example, to name your own
implementation. The :code:`getInstance()` method of the **BaseDocFactory** will then return
an instance of your document factory class.

.. tip::
   For more information about the following methods please have a look at the **JavaDoc**.

.. table:: Overview of the abstract BaseDocFactory methods

   +-----------------------------+---------------------------------------------------------------------+
   | Method                      | Description                                                         |
   +=============================+=====================================================================+
   | generateBlankDocument       |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   +-----------------------------+---------------------------------------------------------------------+
   | generateDocument            | Creates a blank document (for testing purposes).                    |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   +-----------------------------+---------------------------------------------------------------------+
   | generateDocument            | Creates one document.                                               |
   +-----------------------------+---------------------------------------------------------------------+
   | generateTxt                 | Creates a text string that is the result of the mail merge.         |
   |                             |                                                                     |
   +-----------------------------+---------------------------------------------------------------------+
   | generateHTML                | Creates an HTML String that is the result of the mail merge.        |
   |                             |                                                                     |
   +-----------------------------+---------------------------------------------------------------------+
   | generateFilesForHTML        | Generates a list of Files that can be used for HTML representation. |
   |                             |                                                                     |
   +-----------------------------+---------------------------------------------------------------------+
   | generateDocuments           | Generates one or more documents.                                    |
   +-----------------------------+---------------------------------------------------------------------+
   | generateDocuments           | Generates one or more documents each one can be saved in a          |
   |                             | different destination folder.                                       |
   |                             |                                                                     |
   |                             |                                                                     |
   +-----------------------------+---------------------------------------------------------------------+
   | generateDocumentsWith-      | Generates a list of Files that can be used for HTML representation. |
   | DifferentDestination        |                                                                     |
   |                             |                                                                     |
   +-----------------------------+---------------------------------------------------------------------+
   | generateDocumentsWith-      | Generates one or more documents. One FileOperationMessage for each  |
   | DifferentDestination        | document.                                                           |
   +-----------------------------+---------------------------------------------------------------------+   
   | generateMultiple-           | Generates multiple documents appended in one File.                  |
   | DocumentsInOne              |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   +-----------------------------+---------------------------------------------------------------------+
   | generateMultiple-           | Generates multiple documents appended in one File.                  |
   | DocumentsInOne              |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   +-----------------------------+---------------------------------------------------------------------+
   | generateDocument-           | Method to generate one document with merge mail. Mail Merge with    |
   | WithRegions                 | regions supported.                                                  |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   +-----------------------------+---------------------------------------------------------------------+
   | generateDocument-           | Method to generate one document with merge mail. Mail Merge with    |
   | WithRegions                 | regions supported.                                                  |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   +-----------------------------+---------------------------------------------------------------------+
   | generateDocument-           | Method to generate one document with merge mail. Mail Merge with    |
   | WithRegions                 | regions supported.                                                  |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   |                             |                                                                     |
   +-----------------------------+---------------------------------------------------------------------+
   | getTemplateFields           | Returns the list of the Merge Fields names in a template.           |
   +-----------------------------+---------------------------------------------------------------------+
   | getFactoryClass             | Get the Class of the current BaseDocFactory implementation class    |
   +-----------------------------+---------------------------------------------------------------------+
   | isFormatSupported           | This method should check if the format denoted by the String param  |
   |                             | is supported by your document Factory.                              |
   +-----------------------------+---------------------------------------------------------------------+