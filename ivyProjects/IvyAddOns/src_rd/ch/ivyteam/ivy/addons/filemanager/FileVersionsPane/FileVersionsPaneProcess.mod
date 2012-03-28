[Ivy]
[>Created: Mon Feb 13 12:47:56 EST 2012]
13577559B1E4552F 3.17 #module
>Proto >Proto Collection #zClass
Fs0 FileVersionsPaneProcess Big #zClass
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
Fs0 @RichDialogInitStart f4 '' #zField
Fs0 @RichDialogProcessStep f5 '' #zField
Fs0 @PushWFArc f6 '' #zField
Fs0 @PushWFArc f7 '' #zField
Fs0 @RichDialogInitStart f8 '' #zField
Fs0 @RichDialogInitStart f9 '' #zField
Fs0 @PushWFArc f10 '' #zField
Fs0 @PushWFArc f11 '' #zField
Fs0 @RichDialogProcessEnd f12 '' #zField
Fs0 @RichDialogProcessStep f13 '' #zField
Fs0 @Alternative f16 '' #zField
Fs0 @PushWFArc f17 '' #zField
Fs0 @PushWFArc f14 '' #zField
Fs0 @Alternative f18 '' #zField
Fs0 @PushWFArc f19 '' #zField
Fs0 @PushWFArc f15 '' #zField
Fs0 @RichDialog f20 '' #zField
Fs0 @PushWFArc f21 '' #zField
Fs0 @PushWFArc f22 '' #zField
Fs0 @PushWFArc f23 '' #zField
Fs0 @RichDialogProcessStart f24 '' #zField
Fs0 @RichDialogProcessStep f25 '' #zField
Fs0 @PushWFArc f26 '' #zField
Fs0 @RichDialogProcessEnd f27 '' #zField
Fs0 @RichDialogProcessStart f29 '' #zField
Fs0 @RichDialogProcessStep f30 '' #zField
Fs0 @PushWFArc f31 '' #zField
Fs0 @RichDialogProcessEnd f32 '' #zField
Fs0 @PushWFArc f33 '' #zField
Fs0 @Alternative f34 '' #zField
Fs0 @PushWFArc f35 '' #zField
Fs0 @PushWFArc f28 '' #zField
Fs0 @RichDialog f36 '' #zField
Fs0 @PushWFArc f37 '' #zField
Fs0 @PushWFArc f38 '' #zField
>Proto Fs0 Fs0 FileVersionsPaneProcess #zField
Fs0 f0 guid 13577559B37213E0 #txt
Fs0 f0 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
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
Fs0 f0 22 38 20 20 13 0 #rect
Fs0 f0 @|RichDialogInitStartIcon #fIcon
Fs0 f1 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f1 22 142 20 20 13 0 #rect
Fs0 f1 @|RichDialogProcessEndIcon #fIcon
Fs0 f2 expr out #txt
Fs0 f2 32 58 32 142 #arcP
Fs0 f3 guid 135776F4A7177632 #txt
Fs0 f3 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f3 method start(Number) #txt
Fs0 f3 disableUIEvents true #txt
Fs0 f3 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.Number documentOnServerId> param = methodEvent.getInputArguments();
' #txt
Fs0 f3 inParameterMapAction 'out.parentDocumentOnServer.fileID=param.documentOnServerId.toString();
' #txt
Fs0 f3 outParameterDecl '<ch.ivyteam.ivy.addons.filemanager.FileVersion selectedFileVersion> result;
' #txt
Fs0 f3 embeddedRdInitializations '{/desktopHandlerPanel {/fieldName "desktopHandlerPanel"/startMethod "startVisible(Boolean)"/parameterMapping "param.visible=false;\n"/initScript ""/userContext * }}' #txt
Fs0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(Number)</name>
        <nameStyle>13,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f3 158 78 20 20 -41 16 #rect
Fs0 f3 @|RichDialogInitStartIcon #fIcon
Fs0 f4 guid 135776F54AAF5D45 #txt
Fs0 f4 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f4 method start(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer) #txt
Fs0 f4 disableUIEvents true #txt
Fs0 f4 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.filemanager.DocumentOnServer parentDocumentOnServer> param = methodEvent.getInputArguments();
' #txt
Fs0 f4 inParameterMapAction 'out.parentDocumentOnServer=param.parentDocumentOnServer;
' #txt
Fs0 f4 outParameterDecl '<ch.ivyteam.ivy.addons.filemanager.FileVersion selectedFileVersion> result;
' #txt
Fs0 f4 outParameterMapAction 'result.selectedFileVersion=in.selectedFileVersion;
' #txt
Fs0 f4 embeddedRdInitializations '{/desktopHandlerPanel {/fieldName "desktopHandlerPanel"/startMethod "startVisible(Boolean)"/parameterMapping "param.visible=false;\n"/initScript ""/userContext * }}' #txt
Fs0 f4 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(DocumentOnServer)</name>
        <nameStyle>23,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f4 174 38 20 20 -68 -25 #rect
Fs0 f4 @|RichDialogInitStartIcon #fIcon
Fs0 f5 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData out;
' #txt
Fs0 f5 actionTable 'out=in;
' #txt
Fs0 f5 actionCode 'import ch.ivyteam.ivy.addons.filemanager.database.versioning.FileVersioningController;
try{
	if(in.parentDocumentOnServer.fileID.trim().length()>0)
	{
		if(in.#fileVersioningController==null)
		{
			in.fileVersioningController=new FileVersioningController();
		}
		if(in.parentDocumentOnServer.filename.trim().length()==0){
			out.parentDocumentOnServer = in.fileVersioningController.getParentFileWithoutContent(Long.parseLong(in.parentDocumentOnServer.fileID));
		}
	}
}catch(Throwable t){
	in.errorUtil.errorOccurred=true;
	in.errorUtil.throwable = t;
	in.errorUtil.message =ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/errorOccurred");
}' #txt
Fs0 f5 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f5 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>depending on the start method used
set the fileVersioningController and 
get the parent DocumentOnServer Object</name>
        <nameStyle>111
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f5 262 124 36 24 22 1 #rect
Fs0 f5 @|RichDialogProcessStepIcon #fIcon
Fs0 f6 expr out #txt
Fs0 f6 191 54 267 124 #arcP
Fs0 f7 expr out #txt
Fs0 f7 177 91 262 128 #arcP
Fs0 f7 0 0.8170442822164051 0 0 #arcLabel
Fs0 f8 guid 1357786A11BD08CA #txt
Fs0 f8 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f8 method start(Number,ch.ivyteam.ivy.addons.filemanager.database.versioning.FileVersioningController) #txt
Fs0 f8 disableUIEvents true #txt
Fs0 f8 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.Number parentFileId,ch.ivyteam.ivy.addons.filemanager.database.versioning.FileVersioningController fileVersioningController> param = methodEvent.getInputArguments();
' #txt
Fs0 f8 inParameterMapAction 'out.fileVersioningController=param.fileVersioningController;
out.parentDocumentOnServer.fileID=param.parentFileId.toString();
' #txt
Fs0 f8 outParameterDecl '<ch.ivyteam.ivy.addons.filemanager.FileVersion selectedFileVersion> result;
' #txt
Fs0 f8 embeddedRdInitializations '{/desktopHandlerPanel {/fieldName "desktopHandlerPanel"/startMethod "startVisible(Boolean)"/parameterMapping "param.visible=false;\n"/initScript ""/userContext * }}' #txt
Fs0 f8 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(Number,FileVersioningController)</name>
        <nameStyle>38,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f8 350 38 20 20 13 0 #rect
Fs0 f8 @|RichDialogInitStartIcon #fIcon
Fs0 f9 guid 1357786AA0B2C228 #txt
Fs0 f9 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f9 method start(ch.ivyteam.ivy.addons.filemanager.DocumentOnServer,ch.ivyteam.ivy.addons.filemanager.database.versioning.FileVersioningController) #txt
Fs0 f9 disableUIEvents true #txt
Fs0 f9 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.filemanager.DocumentOnServer parentDocumentOnServer,ch.ivyteam.ivy.addons.filemanager.database.versioning.FileVersioningController fileVersioningController> param = methodEvent.getInputArguments();
' #txt
Fs0 f9 inParameterMapAction 'out.fileVersioningController=param.fileVersioningController;
out.parentDocumentOnServer=param.parentDocumentOnServer;
' #txt
Fs0 f9 outParameterDecl '<ch.ivyteam.ivy.addons.filemanager.FileVersion selectedFileVersion> result;
' #txt
Fs0 f9 embeddedRdInitializations '{/desktopHandlerPanel {/fieldName "desktopHandlerPanel"/startMethod "startVisible(Boolean)"/parameterMapping "param.visible=false;\n"/initScript ""/userContext * }}' #txt
Fs0 f9 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(DocumentOnServer,FileVersioningController)</name>
        <nameStyle>48,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f9 383 78 19 20 13 0 #rect
Fs0 f9 @|RichDialogInitStartIcon #fIcon
Fs0 f10 expr out #txt
Fs0 f10 353 55 291 124 #arcP
Fs0 f11 expr out #txt
Fs0 f11 382 91 298 128 #arcP
Fs0 f12 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f12 270 374 20 20 13 0 #rect
Fs0 f12 @|RichDialogProcessEndIcon #fIcon
Fs0 f13 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData out;
' #txt
Fs0 f13 actionTable 'out=in;
' #txt
Fs0 f13 actionCode 'try{
	in.fileVersionsList.addAll(in.fileVersioningController.getFileVersions(Long.parseLong(in.parentDocumentOnServer.fileID)));
}catch(Throwable t){
	in.errorUtil.errorOccurred=true;
	in.errorUtil.throwable = t;
	in.errorUtil.message =ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/errorOccurred");
}' #txt
Fs0 f13 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f13 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>get the fileVersions</name>
        <nameStyle>20
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f13 262 268 36 24 20 -2 #rect
Fs0 f13 @|RichDialogProcessStepIcon #fIcon
Fs0 f16 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f16 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>error?</name>
        <nameStyle>6
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f16 266 194 28 28 14 0 #rect
Fs0 f16 @|AlternativeIcon #fIcon
Fs0 f17 expr out #txt
Fs0 f17 280 148 280 194 #arcP
Fs0 f14 expr in #txt
Fs0 f14 outCond !in.errorUtil.errorOccurred #txt
Fs0 f14 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>No</name>
        <nameStyle>2
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f14 280 222 280 268 #arcP
Fs0 f18 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f18 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>error?</name>
        <nameStyle>6
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f18 266 314 28 28 14 0 #rect
Fs0 f18 @|AlternativeIcon #fIcon
Fs0 f19 expr out #txt
Fs0 f19 280 292 280 314 #arcP
Fs0 f15 expr in #txt
Fs0 f15 outCond !in.errorUtil.errorOccurred #txt
Fs0 f15 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>No</name>
        <nameStyle>2
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f15 280 342 280 374 #arcP
Fs0 f20 targetWindow NEW:card: #txt
Fs0 f20 targetDisplay TOP #txt
Fs0 f20 richDialogId ch.ivyteam.ivy.addons.commondialogs.QuestionDialog #txt
Fs0 f20 startMethod askQuestion(String,String,List<String>,String) #txt
Fs0 f20 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f20 requestActionDecl '<String icon, String question, List<String> buttons, String defaultButton> param;' #txt
Fs0 f20 requestMappingAction 'param.icon="error";
param.question=in.errorUtil.message+" \n"+IF(in.#errorUtil.#throwable!=null,in.errorUtil.throwable.getMessage(),"");
param.buttons=[ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/ok")];
param.defaultButton=ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/ok");
' #txt
Fs0 f20 requestActionCode 'if(in.#errorUtil.#throwable!=null){
	ivy.log.error(in.errorUtil.throwable.getMessage(),in.errorUtil.throwable);
}' #txt
Fs0 f20 responseActionDecl 'ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData out;
' #txt
Fs0 f20 responseMappingAction 'out=in;
' #txt
Fs0 f20 windowConfiguration '{/title "<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/windowTitles/error\")%>"/width 0 /height 0 /centered true /resizable true /maximized false /close_after_last_rd true }' #txt
Fs0 f20 isAsynch false #txt
Fs0 f20 isInnerRd true #txt
Fs0 f20 userContext '* ' #txt
Fs0 f20 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>show Error</name>
        <nameStyle>10
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f20 414 316 36 24 20 -2 #rect
Fs0 f20 @|RichDialogIcon #fIcon
Fs0 f20 -65536|-1|-16777216 #nodeStyle
Fs0 f21 expr in #txt
Fs0 f21 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>YES</name>
        <nameStyle>3
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f21 294 328 414 328 #arcP
Fs0 f22 expr in #txt
Fs0 f22 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>YES</name>
        <nameStyle>3
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f22 294 208 432 316 #arcP
Fs0 f22 1 432 208 #addKink
Fs0 f22 0 0.8510728837725112 0 0 #arcLabel
Fs0 f23 expr out #txt
Fs0 f23 432 340 290 384 #arcP
Fs0 f23 1 432 384 #addKink
Fs0 f23 1 0.31754852312966303 0 0 #arcLabel
Fs0 f24 guid 135779417C8E8B2E #txt
Fs0 f24 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f24 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData out;
' #txt
Fs0 f24 actionTable 'out=in;
' #txt
Fs0 f24 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>openSelectedVersion</name>
        <nameStyle>19,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f24 782 38 20 20 13 0 #rect
Fs0 f24 @|RichDialogProcessStartIcon #fIcon
Fs0 f25 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData out;
' #txt
Fs0 f25 actionTable 'out=in;
' #txt
Fs0 f25 actionCode 'in.errorUtil.errorOccurred=false;
if(in.selectedFileVersion.id>0){
	try{
		// the changes are not going to be flush into the DB
		panel.desktopHandlerPanel.openFileAndCheckForChanges(in.fileVersioningController.getFileVersionWithJavaFile(in.selectedFileVersion.id).javaFile);
	}catch(Throwable t){
		in.errorUtil.errorOccurred=true;
		in.errorUtil.throwable = t;
		in.errorUtil.message =ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/messages/error/errorOccurred");
	}
}
' #txt
Fs0 f25 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f25 774 92 36 24 20 -2 #rect
Fs0 f25 @|RichDialogProcessStepIcon #fIcon
Fs0 f26 expr out #txt
Fs0 f26 792 58 792 92 #arcP
Fs0 f27 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f27 782 206 20 20 13 0 #rect
Fs0 f27 @|RichDialogProcessEndIcon #fIcon
Fs0 f29 guid 1357795FE332E016 #txt
Fs0 f29 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f29 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData out;
' #txt
Fs0 f29 actionTable 'out=in;
' #txt
Fs0 f29 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>selectionChanged</name>
        <nameStyle>16,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f29 990 46 20 20 13 0 #rect
Fs0 f29 @|RichDialogProcessStartIcon #fIcon
Fs0 f30 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData out;
' #txt
Fs0 f30 actionTable 'out=in;
' #txt
Fs0 f30 actionCode 'import ch.ivyteam.ivy.addons.filemanager.FileVersion;

if(panel.#Table.#selectedListEntry != null && panel.Table.selectedListEntry instanceof FileVersion){
	out.selectedFileVersion = panel.Table.selectedListEntry as FileVersion;
}else{
	out.selectedFileVersion= new FileVersion();
}' #txt
Fs0 f30 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f30 982 100 36 24 20 -2 #rect
Fs0 f30 @|RichDialogProcessStepIcon #fIcon
Fs0 f31 expr out #txt
Fs0 f31 1000 66 1000 100 #arcP
Fs0 f32 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f32 990 158 20 20 13 0 #rect
Fs0 f32 @|RichDialogProcessEndIcon #fIcon
Fs0 f33 expr out #txt
Fs0 f33 1000 124 1000 158 #arcP
Fs0 f34 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f34 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>error?</name>
        <nameStyle>6
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f34 778 146 28 28 -48 -20 #rect
Fs0 f34 @|AlternativeIcon #fIcon
Fs0 f35 expr out #txt
Fs0 f35 792 116 792 146 #arcP
Fs0 f28 expr in #txt
Fs0 f28 outCond !in.errorUtil.errorOccurred #txt
Fs0 f28 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>NO</name>
        <nameStyle>2
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f28 792 174 792 206 #arcP
Fs0 f36 targetWindow NEW:card: #txt
Fs0 f36 targetDisplay TOP #txt
Fs0 f36 richDialogId ch.ivyteam.ivy.addons.commondialogs.QuestionDialog #txt
Fs0 f36 startMethod askQuestion(String,String,List<String>,String) #txt
Fs0 f36 type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
Fs0 f36 requestActionDecl '<String icon, String question, List<String> buttons, String defaultButton> param;' #txt
Fs0 f36 requestMappingAction 'param.icon="error";
param.question=in.errorUtil.message+" \n"+IF(in.#errorUtil.#throwable!=null,in.errorUtil.throwable.getMessage(),"");
param.buttons=[ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/ok")];
param.defaultButton=ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/ok");
' #txt
Fs0 f36 requestActionCode 'if(in.#errorUtil.#throwable!=null){
	ivy.log.error(in.errorUtil.throwable.getMessage(),in.errorUtil.throwable);
}' #txt
Fs0 f36 responseActionDecl 'ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData out;
' #txt
Fs0 f36 responseMappingAction 'out=in;
' #txt
Fs0 f36 windowConfiguration '{/title "<%=ivy.cms.co(\"/ch/ivyteam/ivy/addons/filemanager/fileManagement/windowTitles/error\")%>"/width 0 /height 0 /centered true /resizable true /maximized false /close_after_last_rd true }' #txt
Fs0 f36 isAsynch false #txt
Fs0 f36 isInnerRd true #txt
Fs0 f36 userContext '* ' #txt
Fs0 f36 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>show Error</name>
        <nameStyle>10
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f36 862 148 36 24 20 -2 #rect
Fs0 f36 @|RichDialogIcon #fIcon
Fs0 f36 -65536|-1|-16777216 #nodeStyle
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
Fs0 f37 806 160 862 160 #arcP
Fs0 f38 expr out #txt
Fs0 f38 880 172 802 216 #arcP
Fs0 f38 1 880 216 #addKink
Fs0 f38 1 0.19793285210130512 0 0 #arcLabel
>Proto Fs0 .rdData2UIAction 'panel.Table.listData=in.fileVersionsList;
panel.titleLabel.iconUri=IF(in.#parentDocumentOnServer != null && in.parentDocumentOnServer.filename.trim().length()>0,
ivy.cms.cr("/ch/ivyteam/ivy/addons/icons/fileSharing/24"),
ivy.cms.cr("/ch/ivyteam/ivy/addons/icons/warning/24")
);
panel.titleLabel.text=IF(in.#parentDocumentOnServer != null && in.parentDocumentOnServer.filename.trim().length()>0,
ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileVersioning/plainStrings/title")+" "+in.parentDocumentOnServer.filename,
ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileVersioning/plainStrings/noValidParentDocument")
);
' #txt
>Proto Fs0 .type ch.ivyteam.ivy.addons.filemanager.FileVersionsPane.FileVersionsPaneData #txt
>Proto Fs0 .processKind RICH_DIALOG #txt
>Proto Fs0 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <swimlaneLabel></swimlaneLabel>
        <swimlaneLabel></swimlaneLabel>
    </language>
    <swimlaneSize>693</swimlaneSize>
    <swimlaneColor>-10027162</swimlaneColor>
</elementInfo>
' #txt
>Proto Fs0 -8 -8 16 16 16 26 #rect
>Proto Fs0 '' #fIcon
Fs0 f0 mainOut f2 tail #connect
Fs0 f2 head f1 mainIn #connect
Fs0 f4 mainOut f6 tail #connect
Fs0 f6 head f5 mainIn #connect
Fs0 f3 mainOut f7 tail #connect
Fs0 f7 head f5 mainIn #connect
Fs0 f8 mainOut f10 tail #connect
Fs0 f10 head f5 mainIn #connect
Fs0 f9 mainOut f11 tail #connect
Fs0 f11 head f5 mainIn #connect
Fs0 f5 mainOut f17 tail #connect
Fs0 f17 head f16 in #connect
Fs0 f16 out f14 tail #connect
Fs0 f14 head f13 mainIn #connect
Fs0 f13 mainOut f19 tail #connect
Fs0 f19 head f18 in #connect
Fs0 f18 out f15 tail #connect
Fs0 f15 head f12 mainIn #connect
Fs0 f18 out f21 tail #connect
Fs0 f21 head f20 mainIn #connect
Fs0 f16 out f22 tail #connect
Fs0 f22 head f20 mainIn #connect
Fs0 f20 mainOut f23 tail #connect
Fs0 f23 head f12 mainIn #connect
Fs0 f24 mainOut f26 tail #connect
Fs0 f26 head f25 mainIn #connect
Fs0 f29 mainOut f31 tail #connect
Fs0 f31 head f30 mainIn #connect
Fs0 f30 mainOut f33 tail #connect
Fs0 f33 head f32 mainIn #connect
Fs0 f25 mainOut f35 tail #connect
Fs0 f35 head f34 in #connect
Fs0 f34 out f28 tail #connect
Fs0 f28 head f27 mainIn #connect
Fs0 f34 out f37 tail #connect
Fs0 f37 head f36 mainIn #connect
Fs0 f36 mainOut f38 tail #connect
Fs0 f38 head f27 mainIn #connect
