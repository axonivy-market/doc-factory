package ch.ivyteam.ivy.docFactoryExamples;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.aspose.cells.BorderType;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Color;
import com.aspose.cells.Picture;
import com.aspose.cells.Style;
import com.aspose.cells.TextAlignmentType;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.slides.AutoShape;
import com.aspose.slides.IPPImage;
import com.aspose.slides.IPictureFrame;
import com.aspose.slides.IRow;
import com.aspose.slides.IShape;
import com.aspose.slides.ISlide;
import com.aspose.slides.ISlideCollection;
import com.aspose.slides.ITable;
import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;
import com.aspose.slides.ShapeType;
import com.aspose.words.Cell;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.Node;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.Table;

import ch.ivyteam.ivy.addons.docfactory.AsposeDocFactory;
import ch.ivyteam.ivy.addons.docfactory.DocumentTemplate;
import ch.ivyteam.ivy.addons.docfactory.FileOperationMessage;
import ch.ivyteam.ivy.addons.docfactory.TemplateMergeField;
import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeProduct;
import ch.ivyteam.ivy.addons.docfactory.aspose.LicenseLoader;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.scripting.objects.Record;
import ch.ivyteam.ivy.scripting.objects.Recordset;

public class DocumentCreator {
	private String name;
	private Date date;
	private byte[] image;
	private String imageName;
	private ch.ivyteam.ivy.scripting.objects.File ivyFile;
	private java.util.List<String> expectations;
	private String newExpectation;
	private String memberType;

	private String slideTitle;
	private int numberOfSlide;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public void init() {
		expectations = new ArrayList<>();
		expectations.add("Sunshine");
		expectations.add("Beach Bar");
		memberType = "1";
		slideTitle = "DocFactory Demos: Aspose PPT example";
		numberOfSlide = 1;

		// Load license for aspose slides & cell
		try {
			LicenseLoader.loadLicenseforProduct(AsposeProduct.SLIDES);
			LicenseLoader.loadLicenseforProduct(AsposeProduct.CELLS);
		} catch (Exception e) {
			Ivy.log().error(e);
		}
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
	
	public File createWordDocument() {
		FieldMergingCallBack fieldMergingCallback = new FieldMergingCallBack(200, 200);

		// Use custom call back, allows to use byte array and scaling for image
		AsposeDocFactory asposeDocFactory = (AsposeDocFactory) AsposeDocFactory.getInstance().withFieldMergingCallBack(fieldMergingCallback);

		// Default call back AsposeFieldMergingCallback, this supports java file to merge image
		// AsposeDocFactory asposeDocFactory = (AsposeDocFactory) AsposeDocFactory.getInstance();

		// Load template
		ResourceLoader loader = new ResourceLoader();
		Path templatePath = loader.getResource("resources/myDocumentCreatorTemplate.docx").toPath();

		List<TemplateMergeField> mergeFields = new List<>();
		mergeFields.add(new TemplateMergeField("goldMember", memberType.equals("2")));
		mergeFields.add(new TemplateMergeField("name", this.name));
		mergeFields.add(new TemplateMergeField("date", dateFormat.format(date)));
		if(image !=null )
		{
			mergeFields.add(new TemplateMergeField("image", this.image));
		}
		
		DocumentTemplate documentTemplate = new DocumentTemplate();
		documentTemplate.setTemplatePath(templatePath.toString());
		documentTemplate.setOutputName("WordDocument");
		documentTemplate.setOutputPath("files/application/ivy_DocFactoryDemo");
		documentTemplate.setOutputFormat("docx");
		documentTemplate.setMergeFields(mergeFields);
		documentTemplate.setTablesNamesAndFieldsHashtable(asTable(expectations));

		FileOperationMessage fileOperationMessage = asposeDocFactory.generateDocument(documentTemplate);

		/* Aspose basic part to insert more stuff into the generated document*/
		File resultFile = fileOperationMessage.getFiles().get(0);
		Document doc = null;
		try {
			doc = new Document(resultFile.getAbsolutePath());

			// Insert image
			DocumentBuilder builder = new DocumentBuilder(doc);
			Table table = null;
			// find table by index
			// table = (Table) doc.getChild(NodeType.TABLE, 2, true);

			// find table by inner text
			Node[] tables = doc.getChildNodes(NodeType.TABLE, true).toArray();
			for (int i = 0; i < tables.length; i++) {
				Table t = (Table) tables[i];
				if (t.getRows().get(0).getCells().get(0).getText().trim().contains("table_for_scaled_image")) {
					table = t;
					break;
				}
			}
			Cell cell = table.getFirstRow().getCells().get(0);
			// Clear placeHolder text
			Paragraph para = (Paragraph) cell.getLastParagraph();
			para.remove();
			cell.appendChild(new Paragraph(doc));

			// Add and scale image
			builder.moveTo(cell.getFirstParagraph());
			if (image !=null) 
			{
				builder.insertImage(this.image, 60, 60);
			}	
			// insert scaled image from web
			//builder.insertImage("http://www.gstatic.com/tv/thumb/persons/406338/406338_v9_bb.jpg", 200, 260);

			
			doc.save(resultFile.getAbsolutePath());
		} catch (Exception e) {
			Ivy.log().error(e);
		}

		return resultFile;
	}

	public File createMultiDocument() throws IOException {
		FieldMergingCallBack fieldMergingCallback = new FieldMergingCallBack(200, 200);
		AsposeDocFactory asposeDocFactory = (AsposeDocFactory) AsposeDocFactory.getInstance().withFieldMergingCallBack(fieldMergingCallback);

		// Load template
		ResourceLoader loader = new ResourceLoader();
		Path templatePath = loader.getResource("resources/mySimpleDocTemplate.docx").toPath();

		List<TemplateMergeField> mergeFields = new List<>();
		mergeFields.add(new TemplateMergeField("goldMember", memberType.equals("2")));
		mergeFields.add(new TemplateMergeField("name", this.name));
		mergeFields.add(new TemplateMergeField("image", this.image));
		mergeFields.add(new TemplateMergeField("date", dateFormat.format(date)));

		List<DocumentTemplate> documentTemplates = new List<DocumentTemplate>();

		DocumentTemplate documentTemplate1 = new DocumentTemplate();
		documentTemplate1.setTemplatePath(templatePath.toString());
		documentTemplate1.setOutputName("simple1");
		documentTemplate1.setOutputPath("files/application/ivy_DocFactoryDemo");		
		documentTemplate1.setOutputFormat("docx");
		documentTemplate1.setMergeFields(mergeFields);

		DocumentTemplate documentTemplate2 = new DocumentTemplate();
		documentTemplate2.setTemplatePath(templatePath.toString());
		documentTemplate2.setOutputName("simple2");
		documentTemplate2.setOutputPath("files/application/ivy_DocFactoryDemo");		
		documentTemplate2.setOutputFormat("pdf");
		documentTemplate2.setMergeFields(mergeFields);

		DocumentTemplate documentTemplate3 = new DocumentTemplate();
		documentTemplate3.setTemplatePath(templatePath.toString());
		documentTemplate3.setOutputName("simple3");
		documentTemplate3.setOutputPath("files/application/ivy_DocFactoryDemo");		
		documentTemplate3.setOutputFormat("html");
		documentTemplate3.setMergeFields(mergeFields);

		DocumentTemplate documentTemplate4 = new DocumentTemplate();
		documentTemplate4.setTemplatePath(templatePath.toString());
		documentTemplate4.setOutputName("simple4");
		documentTemplate4.setOutputPath("files/application/ivy_DocFactoryDemo");		
		documentTemplate4.setOutputFormat("odt");
		documentTemplate4.setMergeFields(mergeFields);

		DocumentTemplate documentTemplate5 = new DocumentTemplate();
		documentTemplate5.setTemplatePath(templatePath.toString());
		documentTemplate5.setOutputName("simple5");
		documentTemplate5.setOutputPath("files/application/ivy_DocFactoryDemo");		
		documentTemplate5.setOutputFormat("doc");
		documentTemplate5.setMergeFields(mergeFields);

		DocumentTemplate documentTemplate6 = new DocumentTemplate();
		documentTemplate6.setTemplatePath(templatePath.toString());
		documentTemplate6.setOutputName("simple6");
		documentTemplate6.setOutputPath("files/application/ivy_DocFactoryDemo");		
		documentTemplate6.setOutputFormat("txt");
		documentTemplate6.setMergeFields(mergeFields);

		Hashtable<String, Recordset> tablesNamesAndFieldsHashtable = asTable(expectations);
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
		ch.ivyteam.ivy.scripting.objects.File zip = new ch.ivyteam.ivy.scripting.objects.File("DocFactoryDemo_Documents.zip", true);
		FileOutputStream fos = new FileOutputStream(zip.getJavaFile().getAbsolutePath());
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		ZipOutputStream zos = new ZipOutputStream(bos);

		try {
			for (File file : fileOperationMessage.getFiles()) {
				ZipEntry e = new ZipEntry(file.getName());
				zos.putNextEntry(e);
				byte[] data = Files.readAllBytes(file.toPath());
				zos.write(data, 0, data.length);
				zos.closeEntry();
			}

		} catch (IOException e) {
			Ivy.log().error(e);
		} finally {
			zos.close();
		}

		return zip.getJavaFile();
	}

	public File createSimpleDocument() throws IOException {
		AsposeDocFactory asposeDocFactory = (AsposeDocFactory) AsposeDocFactory.getInstance();

		// Load template
		ResourceLoader loader = new ResourceLoader();
		Path templatePath = loader.getResource("resources/myDocumentCreatorTemplate.docx").toPath();

		// Create value for MergeField
		List<TemplateMergeField> mergeFields = new List<>();
		mergeFields.add(new TemplateMergeField("name", this.name));
		mergeFields.add(new TemplateMergeField("date", dateFormat.format(date)));

		// Create input for AsposeDocFactory
		DocumentTemplate documentTemplate = new DocumentTemplate();
		documentTemplate.setTemplatePath(templatePath.toString());
		documentTemplate.setOutputName("DocFactoryDemo_SimpleDocument");
		documentTemplate.setOutputFormat("docx");
		documentTemplate.setMergeFields(mergeFields);

		// Create document
		FileOperationMessage fileOperationMessage = asposeDocFactory.generateDocument(documentTemplate);

		return fileOperationMessage.getFiles().get(0);
	}

	public File createPowerPoint() {
		// Load template
		ResourceLoader loader = new ResourceLoader();
		Path templatePath = loader.getResource("resources/myPowerPointTemplate.pptx").toPath();
		Presentation presentation = new Presentation(templatePath.toString());

		ch.ivyteam.ivy.scripting.objects.File tempFileIvy = null;
		try {
			tempFileIvy = new ch.ivyteam.ivy.scripting.objects.File("PowerPointDocument.pptx", false);
		} catch (IOException e) {
			Ivy.log().error(e.getMessage());
		}

		ISlideCollection slds = presentation.getSlides();
		ISlide slideOne = slds.get_Item(0);

		// Binding shape data
		updateText(slideOne, "{title}", this.slideTitle);
		updateText(slideOne, "{name}", this.name);
		updateText(slideOne, "{date}", dateFormat.format(this.date));

		// Binding image
		if (this.image != null && this.image.length > 0) {
			IPPImage imgx = presentation.getImages().addImage(this.image);
			IShape imagePlaceHolder = findShape(slideOne, "{image}");
			IPictureFrame pf = slideOne.getShapes().addPictureFrame(ShapeType.Rectangle, imagePlaceHolder.getX(),
					imagePlaceHolder.getY(), imagePlaceHolder.getWidth(), imagePlaceHolder.getHeight(), imgx);
			pf.setAlternativeText("My inserted Image");
			// Do other things with image, Setting relative scale width and height
			// pf.setRelativeScaleHeight(0.8f);

			// remove image placeholder
			slideOne.getShapes().remove(imagePlaceHolder);
		}

		// Binding table data
		ITable table = findTable(slideOne);

		for (int i = 0; i < expectations.size(); i++) {
			IRow row = table.getRows().get_Item(1);
			row.get_Item(0).getTextFrame().setText(i + 1 + "");
			row.get_Item(1).getTextFrame().setText(expectations.get(i));
			table.getRows().addClone(row, false);
		}

		// remove empty row
		table.getRows().removeAt(1, true);

		if (numberOfSlide > 1) {
			for (int i = 0; i < numberOfSlide - 1; i++) {
				ISlide clone = presentation.getSlides().get_Item(0);
				presentation.getSlides().addClone(clone);
			}
		}

		presentation.save(tempFileIvy.getAbsolutePath(), SaveFormat.Pptx);

		return tempFileIvy.getJavaFile();
	}

	public File createExcel() throws Exception {
		// Create excel file
		ch.ivyteam.ivy.scripting.objects.File tempExcel = null;
		try {
			tempExcel = new ch.ivyteam.ivy.scripting.objects.File("ExcelDocument.xlsx", false);

		} catch (IOException e) {
			Ivy.log().error(e.getMessage());
		}

		Workbook workbook = new Workbook();
		Worksheet worksheet = workbook.getWorksheets().get("Sheet1");

		// rename default sheet1
		worksheet.setName("IvyDocument1");

		// Hide grid
		worksheet.setGridlinesVisible(false);

		// add text to cell A1 (row 0, column 0)
		com.aspose.cells.Cell cellA1 = worksheet.getCells().get(0, 0);
		cellA1.setValue("DocFactoryDemos");

		// Binding image
		if(ivyFile!=null)
		{
		int pictureIndex = worksheet.getPictures().add(4, 1, new FileInputStream(this.ivyFile.getJavaFile()));
		Picture picture = worksheet.getPictures().get(pictureIndex);
		picture.setWidth(200);
		picture.setHeight(300);
		}
		worksheet.getCells().get("E5").setValue("Name");
		worksheet.getCells().get("F5").setValue(this.name);

		worksheet.getCells().get("E6").setValue("Date");
		worksheet.getCells().get("F6").setValue(dateFormat.format(this.date));

		Style style = workbook.createStyle();
		style.setHorizontalAlignment(TextAlignmentType.LEFT);
		style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
		style.setBorder(BorderType.LEFT_BORDER, CellBorderType.THIN, Color.getBlack());
		style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.THIN, Color.getBlack());
		style.setBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getBlack());

		// binding table header
		// worksheet.getCells().merge(28, 2, 1, 5);
		worksheet.getCells().get("B26").setValue("Your Wish List ");
		worksheet.getCells().get("B28").setStyle(style);
		worksheet.getCells().get("B28").setValue("#");
		worksheet.getCells().get("C28").setStyle(style);
		worksheet.getCells().get("C28").setValue("Description");

		// binding table data
		int startRow = 29;
		for (int i = 0; i < this.expectations.size(); i++) {
			// worksheet.getCells().merge(startRow, 2, 1, 5);
			worksheet.getCells().get("B" + startRow).setStyle(style);
			worksheet.getCells().get("B" + startRow).setValue(i + 1);
			worksheet.getCells().get("C" + startRow).setStyle(style);
			worksheet.getCells().get("C" + startRow).setValue(expectations.get(i));
			startRow++;
		}

		worksheet.autoFitColumns();

		// Save to ivy file
		workbook.save(tempExcel.getAbsolutePath());

		return tempExcel.getJavaFile();
	}

	private void updateText(ISlide slide, String textToReplace, String newText) {
		AutoShape shape = findShape(slide, textToReplace);
		if (shape != null) {
			shape.getTextFrame().setText(newText);
		}
	}

	private AutoShape findShape(ISlide slide, String alttext) {
		for (int i = 0; i < slide.getShapes().size(); i++) {
			if (slide.getShapes().get_Item(i) instanceof AutoShape) {
				if (((AutoShape) slide.getShapes().get_Item(i)).getTextFrame().getText().trim().equalsIgnoreCase(alttext)) {
					return (AutoShape) slide.getShapes().get_Item(i);
				}
			}
		}

		return null;
	}

	private ITable findTable(ISlide slide) {
		for (IShape shape : slide.getShapes()) {
			if (shape instanceof ITable) {
				return (ITable) shape;
			}
		}
		return null;
	}

	public void download(File file) throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();

		if (file != null) {
			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			String fileName = file.getName();

			response.reset();
			response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

			OutputStream responseOutputStream = response.getOutputStream();
			responseOutputStream.write(Files.readAllBytes(file.toPath()));
			responseOutputStream.flush();
			responseOutputStream.close();
			facesContext.responseComplete();
		} else {
			Ivy.log().info("Template NULL");
		}
	}

	public ch.ivyteam.ivy.scripting.objects.File getIvyFileFromByteArray(String fileName, byte[] content) {
		ch.ivyteam.ivy.scripting.objects.File ivyFile = null;
		try {
			ivyFile = new ch.ivyteam.ivy.scripting.objects.File(fileName, true);
			FileUtils.writeByteArrayToFile(ivyFile.getJavaFile(), content);
		} catch (IOException e) {
			Ivy.log().debug(e);
		}

		return ivyFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public java.util.List<String> getExpectations() {
		return expectations;
	}

	public void setExpectations(java.util.List<String> expectations) {
		this.expectations = expectations;
	}

	public String getNewExpectation() {
		return newExpectation;
	}

	public void setNewExpectation(String newExpectation) {
		this.newExpectation = newExpectation;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public ch.ivyteam.ivy.scripting.objects.File getIvyFile() {
		return ivyFile;
	}

	public void setIvyFile(ch.ivyteam.ivy.scripting.objects.File ivyFile) {
		this.ivyFile = ivyFile;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getSlideTitle() {
		return slideTitle;
	}

	public void setSlideTitle(String slideTitle) {
		this.slideTitle = slideTitle;
	}

	public int getNumberOfSlide() {
		return numberOfSlide;
	}

	public void setNumberOfSlide(int numberOfSlide) {
		this.numberOfSlide = numberOfSlide;
	}

}
