[Ivy]
[>Created: Thu Mar 21 10:47:07 EDT 2013]
13D8D008776EF9B2 3.17 #module
>Proto >Proto Collection #zClass
Ms0 MessageDialogProcess Big #zClass
Ms0 RD #cInfo
Ms0 #process
Ms0 @TextInP .ui2RdDataAction .ui2RdDataAction #zField
Ms0 @TextInP .rdData2UIAction .rdData2UIAction #zField
Ms0 @TextInP .resExport .resExport #zField
Ms0 @TextInP .type .type #zField
Ms0 @TextInP .processKind .processKind #zField
Ms0 @AnnotationInP-0n ai ai #zField
Ms0 @TextInP .xml .xml #zField
Ms0 @TextInP .responsibility .responsibility #zField
Ms0 @RichDialogInitStart f0 '' #zField
Ms0 @RichDialogProcessEnd f1 '' #zField
Ms0 @PushWFArc f2 '' #zField
Ms0 @RichDialogInitStart f3 '' #zField
Ms0 @RichDialogProcessStep f4 '' #zField
Ms0 @PushWFArc f5 '' #zField
Ms0 @RichDialogProcessEnd f6 '' #zField
Ms0 @PushWFArc f7 '' #zField
Ms0 @RichDialogProcessStart f8 '' #zField
Ms0 @RichDialogEnd f9 '' #zField
Ms0 @PushWFArc f10 '' #zField
Ms0 @RichDialogInitStart f11 '' #zField
Ms0 @RichDialogProcessStep f12 '' #zField
Ms0 @PushWFArc f13 '' #zField
Ms0 @RichDialogProcessEnd f14 '' #zField
Ms0 @RichDialogProcessStep f16 '' #zField
Ms0 @PushWFArc f17 '' #zField
Ms0 @PushWFArc f15 '' #zField
>Proto Ms0 Ms0 MessageDialogProcess #zField
Ms0 f0 guid 13D8D00879DEF055 #txt
Ms0 f0 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f0 method start() #txt
Ms0 f0 disableUIEvents true #txt
Ms0 f0 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<> param = methodEvent.getInputArguments();
' #txt
Ms0 f0 outParameterDecl '<> result;
' #txt
Ms0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start()</name>
    </language>
</elementInfo>
' #txt
Ms0 f0 86 54 20 20 13 0 #rect
Ms0 f0 @|RichDialogInitStartIcon #fIcon
Ms0 f1 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f1 86 150 20 20 13 0 #rect
Ms0 f1 @|RichDialogProcessEndIcon #fIcon
Ms0 f2 expr out #txt
Ms0 f2 96 74 96 150 #arcP
Ms0 f3 guid 13D8D11B59954BA3 #txt
Ms0 f3 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f3 method start(String,String) #txt
Ms0 f3 disableUIEvents true #txt
Ms0 f3 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.String text,java.lang.String iconUri> param = methodEvent.getInputArguments();
' #txt
Ms0 f3 inParameterMapAction 'out.iconUri=param.iconUri;
out.text=param.text;
' #txt
Ms0 f3 outParameterDecl '<> result;
' #txt
Ms0 f3 embeddedRdInitializations '* ' #txt
Ms0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(String,String)</name>
        <nameStyle>20,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ms0 f3 214 54 20 20 13 0 #rect
Ms0 f3 @|RichDialogInitStartIcon #fIcon
Ms0 f4 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData out;
' #txt
Ms0 f4 actionTable 'out=in;
' #txt
Ms0 f4 actionCode 'panel.messageLabel.setText(in.text);
if(in.#iconUri!=null && in.iconUri.trim().length()>0 && 
	ivy.cms.getContentObject(in.iconUri)!=null){
		panel.iconLabel.setIconUri(in.iconUri);
}
' #txt
Ms0 f4 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f4 206 116 36 24 20 -2 #rect
Ms0 f4 @|RichDialogProcessStepIcon #fIcon
Ms0 f5 expr out #txt
Ms0 f5 224 74 224 116 #arcP
Ms0 f6 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f6 214 182 20 20 13 0 #rect
Ms0 f6 @|RichDialogProcessEndIcon #fIcon
Ms0 f7 expr out #txt
Ms0 f7 224 140 224 182 #arcP
Ms0 f8 guid 13D8D13FB76A405F #txt
Ms0 f8 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f8 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData out;
' #txt
Ms0 f8 actionTable 'out=in;
' #txt
Ms0 f8 actionCode 'import ch.ivyteam.ivy.richdialog.widgets.components.RButton;

if(event.getSource() instanceof RButton){
	RButton b = event.getSource() as RButton;
	out.pressedButton = b.getText();
}' #txt
Ms0 f8 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Ok</name>
        <nameStyle>2,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ms0 f8 398 54 20 20 13 0 #rect
Ms0 f8 @|RichDialogProcessStartIcon #fIcon
Ms0 f9 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f9 guid 13D8D14072642DF8 #txt
Ms0 f9 398 110 20 20 13 0 #rect
Ms0 f9 @|RichDialogEndIcon #fIcon
Ms0 f10 expr out #txt
Ms0 f10 408 74 408 110 #arcP
Ms0 f11 guid 13D8D3198173E6D2 #txt
Ms0 f11 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f11 method startAsConfirmDialog(String,String,List<String>,String) #txt
Ms0 f11 disableUIEvents true #txt
Ms0 f11 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.String text,java.lang.String iconUri,List<java.lang.String> buttonsList,java.lang.String defaultPressedButton> param = methodEvent.getInputArguments();
' #txt
Ms0 f11 inParameterMapAction 'out.buttons=param.buttonsList;
out.confirm=true;
out.defaultButton=param.defaultPressedButton;
out.iconUri=param.iconUri;
out.pressedButton=param.defaultPressedButton;
out.text=param.text;
' #txt
Ms0 f11 outParameterDecl '<java.lang.String pressedButton> result;
' #txt
Ms0 f11 outParameterMapAction 'result.pressedButton=in.pressedButton;
' #txt
Ms0 f11 embeddedRdInitializations '* ' #txt
Ms0 f11 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>startAsConfirmDialog(String,String,List&lt;String&gt;,String)</name>
        <nameStyle>55,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ms0 f11 86 230 20 20 -40 -29 #rect
Ms0 f11 @|RichDialogInitStartIcon #fIcon
Ms0 f12 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData out;
' #txt
Ms0 f12 actionTable 'out=in;
' #txt
Ms0 f12 actionCode 'import ch.ivyteam.ivy.richdialog.widgets.components.RButton;

if(!in.buttons.isEmpty()){
	if(in.buttons.get(0).trim().length()>0){
		panel.okButton.setText(in.buttons.get(0));
		panel.getRootPane().setDefaultButton(panel.okButton);
	}
	
	RButton button;
	int j=1;
	for(int i=1; i<in.buttons.size();i++){
		if(in.buttons.get(i).trim().length()>0){
			button = new RButton();
			button.text=in.buttons.get(i);
			panel.FlowLayoutPane.add(button,j);
			ivy.rd.addEventMapping(button, "Ok");
			button.setFont(panel.okButton.getFont());
			j++;
			if(in.defaultButton.trim().length()>0)
			{
				if (in.buttons.get(i).equalsIgnoreCase(in.defaultButton))
				{
					panel.getRootPane().setDefaultButton(button);
				}
			}
		}
	}
}else{
	panel.okButton.setText(ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/yes"));
	RButton button;
	button = new RButton();
	button.text=ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/no");
	ivy.rd.addEventMapping(button, "Ok");
	panel.FlowLayoutPane.add(button,1);
	if(in.defaultButton.trim().length()>0 && in.defaultButton.equalsIgnoreCase(ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/yes"))){
		panel.getRootPane().setDefaultButton(panel.okButton);
	}else{
		panel.getRootPane().setDefaultButton(button);
	}
}
if(in.pressedButton.trim().length()==0){
	out.pressedButton=panel.getRootPane().getDefaultButton().getText();
}' #txt
Ms0 f12 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f12 78 276 36 24 20 -2 #rect
Ms0 f12 @|RichDialogProcessStepIcon #fIcon
Ms0 f13 expr out #txt
Ms0 f13 96 250 96 276 #arcP
Ms0 f14 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f14 86 358 20 20 13 0 #rect
Ms0 f14 @|RichDialogProcessEndIcon #fIcon
Ms0 f16 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData out;
' #txt
Ms0 f16 actionTable 'out=in;
' #txt
Ms0 f16 actionCode 'panel.messageLabel.setText(in.text);
if(in.#iconUri!=null && in.iconUri.trim().length()>0 && 
	ivy.cms.getContentObject(in.iconUri)!=null){
		panel.iconLabel.setIconUri(in.iconUri);
}' #txt
Ms0 f16 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f16 78 316 36 24 20 -2 #rect
Ms0 f16 @|RichDialogProcessStepIcon #fIcon
Ms0 f17 expr out #txt
Ms0 f17 96 300 96 316 #arcP
Ms0 f15 expr out #txt
Ms0 f15 96 340 96 358 #arcP
>Proto Ms0 .type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
>Proto Ms0 .processKind RICH_DIALOG #txt
>Proto Ms0 -8 -8 16 16 16 26 #rect
>Proto Ms0 '' #fIcon
Ms0 f0 mainOut f2 tail #connect
Ms0 f2 head f1 mainIn #connect
Ms0 f3 mainOut f5 tail #connect
Ms0 f5 head f4 mainIn #connect
Ms0 f4 mainOut f7 tail #connect
Ms0 f7 head f6 mainIn #connect
Ms0 f8 mainOut f10 tail #connect
Ms0 f10 head f9 mainIn #connect
Ms0 f11 mainOut f13 tail #connect
Ms0 f13 head f12 mainIn #connect
Ms0 f12 mainOut f17 tail #connect
Ms0 f17 head f16 mainIn #connect
Ms0 f16 mainOut f15 tail #connect
Ms0 f15 head f14 mainIn #connect
