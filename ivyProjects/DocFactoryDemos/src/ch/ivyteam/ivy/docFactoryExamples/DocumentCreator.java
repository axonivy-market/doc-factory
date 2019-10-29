package ch.ivyteam.ivy.docFactoryExamples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.aspose.cells.BorderType;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Color;
import com.aspose.cells.Picture;
import com.aspose.cells.Style;
import com.aspose.cells.TextAlignmentType;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

import ch.ivyteam.ivy.addons.docfactory.AsposeDocFactory;
import ch.ivyteam.ivy.addons.docfactory.BaseDocFactory;
import ch.ivyteam.ivy.addons.docfactory.DocumentTemplate;
import ch.ivyteam.ivy.addons.docfactory.FileOperationMessage;
import ch.ivyteam.ivy.addons.docfactory.TemplateMergeField;
import ch.ivyteam.ivy.addons.docfactory.aspose.AsposeProduct;
import ch.ivyteam.ivy.addons.docfactory.aspose.LicenseLoader;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.scripting.objects.List;

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

	public final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

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

	public File createWordDocument() throws IOException {
	  File template = new LocalResource("resources/myDocumentCreatorTemplate.docx").asFile();
	  File result = new DocxCreator(this).create(template);
	  return result;
	}

	public File createMultiDocument() throws IOException {
	  File template = new LocalResource("resources/mySimpleDocTemplate.docx").asFile();
	  File result = new DocxCreator(this).createMulti(template);
	  return result;
	}

	public File createSimpleDocument()  {
		AsposeDocFactory asposeDocFactory = (AsposeDocFactory) BaseDocFactory.getInstance();

		// Load template
		Path templatePath = new LocalResource("resources/myDocumentCreatorTemplate.docx").asPath();

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

	public File createPowerPoint() throws IOException {
		// Load template
		java.io.File template = new LocalResource("resources/myPowerPointTemplate.pptx").asFile();
		java.io.File result = new PptCreator(this).create(template);
		return result;
	}
	
	public File createExcel() throws Exception {
		// Create excel file
		ch.ivyteam.ivy.scripting.objects.File tempExcel = 
		    new ch.ivyteam.ivy.scripting.objects.File("ExcelDocument.xlsx", false);

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
