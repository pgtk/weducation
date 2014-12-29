package ru.edu.pgtk.weducation.jsf;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Шаблон для управляемых бинов, реализующий 2 трети функционала.
 * @author Воронин Леонид
 * @param <T> Класс, с которым работает управляемый бин.
 */
public abstract class GenericBean<T> {

  protected transient T item;
  protected boolean edit;
  protected boolean delete;

  @PostConstruct
  protected void resetState() {
    edit = false;
    delete = false;
    item = null;
  }

  protected void addMessage(final Exception e) {
    FacesContext context = FacesContext.getCurrentInstance();
    String message = "Exception class " + e.getClass().getName() + " with message " + e.getMessage();
    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "Error"));
  }

  public T getItem() {
    return item;
  }

  public boolean isEdit() {
    return edit;
  }

  public boolean isDelete() {
    return delete;
  }

  public boolean isBrowse() {
    return (!edit && !delete);
  }
  
  public void cancel() {
    resetState();
  }
  
  public void delete(final T item) {
    this.item = item;
    delete = true;
  }

  public void edit(final T item) {
    this.item = item;
    edit = true;
  }  
}
