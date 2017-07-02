/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Famille;
import bean.User;
import bean.Veuve;
import controller.util.JsfUtil;
import controller.util.SessionUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author asus
 */
@Stateless
public class FamilleFacade extends AbstractFacade<Famille> {

    @PersistenceContext(unitName = "Orphelinat1PU")
    private EntityManager em;

    public Long nombrePersonne(Famille famille) {
        int nombre = 0;
        for (int i = 0; i < famille.getVeuves().size(); i++) {
            Veuve veuve = famille.getVeuves().get(i);
            nombre = nombre + 1;
            for (int j = 0; j < veuve.getOrphelins().size(); j++) {
                Object get = veuve.getOrphelins().get(j);
                nombre = nombre + 1;
            }
        }
        Long res = new Long(nombre);
        return res;
    }

    public List<Famille> findBySituation(String situation) {
        return findByQuery(null, null, null, null, situation, null, null, null, null, null, null);
    }

    public List<String> executerLaRequette(String nomRequette) {
        System.out.println("haaa requette===>" + nomRequette);
        return em.createQuery(nomRequette).getResultList();
    }

    public List<String> findByQueryString(String nomVariable) {
        switch (nomVariable) {
            case "nomFamille": {
                String requete = "SELECT DISTINCT  r.nomFamille FROM Famille r";
                return executerLaRequette(requete);
            }
            case "typeLogement": {
                String requete = "SELECT DISTINCT  r.typeLogement FROM Famille r";
                return executerLaRequette(requete);
            }
            case "adresse": {
                String requete = "SELECT DISTINCT  r.adresse FROM Famille r";
                return executerLaRequette(requete);
            }
            case "zoneGeographique": {
                String requete = "SELECT DISTINCT  r.zoneGeographique FROM Famille r";
                return executerLaRequette(requete);
            }
            case "situation": {
                String requete = "SELECT DISTINCT  r.situation FROM Famille r";
                return executerLaRequette(requete);
            }
            case "responsableZone": {
                String requete = "SELECT DISTINCT  u.nom FROM User u";
                return executerLaRequette(requete);
            }
            case "telephone": {
                String requete = "SELECT DISTINCT  r.telephone FROM Famille r";
                return executerLaRequette(requete);
            }
            default:
                return new ArrayList<>();
        }
    }

    public List<Famille> findByQuery(String nomFamille, String typeLogement, String adresse,
            String zoneGeographique, String situation, String responsableZone,
            Long nombrePersonnesMin, Long nombrePersonnesMax,
            String telephone, Float coutMin, Float coutMax) {
        String requete = "SELECT r FROM Famille r WHERE 1=1 ";
        if (nomFamille != null && !nomFamille.equals("")) {
            requete += " and r.nomFamille='" + nomFamille + "'";
        }
        if (typeLogement != null && !typeLogement.equals("")) {
            requete += " and r.typeLogement='" + typeLogement + "'";
        }
        if (adresse != null && !adresse.equals("")) {
//            requete += " and r.adresse='" + adresse + "'";

            requete += " and r.adresse LIKE CONCAT('%','" + adresse + "','%')";
        }
        if (zoneGeographique != null && !zoneGeographique.equals("")) {
            requete += " and r.zoneGeographique='" + zoneGeographique + "'";
        }
        if (situation != null && !situation.equals("")) {
            requete += " and r.situation='" + situation + "'";
        }
        if (responsableZone != null && !responsableZone.equals("")) {
            requete += " and r.user.nom='" + responsableZone + "'";
        }
        if (nombrePersonnesMin != null && nombrePersonnesMin != 0) {
            requete += " and r.nombrePersonnes >='" + nombrePersonnesMin + "'";
        }
        if (nombrePersonnesMax != null && nombrePersonnesMax != 0) {
            requete += " and r.nombrePersonnes <='" + nombrePersonnesMax + "'";
        }
        if (telephone != null && !telephone.equals("")) {
            requete += " and r.telephone='" + telephone + "'";
        }
        if (coutMin != null && coutMin != 0) {
            requete += " and r.cout >='" + coutMin + "'";
        }
        if (coutMax != null && coutMax != 0) {
            requete += " and r.cout <='" + coutMax + "'";
        }
        System.out.println("haaa requette===>" + requete);
        return em.createQuery(requete).getResultList();
    }

    public User getConnectedUser() {
        return SessionUtil.getConnectedUser();
    }

    private List<Famille> listeVerificationCreation(String nomFamille, String nomResponsable) {
        String requete = "SELECT r FROM Famille r WHERE r.nomFamille='" + nomFamille + "' "
                + "AND r.user.nom='" + nomResponsable + "'";
        return em.createQuery(requete).getResultList();
    }

    @Override
    public void create(Famille famille) {
        famille.setUser(getConnectedUser());
        famille.setCout(0F);
        famille.setNombrePersonnes(0L);
        List<Famille> listVerification = listeVerificationCreation(famille.getNomFamille(), famille.getUser().getNom());
        System.out.println("ha lista de verification :" + listVerification);
        if (listVerification != null) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("FamilleExisteDeja"));
        } else {
            super.create(famille);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FamilleCreated"));
        }
    }

    public int calculNombrePersonnes(Famille famille) {
        int number = 0;
        number = number + famille.getVeuves().size();
        System.out.println("ha la liste : " + famille.getVeuves());
        System.out.println("hahwa l 3adad : " + famille.getVeuves().size());
        System.out.println("hahwa l 3adad apres : " + number);
        for (int j = 0; j < famille.getVeuves().size(); j++) {
            Veuve get = famille.getVeuves().get(j);
            number = number + get.getOrphelins().size();
        }
        return number;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FamilleFacade() {
        super(Famille.class);
    }

}
