package controller;

import bean.Famille;
import bean.Orphelin;
import bean.Veuve;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.FamilleFacade;

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
    
    public void viderLaListe() {
        items = null;
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
        return orphelinFacade.findOrphelinByVeuve(getVeuve());
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
    
    public List<Famille> getItems() {
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