[Ivy]
[>Created: Thu Feb 20 14:25:06 EST 2014]
1406387DE1127837 3.17 #module
>Proto >Proto Collection #zClass
Fs0 ItemTranslationAdminProcess Big #zClass
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
Fs0 @RichDialogProcessStart f3 '' #zField
Fs0 @RichDialogEnd f4 '' #zField
Fs0 @RichDialogProcessStart f6 '' #zField
Fs0 @RichDialogProcessStep f7 '' #zField
Fs0 @PushWFArc f8 '' #zField
Fs0 @RichDialogEnd f9 '' #zField
Fs0 @RichDialogProcessStep f12 '' #zField
Fs0 @Alternative f16 '' #zField
Fs0 @RichDialog f18 '' #zField
Fs0 @PushWFArc f19 '' #zField
Fs0 @RichDialogProcessStep f20 '' #zField
Fs0 @PushWFArc f21 '' #zField
Fs0 @PushWFArc f22 '' #zField
Fs0 @RichDialogProcessEnd f23 '' #zField
Fs0 @PushWFArc f24 '' #zField
Fs0 @RichDialogProcessStep f25 '' #zField
Fs0 @PushWFArc f26 '' #zField
Fs0 @PushWFArc f10 '' #zField
Fs0 @PushWFArc f27 '' #zField
Fs0 @RichDialogInitStart f14 '' #zField
Fs0 @PushWFArc f15 '' #zField
Fs0 @PushWFArc f5 '' #zField
>Proto Fs0 Fs0 ItemTranslationAdminProcess #zField
Fs0 f0 guid 1406387DE3C9853B #txt
Fs0 f0 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData #txt
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
Fs0 f1 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData #txt
Fs0 f1 86 150 20 20 13 0 #rect
Fs0 f1 @|RichDialogProcessEndIcon #fIcon
Fs0 f2 expr out #txt
Fs0 f2 96 74 96 150 #arcP
Fs0 f3 guid 140638D6542DC7CE #txt
Fs0 f3 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData #txt
Fs0 f3 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData out;
' #txt
Fs0 f3 actionTable 'out=in;
out.itemTranslation=in.inputItemTranslation;
' #txt
Fs0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>close</name>
        <nameStyle>5,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f3 342 54 20 20 13 0 #rect
Fs0 f3 @|RichDialogProcessStartIcon #fIcon
Fs0 f4 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData #txt
Fs0 f4 guid 140638D9425ED2CF #txt
Fs0 f4 342 158 20 20 13 0 #rect
Fs0 f4 @|RichDialogEndIcon #fIcon
Fs0 f6 guid 140638DABA6598E7 #txt
Fs0 f6 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData #txt
Fs0 f6 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData out;
' #txt
Fs0 f6 actionTable 'out=in;
' #txt
Fs0 f6 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>save</name>
        <nameStyle>4,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f6 470 54 20 20 13 0 #rect
Fs0 f6 @|RichDialogProcessStartIcon #fIcon
Fs0 f7 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData out;
' #txt
Fs0 f7 actionTable 'out=in;
' #txt
Fs0 f7 actionCode 'import ch.ivyteam.ivy.richdialog.exec.panel.IRichDialogPanel;
import java.util.HashMap;
import ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit.ItemTranslationUnitPanel;
import com.ulcjava.base.application.ULCComponent;

List<IRichDialogPanel> ulcs =panel.ListDisplay.getPanels();

HashMap map = new HashMap();
for(IRichDialogPanel comp:ulcs) {
	ivy.log.debug(comp.toString()+" "+(comp instanceof ItemTranslationUnitPanel));
	if(comp instanceof ItemTranslationUnitPanel) {
		ItemTranslationUnitPanel transp = comp as ItemTranslationUnitPanel;
		map.put(transp.getLanguage(),transp.getTranslation());
	}
}

out.itemTranslation.translations = map;
' #txt
Fs0 f7 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData #txt
Fs0 f7 462 108 36 24 20 -2 #rect
Fs0 f7 @|RichDialogProcessStepIcon #fIcon
Fs0 f8 expr out #txt
Fs0 f8 480 74 480 108 #arcP
Fs0 f9 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData #txt
Fs0 f9 guid 140638DEBFEE0837 #txt
Fs0 f9 470 222 20 20 13 0 #rect
Fs0 f9 @|RichDialogEndIcon #fIcon
Fs0 f12 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData out;
' #txt
Fs0 f12 actionTable 'out=in;
' #txt
Fs0 f12 actionCode 'import ch.ivyteam.ivy.addons.filemanager.database.PersistenceConnectionManagerFactory;
out.translationPersistence = PersistenceConnectionManagerFactory.getIItemTranslationPersistenceInstance(in.config,in.itemType);

in.displayId=System.nanoTime().toString();
panel.ListDisplay.setDisplayId(in.displayId);' #txt
Fs0 f12 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData #txt
Fs0 f12 78 308 36 24 20 -2 #rect
Fs0 f12 @|RichDialogProcessStepIcon #fIcon
Fs0 f16 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData #txt
Fs0 f16 82 354 28 28 14 0 #rect
Fs0 f16 @|AlternativeIcon #fIcon
Fs0 f18 targetWindow THIS #txt
Fs0 f18 targetDisplay EXISTING:<%=in.displayId%> #txt
Fs0 f18 richDialogId ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationUnit #txt
Fs0 f18 startMethod start(String,String) #txt
Fs0 f18 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData #txt
Fs0 f18 requestActionDecl '<String language, String translation> param;' #txt
Fs0 f18 requestActionCode 'String key = in.itemTranslation.translations.keySet().toArray().get(in.counter).toString();
param.language =key;
param.translation = in.itemTranslation.translations.get(key).toString();' #txt
Fs0 f18 responseActionDecl 'ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData out;
' #txt
Fs0 f18 responseMappingAction 'out=in;
' #txt
Fs0 f18 windowConfiguration '* ' #txt
Fs0 f18 isAsynch true #txt
Fs0 f18 isInnerRd true #txt
Fs0 f18 userContext '* ' #txt
Fs0 f18 198 356 36 24 20 -2 #rect
Fs0 f18 @|RichDialogIcon #fIcon
Fs0 f19 expr in #txt
Fs0 f19 outCond in.counter<in.itemTranslation.translations.size() #txt
Fs0 f19 110 368 198 368 #arcP
Fs0 f20 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData out;
' #txt
Fs0 f20 actionTable 'out=in;
' #txt
Fs0 f20 actionCode 'out.counter= in.counter+1;' #txt
Fs0 f20 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData #txt
Fs0 f20 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>counter++</name>
        <nameStyle>9,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f20 198 428 36 24 20 -2 #rect
Fs0 f20 @|RichDialogProcessStepIcon #fIcon
Fs0 f21 expr out #txt
Fs0 f21 216 380 216 428 #arcP
Fs0 f22 expr out #txt
Fs0 f22 198 429 105 373 #arcP
Fs0 f23 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData #txt
Fs0 f23 86 470 20 20 13 0 #rect
Fs0 f23 @|RichDialogProcessEndIcon #fIcon
Fs0 f24 expr in #txt
Fs0 f24 96 382 96 470 #arcP
Fs0 f25 actionDecl 'ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData out;
' #txt
Fs0 f25 actionTable 'out=in;
' #txt
Fs0 f25 actionCode 'in.translationPersistence.update(in.itemTranslation);
out.inputItemTranslation = in.itemTranslation;' #txt
Fs0 f25 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData #txt
Fs0 f25 462 156 36 24 20 -2 #rect
Fs0 f25 @|RichDialogProcessStepIcon #fIcon
Fs0 f26 expr out #txt
Fs0 f26 480 132 480 156 #arcP
Fs0 f10 expr out #txt
Fs0 f10 480 180 480 222 #arcP
Fs0 f27 expr out #txt
Fs0 f27 96 332 96 354 #arcP
Fs0 f14 guid 1406B10646D58C3B #txt
Fs0 f14 type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData #txt
Fs0 f14 method start(ch.ivyteam.ivy.addons.filemanager.ItemTranslation,ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController,ch.ivyteam.ivy.addons.filemanager.database.persistence.TranslatedFileManagerItemsEnum,String) #txt
Fs0 f14 disableUIEvents true #txt
Fs0 f14 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.filemanager.ItemTranslation inputItemTranslation,ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController configuration,ch.ivyteam.ivy.addons.filemanager.database.persistence.TranslatedFileManagerItemsEnum itemType,java.lang.String itemMainName> param = methodEvent.getInputArguments();
' #txt
Fs0 f14 inParameterMapAction 'out.config=param.configuration;
out.inputItemTranslation=param.inputItemTranslation;
out.itemMainName=param.itemMainName;
out.itemType=param.itemType;
' #txt
Fs0 f14 inActionCode 'import ch.ivyteam.ivy.addons.filemanager.ItemTranslation;

out.itemTranslation = new ItemTranslation(param.inputItemTranslation.translatedItemId,param.inputItemTranslation.translations);' #txt
Fs0 f14 outParameterDecl '<ch.ivyteam.ivy.addons.filemanager.ItemTranslation outputItemTranslation> result;
' #txt
Fs0 f14 outParameterMapAction 'result.outputItemTranslation=in.itemTranslation;
' #txt
Fs0 f14 embeddedRdInitializations '* ' #txt
Fs0 f14 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(ItemTranslation,BasicConfigurationController,TranslatedFileManagerItemsEnum,String)</name>
        <nameStyle>89,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f14 86 262 20 20 13 0 #rect
Fs0 f14 @|RichDialogInitStartIcon #fIcon
Fs0 f15 expr out #txt
Fs0 f15 96 282 96 308 #arcP
Fs0 f5 expr out #txt
Fs0 f5 352 74 352 158 #arcP
>Proto Fs0 .rdData2UIAction 'panel.foldernameLabel.text=in.itemMainName;
' #txt
>Proto Fs0 .type ch.ivyteam.ivy.addons.filemanager.util.ItemTranslationAdmin.ItemTranslationAdminData #txt
>Proto Fs0 .processKind RICH_DIALOG #txt
>Proto Fs0 -8 -8 16 16 16 26 #rect
>Proto Fs0 '' #fIcon
Fs0 f0 mainOut f2 tail #connect
Fs0 f2 head f1 mainIn #connect
Fs0 f6 mainOut f8 tail #connect
Fs0 f8 head f7 mainIn #connect
Fs0 f16 out f19 tail #connect
Fs0 f19 head f18 mainIn #connect
Fs0 f18 mainOut f21 tail #connect
Fs0 f21 head f20 mainIn #connect
Fs0 f20 mainOut f22 tail #connect
Fs0 f22 head f16 in #connect
Fs0 f16 out f24 tail #connect
Fs0 f24 head f23 mainIn #connect
Fs0 f7 mainOut f26 tail #connect
Fs0 f26 head f25 mainIn #connect
Fs0 f25 mainOut f10 tail #connect
Fs0 f10 head f9 mainIn #connect
Fs0 f12 mainOut f27 tail #connect
Fs0 f27 head f16 in #connect
Fs0 f14 mainOut f15 tail #connect
Fs0 f15 head f12 mainIn #connect
Fs0 f3 mainOut f5 tail #connect
Fs0 f5 head f4 mainIn #connect
