[Ivy]
16CD86F955A91647 9.3.0 #module
>Proto >Proto Collection #zClass
Vs0 ViewSimpleDocumentProcess Big #zClass
Vs0 RD #cInfo
Vs0 #process
Vs0 @TextInP .type .type #zField
Vs0 @TextInP .processKind .processKind #zField
Vs0 @AnnotationInP-0n ai ai #zField
Vs0 @MessageFlowInP-0n messageIn messageIn #zField
Vs0 @MessageFlowOutP-0n messageOut messageOut #zField
Vs0 @TextInP .xml .xml #zField
Vs0 @TextInP .responsibility .responsibility #zField
Vs0 @UdInit f0 '' #zField
Vs0 @UdProcessEnd f1 '' #zField
Vs0 @UdEvent f3 '' #zField
Vs0 @UdExitEnd f4 '' #zField
Vs0 @PushWFArc f5 '' #zField
Vs0 @GridStep f6 '' #zField
Vs0 @PushWFArc f7 '' #zField
Vs0 @PushWFArc f2 '' #zField
>Proto Vs0 Vs0 ViewSimpleDocumentProcess #zField
Vs0 f0 guid 1692E8E24226F4AC #txt
Vs0 f0 method start(Long) #txt
Vs0 f0 inParameterDecl '<Long documentId> param;' #txt
Vs0 f0 inParameterMapAction 'out.service.documentId=param.documentId;
' #txt
Vs0 f0 outParameterDecl '<> result;' #txt
Vs0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start()</name>
        <nameStyle>7,5,7
</nameStyle>
    </language>
</elementInfo>
' #txt
Vs0 f0 83 51 26 26 -16 15 #rect
Vs0 f1 339 51 26 26 0 12 #rect
Vs0 f3 guid 1692E8E2430D94CD #txt
Vs0 f3 actionTable 'out=in;
' #txt
Vs0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>close</name>
    </language>
</elementInfo>
' #txt
Vs0 f3 83 147 26 26 -15 12 #rect
Vs0 f4 211 147 26 26 0 12 #rect
Vs0 f5 expr out #txt
Vs0 f5 109 160 211 160 #arcP
Vs0 f6 actionTable 'out=in;
' #txt
Vs0 f6 actionCode in.service.init(); #txt
Vs0 f6 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>init</name>
    </language>
</elementInfo>
' #txt
Vs0 f6 168 42 112 44 -8 -8 #rect
Vs0 f7 expr out #txt
Vs0 f7 109 64 168 64 #arcP
Vs0 f2 expr out #txt
Vs0 f2 280 64 339 64 #arcP
>Proto Vs0 .type doc.factory.demos.ViewSimpleDocument.ViewSimpleDocumentData #txt
>Proto Vs0 .processKind HTML_DIALOG #txt
>Proto Vs0 -8 -8 16 16 16 26 #rect
Vs0 f3 mainOut f5 tail #connect
Vs0 f5 head f4 mainIn #connect
Vs0 f0 mainOut f7 tail #connect
Vs0 f7 head f6 mainIn #connect
Vs0 f6 mainOut f2 tail #connect
Vs0 f2 head f1 mainIn #connect
