# IvyAddsOns #

IvyAddOns is an Axon.ivy Project that provides common functionality to other Axon.ivy Projects. 

Until Axon.ivy 7.1 IvyAddOns was a single Axon.ivy Project and its source code was hosted in the subversion repository in Zug (https://zugprovcssvn/svn/Entwicklung/Projects/IvyAddOns/). 
With Axon.ivy 7.1 the code is now hosted here on BitBucket. Moreover, the single project is now split into multiple projects:

* IvyAddOnsCommons (ch.ivyteam.ivy.addons:commons) - Provides WaitForAsyncProcess, ResourceHelper, EnvironmentHelper, WaitTillLastTaskOfCase, ProcessParameter, QRCode, Sudo, ..
* DocFactory ch.ivyteam.ivy.addons:doc-factory) - Provides Aspose DocFactory to create Word, PDF, Excel, Powerpoint documents
* FileManagerApi (ch.ivyteam.ivy.addons:file-manager-api) Provides an extended file manager API 
* IvyAddOns (ch.ivyteam.ivy.addons:ch.ivyteam.ivy.addons) Facade that includes IvyAddOnsCommons, DocFactory and FileManagerApi. For backwards compatibility of old projects that depend on IvyAddOns.
* IvyAddOnsRichDialog (ch.ivyteam.ivy.addons:rich-dialog)- Provides all the functionality for Rich Dialogs like: File Manager, Dynamic Dialogs, Event Log, ...

The idea behind this splitting is that Axon.ivy Projects can decide which functionality they need and depend on those projects they really need.

## CI/CD Infrastruction ##

The following build on our Jenkins in Zug builds all projects for the master branch.

http://zugprojenkins/job/ivy-project-ivy-addons/job/master/

It deploys the IvyAddOns projects to the following maven repository:

https://repo.axonivy.io/artifactory/ext-snapshot-local/ch/ivyteam/ivy/addons/

And the documentation to:

https://repo.axonivy.io/artifactory/ext-snapshot-local/ch/ivyteam/ivy/doc/doc.addons

The Axon.ivy builds gets the artifacts from this repository and integrates them into the Axon.ivy Designer.







