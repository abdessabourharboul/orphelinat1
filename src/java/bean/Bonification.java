/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author asus
 */
@Entity
public class Bonification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomBonification;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateBonification;
    private String description;

    @ManyToOne
    private Orphelin orphelin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomBonification() {
        return nomBonification;
    }

    public void setNomBonification(String nomBonification) {
        this.nomBonification = nomBonification;
    }

    public Date getDateBonification() {
        return dateBonification;
    }

    public void setDateBonification(Date dateBonification) {
        this.dateBonification = dateBonification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Orphelin getOrphelin() {
        return orphelin;
    }

    public void setOrphelin(Orphelin orphelin) {
        this.orphelin = orphelin;
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
        if (!(object instanceof Bonification)) {
            return false;
        }
        Bonification other = (Bonification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Bonification[ id=" + id + " ]";
    }

}
