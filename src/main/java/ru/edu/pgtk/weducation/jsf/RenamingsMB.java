package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.ejb.EJB;
import ru.edu.pgtk.weducation.ejb.RenamingsEJB;
import ru.edu.pgtk.weducation.entity.Renaming;

/**
 * Управляемый бин для переименований
 *
 * @author Воронин Леонид
 */
public class RenamingsMB extends GenericBean<Renaming> implements Serializable {

  @EJB
  private transient RenamingsEJB ejb;

  public void add() {
    item = new Renaming();
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
