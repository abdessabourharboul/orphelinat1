package controller;

import bean.User;
import bean.Veuve;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import controller.util.ServerConfigUtil;
import controller.util.SessionUtil;
import service.VeuveFacade;

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
import org.primefaces.event.FileUploadEvent;

@Named("veuveController")
@SessionScoped
public class VeuveController implements Serializable {

    @EJB
    private service.VeuveFacade ejbFacade;
    private List<Veuve> items = null;
    private Veuve selected;
    private String nomVeuveForSearch;
    private String metierVeuveForSearch;
    private String cinForSearch;
    private Long ageMinForSearch;
    private Long ageMaxForSearch;
    private Date dateNaissanceMinForSearch;
    private Date dateNaissanceMaxForSearch;
    private String passwordForDelete;

    public String getPasswordForDelete() {
        return passwordForDelete;
    }

    public void setPasswordForDelete(String passwordForDelete) {
        this.passwordForDelete = passwordForDelete;
    }

    public User getConnectedUser() {
        return SessionUtil.getConnectedUser();
    }

    public void upload(FileUploadEvent event) {
        getSelected().setPhoto("Veuve-" + new Date().getTime() + ".png");
        ServerConfigUtil.upload(event.getFile(), ServerConfigUtil.getPhotoOrphelinPath(true), getSelected().getPhoto());
    }

    public String findPath(Veuve veuve) {
        if (veuve != null) {
            if (veuve.getPhoto() != null) {
                System.out.println("ha b true" + ServerConfigUtil.getPhotoOrphelinPath(true) + "/" + veuve.getPhoto());
                System.out.println("ha b false" + ServerConfigUtil.getPhotoOrphelinPath(false) + "/" + veuve.getPhoto());
                return ServerConfigUtil.getPhotoOrphelinPath(false) + "/" + veuve.getPhoto();
            }
        }
        return ServerConfigUtil.getPhotoOrphelinPath(false) + "/noOne.png";
    }

    public List<String> getItemsAvailableSelectOneString(String nomVariable) {
        return getFacade().findByQueryString(nomVariable);
    }

    public void rechercheVeuveByQuery() {
        items = ejbFacade.findByQuery(nomVeuveForSearch, metierVeuveForSearch, cinForSearch, ageMinForSearch,
                ageMaxForSearch, dateNaissanceMinForSearch, dateNaissanceMaxForSearch);
        System.out.println(items);
    }

    public void nullerLaListe() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            items = null;
//            System.out.println("hanooo");
        }
    }

    public String getNomVeuveForSearch() {
        return nomVeuveForSearch;
    }

    public void setNomVeuveForSearch(String nomVeuveForSearch) {
        this.nomVeuveForSearch = nomVeuveForSearch;
    }

    public String getMetierVeuveForSearch() {
        return metierVeuveForSearch;
    }

    public void setMetierVeuveForSearch(String metierVeuveForSearch) {
        this.metierVeuveForSearch = metierVeuveForSearch;
    }

    public String getCinForSearch() {
        return cinForSearch;
    }

    public void setCinForSearch(String cinForSearch) {
        this.cinForSearch = cinForSearch;
    }

    public Long getAgeMinForSearch() {
        return ageMinForSearch;
    }

    public void setAgeMinForSearch(Long ageMinForSearch) {
        this.ageMinForSearch = ageMinForSearch;
    }

    public Long getAgeMaxForSearch() {
        return ageMaxForSearch;
    }

    public void setAgeMaxForSearch(Long ageMaxForSearch) {
        this.ageMaxForSearch = ageMaxForSearch;
    }

    public Date getDateNaissanceMinForSearch() {
        return dateNaissanceMinForSearch;
    }

    public void setDateNaissanceMinForSearch(Date dateNaissanceMinForSearch) {
        this.dateNaissanceMinForSearch = dateNaissanceMinForSearch;
    }

    public Date getDateNaissanceMaxForSearch() {
        return dateNaissanceMaxForSearch;
    }

    public void setDateNaissanceMaxForSearch(Date dateNaissanceMaxForSearch) {
        this.dateNaissanceMaxForSearch = dateNaissanceMaxForSearch;
    }

    public VeuveController() {
    }

    public Veuve getSelected() {
        return selected;
    }

    public void setSelected(Veuve selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private VeuveFacade getFacade() {
        return ejbFacade;
    }

    public Veuve prepareCreate() {
        selected = new Veuve();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("VeuveCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VeuveUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VeuveDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Veuve> getItems() {
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

    public Veuve getVeuve(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Veuve> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Veuve> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Veuve.class)
    public static class VeuveControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VeuveController controller = (VeuveController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "veuveController");
            return controller.getVeuve(getKey(value));
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
            if (object instanceof Veuve) {
                Veuve o = (Veuve) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Veuve.class.getName()});
                return null;
            }
        }

    }

}
