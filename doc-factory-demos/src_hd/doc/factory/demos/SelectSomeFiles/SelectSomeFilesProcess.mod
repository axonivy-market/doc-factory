[Ivy]
16CE2142F29FF2C4 9.4.1 #module
>Proto >Proto Collection #zClass
Cs0 SelectSomeFilesProcess Big #zClass
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
Cs0 @UdExitEnd f10 '' #zField
Cs0 @PushWFArc f2 '' #zField
Cs0 @UdProcessEnd f6 '' #zField
Cs0 @GridStep f8 '' #zField
Cs0 @PushWFArc f7 '' #zField
Cs0 @UdMethod f11 '' #zField
Cs0 @PushWFArc f12 '' #zField
Cs0 @UdEvent f5 '' #zField
Cs0 @PushWFArc f14 '' #zField
>Proto Cs0 Cs0 SelectSomeFilesProcess #zField
Cs0 f0 guid 1692E8DD9FCF9CE4 #txt
Cs0 f0 method start() #txt
Cs0 f0 inParameterDecl '<> param;' #txt
Cs0 f0 inParameterMapAction 'out.files=null;
' #txt
Cs0 f0 outParameterDecl '<List<java.io.File> files> result;' #txt
Cs0 f0 outParameterMapAction 'result.files=in.files;
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
Cs0 f10 339 243 26 26 0 12 #rect
Cs0 f2 expr out #txt
Cs0 f2 109 64 339 64 #arcP
Cs0 f6 339 147 26 26 0 12 #rect
Cs0 f8 actionTable 'out=in;
' #txt
Cs0 f8 actionCode '// temporary ivy files
File ivyfile = new File(in.uploadEvent.getFile().getFileName(), true);
ivyfile.writeBinary(in.uploadEvent.getFile().getContent());

out.files.add(ivyfile.getJavaFile());


' #txt
Cs0 f8 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Handle uploaded files</name>
    </language>
</elementInfo>
' #txt
Cs0 f8 160 138 128 44 -60 -8 #rect
Cs0 f7 expr out #txt
Cs0 f7 288 160 339 160 #arcP
Cs0 f11 guid 16CF78AE0476A4DA #txt
Cs0 f11 method uploadFile(FileUploadEvent) #txt
Cs0 f11 inParameterDecl '<org.primefaces.event.FileUploadEvent fileUploadEvent> param;' #txt
Cs0 f11 inParameterMapAction 'out.uploadEvent=param.fileUploadEvent;
' #txt
Cs0 f11 outParameterDecl '<> result;' #txt
Cs0 f11 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>uploadFile(FileUploadEvent)</name>
    </language>
</elementInfo>
' #txt
Cs0 f11 83 147 26 26 -75 24 #rect
Cs0 f12 expr out #txt
Cs0 f12 109 160 160 160 #arcP
Cs0 f5 guid 16CF790D37B8AFC8 #txt
Cs0 f5 actionTable 'out=in;
' #txt
Cs0 f5 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>createDocument</name>
    </language>
</elementInfo>
' #txt
Cs0 f5 83 243 26 26 -45 12 #rect
Cs0 f14 expr out #txt
Cs0 f14 109 256 339 256 #arcP
>Proto Cs0 .type doc.factory.demos.SelectSomeFiles.SelectSomeFilesData #txt
>Proto Cs0 .processKind HTML_DIALOG #txt
>Proto Cs0 -8 -8 16 16 16 26 #rect
Cs0 f0 mainOut f2 tail #connect
Cs0 f2 head f1 mainIn #connect
Cs0 f8 mainOut f7 tail #connect
Cs0 f7 head f6 mainIn #connect
Cs0 f11 mainOut f12 tail #connect
Cs0 f12 head f8 mainIn #connect
Cs0 f5 mainOut f14 tail #connect
Cs0 f14 head f10 mainIn #connect
