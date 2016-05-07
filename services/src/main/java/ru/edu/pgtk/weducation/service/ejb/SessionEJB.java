package ru.edu.pgtk.weducation.service.ejb;

import ru.edu.pgtk.weducation.data.entity.Account;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * EJB компонент для управления сессией.
 * Основное назначение данного компонента - хранение информации о пользователе и о его роли.
 */
@Named("SessionEJB")
@SessionScoped
public class SessionEJB implements Serializable {

    private transient Account user;

    public boolean isAdmin() {
        return (user != null) && user.isAdmin();
    }

    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }

    public boolean isLogged() {
        return user != null;
    }
}
