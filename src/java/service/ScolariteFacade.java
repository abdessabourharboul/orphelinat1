package service;

import bean.Orphelin;
import bean.Scolarite;
import controller.util.DateUtil;
import controller.util.JsfUtil;
import controller.util.ScolariteUtil;
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

    @Override
    public void create(Scolarite scolarite) {
        super.create(toDoBeforeCreate(scolarite));
    }

    @Override
    public void edit(Scolarite scolarite) {
        super.edit(toDoBeforeEdit(scolarite));
    }

    public Scolarite toDoBeforeCreate(Scolarite scolarite) {
        checkAndCalculMoyenneForSilkScolaires(scolarite);
        checkBeforeCreate(scolarite);
        return scolarite;
    }

    public Scolarite toDoBeforeEdit(Scolarite scolarite) {
        checkAndCalculMoyenneForSilkScolaires(scolarite);
        checkBeforeEdit(scolarite);
        return scolarite;
    }

    public void checkAndCalculMoyenneForSilkScolaires(Scolarite scolarite) {
        if (scolarite.getMoyenne1() == null || scolarite.getMoyenne2() == null) {
            scolarite.setMoyenneAnnee(null);
            scolarite.setResultat(null);
        } else {
            float moy = (scolarite.getMoyenne1() + scolarite.getMoyenne2()) / 2;
            scolarite.setMoyenneAnnee(moy);
        }
        if (scolarite.getMoyenneAnnee() != null) {
            if (scolarite.getSilkScolaire() != null) {
                if (scolarite.getSilkScolaire().equals(ScolariteUtil.getPimaire())) {
                    if (scolarite.getMoyenneAnnee() >= 5) {
                        scolarite.setResultat(true);
                    }
                } else {
                    if (scolarite.getMoyenneAnnee() >= 10) {
                        scolarite.setResultat(true);
                    }
                }
            }
        }
    }

    private void checkBeforeCreate(Scolarite scolarite) {
        List<Scolarite> list = findByOrphelinAndScolarYear(scolarite.getOrphelin(), scolarite.getAnneeScolaireFirst());
        if (list.isEmpty()) {
            super.create(scolarite);
        } else {
            if (scolarite.getResultat() == true) {
                passButtonActionListener(scolarite);
            }
            JsfUtil.addSuccessMessage("تذكير: الدراسة موجودة");
        }
    }

    private void checkBeforeEdit(Scolarite scolarite) {
        if (scolarite.getResultat() != null) {
            if (scolarite.getResultat() == true) {
                passButtonActionListener(scolarite);
            }
        }
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

    public List<Scolarite> findByQuery(String nomOrphelin, String etablissement,String niveauScolaire, 
            String filiere, Float moyenne1, Float moyenne2,Float moyenneAnneeExact,
            Boolean resultat, Boolean soutienScolaire,String situation, String anneeScolaireFirst) {
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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ScolariteFacade() {
        super(Scolarite.class);
    }

}
