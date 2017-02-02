/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author asus
 */
@Entity
public class Veuve implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomVeuve;
    private String metierVeuve;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateNaissance;
    private String cin;
    private Long age;

    @ManyToOne
    private Famille famille;

    @OneToMany(mappedBy = "veuve")
    private List<Orphelin> orphelins;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public List<Orphelin> getOrphelins() {
        if (orphelins == null) {
            orphelins = new ArrayList<>();
        }
        return orphelins;
    }

    public void setOrphelins(List<Orphelin> orphelins) {
        this.orphelins = orphelins;
    }

    public Famille getFamille() {
        return famille;
    }

    public void setFamille(Famille famille) {
        this.famille = famille;
    }

    public String getNomVeuve() {
        return nomVeuve;
    }

    public void setNomVeuve(String nomVeuve) {
        this.nomVeuve = nomVeuve;
    }

    public String getMetierVeuve() {
        return metierVeuve;
    }

    public void setMetierVeuve(String metierVeuve) {
        this.metierVeuve = metierVeuve;
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
        if (!(object instanceof Veuve)) {
            return false;
        }
        Veuve other = (Veuve) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nomVeuve;
    }

}
