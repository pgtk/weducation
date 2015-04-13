package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpServletRequest;
import ru.edu.pgtk.weducation.ejb.AccountsEJB;
import ru.edu.pgtk.weducation.ejb.ClientSessionsEJB;
import ru.edu.pgtk.weducation.ejb.DepartmentsEJB;
import ru.edu.pgtk.weducation.entity.Account;
import ru.edu.pgtk.weducation.entity.AccountRole;
import ru.edu.pgtk.weducation.entity.ClientSession;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;
import static ru.edu.pgtk.weducation.jsf.Utils.getExternalContext;

/**
 * Сессионный бин, который будет обеспечивать сквозной функционал
 */
@ManagedBean(name = "sessionMB")
@SessionScoped
public class SessionMB implements Serializable {

  private transient Account user;
  private ClientSession session;
  private String login;
  private String password;
  @EJB
  private transient AccountsEJB usersEJB;
  @EJB
  private transient DepartmentsEJB departments;
  @EJB
  private transient ClientSessionsEJB sessions;

  /**
   * Иннициализация сессионного бина. В данном методе мы запишем в лог о начале
   * сеанса пользователя.
   */
  @PostConstruct
  private void startSession() {
    HttpServletRequest request = (HttpServletRequest) getExternalContext().getRequest();
    session = new ClientSession();
    session.setHostAddress(request.getRemoteHost());
    session.setCraetionTime(new Date());
    if (null != user) {
      session.setAccount(user);
    }
    sessions.save(session);
    // Добавить логирование!
  }

  /**
   * Завершение работы сессионного бина. В данном методе мы запишем в лог об
   * окончании сеанса пользователя.
   */
  @PreDestroy
  private void stopSession() {
    // TODO написать в лог, что сессия завершилась
    // Удаляем из базы данных сессию.
    if (session != null) {
      sessions.delete(session);
    }
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
    String result = "/index?faces-redirect=true";
    // Если пользователь неизвестен - то index!
    if (null == user) {
      return result;
    }
    if (user.getRole() == AccountRole.ADMIN) {
      // Если это учетная запись администратора
      return "/admin/index?faces-redirect=true";
    }
    if (user.getRole() == AccountRole.DEPARTMENT) {
      // Если это учетная запись отделения
      int depcode = user.getCode();
      if (depcode > 0) {
        Utils.setCookie("departmentId", ""+depcode);
        return "/department/index?faces-redirect=true";
      }
    }
    // По умолчанию всё равно index
    return result;
  }

  public String doLogin() {
    try {
      user = usersEJB.get(login, password);
      if (null == user) {
        addMessage("Пользователь с такой комбинацией логина и пароля не обнаружен!");
      }
      if (session != null) {
        try {
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
    return (user != null) && user.isAdmin();
  }

  public String doLogout() {
    user = null;
    login = null;
    password = null;
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

  public Account getUser() {
    return user;
  }

  public boolean isLogged() {
    return user != null;
  }
}
