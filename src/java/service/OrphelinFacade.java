/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Orphelin;
import bean.Veuve;
import static bean.Veuve_.famille;
import controler.util.DateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author asus
 */
@Stateless
public class OrphelinFacade extends AbstractFacade<Orphelin> {

    @PersistenceContext(unitName = "Orphelinat1PU")
    private EntityManager em;
    @EJB
    private FamilleFacade familleFacade;

    public List<Orphelin> findOrphelinByVeuve(Veuve veuve) {
        if (veuve == null || veuve.getId() == null) {
            return new ArrayList<>();
        } else {
            String req = "SELECT o FROM Orphelin o WHERE o.veuve.id='" + veuve.getId() + "'";
            return em.createQuery(req).getResultList();
        }
    }

    @Override
    public void create(Orphelin orphelin) {
        orphelin.setAnneeNaissance(new Long(DateUtil.getYear(orphelin.getDateNaissance())));
        orphelin.setAge(new Long(DateUtil.calculAge(orphelin.getDateNaissance())));
        orphelin.getVeuve().getFamille().setNombrePersonnes(orphelin.getVeuve().getFamille().getNombrePersonnes() + 1);
        familleFacade.edit(orphelin.getVeuve().getFamille());
        super.create(orphelin);
    }

    @Override
    public void edit(Orphelin orphelin) {
        orphelin.setAnneeNaissance(new Long(DateUtil.getYear(orphelin.getDateNaissance())));
        orphelin.setAge(new Long(DateUtil.calculAge(orphelin.getDateNaissance())));
        super.edit(orphelin);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrphelinFacade() {
        super(Orphelin.class);
    }

}
