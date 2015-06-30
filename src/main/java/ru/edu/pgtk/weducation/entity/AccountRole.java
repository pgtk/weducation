package ru.edu.pgtk.weducation.entity;

import javax.ejb.EJBException;

/**
 * Перечисление ролей пользователей информационной системы.
 * @author Воронин Леонид
 */
public enum AccountRole {
  // Добавляйте роли в конец списка!
  ADMIN, DEPARTMENT, RECEPTION;

  private static final String[] descriptions = {
    "Администратор",
    "Отделение",
    "Приемная комиссия"
  // Добавляйте сюда описания для всех новых значений перечисления
  };
  
  public String getDescription() {
    try {
    return descriptions[this.ordinal()];
    } catch (IndexOutOfBoundsException e) {
      throw new EJBException("Количество значений перечисления не соответствует количеству описаний!");
    }
  }  
}
