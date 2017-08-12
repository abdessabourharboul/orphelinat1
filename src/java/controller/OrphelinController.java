package controller;

import bean.Famille;
import bean.Orphelin;
import bean.User;
import bean.Veuve;
import controller.util.DateUtil;
import controller.util.ImageClassUtil;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import controller.util.ServerConfigUtil;
import controller.util.SessionUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import service.OrphelinFacade;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.faces.event.PhaseId;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named("orphelinController")
@SessionScoped
public class OrphelinController implements Serializable {

    @EJB
    private service.OrphelinFacade ejbFacade;
    @EJB
    private service.VeuveFacade veuveFacade;
    private List<Orphelin> items = null;
    private Orphelin selected;
    private Integer size = 0;
    private String nomForSearch;
    private String prenomForSearch;
    private String sexeForSearch;
    private String codeMassarForSearch;
    private String descriptionForSearch;
    private Long anneeNaissanceMinForSearch;
    private Long anneeNaissanceMaxForSearch;
    private Long ageMinForSearch;
    private Long ageMaxForSearch;
    private String zoneGeographiqueForSearch;
    private String passwordForDelete;
    private Famille famille;
    private String situationDeSearch;
    private List<String> photos;
    private String typeOfItems;

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public List<Veuve> getItemsVeuvesAvailableSelectOne(Famille famille) {
        return veuveFacade.findVeuveByFamille(famille);
    }

    public Famille getFamille() {
        return famille;
    }

    public void setFamille(Famille famille) {
        this.famille = famille;
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

    public List<String> getItemsAvailableSelectOneStringNoms() {
        return getFacade().findNomForSearchBySituation(getSituationDeSearch());
    }

    public List<String> getItemsAvailableSelectOneStringPrenoms() {
        return getFacade().findPrenomForSearchBySituation(getSituationDeSearch());
    }

    public List<String> getItemsAvailableSelectOneString(String nomVariable) {
        return getFacade().findByQueryString(nomVariable);
    }

    public void rechercheOrphelinByQuery() {
        items = ejbFacade.findByQuery(nomForSearch, prenomForSearch, sexeForSearch,
                codeMassarForSearch, descriptionForSearch, anneeNaissanceMinForSearch,
                anneeNaissanceMaxForSearch, ageMinForSearch, ageMaxForSearch,
                zoneGeographiqueForSearch, getSituationDeSearch());
        System.out.println(items);
    }

    public void nullerLaListe() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            items = null;
//            System.out.println("hanooo");
        }
    }

    public void setSituationForPage(String situation) {
        setSituationDeSearch(situation);
        System.out.println("hahia situation de search ::::" + getSituationDeSearch());
    }

    public int calculAge(Orphelin orphelin) {
        return DateUtil.calculAge(orphelin.getDateNaissance());
    }

    public int getYear(Orphelin orphelin) {
        return DateUtil.getYear(orphelin.getDateNaissance());
    }

    public void upload(FileUploadEvent event) {
        getSelected().setPhoto("Orphelin-" + new Date().getTime() + ".png");
        ServerConfigUtil.upload(event.getFile(), ServerConfigUtil.getPhotoOrphelinPath(true), getSelected().getPhoto());
    }

    public void uploadEdit(FileUploadEvent event) {
        if (getSelected().getPhoto() == null) {
            upload(event);
            size = 1;
        } else {
            getSelected().getAncienPhotos().add(getSelected().getPhoto());
            getSelected().setPhoto("Orphelin-" + new Date().getTime() + ".png");
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

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String filename = context.getExternalContext().getRequestParameterMap().get("filename");
            return new DefaultStreamedContent(new FileInputStream(new File("/photos/photoOrphelin", filename)));
        }
    }

    public String findPath(Orphelin orphelin) {
        if (orphelin != null) {
            if (orphelin.getPhoto() != null) {
                System.out.println("ha b true" + ServerConfigUtil.getPhotoOrphelinPath(true) + "/" + orphelin.getPhoto());
                System.out.println("ha b false" + ServerConfigUtil.getPhotoOrphelinPath(false) + "/" + orphelin.getPhoto());
                return ServerConfigUtil.getPhotoOrphelinPath(false) + "/" + orphelin.getPhoto();
            }
        }
        return ServerConfigUtil.getPhotoOrphelinPath(false) + "/noOne.png";
    }

    public String findPathByString() {
        return ServerConfigUtil.getPhotoOrphelinPath(false);
    }

    public List<String> findPathByString2() {
        System.out.println("HAHIA STRIIIIIIIIING:" + getFacade().findPathByString(getSelected()));
//        System.out.println("HAHIA LWLA:" + getFacade().findPathByString(getSelected()).get(0));
//        System.out.println("HAHIA TANIA:" + getFacade().findPathByString(getSelected()).get(1));
        setPhotos(getFacade().findPathByString(getSelected()));
        return photos;
    }

    public List<ImageClassUtil> findPathByString1() {
//        System.out.println("HAHWA STRING:" + getFacade().findPathByString(getSelected()));
//        setPhotos(getFacade().findPathByString(getSelected()));
        System.out.println("ha liste des anciens" + getSelected().getAncienPhotos());
        List<ImageClassUtil> res = new ArrayList<>();
//        for (int i = 0; i < getSelected().getAncienPhotos().size(); i++) {
//            String get = getPhotos().get(i);
//            System.out.println("ha get:" + get);
//            ImageClassUtil icu = new ImageClassUtil();
//            icu.setNamePhoto(get);
//        }
//        System.out.println("ha RESSSSSS" + res);
        return res;
    }

    public String getSituationDeSearch() {
        return situationDeSearch;
    }

    public void setSituationDeSearch(String situationDeSearch) {
        this.situationDeSearch = situationDeSearch;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getNomForSearch() {
        return nomForSearch;
    }

    public void setNomForSearch(String nomForSearch) {
        this.nomForSearch = nomForSearch;
    }

    public String getPrenomForSearch() {
        return prenomForSearch;
    }

    public void setPrenomForSearch(String prenomForSearch) {
        this.prenomForSearch = prenomForSearch;
    }

    public String getSexeForSearch() {
        return sexeForSearch;
    }

    public void setSexeForSearch(String sexeForSearch) {
        this.sexeForSearch = sexeForSearch;
    }

    public String getCodeMassarForSearch() {
        return codeMassarForSearch;
    }

    public void setCodeMassarForSearch(String codeMassarForSearch) {
        this.codeMassarForSearch = codeMassarForSearch;
    }

    public String getDescriptionForSearch() {
        return descriptionForSearch;
    }

    public void setDescriptionForSearch(String descriptionForSearch) {
        this.descriptionForSearch = descriptionForSearch;
    }

    public Long getAnneeNaissanceMinForSearch() {
        return anneeNaissanceMinForSearch;
    }

    public void setAnneeNaissanceMinForSearch(Long anneeNaissanceMinForSearch) {
        this.anneeNaissanceMinForSearch = anneeNaissanceMinForSearch;
    }

    public Long getAnneeNaissanceMaxForSearch() {
        return anneeNaissanceMaxForSearch;
    }

    public void setAnneeNaissanceMaxForSearch(Long anneeNaissanceMaxForSearch) {
        this.anneeNaissanceMaxForSearch = anneeNaissanceMaxForSearch;
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

    public String getZoneGeographiqueForSearch() {
        return zoneGeographiqueForSearch;
    }

    public void setZoneGeographiqueForSearch(String zoneGeographiqueForSearch) {
        this.zoneGeographiqueForSearch = zoneGeographiqueForSearch;
    }

    public OrphelinController() {
    }

    public Orphelin getSelected() {
        return selected;
    }

    public void setSelected(Orphelin selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private OrphelinFacade getFacade() {
        return ejbFacade;
    }

    public Orphelin prepareCreate() {
        selected = new Orphelin();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("OrphelinCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("OrphelinUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("OrphelinDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Orphelin> getItemsByType() {
        switch (getTypeOfItems()) {
            case "general":
                return getItems();
            case "situation":
                return getItemsBySituations();
            default:
                return getItems();
        }
    }

    public List<Orphelin> getItemsBySituations() {
        if (items == null) {
            items = getFacade().findOrphelinBySituation(getSituationDeSearch());
        }
        System.out.println("Hani f la Methode getItemsBySituations()");
        return items;
    }

    public List<Orphelin> getItems() {
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
                    JsfUtil.addSuccessMessage(successMessage);
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

    public Orphelin getOrphelin(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Orphelin> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Orphelin> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Orphelin.class)
    public static class OrphelinControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            OrphelinController controller = (OrphelinController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "orphelinController");
            return controller.getOrphelin(getKey(value));
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
            if (object instanceof Orphelin) {
                Orphelin o = (Orphelin) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Orphelin.class.getName()});
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
