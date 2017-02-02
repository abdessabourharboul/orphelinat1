/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Famille;
import bean.Veuve;
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
public class VeuveFacade extends AbstractFacade<Veuve> {

    @PersistenceContext(unitName = "Orphelinat1PU")
    private EntityManager em;
    @EJB
    private FamilleFacade familleFacade;

    public List<Veuve> findVeuveByFamille(Famille famille) {
        if (famille == null || famille.getId() == null) {
            return new ArrayList<>();
        } else {
            String req = "SELECT ve FROM Veuve ve WHERE ve.famille.id='" + famille.getId() + "'";
            return em.createQuery(req).getResultList();
        }
    }

    @Override
    public void create(Veuve veuve) {
        veuve.setAge(new Long(DateUtil.calculAge(veuve.getDateNaissance())));
        veuve.getFamille().setNombrePersonnes(veuve.getFamille().getNombrePersonnes() + 1);
        familleFacade.edit(veuve.getFamille());
        super.create(veuve);
    }

    @Override
    public void edit(Veuve veuve) {
        veuve.setAge(new Long(DateUtil.calculAge(veuve.getDateNaissance())));
        super.edit(veuve);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VeuveFacade() {
        super(Veuve.class);
    }

}
