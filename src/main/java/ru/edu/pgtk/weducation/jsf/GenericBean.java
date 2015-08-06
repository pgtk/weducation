package ru.edu.pgtk.weducation.jsf;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import ru.edu.pgtk.weducation.entity.Account;
import static ru.edu.pgtk.weducation.jsf.Utils.addMessage;

/**
 * Шаблон для управляемых бинов, реализующий 2 трети функционала.
 *
 * @author Воронин Леонид
 * @param <T> Класс, с которым работает управляемый бин.
 */
public abstract class GenericBean<T> {

  @Inject
  protected transient Account user;
  protected transient T item;
  protected boolean edit;
  protected boolean delete;
  protected boolean details;
  protected boolean error;

  @PostConstruct
  private void checkAccount() {
    resetState();
    // Если пользователь неавторизован, то выдаем ошибку и запрещаем работу!
    if (null == user) {
      error = true;
    }
  }

  // Создание нового экземпляра 
  public abstract void newItem();

  // Удаление экземпляра
  public abstract void deleteItem();

  // Сохранение экземпляра
  public abstract void saveItem();

  protected void resetState() {
    edit = false;
    delete = false;
    details = false;
    error = false;
    item = null;
  }

  public void add() {
    try {
      newItem();
      edit = true;
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public void save() {
    try {
      saveItem();
      edit = false;
    } catch (Exception e) {
      addMessage(e);
    }
  }

  public void confirmDelete() {
    try {
      deleteItem();
      resetState();
    } catch (Exception e) {
      addMessage(e);
    }
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

  public Account getUser() {
    return user;
  }

  public void setUser(Account user) {
    this.user = user;
  }
}
