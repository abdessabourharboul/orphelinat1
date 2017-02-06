/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Famille;
import bean.Veuve;
import java.util.ArrayList;
import java.util.List;
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
                String requete = "SELECT DISTINCT  r.responsableZone FROM Famille r";
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
            requete += " and r.adresse='" + adresse + "'";
        }
        if (zoneGeographique != null && !zoneGeographique.equals("")) {
            requete += " and r.zoneGeographique='" + zoneGeographique + "'";
        }
        if (situation != null && !situation.equals("")) {
            requete += " and r.situation='" + situation + "'";
        }
        if (responsableZone != null && !responsableZone.equals("")) {
            requete += " and r.responsableZone='" + responsableZone + "'";
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

    @Override
    public void create(Famille famille) {
        famille.setCout(0F);
        famille.setNombrePersonnes(0L);
        super.create(famille);
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
