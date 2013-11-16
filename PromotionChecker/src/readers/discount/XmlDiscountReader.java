 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package readers.discount;

import db.entities.Discounts;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Miro
 */
public class XmlDiscountReader implements IDiscountReader {

    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private Iterator<Row> rowIterator;
    private int currentRow;
    private int currentSheet;
    
    public XmlDiscountReader(File xml) throws IOException {
        workbook = new HSSFWorkbook(new FileInputStream(xml));
        prepareSheet(0);
    }
    
    public Discounts getNext() {
        if (!hasNext()){
            return null;
        }
        Row row = rowIterator.next();
        currentRow++;
        try {
            Iterator<Cell> cellIterator = row.cellIterator();
            String name = cellIterator.next().getStringCellValue();
            float percentage;
            percentage = getPercentage(cellIterator.next());
            return new Discounts(name, percentage);
        } catch (Exception ex){
            throw new RuntimeException(currentSheet + "");
        }
    }
    
    public boolean hasNext() {
        if (rowIterator.hasNext()){
            return true;
        }
        
        if (currentSheet + 1 < workbook.getNumberOfSheets()){
            prepareSheet(currentSheet + 1);
            return hasNext();
        }
        return false;
    }
    
    private void prepareSheet(int number){
        sheet = workbook.getSheetAt(number);
        currentSheet = number;
        rowIterator = sheet.iterator();
        //skip headers
        if (rowIterator.hasNext()){
            checkSheet(rowIterator.next());
            currentRow = 1;
        }
    }

    private float getPercentage(Cell cell) {
        switch(cell.getCellType()){
            case Cell.CELL_TYPE_NUMERIC:
                return percentageFrom(cell.getNumericCellValue());
            case Cell.CELL_TYPE_STRING:
                return percentageFrom(cell.getStringCellValue());
            default:
                throw new RuntimeException();
        }
    }
    
    private float percentageFrom(String value){
        return Float.valueOf(value.trim().replace("%", ""));
    }
    
    private float percentageFrom(double value){
        if (value < 1){
            return (float)value * 100;
        }
        return (float)value;
    }
    
    private void checkSheet(Row row) {
        if (row.getLastCellNum() > 4){
            throw new RuntimeException("Zła ilość kolumn w pliku.");
        }
    }
}
