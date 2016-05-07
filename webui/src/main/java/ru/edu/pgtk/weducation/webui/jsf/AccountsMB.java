package ru.edu.pgtk.weducation.webui.jsf;

import ru.edu.pgtk.weducation.data.entity.Account;
import ru.edu.pgtk.weducation.data.entity.AccountRole;
import ru.edu.pgtk.weducation.service.ejb.AccountsEJB;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

import static ru.edu.pgtk.weducation.webui.jsf.Utils.addMessage;

@Named("accountsMB")
@ViewScoped
public class AccountsMB extends GenericBean<Account> implements Serializable {

  long serialVersionUID = 0L;

  @EJB
  private AccountsEJB ejb;

  @PostConstruct
  private void checkRestrictions() {
    if ((null == user) || (!user.isAdmin())) {
      error = true;
    }
  }

  public AccountRole[] getRoles() {
    return AccountRole.values();
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

  @Override
  public void newItem() {
    checkRestrictions();
    item = new Account();
  }

  @Override
  public void deleteItem() {
    if ((null != item) && delete) {
      ejb.delete(item);
    }
  }

  @Override
  public void saveItem() {
    if (item.getId() == 0) {
      item.updatePassword();
    }
    ejb.save(item);
  }
}
