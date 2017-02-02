/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Famille;
import bean.Medicament;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author asus
 */
@Stateless
public class MedicamentFacade extends AbstractFacade<Medicament> {

    @PersistenceContext(unitName = "Orphelinat1PU")
    private EntityManager em;
    @EJB
    private FamilleFacade familleFacade;

    @Override
    public void create(Medicament medicament) {
        medicament.getOrphelin().getVeuve().getFamille().setCout(medicament.getOrphelin().getVeuve().getFamille().getCout() + medicament.getPrix());
        familleFacade.edit(medicament.getOrphelin().getVeuve().getFamille());
        super.create(medicament);
    }

    public Float calculerCoutFamille(Famille famille) {
        String req = "SELECT sum(m.prix) FROM Medicament m WHERE m.orphelin.veuve.famille.id='" + famille.getId() + "'";
        System.out.println(req);
        return ((Double) em.createQuery(req).getSingleResult()).floatValue();
    }

    @Override
    public void edit(Medicament medicament) {
        super.edit(medicament);
        medicament.getOrphelin().getVeuve().getFamille().setCout(calculerCoutFamille(medicament.getOrphelin().getVeuve().getFamille()));
        familleFacade.edit(medicament.getOrphelin().getVeuve().getFamille());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MedicamentFacade() {
        super(Medicament.class);
    }

}
