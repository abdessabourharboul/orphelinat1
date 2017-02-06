package controller;

import bean.Maladie;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.MaladieFacade;

import java.io.Serializable;
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

@Named("maladieController")
@SessionScoped
public class MaladieController implements Serializable {

    @EJB
    private service.MaladieFacade ejbFacade;
    private List<Maladie> items = null;
    private Maladie selected;
    private String nomOrphelinForSearch;
    private String nomMaladieForSearch;
    private String descriptionForSearch;

    public List<String> getItemsAvailableSelectOneString(String nomVariable) {
        return getFacade().findByQueryString(nomVariable);
    }

    public void rechercheMaladieByQuery() {
        items = ejbFacade.findByQuery(nomOrphelinForSearch, nomMaladieForSearch, descriptionForSearch);
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

    public String getNomMaladieForSearch() {
        return nomMaladieForSearch;
    }

    public void setNomMaladieForSearch(String nomMaladieForSearch) {
        this.nomMaladieForSearch = nomMaladieForSearch;
    }

    public String getDescriptionForSearch() {
        return descriptionForSearch;
    }

    public void setDescriptionForSearch(String descriptionForSearch) {
        this.descriptionForSearch = descriptionForSearch;
    }

    public MaladieController() {
    }

    public Maladie getSelected() {
        return selected;
    }

    public void setSelected(Maladie selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MaladieFacade getFacade() {
        return ejbFacade;
    }

    public Maladie prepareCreate() {
        selected = new Maladie();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MaladieCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MaladieUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MaladieDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Maladie> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        System.out.println("Hani f la Methode getItems()");
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

    public Maladie getMaladie(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Maladie> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Maladie> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Maladie.class)
    public static class MaladieControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MaladieController controller = (MaladieController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "maladieController");
            return controller.getMaladie(getKey(value));
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
            if (object instanceof Maladie) {
                Maladie o = (Maladie) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Maladie.class.getName()});
                return null;
            }
        }

    }

}
