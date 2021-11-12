package ch.ivyteam.ivy.docFactoryExamples;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.aspose.words.Cell;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.Node;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.Table;

import ch.ivyteam.ivy.addons.docfactory.AsposeDocFactory;
import ch.ivyteam.ivy.addons.docfactory.BaseDocFactory;
import ch.ivyteam.ivy.addons.docfactory.DocumentTemplate;
import ch.ivyteam.ivy.addons.docfactory.FileOperationMessage;
import ch.ivyteam.ivy.addons.docfactory.TemplateMergeField;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.scripting.objects.Record;
import ch.ivyteam.ivy.scripting.objects.Recordset;

public class DocxCreator
{
  private final DocumentCreator service;

  DocxCreator(DocumentCreator service)
  {
    this.service = service;
  }
  
  public java.io.File createSimple(java.io.File template)
  {
    // Create value for MergeField
    List<TemplateMergeField> mergeFields = new List<>();
    mergeFields.add(new TemplateMergeField("name", service.getName()));
    mergeFields.add(new TemplateMergeField("date", service.dateFormat.format(service.getDate())));

    // Create input for AsposeDocFactory
    DocumentTemplate documentTemplate = new DocumentTemplate();
    documentTemplate.setTemplatePath(template.toPath().toString());
    documentTemplate.setOutputName("DocFactoryDemo_SimpleDocument");
    documentTemplate.setOutputFormat("docx");
    documentTemplate.setMergeFields(mergeFields);

    // Create document
    AsposeDocFactory asposeDocFactory = (AsposeDocFactory) BaseDocFactory.getInstance();
    FileOperationMessage fileOperationMessage = asposeDocFactory.generateDocument(documentTemplate);
    return fileOperationMessage.getFiles().get(0);
  }
  
  public java.io.File create(java.io.File template) throws IOException
  {
    // Use custom call back, allows to use byte array and scaling for image
    FieldMergingCallBack fieldMergingCallback = new FieldMergingCallBack(200, 200);

    List<TemplateMergeField> mergeFields = new List<>();
    mergeFields.add(new TemplateMergeField("goldMember", service.getMemberType().equals("2")));
    mergeFields.add(new TemplateMergeField("name", service.getName()));
    mergeFields.add(new TemplateMergeField("date", service.dateFormat.format(service.getDate())));
    if(service.getImage() !=null )
    {
            mergeFields.add(new TemplateMergeField("image", service.getImage()));
    }
    
    ch.ivyteam.ivy.scripting.objects.File output = new ch.ivyteam.ivy.scripting.objects.File("ivy_DocFactoryDemo/WordDocument");
    DocumentTemplate documentTemplate = new DocumentTemplate();
    documentTemplate.setTemplatePath(template.toPath().toString());
    documentTemplate.setOutputName(output.getName());
    documentTemplate.setOutputPath(output.getParentFile().getAbsolutePath());
    documentTemplate.setOutputFormat("docx");
    documentTemplate.setMergeFields(mergeFields);
    documentTemplate.setTablesNamesAndFieldsHashtable(asTable(service.getExpectations()));

    AsposeDocFactory asposeDocFactory = (AsposeDocFactory) BaseDocFactory.getInstance().withFieldMergingCallBack(fieldMergingCallback);
    FileOperationMessage fileOperationMessage = asposeDocFactory.generateDocument(documentTemplate);
    /* Aspose basic part to insert more stuff into the generated document*/
    File resultFile = fileOperationMessage.getFiles().get(0);
    Document doc = null;
    try {
      doc = new Document(resultFile.getAbsolutePath());

      // Insert image
      DocumentBuilder builder = new DocumentBuilder(doc);
      Table table = findTable(doc, "table_for_scaled_image");
      Cell cell = table.getFirstRow().getCells().get(0);
      // Clear placeHolder text
      cell.getLastParagraph().remove();
      cell.appendChild(new Paragraph(doc));

      // Add and scale image
      builder.moveTo(cell.getFirstParagraph());
      if (service.getImage() !=null) 
      {
              builder.insertImage(service.getImage(), 60, 60);
      }       
      // insert scaled image from web
      //builder.insertImage("http://www.gstatic.com/tv/thumb/persons/406338/406338_v9_bb.jpg", 200, 260);
      doc.save(resultFile.getAbsolutePath());
    } catch (Exception e) 
    {
      throw new IOException("Failed to write docx", e);
    }

    return resultFile;
  }
  
  public java.io.File createMulti(java.io.File template) throws IOException
  {
    FieldMergingCallBack fieldMergingCallback = new FieldMergingCallBack(200, 200);
    AsposeDocFactory asposeDocFactory = (AsposeDocFactory) BaseDocFactory.getInstance().withFieldMergingCallBack(fieldMergingCallback);

    Path templatePath = template.toPath();
    List<TemplateMergeField> mergeFields = new List<>();
    mergeFields.add(new TemplateMergeField("goldMember", service.getMemberType().equals("2")));
    mergeFields.add(new TemplateMergeField("name", service.getName()));
    mergeFields.add(new TemplateMergeField("image", service.getImage()));
    mergeFields.add(new TemplateMergeField("date", service.dateFormat.format(service.getDate())));

    List<DocumentTemplate> documentTemplates = new List<DocumentTemplate>();

    ch.ivyteam.ivy.scripting.objects.File output = new ch.ivyteam.ivy.scripting.objects.File("ivy_DocFactoryDemo/outputDocument", true);
    DocumentTemplate documentTemplate1 = new DocumentTemplate();
    documentTemplate1.setTemplatePath(templatePath.toString());
    documentTemplate1.setOutputName("simple1");
    documentTemplate1.setOutputPath(output.getParentFile().getAbsolutePath());                
    documentTemplate1.setOutputFormat("docx");
    documentTemplate1.setMergeFields(mergeFields);

    DocumentTemplate documentTemplate2 = new DocumentTemplate();
    documentTemplate2.setTemplatePath(templatePath.toString());
    documentTemplate2.setOutputName("simple2");
    documentTemplate2.setOutputPath(output.getParentFile().getAbsolutePath());                
    documentTemplate2.setOutputFormat("pdf");
    documentTemplate2.setMergeFields(mergeFields);

    DocumentTemplate documentTemplate3 = new DocumentTemplate();
    documentTemplate3.setTemplatePath(templatePath.toString());
    documentTemplate3.setOutputName("simple3");
    documentTemplate3.setOutputPath(output.getParentFile().getAbsolutePath());                
    documentTemplate3.setOutputFormat("html");
    documentTemplate3.setMergeFields(mergeFields);

    DocumentTemplate documentTemplate4 = new DocumentTemplate();
    documentTemplate4.setTemplatePath(templatePath.toString());
    documentTemplate4.setOutputName("simple4");
    documentTemplate4.setOutputPath(output.getParentFile().getAbsolutePath());                
    documentTemplate4.setOutputFormat("odt");
    documentTemplate4.setMergeFields(mergeFields);

    DocumentTemplate documentTemplate5 = new DocumentTemplate();
    documentTemplate5.setTemplatePath(templatePath.toString());
    documentTemplate5.setOutputName("simple5");
    documentTemplate5.setOutputPath(output.getParentFile().getAbsolutePath());                
    documentTemplate5.setOutputFormat("doc");
    documentTemplate5.setMergeFields(mergeFields);

    DocumentTemplate documentTemplate6 = new DocumentTemplate();
    documentTemplate6.setTemplatePath(templatePath.toString());
    documentTemplate6.setOutputName("simple6");
    documentTemplate6.setOutputPath(output.getParentFile().getAbsolutePath());                
    documentTemplate6.setOutputFormat("txt");
    documentTemplate6.setMergeFields(mergeFields);

    Hashtable<String, Recordset> tablesNamesAndFieldsHashtable = asTable(service.getExpectations());
    documentTemplate1.setTablesNamesAndFieldsHashtable(tablesNamesAndFieldsHashtable);
    documentTemplate2.setTablesNamesAndFieldsHashtable(tablesNamesAndFieldsHashtable);
    documentTemplate3.setTablesNamesAndFieldsHashtable(tablesNamesAndFieldsHashtable);
    documentTemplate4.setTablesNamesAndFieldsHashtable(tablesNamesAndFieldsHashtable);
    documentTemplate5.setTablesNamesAndFieldsHashtable(tablesNamesAndFieldsHashtable);
    documentTemplate6.setTablesNamesAndFieldsHashtable(tablesNamesAndFieldsHashtable);

    documentTemplates.add(documentTemplate1);
    documentTemplates.add(documentTemplate2);
    documentTemplates.add(documentTemplate3);
    documentTemplates.add(documentTemplate4);
    documentTemplates.add(documentTemplate5);
    documentTemplates.add(documentTemplate6);

    FileOperationMessage fileOperationMessage = asposeDocFactory.generateDocuments(documentTemplates);

    Ivy.log().info(fileOperationMessage.getFiles());
    ch.ivyteam.ivy.scripting.objects.File zip = new ch.ivyteam.ivy.scripting.objects.File("ivy_DocFactoryDemo/Documents.zip");
    zip.getParentFile().mkdir();
    zip(zip.getJavaFile(), fileOperationMessage.getFiles());
    return zip.getJavaFile();
  }
   
  private static Hashtable<String, Recordset> asTable(java.util.List<String> expectations)
  {
    Hashtable<String, Recordset> tablesNamesAndFieldsHashtable = new Hashtable<String, Recordset>();
    Recordset rs = new Recordset();
    for (int i = 0; i < expectations.size(); i++) 
    {
      Record r = new Record();
      r.putField("counter", i + 1);
      r.putField("content", expectations.get(i));
      rs.add(r);
    }
    tablesNamesAndFieldsHashtable.put("expect", rs);
    return tablesNamesAndFieldsHashtable;
  }
  
  private static Table findTable(Document doc, String text)
  {
    // find table by index
    // table = (Table) doc.getChild(NodeType.TABLE, 2, true);

    // find table by inner text
    Node[] tables = doc.getChildNodes(NodeType.TABLE, true).toArray();
    for (int i = 0; i < tables.length; i++)
    {
      Table t = (Table) tables[i];
      if (t.getRows().get(0).getCells().get(0).getText().trim().contains(text))
      {
        return t;
      }
    }
    return null;
  }

  private static void zip(File javaFile, List<File> files) throws IOException
  {
    try (
            FileOutputStream fos = new FileOutputStream(javaFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ZipOutputStream zos = new ZipOutputStream(bos);)
    {
      for (File file : files)
      {
        ZipEntry e = new ZipEntry(file.getName());
        zos.putNextEntry(e);
        byte[] data = Files.readAllBytes(file.toPath());
        zos.write(data, 0, data.length);
        zos.closeEntry();
      }
    }
  }
}