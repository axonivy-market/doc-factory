[Ivy]
16CD874DD7DD365F 9.4.3 #module
>Proto >Proto Collection #zClass
Cs0 CreateDocumentsFromTemplatesProcess Big #zClass
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
Cs0 @UdMethod f8 '' #zField
Cs0 @UdProcessEnd f10 '' #zField
Cs0 @GridStep f9 '' #zField
Cs0 @PushWFArc f12 '' #zField
Cs0 @PushWFArc f11 '' #zField
Cs0 @UdMethod f3 '' #zField
Cs0 @UdProcessEnd f4 '' #zField
Cs0 @GridStep f5 '' #zField
Cs0 @PushWFArc f6 '' #zField
Cs0 @PushWFArc f7 '' #zField
Cs0 @GridStep f13 '' #zField
Cs0 @PushWFArc f14 '' #zField
Cs0 @PushWFArc f2 '' #zField
Cs0 @UdProcessEnd f16 '' #zField
Cs0 @GridStep f18 '' #zField
Cs0 @PushWFArc f17 '' #zField
Cs0 @GridStep f20 '' #zField
Cs0 @UdProcessEnd f21 '' #zField
Cs0 @UdEvent f22 '' #zField
Cs0 @PushWFArc f23 '' #zField
Cs0 @PushWFArc f24 '' #zField
Cs0 @UdMethod f25 '' #zField
Cs0 @GridStep f26 '' #zField
Cs0 @UdProcessEnd f27 '' #zField
Cs0 @PushWFArc f28 '' #zField
Cs0 @PushWFArc f29 '' #zField
Cs0 @UdProcessEnd f30 '' #zField
Cs0 @GridStep f31 '' #zField
Cs0 @UdMethod f32 '' #zField
Cs0 @PushWFArc f33 '' #zField
Cs0 @PushWFArc f34 '' #zField
Cs0 @UdEvent f35 '' #zField
Cs0 @PushWFArc f36 '' #zField
>Proto Cs0 Cs0 CreateDocumentsFromTemplatesProcess #zField
Cs0 f0 guid 16923D67E685C598 #txt
Cs0 f0 method start() #txt
Cs0 f0 inParameterDecl '<> param;' #txt
Cs0 f0 outParameterDecl '<> result;' #txt
Cs0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start()</name>
    </language>
</elementInfo>
' #txt
Cs0 f0 83 51 26 26 -16 15 #rect
Cs0 f1 339 51 26 26 0 12 #rect
Cs0 f8 guid 16923DCAC0E6392E #txt
Cs0 f8 method download() #txt
Cs0 f8 inParameterDecl '<> param;' #txt
Cs0 f8 outParameterDecl '<> result;' #txt
Cs0 f8 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>download()</name>
    </language>
</elementInfo>
' #txt
Cs0 f8 83 243 26 26 -31 15 #rect
Cs0 f10 339 243 26 26 0 12 #rect
Cs0 f9 actionTable 'out=in;
' #txt
Cs0 f9 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.Util.FilesUtil;

// uploaded image 
if(in.service.ivyFile.read().length() >0 )
{
	in.service.image = in.service.ivyFile.readBinary(); 
	in.service.imageName = in.service.ivyFile.getName();
}

FilesUtil.downloadJsf(in.service.createWordDocument());' #txt
Cs0 f9 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create Docx</name>
    </language>
</elementInfo>
' #txt
Cs0 f9 168 234 112 44 -33 -8 #rect
Cs0 f12 expr out #txt
Cs0 f12 280 256 339 256 #arcP
Cs0 f11 expr out #txt
Cs0 f11 109 256 168 256 #arcP
Cs0 f3 guid 1692424EDB01FA1B #txt
Cs0 f3 method downloadMultiFile() #txt
Cs0 f3 inParameterDecl '<> param;' #txt
Cs0 f3 outParameterDecl '<> result;' #txt
Cs0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>downloadMultiFile()</name>
    </language>
</elementInfo>
' #txt
Cs0 f3 83 339 26 26 -53 25 #rect
Cs0 f4 339 339 26 26 0 12 #rect
Cs0 f5 actionTable 'out=in;
' #txt
Cs0 f5 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.Util.FilesUtil;

// uploaded image 
if(in.service.ivyFile.read().length() >0 )
{
	in.service.image = in.service.ivyFile.readBinary(); 
	in.service.imageName = in.service.ivyFile.getName();
}

FilesUtil.downloadJsf(in.service.createMultiDocument());' #txt
Cs0 f5 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create Multiple Docs &#13;
and download them in a ZIP</name>
    </language>
</elementInfo>
' #txt
Cs0 f5 140 328 168 48 -83 -15 #rect
Cs0 f6 expr out #txt
Cs0 f6 308 352 339 352 #arcP
Cs0 f7 expr out #txt
Cs0 f7 109 352 140 352 #arcP
Cs0 f13 actionTable 'out=in;
out.service.date=new Date();
out.service.name=ivy.session.getSessionUserName();
' #txt
Cs0 f13 actionCode in.service.init(); #txt
Cs0 f13 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>init Doc Generation Java class</name>
    </language>
</elementInfo>
' #txt
Cs0 f13 136 42 176 44 -83 -8 #rect
Cs0 f14 expr out #txt
Cs0 f14 109 64 136 64 #arcP
Cs0 f2 expr out #txt
Cs0 f2 312 64 339 64 #arcP
Cs0 f16 339 147 26 26 0 12 #rect
Cs0 f18 actionTable 'out=in;
' #txt
Cs0 f18 actionCode 'if(in.service.newExpectation.length()> 0)
{	
	in.service.expectations.add(in.service.newExpectation);
}
in.service.newExpectation = "";' #txt
Cs0 f18 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>add item to List</name>
    </language>
</elementInfo>
' #txt
Cs0 f18 168 138 112 44 -42 -8 #rect
Cs0 f17 expr out #txt
Cs0 f17 280 160 339 160 #arcP
Cs0 f20 actionTable 'out=in;
' #txt
Cs0 f20 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.Util.FilesUtil;
in.service.image = in.uploadEvent.getFile().getContent();
in.service.imageName = in.uploadEvent.getFile().getFileName();
in.service.ivyFile = FilesUtil.primeToIvyFile(in.uploadEvent.getFile());' #txt
Cs0 f20 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Image upload</name>
    </language>
</elementInfo>
' #txt
Cs0 f20 168 426 112 44 -38 -8 #rect
Cs0 f21 339 435 26 26 0 12 #rect
Cs0 f22 guid 169281086A2B905A #txt
Cs0 f22 actionTable 'out=in;
out.uploadEvent=event as org.primefaces.event.FileUploadEvent;
' #txt
Cs0 f22 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>fileUpload</name>
    </language>
</elementInfo>
' #txt
Cs0 f22 83 435 26 26 -28 15 #rect
Cs0 f23 expr out #txt
Cs0 f23 109 448 168 448 #arcP
Cs0 f24 expr out #txt
Cs0 f24 280 448 339 448 #arcP
Cs0 f25 guid 16929B86D66AFADD #txt
Cs0 f25 method downloadPowerPoint() #txt
Cs0 f25 inParameterDecl '<> param;' #txt
Cs0 f25 outParameterDecl '<> result;' #txt
Cs0 f25 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>downloadPowerPoint()</name>
    </language>
</elementInfo>
' #txt
Cs0 f25 75 563 26 26 -62 15 #rect
Cs0 f26 actionTable 'out=in;
' #txt
Cs0 f26 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.Util.FilesUtil;

// uploaded image 
if(in.service.ivyFile.read().length() >0 )
{
	in.service.image = in.service.ivyFile.readBinary(); 
	in.service.imageName = in.service.ivyFile.getName();
}

FilesUtil.downloadJsf(in.service.createPowerPoint());' #txt
Cs0 f26 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create PPTX</name>
    </language>
</elementInfo>
' #txt
Cs0 f26 160 554 112 44 -35 -8 #rect
Cs0 f27 331 563 26 26 0 12 #rect
Cs0 f28 expr out #txt
Cs0 f28 101 576 160 576 #arcP
Cs0 f29 expr out #txt
Cs0 f29 272 576 331 576 #arcP
Cs0 f30 331 659 26 26 0 12 #rect
Cs0 f31 actionTable 'out=in;
' #txt
Cs0 f31 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.Util.FilesUtil;

// uploaded image 
if(in.service.ivyFile.read().length() >0 )
{
	in.service.image = in.service.ivyFile.readBinary(); 
	in.service.imageName = in.service.ivyFile.getName();
}
else
{
	in.service.ivyFile = null;
}

FilesUtil.downloadJsf(in.service.createExcel());' #txt
Cs0 f31 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create XLSX</name>
    </language>
</elementInfo>
' #txt
Cs0 f31 160 650 112 44 -34 -8 #rect
Cs0 f32 guid 1692D29B899FCE25 #txt
Cs0 f32 method downloadExcel() #txt
Cs0 f32 inParameterDecl '<> param;' #txt
Cs0 f32 outParameterDecl '<> result;' #txt
Cs0 f32 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>downloadExcel()</name>
    </language>
</elementInfo>
' #txt
Cs0 f32 75 659 26 26 -45 15 #rect
Cs0 f33 expr out #txt
Cs0 f33 272 672 331 672 #arcP
Cs0 f34 expr out #txt
Cs0 f34 101 672 160 672 #arcP
Cs0 f35 guid 16D1A3391386C6BB #txt
Cs0 f35 actionTable 'out=in;
' #txt
Cs0 f35 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>addNewExpectation</name>
    </language>
</elementInfo>
' #txt
Cs0 f35 83 147 26 26 -47 15 #rect
Cs0 f36 expr out #txt
Cs0 f36 109 160 168 160 #arcP
>Proto Cs0 .type doc.factory.demos.CreateDocumentsFromTemplates.CreateDocumentsFromTemplatesData #txt
>Proto Cs0 .processKind HTML_DIALOG #txt
>Proto Cs0 -8 -8 16 16 16 26 #rect
Cs0 f8 mainOut f11 tail #connect
Cs0 f11 head f9 mainIn #connect
Cs0 f9 mainOut f12 tail #connect
Cs0 f12 head f10 mainIn #connect
Cs0 f3 mainOut f7 tail #connect
Cs0 f7 head f5 mainIn #connect
Cs0 f5 mainOut f6 tail #connect
Cs0 f6 head f4 mainIn #connect
Cs0 f0 mainOut f14 tail #connect
Cs0 f14 head f13 mainIn #connect
Cs0 f13 mainOut f2 tail #connect
Cs0 f2 head f1 mainIn #connect
Cs0 f18 mainOut f17 tail #connect
Cs0 f17 head f16 mainIn #connect
Cs0 f22 mainOut f23 tail #connect
Cs0 f23 head f20 mainIn #connect
Cs0 f20 mainOut f24 tail #connect
Cs0 f24 head f21 mainIn #connect
Cs0 f25 mainOut f28 tail #connect
Cs0 f28 head f26 mainIn #connect
Cs0 f26 mainOut f29 tail #connect
Cs0 f29 head f27 mainIn #connect
Cs0 f32 mainOut f34 tail #connect
Cs0 f34 head f31 mainIn #connect
Cs0 f31 mainOut f33 tail #connect
Cs0 f33 head f30 mainIn #connect
Cs0 f35 mainOut f36 tail #connect
Cs0 f36 head f18 mainIn #connect
