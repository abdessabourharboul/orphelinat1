/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Maladie;
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
public class MaladieFacade extends AbstractFacade<Maladie> {

    @PersistenceContext(unitName = "Orphelinat1PU")
    private EntityManager em;

   

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
            case "nomMaladie": {
                String requete = "SELECT DISTINCT  r.nomMaladie FROM Maladie r";
                return executerLaRequette(requete);
            }
            case "description": {
                String requete = "SELECT DISTINCT  r.description FROM Maladie r";
                return executerLaRequette(requete);
            }
            default:
                return new ArrayList<>();
        }
    }

    public List<Maladie> findByQuery(String nomOrphelin, String nomMaladie, String description) {
        String requete = "SELECT r FROM Maladie r WHERE 1=1 ";
        if (nomOrphelin != null && !nomOrphelin.equals("")) {
            requete += " and r.orphelin.veuve.famille.nomFamille='" + nomOrphelin + "'";
        }
        if (nomMaladie != null && !nomMaladie.equals("")) {
            requete += " and r.nomMaladie='" + nomMaladie + "'";
        }
        if (description != null && !description.equals("")) {
            requete += " and r.description='" + description + "'";
        }
        System.out.println("haaa requette===>" + requete);
        return em.createQuery(requete).getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MaladieFacade() {
        super(Maladie.class);
    }

}
