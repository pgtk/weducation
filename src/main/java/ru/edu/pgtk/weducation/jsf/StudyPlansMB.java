package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.ejb.EJB;
import ru.edu.pgtk.weducation.ejb.StudyPlansEJB;
import ru.edu.pgtk.weducation.entity.StudyPlan;

public class StudyPlansMB extends GenericBean<StudyPlan> implements Serializable {

  @EJB
  private transient StudyPlansEJB ejb;
  
  public void add() {
    item = new StudyPlan();
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
      if (delete && (null != item)) {
        ejb.delete(item);
      }
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
  }
}
