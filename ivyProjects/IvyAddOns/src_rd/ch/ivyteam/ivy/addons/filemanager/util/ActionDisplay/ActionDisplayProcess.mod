[Ivy]
[>Created: Wed Aug 21 08:43:03 CEST 2013]
1409F9CB876ED91F 3.17 #module
>Proto >Proto Collection #zClass
As0 ActionDisplayProcess Big #zClass
As0 RD #cInfo
As0 #process
As0 @TextInP .ui2RdDataAction .ui2RdDataAction #zField
As0 @TextInP .rdData2UIAction .rdData2UIAction #zField
As0 @TextInP .resExport .resExport #zField
As0 @TextInP .type .type #zField
As0 @TextInP .processKind .processKind #zField
As0 @AnnotationInP-0n ai ai #zField
As0 @TextInP .xml .xml #zField
As0 @TextInP .responsibility .responsibility #zField
As0 @RichDialogInitStart f0 '' #zField
As0 @RichDialogProcessEnd f1 '' #zField
As0 @PushWFArc f2 '' #zField
>Proto As0 As0 ActionDisplayProcess #zField
As0 f0 guid 1409F9CB8C26D1C6 #txt
As0 f0 type ch.ivyteam.ivy.addons.filemanager.util.ActionDisplay.ActionDisplayData #txt
As0 f0 method start() #txt
As0 f0 disableUIEvents true #txt
As0 f0 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<> param = methodEvent.getInputArguments();
' #txt
As0 f0 outParameterDecl '<> result;
' #txt
As0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start()</name>
    </language>
</elementInfo>
' #txt
As0 f0 86 54 20 20 13 0 #rect
As0 f0 @|RichDialogInitStartIcon #fIcon
As0 f1 type ch.ivyteam.ivy.addons.filemanager.util.ActionDisplay.ActionDisplayData #txt
As0 f1 86 150 20 20 13 0 #rect
As0 f1 @|RichDialogProcessEndIcon #fIcon
As0 f2 expr out #txt
As0 f2 96 74 96 150 #arcP
>Proto As0 .type ch.ivyteam.ivy.addons.filemanager.util.ActionDisplay.ActionDisplayData #txt
>Proto As0 .processKind RICH_DIALOG #txt
>Proto As0 -8 -8 16 16 16 26 #rect
>Proto As0 '' #fIcon
As0 f0 mainOut f2 tail #connect
As0 f2 head f1 mainIn #connect