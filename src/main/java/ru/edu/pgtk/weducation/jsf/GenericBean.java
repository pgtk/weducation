package ru.edu.pgtk.weducation.jsf;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import ru.edu.pgtk.weducation.entity.Account;

/**
 * Шаблон для управляемых бинов, реализующий 2 трети функционала.
 *
 * @author Воронин Леонид
 * @param <T> Класс, с которым работает управляемый бин.
 */
public abstract class GenericBean<T> {

  protected transient T item;
  protected boolean edit;
  protected boolean delete;
  protected boolean details;
  protected boolean error;
  @ManagedProperty(value = "#{sessionMB.user}")
  protected transient Account user;

  @PostConstruct
  private void checkAccount() {
    resetState();
    // Если пользователь неавторизован, то выдаем ошибку и запрещаем работу!
    if (null == user) {
      error = true;
    }
  }
  
  protected void resetState() {
    edit = false;
    delete = false;
    details = false;
    error = false;
    item = null;
  }

  protected void addMessage(final Exception e) {
    FacesContext context = FacesContext.getCurrentInstance();
    String message = "Exception class " + e.getClass().getName() + " with message " + e.getMessage();
    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "Error"));
  }

  protected void addMessage(final String message) {
    FacesContext context = FacesContext.getCurrentInstance();
    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, "Error"));
  }

  public Account getUser() {
    return user;
  }

  public void setUser(Account user) {
    this.user = user;
  }

  public T getItem() {
    return item;
  }

  public boolean isEdit() {
    return edit && !error;
  }

  public boolean isDelete() {
    return delete && !error;
  }

  public boolean isBrowse() {
    return !(error || edit || delete || details);
  }

  public boolean isDetails() {
    return details && !edit && !delete && !error;
  }

  public boolean isError() {
    return error;
  }

  public void cancelEdit() {
    edit = false;
  }
  
  public void cancelDelete() {
    delete = false;
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
    details = false;
  }
  
  public void switchEdit() {
    if (null != item) {
      edit = true;
    }
  }
  
  public void switchDelete() {
    if (null != item) {
      delete = true;
    }
  }

  public void details(final T item) {
    this.item = item;
    details = true;
  }
}
