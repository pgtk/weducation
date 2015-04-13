package ru.edu.pgtk.weducation.jsf.department;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.DepartmentsEJB;
import ru.edu.pgtk.weducation.entity.Account;
import ru.edu.pgtk.weducation.entity.Department;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

@ManagedBean(name = "departmentRootMB")
@ViewScoped
public class DepartmentRootMB {

  @ManagedProperty(value = "#{sessionMB.user}")
  private transient Account account;
  @EJB
  private transient DepartmentsEJB departments;
  private transient Department department;
  private boolean edit = false;

  @PostConstruct
  private void initBean() {
    if (null != account) {
      int depCode = account.getCode();
      if (depCode > 0) {
        try {
          department = departments.get(depCode);
        } catch (Exception e) {
          addMessage(e);
        }
      }
    }
  }

  public void toggleEdit() {
    try {
      if ((edit) && (null != department)) {
        departments.save(department);
      }
      edit = !edit;
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public String getEditLabel() {
    return edit ? "Сохранить" : "Редактировать";
  }

  public boolean isValid() {
    return department != null;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public boolean isEdit() {
    return edit;
  }

  public Department getDepartment() {
    return department;
  }
}
