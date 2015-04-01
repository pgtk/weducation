package ru.edu.pgtk.weducation.entity;

import javax.ejb.EJBException;

/**
 * Перечисление, задающее изучаемый иностранный язык.
 * @author Воронин Леонид
 */
public enum ForeignLanguage {
  NO, EN, DE, FR;

  private static final String[] descriptions = {
    "не изучал(а)", "английский", "немецкий", "французский"};
  
  public String getDescription() throws EJBException {
    try {
    return descriptions[this.ordinal()];
    } catch (IndexOutOfBoundsException e) {
      throw new EJBException("КОличество значений перечисления не соответствует количеству описаний!");
    }
  }
}
