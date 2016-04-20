package ru.edu.pgtk.weducation.data.entity;

import javax.ejb.EJBException;

/**
 * Форма контроля успеваемости (экхамен, зачет и т.п.).
 *
 * @author Воронин Леонид
 */
public enum ExamForm {

  NONE, EXAM, DIFZACHET, ZACHET, KOMPLEX, KVALIFEXAM, OTHER;

  public String getDescription() throws EJBException {
    switch (this) {
      case NONE:
        return "не предусмотрено";
      case EXAM:
        return "экзамен";
      case DIFZACHET:
        return "диф. зачет";
      case ZACHET:
        return "зачет";
      case KOMPLEX:
        return "комплексный экзамен";
      case KVALIFEXAM:
        return "квалификационный экзамен";
      case OTHER:
        return "другое";
    }
    throw new EJBException("Попытка вернуть описание для неизвестной формы контроля!");
  }
}
