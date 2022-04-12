[Ivy]
16DFD8F971C80EE7 9.4.7 #module
>Proto >Proto Collection #zClass
Bs0 BarCodeProcess Big #zClass
Bs0 RD #cInfo
Bs0 #process
Bs0 @TextInP .colors .colors #zField
Bs0 @TextInP color color #zField
Bs0 @TextInP .type .type #zField
Bs0 @TextInP .processKind .processKind #zField
Bs0 @TextInP .xml .xml #zField
Bs0 @TextInP .responsibility .responsibility #zField
Bs0 @UdInit f0 '' #zField
Bs0 @UdProcessEnd f1 '' #zField
Bs0 @PushWFArc f2 '' #zField
Bs0 @UdEvent f3 '' #zField
Bs0 @UdExitEnd f4 '' #zField
Bs0 @PushWFArc f5 '' #zField
>Proto Bs0 Bs0 BarCodeProcess #zField
Bs0 f0 guid 16DFD2FCD872BAE7 #txt
Bs0 f0 method start(String) #txt
Bs0 f0 inParameterDecl '<String str> param;' #txt
Bs0 f0 inParameterMapAction 'out.str=param.str;
' #txt
Bs0 f0 outParameterDecl '<String str> result;' #txt
Bs0 f0 outParameterMapAction 'result.str=in.str;
' #txt
Bs0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start(String)</name>
    </language>
</elementInfo>
' #txt
Bs0 f0 83 51 26 26 -32 15 #rect
Bs0 f1 211 51 26 26 0 12 #rect
Bs0 f2 109 64 211 64 #arcP
Bs0 f3 guid 16DFD2FCD8E11FE3 #txt
Bs0 f3 actionTable 'out=in;
' #txt
Bs0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>close</name>
    </language>
</elementInfo>
' #txt
Bs0 f3 83 147 26 26 -15 15 #rect
Bs0 f4 211 147 26 26 0 12 #rect
Bs0 f5 109 160 211 160 #arcP
>Proto Bs0 .type doc.factory.demos.BarCode.BarCodeData #txt
>Proto Bs0 .processKind HTML_DIALOG #txt
>Proto Bs0 -8 -8 16 16 16 26 #rect
Bs0 f0 mainOut f2 tail #connect
Bs0 f2 head f1 mainIn #connect
Bs0 f3 mainOut f5 tail #connect
Bs0 f5 head f4 mainIn #connect
