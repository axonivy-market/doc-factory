# IvyAddsOns #

IvyAddOns is an Axon.ivy Project that provides common functionality to other Axon.ivy Projects. 

Until Axon.ivy 7.1 IvyAddOns was a single Axon.ivy Project and its source code was hosted in the subversion repository in Zug (https://zugprovcssvn/svn/Entwicklung/Projects/IvyAddOns/). 
With Axon.ivy 7.1 the code is now hosted here on BitBucket. Moreover, the single project is now split into multiple projects:

* IvyAddOnsCommons
* DocFactory
* FileManagerApi
* IvyAddOns
* IvyAddOnsRichDialog

The idea behind this splitting is that the Axon.ivy Projects can decide which functionality they need and only use the project that delivers this functionality.

## CI/CD Infrastruction ##

The following build on our Jenkins in Zug builds all projects for the master branch.

http://zugprojenkins/job/ivy-project-ivy-addons/job/master/

It deploys the IvyAddOns projects to the following maven repository:

https://repo.axonivy.io/artifactory/ext-snapshot-local/ch/ivyteam/ivy/addons/

And the documentation to:

https://repo.axonivy.io/artifactory/ext-snapshot-local/ch/ivyteam/ivy/doc/doc.addons

The Axon.ivy builds gets the artifacts from this repository and integrates them into the Axon.ivy Designer.







