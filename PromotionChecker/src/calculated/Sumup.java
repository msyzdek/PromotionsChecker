/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package calculated;

import db.entities.Discounts;
import db.entities.Products;

/**
 *
 * @author Miro
 */
public class Sumup {
    private String name;
    private double originalPrice;
    private double discountInPercentage;
    private double discountInMoney;
    private double priceDiscountIncluded;
    
    public Sumup(Products product, Discounts discount){
        name = product.getName();
        originalPrice = product.getPrice();
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

    public double getDiscountInPercentage() {
        return discountInPercentage;
    }

    public void setDiscountInPercentage(double discountInPercentage) {
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
