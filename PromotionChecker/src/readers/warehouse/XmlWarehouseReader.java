package readers.warehouse;

import db.entities.Warehouse;
import exceptions.ProcessingException;
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
public class XmlWarehouseReader implements IWarehouseReader {

    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private Iterator<Row> rowIterator;
    private int currentPosition = 1;
    
    public XmlWarehouseReader(File xml) throws IOException {
        workbook = new HSSFWorkbook(new FileInputStream(xml));
        sheet = workbook.getSheetAt(0);
        rowIterator = sheet.iterator();
        //skip headers
        rowIterator.next();
    }
    
    public Warehouse getNext() {
        if (!rowIterator.hasNext()){
            return null;
        }
        Row row = rowIterator.next();
        currentPosition++;
        String name = null;
        try {
            Iterator<Cell> cellIterator = row.cellIterator();
            Cell cell = cellIterator.next();
            if (cell.getCellType() == Cell.CELL_TYPE_BLANK){
                return getNext();
            }
            name = cell.getStringCellValue();
            int amount = (int)cellIterator.next().getNumericCellValue();
            float price = (float)cellIterator.next().getNumericCellValue();
            if (name == null || name.trim().length() == 0 || price == 0){
                throw new RuntimeException();
            }
            return new Warehouse(name, amount, price, currentPosition);
        } catch (Exception ex) {
            String message;
            if (name != null){
                message = "NAZWA PRODUKTU: " + name;
            } else {
                message = "LINIA: " + currentPosition;
            }
            throw new RuntimeException(message);
        }
    }
    
    public boolean hasNext() {
        return rowIterator.hasNext();
    }

    private void checkSheet(Row row) {
        if (row.getLastCellNum() < 5){
            throw new ProcessingException("Zła ilość kolumn w pliku.");
        }
    }
}
