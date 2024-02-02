.. _df-doc-factory-object:

Document Factory Object
=======================

We implemented the Axon Ivy DocFactory Object based on the commercial
Aspose Java API. To be able to allow developing other Document Factories that
work the same way as this one, a DocFactory implementation should always
extend the abstract class
:code:`ch.ivyteam.ivy.addons.docfactory.BaseDocFactory` and has to provide a
default constructor (no arguments).

This class declares all the public methods a document factory should implement.
For example, it declares the :code:`generateDocument(..)` method that takes a
**DocumentTemplate** as parameter and returns a **FileOperationMessage** as
its result.

The **AsposeDocFactory** provides the default implementation of this
**BaseDocFactory**. It is easy to get an instance of an AsposeDocFactory: The
public static method :code:`BaseDocFactory.getInstance()` returns such an
object.

Suppose you want to use your own implementation class of the **BaseDocFactory**.
In that case, you have to set a special Java system property on the Axon Ivy Engine
named **document.factory**. Its value is the fully qualified name of your
DocFactory class, e.g., :code:`com.acme.docfactory.MyDocFactory`. The
:code:`getInstance()` method of the **BaseDocFactory** will then return an
instance of your document factory class.

.. tip::
   For more information about the methods of the BaseDocFactory, please refer to its **JavaDoc**.

.. tip::
   If you use Aspose libraries delivered with the Axon Ivy Platform outside of the
   AsposeDocFactory, then you have to load the licenses for each product used.
   See :file:DocumentCreator.java in the DocFactory examples.

.. tip::
   If you use Aspose libraries **not** packaged with the Axon Ivy Platform, then you
   have to add the packages and load the appropriate license for each product used. 
   See :file:IvyAsposeBarcoder.java in aspose-barcode-demo.