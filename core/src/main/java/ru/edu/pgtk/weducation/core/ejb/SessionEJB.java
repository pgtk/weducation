package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Account;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * EJB компонент для управления сессией.
 * Основное назначение данного компонента - хранение информации о пользователе и о его роли.
 */
@Named("SessionEJB")
@SessionScoped
public class SessionEJB implements Serializable, SessionDAO {

    private transient Account user;

    @Override
    public boolean isAdmin() {
        return (user != null) && user.isAdmin();
    }

    @Override
    public Account getUser() {
        return user;
    }

    @Override
    public void setUser(Account user) {
        this.user = user;
    }

    @Override
    public boolean isLogged() {
        return user != null;
    }
}
