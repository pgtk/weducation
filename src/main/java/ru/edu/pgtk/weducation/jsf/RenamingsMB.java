package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import ru.edu.pgtk.weducation.ejb.RenamingsEJB;
import ru.edu.pgtk.weducation.entity.Renaming;

/**
 * Управляемый бин для переименований
 *
 * @author Воронин Леонид
 */
@ManagedBean(name = "renamingsMB")
@ViewScoped
public class RenamingsMB extends GenericBean<Renaming> implements Serializable {

  long serialVersionUID = 0L;

  @Inject
  private transient RenamingsEJB ejb;

  @Override
  public void newItem() {
    item = new Renaming();
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
