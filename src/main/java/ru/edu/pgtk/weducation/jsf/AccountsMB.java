package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.AccountsEJB;
import ru.edu.pgtk.weducation.entity.Account;
import ru.edu.pgtk.weducation.entity.AccountRole;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@ViewScoped
@ManagedBean(name = "accountsMB")
public class AccountsMB extends GenericBean<Account> implements Serializable {

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
