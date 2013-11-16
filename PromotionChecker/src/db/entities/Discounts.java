/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db.entities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Miro
 */
@Entity
@Table(name = "DISCOUNTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Discounts.findAll", query = "SELECT d FROM Discounts d"),
    @NamedQuery(name = "Discounts.findById", query = "SELECT d FROM Discounts d WHERE d.id = :id"),
    @NamedQuery(name = "Discounts.findByName", query = "SELECT d FROM Discounts d WHERE d.name = :name"),
    @NamedQuery(name = "Discounts.findByAmountinpercentage", query = "SELECT d FROM Discounts d WHERE d.amountinpercentage = :amountinpercentage"),
    @NamedQuery(name = "Discounts.findByCreatedat", query = "SELECT d FROM Discounts d WHERE d.createdat = :createdat")})
public class Discounts implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "AMOUNTINPERCENTAGE")
    private float amountinpercentage;
    @Column(name = "CREATEDAT")
    @Temporal(TemporalType.DATE)
    private Date createdat;

    public Discounts() {
        createdat = new Date();
    }

    public Discounts(Integer id) {
        this();
        this.id = id;
    }

    public Discounts(String name, float amountinpercentage) {
        this();
        this.name = name;
        this.amountinpercentage = amountinpercentage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Integer oldId = this.id;
        this.id = id;
        changeSupport.firePropertyChange("id", oldId, id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        changeSupport.firePropertyChange("name", oldName, name);
    }

    public float getAmountinpercentage() {
        return amountinpercentage;
    }

    public void setAmountinpercentage(float amountinpercentage) {
        float oldAmountinpercentage = this.amountinpercentage;
        this.amountinpercentage = amountinpercentage;
        changeSupport.firePropertyChange("amountinpercentage", oldAmountinpercentage, amountinpercentage);
    }

    public Date getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        Date oldCreatedat = this.createdat;
        this.createdat = createdat;
        changeSupport.firePropertyChange("createdat", oldCreatedat, createdat);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Discounts)) {
            return false;
        }
        Discounts other = (Discounts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.entities.Discounts[ id=" + id + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
