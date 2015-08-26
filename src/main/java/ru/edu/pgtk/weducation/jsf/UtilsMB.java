package ru.edu.pgtk.weducation.jsf;

import javax.ejb.Stateless;
import javax.inject.Named;

/**
 * EJB для функций, используемых на различных страницах.
 *
 * @author Воронин Леонид
 */
@Stateless
@Named("utilsMB")
public class UtilsMB {

  /**
   * Возвращает класс для CSS в зависимости от значения логической переменной.
   *
   * Данный метод можно использовать чтобы сделать неактивные записи тусклыми.
   *
   * @param on логическая переменная или выражение, влияющее на активность.
   * @return имя стиля CSS в виде строки.
   */
  public String getRowOnOrOffClass(boolean on) {
    return on ? "enabled" : "disabled";
  }

  /**
   * Возвращает удобное для восприятия человеком логическое значение.
   *
   * @param value логическое значение
   * @return Строковое представление значения (да/нет)
   */
  public String getHumanReadableBool(boolean value) {
    return value ? "да" : "нет";
  }

}
