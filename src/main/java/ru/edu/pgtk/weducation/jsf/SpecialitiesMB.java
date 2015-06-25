package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import ru.edu.pgtk.weducation.ejb.SpecialitiesEJB;
import ru.edu.pgtk.weducation.entity.Speciality;

@ManagedBean(name = "specialitiesMB")
@ViewScoped
public class SpecialitiesMB extends GenericBean<Speciality> implements Serializable {

  long serialVersionUID = 0L;

  @Inject
  private transient SpecialitiesEJB ejb;

  @Override
  public void newItem() {
    item = new Speciality();
  }

  @Override
  public void deleteItem() {
    if (delete && (null != item)) {
      ejb.delete(item);
    }
  }

  @Override
  public void saveItem() {
    ejb.save(item);
  }
}
