package ru.edu.pgtk.weducation.ejb;

/**
 * Исключение при работе с данными.
 * @author Воронин Леонид
 */
public class DataException extends Exception {
  
  public DataException(final String message) {
    super(message);
  }
  
  public DataException(final Exception e) {
    super("Exception class " + e.getClass().getName() + " with message " + e.getMessage());
  }
}
