package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import ru.edu.pgtk.weducation.ejb.DepartmentProfilesEJB;
import ru.edu.pgtk.weducation.entity.DepartmentProfile;

@ManagedBean(name = "departmentProfilesMB")
@ViewScoped
public class DepartmentProfilesMB extends GenericBean<DepartmentProfile> implements Serializable {

  long serialVersionUID = 0L;

  @Inject
  private DepartmentProfilesEJB ejb;

  @Override
  public void newItem() {
    item = new DepartmentProfile();
  }

  @Override
  public void deleteItem() {
    if ((null != item) && delete) {
      ejb.delete(item);
    }
  }

  @Override
  public void saveItem() {
    ejb.save(item);
  }
}
