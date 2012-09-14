[Ivy]
[>Created: Fri Jun 15 14:35:52 CEST 2012]
12844EFAEBB78898 3.17 #module
>Proto >Proto Collection #zClass
Es0 EventLogServices Big #zClass
Es0 B #cInfo
Es0 #process
Es0 @MessageFlowInP-0n messageIn messageIn #zField
Es0 @MessageFlowOutP-0n messageOut messageOut #zField
Es0 @TextInP .resExport .resExport #zField
Es0 @TextInP .type .type #zField
Es0 @TextInP .processKind .processKind #zField
Es0 @TextInP .xml .xml #zField
Es0 @TextInP .responsibility .responsibility #zField
Es0 @AnnotationInP-0n ai ai #zField
Es0 @StartSub f0 '' #zField
Es0 @EndSub f1 '' #zField
Es0 @StartSub f3 '' #zField
Es0 @GridStep f6 '' #zField
Es0 @PushWFArc f13 '' #zField
Es0 @GridStep f30 '' #zField
Es0 @PushWFArc f2 '' #zField
Es0 @StartSub f55 '' #zField
Es0 @EndSub f8 '' #zField
Es0 @GridStep f10 '' #zField
Es0 @PushWFArc f11 '' #zField
Es0 @PushWFArc f4 '' #zField
Es0 @PushWFArc f9 '' #zField
Es0 @PushWFArc f12 '' #zField
Es0 @StartSub f14 '' #zField
Es0 @PushWFArc f19 '' #zField
Es0 @EndSub f7 '' #zField
Es0 @StartSub f15 '' #zField
Es0 @GridStep f16 '' #zField
Es0 @PushWFArc f20 '' #zField
Es0 @PushWFArc f5 '' #zField
Es0 @StartSub f17 '' #zField
Es0 @PushWFArc f18 '' #zField
Es0 @StartSub f21 '' #zField
Es0 @EndSub f24 '' #zField
Es0 @GridStep f26 '' #zField
Es0 @PushWFArc f31 '' #zField
Es0 @PushWFArc f22 '' #zField
>Proto Es0 Es0 EventLogServices #zField
Es0 f0 inParamDecl '<ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByCaseId searchByCaseId> param;' #txt
Es0 f0 inParamTable 'out.caseId=param.searchByCaseId.caseId;
' #txt
Es0 f0 outParamDecl '<ch.ivyteam.ivy.addons.data.technical.IvyResultStatus ivyResultStatus,List<ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog> eventLogEntries> result;
' #txt
Es0 f0 outParamTable 'result.ivyResultStatus=in.ivyResultStatus;
result.eventLogEntries=in.eventLogs;
' #txt
Es0 f0 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices out;
' #txt
Es0 f0 callSignature readListByCaseId(ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByCaseId) #txt
Es0 f0 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>readListByCaseId</name>
        <nameStyle>16,7,9
</nameStyle>
        <desc>Get all event log entries for a given case identifier.</desc>
    </language>
</elementInfo>
' #txt
Es0 f0 67 27 26 26 14 0 #rect
Es0 f0 @|StartSubIcon #fIcon
Es0 f1 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f1 211 267 26 26 14 0 #rect
Es0 f1 @|EndSubIcon #fIcon
Es0 f3 inParamDecl '<ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogData eventLogData> param;' #txt
Es0 f3 inParamTable 'out.createWithHistory=true;
out.eventLog.data=param.eventLogData;
' #txt
Es0 f3 outParamDecl '<java.lang.Number eventLogId,ch.ivyteam.ivy.addons.data.technical.IvyResultStatus ivyResultStatus> result;
' #txt
Es0 f3 outParamTable 'result.eventLogId=in.eventLogId;
result.ivyResultStatus=in.ivyResultStatus;
' #txt
Es0 f3 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices out;
' #txt
Es0 f3 callSignature create(ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogData) #txt
Es0 f3 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>create</name>
        <nameStyle>6,5,7,9
</nameStyle>
        <desc>Add a new entry to the event log for the current case.</desc>
    </language>
</elementInfo>
' #txt
Es0 f3 731 27 26 26 14 0 #rect
Es0 f3 @|StartSubIcon #fIcon
Es0 f6 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices out;
' #txt
Es0 f6 actionTable 'out=in;
' #txt
Es0 f6 actionCode 'import ch.ivyteam.ivy.addons.eventlog.data.technical.EventLogSeverity;

out.eventLog.timeStamp = new DateTime();

if (!out.eventLog.data.#application is initialized || "".equals(out.eventLog.data.application))
{
	out.eventLog.data.application = ivy.case.getApplication().getName();
}
if (!out.eventLog.data.#server is initialized)
{
	out.eventLog.data.server = java.net.InetAddress.getLocalHost().getHostName();
}
if (!out.eventLog.data.#environment is initialized)
{
	out.eventLog.data.environment = ivy.session.getActiveEnvironment();
}
if (!out.eventLog.data.#groupId is initialized)
{
	out.eventLog.data.groupId = ivy.case.getIdentifier().toString();
}
if (!out.eventLog.data.#eventDate is initialized)
{
  if (!out.eventLog.data.#eventTime is initialized)
  {
	  out.eventLog.data.eventTime = out.eventLog.timeStamp.getTime();
  }
	out.eventLog.data.eventDate = out.eventLog.timeStamp.getDate();
}
if (!out.eventLog.data.#userName is initialized)
{
	out.eventLog.data.userName = ivy.session.getSessionUserName();
}
if (!out.eventLog.data.#objectId is initialized)
{
	out.eventLog.data.objectId = ivy.case.#businessObjectCode;
}
if (!out.eventLog.data.#initiator is initialized && ivy.task.#activator is initialized)
{
	out.eventLog.data.initiator = ivy.task.activator.getMemberName();
}
if (!out.eventLog.data.#severity is initialized)
{
	out.eventLog.data.severity = EventLogSeverity.INFO;
}


' #txt
Es0 f6 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f6 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Fill EventLog attributes</name>
        <nameStyle>24,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f6 726 100 36 24 20 -2 #rect
Es0 f6 @|StepIcon #fIcon
Es0 f13 expr out #txt
Es0 f13 744 53 744 100 #arcP
Es0 f30 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices out;
' #txt
Es0 f30 actionTable 'out=in;
' #txt
Es0 f30 actionCode 'import ch.ivyteam.ivy.addons.restricted.eventlog.EventLogHelper;
import ch.ivyteam.ivy.addons.technical.MessageCodes;
import ch.ivyteam.ivy.addons.data.technical.IvyResultStatusSeverity;
import ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog;

MessageCodes code;

try
{
	if (in.#caseId is initialized)
	{
		out.eventLogs = EventLogHelper.readAllByCase(in.caseId, ivy.wf);
	}
	if (in.#taskId is initialized)
	{
		out.eventLogs = EventLogHelper.readAllByTask(in.taskId, ivy.wf);
	}
	if (in.#eventLogId is initialized)
	{
		out.eventLog = EventLogHelper.read(in.eventLogId, null, ivy.wf);
	}
	out.ivyResultStatus.successful = true;
}
catch (Exception e)
{
	out.ivyResultStatus.severity = IvyResultStatusSeverity.FATAL;
	out.ivyResultStatus.successful = false;
	out.ivyResultStatus.javaException = e;
	out.ivyResultStatus.detail = e.getClass().getName() + " - " + e.getMessage() + " - " + e.getCause();
	code = MessageCodes.XIVY_ADD_EVT_002;
	out.ivyResultStatus.code = code.toString();
	out.ivyResultStatus.message = ivy.cms.co("/messages/" + code);
	
	ivy.log.error(code.toString() + " - " + out.ivyResultStatus.message + " - " + out.ivyResultStatus.detail, e);
}
' #txt
Es0 f30 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f30 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Select data
and prepare the result</name>
        <nameStyle>34,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f30 206 148 36 24 20 -2 #rect
Es0 f30 @|StepIcon #fIcon
Es0 f2 expr out #txt
Es0 f2 224 172 224 267 #arcP
Es0 f55 inParamDecl '<ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByTaskId searchByTaskId> param;' #txt
Es0 f55 inParamTable 'out.taskId=param.searchByTaskId.taskId;
' #txt
Es0 f55 outParamDecl '<ch.ivyteam.ivy.addons.data.technical.IvyResultStatus ivyResultStatus,List<ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog> eventLogEntries> result;
' #txt
Es0 f55 outParamTable 'result.ivyResultStatus=in.ivyResultStatus;
result.eventLogEntries=in.eventLogs;
' #txt
Es0 f55 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices out;
' #txt
Es0 f55 callSignature readListByTaskId(ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByTaskId) #txt
Es0 f55 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f55 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>readListByTaskId</name>
        <nameStyle>16,5,7,9
</nameStyle>
        <desc>Get all event log entries for a given task identifier.</desc>
    </language>
</elementInfo>
' #txt
Es0 f55 211 27 26 26 14 0 #rect
Es0 f55 @|StartSubIcon #fIcon
Es0 f8 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f8 731 267 26 26 14 0 #rect
Es0 f8 @|EndSubIcon #fIcon
Es0 f10 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices out;
' #txt
Es0 f10 actionTable 'out=in;
' #txt
Es0 f10 actionCode 'import ch.ivyteam.ivy.addons.technical.MessageCodes;
import ch.ivyteam.ivy.addons.restricted.eventlog.EventLogHelper;
import ch.ivyteam.ivy.addons.data.technical.IvyResultStatusSeverity;

MessageCodes code;

try
{
	out.eventLogId = EventLogHelper.createEventLog(in.eventLog.data, in.createWithHistory, ivy.wf);
	out.ivyResultStatus.successful = true;
}
catch (Exception e)
{
	out.ivyResultStatus.severity = IvyResultStatusSeverity.FATAL;
	out.ivyResultStatus.successful = false;
	out.ivyResultStatus.javaException = e;
	out.ivyResultStatus.detail = e.getMessage();
	code = MessageCodes.XIVY_ADD_EVT_001;
	out.ivyResultStatus.code = code.toString();
	out.ivyResultStatus.message = ivy.cms.co("/messages/" + code);
		
	ivy.log.error(code.toString() + " - " + out.ivyResultStatus.message + " - " + out.ivyResultStatus.detail, e);
	
	out.eventLogId = -1;
		
	e.printStackTrace();	
}' #txt
Es0 f10 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f10 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create entry</name>
        <nameStyle>12,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f10 726 164 36 24 20 -2 #rect
Es0 f10 @|StepIcon #fIcon
Es0 f11 expr out #txt
Es0 f11 744 124 744 164 #arcP
Es0 f4 expr out #txt
Es0 f4 744 188 744 267 #arcP
Es0 f9 expr out #txt
Es0 f9 89 48 210 148 #arcP
Es0 f12 expr out #txt
Es0 f12 224 53 224 148 #arcP
Es0 f14 inParamDecl '<java.lang.Number eventLogId> param;' #txt
Es0 f14 inParamTable 'out.eventLogId=param.eventLogId;
' #txt
Es0 f14 outParamDecl '<ch.ivyteam.ivy.addons.data.technical.IvyResultStatus ivyResultStatus,ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog eventLogEntry> result;
' #txt
Es0 f14 outParamTable 'result.ivyResultStatus=in.ivyResultStatus;
result.eventLogEntry=in.eventLog;
' #txt
Es0 f14 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices out;
' #txt
Es0 f14 callSignature read(Number) #txt
Es0 f14 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f14 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>read</name>
        <nameStyle>4,5,7,9
</nameStyle>
        <desc>Get an event log for a given identifier.</desc>
    </language>
</elementInfo>
' #txt
Es0 f14 355 27 26 26 14 0 #rect
Es0 f14 @|StartSubIcon #fIcon
Es0 f19 expr out #txt
Es0 f19 358 48 238 148 #arcP
Es0 f7 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f7 1003 267 26 26 14 0 #rect
Es0 f7 @|EndSubIcon #fIcon
Es0 f15 inParamDecl '<java.lang.Number eventLogId,ch.ivyteam.ivy.addons.eventlog.data.technical.EventLogStatus newStatus> param;' #txt
Es0 f15 inParamTable 'out.eventLogId=param.eventLogId;
out.newStatus=param.newStatus;
' #txt
Es0 f15 outParamDecl '<ch.ivyteam.ivy.addons.data.technical.IvyResultStatus ivyResultStatus> result;
' #txt
Es0 f15 outParamTable 'result.ivyResultStatus=in.ivyResultStatus;
' #txt
Es0 f15 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices out;
' #txt
Es0 f15 callSignature updateStatus(Number,ch.ivyteam.ivy.addons.eventlog.data.technical.EventLogStatus) #txt
Es0 f15 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f15 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>updateStatus</name>
        <nameStyle>12,5,7,9
</nameStyle>
        <desc>Update the status of a event log entry.
Only event log entries that have ''MESSAGE'' as type have a status.</desc>
    </language>
</elementInfo>
' #txt
Es0 f15 1003 27 26 26 14 0 #rect
Es0 f15 @|StartSubIcon #fIcon
Es0 f16 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices out;
' #txt
Es0 f16 actionTable 'out=in;
' #txt
Es0 f16 actionCode 'import ch.ivyteam.ivy.addons.technical.MessageCodes;
import ch.ivyteam.ivy.addons.restricted.eventlog.EventLogHelper;
import ch.ivyteam.ivy.addons.data.technical.IvyResultStatusSeverity;

MessageCodes code;

try
{
	EventLogHelper.updateMessage(in.eventLogId, in.newStatus, ivy.wf);
	out.ivyResultStatus.successful = true;
}
catch (Exception e)
{
	out.ivyResultStatus.severity = IvyResultStatusSeverity.FATAL;
	out.ivyResultStatus.successful = false;
	out.ivyResultStatus.javaException = e;
	out.ivyResultStatus.detail = e.getMessage();
	code = MessageCodes.XIVY_ADD_EVT_003;
	out.ivyResultStatus.code = code.toString();
	out.ivyResultStatus.message = ivy.cms.co("/messages/" + code);
		
	ivy.log.error(code.toString() + " - " + out.ivyResultStatus.message + " - " + out.ivyResultStatus.detail, e);
	
	e.printStackTrace();	
}' #txt
Es0 f16 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f16 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Update entry</name>
        <nameStyle>12,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f16 998 132 36 24 20 -2 #rect
Es0 f16 @|StepIcon #fIcon
Es0 f20 expr out #txt
Es0 f20 1016 156 1016 267 #arcP
Es0 f5 expr out #txt
Es0 f5 1016 53 1016 132 #arcP
Es0 f17 inParamDecl '<ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogData eventLogData> param;' #txt
Es0 f17 inParamTable 'out.createWithHistory=false;
out.eventLog.data=param.eventLogData;
' #txt
Es0 f17 outParamDecl '<java.lang.Number eventLogId,ch.ivyteam.ivy.addons.data.technical.IvyResultStatus ivyResultStatus> result;
' #txt
Es0 f17 outParamTable 'result.eventLogId=in.eventLogId;
result.ivyResultStatus=in.ivyResultStatus;
' #txt
Es0 f17 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices out;
' #txt
Es0 f17 callSignature createWithoutHistory(ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogData) #txt
Es0 f17 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f17 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>createWithoutHistory</name>
        <nameStyle>20,5,7,9
</nameStyle>
        <desc>Add a new entry to the event log for the current case.
The event log case and task history tables are not filled.</desc>
    </language>
</elementInfo>
' #txt
Es0 f17 803 27 26 26 14 0 #rect
Es0 f17 @|StartSubIcon #fIcon
Es0 f18 expr out #txt
Es0 f18 806 49 756 100 #arcP
Es0 f21 inParamDecl '<ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByCaseId searchByCaseId> param;' #txt
Es0 f21 inParamTable 'out.readListId=param.searchByCaseId.caseId;
' #txt
Es0 f21 outParamDecl '<ch.ivyteam.ivy.addons.data.technical.IvyResultStatus ivyResultStatus,List<ch.ivyteam.ivy.addons.data.technical.eventlog.EventLog> eventLogEntries> result;
' #txt
Es0 f21 outParamTable 'result.ivyResultStatus=in.ivyResultStatus;
result.eventLogEntries=in.eventLogs;
' #txt
Es0 f21 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices out;
' #txt
Es0 f21 callSignature readBusinessEventListByCaseId(ch.ivyteam.ivy.addons.data.technical.eventlog.EventLogSearchByCaseId) #txt
Es0 f21 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f21 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>readBusinessEventListByCaseId</name>
        <nameStyle>29,5,7,9
</nameStyle>
        <desc>Get all business events for a given case identifier.</desc>
    </language>
</elementInfo>
' #txt
Es0 f21 467 27 26 26 14 0 #rect
Es0 f21 @|StartSubIcon #fIcon
Es0 f24 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f24 467 267 26 26 14 0 #rect
Es0 f24 @|EndSubIcon #fIcon
Es0 f26 actionDecl 'ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices out;
' #txt
Es0 f26 actionTable 'out=in;
' #txt
Es0 f26 actionCode 'import ch.ivyteam.ivy.addons.technical.MessageCodes;
import ch.ivyteam.ivy.addons.data.technical.IvyResultStatusSeverity;
import ch.ivyteam.ivy.addons.restricted.util.PublicAPIHelper;
import ch.ivyteam.ivy.workflow.ICase;
import ch.ivyteam.ivy.addons.restricted.eventlog.EventLogHelper;


out.events.clear();

try
{
	ICase wfCase = PublicAPIHelper.findCaseAsSystemUser(in.readListId);
	out.eventLogs.clear();
	out.eventLogs.addAll(EventLogHelper.findBusinessEventLogsAsSystemUser(wfCase));
	out.ivyResultStatus.successful = true;
}
catch (Exception e)
{
	out.ivyResultStatus.severity = IvyResultStatusSeverity.FATAL;
	out.ivyResultStatus.successful = false;
	out.ivyResultStatus.javaException = e;
	out.ivyResultStatus.detail = e.getClass().getName() + " - " + e.getMessage() + " - " + e.getCause();
	out.ivyResultStatus.code = MessageCodes.XIVY_ADD_EVT_002.toString();
	out.ivyResultStatus.message = ivy.cms.co("/messages/" + out.ivyResultStatus.code);
	
	ivy.log.error(out.ivyResultStatus.code + " - " + out.ivyResultStatus.message + " - " + out.ivyResultStatus.detail, e);
}
' #txt
Es0 f26 type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
Es0 f26 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>get all case business events</name>
        <nameStyle>28,7,9
</nameStyle>
    </language>
</elementInfo>
' #txt
Es0 f26 462 116 36 24 20 -2 #rect
Es0 f26 @|StepIcon #fIcon
Es0 f31 expr out #txt
Es0 f31 480 53 480 116 #arcP
Es0 f22 expr out #txt
Es0 f22 480 140 480 267 #arcP
>Proto Es0 .type ch.ivyteam.ivy.addons.data.restricted.technical.EventLogServices #txt
>Proto Es0 .processKind CALLABLE_SUB #txt
>Proto Es0 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language/>
</elementInfo>
' #txt
>Proto Es0 0 0 32 24 18 0 #rect
>Proto Es0 @|BIcon #fIcon
Es0 f3 mainOut f13 tail #connect
Es0 f13 head f6 mainIn #connect
Es0 f30 mainOut f2 tail #connect
Es0 f2 head f1 mainIn #connect
Es0 f6 mainOut f11 tail #connect
Es0 f11 head f10 mainIn #connect
Es0 f10 mainOut f4 tail #connect
Es0 f4 head f8 mainIn #connect
Es0 f0 mainOut f9 tail #connect
Es0 f9 head f30 mainIn #connect
Es0 f55 mainOut f12 tail #connect
Es0 f12 head f30 mainIn #connect
Es0 f14 mainOut f19 tail #connect
Es0 f19 head f30 mainIn #connect
Es0 f16 mainOut f20 tail #connect
Es0 f20 head f7 mainIn #connect
Es0 f15 mainOut f5 tail #connect
Es0 f5 head f16 mainIn #connect
Es0 f17 mainOut f18 tail #connect
Es0 f18 head f6 mainIn #connect
Es0 f21 mainOut f31 tail #connect
Es0 f31 head f26 mainIn #connect
Es0 f26 mainOut f22 tail #connect
Es0 f22 head f24 mainIn #connect
