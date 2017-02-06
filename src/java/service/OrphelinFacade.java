/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Bonification;
import bean.Maladie;
import bean.Medicament;
import bean.Orphelin;
import bean.Scolarite;
import bean.Veuve;
import controler.util.DateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author asus
 */
@Stateless
public class OrphelinFacade extends AbstractFacade<Orphelin> {

    @PersistenceContext(unitName = "Orphelinat1PU")
    private EntityManager em;
    @EJB
    private FamilleFacade familleFacade;

    public List<String> executerLaRequette(String nomRequette) {
        System.out.println("haaa requette===>" + nomRequette);
        return em.createQuery(nomRequette).getResultList();
    }

    public List<String> findPathByString(Orphelin orphelin) {
        if (orphelin == null || orphelin.getId() == null) {
            return new ArrayList<>();
        }
        String requete = "SELECT r.ancienPhotos FROM Orphelin r WHERE 1=1 AND "
                + " r.id='" + orphelin.getId() + "'";
        return executerLaRequette(requete);
    }

    public List<String> findByQueryString(String nomVariable) {
        switch (nomVariable) {
            case "prenom": {
                String requete = "SELECT DISTINCT  r.prenom FROM Orphelin r";
                return executerLaRequette(requete);
            }
            case "tailleChaussures": {
                String requete = "SELECT DISTINCT  r.tailleChaussures FROM Orphelin r";
                return executerLaRequette(requete);
            }
            case "sexe": {
                String requete = "SELECT DISTINCT  r.sexe FROM Orphelin r";
                return executerLaRequette(requete);
            }
            case "codeMassar": {
                String requete = "SELECT DISTINCT  r.codeMassar FROM Orphelin r";
                return executerLaRequette(requete);
            }
            case "description": {
                String requete = "SELECT DISTINCT  r.description FROM Orphelin r";
                return executerLaRequette(requete);
            }
            default:
                return new ArrayList<>();
        }
    }

    public List<Orphelin> findByQuery(String prenom, String tailleChaussures, String sexe,
            String codeMassar, String description, Long anneeNaissanceMin, Long anneeNaissanceMax,
            Long ageMin, Long ageMax, Date dateNaissanceMin, Date dateNaissanceMax) {
        String requete = "SELECT r FROM Orphelin r WHERE 1=1 ";
        if (prenom != null && !prenom.equals("")) {
            requete += " and r.prenom='" + prenom + "'";
        }
        if (tailleChaussures != null && !tailleChaussures.equals("")) {
            requete += " and r.tailleChaussures='" + tailleChaussures + "'";
        }
        if (sexe != null && !sexe.equals("")) {
            requete += " and r.sexe='" + sexe + "'";
        }
        if (codeMassar != null && !codeMassar.equals("")) {
            requete += " and r.codeMassar='" + codeMassar + "'";
        }
        if (description != null && !description.equals("")) {
            requete += " and r.description='" + description + "'";
        }
        if (anneeNaissanceMin != null && anneeNaissanceMin != 0) {
            requete += " and r.anneeNaissance >='" + anneeNaissanceMin + "'";
        }
        if (anneeNaissanceMax != null && anneeNaissanceMax != 0) {
            requete += " and r.anneeNaissance <='" + anneeNaissanceMax + "'";
        }
        if (ageMin != null && ageMin != 0) {
            requete += " and r.age >='" + ageMin + "'";
        }
        if (ageMax != null && ageMax != 0) {
            requete += " and r.age <='" + ageMax + "'";
        }
        if (dateNaissanceMin != null) {
            requete += " and r.dateNaissance >='" + DateUtil.getSqlDateTime(dateNaissanceMin) + "'";
        }
        if (dateNaissanceMax != null) {
            requete += " and r.dateNaissance <='" + DateUtil.getSqlDateTime(dateNaissanceMax) + "'";
        }
        System.out.println("haaa requette===>" + requete);
        return em.createQuery(requete).getResultList();
    }

    public List<Orphelin> findOrphelinByVeuve(Veuve veuve) {
        if (veuve == null || veuve.getId() == null) {
            return new ArrayList<>();
        } else {
            String req = "SELECT o FROM Orphelin o WHERE o.veuve.id='" + veuve.getId() + "'";
            return em.createQuery(req).getResultList();
        }
    }

    @Override
    public void create(Orphelin orphelin) {
        orphelin.setAnneeNaissance(new Long(DateUtil.getYear(orphelin.getDateNaissance())));
        orphelin.setAge(new Long(DateUtil.calculAge(orphelin.getDateNaissance())));
        orphelin.getVeuve().getFamille().setNombrePersonnes(orphelin.getVeuve().getFamille().getNombrePersonnes() + 1);
        familleFacade.edit(orphelin.getVeuve().getFamille());
        super.create(orphelin);
    }

    @Override
    public void edit(Orphelin orphelin) {
        orphelin.setAnneeNaissance(new Long(DateUtil.getYear(orphelin.getDateNaissance())));
        orphelin.setAge(new Long(DateUtil.calculAge(orphelin.getDateNaissance())));
        super.edit(orphelin);
    }

    public List<Bonification> findBonificationByOrphelin(Orphelin orphelin) {
        if (orphelin == null || orphelin.getId() == null) {
            return new ArrayList<>();
        } else {
            String req = "SELECT ve FROM Bonification ve WHERE ve.orphelin.id='" + orphelin.getId() + "'";
            return em.createQuery(req).getResultList();
        }
    }

    public List<Maladie> findMaladieByOrphelin(Orphelin orphelin) {
        if (orphelin == null || orphelin.getId() == null) {
            return new ArrayList<>();
        } else {
            String req = "SELECT ve FROM Maladie ve WHERE ve.orphelin.id='" + orphelin.getId() + "'";
            return em.createQuery(req).getResultList();
        }
    }

    public List<Medicament> findMedicamentByOrphelin(Orphelin orphelin) {
        if (orphelin == null || orphelin.getId() == null) {
            return new ArrayList<>();
        } else {
            String req = "SELECT ve FROM Medicament ve WHERE ve.orphelin.id='" + orphelin.getId() + "'";
            return em.createQuery(req).getResultList();
        }
    }

    public List<Scolarite> findScolariteByOrphelin(Orphelin orphelin) {
        if (orphelin == null || orphelin.getId() == null) {
            return new ArrayList<>();
        } else {
            String req = "SELECT ve FROM Scolarite ve WHERE ve.orphelin.id='" + orphelin.getId() + "'";
            return em.createQuery(req).getResultList();
        }
    }

//    public List<Object> findObjectByOrphelin(Orphelin orphelin) {
//        if (orphelin == null || orphelin.getId() == null) {
//            return new ArrayList<>();
//        } else {
//            String req = "SELECT ve FROM '" + Object.class + "' ve WHERE ve.orphelin.id='" + orphelin.getId() + "'";
//            return em.createQuery(req).getResultList();
//        }
//    }
    //    @Override
//    public void remove(Orphelin orphelin) {
//        em.createQuery("DELETE FROM Scolarite b WHERE b.orphelin.id ='" + orphelin.getId() + "'").executeUpdate();
//        em.createQuery("DELETE FROM Bonification b WHERE b.orphelin.id ='" + orphelin.getId() + "'").executeUpdate();
//        em.createQuery("DELETE FROM Maladie b WHERE b.orphelin.id ='" + orphelin.getId() + "'").executeUpdate();
//        em.createQuery("DELETE FROM Medicament b WHERE b.orphelin.id ='" + orphelin.getId() + "'").executeUpdate();
//        super.remove(orphelin);
//    }
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrphelinFacade() {
        super(Orphelin.class);
    }

}
