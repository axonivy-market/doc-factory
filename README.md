# IvyAddsOns #

IvyAddOns is an Axon Ivy Project that provides common functionality to other Axon Ivy Projects. 

Until Axon Ivy 7.1 IvyAddOns was a single Axon Ivy Project and its source code was
hosted in the subversion repository in Zug (https://zugprovcssvn/svn/Entwicklung/Projects/IvyAddOns/).

With Axon Ivy 7.1 the code is now hosted on BitBucket. Moreover, the single project is now split into multiple projects:

* **IvyAddOnsCommons (ch.ivyteam.ivy.addons:commons)**
  
  Provides WaitForAsyncProcess, ResourceHelper, EnvironmentHelper, WaitTillLastTaskOfCase, ProcessParameter, QRCode, Sudo, ..

* **DocFactory ch.ivyteam.ivy.addons:doc-factory)**

  Provides Aspose DocFactory to create Word, PDF, Excel, Powerpoint documents

* **FileManagerApi (ch.ivyteam.ivy.addons:file-manager-api)**

  Provides an extended file manager API

* **IvyAddOns (ch.ivyteam.ivy.addons:ch.ivyteam.ivy.addons)**

  Facade that includes IvyAddOnsCommons, DocFactory and FileManagerApi. For backwards compatibility of old projects that depend on IvyAddOns.

The idea behind this splitting is that Axon Ivy Projects can decide which functionality they need and depend on those projects they really need.

## CI/CD Infrastruction ##

The following build on our Jenkins in Zug builds all projects for the master branch.

https://jenkins.ivyteam.io/job/ivy-project-ivy-addons/

It deploys the IvyAddOns projects to the following maven repository:

https://repo.axonivy.io/artifactory/ext-snapshot-local/ch/ivyteam/ivy/addons/


## Releasing since 8.0

1. Remove `-SNAPSHOT` from maven property `revision` in `maven/pom.xml`
2. Commit / Push and run build
3. Update `revision` in `maven/pom.xml` to next release version
