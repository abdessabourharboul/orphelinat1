/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.User;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import controller.util.JsfUtil;
import controller.util.SessionUtil;
import java.io.IOException;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author asus
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "Orphelinat1PU")
    private EntityManager em;

    public User getConnectedUser() {
        return SessionUtil.getConnectedUser();
    }

    public User tryGettingUser(User myUser) {
        String requette = "SELECT u FROM User u WHERE 1=1"
                + " AND u.id LIKE '" + myUser.getId() + "'"
                + " AND u.password LIKE '" + crypt(myUser.getPassword()) + "'";
        User loadedUser = null;
        try {
            loadedUser = (User) em.createQuery(requette).getSingleResult();
        } catch (Exception e) {
        }
        return loadedUser;
    }

    public String connexion(User myUser) {
        User loadedUser = tryGettingUser(myUser);
        if (loadedUser == null) {
            return "errorPage?faces-redirect=true";
        } else {
            SessionUtil.setAttribute("user", loadedUser);
            loadedUser.setLogged(true);
            super.edit(loadedUser);
            return "first?faces-redirect=true";
        }
    }

    public String crypt(String password) {
        return Hashing.sha256().hashString(password, Charsets.UTF_8).toString();
    }

    public void createUser(User selected) {
        selected.setPassword(crypt(selected.getPassword()));
        super.create(selected);
    }

    public String logOut() {
        User myUser = (User) SessionUtil.getAttribute("user");
        myUser.setLogged(false);
        super.edit(myUser);
        SessionUtil.getSession().invalidate();
        return "/index?faces-redirect=true";
    }

    public void checkAuthentication() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (getConnectedUser() != null) {
            externalContext.redirect(externalContext.getRequestContextPath() + "/faces/first.xhtml");
        }
    }

    public void checkAuthentication1() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if (getConnectedUser() == null) {
            externalContext.redirect(externalContext.getRequestContextPath() + "/faces/index.xhtml");
        }
    }

    public String changePassword(User user, String newPassword, String confNewPassword) {
        User myUser = getConnectedUser();
        if (myUser.getPassword().equals(crypt(user.getPassword())) && newPassword.equals(confNewPassword)) {
            myUser.setPassword(crypt(newPassword));
            super.edit(myUser);
            JsfUtil.addSuccessMessage("Votre mot de passe a été changé avec succès");
            return "/template?faces-redirect=true";
        } else {
            JsfUtil.addErrorMessage("Votre mot de passe n'a pas été changé");
            return "/user/ChangePassword?faces-redirect=true";
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }

}
