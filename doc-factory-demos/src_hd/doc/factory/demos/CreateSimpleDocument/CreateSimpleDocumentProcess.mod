[Ivy]
16CD86F645B6B18E 9.4.6 #module
>Proto >Proto Collection #zClass
Cs0 CreateSimpleDocumentProcess Big #zClass
Cs0 RD #cInfo
Cs0 #process
Cs0 @TextInP .colors .colors #zField
Cs0 @TextInP color color #zField
Cs0 @TextInP .type .type #zField
Cs0 @TextInP .processKind .processKind #zField
Cs0 @AnnotationInP-0n ai ai #zField
Cs0 @MessageFlowInP-0n messageIn messageIn #zField
Cs0 @MessageFlowOutP-0n messageOut messageOut #zField
Cs0 @TextInP .xml .xml #zField
Cs0 @TextInP .responsibility .responsibility #zField
Cs0 @UdInit f0 '' #zField
Cs0 @UdProcessEnd f1 '' #zField
Cs0 @GridStep f6 '' #zField
Cs0 @GridStep f8 '' #zField
Cs0 @PushWFArc f9 '' #zField
Cs0 @PushWFArc f2 '' #zField
Cs0 @UdExitEnd f10 '' #zField
Cs0 @UdEvent f13 '' #zField
Cs0 @PushWFArc f14 '' #zField
Cs0 @PushWFArc f3 '' #zField
>Proto Cs0 Cs0 CreateSimpleDocumentProcess #zField
Cs0 f0 guid 1692E8DD9FCF9CE4 #txt
Cs0 f0 method start() #txt
Cs0 f0 inParameterDecl '<> param;' #txt
Cs0 f0 outParameterDecl '<Long documentId> result;' #txt
Cs0 f0 outParameterMapAction 'result.documentId=in.service.documentId;
' #txt
Cs0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start()</name>
    </language>
</elementInfo>
' #txt
Cs0 f0 83 51 26 26 -16 15 #rect
Cs0 f1 339 51 26 26 0 12 #rect
Cs0 f6 actionTable 'out=in;
' #txt
Cs0 f6 actionCode in.service.createHelloWord(); #txt
Cs0 f6 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create document</name>
    </language>
</elementInfo>
' #txt
Cs0 f6 168 138 112 44 -47 -8 #rect
Cs0 f8 actionTable 'out=in;
' #txt
Cs0 f8 actionCode in.service.init(); #txt
Cs0 f8 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>init</name>
    </language>
</elementInfo>
' #txt
Cs0 f8 168 42 112 44 -8 -8 #rect
Cs0 f9 expr out #txt
Cs0 f9 109 64 168 64 #arcP
Cs0 f2 expr out #txt
Cs0 f2 280 64 339 64 #arcP
Cs0 f10 339 147 26 26 0 12 #rect
Cs0 f13 guid 16D1A363B5FF2DAC #txt
Cs0 f13 actionTable 'out=in;
' #txt
Cs0 f13 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>createDocument</name>
    </language>
</elementInfo>
' #txt
Cs0 f13 83 147 26 26 -47 15 #rect
Cs0 f14 expr out #txt
Cs0 f14 109 160 168 160 #arcP
Cs0 f3 expr out #txt
Cs0 f3 280 160 339 160 #arcP
>Proto Cs0 .type doc.factory.demos.CreateSimpleDocument.CreateSimpleDocumentData #txt
>Proto Cs0 .processKind HTML_DIALOG #txt
>Proto Cs0 -8 -8 16 16 16 26 #rect
Cs0 f0 mainOut f9 tail #connect
Cs0 f9 head f8 mainIn #connect
Cs0 f8 mainOut f2 tail #connect
Cs0 f2 head f1 mainIn #connect
Cs0 f13 mainOut f14 tail #connect
Cs0 f14 head f6 mainIn #connect
Cs0 f6 mainOut f3 tail #connect
Cs0 f3 head f10 mainIn #connect
