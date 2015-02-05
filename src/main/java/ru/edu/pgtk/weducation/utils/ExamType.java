package ru.edu.pgtk.weducation.utils;

import javax.ejb.EJBException;

/**
 * Форма контроля успеваемости (экхамен, зачет и т.п.).
 *
 * @author Воронин Леонид
 */
public enum ExamType {

  NONE, EXAM, ZDIF, KNTR, ZCHT, UNKN;

  public String getDescription() throws EJBException {
    switch (this) {
      case NONE:
        return "не предусмотрено никакой формы контроля";
      case EXAM:
        return "экзамен";
      case ZDIF:
        return "диференцированный зачет";
      case ZCHT:
        return "зачет";
      case KNTR:
        return "контрольная работа";
      case UNKN:
        return "другая форма контроля";
    }
    throw new EJBException("Попытка вернуть описание для неизвестной формы контроля!");
  }
}
