.. _df-mail-merging:

Mail Merging
============

Using templates allows for the automated generation of huge quantities of mail
merge letters to different correspondents. It is widely used as a consistent way
to unify letter correspondence in companies or administrations. The mechanism of
mail merging can be resumed with the following picture:

.. figure:: /_static/images/mail-merge.png

At the heart of the mechanism lies a Mail Merge Engine. This engine receives
some data from a data source (Database, spreadsheet, XML …) and replaces merge
fields in the template with the corresponding data. It saves either each letter
as a separate document or in a single document containing all the letters. The
data should be in the form of a data set (e.g., a list of correspondent addresses).

In our Document Factory, the mail merge engine is provided by the commercial
Aspose Words Java API. You can extend and create your own implementation of the
document factory with other engines.


.. _df-mail-merging-regions:

Mail Merging with Regions
-------------------------

.. warning:: 

   The mail merge with regions described here is specific to the Aspose engine.
   If you extend this current DocFactory by using another engine you have to
   refer to its specific documentation.

If you want to dynamically grow parts inside the document, use mail merge with
regions. To specify a mail merge region in the document, you need to insert two
mail merge fields to mark the beginning and end of the mail merge region. All
document content in a mail merge region will be repeated automatically for every
record in the data source (in most cases, this is a table).

To mark the beginning of a mail merge region, insert a MERGEFIELD with the name
TableStart:MyTable, where MyTable corresponds to the name of the table. To mark
the end of the mail merge region, insert another MERGEFIELD with the name
TableEnd:MyTable. Between these marking fields, place merge fields that
correspond to the fields of your data source (table columns). 

These merge fields will be populated with data from the first row of the data
source, then the whole region will be repeated, and the new fields will be
populated with data from the second row, and so on. 

Follow these simple rules when marking a region: 

   * TableStart and TableEnd fields must be inside the same section in the
     document.
   * If used inside a table, TableStart and TableEnd must be inside the
     same row in the table.
   * Mail merge regions can be nested inside each other.
   * Mail merge regions should be well formed (there is always a matching pair
     of TableStart and TableEnd with the same table name).

The following picture shows a template with a mail merge region:

.. figure:: /_static/images/mail-merge-region-template.png

The following picture shows the result of the mail merge with regions:

.. figure:: /_static/images/mail-merge-region-result.png

We have provided some callables subprocesses to ease the use of mail merge and
mail merge with regions. Please refer to the :ref:`next section <df-doc-factory-object>`.

.. _df-mail-merging-nested_regions:

Mail Merging with Nested Regions
--------------------------------

.. warning:: 

   The mail merge with nested regions described here is specific to the Aspose
   engine. If you extend this current DocFactory by using another engine, you
   have to refer to its specific documentation.

.. tip::

   We retrieved most of the information provided in this section from the Aspose
   official documentation. You can find the complete documentation on the Aspose
   web site at `Aspose <www.aspose.com>`_. 

Most data in relational databases or XML files is hierarchical (e.g. with
parent-child relationships). One common example is a report about employees of a
company. The different employees are employed in different departments and have
several human capital elements. In this case, we have several items nested in
each other: Human Capital elements nested in Employees nested in Departments
nested in a Company.

Aspose.Words allows nesting mail merge regions inside each other in a document
to reflect the way the data is nested. This allows you to easily populate a
document with hierarchical data. Nested mail merge regions are at least two
regions in which one is defined entirely inside the other, so they are “nested”
in one another. In a document it looks like this:

.. figure:: /_static/images/mail-merge-nested-1.png

There are a few things you need to consider when preparing nested mail merge
regions and merge regions in general:

   * The mail merge region opening and closing tag (e.g., TableStart:Order,
     TableEnd:Order) both need to appear in the same row or cell. For example, if
     you start a merge region in a cell of a table, you must end the merge region
     in the same row as the first cell.
   * The names of the columns in the DataTable must match the merge field name.
     Unless you have specified mapped fields the merge will not be successful
     for those fields whose names are different. In the Axon Ivy Implementation of
     this feature we use the Dataclasses Class names as TableStart names and the
     Dataclasses attributes names as mergefields names (more information
     :ref:`here <mail-merge-nested-mapping>`).
   * Mail merge regions should be well formed (there is always a pair of
     matching TableStart and TableEnd merge fields with the same table name).

The following picture shows a template with a mail merge region:

.. figure:: /_static/images/mail-merge-nested-2.png

The following picture shows the result of the mail merge with regions:

.. figure:: /_static/images/mail-merge-nested-3.png

.. _mail-merge-nested-mapping:

The previous nested mail merge has been made by using List of Axon Ivy DataClasses
built as follows:

.. figure:: /_static/images/mail-merge-nested-4.png

**It is very important to create your templates with this in mind:**

   * Each TableStart merge field has to be named like the corresponding
     dataclass: e.g. the TableStart:Employee will correspond to the Employee
     data object. This rule is case sensitive.
   * Within each "Data Object - Table", the merge fields will be filled with the
     corresponding data attribute value. The merge field must have the same name
     as the data attribute. Refer to the level attribute and mergefield in the
     HumanCapital table as an example.

We have provided some callable subprocesses to ease the use of mail merge and
mail merge with nested regions. Please refer to the :ref:`next section
<df-doc-factory-object>`.