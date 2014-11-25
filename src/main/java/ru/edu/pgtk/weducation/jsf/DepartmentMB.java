package ru.edu.pgtk.weducation.jsf;

import java.io.Serializable;
import javax.ejb.EJB;
import ru.edu.pgtk.weducation.ejb.DepartmentEJB;
import ru.edu.pgtk.weducation.entity.Department;

/**
 * Управляемый бин для отделений
 *
 * @author Воронин Леонид
 */
public class DepartmentMB implements Serializable {

  @EJB
  DepartmentEJB ejb;
  private Department item;
  private boolean edit;
  private boolean error;
  private boolean delete;

  private void resetState() {
    edit = false;
    delete = false;
  }

  public boolean isBrowse() {
    return (!edit && !delete);
  }

  public boolean isEdit() {
    return edit;
  }

  public boolean isError() {
    return error;
  }

  public boolean isDelete() {
    return delete;
  }

  public Department getItem() {
    return item;
  }

  public void setItem(Department item) {
    this.item = item;
  }
  
  public void cancel() {
    resetState();
  }
  
  /**
   * Creates a new instance of DepartmentMB
   */
  public DepartmentMB() {
  }

}
