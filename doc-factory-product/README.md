# Document Factory
The Document Factory is a system that allows to generate documents like mail
merges automatically with the help of Microsoft Office Templates (.dot or .dotx
files). The Document Factory is open, can be extended and is based on the Java
Library Aspose that is included in the Axon Ivy platform.

![Document Generation](doc.png)

## Setup
To insert images into a mail merge template, we recommend using the
[Aspose DocumentBuilder](https://docs.aspose.com/words/java/insert-picture-in-document/),
which programmatically retrieves and inserts images during the merge process. This approach provides
a more stable and maintainable solution than relying on the `INCLUDEPICTURE` mail merge field.

*For more details, please refer to process **3.1: Document Creation Capabilities** in demo project*