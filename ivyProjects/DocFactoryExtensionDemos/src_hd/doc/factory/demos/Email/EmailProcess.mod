[Ivy]
1712BF653EA82149 7.5.0 #module
>Proto >Proto Collection #zClass
Cs0 EmailProcess Big #zClass
Cs0 RD #cInfo
Cs0 #process
Cs0 @TextInP .type .type #zField
Cs0 @TextInP .processKind .processKind #zField
Cs0 @TextInP .xml .xml #zField
Cs0 @TextInP .responsibility .responsibility #zField
Cs0 @UdInit f0 '' #zField
Cs0 @UdProcessEnd f1 '' #zField
Cs0 @UdEvent f3 '' #zField
Cs0 @UdExitEnd f4 '' #zField
Cs0 @PushWFArc f5 '' #zField
Cs0 @GridStep f6 '' #zField
Cs0 @UdEvent f7 '' #zField
Cs0 @UdProcessEnd f8 '' #zField
Cs0 @PushWFArc f10 '' #zField
Cs0 @PushWFArc f9 '' #zField
Cs0 @GridStep f11 '' #zField
Cs0 @PushWFArc f12 '' #zField
Cs0 @PushWFArc f2 '' #zField
>Proto Cs0 Cs0 EmailProcess #zField
Cs0 f0 guid 170D46F21633C61C #txt
Cs0 f0 method start() #txt
Cs0 f0 inParameterDecl '<> param;' #txt
Cs0 f0 inParameterMapAction 'out.outlockMail.emailaddress="helloWorld@gmail.com";
out.outlockMail.name="Hugo";
' #txt
Cs0 f0 outParameterDecl '<> result;' #txt
Cs0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start()</name>
    </language>
</elementInfo>
' #txt
Cs0 f0 83 51 26 26 -16 15 #rect
Cs0 f0 @|UdInitIcon #fIcon
Cs0 f1 339 51 26 26 0 12 #rect
Cs0 f1 @|UdProcessEndIcon #fIcon
Cs0 f3 guid 170D46F217370C2E #txt
Cs0 f3 actionTable 'out=in;
' #txt
Cs0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>close</name>
    </language>
</elementInfo>
' #txt
Cs0 f3 83 275 26 26 -15 15 #rect
Cs0 f3 @|UdEventIcon #fIcon
Cs0 f4 211 275 26 26 0 12 #rect
Cs0 f4 @|UdExitEndIcon #fIcon
Cs0 f5 109 288 211 288 #arcP
Cs0 f6 actionTable 'out=in;
' #txt
Cs0 f6 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.IvyAsposeEmailer;

import ch.ivyteam.ivy.request.IHttpResponse;
import javax.servlet.http.HttpServletResponse;

out.OutlockMail.msgFile = new File("newEmail.msg");

IvyAsposeEmailer.createMail(out.outlockMail);

IvyAsposeEmailer.downloadMsgFile(out.outlockMail.msgFile.getJavaFile());

       
' #txt
Cs0 f6 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create an  .msg email file</name>
    </language>
</elementInfo>
' #txt
Cs0 f6 136 154 160 44 -70 -8 #rect
Cs0 f6 @|StepIcon #fIcon
Cs0 f7 guid 170D4782C4E3CBB3 #txt
Cs0 f7 actionTable 'out=in;
' #txt
Cs0 f7 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>create</name>
    </language>
</elementInfo>
' #txt
Cs0 f7 75 163 26 26 -17 15 #rect
Cs0 f7 @|UdEventIcon #fIcon
Cs0 f8 339 163 26 26 0 12 #rect
Cs0 f8 @|UdProcessEndIcon #fIcon
Cs0 f10 101 176 136 176 #arcP
Cs0 f9 296 176 339 176 #arcP
Cs0 f11 actionTable 'out=in;
' #txt
Cs0 f11 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.IvyAsposeEmailer;

IvyAsposeEmailer.init();' #txt
Cs0 f11 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>init Aspose.Email Library</name>
    </language>
</elementInfo>
' #txt
Cs0 f11 152 42 144 44 -68 -8 #rect
Cs0 f11 @|StepIcon #fIcon
Cs0 f12 109 64 152 64 #arcP
Cs0 f2 296 64 339 64 #arcP
>Proto Cs0 .type doc.factory.demos.Email.EmailData #txt
>Proto Cs0 .processKind HTML_DIALOG #txt
>Proto Cs0 -8 -8 16 16 16 26 #rect
>Proto Cs0 '' #fIcon
Cs0 f3 mainOut f5 tail #connect
Cs0 f5 head f4 mainIn #connect
Cs0 f7 mainOut f10 tail #connect
Cs0 f10 head f6 mainIn #connect
Cs0 f6 mainOut f9 tail #connect
Cs0 f9 head f8 mainIn #connect
Cs0 f0 mainOut f12 tail #connect
Cs0 f12 head f11 mainIn #connect
Cs0 f11 mainOut f2 tail #connect
Cs0 f2 head f1 mainIn #connect
