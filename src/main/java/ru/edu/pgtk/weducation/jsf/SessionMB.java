package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import ru.edu.pgtk.weducation.ejb.AccountsEJB;
import ru.edu.pgtk.weducation.ejb.ClientSessionsEJB;
import ru.edu.pgtk.weducation.entity.Account;
import ru.edu.pgtk.weducation.entity.ClientSession;
import static ru.edu.pgtk.weducation.utils.Utils.addMessage;

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
  private transient ClientSessionsEJB sessions;

  /**
   * Иннициализация сессионного бина. В данном методе мы запишем в лог о начале
   * сеанса пользователя.
   */
  @PostConstruct
  private void startSession() {
    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
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

  public String doLogin() {
    try {
      user = usersEJB.get(login, password);
      if (null == user) {
        addMessage("Пользователь с такой комбинацией логина и пароля не обнаружен!");
        return null;
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
      // Если у пользователя есть стартовая страница, перейдем на неё
      String startPage = user.getStartPage();
      if ((startPage != null) && !(startPage.isEmpty())) {
        return startPage;
      }
      // Иначе, перенаправим в корень сайта
      return "/index";
    } catch (Exception e) {
      addMessage("Невозможно войти в систему с такой комбинацией логина и пароля!");
      return null;
    }
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
    return "/index";
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
