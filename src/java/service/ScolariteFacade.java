package service;

import bean.Orphelin;
import bean.Scolarite;
import controller.util.DateUtil;
import controller.util.JsfUtil;
import controller.util.ScolariteUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author asus
 */
@Stateless
public class ScolariteFacade extends AbstractFacade<Scolarite> {

    @PersistenceContext(unitName = "Orphelinat1PU")
    private EntityManager em;

    @Override
    public void create(Scolarite scolarite) {
        Scolarite scolariteCheck = findByOrphelinAndScolarYearSingle(scolarite.getOrphelin(),
                scolarite.getAnneeScolaireFirst());
        if (scolariteCheck != null) {
            JsfUtil.addErrorMessage("الدراسة موجودة من قبل");
        } else {
            checkAndCalculMoyenneForSilkScolaires(scolarite);
            super.create(scolarite);
            if (scolarite.getResultat() == 1) {
                Scolarite scolariteloadedForScolarite = checkExistenceScolariteByOrphelinAndFirstYear(scolarite);
                if (scolariteloadedForScolarite != null) {
                    System.out.println("Haniiiii");
                    createNextScolariteAFterCreation(scolariteloadedForScolarite);
                }
            }
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ScolariteCreated"));
        }
    }

    public void createNextScolariteAFterCreation(Scolarite scolarite) {
        Scolarite clone = clone(scolarite);
        clone.setAnneeScolaireFirst(yearPlusOneString(clone.getAnneeScolaireFirst()));
        clone.setAnneeScolaireSecond(yearPlusOneString(clone.getAnneeScolaireSecond()));
        clone.setMoyenne1(null);
        clone.setMoyenne2(null);
        clone.setMoyenneAnnee(null);
        clone.setResultat(0);
        clone.setNiveauScolaire(succesNiveauScolaire(clone.getNiveauScolaire()));
        clone.setSilkScolaire(succesSilkScolaire(clone.getNiveauScolaire()));
        Scolarite scolariteCheck = findByOrphelinAndScolarYearSingle(clone.getOrphelin(), clone.getAnneeScolaireFirst());
        if (scolariteCheck != null) {
            System.out.println("scolariteCheck: " + scolariteCheck);
        }
        if (scolariteCheck != null) {
            super.remove(scolariteCheck);
            super.create(clone);
        } else {
            super.create(clone);
        }
    }

    private Scolarite checkExistenceScolariteByOrphelinAndFirstYear(Scolarite scolarite) {
        Scolarite scolariteCheck = findByOrphelinAndScolarYearSingle(scolarite.getOrphelin(),
                scolarite.getAnneeScolaireFirst());
        if (scolariteCheck != null) {
            return scolarite;
        } else {
            return null;
        }
    }

    @Override
    public void edit(Scolarite scolarite) {
        checkAndCalculMoyenneForSilkScolaires(scolarite);
        super.edit(scolarite);
        Scolarite loadedAfterEdit = find(scolarite.getId());
        Scolarite clone = clone(loadedAfterEdit);
        clone.setAnneeScolaireFirst(yearPlusOneString(clone.getAnneeScolaireFirst()));
        clone.setAnneeScolaireSecond(yearPlusOneString(clone.getAnneeScolaireSecond()));
        clone.setMoyenne1(null);
        clone.setMoyenne2(null);
        clone.setMoyenneAnnee(null);
        clone.setResultat(0);
        Scolarite scolariteCheck = findByOrphelinAndScolarYearSingle(clone.getOrphelin(), clone.getAnneeScolaireFirst());
        if (loadedAfterEdit.getResultat() == 0) {
            if (scolariteCheck != null) {
                super.remove(scolariteCheck);
            }
        } else if (loadedAfterEdit.getResultat() == 1) {
            clone.setNiveauScolaire(succesNiveauScolaire(clone.getNiveauScolaire()));
            clone.setSilkScolaire(clone.getNiveauScolaire());
            if (scolariteCheck != null) {
                super.remove(scolariteCheck);
                super.create(clone);
            } else {
                super.create(clone);
            }
        }
    }

    public Scolarite clone(Scolarite oldScolarite) {
        Scolarite newScolarite = new Scolarite();
        newScolarite.setAnneeScolaireFirst(oldScolarite.getAnneeScolaireFirst());
        newScolarite.setAnneeScolaireSecond(oldScolarite.getAnneeScolaireSecond());
        newScolarite.setEtablissement(oldScolarite.getEtablissement());
        newScolarite.setFiliere(oldScolarite.getFiliere());
        newScolarite.setMoyenne1(oldScolarite.getMoyenne1());
        newScolarite.setMoyenne2(oldScolarite.getMoyenne2());
        newScolarite.setMoyenneAnnee(oldScolarite.getMoyenneAnnee());
        newScolarite.setNiveauScolaire(oldScolarite.getNiveauScolaire());
        newScolarite.setOrphelin(oldScolarite.getOrphelin());
        newScolarite.setResultat(oldScolarite.getResultat());
        newScolarite.setSilkScolaire(oldScolarite.getSilkScolaire());
        newScolarite.setSoutienScolaire(oldScolarite.getSoutienScolaire());
        return newScolarite;
    }

    public void passOrNotPassButtonActionListener(Scolarite scolarite) {
        super.edit(scolarite);
        Scolarite loadedAfterEdit = find(scolarite.getId());
        Scolarite newScolarite = clone(loadedAfterEdit);
        newScolarite.setAnneeScolaireFirst(yearPlusOneString(newScolarite.getAnneeScolaireFirst()));
        newScolarite.setAnneeScolaireSecond(yearPlusOneString(newScolarite.getAnneeScolaireSecond()));
        newScolarite.setMoyenne1(null);
        newScolarite.setMoyenne2(null);
        newScolarite.setMoyenneAnnee(null);
        newScolarite.setResultat(0);
        if (loadedAfterEdit.getResultat() == 1) {
            newScolarite.setNiveauScolaire(succesNiveauScolaire(loadedAfterEdit.getNiveauScolaire()));
            newScolarite.setSilkScolaire(succesSilkScolaire(newScolarite.getNiveauScolaire()));
        }
        Scolarite scolariteCheck = findByOrphelinAndScolarYearSingle(newScolarite.getOrphelin(),
                newScolarite.getAnneeScolaireFirst());
        if (scolariteCheck != null) {
            System.out.println("HA scolariteCheck: " + scolariteCheck);
        }
        if (scolariteCheck == null) {
            super.create(newScolarite);
        } else {
            super.remove(scolariteCheck);
            super.create(newScolarite);
            JsfUtil.addSuccessMessage("تذكير: الدراسة موجودة");
        }
    }

    public void annulerDecision(Scolarite scolarite) {
        scolarite.setResultat(0);
        super.edit(scolarite);
    }

    public List<String> executerLaRequetteString(String nomRequette) {
        return em.createQuery(nomRequette).getResultList();
    }

    public String succesNiveauScolaire(String niveauActuel) {
        return ScolariteUtil.succesNiveauScolaire(niveauActuel);
    }

    public String succesSilkScolaire(String niveauActuel) {
        return ScolariteUtil.succesSilkScolaire(niveauActuel);
    }

    public List<String> getSilkScolaires() {
        return ScolariteUtil.getSilkScolaires();
    }

    public String anneeScolaireFirstYear() {
        Date d = new Date();
        int month = DateUtil.getMonth();
        if (month >= 0 && month <= 6) {
            return DateUtil.getPreviousYearString();
        } else {
            return DateUtil.getCurrentYearString();
        }
    }

    public List<Scolarite> findScolariteThisYear() {
        Date d = new Date();
        List<Scolarite> loaded = new ArrayList();
        int month = DateUtil.getMonth();
        if (month >= 0 && month <= 6) {
            loaded = findScolariteThisYearPrevious();
        } else {
            loaded = findScolariteThisYearCurrent();
        }
        return loaded;
    }

    public List<Scolarite> findScolariteBySituation(String situation) {
        String requete = "SELECT r FROM Scolarite r WHERE 1=1 and r.orphelin.veuve.famille.situation LIKE CONCAT('%','" + situation + "','%')";
        return executeQuery(requete);
    }

    public List<Scolarite> findScolariteBySituationThisYear(String situation) {
        String requettePrevious = "SELECT r FROM Scolarite r WHERE 1=1 ";
        requettePrevious += " and r.anneeScolaireFirst='" + DateUtil.getPreviousYearString() + "'";
        requettePrevious += " and r.orphelin.veuve.famille.situation LIKE CONCAT('%','" + situation + "','%')";
        List<Scolarite> listPrevious = executeQuery(requettePrevious);
        String requetteCurrent = "SELECT r FROM Scolarite r WHERE 1=1 ";
        requetteCurrent += " and r.anneeScolaireFirst='" + DateUtil.getCurrentYearString() + "'";
        requetteCurrent += " and r.orphelin.veuve.famille.situation LIKE CONCAT('%','" + situation + "','%')";
        List<Scolarite> listCurrent = executeQuery(requetteCurrent);
        int month = DateUtil.getMonth();
        if (month >= 0 && month <= 6) {
            return listPrevious;
        } else {
            return listCurrent;
        }
    }

    public List<Scolarite> findScolariteByNiveaux(String niveauScolaire) {
        String requete = "SELECT r FROM Scolarite r WHERE 1=1 and r.niveauScolaire LIKE CONCAT('%','" + niveauScolaire + "','%')";
        return executeQuery(requete);
    }

    public List<Scolarite> findScolariteByNiveauxThisYear(String niveauScolaire) {
        String requettePrevious = "SELECT r FROM Scolarite r WHERE 1=1 ";
        requettePrevious += " and r.anneeScolaireFirst='" + DateUtil.getPreviousYearString() + "'";
        requettePrevious += " and r.niveauScolaire='" + niveauScolaire + "'";
        List<Scolarite> listPrevious = executeQuery(requettePrevious);
        String requetteCurrent = "SELECT r FROM Scolarite r WHERE 1=1 ";
        requetteCurrent += " and r.anneeScolaireFirst='" + DateUtil.getCurrentYearString() + "'";
        requetteCurrent += " and r.niveauScolaire='" + niveauScolaire + "'";
        List<Scolarite> listCurrent = executeQuery(requetteCurrent);
        int month = DateUtil.getMonth();
        if (month >= 0 && month <= 6) {
            return listPrevious;
        } else {
            return listCurrent;
        }
    }

    public List<Scolarite> findByQuery(String nomOrphelin, String etablissement, String niveauScolaire,
            String filiere, Double moyenne1, Double moyenne2, Double moyenneAnneeExact,
            Integer resultat, Boolean soutienScolaire, String situation, String anneeScolaireFirst) {
        String requete = "SELECT r FROM Scolarite r WHERE 1=1 ";
        if (situation != null && !situation.equals("")) {
            requete += " and r.orphelin.veuve.famille.situation LIKE CONCAT('%','" + situation + "','%')";
        }
        if (niveauScolaire != null && !niveauScolaire.equals("")) {
            requete += " and r.niveauScolaire='" + niveauScolaire + "'";
        }
        if (nomOrphelin != null && !nomOrphelin.equals("")) {
            requete += " and r.orphelin.veuve.famille.nomFamille='" + nomOrphelin + "'";
        }
        if (etablissement != null && !etablissement.equals("")) {
            requete += " and r.etablissement LIKE CONCAT('%','" + etablissement + "','%')";
        }
        if (filiere != null && !filiere.equals("")) {
            requete += " and r.filiere LIKE CONCAT('%','" + filiere + "','%')";
        }
        if (anneeScolaireFirst != null && !anneeScolaireFirst.equals("")) {
            requete += " and r.anneeScolaireFirst='" + anneeScolaireFirst + "'";
        }
        if (moyenne1 != null && moyenne1 != 0) {
            requete += " and r.moyenne1 =" + " " + moyenne1;
        }
        if (moyenne2 != null && moyenne2 != 0) {
            requete += " and r.moyenne2='" + moyenne2 + "'";
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
        return super.executeQuery(requete);
    }

    public List<String> findByQueryString(String nomVariable) {
        switch (nomVariable) {
            case "nomOrphelin": {
                String requete = "SELECT DISTINCT  r.nomFamille FROM Famille r";
                return executerLaRequetteString(requete);
            }
            case "anneeScolaire": {
                String requete = "SELECT DISTINCT  r.anneeScolaireFirst FROM Scolarite r";
                return executerLaRequetteString(requete);
            }
            case "niveauScolaire": {
                return ScolariteUtil.niveauxScolarite();
            }
            case "situation": {
                return ScolariteUtil.situations();
            }
            default:
                return new ArrayList<>();
        }
    }

    public String yearPlusOneString(String anneeString) {
        Integer foo = Integer.parseInt(anneeString);
        foo++;
        String fooString = foo.toString();
        return fooString;
    }

    public Scolarite findByOrphelinAndScolarYearSingle(Orphelin orphelin, String firstYear) {
        try {
            if (orphelin != null && orphelin.getPrenom() != null) {
                String requette = "SELECT s FROM Scolarite s WHERE 1=1 ";
                requette += " and s.orphelin.prenom='" + orphelin.getPrenom() + "'";
                requette += " and s.orphelin.veuve.famille.nomFamille='" + orphelin.getVeuve().getFamille().getNomFamille() + "'";
                requette += " and s.anneeScolaireFirst='" + firstYear + "'";
                System.out.println("Ha requette: " + requette);
                return (Scolarite) em.createQuery(requette).getSingleResult();
            }
            return null;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Scolarite> findByOrphelinAndScolarYear(Orphelin orphelin, String firstYear) {
        if (orphelin != null && orphelin.getPrenom() != null) {
            String requette = "SELECT s FROM Scolarite s WHERE 1=1 ";
            requette += " and s.orphelin.prenom='" + orphelin.getPrenom() + "'";
            requette += " and s.orphelin.veuve.famille.nomFamille='" + orphelin.getVeuve().getFamille().getNomFamille() + "'";
            requette += " and s.anneeScolaireFirst='" + firstYear + "'";
            return executeQuery(requette);
        }
        return new ArrayList();
    }

    private List<Scolarite> findScolariteThisYearPrevious() {
        String requete = "SELECT r FROM Scolarite r WHERE 1=1 ";
        requete += " and r.anneeScolaireFirst='" + DateUtil.getPreviousYearString() + "'";
        return executeQuery(requete);
    }

    private List<Scolarite> findScolariteThisYearCurrent() {
        String requete = "SELECT r FROM Scolarite r WHERE 1=1 ";
        requete += " and r.anneeScolaireFirst='" + DateUtil.getCurrentYearString() + "'";
        return executeQuery(requete);
    }

    public void reglerLesNotes() {
        List<Scolarite> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            Scolarite get = all.get(i);
            if (get.getMoyenne1() != null) {
                get.setMoyenne1(roundToTwo(get.getMoyenne1()));
            }
            if (get.getMoyenne2() != null) {
                get.setMoyenne2(roundToTwo(get.getMoyenne2()));
            }
            if (get.getMoyenne1() != null) {
                get.setMoyenneAnnee(roundToTwo(get.getMoyenne1()));
            }
            super.edit(get);
        }
    }

    public Double roundToTwo(Double d) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(2, BigDecimal.ROUND_DOWN);
        d = bd.doubleValue();
        return d;
    }

    public void checkAndCalculMoyenneForSilkScolaires(Scolarite scolarite) {
        if (scolarite.getMoyenne1() == null || scolarite.getMoyenne2() == null) {
            scolarite.setMoyenneAnnee(null);
            scolarite.setResultat(0);
        } else {
            Double moy = (scolarite.getMoyenne1() + scolarite.getMoyenne2()) / 2;
            scolarite.setMoyenneAnnee(moy);
        }
        if (scolarite.getMoyenne1() != null) {
            scolarite.setMoyenne1(roundToTwo(scolarite.getMoyenne1()));
        }
        if (scolarite.getMoyenne2() != null) {
            scolarite.setMoyenne2(roundToTwo(scolarite.getMoyenne2()));
        }
        if (scolarite.getMoyenneAnnee() != null) {
            scolarite.setMoyenneAnnee(roundToTwo(scolarite.getMoyenneAnnee()));
        }
        if (scolarite.getMoyenneAnnee() != null) {
            if (scolarite.getSilkScolaire() != null) {
                if (scolarite.getSilkScolaire().equals(ScolariteUtil.getPimaire())) {
                    if (scolarite.getMoyenneAnnee() >= 5) {
                        scolarite.setResultat(1);
                    } else {
                        scolarite.setResultat(0);
                    }
                } else {
                    if (scolarite.getMoyenneAnnee() >= 10) {
                        scolarite.setResultat(1);
                    } else {
                        scolarite.setResultat(0);
                    }
                }
            }
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ScolariteFacade() {
        super(Scolarite.class);
    }

}
