[Ivy]
[>Created: Thu Aug 28 10:36:14 EDT 2014]
132D1B65FEDF11D7 3.17 #module
>Proto >Proto Collection #zClass
Ds0 DirectorySecurityManagerProcess Big #zClass
Ds0 RD #cInfo
Ds0 #process
Ds0 @TextInP .ui2RdDataAction .ui2RdDataAction #zField
Ds0 @TextInP .rdData2UIAction .rdData2UIAction #zField
Ds0 @TextInP .resExport .resExport #zField
Ds0 @TextInP .type .type #zField
Ds0 @TextInP .processKind .processKind #zField
Ds0 @AnnotationInP-0n ai ai #zField
Ds0 @TextInP .xml .xml #zField
Ds0 @TextInP .responsibility .responsibility #zField
Ds0 @RichDialogInitStart f0 '' #zField
Ds0 @RichDialogProcessEnd f1 '' #zField
Ds0 @RichDialogProcessStep f3 '' #zField
Ds0 @RichDialogMethodStart f4 '' #zField
Ds0 @PushWFArc f5 '' #zField
Ds0 @RichDialogProcessEnd f6 '' #zField
Ds0 @RichDialogProcessStart f8 '' #zField
Ds0 @RichDialogProcessStep f9 '' #zField
Ds0 @PushWFArc f10 '' #zField
Ds0 @RichDialogProcessEnd f11 '' #zField
Ds0 @RichDialogInitStart f32 '' #zField
Ds0 @RichDialogProcessStep f33 '' #zField
Ds0 @PushWFArc f34 '' #zField
Ds0 @PushWFArc f35 '' #zField
Ds0 @RichDialogMethodStart f36 '' #zField
Ds0 @RichDialogMethodStart f40 '' #zField
Ds0 @RichDialogProcessStep f41 '' #zField
Ds0 @PushWFArc f42 '' #zField
Ds0 @RichDialog f43 '' #zField
Ds0 @PushWFArc f44 '' #zField
Ds0 @RichDialogProcessEnd f45 '' #zField
Ds0 @PushWFArc f46 '' #zField
Ds0 @RichDialogProcessStep f75 '' #zField
Ds0 @PushWFArc f76 '' #zField
Ds0 @RichDialogProcessEnd f77 '' #zField
Ds0 @RichDialogProcessStart f79 '' #zField
Ds0 @RichDialogFireEvent f80 '' #zField
Ds0 @PushWFArc f81 '' #zField
Ds0 @RichDialogProcessEnd f82 '' #zField
Ds0 @PushWFArc f83 '' #zField
Ds0 @RichDialogMethodStart f84 '' #zField
Ds0 @RichDialogProcessStep f85 '' #zField
Ds0 @PushWFArc f86 '' #zField
Ds0 @RichDialogProcessEnd f87 '' #zField
Ds0 @PushWFArc f88 '' #zField
Ds0 @RichDialogProcessStep f89 '' #zField
Ds0 @PushWFArc f90 '' #zField
Ds0 @PushWFArc f7 '' #zField
Ds0 @RichDialogMethodStart f91 '' #zField
Ds0 @RichDialogProcessStep f92 '' #zField
Ds0 @PushWFArc f93 '' #zField
Ds0 @RichDialogProcessEnd f94 '' #zField
Ds0 @PushWFArc f95 '' #zField
Ds0 @RichDialogProcessStep f64 '' #zField
Ds0 @PushWFArc f68 '' #zField
Ds0 @PushWFArc f2 '' #zField
Ds0 @RichDialogProcessStart f72 '' #zField
Ds0 @Alternative f73 '' #zField
Ds0 @PushWFArc f96 '' #zField
Ds0 @RichDialogProcessStep f97 '' #zField
Ds0 @RichDialogProcessEnd f99 '' #zField
Ds0 @PushWFArc f101 '' #zField
Ds0 @RichDialogProcessStep f102 '' #zField
Ds0 @PushWFArc f104 '' #zField
Ds0 @RichDialogProcessStep f100 '' #zField
Ds0 @PushWFArc f105 '' #zField
Ds0 @RichDialogFireEvent f13 '' #zField
Ds0 @PushWFArc f14 '' #zField
Ds0 @PushWFArc f15 '' #zField
Ds0 @PushWFArc f16 '' #zField
Ds0 @RichDialogInitStart f17 '' #zField
Ds0 @PushWFArc f18 '' #zField
Ds0 @PushWFArc f19 '' #zField
Ds0 @RichDialogProcessStep f20 '' #zField
Ds0 @PushWFArc f21 '' #zField
Ds0 @PushWFArc f12 '' #zField
>Proto Ds0 Ds0 DirectorySecurityManagerProcess #zField
Ds0 f0 guid 132D1B66000F11BB #txt
Ds0 f0 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f0 method start() #txt
Ds0 f0 disableUIEvents true #txt
Ds0 f0 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<> param = methodEvent.getInputArguments();
' #txt
Ds0 f0 outParameterDecl '<> result;
' #txt
Ds0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start()</name>
    </language>
</elementInfo>
' #txt
Ds0 f0 90 40 20 20 13 0 #rect
Ds0 f0 @|RichDialogInitStartIcon #fIcon
Ds0 f1 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f1 126 206 20 20 13 0 #rect
Ds0 f1 @|RichDialogProcessEndIcon #fIcon
Ds0 f3 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f3 actionTable 'out=in;
' #txt
Ds0 f3 actionCode 'import ch.ivyteam.ivy.addons.filemanager.database.security.IvyRoleHelper;


if(!in.isLoaded){
	try{ 
		import ch.ivyteam.ivy.addons.filemanager.database.security.DirectorySecurityController;
		out.version=(in.securityController.getSecurityController() as DirectorySecurityController).getSecurityVersion();
			out.ivyRolesTree=IvyRoleHelper.buildIvyRolesTree();
			out.isLoaded=true;
			
	}catch(Throwable t){
		out.error.errorOccurred=true;
		out.error.throwable=t;
		out.error.message = t.getMessage();
	}
}

out.isVisible=!in.isVisible;

' #txt
Ds0 f3 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f3 318 92 36 24 20 -2 #rect
Ds0 f3 @|RichDialogProcessStepIcon #fIcon
Ds0 f4 guid 132D1C1EEB662BC2 #txt
Ds0 f4 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f4 method load() #txt
Ds0 f4 disableUIEvents false #txt
Ds0 f4 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>load()</name>
    </language>
</elementInfo>
' #txt
Ds0 f4 326 38 20 20 13 0 #rect
Ds0 f4 @|RichDialogMethodStartIcon #fIcon
Ds0 f5 expr out #txt
Ds0 f5 336 58 336 92 #arcP
Ds0 f6 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f6 326 190 20 20 13 0 #rect
Ds0 f6 @|RichDialogProcessEndIcon #fIcon
Ds0 f8 guid 132D41EFC77B64BF #txt
Ds0 f8 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f8 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f8 actionTable 'out=in;
' #txt
Ds0 f8 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>roleSelection</name>
        <nameStyle>13,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f8 462 38 20 20 13 0 #rect
Ds0 f8 @|RichDialogProcessStartIcon #fIcon
Ds0 f9 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f9 actionTable 'out=in;
' #txt
Ds0 f9 actionCode 'import ch.ivyteam.ivy.security.IRole;

if(panel.rolesTree.getSelectedTreeNode() != null && panel.rolesTree.getSelectedTreeNode().value != null && panel.rolesTree.getSelectedTreeNode().value instanceof IRole){
	out.selectedIRole = panel.rolesTree.getSelectedTreeNode().value as IRole;
	if(in.#folderOnServer!=null && in.folderOnServer.id>0){
		panel.actionsGridBagLayoutPane.enabled=true;
	}else{
		panel.actionsGridBagLayoutPane.enabled=false;
	}
	panel.mapSelectedIRoleRights();
}else{
	panel.actionsGridBagLayoutPane.enabled=false;
	out.selectedIRole = null;
	}' #txt
Ds0 f9 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f9 454 92 36 24 20 -2 #rect
Ds0 f9 @|RichDialogProcessStepIcon #fIcon
Ds0 f10 expr out #txt
Ds0 f10 472 58 472 92 #arcP
Ds0 f11 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f11 462 182 20 20 13 0 #rect
Ds0 f11 @|RichDialogProcessEndIcon #fIcon
Ds0 f32 guid 132D463DF0C46A88 #txt
Ds0 f32 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f32 method start(String,String,String) #txt
Ds0 f32 disableUIEvents true #txt
Ds0 f32 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.String ivyDBConnexionName,java.lang.String dirTableName,java.lang.String schemaName> param = methodEvent.getInputArguments();
' #txt
Ds0 f32 inParameterMapAction 'out.config.activateSecurity=true;
out.config.databaseSchemaName=param.schemaName;
out.config.directoriesTableName=param.dirTableName;
out.config.ivyDBConnectionName=param.ivyDBConnexionName;
out.config.storeFilesInDB=true;
' #txt
Ds0 f32 outParameterDecl '<> result;
' #txt
Ds0 f32 embeddedRdInitializations '* ' #txt
Ds0 f32 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(String,String,String)</name>
        <nameStyle>27,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f32 174 46 20 20 -52 -28 #rect
Ds0 f32 @|RichDialogInitStartIcon #fIcon
Ds0 f33 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f33 actionTable 'out=in;
' #txt
Ds0 f33 actionCode 'import ch.ivyteam.ivy.addons.filemanager.FileManagementHandlersFactory;
import ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController;
import ch.ivyteam.ivy.security.IRole;


in.securityController = FileManagementHandlersFactory.getFileSecurityHandlerInstance(in.config);

panel.actionsGridBagLayoutPane.enabled=false;
try{
	out.selectedIRole = panel.rolesTree.getTreeData().value as IRole;
}catch(Throwable t)
{
		
}
' #txt
Ds0 f33 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f33 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>make securityController</name>
        <nameStyle>23,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f33 118 108 36 24 20 -2 #rect
Ds0 f33 @|RichDialogProcessStepIcon #fIcon
Ds0 f34 expr out #txt
Ds0 f34 178 64 145 108 #arcP
Ds0 f35 expr out #txt
Ds0 f35 104 58 130 108 #arcP
Ds0 f36 guid 132D469EE2FDD752 #txt
Ds0 f36 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f36 method mapSelectedIRoleRights() #txt
Ds0 f36 disableUIEvents false #txt
Ds0 f36 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>mapSelectedIRoleRights()</name>
    </language>
</elementInfo>
' #txt
Ds0 f36 462 230 20 20 13 0 #rect
Ds0 f36 @|RichDialogMethodStartIcon #fIcon
Ds0 f40 guid 132D46E5157A7030 #txt
Ds0 f40 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f40 method showError() #txt
Ds0 f40 disableUIEvents false #txt
Ds0 f40 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>showError()</name>
    </language>
</elementInfo>
' #txt
Ds0 f40 286 414 20 20 13 0 #rect
Ds0 f40 @|RichDialogMethodStartIcon #fIcon
Ds0 f41 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f41 actionTable 'out=in;
' #txt
Ds0 f41 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f41 278 468 36 24 20 -2 #rect
Ds0 f41 @|RichDialogProcessStepIcon #fIcon
Ds0 f42 expr out #txt
Ds0 f42 296 434 296 468 #arcP
Ds0 f43 targetWindow NEW:card: #txt
Ds0 f43 targetDisplay TOP #txt
Ds0 f43 richDialogId ch.ivyteam.ivy.addons.commondialogs.QuestionDialog #txt
Ds0 f43 startMethod askQuestion(String,String,List<String>,String) #txt
Ds0 f43 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f43 requestActionDecl '<String icon, String question, List<String> buttons, String defaultButton> param;' #txt
Ds0 f43 requestMappingAction 'param.icon="error";
param.question=in.error.message+" \n"+IF(in.#error.#throwable!=null,in.error.throwable.getMessage(),"");
param.buttons=[ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/ok")];
param.defaultButton=ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/buttonLabels/ok");
' #txt
Ds0 f43 requestActionCode 'if(in.#error.#throwable!=null){
	ivy.log.error(in.error.throwable.getMessage(),in.error.throwable);
}' #txt
Ds0 f43 responseActionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f43 responseMappingAction 'out=in;
' #txt
Ds0 f43 windowConfiguration '{/title "\"Error\""/width 0 /height 0 /centered true /resizable true /maximized false /close_after_last_rd true }' #txt
Ds0 f43 isAsynch false #txt
Ds0 f43 isInnerRd true #txt
Ds0 f43 userContext '* ' #txt
Ds0 f43 278 524 36 24 20 -2 #rect
Ds0 f43 @|RichDialogIcon #fIcon
Ds0 f44 expr out #txt
Ds0 f44 296 492 296 524 #arcP
Ds0 f45 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f45 286 590 20 20 13 0 #rect
Ds0 f45 @|RichDialogProcessEndIcon #fIcon
Ds0 f46 expr out #txt
Ds0 f46 296 548 296 590 #arcP
Ds0 f75 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f75 actionTable 'out=in;
' #txt
Ds0 f75 actionCode 'import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityHandler;
try{
		if(in.#folderOnServer!=null && in.folderOnServer.path.trim().length()>0 && in.#selectedIRole!= null && in.selectedIRole.getName().trim().length()>0){	
			String role = in.selectedIRole.getName();
			in.isSelectedRoleAdmin = role.compareTo(in.config.adminRole)==0;
			panel.adminCheckBox.selected= in.isSelectedRoleAdmin || in.folderOnServer.cmrd.contains(role);
			
			panel.codCheckBox.selected= in.isSelectedRoleAdmin || in.folderOnServer.cod.contains(role);
			panel.cudCheckBox.selected= in.isSelectedRoleAdmin || in.folderOnServer.cud.contains(role);
			panel.cddCheckBox.selected= in.isSelectedRoleAdmin ||in.folderOnServer.cdd.contains(role);
			panel.cwfCheckBox.selected= in.isSelectedRoleAdmin ||in.folderOnServer.cwf.contains(role);
			panel.cdfCheckBox.selected= in.isSelectedRoleAdmin ||in.folderOnServer.cdf.contains(role);
			if(in.version>1){
				panel.crdCheckBox.selected= in.isSelectedRoleAdmin ||in.folderOnServer.crd.contains(role);
				panel.ccdCheckBox.selected= in.isSelectedRoleAdmin ||in.folderOnServer.ccd.contains(role);
				panel.ctdCheckBox.selected= in.isSelectedRoleAdmin ||in.folderOnServer.ctd.contains(role);
				panel.cufCheckBox.selected= in.isSelectedRoleAdmin ||in.folderOnServer.cuf.contains(role);
				panel.ccfCheckBox.selected= in.isSelectedRoleAdmin ||in.folderOnServer.ccf.contains(role);
			}
			
			panel.actionsGridBagLayoutPane.enabled=!in.isSelectedRoleAdmin;
		}else {
			in.isSelectedRoleAdmin = false;
		}
	}catch(Throwable t){
		in.error.errorOccurred=true;
		in.error.message=t.getMessage();
		in.error.throwable=t;
		panel.showError();
}
panel._makeSelectedChekBoxesConsistent();' #txt
Ds0 f75 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f75 454 284 36 24 20 -2 #rect
Ds0 f75 @|RichDialogProcessStepIcon #fIcon
Ds0 f76 expr out #txt
Ds0 f76 472 250 472 284 #arcP
Ds0 f77 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f77 462 342 20 20 13 0 #rect
Ds0 f77 @|RichDialogProcessEndIcon #fIcon
Ds0 f79 guid 132D48B28BEB3352 #txt
Ds0 f79 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f79 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f79 actionTable 'out=in;
' #txt
Ds0 f79 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>hide</name>
        <nameStyle>4,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f79 1326 46 20 20 13 0 #rect
Ds0 f79 @|RichDialogProcessStartIcon #fIcon
Ds0 f80 actionCode panel.fireHiddeMe(); #txt
Ds0 f80 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f80 fireEvent hiddeMe() #txt
Ds0 f80 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>hiddeMe@Subsc</name>
        <nameStyle>13
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f80 1318 100 36 24 20 -2 #rect
Ds0 f80 @|RichDialogFireEventIcon #fIcon
Ds0 f81 expr out #txt
Ds0 f81 1336 66 1336 100 #arcP
Ds0 f82 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f82 1326 158 20 20 13 0 #rect
Ds0 f82 @|RichDialogProcessEndIcon #fIcon
Ds0 f83 expr out #txt
Ds0 f83 1336 124 1336 158 #arcP
Ds0 f84 guid 132DE345CF8D108B #txt
Ds0 f84 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f84 method setSelectedFolderOnServer(ch.ivyteam.ivy.addons.filemanager.FolderOnServer) #txt
Ds0 f84 disableUIEvents false #txt
Ds0 f84 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.filemanager.FolderOnServer selectedFolderOnServer> param = methodEvent.getInputArguments();
' #txt
Ds0 f84 inParameterMapAction 'out.folderOnServer=param.selectedFolderOnServer;
' #txt
Ds0 f84 outParameterDecl '<> result;
' #txt
Ds0 f84 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>setSelectedFolderOnServer(FolderOnServer)</name>
        <nameStyle>41,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f84 462 462 20 20 13 0 #rect
Ds0 f84 @|RichDialogMethodStartIcon #fIcon
Ds0 f85 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f85 actionTable 'out=in;
' #txt
Ds0 f85 actionCode 'import ch.ivyteam.ivy.security.IRole;

if(in.isVisible){
	panel.mapSelectedIRoleRights();
	if(in.#selectedIRole!= null && in.selectedIRole.getName().trim().length()>0){	
			panel.actionsGridBagLayoutPane.enabled=in.selectedIRole.getName().compareTo(in.config.adminRole)!=0;
	}
	if(panel.adminCheckBox.selected) {
		panel.codCheckBox.enabled=false;
		panel.cudCheckBox.enabled=false;
		panel.cddCheckBox.enabled=false;
		panel.cudCheckBox.enabled=false;
		panel.crdCheckBox.enabled=false;
		panel.ccdCheckBox.enabled=false;
		panel.ctdCheckBox.enabled=false;
		panel.ccfCheckBox.enabled=false;
		panel.cufCheckBox.enabled=false;
		panel.cwfCheckBox.enabled=false;
		panel.cdfCheckBox.enabled=false;
		panel.codCheckBox.selected=true;
		panel.cudCheckBox.selected=true;
		panel.cddCheckBox.selected=true;
		panel.cudCheckBox.selected=true;
		panel.crdCheckBox.selected=true;
		panel.ccdCheckBox.selected=true;
		panel.ctdCheckBox.selected=true;
		panel.ccfCheckBox.selected=true;
		panel.cufCheckBox.selected=true;
		panel.cwfCheckBox.selected=true;
		panel.cdfCheckBox.selected=true;
	}else{
		panel._makeSelectedChekBoxesConsistent();
	}
}

' #txt
Ds0 f85 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f85 454 516 36 24 20 -2 #rect
Ds0 f85 @|RichDialogProcessStepIcon #fIcon
Ds0 f86 expr out #txt
Ds0 f86 472 482 472 516 #arcP
Ds0 f87 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f87 462 574 20 20 13 0 #rect
Ds0 f87 @|RichDialogProcessEndIcon #fIcon
Ds0 f88 expr out #txt
Ds0 f88 472 540 472 574 #arcP
Ds0 f89 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f89 actionTable 'out=in;
' #txt
Ds0 f89 actionCode 'import ch.ivyteam.ivy.security.IRole;
try{
	if(in.#selectedIRole==null)
	{
		ivy.log.info(in.ivyRolesTree.getRoot().toString()+" "+in.ivyRolesTree.getRoot().getValue());
		out.selectedIRole = in.ivyRolesTree.getRoot().getValue() as IRole;
	}
}catch(Throwable t)
{
		ivy.log.error("Error in DirectorySecurity Manager, load(), cannot select the root IRole",t);
}
if(in.isVisible){
	panel.mapSelectedIRoleRights();
}

' #txt
Ds0 f89 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f89 318 132 36 24 20 -2 #rect
Ds0 f89 @|RichDialogProcessStepIcon #fIcon
Ds0 f90 expr out #txt
Ds0 f90 336 116 336 132 #arcP
Ds0 f7 expr out #txt
Ds0 f7 336 156 336 190 #arcP
Ds0 f91 guid 132F87FC44BAF0F8 #txt
Ds0 f91 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f91 method _makeSelectedChekBoxesConsistent() #txt
Ds0 f91 disableUIEvents false #txt
Ds0 f91 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>_makeSelectedChekBoxesConsistent()</name>
    </language>
</elementInfo>
' #txt
Ds0 f91 862 422 20 20 -101 -31 #rect
Ds0 f91 @|RichDialogMethodStartIcon #fIcon
Ds0 f92 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f92 actionTable 'out=in;
' #txt
Ds0 f92 actionCode 'if(panel.adminCheckBox.selected) {
	panel.codCheckBox.enabled=false;
	panel.cudCheckBox.enabled=false;
	panel.cddCheckBox.enabled=false;
	panel.cudCheckBox.enabled=false;
	panel.crdCheckBox.enabled=false;
	panel.ccdCheckBox.enabled=false;
	panel.ctdCheckBox.enabled=false;
	panel.ccfCheckBox.enabled=false;
	panel.cufCheckBox.enabled=false;
	panel.cwfCheckBox.enabled=false;
	panel.cdfCheckBox.enabled=false;
	panel.codCheckBox.selected=true;
	panel.cudCheckBox.selected=true;
	panel.cddCheckBox.selected=true;
	panel.cudCheckBox.selected=true;
	panel.crdCheckBox.selected=true;
	panel.ccdCheckBox.selected=true;
	panel.ctdCheckBox.selected=true;
	panel.ccfCheckBox.selected=true;
	panel.cufCheckBox.selected=true;
	panel.cwfCheckBox.selected=true;
	panel.cdfCheckBox.selected=true;
} else {
	
	panel.codCheckBox.enabled=true;
	panel.cudCheckBox.enabled=true;
	panel.cddCheckBox.enabled=true;
	panel.cudCheckBox.enabled=true;
	panel.crdCheckBox.enabled=true;
	panel.ccdCheckBox.enabled=true;
	panel.ctdCheckBox.enabled=true;
	panel.ccfCheckBox.enabled=true;
	panel.cufCheckBox.enabled=true;
	panel.cwfCheckBox.enabled=true;
	panel.cdfCheckBox.enabled=true;
	
	if(panel.cwfCheckBox.selected){
		panel.cufCheckBox.selected=true;
		panel.cufCheckBox.enabled=false;
		panel.ccfCheckBox.selected=true;
		panel.ccfCheckBox.enabled=false;
	}else if(in.version==1){
		panel.cufCheckBox.selected=false;
		panel.ccfCheckBox.selected=false;
	}
	if(panel.cudCheckBox.selected){
		panel.ccdCheckBox.selected=true;
		panel.ccdCheckBox.enabled=false;
		panel.crdCheckBox.selected=true;
		panel.crdCheckBox.enabled=false;
	}else if(in.version==1){
		panel.ccdCheckBox.selected=false;
		panel.crdCheckBox.selected=false;
	}
	if(panel.cudCheckBox.selected || panel.crdCheckBox.selected || panel.ccdCheckBox.selected || panel.ctdCheckBox.selected || 
			panel.cddCheckBox.selected || panel.cwfCheckBox.selected || panel.ccfCheckBox.selected || panel.cufCheckBox.selected || panel.cdfCheckBox.selected){
		panel.codCheckBox.selected=true;
		panel.codCheckBox.enabled=false;
		
		if(panel.cddCheckBox.selected){
			panel.cudCheckBox.selected=true;
			panel.cudCheckBox.enabled=false;
			panel.ccdCheckBox.selected=true;
			panel.ccdCheckBox.enabled=false;
			panel.crdCheckBox.selected=true;
			panel.crdCheckBox.enabled=false;
		} else if(panel.ctdCheckBox.selected){
			panel.crdCheckBox.selected=true;
			panel.crdCheckBox.enabled=false;
		}
		
		if(panel.cdfCheckBox.selected){
			panel.cwfCheckBox.selected=true;
			panel.cwfCheckBox.enabled=false;
			panel.cufCheckBox.selected=true;
			panel.cufCheckBox.enabled=false;
			panel.ccfCheckBox.selected=true;
			panel.ccfCheckBox.enabled=false;
		}
	}
}
' #txt
Ds0 f92 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f92 854 476 36 24 20 -2 #rect
Ds0 f92 @|RichDialogProcessStepIcon #fIcon
Ds0 f93 expr out #txt
Ds0 f93 872 442 872 476 #arcP
Ds0 f94 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f94 862 534 20 20 13 0 #rect
Ds0 f94 @|RichDialogProcessEndIcon #fIcon
Ds0 f95 expr out #txt
Ds0 f95 872 500 872 534 #arcP
Ds0 f64 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f64 actionTable 'out=in;
' #txt
Ds0 f64 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f64 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>get the 
security version</name>
        <nameStyle>25,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f64 118 156 36 24 20 -2 #rect
Ds0 f64 @|RichDialogProcessStepIcon #fIcon
Ds0 f68 expr out #txt
Ds0 f68 136 132 136 156 #arcP
Ds0 f2 expr out #txt
Ds0 f2 136 180 136 206 #arcP
Ds0 f72 guid 13F1C25223748BF5 #txt
Ds0 f72 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f72 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f72 actionTable 'out=in;
' #txt
Ds0 f72 actionCode 'import ch.ivyteam.ivy.addons.filemanager.database.security.AbstractDirectorySecurityController;
import ch.ivyteam.ivy.addons.filemanager.database.security.SecurityRightsEnum;
import ch.ivyteam.ivy.richdialog.widgets.components.RCheckBox;
import ch.ivyteam.ivy.richdialog.exec.RdEvent;

Object source = event.getSource();
out.rightType=-1;
if(source instanceof RCheckBox){
	RCheckBox chb = source as RCheckBox;
	in.shouldAddRight=chb.isSelected();
	if(chb.getName().compareTo(panel.adminCheckBox.getName())==0){
		out.rightType = AbstractDirectorySecurityController.MANAGE_SECURITY_RIGHT;
	} else if(chb.getName().compareTo(panel.codCheckBox.getName())==0){
		out.rightType = AbstractDirectorySecurityController.OPEN_DIRECTORY_RIGHT;
	} else if(chb.getName().compareTo(panel.cudCheckBox.getName())==0){
		out.rightType = AbstractDirectorySecurityController.UPDATE_DIRECTORY_RIGHT;
	} else if(chb.getName().compareTo(panel.crdCheckBox.getName())==0){
		out.rightType = AbstractDirectorySecurityController.RENAME_DIRECTORY_RIGHT;
	} else if(chb.getName().compareTo(panel.ccdCheckBox.getName())==0){
		out.rightType = AbstractDirectorySecurityController.CREATE_DIRECTORY_RIGHT;
	} else if(chb.getName().compareTo(panel.ctdCheckBox.getName())==0){
		out.rightType = AbstractDirectorySecurityController.TRANSLATE_DIRECTORY_RIGHT;
	} else if(chb.getName().compareTo(panel.cddCheckBox.getName())==0){
		out.rightType = AbstractDirectorySecurityController.DELETE_DIRECTORY_RIGHT;
	} else if(chb.getName().compareTo(panel.cwfCheckBox.getName())==0){
		out.rightType = AbstractDirectorySecurityController.WRITE_FILES_RIGHT;
	} else if(chb.getName().compareTo(panel.ccfCheckBox.getName())==0){
		out.rightType = AbstractDirectorySecurityController.CREATE_FILES_RIGHT;
	} else if(chb.getName().compareTo(panel.cufCheckBox.getName())==0){
		out.rightType = AbstractDirectorySecurityController.UPDATE_FILES_RIGHT;
	} else if(chb.getName().compareTo(panel.cdfCheckBox.getName())==0){
		out.rightType = AbstractDirectorySecurityController.DELETE_FILES_RIGHT;
	}
}' #txt
Ds0 f72 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>checkBoxStateChange</name>
        <nameStyle>19,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f72 758 30 20 20 -62 -28 #rect
Ds0 f72 @|RichDialogProcessStartIcon #fIcon
Ds0 f73 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f73 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>right type null?</name>
        <nameStyle>16,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f73 754 74 28 28 14 0 #rect
Ds0 f73 @|AlternativeIcon #fIcon
Ds0 f96 expr out #txt
Ds0 f96 768 50 768 74 #arcP
Ds0 f97 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f97 actionTable 'out=in;
' #txt
Ds0 f97 actionCode 'out.folderOnServer = in.securityController.saveFolderOnServer(in.folderOnServer);
' #txt
Ds0 f97 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f97 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>save the folderonserver</name>
        <nameStyle>23,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f97 750 252 36 24 20 -2 #rect
Ds0 f97 @|RichDialogProcessStepIcon #fIcon
Ds0 f99 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f99 758 358 20 20 13 0 #rect
Ds0 f99 @|RichDialogProcessEndIcon #fIcon
Ds0 f101 expr in #txt
Ds0 f101 754 88 758 368 #arcP
Ds0 f101 1 704 88 #addKink
Ds0 f101 2 704 368 #addKink
Ds0 f101 1 0.3315683917577097 0 0 #arcLabel
Ds0 f102 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f102 actionTable 'out=in;
' #txt
Ds0 f102 actionCode 'if(panel.adminCheckBox.selected) {
	panel.codCheckBox.enabled=false;
	panel.cudCheckBox.enabled=false;
	panel.cddCheckBox.enabled=false;
	panel.cudCheckBox.enabled=false;
	panel.cwfCheckBox.enabled=false;
	panel.cdfCheckBox.enabled=false;
	
	panel.codCheckBox.selected=true;
	panel.cudCheckBox.selected=true;
	panel.cddCheckBox.selected=true;
	panel.cudCheckBox.selected=true;
	panel.cwfCheckBox.selected=true;
	panel.cdfCheckBox.selected=true;
	
	if(in.version>1) {
		panel.crdCheckBox.enabled=false;
		panel.ccdCheckBox.enabled=false;
		panel.ctdCheckBox.enabled=false;
		panel.ccfCheckBox.enabled=false;
		panel.cufCheckBox.enabled=false;
		
		panel.crdCheckBox.selected=true;
		panel.ccdCheckBox.selected=true;
		panel.ctdCheckBox.selected=true;
		panel.ccfCheckBox.selected=true;
		panel.cufCheckBox.selected=true;
	}
} else {
	panel.codCheckBox.enabled=true;
	panel.cudCheckBox.enabled=true;
	panel.cddCheckBox.enabled=true;
	panel.cudCheckBox.enabled=true;
	panel.crdCheckBox.enabled=true;
	panel.ccdCheckBox.enabled=true;
	panel.ctdCheckBox.enabled=true;
	panel.ccfCheckBox.enabled=true;
	panel.cufCheckBox.enabled=true;
	panel.cwfCheckBox.enabled=true;
	panel.cdfCheckBox.enabled=true;
	
	if(panel.cwfCheckBox.selected){
		panel.cufCheckBox.selected=true;
		panel.cufCheckBox.enabled=false;
		panel.ccfCheckBox.selected=true;
		panel.ccfCheckBox.enabled=false;
	}
	if(panel.cudCheckBox.selected){
		panel.ccdCheckBox.selected=true;
		panel.ccdCheckBox.enabled=false;
		panel.crdCheckBox.selected=true;
		panel.crdCheckBox.enabled=false;
	}
			
	if(panel.cudCheckBox.selected || panel.crdCheckBox.selected || panel.ccdCheckBox.selected || panel.ctdCheckBox.selected || 
			panel.cddCheckBox.selected || panel.cwfCheckBox.selected || panel.ccfCheckBox.selected || panel.cufCheckBox.selected || panel.cdfCheckBox.selected){
		panel.codCheckBox.selected=true;
		panel.codCheckBox.enabled=false;
			
		if(panel.cddCheckBox.selected){
			panel.cudCheckBox.selected=true;
			panel.cudCheckBox.enabled=false;
			panel.ccdCheckBox.selected=true;
			panel.ccdCheckBox.enabled=false;
			panel.crdCheckBox.selected=true;
			panel.crdCheckBox.enabled=false;
		} else if(panel.ctdCheckBox.selected){
			panel.crdCheckBox.selected=true;
			panel.crdCheckBox.enabled=false;
		}
		
		if(panel.cdfCheckBox.selected){
			panel.cwfCheckBox.selected=true;
			panel.cwfCheckBox.enabled=false;
			panel.cufCheckBox.selected=true;
			panel.cufCheckBox.enabled=false;
			panel.ccfCheckBox.selected=true;
			panel.ccfCheckBox.enabled=false;
		}
	}
}' #txt
Ds0 f102 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f102 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>set consistency by checkboxes selection</name>
        <nameStyle>39,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f102 750 132 36 24 20 -2 #rect
Ds0 f102 @|RichDialogProcessStepIcon #fIcon
Ds0 f104 expr in #txt
Ds0 f104 outCond 'in.rightType !=-1' #txt
Ds0 f104 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>NO</name>
        <nameStyle>2,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f104 768 102 768 132 #arcP
Ds0 f100 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f100 actionTable 'out=in;
' #txt
Ds0 f100 actionCode 'String rolename = in.selectedIRole.getName();

if(panel.codCheckBox.selected){
	if(!in.folderOnServer.cod.contains(rolename)){
		in.folderOnServer.cod.add(rolename);
	}
	if(panel.adminCheckBox.selected){
		if(!in.folderOnServer.cmrd.contains(rolename)){
			in.folderOnServer.cmrd.add(rolename);
		}
	}else{
		in.folderOnServer.cmrd.remove(rolename);
	}
	if(panel.cudCheckBox.selected){
		if(!in.folderOnServer.cud.contains(rolename)){
			in.folderOnServer.cud.add(rolename);
		}
	}else{
		in.folderOnServer.cud.remove(rolename);
	}
	if(panel.cddCheckBox.selected){
		if(!in.folderOnServer.cdd.contains(rolename)){
			in.folderOnServer.cdd.add(rolename);
		}
	}else{
		in.folderOnServer.cdd.remove(rolename);
	}
	if(panel.cwfCheckBox.selected){
		if(!in.folderOnServer.cwf.contains(rolename)){
			in.folderOnServer.cwf.add(rolename);
		}
	}else{
		in.folderOnServer.cwf.remove(rolename);
	}
	if(panel.cdfCheckBox.selected){
		if(!in.folderOnServer.cdf.contains(rolename)){
			in.folderOnServer.cdf.add(rolename);
		}
	}else{
		in.folderOnServer.cdf.remove(rolename);
	}
	if(in.version>1){
		if(panel.ctdCheckBox.selected){
			if(!in.folderOnServer.ctd.contains(rolename)){
				in.folderOnServer.ctd.add(rolename);
			}
		}else{
			in.folderOnServer.ctd.remove(rolename);
		}
		if(panel.crdCheckBox.selected){
			if(!in.folderOnServer.crd.contains(rolename)){
				in.folderOnServer.crd.add(rolename);
			}
		}else{
			in.folderOnServer.crd.remove(rolename);
		}
		if(panel.cufCheckBox.selected){
			if(!in.folderOnServer.cuf.contains(rolename)){
				in.folderOnServer.cuf.add(rolename);
			}
		}else{
			in.folderOnServer.cuf.remove(rolename);
		}
		if(panel.ccfCheckBox.selected){
			if(!in.folderOnServer.ccf.contains(rolename)){
				in.folderOnServer.ccf.add(rolename);
			}
		}else{
			in.folderOnServer.ccf.remove(rolename);
		}
		if(panel.ccdCheckBox.selected){
			if(!in.folderOnServer.ccd.contains(rolename)){
				in.folderOnServer.ccd.add(rolename);
			}
		}else{
			in.folderOnServer.ccd.remove(rolename);
		}
	}
}else{
	in.folderOnServer.cmrd.remove(rolename);
	in.folderOnServer.cdd.remove(rolename);
	in.folderOnServer.ctd.remove(rolename);
	in.folderOnServer.crd.remove(rolename);
	in.folderOnServer.ccd.remove(rolename);
	in.folderOnServer.cud.remove(rolename);
	in.folderOnServer.cod.remove(rolename);
	in.folderOnServer.cuf.remove(rolename);
	in.folderOnServer.cwf.remove(rolename);
	in.folderOnServer.ccf.remove(rolename);
	in.folderOnServer.cdf.remove(rolename);
}
' #txt
Ds0 f100 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f100 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>map folderonserver</name>
        <nameStyle>18,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f100 750 180 36 24 20 -2 #rect
Ds0 f100 @|RichDialogProcessStepIcon #fIcon
Ds0 f105 expr out #txt
Ds0 f105 768 156 768 180 #arcP
Ds0 f13 actionDecl 'ch.ivyteam.ivy.addons.filemanager.FolderOnServer folder;
' #txt
Ds0 f13 actionTable 'folder=in.folderOnServer;
' #txt
Ds0 f13 actionCode panel.fireFolderRightsChanged(folder); #txt
Ds0 f13 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f13 fireEvent folderRightsChanged(ch.ivyteam.ivy.addons.filemanager.FolderOnServer) #txt
Ds0 f13 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>folderRightsChanged@SUBSC</name>
        <nameStyle>25,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f13 750 300 36 24 20 -2 #rect
Ds0 f13 @|RichDialogFireEventIcon #fIcon
Ds0 f14 expr out #txt
Ds0 f14 768 276 768 300 #arcP
Ds0 f15 expr out #txt
Ds0 f15 768 324 768 358 #arcP
Ds0 f16 expr out #txt
Ds0 f16 768 204 768 252 #arcP
Ds0 f17 guid 140A511139181B2D #txt
Ds0 f17 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f17 method start(ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController) #txt
Ds0 f17 disableUIEvents true #txt
Ds0 f17 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.filemanager.configuration.BasicConfigurationController configuration> param = methodEvent.getInputArguments();
' #txt
Ds0 f17 inParameterMapAction 'out.config=param.configuration;
' #txt
Ds0 f17 outParameterDecl '<> result;
' #txt
Ds0 f17 embeddedRdInitializations '* ' #txt
Ds0 f17 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(BasicConfigurationController)</name>
        <nameStyle>35,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f17 30 286 20 20 13 0 #rect
Ds0 f17 @|RichDialogInitStartIcon #fIcon
Ds0 f18 expr out #txt
Ds0 f18 40 286 118 120 #arcP
Ds0 f18 1 40 120 #addKink
Ds0 f18 0 0.7688011836477041 0 0 #arcLabel
Ds0 f19 expr out #txt
Ds0 f19 472 308 472 342 #arcP
Ds0 f20 actionDecl 'ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData out;
' #txt
Ds0 f20 actionTable 'out=in;
' #txt
Ds0 f20 actionCode 'if(in.#selectedIRole!=null && in.selectedIRole.getName().compareTo(in.config.adminRole)==0){
	panel.actionsGridBagLayoutPane.enabled=false;
}else {
	panel._makeSelectedChekBoxesConsistent();
}' #txt
Ds0 f20 type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
Ds0 f20 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>if adminRole selected
disable</name>
        <nameStyle>22,7
7,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f20 454 140 36 24 20 -2 #rect
Ds0 f20 @|RichDialogProcessStepIcon #fIcon
Ds0 f21 expr out #txt
Ds0 f21 472 116 472 140 #arcP
Ds0 f12 expr out #txt
Ds0 f12 472 164 472 182 #arcP
>Proto Ds0 .rdData2UIAction 'panel.ccdCheckBox.visible=in.version>1;
panel.ccfCheckBox.visible=in.version>1;
panel.crdCheckBox.visible=in.version>1;
panel.ctdCheckBox.visible=in.version>1;
panel.cufCheckBox.visible=in.version>1;
panel.filesGridLayoutPane.visible=in.version>1;
panel.Label.text=IF(in.folderOnServer.name.trim().length()>0, in.folderOnServer.name,"");
panel.ManagedActionsLabel.text=IF(in.#selectedIRole!=null && in.selectedIRole.getName().length()>0, 
ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/plainStrings/allowedActions")+" "+
ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/plainStrings/forRole") +" "+in.selectedIRole.getName(),
ivy.cms.co("/ch/ivyteam/ivy/addons/filemanager/fileManagement/plainStrings/allowedActions")
);
panel.rolesTree.treeData=in.ivyRolesTree;
panel.updatedirectoryGridLayoutPane.visible=in.version>1;
panel.adminCheckBox.enabled=!in.isSelectedRoleAdmin && (in.#folderOnServer!=null && in.#selectedIRole!=null && in.folderOnServer.cmrd.contains(in.selectedIRole.getName()));
' #txt
>Proto Ds0 .type ch.ivyteam.ivy.addons.filemanager.DirectorySecurityManager.DirectorySecurityManagerData #txt
>Proto Ds0 .processKind RICH_DIALOG #txt
>Proto Ds0 -8 -8 16 16 16 26 #rect
>Proto Ds0 '' #fIcon
Ds0 f4 mainOut f5 tail #connect
Ds0 f5 head f3 mainIn #connect
Ds0 f8 mainOut f10 tail #connect
Ds0 f10 head f9 mainIn #connect
Ds0 f32 mainOut f34 tail #connect
Ds0 f34 head f33 mainIn #connect
Ds0 f0 mainOut f35 tail #connect
Ds0 f35 head f33 mainIn #connect
Ds0 f40 mainOut f42 tail #connect
Ds0 f42 head f41 mainIn #connect
Ds0 f41 mainOut f44 tail #connect
Ds0 f44 head f43 mainIn #connect
Ds0 f43 mainOut f46 tail #connect
Ds0 f46 head f45 mainIn #connect
Ds0 f36 mainOut f76 tail #connect
Ds0 f76 head f75 mainIn #connect
Ds0 f79 mainOut f81 tail #connect
Ds0 f81 head f80 mainIn #connect
Ds0 f80 mainOut f83 tail #connect
Ds0 f83 head f82 mainIn #connect
Ds0 f84 mainOut f86 tail #connect
Ds0 f86 head f85 mainIn #connect
Ds0 f85 mainOut f88 tail #connect
Ds0 f88 head f87 mainIn #connect
Ds0 f3 mainOut f90 tail #connect
Ds0 f90 head f89 mainIn #connect
Ds0 f89 mainOut f7 tail #connect
Ds0 f7 head f6 mainIn #connect
Ds0 f91 mainOut f93 tail #connect
Ds0 f93 head f92 mainIn #connect
Ds0 f92 mainOut f95 tail #connect
Ds0 f95 head f94 mainIn #connect
Ds0 f33 mainOut f68 tail #connect
Ds0 f68 head f64 mainIn #connect
Ds0 f64 mainOut f2 tail #connect
Ds0 f2 head f1 mainIn #connect
Ds0 f72 mainOut f96 tail #connect
Ds0 f96 head f73 in #connect
Ds0 f101 head f99 mainIn #connect
Ds0 f73 out f104 tail #connect
Ds0 f104 head f102 mainIn #connect
Ds0 f73 out f101 tail #connect
Ds0 f102 mainOut f105 tail #connect
Ds0 f105 head f100 mainIn #connect
Ds0 f97 mainOut f14 tail #connect
Ds0 f14 head f13 mainIn #connect
Ds0 f13 mainOut f15 tail #connect
Ds0 f15 head f99 mainIn #connect
Ds0 f100 mainOut f16 tail #connect
Ds0 f16 head f97 mainIn #connect
Ds0 f17 mainOut f18 tail #connect
Ds0 f18 head f33 mainIn #connect
Ds0 f75 mainOut f19 tail #connect
Ds0 f19 head f77 mainIn #connect
Ds0 f9 mainOut f21 tail #connect
Ds0 f21 head f20 mainIn #connect
Ds0 f20 mainOut f12 tail #connect
Ds0 f12 head f11 mainIn #connect
