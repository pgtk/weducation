package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ru.edu.pgtk.weducation.ejb.SpecialitiesEJB;
import ru.edu.pgtk.weducation.entity.Speciality;

@Named("specialitiesMB")
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
