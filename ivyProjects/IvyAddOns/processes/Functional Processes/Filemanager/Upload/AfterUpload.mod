[Ivy]
[>Created: Thu Dec 04 20:47:56 EST 2014]
14A1821CD33E7BF4 3.17 #module
>Proto >Proto Collection #zClass
Ad0 AfterUpload Big #zClass
Ad0 B #cInfo
Ad0 #process
Ad0 @TextInP .resExport .resExport #zField
Ad0 @TextInP .type .type #zField
Ad0 @TextInP .processKind .processKind #zField
Ad0 @AnnotationInP-0n ai ai #zField
Ad0 @MessageFlowInP-0n messageIn messageIn #zField
Ad0 @MessageFlowOutP-0n messageOut messageOut #zField
Ad0 @TextInP .xml .xml #zField
Ad0 @TextInP .responsibility .responsibility #zField
Ad0 @StartSub f0 '' #zField
Ad0 @EndSub f1 '' #zField
Ad0 @PushWFArc f2 '' #zField
>Proto Ad0 Ad0 AfterUpload #zField
Ad0 f0 inParamDecl '<ch.ivyteam.ivy.addons.filemanager.configuration.FileManagerConfigurationController configuration,ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler fileManagementHandler,List<ch.ivyteam.ivy.addons.filemanager.DocumentOnServer> createdDocumentOnServersWithUploadedFiles> param;' #txt
Ad0 f0 inParamTable 'out.configuration=param.configuration;
out.fileManagementHandler=param.fileManagementHandler;
out.createdDocumentOnServersWithUploadedFiles=param.createdDocumentOnServersWithUploadedFiles;
' #txt
Ad0 f0 outParamDecl '<List<ch.ivyteam.ivy.addons.filemanager.DocumentOnServer> createdDocumentOnServersWithUploadedFiles> result;
' #txt
Ad0 f0 outParamTable 'result.createdDocumentOnServersWithUploadedFiles=in.createdDocumentOnServersWithUploadedFiles;
' #txt
Ad0 f0 actionDecl 'ch.ivyteam.ivy.addons.filemanager.processes.UploadData out;
' #txt
Ad0 f0 callSignature call(ch.ivyteam.ivy.addons.filemanager.configuration.FileManagerConfigurationController,ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler,List<ch.ivyteam.ivy.addons.filemanager.DocumentOnServer>) #txt
Ad0 f0 type ch.ivyteam.ivy.addons.filemanager.processes.UploadData #txt
Ad0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>call(FileManagerConfigurationController,AbstractFileManagementHandler,List&lt;DocumentOnServer&gt;)</name>
    </language>
</elementInfo>
' #txt
Ad0 f0 51 83 26 26 14 0 #rect
Ad0 f0 @|StartSubIcon #fIcon
Ad0 f1 type ch.ivyteam.ivy.addons.filemanager.processes.UploadData #txt
Ad0 f1 51 339 26 26 14 0 #rect
Ad0 f1 @|EndSubIcon #fIcon
Ad0 f2 64 109 64 339 #arcP
>Proto Ad0 .type ch.ivyteam.ivy.addons.filemanager.processes.UploadData #txt
>Proto Ad0 .processKind CALLABLE_SUB #txt
>Proto Ad0 0 0 32 24 18 0 #rect
>Proto Ad0 @|BIcon #fIcon
Ad0 f0 mainOut f2 tail #connect
Ad0 f2 head f1 mainIn #connect
