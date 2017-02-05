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
public class Orphelin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String prenom;
    private String tailleChaussures;
    private String sexe;
    private String codeMassar;
    private String description;
    private String photo;
    private Long anneeNaissance;
    private Long age;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateNaissance;

    @ManyToOne
    private Veuve veuve;

    private List<String> ancienPhotos;

    @OneToMany(mappedBy = "orphelin", orphanRemoval = true)
    private List<Scolarite> scolarites;

    @OneToMany(mappedBy = "orphelin", orphanRemoval = true)
    private List<Medicament> medicaments;

    @OneToMany(mappedBy = "orphelin", orphanRemoval = true)
    private List<Maladie> maladies;

    @OneToMany(mappedBy = "orphelin", orphanRemoval = true)
    private List<Bonification> bonifications;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<String> getAncienPhotos() {
        if (ancienPhotos == null) {
            ancienPhotos = new ArrayList<>();
        }
        return ancienPhotos;
    }

    public void setAncienPhotos(List<String> ancienPhotos) {
        this.ancienPhotos = ancienPhotos;
    }

    public List<Medicament> getMedicaments() {
        if (medicaments == null) {
            medicaments = new ArrayList<>();
        }
        return medicaments;
    }

    public void setMedicaments(List<Medicament> medicaments) {
        this.medicaments = medicaments;
    }

    public List<Maladie> getMaladies() {
        if (maladies == null) {
            maladies = new ArrayList<>();
        }
        return maladies;
    }

    public void setMaladies(List<Maladie> maladies) {
        this.maladies = maladies;
    }

    public List<Bonification> getBonifications() {
        if (bonifications == null) {
            bonifications = new ArrayList<>();
        }
        return bonifications;
    }

    public void setBonifications(List<Bonification> bonifications) {
        this.bonifications = bonifications;
    }

    public List<Scolarite> getScolarites() {
        if (scolarites == null) {
            scolarites = new ArrayList<>();
        }
        return scolarites;
    }

    public void setScolarites(List<Scolarite> scolarites) {
        this.scolarites = scolarites;
    }

    public Long getAnneeNaissance() {
        return anneeNaissance;
    }

    public void setAnneeNaissance(Long anneeNaissance) {
        this.anneeNaissance = anneeNaissance;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getCodeMassar() {
        return codeMassar;
    }

    public void setCodeMassar(String codeMassar) {
        this.codeMassar = codeMassar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Veuve getVeuve() {
        return veuve;
    }

    public void setVeuve(Veuve veuve) {
        this.veuve = veuve;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getTailleChaussures() {
        return tailleChaussures;
    }

    public void setTailleChaussures(String tailleChaussures) {
        this.tailleChaussures = tailleChaussures;
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
        if (!(object instanceof Orphelin)) {
            return false;
        }
        Orphelin other = (Orphelin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return prenom + "  " + getVeuve().getFamille().getNomFamille();
    }

}
