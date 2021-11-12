[Ivy]
16B45CBCE0D2056C 9.3.0 #module
>Proto >Proto Collection #zClass
Ds0 DocFactoryAPIExamples Big #zClass
Ds0 B #cInfo
Ds0 #process
Ds0 @TextInP .type .type #zField
Ds0 @TextInP .processKind .processKind #zField
Ds0 @AnnotationInP-0n ai ai #zField
Ds0 @MessageFlowInP-0n messageIn messageIn #zField
Ds0 @MessageFlowOutP-0n messageOut messageOut #zField
Ds0 @TextInP .xml .xml #zField
Ds0 @TextInP .responsibility .responsibility #zField
Ds0 @StartRequest f19 '' #zField
Ds0 @GridStep f21 '' #zField
Ds0 @PushWFArc f26 '' #zField
Ds0 @GridStep f0 '' #zField
Ds0 @GridStep f4 '' #zField
Ds0 @PushWFArc f11 '' #zField
Ds0 @PushWFArc f1 '' #zField
Ds0 @InfoButton f42 '' #zField
Ds0 @GridStep f6 '' #zField
Ds0 @GridStep f7 '' #zField
Ds0 @StartRequest f8 '' #zField
Ds0 @GridStep f12 '' #zField
Ds0 @PushWFArc f13 '' #zField
Ds0 @PushWFArc f16 '' #zField
Ds0 @PushWFArc f17 '' #zField
Ds0 @GridStep f5 '' #zField
Ds0 @GridStep f9 '' #zField
Ds0 @GridStep f14 '' #zField
Ds0 @StartRequest f20 '' #zField
Ds0 @PushWFArc f22 '' #zField
Ds0 @PushWFArc f23 '' #zField
Ds0 @PushWFArc f25 '' #zField
Ds0 @GridStep f28 '' #zField
Ds0 @StartRequest f29 '' #zField
Ds0 @GridStep f31 '' #zField
Ds0 @PushWFArc f34 '' #zField
Ds0 @GridStep f36 '' #zField
Ds0 @PushWFArc f27 '' #zField
Ds0 @PushWFArc f32 '' #zField
Ds0 @EndRequest f35 '' #zField
Ds0 @PushWFArc f37 '' #zField
Ds0 @EndRequest f2 '' #zField
Ds0 @EndRequest f3 '' #zField
Ds0 @EndRequest f10 '' #zField
Ds0 @PushWFArc f15 '' #zField
Ds0 @PushWFArc f18 '' #zField
Ds0 @PushWFArc f24 '' #zField
Ds0 @StartRequest f30 '' #zField
Ds0 @UserDialog f33 '' #zField
Ds0 @PushWFArc f38 '' #zField
Ds0 @GridStep f39 '' #zField
Ds0 @PushWFArc f40 '' #zField
Ds0 @EndRequest f41 '' #zField
Ds0 @PushWFArc f43 '' #zField
>Proto Ds0 Ds0 DocFactoryAPIExamples #zField
Ds0 f19 outLink start3.ivp #txt
Ds0 f19 inParamDecl '<> param;' #txt
Ds0 f19 requestEnabled true #txt
Ds0 f19 triggerEnabled false #txt
Ds0 f19 callSignature start3() #txt
Ds0 f19 persist false #txt
Ds0 f19 startName '2.2 Document with nested Tables PDF' #txt
Ds0 f19 taskData 'TaskTriggered.EXPRI=2
TaskTriggered.EXROL=Everybody
TaskTriggered.EXTYPE=0
TaskTriggered.PRI=2
TaskTriggered.ROL=Everybody
TaskTriggered.TYPE=0' #txt
Ds0 f19 caseData businessCase.attach=false #txt
Ds0 f19 showInStartList 1 #txt
Ds0 f19 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Document with nested Tables PDF</name>
        <nameStyle>31,5
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f19 @C|.responsibility Everybody #txt
Ds0 f19 65 409 30 30 -30 36 #rect
Ds0 f21 actionTable 'out=in;
' #txt
Ds0 f21 actionCode 'import doc.factory.demos.Position;

import doc.factory.demos.Project;
out.report.employee = "George";
out.report.from = ''1.9.2019'';
out.report.till= new Date();

// project 1
Project pr = new Project();
pr.nr = 23.100;
pr.title = "Release 8";

Position p = new Position();
p.date = ''11.9.2019'';
p.code = 43;
p.description = "Mailand";
p.unit.amount=190;
p.unit.text = "Km";
pr.positions.add(p);
p = new Position();
p.date = ''21.9.2019'';
p.code = 43;
p.description = "Madrid";
p.unit.amount=85;
p.unit.text = "Km";
pr.positions.add(p);

out.report.projects.add(pr);

// project 2
pr = new Project();
pr.nr = 10.500;
pr.title = "Summer Sale";

p = new Position();
p.date = ''23.9.2019'';
p.code = 18;
p.description = "Coffee Cups";
p.unit.amount=60;
p.unit.text = "Pieces";
pr.positions.add(p);
p = new Position();
p.date = ''29.9.2019'';
p.code = 18;
p.description = "Travel Expenses";
p.unit.amount=10;
p.unit.text = "Hours";
pr.positions.add(p);
out.report.projects.add(pr);


' #txt
Ds0 f21 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create complex Data</name>
        <nameStyle>19,5
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f21 144 402 128 44 -57 -8 #rect
Ds0 f26 expr out #txt
Ds0 f26 95 424 144 424 #arcP
Ds0 f0 actionTable 'out=in;
' #txt
Ds0 f0 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.Util.FilesUtil;
import java.util.Locale;
import ch.ivyteam.ivy.addons.docfactory.DocumentTemplate;
		
		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(in.templateFile).
				putDataAsSourceForMailMerge(in.report).
				useLocale(Locale.forLanguageTag("de-CH"));
				

		File output = new File("ivy_DocFactoryDemo/DocWithFullNestedTables.pdf");
		documentTemplate.produceDocument(output.getJavaFile());
		FilesUtil.setFileRef(output);		
		
		' #txt
Ds0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Call API to &#xD;
produce PDF document</name>
    </language>
</elementInfo>
' #txt
Ds0 f0 512 402 160 44 -60 -16 #rect
Ds0 f4 actionTable 'out=in;
' #txt
Ds0 f4 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.LocalResource;

out.templateFile = new LocalResource("resources/myAPITemplateWithNestedTables.docx").asFile();' #txt
Ds0 f4 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>get Template.docx from&#xD;
resources folder</name>
        <nameStyle>4,5
13,5,0
7,5
16,5,0
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f4 312 402 160 44 -61 -16 #rect
Ds0 f11 expr out #txt
Ds0 f11 272 424 312 424 #arcP
Ds0 f1 expr out #txt
Ds0 f1 472 424 512 424 #arcP
Ds0 f42 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>This examples call the DocFactory API to generate documents.&#xD;
The document templates (.docx) used here are loaded from the /src/resources Folder.&#xD;
If you execute the samples, you will find the output documents in the folder "ivy_DocFactoryDemo" in the ivy files directory</name>
    </language>
</elementInfo>
' #txt
Ds0 f42 64 42 672 60 -326 -24 #rect
Ds0 f6 actionTable 'out=in;
' #txt
Ds0 f6 actionCode 'import doc.factory.demos.Position;

import doc.factory.demos.Project;
out.report.employee = "George";
out.report.from = ''1.9.2019'';
out.report.till= new Date();

// project 1
Project pr = new Project();
pr.nr = 23.100;
pr.title = "Release 8";

Position p = new Position();
p.date = ''11.9.2019'';
p.code = 43;
p.description = "Mailand";
p.unit.amount=190;
p.unit.text = "Km";
pr.positions.add(p);
p = new Position();
p.date = ''21.9.2019'';
p.code = 43;
p.description = "Madrid";
p.unit.amount=85;
p.unit.text = "Km";
pr.positions.add(p);

out.report.projects.add(pr);

// project 2
pr = new Project();
pr.nr = 10.500;
pr.title = "Summer Sale";

p = new Position();
p.date = ''23.9.2019'';
p.code = 18;
p.description = "Coffee Cups";
p.unit.amount=60;
p.unit.text = "Pieces";
pr.positions.add(p);
p = new Position();
p.date = ''29.9.2019'';
p.code = 18;
p.description = "Travel Expenses";
p.unit.amount=10;
p.unit.text = "Hours";
pr.positions.add(p);
out.report.projects.add(pr);




' #txt
Ds0 f6 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create complex Data</name>
        <nameStyle>19,5
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f6 144 530 128 44 -57 -8 #rect
Ds0 f7 actionTable 'out=in;
' #txt
Ds0 f7 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.LocalResource;

out.templateFile = new LocalResource("resources/myAPITemplateWithNestedTables.docx").asFile();' #txt
Ds0 f7 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>get Template.docx from&#xD;
resources folder</name>
        <nameStyle>4,5
13,5,0
7,5
16,5,0
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f7 312 530 160 44 -61 -16 #rect
Ds0 f8 outLink start4.ivp #txt
Ds0 f8 inParamDecl '<> param;' #txt
Ds0 f8 requestEnabled true #txt
Ds0 f8 triggerEnabled false #txt
Ds0 f8 callSignature start4() #txt
Ds0 f8 persist false #txt
Ds0 f8 startName '2.3 Document with nested Tables DOCX' #txt
Ds0 f8 taskData 'TaskTriggered.EXPRI=2
TaskTriggered.EXROL=Everybody
TaskTriggered.EXTYPE=0
TaskTriggered.PRI=2
TaskTriggered.ROL=Everybody
TaskTriggered.TYPE=0' #txt
Ds0 f8 caseData businessCase.attach=false #txt
Ds0 f8 showInStartList 1 #txt
Ds0 f8 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Document with nested Tables DOCX</name>
        <nameStyle>32,5
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f8 @C|.responsibility Everybody #txt
Ds0 f8 65 537 30 30 -18 31 #rect
Ds0 f12 actionTable 'out=in;
' #txt
Ds0 f12 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.Util.FilesUtil;
import java.util.Locale;
import ch.ivyteam.ivy.addons.docfactory.DocumentTemplate;
		
		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(in.templateFile).
				putDataAsSourceForMailMerge(in.report).
				useLocale(Locale.forLanguageTag("de-CH"));
				
		File output = new File("ivy_DocFactoryDemo/DocWithFullNestedTables.docx");
		documentTemplate.produceDocument(output.getJavaFile());
		FilesUtil.setFileRef(output);		' #txt
Ds0 f12 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Call API to &#xD;
produce DOCX document</name>
    </language>
</elementInfo>
' #txt
Ds0 f12 512 530 160 44 -62 -16 #rect
Ds0 f13 expr out #txt
Ds0 f13 95 552 144 552 #arcP
Ds0 f16 expr out #txt
Ds0 f16 472 552 512 552 #arcP
Ds0 f17 expr out #txt
Ds0 f17 272 552 312 552 #arcP
Ds0 f5 actionTable 'out=in;
' #txt
Ds0 f5 actionCode 'import doc.factory.demos.Position;

import doc.factory.demos.Project;
out.report.employee = "George";
out.report.from = ''1.9.2019'';
out.report.till= new Date();

// project 1
Project pr = new Project();
pr.nr = 23.100;
pr.title = "Release 8";

Position p = new Position();
p.date = ''11.9.2019'';
p.code = 43;
p.description = "Mailand";
p.unit.amount=190;
p.unit.text = "Km";
pr.positions.add(p);
p = new Position();
p.date = ''21.9.2019'';
p.code = 43;
p.description = "Madrid";
p.unit.amount=85;
p.unit.text = "Km";
pr.positions.add(p);

out.report.projects.add(pr);

// project 2
pr = new Project();
pr.nr = 10.500;
pr.title = "Summer Sale";

p = new Position();
p.date = ''23.9.2019'';
p.code = 18;
p.description = "Coffee Cups";
p.unit.amount=60;
p.unit.text = "Pieces";
pr.positions.add(p);
p = new Position();
p.date = ''29.9.2019'';
p.code = 18;
p.description = "Travel Expenses";
p.unit.amount=10;
p.unit.text = "Hours";
pr.positions.add(p);
out.report.projects.add(pr);


' #txt
Ds0 f5 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create complex Data</name>
        <nameStyle>19,5
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f5 144 674 128 44 -57 -8 #rect
Ds0 f9 actionTable 'out=in;
' #txt
Ds0 f9 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.LocalResource;

out.templateFile = new LocalResource("resources/myAPITemplateWithNestedTables.docx").asFile();' #txt
Ds0 f9 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>get Template.docx from&#xD;
resources folder</name>
        <nameStyle>4,5
13,5,0
7,5
16,5,0
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f9 312 674 160 44 -61 -16 #rect
Ds0 f14 actionTable 'out=in;
' #txt
Ds0 f14 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.Util.FilesUtil;
import java.util.Locale;
import ch.ivyteam.ivy.addons.docfactory.DocumentTemplate;
		
		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(in.templateFile).
				putDataAsSourceForMailMerge(in.report).
				useLocale(Locale.forLanguageTag("de-CH"));
				
		File output = new File("ivy_DocFactoryDemo/DocWithFullNestedTables.html");
		documentTemplate.produceDocument(output.getJavaFile());
		FilesUtil.setFileRef(output);' #txt
Ds0 f14 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Call API to &#xD;
produce HTML document</name>
    </language>
</elementInfo>
' #txt
Ds0 f14 512 674 160 44 -62 -16 #rect
Ds0 f20 outLink start5.ivp #txt
Ds0 f20 inParamDecl '<> param;' #txt
Ds0 f20 requestEnabled true #txt
Ds0 f20 triggerEnabled false #txt
Ds0 f20 callSignature start5() #txt
Ds0 f20 persist false #txt
Ds0 f20 startName '2.4 Document with nested Tables HTML' #txt
Ds0 f20 taskData 'TaskTriggered.EXPRI=2
TaskTriggered.EXROL=Everybody
TaskTriggered.EXTYPE=0
TaskTriggered.PRI=2
TaskTriggered.ROL=Everybody
TaskTriggered.TYPE=0' #txt
Ds0 f20 caseData businessCase.attach=false #txt
Ds0 f20 showInStartList 1 #txt
Ds0 f20 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Document with nested Tables HTML</name>
        <nameStyle>32,5
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f20 @C|.responsibility Everybody #txt
Ds0 f20 65 681 30 30 -25 31 #rect
Ds0 f22 expr out #txt
Ds0 f22 272 696 312 696 #arcP
Ds0 f23 expr out #txt
Ds0 f23 472 696 512 696 #arcP
Ds0 f25 expr out #txt
Ds0 f25 95 696 144 696 #arcP
Ds0 f28 actionTable 'out=in;
' #txt
Ds0 f28 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.LocalResource;

out.templateFile = new LocalResource("resources/myAPITemplateWithCompositeObject.docx").asFile();' #txt
Ds0 f28 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>get Template.docx from&#xD;
resources folder</name>
        <nameStyle>4,5
13,5,0
7,5
16,5,0
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f28 312 234 160 44 -61 -16 #rect
Ds0 f29 outLink start6.ivp #txt
Ds0 f29 inParamDecl '<> param;' #txt
Ds0 f29 requestEnabled true #txt
Ds0 f29 triggerEnabled false #txt
Ds0 f29 callSignature start6() #txt
Ds0 f29 persist false #txt
Ds0 f29 startName '2.1 Document with CompositeObject' #txt
Ds0 f29 taskData 'TaskTriggered.EXPRI=2
TaskTriggered.EXROL=Everybody
TaskTriggered.EXTYPE=0
TaskTriggered.PRI=2
TaskTriggered.ROL=Everybody
TaskTriggered.TYPE=0' #txt
Ds0 f29 caseData businessCase.attach=true #txt
Ds0 f29 showInStartList 1 #txt
Ds0 f29 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Document with CompositeObject</name>
        <nameStyle>29,5
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f29 @C|.responsibility Everybody #txt
Ds0 f29 65 241 30 30 -19 35 #rect
Ds0 f31 actionTable 'out=in;
' #txt
Ds0 f31 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.Util.FilesUtil;
import java.util.Locale;
import ch.ivyteam.ivy.addons.docfactory.DocumentTemplate;
		
		DocumentTemplate documentTemplate = DocumentTemplate.
				withTemplate(in.templateFile).
				putDataAsSourceForMailMerge(in.position).
				useLocale(Locale.forLanguageTag("de-CH"));
				
		// generate the output file into the ivy /files directory
		File output = new File("ivy_DocFactoryDemo/DocWithCompositeObject.pdf");
		documentTemplate.produceDocument(output.getJavaFile());
		FilesUtil.setFileRef(output);		' #txt
Ds0 f31 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Call API to &#xD;
produce PDF document</name>
    </language>
</elementInfo>
' #txt
Ds0 f31 512 234 160 44 -60 -16 #rect
Ds0 f34 expr out #txt
Ds0 f34 472 256 512 256 #arcP
Ds0 f36 actionTable 'out=in;
' #txt
Ds0 f36 actionCode 'import doc.factory.demos.Position;

//a structured Object
Position p = new Position();
p.date = ''11.9.2019'';
p.code = 1243;
p.description = "Paris";
p.unit.amount=1900;
p.unit.text = "Km";

out.position = p;

out.positions.add(p);
' #txt
Ds0 f36 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create an Object</name>
        <nameStyle>16,5
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f36 144 234 112 44 -46 -8 #rect
Ds0 f27 expr out #txt
Ds0 f27 95 256 144 256 #arcP
Ds0 f32 expr out #txt
Ds0 f32 256 256 312 256 #arcP
Ds0 f35 template "view/done.xhtml" #txt
Ds0 f35 721 241 30 30 0 15 #rect
Ds0 f37 expr out #txt
Ds0 f37 672 256 721 256 #arcP
Ds0 f2 template "view/done.xhtml" #txt
Ds0 f2 721 409 30 30 0 15 #rect
Ds0 f3 template "view/done.xhtml" #txt
Ds0 f3 721 537 30 30 0 15 #rect
Ds0 f10 template "view/done.xhtml" #txt
Ds0 f10 721 681 30 30 0 15 #rect
Ds0 f15 expr out #txt
Ds0 f15 672 424 721 424 #arcP
Ds0 f18 expr out #txt
Ds0 f18 672 552 721 552 #arcP
Ds0 f24 expr out #txt
Ds0 f24 672 696 721 696 #arcP
Ds0 f30 outLink start7.ivp #txt
Ds0 f30 inParamDecl '<> param;' #txt
Ds0 f30 requestEnabled true #txt
Ds0 f30 triggerEnabled false #txt
Ds0 f30 callSignature start7() #txt
Ds0 f30 persist false #txt
Ds0 f30 startName '2.5 Combine several PDfs into one ' #txt
Ds0 f30 taskData 'TaskTriggered.EXPRI=2
TaskTriggered.EXROL=Everybody
TaskTriggered.EXTYPE=0
TaskTriggered.PRI=2
TaskTriggered.ROL=Everybody
TaskTriggered.TYPE=0' #txt
Ds0 f30 caseData businessCase.attach=true #txt
Ds0 f30 showInStartList 1 #txt
Ds0 f30 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Combine several PDfs into one </name>
    </language>
</elementInfo>
' #txt
Ds0 f30 @C|.responsibility Everybody #txt
Ds0 f30 65 865 30 30 -25 31 #rect
Ds0 f33 dialogId doc.factory.demos.SelectSomeFiles #txt
Ds0 f33 startMethod start() #txt
Ds0 f33 requestActionDecl '<> param;' #txt
Ds0 f33 responseActionDecl 'doc.factory.demos.Data out;
' #txt
Ds0 f33 responseMappingAction 'out=in;
out.fileList=result.files;
' #txt
Ds0 f33 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Upload some pdfs</name>
    </language>
</elementInfo>
' #txt
Ds0 f33 144 858 112 44 -51 -8 #rect
Ds0 f38 expr out #txt
Ds0 f38 95 880 144 880 #arcP
Ds0 f39 actionTable 'out=in;
' #txt
Ds0 f39 actionCode 'import com.google.common.io.Files;
import ch.ivyteam.ivy.docFactoryExamples.Util.FilesUtil;
import ch.ivyteam.ivy.addons.docfactory.options.FileAppenderOptions;
import ch.ivyteam.ivy.addons.docfactory.FileAppender;

// append pdf files. Other files than pdf are ignored
java.io.File result = FileAppender.getInstance().appendPdfFiles(in.fileList, 
				FileAppenderOptions.getInstance()
				.withAppendedFileName("appendingPdfTogether")
				.withAppendedFileParentDirectoryPath("/")
				);

// save reference to have a link to the result file
File ivyFile = new File("ivy_DocFactoryDemo/"+result.getName());
ivyFile.createNewFile();
Files.move(result, ivyFile.getJavaFile());
FilesUtil.setFileRef(ivyFile.getJavaFile());' #txt
Ds0 f39 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Call FileAppender API</name>
    </language>
</elementInfo>
' #txt
Ds0 f39 320 858 128 44 -59 -8 #rect
Ds0 f40 expr out #txt
Ds0 f40 256 880 320 880 #arcP
Ds0 f41 template "view/done.xhtml" #txt
Ds0 f41 513 865 30 30 0 15 #rect
Ds0 f43 expr out #txt
Ds0 f43 448 880 513 880 #arcP
>Proto Ds0 .type doc.factory.demos.Data #txt
>Proto Ds0 .processKind NORMAL #txt
>Proto Ds0 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <swimlaneLabel></swimlaneLabel>
        <swimlaneLabel>Document with 
CompositeObject</swimlaneLabel>
        <swimlaneLabel>Document with nested Tables</swimlaneLabel>
        <swimlaneLabel>Combine PDFs </swimlaneLabel>
    </language>
    <swimlaneOrientation>false</swimlaneOrientation>
    <swimlaneSize>160</swimlaneSize>
    <swimlaneSize>192</swimlaneSize>
    <swimlaneSize>424</swimlaneSize>
    <swimlaneSize>192</swimlaneSize>
    <swimlaneColor gradient="false">-1</swimlaneColor>
    <swimlaneColor gradient="false">-1</swimlaneColor>
    <swimlaneColor gradient="false">-1</swimlaneColor>
    <swimlaneColor gradient="false">-1</swimlaneColor>
    <swimlaneType>LANE</swimlaneType>
    <swimlaneType>LANE</swimlaneType>
    <swimlaneType>LANE</swimlaneType>
    <swimlaneType>LANE</swimlaneType>
    <swimlaneSpaceBefore>0</swimlaneSpaceBefore>
    <swimlaneSpaceBefore>0</swimlaneSpaceBefore>
    <swimlaneSpaceBefore>0</swimlaneSpaceBefore>
    <swimlaneSpaceBefore>0</swimlaneSpaceBefore>
</elementInfo>
' #txt
>Proto Ds0 0 0 32 24 18 0 #rect
>Proto Ds0 @|BIcon #fIcon
Ds0 f19 mainOut f26 tail #connect
Ds0 f26 head f21 mainIn #connect
Ds0 f21 mainOut f11 tail #connect
Ds0 f11 head f4 mainIn #connect
Ds0 f4 mainOut f1 tail #connect
Ds0 f1 head f0 mainIn #connect
Ds0 f8 mainOut f13 tail #connect
Ds0 f13 head f6 mainIn #connect
Ds0 f6 mainOut f17 tail #connect
Ds0 f17 head f7 mainIn #connect
Ds0 f7 mainOut f16 tail #connect
Ds0 f16 head f12 mainIn #connect
Ds0 f20 mainOut f25 tail #connect
Ds0 f25 head f5 mainIn #connect
Ds0 f5 mainOut f22 tail #connect
Ds0 f22 head f9 mainIn #connect
Ds0 f9 mainOut f23 tail #connect
Ds0 f23 head f14 mainIn #connect
Ds0 f28 mainOut f34 tail #connect
Ds0 f34 head f31 mainIn #connect
Ds0 f29 mainOut f27 tail #connect
Ds0 f27 head f36 mainIn #connect
Ds0 f36 mainOut f32 tail #connect
Ds0 f32 head f28 mainIn #connect
Ds0 f31 mainOut f37 tail #connect
Ds0 f37 head f35 mainIn #connect
Ds0 f0 mainOut f15 tail #connect
Ds0 f15 head f2 mainIn #connect
Ds0 f12 mainOut f18 tail #connect
Ds0 f18 head f3 mainIn #connect
Ds0 f14 mainOut f24 tail #connect
Ds0 f24 head f10 mainIn #connect
Ds0 f30 mainOut f38 tail #connect
Ds0 f38 head f33 mainIn #connect
Ds0 f33 mainOut f40 tail #connect
Ds0 f40 head f39 mainIn #connect
Ds0 f39 mainOut f43 tail #connect
Ds0 f43 head f41 mainIn #connect
