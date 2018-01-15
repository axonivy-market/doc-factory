[Ivy]
[>Created: Tue Aug 07 15:13:32 EDT 2012]
13900F4E09EDA8AA 3.17 #module
>Proto >Proto Collection #zClass
Fs0 FileActionHistoryViewerProcess Big #zClass
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
Fs0 @RichDialogProcessStep f4 '' #zField
Fs0 @RichDialogProcessEnd f6 '' #zField
Fs0 @PushWFArc f7 '' #zField
Fs0 @RichDialogInitStart f3 '' #zField
Fs0 @PushWFArc f5 '' #zField
Fs0 @RichDialogInitStart f8 '' #zField
Fs0 @PushWFArc f9 '' #zField
Fs0 @RichDialogProcessStart f12 '' #zField
Fs0 @RichDialogEnd f13 '' #zField
Fs0 @PushWFArc f14 '' #zField
Fs0 @RichDialogProcessStart f10 '' #zField
Fs0 @PushWFArc f11 '' #zField
>Proto Fs0 Fs0 FileActionHistoryViewerProcess #zField
Fs0 f0 guid 13900F4E0C1F93A7 #txt
Fs0 f0 type ch.ivyteam.ivy.addons.filemanager.fileaction.FileActionHistoryViewer.FileActionHistoryViewerData #txt
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
Fs0 f1 type ch.ivyteam.ivy.addons.filemanager.fileaction.FileActionHistoryViewer.FileActionHistoryViewerData #txt
Fs0 f1 86 150 20 20 13 0 #rect
Fs0 f1 @|RichDialogProcessEndIcon #fIcon
Fs0 f2 expr out #txt
Fs0 f2 96 74 96 150 #arcP
Fs0 f4 actionDecl 'ch.ivyteam.ivy.addons.filemanager.fileaction.FileActionHistoryViewer.FileActionHistoryViewerData out;
' #txt
Fs0 f4 actionTable 'out=in;
' #txt
Fs0 f4 actionCode '
in.fileActions.clear();
if(in.fileid>0){
	try{
		in.fileActions.addAll(in.fileActionHistoryController.getFileActions(in.fileid,in.lang));
	}catch(Throwable t)
	{
		out.errorUtil.errorOccurred=true;
		out.errorUtil.throwable=t;
		out.errorUtil.message=t.getMessage();
		ivy.log.error(t.getMessage(),t);
	}
}' #txt
Fs0 f4 type ch.ivyteam.ivy.addons.filemanager.fileaction.FileActionHistoryViewer.FileActionHistoryViewerData #txt
Fs0 f4 206 116 36 24 20 -2 #rect
Fs0 f4 @|RichDialogProcessStepIcon #fIcon
Fs0 f6 type ch.ivyteam.ivy.addons.filemanager.fileaction.FileActionHistoryViewer.FileActionHistoryViewerData #txt
Fs0 f6 214 198 20 20 13 0 #rect
Fs0 f6 @|RichDialogProcessEndIcon #fIcon
Fs0 f7 expr out #txt
Fs0 f7 224 140 224 198 #arcP
Fs0 f3 guid 13901C5154714378 #txt
Fs0 f3 type ch.ivyteam.ivy.addons.filemanager.fileaction.FileActionHistoryViewer.FileActionHistoryViewerData #txt
Fs0 f3 method start(ch.ivyteam.ivy.addons.filemanager.database.fileaction.FileActionConfiguration,Long,String) #txt
Fs0 f3 disableUIEvents true #txt
Fs0 f3 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.filemanager.database.fileaction.FileActionConfiguration fileActionConfiguration,java.lang.Long fileid,java.lang.String language> param = methodEvent.getInputArguments();
' #txt
Fs0 f3 inParameterMapAction 'out.fileActionHistoryController.config=param.fileActionConfiguration;
out.fileid=param.fileid;
out.lang=param.language;
' #txt
Fs0 f3 outParameterDecl '<> result;
' #txt
Fs0 f3 embeddedRdInitializations '* ' #txt
Fs0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(FileActionConfiguration,Long,String)</name>
        <nameStyle>42,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f3 150 30 20 20 12 -17 #rect
Fs0 f3 @|RichDialogInitStartIcon #fIcon
Fs0 f5 expr out #txt
Fs0 f5 165 48 215 116 #arcP
Fs0 f8 guid 13901C6538F184CD #txt
Fs0 f8 type ch.ivyteam.ivy.addons.filemanager.fileaction.FileActionHistoryViewer.FileActionHistoryViewerData #txt
Fs0 f8 method start(ch.ivyteam.ivy.addons.filemanager.database.fileaction.FileActionConfiguration,ch.ivyteam.ivy.addons.filemanager.DocumentOnServer,String) #txt
Fs0 f8 disableUIEvents true #txt
Fs0 f8 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.filemanager.database.fileaction.FileActionConfiguration fileActionConfiguration,ch.ivyteam.ivy.addons.filemanager.DocumentOnServer document,java.lang.String lang> param = methodEvent.getInputArguments();
' #txt
Fs0 f8 inParameterMapAction 'out.document=param.document;
out.fileActionHistoryController.config=param.fileActionConfiguration;
out.lang=param.lang;
' #txt
Fs0 f8 inActionCode 'try{
	out.fileid=Long.parseLong(param.document.fileID);
}catch(Throwable t)
{
		//do nothing
}' #txt
Fs0 f8 outParameterDecl '<> result;
' #txt
Fs0 f8 embeddedRdInitializations '* ' #txt
Fs0 f8 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(FileActionConfiguration,DocumentOnServer,String)</name>
        <nameStyle>54,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f8 278 70 20 20 13 0 #rect
Fs0 f8 @|RichDialogInitStartIcon #fIcon
Fs0 f9 expr out #txt
Fs0 f9 279 86 240 116 #arcP
Fs0 f12 guid 13901C9272FA2678 #txt
Fs0 f12 type ch.ivyteam.ivy.addons.filemanager.fileaction.FileActionHistoryViewer.FileActionHistoryViewerData #txt
Fs0 f12 actionDecl 'ch.ivyteam.ivy.addons.filemanager.fileaction.FileActionHistoryViewer.FileActionHistoryViewerData out;
' #txt
Fs0 f12 actionTable 'out=in;
' #txt
Fs0 f12 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>close</name>
        <nameStyle>5,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f12 694 86 20 20 13 0 #rect
Fs0 f12 @|RichDialogProcessStartIcon #fIcon
Fs0 f13 type ch.ivyteam.ivy.addons.filemanager.fileaction.FileActionHistoryViewer.FileActionHistoryViewerData #txt
Fs0 f13 guid 13901C948E75D290 #txt
Fs0 f13 694 150 20 20 13 0 #rect
Fs0 f13 @|RichDialogEndIcon #fIcon
Fs0 f14 expr out #txt
Fs0 f14 704 106 704 150 #arcP
Fs0 f10 guid 139026ECC6465309 #txt
Fs0 f10 type ch.ivyteam.ivy.addons.filemanager.fileaction.FileActionHistoryViewer.FileActionHistoryViewerData #txt
Fs0 f10 actionDecl 'ch.ivyteam.ivy.addons.filemanager.fileaction.FileActionHistoryViewer.FileActionHistoryViewerData out;
' #txt
Fs0 f10 actionTable 'out=in;
' #txt
Fs0 f10 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>refresh</name>
        <nameStyle>7,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f10 422 118 20 20 13 0 #rect
Fs0 f10 @|RichDialogProcessStartIcon #fIcon
Fs0 f11 expr out #txt
Fs0 f11 422 128 242 128 #arcP
>Proto Fs0 .rdData2UIAction 'panel.Table.listData=in.fileActions;
panel.tileLabel.text=in.document.filename;
panel.tileLabel.visible=in.#document!=null && in.document.filename.length()>0;
' #txt
>Proto Fs0 .type ch.ivyteam.ivy.addons.filemanager.fileaction.FileActionHistoryViewer.FileActionHistoryViewerData #txt
>Proto Fs0 .processKind RICH_DIALOG #txt
>Proto Fs0 -8 -8 16 16 16 26 #rect
>Proto Fs0 '' #fIcon
Fs0 f0 mainOut f2 tail #connect
Fs0 f2 head f1 mainIn #connect
Fs0 f4 mainOut f7 tail #connect
Fs0 f7 head f6 mainIn #connect
Fs0 f3 mainOut f5 tail #connect
Fs0 f5 head f4 mainIn #connect
Fs0 f8 mainOut f9 tail #connect
Fs0 f9 head f4 mainIn #connect
Fs0 f12 mainOut f14 tail #connect
Fs0 f14 head f13 mainIn #connect
Fs0 f10 mainOut f11 tail #connect
Fs0 f11 head f4 mainIn #connect
