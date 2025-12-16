.. _df-resolving-cyclic-relations:

Resolving Cyclic Relations
==========================

Cyclic relations occur when elements reference each other in a loop.
If not handled properly, this can cause repeated processing, errors, or performance issues.
Managing cyclic relations helps ensure the system works reliably and predictably.

.. figure:: /_static/images/cyclic-relations.png

To resolve this issue, we use a **visited object** approach to detect and avoid repeated processing.

We also introduce a new variable :file:`com.axonivy.utils.docfactory.CyclicSupportLevels`
to control the number of traversal cycles.

When the value of this variable is **1**, the system can scan :file:`A -> B -> C`.

When the value of this variable is **2**, the system can scan :file:`A -> B -> C -> A -> B -> C`.

.. warning:: 

   As this value increases, performance may decrease due to additional traversal cycles.

.. tip::

   This variable has a default value of 1, which is the recommended setting. 