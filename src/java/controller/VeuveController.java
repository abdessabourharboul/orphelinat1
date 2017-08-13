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
    private String cinForSearch;
    private String nomFamilleForSearch;
    private String zoneGeographiqueForSearch;
    private String adresseForSearch;
    private String passwordForDelete;
    private Integer size = 0;
    private String situationDeSearch;
    private String typeOfItems;

    public String generalListenerTemplate() {
        setTypeOfItems("general");
        setSituationDeSearch("");
        return "/veuve/List?faces-redirect=true";
    }

    public String situationListenerTemplate(String situation) {
        setTypeOfItems("situation");
        setSituationDeSearch(situation);
        System.out.println("getSituationDeSearch: " + getSituationDeSearch());
        return "/veuve/List?faces-redirect=true";
    }

    public List<String> getItemsAvailableSelectOneStringNoms() {
        return getFacade().findNomForSearchBySituation(getSituationDeSearch());
    }

    public List<String> getItemsAvailableSelectOneStringNomVeuves() {
        return getFacade().findNomVeuveForSearchBySituation(getSituationDeSearch());
    }

    public void setSituationForPage(String situation) {
        setSituationDeSearch(situation);
        System.out.println("hahia situation de search ::::" + getSituationDeSearch());
    }

    public String getSituationDeSearch() {
        return situationDeSearch;
    }

    public void setSituationDeSearch(String situationDeSearch) {
        this.situationDeSearch = situationDeSearch;
    }

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

    public void uploadEdit(FileUploadEvent event) {
        if (getSelected().getPhoto() == null) {
            upload(event);
            size = 1;
        } else {
            getSelected().getAncienPhotos().add(getSelected().getPhoto());
            getSelected().setPhoto("Veuve-" + new Date().getTime() + ".png");
            ServerConfigUtil.upload(event.getFile(), ServerConfigUtil.getPhotoOrphelinPath(true), getSelected().getPhoto());
            size = 2;
        }
    }

    public void cancelEditOrphelin() {
        if (size == 1) {
            ServerConfigUtil.delete(ServerConfigUtil.getPhotoOrphelinPath(true), getSelected().getPhoto());
            getSelected().setPhoto(null);
        } else if (size == 2) {
            ServerConfigUtil.delete(ServerConfigUtil.getPhotoOrphelinPath(true), getSelected().getPhoto());
            getSelected().setPhoto(getSelected().getAncienPhotos().get(getSelected().getAncienPhotos().size() - 1));
            getSelected().getAncienPhotos().remove(getSelected().getAncienPhotos().size() - 1);
        }
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
        items = ejbFacade.findByQuery(nomVeuveForSearch, cinForSearch, nomFamilleForSearch,
                zoneGeographiqueForSearch, adresseForSearch, getSituationDeSearch());
        System.out.println(items);
    }

    public void nullerLaListe() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            items = null;
//            System.out.println("hanooo");
        }
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getNomVeuveForSearch() {
        return nomVeuveForSearch;
    }

    public void setNomVeuveForSearch(String nomVeuveForSearch) {
        this.nomVeuveForSearch = nomVeuveForSearch;
    }

    public String getCinForSearch() {
        return cinForSearch;
    }

    public void setCinForSearch(String cinForSearch) {
        this.cinForSearch = cinForSearch;
    }

    public String getNomFamilleForSearch() {
        return nomFamilleForSearch;
    }

    public void setNomFamilleForSearch(String nomFamilleForSearch) {
        this.nomFamilleForSearch = nomFamilleForSearch;
    }

    public String getZoneGeographiqueForSearch() {
        return zoneGeographiqueForSearch;
    }

    public void setZoneGeographiqueForSearch(String zoneGeographiqueForSearch) {
        this.zoneGeographiqueForSearch = zoneGeographiqueForSearch;
    }

    public String getAdresseForSearch() {
        return adresseForSearch;
    }

    public void setAdresseForSearch(String adresseForSearch) {
        this.adresseForSearch = adresseForSearch;
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

    public List<Veuve> getItemsBySituations() {
        if (items == null) {
            items = getFacade().findVeuveBySituation(getSituationDeSearch());
        }
        System.out.println("Hani f la Methode getItemsBySituations()");
        return items;
    }

    public List<Veuve> getItemsByType() {
        switch (getTypeOfItems()) {
            case "general":
                return getItems();
            case "situation":
                return getItemsBySituations();
            default:
                return getItems();
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
                    size = 0;
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

    public String getTypeOfItems() {
        if (typeOfItems == null) {
            typeOfItems = "";
        }
        return typeOfItems;
    }

    public void setTypeOfItems(String typeOfItems) {
        this.typeOfItems = typeOfItems;
    }

}
