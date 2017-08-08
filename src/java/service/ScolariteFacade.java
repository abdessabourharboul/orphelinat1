/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Orphelin;
import bean.Scolarite;
import controller.util.DateUtil;
import controller.util.JsfUtil;
import controller.util.ScolariteUtil;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author asus
 */
@Stateless
public class ScolariteFacade extends AbstractFacade<Scolarite> {

    @PersistenceContext(unitName = "Orphelinat1PU")
    private EntityManager em;

    public String succesNiveauScolaire(String niveauActuel) {
        return ScolariteUtil.succesNiveauScolaire(niveauActuel);
    }

    public String succesSilkScolaire(String niveauActuel) {
        return ScolariteUtil.succesSilkScolaire(niveauActuel);
    }

    public String yearPlusOneString(String anneeString) {
        Integer foo = Integer.parseInt(anneeString);
        foo++;
        String fooString = foo.toString();
        return fooString;
    }

    public void passButtonActionListener(Scolarite scolarite) {
        scolarite.setResultat(true);
        super.edit(scolarite);
        Scolarite newScolarite = scolarite;
        String first = yearPlusOneString(scolarite.getAnneeScolaireFirst());
        String second = yearPlusOneString(scolarite.getAnneeScolaireSecond());
        newScolarite.setAnneeScolaireFirst(first);
        newScolarite.setAnneeScolaireSecond(second);
        newScolarite.setId(null);
        newScolarite.setMoyenne1(null);
        newScolarite.setMoyenne2(null);
        newScolarite.setMoyenneAnnee(null);
        newScolarite.setResultat(null);
        newScolarite.setNiveauScolaire(succesNiveauScolaire(scolarite.getNiveauScolaire()));
        newScolarite.setSilkScolaire(succesSilkScolaire(newScolarite.getNiveauScolaire()));
        List<Scolarite> list = findByOrphelinAndScolarYear(newScolarite.getOrphelin(), newScolarite.getAnneeScolaireFirst());
        if (list.isEmpty()) {
            super.create(newScolarite);
        } else {
            for (int i = 0; i < list.size(); i++) {
                Scolarite get = list.get(i);
                get.setNiveauScolaire(newScolarite.getNiveauScolaire());
                get.setSilkScolaire(newScolarite.getSilkScolaire());
                super.edit(get);
            }
            JsfUtil.addSuccessMessage("تذكير: الدراسة موجودة");
        }
    }

    public void notPassButtonActionListener(Scolarite scolarite) {
        scolarite.setResultat(false);
        super.edit(scolarite);
        Scolarite newScolarite = scolarite;
        String first = yearPlusOneString(scolarite.getAnneeScolaireFirst());
        String second = yearPlusOneString(scolarite.getAnneeScolaireSecond());
        newScolarite.setAnneeScolaireFirst(first);
        newScolarite.setAnneeScolaireSecond(second);
        newScolarite.setId(null);
        newScolarite.setMoyenne1(null);
        newScolarite.setMoyenne2(null);
        newScolarite.setMoyenneAnnee(null);
        newScolarite.setResultat(null);
        List<Scolarite> list = findByOrphelinAndScolarYear(newScolarite.getOrphelin(), newScolarite.getAnneeScolaireFirst());
        if (list.isEmpty()) {
            super.create(newScolarite);
        } else {
            for (int i = 0; i < list.size(); i++) {
                Scolarite get = list.get(i);
                get.setNiveauScolaire(scolarite.getNiveauScolaire());
                get.setSilkScolaire(scolarite.getSilkScolaire());
                super.edit(get);
            }
            JsfUtil.addSuccessMessage("تذكير: الدراسة موجودة");
        }
    }

    public void annulerDecision(Scolarite scolarite) {
        scolarite.setResultat(null);
        super.edit(scolarite);
    }

    public List<String> executerLaRequette(String nomRequette) {
        System.out.println("haaa requette===>" + nomRequette);
        return em.createQuery(nomRequette).getResultList();
    }

    public List<String> findByQueryString(String nomVariable) {
        switch (nomVariable) {
            case "nomOrphelin": {
                String requete = "SELECT DISTINCT  r.nomFamille FROM Famille r";
                return executerLaRequette(requete);
            }
            case "etablissement": {
                String requete = "SELECT DISTINCT  r.etablissement FROM Scolarite r";
                return executerLaRequette(requete);
            }
            case "anneeScolaire": {
                String requete = "SELECT DISTINCT  r.anneeScolaire FROM Scolarite r";
                return executerLaRequette(requete);
            }
            case "niveauScolaire": {
                String requete = "SELECT DISTINCT  r.niveauScolaire FROM Scolarite r";
                return executerLaRequette(requete);
            }
            case "filiere": {
                String requete = "SELECT DISTINCT  r.filiere FROM Scolarite r";
                return executerLaRequette(requete);
            }
            default:
                return new ArrayList<>();
        }
    }

    public List<Scolarite> findByQuery(String nomOrphelin, String etablissement,
            String niveauScolaire, String filiere, Float moyenne1, Float moyenne2,
            Float moyenneAnneeMin, Float moyenneAnneeMax, Float moyenneAnneeExact,
            Boolean resultat, Boolean soutienScolaire, String situation) {
        String requete = "SELECT r FROM Scolarite r WHERE 1=1 ";
        requete += " and r.orphelin.veuve.famille.situation LIKE CONCAT('%','" + situation + "','%')";
        if (nomOrphelin != null && !nomOrphelin.equals("")) {
            requete += " and r.orphelin.veuve.famille.nomFamille='" + nomOrphelin + "'";
        }
        if (etablissement != null && !etablissement.equals("")) {
            requete += " and r.etablissement='" + etablissement + "'";
        }

        if (niveauScolaire != null && !niveauScolaire.equals("")) {
            requete += " and r.niveauScolaire='" + niveauScolaire + "'";
        }
        if (filiere != null && !filiere.equals("")) {
            requete += " and r.filiere='" + filiere + "'";
        }
        if (moyenne1 != null && moyenne1 != 0) {
            requete += " and r.moyenne1='" + moyenne1 + "'";
        }
        if (moyenne2 != null && moyenne2 != 0) {
            requete += " and r.moyenne2='" + moyenne2 + "'";
        }
        if (moyenneAnneeMin != null && moyenneAnneeMin != 0) {
            requete += " and r.moyenneAnnee >='" + moyenneAnneeMin + "'";
        }
        if (moyenneAnneeMax != null && moyenneAnneeMax != 0) {
            requete += " and r.moyenneAnnee <='" + moyenneAnneeMax + "'";
        }
        if (moyenneAnneeExact != null && moyenneAnneeExact != 0) {
            requete += " and r.moyenneAnnee ='" + moyenneAnneeExact + "'";
        }
        if (resultat != null) {
            requete += " AND r.resultat=" + resultat;
        }
        if (soutienScolaire != null) {
            requete += " AND r.soutienScolaire=" + soutienScolaire;
        }
        System.out.println("haaa requette===>" + requete);
        return em.createQuery(requete).getResultList();
    }

    public List<Scolarite> findScolariteBySituation(String situation) {
        String requete = "SELECT r FROM Scolarite r WHERE 1=1 and r.orphelin.veuve.famille.situation LIKE CONCAT('%','" + situation + "','%')";
        System.out.println("haaa requette===>" + requete);
        return em.createQuery(requete).getResultList();
    }

    public List<Scolarite> findScolariteByNiveaux(String niveauScolaire) {
        String requete = "SELECT r FROM Scolarite r WHERE 1=1 and r.niveauScolaire LIKE CONCAT('%','" + niveauScolaire + "','%')";
        System.out.println("haaa requette===>" + requete);
        return em.createQuery(requete).getResultList();
    }

    private List<Scolarite> findScolariteThisYearPrevious() {
        String requete = "SELECT r FROM Scolarite r WHERE 1=1 ";
        requete += " and r.anneeScolaireFirst='" + DateUtil.getPreviousYearString() + "'";
        System.out.println("haaa requette===>" + requete);
        return em.createQuery(requete).getResultList();
    }

    private List<Scolarite> findScolariteThisYearCurrent() {
        String requete = "SELECT r FROM Scolarite r WHERE 1=1 ";
        requete += " and r.anneeScolaireFirst='" + DateUtil.getCurrentYearString() + "'";
        System.out.println("haaa requette===>" + requete);
        return em.createQuery(requete).getResultList();
    }

    public List<Scolarite> findScolariteThisYear() {
        Date d = new Date();
        List<Scolarite> loaded = new ArrayList();
        int month = DateUtil.getMonth();
//        System.out.println("month " + month);
        if (month >= 0 && month <= 6) {
            loaded = findScolariteThisYearPrevious();
        } else {
            loaded = findScolariteThisYearCurrent();
        }
        return loaded;
    }

    @Override
    public void create(Scolarite scolarite) {
        super.create(calculMoyEtAfficherResultat(scolarite));
    }

    @Override
    public void edit(Scolarite scolarite) {
        super.edit(calculMoyEtAfficherResultat(scolarite));
    }

    public Scolarite calculMoyEtAfficherResultat(Scolarite scolarite) {
        if (scolarite.getMoyenne1() == null || scolarite.getMoyenne2() == null) {
            scolarite.setMoyenneAnnee(null);
            scolarite.setResultat(null);
        } else {
            float moy = (scolarite.getMoyenne1() + scolarite.getMoyenne2()) / 2;
            scolarite.setMoyenneAnnee(moy);
        }
        return scolarite;
    }

    public List<Scolarite> findByOrphelinAndScolarYear(Orphelin orphelin, String firstYear) {
        if (orphelin != null && orphelin.getPrenom() != null) {
            String requette = "SELECT s FROM Scolarite s WHERE 1=1 ";
            requette += " and s.orphelin.prenom='" + orphelin.getPrenom() + "'";
            requette += " and s.orphelin.veuve.famille.nomFamille='" + orphelin.getVeuve().getFamille().getNomFamille() + "'";
            requette += " and s.anneeScolaireFirst='" + firstYear + "'";
            System.out.println("findByOrphelinAndScolarYear ==> " + requette);
            return em.createQuery(requette).getResultList();
        }
        return new ArrayList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ScolariteFacade() {
        super(Scolarite.class);
    }

}
