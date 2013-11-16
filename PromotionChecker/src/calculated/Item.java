/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package calculated;

import db.entities.Discounts;
import db.entities.Warehouse;

/**
 *
 * @author Miro
 */
public class Item {
    private String name;
    private double originalPrice;
    private float discountInPercentage;
    private double discountInMoney;
    private double priceDiscountIncluded;
    
    public Item(Warehouse warehouse, Discounts discount){
        name = warehouse.getName();
        originalPrice = warehouse.getPrice();
        if (discount != null){
            discountInPercentage = discount.getAmountinpercentage();            
        }
        discountInMoney = originalPrice * (discountInPercentage / 100.0);
        priceDiscountIncluded = originalPrice - discountInMoney;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public float getDiscountInPercentage() {
        return discountInPercentage;
    }

    public void setDiscountInPercentage(float discountInPercentage) {
        this.discountInPercentage = discountInPercentage;
    }

    public double getDiscountInMoney() {
        return discountInMoney;
    }

    public void setDiscountInMoney(double discountInMoney) {
        this.discountInMoney = discountInMoney;
    }

    public double getPriceDiscountIncluded() {
        return priceDiscountIncluded;
    }

    public void setPriceDiscountIncluded(double priceDiscountIncluded) {
        this.priceDiscountIncluded = priceDiscountIncluded;
    }

    
    ;
    public Object[] toArray() {
        return new Object[]{ name, originalPrice, discountInPercentage, discountInMoney, priceDiscountIncluded };
    }
    
}
