package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.AccountsEJB;
import ru.edu.pgtk.weducation.entity.Account;
import ru.edu.pgtk.weducation.entity.AccountRole;

@ViewScoped
@ManagedBean(name = "accountsMB")
public class AccountsMB extends GenericBean<Account> implements Serializable {

  @EJB
  private AccountsEJB ejb;
  @ManagedProperty(value = "#{sessionMB.user}")
  private transient Account user;

  @PostConstruct
  private void checkRestrictions() {
    if ((null == user) || (!user.isAdmin())) {
      error = true;
    }
  }

  public AccountRole[] getRoles() {
    return AccountRole.values();
  }

  public void add() {
    checkRestrictions();
    item = new Account();
    edit = true;
  }

  public void save() {
    try {
      if (item.getId() == 0) {
        item.updatePassword();
      }
      ejb.save(item);
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public void confirmDelete() {
    try {
      ejb.delete(item);
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public void changePassword() {
    if (null != item) {
      try {
        item.updatePassword();
        ejb.save(item);
        resetState();
      } catch (Exception e) {
        addMessage(e);
      }
    }
  }

  public Account getUser() {
    return user;
  }

  public void setUser(Account user) {
    this.user = user;
  }
}
