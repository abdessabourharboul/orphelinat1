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
    private String anneeScolaireFirst;
    private String anneeScolaireSecond;
    private String silkScolaire;
    private String niveauScolaire;
    private String filiere;
    private Double moyenne1;
    private Double moyenne2;
    private Double moyenneAnnee;
    //0 non decidé , 1 success , 2 fail
    private Integer resultat;
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

    public Double getMoyenne1() {
        return moyenne1;
    }

    public void setMoyenne1(Double moyenne1) {
        this.moyenne1 = moyenne1;
    }

    public Double getMoyenne2() {
        return moyenne2;
    }

    public void setMoyenne2(Double moyenne2) {
        this.moyenne2 = moyenne2;
    }

    public Double getMoyenneAnnee() {
        return moyenneAnnee;
    }

    public void setMoyenneAnnee(Double moyenneAnnee) {
        this.moyenneAnnee = moyenneAnnee;
    }

    public Integer getResultat() {
        return resultat;
    }

    public void setResultat(Integer resultat) {
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

//    @Override
//    public String toString() {
//        return "bean.Scolarite[ id=" + id + " ]";
//    }

    @Override
    public String toString() {
        return "Scolarite{" + "id=" + id + ", anneeScolaireFirst=" + anneeScolaireFirst + ", anneeScolaireSecond=" + anneeScolaireSecond + ", silkScolaire=" + silkScolaire + ", niveauScolaire=" + niveauScolaire + ", moyenneAnnee=" + moyenneAnnee + ", resultat=" + resultat + ", orphelin=" + orphelin + '}';
    }
}
