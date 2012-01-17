[Ivy]
[>Created: Tue Mar 08 11:17:00 CET 2011]
12A43AB40C151728 3.16 #module
>Proto >Proto Collection #zClass
Ws0 WorkflowServices Big #zClass
Ws0 B #cInfo
Ws0 #process
Ws0 @TextInP .xml .xml #zField
Ws0 @TextInP .responsibility .responsibility #zField
Ws0 @AnnotationInP-0n ai ai #zField
Ws0 @TextInP .resExport .resExport #zField
Ws0 @TextInP .type .type #zField
Ws0 @TextInP .processKind .processKind #zField
Ws0 @MessageFlowInP-0n messageIn messageIn #zField
Ws0 @MessageFlowOutP-0n messageOut messageOut #zField
Ws0 @StartSub f5 '' #zField
Ws0 @EndSub f0 '' #zField
Ws0 @GridStep f2 '' #zField
Ws0 @StartSub f3 '' #zField
Ws0 @PushWFArc f4 '' #zField
Ws0 @PushWFArc f9 '' #zField
Ws0 @PushWFArc f10 '' #zField
Ws0 @InfoButton f1 '' #zField
Ws0 @AnnotationArc f6 '' #zField
Ws0 @StartSub f7 '' #zField
Ws0 @EndSub f8 '' #zField
Ws0 @PushWFArc f11 '' #zField
Ws0 @InfoButton f12 '' #zField
Ws0 @AnnotationArc f13 '' #zField
>Proto Ws0 Ws0 WorkflowServices #zField
Ws0 f5 inParamDecl '<java.lang.String managedTeam> param;' #txt
Ws0 f5 inParamTable 'out.managedTeams=[param.managedTeam];
out.wfCase=ivy.case;
' #txt
Ws0 f5 outParamDecl '<ch.ivyteam.ivy.addons.data.technical.IvyResultStatus ivyResultStatus> result;
' #txt
Ws0 f5 outParamTable 'result.ivyResultStatus=in.ivyResultStatus;
' #txt
Ws0 f5 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.WorkflowServices out;
' #txt
Ws0 f5 callSignature createCaseManagedTeam(String) #txt
Ws0 f5 type ch.ivyteam.ivy.addons.data.restricted.technical.WorkflowServices #txt
Ws0 f5 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>createCaseManagedTeam</name>
        <nameStyle>21,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Ws0 f5 259 91 26 26 14 0 #rect
Ws0 f5 @|StartSubIcon #fIcon
Ws0 f0 type ch.ivyteam.ivy.addons.data.restricted.technical.WorkflowServices #txt
Ws0 f0 59 227 26 26 14 0 #rect
Ws0 f0 @|EndSubIcon #fIcon
Ws0 f2 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.WorkflowServices out;
' #txt
Ws0 f2 actionTable 'out=in;
' #txt
Ws0 f2 actionCode 'import ch.ivyteam.ivy.addons.restricted.workflow.CaseManagedTeamHelper;


// define the case managed teams
out.ivyResultStatus = CaseManagedTeamHelper.setCaseManagedTeams(in.wfCase, in.managedTeams);
' #txt
Ws0 f2 type ch.ivyteam.ivy.addons.data.restricted.technical.WorkflowServices #txt
Ws0 f2 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>set the 
case managed team</name>
        <nameStyle>9,7,9
17,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Ws0 f2 54 156 36 24 20 -2 #rect
Ws0 f2 @|StepIcon #fIcon
Ws0 f3 inParamDecl '<List<java.lang.String> managedTeams> param;' #txt
Ws0 f3 inParamTable 'out.managedTeams=param.managedTeams;
out.wfCase=ivy.case;
' #txt
Ws0 f3 outParamDecl '<ch.ivyteam.ivy.addons.data.technical.IvyResultStatus ivyResultStatus> result;
' #txt
Ws0 f3 outParamTable 'result.ivyResultStatus=in.ivyResultStatus;
' #txt
Ws0 f3 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.WorkflowServices out;
' #txt
Ws0 f3 callSignature createCaseManagedTeams(List<String>) #txt
Ws0 f3 type ch.ivyteam.ivy.addons.data.restricted.technical.WorkflowServices #txt
Ws0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>createCaseManagedTeams</name>
        <nameStyle>22,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Ws0 f3 59 91 26 26 14 0 #rect
Ws0 f3 @|StartSubIcon #fIcon
Ws0 f4 expr out #txt
Ws0 f4 72 117 72 156 #arcP
Ws0 f9 expr out #txt
Ws0 f9 72 180 72 227 #arcP
Ws0 f10 expr out #txt
Ws0 f10 272 117 90 168 #arcP
Ws0 f10 1 272 168 #addKink
Ws0 f10 1 0.37365669255757167 0 0 #arcLabel
Ws0 f1 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>This Callable Sub Process Start is deprecated.
Please use the createCaseManagedTeams instead.</name>
        <nameStyle>62,7
22,0,7
9,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ws0 f1 331 45 11 39 7 -16 #rect
Ws0 f1 @|IBIcon #fIcon
Ws0 f1 -65536|-1|-16777216 #nodeStyle
Ws0 f6 330 63 282 96 #arcP
Ws0 f7 outParamDecl '<java.lang.String displayId> result;
' #txt
Ws0 f7 outParamTable 'result.displayId="workflowUIWindowCenterDisplay";
' #txt
Ws0 f7 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.WorkflowServices out;
' #txt
Ws0 f7 callSignature getWindowCenterDisplayId() #txt
Ws0 f7 type ch.ivyteam.ivy.addons.data.restricted.technical.WorkflowServices #txt
Ws0 f7 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>getWindowCenterDisplayId()</name>
        <nameStyle>26,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ws0 f7 683 91 26 26 14 0 #rect
Ws0 f7 @|StartSubIcon #fIcon
Ws0 f8 type ch.ivyteam.ivy.addons.data.restricted.technical.WorkflowServices #txt
Ws0 f8 683 235 26 26 14 0 #rect
Ws0 f8 @|EndSubIcon #fIcon
Ws0 f11 expr out #txt
Ws0 f11 696 117 696 235 #arcP
Ws0 f12 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>It provides the center display id on which the WorkflowUI is loaded as &quot;Workflow&quot; tab. 
It could be used if you want to load some Rich Dialog as same level as &quot;Workflow&quot; tab , for example  from you custom banner.
Do NOT change this value!</name>
        <nameStyle>238,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Ws0 f12 874 78 12 52 12 -25 #rect
Ws0 f12 @|IBIcon #fIcon
Ws0 f12 -4942081|-1|-16777216 #nodeStyle
Ws0 f13 874 104 709 104 #arcP
>Proto Ws0 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language/>
</elementInfo>
' #txt
>Proto Ws0 .type ch.ivyteam.ivy.addons.data.restricted.technical.WorkflowServices #txt
>Proto Ws0 .processKind CALLABLE_SUB #txt
>Proto Ws0 0 0 32 24 18 0 #rect
>Proto Ws0 @|BIcon #fIcon
Ws0 f3 mainOut f4 tail #connect
Ws0 f4 head f2 mainIn #connect
Ws0 f2 mainOut f9 tail #connect
Ws0 f9 head f0 mainIn #connect
Ws0 f5 mainOut f10 tail #connect
Ws0 f10 head f2 mainIn #connect
Ws0 f1 ao f6 tail #connect
Ws0 f6 head f5 @CG|ai #connect
Ws0 f7 mainOut f11 tail #connect
Ws0 f11 head f8 mainIn #connect
Ws0 f12 ao f13 tail #connect
Ws0 f13 head f7 @CG|ai #connect
