package controller;

import bean.Bonification;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.BonificationFacade;

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

@Named("bonificationController")
@SessionScoped
public class BonificationController implements Serializable {

    @EJB
    private service.BonificationFacade ejbFacade;
    private List<Bonification> items = null;
    private Bonification selected;
    private String nomOrphelinForSearch;
    private String nomBonificationForSearch;
    private String descriptionForSearch;
    private Date dateBonificationMinForSearch;
    private Date dateBonificationMaxForSearch;

    public void rechercheBonificationByQuery() {
        items = ejbFacade.findByQuery(nomOrphelinForSearch, nomBonificationForSearch, descriptionForSearch,
                dateBonificationMinForSearch, dateBonificationMaxForSearch);
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

    public String getNomBonificationForSearch() {
        return nomBonificationForSearch;
    }

    public void setNomBonificationForSearch(String nomBonificationForSearch) {
        this.nomBonificationForSearch = nomBonificationForSearch;
    }

    public String getDescriptionForSearch() {
        return descriptionForSearch;
    }

    public void setDescriptionForSearch(String descriptionForSearch) {
        this.descriptionForSearch = descriptionForSearch;
    }

    public Date getDateBonificationMinForSearch() {
        return dateBonificationMinForSearch;
    }

    public void setDateBonificationMinForSearch(Date dateBonificationMinForSearch) {
        this.dateBonificationMinForSearch = dateBonificationMinForSearch;
    }

    public Date getDateBonificationMaxForSearch() {
        return dateBonificationMaxForSearch;
    }

    public void setDateBonificationMaxForSearch(Date dateBonificationMaxForSearch) {
        this.dateBonificationMaxForSearch = dateBonificationMaxForSearch;
    }

    public BonificationController() {
    }

    public Bonification getSelected() {
        return selected;
    }

    public void setSelected(Bonification selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private BonificationFacade getFacade() {
        return ejbFacade;
    }

    public Bonification prepareCreate() {
        selected = new Bonification();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("BonificationCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("BonificationUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("BonificationDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Bonification> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
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

    public Bonification getBonification(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Bonification> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Bonification> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Bonification.class)
    public static class BonificationControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BonificationController controller = (BonificationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "bonificationController");
            return controller.getBonification(getKey(value));
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
            if (object instanceof Bonification) {
                Bonification o = (Bonification) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Bonification.class.getName()});
                return null;
            }
        }

    }

}
