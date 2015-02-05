package ru.edu.pgtk.weducation.utils;

/**
 * Класс, реализующий семестр.
 *
 * @author Воронин Леонид
 */
public class Semester {
  private int number = 0;    // Номер семестра
  private int weeks = 0;     // количество недель

  public Semester() {
    super();
  }
  
  /**
   * Конструктор с параметрами
   * @param num номер семестра
   * @param start номер недели с которой начинается семестр
   * @param count количество недель в семестре
   */
  public Semester(int num, int count) {
    number = num;
    weeks = count;
  }
  
  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public int getWeeks() {
    return weeks;
  }

  public void setWeeks(int weeks) {
    this.weeks = weeks;
  }
  
  @Override
  public String toString() {
    return "Semester [number=" + number + ", weeks=" + weeks +"]";
  }
}
