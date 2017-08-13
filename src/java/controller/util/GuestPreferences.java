package controller.util;

import bean.User;
import java.io.Serializable;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
//import controller.util.Theme;

@ManagedBean(name = "guestPreferences")
@SessionScoped
public class GuestPreferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private service.UserFacade userFacade;
    private String theme = "cupertino";

    public String getTheme() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (params.containsKey("theme")) {
            theme = params.get("theme");
        }
        if (getConnectedUser() != null) {
            theme = getConnectedUser().getTheme();
        }
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public User getConnectedUser() {
        return SessionUtil.getConnectedUser();
    }

    public String changeTheme() {
        User user = getConnectedUser();
        user.setTheme(theme);
        userFacade.changeTheme(user);
        return "/general/general?faces-redirect=true";
    }

}
