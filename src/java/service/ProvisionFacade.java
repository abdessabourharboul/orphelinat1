/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Famille;
import bean.Provision;
import controller.util.DateUtil;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author asus
 */
@Stateless
public class ProvisionFacade extends AbstractFacade<Provision> {

    @PersistenceContext(unitName = "Orphelinat1PU")
    private EntityManager em;

    public List<Famille> findFamillesByNombrePersonnes(Long nombrePersonnes) {
        String requete = "SELECT r FROM Famille r WHERE 1=1 ";
        requete += " and r.nombrePersonnes ='" + nombrePersonnes + "'";
        System.out.println("haaa requette===>" + requete);
        return em.createQuery(requete).getResultList();
    }

    public List<Famille> findFamillesByNombrePersonnesSuperior(Long nombrePersonnes) {
        String requete = "SELECT r FROM Famille r WHERE 1=1 ";
        requete += " and r.nombrePersonnes >='" + nombrePersonnes + "'";
        System.out.println("haaa requette===>" + requete);
        return em.createQuery(requete).getResultList();
    }

    public List<Famille> deuxPersonnes() {
        return findFamillesByNombrePersonnes(2L);
    }

    public List<Famille> troisPersonnes() {
        return findFamillesByNombrePersonnes(3L);
    }

    public List<Famille> quatresPersonnes() {
        return findFamillesByNombrePersonnes(4L);
    }

    public List<Famille> plusCinqPersonnes() {
        return findFamillesByNombrePersonnesSuperior(5L);
    }

    public int nombreFamilles(Long nombrePersonnes) {
        return findFamillesByNombrePersonnes(nombrePersonnes).size();
    }

    public List<Provision> getItems() {
        String currentYear = DateUtil.getCurrentYearString();
        Provision provisionDeux = clone(2L, currentYear);
        Provision provisionTrois = clone(3L, currentYear);
        Provision provisionQuatre = clone(4L, currentYear);
        Provision provisionCinqOuPlus = clone(5L, currentYear);

        Provision provisionDeuxCheck = findProvisionByNombrePersonnesAndCurrentYear(provisionDeux.getNombrePersonne(), provisionDeux.getAnnee());
        Provision provisionTroisCheck = findProvisionByNombrePersonnesAndCurrentYear(provisionTrois.getNombrePersonne(), provisionTrois.getAnnee());
        Provision provisionQuatreCheck = findProvisionByNombrePersonnesAndCurrentYear(provisionQuatre.getNombrePersonne(), provisionQuatre.getAnnee());
        Provision provisionCinqOuPlusCheck = findProvisionByNombrePersonnesAndCurrentYear(provisionCinqOuPlus.getNombrePersonne(), provisionCinqOuPlus.getAnnee());

        if (provisionDeuxCheck == null) {
            super.create(provisionDeux);
        }
        if (provisionTroisCheck == null) {
            super.create(provisionTrois);
        }
        if (provisionQuatreCheck == null) {
            super.create(provisionQuatre);
        }
        if (provisionCinqOuPlusCheck == null) {
            super.create(provisionCinqOuPlus);
        }

        List<Provision> items = findProvisionsByCurrentYear(DateUtil.getCurrentYearString());
        return items;
    }

    public Provision clone(Long np, String cy) {
        Provision provision = new Provision();
        provision.setNombrePersonne(np);
        provision.setAnnee(cy);
        return provision;
    }

    public Provision findProvisionByNombrePersonnesAndCurrentYear(Long np, String cy) {
        try {
            if (np != null && cy != null) {
                String requette = "SELECT s FROM Provision s WHERE 1=1 ";
                requette += " and s.nombrePersonne='" + np + "'";
                requette += " and s.annee='" + cy + "'";
                System.out.println("Ha requette: " + requette);
                return (Provision) em.createQuery(requette).getSingleResult();
            }
            return null;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Provision> findProvisionsByCurrentYear(String cy) {
        String requette = "SELECT s FROM Provision s WHERE 1=1 ";
        requette += " and s.annee='" + cy + "'";
        System.out.println("Ha requette: " + requette);
        return em.createQuery(requette).getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProvisionFacade() {
        super(Provision.class);
    }

}
