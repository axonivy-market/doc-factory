[Ivy]
16DFD8AB2E4BFFF9 9.2.0 #module
>Proto >Proto Collection #zClass
II0 IvyDocumentAPI Big #zClass
II0 B #cInfo
II0 #process
II0 @TextInP .type .type #zField
II0 @TextInP .processKind .processKind #zField
II0 @TextInP .xml .xml #zField
II0 @TextInP .responsibility .responsibility #zField
II0 @StartRequest f3 '' #zField
II0 @UserDialog f8 '' #zField
II0 @EndTask f10 '' #zField
II0 @UserDialog f4 '' #zField
II0 @TaskSwitchSimple f12 '' #zField
II0 @InfoButton f14 '' #zField
II0 @PushWFArc f5 '' #zField
II0 @TkArc f13 '' #zField
II0 @PushWFArc f9 '' #zField
II0 @PushWFArc f11 '' #zField
>Proto II0 II0 IvyDocumentAPI #zField
II0 f3 outLink start2.ivp #txt
II0 f3 inParamDecl '<> param;' #txt
II0 f3 requestEnabled true #txt
II0 f3 triggerEnabled false #txt
II0 f3 callSignature start2() #txt
II0 f3 persist true #txt
II0 f3 startName '4.1 Create and Attach Document to Case' #txt
II0 f3 caseData businessCase.attach=true #txt
II0 f3 wfuser 1 #txt
II0 f3 showInStartList 1 #txt
II0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create and Attach Document to Case</name>
    </language>
</elementInfo>
' #txt
II0 f3 @C|.responsibility Everybody #txt
II0 f3 81 145 30 30 -23 31 #rect
II0 f3 @|StartRequestIcon #fIcon
II0 f8 dialogId doc.factory.demos.CreateSimpleDocument #txt
II0 f8 startMethod start() #txt
II0 f8 requestActionDecl '<> param;' #txt
II0 f8 responseActionDecl 'doc.factory.demos.Data out;
' #txt
II0 f8 responseMappingAction 'out=in;
out.documentId=result.documentId;
' #txt
II0 f8 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create document&#13;
Attach it to case documents</name>
        <nameStyle>44,5
</nameStyle>
    </language>
</elementInfo>
' #txt
II0 f8 152 138 176 44 -69 -16 #rect
II0 f8 @|UserDialogIcon #fIcon
II0 f10 609 145 30 30 0 15 #rect
II0 f10 @|EndIcon #fIcon
II0 f4 dialogId doc.factory.demos.ViewSimpleDocument #txt
II0 f4 startMethod start(java.lang.Long) #txt
II0 f4 requestActionDecl '<Long documentId> param;' #txt
II0 f4 requestMappingAction 'param.documentId=in.documentId;
' #txt
II0 f4 responseActionDecl 'doc.factory.demos.Data out;
' #txt
II0 f4 responseMappingAction 'out=in;
' #txt
II0 f4 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>View document&#13;
from case</name>
        <nameStyle>24,5
</nameStyle>
    </language>
</elementInfo>
' #txt
II0 f4 432 138 128 44 -41 -16 #rect
II0 f4 @|UserDialogIcon #fIcon
II0 f12 actionTable 'out=in1;
' #txt
II0 f12 taskData 'TaskA.NAM=Task\: View attached document
TaskA.ROL=Everybody
TaskA.TYPE=0' #txt
II0 f12 template "" #txt
II0 f12 369 145 30 30 0 16 #rect
II0 f12 @|TaskSwitchSimpleIcon #fIcon
II0 f14 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>The case.documents API is used to link &#xD;
a generated document  with the case</name>
    </language>
</elementInfo>
' #txt
II0 f14 88 42 224 44 -109 -16 #rect
II0 f14 @|IBIcon #fIcon
II0 f5 expr out #txt
II0 f5 111 160 152 160 #arcP
II0 f13 expr out #txt
II0 f13 328 160 369 160 #arcP
II0 f9 expr in #txt
II0 f9 outCond ivp=="TaskA.ivp" #txt
II0 f9 399 160 432 160 #arcP
II0 f11 expr out #txt
II0 f11 560 160 609 160 #arcP
>Proto II0 .type doc.factory.demos.Data #txt
>Proto II0 .processKind NORMAL #txt
>Proto II0 0 0 32 24 18 0 #rect
>Proto II0 @|BIcon #fIcon
II0 f4 mainOut f11 tail #connect
II0 f11 head f10 mainIn #connect
II0 f3 mainOut f5 tail #connect
II0 f5 head f8 mainIn #connect
II0 f8 mainOut f13 tail #connect
II0 f13 head f12 in #connect
II0 f12 out f9 tail #connect
II0 f9 head f4 mainIn #connect
