[Ivy]
[>Created: Mon Oct 07 21:50:04 EDT 2013]
125FDE1E1CFE410A 3.17 #module
>Proto >Proto Collection #zClass
Fs0 FileUploadPreparerProcess Big #zClass
Fs0 RD #cInfo
Fs0 #process
Fs0 @TextInP .ui2RdDataAction .ui2RdDataAction #zField
Fs0 @TextInP .rdData2UIAction .rdData2UIAction #zField
Fs0 @AnnotationInP-0n ai ai #zField
Fs0 @TextInP .xml .xml #zField
Fs0 @TextInP .responsibility .responsibility #zField
Fs0 @MessageFlowInP-0n messageIn messageIn #zField
Fs0 @MessageFlowOutP-0n messageOut messageOut #zField
Fs0 @TextInP .resExport .resExport #zField
Fs0 @TextInP .type .type #zField
Fs0 @TextInP .processKind .processKind #zField
Fs0 @RichDialogInitStart f0 '' #zField
Fs0 @RichDialogProcessEnd f1 '' #zField
Fs0 @PushWFArc f2 '' #zField
Fs0 @RichDialogInitStart f3 '' #zField
Fs0 @PushWFArc f4 '' #zField
Fs0 @RichDialogMethodStart f5 '' #zField
Fs0 @RichDialogMethodStart f6 '' #zField
Fs0 @RichDialogMethodStart f7 '' #zField
Fs0 @RichDialogProcessEnd f8 '' #zField
Fs0 @RichDialogProcessStep f10 '' #zField
Fs0 @PushWFArc f11 '' #zField
Fs0 @RichDialogProcessEnd f12 '' #zField
Fs0 @PushWFArc f13 '' #zField
Fs0 @RichDialogProcessStep f14 '' #zField
Fs0 @PushWFArc f15 '' #zField
Fs0 @RichDialogProcessEnd f16 '' #zField
Fs0 @PushWFArc f17 '' #zField
Fs0 @RichDialogProcessStart f18 '' #zField
Fs0 @RichDialogProcessStep f19 '' #zField
Fs0 @PushWFArc f20 '' #zField
Fs0 @RichDialogProcessEnd f21 '' #zField
Fs0 @RichDialogProcessStart f23 '' #zField
Fs0 @RichDialogProcessStep f24 '' #zField
Fs0 @PushWFArc f25 '' #zField
Fs0 @RichDialogProcessEnd f26 '' #zField
Fs0 @PushWFArc f27 '' #zField
Fs0 @RichDialogProcessStart f28 '' #zField
Fs0 @RichDialogProcessStep f29 '' #zField
Fs0 @PushWFArc f30 '' #zField
Fs0 @RichDialogProcessEnd f31 '' #zField
Fs0 @PushWFArc f32 '' #zField
Fs0 @RichDialogProcessStep f36 '' #zField
Fs0 @RichDialogProcessEnd f38 '' #zField
Fs0 @PushWFArc f39 '' #zField
Fs0 @PushWFArc f22 '' #zField
Fs0 @RichDialogMethodStart f33 '' #zField
Fs0 @PushWFArc f34 '' #zField
Fs0 @RichDialogMethodStart f40 '' #zField
Fs0 @PushWFArc f41 '' #zField
Fs0 @RichDialogInitStart f35 '' #zField
Fs0 @RichDialogProcessStep f42 '' #zField
Fs0 @PushWFArc f43 '' #zField
Fs0 @PushWFArc f37 '' #zField
Fs0 @PushWFArc f9 '' #zField
Fs0 @RichDialogMethodStart f44 '' #zField
Fs0 @RichDialogProcessStep f46 '' #zField
Fs0 @PushWFArc f47 '' #zField
Fs0 @RichDialogProcessEnd f48 '' #zField
Fs0 @PushWFArc f49 '' #zField
Fs0 @RichDialogMethodStart f45 '' #zField
Fs0 @RichDialogProcessEnd f50 '' #zField
Fs0 @PushWFArc f51 '' #zField
Fs0 @RichDialogMethodStart f52 '' #zField
Fs0 @RichDialog f53 '' #zField
Fs0 @PushWFArc f54 '' #zField
Fs0 @RichDialogProcessEnd f55 '' #zField
Fs0 @PushWFArc f56 '' #zField
>Proto Fs0 Fs0 FileUploadPreparerProcess #zField
Fs0 f0 guid 12181518EFF20514 #txt
Fs0 f0 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f0 method start() #txt
Fs0 f0 disableUIEvents false #txt
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
Fs0 f0 54 54 20 20 13 0 #rect
Fs0 f0 @|RichDialogInitStartIcon #fIcon
Fs0 f1 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f1 51 155 26 26 14 0 #rect
Fs0 f1 @|RichDialogProcessEndIcon #fIcon
Fs0 f2 expr out #txt
Fs0 f2 64 74 64 155 #arcP
Fs0 f3 guid 1218160837997FAC #txt
Fs0 f3 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f3 method start(String) #txt
Fs0 f3 disableUIEvents false #txt
Fs0 f3 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.String serverPath> param = methodEvent.getInputArguments();
' #txt
Fs0 f3 inParameterMapAction 'out.configurationController.rootPath=param.serverPath;
' #txt
Fs0 f3 outParameterDecl '<> result;
' #txt
Fs0 f3 embeddedRdInitializations '* ' #txt
Fs0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(String)</name>
        <nameStyle>13,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f3 158 54 20 20 13 0 #rect
Fs0 f3 @|RichDialogInitStartIcon #fIcon
Fs0 f4 expr out #txt
Fs0 f4 168 74 77 168 #arcP
Fs0 f4 1 168 168 #addKink
Fs0 f4 0 0.9907300718093622 0 0 #arcLabel
Fs0 f5 guid 1218160C63FF4DC1 #txt
Fs0 f5 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f5 method clear() #txt
Fs0 f5 disableUIEvents false #txt
Fs0 f5 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>clear()</name>
    </language>
</elementInfo>
' #txt
Fs0 f5 350 70 20 20 13 0 #rect
Fs0 f5 @|RichDialogMethodStartIcon #fIcon
Fs0 f6 guid 1218160D17A386B0 #txt
Fs0 f6 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f6 method setServerPath(String) #txt
Fs0 f6 disableUIEvents false #txt
Fs0 f6 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.String serverPath> param = methodEvent.getInputArguments();
' #txt
Fs0 f6 inParameterMapAction 'out.configurationController.rootPath=param.serverPath;
' #txt
Fs0 f6 outParameterDecl '<> result;
' #txt
Fs0 f6 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>setServerPath(String)</name>
        <nameStyle>21,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f6 614 70 20 20 13 0 #rect
Fs0 f6 @|RichDialogMethodStartIcon #fIcon
Fs0 f7 guid 1218160DDFDD084B #txt
Fs0 f7 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f7 method uploadPreparedFiles() #txt
Fs0 f7 disableUIEvents false #txt
Fs0 f7 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>uploadPreparedFiles()</name>
    </language>
</elementInfo>
' #txt
Fs0 f7 350 318 20 20 13 0 #rect
Fs0 f7 @|RichDialogMethodStartIcon #fIcon
Fs0 f8 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f8 611 163 26 26 14 0 #rect
Fs0 f8 @|RichDialogProcessEndIcon #fIcon
Fs0 f10 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData out;
' #txt
Fs0 f10 actionTable 'out=in;
' #txt
Fs0 f10 actionCode 'out.filesToUpload.clear();
out.selectedFiles.clear();
out.filesChoosed.clear();
out.configurationController.rootPath="";' #txt
Fs0 f10 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f10 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Clear the uploadFileList</name>
        <nameStyle>24,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f10 342 124 36 24 23 -10 #rect
Fs0 f10 @|RichDialogProcessStepIcon #fIcon
Fs0 f11 expr out #txt
Fs0 f11 360 90 360 124 #arcP
Fs0 f12 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f12 347 179 26 26 14 0 #rect
Fs0 f12 @|RichDialogProcessEndIcon #fIcon
Fs0 f13 expr out #txt
Fs0 f13 360 148 360 179 #arcP
Fs0 f14 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData out;
' #txt
Fs0 f14 actionTable 'out=in;
' #txt
Fs0 f14 actionCode 'import ch.ivyteam.ivy.addons.filemanager.FileHandler;
if(!in.configurationController.rootPath.equalsIgnoreCase("")){
	if(in.configurationController.storeFilesInDB)
	{
		in.uploadManager.uploadPreparedFilesWithoutShowingProgress(in.filesToUpload);
	}else
	{
		for(java.io.File f: in.filesToUpload){
			FileHandler.upload(f,in.configurationController.rootPath);
		}
	}
}
' #txt
Fs0 f14 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f14 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>loop in the file list
and upload each one to the serverPath</name>
        <nameStyle>59,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f14 342 372 36 24 20 -11 #rect
Fs0 f14 @|RichDialogProcessStepIcon #fIcon
Fs0 f15 expr out #txt
Fs0 f15 360 338 360 372 #arcP
Fs0 f16 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f16 347 427 26 26 14 0 #rect
Fs0 f16 @|RichDialogProcessEndIcon #fIcon
Fs0 f17 expr out #txt
Fs0 f17 360 396 360 427 #arcP
Fs0 f18 guid 1218163E13187083 #txt
Fs0 f18 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f18 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData out;
' #txt
Fs0 f18 actionTable 'out=in;
' #txt
Fs0 f18 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>prepareFilesToUpload</name>
        <nameStyle>20,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f18 862 54 20 20 13 0 #rect
Fs0 f18 @|RichDialogProcessStartIcon #fIcon
Fs0 f19 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData out;
' #txt
Fs0 f19 actionTable 'out=in;
' #txt
Fs0 f19 actionCode 'import ch.ivyteam.ivy.addons.filemanager.FileUploadHandler;
FileUploadHandler fuh = new FileUploadHandler();

fuh.prepareFilesForUpload(panel,"_getChoosedFiles");' #txt
Fs0 f19 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f19 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>choose the files to upload</name>
        <nameStyle>26,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f19 854 117 36 23 20 -2 #rect
Fs0 f19 @|RichDialogProcessStepIcon #fIcon
Fs0 f20 expr out #txt
Fs0 f20 872 74 872 116 #arcP
Fs0 f20 0 0.5000000000000001 0 0 #arcLabel
Fs0 f21 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f21 859 227 26 26 14 0 #rect
Fs0 f21 @|RichDialogProcessEndIcon #fIcon
Fs0 f23 guid 1218168796D76D51 #txt
Fs0 f23 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f23 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData out;
' #txt
Fs0 f23 actionTable 'out=in;
' #txt
Fs0 f23 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>removesSelectedFile</name>
        <nameStyle>19,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f23 1118 54 20 20 13 0 #rect
Fs0 f23 @|RichDialogProcessStartIcon #fIcon
Fs0 f24 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData out;
' #txt
Fs0 f24 actionTable 'out=in;
' #txt
Fs0 f24 actionCode 'List<java.io.File> files = new List<java.io.File>();
files.addAll(in.selectedFiles);
out.filesToUpload.removeAll(files);' #txt
Fs0 f24 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f24 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>removes each selected File</name>
        <nameStyle>26,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f24 1110 108 36 24 20 -2 #rect
Fs0 f24 @|RichDialogProcessStepIcon #fIcon
Fs0 f25 expr out #txt
Fs0 f25 1128 74 1128 108 #arcP
Fs0 f26 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f26 1115 171 26 26 14 0 #rect
Fs0 f26 @|RichDialogProcessEndIcon #fIcon
Fs0 f27 expr out #txt
Fs0 f27 1128 132 1128 171 #arcP
Fs0 f28 guid 1218169FE8D42789 #txt
Fs0 f28 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f28 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData out;
' #txt
Fs0 f28 actionTable 'out=in;
' #txt
Fs0 f28 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>fileSelectionChanged</name>
        <nameStyle>20,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f28 862 334 20 20 13 0 #rect
Fs0 f28 @|RichDialogProcessStartIcon #fIcon
Fs0 f29 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData out;
' #txt
Fs0 f29 actionTable 'out=in;
' #txt
Fs0 f29 actionCode 'in.selectedFiles.clear();
if(panel.#filesTable.getSelectedListEntries() != null && panel.filesTable.getSelectedListEntries() instanceof List<java.io.File>){
	in.selectedFiles.addAll(panel.filesTable.getSelectedListEntries() as List<java.io.File> );
}' #txt
Fs0 f29 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f29 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language/>
</elementInfo>
' #txt
Fs0 f29 854 380 36 24 20 -2 #rect
Fs0 f29 @|RichDialogProcessStepIcon #fIcon
Fs0 f30 expr out #txt
Fs0 f30 872 354 872 380 #arcP
Fs0 f31 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f31 859 451 26 26 14 0 #rect
Fs0 f31 @|RichDialogProcessEndIcon #fIcon
Fs0 f32 expr out #txt
Fs0 f32 872 404 872 451 #arcP
Fs0 f36 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData out;
' #txt
Fs0 f36 actionTable 'out=in;
' #txt
Fs0 f36 actionCode 'for(java.io.File file: in.filesChoosed){
	if(!in.filesToUpload.contains(file)){
		in.filesToUpload.add(file);
	}
}' #txt
Fs0 f36 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f36 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language/>
</elementInfo>
' #txt
Fs0 f36 606 380 36 24 20 -2 #rect
Fs0 f36 @|RichDialogProcessStepIcon #fIcon
Fs0 f38 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f38 611 443 26 26 14 0 #rect
Fs0 f38 @|RichDialogProcessEndIcon #fIcon
Fs0 f39 expr out #txt
Fs0 f39 624 404 624 443 #arcP
Fs0 f22 expr out #txt
Fs0 f22 872 139 872 227 #arcP
Fs0 f33 guid 12B2B6B3C348545D #txt
Fs0 f33 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f33 method uploadPreparedFiles(String) #txt
Fs0 f33 disableUIEvents false #txt
Fs0 f33 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.String path> param = methodEvent.getInputArguments();
' #txt
Fs0 f33 inParameterMapAction 'out.configurationController.rootPath=param.path;
' #txt
Fs0 f33 outParameterDecl '<> result;
' #txt
Fs0 f33 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>uploadPreparedFiles(String)</name>
        <nameStyle>27,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f33 294 286 20 20 13 0 #rect
Fs0 f33 @|RichDialogMethodStartIcon #fIcon
Fs0 f34 expr out #txt
Fs0 f34 309 304 352 372 #arcP
Fs0 f40 guid 1324986575B22009 #txt
Fs0 f40 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f40 method _getChoosedFiles(List<java.io.File>) #txt
Fs0 f40 disableUIEvents false #txt
Fs0 f40 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<List<java.io.File> files> param = methodEvent.getInputArguments();
' #txt
Fs0 f40 inParameterMapAction 'out.filesChoosed=param.files;
' #txt
Fs0 f40 outParameterDecl '<> result;
' #txt
Fs0 f40 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>_getChoosedFiles(List&lt;File&gt;)</name>
        <nameStyle>28,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f40 614 318 20 20 13 0 #rect
Fs0 f40 @|RichDialogMethodStartIcon #fIcon
Fs0 f41 expr out #txt
Fs0 f41 624 338 624 380 #arcP
Fs0 f35 guid 137BC90890BA964A #txt
Fs0 f35 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f35 method start(ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController) #txt
Fs0 f35 disableUIEvents true #txt
Fs0 f35 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController configurationController> param = methodEvent.getInputArguments();
' #txt
Fs0 f35 inParameterMapAction 'out.configurationController=param.configurationController;
' #txt
Fs0 f35 outParameterDecl '<> result;
' #txt
Fs0 f35 embeddedRdInitializations '* ' #txt
Fs0 f35 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(BasicConfigurationController)</name>
        <nameStyle>35,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f35 54 278 20 20 -50 13 #rect
Fs0 f35 @|RichDialogInitStartIcon #fIcon
Fs0 f42 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData out;
' #txt
Fs0 f42 actionTable 'out=in;
' #txt
Fs0 f42 actionCode 'import ch.ivyteam.ivy.addons.filemanager.database.AbstractFileManagementHandler;
import ch.ivyteam.ivy.addons.filemanager.database.fileaction.FileActionHistoryController;
import ch.ivyteam.ivy.addons.filemanager.FileUploadHandler;

in.fileManagementHandler = AbstractFileManagementHandler.getInstance(in.configurationController);
if(in.configurationController.fileActionHistoryConfiguration.activateFileActionHistory)
{
	in.fileManagementHandler.setFileActionHistoryController(new FileActionHistoryController(in.configurationController.fileActionHistoryConfiguration));
}
if(in.configurationController.storeFilesInDB)
{
	File dir = new File(ivy.session.getSessionUserName()+"/"+System.nanoTime()+"/Upload/",true);
	dir.mkdir();
	if(dir.isDirectory())
	{
		in.uploadManager = new FileUploadHandler(panel,"_onUploadError","_getUploadedFileFromUploadHandler","_onChangeFile","_onProgress",dir.getAbsolutePath());		
	}else{
		in.uploadManager = new FileUploadHandler(panel,"_onUploadError","_uploadDone","_askForOverwriteFiles","",in.configurationController.rootPath);
	}
	in.uploadManager.setAreFilesStoredInDB(true);
	in.uploadManager.setFilesDestinationPathForDB(in.configurationController.rootPath);
	in.uploadManager.setFileHandlerMgt(in.fileManagementHandler);
}else{
	in.uploadManager = new FileUploadHandler(panel,"_onUploadError","_uploadDone","_askForOverwriteFiles","",in.configurationController.rootPath);
}

' #txt
Fs0 f42 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f42 46 212 36 24 20 -2 #rect
Fs0 f42 @|RichDialogProcessStepIcon #fIcon
Fs0 f43 expr out #txt
Fs0 f43 64 278 64 236 #arcP
Fs0 f37 expr out #txt
Fs0 f37 64 212 64 181 #arcP
Fs0 f9 expr out #txt
Fs0 f9 624 90 624 163 #arcP
Fs0 f44 guid 1390900A6A714283 #txt
Fs0 f44 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f44 method _getUploadedFileFromUploadHandler(ch.ivyteam.ivy.addons.filemanager.ReturnedMessage) #txt
Fs0 f44 disableUIEvents false #txt
Fs0 f44 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.filemanager.ReturnedMessage returnedMessage> param = methodEvent.getInputArguments();
' #txt
Fs0 f44 inParameterMapAction 'out.returnedMessage=param.returnedMessage;
' #txt
Fs0 f44 outParameterDecl '<> result;
' #txt
Fs0 f44 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>_getUploadedFileFromUploadHandler(ReturnedMessage)</name>
        <nameStyle>50,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f44 358 566 20 20 -77 -35 #rect
Fs0 f44 @|RichDialogMethodStartIcon #fIcon
Fs0 f46 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData out;
' #txt
Fs0 f46 actionTable 'out=in;
' #txt
Fs0 f46 actionCode 'try{
	in.fileManagementHandler.insertDocuments(in.returnedMessage.getDocumentOnServers());
}catch(Throwable t)
{
	ivy.log.error("Error in FileUploadPreparer",t);
}' #txt
Fs0 f46 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f46 350 612 36 24 20 -2 #rect
Fs0 f46 @|RichDialogProcessStepIcon #fIcon
Fs0 f47 expr out #txt
Fs0 f47 368 586 368 612 #arcP
Fs0 f48 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f48 358 662 20 20 13 0 #rect
Fs0 f48 @|RichDialogProcessEndIcon #fIcon
Fs0 f49 expr out #txt
Fs0 f49 368 636 368 662 #arcP
Fs0 f45 guid 13909076AEC65BA5 #txt
Fs0 f45 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f45 method _uploadDone(List<java.io.File>) #txt
Fs0 f45 disableUIEvents false #txt
Fs0 f45 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<List<java.io.File> filesUploaded> param = methodEvent.getInputArguments();
' #txt
Fs0 f45 inActionCode 'import ch.ivyteam.ivy.addons.filemanager.FileHandler;
import ch.ivyteam.ivy.addons.filemanager.DocumentOnServer;

try{
	out.fileManagementHandler.insertFiles(param.filesUploaded,"","");
}catch(Throwable t)
{
		ivy.log.error("Error in FileUploadPreparer",t);
}

' #txt
Fs0 f45 outParameterDecl '<> result;
' #txt
Fs0 f45 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>_uploadDone(List&lt;File&gt;)</name>
        <nameStyle>23,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f45 470 598 20 20 13 0 #rect
Fs0 f45 @|RichDialogMethodStartIcon #fIcon
Fs0 f50 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f50 470 646 20 20 13 0 #rect
Fs0 f50 @|RichDialogProcessEndIcon #fIcon
Fs0 f51 expr out #txt
Fs0 f51 480 618 480 646 #arcP
Fs0 f52 guid 14195A6966E1D725 #txt
Fs0 f52 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f52 method _onUploadError(ch.ivyteam.ivy.addons.filemanager.ReturnedMessage) #txt
Fs0 f52 disableUIEvents false #txt
Fs0 f52 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.filemanager.ReturnedMessage returnedMessage> param = methodEvent.getInputArguments();
' #txt
Fs0 f52 inParameterMapAction 'out.returnedMessage=param.returnedMessage;
' #txt
Fs0 f52 outParameterDecl '<> result;
' #txt
Fs0 f52 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>_onUploadError(ReturnedMessage)</name>
        <nameStyle>31,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Fs0 f52 358 734 20 20 13 0 #rect
Fs0 f52 @|RichDialogMethodStartIcon #fIcon
Fs0 f53 targetWindow NEW:card: #txt
Fs0 f53 targetDisplay TOP #txt
Fs0 f53 richDialogId ch.ivyteam.ivy.addons.filemanager.util.MessageDialog #txt
Fs0 f53 startMethod start(String,String) #txt
Fs0 f53 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f53 requestActionDecl '<String text, String iconUri> param;' #txt
Fs0 f53 requestMappingAction 'param.iconUri="/ch/ivyteam/ivy/addons/icons/close/48";
' #txt
Fs0 f53 requestActionCode 'param.text=in.returnedMessage.text;
if(in.#returnedMessage.#file!=null) {
	param.text=param.text.replace("</html>","");
	param.text+="<br>"+in.returnedMessage.file.getName();
}
' #txt
Fs0 f53 responseActionDecl 'ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData out;
' #txt
Fs0 f53 responseMappingAction 'out=in;
' #txt
Fs0 f53 windowConfiguration '* ' #txt
Fs0 f53 isAsynch false #txt
Fs0 f53 isInnerRd true #txt
Fs0 f53 userContext '* ' #txt
Fs0 f53 350 780 36 24 20 -2 #rect
Fs0 f53 @|RichDialogIcon #fIcon
Fs0 f54 expr out #txt
Fs0 f54 368 754 368 780 #arcP
Fs0 f55 type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
Fs0 f55 358 838 20 20 13 0 #rect
Fs0 f55 @|RichDialogProcessEndIcon #fIcon
Fs0 f56 expr out #txt
Fs0 f56 368 804 368 838 #arcP
>Proto Fs0 .rdData2UIAction 'panel.deleteButton.enabled=IF(in.selectedFiles.isEmpty(), false, true);
panel.filesTable.listData=in.filesToUpload;
' #txt
>Proto Fs0 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <swimlaneLabel>Start methods</swimlaneLabel>
        <swimlaneLabel>Methods</swimlaneLabel>
        <swimlaneLabel>Events</swimlaneLabel>
        <swimlaneLabel></swimlaneLabel>
    </language>
    <swimlaneSize>250</swimlaneSize>
    <swimlaneSize>562</swimlaneSize>
    <swimlaneSize>497</swimlaneSize>
    <swimlaneColor>-6710785</swimlaneColor>
    <swimlaneColor>-26215</swimlaneColor>
    <swimlaneColor>-3342439</swimlaneColor>
</elementInfo>
' #txt
>Proto Fs0 .type ch.ivyteam.ivy.addons.filemanager.FileUploadPreparer.FileUploadPreparerData #txt
>Proto Fs0 .processKind RICH_DIALOG #txt
>Proto Fs0 -8 -8 16 16 16 26 #rect
>Proto Fs0 '' #fIcon
Fs0 f0 mainOut f2 tail #connect
Fs0 f2 head f1 mainIn #connect
Fs0 f3 mainOut f4 tail #connect
Fs0 f4 head f1 mainIn #connect
Fs0 f5 mainOut f11 tail #connect
Fs0 f11 head f10 mainIn #connect
Fs0 f10 mainOut f13 tail #connect
Fs0 f13 head f12 mainIn #connect
Fs0 f7 mainOut f15 tail #connect
Fs0 f15 head f14 mainIn #connect
Fs0 f14 mainOut f17 tail #connect
Fs0 f17 head f16 mainIn #connect
Fs0 f18 mainOut f20 tail #connect
Fs0 f20 head f19 mainIn #connect
Fs0 f23 mainOut f25 tail #connect
Fs0 f25 head f24 mainIn #connect
Fs0 f24 mainOut f27 tail #connect
Fs0 f27 head f26 mainIn #connect
Fs0 f28 mainOut f30 tail #connect
Fs0 f30 head f29 mainIn #connect
Fs0 f29 mainOut f32 tail #connect
Fs0 f32 head f31 mainIn #connect
Fs0 f36 mainOut f39 tail #connect
Fs0 f39 head f38 mainIn #connect
Fs0 f19 mainOut f22 tail #connect
Fs0 f22 head f21 mainIn #connect
Fs0 f33 mainOut f34 tail #connect
Fs0 f34 head f14 mainIn #connect
Fs0 f40 mainOut f41 tail #connect
Fs0 f41 head f36 mainIn #connect
Fs0 f35 mainOut f43 tail #connect
Fs0 f43 head f42 mainIn #connect
Fs0 f42 mainOut f37 tail #connect
Fs0 f37 head f1 mainIn #connect
Fs0 f6 mainOut f9 tail #connect
Fs0 f9 head f8 mainIn #connect
Fs0 f44 mainOut f47 tail #connect
Fs0 f47 head f46 mainIn #connect
Fs0 f46 mainOut f49 tail #connect
Fs0 f49 head f48 mainIn #connect
Fs0 f45 mainOut f51 tail #connect
Fs0 f51 head f50 mainIn #connect
Fs0 f52 mainOut f54 tail #connect
Fs0 f54 head f53 mainIn #connect
Fs0 f53 mainOut f56 tail #connect
Fs0 f56 head f55 mainIn #connect
