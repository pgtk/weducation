package ru.edu.pgtk.weducation.core.ejb;

import ru.edu.pgtk.weducation.core.entity.Account;

import javax.ejb.Remote;

/**
 * Интерфейс сессионного компонента для авторизации
 * Created by leonid on 07.06.16.
 */
@Remote
public interface SessionDAO {

    boolean isAdmin();

    Account getUser();

    void setUser(Account user);

    boolean isLogged();
}
