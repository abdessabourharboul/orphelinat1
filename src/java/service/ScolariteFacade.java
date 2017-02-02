/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Scolarite;
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
            if (moy >= 10) {
                scolarite.setResultat(true);
            } else {
                scolarite.setResultat(false);
            }
        }
        return scolarite;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ScolariteFacade() {
        super(Scolarite.class);
    }

}
