package ch.ivyteam.ivy.docFactoryExamples;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import com.aspose.cells.BorderType;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Color;
import com.aspose.cells.Picture;
import com.aspose.cells.Style;
import com.aspose.cells.TextAlignmentType;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

class XlsxCreator {
  private final DocumentCreator service;

  public XlsxCreator(DocumentCreator service) {
    this.service = service;
  }

  public File create() throws Exception {
    // Create excel file
    ch.ivyteam.ivy.scripting.objects.File tempExcel = new ch.ivyteam.ivy.scripting.objects.File(
            "ivy_DocFactoryDemo/ExcelDocument.xlsx", false);
    tempExcel.createNewFile();

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
    if (service.getIvyFile() != null) {
      try (InputStream is = new FileInputStream(service.getIvyFile().getJavaFile())) {
        int pictureIndex = worksheet.getPictures().add(4, 1, is);
        Picture picture = worksheet.getPictures().get(pictureIndex);
        picture.setWidth(200);
        picture.setHeight(300);
      }
    }
    worksheet.getCells().get("E5").setValue("Name");
    worksheet.getCells().get("F5").setValue(service.getName());

    worksheet.getCells().get("E6").setValue("Date");
    worksheet.getCells().get("F6").setValue(service.dateFormat.format(service.getDate()));

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
    List<String> expectations = service.getExpectations();
    int startRow = 29;
    for (int i = 0; i < expectations.size(); i++) {
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
}