[Ivy]
[>Created: Thu Jun 21 12:52:26 EDT 2012]
13800684CB843ED5 3.17 #module
>Proto >Proto Collection #zClass
Fs0 FileTagWriterProcess Big #zClass
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
Fs0 @PushWFArc f10 '' #zField
Fs0 @RichDialogProcessStep f11 '' #zField
Fs0 @PushWFArc f12 '' #zField
Fs0 @PushWFArc f9 '' #zField
>Proto Fs0 Fs0 FileTagWriterProcess #zField
Fs0 f0 guid 13800684CCCB6DD9 #txt
Fs0 f0 type ch.ivyteam.ivy.addons.filemanager.tag.FileTagWriter.FileTagWriterData #txt
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
Fs0 f1 type ch.ivyteam.ivy.addons.filemanager.tag.FileTagWriter.FileTagWriterData #txt
Fs0 f1 86 150 20 20 13 0 #rect
Fs0 f1 @|RichDialogProcessEndIcon #fIcon
Fs0 f2 expr out #txt
Fs0 f2 96 74 96 150 #arcP
Fs0 f3 guid 1380074989E25F18 #txt
Fs0 f3 type ch.ivyteam.ivy.addons.filemanager.tag.FileTagWriter.FileTagWriterData #txt
Fs0 f3 method start(ch.ivyteam.ivy.addons.filemanager.FileTag,String) #txt
Fs0 f3 disableUIEvents true #txt
Fs0 f3 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.filemanager.FileTag fileTag,java.lang.String parentDocumentName> param = methodEvent.getInputArguments();
' #txt
Fs0 f3 inParameterMapAction 'out.actionCancelled=true;
out.documentName=param.parentDocumentName;
out.fileTag=param.fileTag;
' #txt
Fs0 f3 outParameterDecl '<ch.ivyteam.ivy.addons.filemanager.FileTag fileTag,java.lang.Boolean actionCancelled> result;
' #txt
Fs0 f3 outParameterMapAction 'result.fileTag=in.fileTag;
result.actionCancelled=in.actionCancelled;
' #txt
Fs0 f3 embeddedRdInitializations '* ' #txt
Fs0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(FileTag,String)</name>
        <nameStyle>21,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f3 198 54 20 20 13 0 #rect
Fs0 f3 @|RichDialogInitStartIcon #fIcon
Fs0 f4 type ch.ivyteam.ivy.addons.filemanager.tag.FileTagWriter.FileTagWriterData #txt
Fs0 f4 198 126 20 20 13 0 #rect
Fs0 f4 @|RichDialogProcessEndIcon #fIcon
Fs0 f5 expr out #txt
Fs0 f5 208 74 208 126 #arcP
Fs0 f6 guid 138007A11DD52E71 #txt
Fs0 f6 type ch.ivyteam.ivy.addons.filemanager.tag.FileTagWriter.FileTagWriterData #txt
Fs0 f6 actionDecl 'ch.ivyteam.ivy.addons.filemanager.tag.FileTagWriter.FileTagWriterData out;
' #txt
Fs0 f6 actionTable 'out=in;
out.actionCancelled=in.fileTag.tag.trim().length()>0;
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
Fs0 f7 guid 138007A34771BD5C #txt
Fs0 f7 type ch.ivyteam.ivy.addons.filemanager.tag.FileTagWriter.FileTagWriterData #txt
Fs0 f7 actionDecl 'ch.ivyteam.ivy.addons.filemanager.tag.FileTagWriter.FileTagWriterData out;
' #txt
Fs0 f7 actionTable 'out=in;
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
Fs0 f7 526 54 20 20 13 0 #rect
Fs0 f7 @|RichDialogProcessStartIcon #fIcon
Fs0 f8 type ch.ivyteam.ivy.addons.filemanager.tag.FileTagWriter.FileTagWriterData #txt
Fs0 f8 guid 138007A6D6ECC229 #txt
Fs0 f8 470 150 20 20 13 0 #rect
Fs0 f8 @|RichDialogEndIcon #fIcon
Fs0 f10 expr out #txt
Fs0 f10 530 72 485 151 #arcP
Fs0 f11 actionDecl 'ch.ivyteam.ivy.addons.filemanager.tag.FileTagWriter.FileTagWriterData out;
' #txt
Fs0 f11 actionTable 'out=in;
' #txt
Fs0 f11 actionCode 'if(in.fileTag.tag.trim().length()==0)
{
	out.actionCancelled=true;
}else{
	out.actionCancelled=false;
}' #txt
Fs0 f11 type ch.ivyteam.ivy.addons.filemanager.tag.FileTagWriter.FileTagWriterData #txt
Fs0 f11 430 100 36 24 20 -2 #rect
Fs0 f11 @|RichDialogProcessStepIcon #fIcon
Fs0 f12 expr out #txt
Fs0 f12 421 72 440 100 #arcP
Fs0 f9 expr out #txt
Fs0 f9 456 124 474 151 #arcP
>Proto Fs0 .ui2RdDataAction 'out.fileTag.tag=panel.TextField.valueAsString;
' #txt
>Proto Fs0 .rdData2UIAction 'panel.Label.text=IF(in.#fileTag!=null && in.fileTag.id>0,
ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileTags/editFileTag").replaceFirst("\\[FILE]", in.documentName),
ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileTags/newFileTag").replaceFirst("\\[FILE]", in.documentName)
);
panel.TextField.valueAsString=in.fileTag.tag;
' #txt
>Proto Fs0 .type ch.ivyteam.ivy.addons.filemanager.tag.FileTagWriter.FileTagWriterData #txt
>Proto Fs0 .processKind RICH_DIALOG #txt
>Proto Fs0 -8 -8 16 16 16 26 #rect
>Proto Fs0 '' #fIcon
Fs0 f0 mainOut f2 tail #connect
Fs0 f2 head f1 mainIn #connect
Fs0 f3 mainOut f5 tail #connect
Fs0 f5 head f4 mainIn #connect
Fs0 f7 mainOut f10 tail #connect
Fs0 f10 head f8 mainIn #connect
Fs0 f6 mainOut f12 tail #connect
Fs0 f12 head f11 mainIn #connect
Fs0 f11 mainOut f9 tail #connect
Fs0 f9 head f8 mainIn #connect
