[Ivy]
[>Created: Fri Aug 09 11:11:29 EDT 2013]
140639A3B002179A 3.17 #module
>Proto >Proto Collection #zClass
Fs0 ItemTranslationUnitProcess Big #zClass
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
Fs0 @RichDialogMethodStart f4 '' #zField
Fs0 @RichDialogMethodStart f5 '' #zField
Fs0 @RichDialogMethodStart f6 '' #zField
Fs0 @RichDialogMethodStart f7 '' #zField
Fs0 @RichDialogProcessEnd f8 '' #zField
Fs0 @PushWFArc f9 '' #zField
Fs0 @RichDialogProcessEnd f10 '' #zField
Fs0 @PushWFArc f11 '' #zField
Fs0 @RichDialogProcessEnd f12 '' #zField
Fs0 @PushWFArc f13 '' #zField
Fs0 @RichDialogProcessEnd f14 '' #zField
Fs0 @PushWFArc f15 '' #zField
Fs0 @RichDialogProcessEnd f16 '' #zField
Fs0 @PushWFArc f17 '' #zField
>Proto Fs0 Fs0 ItemTranslationUnitProcess #zField
Fs0 f0 guid 140639A3B2FD1152 #txt
Fs0 f0 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit.ItemTranslationUnitData #txt
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
Fs0 f1 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit.ItemTranslationUnitData #txt
Fs0 f1 86 150 20 20 13 0 #rect
Fs0 f1 @|RichDialogProcessEndIcon #fIcon
Fs0 f2 expr out #txt
Fs0 f2 96 74 96 150 #arcP
Fs0 f3 guid 140639C9186B2E2C #txt
Fs0 f3 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit.ItemTranslationUnitData #txt
Fs0 f3 method start(String,String) #txt
Fs0 f3 disableUIEvents true #txt
Fs0 f3 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.String language,java.lang.String translation> param = methodEvent.getInputArguments();
' #txt
Fs0 f3 inParameterMapAction 'out.language=param.language;
out.translation=param.translation;
' #txt
Fs0 f3 outParameterDecl '<> result;
' #txt
Fs0 f3 embeddedRdInitializations '* ' #txt
Fs0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(String,String)</name>
        <nameStyle>20,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f3 86 214 20 20 13 0 #rect
Fs0 f3 @|RichDialogInitStartIcon #fIcon
Fs0 f4 guid 140639C9D4EB8284 #txt
Fs0 f4 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit.ItemTranslationUnitData #txt
Fs0 f4 method setLanguage(String) #txt
Fs0 f4 disableUIEvents false #txt
Fs0 f4 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.String language> param = methodEvent.getInputArguments();
' #txt
Fs0 f4 inParameterMapAction 'out.language=param.language;
' #txt
Fs0 f4 outParameterDecl '<> result;
' #txt
Fs0 f4 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>setLanguage(String)</name>
        <nameStyle>19,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f4 374 46 20 20 13 0 #rect
Fs0 f4 @|RichDialogMethodStartIcon #fIcon
Fs0 f5 guid 140639CA6CF6E6F5 #txt
Fs0 f5 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit.ItemTranslationUnitData #txt
Fs0 f5 method getLanguage() #txt
Fs0 f5 disableUIEvents false #txt
Fs0 f5 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<> param = methodEvent.getInputArguments();
' #txt
Fs0 f5 outParameterDecl '<java.lang.String language> result;
' #txt
Fs0 f5 outParameterMapAction 'result.language=in.language.trim();
' #txt
Fs0 f5 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>getLanguage()</name>
        <nameStyle>13,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f5 574 54 20 20 13 0 #rect
Fs0 f5 @|RichDialogMethodStartIcon #fIcon
Fs0 f6 guid 140639CB29FD4422 #txt
Fs0 f6 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit.ItemTranslationUnitData #txt
Fs0 f6 method setTranslation(String) #txt
Fs0 f6 disableUIEvents false #txt
Fs0 f6 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.String translation> param = methodEvent.getInputArguments();
' #txt
Fs0 f6 inParameterMapAction 'out.translation=param.translation;
' #txt
Fs0 f6 outParameterDecl '<> result;
' #txt
Fs0 f6 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>setTranslation(String)</name>
        <nameStyle>22,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f6 382 158 20 20 13 0 #rect
Fs0 f6 @|RichDialogMethodStartIcon #fIcon
Fs0 f7 guid 140639CB9568104F #txt
Fs0 f7 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit.ItemTranslationUnitData #txt
Fs0 f7 method getTranslation() #txt
Fs0 f7 disableUIEvents false #txt
Fs0 f7 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<> param = methodEvent.getInputArguments();
' #txt
Fs0 f7 outParameterDecl '<java.lang.String translation> result;
' #txt
Fs0 f7 outParameterMapAction 'result.translation=in.translation.trim();
' #txt
Fs0 f7 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>getTranslation()</name>
        <nameStyle>16,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f7 574 158 20 20 13 0 #rect
Fs0 f7 @|RichDialogMethodStartIcon #fIcon
Fs0 f8 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit.ItemTranslationUnitData #txt
Fs0 f8 374 94 20 20 13 0 #rect
Fs0 f8 @|RichDialogProcessEndIcon #fIcon
Fs0 f9 expr out #txt
Fs0 f9 384 66 384 94 #arcP
Fs0 f10 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit.ItemTranslationUnitData #txt
Fs0 f10 382 214 20 20 13 0 #rect
Fs0 f10 @|RichDialogProcessEndIcon #fIcon
Fs0 f11 expr out #txt
Fs0 f11 392 178 392 214 #arcP
Fs0 f12 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit.ItemTranslationUnitData #txt
Fs0 f12 574 214 20 20 13 0 #rect
Fs0 f12 @|RichDialogProcessEndIcon #fIcon
Fs0 f13 expr out #txt
Fs0 f13 584 178 584 214 #arcP
Fs0 f14 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit.ItemTranslationUnitData #txt
Fs0 f14 574 110 20 20 13 0 #rect
Fs0 f14 @|RichDialogProcessEndIcon #fIcon
Fs0 f15 expr out #txt
Fs0 f15 584 74 584 110 #arcP
Fs0 f16 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit.ItemTranslationUnitData #txt
Fs0 f16 86 270 20 20 13 0 #rect
Fs0 f16 @|RichDialogProcessEndIcon #fIcon
Fs0 f17 expr out #txt
Fs0 f17 96 234 96 270 #arcP
>Proto Fs0 .ui2RdDataAction 'out.language=panel.langLabel.text;
out.translation=panel.translationTextField.text;
' #txt
>Proto Fs0 .rdData2UIAction 'panel.langLabel.text=in.language;
panel.translationTextField.text=in.translation;
' #txt
>Proto Fs0 .type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit.ItemTranslationUnitData #txt
>Proto Fs0 .processKind RICH_DIALOG #txt
>Proto Fs0 -8 -8 16 16 16 26 #rect
>Proto Fs0 '' #fIcon
Fs0 f0 mainOut f2 tail #connect
Fs0 f2 head f1 mainIn #connect
Fs0 f4 mainOut f9 tail #connect
Fs0 f9 head f8 mainIn #connect
Fs0 f6 mainOut f11 tail #connect
Fs0 f11 head f10 mainIn #connect
Fs0 f7 mainOut f13 tail #connect
Fs0 f13 head f12 mainIn #connect
Fs0 f5 mainOut f15 tail #connect
Fs0 f15 head f14 mainIn #connect
Fs0 f3 mainOut f17 tail #connect
Fs0 f17 head f16 mainIn #connect
