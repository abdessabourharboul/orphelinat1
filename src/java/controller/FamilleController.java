package controller;

import bean.Bonification;
import bean.Famille;
import bean.Maladie;
import bean.Medicament;
import bean.Orphelin;
import bean.Scolarite;
import bean.User;
import bean.Veuve;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import controller.util.SessionUtil;
import service.FamilleFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import service.OrphelinFacade;
import service.VeuveFacade;

@Named("familleController")
@SessionScoped
public class FamilleController implements Serializable {

    @EJB
    private service.FamilleFacade ejbFacade;
    @EJB
    private VeuveFacade veuveFacade;
    @EJB
    private OrphelinFacade orphelinFacade;
    private List<Famille> items = null;
    private Famille selected;
    private List<Veuve> veuves;
    private Veuve veuve;
    private List<Orphelin> orphelins;
    private Orphelin orphelin;
    private List<Famille> familles;
    private String nomFamilleForSearch;
    private String typeLogementForSearch;
    private String adresseForSearch;
    private String zoneGeographiqueForSearch;
    private String situationForSearch;
    private String responsableZoneForSearch;
    private Long nombrePersonnesMinForSearch;
    private Long nombrePersonnesMaxForSearch;
    private String telephoneForSearch;
    private Float coutMinForSearch;
    private Float coutMaxForSearch;
    private String passwordForDelete;

    public List<String> getItemsAvailableSelectOneString(String nomVariable) {
        return getFacade().findByQueryString(nomVariable);
    }

    public void rechercheFamilleByQuery() {
        items = ejbFacade.findByQuery(nomFamilleForSearch, typeLogementForSearch, adresseForSearch,
                zoneGeographiqueForSearch, situationForSearch, responsableZoneForSearch,
                nombrePersonnesMinForSearch, nombrePersonnesMaxForSearch,
                telephoneForSearch, coutMinForSearch, coutMaxForSearch);
        System.out.println(items);
    }

    public String getPasswordForDelete() {
        return passwordForDelete;
    }

    public void setPasswordForDelete(String passwordForDelete) {
        this.passwordForDelete = passwordForDelete;
    }

    public List<Famille> getFamilles() {
        if (familles == null) {
            familles = new ArrayList<>();
        }
        return familles;
    }

    public void setFamilles(List<Famille> familles) {
        this.familles = familles;
    }

    public String getNomFamilleForSearch() {
        return nomFamilleForSearch;
    }

    public void setNomFamilleForSearch(String nomFamilleForSearch) {
        this.nomFamilleForSearch = nomFamilleForSearch;
    }

    public String getTypeLogementForSearch() {
        return typeLogementForSearch;
    }

    public void setTypeLogementForSearch(String typeLogementForSearch) {
        this.typeLogementForSearch = typeLogementForSearch;
    }

    public String getAdresseForSearch() {
        return adresseForSearch;
    }

    public void setAdresseForSearch(String adresseForSearch) {
        this.adresseForSearch = adresseForSearch;
    }

    public String getZoneGeographiqueForSearch() {
        return zoneGeographiqueForSearch;
    }

    public void setZoneGeographiqueForSearch(String zoneGeographiqueForSearch) {
        this.zoneGeographiqueForSearch = zoneGeographiqueForSearch;
    }

    public String getSituationForSearch() {
        return situationForSearch;
    }

    public void setSituationForSearch(String situationForSearch) {
        this.situationForSearch = situationForSearch;
    }

    public String getResponsableZoneForSearch() {
        return responsableZoneForSearch;
    }

    public Long getNombrePersonnesMinForSearch() {
        return nombrePersonnesMinForSearch;
    }

    public void setNombrePersonnesMinForSearch(Long nombrePersonnesMinForSearch) {
        this.nombrePersonnesMinForSearch = nombrePersonnesMinForSearch;
    }

    public Long getNombrePersonnesMaxForSearch() {
        return nombrePersonnesMaxForSearch;
    }

    public void setNombrePersonnesMaxForSearch(Long nombrePersonnesMaxForSearch) {
        this.nombrePersonnesMaxForSearch = nombrePersonnesMaxForSearch;
    }

    public String getTelephoneForSearch() {
        return telephoneForSearch;
    }

    public void setTelephoneForSearch(String telephoneForSearch) {
        this.telephoneForSearch = telephoneForSearch;
    }

    public Float getCoutMinForSearch() {
        return coutMinForSearch;
    }

    public void setCoutMinForSearch(Float coutMinForSearch) {
        this.coutMinForSearch = coutMinForSearch;
    }

    public Float getCoutMaxForSearch() {
        return coutMaxForSearch;
    }

    public void setCoutMaxForSearch(Float coutMaxForSearch) {
        this.coutMaxForSearch = coutMaxForSearch;
    }

    public void setResponsableZoneForSearch(String responsableZoneForSearch) {
        this.responsableZoneForSearch = responsableZoneForSearch;
    }

    public List<Bonification> findBonificationByOrphelin(Orphelin orphelin) {
        return orphelinFacade.findBonificationByOrphelin(orphelin);
    }

    public List<Maladie> findMaladieByOrphelin(Orphelin orphelin) {
        return orphelinFacade.findMaladieByOrphelin(orphelin);
    }

    public List<Medicament> findMedicamentByOrphelin(Orphelin orphelin) {
        return orphelinFacade.findMedicamentByOrphelin(orphelin);
    }

    public List<Scolarite> findScolariteByOrphelin(Orphelin orphelin) {
        return orphelinFacade.findScolariteByOrphelin(orphelin);
    }

    public void detailsFamille(Famille famille) {
        setSelected(famille);
    }

    public void detailsVeuve(Veuve veuve) {
        setVeuve(veuve);
    }

    public void detailsOrphelin(Orphelin orphelin) {
        setOrphelin(orphelin);
    }

    public void regenererLesListes() {
        veuve = null;
        orphelin = null;
    }

    public void findFamillesForZoneGeographique(String zoneGeo) {
        items = getFacade().findByQuery(null, null, null, zoneGeo, null, null, null, null, null, null, null);
    }

    public void nullerLaListe() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            items = null;
//            System.out.println("hanooo");
        }
    }

    public int calculNombrePersonnes(Famille famille) {
        return ejbFacade.calculNombrePersonnes(famille);
    }

    public FamilleController() {
    }

    public Famille getSelected() {
        return selected;
    }

    public void setSelected(Famille selected) {
        this.selected = selected;
    }

    public List<Veuve> getVeuves() {
        return veuveFacade.findVeuveByFamille(getSelected());
    }

    public Veuve getVeuve() {
        if (veuve == null) {
            veuve = new Veuve();
        }
        return veuve;
    }

    public void setVeuve(Veuve veuve) {
        this.veuve = veuve;
    }

    public void setVeuves(List<Veuve> veuves) {
        this.veuves = veuves;
    }

    public List<Orphelin> getOrphelins() {
        return veuveFacade.findOrphelinsByFamille(getSelected());
//        return orphelinFacade.findOrphelinByVeuve(getVeuve());
    }

    public void setOrphelins(List<Orphelin> orphelins) {
        this.orphelins = orphelins;
    }

    public Orphelin getOrphelin() {
        if (orphelin == null) {
            orphelin = new Orphelin();
        }
        return orphelin;
    }

    public void setOrphelin(Orphelin orphelin) {
        this.orphelin = orphelin;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FamilleFacade getFacade() {
        return ejbFacade;
    }

    public Famille prepareCreate() {
        selected = new Famille();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FamilleCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FamilleUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FamilleDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Famille> getItemsBySituations(String situation) {
        if (items == null) {
            items = getFacade().findBySituation(situation);
        }
        System.out.println("Hani f la Methode getItemsBySituations()");
        return items;
    }

    public List<Famille> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        System.out.println("Hani f la Methode getItems()");
        return items;
    }

    public User getConnectedUser() {
        return SessionUtil.getConnectedUser();
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    getFacade().create(selected);
//                    JsfUtil.addSuccessMessage(successMessage);
                } else if (persistAction == PersistAction.UPDATE) {
                    getFacade().edit(selected);
                    JsfUtil.addSuccessMessage(successMessage);
                } else {
                    getFacade().removeItem(selected, passwordForDelete, getConnectedUser());
//                  getFacade().remove(selected);
                }
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Famille getFamille(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Famille> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Famille> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Famille.class)
    public static class FamilleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FamilleController controller = (FamilleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "familleController");
            return controller.getFamille(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Famille) {
                Famille o = (Famille) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Famille.class.getName()});
                return null;
            }
        }

    }

}
