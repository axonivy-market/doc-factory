WaitForAsyncProcess
*******************

The intermediate event bean
:code:`ch.ivyteam.ivy.addons.process.async.WaitForAsyncProcess` lets you wait in
your main business process for 1-n asynchronous processes until all of them have
finished and/or sent a signal that they have ended. 

For a quick demo checkout IvyDemos and see
**Processes/WaitForAsyncProcessDemo/MainBusinessProcess**.


How to
======

If you want to wait on the intermediate event until your asynchronous processes
have ended, you need to do the following 4 steps: 

   * :ref:`async-process-generate-id`
   * :ref:`async-process-trgger-and-register`
   * :ref:`async-process-optional-en`
   * :ref:`async-process-wait-on-event`


.. _async-process-generate-id:

Generate an EventID
-------------------

The unique EventID is used to determine if all processes with the same EventID
have ended. You can use :code:`ivy.case.getId()` or some other
methods to generate a unique identifier. 


.. _async-process-trgger-and-register:

Trigger and register your asynchronous process
----------------------------------------------

After you create the EventID you can trigger and register your processes. There
must be at least one registered process. Otherwise the intermediate event bean
will wait forever. 

Your asynchronous process start must be startable by a trigger and the
**Responsible Role** for the trigger must be set to **SYSTEM**. 

.. figure:: /_static/images/async-process/process-start.jpg

Now that you can start your process with a trigger, you can insert a trigger
element in your main process and call your asynchronous process. In the output
tab, you have to register the process.

:code:`WaitForAsyncProcess.registerProcess(triggeredTask.getCase(),in.eventId)`

.. figure:: /_static/images/async-process/process-register.jpg

The **registerProcess** method returns the eventId that was passed as a parameter.
This is for convenience so you can call **registerProcess** on your eventId
property. 

You can repeat this step until all your asynchronous process are triggered. 


.. _async-process-optional-end:

(Optional) Signal the end of the asynchronous process
-----------------------------------------------------

If you just want to execute an asynchronous process without sending any
parameters back to the main process, you can skip this step. 

However, if you want to send process data back to your main process, you can do
this by using the callable **Functional
Process/WaitForAsyncProcess/SignalEndOfAsyncProcess** and the Java class
:code:`ch.ivyteam.ivy.addons.process.parameter.ProcessParameter`. 

   * Create an attribute in the data class of your process of the type
     :code:`ch.ivyteam.ivy.addons.process.parameter.ProcessParameter`
   * Call **signalEnd**

ProcessParameter provides a Map like interface to store process data. It
supports the basic Ivy Data types. 

.. figure:: /_static/images/async-process/process-parameter.jpg

.. tip:: List must be one of the putXXX data types!

Here an example how you can put process data into the ProcessParameter: 

.. code::

   in.processParameter.putString("email", in.email)
      .putBoolean("email-created", true);

You can also put ProcessParameter objects into a ProcessParameter object in case
you need to compose an object like structure.

.. code::

   import ch.ivyteam.ivy.addons.process.parameter.ProcessParameter;
   ProcessParameter contact = new ProcessParameter();
   contact.putString("street", "Sukhumvit Soi 4");
   contact.putString("zip", "10110");
   // ... in.processParameter.putProcessParameter("contact", contact);

As an alternative you could separate the object path with dots. 

.. tip:: For convenience you can chain calls to putXXX. 

.. code::

   in.processParameter.putString("contact.street", "Sukhumvit Soi 4")
      .putString("contact.zip", "10110"); 

Now you are ready to send the parameters back to the main process. Use the
**SignalEndOfAsyncProcess** callable to do this. 

.. figure:: /_static/images/async-process/process-end.jpg

As you can see you have to pass 2 parameters: **parameterName** and **parameter**

As you will see later, **parameterName** is used as an access key on your
intermediate event bean to retrieve the ProcessParameter from all the
asynchronous processes that you triggered. Hence it has to be unique among the
triggered processes. **parameter** is the object you just filled with your process
data. 

.. tip::

   You could continue to do things/tasks in your asynchronous process after you
   sent the **signalEnd**. This is useful if you already have the data that you want
   to send back but still need to do other things. However, you can send the
   **signalEnd** only once in your process. If you don't have to send back data, you
   can use **signalEndNoParameter**. It works like **signalEnd** but with no parameter. 

.. figure:: /_static/images/async-process/process-end-nopara.jpg


.. _async-process-wait-on-event:

Wait on the intermediate event
------------------------------

Now you are ready to setup the intermediate event bean that waits for all your
asynchronous processes. 

.. figure:: /_static/images/async-process/process-event-1.jpg

Set the **Java Class to execute** to
:code:`ch.ivyteam.ivy.addons.process.async.WaitForAsyncProcess` and set **Event
ID** to the
ID you generated. In the Editor tab you can set the polling time if you want.
Note that this property can not be dynamic. You have to insert a number. 

.. figure:: /_static/images/async-process/process-event-2.jpg

On the Output tab you can now get your ProcessParameter objects with the access
key you defined in your asynchronous process. 


Why can't I use my own data classes?
------------------------------------

This should be considered a feature because it decouples the 2 processes (Main
business process and asynchronous sub process) from each other. Consider the
case where you have a main project that has a library project attached. If you
now call a process in the library project that returns a data class type defined
in it, you tie the main project to the library project. Now if you wish to
create a new implementation of the library project (lets say Xpertline instead
of SAP) you face the problem that you don't have the same data classes. You'd
have to change the main project to support the new data class. 

On the other hand, now it exchanges the data in the ProcessParameter object. You
only need to match the parameter names in order to exchange data. You don't have
to relay on a particular data type from another project and visa versa. 