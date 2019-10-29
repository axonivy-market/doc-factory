[Ivy]
160D67F5A5647B10 7.5.0 #module
>Proto >Proto Collection #zClass
De0 DocFactorySubprocessExamples Big #zClass
De0 B #cInfo
De0 #process
De0 @TextInP .type .type #zField
De0 @TextInP .processKind .processKind #zField
De0 @AnnotationInP-0n ai ai #zField
De0 @MessageFlowInP-0n messageIn messageIn #zField
De0 @MessageFlowOutP-0n messageOut messageOut #zField
De0 @TextInP .xml .xml #zField
De0 @TextInP .responsibility .responsibility #zField
De0 @StartRequest f0 '' #zField
De0 @GridStep f3 '' #zField
De0 @PushWFArc f4 '' #zField
De0 @CallSub f5 '' #zField
De0 @GridStep f7 '' #zField
De0 @PushWFArc f8 '' #zField
De0 @PushWFArc f6 '' #zField
De0 @StartRequest f10 '' #zField
De0 @GridStep f11 '' #zField
De0 @CallSub f12 '' #zField
De0 @GridStep f13 '' #zField
De0 @PushWFArc f14 '' #zField
De0 @PushWFArc f16 '' #zField
De0 @PushWFArc f17 '' #zField
De0 @GridStep f20 '' #zField
De0 @CallSub f22 '' #zField
De0 @PushWFArc f24 '' #zField
De0 @StartRequest f19 '' #zField
De0 @GridStep f27 '' #zField
De0 @CallSub f29 '' #zField
De0 @StartRequest f30 '' #zField
De0 @GridStep f31 '' #zField
De0 @PushWFArc f32 '' #zField
De0 @PushWFArc f33 '' #zField
De0 @PushWFArc f34 '' #zField
De0 @CallSub f37 '' #zField
De0 @GridStep f38 '' #zField
De0 @StartRequest f39 '' #zField
De0 @PushWFArc f43 '' #zField
De0 @GridStep f45 '' #zField
De0 @GridStep f46 '' #zField
De0 @PushWFArc f21 '' #zField
De0 @PushWFArc f25 '' #zField
De0 @PushWFArc f26 '' #zField
De0 @PushWFArc f40 '' #zField
De0 @InfoButton f42 '' #zField
De0 @InfoButton f44 '' #zField
De0 @InfoButton f47 '' #zField
De0 @EndRequest f48 '' #zField
De0 @PushWFArc f49 '' #zField
De0 @EndRequest f1 '' #zField
De0 @EndRequest f2 '' #zField
De0 @EndRequest f50 '' #zField
De0 @EndRequest f51 '' #zField
De0 @PushWFArc f52 '' #zField
De0 @PushWFArc f41 '' #zField
De0 @PushWFArc f35 '' #zField
De0 @PushWFArc f23 '' #zField
>Proto De0 De0 DocFactorySubprocessExamples #zField
De0 f0 outLink start1.ivp #txt
De0 f0 inParamDecl '<> param;' #txt
De0 f0 requestEnabled true #txt
De0 f0 triggerEnabled false #txt
De0 f0 callSignature start1() #txt
De0 f0 persist false #txt
De0 f0 startName '1.1 Document with TemplateMergeFields' #txt
De0 f0 taskData 'TaskTriggered.EXPRI=2
TaskTriggered.EXROL=Everybody
TaskTriggered.EXTYPE=0
TaskTriggered.PRI=2
TaskTriggered.ROL=Everybody
TaskTriggered.TYPE=0' #txt
De0 f0 showInStartList 1 #txt
De0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Document with TemplateMergeFields</name>
        <nameStyle>33,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f0 @C|.responsibility Everybody #txt
De0 f0 81 225 30 30 -30 35 #rect
De0 f0 @|StartRequestIcon #fIcon
De0 f3 actionTable 'out=in;
' #txt
De0 f3 actionCode 'import ch.ivyteam.ivy.addons.docfactory.TemplateMergeField;

//Person Data
in.mergeFields.add(new TemplateMergeField("name", "Hüerlimann"));
in.mergeFields.add(new TemplateMergeField("firstname", "Caty"));
in.mergeFields.add(new TemplateMergeField("address.street", "Baarerstrasse 12"));
in.mergeFields.add(new TemplateMergeField("address.city", "CH-6300 Zug"));
' #txt
De0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create some &#xD;
TemplateMergeFields</name>
        <nameStyle>14,5
19,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f3 136 218 144 44 -54 -16 #rect
De0 f3 @|StepIcon #fIcon
De0 f4 expr out #txt
De0 f4 111 240 136 240 #arcP
De0 f5 processCall 'Functional Processes/writeSerialLetterToOneCorrespondent:writeDocumentWithMailMergeTable(List<ch.ivyteam.ivy.addons.docfactory.TemplateMergeField>,String,String,String,String,List<String>,List<Recordset>)' #txt
De0 f5 requestActionDecl '<List<ch.ivyteam.ivy.addons.docfactory.TemplateMergeField> listOfTemplateMergeFields,String optionalOutputpath,String optionalLetterName,String optionalOutputFormat,String templatePath,List<String> tableNamesList,List<Recordset> recordsetsList> param;' #txt
De0 f5 requestMappingAction 'param.listOfTemplateMergeFields=in.mergeFields;
param.optionalOutputpath="files/application/ivy_DocFactoryDemo";
param.optionalLetterName="DocWithMergeFields";
param.optionalOutputFormat="docx";
param.templatePath=in.templateFile.getAbsolutePath();
' #txt
De0 f5 responseActionDecl 'doc.factory.demos.Data out;
' #txt
De0 f5 responseMappingAction 'out=in;
' #txt
De0 f5 responseActionCode 'import ch.ivyteam.ivy.docFactoryExamples.Util.FilesUtil;
in.templateFile.delete();

FilesUtil.setFileRef(result.fileOperationMessage.files.get(0));' #txt
De0 f5 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create Document&#xD;
with TemplateMergeFields</name>
    </language>
</elementInfo>
' #txt
De0 f5 512 218 160 44 -72 -20 #rect
De0 f5 @|CallSubIcon #fIcon
De0 f7 actionTable 'out=in;
' #txt
De0 f7 actionCode 'import ch.ivyteam.ivy.cm.IContentObjectValue;

File tempFile = new File("Templates/myTemplateWithMergeFields.doc", true);
    ivy.log.debug("javafile "+tempFile);  
if (!tempFile.exists())
{
    //create the template file
    tempFile.createNewFile();
 
 		// export content from cms into the template file
    IContentObjectValue cov = ivy.cms.getContentObjectValue("/Templates/myTemplateWithMergeFields", null, true);
    cov.exportContentToFile(tempFile.getJavaFile(), null);
}

out.templateFile = tempFile.getJavaFile();' #txt
De0 f7 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Get Template from
CMS</name>
        <nameStyle>3,5
18,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f7 320 218 128 44 -45 -16 #rect
De0 f7 @|StepIcon #fIcon
De0 f8 expr out #txt
De0 f8 280 240 320 240 #arcP
De0 f6 expr out #txt
De0 f6 448 240 512 240 #arcP
De0 f10 outLink start2.ivp #txt
De0 f10 inParamDecl '<> param;' #txt
De0 f10 requestEnabled true #txt
De0 f10 triggerEnabled false #txt
De0 f10 callSignature start2() #txt
De0 f10 persist false #txt
De0 f10 startName '1.2 Document with Object' #txt
De0 f10 taskData 'TaskTriggered.EXPRI=2
TaskTriggered.EXROL=Everybody
TaskTriggered.EXTYPE=0
TaskTriggered.PRI=2
TaskTriggered.ROL=Everybody
TaskTriggered.TYPE=0' #txt
De0 f10 showInStartList 1 #txt
De0 f10 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Document with Object</name>
        <nameStyle>20,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f10 @C|.responsibility Everybody #txt
De0 f10 81 417 30 30 -33 38 #rect
De0 f10 @|StartRequestIcon #fIcon
De0 f11 actionTable 'out=in;
out.mergeFields=[];
out.positions=[];
out.report.projects=[];
out.tableRecords=[];
' #txt
De0 f11 actionCode 'import doc.factory.demos.Position;

//a structured Object
Position p = new Position();
p.date = ''11.9.2019'';
p.code = 1243;
p.description = "Öb dem lumik aosdadf";
p.unit.amount=190;
p.unit.text = "Km";

out.position = p;

' #txt
De0 f11 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create an Object</name>
        <nameStyle>16,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f11 152 410 112 44 -46 -8 #rect
De0 f11 @|StepIcon #fIcon
De0 f12 processCall 'Functional Processes/writeSerialLetterToOneCorrespondent:writeDocumentWithOneDataClass(ch.ivyteam.ivy.scripting.objects.CompositeObject,String,String,String,String)' #txt
De0 f12 requestActionDecl '<CompositeObject data,String optionalOutputpath,String optionalLetterName,String optionalOutputFormat,String templatePath> param;' #txt
De0 f12 requestMappingAction 'param.data=in.position;
param.optionalOutputpath="files/application/ivy_DocFactoryDemo";
param.optionalLetterName="DocWithObjectData";
param.optionalOutputFormat="pdf";
param.templatePath=in.templateFile.getAbsolutePath();
' #txt
De0 f12 responseActionDecl 'doc.factory.demos.Data out;
' #txt
De0 f12 responseMappingAction 'out=in;
' #txt
De0 f12 responseActionCode 'import ch.ivyteam.ivy.docFactoryExamples.Util.FilesUtil;
in.templateFile.delete();

FilesUtil.setFileRef(result.fileOperationMessage.files.get(0));' #txt
De0 f12 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create Document
with Object</name>
        <nameStyle>27,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f12 536 410 112 44 -48 -20 #rect
De0 f12 @|CallSubIcon #fIcon
De0 f13 actionTable 'out=in;
' #txt
De0 f13 actionCode 'import ch.ivyteam.ivy.cm.IContentObjectValue;

File tempFile = new File("Templates/myTemplateWithObjectData.doc", true);
if (!tempFile.exists())
{
    //legt leeres file an (muss vorhanden sein für export)
    tempFile.createNewFile();          
 		// export content from cms
    IContentObjectValue cov = ivy.cms.getContentObjectValue("/Templates/myTemplateWithObjectData", null,true);
    cov.exportContentToFile(tempFile.getJavaFile(), null);
}

out.templateFile = tempFile.getJavaFile();' #txt
De0 f13 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Get Template from
CMS</name>
        <nameStyle>3,5
18,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f13 320 410 128 44 -45 -16 #rect
De0 f13 @|StepIcon #fIcon
De0 f14 expr out #txt
De0 f14 111 432 152 432 #arcP
De0 f16 expr out #txt
De0 f16 264 432 320 432 #arcP
De0 f17 expr out #txt
De0 f17 448 432 536 432 #arcP
De0 f20 actionTable 'out=in;
' #txt
De0 f20 actionCode 'import ch.ivyteam.ivy.cm.IContentObjectValue;

File tempFile = new File("Templates/myTemplateWithTable.doc", true);

if (!tempFile.exists())
{
    //legt leeres file an (muss vorhanden sein für export)
    tempFile.createNewFile();          
 		// export content from cms
    IContentObjectValue cov = ivy.cms.getContentObjectValue("/Templates/myTemplateWithTable", null,true);
    cov.exportContentToFile(tempFile.getJavaFile(), null);
}

out.templateFile = tempFile.getJavaFile();' #txt
De0 f20 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Get Template from
CMS</name>
        <nameStyle>3,5
18,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f20 320 602 128 44 -45 -16 #rect
De0 f20 @|StepIcon #fIcon
De0 f22 processCall 'Functional Processes/writeSerialLetterToOneCorrespondent:writeDocumentWithMailMergeTable(List<ch.ivyteam.ivy.addons.docfactory.TemplateMergeField>,String,String,String,String,List<String>,List<Recordset>)' #txt
De0 f22 requestActionDecl '<List<ch.ivyteam.ivy.addons.docfactory.TemplateMergeField> listOfTemplateMergeFields,String optionalOutputpath,String optionalLetterName,String optionalOutputFormat,String templatePath,List<String> tableNamesList,List<Recordset> recordsetsList> param;' #txt
De0 f22 requestMappingAction 'param.listOfTemplateMergeFields=in.mergeFields;
param.optionalOutputpath="files/application/ivy_DocFactoryDemo";
param.optionalLetterName="DocWithTable";
param.optionalOutputFormat="pdf";
param.templatePath=in.templateFile.getAbsolutePath();
param.tableNamesList=in.tableNames;
param.recordsetsList=in.tableRecords;
' #txt
De0 f22 responseActionDecl 'doc.factory.demos.Data out;
' #txt
De0 f22 responseMappingAction 'out=in;
' #txt
De0 f22 responseActionCode 'import ch.ivyteam.ivy.docFactoryExamples.Util.FilesUtil;
in.templateFile.delete();

FilesUtil.setFileRef(result.fileOperationMessage.files.get(0));' #txt
De0 f22 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create Document
with Table</name>
        <nameStyle>26,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f22 536 602 112 44 -48 -20 #rect
De0 f22 @|CallSubIcon #fIcon
De0 f24 expr out #txt
De0 f24 448 624 536 624 #arcP
De0 f19 outLink start3.ivp #txt
De0 f19 inParamDecl '<> param;' #txt
De0 f19 requestEnabled true #txt
De0 f19 triggerEnabled false #txt
De0 f19 callSignature start3() #txt
De0 f19 persist false #txt
De0 f19 startName '1.3 Document with Table' #txt
De0 f19 taskData 'TaskTriggered.EXPRI=2
TaskTriggered.EXROL=Everybody
TaskTriggered.EXTYPE=0
TaskTriggered.PRI=2
TaskTriggered.ROL=Everybody
TaskTriggered.TYPE=0' #txt
De0 f19 caseData businessCase.attach=true #txt
De0 f19 showInStartList 1 #txt
De0 f19 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Document with Table</name>
        <nameStyle>19,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f19 @C|.responsibility Everybody #txt
De0 f19 81 609 30 30 -46 32 #rect
De0 f19 @|StartRequestIcon #fIcon
De0 f27 actionTable 'out=in;
' #txt
De0 f27 actionCode 'import ch.ivyteam.ivy.cm.IContentObjectValue;

File tempFile = new File("Templates/myTemplateWithConditionalText.doc", true);

if (!tempFile.exists())
{
    //legt leeres file an (muss vorhanden sein für export)
    tempFile.createNewFile();          
 		// export content from cms
    IContentObjectValue cov = ivy.cms.getContentObjectValue("/Templates/myTemplateWithConditionalText", null,true);
    cov.exportContentToFile(tempFile.getJavaFile(), null);
}

out.templateFile = tempFile.getJavaFile();' #txt
De0 f27 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Get Template from
CMS</name>
        <nameStyle>3,5
18,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f27 320 770 128 44 -45 -16 #rect
De0 f27 @|StepIcon #fIcon
De0 f29 processCall 'Functional Processes/writeSerialLetterToOneCorrespondent:writeSerialLetterToOneCorrespondant(List<ch.ivyteam.ivy.addons.docfactory.TemplateMergeField>,String,String,String,String)' #txt
De0 f29 requestActionDecl '<List<ch.ivyteam.ivy.addons.docfactory.TemplateMergeField> listOfTemplateMergeFields,String optionalOutputpath,String optionalLetterName,String optionalOutputFormat,String templatePath> param;' #txt
De0 f29 requestMappingAction 'param.listOfTemplateMergeFields=in.mergeFields;
param.optionalOutputpath="files/application/ivy_DocFactoryDemo";
param.optionalLetterName="DocWithConditionalText";
param.optionalOutputFormat="pdf";
param.templatePath=in.templateFile.getAbsolutePath();
' #txt
De0 f29 responseActionDecl 'doc.factory.demos.Data out;
' #txt
De0 f29 responseMappingAction 'out=in;
' #txt
De0 f29 responseActionCode 'import ch.ivyteam.ivy.docFactoryExamples.Util.FilesUtil;
in.templateFile.delete();

FilesUtil.setFileRef(result.fileOperationMessage.files.get(0));' #txt
De0 f29 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create Document
with conditional Text</name>
        <nameStyle>37,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f29 528 770 128 44 -55 -20 #rect
De0 f29 @|CallSubIcon #fIcon
De0 f30 outLink start4.ivp #txt
De0 f30 inParamDecl '<> param;' #txt
De0 f30 requestEnabled true #txt
De0 f30 triggerEnabled false #txt
De0 f30 callSignature start4() #txt
De0 f30 persist false #txt
De0 f30 startName '1.2 Document with Conditional Text' #txt
De0 f30 taskData 'TaskTriggered.EXPRI=2
TaskTriggered.EXROL=Everybody
TaskTriggered.EXTYPE=0
TaskTriggered.PRI=2
TaskTriggered.ROL=Everybody
TaskTriggered.TYPE=0' #txt
De0 f30 caseData businessCase.attach=false #txt
De0 f30 showInStartList 1 #txt
De0 f30 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Document with Conditional Text</name>
        <nameStyle>30,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f30 @C|.responsibility Everybody #txt
De0 f30 81 777 30 30 -46 30 #rect
De0 f30 @|StartRequestIcon #fIcon
De0 f31 actionTable 'out=in;
' #txt
De0 f31 actionCode 'import ch.ivyteam.ivy.addons.docfactory.TemplateMergeField;
//Customer Data
in.mergeFields.add(new TemplateMergeField("name", "Hüerlimann"));
in.mergeFields.add(new TemplateMergeField("firstname", "Caty"));
in.mergeFields.add(new TemplateMergeField("address", "CH-6300 Zug"));
in.mergeFields.add(new TemplateMergeField("country", "Switzerland"));
' #txt
De0 f31 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create some &#xD;
Template MergeFields</name>
        <nameStyle>34,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f31 136 770 144 44 -54 -16 #rect
De0 f31 @|StepIcon #fIcon
De0 f32 expr out #txt
De0 f32 111 792 136 792 #arcP
De0 f33 expr out #txt
De0 f33 280 792 320 792 #arcP
De0 f34 expr out #txt
De0 f34 448 792 528 792 #arcP
De0 f37 processCall 'Functional Processes/writeSerialLetterToOneCorrespondent:writeDocumentWithMailMergeNestedTableWithListOfDatas(String,String,String,String,ch.ivyteam.ivy.scripting.objects.CompositeObject,List<ch.ivyteam.ivy.scripting.objects.CompositeObject>)' #txt
De0 f37 requestActionDecl '<String optionalOutputpath,String optionalLetterName,String optionalOutputFormat,String templatePath,CompositeObject aData,List<CompositeObject> nestedListOfDatas> param;' #txt
De0 f37 requestMappingAction 'param.optionalOutputpath="files/application/ivy_DocFactoryDemo";
param.optionalLetterName="DocWithNestedTables";
param.optionalOutputFormat="pdf";
param.templatePath=in.templateFile.getAbsolutePath();
param.aData=in.report;
param.nestedListOfDatas=in.report.projects;
' #txt
De0 f37 responseActionDecl 'doc.factory.demos.Data out;
' #txt
De0 f37 responseMappingAction 'out=in;
' #txt
De0 f37 responseActionCode 'import ch.ivyteam.ivy.docFactoryExamples.Util.FilesUtil;
in.templateFile.delete();

FilesUtil.setFileRef(result.fileOperationMessage.files.get(0));' #txt
De0 f37 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create Document
with tables in tables</name>
        <nameStyle>37
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f37 528 978 128 44 -54 -20 #rect
De0 f37 @|CallSubIcon #fIcon
De0 f38 actionTable 'out=in;
' #txt
De0 f38 actionCode 'import ch.ivyteam.ivy.cm.IContentObjectValue;

File tempFile = new File("Templates/myTemplateWithNestedTables.doc", true);

if (!tempFile.exists())
{
    //legt leeres file an (muss vorhanden sein für export)
    tempFile.createNewFile();          
 		// export content from cms
    IContentObjectValue cov = ivy.cms.getContentObjectValue("/Templates/myTemplateWithNestedTables", null,true);
    cov.exportContentToFile(tempFile.getJavaFile(), null);
}

out.templateFile = tempFile.getJavaFile();' #txt
De0 f38 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Get Template from
CMS</name>
        <nameStyle>3,5
18,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f38 320 978 128 44 -45 -16 #rect
De0 f38 @|StepIcon #fIcon
De0 f39 outLink start5.ivp #txt
De0 f39 inParamDecl '<> param;' #txt
De0 f39 requestEnabled true #txt
De0 f39 triggerEnabled false #txt
De0 f39 callSignature start5() #txt
De0 f39 persist false #txt
De0 f39 startName '1.5 Document with nested Tables' #txt
De0 f39 taskData 'TaskTriggered.EXPRI=2
TaskTriggered.EXROL=Everybody
TaskTriggered.EXTYPE=0
TaskTriggered.PRI=2
TaskTriggered.ROL=Everybody
TaskTriggered.TYPE=0' #txt
De0 f39 caseData businessCase.attach=true #txt
De0 f39 showInStartList 1 #txt
De0 f39 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Document with nested Tables</name>
        <nameStyle>27,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f39 @C|.responsibility Everybody #txt
De0 f39 81 985 30 30 -50 35 #rect
De0 f39 @|StartRequestIcon #fIcon
De0 f43 expr out #txt
De0 f43 448 1000 528 1000 #arcP
De0 f45 actionTable 'out=in;
' #txt
De0 f45 actionCode 'import ch.ivyteam.ivy.addons.docfactory.TemplateMergeField;
import doc.factory.demos.Position;

//Header Data
in.mergeFields.add(new TemplateMergeField("name", "Hüerlimann"));
in.mergeFields.add(new TemplateMergeField("firstname", "Caty"));
in.mergeFields.add(new TemplateMergeField("address", "CH-6300 Zug"));



//Recordset for Table rows
Recordset rs = new Recordset();
rs.addColumn("date", [new Date(), new Date(), new Date()]);
rs.addColumn("hours", [12,34,51]);
rs.addColumn("desc", ["asdf","erhzh","tzjhfg"]);

out.tableRecords.add(rs);
out.tableNames.add("reporting");' #txt
De0 f45 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create Recordset Data</name>
        <nameStyle>21,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f45 136 602 144 44 -63 -8 #rect
De0 f45 @|StepIcon #fIcon
De0 f46 actionTable 'out=in;
' #txt
De0 f46 actionCode 'import doc.factory.demos.Position;

import doc.factory.demos.Project;
out.report.employee = "George";
out.report.from = ''1.9.2019'';
out.report.till= new Date();

// project 1
Project pr = new Project();
pr.nr = 23.100;
pr.title = "The red brown Fox";

Position p = new Position();
p.date = ''11.9.2019'';
p.code = 43;
p.description = "ghj aadfs adsf";
p.unit.amount=190;
p.unit.text = "Km";
pr.positions.add(p);
p = new Position();
p.date = ''21.9.2019'';
p.code = 43;
p.description = "döns du eimm";
p.unit.amount=85;
p.unit.text = "Km";
pr.positions.add(p);

out.report.projects.add(pr);

// project 2
pr = new Project();
pr.nr = 10.500;
pr.title = "Over the Rainbow";

p = new Position();
p.date = ''23.9.2019'';
p.code = 18;
p.description = "ekn lwe lskfj dflj";
p.unit.amount=60;
p.unit.text = "Pieces";
pr.positions.add(p);
p = new Position();
p.date = ''29.9.2019'';
p.code = 18;
p.description = "rtzundsav asf";
p.unit.amount=10;
p.unit.text = "Hours";
pr.positions.add(p);
out.report.projects.add(pr);

' #txt
De0 f46 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Create complex Data</name>
        <nameStyle>19,5
</nameStyle>
    </language>
</elementInfo>
' #txt
De0 f46 144 978 128 44 -57 -8 #rect
De0 f46 @|StepIcon #fIcon
De0 f21 expr out #txt
De0 f21 111 624 136 624 #arcP
De0 f25 expr out #txt
De0 f25 280 624 320 624 #arcP
De0 f26 expr out #txt
De0 f26 111 1000 144 1000 #arcP
De0 f40 expr out #txt
De0 f40 272 1000 320 1000 #arcP
De0 f42 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>This examples show the usage of the CallSubprocesses provided by the DocFactory project.&#xD;
The data that has to be expanded into {MergeFields} in the document template can be delivered in a number of ways.&#xD;
The word document templates (.doc) used in theses examples are loaded from the CMS. &#xD;
If you execute the samples, you will find the output documents in the folder "ivy_DocFactoryDemo" in the ivy files directory</name>
    </language>
</elementInfo>
' #txt
De0 f42 96 34 672 76 -326 -32 #rect
De0 f42 @|IBIcon #fIcon
De0 f44 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Limitation:&#xD;
Using this method, nested objects &#xD;
e.g. "person.address" are not expanded.</name>
    </language>
</elementInfo>
' #txt
De0 f44 800 402 224 60 -109 -24 #rect
De0 f44 @|IBIcon #fIcon
De0 f47 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Limitation: &#xD;
Using this method, iner nested objects &#xD;
e.g. "person.address" are not expanded.</name>
    </language>
</elementInfo>
' #txt
De0 f47 800 970 224 60 -109 -24 #rect
De0 f47 @|IBIcon #fIcon
De0 f48 template "/ProcessPages/DocFactoryAPIExamples/DonePage.ivc" #txt
De0 f48 721 225 30 30 0 15 #rect
De0 f48 @|EndRequestIcon #fIcon
De0 f49 expr out #txt
De0 f49 672 240 721 240 #arcP
De0 f1 template "/ProcessPages/DocFactoryAPIExamples/DonePage.ivc" #txt
De0 f1 721 417 30 30 0 15 #rect
De0 f1 @|EndRequestIcon #fIcon
De0 f2 template "/ProcessPages/DocFactoryAPIExamples/DonePage.ivc" #txt
De0 f2 721 609 30 30 0 15 #rect
De0 f2 @|EndRequestIcon #fIcon
De0 f50 template "/ProcessPages/DocFactoryAPIExamples/DonePage.ivc" #txt
De0 f50 721 777 30 30 0 15 #rect
De0 f50 @|EndRequestIcon #fIcon
De0 f51 template "/ProcessPages/DocFactoryAPIExamples/DonePage.ivc" #txt
De0 f51 721 985 30 30 0 15 #rect
De0 f51 @|EndRequestIcon #fIcon
De0 f52 expr out #txt
De0 f52 656 1000 721 1000 #arcP
De0 f41 expr out #txt
De0 f41 656 792 721 792 #arcP
De0 f35 expr out #txt
De0 f35 648 624 721 624 #arcP
De0 f23 expr out #txt
De0 f23 648 432 721 432 #arcP
>Proto De0 .type doc.factory.demos.Data #txt
>Proto De0 .processKind NORMAL #txt
>Proto De0 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <swimlaneLabel></swimlaneLabel>
        <swimlaneLabel>TemplateMergeFelds</swimlaneLabel>
        <swimlaneLabel>CompositeObject Data</swimlaneLabel>
        <swimlaneLabel>Document with Table</swimlaneLabel>
        <swimlaneLabel>Conditional Text</swimlaneLabel>
        <swimlaneLabel>Nested Tables</swimlaneLabel>
    </language>
    <swimlaneOrientation>false</swimlaneOrientation>
    <swimlaneSize>160</swimlaneSize>
    <swimlaneSize>176</swimlaneSize>
    <swimlaneSize>192</swimlaneSize>
    <swimlaneSize>184</swimlaneSize>
    <swimlaneSize>192</swimlaneSize>
    <swimlaneSize>192</swimlaneSize>
    <swimlaneColor gradient="false">-1</swimlaneColor>
    <swimlaneColor gradient="false">-1</swimlaneColor>
    <swimlaneColor gradient="false">-1</swimlaneColor>
    <swimlaneColor gradient="false">-1</swimlaneColor>
    <swimlaneColor gradient="false">-1</swimlaneColor>
    <swimlaneColor gradient="false">-1</swimlaneColor>
    <swimlaneType>LANE</swimlaneType>
    <swimlaneType>LANE</swimlaneType>
    <swimlaneType>LANE</swimlaneType>
    <swimlaneType>LANE</swimlaneType>
    <swimlaneType>LANE</swimlaneType>
    <swimlaneType>LANE</swimlaneType>
    <swimlaneSpaceBefore>0</swimlaneSpaceBefore>
    <swimlaneSpaceBefore>0</swimlaneSpaceBefore>
    <swimlaneSpaceBefore>0</swimlaneSpaceBefore>
    <swimlaneSpaceBefore>0</swimlaneSpaceBefore>
    <swimlaneSpaceBefore>0</swimlaneSpaceBefore>
    <swimlaneSpaceBefore>0</swimlaneSpaceBefore>
</elementInfo>
' #txt
>Proto De0 0 0 32 24 18 0 #rect
>Proto De0 @|BIcon #fIcon
De0 f0 mainOut f4 tail #connect
De0 f4 head f3 mainIn #connect
De0 f3 mainOut f8 tail #connect
De0 f8 head f7 mainIn #connect
De0 f7 mainOut f6 tail #connect
De0 f6 head f5 mainIn #connect
De0 f10 mainOut f14 tail #connect
De0 f14 head f11 mainIn #connect
De0 f11 mainOut f16 tail #connect
De0 f16 head f13 mainIn #connect
De0 f13 mainOut f17 tail #connect
De0 f17 head f12 mainIn #connect
De0 f20 mainOut f24 tail #connect
De0 f24 head f22 mainIn #connect
De0 f30 mainOut f32 tail #connect
De0 f32 head f31 mainIn #connect
De0 f31 mainOut f33 tail #connect
De0 f33 head f27 mainIn #connect
De0 f27 mainOut f34 tail #connect
De0 f34 head f29 mainIn #connect
De0 f38 mainOut f43 tail #connect
De0 f43 head f37 mainIn #connect
De0 f19 mainOut f21 tail #connect
De0 f21 head f45 mainIn #connect
De0 f45 mainOut f25 tail #connect
De0 f25 head f20 mainIn #connect
De0 f39 mainOut f26 tail #connect
De0 f26 head f46 mainIn #connect
De0 f46 mainOut f40 tail #connect
De0 f40 head f38 mainIn #connect
De0 f5 mainOut f49 tail #connect
De0 f49 head f48 mainIn #connect
De0 f37 mainOut f52 tail #connect
De0 f52 head f51 mainIn #connect
De0 f29 mainOut f41 tail #connect
De0 f41 head f50 mainIn #connect
De0 f22 mainOut f35 tail #connect
De0 f35 head f2 mainIn #connect
De0 f12 mainOut f23 tail #connect
De0 f23 head f1 mainIn #connect
