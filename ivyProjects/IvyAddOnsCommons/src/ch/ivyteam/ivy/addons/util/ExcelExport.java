package ch.ivyteam.ivy.addons.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import ch.ivyteam.ivy.environment.EnvironmentNotAvailableException;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.persistence.PersistencyException;
import ch.ivyteam.ivy.richdialog.widgets.components.ModelConfigurationProps;
import ch.ivyteam.ivy.richdialog.widgets.components.RTable;
import ch.ivyteam.ivy.scripting.objects.Date;
import ch.ivyteam.ivy.scripting.objects.DateTime;
import ch.ivyteam.ivy.scripting.objects.Time;
import ch.ivyteam.ivy.scripting.objects.util.IvyDefaultValues;
import ch.ivyteam.ivy.scripting.system.IIvyScriptClassRepository;

import com.ulcjava.base.application.table.ITableModel;
import com.ulcjava.base.application.table.ULCTableColumnModel;

/**
 * Helper methods that permits the export to Excel.
 * 
 * @author Rifat Binjos, TI-Informatique
 * @author Patrick Joly, TI-Informatique
 * @author Emmanuel Comba, Soreco
 * @since 11.05.2010
 */
public final class ExcelExport
{
  // @see org.apache.poi.ss.usermodel.BuiltinFormats
  // 0xe, "m/d/yy"
  // 0x15, "h:mm:ss"
  // 0x16, "m/d/yy h:mm"

  private static final int EXCEL_DATETIME_CELL_FORMAT = 0x16;

  private static final int EXCEL_TIME_CELL_FORMAT = 0x15;

  private static final int EXCEL_DATE_CELL_FORMAT = 0x0e;

  private static final String INTERNAL_DATE_FORMAT = "yyyyMMdd'T'HHmmss";

  private static final String DEFAULT_SHEET_NAME = "Table";

  private HSSFWorkbook workBook;

  private HSSFSheet sheet;

  private HSSFRow excelRow;

  private HSSFCell cell;

  private HSSFCellStyle defaultCellStyle;

  private HSSFCellStyle headerCellStyle;

  private HSSFCellStyle dateCellStyle;

  private HSSFCellStyle timeCellStyle;

  private HSSFCellStyle dateTimeCellStyle;

  private DateFormat dateFormatter;

  private ExcelExport()
  {
    this(null);
  }

  private ExcelExport(String sheetName)
  {
    HSSFFont font;

    sheet = null;

    workBook = new HSSFWorkbook();
    defaultCellStyle = workBook.createCellStyle();
    defaultCellStyle.setWrapText(true);
    dateCellStyle = workBook.createCellStyle();
    dateCellStyle.setDataFormat((short) EXCEL_DATE_CELL_FORMAT);
    timeCellStyle = workBook.createCellStyle();
    timeCellStyle.setDataFormat((short) EXCEL_TIME_CELL_FORMAT);
    dateTimeCellStyle = workBook.createCellStyle();
    dateTimeCellStyle.setDataFormat((short) EXCEL_DATETIME_CELL_FORMAT);

    headerCellStyle = workBook.createCellStyle();
    headerCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    font = workBook.createFont();
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    headerCellStyle.setFont(font);
    dateFormatter = new SimpleDateFormat(INTERNAL_DATE_FORMAT);

    sheet = workBook.createSheet(sheetName == null ? DEFAULT_SHEET_NAME : sheetName);
  }

  private static HSSFWorkbook exportListAsExcel(List<String> headers, List<List<Object>> rows,
          String sheetName)
  {
    ExcelExport export;

    export = new ExcelExport(sheetName);

    return export.exportListAsExcel(headers, rows);
  }

  /**
   * Exports a java list of list of object to a stream with Excel content.
   * 
   * @param headers a list of String that is used to fill the 1st line of the Excel sheet
   * @param rows value contents of the sheet
   * @param sheetName name of the Excel worksheet into the Excel file
   * @param outputStream outputStream stream into which the Excel content is written
   * @throws IOException
   */
  public static void exportListAsExcel(List<String> headers, List<List<Object>> rows, String sheetName,
          OutputStream outputStream) throws IOException
  {
    HSSFWorkbook workbook;

    workbook = exportListAsExcel(headers, rows, sheetName);

    write(workbook, outputStream);
  }

  /**
   * Exports an RTable to a stream with Excel content
   * 
   * @param table reference to the RTable to export
   * @param sheetName name of the Excel worksheet into the Excel file
   * @param outputStream stream into which the Excel content is written
   * @throws IOException
   * @throws PersistencyException
   * @throws EnvironmentNotAvailableException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   * @throws NoSuchMethodException
   * @throws IllegalArgumentException
   * @throws SecurityException
   */
  public static void exportRTableAsExcel(RTable table, String sheetName, OutputStream outputStream)
          throws IOException, EnvironmentNotAvailableException, PersistencyException, SecurityException,
          IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    HSSFWorkbook workbook;

    workbook = exportRTableAsExcel(table, sheetName);

    write(workbook, outputStream);
  }

  private static HSSFWorkbook exportRTableAsExcel(RTable table, String sheetName)
          throws EnvironmentNotAvailableException, PersistencyException, SecurityException,
          NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    List<String> headers;
    List<List<Object>> tableValues;
    List<Object> rowValues;
    ULCTableColumnModel columnModel;
    String usedSheetName;
    int rowIndex;
    int columnIndex;
    Object rawValue;
    int rowNumber;
    ITableModel model;
    /**
     * The method getRawValueAt is not visible. Keep that method in this var to avoid getting it every time.
     */
    Method getRawValueAtMethod = null;

    usedSheetName = sheetName;
    if (usedSheetName == null)
    {
      usedSheetName = table.getName();
    }

    headers = new ArrayList<String>();
    columnModel = table.getColumnModel();

    for (int i = 0; i < columnModel.getColumnCount(); i++)
    {
      headers.add(columnModel.getColumn(i).getHeaderValue().toString());
    }

    model = table.getModel();
    rowNumber = model.getRowCount();

    if (model != null)
    {
      Class<?> clazz = model.getClass();
      getRawValueAtMethod = clazz.getMethod("getRawValueAt", Integer.TYPE, Integer.TYPE,
              IIvyScriptClassRepository.class);
    }

    tableValues = new ArrayList<List<Object>>();
    for (int y = 0; y < rowNumber; y++)
    {
      rowValues = new ArrayList<Object>();

      for (int x = 0; x < columnModel.getColumnCount(); x++)
      {

        rowIndex = table.convertRowIndexToModel(y);
        columnIndex = table.convertColumnIndexToModel(x);

        ModelConfigurationProps props = (ModelConfigurationProps) model.getClass().getMethod(
                "getModelConfiguration").invoke(model);
        String fieldName = props.getColumnModelConfigurationProps(columnIndex).getFieldConfiguration();
        Object value;

        value = null;
        if (fieldName != null && !fieldName.isEmpty())
        {
          rawValue = getRawValueAtMethod.invoke(model, rowIndex, columnIndex, Ivy.request().getProject()
                  .getIvyScriptClassRepository());

          if (IvyDefaultValues.isDefaultObject(rawValue))
          {
            rawValue = null;
          }

          if (rawValue != null
                  && (rawValue instanceof Number || rawValue instanceof Date || rawValue instanceof DateTime
                          || rawValue instanceof java.util.Date || rawValue instanceof Time))
          {
            value = rawValue;
          }
        }
        if (value == null)
        {
          value = model.getValueAt(rowIndex, columnIndex);
        }
        rowValues.add(value);

      }
      tableValues.add(rowValues);
    }

    return exportListAsExcel(headers, tableValues, usedSheetName);
  }

  private static void write(HSSFWorkbook workbook, OutputStream outputStream) throws IOException
  {
    workbook.write(outputStream);
  }

  private void addCell(int currentColumn, Object cellContent)
  {
    Object usedCellContent;
    usedCellContent = cellContent;

    // if the object contains ivy default values do not export that value
    if (IvyDefaultValues.isDefaultObject(cellContent))
    {
      usedCellContent = null;
    }
    else if (cellContent instanceof Date)
    {
      usedCellContent = ((Date) cellContent).toDate();
    }
    else if (cellContent instanceof Time)
    {
      usedCellContent = ((Time) cellContent).toDate();
    }
    else if (cellContent instanceof DateTime)
    {
      usedCellContent = ((DateTime) cellContent).toDate();
    }

    if (usedCellContent instanceof Number)
    {
      cell = excelRow.createCell(currentColumn, HSSFCell.CELL_TYPE_NUMERIC);
      cell.setCellValue(((Number) usedCellContent).doubleValue());
      cell.setCellStyle(defaultCellStyle);
    }
    else if (usedCellContent instanceof java.util.Date)
    {
      java.util.Date date = (java.util.Date) usedCellContent;
      String dateString;

      dateString = dateFormatter.format(date);

      cell = excelRow.createCell(currentColumn, HSSFCell.CELL_TYPE_NUMERIC);
      cell.setCellValue((java.util.Date) usedCellContent);
      if (dateString.startsWith("00000000T") || cellContent instanceof Time)
      {
        cell.setCellStyle(timeCellStyle);
      }
      else if (dateString.endsWith("T000000") || cellContent instanceof Date)
      {
        cell.setCellStyle(dateCellStyle);
      }
      else
      {
        cell.setCellStyle(dateTimeCellStyle);
      }
    }
    else
    {
      cell = excelRow.createCell(currentColumn);
      cell.setCellValue(new HSSFRichTextString(usedCellContent == null ? "" : usedCellContent.toString()));
      cell.setCellStyle(defaultCellStyle);
    }
  }

  private void addHeaderCell(int currentColumn, String columnHeader)
  {
    cell = excelRow.createCell(currentColumn);
    cell.setCellValue(new HSSFRichTextString(columnHeader));
    cell.setCellStyle(headerCellStyle);
  }

  private HSSFWorkbook exportListAsExcel(List<String> headers, List<List<Object>> rows)
  {
    int currentColumn;
    short currentRow;
    short columnNumber;

    currentRow = 0;

    columnNumber = 0;
    if (headers != null)
    {
      excelRow = sheet.createRow(currentRow++);
      currentColumn = 0;
      for (String columnHeader : headers)
      {
        addHeaderCell(currentColumn++, columnHeader);
      }
      columnNumber = (short) currentColumn;
    }

    if (rows != null)
    {
      for (Iterable<Object> row : rows)
      {
        excelRow = sheet.createRow(currentRow++);
        currentColumn = 0;
        for (Object cellContent : row)
        {
          addCell(currentColumn, cellContent);
          currentColumn++;
        }
      }
    }
    for (short i = 0; i < columnNumber; i++)
    {
      sheet.autoSizeColumn(i, false);
    }

    return workBook;
  }
}
