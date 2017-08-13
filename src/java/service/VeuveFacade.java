/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Famille;
import bean.Orphelin;
import bean.Veuve;
import controller.util.DateUtil;
import controller.util.JsfUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author asus
 */
@Stateless
public class VeuveFacade extends AbstractFacade<Veuve> {

    @PersistenceContext(unitName = "Orphelinat1PU")
    private EntityManager em;
    @EJB
    private FamilleFacade familleFacade;
    @EJB
    private OrphelinFacade orphelinFacade;

    public List<String> findNomVeuveForSearchBySituation(String situation) {
        if (situation == null) {
            situation = "";
        }
        String requete = "SELECT DISTINCT  r.nomVeuve FROM Veuve r WHERE 1=1";
        requete += " and r.famille.situation LIKE CONCAT('%','" + situation + "','%')";
        return executerLaRequette(requete);
    }

    public List<String> findNomForSearchBySituation(String situation) {
        if (situation == null) {
            situation = "";
        }
        String requete = "SELECT DISTINCT  r.nomFamille FROM Famille r WHERE 1=1";
        requete += " and r.situation LIKE CONCAT('%','" + situation + "','%')";
        return executerLaRequette(requete);
    }

    public List<String> executerLaRequette(String nomRequette) {
        System.out.println("haaa requette===>" + nomRequette);
        return em.createQuery(nomRequette).getResultList();
    }

    public List<Veuve> findVeuveBySituation(String situation) {
        String requete = "SELECT r FROM Veuve r WHERE 1=1 and r.famille.situation LIKE CONCAT('%','" + situation + "','%')";
        System.out.println("haaa requette===>" + requete);
        return em.createQuery(requete).getResultList();
    }

    public List<String> findByQueryString(String nomVariable) {
        switch (nomVariable) {
            case "nomVeuve": {
                String requete = "SELECT DISTINCT  r.nomVeuve FROM Veuve r";
                return executerLaRequette(requete);
            }
            case "metierVeuve": {
                String requete = "SELECT DISTINCT  r.metierVeuve FROM Veuve r";
                return executerLaRequette(requete);
            }
            case "cin": {
                String requete = "SELECT DISTINCT  r.cin FROM Veuve r";
                return executerLaRequette(requete);
            }
            default:
                return new ArrayList<>();
        }
    }

    public List<Veuve> findByQuery(String nomVeuve, String cin, String nomFamille,
            String zoneGeographique, String adresse, String situation) {
        String requete = "SELECT r FROM Veuve r WHERE 1=1 ";
        requete += " and r.famille.situation LIKE CONCAT('%','" + situation + "','%')";
        if (nomVeuve != null && !nomVeuve.equals("")) {
            requete += " and r.nomVeuve='" + nomVeuve + "'";
        }
        if (cin != null && !cin.equals("")) {
            requete += " and r.cin='" + cin + "'";
        }
        if (nomFamille != null && !nomFamille.equals("")) {
            requete += " and r.famille.nomFamille LIKE CONCAT('%','" + nomFamille + "','%')";
        }
        if (zoneGeographique != null && !zoneGeographique.equals("")) {
            requete += " and r.famille.user.zoneGeographique LIKE CONCAT('%','" + zoneGeographique + "','%')";
        }
        if (adresse != null && !adresse.equals("")) {
            requete += " and r.famille.adresse LIKE CONCAT('%','" + adresse + "','%')";
        }

        System.out.println("haaa requette===>" + requete);
        return em.createQuery(requete).getResultList();
    }

    public List<Veuve> findVeuveByFamille(Famille famille) {
        if (famille == null || famille.getId() == null) {
            return new ArrayList<>();
        } else {
            String req = "SELECT ve FROM Veuve ve WHERE ve.famille.id='" + famille.getId() + "'";
            return em.createQuery(req).getResultList();
        }
    }

    public List<Orphelin> findOrphelinsByFamille(Famille famille) {
        List<Orphelin> resultats = new ArrayList();
        List<Veuve> veuves = findVeuveByFamille(famille);
        for (int i = 0; i < veuves.size(); i++) {
            Veuve get = veuves.get(i);
            for (int j = 0; j < get.getOrphelins().size(); j++) {
                Orphelin get1 = get.getOrphelins().get(j);
                resultats.add(get1);
            }
        }
        return resultats;
    }

    private List<Veuve> listeVerificationCreation(String nomVeuve) {
        String requete = "SELECT r FROM Veuve r WHERE r.nomVeuve='" + nomVeuve + "'";
        System.out.println("ha le nom de la veuve a cree" + nomVeuve);
        return em.createQuery(requete).getResultList();
    }

    @Override
    public void create(Veuve veuve) {
        if (veuve.getDateNaissance() != null) {
            veuve.setAge(new Long(DateUtil.calculAge(veuve.getDateNaissance())));
        }
        veuve.getFamille().setNombrePersonnes(familleFacade.nombrePersonne(veuve.getFamille()) + 1);
//        veuve.getFamille().setNombrePersonnes(veuve.getFamille().getNombrePersonnes() + 1);
        List<Veuve> listVerification = listeVerificationCreation(veuve.getNomVeuve());
        System.out.println("ha lista de verification :" + listVerification);
        if (!listVerification.isEmpty()) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("VeuveExisteDeja"));
        } else {
            familleFacade.edit(veuve.getFamille());
            super.create(veuve);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("VeuveCreated"));
        }
    }

    @Override
    public void edit(Veuve veuve) {
        if (veuve.getDateNaissance() != null) {
            veuve.setAge(new Long(DateUtil.calculAge(veuve.getDateNaissance())));
        }
        super.edit(veuve);
    }

//    @Override
//    public void remove(Veuve veuve) {
//        for (int i = 0; i < veuve.getOrphelins().size(); i++) {
//            Orphelin get = veuve.getOrphelins().get(i);
//            orphelinFacade.remove(get);
//        }
//        super.remove(veuve);
//    }
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VeuveFacade() {
        super(Veuve.class);
    }

}
