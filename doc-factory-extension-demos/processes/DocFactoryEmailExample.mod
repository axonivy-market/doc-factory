[Ivy]
1712BF5507F25F15 9.4.8 #module
>Proto >Proto Collection #zClass
Ds0 DocFactoryEMailExample Big #zClass
Ds0 B #cInfo
Ds0 #process
Ds0 @TextInP .colors .colors #zField
Ds0 @TextInP color color #zField
Ds0 @TextInP .type .type #zField
Ds0 @TextInP .processKind .processKind #zField
Ds0 @AnnotationInP-0n ai ai #zField
Ds0 @MessageFlowInP-0n messageIn messageIn #zField
Ds0 @MessageFlowOutP-0n messageOut messageOut #zField
Ds0 @TextInP .xml .xml #zField
Ds0 @TextInP .responsibility .responsibility #zField
Ds0 @InfoButton f42 '' #zField
Ds0 @StartRequest f0 '' #zField
Ds0 @EndTask f2 '' #zField
Ds0 @UserDialog f3 '' #zField
Ds0 @PushWFArc f4 '' #zField
Ds0 @PushWFArc f5 '' #zField
Ds0 @InfoButton f22 '' #zField
>Proto Ds0 Ds0 DocFactoryEMailExample #zField
Ds0 f42 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>This example uses aspose.email library to create an email .msg file.&#13;
Aspose.email.jar is not included in the Axon Ivy DocFactory by default but can be added to the project classpath.&#13;
The generated .msg file is sent to the Web browser.</name>
    </language>
</elementInfo>
' #txt
Ds0 f42 32 58 608 60 -301 -24 #rect
Ds0 f0 outLink start.ivp #txt
Ds0 f0 inParamDecl '<> param;' #txt
Ds0 f0 requestEnabled true #txt
Ds0 f0 triggerEnabled false #txt
Ds0 f0 callSignature start() #txt
Ds0 f0 startName '2. Create an .msg mail document' #txt
Ds0 f0 caseData businessCase.attach=true #txt
Ds0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Email .msg document</name>
    </language>
</elementInfo>
' #txt
Ds0 f0 @C|.responsibility Everybody #txt
Ds0 f0 113 273 30 30 -40 27 #rect
Ds0 f2 401 273 30 30 0 15 #rect
Ds0 f3 dialogId doc.factory.demos.Email #txt
Ds0 f3 startMethod start() #txt
Ds0 f3 requestActionDecl '<> param;' #txt
Ds0 f3 responseMappingAction 'out=in;
' #txt
Ds0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create an email .msg</name>
    </language>
</elementInfo>
' #txt
Ds0 f3 208 266 128 44 -59 -8 #rect
Ds0 f4 143 288 208 288 #arcP
Ds0 f5 336 288 401 288 #arcP
Ds0 f22 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Note: That you have to initialize the modul first. </name>
    </language>
</elementInfo>
' #txt
Ds0 f22 480 273 272 30 -127 -8 #rect
>Proto Ds0 .type doc.factory.demos.Data #txt
>Proto Ds0 .processKind NORMAL #txt
>Proto Ds0 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <swimlaneLabel></swimlaneLabel>
        <swimlaneLabel>Email .MSG Generation</swimlaneLabel>
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
Ds0 f0 mainOut f4 tail #connect
Ds0 f4 head f3 mainIn #connect
Ds0 f3 mainOut f5 tail #connect
Ds0 f5 head f2 mainIn #connect
