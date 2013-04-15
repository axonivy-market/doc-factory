[Ivy]
[>Created: Mon Jan 28 15:25:39 CET 2013]
1284D7DC62A3BA67 3.17 #module
>Proto >Proto Collection #zClass
Es0 EventLogDisplayListProcess Big #zClass
Es0 RD #cInfo
Es0 #process
Es0 @MessageFlowInP-0n messageIn messageIn #zField
Es0 @MessageFlowOutP-0n messageOut messageOut #zField
Es0 @TextInP .resExport .resExport #zField
Es0 @TextInP .type .type #zField
Es0 @TextInP .processKind .processKind #zField
Es0 @TextInP .xml .xml #zField
Es0 @TextInP .responsibility .responsibility #zField
Es0 @AnnotationInP-0n ai ai #zField
Es0 @TextInP .ui2RdDataAction .ui2RdDataAction #zField
Es0 @TextInP .rdData2UIAction .rdData2UIAction #zField
Es0 @RichDialogInitStart f0 '' #zField
Es0 @RichDialogProcessEnd f1 '' #zField
Es0 @RichDialogInitStart f3 '' #zField
Es0 @RichDialogMethodStart f7 '' #zField
Es0 @RichDialogMethodStart f8 '' #zField
Es0 @RichDialogProcessEnd f9 '' #zField
Es0 @Alternative f2 '' #zField
Es0 @PushWFArc f12 '' #zField
Es0 @PushWFArc f13 '' #zField
Es0 @RichDialogProcessEnd f6 '' #zField
Es0 @PushWFArc f15 '' #zField
Es0 @CallSub f11 '' #zField
Es0 @PushWFArc f16 '' #zField
Es0 @Alternative f17 '' #zField
Es0 @RichDialog f19 '' #zField
Es0 @PushWFArc f21 '' #zField
Es0 @PushWFArc f4 '' #zField
Es0 @RichDialogProcessStep f14 '' #zField
Es0 @PushWFArc f5 '' #zField
Es0 @PushWFArc f20 '' #zField
Es0 @PushWFArc f10 '' #zField
Es0 @RichDialogMethodStart f23 '' #zField
Es0 @RichDialogProcessEnd f25 '' #zField
Es0 @PushWFArc f26 '' #zField
Es0 @RichDialogProcessStep f24 '' #zField
Es0 @PushWFArc f27 '' #zField
Es0 @RichDialogInitStart f31 '' #zField
Es0 @PushWFArc f32 '' #zField
Es0 @CallSub f33 '' #zField
Es0 @PushWFArc f34 '' #zField
Es0 @PushWFArc f35 '' #zField
Es0 @RichDialogMethodStart f30 '' #zField
Es0 @RichDialogMethodStart f36 '' #zField
Es0 @RichDialogProcessEnd f37 '' #zField
Es0 @PushWFArc f38 '' #zField
Es0 @PushWFArc f39 '' #zField
Es0 @CallSub f41 '' #zField
Es0 @RichDialogProcessEnd f44 '' #zField
Es0 @PushWFArc f48 '' #zField
Es0 @RichDialogProcessStep f59 '' #zField
Es0 @RichDialogProcessStep f49 '' #zField
Es0 @PushWFArc f50 '' #zField
Es0 @PushWFArc f43 '' #zField
Es0 @PushWFArc f18 '' #zField
Es0 @PushWFArc f45 '' #zField
Es0 @RichDialogInitStart f28 '' #zField
Es0 @PushWFArc f29 '' #zField
Es0 @RichDialogMethodStart f22 '' #zField
Es0 @PushWFArc f42 '' #zField
Es0 @RichDialogMethodStart f40 '' #zField
Es0 @PushWFArc f46 '' #zField
>Proto Es0 Es0 EventLogDisplayListProcess #zField
Es0 f0 guid 12844DD6E1FC7357 #txt
Es0 f0 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f0 method start() #txt
Es0 f0 disableUIEvents true #txt
Es0 f0 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<> param = methodEvent.getInputArguments();
' #txt
Es0 f0 inActionCode 'out.eventLogs = [];' #txt
Es0 f0 outParameterDecl '<> result;
' #txt
Es0 f0 embeddedRdInitializations '* ' #txt
Es0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start()</name>
        <nameStyle>7,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f0 190 46 20 20 13 0 #rect
Es0 f0 @|RichDialogInitStartIcon #fIcon
Es0 f1 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f1 190 470 20 20 13 0 #rect
Es0 f1 @|RichDialogProcessEndIcon #fIcon
Es0 f3 guid 1284947F445EC90C #txt
Es0 f3 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f3 method start(ch.ivyteam.ivy.workflow.ICase) #txt
Es0 f3 disableUIEvents true #txt
Es0 f3 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.workflow.ICase wfCase> param = methodEvent.getInputArguments();
' #txt
Es0 f3 inActionCode '// this is all events display mode
out.displayMode = 0;

if (param.#wfCase is initialized)
{
	out.setWfCase(param.wfCase);
	ivy.log.debug("Event log display param case {0} {1} recieved for the event logs.", param.wfCase.getIdentifier(), param.wfCase.name);
}
else
{
	ivy.log.info("Event log display Param case is not initialized...");
}' #txt
Es0 f3 outParameterDecl '<> result;
' #txt
Es0 f3 embeddedRdInitializations '* ' #txt
Es0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(ICase)</name>
        <nameStyle>12,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f3 438 46 20 20 13 0 #rect
Es0 f3 @|RichDialogInitStartIcon #fIcon
Es0 f7 guid 128494D2F14F43E9 #txt
Es0 f7 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f7 method getWfCase() #txt
Es0 f7 disableUIEvents false #txt
Es0 f7 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<> param = methodEvent.getInputArguments();
' #txt
Es0 f7 outParameterDecl '<ch.ivyteam.ivy.workflow.ICase wfCase> result;
' #txt
Es0 f7 outActionCode 'if (in.#wfCase is initialized)
{
	result.wfCase = in.wfCase;
}
else
{
	ivy.log.info("Event log display result case is not initialized...");
}' #txt
Es0 f7 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>getWfCase()</name>
        <nameStyle>11,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f7 1374 46 20 20 13 0 #rect
Es0 f7 @|RichDialogMethodStartIcon #fIcon
Es0 f8 guid 128494D427C5DAA1 #txt
Es0 f8 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f8 method setWfCase(ch.ivyteam.ivy.workflow.ICase) #txt
Es0 f8 disableUIEvents false #txt
Es0 f8 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.workflow.ICase wfCase> param = methodEvent.getInputArguments();
' #txt
Es0 f8 inActionCode '
// set the case
if (param.#wfCase is initialized)
{
	out.setWfCase(param.wfCase);
	ivy.log.debug("Event log display param case {0} {1} recieved for the event logs.", param.wfCase.getIdentifier(), param.wfCase.name);
}
else
{
	ivy.log.info("Event log display Param case is not initialized...");
}' #txt
Es0 f8 outParameterDecl '<> result;
' #txt
Es0 f8 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>setWfCase(ICase)</name>
        <nameStyle>16,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f8 1126 46 20 20 13 0 #rect
Es0 f8 @|RichDialogMethodStartIcon #fIcon
Es0 f9 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f9 438 542 20 20 13 0 #rect
Es0 f9 @|RichDialogProcessEndIcon #fIcon
Es0 f2 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f2 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>case is initialized? which display mode?</name>
        <nameStyle>40,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f2 434 122 28 28 14 0 #rect
Es0 f2 @|AlternativeIcon #fIcon
Es0 f12 expr out #txt
Es0 f12 1136 66 461 135 #arcP
Es0 f12 1 1136 96 #addKink
Es0 f12 1 0.2884876321120504 0 0 #arcLabel
Es0 f13 expr out #txt
Es0 f13 448 66 448 122 #arcP
Es0 f13 0 0.32152818721790627 0 0 #arcLabel
Es0 f6 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f6 1374 158 20 20 13 0 #rect
Es0 f6 @|RichDialogProcessEndIcon #fIcon
Es0 f15 expr out #txt
Es0 f15 1384 66 1384 158 #arcP
Es0 f15 0 0.334994637801797 0 0 #arcLabel
Es0 f11 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f11 processCall 'Functional Processes/technical/EventLogServices:readListByCaseId(ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByCaseId)' #txt
Es0 f11 doCall true #txt
Es0 f11 requestActionDecl '<ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByCaseId searchByCaseId> param;
' #txt
Es0 f11 requestMappingAction 'param.searchByCaseId.caseId=in.wfCase.getIdentifier();
' #txt
Es0 f11 responseActionDecl 'ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData out;
' #txt
Es0 f11 responseMappingAction 'out=in;
out.eventLogs=result.eventLogEntries;
out.ivyResultStatus=result.ivyResultStatus;
' #txt
Es0 f11 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>read
event list</name>
        <nameStyle>15,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f11 374 212 36 24 20 -2 #rect
Es0 f11 @|CallSubIcon #fIcon
Es0 f16 expr in #txt
Es0 f16 outCond 'in.#wfCase is initialized && in.displayMode==0' #txt
Es0 f16 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>all 
events</name>
        <nameStyle>11,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f16 438 140 392 212 #arcP
Es0 f16 1 392 160 #addKink
Es0 f16 1 0.46153846153846156 3 0 #arcLabel
Es0 f17 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f17 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>operation NOT successful
and NOT silent mode?</name>
        <nameStyle>25,7,9
19,7,9
1,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f17 434 442 28 28 12 -19 #rect
Es0 f17 @|AlternativeIcon #fIcon
Es0 f19 targetDisplay TOP #txt
Es0 f19 richDialogId ch.ivyteam.ivy.addons.commondialogs.ErrorDialog #txt
Es0 f19 startMethod showError(ch.ivyteam.ivy.addons.data.technical.IvyResultStatus,Boolean,Boolean) #txt
Es0 f19 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f19 requestActionDecl '<ch.ivyteam.ivy.addons.data.technical.IvyResultStatus ivyResultStatus, Boolean showCopyButton, Boolean showDetailButton> param;' #txt
Es0 f19 requestActionCode 'param.ivyResultStatus = in.ivyResultStatus;

param.showCopyButton = true;
param.showDetailButton = true;

in.windowTitle = ivy.cms.co("/ch/ivyteam/ivy/addons/eventlog/plainStrings/eventLogError");' #txt
Es0 f19 responseActionDecl 'ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData out;
' #txt
Es0 f19 responseMappingAction 'out=in;
' #txt
Es0 f19 windowConfiguration '{/title "<%=in.windowTitle%>"/width 0 /height 0 /centered true /resizable true /maximized false /close_after_last_rd true }' #txt
Es0 f19 isAsynch false #txt
Es0 f19 isInnerRd true #txt
Es0 f19 isDialog true #txt
Es0 f19 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>show 
ErrorDialog</name>
        <nameStyle>17,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f19 582 484 36 24 20 -2 #rect
Es0 f19 @|RichDialogIcon #fIcon
Es0 f21 expr out #txt
Es0 f21 600 508 457 550 #arcP
Es0 f21 1 600 536 #addKink
Es0 f21 1 0.14711653590268986 0 0 #arcLabel
Es0 f4 expr out #txt
Es0 f4 200 66 200 470 #arcP
Es0 f14 actionDecl 'ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData out;
' #txt
Es0 f14 actionTable 'out=in;
' #txt
Es0 f14 actionCode 'ivy.log.error("Event logs display {0}.",
							ivy.cms.co("/ch/ivyteam/ivy/addons/eventlog/plainStrings/eventLogsDisplayFailedCaseIsNotInitialized"));' #txt
Es0 f14 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f14 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>it is not possible to 
display event logs,
the case is not initialized,
display warning on the log</name>
        <nameStyle>98,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f14 238 284 36 24 20 -2 #rect
Es0 f14 @|RichDialogProcessStepIcon #fIcon
Es0 f5 expr out #txt
Es0 f5 256 308 438 548 #arcP
Es0 f5 1 256 472 #addKink
Es0 f5 1 0.3260308304956644 0 0 #arcLabel
Es0 f20 expr in #txt
Es0 f20 outCond '!in.ivyResultStatus.successful && 
!in.silentMode' #txt
Es0 f20 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>yes</name>
        <nameStyle>3,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f20 462 456 600 484 #arcP
Es0 f20 1 600 456 #addKink
Es0 f20 0 0.7414335424923689 0 0 #arcLabel
Es0 f10 expr in #txt
Es0 f10 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>no</name>
        <nameStyle>2,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f10 448 470 448 542 #arcP
Es0 f23 guid 12A32B719A4B5CD8 #txt
Es0 f23 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f23 method setSilentMode(Boolean) #txt
Es0 f23 disableUIEvents false #txt
Es0 f23 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.Boolean silentMode> param = methodEvent.getInputArguments();
' #txt
Es0 f23 inActionCode 'out.silentMode = param.silentMode;
ivy.log.debug("Turning the silent mode to {0}.", out.silentMode);' #txt
Es0 f23 outParameterDecl '<> result;
' #txt
Es0 f23 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>setSilentMode(Boolean)</name>
        <nameStyle>22,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f23 1478 46 20 20 13 0 #rect
Es0 f23 @|RichDialogMethodStartIcon #fIcon
Es0 f25 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f25 1478 158 20 20 13 0 #rect
Es0 f25 @|RichDialogProcessEndIcon #fIcon
Es0 f26 expr out #txt
Es0 f26 1488 66 1488 158 #arcP
Es0 f26 0 0.49171150495909915 0 0 #arcLabel
Es0 f24 actionDecl 'ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData out;
' #txt
Es0 f24 actionTable 'out=in;
' #txt
Es0 f24 actionCode 'if (!out.ivyResultStatus.successful)
{
	out.eventLogs.clear();
}' #txt
Es0 f24 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f24 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>if operation not successful
clear list</name>
        <nameStyle>38,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f24 430 364 36 24 20 -2 #rect
Es0 f24 @|RichDialogProcessStepIcon #fIcon
Es0 f27 expr out #txt
Es0 f27 396 236 444 364 #arcP
Es0 f31 guid 12C548D933113C6D #txt
Es0 f31 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f31 method startWithBusinessEventsOnly(ch.ivyteam.ivy.workflow.ICase) #txt
Es0 f31 disableUIEvents true #txt
Es0 f31 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.workflow.ICase wfCase> param = methodEvent.getInputArguments();
' #txt
Es0 f31 inActionCode '// this is business events display mode
out.displayMode = 1;


if (param.#wfCase is initialized)
{
	out.setWfCase(param.wfCase);
	ivy.log.debug("Event log display param case {0} {1} recieved for the event logs.", param.wfCase.getIdentifier(), param.wfCase.name);
}
else
{
	ivy.log.info("Event log display Param case is not initialized...");
}' #txt
Es0 f31 outParameterDecl '<> result;
' #txt
Es0 f31 embeddedRdInitializations '* ' #txt
Es0 f31 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>startWithBusinessEventsOnly(ICase)</name>
        <nameStyle>34,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f31 574 46 20 20 13 0 #rect
Es0 f31 @|RichDialogInitStartIcon #fIcon
Es0 f32 expr out #txt
Es0 f32 584 66 459 133 #arcP
Es0 f32 1 584 104 #addKink
Es0 f32 1 0.37881927117440817 0 0 #arcLabel
Es0 f33 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f33 processCall 'Functional Processes/technical/EventLogServices:readBusinessEventListByCaseId(ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByCaseId)' #txt
Es0 f33 doCall true #txt
Es0 f33 requestActionDecl '<ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByCaseId searchByCaseId> param;
' #txt
Es0 f33 requestMappingAction 'param.searchByCaseId.caseId=in.wfCase.getIdentifier();
' #txt
Es0 f33 responseActionDecl 'ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData out;
' #txt
Es0 f33 responseMappingAction 'out=in;
out.eventLogs=result.eventLogEntries;
out.ivyResultStatus=result.ivyResultStatus;
' #txt
Es0 f33 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>read
business event list</name>
        <nameStyle>24,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f33 502 212 36 24 20 -2 #rect
Es0 f33 @|CallSubIcon #fIcon
Es0 f34 expr in #txt
Es0 f34 outCond 'in.#wfCase is initialized && in.displayMode==1' #txt
Es0 f34 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>business 
events</name>
        <nameStyle>16,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f34 459 140 520 212 #arcP
Es0 f34 1 520 160 #addKink
Es0 f34 1 0.5 1 0 #arcLabel
Es0 f35 expr out #txt
Es0 f35 514 236 454 364 #arcP
Es0 f30 guid 12C557B3041245C4 #txt
Es0 f30 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f30 method getDisplayMode() #txt
Es0 f30 disableUIEvents false #txt
Es0 f30 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<> param = methodEvent.getInputArguments();
' #txt
Es0 f30 outParameterDecl '<java.lang.Number displayMode> result;
' #txt
Es0 f30 outActionCode 'result.displayMode = in.displayMode;' #txt
Es0 f30 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>getDisplayMode()</name>
        <nameStyle>16,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f30 1134 310 20 20 13 0 #rect
Es0 f30 @|RichDialogMethodStartIcon #fIcon
Es0 f36 guid 12C557B3C1FE8FAB #txt
Es0 f36 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f36 method setDisplayMode(Number) #txt
Es0 f36 disableUIEvents false #txt
Es0 f36 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<java.lang.Number displayMode> param = methodEvent.getInputArguments();
' #txt
Es0 f36 inActionCode 'out.displayMode = param.displayMode;' #txt
Es0 f36 outParameterDecl '<> result;
' #txt
Es0 f36 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>setDisplayMode(Number)</name>
        <nameStyle>22,5,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f36 1278 310 20 20 13 0 #rect
Es0 f36 @|RichDialogMethodStartIcon #fIcon
Es0 f37 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f37 1134 462 20 20 13 0 #rect
Es0 f37 @|RichDialogProcessEndIcon #fIcon
Es0 f38 expr out #txt
Es0 f38 1144 330 1144 462 #arcP
Es0 f39 expr out #txt
Es0 f39 1288 330 1153 470 #arcP
Es0 f39 1 1288 448 #addKink
Es0 f39 1 0.17094702239766077 0 0 #arcLabel
Es0 f41 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f41 processCall 'Functional Processes/technical/EventLogServices:readBusinessEventListByBusinessObject(ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByBusinessObject)' #txt
Es0 f41 doCall true #txt
Es0 f41 requestActionDecl '<ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByBusinessObject businessObject> param;
' #txt
Es0 f41 requestMappingAction 'param.businessObject=in.businessObject;
' #txt
Es0 f41 responseActionDecl 'ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData out;
' #txt
Es0 f41 responseMappingAction 'out=in;
out.eventLogs=result.eventLogEntries;
out.ivyResultStatus=result.ivyResultStatus;
' #txt
Es0 f41 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Read business events
by Business Object</name>
        <nameStyle>21,7
18,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f41 694 212 36 24 20 -2 #rect
Es0 f41 @|CallSubIcon #fIcon
Es0 f44 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f44 1662 158 20 20 13 0 #rect
Es0 f44 @|RichDialogProcessEndIcon #fIcon
Es0 f48 expr in #txt
Es0 f48 434 136 256 284 #arcP
Es0 f48 1 256 136 #addKink
Es0 f48 1 0.008865222344643842 0 0 #arcLabel
Es0 f59 actionDecl 'ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData out;
' #txt
Es0 f59 actionTable 'out=in;
' #txt
Es0 f59 actionCode 'import ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchCriteria;

if (in.columns.size() != 0)
{
	panel.setModel(in.columns);
}

' #txt
Es0 f59 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f59 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Update table model configuration</name>
        <nameStyle>32,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f59 694 332 36 24 20 -2 #rect
Es0 f59 @|RichDialogProcessStepIcon #fIcon
Es0 f49 actionDecl 'ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData out;
' #txt
Es0 f49 actionTable 'out=in;
' #txt
Es0 f49 actionCode 'in.cmsContext.add(0,"/ch/ivyteam/ivy/addons/eventlog/EventLogDisplayList/plainStrings");
' #txt
Es0 f49 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f49 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Add default cms context for headers</name>
        <nameStyle>35,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f49 694 276 36 24 20 -2 #rect
Es0 f49 @|RichDialogProcessStepIcon #fIcon
Es0 f50 expr out #txt
Es0 f50 712 236 712 276 #arcP
Es0 f50 0 0.40348984326618254 0 0 #arcLabel
Es0 f43 expr out #txt
Es0 f43 448 388 448 442 #arcP
Es0 f18 expr out #txt
Es0 f18 712 300 712 332 #arcP
Es0 f45 expr out #txt
Es0 f45 694 346 466 374 #arcP
Es0 f28 guid 13C818A026152D91 #txt
Es0 f28 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f28 method startWithBusinessEventsOnly(ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByBusinessObject,List<ch.ivyteam.ivy.addons.eventlog.EventLogProperty>,String) #txt
Es0 f28 disableUIEvents true #txt
Es0 f28 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByBusinessObject businessObject,List<ch.ivyteam.ivy.addons.eventlog.EventLogProperty> columns,java.lang.String headerCmsUri> param = methodEvent.getInputArguments();
' #txt
Es0 f28 inActionCode 'out.businessObject = param.businessObject;
out.cmsContext = [param.headerCmsUri];
out.columns = param.columns;' #txt
Es0 f28 outParameterDecl '<> result;
' #txt
Es0 f28 embeddedRdInitializations '* ' #txt
Es0 f28 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>startWithBusinessEventsOnly(EventLogSearchByBusinessObject,List&lt;EventLogProperty&gt;,String)</name>
        <nameStyle>89,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f28 702 150 20 20 13 0 #rect
Es0 f28 @|RichDialogInitStartIcon #fIcon
Es0 f29 expr out #txt
Es0 f29 712 170 712 212 #arcP
Es0 f22 guid 13C818B7A19F0B23 #txt
Es0 f22 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f22 method setBusinessObject(ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByBusinessObject) #txt
Es0 f22 disableUIEvents false #txt
Es0 f22 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByBusinessObject businessObject> param = methodEvent.getInputArguments();
' #txt
Es0 f22 inActionCode '
// set business object
if (param.#businessObject is initialized)
{
	out.businessObject = param.businessObject;	
}
else
{
	ivy.log.info("Event log display Param business object is not initialized...");
}' #txt
Es0 f22 outParameterDecl '<> result;
' #txt
Es0 f22 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>setBusinessObject(EventLogSearchByBusinessObject)</name>
    </language>
</elementInfo>
' #txt
Es0 f22 1126 214 20 20 13 0 #rect
Es0 f22 @|RichDialogMethodStartIcon #fIcon
Es0 f42 expr out #txt
Es0 f42 1126 224 730 224 #arcP
Es0 f40 guid 13C818C5CF32197D #txt
Es0 f40 type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
Es0 f40 method getBusinessObject() #txt
Es0 f40 disableUIEvents false #txt
Es0 f40 inParameterDecl 'ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent methodEvent = event as ch.ivyteam.ivy.richdialog.exec.RdMethodCallEvent;
<> param = methodEvent.getInputArguments();
' #txt
Es0 f40 outParameterDecl '<ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByBusinessObject businessObject> result;
' #txt
Es0 f40 outActionCode 'if (in.#businessObject is initialized)
{
	result.businessObject = in.businessObject;
}
else
{
	ivy.log.info("Event log display result business object is not initialized...");
}' #txt
Es0 f40 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>getBusinessObject()</name>
        <nameStyle>19,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f40 1662 46 20 20 13 0 #rect
Es0 f40 @|RichDialogMethodStartIcon #fIcon
Es0 f46 expr out #txt
Es0 f46 1672 66 1672 158 #arcP
>Proto Es0 .type ch.ivyteam.ivy.addons.eventlog.EventLogDisplayList.EventLogDisplayListData #txt
>Proto Es0 .processKind RICH_DIALOG #txt
>Proto Es0 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <swimlaneLabel>History</swimlaneLabel>
        <swimlaneLabel>Start methods</swimlaneLabel>
        <swimlaneLabel>Methods</swimlaneLabel>
        <swimlaneLabel></swimlaneLabel>
    </language>
    <swimlaneSize>166</swimlaneSize>
    <swimlaneSize>936</swimlaneSize>
    <swimlaneSize>715</swimlaneSize>
    <swimlaneColor>-3355393</swimlaneColor>
    <swimlaneColor>-10053121</swimlaneColor>
    <swimlaneColor>-10066177</swimlaneColor>
</elementInfo>
' #txt
>Proto Es0 .rdData2UIAction 'panel.eventLogsTable.listData=in.eventLogs;
' #txt
>Proto Es0 -8 -8 16 16 16 26 #rect
>Proto Es0 '' #fIcon
Es0 f8 mainOut f12 tail #connect
Es0 f12 head f2 in #connect
Es0 f3 mainOut f13 tail #connect
Es0 f13 head f2 in #connect
Es0 f7 mainOut f15 tail #connect
Es0 f15 head f6 mainIn #connect
Es0 f2 out f16 tail #connect
Es0 f16 head f11 mainIn #connect
Es0 f19 mainOut f21 tail #connect
Es0 f21 head f9 mainIn #connect
Es0 f0 mainOut f4 tail #connect
Es0 f4 head f1 mainIn #connect
Es0 f14 mainOut f5 tail #connect
Es0 f5 head f9 mainIn #connect
Es0 f17 out f20 tail #connect
Es0 f20 head f19 mainIn #connect
Es0 f17 out f10 tail #connect
Es0 f10 head f9 mainIn #connect
Es0 f23 mainOut f26 tail #connect
Es0 f26 head f25 mainIn #connect
Es0 f11 mainOut f27 tail #connect
Es0 f27 head f24 mainIn #connect
Es0 f31 mainOut f32 tail #connect
Es0 f32 head f2 in #connect
Es0 f2 out f34 tail #connect
Es0 f34 head f33 mainIn #connect
Es0 f33 mainOut f35 tail #connect
Es0 f35 head f24 mainIn #connect
Es0 f30 mainOut f38 tail #connect
Es0 f38 head f37 mainIn #connect
Es0 f36 mainOut f39 tail #connect
Es0 f39 head f37 mainIn #connect
Es0 f2 out f48 tail #connect
Es0 f48 head f14 mainIn #connect
Es0 f41 mainOut f50 tail #connect
Es0 f50 head f49 mainIn #connect
Es0 f24 mainOut f43 tail #connect
Es0 f43 head f17 in #connect
Es0 f49 mainOut f18 tail #connect
Es0 f18 head f59 mainIn #connect
Es0 f59 mainOut f45 tail #connect
Es0 f45 head f24 mainIn #connect
Es0 f28 mainOut f29 tail #connect
Es0 f29 head f41 mainIn #connect
Es0 f22 mainOut f42 tail #connect
Es0 f42 head f41 mainIn #connect
Es0 f40 mainOut f46 tail #connect
Es0 f46 head f44 mainIn #connect
