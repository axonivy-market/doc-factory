[Ivy]
[>Created: Tue Mar 19 22:19:47 EDT 2013]
13D83ACB5AF97DAD 3.17 #module
>Proto >Proto Collection #zClass
Fs0 FileAndDirectoryNameDialogProcess Big #zClass
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
Fs0 @RichDialogProcessStart f4 '' #zField
Fs0 @RichDialogProcessStart f5 '' #zField
Fs0 @RichDialogProcessStart f6 '' #zField
Fs0 @RichDialogEnd f7 '' #zField
Fs0 @PushWFArc f8 '' #zField
Fs0 @PushWFArc f9 '' #zField
Fs0 @RichDialogProcessStep f10 '' #zField
Fs0 @RichDialogProcessEnd f12 '' #zField
Fs0 @PushWFArc f13 '' #zField
Fs0 @Alternative f14 '' #zField
Fs0 @PushWFArc f15 '' #zField
Fs0 @RichDialogProcessStep f16 '' #zField
Fs0 @PushWFArc f17 '' #zField
Fs0 @RichDialogProcessStep f18 '' #zField
Fs0 @PushWFArc f19 '' #zField
Fs0 @RichDialogProcessEnd f20 '' #zField
Fs0 @PushWFArc f21 '' #zField
Fs0 @PushWFArc f22 '' #zField
Fs0 @RichDialogInitStart f3 '' #zField
Fs0 @PushWFArc f11 '' #zField
>Proto Fs0 Fs0 FileAndDirectoryNameDialogProcess #zField
Fs0 f0 guid 13D83ACB5C5EFF7A #txt
Fs0 f0 type ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData #txt
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
Fs0 f1 type ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData #txt
Fs0 f1 86 150 20 20 13 0 #rect
Fs0 f1 @|RichDialogProcessEndIcon #fIcon
Fs0 f2 expr out #txt
Fs0 f2 96 74 96 150 #arcP
Fs0 f4 guid 13D83C89032F0208 #txt
Fs0 f4 type ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData #txt
Fs0 f4 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData out;
' #txt
Fs0 f4 actionTable 'out=in;
' #txt
Fs0 f4 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>KeyPressed</name>
        <nameStyle>10,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f4 558 54 20 20 13 0 #rect
Fs0 f4 @|RichDialogProcessStartIcon #fIcon
Fs0 f5 guid 13D83C8E23A4250D #txt
Fs0 f5 type ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData #txt
Fs0 f5 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData out;
' #txt
Fs0 f5 actionTable 'out=in;
out.buttonClicked=in.error?panel.cancelButton.getText():panel.okButton.getText();
' #txt
Fs0 f5 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>OK</name>
        <nameStyle>2,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f5 758 54 20 20 13 0 #rect
Fs0 f5 @|RichDialogProcessStartIcon #fIcon
Fs0 f6 guid 13D83C926A39FEAD #txt
Fs0 f6 type ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData #txt
Fs0 f6 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData out;
' #txt
Fs0 f6 actionTable 'out=in;
out.buttonClicked=panel.cancelButton.getText();
' #txt
Fs0 f6 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Cancel</name>
        <nameStyle>6,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f6 862 54 20 20 13 0 #rect
Fs0 f6 @|RichDialogProcessStartIcon #fIcon
Fs0 f7 type ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData #txt
Fs0 f7 guid 13D83CA07528944B #txt
Fs0 f7 814 126 20 20 13 0 #rect
Fs0 f7 @|RichDialogEndIcon #fIcon
Fs0 f8 expr out #txt
Fs0 f8 774 71 817 128 #arcP
Fs0 f9 expr out #txt
Fs0 f9 866 72 829 127 #arcP
Fs0 f10 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData out;
' #txt
Fs0 f10 actionTable 'out=in;
' #txt
Fs0 f10 actionCode 'out.buttonClicked=panel.cancelButton.getText();

if(in.iconUri.trim().length()>0 && ivy.cms.getContentObject(in.iconUri)!=null){
	panel.iconLabel.setIconUri(ivy.cms.cr(in.iconUri));
}

if(in.excludePattern.trim().length()>0){
	out.pattern= java.util.regex.Pattern.compile(in.excludePattern.trim());
	String s = in.excludePattern;
	if(s.startsWith(".*[") && s.endsWith("].*")){
		s=s.substring(3,s.lastIndexOf("].*"));
	}
	out.invalidPatternMessage=ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/invalidCharacterInName")+" "+s;
}
' #txt
Fs0 f10 type ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData #txt
Fs0 f10 206 116 36 24 20 -2 #rect
Fs0 f10 @|RichDialogProcessStepIcon #fIcon
Fs0 f12 type ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData #txt
Fs0 f12 214 182 20 20 13 0 #rect
Fs0 f12 @|RichDialogProcessEndIcon #fIcon
Fs0 f13 expr out #txt
Fs0 f13 224 140 224 182 #arcP
Fs0 f14 type ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData #txt
Fs0 f14 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>text entered?</name>
        <nameStyle>13,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f14 554 114 28 28 14 0 #rect
Fs0 f14 @|AlternativeIcon #fIcon
Fs0 f15 expr out #txt
Fs0 f15 568 74 568 114 #arcP
Fs0 f16 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData out;
' #txt
Fs0 f16 actionTable 'out=in;
' #txt
Fs0 f16 actionCode 'import java.util.regex.Matcher;
in.error=false;
panel.messageLabel.visible=false;
if(in.#pattern!=null){
	Matcher m = in.pattern.matcher(panel.nameTextField.getText().trim());
	if(m.matches()){
		panel.messageLabel.setText(in.invalidPatternMessage);
		panel.messageLabel.visible=true;
		in.error=true;
	}
}
if(!in.excludeNames.isEmpty() && !in.error){
	if(in.excludeNames.contains(panel.nameTextField.getText())){
		
		panel.messageLabel.setText(in.type==0?ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/information/fileAlreadyExists"):
		ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/information/dirAlreadyExists"));
		panel.messageLabel.visible=true;
		in.error=true;
	}
}' #txt
Fs0 f16 type ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData #txt
Fs0 f16 550 188 36 24 20 -2 #rect
Fs0 f16 @|RichDialogProcessStepIcon #fIcon
Fs0 f17 expr in #txt
Fs0 f17 outCond panel.nameTextField.getText().trim().length()>0 #txt
Fs0 f17 568 142 568 188 #arcP
Fs0 f18 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData out;
' #txt
Fs0 f18 actionTable 'out=in;
out.error=true;
' #txt
Fs0 f18 type ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData #txt
Fs0 f18 462 180 36 24 20 -2 #rect
Fs0 f18 @|RichDialogProcessStepIcon #fIcon
Fs0 f19 expr in #txt
Fs0 f19 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>NO</name>
        <nameStyle>2,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f19 554 128 480 180 #arcP
Fs0 f19 1 480 128 #addKink
Fs0 f19 0 0.8409693526413281 0 0 #arcLabel
Fs0 f20 type ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData #txt
Fs0 f20 558 286 20 20 13 0 #rect
Fs0 f20 @|RichDialogProcessEndIcon #fIcon
Fs0 f21 expr out #txt
Fs0 f21 490 204 561 288 #arcP
Fs0 f22 expr out #txt
Fs0 f22 568 212 568 286 #arcP
Fs0 f3 guid 13D83F8E07C4C789 #txt
Fs0 f3 type ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData #txt
Fs0 f3 method start(String,String,List<String>,String,String,Number) #txt
Fs0 f3 disableUIEvents true #txt
Fs0 f3 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.String text,java.lang.String name,List<java.lang.String> excludeNames,java.lang.String excludePattern,java.lang.String iconUri,java.lang.Number type> param = methodEvent.getInputArguments();
' #txt
Fs0 f3 inParameterMapAction 'out.error=false;
out.excludeNames=param.excludeNames;
out.excludePattern=param.excludePattern;
out.iconUri=param.iconUri;
out.name=param.name;
out.text=param.text;
out.type=param.type;
' #txt
Fs0 f3 outParameterDecl '<java.lang.String name,java.lang.String clickedButton> result;
' #txt
Fs0 f3 outParameterMapAction 'result.name=in.name;
result.clickedButton=in.buttonClicked;
' #txt
Fs0 f3 embeddedRdInitializations '* ' #txt
Fs0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(String,String,List&lt;String&gt;,String,String,Number)</name>
        <nameStyle>54,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f3 214 54 20 20 13 0 #rect
Fs0 f3 @|RichDialogInitStartIcon #fIcon
Fs0 f11 expr out #txt
Fs0 f11 224 74 224 116 #arcP
>Proto Fs0 .ui2RdDataAction 'out.name=panel.nameTextField.text;
' #txt
>Proto Fs0 .rdData2UIAction 'panel.messageLabel.visible=in.error && in.name.trim().length()>0;
panel.nameTextField.text=in.name;
panel.okButton.enabled=!in.error;
panel.textLabel.text=in.text;
' #txt
>Proto Fs0 .type ch.ivyteam.ivy.addons.filemanager.util.FileAndDirectoryNameDialog.FileAndDirectoryNameDialogData #txt
>Proto Fs0 .processKind RICH_DIALOG #txt
>Proto Fs0 -8 -8 16 16 16 26 #rect
>Proto Fs0 '' #fIcon
Fs0 f0 mainOut f2 tail #connect
Fs0 f2 head f1 mainIn #connect
Fs0 f5 mainOut f8 tail #connect
Fs0 f8 head f7 mainIn #connect
Fs0 f6 mainOut f9 tail #connect
Fs0 f9 head f7 mainIn #connect
Fs0 f10 mainOut f13 tail #connect
Fs0 f13 head f12 mainIn #connect
Fs0 f4 mainOut f15 tail #connect
Fs0 f15 head f14 in #connect
Fs0 f14 out f17 tail #connect
Fs0 f17 head f16 mainIn #connect
Fs0 f14 out f19 tail #connect
Fs0 f19 head f18 mainIn #connect
Fs0 f18 mainOut f21 tail #connect
Fs0 f21 head f20 mainIn #connect
Fs0 f16 mainOut f22 tail #connect
Fs0 f22 head f20 mainIn #connect
Fs0 f3 mainOut f11 tail #connect
Fs0 f11 head f10 mainIn #connect
