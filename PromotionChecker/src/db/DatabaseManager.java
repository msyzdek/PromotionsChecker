/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Miro
 */
public class DatabaseManager<T> {
    
    protected EntityManagerFactory emf;
    protected EntityManager em;
    
    protected DatabaseManager() {
        emf = Persistence.createEntityManagerFactory("PromotionCheckerPU");
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }
    
    public void create(T entity){
        em.persist(entity);
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize(); 
        close();
    }
    
    public void save(){
        em.getTransaction().commit();
    }
    
    public void close(){
        em.close();
        emf.close();
    }
}
