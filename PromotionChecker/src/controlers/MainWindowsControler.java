package controlers;

import calculated.Item;
import db.DiscountManager;
import db.WarehouseManager;
import db.entities.Discounts;
import db.entities.Warehouse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        } catch (Exception ex) {
            view.showError(ex.getMessage());
        }
    }
    
    public void readDiscountsFromXml(File sourceFile){
        try {
            readDiscountsActionPerformed(new XmlDiscountReader(sourceFile));
        } catch (Exception ex) {
            view.showError(ex.getMessage());
        }
    }
    
    public void readWarehouseActionPerformed(IWarehouseReader warehouseReader){
        HashMap<String, Warehouse> warehouses = readFromFile(warehouseReader);
                
        WarehouseManager warehouseManager = null;
        boolean criticalError = false;
        try {
            warehouseManager = new WarehouseManager();
            for (Entry<String, Warehouse> warehouse : warehouses.entrySet()){
                warehouseManager.create(warehouse.getValue());
            }
            warehouseManager.save();
        } catch(Exception ex){
            view.showCriticalError();
            criticalError = true;
        } finally {
            if (warehouseManager != null){
                warehouseManager.close();
            }
        }
        displayError(errors, criticalError);
        view.repiantWarehouseTable();
        recalculateItems();
    }
    
    private HashMap<String, Warehouse> readFromFile(IWarehouseReader warehouseReader){
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
        return warehouses;
    }

    //TODO: refactor to interface
    public void readDiscountsActionPerformed(XmlDiscountReader xmlDiscountReader) {
        HashMap<String, Discounts> discounts = new HashMap<String, Discounts>();
        //TODO: check prices
        ArrayList<String> errors = new ArrayList<String>();
        while(xmlDiscountReader.hasNext()){
            try {
                Discounts discount = xmlDiscountReader.getNext();
                if (discounts.get(discount.getName()) == null){
                    discounts.put(discount.getName(), discount);
                }
            } catch (RuntimeException ex) {
                errors.add(ex.getMessage());
            }
        }
        
        DiscountManager discountManager = null;
        boolean criticalError = false;
        try {
            discountManager = new DiscountManager();
            for (Entry<String, Discounts> discount : discounts.entrySet()){
                discountManager.create(discount.getValue());
            }
            discountManager.save();
        } catch(Exception ex){
            view.showCriticalError();
            criticalError = true;
        } finally {
            if (discountManager != null){
                discountManager.close();
            }
        }
        displayError(errors, criticalError);

        view.repaintDiscountsTable();
        recalculateItems();
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
    
    private boolean displayErrorIfNeeded(List<String> list){
        if (list.size() > 0){
            view.showError(list);
            return true;
        }
        return false;
    }
}
