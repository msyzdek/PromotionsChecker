package controlers;

import calculated.Item;
import db.DiscountManager;
import db.WarehouseManager;
import db.entities.Discounts;
import db.entities.Warehouse;
import exceptions.ProcessingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import readers.discount.IDiscountReader;
import readers.discount.XmlDiscountReader;
import readers.warehouse.IWarehouseReader;
import readers.warehouse.XmlWarehouseReader;
import view.MainWindow;

/**
 *
 * @author Miro
 */
public class MainWindowsControler {
    
    private MainWindow view;

    public MainWindowsControler(MainWindow view){
        this.view = view;
        recalculateItems();
    }

    public void readWarehouseFromXml(File sourceFile){
        try {
            readWarehouseActionPerformed(new XmlWarehouseReader(sourceFile));
        } catch (ProcessingException ex){
            view.showError(ex);
        } catch (Exception ex) {
            view.showCriticalError(ex.getMessage());
        }
    }
    
    public void readDiscountsFromXml(File sourceFile){
        try {
            readDiscountsActionPerformed(new XmlDiscountReader(sourceFile));
        } catch (ProcessingException ex){
            view.showError(ex);
        } catch (Exception ex) {
            view.showCriticalError(ex.getMessage());
        }
    }
    
    public void readWarehouseActionPerformed(IWarehouseReader warehouseReader){
        HashMap<String, Warehouse> warehouses = readWarehousesFromFile(warehouseReader);
        saveWarehousesToDatabase(warehouses);
        view.repiantWarehouseTable();
        recalculateItems();
    }
    
    private HashMap<String, Warehouse> readWarehousesFromFile(IWarehouseReader warehouseReader){
        HashMap<String, Warehouse> warehouses = new HashMap<String, Warehouse>();
            //TODO: check prices
        ArrayList<String> errors = new ArrayList<String>();
        while(warehouseReader.hasNext()){
            try {
                Warehouse warehouse = warehouseReader.getNext();
                Warehouse fromHash = warehouses.get(warehouse.getName()); 
                if (fromHash != null){
                    fromHash.setAmount(fromHash.getAmount() + warehouse.getAmount());
                } else {
                    warehouses.put(warehouse.getName(), warehouse);
                }
            } catch (RuntimeException ex) {
                errors.add(ex.getMessage());
            }
        }
        throwExceptionIfPresent(errors);
        return warehouses;
    }
    
    private void saveWarehousesToDatabase(HashMap<String, Warehouse> warehouses){
        WarehouseManager warehouseManager = null;
        try {
            warehouseManager = new WarehouseManager();
            for (Entry<String, Warehouse> warehouse : warehouses.entrySet()){
                warehouseManager.create(warehouse.getValue());
            }
            warehouseManager.save();
        } finally {
            if (warehouseManager != null){
                warehouseManager.close();
            }
        }
    }

    public void readDiscountsActionPerformed(IDiscountReader discountReader) {
        HashMap<String, Discounts> discounts = readDiscountsFromFile(discountReader);
        saveDiscountsToDatabase(discounts);
        view.repaintDiscountsTable();
        recalculateItems();
    }

    private HashMap<String, Discounts> readDiscountsFromFile(IDiscountReader discountReader){
        HashMap<String, Discounts> discounts = new HashMap<String, Discounts>();
        //TODO: check prices
        ArrayList<String> errors = new ArrayList<String>();
        while(discountReader.hasNext()){
            try {
                Discounts discount = discountReader.getNext();
                if (discounts.get(discount.getName()) == null){
                    discounts.put(discount.getName(), discount);
                }
            } catch (RuntimeException ex) {
                errors.add(ex.getMessage());
            }
        }
        throwExceptionIfPresent(errors);
        return discounts;
    }

    private void saveDiscountsToDatabase(HashMap<String, Discounts> discounts) {
        DiscountManager discountManager = null;
        try {
            discountManager = new DiscountManager();
            for (Entry<String, Discounts> discount : discounts.entrySet()){
                discountManager.create(discount.getValue());
            }
            discountManager.save();
        } finally {
            if (discountManager != null){
                discountManager.close();
            }
        }
    }
    
    private void recalculateItems() {
        if (view.getDiscountsList().isEmpty() || view.getWarehouseList().isEmpty()){
            return;
        }
        
        HashMap<String, Discounts> discounts = new HashMap<String, Discounts>();
        for (Discounts discount : view.getDiscountsList()){
            discounts.put(discount.getName(), discount);
        }
        
        List<Item> items = view.getSumupList();
        for (Warehouse warehouse : view.getWarehouseList()){
            items.add(new Item(warehouse, discounts.get(warehouse.getName())));
        }
        view.repaintItemsTable();
    }
    
    private void throwExceptionIfPresent(List<String> list){
        if (list != null && list.size() > 0){
            throw new ProcessingException(list);
        }
    }

    public void saveExampleFile(File selectedFile, String systemFileName, String saveAs) {
        FileOutputStream fileOutputStream = null;
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("resources/" + systemFileName);
            fileOutputStream = new FileOutputStream(selectedFile + "/" + saveAs);
            byte[] data = new byte[10000];
            int amount;
            while ((amount = inputStream.read(data)) > 0){
                fileOutputStream.write(data, 0, amount);
            }
        } catch (Exception ex) {
            Logger.getLogger(MainWindowsControler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(MainWindowsControler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void saveDiscountFileExample(File selectedFile) {
        saveExampleFile(selectedFile, "dscounts.xls", "rabaty.xls");
    }

    public void saveWarehouseFileExample(File selectedFile) {
        saveExampleFile(selectedFile, "warehouse.xls", "stan magazynu.xls");
    }
}
