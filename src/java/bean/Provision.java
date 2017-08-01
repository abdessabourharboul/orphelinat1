/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author asus
 */
@Entity
public class Provision implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annee;
    private Long nombrePersonne;
    private Long quantiteFarine;
    private Long quantiteHuile;
    private Long quantiteSucre;
    private Long quantiteLentilles;
    private Long quantiteThe;
    private Long quantiteRiz;
    private Long quantiteDchicha;
    private Long quantiteMfelka;
    private Long quantiteBeurre;
    private Long quantiteOeuf;
    private Long quantiteOni;
    private Long quantiteShampooing;
    private Long quantiteSavon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public Long getNombrePersonne() {
        return nombrePersonne;
    }

    public void setNombrePersonne(Long nombrePersonne) {
        this.nombrePersonne = nombrePersonne;
    }

    public Long getQuantiteFarine() {
        return quantiteFarine;
    }

    public void setQuantiteFarine(Long quantiteFarine) {
        this.quantiteFarine = quantiteFarine;
    }

    public Long getQuantiteHuile() {
        return quantiteHuile;
    }

    public void setQuantiteHuile(Long quantiteHuile) {
        this.quantiteHuile = quantiteHuile;
    }

    public Long getQuantiteSucre() {
        return quantiteSucre;
    }

    public void setQuantiteSucre(Long quantiteSucre) {
        this.quantiteSucre = quantiteSucre;
    }

    public Long getQuantiteLentilles() {
        return quantiteLentilles;
    }

    public void setQuantiteLentilles(Long quantiteLentilles) {
        this.quantiteLentilles = quantiteLentilles;
    }

    public Long getQuantiteThe() {
        return quantiteThe;
    }

    public void setQuantiteThe(Long quantiteThe) {
        this.quantiteThe = quantiteThe;
    }

    public Long getQuantiteRiz() {
        return quantiteRiz;
    }

    public void setQuantiteRiz(Long quantiteRiz) {
        this.quantiteRiz = quantiteRiz;
    }

    public Long getQuantiteDchicha() {
        return quantiteDchicha;
    }

    public void setQuantiteDchicha(Long quantiteDchicha) {
        this.quantiteDchicha = quantiteDchicha;
    }

    public Long getQuantiteMfelka() {
        return quantiteMfelka;
    }

    public void setQuantiteMfelka(Long quantiteMfelka) {
        this.quantiteMfelka = quantiteMfelka;
    }

    public Long getQuantiteBeurre() {
        return quantiteBeurre;
    }

    public void setQuantiteBeurre(Long quantiteBeurre) {
        this.quantiteBeurre = quantiteBeurre;
    }

    public Long getQuantiteOeuf() {
        return quantiteOeuf;
    }

    public void setQuantiteOeuf(Long quantiteOeuf) {
        this.quantiteOeuf = quantiteOeuf;
    }

    public Long getQuantiteOni() {
        return quantiteOni;
    }

    public void setQuantiteOni(Long quantiteOni) {
        this.quantiteOni = quantiteOni;
    }

    public Long getQuantiteShampooing() {
        return quantiteShampooing;
    }

    public void setQuantiteShampooing(Long quantiteShampooing) {
        this.quantiteShampooing = quantiteShampooing;
    }

    public Long getQuantiteSavon() {
        return quantiteSavon;
    }

    public void setQuantiteSavon(Long quantiteSavon) {
        this.quantiteSavon = quantiteSavon;
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
        if (!(object instanceof Provision)) {
            return false;
        }
        Provision other = (Provision) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Provision[ id=" + id + " ]";
    }

}
