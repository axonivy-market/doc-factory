[Ivy]
[>Created: Mon Jun 18 12:46:05 EDT 2012]
137F053222EE7C84 3.17 #module
>Proto >Proto Collection #zClass
Fs0 FileTypeWriterProcess Big #zClass
Fs0 RD #cInfo
Fs0 #process
Fs0 @TextInP .ui2RdDataAction .ui2RdDataAction #zField
Fs0 @TextInP .rdData2UIAction .rdData2UIAction #zField
Fs0 @TextInP .resExport .resExport #zField
Fs0 @TextInP .type .type #zField
Fs0 @TextInP .processKind .processKind #zField
Fs0 @AnnotationInP-0n ai ai #zField
Fs0 @TextInP .xml .xml #zField
Fs0 @TextInP .responsibility .responsibility #zField
Fs0 @RichDialogInitStart f0 '' #zField
Fs0 @RichDialogProcessEnd f1 '' #zField
Fs0 @PushWFArc f2 '' #zField
Fs0 @RichDialogInitStart f3 '' #zField
Fs0 @RichDialogProcessEnd f4 '' #zField
Fs0 @PushWFArc f5 '' #zField
Fs0 @RichDialogProcessStart f6 '' #zField
Fs0 @RichDialogProcessStart f7 '' #zField
Fs0 @RichDialogEnd f8 '' #zField
Fs0 @PushWFArc f9 '' #zField
Fs0 @RichDialogEnd f10 '' #zField
Fs0 @PushWFArc f11 '' #zField
>Proto Fs0 Fs0 FileTypeWriterProcess #zField
Fs0 f0 guid 137F053225077433 #txt
Fs0 f0 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypeWriter.FileTypeWriterData #txt
Fs0 f0 method start() #txt
Fs0 f0 disableUIEvents true #txt
Fs0 f0 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<> param = methodEvent.getInputArguments();
' #txt
Fs0 f0 outParameterDecl '<> result;
' #txt
Fs0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start()</name>
    </language>
</elementInfo>
' #txt
Fs0 f0 86 54 20 20 13 0 #rect
Fs0 f0 @|RichDialogInitStartIcon #fIcon
Fs0 f1 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypeWriter.FileTypeWriterData #txt
Fs0 f1 86 150 20 20 13 0 #rect
Fs0 f1 @|RichDialogProcessEndIcon #fIcon
Fs0 f2 expr out #txt
Fs0 f2 96 74 96 150 #arcP
Fs0 f3 guid 137F05E18446F31E #txt
Fs0 f3 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypeWriter.FileTypeWriterData #txt
Fs0 f3 method start(ch.ivyteam.ivy.addons.filemanager.FileType) #txt
Fs0 f3 disableUIEvents true #txt
Fs0 f3 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.filemanager.FileType fileType> param = methodEvent.getInputArguments();
' #txt
Fs0 f3 inParameterMapAction 'out.actionCancelled=true;
out.fileType=param.fileType;
' #txt
Fs0 f3 outParameterDecl '<ch.ivyteam.ivy.addons.filemanager.FileType fileType,java.lang.Boolean actionCancelled> result;
' #txt
Fs0 f3 outParameterMapAction 'result.fileType=in.fileType;
result.actionCancelled=in.actionCancelled;
' #txt
Fs0 f3 embeddedRdInitializations '* ' #txt
Fs0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(FileType)</name>
        <nameStyle>15,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f3 206 54 20 20 13 0 #rect
Fs0 f3 @|RichDialogInitStartIcon #fIcon
Fs0 f4 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypeWriter.FileTypeWriterData #txt
Fs0 f4 206 118 20 20 13 0 #rect
Fs0 f4 @|RichDialogProcessEndIcon #fIcon
Fs0 f5 expr out #txt
Fs0 f5 216 74 216 118 #arcP
Fs0 f6 guid 137F05E83A6755DB #txt
Fs0 f6 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypeWriter.FileTypeWriterData #txt
Fs0 f6 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypeWriter.FileTypeWriterData out;
' #txt
Fs0 f6 actionTable 'out=in;
out.actionCancelled=in.fileType.fileTypeName.trim().length()==0;
' #txt
Fs0 f6 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>ok</name>
        <nameStyle>2,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f6 406 54 20 20 13 0 #rect
Fs0 f6 @|RichDialogProcessStartIcon #fIcon
Fs0 f7 guid 137F05EA806B2D49 #txt
Fs0 f7 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypeWriter.FileTypeWriterData #txt
Fs0 f7 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypeWriter.FileTypeWriterData out;
' #txt
Fs0 f7 actionTable 'out=in;
out.actionCancelled=true;
' #txt
Fs0 f7 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>cancel</name>
        <nameStyle>6,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f7 510 54 20 20 13 0 #rect
Fs0 f7 @|RichDialogProcessStartIcon #fIcon
Fs0 f8 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypeWriter.FileTypeWriterData #txt
Fs0 f8 guid 137F05ED5964030A #txt
Fs0 f8 510 102 20 20 13 0 #rect
Fs0 f8 @|RichDialogEndIcon #fIcon
Fs0 f9 expr out #txt
Fs0 f9 520 74 520 102 #arcP
Fs0 f10 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypeWriter.FileTypeWriterData #txt
Fs0 f10 guid 137F05EE636DB5A3 #txt
Fs0 f10 406 110 20 20 13 0 #rect
Fs0 f10 @|RichDialogEndIcon #fIcon
Fs0 f11 expr out #txt
Fs0 f11 416 74 416 110 #arcP
>Proto Fs0 .ui2RdDataAction 'out.fileType.applicationName=panel.appNameTextField.valueAsString;
out.fileType.fileTypeName=panel.nameTextField.valueAsString;
' #txt
>Proto Fs0 .rdData2UIAction 'panel.appNameTextField.valueAsString=in.fileType.applicationName;
panel.idLabel.text=IF(in.fileType.id>0, "Id: "+in.fileType.id,
ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileTypes/strings/newFileType")
);
panel.nameTextField.valueAsString=in.fileType.fileTypeName;
' #txt
>Proto Fs0 .type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypeWriter.FileTypeWriterData #txt
>Proto Fs0 .processKind RICH_DIALOG #txt
>Proto Fs0 -8 -8 16 16 16 26 #rect
>Proto Fs0 '' #fIcon
Fs0 f0 mainOut f2 tail #connect
Fs0 f2 head f1 mainIn #connect
Fs0 f3 mainOut f5 tail #connect
Fs0 f5 head f4 mainIn #connect
Fs0 f7 mainOut f9 tail #connect
Fs0 f9 head f8 mainIn #connect
Fs0 f6 mainOut f11 tail #connect
Fs0 f11 head f10 mainIn #connect
