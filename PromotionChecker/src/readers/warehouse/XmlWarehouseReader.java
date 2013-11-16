package readers.warehouse;

import db.entities.Warehouse;
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
        try {
            Iterator<Cell> cellIterator = row.cellIterator();
            String name = cellIterator.next().getStringCellValue();
            int amount = (int)cellIterator.next().getNumericCellValue();
            float price = (float)cellIterator.next().getNumericCellValue();
            if (name == null || name.trim().length() == 0 || price == 0){
                throw new RuntimeException();
            }
            return new Warehouse(name, amount, price, currentPosition);
        } catch (Exception ex) {
            throw new RuntimeException(currentPosition + "");
        }
    }
    
    public boolean hasNext() {
        return rowIterator.hasNext();
    }

    private void checkSheet(Row row) {
        if (row.getLastCellNum() < 5){
            throw new RuntimeException("Zła ilość kolumn w pliku.");
        }
    }
}
