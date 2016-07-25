[Ivy]
[>Created: Mon Jul 25 13:56:13 CEST 2016]
15621E7FCADD3A4D 3.18 #module
>Proto >Proto Collection #zClass
Rn0 RichDialogPanelDefinition Big #zClass
Rn0 B #cInfo
Rn0 #process
Rn0 @TextInP .resExport .resExport #zField
Rn0 @TextInP .type .type #zField
Rn0 @TextInP .processKind .processKind #zField
Rn0 @AnnotationInP-0n ai ai #zField
Rn0 @MessageFlowInP-0n messageIn messageIn #zField
Rn0 @MessageFlowOutP-0n messageOut messageOut #zField
Rn0 @TextInP .xml .xml #zField
Rn0 @TextInP .responsibility .responsibility #zField
Rn0 @StartSub f0 '' #zField
Rn0 @EndSub f1 '' #zField
Rn0 @PushWFArc f2 '' #zField
>Proto Rn0 Rn0 RichDialogPanelDefinition #zField
Rn0 f0 inParamDecl '<ch.ivyteam.ivy.addons.filemanager.FileManager.FileManagerPanel panel,ch.ivyteam.ivy.addons.filemanager.configuration.FileManagerConfigurationController fileManagerConfiguration> param;' #txt
Rn0 f0 inParamTable 'out.fileManagerConfiguration=param.fileManagerConfiguration;
out.panel=param.panel;
' #txt
Rn0 f0 outParamDecl '<> result;
' #txt
Rn0 f0 actionDecl 'ch.ivyteam.ivy.addons.filemanager.processes.RichDialogPanelDefinitionData out;
' #txt
Rn0 f0 callSignature call(ch.ivyteam.ivy.addons.filemanager.FileManager.FileManagerPanel,ch.ivyteam.ivy.addons.filemanager.configuration.FileManagerConfigurationController) #txt
Rn0 f0 type ch.ivyteam.ivy.addons.filemanager.processes.RichDialogPanelDefinitionData #txt
Rn0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>call(FileManagerPanel,FileManagerConfigurationController)</name>
    </language>
</elementInfo>
' #txt
Rn0 f0 51 83 26 26 14 0 #rect
Rn0 f0 @|StartSubIcon #fIcon
Rn0 f1 type ch.ivyteam.ivy.addons.filemanager.processes.RichDialogPanelDefinitionData #txt
Rn0 f1 51 339 26 26 14 0 #rect
Rn0 f1 @|EndSubIcon #fIcon
Rn0 f2 expr out #txt
Rn0 f2 64 109 64 339 #arcP
>Proto Rn0 .type ch.ivyteam.ivy.addons.filemanager.processes.RichDialogPanelDefinitionData #txt
>Proto Rn0 .processKind CALLABLE_SUB #txt
>Proto Rn0 0 0 32 24 18 0 #rect
>Proto Rn0 @|BIcon #fIcon
Rn0 f0 mainOut f2 tail #connect
Rn0 f2 head f1 mainIn #connect
