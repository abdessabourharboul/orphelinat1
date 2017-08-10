package controller;

import bean.Scolarite;
import bean.User;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import controller.util.ScolariteUtil;
import controller.util.SessionUtil;
import service.ScolariteFacade;
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

@Named("scolariteController")
@SessionScoped
public class ScolariteController implements Serializable {

    @EJB
    private service.ScolariteFacade ejbFacade;
    private List<Scolarite> items = null;
    private Scolarite selected;
    private String nomOrphelinForSearch;
    private String etablissementForSearch;
    private String niveauScolaireForSearch;
    private String filiereForSearch;
    private String anneeScolaireFirstForSearch;
    private Float moyenne1ForSearch;
    private Float moyenne2ForSearch;
    private Float moyenneAnneeMinForSearch;
    private Float moyenneAnneeMaxForSearch;
    private Float moyenneAnneeExact;
    private Boolean resultatForSearch = null;
    private Boolean soutienScolaireForSearch = null;
    private String passwordForDelete;
    private String situationDeSearch;
    private String niveauScolaireDeSearch;
    private String typeOfItems;

    public String stringToIntToString(String a) {
        int foo = Integer.parseInt(a);
        foo++;
        String str = Integer.toString(foo);
        return str;
    }

    public String generalListenerTemplate(String type) {
        setTypeOfItems(type);
        setSituationDeSearch("");
        setNiveauScolaireDeSearch("");
        return "/scolarite/List?faces-redirect=true";
    }

    public String currentYearListenerTemplate(String type) {
        setTypeOfItems(type);
        setSituationDeSearch("");
        setNiveauScolaireDeSearch("");
        return "/scolarite/List?faces-redirect=true";
    }

    public String situationListenerTemplate(String situation, String type) {
        setTypeOfItems(type);
        setSituationDeSearch(situation);
        setNiveauScolaireDeSearch("");
        System.out.println("hahia " + getSituationDeSearch());
        return "/scolarite/List?faces-redirect=true";
    }

    public String niveauListenerTemplate(String niveau, String type) {
        setTypeOfItems(type);
        setNiveauScolaireDeSearch(niveau);
        setSituationDeSearch("");
        System.out.println("hahia " + getNiveauScolaireDeSearch());
        return "/scolarite/List?faces-redirect=true";
    }

    public void setSituationForPage(String situation) {
        setSituationDeSearch(situation);
        System.out.println("hahia " + getSituationDeSearch());
    }

    public void setNiveauScolaireForPage(String niveauScolaire) {
        setNiveauScolaireDeSearch(niveauScolaire);
    }

    public String passOrNot(Scolarite scolarite) {
        if (scolarite.getResultat() == null) {
            return "لم يحدد بعد";
        } else if (scolarite.getResultat().equals(true)) {
            return "انتقل";
        } else if (scolarite.getResultat().equals(false)) {
            return "كرر";
        }
        return "";
    }

    public boolean resultatBoolean(Scolarite scolarite) {
        if (scolarite.getResultat() == null) {
            return false;
        } else {
            return true;
        }
    }

    public String couleurResultatBoolean(Boolean res) {
        if (res != null) {
            if (res.equals(true)) {
                return "#13CD35";
            } else if (res.equals(false)) {
                return "#E76262";
            }
        }
        return "";
    }

    public void passButtonActionListener(Scolarite scolarite) {
        ejbFacade.passButtonActionListener(scolarite);
        items = null;
    }

    public void notPassButtonActionListener(Scolarite scolarite) {
        ejbFacade.notPassButtonActionListener(scolarite);
        items = null;
    }

    public void annulerDecision(Scolarite scolarite) {
        ejbFacade.annulerDecision(scolarite);
        items = null;
    }

    public List<String> getSilkScolaires() {
        return ejbFacade.getSilkScolaires();
    }

    public List<String> getNiveauxScolaires() {
        return ScolariteUtil.findNiveauxBySilk(getSelected().getSilkScolaire());
    }

    public List<String> niveauxScolairesList() {
        return ScolariteUtil.niveauxScolarite();
    }

    public void setAnneSecondaireAjax() {
        Integer foo = Integer.parseInt(getSelected().getAnneeScolaireFirst());
        String fooString;
        foo++;
        fooString = foo.toString();
        getSelected().setAnneeScolaireSecond(fooString);
    }

    public User getConnectedUser() {
        return SessionUtil.getConnectedUser();
    }

    public List<String> getItemsAvailableSelectOneString(String nomVariable) {
        return getFacade().findByQueryString(nomVariable);
    }

    public void rechercheScolariteByQuery() {
        items = ejbFacade.findByQuery(nomOrphelinForSearch, etablissementForSearch, getNiveauScolaireDeSearch(),
                filiereForSearch, moyenne1ForSearch, moyenne2ForSearch, moyenneAnneeExact,
                resultatForSearch, soutienScolaireForSearch, getSituationDeSearch(), anneeScolaireFirstForSearch);
    }

    public void nullerLaListe() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            if (items != null) {
                items = null;
            }
        }
    }

    public ScolariteController() {
    }

    public Scolarite getSelected() {
        return selected;
    }

    public void setSelected(Scolarite selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ScolariteFacade getFacade() {
        return ejbFacade;
    }

    public Scolarite prepareCreate() {
        selected = new Scolarite();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ScolariteCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ScolariteUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ScolariteDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Scolarite> getItemsByType() {
        switch (getTypeOfItems()) {
            case "general":
                return getItems();
            case "currentYear":
                return getItemsThisYear();
            case "situation":
                return getItemsBySituations();
            case "niveau":
                return getItemsByNiveaux();
            default:
                return getItems();
        }
    }

    public List<Scolarite> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<Scolarite> getItemsThisYear() {
        if (items == null) {
            items = getFacade().findScolariteThisYear();
        }
        return items;
    }

    public List<Scolarite> getItemsByNiveaux() {
        if (items == null) {
            items = getFacade().findScolariteByNiveauxThisYear(getNiveauScolaireDeSearch());
        }
        return items;
    }

    public List<Scolarite> getItemsBySituations() {
        if (items == null) {
            items = getFacade().findScolariteBySituationThisYear(getSituationDeSearch());
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction == PersistAction.CREATE) {
                    getFacade().create(selected);
                    items = null;
                    JsfUtil.addSuccessMessage(successMessage);
                } else if (persistAction == PersistAction.UPDATE) {
                    getFacade().edit(selected);
                    items = null;
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

    public Scolarite getScolarite(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Scolarite> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Scolarite> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Scolarite.class)
    public static class ScolariteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ScolariteController controller = (ScolariteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "scolariteController");
            return controller.getScolarite(getKey(value));
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
            if (object instanceof Scolarite) {
                Scolarite o = (Scolarite) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Scolarite.class.getName()});
                return null;
            }
        }

    }

    public Float getMoyenneAnneeExact() {
        return moyenneAnneeExact;
    }

    public void setMoyenneAnneeExact(Float moyenneAnneeExact) {
        this.moyenneAnneeExact = moyenneAnneeExact;
    }

    public String getNomOrphelinForSearch() {
        return nomOrphelinForSearch;
    }

    public void setNomOrphelinForSearch(String nomOrphelinForSearch) {
        this.nomOrphelinForSearch = nomOrphelinForSearch;
    }

    public String getEtablissementForSearch() {
        return etablissementForSearch;
    }

    public void setEtablissementForSearch(String etablissementForSearch) {
        this.etablissementForSearch = etablissementForSearch;
    }

    public String getNiveauScolaireForSearch() {
        return niveauScolaireForSearch;
    }

    public void setNiveauScolaireForSearch(String niveauScolaireForSearch) {
        this.niveauScolaireForSearch = niveauScolaireForSearch;
    }

    public String getFiliereForSearch() {
        return filiereForSearch;
    }

    public void setFiliereForSearch(String filiereForSearch) {
        this.filiereForSearch = filiereForSearch;
    }

    public Float getMoyenne1ForSearch() {
        return moyenne1ForSearch;
    }

    public void setMoyenne1ForSearch(Float moyenne1ForSearch) {
        this.moyenne1ForSearch = moyenne1ForSearch;
    }

    public Float getMoyenne2ForSearch() {
        return moyenne2ForSearch;
    }

    public void setMoyenne2ForSearch(Float moyenne2ForSearch) {
        this.moyenne2ForSearch = moyenne2ForSearch;
    }

    public Float getMoyenneAnneeMinForSearch() {
        return moyenneAnneeMinForSearch;
    }

    public void setMoyenneAnneeMinForSearch(Float moyenneAnneeMinForSearch) {
        this.moyenneAnneeMinForSearch = moyenneAnneeMinForSearch;
    }

    public Float getMoyenneAnneeMaxForSearch() {
        return moyenneAnneeMaxForSearch;
    }

    public void setMoyenneAnneeMaxForSearch(Float moyenneAnneeMaxForSearch) {
        this.moyenneAnneeMaxForSearch = moyenneAnneeMaxForSearch;
    }

    public Boolean getResultatForSearch() {
        return resultatForSearch;
    }

    public void setResultatForSearch(Boolean resultatForSearch) {
        this.resultatForSearch = resultatForSearch;
    }

    public Boolean getSoutienScolaireForSearch() {
        return soutienScolaireForSearch;
    }

    public void setSoutienScolaireForSearch(Boolean soutienScolaireForSearch) {
        this.soutienScolaireForSearch = soutienScolaireForSearch;
    }

    public String getPasswordForDelete() {
        return passwordForDelete;
    }

    public void setPasswordForDelete(String passwordForDelete) {
        this.passwordForDelete = passwordForDelete;
    }

    public String getSituationDeSearch() {
        return situationDeSearch;
    }

    public void setSituationDeSearch(String situationDeSearch) {
        this.situationDeSearch = situationDeSearch;
    }

    public String getNiveauScolaireDeSearch() {
        return niveauScolaireDeSearch;
    }

    public void setNiveauScolaireDeSearch(String niveauScolaireDeSearch) {
        this.niveauScolaireDeSearch = niveauScolaireDeSearch;
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

    public String getAnneeScolaireFirstForSearch() {
        return anneeScolaireFirstForSearch;
    }

    public void setAnneeScolaireFirstForSearch(String anneeScolaireFirstForSearch) {
        this.anneeScolaireFirstForSearch = anneeScolaireFirstForSearch;
    }

}
