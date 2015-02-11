package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import ru.edu.pgtk.weducation.ejb.SpecialitiesEJB;
import ru.edu.pgtk.weducation.entity.Speciality;

@ManagedBean(name = "specialitiesMB")
@ViewScoped
public class SpecialitiesMB extends GenericBean<Speciality> implements Serializable {

  @EJB
  private transient SpecialitiesEJB ejb;

  public void save() {
    try {
      ejb.save(item);
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public void add() {
    item = new Speciality();
    edit = true;
  }

  public void confirmDelete() {
    try {
      if (delete && (null != item)) {
        ejb.delete(item);
      }
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }
}
