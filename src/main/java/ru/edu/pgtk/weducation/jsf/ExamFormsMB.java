package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.ejb.EJB;
import ru.edu.pgtk.weducation.ejb.ExamFormsEJB;
import ru.edu.pgtk.weducation.entity.ExamForm;

public class ExamFormsMB extends GenericBean<ExamForm> implements Serializable {

  @EJB
  private ExamFormsEJB ejb;

  public void add() {
    item = new ExamForm();
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
