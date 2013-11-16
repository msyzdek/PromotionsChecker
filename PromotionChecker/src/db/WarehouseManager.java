/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import db.entities.Warehouse;
import java.util.List;

/**
 *
 * @author Miro
 */
public class WarehouseManager extends DatabaseManager<Warehouse> {

    public List<Warehouse> getAll() {
        return em.createNamedQuery("Warehouse.findAll", Warehouse.class).getResultList();
    }
        
}
