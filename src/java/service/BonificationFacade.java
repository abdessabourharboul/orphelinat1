/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Bonification;
import controler.util.DateUtil;
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
