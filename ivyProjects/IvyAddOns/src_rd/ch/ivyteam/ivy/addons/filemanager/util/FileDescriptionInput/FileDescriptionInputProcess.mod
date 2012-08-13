[Ivy]
[>Created: Tue Jul 03 18:11:04 CEST 2012]
13847B0C023E2C74 3.17 #module
>Proto >Proto Collection #zClass
Fs0 FileDescriptionInputProcess Big #zClass
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
Fs0 @RichDialogProcessStart f6 '' #zField
Fs0 @RichDialogProcessStart f7 '' #zField
Fs0 @RichDialogProcessStart f11 '' #zField
Fs0 @RichDialogProcessStep f12 '' #zField
Fs0 @PushWFArc f13 '' #zField
Fs0 @RichDialogProcessEnd f14 '' #zField
Fs0 @PushWFArc f15 '' #zField
Fs0 @RichDialogInitStart f3 '' #zField
Fs0 @RichDialogProcessStep f16 '' #zField
Fs0 @PushWFArc f17 '' #zField
Fs0 @RichDialogEnd f18 '' #zField
Fs0 @PushWFArc f19 '' #zField
Fs0 @PushWFArc f10 '' #zField
Fs0 @PushWFArc f8 '' #zField
>Proto Fs0 Fs0 FileDescriptionInputProcess #zField
Fs0 f0 guid 13847B0C03B4447B #txt
Fs0 f0 type ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData #txt
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
Fs0 f1 type ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData #txt
Fs0 f1 86 150 20 20 13 0 #rect
Fs0 f1 @|RichDialogProcessEndIcon #fIcon
Fs0 f2 expr out #txt
Fs0 f2 96 74 96 150 #arcP
Fs0 f6 guid 1384D52DC5C1D9DD #txt
Fs0 f6 type ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData #txt
Fs0 f6 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData out;
' #txt
Fs0 f6 actionTable 'out=in;
out.cancelled=false;
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
Fs0 f6 382 54 20 20 13 0 #rect
Fs0 f6 @|RichDialogProcessStartIcon #fIcon
Fs0 f7 guid 1384D53675A33931 #txt
Fs0 f7 type ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData #txt
Fs0 f7 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData out;
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
Fs0 f7 502 54 20 20 13 0 #rect
Fs0 f7 @|RichDialogProcessStartIcon #fIcon
Fs0 f11 guid 1384D53B01A9135E #txt
Fs0 f11 type ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData #txt
Fs0 f11 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData out;
' #txt
Fs0 f11 actionTable 'out=in;
' #txt
Fs0 f11 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>inputChanged</name>
        <nameStyle>12,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f11 446 238 20 20 13 0 #rect
Fs0 f11 @|RichDialogProcessStartIcon #fIcon
Fs0 f12 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData out;
' #txt
Fs0 f12 actionTable 'out=in;
' #txt
Fs0 f12 actionCode 'if(in.maxChar>0)
{
	if(in.description.length()>in.maxChar){
		panel.okButton.enabled=false;
		panel.validationLabel.visible=true;
	}else{
		panel.okButton.enabled=true;
		panel.validationLabel.visible=false;
	}
}' #txt
Fs0 f12 type ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData #txt
Fs0 f12 438 292 36 24 20 -2 #rect
Fs0 f12 @|RichDialogProcessStepIcon #fIcon
Fs0 f13 expr out #txt
Fs0 f13 456 258 456 292 #arcP
Fs0 f14 type ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData #txt
Fs0 f14 446 350 20 20 13 0 #rect
Fs0 f14 @|RichDialogProcessEndIcon #fIcon
Fs0 f15 expr out #txt
Fs0 f15 456 316 456 350 #arcP
Fs0 f3 guid 1384D59647E50C93 #txt
Fs0 f3 type ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData #txt
Fs0 f3 method start(String,Number,String) #txt
Fs0 f3 disableUIEvents true #txt
Fs0 f3 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.String input,java.lang.Number maxChar,java.lang.String textLabel> param = methodEvent.getInputArguments();
' #txt
Fs0 f3 inParameterMapAction 'out.cancelled=true;
out.description=param.input;
out.maxChar=param.maxChar;
' #txt
Fs0 f3 outParameterDecl '<java.lang.String output,java.lang.Boolean cancelled> result;
' #txt
Fs0 f3 outParameterMapAction 'result.output=in.description;
result.cancelled=in.cancelled;
' #txt
Fs0 f3 embeddedRdInitializations '* ' #txt
Fs0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(String,Number,String)</name>
        <nameStyle>27,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f3 174 54 20 20 13 0 #rect
Fs0 f3 @|RichDialogInitStartIcon #fIcon
Fs0 f16 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData out;
' #txt
Fs0 f16 actionTable 'out=in;
' #txt
Fs0 f16 actionCode 'panel.TextArea.requestFocusInWindow();
panel.validationLabel.setText(ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/information/enteredTextToolong").replaceAll("LLL",in.maxChar.toString()));
panel.validationLabel.visible=false;' #txt
Fs0 f16 type ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData #txt
Fs0 f16 166 92 36 24 20 -2 #rect
Fs0 f16 @|RichDialogProcessStepIcon #fIcon
Fs0 f17 expr out #txt
Fs0 f17 184 74 184 92 #arcP
Fs0 f18 type ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData #txt
Fs0 f18 guid 1384D8FB89C8D076 #txt
Fs0 f18 446 94 20 20 13 0 #rect
Fs0 f18 @|RichDialogEndIcon #fIcon
Fs0 f19 expr out #txt
Fs0 f19 503 69 464 98 #arcP
Fs0 f10 expr out #txt
Fs0 f10 400 69 447 98 #arcP
Fs0 f8 expr out #txt
Fs0 f8 184 116 438 304 #arcP
Fs0 f8 1 184 304 #addKink
Fs0 f8 1 0.13188930860326178 0 0 #arcLabel
>Proto Fs0 .ui2RdDataAction 'out.description=panel.TextArea.text;
' #txt
>Proto Fs0 .rdData2UIAction 'panel.titelLabel.text=in.textLabel;
panel.TextArea.text=in.description;
' #txt
>Proto Fs0 .type ch.ivyteam.ivy.addons.filemanager.util.FileDescriptionInput.FileDescriptionInputData #txt
>Proto Fs0 .processKind RICH_DIALOG #txt
>Proto Fs0 -8 -8 16 16 16 26 #rect
>Proto Fs0 '' #fIcon
Fs0 f0 mainOut f2 tail #connect
Fs0 f2 head f1 mainIn #connect
Fs0 f11 mainOut f13 tail #connect
Fs0 f13 head f12 mainIn #connect
Fs0 f12 mainOut f15 tail #connect
Fs0 f15 head f14 mainIn #connect
Fs0 f3 mainOut f17 tail #connect
Fs0 f17 head f16 mainIn #connect
Fs0 f7 mainOut f19 tail #connect
Fs0 f19 head f18 mainIn #connect
Fs0 f6 mainOut f10 tail #connect
Fs0 f10 head f18 mainIn #connect
Fs0 f16 mainOut f8 tail #connect
Fs0 f8 head f12 mainIn #connect
