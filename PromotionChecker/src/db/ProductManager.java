/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import db.entities.Products;
import java.util.List;

/**
 *
 * @author Miro
 */
public class ProductManager extends DatabaseManager<Products> {

    public List<Products> getAll() {
        return em.createNamedQuery("Product.findAll", Products.class).getResultList();
    }
        
}
