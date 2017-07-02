/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Bonification;
import bean.Maladie;
import bean.User;
import controler.util.DateUtil;
import controller.util.HashPasswords;
import controller.util.JsfUtil;
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
public class BonificationFacade extends AbstractFacade<Bonification> {

    @PersistenceContext(unitName = "Orphelinat1PU")
    private EntityManager em;
    
//    public void removeItem(Bonification bonification, String passwordConfirmation, User user) {
//        if (HashPasswords.crypt(passwordConfirmation).equals(user.getPasswordSuppresion())) {
//            super.remove(bonification);
//        } else {
//            JsfUtil.addErrorMessage("Mot de passe incorrect");
//        }
//    }

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
            case "nomBonification": {
                String requete = "SELECT DISTINCT  r.nomBonification FROM Bonification r";
                return executerLaRequette(requete);
            }
            case "description": {
                String requete = "SELECT DISTINCT  r.description FROM Bonification r";
                return executerLaRequette(requete);
            }
            default:
                return new ArrayList<>();
        }
    }

    public List<Bonification> findByQuery(String nomOrphelin, String nomBonification, String description,
            Date dateBonificationMin, Date dateBonificationMax) {
        String requete = "SELECT r FROM Bonification r WHERE 1=1 ";
        if (nomOrphelin != null && !nomOrphelin.equals("")) {
            requete += " and r.orphelin.veuve.famille.nomFamille='" + nomOrphelin + "'";
        }
        if (nomBonification != null && !nomBonification.equals("")) {
            requete += " and r.nomBonification='" + nomBonification + "'";
        }
        if (description != null && !description.equals("")) {
            requete += " and r.description='" + description + "'";
        }
        if (dateBonificationMin != null) {
            requete += " and r.dateBonification >='" + DateUtil.getSqlDateTime(dateBonificationMin) + "'";
        }
        if (dateBonificationMax != null) {
            requete += " and r.dateBonification <='" + DateUtil.getSqlDateTime(dateBonificationMax) + "'";
        }
        System.out.println("haaa requette===>" + requete);
        return em.createQuery(requete).getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BonificationFacade() {
        super(Bonification.class);
    }

}
