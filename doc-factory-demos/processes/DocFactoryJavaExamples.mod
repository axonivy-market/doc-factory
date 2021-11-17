[Ivy]
16CD7829EF6B489B 9.3.1 #module
>Proto >Proto Collection #zClass
Ds0 DocFactoryJavaExamples Big #zClass
Ds0 B #cInfo
Ds0 #process
Ds0 @TextInP .type .type #zField
Ds0 @TextInP .processKind .processKind #zField
Ds0 @AnnotationInP-0n ai ai #zField
Ds0 @MessageFlowInP-0n messageIn messageIn #zField
Ds0 @MessageFlowOutP-0n messageOut messageOut #zField
Ds0 @TextInP .xml .xml #zField
Ds0 @TextInP .responsibility .responsibility #zField
Ds0 @UserDialog f3 '' #zField
Ds0 @EndTask f4 '' #zField
Ds0 @StartRequest f5 '' #zField
Ds0 @PushWFArc f6 '' #zField
Ds0 @PushWFArc f7 '' #zField
Ds0 @InfoButton f42 '' #zField
>Proto Ds0 Ds0 DocFactoryJavaExamples #zField
Ds0 f3 dialogId doc.factory.demos.CreateDocumentsFromTemplates #txt
Ds0 f3 startMethod start() #txt
Ds0 f3 requestActionDecl '<> param;' #txt
Ds0 f3 responseActionDecl 'ivy.aspose.Data out;
' #txt
Ds0 f3 responseMappingAction 'out=in;
' #txt
Ds0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>DocumentCreation Samples</name>
    </language>
</elementInfo>
' #txt
Ds0 f3 152 266 176 44 -78 -8 #rect
Ds0 f4 385 273 30 30 0 15 #rect
Ds0 f5 outLink start2.ivp #txt
Ds0 f5 inParamDecl '<> param;' #txt
Ds0 f5 requestEnabled true #txt
Ds0 f5 triggerEnabled false #txt
Ds0 f5 callSignature start2() #txt
Ds0 f5 persist false #txt
Ds0 f5 startName '3.1 Document Creation Capabilities' #txt
Ds0 f5 caseData businessCase.attach=true #txt
Ds0 f5 showInStartList 1 #txt
Ds0 f5 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Document Creation Capabilities</name>
    </language>
</elementInfo>
' #txt
Ds0 f5 @C|.responsibility Everybody #txt
Ds0 f5 81 273 30 30 -26 30 #rect
Ds0 f6 expr out #txt
Ds0 f6 111 288 152 288 #arcP
Ds0 f7 expr out #txt
Ds0 f7 328 288 385 288 #arcP
Ds0 f42 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>This examples are using java to call DocFactory/Aspose API.&#13;
The document templates (.docx) used here are loaded from the /src/resources Folder.&#13;
The generated documents are created in the ivy files directory and displayed in the Webbrowser.</name>
        <nameStyle>24,5
4,5,0
213,5
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f42 24 66 528 60 -261 -24 #rect
>Proto Ds0 .type doc.factory.demos.Data #txt
>Proto Ds0 .processKind NORMAL #txt
>Proto Ds0 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <swimlaneLabel></swimlaneLabel>
        <swimlaneLabel>Doc Generation 
Capabilities</swimlaneLabel>
    </language>
    <swimlaneOrientation>false</swimlaneOrientation>
    <swimlaneSize>192</swimlaneSize>
    <swimlaneSize>192</swimlaneSize>
    <swimlaneColor gradient="false">-1</swimlaneColor>
    <swimlaneColor gradient="false">-1</swimlaneColor>
    <swimlaneType>LANE</swimlaneType>
    <swimlaneType>LANE</swimlaneType>
    <swimlaneSpaceBefore>0</swimlaneSpaceBefore>
    <swimlaneSpaceBefore>0</swimlaneSpaceBefore>
</elementInfo>
' #txt
>Proto Ds0 0 0 32 24 18 0 #rect
>Proto Ds0 @|BIcon #fIcon
Ds0 f5 mainOut f6 tail #connect
Ds0 f6 head f3 mainIn #connect
Ds0 f3 mainOut f7 tail #connect
Ds0 f7 head f4 mainIn #connect
