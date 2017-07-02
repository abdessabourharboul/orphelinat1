/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Famille;
import bean.Orphelin;
import bean.Veuve;
import controler.util.DateUtil;
import controller.util.JsfUtil;
import java.util.ArrayList;
import java.util.Date;
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

    public List<String> executerLaRequette(String nomRequette) {
        System.out.println("haaa requette===>" + nomRequette);
        return em.createQuery(nomRequette).getResultList();
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

    public List<Veuve> findByQuery(String nomVeuve, String metierVeuve, String cin,
            Long ageMin, Long ageMax, Date dateNaissanceMin, Date dateNaissanceMax) {
        String requete = "SELECT r FROM Veuve r WHERE 1=1 ";
        if (nomVeuve != null && !nomVeuve.equals("")) {
            requete += " and r.nomVeuve='" + nomVeuve + "'";
        }
        if (metierVeuve != null && !metierVeuve.equals("")) {
            requete += " and r.metierVeuve='" + metierVeuve + "'";
        }
        if (cin != null && !cin.equals("")) {
            requete += " and r.cin='" + cin + "'";
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
        return em.createQuery(requete).getResultList();
    }

    @Override
    public void create(Veuve veuve) {
        veuve.setAge(new Long(DateUtil.calculAge(veuve.getDateNaissance())));
        veuve.getFamille().setNombrePersonnes(familleFacade.nombrePersonne(veuve.getFamille()) + 1);
//        veuve.getFamille().setNombrePersonnes(veuve.getFamille().getNombrePersonnes() + 1);
        List<Veuve> listVerification = listeVerificationCreation(veuve.getNomVeuve());
        System.out.println("ha lista de verification :" + listVerification);
        if (listVerification != null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("VeuveExisteDeja"));
        } else {
            familleFacade.edit(veuve.getFamille());
            super.create(veuve);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("VeuveCreated"));
        }
    }

    @Override
    public void edit(Veuve veuve) {
        veuve.setAge(new Long(DateUtil.calculAge(veuve.getDateNaissance())));
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
