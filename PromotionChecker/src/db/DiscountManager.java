/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import db.entities.Discounts;

/**
 *
 * @author Miro
 */
public class DiscountManager extends DatabaseManager {
        
    public void create(Discounts discount){
        em.persist(discount);
    }
}
