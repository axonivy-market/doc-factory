[Ivy]
[>Created: Tue Aug 27 15:35:29 CEST 2013]
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
Ms0 @RichDialogInitStart f18 '' #zField
Ms0 @RichDialogProcessStep f19 '' #zField
Ms0 @PushWFArc f20 '' #zField
Ms0 @RichDialogProcessEnd f21 '' #zField
Ms0 @PushWFArc f22 '' #zField
Ms0 @RichDialogProcessStep f24 '' #zField
Ms0 @RichDialogProcessEnd f26 '' #zField
Ms0 @RichDialogInitStart f23 '' #zField
Ms0 @RichDialogUiSync f28 '' #zField
Ms0 @PushWFArc f29 '' #zField
Ms0 @PushWFArc f27 '' #zField
Ms0 @RichDialogProcessStep f25 '' #zField
Ms0 @PushWFArc f30 '' #zField
Ms0 @PushWFArc f31 '' #zField
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
Ms0 f8 actionCode 'import com.ulcjava.base.application.ULCComponent;
import ch.ivyteam.ivy.richdialog.widgets.components.RButton;
import ch.ivyteam.ivy.richdialog.widgets.components.RCheckBox;
import ch.ivyteam.ivy.richdialog.widgets.components.RLabel;

if(event.getSource() instanceof RButton || event.getSource() instanceof RLabel){
	if(in.startCheckBoxes){
		List<ULCComponent> liste = panel.checkBoxesGridLayoutPane.getComponents();
		for(ULCComponent ch : liste) {
			if(ch instanceof RCheckBox) {
				RCheckBox c = ch as RCheckBox;
				if(c.isSelected()){
					out.pressedButton = c.getText();
					break;
				}
			}
		}
	}else{
		if(event.getSource() instanceof RButton){
			RButton b = event.getSource() as RButton;
			out.pressedButton = b.getText();
		} else {
			RLabel b = event.getSource() as RLabel;
			out.pressedButton = b.getText();
		}
		if(!in.optionalCheckBox.isEmpty()) {
			out.confirm=panel.optionalCheckBox.selected;
		}
	}
}else if(in.startCheckBoxes){
	List<ULCComponent> liste = panel.checkBoxesGridLayoutPane.getComponents();
	for(ULCComponent ch : liste) {
		if(ch instanceof RCheckBox) {
			RCheckBox c = ch as RCheckBox;
			if(c.isSelected()){
				out.pressedButton = c.getText();
				break;
			}
		}
	}
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
Ms0 f18 guid 13FFECF2139C072F #txt
Ms0 f18 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f18 method startAsOptionChooser(String,String,List<String>,String) #txt
Ms0 f18 disableUIEvents true #txt
Ms0 f18 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.String text,java.lang.String iconUri,List<java.lang.String> checkBoxsList,java.lang.String defaultSelectedCheckbox> param = methodEvent.getInputArguments();
' #txt
Ms0 f18 inParameterMapAction 'out.buttons=param.checkBoxsList;
out.defaultButton=param.defaultSelectedCheckbox;
out.iconUri=param.iconUri;
out.startCheckBoxes=true;
out.text=param.text;
' #txt
Ms0 f18 outParameterDecl '<java.lang.String choosedCheckBox> result;
' #txt
Ms0 f18 outParameterMapAction 'result.choosedCheckBox=in.pressedButton;
' #txt
Ms0 f18 embeddedRdInitializations '* ' #txt
Ms0 f18 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>startAsOptionChooser(String,String,List&lt;String&gt;,String)</name>
        <nameStyle>55,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ms0 f18 214 270 20 20 13 0 #rect
Ms0 f18 @|RichDialogInitStartIcon #fIcon
Ms0 f19 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData out;
' #txt
Ms0 f19 actionTable 'out=in;
' #txt
Ms0 f19 actionCode 'import ch.ivyteam.ivy.richdialog.widgets.components.RCheckBox;
import ch.ivyteam.ivy.richdialog.widgets.components.RButton;

panel.okButton.setText(ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/ok"));

panel.getRootPane().setDefaultButton(panel.okButton);
if(!in.buttons.isEmpty()){
	panel.checkBoxesGridLayoutPane.setRows(in.buttons.size());
	panel.checkBoxesGridLayoutPane.setColumns(1);
	RCheckBox check;
	for(int i = 0; i< in.buttons.size();i++) {
		check = new RCheckBox();
		check.setText(in.buttons.get(i));
		panel.checkBoxesGridLayoutPane.add(check,i);
		check.setFont(panel.okButton.getFont());
		if((in.defaultButton== null || in.defaultButton.trim().isEmpty()) && i==0) {
			check.setSelected(true);
		}else if(check.getText().equalsIgnoreCase(in.defaultButton)) {
			check.setSelected(true);
		}
		check.setGroup(panel.getButtonGroup());
	}
	
	
}
panel.messageLabel.setText(in.text);
if(in.#iconUri!=null && in.iconUri.trim().length()>0 && 
	ivy.cms.getContentObject(in.iconUri)!=null){
		panel.iconLabel.setIconUri(in.iconUri);
}' #txt
Ms0 f19 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f19 206 324 36 24 20 -2 #rect
Ms0 f19 @|RichDialogProcessStepIcon #fIcon
Ms0 f20 expr out #txt
Ms0 f20 224 290 224 324 #arcP
Ms0 f21 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f21 214 374 20 20 13 0 #rect
Ms0 f21 @|RichDialogProcessEndIcon #fIcon
Ms0 f22 expr out #txt
Ms0 f22 224 348 224 374 #arcP
Ms0 f24 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData out;
' #txt
Ms0 f24 actionTable 'out=in;
' #txt
Ms0 f24 actionCode 'import com.ulcjava.base.application.BorderFactory;
import ch.ivyteam.ivy.richdialog.widgets.components.RButton;
panel.messageLabel.setText(in.text);
in.startButtonsOptions=true;
if(in.underButtons.isEmpty()) {
	panel.okButton.setVisible(false);
}else {
	if(in.underButtons.get(0).trim().length()>0){
		panel.okButton.setText(in.underButtons.get(0));
		panel.getRootPane().setDefaultButton(panel.okButton);
		if(!in.underButtonsIconsUris.isEmpty()) {
			panel.okButton.setIconUri(in.underButtonsIconsUris.get(0));
		}
	}
	RButton button;
	int j=1;
	for(int i=1; i<in.underButtons.size();i++){
		if(in.underButtons.get(i).trim().length()>0){
			button = new RButton();
			button.text=in.underButtons.get(i);
			panel.FlowLayoutPane.add(button,j);
			ivy.rd.addEventMapping(button, "Ok");
			button.setFont(panel.okButton.getFont());
			if(i<in.underButtonsIconsUris.size()) {
				button.setIconUri(in.underButtonsIconsUris.get(i));
			}
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
}
if(!in.buttons.isEmpty()){
	panel.checkBoxesGridLayoutPane.setRows(in.buttons.size());
	panel.checkBoxesGridLayoutPane.setColumns(1);
	RButton button;
	for(int i = 0; i< in.buttons.size();i++) {
		button = new RButton();
		button.setText(in.buttons.get(i));
		panel.checkBoxesGridLayoutPane.add(button,i);
		///insetsBottom \"10\"/insetsTop \"10\"/insetsRight \"10\"/insetsLeft \"10\"/horizontalTextPosition \"LEFT\"
		button.setStyleProperties("{/columns \""+i+"\"/fill \"HORIZONTAL\"/weightX \"1\"}");
		//button.setHorizontalTextPosition(RButton.LEFT);
		button.setHorizontalAlignment(RButton.LEFT);
		//button.setBadgeColor(com.ulcjava.base.application.util.Color.red);
		//button.setCornerRadius(20);
		button.badgeValue=0;
		//button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, com.ulcjava.base.application.util.Color.gray));
		//button.setBackground(com.ulcjava.base.application.util.Color.yellow);
		if(i<in.mainButtonsIconsUris.size()) {
			button.setIconUri(in.mainButtonsIconsUris.get(i));
		}
		ivy.rd.addEventMapping(button, "Ok");
		//button.setBorderPainted(false);
	}
}
' #txt
Ms0 f24 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f24 78 492 36 24 20 -2 #rect
Ms0 f24 @|RichDialogProcessStepIcon #fIcon
Ms0 f26 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f26 86 574 20 20 13 0 #rect
Ms0 f26 @|RichDialogProcessEndIcon #fIcon
Ms0 f23 guid 140956B974AA5471 #txt
Ms0 f23 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f23 method startAsButtonOptionChooser(String,List<String>,List<String>,List<String>,List<String>,String) #txt
Ms0 f23 disableUIEvents true #txt
Ms0 f23 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.String message,List<java.lang.String> mainButtons,List<java.lang.String> mainButtonsIconsUris,List<java.lang.String> underButtons,List<java.lang.String> underButtonsIconsUris,java.lang.String optionalCheckBox> param = methodEvent.getInputArguments();
' #txt
Ms0 f23 inParameterMapAction 'out.buttons=param.mainButtons;
out.mainButtonsIconsUris=param.mainButtonsIconsUris;
out.optionalCheckBox=param.optionalCheckBox;
out.startButtonsOptions=true;
out.startCheckBoxes=false;
out.text=param.message;
out.underButtons=param.underButtons;
out.underButtonsIconsUris=param.underButtonsIconsUris;
' #txt
Ms0 f23 outParameterDecl '<java.lang.String clickedButtonText,java.lang.Boolean optionalCheckBoxSelected> result;
' #txt
Ms0 f23 outParameterMapAction 'result.clickedButtonText=in.pressedButton;
result.optionalCheckBoxSelected=in.confirm;
' #txt
Ms0 f23 embeddedRdInitializations '* ' #txt
Ms0 f23 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>startAsButtonOptionChooser(String,List&lt;String&gt;,List&lt;String&gt;,List&lt;String&gt;,List&lt;String&gt;,String)</name>
        <nameStyle>93,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ms0 f23 86 446 20 20 13 0 #rect
Ms0 f23 @|RichDialogInitStartIcon #fIcon
Ms0 f28 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f28 guid 1409F8F7E1DFD9DB #txt
Ms0 f28 83 531 26 26 13 0 #rect
Ms0 f28 @|RichDialogUiSyncIcon #fIcon
Ms0 f29 expr out #txt
Ms0 f29 96 516 96 531 #arcP
Ms0 f27 expr out #txt
Ms0 f27 96 557 96 574 #arcP
Ms0 f25 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData out;
' #txt
Ms0 f25 actionTable 'out=in;
' #txt
Ms0 f25 actionCode 'import com.ulcjava.base.application.util.Cursor;
import ch.ivyteam.ivy.richdialog.widgets.containers.RBoxPane;
import com.ulcjava.base.application.BorderFactory;
import ch.ivyteam.ivy.richdialog.widgets.components.RButton;

panel.messageLabel.setText(in.text);
in.startButtonsOptions=true;
if(in.underButtons.isEmpty()) {
	panel.okButton.setVisible(false);
}else {
	if(in.underButtons.get(0).trim().length()>0){
		panel.okButton.setText(in.underButtons.get(0));
		panel.getRootPane().setDefaultButton(panel.okButton);
		if(!in.underButtonsIconsUris.isEmpty()) {
			panel.okButton.setIconUri(in.underButtonsIconsUris.get(0));
		}
	}
	RButton button;
	int j=1;
	for(int i=1; i<in.underButtons.size();i++){
		if(in.underButtons.get(i).trim().length()>0){
			button = new RButton();
			button.text=in.underButtons.get(i);
			panel.FlowLayoutPane.add(button,j);
			ivy.rd.addEventMapping(button, "Ok");
			button.setFont(panel.okButton.getFont());
			if(i<in.underButtonsIconsUris.size()) {
				button.setIconUri(in.underButtonsIconsUris.get(i));
			}
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
}
if(!in.buttons.isEmpty()){
	panel.checkBoxesGridLayoutPane.setRows(in.buttons.size());
	panel.checkBoxesGridLayoutPane.setColumns(1);
	RBoxPane box;
	RButton button;
	for(int i = 0; i< in.buttons.size();i++) {
		box = new RBoxPane();
		button = new RButton();
		button.setText(in.buttons.get(i));
		panel.checkBoxesGridLayoutPane.setVgap(1);
		panel.checkBoxesGridLayoutPane.add(box,i);
		//box.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, com.ulcjava.base.application.util.Color.lightGray));
		box.setCornerRadius(15);
		box.setBackground(new com.ulcjava.base.application.util.Color(240,240,240));
		box.setMargin(1);

		box.setTranslucency(0.9);
		box.setCursor(Cursor.HAND_CURSOR);
		box.add(1,button);
		
		///insetsBottom \"10\"/insetsTop \"10\"/insetsRight \"10\"/insetsLeft \"10\"/horizontalTextPosition \"LEFT\"
		button.setStyleProperties("{/columns \""+i+"\"/fill \"HORIZONTAL\"/weightX \"1\"}");
		button.setHorizontalAlignment(RButton.LEFT);
		button.setOpaque(true);
		button.setTranslucency(1);
		button.badgeValue=0;
		if(i<in.mainButtonsIconsUris.size()) {
			button.setIconUri(in.mainButtonsIconsUris.get(i));
		}
		ivy.rd.addEventMapping(button, "Ok");
		button.setBorderPainted(false);
	}
}
' #txt
Ms0 f25 type ch.ivyteam.ivy.addons.filemanager.util.MessageDialog.MessageDialogData #txt
Ms0 f25 222 508 36 24 20 -2 #rect
Ms0 f25 @|RichDialogProcessStepIcon #fIcon
Ms0 f30 expr out #txt
Ms0 f30 105 460 222 512 #arcP
Ms0 f31 expr out #txt
Ms0 f31 222 528 105 579 #arcP
>Proto Ms0 .rdData2UIAction 'panel.optionalCheckBox.text=in.optionalCheckBox;
panel.optionalCheckBox.visible=!in.optionalCheckBox.isEmpty();
panel.checkBoxesGridLayoutPane.visible=in.startCheckBoxes || in.startButtonsOptions;
' #txt
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
Ms0 f18 mainOut f20 tail #connect
Ms0 f20 head f19 mainIn #connect
Ms0 f19 mainOut f22 tail #connect
Ms0 f22 head f21 mainIn #connect
Ms0 f24 mainOut f29 tail #connect
Ms0 f29 head f28 mainIn #connect
Ms0 f28 mainOut f27 tail #connect
Ms0 f27 head f26 mainIn #connect
Ms0 f23 mainOut f30 tail #connect
Ms0 f30 head f25 mainIn #connect
Ms0 f25 mainOut f31 tail #connect
Ms0 f31 head f26 mainIn #connect
