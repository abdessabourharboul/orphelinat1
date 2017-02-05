/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Famille;
import bean.Medicament;
import bean.Orphelin;
import controler.util.DateUtil;
import java.util.Date;
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
public class MedicamentFacade extends AbstractFacade<Medicament> {

    @PersistenceContext(unitName = "Orphelinat1PU")
    private EntityManager em;
    @EJB
    private FamilleFacade familleFacade;

    public List<Medicament> findByQuery(String nomOrphelin, String nomMedicament, String description,
            Float prixMin, Float prixMax, Date datePriseMin, Date datePriseMax) {
        String requete = "SELECT r FROM Medicament r WHERE 1=1 ";
        if (nomOrphelin != null && !nomOrphelin.equals("")) {
            requete += " and r.orphelin.veuve.famille.nomFamille='" + nomOrphelin + "'";
        }
        if (nomMedicament != null && !nomMedicament.equals("")) {
            requete += " and r.nomMedicament='" + nomMedicament + "'";
        }
        if (description != null && !description.equals("")) {
            requete += " and r.description='" + description + "'";
        }
        if (prixMin != null && prixMin != 0) {
            requete += " and r.prix >='" + prixMin + "'";
        }
        if (prixMax != null && prixMax != 0) {
            requete += " and r.prix <='" + prixMax + "'";
        }
        if (datePriseMin != null) {
            requete += " and r.datePrise >='" + DateUtil.getSqlDateTime(datePriseMin) + "'";
        }
        if (datePriseMax != null) {
            requete += " and r.datePrise <='" + DateUtil.getSqlDateTime(datePriseMax) + "'";
        }
        System.out.println("haaa requette===>" + requete);
        return em.createQuery(requete).getResultList();
    }

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
