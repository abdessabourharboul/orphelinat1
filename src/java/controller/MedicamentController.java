package controller;

import bean.Medicament;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.MedicamentFacade;

import java.io.Serializable;
import java.util.Date;
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

@Named("medicamentController")
@SessionScoped
public class MedicamentController implements Serializable {

    @EJB
    private service.MedicamentFacade ejbFacade;
    private List<Medicament> items = null;
    private Medicament selected;
    private String nomOrphelinForSearch;
    private String nomMedicamentForSearch;
    private String descriptionForSearch;
    private Float prixMinForSearch;
    private Float prixMaxForSearch;
    private Date datePriseMinForSearch;
    private Date datePriseMaxForSearch;

    public List<String> getItemsAvailableSelectOneString(String nomVariable) {
        return getFacade().findByQueryString(nomVariable);
    }

    public void rechercheMedicamentByQuery() {
        items = ejbFacade.findByQuery(nomOrphelinForSearch, nomMedicamentForSearch, descriptionForSearch,
                prixMinForSearch, prixMaxForSearch, datePriseMinForSearch, datePriseMaxForSearch);
        System.out.println(items);
    }

    public void nullerLaListe() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            items = null;
//            System.out.println("hanooo");
        }
    }

    public String getNomOrphelinForSearch() {
        return nomOrphelinForSearch;
    }

    public void setNomOrphelinForSearch(String nomOrphelinForSearch) {
        this.nomOrphelinForSearch = nomOrphelinForSearch;
    }

    public String getNomMedicamentForSearch() {
        return nomMedicamentForSearch;
    }

    public void setNomMedicamentForSearch(String nomMedicamentForSearch) {
        this.nomMedicamentForSearch = nomMedicamentForSearch;
    }

    public String getDescriptionForSearch() {
        return descriptionForSearch;
    }

    public void setDescriptionForSearch(String descriptionForSearch) {
        this.descriptionForSearch = descriptionForSearch;
    }

    public Float getPrixMinForSearch() {
        return prixMinForSearch;
    }

    public void setPrixMinForSearch(Float prixMinForSearch) {
        this.prixMinForSearch = prixMinForSearch;
    }

    public Float getPrixMaxForSearch() {
        return prixMaxForSearch;
    }

    public void setPrixMaxForSearch(Float prixMaxForSearch) {
        this.prixMaxForSearch = prixMaxForSearch;
    }

    public Date getDatePriseMinForSearch() {
        return datePriseMinForSearch;
    }

    public void setDatePriseMinForSearch(Date datePriseMinForSearch) {
        this.datePriseMinForSearch = datePriseMinForSearch;
    }

    public Date getDatePriseMaxForSearch() {
        return datePriseMaxForSearch;
    }

    public void setDatePriseMaxForSearch(Date datePriseMaxForSearch) {
        this.datePriseMaxForSearch = datePriseMaxForSearch;
    }

    public MedicamentController() {
    }

    public Medicament getSelected() {
        return selected;
    }

    public void setSelected(Medicament selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MedicamentFacade getFacade() {
        return ejbFacade;
    }

    public Medicament prepareCreate() {
        selected = new Medicament();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MedicamentCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MedicamentUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MedicamentDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Medicament> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    getFacade().create(selected);
                } else if (persistAction == PersistAction.UPDATE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
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

    public Medicament getMedicament(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Medicament> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Medicament> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Medicament.class)
    public static class MedicamentControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MedicamentController controller = (MedicamentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "medicamentController");
            return controller.getMedicament(getKey(value));
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
            if (object instanceof Medicament) {
                Medicament o = (Medicament) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Medicament.class.getName()});
                return null;
            }
        }

    }

}
