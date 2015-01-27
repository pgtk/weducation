package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.ejb.EJB;
import ru.edu.pgtk.weducation.ejb.SchoolsEJB;
import ru.edu.pgtk.weducation.entity.School;

public class SchoolsMB extends GenericBean<School> implements Serializable {

  @EJB
  private transient SchoolsEJB ejb;

  public void add() {
    item = new School();
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
      if ((null != item) && delete) {
        ejb.delete(item);
      }
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }
}
