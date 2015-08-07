package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ru.edu.pgtk.weducation.ejb.SchoolsEJB;
import ru.edu.pgtk.weducation.entity.School;

@Named("schoolsMB")
@ViewScoped
public class SchoolsMB extends GenericBean<School> implements Serializable {

  long serialVersionUID = 0L;

  @Inject
  private transient SchoolsEJB ejb;

  @Override
  public void newItem() {
    item = new School();
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
