package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.DepartmentsEJB;
import ru.edu.pgtk.weducation.entity.Department;

@ManagedBean(name = "departmentsMB")
@ViewScoped
public class DepartmentsMB extends GenericBean<Department> implements Serializable {

  @EJB
  private transient DepartmentsEJB ejb;

  public void add() {
    item = new Department();
    edit = true;
  }

  public void save() {
    try {
      ejb.save(item);
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public void confirmDelete() {
    try {
      if (delete && (item != null)) {
        ejb.delete(item);
      }
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }
}
