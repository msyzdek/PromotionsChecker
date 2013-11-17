package writers;

import calculated.Sumup;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Miro
 */
public class XmlSumupWriter implements ISumupWriter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private int currentRow;
    
    public XmlSumupWriter() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet();
        addHeaders();
     }
    
    public void writeNext(Sumup sumup) {
        writeRow(new Object[] {sumup.getName(), sumup.getDiscountInPercentage(),
                               sumup.getOriginalPrice(), sumup.getPriceDiscountIncluded()});
    }

    private void writeRow(Object[] values){
        Row row = sheet.createRow(currentRow);
        int currentCell = 0;
        for (Object value : values){
            if (value instanceof Integer){
                row.createCell(currentCell++).setCellValue((Integer)value);  
            } else if (value instanceof Double){
                row.createCell(currentCell++).setCellValue((Double)value);  
            } else {
                row.createCell(currentCell++).setCellValue(value.toString());  
            }
        }
        currentRow++;
    }
    
    public void saveToFile(FileOutputStream xls) throws IOException{
        workbook.write(xls);
    }
    
    private void addHeaders() {
        writeRow(new String[] {"Nazwa", "Rabat", "Cena oryginalna", "Cena po rabacie"});
    }
}
