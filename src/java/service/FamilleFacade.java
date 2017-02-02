/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Famille;
import bean.Veuve;
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
