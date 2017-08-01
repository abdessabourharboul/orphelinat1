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
import javax.persistence.ManyToOne;

/**
 *
 * @author asus
 */
@Entity
public class Scolarite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String etablissement;
    private String anneeScolaire;
    private String anneeScolaireFirst;
    private String anneeScolaireSecond;
    private String silkScolaire;
    private String niveauScolaire;
    private String filiere;
    private Float moyenne1;
    private Float moyenne2;
    private Float moyenneAnnee;
    private Boolean resultat;
    private Boolean soutienScolaire;

    @ManyToOne
    private Orphelin orphelin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSilkScolaire() {
        return silkScolaire;
    }

    public void setSilkScolaire(String silkScolaire) {
        this.silkScolaire = silkScolaire;
    }

    public String getAnneeScolaire() {
        return anneeScolaire;
    }

    public void setAnneeScolaire(String anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public String getAnneeScolaireFirst() {
        return anneeScolaireFirst;
    }

    public void setAnneeScolaireFirst(String anneeScolaireFirst) {
        this.anneeScolaireFirst = anneeScolaireFirst;
    }

    public String getAnneeScolaireSecond() {
        return anneeScolaireSecond;
    }

    public void setAnneeScolaireSecond(String anneeScolaireSecond) {
        this.anneeScolaireSecond = anneeScolaireSecond;
    }

    public String getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;
    }

    public String getNiveauScolaire() {
        return niveauScolaire;
    }

    public void setNiveauScolaire(String niveauScolaire) {
        this.niveauScolaire = niveauScolaire;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public Float getMoyenne1() {
        return moyenne1;
    }

    public void setMoyenne1(Float moyenne1) {
        this.moyenne1 = moyenne1;
    }

    public Float getMoyenne2() {
        return moyenne2;
    }

    public void setMoyenne2(Float moyenne2) {
        this.moyenne2 = moyenne2;
    }

    public Float getMoyenneAnnee() {
        return moyenneAnnee;
    }

    public void setMoyenneAnnee(Float moyenneAnnee) {
        this.moyenneAnnee = moyenneAnnee;
    }

    public Boolean getResultat() {
        return resultat;
    }

    public void setResultat(Boolean resultat) {
        this.resultat = resultat;
    }

    public Boolean getSoutienScolaire() {
        return soutienScolaire;
    }

    public void setSoutienScolaire(Boolean soutienScolaire) {
        this.soutienScolaire = soutienScolaire;
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
        if (!(object instanceof Scolarite)) {
            return false;
        }
        Scolarite other = (Scolarite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Scolarite[ id=" + id + " ]";
    }

}
