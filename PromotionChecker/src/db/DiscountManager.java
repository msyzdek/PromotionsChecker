/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import db.entities.Discounts;
import db.entities.Products;
import java.util.GregorianCalendar;

/**
 *
 * @author Miro
 */
public class DiscountManager extends DatabaseManager<Discounts> {
    
    public void deleteAll() {
        em.createQuery("DELETE FROM Discounts").executeUpdate();
    }
}
