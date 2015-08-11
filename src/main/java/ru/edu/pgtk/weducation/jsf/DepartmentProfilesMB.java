package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ru.edu.pgtk.weducation.ejb.DepartmentProfilesEJB;
import ru.edu.pgtk.weducation.entity.DepartmentProfile;

@Named("departmentProfilesMB")
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
