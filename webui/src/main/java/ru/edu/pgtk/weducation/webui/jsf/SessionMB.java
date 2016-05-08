package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.core.ejb.AccountsDAO;
import ru.edu.pgtk.weducation.core.ejb.ClientSessionsEJB;
import ru.edu.pgtk.weducation.core.ejb.SessionEJB;
import ru.edu.pgtk.weducation.core.entity.Account;
import ru.edu.pgtk.weducation.core.entity.AccountRole;
import ru.edu.pgtk.weducation.core.entity.ClientSession;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.PrePassivate;
import javax.ejb.StatefulTimeout;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;
import static ru.edu.pgtk.weducation.webui.jsf.Utils.getExternalContext;

/**
 * Сессионный бин, который будет обеспечивать сквозной функционал
 */
@Named("sessionMB")
@SessionScoped
@StatefulTimeout(unit = TimeUnit.MINUTES, value = 5)
public class SessionMB implements Serializable {

    long serialVersionUID = 0L;

    private ClientSession session;
    private String login;
    private String password;
    @Inject
    private transient AccountsDAO usersEJB;
    @Inject
    private transient SessionEJB ejbSession;
    @Inject
    private transient ClientSessionsEJB sessions;

    /**
     * Иннициализация сессионного бина. В данном методе мы запишем в лог о начале
     * сеанса пользователя.
     */
    @PostConstruct
    private void startSession() {
        HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();
        Account user = ejbSession != null ? ejbSession.getUser() : null;
        session = new ClientSession();
        session.setHostAddress(request.getRemoteHost());
        session.setCraetionTime(new Date());
        session.setAccount(user);
        sessions.save(session);
        // Добавить логирование!
        System.out.println("Session for " +
                ((null != user) ? user.getFullName() : "unlogged user") + " started.");
    }

    /**
     * Завершение работы сессионного бина. В данном методе мы запишем в лог об
     * окончании сеанса пользователя.
     */
    @PreDestroy
    private void stopSession() {
        Account user = ejbSession != null ? ejbSession.getUser() : null;
        System.out.println("Session for " +
                ((null != user) ? user.getFullName() : "unlogged user") + " passivated.");
        // Удаляем из базы данных сессию.
        if (session != null) {
            sessions.delete(session);
        }
        // На всякий случай обнулим пользователя
        if (null != user) {
            user = null;
        }
    }

    /**
     * Для отладки поведения компонентов этот метод временно добавлен.
     * <p>
     * Анализ журнала позволит понять как много экземпляров и сколько они живут.
     */
    @PrePassivate
    private void passivateSession() {
        Account user = ejbSession != null ? ejbSession.getUser() : null;
        System.out.println("Session for " +
                ((null != user) ? user.getFullName() : "unlogged user") + " stopped.");
    }

    /**
     * Возвращает стартовую страницу для любого класса пользователя. Для отделения
     * этот метод будет возвращаеть адрес стартовой страницы отделения. Для
     * админа, возможно адрес админки. Для незарегистрированного пользователя -
     * /index.
     *
     * @return Адрес стартовой страницы в виде строки.
     */
    public String startPage() {
        final String index = "/index?faces-redirect=true";
        Account user = ejbSession != null ? ejbSession.getUser() : null;
        // Если пользователь неизвестен - то index!
        if (null == user) {
            return index;
        }
        if (user.getRole() == AccountRole.ADMIN) {
            // Если это учетная запись администратора
            return "/admin" + index;
        }
        if (user.getRole() == AccountRole.RECEPTION) {
            // Если это учетная запись приемной комиссии
            return "/reception" + index;
        }
        if (user.getRole() == AccountRole.DEPARTMENT) {
            // Если это учетная запись отделения
            int depcode = user.getCode();
            if (depcode > 0) {
                Utils.setCookie("departmentId", "" + depcode);
                return "/department" + index;
            }
        }
        // По умолчанию всё равно index
        return index;
    }

    public String doLogin() {
        try {
            Account user = usersEJB.get(login, password);
            if (null == user) {
                addMessage("Пользователь с такой комбинацией логина и пароля не обнаружен!");
                return null;
            }
            if (session != null) {
                try {
                    ejbSession.setUser(user);
                    session.setAccount(user);
                    sessions.save(session);
                } catch (Exception e) {
                    addMessage(e);
                }
            }
            // TODO Добавить логирование!
            return startPage();
        } catch (Exception e) {
            addMessage("Невозможно войти в систему с такой комбинацией логина и пароля!");
        }
        return null;
    }

    public boolean isAdmin() {
        return ejbSession != null && ejbSession.isAdmin();
    }

    public String doLogout() {
        login = null;
        password = null;
        ejbSession.setUser(null);
        if (null != session) {
            session.setAccount(null);
            sessions.save(session);
        }
        return startPage();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Produces
    public Account getUser() {
        return ejbSession != null ? ejbSession.getUser() : null;
    }

    public boolean isLogged() {
        return ejbSession != null && ejbSession.isLogged();
    }
}
