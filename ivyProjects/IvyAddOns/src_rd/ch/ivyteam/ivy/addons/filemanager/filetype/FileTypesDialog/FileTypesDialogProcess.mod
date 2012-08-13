[Ivy]
[>Created: Wed Jun 20 12:30:52 EDT 2012]
137EFFFAE6078CBB 3.17 #module
>Proto >Proto Collection #zClass
Fs0 FileTypesDialogProcess Big #zClass
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
Fs0 @RichDialogInitStart f3 '' #zField
Fs0 @RichDialogInitStart f4 '' #zField
Fs0 @RichDialogProcessStep f5 '' #zField
Fs0 @RichDialogProcessEnd f7 '' #zField
Fs0 @RichDialogProcessStart f2 '' #zField
Fs0 @RichDialogProcessStart f9 '' #zField
Fs0 @RichDialogProcessStart f10 '' #zField
Fs0 @RichDialogEnd f11 '' #zField
Fs0 @PushWFArc f12 '' #zField
Fs0 @RichDialog f14 '' #zField
Fs0 @PushWFArc f15 '' #zField
Fs0 @RichDialog f16 '' #zField
Fs0 @PushWFArc f17 '' #zField
Fs0 @Alternative f18 '' #zField
Fs0 @Alternative f19 '' #zField
Fs0 @PushWFArc f20 '' #zField
Fs0 @PushWFArc f21 '' #zField
Fs0 @RichDialogProcessStep f22 '' #zField
Fs0 @RichDialogProcessStep f23 '' #zField
Fs0 @PushWFArc f24 '' #zField
Fs0 @PushWFArc f25 '' #zField
Fs0 @RichDialogProcessStart f26 '' #zField
Fs0 @RichDialogEnd f27 '' #zField
Fs0 @PushWFArc f28 '' #zField
Fs0 @RichDialogProcessEnd f33 '' #zField
Fs0 @RichDialogProcessEnd f35 '' #zField
Fs0 @PushWFArc f37 '' #zField
Fs0 @PushWFArc f38 '' #zField
Fs0 @RichDialogProcessStep f31 '' #zField
Fs0 @PushWFArc f32 '' #zField
Fs0 @PushWFArc f6 '' #zField
Fs0 @PushWFArc f34 '' #zField
Fs0 @RichDialogProcessStep f13 '' #zField
Fs0 @PushWFArc f36 '' #zField
Fs0 @PushWFArc f1 '' #zField
Fs0 @RichDialogProcessStart f39 '' #zField
Fs0 @RichDialogProcessEnd f40 '' #zField
Fs0 @PushWFArc f41 '' #zField
Fs0 @RichDialogMethodStart f42 '' #zField
Fs0 @RichDialog f43 '' #zField
Fs0 @PushWFArc f44 '' #zField
Fs0 @RichDialogProcessEnd f45 '' #zField
Fs0 @PushWFArc f46 '' #zField
Fs0 @RichDialogProcessStep f47 '' #zField
Fs0 @PushWFArc f48 '' #zField
Fs0 @PushWFArc f8 '' #zField
Fs0 @RichDialogProcessStep f49 '' #zField
Fs0 @PushWFArc f50 '' #zField
Fs0 @PushWFArc f29 '' #zField
Fs0 @RichDialogProcessStep f51 '' #zField
Fs0 @PushWFArc f52 '' #zField
Fs0 @PushWFArc f30 '' #zField
Fs0 @RichDialogProcessStart f53 '' #zField
Fs0 @RichDialog f54 '' #zField
Fs0 @PushWFArc f55 '' #zField
Fs0 @Alternative f56 '' #zField
Fs0 @PushWFArc f57 '' #zField
Fs0 @RichDialogProcessEnd f58 '' #zField
Fs0 @PushWFArc f59 '' #zField
Fs0 @RichDialogProcessStep f60 '' #zField
Fs0 @RichDialogProcessStep f61 '' #zField
Fs0 @PushWFArc f62 '' #zField
Fs0 @PushWFArc f63 '' #zField
Fs0 @PushWFArc f64 '' #zField
>Proto Fs0 Fs0 FileTypesDialogProcess #zField
Fs0 f0 guid 137EFFFAE7CFDE05 #txt
Fs0 f0 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
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
Fs0 f3 guid 137F00D5DBEF6F41 #txt
Fs0 f3 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f3 method startAsFileTypeChooser(ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController) #txt
Fs0 f3 disableUIEvents true #txt
Fs0 f3 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController configurationController> param = methodEvent.getInputArguments();
' #txt
Fs0 f3 inParameterMapAction 'out.actionCancelled=true;
out.configurationController=param.configurationController;
' #txt
Fs0 f3 outParameterDecl '<ch.ivyteam.ivy.addons.filemanager.FileType choosedFileType,java.lang.Boolean actionCancelled> result;
' #txt
Fs0 f3 outParameterMapAction 'result.choosedFileType=in.choosedFileType;
result.actionCancelled=in.actionCancelled;
' #txt
Fs0 f3 embeddedRdInitializations '* ' #txt
Fs0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>startAsFileTypeChooser(BasicConfigurationController)</name>
        <nameStyle>52,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f3 182 54 20 20 13 0 #rect
Fs0 f3 @|RichDialogInitStartIcon #fIcon
Fs0 f4 guid 137F01260965268E #txt
Fs0 f4 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f4 method startAsFileTypeManager(ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController) #txt
Fs0 f4 disableUIEvents true #txt
Fs0 f4 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController configurationController> param = methodEvent.getInputArguments();
' #txt
Fs0 f4 inParameterMapAction 'out.configurationController=param.configurationController;
' #txt
Fs0 f4 outParameterDecl '<> result;
' #txt
Fs0 f4 embeddedRdInitializations '* ' #txt
Fs0 f4 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>startAsFileTypeManager(BasicConfigurationController)</name>
        <nameStyle>52,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f4 262 94 20 20 13 0 #rect
Fs0 f4 @|RichDialogInitStartIcon #fIcon
Fs0 f5 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f5 actionTable 'out=in;
' #txt
Fs0 f5 actionCode 'import ch.ivyteam.ivy.addons.filemanager.database.filetype.FileTypesController;
try{
	in.fileTypesController = new FileTypesController(in.configurationController);
	out.fileTypesList = in.fileTypesController.getAllFileTypes();
}catch(Throwable t)
{
	out.errorUtil.errorOccurred=true;
	out.errorUtil.throwable=t;
	out.errorUtil.message="Error: "+t.getMessage();
}' #txt
Fs0 f5 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f5 174 164 36 24 20 -2 #rect
Fs0 f5 @|RichDialogProcessStepIcon #fIcon
Fs0 f7 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f7 182 262 20 20 13 0 #rect
Fs0 f7 @|RichDialogProcessEndIcon #fIcon
Fs0 f2 guid 137F051D6ACA7C4E #txt
Fs0 f2 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f2 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f2 actionTable 'out=in;
out.errorUtil.errorOccurred=false;
' #txt
Fs0 f2 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>new</name>
        <nameStyle>3,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f2 630 62 20 20 13 0 #rect
Fs0 f2 @|RichDialogProcessStartIcon #fIcon
Fs0 f9 guid 137F051F555AD2DA #txt
Fs0 f9 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f9 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f9 actionTable 'out=in;
out.errorUtil.errorOccurred=false;
' #txt
Fs0 f9 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>modify</name>
        <nameStyle>6,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f9 726 62 20 20 13 0 #rect
Fs0 f9 @|RichDialogProcessStartIcon #fIcon
Fs0 f10 guid 137F0522C6F6791B #txt
Fs0 f10 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f10 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f10 actionTable 'out=in;
out.actionCancelled=true;
' #txt
Fs0 f10 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>cancel</name>
        <nameStyle>6,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f10 878 70 20 20 13 0 #rect
Fs0 f10 @|RichDialogProcessStartIcon #fIcon
Fs0 f11 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f11 guid 137F0527DCF376A9 #txt
Fs0 f11 878 134 20 20 13 0 #rect
Fs0 f11 @|RichDialogEndIcon #fIcon
Fs0 f12 expr out #txt
Fs0 f12 888 90 888 134 #arcP
Fs0 f14 targetWindow NEW:card: #txt
Fs0 f14 targetDisplay TOP #txt
Fs0 f14 richDialogId ch.ivyteam.ivy.addons.filemanager.filetype.FileTypeWriter #txt
Fs0 f14 startMethod start(ch.ivyteam.ivy.addons.filemanager.FileType) #txt
Fs0 f14 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f14 requestActionDecl '<ch.ivyteam.ivy.addons.filemanager.FileType fileType> param;' #txt
Fs0 f14 requestMappingAction 'param.fileType.applicationName="";
param.fileType.fileTypeName="";
param.fileType.id=0;
' #txt
Fs0 f14 responseActionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f14 responseMappingAction 'out=in;
out.editedFileType=result.fileType;
out.modCancelled=result.actionCancelled;
' #txt
Fs0 f14 windowConfiguration '{/title "<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileTypes/strings/newFileType\")%>"/width 0 /height 0 /centered true /resizable true /maximized false /close_after_last_rd true }' #txt
Fs0 f14 isAsynch false #txt
Fs0 f14 isInnerRd true #txt
Fs0 f14 userContext '* ' #txt
Fs0 f14 622 116 36 24 20 -2 #rect
Fs0 f14 @|RichDialogIcon #fIcon
Fs0 f15 expr out #txt
Fs0 f15 640 82 640 116 #arcP
Fs0 f16 targetWindow NEW:card: #txt
Fs0 f16 targetDisplay TOP #txt
Fs0 f16 richDialogId ch.ivyteam.ivy.addons.filemanager.filetype.FileTypeWriter #txt
Fs0 f16 startMethod start(ch.ivyteam.ivy.addons.filemanager.FileType) #txt
Fs0 f16 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f16 requestActionDecl '<ch.ivyteam.ivy.addons.filemanager.FileType fileType> param;' #txt
Fs0 f16 requestMappingAction 'param.fileType=in.choosedFileType.clone();
' #txt
Fs0 f16 responseActionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f16 responseMappingAction 'out=in;
out.editedFileType=result.fileType;
out.modCancelled=result.actionCancelled;
' #txt
Fs0 f16 windowConfiguration '{/title "<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileTypes/strings/modifyFileType\")%>"/width 0 /height 0 /centered true /resizable true /maximized false /close_after_last_rd true }' #txt
Fs0 f16 isAsynch false #txt
Fs0 f16 isInnerRd true #txt
Fs0 f16 userContext '* ' #txt
Fs0 f16 718 116 36 24 20 -2 #rect
Fs0 f16 @|RichDialogIcon #fIcon
Fs0 f17 expr out #txt
Fs0 f17 736 82 736 116 #arcP
Fs0 f18 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f18 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>cancelled?</name>
        <nameStyle>10
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f18 626 170 28 28 14 0 #rect
Fs0 f18 @|AlternativeIcon #fIcon
Fs0 f19 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f19 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>cancelled?</name>
        <nameStyle>10
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f19 722 170 28 28 14 0 #rect
Fs0 f19 @|AlternativeIcon #fIcon
Fs0 f20 expr out #txt
Fs0 f20 736 140 736 170 #arcP
Fs0 f21 expr out #txt
Fs0 f21 640 140 640 170 #arcP
Fs0 f22 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f22 actionTable 'out=in;
' #txt
Fs0 f22 actionCode 'import ch.ivyteam.ivy.addons.filemanager.database.filetype.FileTypesController;
try{
	in.fileTypesController.createFileType(in.editedFileType.fileTypeName,in.editedFileType.applicationName);
	in.fileTypesList.clear();
	out.fileTypesList = in.fileTypesController.getAllFileTypes();
	out.choosedFileType=null;
}catch(Throwable t)
{
	out.errorUtil.errorOccurred=true;
	out.errorUtil.throwable=t;
	out.errorUtil.message="Error: "+t.getMessage();
}' #txt
Fs0 f22 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f22 622 236 36 24 20 -2 #rect
Fs0 f22 @|RichDialogProcessStepIcon #fIcon
Fs0 f23 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f23 actionTable 'out=in;
' #txt
Fs0 f23 actionCode 'import ch.ivyteam.ivy.addons.filemanager.database.filetype.FileTypesController;
try{
	in.fileTypesController.modifyFileType(in.editedFileType);
	in.fileTypesList.clear();
	out.fileTypesList = in.fileTypesController.getAllFileTypes();
	out.choosedFileType = null;
}catch(Throwable t)
{
	out.errorUtil.errorOccurred=true;
	out.errorUtil.throwable=t;
	out.errorUtil.message="Error: "+t.getMessage();
}' #txt
Fs0 f23 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f23 718 236 36 24 20 -2 #rect
Fs0 f23 @|RichDialogProcessStepIcon #fIcon
Fs0 f24 expr in #txt
Fs0 f24 outCond !in.modCancelled #txt
Fs0 f24 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>No</name>
        <nameStyle>2
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f24 736 198 736 236 #arcP
Fs0 f25 expr in #txt
Fs0 f25 outCond !in.modCancelled #txt
Fs0 f25 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>No</name>
        <nameStyle>2
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f25 640 198 640 236 #arcP
Fs0 f26 guid 137F06E01966213C #txt
Fs0 f26 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f26 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f26 actionTable 'out=in;
out.actionCancelled=false;
' #txt
Fs0 f26 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>select</name>
        <nameStyle>6,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f26 878 222 20 20 13 0 #rect
Fs0 f26 @|RichDialogProcessStartIcon #fIcon
Fs0 f27 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f27 guid 137F06E2B9466FE2 #txt
Fs0 f27 878 278 20 20 13 0 #rect
Fs0 f27 @|RichDialogEndIcon #fIcon
Fs0 f28 expr out #txt
Fs0 f28 888 242 888 278 #arcP
Fs0 f33 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f33 630 318 20 20 13 0 #rect
Fs0 f33 @|RichDialogProcessEndIcon #fIcon
Fs0 f35 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f35 726 318 20 20 13 0 #rect
Fs0 f35 @|RichDialogProcessEndIcon #fIcon
Fs0 f37 expr in #txt
Fs0 f37 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>YES</name>
        <nameStyle>3
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f37 626 184 630 328 #arcP
Fs0 f37 1 560 184 #addKink
Fs0 f37 2 560 328 #addKink
Fs0 f37 1 0.4079241148199974 0 0 #arcLabel
Fs0 f38 expr in #txt
Fs0 f38 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>YES</name>
        <nameStyle>3
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f38 750 184 746 328 #arcP
Fs0 f38 1 800 184 #addKink
Fs0 f38 2 800 328 #addKink
Fs0 f38 1 0.43193289380624156 0 0 #arcLabel
Fs0 f31 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f31 actionTable 'out=in;
' #txt
Fs0 f31 panelTable 'panel.chooserFlowLayoutPane.visible=true;
panel.managerFlowLayoutPane.visible=false;
' #txt
Fs0 f31 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f31 174 100 36 24 20 -2 #rect
Fs0 f31 @|RichDialogProcessStepIcon #fIcon
Fs0 f32 expr out #txt
Fs0 f32 192 74 192 100 #arcP
Fs0 f6 expr out #txt
Fs0 f6 192 124 192 164 #arcP
Fs0 f34 expr out #txt
Fs0 f34 96 74 174 112 #arcP
Fs0 f34 1 96 112 #addKink
Fs0 f34 1 0.17176576927455847 0 0 #arcLabel
Fs0 f13 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f13 actionTable 'out=in;
' #txt
Fs0 f13 panelTable 'panel.chooserFlowLayoutPane.visible=false;
panel.managerFlowLayoutPane.visible=true;
' #txt
Fs0 f13 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f13 254 164 36 24 20 -2 #rect
Fs0 f13 @|RichDialogProcessStepIcon #fIcon
Fs0 f36 expr out #txt
Fs0 f36 272 114 272 164 #arcP
Fs0 f1 expr out #txt
Fs0 f1 254 176 210 176 #arcP
Fs0 f39 guid 137F0A2DEC8265D2 #txt
Fs0 f39 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f39 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f39 actionTable 'out=in;
' #txt
Fs0 f39 actionCode 'import ch.ivyteam.ivy.addons.filemanager.FileType;

if(panel.#fileTypesTable.#selectedListEntry!=null)
{
	out.choosedFileType = panel.fileTypesTable.selectedListEntry as FileType;
}else{
	out.choosedFileType = null;
	}' #txt
Fs0 f39 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>selectionChanged</name>
        <nameStyle>16,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f39 630 406 20 20 13 0 #rect
Fs0 f39 @|RichDialogProcessStartIcon #fIcon
Fs0 f40 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f40 630 462 20 20 13 0 #rect
Fs0 f40 @|RichDialogProcessEndIcon #fIcon
Fs0 f41 expr out #txt
Fs0 f41 640 426 640 462 #arcP
Fs0 f42 guid 137F0BAA21E4890B #txt
Fs0 f42 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f42 method showError() #txt
Fs0 f42 disableUIEvents false #txt
Fs0 f42 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>showError()</name>
    </language>
</elementInfo>
' #txt
Fs0 f42 182 310 20 20 13 0 #rect
Fs0 f42 @|RichDialogMethodStartIcon #fIcon
Fs0 f43 targetWindow NEW:card: #txt
Fs0 f43 targetDisplay TOP #txt
Fs0 f43 richDialogId ch.ivyteam.ivy.addons.commondialogs.ErrorDialog #txt
Fs0 f43 startMethod showError(ch.ivyteam.ivy.addons.data.technical.IvyResultStatus,Boolean,Boolean) #txt
Fs0 f43 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f43 requestActionDecl '<ch.ivyteam.ivy.addons.data.technical.IvyResultStatus ivyResultStatus, Boolean showCopyButton, Boolean showDetailButton> param;' #txt
Fs0 f43 requestMappingAction 'param.ivyResultStatus.detail=in.errorUtil.throwable.getMessage();
param.ivyResultStatus.javaException=in.errorUtil.throwable;
param.ivyResultStatus.message=in.errorUtil.message;
param.ivyResultStatus.successful=false;
' #txt
Fs0 f43 responseActionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f43 responseMappingAction 'out=in;
' #txt
Fs0 f43 windowConfiguration '{/title "Error"/width 0 /height 0 /centered true /resizable true /maximized false /close_after_last_rd true }' #txt
Fs0 f43 isAsynch false #txt
Fs0 f43 isInnerRd true #txt
Fs0 f43 userContext '* ' #txt
Fs0 f43 174 364 36 24 20 -2 #rect
Fs0 f43 @|RichDialogIcon #fIcon
Fs0 f44 expr out #txt
Fs0 f44 192 330 192 364 #arcP
Fs0 f45 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f45 182 422 20 20 13 0 #rect
Fs0 f45 @|RichDialogProcessEndIcon #fIcon
Fs0 f46 expr out #txt
Fs0 f46 192 388 192 422 #arcP
Fs0 f47 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f47 actionTable 'out=in;
' #txt
Fs0 f47 actionCode 'if(in.errorUtil.errorOccurred)
{
	panel.showError();
}' #txt
Fs0 f47 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f47 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>if error show error</name>
        <nameStyle>19
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f47 174 212 36 24 20 -2 #rect
Fs0 f47 @|RichDialogProcessStepIcon #fIcon
Fs0 f48 expr out #txt
Fs0 f48 192 188 192 212 #arcP
Fs0 f8 expr out #txt
Fs0 f8 192 236 192 262 #arcP
Fs0 f49 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f49 actionTable 'out=in;
' #txt
Fs0 f49 actionCode 'if(in.errorUtil.errorOccurred)
{
	panel.showError();
}' #txt
Fs0 f49 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f49 622 276 36 24 20 -2 #rect
Fs0 f49 @|RichDialogProcessStepIcon #fIcon
Fs0 f50 expr out #txt
Fs0 f50 640 260 640 276 #arcP
Fs0 f29 expr out #txt
Fs0 f29 640 300 640 318 #arcP
Fs0 f51 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f51 actionTable 'out=in;
' #txt
Fs0 f51 actionCode 'if(in.errorUtil.errorOccurred)
{
	panel.showError();
}' #txt
Fs0 f51 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f51 718 276 36 24 20 -2 #rect
Fs0 f51 @|RichDialogProcessStepIcon #fIcon
Fs0 f52 expr out #txt
Fs0 f52 736 260 736 276 #arcP
Fs0 f30 expr out #txt
Fs0 f30 736 300 736 318 #arcP
Fs0 f53 guid 137F0DEB855F5E09 #txt
Fs0 f53 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f53 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f53 actionTable 'out=in;
' #txt
Fs0 f53 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>delete</name>
        <nameStyle>6,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f53 1014 62 20 20 13 0 #rect
Fs0 f53 @|RichDialogProcessStartIcon #fIcon
Fs0 f54 targetWindow NEW:card: #txt
Fs0 f54 targetDisplay TOP #txt
Fs0 f54 richDialogId ch.ivyteam.ivy.addons.commondialogs.QuestionDialog #txt
Fs0 f54 startMethod askQuestionWithTitle(String,String,String,List<String>) #txt
Fs0 f54 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f54 requestActionDecl '<String icon, String questionTitle, String questionText, List<String> buttons> param;' #txt
Fs0 f54 requestMappingAction 'param.questionTitle=ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/windowTitles/Warning");
param.questionText=ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileTypes/messages/deleteFileType");
param.buttons=[ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/yes"),
ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/no")];
' #txt
Fs0 f54 responseActionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f54 responseMappingAction 'out=in;
out.modCancelled=result.pressedButton.equalsIgnoreCase(ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/no"));
' #txt
Fs0 f54 windowConfiguration '{/title "<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/windowTitles/question\")%>"/width 300 /height 200 /centered true /resizable true /maximized false /close_after_last_rd true }' #txt
Fs0 f54 isAsynch false #txt
Fs0 f54 isInnerRd true #txt
Fs0 f54 userContext '* ' #txt
Fs0 f54 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>ask if sure</name>
        <nameStyle>11
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f54 1006 116 36 24 20 -2 #rect
Fs0 f54 @|RichDialogIcon #fIcon
Fs0 f55 expr out #txt
Fs0 f55 1024 82 1024 116 #arcP
Fs0 f56 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f56 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>cancelled?</name>
        <nameStyle>10
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f56 1010 170 28 28 14 0 #rect
Fs0 f56 @|AlternativeIcon #fIcon
Fs0 f57 expr out #txt
Fs0 f57 1024 140 1024 170 #arcP
Fs0 f58 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f58 1014 350 20 20 13 0 #rect
Fs0 f58 @|RichDialogProcessEndIcon #fIcon
Fs0 f59 expr in #txt
Fs0 f59 outCond in.modCancelled #txt
Fs0 f59 1038 184 1034 360 #arcP
Fs0 f59 1 1104 184 #addKink
Fs0 f59 2 1104 360 #addKink
Fs0 f59 1 0.3282512588169526 0 0 #arcLabel
Fs0 f60 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f60 actionTable 'out=in;
' #txt
Fs0 f60 actionCode 'if(in.errorUtil.errorOccurred)
{
	panel.showError();
}' #txt
Fs0 f60 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f60 1006 292 36 24 20 -2 #rect
Fs0 f60 @|RichDialogProcessStepIcon #fIcon
Fs0 f61 actionDecl 'ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData out;
' #txt
Fs0 f61 actionTable 'out=in;
' #txt
Fs0 f61 actionCode 'import ch.ivyteam.ivy.addons.filemanager.database.filetype.FileTypesController;
try{
	in.fileTypesController.deleteFileType(in.choosedFileType.getId());
	panel.fireSE_fileTypeDeleted(in.choosedFileType.getId());
	in.fileTypesList.clear();
	out.fileTypesList = in.fileTypesController.getAllFileTypes();
	
	out.choosedFileType = null;
	
}catch(Throwable t)
{
	out.errorUtil.errorOccurred=true;
	out.errorUtil.throwable=t;
	out.errorUtil.message="Error: "+t.getMessage();
}' #txt
Fs0 f61 type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
Fs0 f61 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>deleteFIleType
fire SE fileTypeDeleted</name>
        <nameStyle>38
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f61 1006 228 36 24 21 -15 #rect
Fs0 f61 @|RichDialogProcessStepIcon #fIcon
Fs0 f62 expr out #txt
Fs0 f62 1024 252 1024 292 #arcP
Fs0 f63 expr in #txt
Fs0 f63 1024 198 1024 228 #arcP
Fs0 f64 expr out #txt
Fs0 f64 1024 316 1024 350 #arcP
>Proto Fs0 .rdData2UIAction 'panel.fileTypesTable.listData=in.fileTypesList;
panel.modifyButton.enabled=in.#choosedFileType!=null && in.choosedFileType.id>0;
panel.selectButton.enabled=in.#choosedFileType!=null && in.choosedFileType.id>0;
panel.deleteButton.enabled=in.#choosedFileType!=null && in.choosedFileType.id>0;
' #txt
>Proto Fs0 .type ch.ivyteam.ivy.addons.filemanager.filetype.FileTypesDialog.FileTypesDialogData #txt
>Proto Fs0 .processKind RICH_DIALOG #txt
>Proto Fs0 -8 -8 16 16 16 26 #rect
>Proto Fs0 '' #fIcon
Fs0 f10 mainOut f12 tail #connect
Fs0 f12 head f11 mainIn #connect
Fs0 f2 mainOut f15 tail #connect
Fs0 f15 head f14 mainIn #connect
Fs0 f9 mainOut f17 tail #connect
Fs0 f17 head f16 mainIn #connect
Fs0 f16 mainOut f20 tail #connect
Fs0 f20 head f19 in #connect
Fs0 f14 mainOut f21 tail #connect
Fs0 f21 head f18 in #connect
Fs0 f19 out f24 tail #connect
Fs0 f24 head f23 mainIn #connect
Fs0 f18 out f25 tail #connect
Fs0 f25 head f22 mainIn #connect
Fs0 f26 mainOut f28 tail #connect
Fs0 f28 head f27 mainIn #connect
Fs0 f18 out f37 tail #connect
Fs0 f37 head f33 mainIn #connect
Fs0 f19 out f38 tail #connect
Fs0 f38 head f35 mainIn #connect
Fs0 f3 mainOut f32 tail #connect
Fs0 f32 head f31 mainIn #connect
Fs0 f31 mainOut f6 tail #connect
Fs0 f6 head f5 mainIn #connect
Fs0 f0 mainOut f34 tail #connect
Fs0 f34 head f31 mainIn #connect
Fs0 f4 mainOut f36 tail #connect
Fs0 f36 head f13 mainIn #connect
Fs0 f13 mainOut f1 tail #connect
Fs0 f1 head f5 mainIn #connect
Fs0 f39 mainOut f41 tail #connect
Fs0 f41 head f40 mainIn #connect
Fs0 f42 mainOut f44 tail #connect
Fs0 f44 head f43 mainIn #connect
Fs0 f43 mainOut f46 tail #connect
Fs0 f46 head f45 mainIn #connect
Fs0 f5 mainOut f48 tail #connect
Fs0 f48 head f47 mainIn #connect
Fs0 f47 mainOut f8 tail #connect
Fs0 f8 head f7 mainIn #connect
Fs0 f22 mainOut f50 tail #connect
Fs0 f50 head f49 mainIn #connect
Fs0 f49 mainOut f29 tail #connect
Fs0 f29 head f33 mainIn #connect
Fs0 f23 mainOut f52 tail #connect
Fs0 f52 head f51 mainIn #connect
Fs0 f51 mainOut f30 tail #connect
Fs0 f30 head f35 mainIn #connect
Fs0 f53 mainOut f55 tail #connect
Fs0 f55 head f54 mainIn #connect
Fs0 f54 mainOut f57 tail #connect
Fs0 f57 head f56 in #connect
Fs0 f56 out f59 tail #connect
Fs0 f59 head f58 mainIn #connect
Fs0 f61 mainOut f62 tail #connect
Fs0 f62 head f60 mainIn #connect
Fs0 f56 out f63 tail #connect
Fs0 f63 head f61 mainIn #connect
Fs0 f60 mainOut f64 tail #connect
Fs0 f64 head f58 mainIn #connect
