.. _df-mail-merging:

Mail Merging
============

Using templates allows for automatical generation of huge quantities of serial
letters to different correspondents. It is widely used as a consistent way to
unify the letter correspondence in companies or administrations. The mechanism
of mail merging can be resumed with the following picture:

.. figure:: /_static/images/doc-factory/mail-merge.png

At the heart of the mechanism lies a Merge Mail Engine. This Engine receives
some Data from a Data Source (DB, XML …) and replaces merge fields in the
template with the corresponding information. It saves each letter as a separate
document or in a document containing all the letters. The data should be in the
form of a Data Set (list of correspondents’ addresses for example).

In our Document Factory, the Mail Merge Engine is provided by the commercial
Aspose Words Java API. You can extend and create your own implementation of the
document Factory with other engines.


.. _df-mail-merging-regions:

Mail Merging with Regions
=========================

.. warning:: 

   The mail merge with regions described here is specific to the Aspose engine.
   If you extend this current DocFactory by using another engine you have to
   refer to its specifical documentation.

If you want to dynamically grow portions inside the document, use mail merge
with regions. To specify a mail merge region in the document you need to insert
two mail merge fields to mark the beginning and end of the mail merge region.
All document content that is included inside a mail merge region automatically
will be repeated for every record in the data source (in most cases this is a
table). 

To mark the beginning of a mail merge region, insert a MERGEFIELD with the name
TableStart:MyTable, where MyTable corresponds to the name of the table. To mark
the end of the mail merge region insert another MERGEFIELD with the name
TableEnd:MyTable. Between these marking fields, place merge fields that
correspond to the fields of your data source (table columns). 

These merge fields will be populated with data from the first row of the data
source, then the whole region will be repeated, and the new fields will be
populated with data from the second row, and so on. 

Follow these simple rules when marking a region: 

   * TableStart and TableEnd fields must be inside the same section in the
     document.
   * If used inside a table,TableStart and TableEnd must be inside the
     same row in the table.
   * Mail merge regions can be nested inside each other.
   * Mail merge regions should be well formed (there is always a pair of
     matching TableStart and TableEnd with the same table name).

The following picture shows a template with a mail merge region:

.. figure:: /_static/images/doc-factory/mail-merge-region-template.png

The following picture shows the result of the mail merge with regions:

.. figure:: /_static/images/doc-factory/mail-merge-region-result.png

We have provided some callables subprocesses to ease the use of mail merge and
mail merge with regions. Please refer to the :ref:`next section <df-doc-factory-object>`.

.. _df-mail-merging-nested_regions:

Mail Merging with Nested Regions
================================

.. warning:: 

   The mail merge with nested regions described here is specific to the Aspose
   engine. If you extend this current DocFactory by using another engine you
   have to refer to its specifical documentation.

.. tip::

   Most of the information provided in this section was retrieved from the
   aspose official documentation. You can find the complete documentation on the
   aspose web site at `Aspose <www.aspose.com>`_. 

Most data in relational databases or XML files is hierarchical (e.g. with
parent-child relationships). One common example is a report about employees of a
company. The different employees are employed in different departments and have
for example several human capital elements. In this case we have several Items
nested in each other: Human Capital elements nested in Employees nested in
Departments nested in a Company.

Aspose.Words allows nesting mail merge regions inside each other in a document
to reflect the way the data is nested and this allows you to easily populate a
document with hierarchical data. Nested mail merge regions are at least two
regions in which one is defined entirely inside the other, so they are “nested”
in one another. In a document it looks like this:

.. figure:: /_static/images/doc-factory/mail-merge-nested-1.png

There are a few things you need to consider when preparing nested mail merge
regions and merge regions in general:

   * The mail merge region opening and closing tag (e.g. TableStart:Order,
     TableEnd:Order) both need to appear in the same row or cell. For example, if
     you start a merge region in a cell of a table, you must end the merge region
     in the same row as the first cell.
   * The names of the columns in the DataTable must match the merge field name.
     Unless you have specified mapped fields the merge will not be successful
     for those fields whose names are different. In the Ivy Implementation of
     this feature we use the Dataclasses Class names as TableStart names and the
     Dataclasses attributes names as mergefields names (more information
     :ref:`here <mail-merge-nested-mapping>`).
   * Mail merge regions should be well formed (there is always a pair of
     matching TableStart and TableEnd with the same table name).

The following picture shows a template with a mail merge region:

.. figure:: /_static/images/doc-factory/mail-merge-nested-2.png

The following picture shows the result of the mail merge with regions:

.. figure:: /_static/images/doc-factory/mail-merge-nested-3.png

.. _mail-merge-nested-mapping:

The previous nested mail merging was made by using List of Ivy DataClasses built as following:

.. figure:: /_static/images/doc-factory/mail-merge-nested-4.png

**It is very important to create your templates with this in mind:**

   * Each TableStart merge field has to be named like the corresponding
     dataclass: e.g. the TableStart:Employee will correspond to the Employee data
     object. This rule is case sensitive.
   * Within each "Data Object - Table" the merge fields will be filled with the
     corresponding data attribute value. The merge field must have the same name
     as the data attribute. E.g. the level attribute/mergefield in the
     HumanCapital table.

We have provided some callables subprocesses to ease the use of mail merge and
mail merge with nested regions. Please refer to the :ref:`next section
<df-doc-factory-object>`.