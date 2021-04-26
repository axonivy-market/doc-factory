[Ivy]
16CD7829EF6B489B 9.3.0 #module
>Proto >Proto Collection #zClass
Ds0 DocFactoryBarcodeExample Big #zClass
Ds0 B #cInfo
Ds0 #process
Ds0 @TextInP .type .type #zField
Ds0 @TextInP .processKind .processKind #zField
Ds0 @AnnotationInP-0n ai ai #zField
Ds0 @MessageFlowInP-0n messageIn messageIn #zField
Ds0 @MessageFlowOutP-0n messageOut messageOut #zField
Ds0 @TextInP .xml .xml #zField
Ds0 @TextInP .responsibility .responsibility #zField
Ds0 @InfoButton f42 '' #zField
Ds0 @StartRequest f15 '' #zField
Ds0 @GridStep f16 '' #zField
Ds0 @UserDialog f17 '' #zField
Ds0 @EndRequest f18 '' #zField
Ds0 @PushWFArc f19 '' #zField
Ds0 @PushWFArc f20 '' #zField
Ds0 @PushWFArc f21 '' #zField
Ds0 @InfoButton f22 '' #zField
>Proto Ds0 Ds0 DocFactoryBarcodeExample #zField
Ds0 f42 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Uses additional aspose-barcode.jar, that is not included in the Axon Ivy DocFactory by default but can be added to the project classpath.&#13;
The document templates (.docx) used here are loaded from the /src/resources Folder.&#13;
The generated documents are created in the ivy files directory and displayed in the Webbrowser.</name>
        <nameStyle>319,5
</nameStyle>
    </language>
</elementInfo>
' #txt
Ds0 f42 32 66 752 60 -367 -24 #rect
Ds0 f15 outLink start.ivp #txt
Ds0 f15 inParamDecl '<> param;' #txt
Ds0 f15 requestEnabled true #txt
Ds0 f15 triggerEnabled false #txt
Ds0 f15 callSignature start() #txt
Ds0 f15 startName '1. Generate Barcodes' #txt
Ds0 f15 caseData businessCase.attach=true #txt
Ds0 f15 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Barcode generation</name>
    </language>
</elementInfo>
' #txt
Ds0 f15 @C|.responsibility Everybody #txt
Ds0 f15 49 273 30 30 -33 30 #rect
Ds0 f16 actionTable 'out=in;
' #txt
Ds0 f16 actionCode 'import ch.ivyteam.ivy.docFactoryExamples.IvyAsposeBarcoder;
import com.aspose.barcode.EncodeTypes;
import com.aspose.barcode.generation.BarcodeGenerator;
 
IvyAsposeBarcoder.init();

File tempFile = new File("ivy_DocFactoryDemo");
tempFile.mkdir();

BarcodeGenerator generator = new BarcodeGenerator(EncodeTypes.CODE_128);
generator.setCodeText(in.str);
generator.save(new File("ivy_DocFactoryDemo/code128.png").getJavaFile().getPath());
         
generator.setBarcodeType(EncodeTypes.QR);
generator.getParameters().setResolution(200);
generator.save(new File("ivy_DocFactoryDemo/codeQR.png").getJavaFile().getPath());      
                   
generator.setBarcodeType(EncodeTypes.EAN_14);
generator.save(new File("ivy_DocFactoryDemo/codeEAN_14.png").getJavaFile().getPath());        
         
generator.setBarcodeType(EncodeTypes.DOT_CODE);
generator.getParameters().setResolution(50);
generator.save(new File("ivy_DocFactoryDemo/codeDOT.png").getJavaFile().getPath());      
     
generator.setBarcodeType(EncodeTypes.ISBN);
generator.getParameters().setResolution(150);
generator.save(new File("ivy_DocFactoryDemo/codeISBN.png").getJavaFile().getPath());    

generator.setBarcodeType(EncodeTypes.DATA_MATRIX);
generator.getParameters().setResolution(300);
generator.save(new File("ivy_DocFactoryDemo/codeMatrix.png").getJavaFile().getPath());           ' #txt
Ds0 f16 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Print Barcodes</name>
    </language>
</elementInfo>
' #txt
Ds0 f16 296 266 112 44 -40 -8 #rect
Ds0 f17 dialogId doc.factory.demos.BarCode #txt
Ds0 f17 startMethod start(String) #txt
Ds0 f17 requestActionDecl '<String str> param;' #txt
Ds0 f17 requestMappingAction 'param.str=in.str;
' #txt
Ds0 f17 responseMappingAction 'out=in;
out.str=result.str;
' #txt
Ds0 f17 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Enter Parameter</name>
    </language>
</elementInfo>
' #txt
Ds0 f17 136 266 112 44 -45 -8 #rect
Ds0 f18 template "/ProcessPages/BarcodeExample/Show.ivc" #txt
Ds0 f18 465 273 30 30 0 15 #rect
Ds0 f19 79 288 136 288 #arcP
Ds0 f20 248 288 296 288 #arcP
Ds0 f21 408 288 465 288 #arcP
Ds0 f22 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>Note: That you have to initialize the modul first.</name>
    </language>
</elementInfo>
' #txt
Ds0 f22 584 273 256 30 -125 -8 #rect
>Proto Ds0 .type doc.factory.demos.Data #txt
>Proto Ds0 .processKind NORMAL #txt
>Proto Ds0 .xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <swimlaneLabel></swimlaneLabel>
        <swimlaneLabel>Barcode Generation</swimlaneLabel>
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
Ds0 f16 mainOut f21 tail #connect
Ds0 f21 head f18 mainIn #connect
Ds0 f15 mainOut f19 tail #connect
Ds0 f19 head f17 mainIn #connect
Ds0 f17 mainOut f20 tail #connect
Ds0 f20 head f16 mainIn #connect
