package controlers;

import calculated.Item;
import db.DiscountManager;
import db.ProductManager;
import db.entities.Discounts;
import db.entities.Products;
import exceptions.ProcessingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import readers.discount.IDiscountReader;
import readers.discount.XmlDiscountReader;
import readers.product.IProductReader;
import readers.product.XmlProductReader;
import view.ExpirationWindow;
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

    public void readProductFromXml(File sourceFile){
        try {
            readProductActionPerformed(new XmlProductReader(sourceFile));
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
    
    public void readProductActionPerformed(IProductReader productReader){
        HashMap<String, Products> products = readProductsFromFile(productReader);
        saveProductsToDatabase(products);
        view.repiantProductTable();
        recalculateItems();
    }
    
    private HashMap<String, Products> readProductsFromFile(IProductReader productReader){
        HashMap<String, Products> products = new HashMap<String, Products>();
            //TODO: check prices
        ArrayList<String> errors = new ArrayList<String>();
        while(productReader.hasNext()){
            try {
                Products product = productReader.getNext();
                Products fromHash = products.get(product.getName()); 
                if (fromHash != null){
                    fromHash.setAmount(fromHash.getAmount() + product.getAmount());
                } else {
                    products.put(product.getName(), product);
                }
            } catch (RuntimeException ex) {
                errors.add(ex.getMessage());
            }
        }
        throwExceptionIfPresent(errors);
        return products;
    }
    
    private void saveProductsToDatabase(HashMap<String, Products> products){
        ProductManager productManager = null;
        try {
            productManager = new ProductManager();
            for (Entry<String, Products> product : products.entrySet()){
                productManager.create(product.getValue());
            }
            productManager.save();
        } finally {
            if (productManager != null){
                productManager.close();
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
        if (view.getDiscountsList().isEmpty() || view.getProductList().isEmpty()){
            return;
        }
        
        HashMap<String, Discounts> discounts = new HashMap<String, Discounts>();
        for (Discounts discount : view.getDiscountsList()){
            discounts.put(discount.getName(), discount);
        }
        
        List<Item> items = view.getSumupList();
        for (Products product : view.getProductList()){
            items.add(new Item(product, discounts.get(product.getName())));
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

    public void saveProductFileExample(File selectedFile) {
        saveExampleFile(selectedFile, "product.xls", "stan magazynu.xls");
    }
    
    public static boolean expired() {
        GregorianCalendar expirationDate = new GregorianCalendar(2014, 12, 1);
        return expirationDate.compareTo(new GregorianCalendar()) < 0;
    }
}
