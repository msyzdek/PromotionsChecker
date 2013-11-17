package controlers;

import calculated.Sumup;
import db.DiscountManager;
import db.ProductManager;
import db.entities.Discounts;
import db.entities.Products;
import exceptions.ProcessingException;
import java.io.File;
import java.io.FileInputStream;
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
import view.MainWindow;
import writers.ISumupWriter;
import writers.XmlSumupWriter;

/**
 *
 * @author Miro
 */
public class MainWindowsControler {
    
    private MainWindow view;

    public MainWindowsControler(MainWindow view){
        this.view = view;
        recalculatesumups();
    }

    public void readProductFromXml(File sourceFile){
        FileInputStream xml = null;
        try {
            xml = new FileInputStream(sourceFile);
            readProductActionPerformed(new XmlProductReader(xml));
        } catch (ProcessingException ex){
            view.showError(ex);
        } catch (Exception ex) {
            view.showCriticalError(ex.getMessage());
        } finally {
            try {
                if (xml != null){
                    xml.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(MainWindowsControler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void readDiscountsFromXml(File sourceFile){
        FileInputStream xml = null;
        try {
            xml = new FileInputStream(sourceFile);
            readDiscountsActionPerformed(new XmlDiscountReader(xml));
        } catch (ProcessingException ex){
            view.showError(ex);
        } catch (Exception ex) {
            view.showCriticalError(ex.getMessage());
        } finally {
            try {
                if (xml != null){
                    xml.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(MainWindowsControler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void saveSumupToXml(File sourceFile){
        FileOutputStream xml = null;
        try {
            xml = new FileOutputStream(sourceFile + "/ceny po rabacie.xlsx");
            writeSumupActionPerformed( new XmlSumupWriter(), xml);
        } catch (ProcessingException ex){
            view.showError(ex);
        } catch (Exception ex) {
            view.showCriticalError(ex.getMessage());
        } finally {
            try {
                if (xml != null){
                    xml.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(MainWindowsControler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void writeSumupActionPerformed(ISumupWriter sumupWriter, FileOutputStream xml) throws IOException{
        for (Sumup sumup : view.getSumupList()){
            if (sumup.getDiscountInPercentage() > 0){
                sumupWriter.writeNext(sumup);
            }
        }
        sumupWriter.saveToFile(xml);
    }
    
    private void readProductActionPerformed(IProductReader productReader){
        HashMap<String, Products> products = readProductsFromFile(productReader);
        saveProductsToDatabase(products);
        view.repiantProductTable();
        recalculatesumups();
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

    private void readDiscountsActionPerformed(IDiscountReader discountReader) {
        HashMap<String, Discounts> discounts = readDiscountsFromFile(discountReader);
        saveDiscountsToDatabase(discounts);
        view.repaintDiscountsTable();
        recalculatesumups();
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
    
    private void recalculatesumups() {
        if (view.getDiscountsList().isEmpty() || view.getProductList().isEmpty()){
            return;
        }
        
        HashMap<String, Discounts> discounts = new HashMap<String, Discounts>();
        for (Discounts discount : view.getDiscountsList()){
            discounts.put(discount.getName(), discount);
        }
        
        List<Sumup> sumups = view.getSumupList();
        for (Products product : view.getProductList()){
            sumups.add(new Sumup(product, discounts.get(product.getName())));
        }
        view.repaintsumupsTable();
    }
    
    private void throwExceptionIfPresent(List<String> list){
        if (list != null && list.size() > 0){
            throw new ProcessingException(list);
        }
    }

    private void saveExampleFile(File selectedFile, String systemFileName, String saveAs) {
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
        saveExampleFile(selectedFile, "products.xls", "stan magazynu.xls");
    }
    
    public static boolean expired() {
        GregorianCalendar expirationDate = new GregorianCalendar(2014, 12, 1);
        return expirationDate.compareTo(new GregorianCalendar()) < 0;
    }
}
