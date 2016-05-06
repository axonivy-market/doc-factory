[Ivy]
[>Created: Thu Dec 04 20:48:18 EST 2014]
14A1820E679D6A5E 3.17 #module
>Proto >Proto Collection #zClass
Bd0 BeforeUpload Big #zClass
Bd0 B #cInfo
Bd0 #process
Bd0 @TextInP .resExport .resExport #zField
Bd0 @TextInP .type .type #zField
Bd0 @TextInP .processKind .processKind #zField
Bd0 @AnnotationInP-0n ai ai #zField
Bd0 @MessageFlowInP-0n messageIn messageIn #zField
Bd0 @MessageFlowOutP-0n messageOut messageOut #zField
Bd0 @TextInP .xml .xml #zField
Bd0 @TextInP .responsibility .responsibility #zField
Bd0 @StartSub f0 '' #zField
Bd0 @EndSub f1 '' #zField
Bd0 @PushWFArc f2 '' #zField
>Proto Bd0 Bd0 BeforeUpload #zField
Bd0 f0 inParamDecl '<ch.ivyteam.ivy.addons.filemanager.configuration.FileManagerConfigurationController configuration,ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler fileManagementHandler,List<java.lang.String> selectedClientFilesForUpload> param;' #txt
Bd0 f0 inParamTable 'out.configuration=param.configuration;
out.fileManagementHandler=param.fileManagementHandler;
out.selectedClientFilesForUpload=param.selectedClientFilesForUpload;
' #txt
Bd0 f0 outParamDecl '<List<java.lang.String> selectedClientFilesForUpload> result;
' #txt
Bd0 f0 outParamTable 'result.selectedClientFilesForUpload=in.selectedClientFilesForUpload;
' #txt
Bd0 f0 actionDecl 'ch.ivyteam.ivy.addons.filemanager.processes.UploadData out;
' #txt
Bd0 f0 callSignature call(ch.ivyteam.ivy.addons.filemanager.configuration.FileManagerConfigurationController,ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler,List<String>) #txt
Bd0 f0 type ch.ivyteam.ivy.addons.filemanager.processes.UploadData #txt
Bd0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>call(FileManagerConfigurationController,AbstractFileManagementHandler,List&lt;String&gt;)</name>
        <nameStyle>83,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Bd0 f0 51 83 26 26 14 0 #rect
Bd0 f0 @|StartSubIcon #fIcon
Bd0 f1 type ch.ivyteam.ivy.addons.filemanager.processes.UploadData #txt
Bd0 f1 51 339 26 26 14 0 #rect
Bd0 f1 @|EndSubIcon #fIcon
Bd0 f2 expr out #txt
Bd0 f2 64 109 64 339 #arcP
>Proto Bd0 .type ch.ivyteam.ivy.addons.filemanager.processes.UploadData #txt
>Proto Bd0 .processKind CALLABLE_SUB #txt
>Proto Bd0 0 0 32 24 18 0 #rect
>Proto Bd0 @|BIcon #fIcon
Bd0 f0 mainOut f2 tail #connect
Bd0 f2 head f1 mainIn #connect
