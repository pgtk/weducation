package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.EJBException;
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
  private transient Account account;

  private void checkRestrictions() {
    if ((null == account) || (!account.isAdmin())) {
      throw new EJBException("У вас недостаточно полномочий для выполнения этой операции!");
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
    checkRestrictions();
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
    checkRestrictions();
    try {
      ejb.delete(item);
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public void changePassword() {
    checkRestrictions();
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

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }
}
