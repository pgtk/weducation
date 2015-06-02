package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.SchoolsEJB;
import ru.edu.pgtk.weducation.entity.School;

@ManagedBean(name = "schoolsMB")
@ViewScoped
public class SchoolsMB extends GenericBean<School> implements Serializable {

  @EJB
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
